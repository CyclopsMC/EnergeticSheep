package org.cyclops.energeticsheep.entity;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import org.cyclops.cyclopscore.config.extendedconfig.EntityClientConfig;
import org.cyclops.cyclopscore.config.extendedconfig.EntityConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.energeticsheep.client.render.entity.RenderEntityEnergeticSheep;
import org.cyclops.energeticsheep.client.render.entity.state.EntityRenderStateEnergeticSheep;

/**
 * @author rubensworks
 */
public class EntityEnergeticSheepClientConfigCommon<M extends IModBase, T extends EntityEnergeticSheepCommon> extends EntityClientConfig<M, T> {
    public EntityEnergeticSheepClientConfigCommon(EntityConfigCommon<M, T> entityConfig) {
        super(entityConfig);
    }

    @Override
    public EntityRenderer<? super EntityEnergeticSheepCommon, EntityRenderStateEnergeticSheep> getRender(EntityRendererProvider.Context renderContext, ItemRenderer renderItem) {
        return new RenderEntityEnergeticSheep(renderContext, getEntityConfig());
    }
}
