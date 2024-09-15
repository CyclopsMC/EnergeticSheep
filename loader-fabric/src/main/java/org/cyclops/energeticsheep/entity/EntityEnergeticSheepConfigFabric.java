package org.cyclops.energeticsheep.entity;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.lookup.v1.entity.EntityApiLookup;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.Heightmap;
import org.cyclops.cyclopscore.config.ConfigurablePropertyCommon;
import org.cyclops.cyclopscore.config.ModConfigLocation;
import org.cyclops.energeticsheep.EnergeticSheepFabric;
import org.cyclops.energeticsheep.entity.layers.LayerEnergeticSheepCharge;
import org.jetbrains.annotations.Nullable;
import team.reborn.energy.api.EnergyStorage;

/**
 * @author rubensworks
 */
public class EntityEnergeticSheepConfigFabric extends EntityEnergeticSheepConfigCommon<EnergeticSheepFabric, EntityEnergeticSheepFabric> {

    @ConfigurablePropertyCommon(category = "mob", comment = "The biome spawn weight. Set to -1 to disable.", configLocation = ModConfigLocation.SERVER)
    public static int spawnWeight = 3;
    @ConfigurablePropertyCommon(category = "mob", comment = "The biome spawn minimum group size", configLocation = ModConfigLocation.SERVER)
    public static int spawnMinGroupSize = 2;
    @ConfigurablePropertyCommon(category = "mob", comment = "The biome spawn maximum group size", configLocation = ModConfigLocation.SERVER)
    public static int spawnMaxGroupSize = 4;

    public EntityEnergeticSheepConfigFabric() {
        super(EnergeticSheepFabric._instance, EntityEnergeticSheepFabric::new);
        ServerEntityEvents.ENTITY_LOAD.register(this::onLightning);
    }

    @Override
    public void onForgeRegistered() {
        super.onForgeRegistered();

        // Handle biome spawning
        if (spawnWeight > 0) {
            TagKey<Biome> biomeTagNotEnergeticSheep = TagKey.create(Registries.BIOME, ResourceLocation.parse("energeticsheep:is_not_energetic_sheep"));
            BiomeModifications.addSpawn(
                    biome -> biome.hasTag(BiomeTags.IS_OVERWORLD) && !biome.hasTag(biomeTagNotEnergeticSheep),
                    MobCategory.CREATURE,
                    getInstance(),
                    spawnWeight,
                    spawnMinGroupSize,
                    spawnMaxGroupSize
            );
        }

        // Spawn placements
        SpawnPlacements.register(getInstance(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);

        // Register energy capability
        EnergeticSheepFabric.ENERGY_STORAGE_ENTITY.registerForTypes(new EntityApiLookup.EntityApiProvider<EnergyStorage, Void>() {
            @Override
            public @Nullable EnergyStorage find(Entity entity, Void context) {
                return ((EntityEnergeticSheepFabric) entity).getEnergyStorage();
            }
        }, getInstance());

        // Register entity attributes
        FabricDefaultAttributeRegistry.register(getInstance(), Sheep.createAttributes());

        if (getMod().getModHelpers().getMinecraftHelpers().isClientSide()) {
            registerClientSideLayer();
        }
    }

    private void registerClientSideLayer() {
        EntityModelLayerRegistry.registerModelLayer(LayerEnergeticSheepCharge.MODEL_LAYER_FUR_SCALED, () -> LayerEnergeticSheepCharge.createFurLayer(1.05F));
    }

    public void onLightning(Entity entity, ServerLevel world) {
        if (entity instanceof LightningBolt) {
            ((LightningBolt) entity).getHitEntities().forEach(hitEntity -> {
                if (hitEntity.getClass() == Sheep.class) {
                    EntityEnergeticSheepCommon.onLightning((Sheep) hitEntity);
                    entity.remove(Entity.RemovalReason.KILLED);
                }
            });
        }
    }
}
