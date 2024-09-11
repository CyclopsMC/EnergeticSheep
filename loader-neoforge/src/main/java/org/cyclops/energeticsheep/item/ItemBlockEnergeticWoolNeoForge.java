package org.cyclops.energeticsheep.item;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.cyclops.energeticsheep.block.BlockEnergeticWool;

/**
 * @author rubensworks
 */
public class ItemBlockEnergeticWoolNeoForge extends ItemBlockEnergeticWoolCommon {
    public ItemBlockEnergeticWoolNeoForge(BlockEnergeticWool block, Properties builder) {
        super(block, builder);
    }

    @Override
    protected int getEnergyStored(ItemStack itemStack) {
        IEnergyStorage energyStorage = itemStack.getCapability(Capabilities.EnergyStorage.ITEM);
        return energyStorage.getEnergyStored();
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        InteractionResult result = ItemEnergeticShearsNeoForge.transferEnergy(context.getPlayer(), context.getClickedPos(), context.getClickedFace(), context.getHand());
        if (result == null) {
            return super.onItemUseFirst(stack, context);
        }
        return result;
    }
}
