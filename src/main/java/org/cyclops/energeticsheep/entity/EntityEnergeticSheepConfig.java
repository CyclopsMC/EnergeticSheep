package org.cyclops.energeticsheep.entity;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.extendedconfig.EntityConfig;
import org.cyclops.energeticsheep.EnergeticSheep;
import org.cyclops.energeticsheep.client.render.entity.RenderEntityEnergeticSheep;

import java.util.List;
import java.util.Set;

/**
 * Config for the {@link EntityEnergeticSheep}.
 * @author rubensworks
 *
 */
public class EntityEnergeticSheepConfig extends EntityConfig<EntityEnergeticSheep> {

    @ConfigurableProperty(category = "mob", comment = "How much base energy the sheep can regenerate each time.", configLocation = ModConfig.Type.SERVER)
    public static int sheepBaseCapacity = 20000;

    @ConfigurableProperty(category = "mob", comment = "How much base energy energetic wool wool has.", configLocation = ModConfig.Type.SERVER)
    public static int woolBaseCapacity = 500;

    @ConfigurableProperty(category = "mob", comment = "This factor will be multiplied by the ordinal value of the color, and will be multiplied with the base sheepBaseCapacity of the sheep.", isCommandable = true)
    public static double additionalCapacityColorFactor = 0.075D;

    @ConfigurableProperty(category = "mob", comment = "The 1/X chance on having an energetic baby when breeding.", configLocation = ModConfig.Type.SERVER, isCommandable = true)
    public static int babyChance = 3;

    @ConfigurableProperty(category = "mob", comment = "The 1/X chance on having an energetic baby when breeding with a power-breeding item.", configLocation = ModConfig.Type.SERVER, isCommandable = true)
    public static int babyChancePowerBreeding = 1;

    @ConfigurableProperty(category = "mob", minimalValue = 0, comment = "Spawn weight for energetic sheep. If this is is set to 0, energetic sheep will only be created by lightning strikes.", configLocation = ModConfig.Type.SERVER, requiresMcRestart = true)
    public static int spawnWeight = 3;

    @ConfigurableProperty(category = "mob",
            comment = "The items that can be used to power-breed sheep, by unique item name.", configLocation = ModConfig.Type.SERVER)
    public static List<String> powerBreedingItems = Lists.newArrayList(
            "minecraft:rabbit_stew",
            "minecraft:chorus_fruit",
            "integrateddynamics:menril_berries"
    );

    public EntityEnergeticSheepConfig() {
        super(
                EnergeticSheep._instance,
                "energetic_sheep",
                eConfig -> EntityType.Builder.create(EntityEnergeticSheep::new, EntityClassification.CREATURE)
                        .size(0.9F, 1.3F)
                        .immuneToFire()
        );
        FMLJavaModLoadingContext.get().getModEventBus().register(this);
    }

    @SubscribeEvent
    public void onConfigLoad(ModConfig.Loading configEvent) {
        // Collect biomes in which sheep spawn
        Set<Biome> biomes = Sets.newIdentityHashSet();
        for (Biome biome : ForgeRegistries.BIOMES.getValues()) {
            for (Biome.SpawnListEntry spawn : biome.getSpawns(EntityClassification.CREATURE)) {
                if (spawn.entityType == EntityType.SHEEP) {
                    biomes.add(biome);
                }
            }
        }

        // Register energetic sheep spawns to collected biomes
        for (Biome biome : biomes) {
            biome.addSpawn(EntityClassification.CREATURE, new Biome.SpawnListEntry(getInstance(), spawnWeight, 2, 4));
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public EntityRenderer<? super EntityEnergeticSheep> getRender(EntityRendererManager entityRendererManager, ItemRenderer itemRenderer) {
        return new RenderEntityEnergeticSheep(entityRendererManager, this);
    }
    
}
