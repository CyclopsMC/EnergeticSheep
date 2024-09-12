package org.cyclops.energeticsheep.item;

import org.cyclops.energeticsheep.EnergeticSheepForge;

/**
 * @author rubensworks
 */
public class ItemEnergeticShearsConfigForge extends ItemEnergeticShearsConfigCommon<EnergeticSheepForge> {
    public ItemEnergeticShearsConfigForge() {
        super(
                EnergeticSheepForge._instance,
                eConfig -> new ItemEnergeticShearsForge(getProperties())
        );
    }
}
