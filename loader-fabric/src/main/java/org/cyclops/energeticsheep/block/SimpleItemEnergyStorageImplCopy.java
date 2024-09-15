package org.cyclops.energeticsheep.block;

import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.world.item.ItemStack;
import team.reborn.energy.api.EnergyStorage;
import team.reborn.energy.api.base.SimpleEnergyItem;

/**
 * A copy of Fabric's SimpleItemEnergyStorageImpl, but with public constructor
 * @author rubensworks
 */
public class SimpleItemEnergyStorageImplCopy implements EnergyStorage {

    private final ContainerItemContext ctx;
    private final long capacity;
    private final long maxInsert, maxExtract;

    public SimpleItemEnergyStorageImplCopy(ContainerItemContext ctx, long capacity, long maxInsert, long maxExtract) {
        this.ctx = ctx;
        this.capacity = capacity;
        this.maxInsert = maxInsert;
        this.maxExtract = maxExtract;
    }

    /**
     * Try to set the energy of the stack to {@code energyAmountPerCount}, return true if success.
     */
    private boolean trySetEnergy(long energyAmountPerCount, long count, TransactionContext transaction) {
        ItemStack newStack = ctx.getItemVariant().toStack();
        SimpleEnergyItem.setStoredEnergyUnchecked(newStack, energyAmountPerCount);
        ItemVariant newVariant = ItemVariant.of(newStack);

        // Try to convert exactly `count` items.
        try (Transaction nested = transaction.openNested()) {
            if (ctx.extract(ctx.getItemVariant(), count, nested) == count && ctx.insert(newVariant, count, nested) == count) {
                nested.commit();
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean supportsInsertion() {
        return maxInsert > 0;
    }

    @Override
    public long insert(long maxAmount, TransactionContext transaction) {
        long count = ctx.getAmount();

        long maxAmountPerCount = maxAmount / count;
        long currentAmountPerCount = getAmount() / count;
        long insertedPerCount = Math.min(maxInsert, Math.min(maxAmountPerCount, capacity - currentAmountPerCount));

        if (insertedPerCount > 0) {
            if (trySetEnergy(currentAmountPerCount + insertedPerCount, count, transaction)) {
                return insertedPerCount * count;
            }
        }

        return 0;
    }

    @Override
    public boolean supportsExtraction() {
        return maxExtract > 0;
    }

    @Override
    public long extract(long maxAmount, TransactionContext transaction) {
        long count = ctx.getAmount();

        long maxAmountPerCount = maxAmount / count;
        long currentAmountPerCount = getAmount() / count;
        long extractedPerCount = Math.min(maxExtract, Math.min(maxAmountPerCount, currentAmountPerCount));

        if (extractedPerCount > 0) {
            if (trySetEnergy(currentAmountPerCount - extractedPerCount, count, transaction)) {
                return extractedPerCount * count;
            }
        }

        return 0;
    }

    @Override
    public long getAmount() {
        return ctx.getAmount() * SimpleEnergyItem.getStoredEnergyUnchecked(ctx.getItemVariant().getComponents());
    }

    @Override
    public long getCapacity() {
        return ctx.getAmount() * capacity;
    }

}
