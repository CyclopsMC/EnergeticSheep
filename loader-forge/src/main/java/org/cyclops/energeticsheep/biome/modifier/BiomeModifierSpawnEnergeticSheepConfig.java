package org.cyclops.energeticsheep.biome.modifier;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.biome.Biome;
import org.cyclops.cyclopscore.config.extendedconfig.BiomeModifierConfigForge;
import org.cyclops.energeticsheep.EnergeticSheepForge;

/**
 * @author rubensworks
 */
public class BiomeModifierSpawnEnergeticSheepConfig extends BiomeModifierConfigForge<BiomeModifierSpawnEnergeticSheep> {

    public BiomeModifierSpawnEnergeticSheepConfig() {
        super(
                EnergeticSheepForge._instance,
                "spawn_energetic_sheep",
                eConfig -> RecordCodecBuilder.mapCodec(builder -> builder.group(
                        Biome.LIST_CODEC.fieldOf("biomes").forGetter(BiomeModifierSpawnEnergeticSheep::biomes),
                        Biome.LIST_CODEC.fieldOf("biomesBlacklist").forGetter(BiomeModifierSpawnEnergeticSheep::biomes),
                        Codec.INT.fieldOf("spawnWeight").forGetter(BiomeModifierSpawnEnergeticSheep::spawnWeight),
                        Codec.INT.fieldOf("minCount").forGetter(BiomeModifierSpawnEnergeticSheep::minCount),
                        Codec.INT.fieldOf("maxCount").forGetter(BiomeModifierSpawnEnergeticSheep::maxCount)
                ).apply(builder, BiomeModifierSpawnEnergeticSheep::new))
        );
    }

}
