package org.cyclops.energeticsheep;

import com.mojang.serialization.MapCodec;
import net.minecraftforge.registries.ObjectHolder;
import org.cyclops.energeticsheep.biome.modifier.BiomeModifierSpawnEnergeticSheep;

/**
 * Referenced registry entries.
 * @author rubensworks
 */
public class RegistryEntriesForge {

    @ObjectHolder(registryName = "forge:biome_modifier_serializers", value = "energeticsheep:spawn_energetic_sheep")
    public static final MapCodec<BiomeModifierSpawnEnergeticSheep> BIOME_MODIFIER_SPAWN_ENERGETIC_SHEEP = null;

}
