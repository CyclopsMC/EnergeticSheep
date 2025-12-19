package org.cyclops.energeticsheep.client.render.entity;

import net.minecraft.client.model.animal.sheep.SheepModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.AgeableMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.layers.SheepWoolLayer;
import net.minecraft.resources.Identifier;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfigCommon;
import org.cyclops.energeticsheep.Reference;
import org.cyclops.energeticsheep.client.render.entity.state.EntityRenderStateEnergeticSheep;
import org.cyclops.energeticsheep.entity.EntityEnergeticSheepCommon;
import org.cyclops.energeticsheep.entity.layers.LayerEnergeticSheepCharge;

/**
 * Renderer for {@link EntityEnergeticSheepCommon}.
 *
 * @author rubensworks
 *
 */
public class RenderEntityEnergeticSheep extends AgeableMobRenderer<EntityEnergeticSheepCommon, EntityRenderStateEnergeticSheep, SheepModel> {

    private Identifier texture;

    /**
     * Make a new instance.
     * @param renderContext The render context.
     * @param config Then config.
     */
    public RenderEntityEnergeticSheep(EntityRendererProvider.Context renderContext, ExtendedConfigCommon<?, ?, ?> config) {
        super(renderContext, new SheepModel(renderContext.bakeLayer(ModelLayers.SHEEP)), new SheepModel(renderContext.bakeLayer(ModelLayers.SHEEP_BABY)), 0.7F);
        this.addLayer((RenderLayer) new SheepWoolLayer((RenderLayerParent) this, renderContext.getModelSet()));
        this.addLayer((RenderLayer) new LayerEnergeticSheepCharge((RenderLayerParent) this, renderContext.getModelSet()));
        texture = Identifier.fromNamespaceAndPath(Reference.MOD_ID, "textures/entities/" + config.getNamedId() + ".png");
    }

    @Override
    public Identifier getTextureLocation(EntityRenderStateEnergeticSheep renderState) {
        return texture;
    }

    @Override
    public EntityRenderStateEnergeticSheep createRenderState() {
        return new EntityRenderStateEnergeticSheep();
    }

    @Override
    public void extractRenderState(EntityEnergeticSheepCommon entity, EntityRenderStateEnergeticSheep renderState, float partialTicks) {
        super.extractRenderState(entity, renderState, partialTicks);

        renderState.isPowered = entity.getEnergyClient() > 0;
        renderState.woolColor = entity.getColor();
        renderState.isSheared = entity.getEnergyClient() == 0;
        renderState.isBaby = entity.isBaby();
    }
}
