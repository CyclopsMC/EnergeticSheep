package org.cyclops.energeticsheep.entity;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PowerableMob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.storage.loot.LootTable;
import org.cyclops.energeticsheep.Reference;
import org.cyclops.energeticsheep.RegistryEntries;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

/**
 * A sheep that produces energy.
 * @author rubensworks
 *
 */
public abstract class EntityEnergeticSheepCommon extends Sheep implements PowerableMob {

    public static final ResourceKey<LootTable> LOOTTABLE_SHEEP_WHITE      = ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "entities/energetic_sheep/white"));
    public static final ResourceKey<LootTable> LOOTTABLE_SHEEP_ORANGE     = ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "entities/energetic_sheep/orange"));
    public static final ResourceKey<LootTable> LOOTTABLE_SHEEP_MAGENTA    = ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "entities/energetic_sheep/magenta"));
    public static final ResourceKey<LootTable> LOOTTABLE_SHEEP_LIGHT_BLUE = ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "entities/energetic_sheep/light_blue"));
    public static final ResourceKey<LootTable> LOOTTABLE_SHEEP_YELLOW     = ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "entities/energetic_sheep/yellow"));
    public static final ResourceKey<LootTable> LOOTTABLE_SHEEP_LIME       = ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "entities/energetic_sheep/lime"));
    public static final ResourceKey<LootTable> LOOTTABLE_SHEEP_PINK       = ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "entities/energetic_sheep/pink"));
    public static final ResourceKey<LootTable> LOOTTABLE_SHEEP_GRAY       = ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "entities/energetic_sheep/gray"));
    public static final ResourceKey<LootTable> LOOTTABLE_SHEEP_LIGHT_GRAY = ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "entities/energetic_sheep/light_gray"));
    public static final ResourceKey<LootTable> LOOTTABLE_SHEEP_CYAN       = ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "entities/energetic_sheep/cyan"));
    public static final ResourceKey<LootTable> LOOTTABLE_SHEEP_PURPLE     = ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "entities/energetic_sheep/purple"));
    public static final ResourceKey<LootTable> LOOTTABLE_SHEEP_BLUE       = ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "entities/energetic_sheep/blue"));
    public static final ResourceKey<LootTable> LOOTTABLE_SHEEP_BROWN      = ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "entities/energetic_sheep/brown"));
    public static final ResourceKey<LootTable> LOOTTABLE_SHEEP_GREEN      = ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "entities/energetic_sheep/green"));
    public static final ResourceKey<LootTable> LOOTTABLE_SHEEP_RED        = ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "entities/energetic_sheep/red"));
    public static final ResourceKey<LootTable> LOOTTABLE_SHEEP_BLACK      = ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "entities/energetic_sheep/black"));

    protected final Map<DyeColor, ItemLike> woolByColor;

    private static final EntityDataAccessor<Integer> ENERGY = SynchedEntityData.<Integer>defineId(EntityEnergeticSheepCommon.class, EntityDataSerializers.INT);

    private boolean powerBreeding = false;

    public EntityEnergeticSheepCommon(EntityType<? extends EntityEnergeticSheepCommon> type, Level world) {
        super(type, world);
        this.xpReward = 10;
        this.woolByColor = Util.make(Maps.newEnumMap(DyeColor.class), (p_203402_0_) -> {
            p_203402_0_.put(DyeColor.WHITE, RegistryEntries.ITEM_ENERGETIC_WOOL_WHITE.value());
            p_203402_0_.put(DyeColor.ORANGE, RegistryEntries.ITEM_ENERGETIC_WOOL_ORANGE.value());
            p_203402_0_.put(DyeColor.MAGENTA, RegistryEntries.ITEM_ENERGETIC_WOOL_MAGENTA.value());
            p_203402_0_.put(DyeColor.LIGHT_BLUE, RegistryEntries.ITEM_ENERGETIC_WOOL_LIGHT_BLUE.value());
            p_203402_0_.put(DyeColor.YELLOW, RegistryEntries.ITEM_ENERGETIC_WOOL_YELLOW.value());
            p_203402_0_.put(DyeColor.LIME, RegistryEntries.ITEM_ENERGETIC_WOOL_LIME.value());
            p_203402_0_.put(DyeColor.PINK, RegistryEntries.ITEM_ENERGETIC_WOOL_PINK.value());
            p_203402_0_.put(DyeColor.GRAY, RegistryEntries.ITEM_ENERGETIC_WOOL_GRAY.value());
            p_203402_0_.put(DyeColor.LIGHT_GRAY, RegistryEntries.ITEM_ENERGETIC_WOOL_LIGHT_GRAY.value());
            p_203402_0_.put(DyeColor.CYAN, RegistryEntries.ITEM_ENERGETIC_WOOL_CYAN.value());
            p_203402_0_.put(DyeColor.PURPLE, RegistryEntries.ITEM_ENERGETIC_WOOL_PURPLE.value());
            p_203402_0_.put(DyeColor.BLUE, RegistryEntries.ITEM_ENERGETIC_WOOL_BLUE.value());
            p_203402_0_.put(DyeColor.BROWN, RegistryEntries.ITEM_ENERGETIC_WOOL_BROWN.value());
            p_203402_0_.put(DyeColor.GREEN, RegistryEntries.ITEM_ENERGETIC_WOOL_GREEN.value());
            p_203402_0_.put(DyeColor.RED, RegistryEntries.ITEM_ENERGETIC_WOOL_RED.value());
            p_203402_0_.put(DyeColor.BLACK, RegistryEntries.ITEM_ENERGETIC_WOOL_BLACK.value());
        });
    }

    public static int getCapacity(DyeColor color) {
        return getCapacity(color, EntityEnergeticSheepConfigCommon.sheepBaseCapacity);
    }

    public static int getCapacity(DyeColor color, int base) {
        return (int) (base * (1 + (DyeColor.values().length - color.ordinal() - 1)
                * EntityEnergeticSheepConfigCommon.additionalCapacityColorFactor));
    }

    public static void onLightning(Sheep sheep) {
        EntityEnergeticSheepCommon energeticSheep = RegistryEntries.ENTITY_TYPE_ENERGETIC_SHEEP.value().create(sheep.level());

        if (sheep.hasCustomName()) {
            energeticSheep.setCustomName(sheep.getCustomName());
        }
        energeticSheep.age = sheep.getAge();
        energeticSheep.setSheared(sheep.isSheared());
        energeticSheep.setFleeceColorInternal(sheep.getColor());
        energeticSheep.absMoveTo(sheep.getX(), sheep.getY(), sheep.getZ(),
                sheep.yRotO, sheep.xRotO);

        sheep.remove(RemovalReason.DISCARDED);
        sheep.level().addFreshEntity(energeticSheep);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.targetSelector.removeGoal(this.eatBlockGoal);
        this.eatBlockGoal = new EntityAIEatGrassFast(this);
        this.targetSelector.addGoal(5, this.eatBlockGoal);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ENERGY, 0);
    }

    protected void updateEnergy(int energy) {
        this.entityData.set(ENERGY, energy);
        float ratio = (float) energy / getCapacity();
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.23D * (1 + ratio));
        if (energy == 0) {
            this.setSheared(true);
        } else if (this.isSheared()) {
            this.setSheared(false);
        }
    }

    public int getEnergyClient() {
        return this.entityData.get(ENERGY);
    }

    public abstract int getCapacity();

    @Override
    public void ate() {
        super.ate();
        restoreAllEnergy();
    }

    protected abstract void initializeEnergy(DyeColor color);

    protected abstract void restoreAllEnergy();

    protected abstract void consumeAllEnergy();

    @Override
    public void shear(SoundSource soundSource) {
        // We need to override this in case other mods are calling mobInteract with shears explicitly
//        super.shear(p_29819_);

        for (ItemStack item : this.onShearedInternal(null, null, null, null)) {
            ItemEntity itementity = this.spawnAtLocation(item);
            if (itementity != null) {
                itementity.setDeltaMovement(itementity.getDeltaMovement().add((double)((this.random.nextFloat() - this.random.nextFloat()) * 0.1F), (double)(this.random.nextFloat() * 0.05F), (double)((this.random.nextFloat() - this.random.nextFloat()) * 0.1F)));
            }
        }
    }

    protected List<ItemStack> onShearedInternal(@Nullable Player player, ItemStack item, Level world, BlockPos pos) {
        this.setSheared(true);
        consumeAllEnergy();
        int i = 1 + this.random.nextInt(3);

        List<ItemStack> ret = Lists.newArrayList();
        for (int j = 0; j < i; ++j) {
            ret.add(new ItemStack(woolByColor.get(this.getColor())));
        }

        this.playSound(SoundEvents.SHEEP_SHEAR, 1.0F, 1.0F);
        return ret;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("powerBreeding", powerBreeding);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setFleeceColorInternal(DyeColor.byId(compound.getByte("Color")));
        this.powerBreeding = compound.getBoolean("powerBreeding");
    }

    // MCP: createChild
    @Override
    public Sheep getBreedOffspring(ServerLevel world, AgeableMob ageable) {
        int chance = this.powerBreeding
                ? EntityEnergeticSheepConfigCommon.babyChancePowerBreeding : EntityEnergeticSheepConfigCommon.babyChance;
        this.powerBreeding = false;
        if (chance > 0 && this.random.nextInt(chance) == 0) {
            EntityEnergeticSheepCommon child = RegistryEntries.ENTITY_TYPE_ENERGETIC_SHEEP.value().create(getCommandSenderWorld());

            // If parents have equal color, child has same color, otherwise random.
            DyeColor color;
            if (this.getColor() == ((EntityEnergeticSheepCommon) ageable).getColor()) {
                color = this.getColor();
            } else {
                color = getRandomColor(this.random);
            }
            child.setFleeceColorInternal(color);

            return child;
        }
        return super.getBreedOffspring(world, ageable);
    }

    @Override
    public float getVoicePitch() {
        return this.isBaby()
                ? (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 2.0F
                : (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.5F;
    }

    @Nullable
    @Override
    public ResourceKey<LootTable> getDefaultLootTable() {
        if (this.isSheared()) {
            return EntityType.SHEEP.getDefaultLootTable();
        } else {
            switch (this.getColor()) {
                case WHITE:
                default:
                    return LOOTTABLE_SHEEP_WHITE;
                case ORANGE:
                    return LOOTTABLE_SHEEP_ORANGE;
                case MAGENTA:
                    return LOOTTABLE_SHEEP_MAGENTA;
                case LIGHT_BLUE:
                    return LOOTTABLE_SHEEP_LIGHT_BLUE;
                case YELLOW:
                    return LOOTTABLE_SHEEP_YELLOW;
                case LIME:
                    return LOOTTABLE_SHEEP_LIME;
                case PINK:
                    return LOOTTABLE_SHEEP_PINK;
                case GRAY:
                    return LOOTTABLE_SHEEP_GRAY;
                case LIGHT_GRAY:
                    return LOOTTABLE_SHEEP_LIGHT_GRAY;
                case CYAN:
                    return LOOTTABLE_SHEEP_CYAN;
                case PURPLE:
                    return LOOTTABLE_SHEEP_PURPLE;
                case BLUE:
                    return LOOTTABLE_SHEEP_BLUE;
                case BROWN:
                    return LOOTTABLE_SHEEP_BROWN;
                case GREEN:
                    return LOOTTABLE_SHEEP_GREEN;
                case RED:
                    return LOOTTABLE_SHEEP_RED;
                case BLACK:
                    return LOOTTABLE_SHEEP_BLACK;
            }
        }
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn) {
        SpawnGroupData livingdata = super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn);
        this.setFleeceColorInternal(getRandomColor(this.random));
        return livingdata;
    }

    // MCP: processInteract
    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        // Stop dying action
        if (player.getItemInHand(hand).getItem() instanceof DyeItem) {
            return InteractionResult.CONSUME;
        }
        return super.mobInteract(player, hand);
    }

    @Override
    public void setColor(DyeColor color) {
        // Do nothing, we don't allow custom color setting
    }

    protected void setFleeceColorInternal(DyeColor color) {
        super.setColor(color);
        this.initializeEnergy(color);
    }

    protected static DyeColor getRandomColor(RandomSource random) {
        return DyeColor.values()[random.nextInt(DyeColor.values().length)];
    }

    protected boolean isPowerBreedingItem(ItemStack stack) {
        for(String name : EntityEnergeticSheepConfigCommon.powerBreedingItems) {
            if(BuiltInRegistries.ITEM.getKey(stack.getItem()).toString().equals(name)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return super.isFood(stack) || isPowerBreedingItem(stack);
    }

    @Override
    protected void usePlayerItem(Player player, InteractionHand hand, ItemStack stack) {
        if (isPowerBreedingItem(stack)) {
            powerBreeding = true;
            if (!getCommandSenderWorld().isClientSide()) {
                ((ServerLevel) getCommandSenderWorld()).sendParticles(ParticleTypes.INSTANT_EFFECT,
                        this.getX(), this.getY(), this.getZ(), 10, 0.5F, 0.5F, 0.5F, 2F);
            }
        }
        super.usePlayerItem(player, hand, stack);
    }

    @Override
    public boolean isPowered() {
        return this.getEnergyClient() > 0;
    }
}
