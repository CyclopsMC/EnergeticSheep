package org.cyclops.energeticsheep.client.render.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;
import org.cyclops.energeticsheep.Reference;
import org.cyclops.energeticsheep.block.blockentity.BlockEntityEnergeticWool;
import org.cyclops.energeticsheep.client.render.blockentity.state.BlockEntityRenderStateEnergeticWool;

/**
 * Block entity renderer for {@link BlockEntityEnergeticWool}.
 * Renders the same energy-swirl overlay as the creeper charge effect.
 * @author rubensworks
 */
public class RenderBlockEntityEnergeticWool implements BlockEntityRenderer<BlockEntityEnergeticWool, BlockEntityRenderStateEnergeticWool> {

    public static final ModelLayerLocation MODEL_LAYER = new ModelLayerLocation(
            Identifier.fromNamespaceAndPath(Reference.MOD_ID, "energetic_wool"), "main");

    private static final Identifier CHARGE_TEXTURE =
            Identifier.withDefaultNamespace("textures/entity/creeper/creeper_armor.png");

    private final WoolBlockChargeModel model;

    public RenderBlockEntityEnergeticWool(BlockEntityRendererProvider.Context context) {
        this.model = new WoolBlockChargeModel(context.bakeLayer(MODEL_LAYER));
    }

    public static LayerDefinition createLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        // A cube spanning a full block (16x16x16 model units) with slight expansion
        root.addOrReplaceChild("cube",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(0.0F, 0.0F, 0.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.5F)),
                PartPose.ZERO);
        return LayerDefinition.create(mesh, 64, 32);
    }

    @Override
    public BlockEntityRenderStateEnergeticWool createRenderState() {
        return new BlockEntityRenderStateEnergeticWool();
    }

    @Override
    public void extractRenderState(BlockEntityEnergeticWool blockEntity,
                                   BlockEntityRenderStateEnergeticWool renderState,
                                   float partialTicks,
                                   Vec3 camera,
                                   ModelFeatureRenderer.CrumblingOverlay crumbling) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, renderState, partialTicks, camera, crumbling);
        if (blockEntity.getLevel() != null) {
            renderState.gameTime = blockEntity.getLevel().getGameTime() + partialTicks;
        }
    }

    @Override
    public void submit(BlockEntityRenderStateEnergeticWool state,
                       PoseStack poseStack,
                       SubmitNodeCollector collector,
                       CameraRenderState cameraRenderState) {
        float t = state.gameTime;
        float xOffset = t * 0.01F;
        float yOffset = t * 0.01F;
        RenderType renderType = RenderTypes.energySwirl(CHARGE_TEXTURE, xOffset, yOffset);
        model.setupAnim(state);
        collector.order(1).submitModel(model, state, poseStack, renderType,
                state.lightCoords, OverlayTexture.NO_OVERLAY, -8355712, null, 0, state.breakProgress);
    }

    /**
     * Simple model wrapping a single full-block cube for the energy swirl overlay.
     */
    public static class WoolBlockChargeModel extends Model<BlockEntityRenderStateEnergeticWool> {

        public WoolBlockChargeModel(ModelPart root) {
            super(root, RenderTypes::entitySolid);
        }

    }

}
