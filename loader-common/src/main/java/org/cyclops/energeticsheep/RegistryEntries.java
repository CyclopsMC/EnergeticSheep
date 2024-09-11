package org.cyclops.energeticsheep;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import org.cyclops.cyclopscore.config.DeferredHolderCommon;
import org.cyclops.energeticsheep.entity.EntityEnergeticSheepCommon;


/**
 * Referenced registry entries.
 * @author rubensworks
 */
public class RegistryEntries {

    public static final DeferredHolderCommon<Item, Item> ITEM_ENERGETIC_SHEARS = DeferredHolderCommon.create(Registries.ITEM, ResourceLocation.parse("energeticsheep:energetic_shears"));
    public static final DeferredHolderCommon<Item, Item> ITEM_ENERGETIC_WOOL_WHITE = DeferredHolderCommon.create(Registries.ITEM, ResourceLocation.parse("energeticsheep:white_energetic_wool"));
    public static final DeferredHolderCommon<Item, Item> ITEM_ENERGETIC_WOOL_ORANGE = DeferredHolderCommon.create(Registries.ITEM, ResourceLocation.parse("energeticsheep:orange_energetic_wool"));
    public static final DeferredHolderCommon<Item, Item> ITEM_ENERGETIC_WOOL_MAGENTA = DeferredHolderCommon.create(Registries.ITEM, ResourceLocation.parse("energeticsheep:magenta_energetic_wool"));
    public static final DeferredHolderCommon<Item, Item> ITEM_ENERGETIC_WOOL_LIGHT_BLUE = DeferredHolderCommon.create(Registries.ITEM, ResourceLocation.parse("energeticsheep:light_blue_energetic_wool"));
    public static final DeferredHolderCommon<Item, Item> ITEM_ENERGETIC_WOOL_YELLOW = DeferredHolderCommon.create(Registries.ITEM, ResourceLocation.parse("energeticsheep:yellow_energetic_wool"));
    public static final DeferredHolderCommon<Item, Item> ITEM_ENERGETIC_WOOL_LIME = DeferredHolderCommon.create(Registries.ITEM, ResourceLocation.parse("energeticsheep:lime_energetic_wool"));
    public static final DeferredHolderCommon<Item, Item> ITEM_ENERGETIC_WOOL_PINK = DeferredHolderCommon.create(Registries.ITEM, ResourceLocation.parse("energeticsheep:pink_energetic_wool"));
    public static final DeferredHolderCommon<Item, Item> ITEM_ENERGETIC_WOOL_GRAY = DeferredHolderCommon.create(Registries.ITEM, ResourceLocation.parse("energeticsheep:gray_energetic_wool"));
    public static final DeferredHolderCommon<Item, Item> ITEM_ENERGETIC_WOOL_LIGHT_GRAY = DeferredHolderCommon.create(Registries.ITEM, ResourceLocation.parse("energeticsheep:light_gray_energetic_wool"));
    public static final DeferredHolderCommon<Item, Item> ITEM_ENERGETIC_WOOL_CYAN = DeferredHolderCommon.create(Registries.ITEM, ResourceLocation.parse("energeticsheep:cyan_energetic_wool"));
    public static final DeferredHolderCommon<Item, Item> ITEM_ENERGETIC_WOOL_PURPLE = DeferredHolderCommon.create(Registries.ITEM, ResourceLocation.parse("energeticsheep:purple_energetic_wool"));
    public static final DeferredHolderCommon<Item, Item> ITEM_ENERGETIC_WOOL_BLUE = DeferredHolderCommon.create(Registries.ITEM, ResourceLocation.parse("energeticsheep:blue_energetic_wool"));
    public static final DeferredHolderCommon<Item, Item> ITEM_ENERGETIC_WOOL_BROWN = DeferredHolderCommon.create(Registries.ITEM, ResourceLocation.parse("energeticsheep:brown_energetic_wool"));
    public static final DeferredHolderCommon<Item, Item> ITEM_ENERGETIC_WOOL_GREEN = DeferredHolderCommon.create(Registries.ITEM, ResourceLocation.parse("energeticsheep:green_energetic_wool"));
    public static final DeferredHolderCommon<Item, Item> ITEM_ENERGETIC_WOOL_RED = DeferredHolderCommon.create(Registries.ITEM, ResourceLocation.parse("energeticsheep:red_energetic_wool"));
    public static final DeferredHolderCommon<Item, Item> ITEM_ENERGETIC_WOOL_BLACK = DeferredHolderCommon.create(Registries.ITEM, ResourceLocation.parse("energeticsheep:black_energetic_wool"));

    public static final DeferredHolderCommon<EntityType<?>, EntityType<EntityEnergeticSheepCommon>> ENTITY_TYPE_ENERGETIC_SHEEP = DeferredHolderCommon.create(Registries.ENTITY_TYPE, ResourceLocation.parse("energeticsheep:energetic_sheep"));

}
