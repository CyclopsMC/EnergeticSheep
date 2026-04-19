package org.cyclops.energeticsheep.client;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.DepthStencilState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.TextureTransform;
import net.minecraft.resources.Identifier;

import java.util.Optional;

/**
 * Custom render types for EnergeticSheep.
 */
public class EnergeticSheepRenderTypes {

    /**
     * Energy-swirl pipeline that writes only to the RGB channels (not alpha).
     * This is identical to {@code RenderPipelines.ENERGY_SWIRL} except it uses
     * {@code ColorTargetState.WRITE_COLOR} so the destination alpha is preserved,
     * which prevents the swirl overlay from punching transparency holes in GUI items.
     */
    public static final RenderPipeline ENERGY_SWIRL_PRESERVE_ALPHA = RenderPipeline.builder(RenderPipelines.MATRICES_FOG_SNIPPET)
            .withLocation("pipeline/energeticsheep/energy_swirl_preserve_alpha")
            .withVertexShader("core/entity")
            .withFragmentShader("core/entity")
            .withShaderDefine("ALPHA_CUTOUT", 0.1F)
            .withShaderDefine("EMISSIVE")
            .withShaderDefine("NO_OVERLAY")
            .withShaderDefine("NO_CARDINAL_LIGHTING")
            .withShaderDefine("APPLY_TEXTURE_MATRIX")
            .withSampler("Sampler0")
            .withColorTargetState(new ColorTargetState(Optional.of(BlendFunction.ADDITIVE), ColorTargetState.WRITE_COLOR))
            .withCull(false)
            .withVertexFormat(DefaultVertexFormat.ENTITY, VertexFormat.Mode.QUADS)
            .withDepthStencilState(DepthStencilState.DEFAULT)
            .build();

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
                RenderSetup.builder(ENERGY_SWIRL_PRESERVE_ALPHA)
                        .withTexture("Sampler0", texture)
                        .setTextureTransform(new TextureTransform.OffsetTextureTransform(uOffset, vOffset))
                        .useLightmap()
                        .useOverlay()
                        .sortOnUpload()
                        .createRenderSetup()
        );
    }
}
