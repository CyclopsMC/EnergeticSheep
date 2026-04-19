package org.cyclops.energeticsheep.client.render.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.special.NoDataSpecialModelRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;
import org.cyclops.energeticsheep.Reference;

import java.util.function.Consumer;

/**
 * Special item model renderer that adds the charged energy-swirl overlay to energetic wool items.
 * @author rubensworks
 */
public class ItemEnergeticWoolChargeSpecialRenderer implements NoDataSpecialModelRenderer {

    public static final ModelLayerLocation MODEL_LAYER = new ModelLayerLocation(
            Identifier.fromNamespaceAndPath(Reference.MOD_ID, "energetic_wool_charge"), "main");

    private static final Identifier CHARGE_TEXTURE =
            Identifier.withDefaultNamespace("textures/entity/creeper/creeper_armor.png");

    private final WoolChargeModel model;

    public ItemEnergeticWoolChargeSpecialRenderer(ModelPart root) {
        this.model = new WoolChargeModel(root);
    }

    public static LayerDefinition createLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("cube",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(0.0F, 0.0F, 0.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.5F)),
                PartPose.ZERO);
        return LayerDefinition.create(mesh, 64, 32);
    }

    @Override
    public void getExtents(Consumer<org.joml.Vector3fc> consumer) {}

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector collector, int lightCoords, int overlay, boolean isGui, int seed) {
        Minecraft minecraft = Minecraft.getInstance();
        Level level = minecraft.level;
        float t = level != null
                ? (float) level.getGameTime() + minecraft.getDeltaTracker().getGameTimeDeltaPartialTick(true)
                : 0f;
        float xOffset = t * 0.01F;
        float yOffset = t * 0.01F;
        RenderType renderType = RenderTypes.energySwirl(CHARGE_TEXTURE, xOffset, yOffset);
        WoolChargeState state = new WoolChargeState();
        model.setupAnim(state);
        collector.order(1).submitModel(model, state, poseStack, renderType,
                lightCoords, OverlayTexture.NO_OVERLAY, -8355712, null, 0, null);
    }

    /**
     * Unbaked form used for codec-based registration and JSON item model wiring.
     */
    public static class Unbaked implements NoDataSpecialModelRenderer.Unbaked {

        public static final MapCodec<Unbaked> CODEC = MapCodec.unit(new Unbaked());

        @Override
        public MapCodec<? extends NoDataSpecialModelRenderer.Unbaked> type() {
            return CODEC;
        }

        @Override
        public NoDataSpecialModelRenderer bake(SpecialModelRenderer.BakingContext context) {
            return new ItemEnergeticWoolChargeSpecialRenderer(
                    context.entityModelSet().bakeLayer(MODEL_LAYER));
        }

    }

    private static class WoolChargeState {}

    private static class WoolChargeModel extends Model<WoolChargeState> {

        public WoolChargeModel(ModelPart root) {
            super(root, RenderTypes::entitySolid);
        }

    }

}
