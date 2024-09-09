package org.cyclops.energeticsheep.biome.modifier;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.ModifiableBiomeInfo;
import org.cyclops.energeticsheep.RegistryEntries;

public record BiomeModifierSpawnEnergeticSheep(HolderSet<Biome> biomes, HolderSet<Biome> biomesBlacklist, int spawnWeight, int minCount, int maxCount) implements BiomeModifier {
    @Override
    public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        if (phase == Phase.ADD && biomes.contains(biome) && !biomesBlacklist.contains(biome)) {
            builder.getMobSpawnSettings().addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(RegistryEntries.ENTITY_TYPE_ENERGETIC_SHEEP.get(), spawnWeight, minCount, maxCount));
        }
    }

    @Override
    public MapCodec<? extends BiomeModifier> codec() {
        return RegistryEntries.BIOME_MODIFIER_SPAWN_ENERGETIC_SHEEP.get();
    }
}
