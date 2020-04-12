package org.cyclops.energeticsheep.entity;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IChargeableMob;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
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
@OnlyIn(value = Dist.CLIENT, _interface = IChargeableMob.class)
public class EntityEnergeticSheep extends SheepEntity implements IChargeableMob {

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

    private final Map<DyeColor, IItemProvider> woolByColor;

    private static final DataParameter<Integer> ENERGY = EntityDataManager.<Integer>createKey(EntityEnergeticSheep.class, DataSerializers.VARINT);

    @Nullable
    private IEnergyStorage energyStorage;
    private boolean powerBreeding = false;

    public EntityEnergeticSheep(EntityType<? extends EntityEnergeticSheep> type, World world) {
        super(type, world);
        this.experienceValue = 10;
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
        if (event.getEntity().getClass() == SheepEntity.class) {
            SheepEntity sheep = (SheepEntity) event.getEntity();
            EntityEnergeticSheep energeticSheep = RegistryEntries.ENTITY_TYPE_ENERGETIC_SHEEP.create(sheep.world);

            if (sheep.hasCustomName()) {
                energeticSheep.setCustomName(sheep.getCustomName());
            }
            energeticSheep.growingAge = sheep.getGrowingAge();
            energeticSheep.setSheared(sheep.getSheared());
            energeticSheep.setFleeceColorInternal(sheep.getFleeceColor());
            energeticSheep.setPositionAndRotation(sheep.getPosX(), sheep.getPosY(), sheep.getPosZ(),
                    sheep.rotationYaw, sheep.rotationPitch);

            sheep.remove();
            sheep.world.addEntity(energeticSheep);

            event.getLightning().remove();
        }
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.targetSelector.removeGoal(this.eatGrassGoal);
        this.eatGrassGoal = new EntityAIEatGrassFast(this);
        this.targetSelector.addGoal(5, this.eatGrassGoal);
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(ENERGY, 0);
    }

    protected void updateEnergy(int energy) {
        this.dataManager.set(ENERGY, energy);
        float ratio = (float) energy / getCapacity();
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.23D * (1 + ratio));
        if (energy == 0) {
            this.setSheared(true);
        } else if (this.getSheared()) {
            this.setSheared(false);
        }
    }

    public int getEnergyClient() {
        return this.dataManager.get(ENERGY);
    }

    public int getCapacity() {
        return this.energyStorage != null ? this.energyStorage.getMaxEnergyStored() : 0;
    }

    @Override
    public void eatGrassBonus() {
        super.eatGrassBonus();
        if (this.energyStorage != null) {
            this.energyStorage.receiveEnergy(this.energyStorage.getMaxEnergyStored(), false);
        }
    }

    @Override
    public List<ItemStack> onSheared(ItemStack item, IWorld world, BlockPos pos, int fortune) {
        this.setSheared(true);
        if (this.energyStorage != null) {
            this.energyStorage.extractEnergy(this.energyStorage.getMaxEnergyStored(), false);
        }
        int i = 1 + this.rand.nextInt(3);

        List<ItemStack> ret = Lists.newArrayList();
        for (int j = 0; j < i; ++j) {
            ret.add(new ItemStack(woolByColor.get(this.getFleeceColor())));
        }

        this.playSound(SoundEvents.ENTITY_SHEEP_SHEAR, 1.0F, 1.0F);
        return ret;
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        if (this.energyStorage != null) {
            compound.putInt("energy", this.energyStorage.getEnergyStored());
            compound.putBoolean("powerBreeding", powerBreeding);
        }
    }

    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
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

    @Override
    public SheepEntity createChild(AgeableEntity ageable) {
        int chance = this.powerBreeding
                ? EntityEnergeticSheepConfig.babyChancePowerBreeding : EntityEnergeticSheepConfig.babyChance;
        this.powerBreeding = false;
        if (chance > 0 && this.rand.nextInt(chance) == 0) {
            EntityEnergeticSheep child = RegistryEntries.ENTITY_TYPE_ENERGETIC_SHEEP.create(getEntityWorld());

            // If parents have equal color, child has same color, otherwise random.
            DyeColor color;
            if (this.getFleeceColor() == ((EntityEnergeticSheep) ageable).getFleeceColor()) {
                color = this.getFleeceColor();
            } else {
                color = getRandomColor(this.world.rand);
            }
            child.setFleeceColorInternal(color);

            return child;
        }
        return super.createChild(ageable);
    }

    @Override
    protected float getSoundPitch() {
        return this.isChild()
                ? (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 2.0F
                : (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.5F;
    }

    @Nullable
    @Override
    public ResourceLocation getLootTable() {
        if (this.getSheared()) {
            return EntityType.SHEEP.getLootTable();
        } else {
            switch (this.getFleeceColor()) {
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

    @Nullable
    public ILivingEntityData onInitialSpawn(IWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        ILivingEntityData livingdata = super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        this.setFleeceColorInternal(getRandomColor(this.world.rand));
        this.energyStorage.receiveEnergy(this.energyStorage.getMaxEnergyStored(), false);
        return livingdata;
    }

    @Override
    public boolean processInteract(PlayerEntity player, Hand hand) {
        // Stop dying action
        return player.getHeldItem(hand).getItem() instanceof DyeItem || super.processInteract(player, hand);
    }

    @Override
    public void setFleeceColor(DyeColor color) {
        // Do nothing, we don't allow custom color setting
    }

    protected void setFleeceColorInternal(DyeColor color) {
        super.setFleeceColor(color);
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
    public boolean isBreedingItem(ItemStack stack) {
        return super.isBreedingItem(stack) || isPowerBreedingItem(stack);
    }

    @Override
    protected void consumeItemFromStack(PlayerEntity player, ItemStack stack) {
        if (isPowerBreedingItem(stack)) {
            powerBreeding = true;
            if (!getEntityWorld().isRemote()) {
                ((ServerWorld) getEntityWorld()).spawnParticle(ParticleTypes.INSTANT_EFFECT,
                        this.getPosX(), this.getPosY(), this.getPosZ(), 10, 0.5F, 0.5F, 0.5F, 2F);
            }
        }
        super.consumeItemFromStack(player, stack);
    }

    @Override
    public boolean func_225509_J__() {
        return this.getEnergyClient() > 0;
    }
}
