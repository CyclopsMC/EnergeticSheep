package org.cyclops.energeticsheep.entity.layers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelSheep1;
import net.minecraft.client.model.ModelSheep2;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import org.cyclops.energeticsheep.client.render.entity.RenderEntityEnergeticSheep;
import org.cyclops.energeticsheep.entity.EntityEnergeticSheep;
import org.lwjgl.opengl.GL11;

/**
 * Layer renderer for the energy charge.
 * @author rubensworks
 */
public class LayerEnergeticSheepCharge implements LayerRenderer<EntityEnergeticSheep> {

    private static final ResourceLocation CHARGE_TEXTURE =
            new ResourceLocation("textures/entity/creeper/creeper_armor.png");

    private final RenderEntityEnergeticSheep renderer;
    private final ModelSheepScaled sheepModel = new ModelSheepScaled(1.05F);

    public LayerEnergeticSheepCharge(RenderEntityEnergeticSheep renderer) {
        this.renderer = renderer;
    }

    @Override
    public void doRenderLayer(EntityEnergeticSheep sheep, float limbSwing, float limbSwingAmount, float partialTicks,
                              float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        int energy = sheep.getEnergyClient();
        if (energy > 0) {
            boolean flag = sheep.isInvisible();
            GlStateManager.depthMask(!flag);
            this.renderer.bindTexture(CHARGE_TEXTURE);
            GlStateManager.matrixMode(5890);
            GlStateManager.loadIdentity();
            float offset = (float)sheep.ticksExisted + partialTicks;
            GlStateManager.translate(offset * 0.01F, offset * 0.01F, 0.0F);
            GlStateManager.matrixMode(5888);
            GlStateManager.enableBlend();
            GlStateManager.color(0.02F, 0.69F, 0.67F, 1.0F);
            GlStateManager.disableLighting();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
            this.sheepModel.setModelAttributes(this.renderer.getMainModel());
            Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
            this.sheepModel.render(sheep, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
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

    public static class ModelSheepScaled extends ModelSheep1 {

        public ModelSheepScaled(float scale) {
            this.head = new ModelRenderer(this, 0, 0);
            this.head.addBox(-3.0F, -4.0F, -4.0F, 6, 6, 6, 0.6F * scale);
            this.head.setRotationPoint(0.0F, 6.0F, -8.0F);
            this.body = new ModelRenderer(this, 28, 8);
            this.body.addBox(-4.0F, -10.0F, -7.0F, 8, 16, 6, 1.75F * scale);
            this.body.setRotationPoint(0.0F, 5.0F, 2.0F);
            float f = 0.5F;
            this.leg1 = new ModelRenderer(this, 0, 16);
            this.leg1.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, 0.5F * scale);
            this.leg1.setRotationPoint(-3.0F, 12.0F, 7.0F);
            this.leg2 = new ModelRenderer(this, 0, 16);
            this.leg2.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, 0.5F * scale);
            this.leg2.setRotationPoint(3.0F, 12.0F, 7.0F);
            this.leg3 = new ModelRenderer(this, 0, 16);
            this.leg3.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, 0.5F * scale);
            this.leg3.setRotationPoint(-3.0F, 12.0F, -5.0F);
            this.leg4 = new ModelRenderer(this, 0, 16);
            this.leg4.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, 0.5F * scale);
            this.leg4.setRotationPoint(3.0F, 12.0F, -5.0F);
        }

    }
}
