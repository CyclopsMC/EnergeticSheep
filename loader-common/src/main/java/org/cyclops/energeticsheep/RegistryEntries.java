package org.cyclops.energeticsheep;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.cyclops.cyclopscore.config.DeferredHolderCommon;
import org.cyclops.energeticsheep.block.blockentity.BlockEntityEnergeticWool;
import org.cyclops.energeticsheep.entity.EntityEnergeticSheepCommon;
import org.cyclops.energeticsheep.item.ItemEnergeticShearsCommon;


/**
 * Referenced registry entries.
 * @author rubensworks
 */
public class RegistryEntries {

    public static final DeferredHolderCommon<Item, ItemEnergeticShearsCommon> ITEM_ENERGETIC_SHEARS = DeferredHolderCommon.create(Registries.ITEM, Identifier.parse("energeticsheep:energetic_shears"));
    public static final DeferredHolderCommon<Item, Item> ITEM_ENERGETIC_WOOL_WHITE = DeferredHolderCommon.create(Registries.ITEM, Identifier.parse("energeticsheep:white_energetic_wool"));
    public static final DeferredHolderCommon<Item, Item> ITEM_ENERGETIC_WOOL_ORANGE = DeferredHolderCommon.create(Registries.ITEM, Identifier.parse("energeticsheep:orange_energetic_wool"));
    public static final DeferredHolderCommon<Item, Item> ITEM_ENERGETIC_WOOL_MAGENTA = DeferredHolderCommon.create(Registries.ITEM, Identifier.parse("energeticsheep:magenta_energetic_wool"));
    public static final DeferredHolderCommon<Item, Item> ITEM_ENERGETIC_WOOL_LIGHT_BLUE = DeferredHolderCommon.create(Registries.ITEM, Identifier.parse("energeticsheep:light_blue_energetic_wool"));
    public static final DeferredHolderCommon<Item, Item> ITEM_ENERGETIC_WOOL_YELLOW = DeferredHolderCommon.create(Registries.ITEM, Identifier.parse("energeticsheep:yellow_energetic_wool"));
    public static final DeferredHolderCommon<Item, Item> ITEM_ENERGETIC_WOOL_LIME = DeferredHolderCommon.create(Registries.ITEM, Identifier.parse("energeticsheep:lime_energetic_wool"));
    public static final DeferredHolderCommon<Item, Item> ITEM_ENERGETIC_WOOL_PINK = DeferredHolderCommon.create(Registries.ITEM, Identifier.parse("energeticsheep:pink_energetic_wool"));
    public static final DeferredHolderCommon<Item, Item> ITEM_ENERGETIC_WOOL_GRAY = DeferredHolderCommon.create(Registries.ITEM, Identifier.parse("energeticsheep:gray_energetic_wool"));
    public static final DeferredHolderCommon<Item, Item> ITEM_ENERGETIC_WOOL_LIGHT_GRAY = DeferredHolderCommon.create(Registries.ITEM, Identifier.parse("energeticsheep:light_gray_energetic_wool"));
    public static final DeferredHolderCommon<Item, Item> ITEM_ENERGETIC_WOOL_CYAN = DeferredHolderCommon.create(Registries.ITEM, Identifier.parse("energeticsheep:cyan_energetic_wool"));
    public static final DeferredHolderCommon<Item, Item> ITEM_ENERGETIC_WOOL_PURPLE = DeferredHolderCommon.create(Registries.ITEM, Identifier.parse("energeticsheep:purple_energetic_wool"));
    public static final DeferredHolderCommon<Item, Item> ITEM_ENERGETIC_WOOL_BLUE = DeferredHolderCommon.create(Registries.ITEM, Identifier.parse("energeticsheep:blue_energetic_wool"));
    public static final DeferredHolderCommon<Item, Item> ITEM_ENERGETIC_WOOL_BROWN = DeferredHolderCommon.create(Registries.ITEM, Identifier.parse("energeticsheep:brown_energetic_wool"));
    public static final DeferredHolderCommon<Item, Item> ITEM_ENERGETIC_WOOL_GREEN = DeferredHolderCommon.create(Registries.ITEM, Identifier.parse("energeticsheep:green_energetic_wool"));
    public static final DeferredHolderCommon<Item, Item> ITEM_ENERGETIC_WOOL_RED = DeferredHolderCommon.create(Registries.ITEM, Identifier.parse("energeticsheep:red_energetic_wool"));
    public static final DeferredHolderCommon<Item, Item> ITEM_ENERGETIC_WOOL_BLACK = DeferredHolderCommon.create(Registries.ITEM, Identifier.parse("energeticsheep:black_energetic_wool"));

    public static final DeferredHolderCommon<EntityType<?>, EntityType<EntityEnergeticSheepCommon>> ENTITY_TYPE_ENERGETIC_SHEEP = DeferredHolderCommon.create(Registries.ENTITY_TYPE, Identifier.parse("energeticsheep:energetic_sheep"));

    public static final DeferredHolderCommon<BlockEntityType<?>, BlockEntityType<BlockEntityEnergeticWool>> BLOCK_ENTITY_TYPE_ENERGETIC_WOOL = DeferredHolderCommon.create(Registries.BLOCK_ENTITY_TYPE, Identifier.parse("energeticsheep:energetic_wool"));

}
