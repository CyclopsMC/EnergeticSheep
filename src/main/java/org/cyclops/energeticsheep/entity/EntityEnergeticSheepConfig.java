package org.cyclops.energeticsheep.entity;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BiomeLoadingEvent;
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
                eConfig -> EntityType.Builder.of(EntityEnergeticSheep::new, EntityClassification.CREATURE)
                        .sized(0.9F, 1.3F)
                        .fireImmune()
        );
        MinecraftForge.EVENT_BUS.addListener(this::onBiomeLoadingEvent);
    }

    public void onBiomeLoadingEvent(BiomeLoadingEvent event) {
        // Register energetic sheep spawns to biomes in which regular sheep spawn
        List<MobSpawnInfo.Spawners> spawners = event.getSpawns().getSpawner(EntityClassification.CREATURE);
        for (MobSpawnInfo.Spawners spawner : spawners) {
            if (spawner.type == EntityType.SHEEP) {
                spawners.add(new MobSpawnInfo.Spawners(getInstance(), spawnWeight, 2, 4));
                break;
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public EntityRenderer<? super EntityEnergeticSheep> getRender(EntityRendererManager entityRendererManager, ItemRenderer itemRenderer) {
        return new RenderEntityEnergeticSheep(entityRendererManager, this);
    }

    @Override
    public void onForgeRegistered() {
        super.onForgeRegistered();
        // MCP: registerAttributes
        GlobalEntityTypeAttributes.put(getInstance(), SheepEntity.createAttributes().build());
    }
}
