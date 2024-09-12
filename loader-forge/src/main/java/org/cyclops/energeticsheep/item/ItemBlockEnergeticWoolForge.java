package org.cyclops.energeticsheep.item;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import org.cyclops.cyclopscore.RegistryEntriesCommon;
import org.cyclops.energeticsheep.block.BlockEnergeticWool;

/**
 * @author rubensworks
 */
public class ItemBlockEnergeticWoolForge extends ItemBlockEnergeticWoolCommon {
    public ItemBlockEnergeticWoolForge(BlockEnergeticWool block, Properties builder) {
        super(block, builder);
    }

    @Override
    protected int getEnergyStored(ItemStack itemStack) {
        return itemStack.get(RegistryEntriesCommon.COMPONENT_ENERGY_STORAGE.value());
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        InteractionResult result = ItemEnergeticShearsForge.transferEnergy(context.getPlayer(), context.getClickedPos(), context.getClickedFace(), context.getHand());
        if (result == null) {
            return super.onItemUseFirst(stack, context);
        }
        return result;
    }

    // TODO: restore when item caps are restored in Forge
//    @Override
//    public ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag nbt) {
//        return new DefaultCapabilityProvider<>(() -> ForgeCapabilities.ENERGY, new EnergyStorage(
//                EntityEnergeticSheep.getCapacity(((BlockEnergeticWool) this.getBlock()).getColor(), EntityEnergeticSheepConfig.woolBaseCapacity), stack));
//    }
}
