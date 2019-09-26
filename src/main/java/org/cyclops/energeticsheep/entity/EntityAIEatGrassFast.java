package org.cyclops.energeticsheep.entity;

import com.google.common.base.Predicate;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.ai.goal.EatGrassGoal;
import net.minecraft.util.math.BlockPos;

/**
 * A faster version of {@link EatGrassGoal}.
 * @author rubensworks
 */
public class EntityAIEatGrassFast extends EatGrassGoal {

    private static final Predicate<BlockState> IS_TALL_GRASS = (blockState) -> blockState.getBlock() == Blocks.TALL_GRASS;

    private final EntityEnergeticSheep grassEaterEntity;

    public EntityAIEatGrassFast(EntityEnergeticSheep grassEaterEntityIn) {
        super(grassEaterEntityIn);
        this.grassEaterEntity = grassEaterEntityIn;
    }

    @Override
    public boolean shouldExecute() {
        if (!this.grassEaterEntity.getSheared()
                || this.grassEaterEntity.getRNG().nextInt(this.grassEaterEntity.isChild() ? 10 : 200) != 0) {
            return false;
        } else {
            BlockPos blockpos = new BlockPos(this.grassEaterEntity.posX, this.grassEaterEntity.posY, this.grassEaterEntity.posZ);
            if (IS_TALL_GRASS.apply(this.grassEaterEntity.world.getBlockState(blockpos))) {
                return true;
            } else  {
                return this.grassEaterEntity.world.getBlockState(blockpos.down()).getBlock() == Blocks.GRASS;
            }
        }
    }
}
