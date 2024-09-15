package org.cyclops.energeticsheep.block;

import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import org.cyclops.energeticsheep.EnergeticSheepFabric;
import org.cyclops.energeticsheep.entity.EntityEnergeticSheepCommon;
import org.cyclops.energeticsheep.entity.EntityEnergeticSheepConfigCommon;
import org.cyclops.energeticsheep.item.ItemBlockEnergeticWoolFabric;
import team.reborn.energy.api.EnergyStorage;
import team.reborn.energy.api.base.DelegatingEnergyStorage;

/**
 * @author rubensworks
 */
public class BlockEnergeticWoolConfigFabric extends BlockEnergeticWoolConfigCommon<EnergeticSheepFabric> {
    public BlockEnergeticWoolConfigFabric(DyeColor color) {
        super(
                EnergeticSheepFabric._instance,
                color,
                (eConfig, block) -> new ItemBlockEnergeticWoolFabric((BlockEnergeticWool) block, new Item.Properties())
        );
    }

    @Override
    public void onForgeRegistered() {
        super.onForgeRegistered();

        final int capacity = EntityEnergeticSheepCommon.getCapacity(((BlockEnergeticWool) getInstance()).getColor(), EntityEnergeticSheepConfigCommon.woolBaseCapacity);
        EnergyStorage.ITEM.registerForItems(
                (itemStack, context) -> new DelegatingEnergyStorage(
                        new BlockEnergeticWoolEnergyStorageFabric(context, capacity),
                        () -> context.getItemVariant().isOf(itemStack.getItem()) && context.getAmount() > 0
                ),
                getInstance()
        );
    }
}
