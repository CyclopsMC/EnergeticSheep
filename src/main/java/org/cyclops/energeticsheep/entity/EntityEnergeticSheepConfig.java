package org.cyclops.energeticsheep.entity;

import com.google.common.collect.Lists;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.animal.Sheep;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.extendedconfig.EntityConfig;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.energeticsheep.EnergeticSheep;
import org.cyclops.energeticsheep.client.render.entity.RenderEntityEnergeticSheep;
import org.cyclops.energeticsheep.entity.layers.LayerEnergeticSheepCharge;

import java.util.List;

/**
 * Config for the {@link EntityEnergeticSheep}.
 * @author rubensworks
 *
 */
public class EntityEnergeticSheepConfig extends EntityConfig<EntityEnergeticSheep> {

    @ConfigurableProperty(category = "mob", comment = "How much base energy the sheep can regenerate each time.", configLocation = ModConfig.Type.SERVER)
    public static int sheepBaseCapacity = 20000;

    @ConfigurableProperty(category = "mob", comment = "How much base energy energetic wool wool has.", configLocation = ModConfig.Type.SERVER)
    public static int woolBaseCapacity = 500;

    @ConfigurableProperty(category = "mob", comment = "This factor will be multiplied by the ordinal value of the color, and will be multiplied with the base sheepBaseCapacity of the sheep.", isCommandable = true)
    public static double additionalCapacityColorFactor = 0.075D;

    @ConfigurableProperty(category = "mob", comment = "The 1/X chance on having an energetic baby when breeding.", configLocation = ModConfig.Type.SERVER, isCommandable = true)
    public static int babyChance = 3;

    @ConfigurableProperty(category = "mob", comment = "The 1/X chance on having an energetic baby when breeding with a power-breeding item.", configLocation = ModConfig.Type.SERVER, isCommandable = true)
    public static int babyChancePowerBreeding = 1;

    @ConfigurableProperty(category = "mob",
            comment = "The items that can be used to power-breed sheep, by unique item name.", configLocation = ModConfig.Type.SERVER)
    public static List<String> powerBreedingItems = Lists.newArrayList(
            "minecraft:rabbit_stew",
            "minecraft:chorus_fruit",
            "integrateddynamics:menril_berries"
    );

    public EntityEnergeticSheepConfig() {
        super(
                EnergeticSheep._instance,
                "energetic_sheep",
                eConfig -> EntityType.Builder.of(EntityEnergeticSheep::new, MobCategory.CREATURE)
                        .sized(0.9F, 1.3F)
                        .fireImmune()
        );
        EnergeticSheep._instance.getModEventBus().addListener(this::onEntityAttributesCreation);
        if (MinecraftHelpers.isClientSide()) {
            EnergeticSheep._instance.getModEventBus().addListener(LayerEnergeticSheepCharge::loadLayerDefinitions);
        }
        EnergeticSheep._instance.getModEventBus().addListener(this::registerCapabilities);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public EntityRenderer<? super EntityEnergeticSheep> getRender(EntityRendererProvider.Context renderContext, ItemRenderer itemRenderer) {
        return new RenderEntityEnergeticSheep(renderContext, this);
    }

    public void onEntityAttributesCreation(EntityAttributeCreationEvent event) {
        event.put(getInstance(), Sheep.createAttributes().build());
    }

    public void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerEntity(
                Capabilities.EnergyStorage.ENTITY,
                getInstance(),
                (entity, context) -> entity.getEnergyStorage()
        );
    }
}
