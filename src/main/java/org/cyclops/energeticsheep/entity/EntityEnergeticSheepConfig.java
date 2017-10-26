package org.cyclops.energeticsheep.entity;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.ConfigurableTypeCategory;
import org.cyclops.cyclopscore.config.extendedconfig.MobConfig;
import org.cyclops.cyclopscore.helper.Helpers;
import org.cyclops.energeticsheep.EnergeticSheep;
import org.cyclops.energeticsheep.client.render.entity.RenderEntityEnergeticSheep;

/**
 * Config for the {@link EntityEnergeticSheep}.
 * @author rubensworks
 *
 */
public class EntityEnergeticSheepConfig extends MobConfig<EntityEnergeticSheep> {

    /**
     * The unique instance.
     */
    public static EntityEnergeticSheepConfig _instance;

    /**
     * How much energy the sheep can regenerate each time.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.MOB, comment = "How much energy the sheep can regenerate each time.")
    public static int capacity = 100000;

    /**
     * The 1/X chance on having an energetic baby when breeding.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.MOB, comment = "The 1/X chance on having an energetic baby when breeding.")
    public static int babyChance = 10;

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
    public void onForgeRegistered() {
        super.onForgeRegistered();
        for (Biome biome : Biome.REGISTRY) {
            EntityRegistry.addSpawn(EntityEnergeticSheep.class, 5, 2, 4, EnumCreatureType.CREATURE, biome);
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public Render getRender(RenderManager renderManager) {
        return new RenderEntityEnergeticSheep(renderManager, this);
    }
    
}
