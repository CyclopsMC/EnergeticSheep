package org.cyclops.energeticsheep.item;

import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.helper.L10NHelpers;
import org.cyclops.cyclopscore.item.IInformationProvider;
import org.cyclops.cyclopscore.item.ItemBlockMetadata;
import org.cyclops.cyclopscore.modcompat.capabilities.DefaultCapabilityProvider;
import org.cyclops.energeticsheep.entity.EntityEnergeticSheep;
import org.cyclops.energeticsheep.entity.EntityEnergeticSheepConfig;

import java.util.List;

/**
 * @author rubensworks
 */
public class ItemBlockEnergeticWool extends ItemBlockMetadata {
    /**
     * Make a new instance.
     * @param block The blockState instance.
     */
    public ItemBlockEnergeticWool(Block block) {
        super(block);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack itemStack, World world, List<String> list, ITooltipFlag flag) {
        super.addInformation(itemStack, world, list, flag);
        IEnergyStorage energyStorage = itemStack.getCapability(CapabilityEnergy.ENERGY, null);
        int amount = energyStorage.getEnergyStored();
        String line = String.format("%,d", amount) + " "
                + L10NHelpers.localize("general.energeticsheep.energy_unit.name");
        list.add(IInformationProvider.ITEM_PREFIX + line);
    }

    @Override
    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side,
                                           float hitX, float hitY, float hitZ, EnumHand hand) {
        EnumActionResult result = ItemEnergeticShears.transferEnergy(player, pos, side, hand);
        if (result == null) {
            return super.onItemUseFirst(player, world, pos, side, hitX, hitY, hitZ, hand);
        }
        return result;
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
        return new DefaultCapabilityProvider<>(() -> CapabilityEnergy.ENERGY, new EnergyStorage(
                EntityEnergeticSheep.getCapacity(EnumDyeColor.byMetadata(stack.getMetadata()),
                        EntityEnergeticSheepConfig.woolBaseCapacity), stack));
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
