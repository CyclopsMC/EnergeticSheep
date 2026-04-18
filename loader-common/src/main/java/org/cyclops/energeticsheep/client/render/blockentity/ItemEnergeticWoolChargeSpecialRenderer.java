package org.cyclops.energeticsheep.client.render.blockentity;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.renderer.special.NoDataSpecialModelRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import org.cyclops.cyclopscore.client.render.blockentity.ItemStackBlockEntityRendererBase;
import org.cyclops.energeticsheep.RegistryEntries;
import org.cyclops.energeticsheep.block.blockentity.BlockEntityEnergeticWool;

/**
 * Special item model renderer that adds the charged energy-swirl overlay to energetic wool items.
 * @author rubensworks
 */
public class ItemEnergeticWoolChargeSpecialRenderer extends ItemStackBlockEntityRendererBase {

    public ItemEnergeticWoolChargeSpecialRenderer() {
        super(() -> new BlockEntityEnergeticWool(
                RegistryEntries.BLOCK_ENTITY_TYPE_ENERGETIC_WOOL.value(),
                BlockPos.ZERO,
                Blocks.AIR.defaultBlockState()));
    }

    /**
     * Unbaked form used for codec-based registration and JSON item model wiring.
     */
    public static class EnergeticWoolChargeUnbaked implements NoDataSpecialModelRenderer.Unbaked {

        public static final MapCodec<EnergeticWoolChargeUnbaked> CODEC = MapCodec.unit(new EnergeticWoolChargeUnbaked());

        @Override
        public MapCodec<? extends NoDataSpecialModelRenderer.Unbaked> type() {
            return CODEC;
        }

        @Override
        public NoDataSpecialModelRenderer bake(SpecialModelRenderer.BakingContext context) {
            return new ItemEnergeticWoolChargeSpecialRenderer();
        }

    }

}
