package org.cyclops.energeticsheep.item;

import net.minecraft.core.dispenser.DispenseItemBehavior;
import org.cyclops.energeticsheep.EnergeticSheepForge;

/**
 * @author rubensworks
 */
public class ItemEnergeticShearsConfigForge extends ItemEnergeticShearsConfigCommon<EnergeticSheepForge> {
    public ItemEnergeticShearsConfigForge() {
        super(
                EnergeticSheepForge._instance,
                (eConfig, properties) -> new ItemEnergeticShearsForge(getProperties(properties))
        );
    }

    @Override
    protected DispenseItemBehavior createDispenseBehavior() {
        return new EnergeticShearsDispenseItemBehaviorForge();
    }
}
