package org.cyclops.energeticsheep.client;

import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.TextureTransform;
import net.minecraft.resources.Identifier;

/**
 * Custom render types for EnergeticSheep.
 */
public class EnergeticSheepRenderTypes {

    /**
     * Creates an energy-swirl {@link RenderType} that additively blends the swirl colours
     * onto the destination but leaves the destination alpha channel untouched.
     *
     * @param texture  the swirl texture
     * @param uOffset  horizontal texture scroll offset
     * @param vOffset  vertical texture scroll offset
     * @return the render type
     */
    public static RenderType energySwirlPreserveAlpha(Identifier texture, float uOffset, float vOffset) {
        return RenderType.create(
                "energeticsheep_energy_swirl",
                RenderSetup.builder(RenderPipelines.ENERGY_SWIRL)
                        .withTexture("Sampler0", texture)
                        .setTextureTransform(new TextureTransform.OffsetTextureTransform(uOffset, vOffset))
                        .useLightmap()
                        .useOverlay()
                        .sortOnUpload()
                        .createRenderSetup()
        );
    }
}
