package org.cyclops.energeticsheep.mixin;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderers;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;
import org.cyclops.energeticsheep.Reference;
import org.cyclops.energeticsheep.client.render.blockentity.ItemEnergeticWoolChargeSpecialRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SpecialModelRenderers.class)
public class MixinSpecialModelRenderers {

    @Shadow
    private static ExtraCodecs.LateBoundIdMapper<Identifier, MapCodec<? extends SpecialModelRenderer.Unbaked<?>>> ID_MAPPER;

    @Inject(method = "bootstrap", at = @At("RETURN"))
    private static void registerEnergeticWoolChargeRenderer(CallbackInfo ci) {
        ID_MAPPER.put(
                Identifier.fromNamespaceAndPath(Reference.MOD_ID, "energetic_wool_charge"),
                ItemEnergeticWoolChargeSpecialRenderer.Unbaked.CODEC
        );
    }

}
