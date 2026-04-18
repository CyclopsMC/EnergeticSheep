package org.cyclops.energeticsheep.entity;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.lookup.v1.entity.EntityApiLookup;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.sheep.Sheep;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.Heightmap;
import org.cyclops.cyclopscore.config.ConfigurablePropertyCommon;
import org.cyclops.cyclopscore.config.ModConfigLocation;
import org.cyclops.energeticsheep.EnergeticSheepFabric;
import org.cyclops.energeticsheep.RegistryEntries;
import org.cyclops.energeticsheep.client.render.blockentity.RenderBlockEntityEnergeticWool;
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
    public void onRegistryRegistered() {
        super.onRegistryRegistered();

        // Handle biome spawning
        if (spawnWeight > 0) {
            TagKey<Biome> biomeTagNotEnergeticSheep = TagKey.create(Registries.BIOME, Identifier.parse("energeticsheep:is_not_energetic_sheep"));
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
        ModelLayerRegistry.registerModelLayer(LayerEnergeticSheepCharge.MODEL_LAYER_FUR_SCALED, () -> LayerEnergeticSheepCharge.createFurLayer(1.05F));
        ModelLayerRegistry.registerModelLayer(RenderBlockEntityEnergeticWool.MODEL_LAYER, RenderBlockEntityEnergeticWool::createLayer);
        BlockEntityRendererRegistry.register(RegistryEntries.BLOCK_ENTITY_TYPE_ENERGETIC_WOOL.value(), RenderBlockEntityEnergeticWool::new);
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
