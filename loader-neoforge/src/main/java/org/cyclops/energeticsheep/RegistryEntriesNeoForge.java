package org.cyclops.energeticsheep;

import com.mojang.serialization.MapCodec;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.cyclops.energeticsheep.biome.modifier.BiomeModifierSpawnEnergeticSheep;

/**
 * Referenced registry entries.
 * @author rubensworks
 */
public class RegistryEntriesNeoForge {

    public static final DeferredHolder<MapCodec<? extends BiomeModifier>, MapCodec<BiomeModifierSpawnEnergeticSheep>> BIOME_MODIFIER_SPAWN_ENERGETIC_SHEEP = DeferredHolder.create(NeoForgeRegistries.BIOME_MODIFIER_SERIALIZERS.key(), ResourceLocation.parse("energeticsheep:spawn_energetic_sheep"));

}
