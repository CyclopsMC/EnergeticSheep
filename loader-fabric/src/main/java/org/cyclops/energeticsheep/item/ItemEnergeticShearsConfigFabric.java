package org.cyclops.energeticsheep.item;

import org.cyclops.energeticsheep.EnergeticSheepFabric;
import team.reborn.energy.api.EnergyStorage;
import team.reborn.energy.api.base.SimpleEnergyItem;

/**
 * @author rubensworks
 */
public class ItemEnergeticShearsConfigFabric extends ItemEnergeticShearsConfigCommon<EnergeticSheepFabric> {
    public ItemEnergeticShearsConfigFabric() {
        super(
                EnergeticSheepFabric._instance,
                (eConfig, properties) -> new ItemEnergeticShearsFabric(properties
                        .component(EnergyStorage.ENERGY_COMPONENT, 0L)
                        .durability(1))
        );
    }

    @Override
    public void onRegistryRegistered() {
        super.onRegistryRegistered();

        EnergyStorage.ITEM.registerForItems(
                (itemStack, context) -> SimpleEnergyItem.createStorage(context, ItemEnergeticShearsConfigCommon.capacity, Integer.MAX_VALUE, Integer.MAX_VALUE),
                getInstance()
        );
    }
}
