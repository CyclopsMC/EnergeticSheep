package org.cyclops.energeticsheep.block;

import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.world.item.ItemStack;

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

        transaction.addCloseCallback((transactionInner, result) -> {
            if (result.wasCommitted()) {
                ItemStack itemStack = ctx.getItemVariant().toStack();
                itemStack.shrink(1);
                try (Transaction nested = transactionInner.openNested()) {
                    ctx.extract(ItemVariant.of(ctx.getItemVariant().toStack()), 1, nested);
                    nested.commit();
                }
            }
        });
        return super.extract(maxAmount, transaction);
    }
}
