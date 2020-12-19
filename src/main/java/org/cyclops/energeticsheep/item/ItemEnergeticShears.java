package org.cyclops.energeticsheep.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.Items;
import net.minecraft.item.ShearsItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
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
public class ItemEnergeticShears extends ShearsItem {

    public ItemEnergeticShears(Item.Properties builder) {
        super(builder);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack itemStack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(itemStack, worldIn, tooltip, flagIn);
        IEnergyStorage energyStorage = getEnergyStorage(itemStack);
        if (energyStorage != null) {
            int amount = energyStorage.getEnergyStored();
            int capacity = energyStorage.getMaxEnergyStored();
            String line = String.format("%,d", amount) + " / " + String.format("%,d", capacity)
                    + " " + L10NHelpers.localize("general.energeticsheep.energy_unit");
            tooltip.add(new StringTextComponent(line).applyTextStyle(IInformationProvider.ITEM_PREFIX));
        }
    }

    public static ActionResultType transferEnergy(PlayerEntity player, BlockPos pos, Direction side, Hand hand) {
        World worldIn = player.world;
        if (!player.isCrouching()) {
            return TileHelpers.getCapability(worldIn, pos, side, CapabilityEnergy.ENERGY)
                    .map(energyTarget -> {
                        ItemStack itemStack = player.getHeldItem(hand);
                        return itemStack.getCapability(CapabilityEnergy.ENERGY)
                                .map(energyItem -> energyTarget.receiveEnergy(
                                        energyItem.extractEnergy(
                                                energyTarget.receiveEnergy(
                                                        energyItem.extractEnergy(ItemEnergeticShearsConfig.usageTransferAmount, true),
                                                        true),
                                                worldIn.isRemote),
                                        worldIn.isRemote) > 0 ? ActionResultType.SUCCESS : ActionResultType.FAIL)
                                .orElse(null);
                    })
                    .orElse(null);
        }
        return null;
    }

    @Override
    public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context) {
        ActionResultType result = ItemEnergeticShears.transferEnergy(context.getPlayer(), context.getPos(), context.getFace(), context.getHand());
        if (result == null) {
            return super.onItemUseFirst(stack, context);
        }
        return result;
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return oldStack.getItem() != newStack.getItem();
    }

    @Nullable
    protected IEnergyStorage getEnergyStorage(ItemStack itemStack) {
        if (CapabilityEnergy.ENERGY == null) { // Can be null during item registration, when caps are not registered yet
            return null;
        }

        return itemStack.getCapability(CapabilityEnergy.ENERGY).orElse(null);
    }

    protected boolean canShear(ItemStack itemStack) {
        IEnergyStorage energyStorage = getEnergyStorage(itemStack);
        return energyStorage != null && energyStorage.getEnergyStored() > ItemEnergeticShearsConfig.shearConsumption;
    }

    protected void consumeOnShear(ItemStack itemStack) {
        IEnergyStorage energyStorage = getEnergyStorage(itemStack);
        if (energyStorage != null) {
            energyStorage.extractEnergy(ItemEnergeticShearsConfig.shearConsumption, false);
        }
    }

    @Override
    public boolean onBlockStartBreak(ItemStack itemStack, BlockPos pos, PlayerEntity player) {
        if (player.world.isRemote || player.isCreative() || !canShear(itemStack)) {
            return false;
        }
        Block block = player.world.getBlockState(pos).getBlock();
        if (block instanceof IShearable) {
            IShearable target = (net.minecraftforge.common.IShearable)block;
            if (target.isShearable(itemStack, player.world, pos)) {
                List<ItemStack> drops = target.onSheared(itemStack, player.world, pos,
                        EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, itemStack));
                Random rand = new java.util.Random();

                for (ItemStack stack : drops) {
                    float f = 0.7F;
                    double d  = (double)(rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
                    double d1 = (double)(rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
                    double d2 = (double)(rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
                    ItemEntity entityitem = new ItemEntity(player.world, (double)pos.getX() + d, (double)pos.getY() + d1, (double)pos.getZ() + d2, stack);
                    entityitem.setDefaultPickupDelay();
                    player.world.addEntity(entityitem);
                }

                consumeOnShear(itemStack);
                player.setHeldItem(player.getActiveHand(), itemStack);
                player.addStat(Stats.BLOCK_MINED.get(block));
                player.world.setBlockState(pos, Blocks.AIR.getDefaultState(), 11);
                return true;
            }
        }
        return false;
    }

    @Override
    public float getDestroySpeed(ItemStack itemStack, BlockState state) {
        float factor = canShear(itemStack) ? 1.5F : 0.1F;
        float superSpeed = super.getDestroySpeed(itemStack, state);
        if (superSpeed != 1.0F) {
            return superSpeed * factor;
        }
        return superSpeed;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack itemStack, World worldIn, BlockState state, BlockPos pos, LivingEntity entityLiving) {
        if (!worldIn.isRemote) {
            consumeOnShear(itemStack);
        }

        Block block = state.getBlock();
        return !state.isIn(BlockTags.LEAVES) && block != Blocks.COBWEB && block != Blocks.GRASS && block != Blocks.FERN && block != Blocks.DEAD_BUSH && block != Blocks.VINE && block != Blocks.TRIPWIRE && !block.isIn(BlockTags.WOOL) ? super.onBlockDestroyed(itemStack, worldIn, state, pos, entityLiving) : true;
    }

    @Override
    public boolean canHarvestBlock(BlockState blockIn) {
        return Items.SHEARS.canHarvestBlock(blockIn);
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack itemStack, PlayerEntity player, LivingEntity entity, Hand hand) {
        if (entity.world.isRemote) {
            return false;
        }

        LazyOptional<IEnergyStorage> energyCapability = entity.getCapability(CapabilityEnergy.ENERGY);
        if (energyCapability.isPresent()) {
            IEnergyStorage entityEnergy = energyCapability.orElse(null);
            IEnergyStorage itemEnergy = getEnergyStorage(itemStack);
            int moved = entityEnergy.extractEnergy(
                    itemEnergy.receiveEnergy(
                            entityEnergy.extractEnergy(ItemEnergeticShearsConfig.usageTransferAmount, true),
                            false),
                    false);
            if (moved > 0) {
                player.setHeldItem(hand, itemStack);
                entity.playSound(SoundEvents.ENTITY_SHEEP_SHEAR, 1.0F, 1.0F);
            }
            return true;
        }
        if (canShear(itemStack) && entity instanceof IShearable) {
            net.minecraftforge.common.IShearable target = (IShearable)entity;
            BlockPos pos = new BlockPos(entity.getPosX(), entity.getPosY(), entity.getPosZ());
            if (target.isShearable(itemStack, entity.world, pos)) {
                List<ItemStack> drops = target.onSheared(itemStack, entity.world, pos,
                        EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, itemStack));

                Random rand = new Random();
                for(ItemStack stack : drops) {
                    ItemEntity ent = entity.entityDropItem(stack, 1.0F);
                    ent.setMotion(ent.getMotion().add(
                            (rand.nextFloat() - rand.nextFloat()) * 0.1F,
                            rand.nextFloat() * 0.05F,
                            (rand.nextFloat() - rand.nextFloat()) * 0.1F)
                    );
                }
                consumeOnShear(itemStack);
                player.setHeldItem(hand, itemStack);
            }
            return true;
        }
        return false;
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
    public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT nbt) {
        return new DefaultCapabilityProvider<>(() -> CapabilityEnergy.ENERGY,
                new EnergyStorageItem(ItemEnergeticShearsConfig.capacity, stack));
    }
}
