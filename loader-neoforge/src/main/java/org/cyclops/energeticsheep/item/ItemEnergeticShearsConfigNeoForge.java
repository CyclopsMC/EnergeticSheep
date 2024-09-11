package org.cyclops.energeticsheep.item;

import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.energy.ComponentEnergyStorage;
import org.cyclops.cyclopscore.RegistryEntries;
import org.cyclops.energeticsheep.EnergeticSheepNeoForge;

/**
 * @author rubensworks
 */
public class ItemEnergeticShearsConfigNeoForge extends ItemEnergeticShearsConfigCommon<EnergeticSheepNeoForge> {
    public ItemEnergeticShearsConfigNeoForge() {
        super(
                EnergeticSheepNeoForge._instance,
                eConfig -> new ItemEnergeticShearsNeoForge(getProperties())
        );
        EnergeticSheepNeoForge._instance.getModEventBus().addListener(this::registerCapabilities);
    }

    public void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerItem(
                Capabilities.EnergyStorage.ITEM,
                (stack, context) -> new ComponentEnergyStorage(stack, RegistryEntries.COMPONENT_ENERGY_STORAGE.get(), ItemEnergeticShearsConfigCommon.capacity, Integer.MAX_VALUE, Integer.MAX_VALUE),
                getInstance()
        );
    }
}
