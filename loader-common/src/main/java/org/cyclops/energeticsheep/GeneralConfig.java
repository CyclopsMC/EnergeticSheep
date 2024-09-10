package org.cyclops.energeticsheep;

import org.cyclops.cyclopscore.config.extendedconfig.DummyConfigCommon;
import org.cyclops.cyclopscore.helper.ModBaseCommon;

/**
 * A config with general options for this mod.
 * @author rubensworks
 *
 */
public class GeneralConfig extends DummyConfigCommon<ModBaseCommon<?>> {

    public GeneralConfig(ModBaseCommon<?> mod) {
        super(mod, "general");
    }

}
