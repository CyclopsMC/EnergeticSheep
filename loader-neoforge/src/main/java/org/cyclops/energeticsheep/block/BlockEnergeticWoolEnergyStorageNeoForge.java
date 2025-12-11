package org.cyclops.energeticsheep.block;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.transfer.transaction.SnapshotJournal;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

/**
 * @author rubensworks
 */
public class BlockEnergeticWoolEnergyStorageNeoForge implements EnergyHandler {

    private final int capacity;
    private final ItemStack itemStack;
    private final Journal journal;

    public BlockEnergeticWoolEnergyStorageNeoForge(int capacity, ItemStack itemStack) {
        this.capacity = capacity;
        this.itemStack = itemStack;
        this.journal = new Journal();
    }

    @Override
    public long getAmountAsLong() {
        return this.capacity;
    }

    @Override
    public long getCapacityAsLong() {
        return this.capacity;
    }

    @Override
    public int insert(int amount, TransactionContext transaction) {
        return 0;
    }

    @Override
    public int extract(int amount, TransactionContext transaction) {
        if (amount >= this.capacity) {
            this.journal.updateSnapshots(transaction);
            this.itemStack.shrink(1);
            return this.capacity;
        }
        return 0;
    }

    public class Journal extends SnapshotJournal<Integer> {

        @Override
        protected Integer createSnapshot() {
            return itemStack.getCount();
        }

        @Override
        protected void revertToSnapshot(Integer snapshot) {
            itemStack.setCount(snapshot);
        }
    }
}
