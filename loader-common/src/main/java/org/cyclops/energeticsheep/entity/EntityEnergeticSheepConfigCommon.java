package org.cyclops.energeticsheep.entity;

import com.google.common.collect.Lists;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import org.cyclops.cyclopscore.config.ConfigurablePropertyCommon;
import org.cyclops.cyclopscore.config.ModConfigLocation;
import org.cyclops.cyclopscore.config.extendedconfig.EntityClientConfig;
import org.cyclops.cyclopscore.config.extendedconfig.EntityConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;

import java.util.List;

/**
 * Config for the {@link EntityEnergeticSheepCommon}.
 * @author rubensworks
 *
 */
public class EntityEnergeticSheepConfigCommon<M extends IModBase, T extends EntityEnergeticSheepCommon> extends EntityConfigCommon<M, T> {

    @ConfigurablePropertyCommon(category = "mob", comment = "How much base energy the sheep can regenerate each time.", configLocation = ModConfigLocation.SERVER)
    public static int sheepBaseCapacity = 20000;

    @ConfigurablePropertyCommon(category = "mob", comment = "How much base energy energetic wool wool has.", configLocation = ModConfigLocation.SERVER)
    public static int woolBaseCapacity = 500;

    @ConfigurablePropertyCommon(category = "mob", comment = "This factor will be multiplied by the ordinal value of the color, and will be multiplied with the base sheepBaseCapacity of the sheep.", isCommandable = true)
    public static double additionalCapacityColorFactor = 0.075D;

    @ConfigurablePropertyCommon(category = "mob", comment = "The 1/X chance on having an energetic baby when breeding.", configLocation = ModConfigLocation.SERVER, isCommandable = true)
    public static int babyChance = 3;

    @ConfigurablePropertyCommon(category = "mob", comment = "The 1/X chance on having an energetic baby when breeding with a power-breeding item.", configLocation = ModConfigLocation.SERVER, isCommandable = true)
    public static int babyChancePowerBreeding = 1;

    @ConfigurablePropertyCommon(category = "mob",
            comment = "The items that can be used to power-breed sheep, by unique item name.", configLocation = ModConfigLocation.SERVER)
    public static List<String> powerBreedingItems = Lists.newArrayList(
            "minecraft:rabbit_stew",
            "minecraft:chorus_fruit",
            "integrateddynamics:menril_berries"
    );

    public EntityEnergeticSheepConfigCommon(M mod, EntityType.EntityFactory<T> entityFactory) {
        super(
                mod,
                "energetic_sheep",
                eConfig -> EntityType.Builder.of(entityFactory, MobCategory.CREATURE)
                        .sized(0.9F, 1.3F)
                        .fireImmune(),
                getDefaultSpawnEggItemConfigConstructor(
                        mod,
                        "energetic_sheep_spawn_egg",
                        mod.getModHelpers().getBaseHelpers().RGBToInt(0, 111, 108),
                        mod.getModHelpers().getBaseHelpers().RGBToInt(14, 167, 163)
                )
        );
    }

    @Override
    public EntityClientConfig<M, T> getEntityClientConfig() {
        return new EntityEnergeticSheepClientConfigCommon<>(this);
    }
}
