package org.cyclops.energeticsheep.mixin;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.ItemModels;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;
import org.cyclops.energeticsheep.Reference;
import org.cyclops.energeticsheep.client.render.item.AnimatedEnergeticWoolChargeItemModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemModels.class)
public class MixinItemModels {

    @Shadow
    private static ExtraCodecs.LateBoundIdMapper<Identifier, MapCodec<? extends ItemModel.Unbaked>> ID_MAPPER;

    @Inject(method = "bootstrap", at = @At("RETURN"), remap = false)
    private static void registerEnergeticWoolChargeItemModel(CallbackInfo ci) {
        ID_MAPPER.put(
                Identifier.fromNamespaceAndPath(Reference.MOD_ID, "energetic_wool_charge_item"),
                AnimatedEnergeticWoolChargeItemModel.Unbaked.CODEC);
    }

}
