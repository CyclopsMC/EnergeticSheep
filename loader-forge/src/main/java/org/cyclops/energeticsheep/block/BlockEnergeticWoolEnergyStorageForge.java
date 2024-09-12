package org.cyclops.energeticsheep.block;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.energy.IEnergyStorage;

/**
 * @author rubensworks
 */
public class BlockEnergeticWoolEnergyStorageForge implements IEnergyStorage {

    private final int capacity;
    private final ItemStack itemStack;

    public BlockEnergeticWoolEnergyStorageForge(int capacity, ItemStack itemStack) {
        this.capacity = capacity;
        this.itemStack = itemStack;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        return 0;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        if (maxExtract >= this.capacity) {
            if (!simulate) {
                this.itemStack.shrink(1);
            }
            return this.capacity;
        }
        return 0;
    }

    @Override
    public int getEnergyStored() {
        return this.capacity;
    }

    @Override
    public int getMaxEnergyStored() {
        return this.capacity;
    }

    @Override
    public boolean canExtract() {
        return true;
    }

    @Override
    public boolean canReceive() {
        return false;
    }
}
