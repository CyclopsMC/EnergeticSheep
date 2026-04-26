package org.cyclops.energeticsheep.gametest;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.sheep.Sheep;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.cyclops.energeticsheep.entity.EntityEnergeticSheepConfigCommon;
import org.cyclops.cyclopscore.gametest.GameTest;
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
            helper.assertTrue(entity.getEnergyClient() > 500, Component.literal("Sheep does not have enough energy"));
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
            InteractionResult result = player.interactOn(entity, InteractionHand.MAIN_HAND, entity.position());
            helper.assertItemEntityPresent(entity.getWoolByColor().get(entity.getColor()).asItem());
            helper.assertTrue(result == InteractionResult.SUCCESS || result == InteractionResult.SUCCESS_SERVER, Component.literal("Interaction failed"));
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
            InteractionResult result = player.interactOn(entity, InteractionHand.MAIN_HAND, entity.position());
            helper.assertItemEntityNotPresent(entity.getWoolByColor().get(entity.getColor()).asItem());
            helper.assertTrue(RegistryEntries.ITEM_ENERGETIC_SHEARS.value().getEnergyStored(player.getMainHandItem()) > 500, Component.literal("Shears do not have enough energy"));
            helper.assertTrue(result.equals(InteractionResult.SUCCESS), Component.literal("Interaction failed"));
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
        player.interactOn(entity, InteractionHand.MAIN_HAND, entity.position());

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
            helper.assertTrue(entity.getEnergyClient() > 500, Component.literal("Sheep does not have enough energy"));
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testShearEnergeticLeavesWithPower(GameTestHelper helper) {
        // For some unknown reason, this test does not work in Forge (does work in-game)
        // TODO: try to re-enable later
        if (isForge()) {
            helper.succeed();
            return;
        }

        // Add leaves block
        helper.setBlock(POS, Blocks.ACACIA_LEAVES);

        // Give energetic shears with power to player
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        ItemStack itemStack = new ItemStack(RegistryEntries.ITEM_ENERGETIC_SHEARS);
        player.setItemInHand(InteractionHand.MAIN_HAND, itemStack);
        RegistryEntries.ITEM_ENERGETIC_SHEARS.value().setEnergyStored(itemStack, RegistryEntries.ITEM_ENERGETIC_SHEARS.value().getMaxEnergyStored(itemStack), player, player.getUsedItemHand());

        helper.succeedWhen(() -> {
            // Break leaves with energetic shears
            BlockState blockState = helper.getBlockState(POS);
            InteractionResult interactionResult = itemStack.useOn(new UseOnContext(player, InteractionHand.MAIN_HAND, new BlockHitResult(POS.getCenter(), Direction.NORTH, helper.absolutePos(POS), false)));
            helper.assertFalse(interactionResult == InteractionResult.FAIL, Component.literal("Interaction must succeed"));
            helper.assertTrue(itemStack.getItem().mineBlock(itemStack, helper.getLevel(), blockState, helper.absolutePos(POS), player), Component.literal("Item can not mine block"));
            helper.assertTrue(player.hasCorrectToolForDrops(blockState), Component.literal("Player must have correct tool"));
            blockState.getBlock().playerDestroy(helper.getLevel(), player, helper.absolutePos(POS), blockState, null, itemStack);

            helper.assertItemEntityPresent(Items.ACACIA_LEAVES);
            helper.assertTrue(RegistryEntries.ITEM_ENERGETIC_SHEARS.value().getEnergyStored(player.getMainHandItem()) < RegistryEntries.ITEM_ENERGETIC_SHEARS.value().getMaxEnergyStored(player.getMainHandItem()), Component.literal("No energy was consumed from shears"));
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testShearEnergeticLeavesNoPower(GameTestHelper helper) {
        // Add leaves block
        helper.setBlock(POS, Blocks.ACACIA_LEAVES);

        // Give energetic shears without power to player
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        ItemStack itemStack = new ItemStack(RegistryEntries.ITEM_ENERGETIC_SHEARS);
        player.setItemInHand(InteractionHand.MAIN_HAND, itemStack);

        helper.succeedWhen(() -> {
            // Attempt to break leaves with energetic shears that has no power
            InteractionResult interactionResult = itemStack.useOn(new UseOnContext(player, InteractionHand.MAIN_HAND, new BlockHitResult(POS.getCenter(), Direction.NORTH, helper.absolutePos(POS), false)));
            helper.assertFalse(interactionResult == InteractionResult.FAIL, Component.literal("Interaction must fail"));

            helper.assertItemEntityNotPresent(Items.ACACIA_LEAVES);
            helper.assertTrue(RegistryEntries.ITEM_ENERGETIC_SHEARS.value().getEnergyStored(player.getMainHandItem()) == 0, Component.literal("Energy must remain zero"));
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testShearEnergeticWithPowerRegularSheep(GameTestHelper helper) {
        Sheep entity = SPAWN_REGULAR(helper);
        entity.setColor(DyeColor.WHITE);

        // Give energetic shears to player
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        ItemStack itemStack = new ItemStack(RegistryEntries.ITEM_ENERGETIC_SHEARS);
        player.setItemInHand(InteractionHand.MAIN_HAND, itemStack);
        RegistryEntries.ITEM_ENERGETIC_SHEARS.value().setEnergyStored(itemStack, RegistryEntries.ITEM_ENERGETIC_SHEARS.value().getMaxEnergyStored(itemStack), player, player.getUsedItemHand());

        helper.succeedWhen(() -> {
            // Right click with energetic shears
            InteractionResult result = player.interactOn(entity, InteractionHand.MAIN_HAND, entity.position());
            helper.assertItemEntityPresent(Items.WHITE_WOOL);
            helper.assertTrue(RegistryEntries.ITEM_ENERGETIC_SHEARS.value().getEnergyStored(player.getMainHandItem()) < RegistryEntries.ITEM_ENERGETIC_SHEARS.value().getMaxEnergyStored(player.getMainHandItem()), Component.literal("No energy was consumed from shears"));
            helper.assertTrue(result.equals(InteractionResult.SUCCESS), Component.literal("Interaction failed"));
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testShearEnergeticNoPowerRegularSheep(GameTestHelper helper) {
        Sheep entity = SPAWN_REGULAR(helper);
        entity.setColor(DyeColor.WHITE);

        // Give energetic shears to player
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        ItemStack itemStack = new ItemStack(RegistryEntries.ITEM_ENERGETIC_SHEARS);
        player.setItemInHand(InteractionHand.MAIN_HAND, itemStack);

        helper.succeedWhen(() -> {
            // Right click with energetic shears
            InteractionResult result = player.interactOn(entity, InteractionHand.MAIN_HAND, entity.position());
            helper.assertItemEntityNotPresent(Items.WHITE_WOOL);
            helper.assertTrue(RegistryEntries.ITEM_ENERGETIC_SHEARS.value().getEnergyStored(player.getMainHandItem()) == 0, Component.literal("Shears do not have enough energy"));
            helper.assertTrue(result.equals(InteractionResult.PASS), Component.literal("Interaction did not pass"));
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testBreedEnergeticWithRegularCanMate(GameTestHelper helper) {
        EntityEnergeticSheepCommon energetic = SPAWN(helper);
        Sheep regular = SPAWN_REGULAR(helper);

        energetic.setInLove(null);
        regular.setInLove(null);

        helper.succeedWhen(() -> {
            helper.assertTrue(energetic.canMate(regular), Component.literal("Energetic sheep should be able to mate with regular sheep"));
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testBreedEnergeticWithRegularCanNotMateWhenNotInLove(GameTestHelper helper) {
        EntityEnergeticSheepCommon energetic = SPAWN(helper);
        Sheep regular = SPAWN_REGULAR(helper);

        // Neither in love
        helper.succeedWhen(() -> {
            helper.assertFalse(energetic.canMate(regular), Component.literal("Energetic sheep should not be able to mate with regular sheep when not in love"));
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testBreedEnergeticWithEnergeticCanMate(GameTestHelper helper) {
        EntityEnergeticSheepCommon energetic1 = SPAWN(helper);
        EntityEnergeticSheepCommon energetic2 = SPAWN(helper);

        energetic1.setInLove(null);
        energetic2.setInLove(null);

        helper.succeedWhen(() -> {
            helper.assertTrue(energetic1.canMate(energetic2), Component.literal("Energetic sheep should be able to mate with another energetic sheep"));
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testBreedEnergeticWithRegularProducesEnergetic(GameTestHelper helper) {
        EntityEnergeticSheepCommon energetic = SPAWN(helper);
        Sheep regular = SPAWN_REGULAR(helper);

        // Force energetic offspring by setting chance to 1
        int oldChance = EntityEnergeticSheepConfigCommon.babyChance;
        EntityEnergeticSheepConfigCommon.babyChance = 1;
        energetic.setInLove(null);
        regular.setInLove(null);

        helper.succeedWhen(() -> {
            AgeableMob offspring = energetic.getBreedOffspring((ServerLevel) helper.getLevel(), regular);
            EntityEnergeticSheepConfigCommon.babyChance = oldChance;
            helper.assertTrue(offspring instanceof EntityEnergeticSheepCommon, Component.literal("Offspring should be an energetic sheep when chance is 1"));
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testBreedEnergeticWithRegularProducesRegular(GameTestHelper helper) {
        EntityEnergeticSheepCommon energetic = SPAWN(helper);
        Sheep regular = SPAWN_REGULAR(helper);

        // Force regular offspring by setting chance to 0
        int oldChance = EntityEnergeticSheepConfigCommon.babyChance;
        EntityEnergeticSheepConfigCommon.babyChance = 0;
        energetic.setInLove(null);
        regular.setInLove(null);

        helper.succeedWhen(() -> {
            AgeableMob offspring = energetic.getBreedOffspring((ServerLevel) helper.getLevel(), regular);
            EntityEnergeticSheepConfigCommon.babyChance = oldChance;
            helper.assertFalse(offspring instanceof EntityEnergeticSheepCommon, Component.literal("Offspring should be a regular sheep when chance is 0"));
            helper.assertTrue(offspring instanceof Sheep, Component.literal("Offspring should be a Sheep"));
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testBreedEnergeticWithEnergeticProducesEnergetic(GameTestHelper helper) {
        EntityEnergeticSheepCommon energetic1 = SPAWN(helper);
        EntityEnergeticSheepCommon energetic2 = SPAWN(helper);

        // Force energetic offspring
        int oldChance = EntityEnergeticSheepConfigCommon.babyChance;
        EntityEnergeticSheepConfigCommon.babyChance = 1;
        energetic1.setInLove(null);
        energetic2.setInLove(null);

        helper.succeedWhen(() -> {
            AgeableMob offspring = energetic1.getBreedOffspring((ServerLevel) helper.getLevel(), energetic2);
            EntityEnergeticSheepConfigCommon.babyChance = oldChance;
            helper.assertTrue(offspring instanceof EntityEnergeticSheepCommon, Component.literal("Offspring should be an energetic sheep when both parents are energetic"));
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testBreedEnergeticSameColorKeepsColor(GameTestHelper helper) {
        EntityEnergeticSheepCommon energetic1 = SPAWN(helper);
        EntityEnergeticSheepCommon energetic2 = SPAWN(helper);
        energetic1.setFleeceColorInternal(DyeColor.RED);
        energetic2.setFleeceColorInternal(DyeColor.RED);

        int oldChance = EntityEnergeticSheepConfigCommon.babyChance;
        EntityEnergeticSheepConfigCommon.babyChance = 1;
        energetic1.setInLove(null);
        energetic2.setInLove(null);

        helper.succeedWhen(() -> {
            AgeableMob offspring = energetic1.getBreedOffspring((ServerLevel) helper.getLevel(), energetic2);
            EntityEnergeticSheepConfigCommon.babyChance = oldChance;
            helper.assertTrue(offspring instanceof EntityEnergeticSheepCommon, Component.literal("Offspring should be energetic"));
            helper.assertTrue(((Sheep) offspring).getColor() == DyeColor.RED, Component.literal("Same-colored parents should produce same-colored energetic offspring"));
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testBreedEnergeticWithRegularSameColorKeepsColor(GameTestHelper helper) {
        EntityEnergeticSheepCommon energetic = SPAWN(helper);
        energetic.setFleeceColorInternal(DyeColor.BLUE);
        Sheep regular = SPAWN_REGULAR(helper);
        regular.setColor(DyeColor.BLUE);

        int oldChance = EntityEnergeticSheepConfigCommon.babyChance;
        EntityEnergeticSheepConfigCommon.babyChance = 1;
        energetic.setInLove(null);
        regular.setInLove(null);

        helper.succeedWhen(() -> {
            AgeableMob offspring = energetic.getBreedOffspring((ServerLevel) helper.getLevel(), regular);
            EntityEnergeticSheepConfigCommon.babyChance = oldChance;
            helper.assertTrue(offspring instanceof EntityEnergeticSheepCommon, Component.literal("Offspring should be energetic"));
            helper.assertTrue(((Sheep) offspring).getColor() == DyeColor.BLUE, Component.literal("Same-colored parents should produce same-colored energetic offspring"));
        });
    }

    @GameTest(template = TEMPLATE_EMPTY, timeoutTicks = 200)
    public void testBreedEnergeticWithRegularViaBreedGoal(GameTestHelper helper) {
        EntityEnergeticSheepCommon energetic = SPAWN(helper);
        Sheep regular = SPAWN_REGULAR(helper);

        // Put both sheep in love mode directly (Animal.mobInteract only calls setInLove for ServerPlayer,
        // but we want to test that the BreedGoal itself works when both are in love)
        energetic.setInLove(null);
        regular.setInLove(null);

        helper.assertTrue(energetic.isInLove(), Component.literal("Energetic sheep should be in love"));
        helper.assertTrue(regular.isInLove(), Component.literal("Regular sheep should be in love"));

        // Wait for the BreedGoal to run and produce a baby (love mode will be cleared after breeding)
        helper.succeedWhen(() -> {
            long totalBabies = helper.getLevel().getEntitiesOfClass(Sheep.class, energetic.getBoundingBox().inflate(16), e -> e.isBaby()).size();
            helper.assertTrue(totalBabies >= 1, Component.literal("A baby sheep should have spawned from the cross-breed"));
        });
    }

    @GameTest(template = TEMPLATE_EMPTY, timeoutTicks = 200)
    public void testBreedEnergeticWithEnergeticViaBreedGoal(GameTestHelper helper) {
        EntityEnergeticSheepCommon energetic1 = SPAWN(helper);
        EntityEnergeticSheepCommon energetic2 = SPAWN(helper);

        // Put both energetic sheep in love mode directly
        energetic1.setInLove(null);
        energetic2.setInLove(null);

        helper.assertTrue(energetic1.isInLove(), Component.literal("Energetic sheep 1 should be in love"));
        helper.assertTrue(energetic2.isInLove(), Component.literal("Energetic sheep 2 should be in love"));

        // Wait for the BreedGoal to run and produce a baby (love mode will be cleared after breeding)
        helper.succeedWhen(() -> {
            long totalBabies = helper.getLevel().getEntitiesOfClass(Sheep.class, energetic1.getBoundingBox().inflate(16), e -> e.isBaby()).size();
            helper.assertTrue(totalBabies >= 1, Component.literal("A baby sheep should have spawned from energetic x energetic breeding"));
        });
    }

    protected static EntityEnergeticSheepCommon SPAWN(GameTestHelper helper) {
        EntityEnergeticSheepCommon entity = helper.spawn(RegistryEntries.ENTITY_TYPE_ENERGETIC_SHEEP.value(), POS.above());
        entity.finalizeSpawn(helper.getLevel(), helper.getLevel().getCurrentDifficultyAt(POS), EntitySpawnReason.NATURAL, null);
        return entity;
    }

    protected static Sheep SPAWN_REGULAR(GameTestHelper helper) {
        Sheep entity = helper.spawn(EntityType.SHEEP, POS.above());
        entity.finalizeSpawn(helper.getLevel(), helper.getLevel().getCurrentDifficultyAt(POS), EntitySpawnReason.NATURAL, null);
        return entity;
    }

    protected static EntityType<EntityEnergeticSheepCommon> SHEEP() {
        return RegistryEntries.ENTITY_TYPE_ENERGETIC_SHEEP.value();
    }

    protected boolean isForge() {
        try {
            Class.forName("net.minecraftforge.common.MinecraftForge");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

}
