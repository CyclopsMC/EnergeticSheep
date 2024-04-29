package org.cyclops.energeticsheep;

import com.mojang.serialization.Codec;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.cyclops.energeticsheep.biome.modifier.BiomeModifierSpawnEnergeticSheep;
import org.cyclops.energeticsheep.entity.EntityEnergeticSheep;

/**
 * Referenced registry entries.
 * @author rubensworks
 */
public class RegistryEntries {

    public static final DeferredHolder<Item, Item> ITEM_ENERGETIC_SHEARS = DeferredHolder.create(Registries.ITEM, new ResourceLocation("energeticsheep:energetic_shears"));
    public static final DeferredHolder<Item, Item> ITEM_ENERGETIC_WOOL_WHITE = DeferredHolder.create(Registries.ITEM, new ResourceLocation("energeticsheep:white_energetic_wool"));
    public static final DeferredHolder<Item, Item> ITEM_ENERGETIC_WOOL_ORANGE = DeferredHolder.create(Registries.ITEM, new ResourceLocation("energeticsheep:orange_energetic_wool"));
    public static final DeferredHolder<Item, Item> ITEM_ENERGETIC_WOOL_MAGENTA = DeferredHolder.create(Registries.ITEM, new ResourceLocation("energeticsheep:magenta_energetic_wool"));
    public static final DeferredHolder<Item, Item> ITEM_ENERGETIC_WOOL_LIGHT_BLUE = DeferredHolder.create(Registries.ITEM, new ResourceLocation("energeticsheep:light_blue_energetic_wool"));
    public static final DeferredHolder<Item, Item> ITEM_ENERGETIC_WOOL_YELLOW = DeferredHolder.create(Registries.ITEM, new ResourceLocation("energeticsheep:yellow_energetic_wool"));
    public static final DeferredHolder<Item, Item> ITEM_ENERGETIC_WOOL_LIME = DeferredHolder.create(Registries.ITEM, new ResourceLocation("energeticsheep:lime_energetic_wool"));
    public static final DeferredHolder<Item, Item> ITEM_ENERGETIC_WOOL_PINK = DeferredHolder.create(Registries.ITEM, new ResourceLocation("energeticsheep:pink_energetic_wool"));
    public static final DeferredHolder<Item, Item> ITEM_ENERGETIC_WOOL_GRAY = DeferredHolder.create(Registries.ITEM, new ResourceLocation("energeticsheep:gray_energetic_wool"));
    public static final DeferredHolder<Item, Item> ITEM_ENERGETIC_WOOL_LIGHT_GRAY = DeferredHolder.create(Registries.ITEM, new ResourceLocation("energeticsheep:light_gray_energetic_wool"));
    public static final DeferredHolder<Item, Item> ITEM_ENERGETIC_WOOL_CYAN = DeferredHolder.create(Registries.ITEM, new ResourceLocation("energeticsheep:cyan_energetic_wool"));
    public static final DeferredHolder<Item, Item> ITEM_ENERGETIC_WOOL_PURPLE = DeferredHolder.create(Registries.ITEM, new ResourceLocation("energeticsheep:purple_energetic_wool"));
    public static final DeferredHolder<Item, Item> ITEM_ENERGETIC_WOOL_BLUE = DeferredHolder.create(Registries.ITEM, new ResourceLocation("energeticsheep:blue_energetic_wool"));
    public static final DeferredHolder<Item, Item> ITEM_ENERGETIC_WOOL_BROWN = DeferredHolder.create(Registries.ITEM, new ResourceLocation("energeticsheep:brown_energetic_wool"));
    public static final DeferredHolder<Item, Item> ITEM_ENERGETIC_WOOL_GREEN = DeferredHolder.create(Registries.ITEM, new ResourceLocation("energeticsheep:green_energetic_wool"));
    public static final DeferredHolder<Item, Item> ITEM_ENERGETIC_WOOL_RED = DeferredHolder.create(Registries.ITEM, new ResourceLocation("energeticsheep:red_energetic_wool"));
    public static final DeferredHolder<Item, Item> ITEM_ENERGETIC_WOOL_BLACK = DeferredHolder.create(Registries.ITEM, new ResourceLocation("energeticsheep:black_energetic_wool"));

    public static final DeferredHolder<EntityType<?>, EntityType<EntityEnergeticSheep>> ENTITY_TYPE_ENERGETIC_SHEEP = DeferredHolder.create(Registries.ENTITY_TYPE, new ResourceLocation("energeticsheep:energetic_sheep"));

    public static final DeferredHolder<Codec<? extends BiomeModifier>, Codec<BiomeModifierSpawnEnergeticSheep>> BIOME_MODIFIER_SPAWN_ENERGETIC_SHEEP = DeferredHolder.create(NeoForgeRegistries.BIOME_MODIFIER_SERIALIZERS.key(), new ResourceLocation("energeticsheep:spawn_energetic_sheep"));

}
