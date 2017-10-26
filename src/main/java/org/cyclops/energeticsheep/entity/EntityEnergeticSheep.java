package org.cyclops.energeticsheep.entity;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.event.entity.EntityStruckByLightningEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.cyclops.cyclopscore.config.configurable.IConfigurable;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;

import javax.annotation.Nullable;

/**
 * A sheep that produces energy.
 * @author rubensworks
 *
 */
@Mod.EventBusSubscriber
public class EntityEnergeticSheep extends EntitySheep implements IConfigurable {

    private static final DataParameter<Integer> ENERGY = EntityDataManager.<Integer>createKey(EntityEnergeticSheep.class, DataSerializers.VARINT);

    private final IEnergyStorage energyStorage;

    /**
     * Make a new instance.
     * @param world The world.
     */
    public EntityEnergeticSheep(World world) {
        super(world);
        this.energyStorage = new EnergyStorage(EntityEnergeticSheepConfig.capacity) {
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
        this.energyStorage.receiveEnergy(this.energyStorage.getMaxEnergyStored(), false);
        this.experienceValue = 10;
        this.isImmuneToFire = true;
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
            energeticSheep.setFleeceColor(sheep.getFleeceColor());
            energeticSheep.setPositionAndRotation(sheep.posX, sheep.posY, sheep.posZ,
                    sheep.rotationYaw, sheep.rotationPitch);

            sheep.world.removeEntity(sheep);
            sheep.world.spawnEntity(energeticSheep);

            event.getLightning().setDead();
        }
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
        return this.energyStorage.getMaxEnergyStored();
    }

    @Override
    public ExtendedConfig getConfig() {
        return null;
    }

    @Override
    public boolean isShearable(ItemStack item, IBlockAccess world, BlockPos pos) {
        return false;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("energy", this.energyStorage.getEnergyStored());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.energyStorage.receiveEnergy(compound.getInteger("energy"), false);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityEnergy.ENERGY || super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityEnergy.ENERGY) {
            return (T) this.energyStorage;
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public EntitySheep createChild(EntityAgeable ageable) {
        if (EntityEnergeticSheepConfig.babyChance > 0
                && this.rand.nextInt(EntityEnergeticSheepConfig.babyChance) == 0) {
            return new EntityEnergeticSheep(getEntityWorld());
        }
        return super.createChild(ageable);
    }

    @Override
    protected float getSoundPitch() {
        return this.isChild()
                ? (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.75F
                : (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.25F;
    }

    @Nullable
    @Override
    protected ResourceLocation getLootTable() {
        return LootTableList.ENTITIES_SHEEP;
    }
}
