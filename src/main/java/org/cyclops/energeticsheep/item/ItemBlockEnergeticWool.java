package org.cyclops.energeticsheep.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import org.cyclops.cyclopscore.helper.L10NHelpers;
import org.cyclops.cyclopscore.item.IInformationProvider;
import org.cyclops.cyclopscore.modcompat.capabilities.DefaultCapabilityProvider;
import org.cyclops.energeticsheep.block.BlockEnergeticWool;
import org.cyclops.energeticsheep.entity.EntityEnergeticSheep;
import org.cyclops.energeticsheep.entity.EntityEnergeticSheepConfig;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author rubensworks
 */
public class ItemBlockEnergeticWool extends BlockItem {

    public ItemBlockEnergeticWool(BlockEnergeticWool block, Item.Properties builder) {
        super(block, builder);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(itemStack, worldIn, tooltip, flagIn);
        if (CapabilityEnergy.ENERGY != null) { // Can be null during item registration, when caps are not registered yet
            IEnergyStorage energyStorage = itemStack.getCapability(CapabilityEnergy.ENERGY).orElse(null);
            int amount = energyStorage.getEnergyStored();
            String line = String.format("%,d", amount) + " "
                    + L10NHelpers.localize("general.energeticsheep.energy_unit");
            tooltip.add(Component.literal(line).withStyle(IInformationProvider.ITEM_PREFIX));
        }
        L10NHelpers.addOptionalInfo(tooltip, "block.energeticsheep.energetic_wool");
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        InteractionResult result = ItemEnergeticShears.transferEnergy(context.getPlayer(), context.getClickedPos(), context.getClickedFace(), context.getHand());
        if (result == null) {
            return super.onItemUseFirst(stack, context);
        }
        return result;
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag nbt) {
        return new DefaultCapabilityProvider<>(() -> CapabilityEnergy.ENERGY, new EnergyStorage(
                EntityEnergeticSheep.getCapacity(((BlockEnergeticWool) this.getBlock()).getColor(), EntityEnergeticSheepConfig.woolBaseCapacity), stack));
    }

    public static class EnergyStorage implements IEnergyStorage {

        private final int capacity;
        private final ItemStack itemStack;

        public EnergyStorage(int capacity, ItemStack itemStack) {
            this.capacity = capacity;
            this.itemStack = itemStack;
        }

        @Override
        public int receiveEnergy(int maxReceive, boolean simulate) {
            return 0;
        }

        @Override
        public int extractEnergy(int maxExtract, boolean simulate) {
            if (maxExtract >= this.capacity) {
                if (!simulate) {
                    this.itemStack.shrink(1);
                }
                return this.capacity;
            }
            return 0;
        }

        @Override
        public int getEnergyStored() {
            return this.capacity;
        }

        @Override
        public int getMaxEnergyStored() {
            return this.capacity;
        }

        @Override
        public boolean canExtract() {
            return true;
        }

        @Override
        public boolean canReceive() {
            return false;
        }
    }
}
