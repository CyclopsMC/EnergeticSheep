package org.cyclops.energeticsheep.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.cyclops.energeticsheep.Reference;
import org.cyclops.energeticsheep.RegistryEntries;
import org.cyclops.energeticsheep.block.blockentity.BlockEntityEnergeticWool;

/**
 * A block that can delay variables.
 * @author rubensworks
 */
public class BlockEnergeticWool extends Block implements EntityBlock {

    public static final ResourceKey<DamageType> DAMAGE_TYPE_SHOCK = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.fromNamespaceAndPath(Reference.MOD_ID, "energeticwool_shock"));

    protected static final VoxelShape COLLISION_SHAPE = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 15.0D, 15.0D);

    private final DyeColor color;

    public BlockEnergeticWool(Block.Properties properties, DyeColor color) {
        super(properties);
        this.color = color;
    }

    public DyeColor getColor() {
        return color;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return COLLISION_SHAPE;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BlockEntityEnergeticWool(RegistryEntries.BLOCK_ENTITY_TYPE_ENERGETIC_WOOL.value(), pos, state);
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier applier, boolean intersects) {
        super.entityInside(state, level, pos, entity, applier, intersects);
        entity.hurt(new DamageSource(
                level.registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(DAMAGE_TYPE_SHOCK)
        ), 1.0F);
    }

}
