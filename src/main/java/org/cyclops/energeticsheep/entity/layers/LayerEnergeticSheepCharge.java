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
    protected float func_225634_a_(float v) {
        return v * 0.01F;
    }

    @Override
    protected ResourceLocation func_225633_a_() {
        return CHARGE_TEXTURE;
    }

    @Override
    protected EntityModel<EntityEnergeticSheep> func_225635_b_() {
        return this.sheepModel;
    }

    public static class SheepWoolModelScaled extends SheepWoolModel {

        public SheepWoolModelScaled(float scale) {
            // head
            this.headModel = new ModelRenderer(this, 0, 0);
            this.headModel.addBox(-3.0F, -4.0F, -4.0F, 6, 6, 6, 0.6F * scale);
            this.headModel.setRotationPoint(0.0F, 6.0F, -8.0F);
            // body
            this.body = new ModelRenderer(this, 28, 8);
            this.body.addBox(-4.0F, -10.0F, -7.0F, 8, 16, 6, 1.75F * scale);
            this.body.setRotationPoint(0.0F, 5.0F, 2.0F);
            float f = 0.5F;
            // leg 1
            this.legBackRight = new ModelRenderer(this, 0, 16);
            this.legBackRight.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, 0.5F * scale);
            this.legBackRight.setRotationPoint(-3.0F, 12.0F, 7.0F);
            // leg 2
            this.legBackLeft = new ModelRenderer(this, 0, 16);
            this.legBackLeft.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, 0.5F * scale);
            this.legBackLeft.setRotationPoint(3.0F, 12.0F, 7.0F);
            // leg 3
            this.legFrontRight = new ModelRenderer(this, 0, 16);
            this.legFrontRight.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, 0.5F * scale);
            this.legFrontRight.setRotationPoint(-3.0F, 12.0F, -5.0F);
            // leg 4
            this.legFrontLeft = new ModelRenderer(this, 0, 16);
            this.legFrontLeft.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, 0.5F * scale);
            this.legFrontLeft.setRotationPoint(3.0F, 12.0F, -5.0F);
        }

    }
}
