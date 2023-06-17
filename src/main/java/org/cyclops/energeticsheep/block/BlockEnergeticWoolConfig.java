package org.cyclops.energeticsheep.block;

import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.energeticsheep.EnergeticSheep;
import org.cyclops.energeticsheep.item.ItemBlockEnergeticWool;

/**
 * Config for {@link BlockEnergeticWool}.
 * @author rubensworks
 */
public class BlockEnergeticWoolConfig extends BlockConfig {

    public BlockEnergeticWoolConfig(DyeColor color) {
        super(
            EnergeticSheep._instance,
                color.getName() + "_energetic_wool",
                eConfig -> new BlockEnergeticWool(Block.Properties.of()
                        .mapColor(color)
                        .strength(0.8F)
                        .sound(SoundType.WOOL),
                        color),
                (eConfig, block) -> new ItemBlockEnergeticWool((BlockEnergeticWool) block, new Item.Properties())
        );
    }

}
