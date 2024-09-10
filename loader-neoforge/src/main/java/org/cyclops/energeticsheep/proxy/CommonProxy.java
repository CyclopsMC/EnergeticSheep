package org.cyclops.energeticsheep.proxy;

import org.cyclops.cyclopscore.init.ModBase;
import org.cyclops.cyclopscore.proxy.CommonProxyComponent;
import org.cyclops.energeticsheep.EnergeticSheepNeoForge;

/**
 * Proxy for server and client side.
 * @author rubensworks
 *
 */
public class CommonProxy extends CommonProxyComponent {

    @Override
    public ModBase getMod() {
        return EnergeticSheepNeoForge._instance;
    }

}
