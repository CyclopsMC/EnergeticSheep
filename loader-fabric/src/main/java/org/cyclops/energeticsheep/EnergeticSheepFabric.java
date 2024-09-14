package org.cyclops.energeticsheep;

import net.fabricmc.api.ModInitializer;
import org.cyclops.cyclopscore.config.ConfigHandlerCommon;
import org.cyclops.cyclopscore.init.ModBaseFabric;
import org.cyclops.cyclopscore.proxy.IClientProxyCommon;
import org.cyclops.cyclopscore.proxy.ICommonProxyCommon;
import org.cyclops.energeticsheep.proxy.ClientProxyFabric;
import org.cyclops.energeticsheep.proxy.CommonProxyFabric;

/**
 * The main mod class of CyclopsCore.
 * @author rubensworks
 */
public class EnergeticSheepFabric extends ModBaseFabric<EnergeticSheepFabric> implements ModInitializer {

    /**
     * The unique instance of this mod.
     */
    public static EnergeticSheepFabric _instance;

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
    protected void onConfigsRegister(ConfigHandlerCommon configHandler) {
        super.onConfigsRegister(configHandler);

        configHandler.addConfigurable(new GeneralConfig(this));
    }
}
