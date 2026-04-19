package org.cyclops.energeticsheep.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.sheep.Sheep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterItemModelsEvent;
import net.neoforged.neoforge.client.event.RegisterSpecialModelRendererEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.EntityStruckByLightningEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import org.cyclops.energeticsheep.EnergeticSheepNeoForge;
import org.cyclops.energeticsheep.Reference;
import org.cyclops.energeticsheep.client.render.blockentity.ItemEnergeticWoolChargeSpecialRenderer;
import org.cyclops.energeticsheep.client.render.item.AnimatedEnergeticWoolChargeItemModel;
import org.cyclops.energeticsheep.entity.layers.LayerEnergeticSheepCharge;
import net.minecraft.resources.Identifier;

/**
 * @author rubensworks
 */
public class EntityEnergeticSheepConfigNeoForge extends EntityEnergeticSheepConfigCommon<EnergeticSheepNeoForge, EntityEnergeticSheepNeoForge> {

    public EntityEnergeticSheepConfigNeoForge() {
        super(EnergeticSheepNeoForge._instance, EntityEnergeticSheepNeoForge::new);
        getMod().getModEventBus().addListener(this::onEntityAttributesCreation);
        if (getMod().getModHelpers().getMinecraftHelpers().isClientSide()) {
            getMod().getModEventBus().addListener(this::loadLayerDefinitions);
            getMod().getModEventBus().addListener(this::registerBlockEntityRenderers);
            getMod().getModEventBus().addListener(this::registerSpecialModelRenderers);
            getMod().getModEventBus().addListener(this::registerItemModels);
        }
        getMod().getModEventBus().addListener(this::registerCapabilities);
        getMod().getModEventBus().addListener(this::registerSpawnPlacements);
        NeoForge.EVENT_BUS.addListener(this::onLightning);
    }

    public void onEntityAttributesCreation(EntityAttributeCreationEvent event) {
        event.put(getInstance(), Sheep.createAttributes().build());
    }

    public void loadLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(LayerEnergeticSheepCharge.MODEL_LAYER_FUR_SCALED, () -> LayerEnergeticSheepCharge.createFurLayer(1.05F));
        event.registerLayerDefinition(ItemEnergeticWoolChargeSpecialRenderer.MODEL_LAYER, ItemEnergeticWoolChargeSpecialRenderer::createLayer);
    }

    public void registerBlockEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
    }

    public void registerSpecialModelRenderers(RegisterSpecialModelRendererEvent event) {
        event.register(
                Identifier.fromNamespaceAndPath(Reference.MOD_ID, "energetic_wool_charge"),
                ItemEnergeticWoolChargeSpecialRenderer.Unbaked.CODEC);
    }

    public void registerItemModels(RegisterItemModelsEvent event) {
        event.register(
                Identifier.fromNamespaceAndPath(Reference.MOD_ID, "energetic_wool_charge_item"),
                AnimatedEnergeticWoolChargeItemModel.Unbaked.CODEC);
    }

    public void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerEntity(
                Capabilities.Energy.ENTITY,
                getInstance(),
                (entity, context) -> entity.getEnergyStorage()
        );
    }

    public void registerSpawnPlacements(RegisterSpawnPlacementsEvent event) {
        event.register(getInstance(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
    }

    public void onLightning(EntityStruckByLightningEvent event) {
        if (event.getEntity().getClass() == Sheep.class) {
            EntityEnergeticSheepCommon.onLightning((Sheep) event.getEntity());
            event.getLightning().remove(Entity.RemovalReason.KILLED);
        }
    }
}
