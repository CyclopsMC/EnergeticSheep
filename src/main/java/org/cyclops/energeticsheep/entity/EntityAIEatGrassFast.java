package org.cyclops.energeticsheep.entity;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockStateMatcher;
import net.minecraft.entity.ai.EntityAIEatGrass;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

/**
 * A faster version of {@link EntityAIEatGrass}.
 * @author rubensworks
 */
public class EntityAIEatGrassFast extends EntityAIEatGrass {

    private static final Predicate<IBlockState> IS_TALL_GRASS = BlockStateMatcher.forBlock(Blocks.TALLGRASS)
            .where(BlockTallGrass.TYPE, Predicates.equalTo(BlockTallGrass.EnumType.GRASS));

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
