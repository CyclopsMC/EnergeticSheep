package org.cyclops.energeticsheep.biome.modifier;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.attribute.modifier.MobSpawnSettingsModifier;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.MobSpawnSettingsBuilder;
import net.minecraftforge.common.world.ModifiableBiomeInfo;
import org.cyclops.energeticsheep.RegistryEntries;
import org.cyclops.energeticsheep.RegistryEntriesForge;

public record BiomeModifierSpawnEnergeticSheep(HolderSet<Biome> biomes, HolderSet<Biome> biomesBlacklist, int spawnWeight, int minCount, int maxCount) implements BiomeModifier {
    @Override
    public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        if (phase == Phase.ADD && biomes.contains(biome) && !biomesBlacklist.contains(biome)) {
            var entry = builder.attributes().get(EnvironmentAttributes.NATURAL_MOB_SPAWNS);
            if (entry == null || entry.modifier() != MobSpawnSettingsModifier.overlay()) {
                return;
            }
            MobSpawnSettingsBuilder spawns = new MobSpawnSettingsBuilder(entry.cast(MobSpawnSettingsModifier.overlay()));
            spawns.addSpawn(RegistryEntries.ENTITY_TYPE_ENERGETIC_SHEEP.value(), MobCategory.CREATURE, spawnWeight, UniformInt.of(minCount, maxCount));
            builder.attributes().modify(EnvironmentAttributes.NATURAL_MOB_SPAWNS, MobSpawnSettingsModifier.overlay(), spawns.build());
        }
    }

    @Override
    public MapCodec<? extends BiomeModifier> codec() {
        return RegistryEntriesForge.BIOME_MODIFIER_SPAWN_ENERGETIC_SHEEP.get();
    }
}
