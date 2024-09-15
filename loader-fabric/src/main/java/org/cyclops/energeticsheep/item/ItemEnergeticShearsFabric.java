package org.cyclops.energeticsheep.item;

import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Shearable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.cyclops.energeticsheep.EnergeticSheepFabric;
import team.reborn.energy.api.EnergyStorage;
import team.reborn.energy.api.EnergyStorageUtil;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author rubensworks
 */
public class ItemEnergeticShearsFabric extends ItemEnergeticShearsCommon {
    public ItemEnergeticShearsFabric(Properties builder) {
        super(builder);
    }

    @Override
    protected String getEnergyUnitUnlocalized() {
        return "general.energeticsheep.energy_unit_fabric";
    }

    @Override
    public boolean mineBlock(ItemStack itemStack, Level worldIn, BlockState state, BlockPos pos, LivingEntity entityLiving) {
        if (!worldIn.isClientSide) {
            if (entityLiving instanceof Player player) {
                consumeOnShear(itemStack, player, player.getUsedItemHand());
            }
        }

        Block block = state.getBlock();
        return !state.is(BlockTags.LEAVES) && block != Blocks.COBWEB && block != Blocks.GRASS_BLOCK && block != Blocks.FERN && block != Blocks.DEAD_BUSH && block != Blocks.VINE && block != Blocks.TRIPWIRE && !state.is(BlockTags.WOOL) ? super.mineBlock(itemStack, worldIn, state, pos, entityLiving) : true;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        InteractionResult result = ItemEnergeticShearsFabric.transferEnergy(context.getPlayer(), context.getClickedPos(), context.getClickedFace(), context.getHand());
        if (result == null) {
            return super.useOn(context);
        }
        return result;
    }

    @Override
    public boolean allowComponentsUpdateAnimation(Player player, InteractionHand hand, ItemStack oldStack, ItemStack newStack) {
        return oldStack.getItem() != newStack.getItem();
    }

    @Override
    public int getEnergyStored(ItemStack itemStack) {
        return (int) EnergyStorage.ITEM.find(itemStack, ContainerItemContext.withConstant(itemStack)).getAmount();
    }

    @Override
    public int getMaxEnergyStored(ItemStack itemStack) {
        return (int) EnergyStorage.ITEM.find(itemStack, ContainerItemContext.withConstant(itemStack)).getCapacity();
    }

    @Override
    public void consumeEnergy(ItemStack itemStack, int amount, Player player, InteractionHand hand) {
        EnergyStorage energyStorage = EnergyStorage.ITEM.find(itemStack, ContainerItemContext.ofPlayerHand(player, hand));
        if (energyStorage != null) {
            try (Transaction transaction = Transaction.openOuter()) {
                energyStorage.extract(amount, transaction);
                transaction.commit();
            }
        }
    }

    @Override
    protected int moveEnergyFromEntityToItem(LivingEntity entity, ItemStack itemStack, int usageTransferAmount, Player player, InteractionHand hand) {
        try (Transaction transaction = Transaction.openOuter()) {
            EnergyStorage source = EnergeticSheepFabric.ENERGY_STORAGE_ENTITY.find(entity, null);
            EnergyStorage target = EnergyStorage.ITEM.find(player.getItemInHand(hand), ContainerItemContext.ofPlayerHand(player, hand));
            if (source != null && target != null) {
                long moved = EnergyStorageUtil.move(source, target, usageTransferAmount, transaction);
                transaction.commit();
                return (int) moved;
            }
        }
        return 0;
    }

    @Nullable
    @Override
    protected List<ItemStack> getShearableDrops(Object maybeShearable, @Nullable Player player, ItemStack item, Level level, BlockPos pos) {
        if (maybeShearable instanceof Shearable shearable) {
            shearable.shear(SoundSource.PLAYERS);
            return new ArrayList<>();
        }
        return null;
    }

    public static InteractionResult transferEnergy(Player player, BlockPos pos, Direction side, InteractionHand hand) {
        Level worldIn = player.level();
        try (Transaction transaction = Transaction.openOuter()) {
            EnergyStorage source = EnergyStorage.ITEM.find(player.getItemInHand(hand), ContainerItemContext.ofPlayerHand(player, hand));
            EnergyStorage target = EnergyStorage.SIDED.find(worldIn, pos, side);
            if (target != null) {
                long moved = EnergyStorageUtil.move(source, target, ItemEnergeticShearsConfigCommon.usageTransferAmount, transaction);
                if (!worldIn.isClientSide) {
                    transaction.commit();
                }
                return moved > 0 ? InteractionResult.SUCCESS :InteractionResult.FAIL;
            }
        }
        return null;
    }
}
