package org.cyclops.energeticsheep.capability.energystorage;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.energy.IEnergyStorage;
import org.cyclops.cyclopscore.helper.ItemStackHelpers;

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
        NBTTagCompound tag = ItemStackHelpers.getSafeTagCompound(itemStack);
        return tag.getInteger("energy");
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
        NBTTagCompound tag = ItemStackHelpers.getSafeTagCompound(itemStack);
        tag.setInteger("energy", energy);
    }
}
