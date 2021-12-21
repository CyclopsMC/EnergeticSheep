package org.cyclops.energeticsheep.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
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
                eConfig -> new BlockEnergeticWool(Block.Properties.of(Material.WOOL, color.getMaterialColor())
                        .strength(0.8F)
                        .sound(SoundType.WOOL),
                        color),
                (eConfig, block) -> new ItemBlockEnergeticWool((BlockEnergeticWool) block, new Item.Properties()
                        .tab(EnergeticSheep._instance.getDefaultItemGroup()))
        );
    }

}
