package org.cyclops.energeticsheep.client.render.entity;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.layers.SheepWoolLayer;
import net.minecraft.client.renderer.entity.model.SheepModel;
import net.minecraft.util.ResourceLocation;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.energeticsheep.Reference;
import org.cyclops.energeticsheep.entity.EntityEnergeticSheep;
import org.cyclops.energeticsheep.entity.layers.LayerEnergeticSheepCharge;

/**
 * Renderer for {@link EntityEnergeticSheep}.
 * 
 * @author rubensworks
 *
 */
public class RenderEntityEnergeticSheep extends MobRenderer<EntityEnergeticSheep, SheepModel<EntityEnergeticSheep>> {

    private ResourceLocation texture;

    /**
     * Make a new instance.
     * @param renderManager The render manager.
     * @param config Then config.
     */
    public RenderEntityEnergeticSheep(EntityRendererManager renderManager, ExtendedConfig<?, ?> config) {
        super(renderManager, new SheepModel<>(), 0.7F);
        this.addLayer((LayerRenderer) new SheepWoolLayer((MobRenderer) this));
        this.addLayer(new LayerEnergeticSheepCharge(this));
        texture = new ResourceLocation(Reference.MOD_ID, Reference.TEXTURE_PATH_ENTITIES + config.getNamedId() + ".png");
    }
    
    @Override
    public ResourceLocation getTextureLocation(EntityEnergeticSheep entity) {
        return texture;
    }

}
