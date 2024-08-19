package org.cyclops.energeticsheep.item;


import net.minecraft.world.item.Item;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.energy.ComponentEnergyStorage;
import org.cyclops.cyclopscore.RegistryEntries;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.energeticsheep.EnergeticSheep;

/**
 * Config for the {@link ItemEnergeticShears}.
 * @author rubensworks
 *
 */
public class ItemEnergeticShearsConfig extends ItemConfig {

    @ConfigurableProperty(category = "item", comment = "The amount of energy this item can hold.", requiresMcRestart = true, isCommandable = true, configLocation = ModConfig.Type.SERVER)
    public static int capacity = 1000000;

    @ConfigurableProperty(category = "item", comment = "How much energy should be transferred on each usage.", isCommandable = true, configLocation = ModConfig.Type.SERVER)
    public static int usageTransferAmount = 100000;

    @ConfigurableProperty(category = "item", comment = "How much the regular shear action should consume.", isCommandable = true, configLocation = ModConfig.Type.SERVER)
    public static int shearConsumption = 100;

    public ItemEnergeticShearsConfig() {
        super(
                EnergeticSheep._instance,
                "energetic_shears",
                eConfig -> new ItemEnergeticShears(new Item.Properties()
                        .component(RegistryEntries.COMPONENT_ENERGY_STORAGE, 0)
                        .durability(1))
        );
        EnergeticSheep._instance.getModEventBus().addListener(this::registerCapabilities);
    }

    public void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerItem(
                Capabilities.EnergyStorage.ITEM,
                (stack, context) -> new ComponentEnergyStorage(stack, RegistryEntries.COMPONENT_ENERGY_STORAGE.get(), ItemEnergeticShearsConfig.capacity, Integer.MAX_VALUE, Integer.MAX_VALUE),
                getInstance()
        );
    }

}
