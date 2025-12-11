package org.cyclops.energeticsheep.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.transfer.energy.SimpleEnergyHandler;
import net.neoforged.neoforge.transfer.transaction.Transaction;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author rubensworks
 */
public class EntityEnergeticSheepNeoForge extends EntityEnergeticSheepCommon {

    @Nullable
    private EnergyHandler energyStorage;

    public EntityEnergeticSheepNeoForge(EntityType<? extends EntityEnergeticSheepCommon> type, Level world) {
        super(type, world);
    }

    @Nullable
    public EnergyHandler getEnergyStorage() {
        return energyStorage;
    }

    @Override
    protected void initializeEnergy(DyeColor color) {
        this.energyStorage = new SimpleEnergyHandler(getCapacity(color)) {
            @Override
            protected void onEnergyChanged(int previousAmount) {
                super.onEnergyChanged(previousAmount);
                EntityEnergeticSheepNeoForge.this.updateEnergy(energy);
            }
        };
    }

    @Override
    public int getCapacity() {
        return this.energyStorage != null ? this.energyStorage.getCapacityAsInt() : 0;
    }

    @Override
    protected void restoreAllEnergy() {
        if (this.energyStorage != null) {
            try (var tx = Transaction.openRoot()) {
                this.energyStorage.insert(this.energyStorage.getCapacityAsInt(), tx);
                tx.commit();
            }
        }
    }

    @Override
    protected void consumeAllEnergy() {
        if (this.energyStorage != null) {
            try (var tx = Transaction.openRoot()) {
                this.energyStorage.extract(this.energyStorage.getCapacityAsInt(), tx);
                tx.commit();
            }
        }
    }

    @Override
    public List<ItemStack> onSheared(@Nullable Player player, ItemStack item, Level world, BlockPos pos) {
        return onShearedInternal(player, item, world, pos);
    }

    @Override
    public void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        if (this.energyStorage != null) {
            output.putInt("energy", this.energyStorage.getCapacityAsInt());
        }
    }

    @Override
    public void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        try (var tx = Transaction.openRoot()) {
            this.energyStorage.insert(input.getInt("energy").orElseThrow(), tx);
            tx.commit();
        }
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, EntitySpawnReason reason, @org.jetbrains.annotations.Nullable SpawnGroupData spawnDataIn) {
        SpawnGroupData data = super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn);
        try (var tx = Transaction.openRoot()) {
            this.energyStorage.insert(this.energyStorage.getCapacityAsInt(), tx);
            tx.commit();
        }
        return data;
    }
}
