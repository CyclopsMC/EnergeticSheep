package org.cyclops.energeticsheep.entity.layers;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.entity.model.SheepModel;
import net.minecraft.client.renderer.entity.model.SheepWoolModel;
import net.minecraft.util.ResourceLocation;
import org.cyclops.energeticsheep.client.render.entity.RenderEntityEnergeticSheep;
import org.cyclops.energeticsheep.entity.EntityEnergeticSheep;

/**
 * Layer renderer for the energy charge.
 * @author rubensworks
 */
public class LayerEnergeticSheepCharge extends LayerRenderer<EntityEnergeticSheep, SheepModel<EntityEnergeticSheep>> {

    private static final ResourceLocation CHARGE_TEXTURE =
            new ResourceLocation("textures/entity/creeper/creeper_armor.png");

    private final RenderEntityEnergeticSheep renderer;
    private final SheepWoolModelScaled sheepModel = new SheepWoolModelScaled(1.05F);

    public LayerEnergeticSheepCharge(RenderEntityEnergeticSheep renderer) {
        super(renderer);
        this.renderer = renderer;
    }

    @Override
    public void render(EntityEnergeticSheep sheep, float limbSwing, float limbSwingAmount, float partialTicks,
                              float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        int energy = sheep.getEnergyClient();
        if (energy > 0) {
            boolean flag = sheep.isInvisible();
            GlStateManager.depthMask(!flag);
            this.renderer.bindTexture(CHARGE_TEXTURE);
            GlStateManager.matrixMode(5890);
            GlStateManager.loadIdentity();
            float offset = (float)sheep.ticksExisted + partialTicks;
            GlStateManager.translatef(offset * 0.01F, offset * 0.01F, 0.0F);
            GlStateManager.matrixMode(5888);
            GlStateManager.enableBlend();
            GlStateManager.color4f(0.02F, 0.69F, 0.67F, 1.0F);
            GlStateManager.disableLighting();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
            this.getEntityModel().setModelAttributes(this.sheepModel);
            GameRenderer gamerenderer = Minecraft.getInstance().gameRenderer;
            gamerenderer.setupFogColor(true);
            this.sheepModel.render(sheep, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            gamerenderer.setupFogColor(false);
            GlStateManager.matrixMode(5890);
            GlStateManager.loadIdentity();
            GlStateManager.matrixMode(5888);
            GlStateManager.enableLighting();
            GlStateManager.disableBlend();
            GlStateManager.depthMask(true);
        }
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }

    public static class SheepWoolModelScaled extends SheepWoolModel {

        public SheepWoolModelScaled(float scale) {
            // head
            this.headModel = new RendererModel(this, 0, 0);
            this.headModel.addBox(-3.0F, -4.0F, -4.0F, 6, 6, 6, 0.6F * scale);
            this.headModel.setRotationPoint(0.0F, 6.0F, -8.0F);
            // body
            this.field_78148_b = new RendererModel(this, 28, 8);
            this.field_78148_b.addBox(-4.0F, -10.0F, -7.0F, 8, 16, 6, 1.75F * scale);
            this.field_78148_b.setRotationPoint(0.0F, 5.0F, 2.0F);
            float f = 0.5F;
            // leg 1
            this.field_78149_c = new RendererModel(this, 0, 16);
            this.field_78149_c.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, 0.5F * scale);
            this.field_78149_c.setRotationPoint(-3.0F, 12.0F, 7.0F);
            // leg 2
            this.field_78146_d = new RendererModel(this, 0, 16);
            this.field_78146_d.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, 0.5F * scale);
            this.field_78146_d.setRotationPoint(3.0F, 12.0F, 7.0F);
            // leg 3
            this.field_78147_e = new RendererModel(this, 0, 16);
            this.field_78147_e.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, 0.5F * scale);
            this.field_78147_e.setRotationPoint(-3.0F, 12.0F, -5.0F);
            // leg 4
            this.field_78144_f = new RendererModel(this, 0, 16);
            this.field_78144_f.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, 0.5F * scale);
            this.field_78144_f.setRotationPoint(3.0F, 12.0F, -5.0F);
        }

    }
}
