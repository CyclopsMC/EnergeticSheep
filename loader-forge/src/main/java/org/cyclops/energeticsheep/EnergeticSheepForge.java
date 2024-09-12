package org.cyclops.energeticsheep;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import org.cyclops.cyclopscore.config.ConfigHandlerCommon;
import org.cyclops.cyclopscore.init.ModBaseForge;
import org.cyclops.cyclopscore.proxy.IClientProxyCommon;
import org.cyclops.cyclopscore.proxy.ICommonProxyCommon;
import org.cyclops.energeticsheep.biome.modifier.BiomeModifierSpawnEnergeticSheepConfig;
import org.cyclops.energeticsheep.block.BlockEnergeticWoolConfigForge;
import org.cyclops.energeticsheep.entity.EntityEnergeticSheepConfigForge;
import org.cyclops.energeticsheep.item.ItemEnergeticShearsConfigForge;
import org.cyclops.energeticsheep.proxy.ClientProxyForge;
import org.cyclops.energeticsheep.proxy.CommonProxyForge;

/**
 * The main mod class of this mod.
 * @author rubensworks
 *
 */
@Mod(Reference.MOD_ID)
public class EnergeticSheepForge extends ModBaseForge<EnergeticSheepForge> {

    /**
     * The unique instance of this mod.
     */
    public static EnergeticSheepForge _instance;

    public EnergeticSheepForge() {
        super(Reference.MOD_ID, (instance) -> _instance = instance);
    }

    @Override
    protected IClientProxyCommon constructClientProxy() {
        return new ClientProxyForge();
    }

    @Override
    protected ICommonProxyCommon constructCommonProxy() {
        return new CommonProxyForge();
    }

    @Override
    protected boolean hasDefaultCreativeModeTab() {
        return true;
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

        configHandler.addConfigurable(new EntityEnergeticSheepConfigForge());

        configHandler.addConfigurable(new ItemEnergeticShearsConfigForge());

        for (DyeColor color : DyeColor.values()) {
            configHandler.addConfigurable(new BlockEnergeticWoolConfigForge(color));
        }

        configHandler.addConfigurable(new BiomeModifierSpawnEnergeticSheepConfig());
    }
}
