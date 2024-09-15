package org.cyclops.energeticsheep;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.lookup.v1.entity.EntityApiLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import org.cyclops.cyclopscore.config.ConfigHandlerCommon;
import org.cyclops.cyclopscore.init.ModBaseFabric;
import org.cyclops.cyclopscore.proxy.IClientProxyCommon;
import org.cyclops.cyclopscore.proxy.ICommonProxyCommon;
import org.cyclops.energeticsheep.block.BlockEnergeticWoolConfigFabric;
import org.cyclops.energeticsheep.entity.EntityEnergeticSheepConfigFabric;
import org.cyclops.energeticsheep.item.ItemEnergeticShearsConfigFabric;
import org.cyclops.energeticsheep.proxy.ClientProxyFabric;
import org.cyclops.energeticsheep.proxy.CommonProxyFabric;
import org.jetbrains.annotations.Nullable;
import team.reborn.energy.api.EnergyStorage;

/**
 * The main mod class of CyclopsCore.
 * @author rubensworks
 */
public class EnergeticSheepFabric extends ModBaseFabric<EnergeticSheepFabric> implements ModInitializer {

    /**
     * The unique instance of this mod.
     */
    public static EnergeticSheepFabric _instance;

    public static EntityApiLookup<EnergyStorage, @Nullable Void> ENERGY_STORAGE_ENTITY =
            EntityApiLookup.get(ResourceLocation.fromNamespaceAndPath("teamreborn", "energy"), EnergyStorage.class, Void.class);

    public EnergeticSheepFabric() {
        super(Reference.MOD_ID, (instance) -> _instance = instance);
    }

    @Override
    protected IClientProxyCommon constructClientProxy() {
        return new ClientProxyFabric();
    }

    @Override
    protected ICommonProxyCommon constructCommonProxy() {
        return new CommonProxyFabric();
    }

    @Override
    protected CreativeModeTab.Builder constructDefaultCreativeModeTab(CreativeModeTab.Builder builder) {
        return super.constructDefaultCreativeModeTab(builder)
                .icon(() -> new ItemStack(RegistryEntries.ITEM_ENERGETIC_SHEARS));
    }

    @Override
    protected void onConfigsRegister(ConfigHandlerCommon configHandler) {
        super.onConfigsRegister(configHandler);

        configHandler.addConfigurable(new GeneralConfig(this));

        configHandler.addConfigurable(new EntityEnergeticSheepConfigFabric());

        configHandler.addConfigurable(new ItemEnergeticShearsConfigFabric());

        for (DyeColor color : DyeColor.values()) {
            configHandler.addConfigurable(new BlockEnergeticWoolConfigFabric(color));
        }
    }
}
