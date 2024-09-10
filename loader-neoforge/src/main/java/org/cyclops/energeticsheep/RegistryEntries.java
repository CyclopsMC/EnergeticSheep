package org.cyclops.energeticsheep;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.cyclops.energeticsheep.biome.modifier.BiomeModifierSpawnEnergeticSheep;
import org.cyclops.energeticsheep.entity.EntityEnergeticSheep;

/**
 * Referenced registry entries.
 * @author rubensworks
 */
public class RegistryEntries { // TODO: rm

    public static final DeferredHolder<EntityType<?>, EntityType<EntityEnergeticSheep>> ENTITY_TYPE_ENERGETIC_SHEEP = DeferredHolder.create(Registries.ENTITY_TYPE, ResourceLocation.parse("energeticsheep:energetic_sheep"));

    public static final DeferredHolder<MapCodec<? extends BiomeModifier>, MapCodec<BiomeModifierSpawnEnergeticSheep>> BIOME_MODIFIER_SPAWN_ENERGETIC_SHEEP = DeferredHolder.create(NeoForgeRegistries.BIOME_MODIFIER_SERIALIZERS.key(), ResourceLocation.parse("energeticsheep:spawn_energetic_sheep"));

}
