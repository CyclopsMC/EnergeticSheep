package org.cyclops.energeticsheep.capability.energystorage;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.energy.IEnergyStorage;

/**
 * {@link IEnergyStorage} implementation for items.
 * @author rubensworks
 */
public class EnergyStorageItem implements IEnergyStorage {

    private final int capacity;
    private final ItemStack itemStack;

    public EnergyStorageItem(int capacity, ItemStack itemStack) {
        this.capacity = capacity;
        this.itemStack = itemStack;
    }

    @Override
    public int getEnergyStored() {
        CompoundNBT tag = itemStack.getTag();
        return tag == null ? 0 : tag.getInt("energy");
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
        return true;
    }

    @Override
    public int receiveEnergy(int energy, boolean simulate) {
        int stored = getEnergyStored();
        int energyReceived = Math.min(getMaxEnergyStored() - stored, energy);
        if(!simulate) {
            setEnergy(itemStack, stored + energyReceived);
        }
        return energyReceived;
    }

    @Override
    public int extractEnergy(int energy, boolean simulate) {
        int stored = getEnergyStored();
        int newEnergy = Math.max(stored - energy, 0);
        if(!simulate) {
            setEnergy(itemStack, newEnergy);
        }
        return stored - newEnergy;
    }

    protected void setEnergy(ItemStack itemStack, int energy) {
        CompoundNBT tag = itemStack.getOrCreateTag();
        tag.putInt("energy", energy);
    }
}
