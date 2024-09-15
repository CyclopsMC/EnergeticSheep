package org.cyclops.energeticsheep.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.cyclops.cyclopscore.helper.IModHelpers;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;

/**
 * Can shear energy off energetic shears.
 * @author rubensworks
 *
 */
public abstract class ItemEnergeticShearsCommon extends ShearsItem {

    public ItemEnergeticShearsCommon(Item.Properties builder) {
        super(builder);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(itemStack, context, tooltip, flagIn);
        int capacity = getMaxEnergyStored(itemStack);
        if (capacity > 0) {
            int amount = getEnergyStored(itemStack);
            String line = String.format("%,d", amount) + " / " + String.format("%,d", capacity)
                    + " " + IModHelpers.get().getL10NHelpers().localize("general.energeticsheep.energy_unit");
            tooltip.add(Component.literal(line).withStyle(ChatFormatting.RED));
        }
    }

    public abstract int getEnergyStored(ItemStack itemStack);

    public abstract int getMaxEnergyStored(ItemStack itemStack);

    public abstract void consumeEnergy(ItemStack itemStack, int amount, Player player, InteractionHand hand);

    protected abstract int moveEnergyFromEntityToItem(LivingEntity entity, ItemStack itemStack, int usageTransferAmount, Player player, InteractionHand hand);

    @Nullable
    protected abstract List<ItemStack> getShearableDrops(Object maybeShearable, @Nullable Player player, ItemStack item, Level level, BlockPos pos);

    protected boolean canShear(ItemStack itemStack) {
        return getEnergyStored(itemStack) > ItemEnergeticShearsConfigCommon.shearConsumption;
    }

    protected void consumeOnShear(ItemStack itemStack, Player player, InteractionHand hand) {
        consumeEnergy(itemStack, ItemEnergeticShearsConfigCommon.shearConsumption, player, hand);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        ItemStack itemStack = context.getItemInHand();
        BlockPos pos = context.getClickedPos();
        if (player.level().isClientSide || player.isCreative() || !canShear(itemStack)) {
            return super.useOn(context);
        }
        Block block = player.level().getBlockState(pos).getBlock();
        List<ItemStack> drops = getShearableDrops(block, player, itemStack, player.level(), pos);
        if (drops != null) {
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

            consumeOnShear(itemStack, player, context.getHand());
            player.setItemInHand(player.getUsedItemHand(), itemStack);
            player.awardStat(Stats.BLOCK_MINED.get(block));
            player.level().setBlock(pos, Blocks.AIR.defaultBlockState(), 11);
            return InteractionResult.SUCCESS;
        }
        return super.useOn(context);
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
    public boolean isCorrectToolForDrops(ItemStack itemStack, BlockState blockIn) {
        return Items.SHEARS.isCorrectToolForDrops(itemStack, blockIn);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack itemStack, Player player, LivingEntity entity, InteractionHand hand) {
        if (entity.level().isClientSide) {
            return InteractionResult.PASS;
        }

        if (moveEnergyFromEntityToItem(entity, itemStack, ItemEnergeticShearsConfigCommon.usageTransferAmount, player, hand) > 0) {
            player.setItemInHand(hand, itemStack);
            entity.playSound(SoundEvents.SHEEP_SHEAR, 1.0F, 1.0F);
            return InteractionResult.SUCCESS;
        }
        if (canShear(itemStack)) {
            List<ItemStack> drops = getShearableDrops(entity, player, itemStack, entity.level(), entity.getOnPos());
            if (drops != null) {
                Random rand = new Random();
                for(ItemStack stack : drops) {
                    ItemEntity ent = entity.spawnAtLocation(stack, 1.0F);
                    ent.setDeltaMovement(ent.getDeltaMovement().add(
                            (rand.nextFloat() - rand.nextFloat()) * 0.1F,
                            rand.nextFloat() * 0.05F,
                            (rand.nextFloat() - rand.nextFloat()) * 0.1F)
                    );
                }
                consumeOnShear(itemStack, player, hand);
                player.setItemInHand(hand, itemStack);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return true;
    }

    @Override
    public int getBarWidth(ItemStack itemStack) {
        return Math.round((float)getEnergyStored(itemStack) * 13.0F / (float)getMaxEnergyStored(itemStack));
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return Mth.hsvToRgb(Math.max(0.0F, ((float) getBarWidth(stack)) / 13) / 3.0F, 1.0F, 1.0F);
    }
}
