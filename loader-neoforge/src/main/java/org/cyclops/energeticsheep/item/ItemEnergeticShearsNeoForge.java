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
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.transfer.energy.EnergyHandlerUtil;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.cyclops.cyclopscore.helper.IModHelpersNeoForge;

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
        EnergyHandler itemEnergy = getEnergyStorage(stack);
        if (itemEnergy != null) {
            try (var tx = Transaction.openRoot()) {
                itemEnergy.extract(amount * ItemEnergeticShearsConfigCommon.shearConsumption, tx);
                tx.commit();
            }
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
    protected EnergyHandler getEnergyStorage(ItemStack itemStack) {
        return itemStack.getCapability(Capabilities.Energy.ITEM, ItemAccess.forStack(itemStack));
    }

    @Override
    public void setEnergyStored(ItemStack itemStack, int energy, Player player, InteractionHand hand) {
        EnergyHandler energyStorage = getEnergyStorage(itemStack);
        if (energyStorage != null) {
            try (var tx = Transaction.openRoot()) {
                energyStorage.insert(energy, tx);
                tx.commit();
            }
        }
    }

    @Override
    public int getEnergyStored(ItemStack itemStack) {
        EnergyHandler energyStorage = getEnergyStorage(itemStack);
        return energyStorage != null ? energyStorage.getAmountAsInt() : 0;
    }

    @Override
    public int getMaxEnergyStored(ItemStack itemStack) {
        EnergyHandler energyStorage = getEnergyStorage(itemStack);
        return energyStorage != null ? energyStorage.getCapacityAsInt() : 0;
    }

    @Override
    public void consumeEnergy(ItemStack itemStack, int amount, Player player, InteractionHand hand) {
        EnergyHandler energyStorage = getEnergyStorage(itemStack);
        if (energyStorage != null) {
            try (var tx = Transaction.openRoot()) {
                energyStorage.extract(amount, tx);
                tx.commit();
            }
        }
    }

    @Override
    protected int moveEnergyFromEntityToItem(LivingEntity entity, ItemStack itemStack, int usageTransferAmount, Player player, InteractionHand hand) {
        Optional<EnergyHandler> energyCapability = Optional.ofNullable(entity.getCapability(Capabilities.Energy.ENTITY, null));
        if (energyCapability.isPresent()) {
            EnergyHandler entityEnergy = energyCapability.orElse(null);
            EnergyHandler itemEnergy = getEnergyStorage(itemStack);
            return EnergyHandlerUtil.move(entityEnergy, itemEnergy, usageTransferAmount, null);
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
            return IModHelpersNeoForge.get().getCapabilityHelpers().getCapability(worldIn, pos, side, Capabilities.Energy.BLOCK)
                    .map(energyTarget -> {
                        ItemAccess itemAccess = ItemAccess.forPlayerInteraction(player, hand);
                        return Optional.ofNullable(itemAccess.getCapability(Capabilities.Energy.ITEM))
                                .map(energyItem -> EnergyHandlerUtil.move(energyItem, energyTarget, ItemEnergeticShearsConfigCommon.usageTransferAmount, null) > 0 ? InteractionResult.SUCCESS : InteractionResult.FAIL)
                                .orElse(null);
                    })
                    .orElse(null);
        }
        return null;
    }
}
