package org.cyclops.energeticsheep.item;

import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.Shearable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import org.cyclops.energeticsheep.entity.EntityEnergeticSheepFabric;
import team.reborn.energy.api.EnergyStorage;

import java.util.List;

/**
 * Custom dispense behavior for energetic shears that transfers energy from energetic sheep
 * to the shears instead of dropping wool.
 *
 * <p>We extend {@link OptionalDispenseItemBehavior} directly (rather than
 * {@link net.minecraft.core.dispenser.ShearsDispenseItemBehavior}) so that we never call
 * {@code hurtAndBreak} on the item stack. On Fabric the energetic shears have
 * {@code .durability(1)}, which means a single {@code hurtAndBreak(1, ...)} call would
 * destroy the item. Instead we consume energy from the data component ourselves.</p>
 *
 * @author rubensworks
 */
public class EnergeticShearsDispenseItemBehaviorFabric extends OptionalDispenseItemBehavior {

    @Override
    protected ItemStack execute(BlockSource source, ItemStack stack) {
        if (source.level().isClientSide()) {
            return stack;
        }

        BlockPos blockPos = source.pos().relative(source.state().getValue(DispenserBlock.FACING));
        List<Entity> entities = source.level().getEntitiesOfClass(Entity.class, new AABB(blockPos), EntitySelector.NO_SPECTATORS);

        for (Entity entity : entities) {
            if (entity instanceof EntityEnergeticSheepFabric sheep) {
                EnergyStorage sheepStorage = sheep.getEnergyStorage();
                if (sheepStorage != null) {
                    long shearsEnergy = stack.getOrDefault(EnergyStorage.ENERGY_COMPONENT, 0L);
                    long shearsCapacity = ItemEnergeticShearsConfigCommon.capacity;
                    long toTransfer = Math.min(
                            sheepStorage.getAmount(),
                            Math.min(ItemEnergeticShearsConfigCommon.usageTransferAmount, shearsCapacity - shearsEnergy));
                    if (toTransfer > 0) {
                        try (Transaction tx = Transaction.openOuter()) {
                            long extracted = sheepStorage.extract(toTransfer, tx);
                            if (extracted > 0) {
                                stack.set(EnergyStorage.ENERGY_COMPONENT, shearsEnergy + extracted);
                                tx.commit();
                                this.setSuccess(true);
                                entity.playSound(SoundEvents.SHEEP_SHEAR, 1.0F, 1.0F);
                                return stack;
                            }
                        }
                    }
                }
                // Energetic sheep with no transferable energy - don't fall through to vanilla shearing
                return stack;
            }

            // For other shearable entities (e.g. regular sheep): shear and consume energy
            if (entity instanceof Shearable shearable && shearable.readyForShearing()) {
                shearable.shear(source.level(), SoundSource.BLOCKS, stack);
                source.level().gameEvent(null, GameEvent.SHEAR, entity.position());
                this.setSuccess(true);
                // Consume energy from the shears component (mirrors what damageItem does on NeoForge)
                long shearsEnergy = stack.getOrDefault(EnergyStorage.ENERGY_COMPONENT, 0L);
                long consumed = Math.min(shearsEnergy, ItemEnergeticShearsConfigCommon.shearConsumption);
                stack.set(EnergyStorage.ENERGY_COMPONENT, shearsEnergy - consumed);
                return stack;
            }
        }

        return stack;
    }
}
