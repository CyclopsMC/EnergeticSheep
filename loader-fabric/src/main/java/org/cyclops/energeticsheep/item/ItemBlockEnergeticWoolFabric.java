package org.cyclops.energeticsheep.item;

import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import org.cyclops.energeticsheep.block.BlockEnergeticWool;
import team.reborn.energy.api.EnergyStorage;

/**
 * @author rubensworks
 */
public class ItemBlockEnergeticWoolFabric extends ItemBlockEnergeticWoolCommon {
    public ItemBlockEnergeticWoolFabric(BlockEnergeticWool block, Properties builder) {
        super(block, builder);
    }

    @Override
    protected String getEnergyUnitUnlocalized() {
        return "general.energeticsheep.energy_unit_fabric";
    }

    @Override
    protected int getEnergyStored(ItemStack itemStack) {
        EnergyStorage energyStorage = EnergyStorage.ITEM.find(itemStack, ContainerItemContext.withConstant(itemStack));
        return (int) energyStorage.getAmount();
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        InteractionResult result = ItemEnergeticShearsFabric.transferEnergy(context.getPlayer(), context.getClickedPos(), context.getClickedFace(), context.getHand());
        if (result == null) {
            return super.useOn(context);
        }
        return result;
    }
}
