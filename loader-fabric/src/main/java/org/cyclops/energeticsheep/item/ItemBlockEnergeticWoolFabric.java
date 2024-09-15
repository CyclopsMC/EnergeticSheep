package org.cyclops.energeticsheep.item;

import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.minecraft.world.item.ItemStack;
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
    protected int getEnergyStored(ItemStack itemStack) {
        EnergyStorage energyStorage = EnergyStorage.ITEM.find(itemStack, ContainerItemContext.withConstant(itemStack));
        return (int) energyStorage.getAmount();
    }

    // TODO: impl interacting with blocks with energy
//    @Override
//    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
//        InteractionResult result = ItemEnergeticShearsFabric.transferEnergy(context.getPlayer(), context.getClickedPos(), context.getClickedFace(), context.getHand());
//        if (result == null) {
//            return super.onItemUseFirst(stack, context);
//        }
//        return result;
//    }
}
