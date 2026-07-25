package org.cyclops.energeticsheep.entity;

import net.minecraft.core.Direction;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;

/**
 * @author rubensworks
 */
public class EntityEnergeticSheepForge extends EntityEnergeticSheepCommon {

    @Nullable
    private IEnergyStorage energyStorage;

    public EntityEnergeticSheepForge(EntityType<? extends EntityEnergeticSheepCommon> type, Level world) {
        super(type, world);
    }

    @Nullable
    public IEnergyStorage getEnergyStorage() {
        return energyStorage;
    }

    @Override
    protected void initializeEnergy(DyeColor color) {
        this.energyStorage = new EnergyStorage(getCapacity(color)) {
            @Override
            public int receiveEnergy(int maxReceive, boolean simulate) {
                int ret = super.receiveEnergy(maxReceive, simulate);
                if (!simulate) {
                    EntityEnergeticSheepForge.this.updateEnergy(energy);
                }
                return ret;
            }

            @Override
            public int extractEnergy(int maxExtract, boolean simulate) {
                int ret = super.extractEnergy(maxExtract, simulate);
                if (!simulate) {
                    EntityEnergeticSheepForge.this.updateEnergy(energy);
                }
                return ret;
            }
        };
    }

    @Override
    public int getCapacity() {
        return this.energyStorage != null ? this.energyStorage.getMaxEnergyStored() : 0;
    }

    @Override
    protected void restoreAllEnergy() {
        if (this.energyStorage != null) {
            this.energyStorage.receiveEnergy(this.energyStorage.getMaxEnergyStored(), false);
        }
    }

    @Override
    protected void consumeAllEnergy() {
        if (this.energyStorage != null) {
            this.energyStorage.extractEnergy(this.energyStorage.getMaxEnergyStored(), false);
        }
    }

    @Override
    public void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        if (this.energyStorage != null) {
            output.putInt("energy", this.energyStorage.getEnergyStored());
        }
    }

    @Override
    public void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.energyStorage.receiveEnergy(input.getInt("energy").orElseThrow(), false);
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, EntitySpawnReason reason, @org.jetbrains.annotations.Nullable SpawnGroupData spawnDataIn) {
        SpawnGroupData data = super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn);
        this.energyStorage.receiveEnergy(this.energyStorage.getMaxEnergyStored(), false);
        return data;
    }

    @Nullable
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
        if (capability == ForgeCapabilities.ENERGY && this.energyStorage != null) {
            return LazyOptional.of(() -> this.energyStorage).cast();
        }
        return super.getCapability(capability, facing);
    }
}
