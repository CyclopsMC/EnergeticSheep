package org.cyclops.energeticsheep.item;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.config.configurable.IConfigurableItem;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.cyclopscore.helper.L10NHelpers;
import org.cyclops.cyclopscore.helper.TileHelpers;
import org.cyclops.cyclopscore.item.IInformationProvider;
import org.cyclops.cyclopscore.modcompat.capabilities.DefaultCapabilityProvider;
import org.cyclops.energeticsheep.capability.energystorage.EnergyStorageItem;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

/**
 * Can shear energy off energetic shears.
 * @author rubensworks
 *
 */
public class ItemEnergeticShears extends ItemShears implements IConfigurableItem {

    protected ItemConfig eConfig = null;

    private static ItemEnergeticShears _instance = null;
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static ItemEnergeticShears getInstance() {
        return _instance;
    }

    public ItemEnergeticShears(ExtendedConfig<ItemConfig> eConfig) {
        this.setConfig((ItemConfig)eConfig);
        this.setUnlocalizedName(eConfig.getUnlocalizedName());
        this.setMaxDamage(0);
    }

    private void setConfig(ItemConfig eConfig) {
        this.eConfig = eConfig;
    }

    @Override
    public ItemConfig getConfig() {
        return eConfig;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack itemStack, World world, List<String> list, ITooltipFlag flag) {
        super.addInformation(itemStack, world, list, flag);
        IEnergyStorage energyStorage = getEnergyStorage(itemStack);
        int amount = energyStorage.getEnergyStored();
        int capacity = energyStorage.getMaxEnergyStored();
        String line = String.format("%,d", amount) + " / " + String.format("%,d", capacity)
                + " " + L10NHelpers.localize("general.energeticsheep.energy_unit.name");
        list.add(IInformationProvider.ITEM_PREFIX + line);
        L10NHelpers.addOptionalInfo(list, getUnlocalizedName(itemStack));
    }

    public static EnumActionResult transferEnergy(EntityPlayer player, BlockPos pos, EnumFacing side, EnumHand hand) {
        World worldIn = player.world;
        if (!player.isSneaking()) {
            IEnergyStorage energyTarget = TileHelpers.getCapability(worldIn, pos, side, CapabilityEnergy.ENERGY);
            if (energyTarget != null) {
                ItemStack itemStack = player.getHeldItem(hand);
                IEnergyStorage energyItem = itemStack.getCapability(CapabilityEnergy.ENERGY, null);
                if (energyItem != null) {
                    return energyTarget.receiveEnergy(
                            energyItem.extractEnergy(
                                    energyTarget.receiveEnergy(
                                            energyItem.extractEnergy(ItemEnergeticShearsConfig.usageTransferAmount, true),
                                            true),
                                    worldIn.isRemote),
                            worldIn.isRemote) > 0 ? EnumActionResult.SUCCESS : EnumActionResult.FAIL;
                }
            }
        }
        return null;
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
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return oldStack.getItem() != newStack.getItem();
    }

    protected IEnergyStorage getEnergyStorage(ItemStack itemStack) {
        return itemStack.getCapability(CapabilityEnergy.ENERGY, null);
    }

    protected boolean canShear(ItemStack itemStack) {
        return getEnergyStorage(itemStack).getEnergyStored() > ItemEnergeticShearsConfig.shearConsumption;
    }

    protected void consumeOnShear(ItemStack itemStack) {
        IEnergyStorage energyStorage = getEnergyStorage(itemStack);
        energyStorage.extractEnergy(ItemEnergeticShearsConfig.shearConsumption, false);
    }

    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, EntityPlayer player) {
        if (player.world.isRemote || player.capabilities.isCreativeMode || !canShear(itemstack)) {
            return false;
        }
        Block block = player.world.getBlockState(pos).getBlock();
        if (block instanceof IShearable) {
            IShearable target = (net.minecraftforge.common.IShearable)block;
            if (target.isShearable(itemstack, player.world, pos)) {
                List<ItemStack> drops = target.onSheared(itemstack, player.world, pos,
                        EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, itemstack));
                Random rand = new java.util.Random();

                for (ItemStack stack : drops) {
                    float f = 0.7F;
                    double d  = (double)(rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
                    double d1 = (double)(rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
                    double d2 = (double)(rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
                    EntityItem entityitem = new EntityItem(player.world, (double)pos.getX() + d, (double)pos.getY() + d1, (double)pos.getZ() + d2, stack);
                    entityitem.setDefaultPickupDelay();
                    player.world.spawnEntity(entityitem);
                }

                consumeOnShear(itemstack);
                player.addStat(net.minecraft.stats.StatList.getBlockStats(block));
                player.world.setBlockState(pos, Blocks.AIR.getDefaultState(), 11);
                return true;
            }
        }
        return false;
    }

    @Override
    public float getDestroySpeed(ItemStack itemStack, IBlockState state) {
        float factor = canShear(itemStack) ? 1.5F : 0.1F;
        float superSpeed = super.getDestroySpeed(itemStack, state);
        if (superSpeed != 1.0F) {
            return superSpeed * factor;
        }
        return superSpeed;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack itemStack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving) {
        if (!worldIn.isRemote) {
            consumeOnShear(itemStack);
        }

        Block block = state.getBlock();
        if (block instanceof net.minecraftforge.common.IShearable) return true;
        return state.getMaterial() != Material.LEAVES && block != Blocks.WEB && block != Blocks.TALLGRASS && block != Blocks.VINE && block != Blocks.TRIPWIRE && block != Blocks.WOOL ? super.onBlockDestroyed(itemStack, worldIn, state, pos, entityLiving) : true;
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack itemStack, EntityPlayer player, EntityLivingBase entity, EnumHand hand) {
        if (entity.world.isRemote) {
            return false;
        }

        if (entity.hasCapability(CapabilityEnergy.ENERGY, null)) {
            IEnergyStorage entityEnergy = entity.getCapability(CapabilityEnergy.ENERGY, null);
            IEnergyStorage itemEnergy = getEnergyStorage(itemStack);
            int moved = entityEnergy.extractEnergy(
                    itemEnergy.receiveEnergy(
                            entityEnergy.extractEnergy(ItemEnergeticShearsConfig.usageTransferAmount, true),
                            false),
                    false);
            if (moved > 0) {
                entity.playSound(SoundEvents.ENTITY_SHEEP_SHEAR, 1.0F, 1.0F);
            }
            return true;
        }
        if (canShear(itemStack) && entity instanceof IShearable) {
            net.minecraftforge.common.IShearable target = (IShearable)entity;
            BlockPos pos = new BlockPos(entity.posX, entity.posY, entity.posZ);
            if (target.isShearable(itemStack, entity.world, pos)) {
                List<ItemStack> drops = target.onSheared(itemStack, entity.world, pos,
                        EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, itemStack));

                Random rand = new Random();
                for(ItemStack stack : drops) {
                    EntityItem ent = entity.entityDropItem(stack, 1.0F);
                    ent.motionY += rand.nextFloat() * 0.05F;
                    ent.motionX += (rand.nextFloat() - rand.nextFloat()) * 0.1F;
                    ent.motionZ += (rand.nextFloat() - rand.nextFloat()) * 0.1F;
                }
                consumeOnShear(itemStack);
            }
            return true;
        }
        return false;
    }

    @Nullable
    @Override
    @SideOnly(Side.CLIENT)
    public IItemColor getItemColorHandler() {
        return null;
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return true;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack itemStack) {
        IEnergyStorage energyStorage = getEnergyStorage(itemStack);
        double amount = energyStorage.getEnergyStored();
        double capacity = energyStorage.getMaxEnergyStored();
        return (capacity - amount) / capacity;
    }

    @Override
    public int getRGBDurabilityForDisplay(ItemStack stack) {
        return MathHelper.hsvToRGB(Math.max(0.0F, 1 - (float) getDurabilityForDisplay(stack)) / 3.0F, 1.0F, 1.0F);
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
        return new DefaultCapabilityProvider<>(() -> CapabilityEnergy.ENERGY,
                new EnergyStorageItem(ItemEnergeticShearsConfig.capacity, stack));
    }
}
