package org.cyclops.energeticsheep.entity.layers;

import net.minecraft.client.model.SheepModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EnergySwirlLayer;
import net.minecraft.client.renderer.entity.state.SheepRenderState;
import net.minecraft.resources.ResourceLocation;
import org.cyclops.energeticsheep.Reference;
import org.cyclops.energeticsheep.client.render.entity.state.EntityRenderStateEnergeticSheep;

/**
 * Layer renderer for the energy charge.
 * @author rubensworks
 */
public class LayerEnergeticSheepCharge extends EnergySwirlLayer<SheepRenderState, SheepModel> {

    public static ModelLayerLocation MODEL_LAYER_FUR_SCALED = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "sheep"), "fur");

    private static final ResourceLocation CHARGE_TEXTURE =
            ResourceLocation.withDefaultNamespace("textures/entity/creeper/creeper_armor.png");

    private final SheepModel sheepModel;

    public LayerEnergeticSheepCharge(RenderLayerParent<SheepRenderState, SheepModel> renderer, EntityModelSet entityModelSet) {
        super(renderer);
        sheepModel = new SheepModel(entityModelSet.bakeLayer(MODEL_LAYER_FUR_SCALED));
    }

    public static LayerDefinition createFurLayer(float scale) {
        // Adapted from SheepFurModel::createFurLayer
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("head",
                CubeListBuilder.create().texOffs(0, 0)
                    .addBox(-3.0F, -4.0F, -4.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.6F * scale)),
                PartPose.offset(0.0F, 6.0F, -8.0F));
        partdefinition.addOrReplaceChild("body",
                CubeListBuilder.create().texOffs(28, 8)
                        .addBox(-4.0F, -10.0F, -7.0F, 8.0F, 16.0F, 6.0F, new CubeDeformation(1.75F * scale)),
                PartPose.offsetAndRotation(0.0F, 5.0F, 2.0F, ((float)Math.PI / 2F), 0.0F, 0.0F));
        CubeListBuilder cubelistbuilder = CubeListBuilder.create()
                .texOffs(0, 16)
                .addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.5F * scale));
        partdefinition.addOrReplaceChild("right_hind_leg", cubelistbuilder, PartPose.offset(-3.0F, 12.0F, 7.0F));
        partdefinition.addOrReplaceChild("left_hind_leg", cubelistbuilder, PartPose.offset(3.0F, 12.0F, 7.0F));
        partdefinition.addOrReplaceChild("right_front_leg", cubelistbuilder, PartPose.offset(-3.0F, 12.0F, -5.0F));
        partdefinition.addOrReplaceChild("left_front_leg", cubelistbuilder, PartPose.offset(3.0F, 12.0F, -5.0F));
        return LayerDefinition.create(meshdefinition, 64, 32);
    }

    @Override
    protected boolean isPowered(SheepRenderState renderStateEnergeticSheep) {
        return ((EntityRenderStateEnergeticSheep) renderStateEnergeticSheep).isPowered;
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
    protected SheepModel model() {
        return this.sheepModel;
    }
}
