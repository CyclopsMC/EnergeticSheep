package org.cyclops.energeticsheep.entity.layers;

import net.minecraft.client.renderer.entity.layers.EnergyLayer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.SheepModel;
import net.minecraft.client.renderer.entity.model.SheepWoolModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import org.cyclops.energeticsheep.client.render.entity.RenderEntityEnergeticSheep;
import org.cyclops.energeticsheep.entity.EntityEnergeticSheep;

/**
 * Layer renderer for the energy charge.
 * @author rubensworks
 */
public class LayerEnergeticSheepCharge extends EnergyLayer<EntityEnergeticSheep, SheepModel<EntityEnergeticSheep>> {

    private static final ResourceLocation CHARGE_TEXTURE =
            new ResourceLocation("textures/entity/creeper/creeper_armor.png");

    private final RenderEntityEnergeticSheep renderer;
    private final SheepWoolModelScaled sheepModel = new SheepWoolModelScaled(1.05F);

    public LayerEnergeticSheepCharge(RenderEntityEnergeticSheep renderer) {
        super(renderer);
        this.renderer = renderer;
    }

    @Override
    protected float xOffset(float v) {
        return v * 0.01F;
    }

    @Override
    protected ResourceLocation getTextureLocation() {
        return CHARGE_TEXTURE;
    }

    @Override
    protected EntityModel<EntityEnergeticSheep> model() {
        return this.sheepModel;
    }

    public static class SheepWoolModelScaled extends SheepWoolModel {

        public SheepWoolModelScaled(float scale) {
            // head
            this.head = new ModelRenderer(this, 0, 0);
            this.head.addBox(-3.0F, -4.0F, -4.0F, 6, 6, 6, 0.6F * scale);
            this.head.setPos(0.0F, 6.0F, -8.0F);
            // body
            this.body = new ModelRenderer(this, 28, 8);
            this.body.addBox(-4.0F, -10.0F, -7.0F, 8, 16, 6, 1.75F * scale);
            this.body.setPos(0.0F, 5.0F, 2.0F);
            float f = 0.5F;
            // leg 1
            this.leg0 = new ModelRenderer(this, 0, 16);
            this.leg0.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, 0.5F * scale);
            this.leg0.setPos(-3.0F, 12.0F, 7.0F);
            // leg 2
            this.leg1 = new ModelRenderer(this, 0, 16);
            this.leg1.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, 0.5F * scale);
            this.leg1.setPos(3.0F, 12.0F, 7.0F);
            // leg 3
            this.leg2 = new ModelRenderer(this, 0, 16);
            this.leg2.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, 0.5F * scale);
            this.leg2.setPos(-3.0F, 12.0F, -5.0F);
            // leg 4
            this.leg3 = new ModelRenderer(this, 0, 16);
            this.leg3.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, 0.5F * scale);
            this.leg3.setPos(3.0F, 12.0F, -5.0F);
        }

    }
}
