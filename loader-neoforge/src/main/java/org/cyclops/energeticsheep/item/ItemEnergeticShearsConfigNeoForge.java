package org.cyclops.energeticsheep.item;

import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.transfer.energy.ItemAccessEnergyHandler;
import org.cyclops.cyclopscore.RegistryEntries;
import org.cyclops.energeticsheep.EnergeticSheepNeoForge;

/**
 * @author rubensworks
 */
public class ItemEnergeticShearsConfigNeoForge extends ItemEnergeticShearsConfigCommon<EnergeticSheepNeoForge> {
    public ItemEnergeticShearsConfigNeoForge() {
        super(
                EnergeticSheepNeoForge._instance,
                (eConfig, properties) -> new ItemEnergeticShearsNeoForge(getProperties(properties))
        );
        EnergeticSheepNeoForge._instance.getModEventBus().addListener(this::registerCapabilities);
    }

    public void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerItem(
                Capabilities.Energy.ITEM,
                (stack, itemAccess) -> new ItemAccessEnergyHandler(itemAccess, RegistryEntries.COMPONENT_ENERGY_STORAGE.get(), ItemEnergeticShearsConfigCommon.capacity, Integer.MAX_VALUE, Integer.MAX_VALUE),
                getInstance()
        );
    }

    @Override
    protected DispenseItemBehavior createDispenseBehavior() {
        return new EnergeticShearsDispenseItemBehaviorNeoForge();
    }
}
