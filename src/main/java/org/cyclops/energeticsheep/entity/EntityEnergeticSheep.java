package org.cyclops.energeticsheep.entity;

import com.google.common.collect.Lists;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIEatGrass;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.event.entity.EntityStruckByLightningEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.cyclops.cyclopscore.config.configurable.IConfigurable;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.energeticsheep.block.BlockEnergeticWool;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

/**
 * A sheep that produces energy.
 * @author rubensworks
 *
 */
@Mod.EventBusSubscriber
public class EntityEnergeticSheep extends EntitySheep implements IConfigurable {

    private static final DataParameter<Integer> ENERGY = EntityDataManager.<Integer>createKey(EntityEnergeticSheep.class, DataSerializers.VARINT);

    @Nullable
    private IEnergyStorage energyStorage;
    private boolean powerBreeding = false;

    /**
     * Make a new instance.
     * @param world The world.
     */
    public EntityEnergeticSheep(World world) {
        super(world);
        this.experienceValue = 10;
        this.isImmuneToFire = true;
    }

    public static int getCapacity(EnumDyeColor color) {
        return getCapacity(color, EntityEnergeticSheepConfig.sheepBaseCapacity);
    }

    public static int getCapacity(EnumDyeColor color, int base) {
        return (int) (base * (1 + (EnumDyeColor.values().length - color.ordinal() - 1)
                * EntityEnergeticSheepConfig.additionalCapacityColorFactor));
    }

    protected void setEnergyStorage(EnumDyeColor color) {
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
        if (event.getEntity().getClass() == EntitySheep.class) {
            EntitySheep sheep = (EntitySheep) event.getEntity();
            EntityEnergeticSheep energeticSheep = new EntityEnergeticSheep(sheep.world);

            if (sheep.hasCustomName()) {
                energeticSheep.setCustomNameTag(sheep.getCustomNameTag());
            }
            energeticSheep.growingAge = sheep.getGrowingAge();
            energeticSheep.setSheared(sheep.getSheared());
            energeticSheep.setFleeceColorInternal(sheep.getFleeceColor());
            energeticSheep.setPositionAndRotation(sheep.posX, sheep.posY, sheep.posZ,
                    sheep.rotationYaw, sheep.rotationPitch);

            sheep.world.removeEntity(sheep);
            sheep.world.spawnEntity(energeticSheep);

            event.getLightning().setDead();
        }
    }

    @Override
    protected void initEntityAI() {
        super.initEntityAI();
        EntityAIEatGrass eatGrassAi = null;
        for (EntityAITasks.EntityAITaskEntry taskEntry : this.tasks.taskEntries) {
            if (taskEntry.action instanceof EntityAIEatGrass) {
                eatGrassAi = (EntityAIEatGrass) taskEntry.action;
            }
        }
        this.targetTasks.removeTask(eatGrassAi);
        eatGrassAi = new EntityAIEatGrassFast(this);
        this.targetTasks.addTask(5, eatGrassAi);
        ReflectionHelper.setPrivateValue(EntitySheep.class, this, eatGrassAi,
                "field_146087_bs", "entityAIEatGrass");
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(ENERGY, 0);
    }

    protected void updateEnergy(int energy) {
        this.dataManager.set(ENERGY, energy);
        float ratio = (float) energy / getCapacity();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.23D * (1 + ratio));
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
    public ExtendedConfig getConfig() {
        return null;
    }

    @Override
    public List<ItemStack> onSheared(ItemStack item, IBlockAccess world, BlockPos pos, int fortune) {
        this.setSheared(true);
        if (this.energyStorage != null) {
            this.energyStorage.extractEnergy(this.energyStorage.getMaxEnergyStored(), false);
        }
        int i = 1 + this.rand.nextInt(3);

        List<ItemStack> ret = Lists.newArrayList();
        for (int j = 0; j < i; ++j) {
            ret.add(new ItemStack(Item.getItemFromBlock(BlockEnergeticWool.getInstance()),
                    1, this.getFleeceColor().getMetadata()));
        }

        this.playSound(SoundEvents.ENTITY_SHEEP_SHEAR, 1.0F, 1.0F);
        return ret;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        if (this.energyStorage != null) {
            compound.setInteger("energy", this.energyStorage.getEnergyStored());
            compound.setBoolean("powerBreeding", powerBreeding);
        }
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.setFleeceColorInternal(EnumDyeColor.byMetadata(compound.getByte("Color")));
        this.energyStorage.receiveEnergy(compound.getInteger("energy"), false);
        this.powerBreeding = compound.getBoolean("powerBreeding");
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityEnergy.ENERGY || super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityEnergy.ENERGY && this.energyStorage != null) {
            return (T) this.energyStorage;
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public EntitySheep createChild(EntityAgeable ageable) {
        int chance = this.powerBreeding
                ? EntityEnergeticSheepConfig.babyChancePowerBreeding : EntityEnergeticSheepConfig.babyChance;
        this.powerBreeding = false;
        if (chance > 0 && this.rand.nextInt(chance) == 0) {
            EntityEnergeticSheep child = new EntityEnergeticSheep(getEntityWorld());

            // If parents have equal color, child has same color, otherwise random.
            EnumDyeColor color;
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
    protected ResourceLocation getLootTable() {
        if (this.getSheared()) {
            return LootTableList.ENTITIES_SHEEP;
        } else {
            switch (this.getFleeceColor()) {
                case WHITE:
                default:
                    return EntityEnergeticSheepConfig.LOOTTABLE_SHEEP_WHITE;
                case ORANGE:
                    return EntityEnergeticSheepConfig.LOOTTABLE_SHEEP_ORANGE;
                case MAGENTA:
                    return EntityEnergeticSheepConfig.LOOTTABLE_SHEEP_MAGENTA;
                case LIGHT_BLUE:
                    return EntityEnergeticSheepConfig.LOOTTABLE_SHEEP_LIGHT_BLUE;
                case YELLOW:
                    return EntityEnergeticSheepConfig.LOOTTABLE_SHEEP_YELLOW;
                case LIME:
                    return EntityEnergeticSheepConfig.LOOTTABLE_SHEEP_LIME;
                case PINK:
                    return EntityEnergeticSheepConfig.LOOTTABLE_SHEEP_PINK;
                case GRAY:
                    return EntityEnergeticSheepConfig.LOOTTABLE_SHEEP_GRAY;
                case SILVER:
                    return EntityEnergeticSheepConfig.LOOTTABLE_SHEEP_SILVER;
                case CYAN:
                    return EntityEnergeticSheepConfig.LOOTTABLE_SHEEP_CYAN;
                case PURPLE:
                    return EntityEnergeticSheepConfig.LOOTTABLE_SHEEP_PURPLE;
                case BLUE:
                    return EntityEnergeticSheepConfig.LOOTTABLE_SHEEP_BLUE;
                case BROWN:
                    return EntityEnergeticSheepConfig.LOOTTABLE_SHEEP_BROWN;
                case GREEN:
                    return EntityEnergeticSheepConfig.LOOTTABLE_SHEEP_GREEN;
                case RED:
                    return EntityEnergeticSheepConfig.LOOTTABLE_SHEEP_RED;
                case BLACK:
                    return EntityEnergeticSheepConfig.LOOTTABLE_SHEEP_BLACK;
            }
        }
    }

    @Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
        livingdata = super.onInitialSpawn(difficulty, livingdata);
        this.setFleeceColorInternal(getRandomColor(this.world.rand));
        this.energyStorage.receiveEnergy(this.energyStorage.getMaxEnergyStored(), false);
        return livingdata;
    }

    @Override
    public boolean processInteract(EntityPlayer player, EnumHand hand) {
        // Stop dying action
        return player.getHeldItem(hand).getItem() instanceof ItemDye || super.processInteract(player, hand);
    }

    @Override
    public void setFleeceColor(EnumDyeColor color) {
        // Do nothing, we don't allow custom color setting
    }

    protected void setFleeceColorInternal(EnumDyeColor color) {
        super.setFleeceColor(color);
        this.setEnergyStorage(color);
    }

    protected static EnumDyeColor getRandomColor(Random random) {
        return EnumDyeColor.values()[random.nextInt(EnumDyeColor.values().length)];
    }

    protected boolean isPowerBreedingItem(ItemStack stack) {
        for(String name : EntityEnergeticSheepConfig.powerBreedingItems) {
            if(Item.REGISTRY.getNameForObject(stack.getItem()).toString().equals(name)) {
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
    protected void consumeItemFromStack(EntityPlayer player, ItemStack stack) {
        if (isPowerBreedingItem(stack)) {
            powerBreeding = true;
            if (getEntityWorld() instanceof WorldServer) {
                ((WorldServer) getEntityWorld()).spawnParticle(EnumParticleTypes.SPELL_INSTANT, false,
                        this.posX, this.posY, this.posZ, 10, 0.5F, 0.5F, 0.5F, 2F);
            }
        }
        super.consumeItemFromStack(player, stack);
    }
}
