package org.cyclops.energeticsheep;

import com.mojang.serialization.Codec;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ObjectHolder;
import org.cyclops.energeticsheep.biome.modifier.BiomeModifierSpawnEnergeticSheep;
import org.cyclops.energeticsheep.entity.EntityEnergeticSheep;

/**
 * Referenced registry entries.
 * @author rubensworks
 */
public class RegistryEntries {

    @ObjectHolder(registryName = "item", value = "energeticsheep:energetic_shears")
    public static final Item ITEM_ENERGETIC_SHEARS = null;
    @ObjectHolder(registryName = "item", value = "energeticsheep:white_energetic_wool")
    public static final Item ITEM_ENERGETIC_WOOL_WHITE = null;
    @ObjectHolder(registryName = "item", value = "energeticsheep:orange_energetic_wool")
    public static final Item ITEM_ENERGETIC_WOOL_ORANGE = null;
    @ObjectHolder(registryName = "item", value = "energeticsheep:magenta_energetic_wool")
    public static final Item ITEM_ENERGETIC_WOOL_MAGENTA = null;
    @ObjectHolder(registryName = "item", value = "energeticsheep:light_blue_energetic_wool")
    public static final Item ITEM_ENERGETIC_WOOL_LIGHT_BLUE = null;
    @ObjectHolder(registryName = "item", value = "energeticsheep:yellow_energetic_wool")
    public static final Item ITEM_ENERGETIC_WOOL_YELLOW = null;
    @ObjectHolder(registryName = "item", value = "energeticsheep:lime_energetic_wool")
    public static final Item ITEM_ENERGETIC_WOOL_LIME = null;
    @ObjectHolder(registryName = "item", value = "energeticsheep:pink_energetic_wool")
    public static final Item ITEM_ENERGETIC_WOOL_PINK = null;
    @ObjectHolder(registryName = "item", value = "energeticsheep:gray_energetic_wool")
    public static final Item ITEM_ENERGETIC_WOOL_GRAY = null;
    @ObjectHolder(registryName = "item", value = "energeticsheep:light_gray_energetic_wool")
    public static final Item ITEM_ENERGETIC_WOOL_LIGHT_GRAY = null;
    @ObjectHolder(registryName = "item", value = "energeticsheep:cyan_energetic_wool")
    public static final Item ITEM_ENERGETIC_WOOL_CYAN = null;
    @ObjectHolder(registryName = "item", value = "energeticsheep:purple_energetic_wool")
    public static final Item ITEM_ENERGETIC_WOOL_PURPLE = null;
    @ObjectHolder(registryName = "item", value = "energeticsheep:blue_energetic_wool")
    public static final Item ITEM_ENERGETIC_WOOL_BLUE = null;
    @ObjectHolder(registryName = "item", value = "energeticsheep:brown_energetic_wool")
    public static final Item ITEM_ENERGETIC_WOOL_BROWN = null;
    @ObjectHolder(registryName = "item", value = "energeticsheep:green_energetic_wool")
    public static final Item ITEM_ENERGETIC_WOOL_GREEN = null;
    @ObjectHolder(registryName = "item", value = "energeticsheep:red_energetic_wool")
    public static final Item ITEM_ENERGETIC_WOOL_RED = null;
    @ObjectHolder(registryName = "item", value = "energeticsheep:black_energetic_wool")
    public static final Item ITEM_ENERGETIC_WOOL_BLACK = null;

    @ObjectHolder(registryName = "entity_type", value = "energeticsheep:energetic_sheep")
    public static final EntityType<EntityEnergeticSheep> ENTITY_TYPE_ENERGETIC_SHEEP = null;

    @ObjectHolder(registryName = "forge:biome_modifier_serializers", value = "energeticsheep:spawn_energetic_sheep")
    public static final Codec<BiomeModifierSpawnEnergeticSheep> BIOME_MODIFIER_SPAWN_ENERGETIC_SHEEP = null;

}
