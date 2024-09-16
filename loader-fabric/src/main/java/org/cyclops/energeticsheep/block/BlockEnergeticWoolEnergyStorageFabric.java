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
        // Make sure we can only extract a single item at a time
        long capacitySingular = getCapacity() / ctx.getAmount();
        if (maxAmount < capacitySingular) {
            return 0;
        }
        maxAmount = capacitySingular;

        // Do actual extraction
        // We don't call super, since we don't want to modify the data component, but only reduce the stack size
        long extracted = maxAmount;
        if (extracted > 0) {
            ctx.extract(ItemVariant.of(ctx.getItemVariant().toStack()), extracted / capacitySingular, transaction);
        }
        return extracted;
    }
}
