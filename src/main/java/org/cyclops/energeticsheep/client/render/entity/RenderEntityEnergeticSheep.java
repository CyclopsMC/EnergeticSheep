package org.cyclops.energeticsheep.client.render.entity;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSheep;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.util.ResourceLocation;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.config.extendedconfig.MobConfig;
import org.cyclops.energeticsheep.Reference;
import org.cyclops.energeticsheep.entity.EntityEnergeticSheep;
import org.cyclops.energeticsheep.entity.layers.LayerEnergeticSheepCharge;

/**
 * Renderer for {@link EntityEnergeticSheep}.
 * 
 * @author rubensworks
 *
 */
public class RenderEntityEnergeticSheep extends RenderSheep {

    private ResourceLocation texture;

    /**
     * Make a new instance.
     * @param renderManager The render manager.
     * @param config Then config.
     */
    public RenderEntityEnergeticSheep(RenderManager renderManager, ExtendedConfig<MobConfig<EntityEnergeticSheep>> config) {
        super(renderManager);
        this.addLayer(new LayerEnergeticSheepCharge(this));
        texture = new ResourceLocation(Reference.MOD_ID, Reference.TEXTURE_PATH_ENTITIES + config.getNamedId() + ".png");
    }
    
    @Override
    protected ResourceLocation getEntityTexture(EntitySheep entity) {
        return texture;
    }

}
