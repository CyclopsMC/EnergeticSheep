package org.cyclops.energeticsheep.block;

import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import org.cyclops.energeticsheep.EnergeticSheepNeoForge;
import org.cyclops.energeticsheep.entity.EntityEnergeticSheep;
import org.cyclops.energeticsheep.entity.EntityEnergeticSheepConfig;
import org.cyclops.energeticsheep.item.ItemBlockEnergeticWool;

/**
 * @author rubensworks
 */
public class BlockEnergeticWoolConfig extends BlockEnergeticWoolConfigCommon<EnergeticSheepNeoForge> {
    public BlockEnergeticWoolConfig(DyeColor color) {
        super(
                EnergeticSheepNeoForge._instance,
                color,
                (eConfig, block) -> new ItemBlockEnergeticWool((BlockEnergeticWool) block, new Item.Properties())
        );
        EnergeticSheepNeoForge._instance.getModEventBus().addListener(this::registerCapabilities);
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
