package org.cyclops.energeticsheep.entity;

import com.google.common.collect.Lists;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.extendedconfig.EntityConfig;
import org.cyclops.energeticsheep.EnergeticSheep;
import org.cyclops.energeticsheep.client.render.entity.RenderEntityEnergeticSheep;
import org.cyclops.energeticsheep.entity.layers.LayerEnergeticSheepCharge;

import java.util.List;

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
                eConfig -> EntityType.Builder.of(EntityEnergeticSheep::new, MobCategory.CREATURE)
                        .sized(0.9F, 1.3F)
                        .fireImmune()
        );
        MinecraftForge.EVENT_BUS.addListener(this::onBiomeLoadingEvent);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onEntityAttributesModification);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void onRegistered() {
        super.onRegistered();
        FMLJavaModLoadingContext.get().getModEventBus().addListener(LayerEnergeticSheepCharge::loadLayerDefinitions);
    }

    public void onBiomeLoadingEvent(BiomeLoadingEvent event) {
        // Register energetic sheep spawns to biomes in which regular sheep spawn
        List<MobSpawnSettings.SpawnerData> spawners = event.getSpawns().getSpawner(MobCategory.CREATURE);
        for (MobSpawnSettings.SpawnerData spawner : spawners) {
            if (spawner.type == EntityType.SHEEP) {
                spawners.add(new MobSpawnSettings.SpawnerData(getInstance(), spawnWeight, 2, 4));
                break;
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public EntityRenderer<? super EntityEnergeticSheep> getRender(EntityRendererProvider.Context renderContext, ItemRenderer itemRenderer) {
        return new RenderEntityEnergeticSheep(renderContext, this);
    }

    public void onEntityAttributesModification(EntityAttributeModificationEvent event) {
        // Same as Sheep.createAttributes()
        event.add(getInstance(), Attributes.MAX_HEALTH);
        event.add(getInstance(), Attributes.KNOCKBACK_RESISTANCE);
        event.add(getInstance(), Attributes.MOVEMENT_SPEED);
        event.add(getInstance(), Attributes.ARMOR);
        event.add(getInstance(), Attributes.ARMOR_TOUGHNESS);
        event.add(getInstance(), net.minecraftforge.common.ForgeMod.SWIM_SPEED.get());
        event.add(getInstance(), net.minecraftforge.common.ForgeMod.NAMETAG_DISTANCE.get());
        event.add(getInstance(), net.minecraftforge.common.ForgeMod.ENTITY_GRAVITY.get());
        event.add(getInstance(), Attributes.FOLLOW_RANGE, 16.0D);
        event.add(getInstance(), Attributes.ATTACK_KNOCKBACK);
        event.add(getInstance(), Attributes.MAX_HEALTH, 8.0D);
        event.add(getInstance(), Attributes.MOVEMENT_SPEED, 0.23F);
    }
}
