package org.cyclops.energeticsheep.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import org.cyclops.cyclopscore.helper.IL10NHelpers;
import org.cyclops.cyclopscore.helper.IModHelpers;
import org.cyclops.energeticsheep.block.BlockEnergeticWool;

import java.util.function.Consumer;

/**
 * @author rubensworks
 */
public abstract class ItemBlockEnergeticWoolCommon extends BlockItem {

    public ItemBlockEnergeticWoolCommon(BlockEnergeticWool block, Item.Properties builder) {
        super(block, builder);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> tooltipAdder, TooltipFlag flagIn) {
        super.appendHoverText(itemStack, context, display, tooltipAdder, flagIn);
        IL10NHelpers l10nHelpers = IModHelpers.get().getL10NHelpers();
        String line = String.format("%,d", getEnergyStored(itemStack)) + " "
                + l10nHelpers.localize(getEnergyUnitUnlocalized());
        tooltipAdder.accept(Component.literal(line).withStyle(ChatFormatting.RED));
        l10nHelpers.addOptionalInfo(tooltipAdder, "block.energeticsheep.energetic_wool");
    }

    protected String getEnergyUnitUnlocalized() {
        return "general.energeticsheep.energy_unit";
    }

    protected abstract int getEnergyStored(ItemStack itemStack);

}
