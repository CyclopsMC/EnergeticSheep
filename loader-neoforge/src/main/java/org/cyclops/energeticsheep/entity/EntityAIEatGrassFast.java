package org.cyclops.energeticsheep.entity;

import com.google.common.base.Predicate;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.ai.goal.EatBlockGoal;
import net.minecraft.core.BlockPos;

/**
 * A faster version of {@link EatBlockGoal}.
 * @author rubensworks
 */
public class EntityAIEatGrassFast extends EatBlockGoal {

    private static final Predicate<BlockState> IS_TALL_GRASS = (blockState) -> blockState.getBlock() == Blocks.TALL_GRASS;

    private final EntityEnergeticSheep grassEaterEntity;

    public EntityAIEatGrassFast(EntityEnergeticSheep grassEaterEntityIn) {
        super(grassEaterEntityIn);
        this.grassEaterEntity = grassEaterEntityIn;
    }

    @Override
    public boolean canUse() {
        if (!this.grassEaterEntity.isSheared()
                || this.grassEaterEntity.getRandom().nextInt(this.grassEaterEntity.isBaby() ? 10 : 200) != 0) {
            return false;
        } else {
            BlockPos blockpos = this.grassEaterEntity.getOnPos();
            if (IS_TALL_GRASS.apply(this.grassEaterEntity.level().getBlockState(blockpos))) {
                return true;
            } else  {
                return this.grassEaterEntity.level().getBlockState(blockpos.below()).getBlock() == Blocks.GRASS_BLOCK;
            }
        }
    }
}
