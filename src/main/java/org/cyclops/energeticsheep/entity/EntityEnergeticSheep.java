package org.cyclops.energeticsheep.entity;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.event.entity.EntityStruckByLightningEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import org.cyclops.energeticsheep.Reference;
import org.cyclops.energeticsheep.RegistryEntries;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * A sheep that produces energy.
 * @author rubensworks
 *
 */
@Mod.EventBusSubscriber
@OnlyIn(value = Dist.CLIENT, _interface = PowerableMob.class)
public class EntityEnergeticSheep extends Sheep implements PowerableMob {

    public static final ResourceLocation LOOTTABLE_SHEEP_WHITE      = new ResourceLocation(Reference.MOD_ID, "entities/energetic_sheep/white");
    public static final ResourceLocation LOOTTABLE_SHEEP_ORANGE     = new ResourceLocation(Reference.MOD_ID, "entities/energetic_sheep/orange");
    public static final ResourceLocation LOOTTABLE_SHEEP_MAGENTA    = new ResourceLocation(Reference.MOD_ID, "entities/energetic_sheep/magenta");
    public static final ResourceLocation LOOTTABLE_SHEEP_LIGHT_BLUE = new ResourceLocation(Reference.MOD_ID, "entities/energetic_sheep/light_blue");
    public static final ResourceLocation LOOTTABLE_SHEEP_YELLOW     = new ResourceLocation(Reference.MOD_ID, "entities/energetic_sheep/yellow");
    public static final ResourceLocation LOOTTABLE_SHEEP_LIME       = new ResourceLocation(Reference.MOD_ID, "entities/energetic_sheep/lime");
    public static final ResourceLocation LOOTTABLE_SHEEP_PINK       = new ResourceLocation(Reference.MOD_ID, "entities/energetic_sheep/pink");
    public static final ResourceLocation LOOTTABLE_SHEEP_GRAY       = new ResourceLocation(Reference.MOD_ID, "entities/energetic_sheep/gray");
    public static final ResourceLocation LOOTTABLE_SHEEP_LIGHT_GRAY = new ResourceLocation(Reference.MOD_ID, "entities/energetic_sheep/light_gray");
    public static final ResourceLocation LOOTTABLE_SHEEP_CYAN       = new ResourceLocation(Reference.MOD_ID, "entities/energetic_sheep/cyan");
    public static final ResourceLocation LOOTTABLE_SHEEP_PURPLE     = new ResourceLocation(Reference.MOD_ID, "entities/energetic_sheep/purple");
    public static final ResourceLocation LOOTTABLE_SHEEP_BLUE       = new ResourceLocation(Reference.MOD_ID, "entities/energetic_sheep/blue");
    public static final ResourceLocation LOOTTABLE_SHEEP_BROWN      = new ResourceLocation(Reference.MOD_ID, "entities/energetic_sheep/brown");
    public static final ResourceLocation LOOTTABLE_SHEEP_GREEN      = new ResourceLocation(Reference.MOD_ID, "entities/energetic_sheep/green");
    public static final ResourceLocation LOOTTABLE_SHEEP_RED        = new ResourceLocation(Reference.MOD_ID, "entities/energetic_sheep/red");
    public static final ResourceLocation LOOTTABLE_SHEEP_BLACK      = new ResourceLocation(Reference.MOD_ID, "entities/energetic_sheep/black");

    private final Map<DyeColor, ItemLike> woolByColor;

    private static final EntityDataAccessor<Integer> ENERGY = SynchedEntityData.<Integer>defineId(EntityEnergeticSheep.class, EntityDataSerializers.INT);

    @Nullable
    private IEnergyStorage energyStorage;
    private boolean powerBreeding = false;

    public EntityEnergeticSheep(EntityType<? extends EntityEnergeticSheep> type, Level world) {
        super(type, world);
        this.xpReward = 10;
        this.woolByColor = Util.make(Maps.newEnumMap(DyeColor.class), (p_203402_0_) -> {
            p_203402_0_.put(DyeColor.WHITE, RegistryEntries.ITEM_ENERGETIC_WOOL_WHITE);
            p_203402_0_.put(DyeColor.ORANGE, RegistryEntries.ITEM_ENERGETIC_WOOL_ORANGE);
            p_203402_0_.put(DyeColor.MAGENTA, RegistryEntries.ITEM_ENERGETIC_WOOL_MAGENTA);
            p_203402_0_.put(DyeColor.LIGHT_BLUE, RegistryEntries.ITEM_ENERGETIC_WOOL_LIGHT_BLUE);
            p_203402_0_.put(DyeColor.YELLOW, RegistryEntries.ITEM_ENERGETIC_WOOL_YELLOW);
            p_203402_0_.put(DyeColor.LIME, RegistryEntries.ITEM_ENERGETIC_WOOL_LIME);
            p_203402_0_.put(DyeColor.PINK, RegistryEntries.ITEM_ENERGETIC_WOOL_PINK);
            p_203402_0_.put(DyeColor.GRAY, RegistryEntries.ITEM_ENERGETIC_WOOL_GRAY);
            p_203402_0_.put(DyeColor.LIGHT_GRAY, RegistryEntries.ITEM_ENERGETIC_WOOL_LIGHT_GRAY);
            p_203402_0_.put(DyeColor.CYAN, RegistryEntries.ITEM_ENERGETIC_WOOL_CYAN);
            p_203402_0_.put(DyeColor.PURPLE, RegistryEntries.ITEM_ENERGETIC_WOOL_PURPLE);
            p_203402_0_.put(DyeColor.BLUE, RegistryEntries.ITEM_ENERGETIC_WOOL_BLUE);
            p_203402_0_.put(DyeColor.BROWN, RegistryEntries.ITEM_ENERGETIC_WOOL_BROWN);
            p_203402_0_.put(DyeColor.GREEN, RegistryEntries.ITEM_ENERGETIC_WOOL_GREEN);
            p_203402_0_.put(DyeColor.RED, RegistryEntries.ITEM_ENERGETIC_WOOL_RED);
            p_203402_0_.put(DyeColor.BLACK, RegistryEntries.ITEM_ENERGETIC_WOOL_BLACK);
        });
    }

    public static int getCapacity(DyeColor color) {
        return getCapacity(color, EntityEnergeticSheepConfig.sheepBaseCapacity);
    }

    public static int getCapacity(DyeColor color, int base) {
        return (int) (base * (1 + (DyeColor.values().length - color.ordinal() - 1)
                * EntityEnergeticSheepConfig.additionalCapacityColorFactor));
    }

    protected void setEnergyStorage(DyeColor color) {
        this.energyStorage = new EnergyStorage(getCapacity(color)) {
            @Override
            public int receiveEnergy(int maxReceive, boolean simulate) {
                int ret = super.receiveEnergy(maxReceive, simulate);
                if (!simulate) {
                    EntityEnergeticSheep.this.updateEnergy(energy);
                }
                return ret;
            }

            @Override
            public int extractEnergy(int maxExtract, boolean simulate) {
                int ret = super.extractEnergy(maxExtract, simulate);
                if (!simulate) {
                    EntityEnergeticSheep.this.updateEnergy(energy);
                }
                return ret;
            }
        };
    }

    @SubscribeEvent
    public static void onLightning(EntityStruckByLightningEvent event) {
        if (event.getEntity().getClass() == Sheep.class) {
            Sheep sheep = (Sheep) event.getEntity();
            EntityEnergeticSheep energeticSheep = RegistryEntries.ENTITY_TYPE_ENERGETIC_SHEEP.create(sheep.level);

            if (sheep.hasCustomName()) {
                energeticSheep.setCustomName(sheep.getCustomName());
            }
            energeticSheep.age = sheep.getAge();
            energeticSheep.setSheared(sheep.isSheared());
            energeticSheep.setFleeceColorInternal(sheep.getColor());
            energeticSheep.absMoveTo(sheep.getX(), sheep.getY(), sheep.getZ(),
                    sheep.yRotO, sheep.xRotO);

            sheep.remove(RemovalReason.DISCARDED);
            sheep.level.addFreshEntity(energeticSheep);

            event.getLightning().remove(RemovalReason.KILLED);
        }
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.targetSelector.removeGoal(this.eatBlockGoal);
        this.eatBlockGoal = new EntityAIEatGrassFast(this);
        this.targetSelector.addGoal(5, this.eatBlockGoal);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ENERGY, 0);
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

    public int getCapacity() {
        return this.energyStorage != null ? this.energyStorage.getMaxEnergyStored() : 0;
    }

    @Override
    public void ate() {
        super.ate();
        if (this.energyStorage != null) {
            this.energyStorage.receiveEnergy(this.energyStorage.getMaxEnergyStored(), false);
        }
    }

    @Override
    public List<ItemStack> onSheared(@Nullable Player player, ItemStack item, Level world, BlockPos pos, int fortune) {
        this.setSheared(true);
        if (this.energyStorage != null) {
            this.energyStorage.extractEnergy(this.energyStorage.getMaxEnergyStored(), false);
        }
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
        if (this.energyStorage != null) {
            compound.putInt("energy", this.energyStorage.getEnergyStored());
            compound.putBoolean("powerBreeding", powerBreeding);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setFleeceColorInternal(DyeColor.byId(compound.getByte("Color")));
        this.energyStorage.receiveEnergy(compound.getInt("energy"), false);
        this.powerBreeding = compound.getBoolean("powerBreeding");
    }

    @Nullable
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
        if (capability == CapabilityEnergy.ENERGY && this.energyStorage != null) {
            return LazyOptional.of(() -> this.energyStorage).cast();
        }
        return super.getCapability(capability, facing);
    }

    // MCP: createChild
    @Override
    public Sheep getBreedOffspring(ServerLevel world, AgeableMob ageable) {
        int chance = this.powerBreeding
                ? EntityEnergeticSheepConfig.babyChancePowerBreeding : EntityEnergeticSheepConfig.babyChance;
        this.powerBreeding = false;
        if (chance > 0 && this.random.nextInt(chance) == 0) {
            EntityEnergeticSheep child = RegistryEntries.ENTITY_TYPE_ENERGETIC_SHEEP.create(getCommandSenderWorld());

            // If parents have equal color, child has same color, otherwise random.
            DyeColor color;
            if (this.getColor() == ((EntityEnergeticSheep) ageable).getColor()) {
                color = this.getColor();
            } else {
                color = getRandomColor(this.level.random);
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
    public ResourceLocation getDefaultLootTable() {
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
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        SpawnGroupData livingdata = super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        this.setFleeceColorInternal(getRandomColor(this.level.random));
        this.energyStorage.receiveEnergy(this.energyStorage.getMaxEnergyStored(), false);
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
        this.setEnergyStorage(color);
    }

    protected static DyeColor getRandomColor(Random random) {
        return DyeColor.values()[random.nextInt(DyeColor.values().length)];
    }

    protected boolean isPowerBreedingItem(ItemStack stack) {
        for(String name : EntityEnergeticSheepConfig.powerBreedingItems) {
            if(ForgeRegistries.ITEMS.getKey(stack.getItem()).toString().equals(name)) {
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
