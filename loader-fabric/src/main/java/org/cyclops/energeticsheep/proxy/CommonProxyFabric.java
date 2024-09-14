package org.cyclops.energeticsheep.proxy;

import org.cyclops.cyclopscore.init.ModBaseFabric;
import org.cyclops.cyclopscore.proxy.CommonProxyComponentFabric;
import org.cyclops.energeticsheep.EnergeticSheepFabric;

/**
 * Proxy for server and client side.
 * @author rubensworks
 *
 */
public class CommonProxyFabric extends CommonProxyComponentFabric {

    @Override
    public ModBaseFabric<?> getMod() {
        return EnergeticSheepFabric._instance;
    }

}
