package org.cyclops.energeticsheep.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.ShearsDispenseItemBehavior;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.transfer.energy.EnergyHandlerUtil;
import org.cyclops.energeticsheep.entity.EntityEnergeticSheepNeoForge;

import java.util.List;

/**
 * Custom dispense behavior for energetic shears that transfers energy from energetic sheep
 * to the shears instead of dropping wool.
 *
 * @author rubensworks
 */
public class EnergeticShearsDispenseItemBehaviorNeoForge extends ShearsDispenseItemBehavior {

    @Override
    protected ItemStack execute(BlockSource source, ItemStack stack) {
        if (!source.level().isClientSide()) {
            BlockPos blockPos = source.pos().relative(source.state().getValue(DispenserBlock.FACING));
            List<Entity> entities = source.level().getEntitiesOfClass(Entity.class, new AABB(blockPos), EntitySelector.NO_SPECTATORS);
            for (Entity entity : entities) {
                if (entity instanceof EntityEnergeticSheepNeoForge sheep) {
                    EnergyHandler entityEnergy = sheep.getEnergyStorage();
                    EnergyHandler itemEnergy = stack.getCapability(Capabilities.Energy.ITEM, ItemAccess.forStack(stack));
                    if (entityEnergy != null && itemEnergy != null) {
                        int moved = EnergyHandlerUtil.move(entityEnergy, itemEnergy, ItemEnergeticShearsConfigCommon.usageTransferAmount, null);
                        if (moved > 0) {
                            this.setSuccess(true);
                            entity.playSound(SoundEvents.SHEEP_SHEAR, 1.0F, 1.0F);
                            return stack;
                        }
                    }
                    // Energetic sheep with no transferable energy - don't fall through to vanilla shearing
                    return stack;
                }
            }
        }
        // No energetic sheep found - use vanilla shearing behavior for other entities
        return super.execute(source, stack);
    }
}
