package org.cyclops.energeticsheep.biome.modifier;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo;
import org.cyclops.energeticsheep.RegistryEntries;

public record BiomeModifierSpawnEnergeticSheep(HolderSet<Biome> biomes, int spawnWeight, int minCount, int maxCount) implements BiomeModifier {
    @Override
    public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        if (phase == Phase.ADD && biomes.contains(biome)) {
            builder.getMobSpawnSettings().addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(RegistryEntries.ENTITY_TYPE_ENERGETIC_SHEEP, spawnWeight, minCount, maxCount));
        }
    }

    @Override
    public Codec<? extends BiomeModifier> codec() {
        return RegistryEntries.BIOME_MODIFIER_SPAWN_ENERGETIC_SHEEP;
    }
}
