package org.cyclops.energeticsheep.client.render.item;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.item.ModelRenderProperties;
import net.minecraft.client.renderer.special.NoDataSpecialModelRenderer;
import net.minecraft.client.resources.model.ResolvableModel;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.cyclops.energeticsheep.client.render.blockentity.ItemEnergeticWoolChargeSpecialRenderer;
import org.joml.Matrix4fc;

/**
 * A custom {@link ItemModel} for the energetic wool charge overlay.
 * Unlike the vanilla {@code minecraft:special} type (which only marks the render state as animated for
 * enchanted items), this model always calls {@link ItemStackRenderState#setAnimated()} so that the
 * GUI re-submits the swirl geometry every frame and the animation never freezes.
 *
 * @author rubensworks
 */
public class AnimatedEnergeticWoolChargeItemModel implements ItemModel {

    private final NoDataSpecialModelRenderer specialRenderer;
    private final ModelRenderProperties properties;
    private final Matrix4fc transformation;

    public AnimatedEnergeticWoolChargeItemModel(
            NoDataSpecialModelRenderer specialRenderer,
            ModelRenderProperties properties,
            Matrix4fc transformation) {
        this.specialRenderer = specialRenderer;
        this.properties = properties;
        this.transformation = transformation;
    }

    @Override
    public void update(
            ItemStackRenderState renderState,
            ItemStack stack,
            ItemModelResolver resolver,
            ItemDisplayContext context,
            ClientLevel level,
            ItemOwner owner,
            int seed) {
        renderState.setAnimated();
        renderState.appendModelIdentityElement(this);
        ItemStackRenderState.LayerRenderState layer = renderState.newLayer();
        layer.setExtents(ItemStackRenderState.LayerRenderState.NO_EXTENTS_SUPPLIER);
        layer.setLocalTransform(transformation);
        layer.setupSpecialModel(specialRenderer, null);
        properties.applyToLayer(layer, context);
    }

    /**
     * Unbaked form used for codec-based registration and JSON item model wiring.
     */
    public static class Unbaked implements ItemModel.Unbaked {

        public static final MapCodec<Unbaked> CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                                Identifier.CODEC.fieldOf("base").forGetter(u -> u.base))
                        .apply(instance, Unbaked::new));

        private final Identifier base;

        public Unbaked(Identifier base) {
            this.base = base;
        }

        @Override
        public MapCodec<? extends ItemModel.Unbaked> type() {
            return CODEC;
        }

        @Override
        public void resolveDependencies(ResolvableModel.Resolver resolver) {
            resolver.markDependency(base);
        }

        @Override
        public ItemModel bake(ItemModel.BakingContext context, Matrix4fc matrix) {
            NoDataSpecialModelRenderer renderer =
                    new ItemEnergeticWoolChargeSpecialRenderer.Unbaked().bake(context);
            if (renderer == null) {
                return context.missingItemModel(matrix);
            }
            var baker = context.blockModelBaker();
            var resolvedModel = baker.getModel(base);
            ModelRenderProperties props = ModelRenderProperties.fromResolvedModel(
                    baker, resolvedModel, resolvedModel.getTopTextureSlots());
            return new AnimatedEnergeticWoolChargeItemModel(renderer, props, matrix);
        }

    }

}
