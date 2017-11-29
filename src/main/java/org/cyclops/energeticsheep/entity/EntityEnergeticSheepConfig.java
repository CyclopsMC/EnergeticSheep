package org.cyclops.energeticsheep.entity;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.ConfigurableTypeCategory;
import org.cyclops.cyclopscore.config.extendedconfig.MobConfig;
import org.cyclops.cyclopscore.helper.Helpers;
import org.cyclops.energeticsheep.EnergeticSheep;
import org.cyclops.energeticsheep.Reference;
import org.cyclops.energeticsheep.client.render.entity.RenderEntityEnergeticSheep;

/**
 * Config for the {@link EntityEnergeticSheep}.
 * @author rubensworks
 *
 */
public class EntityEnergeticSheepConfig extends MobConfig<EntityEnergeticSheep> {

    public static final ResourceLocation LOOTTABLE_SHEEP_WHITE      = LootTableList.register(new ResourceLocation(Reference.MOD_ID, "entities/energetic_sheep/white"));
    public static final ResourceLocation LOOTTABLE_SHEEP_ORANGE     = LootTableList.register(new ResourceLocation(Reference.MOD_ID, "entities/energetic_sheep/orange"));
    public static final ResourceLocation LOOTTABLE_SHEEP_MAGENTA    = LootTableList.register(new ResourceLocation(Reference.MOD_ID, "entities/energetic_sheep/magenta"));
    public static final ResourceLocation LOOTTABLE_SHEEP_LIGHT_BLUE = LootTableList.register(new ResourceLocation(Reference.MOD_ID, "entities/energetic_sheep/light_blue"));
    public static final ResourceLocation LOOTTABLE_SHEEP_YELLOW     = LootTableList.register(new ResourceLocation(Reference.MOD_ID, "entities/energetic_sheep/yellow"));
    public static final ResourceLocation LOOTTABLE_SHEEP_LIME       = LootTableList.register(new ResourceLocation(Reference.MOD_ID, "entities/energetic_sheep/lime"));
    public static final ResourceLocation LOOTTABLE_SHEEP_PINK       = LootTableList.register(new ResourceLocation(Reference.MOD_ID, "entities/energetic_sheep/pink"));
    public static final ResourceLocation LOOTTABLE_SHEEP_GRAY       = LootTableList.register(new ResourceLocation(Reference.MOD_ID, "entities/energetic_sheep/gray"));
    public static final ResourceLocation LOOTTABLE_SHEEP_SILVER     = LootTableList.register(new ResourceLocation(Reference.MOD_ID, "entities/energetic_sheep/silver"));
    public static final ResourceLocation LOOTTABLE_SHEEP_CYAN       = LootTableList.register(new ResourceLocation(Reference.MOD_ID, "entities/energetic_sheep/cyan"));
    public static final ResourceLocation LOOTTABLE_SHEEP_PURPLE     = LootTableList.register(new ResourceLocation(Reference.MOD_ID, "entities/energetic_sheep/purple"));
    public static final ResourceLocation LOOTTABLE_SHEEP_BLUE       = LootTableList.register(new ResourceLocation(Reference.MOD_ID, "entities/energetic_sheep/blue"));
    public static final ResourceLocation LOOTTABLE_SHEEP_BROWN      = LootTableList.register(new ResourceLocation(Reference.MOD_ID, "entities/energetic_sheep/brown"));
    public static final ResourceLocation LOOTTABLE_SHEEP_GREEN      = LootTableList.register(new ResourceLocation(Reference.MOD_ID, "entities/energetic_sheep/green"));
    public static final ResourceLocation LOOTTABLE_SHEEP_RED        = LootTableList.register(new ResourceLocation(Reference.MOD_ID, "entities/energetic_sheep/red"));
    public static final ResourceLocation LOOTTABLE_SHEEP_BLACK      = LootTableList.register(new ResourceLocation(Reference.MOD_ID, "entities/energetic_sheep/black"));

    /**
     * The unique instance.
     */
    public static EntityEnergeticSheepConfig _instance;

    /**
     * How much base energy the sheep can regenerate each time.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.MOB, comment = "How much base energy the sheep can regenerate each time.")
    public static int sheepBaseCapacity = 20000;

    /**
     * How much base energy energetic wool wool has.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.MOB, comment = "How much base energy energetic wool wool has.")
    public static int woolBaseCapacity = 500;

    /**
     * This factor will be multiplied by the ordinal value of the color, and will be multiplied with the base sheepBaseCapacity of the sheep.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.MOB, comment = "This factor will be multiplied by the ordinal value of the color, and will be multiplied with the base sheepBaseCapacity of the sheep.")
    public static double additionalCapacityColorFactor = 0.075D;

    /**
     * The 1/X chance on having an energetic baby when breeding.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.MOB, comment = "The 1/X chance on having an energetic baby when breeding.")
    public static int babyChance = 3;

    /**
     * Allow natural spawning of energetic sheep. If this is false,
     * energetic sheep will only be created by lightning strikes.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.MOB, comment = "Allow natural spawning of energetic sheep. If this is false, energetic sheep will only be created by lightning strikes.")
    public static Boolean naturalSpawn = true;



    /**
     * Make a new instance.
     */
    public EntityEnergeticSheepConfig() {
        super(
                EnergeticSheep._instance,
                true,
                "energetic_sheep",
                null,
                EntityEnergeticSheep.class
        );
    }
    
    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean hasSpawnEgg() {
        return true;
    }

    @Override
    public int getBackgroundEggColor() {
        return Helpers.RGBToInt(0, 111, 108);
    }

    @Override
    public int getForegroundEggColor() {
        return Helpers.RGBToInt(14, 167, 163);
    }

    @Override
    public void onRegistered() {
        super.onRegistered();
        if (naturalSpawn) {
            for (Biome biome : Biome.REGISTRY) {
                EntityRegistry.addSpawn(EntityEnergeticSheep.class, 5, 2, 4, EnumCreatureType.CREATURE, biome);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public Render getRender(RenderManager renderManager) {
        return new RenderEntityEnergeticSheep(renderManager, this);
    }
    
}
