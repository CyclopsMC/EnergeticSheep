package org.cyclops.energeticsheep.item;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
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
        EnergyHandler energyStorage = itemStack.getCapability(Capabilities.Energy.ITEM, ItemAccess.forStack(itemStack));
        return energyStorage.getAmountAsInt();
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
