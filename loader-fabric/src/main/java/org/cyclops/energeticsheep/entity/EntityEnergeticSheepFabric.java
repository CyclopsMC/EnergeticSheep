package org.cyclops.energeticsheep.entity;

import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import team.reborn.energy.api.EnergyStorage;
import team.reborn.energy.api.base.SimpleEnergyStorage;

import javax.annotation.Nullable;

/**
 * @author rubensworks
 */
public class EntityEnergeticSheepFabric extends EntityEnergeticSheepCommon {

    @Nullable
    private EnergyStorage energyStorage;

    public EntityEnergeticSheepFabric(EntityType<? extends EntityEnergeticSheepCommon> type, Level world) {
        super(type, world);
    }

    @Nullable
    public EnergyStorage getEnergyStorage() {
        return energyStorage;
    }

    @Override
    protected void initializeEnergy(DyeColor color) {
        int capacity = getCapacity(color);
        this.energyStorage = new SimpleEnergyStorage(capacity, capacity, capacity) {
            @Override
            protected void onFinalCommit() {
                super.onFinalCommit();

                EntityEnergeticSheepFabric.this.updateEnergy((int) getAmount());
            }
        };
    }

    @Override
    public int getCapacity() {
        return this.energyStorage != null ? (int) this.energyStorage.getCapacity() : 0;
    }

    @Override
    protected void restoreAllEnergy() {
        if (this.energyStorage != null) {
            try (Transaction transaction = Transaction.openOuter()) {
                this.energyStorage.insert(this.energyStorage.getCapacity(), transaction);
                transaction.commit();
            }
        }
    }

    @Override
    protected void consumeAllEnergy() {
        if (this.energyStorage != null) {
            try (Transaction transaction = Transaction.openOuter()) {
                this.energyStorage.extract(this.energyStorage.getCapacity(), transaction);
                transaction.commit();
            }
        }
    }

    @Override
    public void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        if (this.energyStorage != null) {
            output.putLong("energy", this.energyStorage.getAmount());
        }
    }

    @Override
    public void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        try (Transaction transaction = Transaction.openOuter()) {
            this.energyStorage.insert(input.getLong("energy").orElseThrow(), transaction);
            transaction.commit();
        }
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, EntitySpawnReason reason, @org.jetbrains.annotations.Nullable SpawnGroupData spawnDataIn) {
        SpawnGroupData data = super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn);
        try (Transaction transaction = Transaction.openOuter()) {
            this.energyStorage.insert(this.energyStorage.getCapacity(), transaction);
            transaction.commit();
        }
        return data;
    }
}
