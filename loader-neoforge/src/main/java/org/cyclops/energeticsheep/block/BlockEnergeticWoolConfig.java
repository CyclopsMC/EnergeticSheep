package org.cyclops.energeticsheep.block;

import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.energeticsheep.EnergeticSheep;
import org.cyclops.energeticsheep.entity.EntityEnergeticSheep;
import org.cyclops.energeticsheep.entity.EntityEnergeticSheepConfig;
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
                        .sound(SoundType.WOOL)
                        .ignitedByLava(),
                        color),
                (eConfig, block) -> new ItemBlockEnergeticWool((BlockEnergeticWool) block, new Item.Properties())
        );
        EnergeticSheep._instance.getModEventBus().addListener(this::registerCapabilities);
    }

    public void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerItem(
                Capabilities.EnergyStorage.ITEM,
                (stack, context) -> new ItemBlockEnergeticWool.EnergyStorage(
                        EntityEnergeticSheep.getCapacity(((BlockEnergeticWool) this.getInstance()).getColor(), EntityEnergeticSheepConfig.woolBaseCapacity), stack),
                getInstance()
        );
    }

}
