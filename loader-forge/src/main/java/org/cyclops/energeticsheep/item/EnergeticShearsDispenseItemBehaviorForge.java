package org.cyclops.energeticsheep.item;

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
import net.minecraftforge.energy.IEnergyStorage;
import org.cyclops.energeticsheep.entity.EntityEnergeticSheepForge;

import java.util.List;

/**
 * Custom dispense behavior for energetic shears that transfers energy from energetic sheep
 * to the shears instead of dropping wool.
 *
 * <p>We extend {@link OptionalDispenseItemBehavior} directly (rather than
 * {@link net.minecraft.core.dispenser.ShearsDispenseItemBehavior}) so that we never call
 * {@code hurtAndBreak} on the item stack. Instead we consume energy via the item's energy
 * storage when shearing.</p>
 *
 * @author rubensworks
 */
public class EnergeticShearsDispenseItemBehaviorForge extends OptionalDispenseItemBehavior {

    @Override
    protected ItemStack execute(BlockSource source, ItemStack stack) {
        if (source.level().isClientSide()) {
            return stack;
        }

        BlockPos blockPos = source.pos().relative(source.state().getValue(DispenserBlock.FACING));
        List<Entity> entities = source.level().getEntitiesOfClass(Entity.class, new AABB(blockPos), EntitySelector.NO_SPECTATORS);

        for (Entity entity : entities) {
            if (entity instanceof EntityEnergeticSheepForge sheep) {
                IEnergyStorage entityEnergy = sheep.getEnergyStorage();
                IEnergyStorage itemEnergy = ItemEnergeticShearsForge.getEnergyStorageInternal(stack);
                if (entityEnergy != null && itemEnergy != null) {
                    int moved = entityEnergy.extractEnergy(
                            itemEnergy.receiveEnergy(
                                    entityEnergy.extractEnergy(ItemEnergeticShearsConfigCommon.usageTransferAmount, true),
                                    false),
                            false);
                    if (moved > 0) {
                        this.setSuccess(true);
                        entity.playSound(SoundEvents.SHEEP_SHEAR, 1.0F, 1.0F);
                        return stack;
                    }
                }
                // Energetic sheep with no transferable energy - don't fall through to vanilla shearing
                return stack;
            }

            // For other shearable entities (e.g. regular sheep): shear and consume energy
            if (entity instanceof Shearable shearable) {
                shearable.shear(source.level(), SoundSource.MASTER, stack);
                source.level().gameEvent(null, GameEvent.SHEAR, entity.position());
                this.setSuccess(true);
                // Consume energy instead of calling hurtAndBreak
                IEnergyStorage itemEnergy = ItemEnergeticShearsForge.getEnergyStorageInternal(stack);
                if (itemEnergy != null) {
                    itemEnergy.extractEnergy(ItemEnergeticShearsConfigCommon.shearConsumption, false);
                }
                return stack;
            }
        }

        return stack;
    }
}
