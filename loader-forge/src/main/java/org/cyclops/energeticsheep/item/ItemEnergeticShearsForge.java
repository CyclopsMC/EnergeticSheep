package org.cyclops.energeticsheep.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.IForgeShearable;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import org.cyclops.cyclopscore.RegistryEntriesCommon;
import org.cyclops.cyclopscore.helper.IModHelpersForge;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

/**
 * @author rubensworks
 */
public class ItemEnergeticShearsForge extends ItemEnergeticShearsCommon {
    public ItemEnergeticShearsForge(Properties builder) {
        super(builder);
    }

    @Override
    public boolean mineBlock(ItemStack itemStack, Level worldIn, BlockState state, BlockPos pos, LivingEntity entityLiving) {
        if (!worldIn.isClientSide) {
            consumeOnShear(itemStack, null, null);
        }

        Block block = state.getBlock();
        return !state.is(BlockTags.LEAVES) && block != Blocks.COBWEB && block != Blocks.GRASS_BLOCK && block != Blocks.FERN && block != Blocks.DEAD_BUSH && block != Blocks.VINE && block != Blocks.TRIPWIRE && !state.is(BlockTags.WOOL) ? super.mineBlock(itemStack, worldIn, state, pos, entityLiving) : true;
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        InteractionResult result = transferEnergy(context.getPlayer(), context.getClickedPos(), context.getClickedFace(), context.getHand());
        if (result == null) {
            return super.onItemUseFirst(stack, context);
        }
        return result;
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return oldStack.getItem() != newStack.getItem();
    }

    @Nullable
    protected IEnergyStorage getEnergyStorage(ItemStack itemStack) {
        return getEnergyStorageInternal(itemStack);
    }

    protected static IEnergyStorage getEnergyStorageInternal(ItemStack itemStack) {
        // TODO: restore when item caps are restored in Forge
//        return itemStack.getCapability(Capabilities.EnergyStorage.ITEM);
        return new ComponentEnergyStorage(itemStack, RegistryEntriesCommon.COMPONENT_ENERGY_STORAGE.value(), ItemEnergeticShearsConfigCommon.capacity);
    }

    @Override
    public int getEnergyStored(ItemStack itemStack) {
        IEnergyStorage energyStorage = getEnergyStorage(itemStack);
        return energyStorage != null ? energyStorage.getEnergyStored() : 0;
    }

    @Override
    public int getMaxEnergyStored(ItemStack itemStack) {
        IEnergyStorage energyStorage = getEnergyStorage(itemStack);
        return energyStorage != null ? energyStorage.getMaxEnergyStored() : 0;
    }

    @Override
    public void consumeEnergy(ItemStack itemStack, int amount, Player player, InteractionHand hand) {
        IEnergyStorage energyStorage = getEnergyStorage(itemStack);
        if (energyStorage != null) {
            energyStorage.extractEnergy(amount, false);
        }
    }

    @Override
    protected int moveEnergyFromEntityToItem(LivingEntity entity, ItemStack itemStack, int usageTransferAmount, Player player, InteractionHand hand) {
        LazyOptional<IEnergyStorage> energyCapability = entity.getCapability(ForgeCapabilities.ENERGY, null);
        if (energyCapability.isPresent()) {
            IEnergyStorage entityEnergy = energyCapability.orElse(null);
            IEnergyStorage itemEnergy = getEnergyStorage(itemStack);
            return entityEnergy.extractEnergy(
                    itemEnergy.receiveEnergy(
                            entityEnergy.extractEnergy(usageTransferAmount, true),
                            false),
                    false);
        }
        return 0;
    }

    @Nullable
    @Override
    protected List<ItemStack> getShearableDrops(Object maybeShearable, @Nullable Player player, ItemStack item, Level level, BlockPos pos) {
        if (maybeShearable instanceof IForgeShearable shearable && shearable.isShearable(item, level, pos)) {
            return shearable.onSheared(player, item, level, pos, 0);
        }
        return null;
    }

    public static InteractionResult transferEnergy(Player player, BlockPos pos, Direction side, InteractionHand hand) {
        Level worldIn = player.level();
        if (!player.isCrouching()) {
            return IModHelpersForge.get().getCapabilityHelpers().getCapability(worldIn, pos, side, ForgeCapabilities.ENERGY)
                    .map(energyTarget -> {
                        ItemStack itemStack = player.getItemInHand(hand);
                        return Optional.of(getEnergyStorageInternal(itemStack))
                                .map(energyItem -> energyTarget.receiveEnergy(
                                        energyItem.extractEnergy(
                                                energyTarget.receiveEnergy(
                                                        energyItem.extractEnergy(ItemEnergeticShearsConfigCommon.usageTransferAmount, true),
                                                        true),
                                                worldIn.isClientSide),
                                        worldIn.isClientSide) > 0 ? InteractionResult.SUCCESS : InteractionResult.FAIL)
                                .orElse(null);
                    })
                    .orElse(null);
        }
        return null;
    }

    // TODO: restore when item caps are restored in Forge
//    @Override
//    public ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag nbt) {
//        return new DefaultCapabilityProvider<>(() -> ForgeCapabilities.ENERGY,
//                new EnergyStorageItem(ItemEnergeticShearsConfigCommon.capacity, stack));
//    }
}
