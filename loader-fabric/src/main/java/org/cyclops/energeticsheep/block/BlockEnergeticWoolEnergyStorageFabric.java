package org.cyclops.energeticsheep.block;

import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;

/**
 * @author rubensworks
 */
public class BlockEnergeticWoolEnergyStorageFabric extends SimpleItemEnergyStorageImplCopy {

    private final ContainerItemContext ctx;

    public BlockEnergeticWoolEnergyStorageFabric(ContainerItemContext ctx, int capacity) {
        super(ctx, capacity, 0, capacity);
        this.ctx = ctx;
    }

    @Override
    public long getAmount() {
        return getCapacity();
    }

    @Override
    public long extract(long maxAmount, TransactionContext transaction) {
        if (maxAmount < getCapacity()) {
            return 0;
        }

        long extracted = super.extract(maxAmount, transaction);
        if (extracted > 0) {
            ctx.extract(ItemVariant.of(ctx.getItemVariant().toStack()), 1, transaction);
        }
        return extracted;
    }
}
