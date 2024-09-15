package org.cyclops.energeticsheep.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.common.IShearable;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.cyclops.cyclopscore.helper.BlockEntityHelpers;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * @author rubensworks
 */
public class ItemEnergeticShearsNeoForge extends ItemEnergeticShearsCommon {
    public ItemEnergeticShearsNeoForge(Properties builder) {
        super(builder);
    }

    @Override
    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, @Nullable T entity, Consumer<Item> onBroken) {
        // Consume energy instead of damaging the item
        amount = super.damageItem(stack, amount, entity, onBroken);
        IEnergyStorage itemEnergy = getEnergyStorage(stack);
        if (itemEnergy != null) {
            itemEnergy.extractEnergy(amount * ItemEnergeticShearsConfigCommon.shearConsumption, false);
        }
        return 0;
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
        return itemStack.getCapability(Capabilities.EnergyStorage.ITEM);
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
        Optional<IEnergyStorage> energyCapability = Optional.ofNullable(entity.getCapability(Capabilities.EnergyStorage.ENTITY, null));
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
        if (maybeShearable instanceof IShearable shearable && shearable.isShearable(player, item, level, pos)) {
            return shearable.onSheared(player, item, level, pos);
        }
        return null;
    }

    public static InteractionResult transferEnergy(Player player, BlockPos pos, Direction side, InteractionHand hand) {
        Level worldIn = player.level();
        if (!player.isCrouching()) {
            return BlockEntityHelpers.getCapability(worldIn, pos, side, Capabilities.EnergyStorage.BLOCK)
                    .map(energyTarget -> {
                        ItemStack itemStack = player.getItemInHand(hand);
                        return Optional.ofNullable(itemStack.getCapability(Capabilities.EnergyStorage.ITEM))
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
}
