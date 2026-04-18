package org.cyclops.energeticsheep.block.blockentity;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import org.cyclops.cyclopscore.config.extendedconfig.BlockEntityConfigCommon;
import org.cyclops.energeticsheep.EnergeticSheepFabric;
import org.cyclops.energeticsheep.block.BlockEnergeticWool;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author rubensworks
 */
public class BlockEntityEnergeticWoolConfigFabric extends BlockEntityConfigCommon<BlockEntityEnergeticWool, EnergeticSheepFabric> {

    public BlockEntityEnergeticWoolConfigFabric() {
        super(
                EnergeticSheepFabric._instance,
                "energetic_wool",
                eConfig -> {
                    List<Block> woolBlocks = BuiltInRegistries.BLOCK.stream()
                            .filter(b -> b instanceof BlockEnergeticWool)
                            .collect(Collectors.toList());
                    return FabricBlockEntityTypeBuilder
                            .create(
                                    (pos, state) -> new BlockEntityEnergeticWool(eConfig.getInstance(), pos, state),
                                    woolBlocks.toArray(new Block[0]))
                            .build();
                }
        );
    }

}
