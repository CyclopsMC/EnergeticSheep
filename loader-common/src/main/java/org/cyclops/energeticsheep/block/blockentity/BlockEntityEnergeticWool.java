package org.cyclops.energeticsheep.block.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.cyclops.cyclopscore.blockentity.CyclopsBlockEntity;

import javax.annotation.Nonnull;

/**
 * Block entity for the energetic wool block.
 * Carries no data; its presence enables the charged animation overlay renderer.
 * @author rubensworks
 */
public class BlockEntityEnergeticWool extends CyclopsBlockEntity {

    public BlockEntityEnergeticWool(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    @Override
    public boolean isValidBlockState(@Nonnull BlockState blockState) {
        // This is a rendering-only block entity; allow any block state (including air used for item rendering).
        return true;
    }

}
