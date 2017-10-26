package org.cyclops.energeticsheep.item;


import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.ConfigurableTypeCategory;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.energeticsheep.EnergeticSheep;

/**
 * Config for the {@link ItemEnergeticShears}.
 * @author rubensworks
 *
 */
public class ItemEnergeticShearsConfig extends ItemConfig {

    /**
     * The unique instance.
     */
    public static ItemEnergeticShearsConfig _instance;
    /**
     * The amount of energy this item can hold.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.ITEM, comment = "The amount of energy this item can hold.", requiresMcRestart = true, isCommandable = true)
    public static int capacity = 1000000;

    /**
     * How much energy should be transferred on each usage.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.ITEM, comment = "How much energy should be transferred on each usage.", isCommandable = true)
    public static int usageTransferAmount = 100000;

    /**
     * How much the regular shear action should consume.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.ITEM, comment = "How much the regular shear action should consume.", isCommandable = true)
    public static int shearConsumption = 100;

    /**
     * Make a new instance.
     */
    public ItemEnergeticShearsConfig() {
        super(
                EnergeticSheep._instance,
                true,
                "energetic_shears",
                null,
                ItemEnergeticShears.class
        );
    }
    
}
