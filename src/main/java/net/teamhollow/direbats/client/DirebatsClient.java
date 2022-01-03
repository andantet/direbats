package net.teamhollow.direbats.client;

import com.google.common.reflect.Reflection;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.teamhollow.direbats.Direbats;
import net.teamhollow.direbats.client.model.DirebatsEntityModelLayers;
import net.teamhollow.direbats.client.render.entity.DirebatEntityRenderer;
import net.teamhollow.direbats.client.render.entity.DirebatFangArrowEntityRenderer;
import net.teamhollow.direbats.entity.DirebatsEntities;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Environment(EnvType.CLIENT)
public class DirebatsClient implements ClientModInitializer {
    public static final Logger LOGGER = LogManager.getLogger("%s-client".formatted(Direbats.MOD_ID));

    @SuppressWarnings("UnstableApiUsage")
    @Override
    public void onInitializeClient() {
        LOGGER.info("Initializing {}-client", Direbats.MOD_NAME);

        Reflection.initialize(DirebatsEntityModelLayers.class);

        EntityRendererRegistry.register(DirebatsEntities.DIREBAT, DirebatEntityRenderer::new);
        EntityRendererRegistry.register(DirebatsEntities.DIREBAT_FANG_ARROW, DirebatFangArrowEntityRenderer::new);

        LOGGER.info("Initialized {}-client", Direbats.MOD_NAME);
    }
}
