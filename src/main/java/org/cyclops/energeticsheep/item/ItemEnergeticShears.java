package org.cyclops.energeticsheep.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.IForgeShearable;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import org.cyclops.cyclopscore.helper.BlockEntityHelpers;
import org.cyclops.cyclopscore.helper.L10NHelpers;
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
    public void appendHoverText(ItemStack itemStack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(itemStack, worldIn, tooltip, flagIn);
        IEnergyStorage energyStorage = getEnergyStorage(itemStack);
        if (energyStorage != null) {
            int amount = energyStorage.getEnergyStored();
            int capacity = energyStorage.getMaxEnergyStored();
            String line = String.format("%,d", amount) + " / " + String.format("%,d", capacity)
                    + " " + L10NHelpers.localize("general.energeticsheep.energy_unit");
            tooltip.add(Component.literal(line).withStyle(IInformationProvider.ITEM_PREFIX));
        }
    }

    public static InteractionResult transferEnergy(Player player, BlockPos pos, Direction side, InteractionHand hand) {
        Level worldIn = player.level();
        if (!player.isCrouching()) {
            return BlockEntityHelpers.getCapability(worldIn, pos, side, ForgeCapabilities.ENERGY)
                    .map(energyTarget -> {
                        ItemStack itemStack = player.getItemInHand(hand);
                        return itemStack.getCapability(ForgeCapabilities.ENERGY)
                                .map(energyItem -> energyTarget.receiveEnergy(
                                        energyItem.extractEnergy(
                                                energyTarget.receiveEnergy(
                                                        energyItem.extractEnergy(ItemEnergeticShearsConfig.usageTransferAmount, true),
                                                        true),
                                                worldIn.isClientSide),
                                        worldIn.isClientSide) > 0 ? InteractionResult.SUCCESS : InteractionResult.FAIL)
                                .orElse(null);
                    })
                    .orElse(null);
        }
        return null;
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
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return oldStack.getItem() != newStack.getItem();
    }

    @Nullable
    protected IEnergyStorage getEnergyStorage(ItemStack itemStack) {
        if (ForgeCapabilities.ENERGY == null) { // Can be null during item registration, when caps are not registered yet
            return null;
        }

        return itemStack.getCapability(ForgeCapabilities.ENERGY).orElse(null);
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
    public boolean onBlockStartBreak(ItemStack itemStack, BlockPos pos, Player player) {
        if (player.level().isClientSide || player.isCreative() || !canShear(itemStack)) {
            return false;
        }
        Block block = player.level().getBlockState(pos).getBlock();
        if (block instanceof IForgeShearable) {
            IForgeShearable target = (IForgeShearable) block;
            if (target.isShearable(itemStack, player.level(), pos)) {
                List<ItemStack> drops = target.onSheared(player, itemStack, player.level(), pos,
                        EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BLOCK_FORTUNE, itemStack));
                Random rand = new java.util.Random();

                for (ItemStack stack : drops) {
                    float f = 0.7F;
                    double d  = (double)(rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
                    double d1 = (double)(rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
                    double d2 = (double)(rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
                    ItemEntity entityitem = new ItemEntity(player.level(), (double)pos.getX() + d, (double)pos.getY() + d1, (double)pos.getZ() + d2, stack);
                    entityitem.setDefaultPickUpDelay();
                    player.level().addFreshEntity(entityitem);
                }

                consumeOnShear(itemStack);
                player.setItemInHand(player.getUsedItemHand(), itemStack);
                player.awardStat(Stats.BLOCK_MINED.get(block));
                player.level().setBlock(pos, Blocks.AIR.defaultBlockState(), 11);
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
    public boolean mineBlock(ItemStack itemStack, Level worldIn, BlockState state, BlockPos pos, LivingEntity entityLiving) {
        if (!worldIn.isClientSide) {
            consumeOnShear(itemStack);
        }

        Block block = state.getBlock();
        return !state.is(BlockTags.LEAVES) && block != Blocks.COBWEB && block != Blocks.GRASS && block != Blocks.FERN && block != Blocks.DEAD_BUSH && block != Blocks.VINE && block != Blocks.TRIPWIRE && !state.is(BlockTags.WOOL) ? super.mineBlock(itemStack, worldIn, state, pos, entityLiving) : true;
    }

    @Override
    public boolean isCorrectToolForDrops(BlockState blockIn) {
        return Items.SHEARS.isCorrectToolForDrops(blockIn);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack itemStack, Player player, LivingEntity entity, InteractionHand hand) {
        if (entity.level().isClientSide) {
            return InteractionResult.PASS;
        }

        LazyOptional<IEnergyStorage> energyCapability = entity.getCapability(ForgeCapabilities.ENERGY);
        if (energyCapability.isPresent()) {
            IEnergyStorage entityEnergy = energyCapability.orElse(null);
            IEnergyStorage itemEnergy = getEnergyStorage(itemStack);
            int moved = entityEnergy.extractEnergy(
                    itemEnergy.receiveEnergy(
                            entityEnergy.extractEnergy(ItemEnergeticShearsConfig.usageTransferAmount, true),
                            false),
                    false);
            if (moved > 0) {
                player.setItemInHand(hand, itemStack);
                entity.playSound(SoundEvents.SHEEP_SHEAR, 1.0F, 1.0F);
            }
            return InteractionResult.SUCCESS;
        }
        if (canShear(itemStack) && entity instanceof IForgeShearable) {
            IForgeShearable target = (IForgeShearable)entity;
            BlockPos pos = entity.getOnPos();
            if (target.isShearable(itemStack, entity.level(), pos)) {
                List<ItemStack> drops = target.onSheared(player, itemStack, entity.level(), pos,
                        EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BLOCK_FORTUNE, itemStack));

                Random rand = new Random();
                for(ItemStack stack : drops) {
                    ItemEntity ent = entity.spawnAtLocation(stack, 1.0F);
                    ent.setDeltaMovement(ent.getDeltaMovement().add(
                            (rand.nextFloat() - rand.nextFloat()) * 0.1F,
                            rand.nextFloat() * 0.05F,
                            (rand.nextFloat() - rand.nextFloat()) * 0.1F)
                    );
                }
                consumeOnShear(itemStack);
                player.setItemInHand(hand, itemStack);
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return true;
    }

    @Override
    public int getBarWidth(ItemStack itemStack) {
        IEnergyStorage energyStorage = getEnergyStorage(itemStack);
        double amount = energyStorage.getEnergyStored();
        double capacity = energyStorage.getMaxEnergyStored();
        return Math.round((float)amount * 13.0F / (float)capacity);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return Mth.hsvToRgb(Math.max(0.0F, ((float) getBarWidth(stack)) / 13) / 3.0F, 1.0F, 1.0F);
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag nbt) {
        return new DefaultCapabilityProvider<>(() -> ForgeCapabilities.ENERGY,
                new EnergyStorageItem(ItemEnergeticShearsConfig.capacity, stack));
    }
}
