package org.cyclops.energeticsheep.mixin;

import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.cyclops.energeticsheep.RegistryEntries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ItemPredicate.class)
public class MixinItemPredicate {
    @Inject(method = "test", at = @At(value = "RETURN"), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private void test(ItemStack itemStack, CallbackInfoReturnable<Boolean> callback) {
        ItemPredicate itemPredicate = (ItemPredicate) (Object) this;
        if (!callback.getReturnValue() && itemStack.getItem() == RegistryEntries.ITEM_ENERGETIC_SHEARS.value() && itemPredicate.items().get().stream().anyMatch(h -> h.is(ResourceLocation.parse("minecraft:shears")))) {
            callback.setReturnValue(true);
        }
    }
}
