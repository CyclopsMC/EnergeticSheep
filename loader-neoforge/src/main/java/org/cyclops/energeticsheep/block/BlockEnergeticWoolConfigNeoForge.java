package org.cyclops.energeticsheep.block;

import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import org.cyclops.energeticsheep.EnergeticSheepNeoForge;
import org.cyclops.energeticsheep.entity.EntityEnergeticSheepCommon;
import org.cyclops.energeticsheep.entity.EntityEnergeticSheepConfigCommon;
import org.cyclops.energeticsheep.item.ItemBlockEnergeticWoolNeoForge;

/**
 * @author rubensworks
 */
public class BlockEnergeticWoolConfigNeoForge extends BlockEnergeticWoolConfigCommon<EnergeticSheepNeoForge> {
    public BlockEnergeticWoolConfigNeoForge(DyeColor color) {
        super(
                EnergeticSheepNeoForge._instance,
                color,
                (eConfig, block) -> new ItemBlockEnergeticWoolNeoForge((BlockEnergeticWool) block, new Item.Properties())
        );
        EnergeticSheepNeoForge._instance.getModEventBus().addListener(this::registerCapabilities);
    }

    public void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerItem(
                Capabilities.EnergyStorage.ITEM,
                (stack, context) -> new BlockEnergeticWoolEnergyStorageNeoForge(
                        EntityEnergeticSheepCommon.getCapacity(((BlockEnergeticWool) this.getInstance()).getColor(), EntityEnergeticSheepConfigCommon.woolBaseCapacity), stack),
                getInstance()
        );
    }
}
