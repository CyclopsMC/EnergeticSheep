package org.cyclops.energeticsheep.block;

import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;

import java.util.function.BiFunction;

/**
 * Config for {@link BlockEnergeticWool}.
 * @author rubensworks
 */
public class BlockEnergeticWoolConfigCommon<M extends IModBase> extends BlockConfigCommon<M> {

    public BlockEnergeticWoolConfigCommon(M mod, DyeColor color, BiFunction<BlockConfigCommon<M>, Block, ? extends Item> itemConstructor) {
        super(
                mod,
                color.getName() + "_energetic_wool",
                eConfig -> new BlockEnergeticWool(Block.Properties.of()
                        .mapColor(color)
                        .strength(0.8F)
                        .sound(SoundType.WOOL)
                        .ignitedByLava(),
                        color),
                itemConstructor
        );
    }

}
