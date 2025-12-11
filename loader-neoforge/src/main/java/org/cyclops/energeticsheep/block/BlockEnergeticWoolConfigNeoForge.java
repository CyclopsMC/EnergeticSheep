package org.cyclops.energeticsheep.block;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
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
                (eConfig, block) -> new ItemBlockEnergeticWoolNeoForge((BlockEnergeticWool) block, new Item.Properties()
                        .setId(ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(eConfig.getMod().getModId(), eConfig.getNamedId())))
                        .useBlockDescriptionPrefix())
        );
        EnergeticSheepNeoForge._instance.getModEventBus().addListener(this::registerCapabilities);
    }

    public void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerItem(
                Capabilities.Energy.ITEM,
                (stack, context) -> new BlockEnergeticWoolEnergyStorageNeoForge(
                        EntityEnergeticSheepCommon.getCapacity(((BlockEnergeticWool) this.getInstance()).getColor(), EntityEnergeticSheepConfigCommon.woolBaseCapacity), stack),
                getInstance()
        );
    }
}
