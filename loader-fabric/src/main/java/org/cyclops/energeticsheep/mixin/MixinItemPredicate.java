package org.cyclops.energeticsheep.mixin;

import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemInstance;
import org.cyclops.energeticsheep.RegistryEntries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ItemPredicate.class)
public class MixinItemPredicate {
    @Inject(method = "test", at = @At(value = "RETURN"), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private void test(ItemInstance itemInstance, CallbackInfoReturnable<Boolean> callback) {
        ItemPredicate itemPredicate = (ItemPredicate) (Object) this;
        if (!callback.getReturnValue() && itemInstance.is(RegistryEntries.ITEM_ENERGETIC_SHEARS.value()) && itemPredicate.items().get().stream().anyMatch(h -> h.is(Identifier.parse("minecraft:shears")))) {
            callback.setReturnValue(true);
        }
    }
}
