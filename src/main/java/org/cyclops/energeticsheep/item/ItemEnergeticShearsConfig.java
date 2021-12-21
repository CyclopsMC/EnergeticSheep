package org.cyclops.energeticsheep.item;


import net.minecraft.world.item.Item;
import net.minecraftforge.fml.config.ModConfig;
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
                        .durability(0)
                        .tab(EnergeticSheep._instance.getDefaultItemGroup()))
        );
    }

}
