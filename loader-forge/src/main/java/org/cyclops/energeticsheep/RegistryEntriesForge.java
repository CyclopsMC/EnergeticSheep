package org.cyclops.energeticsheep;

import com.mojang.serialization.MapCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.cyclops.energeticsheep.biome.modifier.BiomeModifierSpawnEnergeticSheep;

/**
 * Referenced registry entries.
 * @author rubensworks
 */
public class RegistryEntriesForge {

    public static final RegistryObject<MapCodec<BiomeModifierSpawnEnergeticSheep>> BIOME_MODIFIER_SPAWN_ENERGETIC_SHEEP = RegistryObject.create(ResourceLocation.parse("energeticsheep:spawn_energetic_sheep"), ForgeRegistries.BIOME_MODIFIER_SERIALIZERS.get());

}
