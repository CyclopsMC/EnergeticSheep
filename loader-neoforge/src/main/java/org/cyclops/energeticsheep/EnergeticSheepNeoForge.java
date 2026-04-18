package org.cyclops.energeticsheep;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import org.apache.logging.log4j.Level;
import org.cyclops.cyclopscore.config.ConfigHandlerCommon;
import org.cyclops.cyclopscore.init.ModBaseNeoForge;
import org.cyclops.cyclopscore.proxy.IClientProxy;
import org.cyclops.cyclopscore.proxy.ICommonProxy;
import org.cyclops.energeticsheep.biome.modifier.BiomeModifierSpawnEnergeticSheepConfig;
import org.cyclops.energeticsheep.block.BlockEnergeticWoolConfigNeoForge;
import org.cyclops.energeticsheep.block.blockentity.BlockEntityEnergeticWoolConfigNeoForge;
import org.cyclops.energeticsheep.entity.EntityEnergeticSheepConfigNeoForge;
import org.cyclops.energeticsheep.gametest.GameTestsCommon;
import org.cyclops.energeticsheep.item.ItemEnergeticShearsConfigNeoForge;
import org.cyclops.energeticsheep.proxy.ClientProxy;
import org.cyclops.energeticsheep.proxy.CommonProxy;

/**
 * The main mod class of this mod.
 * @author rubensworks (aka kroeserr)
 *
 */
@Mod(Reference.MOD_ID)
public class EnergeticSheepNeoForge extends ModBaseNeoForge<EnergeticSheepNeoForge> {

    /**
     * The unique instance of this mod.
     */
    public static EnergeticSheepNeoForge _instance;

    public EnergeticSheepNeoForge(IEventBus modEventBus) {
        super(Reference.MOD_ID, (instance) -> _instance = instance, modEventBus);
    }

    @Override
    protected IClientProxy constructClientProxy() {
        return new ClientProxy();
    }

    @Override
    protected ICommonProxy constructCommonProxy() {
        return new CommonProxy();
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

        configHandler.addConfigurable(new EntityEnergeticSheepConfigNeoForge());

        configHandler.addConfigurable(new ItemEnergeticShearsConfigNeoForge());

        for (DyeColor color : DyeColor.values()) {
            configHandler.addConfigurable(new BlockEnergeticWoolConfigNeoForge(color));
        }

        configHandler.addConfigurable(new BlockEntityEnergeticWoolConfigNeoForge());

        configHandler.addConfigurable(new BiomeModifierSpawnEnergeticSheepConfig());
    }

    @Override
    public Class<?>[] getGameTestClasses() {
        return new Class<?>[] { GameTestsCommon.class };
    }

    /**
     * Log a new info message for this mod.
     * @param message The message to show.
     */
    public static void clog(String message) {
        clog(Level.INFO, message);
    }

    /**
     * Log a new message of the given level for this mod.
     * @param level The level in which the message must be shown.
     * @param message The message to show.
     */
    public static void clog(Level level, String message) {
        EnergeticSheepNeoForge._instance.getLoggerHelper().log(level, message);
    }

}
