package org.cyclops.energeticsheep.item;


import net.minecraft.world.item.Item;
import org.cyclops.cyclopscore.RegistryEntriesCommon;
import org.cyclops.cyclopscore.config.ConfigurablePropertyCommon;
import org.cyclops.cyclopscore.config.ModConfigLocation;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;

import java.util.function.Function;

/**
 * Config for the {@link ItemEnergeticShearsCommon}.
 * @author rubensworks
 *
 */
public class ItemEnergeticShearsConfigCommon<M extends IModBase> extends ItemConfigCommon<M> {

    @ConfigurablePropertyCommon(category = "item", comment = "The amount of energy this item can hold.", requiresMcRestart = true, isCommandable = true, configLocation = ModConfigLocation.SERVER)
    public static int capacity = 1000000;

    @ConfigurablePropertyCommon(category = "item", comment = "How much energy should be transferred on each usage.", isCommandable = true, configLocation = ModConfigLocation.SERVER)
    public static int usageTransferAmount = 100000;

    @ConfigurablePropertyCommon(category = "item", comment = "How much the regular shear action should consume.", isCommandable = true, configLocation = ModConfigLocation.SERVER)
    public static int shearConsumption = 100;

    public ItemEnergeticShearsConfigCommon(M mod, Function<ItemConfigCommon<M>, ? extends Item> elementConstructor) {
        super(
                mod,
                "energetic_shears",
                elementConstructor
        );
    }

    protected static Item.Properties getProperties() {
        return new Item.Properties()
                .component(RegistryEntriesCommon.COMPONENT_ENERGY_STORAGE.value(), 0)
                .durability(1);
    }

}
