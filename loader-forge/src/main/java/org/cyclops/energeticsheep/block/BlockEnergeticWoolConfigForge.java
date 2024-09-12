package org.cyclops.energeticsheep.block;

import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import org.cyclops.cyclopscore.RegistryEntriesCommon;
import org.cyclops.energeticsheep.EnergeticSheepForge;
import org.cyclops.energeticsheep.entity.EntityEnergeticSheepCommon;
import org.cyclops.energeticsheep.entity.EntityEnergeticSheepConfigCommon;
import org.cyclops.energeticsheep.item.ItemBlockEnergeticWoolForge;

/**
 * @author rubensworks
 */
public class BlockEnergeticWoolConfigForge extends BlockEnergeticWoolConfigCommon<EnergeticSheepForge> {
    public BlockEnergeticWoolConfigForge(DyeColor color) {
        super(
                EnergeticSheepForge._instance,
                color,
                (eConfig, block) -> new ItemBlockEnergeticWoolForge((BlockEnergeticWool) block, new Item.Properties()
                        .component(RegistryEntriesCommon.COMPONENT_ENERGY_STORAGE.value(), EntityEnergeticSheepCommon.getCapacity(color, EntityEnergeticSheepConfigCommon.woolBaseCapacity)))
        );
    }
}
