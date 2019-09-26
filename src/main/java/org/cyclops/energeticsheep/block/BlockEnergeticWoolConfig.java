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
                color.getTranslationKey() + "_energetic_wool",
                eConfig -> new BlockEnergeticWool(Block.Properties.create(Material.WOOL, color.getMapColor())
                        .hardnessAndResistance(0.8F)
                        .sound(SoundType.CLOTH),
                        color),
                (eConfig, block) -> new ItemBlockEnergeticWool((BlockEnergeticWool) block, new Item.Properties()
                        .group(EnergeticSheep._instance.getDefaultItemGroup()))
        );
    }

}
