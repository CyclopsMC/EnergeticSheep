package org.cyclops.energeticsheep.gametest;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import org.cyclops.energeticsheep.Reference;
import org.cyclops.energeticsheep.RegistryEntries;
import org.cyclops.energeticsheep.entity.EntityAIEatGrassFast;
import org.cyclops.energeticsheep.entity.EntityEnergeticSheepCommon;

/**
 * @author rubensworks
 */
public class GameTestsCommon {

    public static final String TEMPLATE_EMPTY = Reference.MOD_ID + ":empty10";
    public static final BlockPos POS = BlockPos.ZERO.offset(2, 1, 2);

    @GameTest(template = TEMPLATE_EMPTY)
    public void testSpawn(GameTestHelper helper) {
        EntityEnergeticSheepCommon entity = SPAWN(helper);

        helper.succeedWhen(() -> {
            helper.assertEntitiesPresent(SHEEP(), POS, 1, 3);
            helper.assertTrue(entity.getEnergyClient() > 500, "Sheep does not have enough energy");
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testShear(GameTestHelper helper) {
        EntityEnergeticSheepCommon entity = SPAWN(helper);

        // Give shears to player
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        ItemStack itemStack = new ItemStack(Items.SHEARS);
        player.setItemInHand(InteractionHand.MAIN_HAND, itemStack);

        helper.succeedWhen(() -> {
            // Right click with shears
            InteractionResult result = player.interactOn(entity, InteractionHand.MAIN_HAND);
            helper.assertItemEntityPresent(entity.getWoolByColor().get(entity.getColor()).asItem());
            helper.assertTrue(result.indicateItemUse(), "Interaction failed");
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testShearEnergetic(GameTestHelper helper) {
        EntityEnergeticSheepCommon entity = SPAWN(helper);

        // Give energetic shears to player
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        ItemStack itemStack = new ItemStack(RegistryEntries.ITEM_ENERGETIC_SHEARS);
        player.setItemInHand(InteractionHand.MAIN_HAND, itemStack);

        helper.succeedWhen(() -> {
            // Right click with energetic shears
            InteractionResult result = player.interactOn(entity, InteractionHand.MAIN_HAND);
            helper.assertItemEntityNotPresent(entity.getWoolByColor().get(entity.getColor()).asItem());
            helper.assertTrue(RegistryEntries.ITEM_ENERGETIC_SHEARS.value().getEnergyStored(player.getMainHandItem()) > 500, "Shears do not have enough energy");
            helper.assertTrue(result.equals(InteractionResult.SUCCESS), "Interaction failed");
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testEatGrass(GameTestHelper helper) {
        EntityAIEatGrassFast.EAT_CHANCE = 1;

        EntityEnergeticSheepCommon entity = SPAWN(helper);

        // Give shears to player
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        ItemStack itemStack = new ItemStack(Items.SHEARS);
        player.setItemInHand(InteractionHand.MAIN_HAND, itemStack);

        // Right click with shears
        player.interactOn(entity, InteractionHand.MAIN_HAND);

        // Place grass under sheep
        helper.setBlock(POS.below().east(), Blocks.GRASS_BLOCK);
        helper.setBlock(POS.below(), Blocks.GRASS_BLOCK);
        helper.setBlock(POS.below().west(), Blocks.GRASS_BLOCK);
        for (Direction direction : new Direction[]{ Direction.NORTH, Direction.SOUTH }) {
            helper.setBlock(POS.below().relative(direction).east(), Blocks.GRASS_BLOCK);
            helper.setBlock(POS.below().relative(direction), Blocks.GRASS_BLOCK);
            helper.setBlock(POS.below().relative(direction).west(), Blocks.GRASS_BLOCK);
        }

        // Place fences around sheep
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            helper.setBlock(POS.relative(direction), Blocks.ACACIA_FENCE);
        }

        helper.succeedWhen(() -> {
            helper.assertEntitiesPresent(SHEEP(), POS, 1, 3);
            helper.assertTrue(entity.getEnergyClient() > 500, "Sheep does not have enough energy");
        });
    }

    // TODO: fixme
//    @GameTest(template = TEMPLATE_EMPTY)
//    public void testShearEnergeticLeavesWithPower(GameTestHelper helper) {
//        // Add leaves block
//        helper.setBlock(POS, Blocks.ACACIA_LEAVES);
//
//        // Give energetic shears to player
//        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
//        ItemStack itemStack = new ItemStack(RegistryEntries.ITEM_ENERGETIC_SHEARS);
//        RegistryEntries.ITEM_ENERGETIC_SHEARS.value().setEnergyStored(itemStack, RegistryEntries.ITEM_ENERGETIC_SHEARS.value().getMaxEnergyStored(itemStack), player, player.getUsedItemHand());
//        player.setItemInHand(InteractionHand.MAIN_HAND, itemStack);
//
//        helper.succeedWhen(() -> {
//            // Break leaves with energetic shears
//            itemStack.mineBlock(helper.getLevel(), helper.getLevel().getBlockState(POS), POS, player);
//            helper.assertItemEntityPresent(Items.ACACIA_LEAVES);
//            helper.assertTrue(player.getInventory().items.stream().anyMatch(i -> i.getItem() == Items.ACACIA_LEAVES), "No leaves are in the player inventory");
//        });
//    }

    // TODO: make the test below a variant of the test above
    @GameTest(template = TEMPLATE_EMPTY)
    public void testShearEnergeticLeavesNoPower(GameTestHelper helper) {
        // Add leaves block
        helper.setBlock(POS, Blocks.ACACIA_LEAVES);

        // Give energetic shears to player
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        ItemStack itemStack = new ItemStack(RegistryEntries.ITEM_ENERGETIC_SHEARS);
        player.setItemInHand(InteractionHand.MAIN_HAND, itemStack);

        helper.succeedWhen(() -> {
            // Right click with energetic shears
            helper.useBlock(POS, player);
            helper.assertItemEntityNotPresent(Items.ACACIA_LEAVES);
        });
    }

    // TODO: shear a regular sheep

    protected static EntityEnergeticSheepCommon SPAWN(GameTestHelper helper) {
        EntityEnergeticSheepCommon entity = helper.spawn(RegistryEntries.ENTITY_TYPE_ENERGETIC_SHEEP.value(), POS.above());
        entity.finalizeSpawn(helper.getLevel(), helper.getLevel().getCurrentDifficultyAt(POS), MobSpawnType.NATURAL, null);
        return entity;
    }

    protected static EntityType<EntityEnergeticSheepCommon> SHEEP() {
        return RegistryEntries.ENTITY_TYPE_ENERGETIC_SHEEP.value();
    }

}
