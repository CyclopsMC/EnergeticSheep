package org.cyclops.energeticsheep.block.blockentity;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.cyclops.cyclopscore.config.extendedconfig.BlockEntityConfigCommon;
import org.cyclops.energeticsheep.EnergeticSheepNeoForge;
import org.cyclops.energeticsheep.block.BlockEnergeticWool;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author rubensworks
 */
public class BlockEntityEnergeticWoolConfigNeoForge extends BlockEntityConfigCommon<BlockEntityEnergeticWool, EnergeticSheepNeoForge> {

    public BlockEntityEnergeticWoolConfigNeoForge() {
        super(
                EnergeticSheepNeoForge._instance,
                "energetic_wool",
                eConfig -> {
                    Set<Block> woolBlocks = BuiltInRegistries.BLOCK.stream()
                            .filter(b -> b instanceof BlockEnergeticWool)
                            .collect(Collectors.toUnmodifiableSet());
                    return new BlockEntityType<>(
                            (pos, state) -> new BlockEntityEnergeticWool(eConfig.getInstance(), pos, state),
                            woolBlocks);
                }
        );
    }

}
