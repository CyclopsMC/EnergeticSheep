package org.cyclops.energeticsheep.block;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.energeticsheep.EnergeticSheep;
import org.cyclops.energeticsheep.item.ItemBlockEnergeticWool;

/**
 * Config for {@link BlockEnergeticWool}.
 * @author rubensworks
 */
public class BlockEnergeticWoolConfig extends BlockConfig {

    /**
     * The unique instance.
     */
    public static BlockEnergeticWoolConfig _instance;

    /**
     * Make a new instance.
     */
    public BlockEnergeticWoolConfig() {
        super(
            EnergeticSheep._instance,
            true,
            "energetic_wool",
            null,
            BlockEnergeticWool.class
        );
    }

    @Override
    public Class<? extends ItemBlock> getItemBlockClass() {
        return ItemBlockEnergeticWool.class;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onInit(Step step) {
        super.onInit(step);
        if(step == Step.INIT) {
            for (int meta = 0; meta < 16; meta++) {
                Item item = Item.getItemFromBlock(getBlockInstance());
                String modId = getMod().getModId();
                String itemName = getModelName(new ItemStack(item, 1, meta));
                ModelResourceLocation modelResourceLocation = new ModelResourceLocation(
                        modId + ":" + itemName, "inventory");
                Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, meta, modelResourceLocation);

            }
        }
    }

    @Override
    public String getModelName(ItemStack itemStack) {
        return super.getModelName(itemStack) + "_"
                + EnumDyeColor.byMetadata(itemStack.getItemDamage()).getDyeColorName();
    }
}
