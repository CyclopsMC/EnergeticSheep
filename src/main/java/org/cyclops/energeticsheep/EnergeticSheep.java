package org.cyclops.energeticsheep;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.Level;
import org.cyclops.cyclopscore.config.ConfigHandler;
import org.cyclops.cyclopscore.init.ModBaseVersionable;
import org.cyclops.cyclopscore.proxy.IClientProxy;
import org.cyclops.cyclopscore.proxy.ICommonProxy;
import org.cyclops.energeticsheep.biome.modifier.BiomeModifierSpawnEnergeticSheepConfig;
import org.cyclops.energeticsheep.block.BlockEnergeticWoolConfig;
import org.cyclops.energeticsheep.entity.EntityEnergeticSheepConfig;
import org.cyclops.energeticsheep.item.ItemEnergeticShearsConfig;
import org.cyclops.energeticsheep.item.ItemEnergeticSheepSpawnEggConfig;
import org.cyclops.energeticsheep.proxy.ClientProxy;
import org.cyclops.energeticsheep.proxy.CommonProxy;

/**
 * The main mod class of this mod.
 * @author rubensworks (aka kroeserr)
 *
 */
@Mod(Reference.MOD_ID)
public class EnergeticSheep extends ModBaseVersionable<EnergeticSheep> {

    /**
     * The unique instance of this mod.
     */
    public static EnergeticSheep _instance;

    public EnergeticSheep() {
        super(Reference.MOD_ID, (instance) -> _instance = instance);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    protected IClientProxy constructClientProxy() {
        return new ClientProxy();
    }

    @Override
    @OnlyIn(Dist.DEDICATED_SERVER)
    protected ICommonProxy constructCommonProxy() {
        return new CommonProxy();
    }

    @Override
    protected CreativeModeTab.Builder constructDefaultCreativeModeTab(CreativeModeTab.Builder builder) {
        return super.constructDefaultCreativeModeTab(builder)
                .icon(() -> new ItemStack(RegistryEntries.ITEM_ENERGETIC_SHEARS));
    }

    @Override
    protected void onConfigsRegister(ConfigHandler configHandler) {
        super.onConfigsRegister(configHandler);

        configHandler.addConfigurable(new GeneralConfig());

        EntityEnergeticSheepConfig entityEnergeticSheepConfig = new EntityEnergeticSheepConfig();
        configHandler.addConfigurable(entityEnergeticSheepConfig);

        configHandler.addConfigurable(new ItemEnergeticShearsConfig());
        configHandler.addConfigurable(new ItemEnergeticSheepSpawnEggConfig(entityEnergeticSheepConfig));

        for (DyeColor color : DyeColor.values()) {
            configHandler.addConfigurable(new BlockEnergeticWoolConfig(color));
        }

        configHandler.addConfigurable(new BiomeModifierSpawnEnergeticSheepConfig());
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
        EnergeticSheep._instance.getLoggerHelper().log(level, message);
    }

}
