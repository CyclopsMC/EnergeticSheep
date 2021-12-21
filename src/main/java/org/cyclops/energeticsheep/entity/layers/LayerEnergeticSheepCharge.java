package org.cyclops.energeticsheep.entity.layers;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.SheepFurModel;
import net.minecraft.client.model.SheepModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.entity.layers.EnergySwirlLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.EntityRenderersEvent;
import org.cyclops.energeticsheep.Reference;
import org.cyclops.energeticsheep.client.render.entity.RenderEntityEnergeticSheep;
import org.cyclops.energeticsheep.entity.EntityEnergeticSheep;

/**
 * Layer renderer for the energy charge.
 * @author rubensworks
 */
public class LayerEnergeticSheepCharge extends EnergySwirlLayer<EntityEnergeticSheep, SheepModel<EntityEnergeticSheep>> {

    public static ModelLayerLocation MODEL_LAYER_FUR_SCALED = new ModelLayerLocation(new ResourceLocation(Reference.MOD_ID, "sheep"), "fur");

    private static final ResourceLocation CHARGE_TEXTURE =
            new ResourceLocation("textures/entity/creeper/creeper_armor.png");

    private final SheepFurModel sheepModel;

    public LayerEnergeticSheepCharge(RenderEntityEnergeticSheep renderer, EntityModelSet entityModelSet) {
        super(renderer);
        sheepModel = new SheepFurModel<EntityEnergeticSheep>(entityModelSet.bakeLayer(MODEL_LAYER_FUR_SCALED));
    }

    public static void loadLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(MODEL_LAYER_FUR_SCALED, () -> LayerEnergeticSheepCharge.createFurLayer(1.05F));
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
}
