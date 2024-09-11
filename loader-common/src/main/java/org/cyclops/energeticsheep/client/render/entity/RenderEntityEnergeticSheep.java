package org.cyclops.energeticsheep.client.render.entity;

import net.minecraft.client.model.SheepModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.layers.SheepFurLayer;
import net.minecraft.resources.ResourceLocation;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfigCommon;
import org.cyclops.energeticsheep.Reference;
import org.cyclops.energeticsheep.entity.EntityEnergeticSheepCommon;
import org.cyclops.energeticsheep.entity.layers.LayerEnergeticSheepCharge;

/**
 * Renderer for {@link EntityEnergeticSheepCommon}.
 *
 * @author rubensworks
 *
 */
public class RenderEntityEnergeticSheep extends MobRenderer<EntityEnergeticSheepCommon, SheepModel<EntityEnergeticSheepCommon>> {

    private ResourceLocation texture;

    /**
     * Make a new instance.
     * @param renderContext The render context.
     * @param config Then config.
     */
    public RenderEntityEnergeticSheep(EntityRendererProvider.Context renderContext, ExtendedConfigCommon<?, ?, ?> config) {
        super(renderContext, new SheepModel<>(renderContext.bakeLayer(ModelLayers.SHEEP)), 0.7F);
        this.addLayer((RenderLayer) new SheepFurLayer((MobRenderer) this, renderContext.getModelSet()));
        this.addLayer(new LayerEnergeticSheepCharge(this, renderContext.getModelSet()));
        texture = ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "textures/entities/" + config.getNamedId() + ".png");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityEnergeticSheepCommon entity) {
        return texture;
    }

}
