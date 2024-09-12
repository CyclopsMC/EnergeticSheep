package org.cyclops.energeticsheep.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.EntityStruckByLightningEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import org.cyclops.energeticsheep.EnergeticSheepForge;
import org.cyclops.energeticsheep.entity.layers.LayerEnergeticSheepCharge;

/**
 * @author rubensworks
 */
public class EntityEnergeticSheepConfigForge extends EntityEnergeticSheepConfigCommon<EnergeticSheepForge, EntityEnergeticSheepForge> {

    public EntityEnergeticSheepConfigForge() {
        super(EnergeticSheepForge._instance, EntityEnergeticSheepForge::new);
        getMod().getModEventBus().addListener(this::onEntityAttributesCreation);
        if (getMod().getModHelpers().getMinecraftHelpers().isClientSide()) {
            getMod().getModEventBus().addListener(this::loadLayerDefinitions);
        }
        getMod().getModEventBus().addListener(this::registerSpawnPlacements);
        MinecraftForge.EVENT_BUS.addListener(this::onLightning);
    }

    public void onEntityAttributesCreation(EntityAttributeCreationEvent event) {
        event.put(getInstance(), Sheep.createAttributes().build());
    }

    public void loadLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(LayerEnergeticSheepCharge.MODEL_LAYER_FUR_SCALED, () -> LayerEnergeticSheepCharge.createFurLayer(1.05F));
    }

    public void registerSpawnPlacements(SpawnPlacementRegisterEvent event) {
        event.register(getInstance(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules, SpawnPlacementRegisterEvent.Operation.AND);
    }

    public void onLightning(EntityStruckByLightningEvent event) {
        if (event.getEntity().getClass() == Sheep.class) {
            EntityEnergeticSheepCommon.onLightning((Sheep) event.getEntity());
            event.getLightning().remove(Entity.RemovalReason.KILLED);
        }
    }
}
