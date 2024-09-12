package org.cyclops.energeticsheep.proxy;

import org.cyclops.cyclopscore.init.ModBaseForge;
import org.cyclops.cyclopscore.proxy.CommonProxyComponentForge;
import org.cyclops.energeticsheep.EnergeticSheepForge;

/**
 * Proxy for server and client side.
 * @author rubensworks
 *
 */
public class CommonProxyForge extends CommonProxyComponentForge {

    @Override
    public ModBaseForge<?> getMod() {
        return EnergeticSheepForge._instance;
    }

}
