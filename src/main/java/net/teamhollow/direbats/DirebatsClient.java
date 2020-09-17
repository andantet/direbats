package net.teamhollow.direbats;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.teamhollow.direbats.entity.direbat.DirebatEntityRenderer;
import net.teamhollow.direbats.entity.direbat_fang_arrow.DirebatFangArrowEntityRenderer;
import net.teamhollow.direbats.init.DBEntities;

public class DirebatsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        this.registerRenderers();
    }

    @Environment(EnvType.CLIENT)
    public void registerRenderers() {
        EntityRendererRegistry INSTANCE = EntityRendererRegistry.INSTANCE;

        INSTANCE.register(
            DBEntities.DIREBAT,
            (entityRenderDispatcher, context) -> new DirebatEntityRenderer(entityRenderDispatcher)
        );
        INSTANCE.register(
            DBEntities.DIREBAT_FANG_ARROW,
            (entityRenderDispatcher, context) -> new DirebatFangArrowEntityRenderer(entityRenderDispatcher)
        );
    }
}
