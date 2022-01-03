package net.teamhollow.direbats.client.model;

import com.google.common.collect.ImmutableMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;
import net.teamhollow.direbats.Direbats;
import net.teamhollow.direbats.client.model.entity.DirebatEntityModel;
import net.teamhollow.direbats.mixin.client.EntityModelLayersInvoker;

@Environment(EnvType.CLIENT)
public class DirebatsEntityModelLayers {
    public static final EntityModelLayer DIREBAT = registerMain("direbat");

    static {
        new ImmutableMap.Builder<EntityModelLayer, EntityModelLayerRegistry.TexturedModelDataProvider>()
            .put(DirebatsEntityModelLayers.DIREBAT, DirebatEntityModel::getTexturedModelData)
            .build().forEach(EntityModelLayerRegistry::registerModelLayer);
    }

    private static EntityModelLayer registerMain(String id) {
        return EntityModelLayersInvoker.invoke_register(new Identifier(Direbats.MOD_ID, id).toString(), "main");
    }
}
