package org.cyclops.energeticsheep.item;


import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.cyclopscore.helper.Helpers;
import org.cyclops.energeticsheep.EnergeticSheep;
import org.cyclops.energeticsheep.entity.EntityEnergeticSheepConfig;

/**
 * Config for the energetic sheep spawn egg.
 * @author rubensworks
 *
 */
public class ItemEnergeticSheepSpawnEggConfig extends ItemConfig {

    public ItemEnergeticSheepSpawnEggConfig(EntityEnergeticSheepConfig entityEnergeticSheepConfig) {
        super(
                EnergeticSheep._instance,
                "energetic_sheep_spawn_egg",
                eConfig -> new SpawnEggItem(entityEnergeticSheepConfig.getInstance(),
                        Helpers.RGBToInt(0, 111, 108),
                        Helpers.RGBToInt(14, 167, 163),
                        new Item.Properties().group(EnergeticSheep._instance.getDefaultItemGroup()))
        );
    }
    
}
