package org.cyclops.energeticsheep.item;


import net.minecraft.world.item.Item;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.energeticsheep.EnergeticSheep;
import org.cyclops.energeticsheep.capability.energystorage.EnergyStorageItem;

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
                        .durability(0))
        );
        EnergeticSheep._instance.getModEventBus().addListener(this::registerCapabilities);
    }

    public void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerItem(
                Capabilities.EnergyStorage.ITEM,
                (stack, context) -> new EnergyStorageItem(ItemEnergeticShearsConfig.capacity, stack),
                getInstance()
        );
    }

}
