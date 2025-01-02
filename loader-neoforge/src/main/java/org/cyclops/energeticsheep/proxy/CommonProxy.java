package org.cyclops.energeticsheep.proxy;

import org.cyclops.cyclopscore.init.ModBaseNeoForge;
import org.cyclops.cyclopscore.proxy.CommonProxyComponent;
import org.cyclops.energeticsheep.EnergeticSheepNeoForge;

/**
 * Proxy for server and client side.
 * @author rubensworks
 *
 */
public class CommonProxy extends CommonProxyComponent {

    @Override
    public ModBaseNeoForge<EnergeticSheepNeoForge> getMod() {
        return EnergeticSheepNeoForge._instance;
    }

}
