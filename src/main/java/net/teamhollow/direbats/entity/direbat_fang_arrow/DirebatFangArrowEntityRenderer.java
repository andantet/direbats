package net.teamhollow.direbats.entity.direbat_fang_arrow;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.util.Identifier;
import net.teamhollow.direbats.init.DBEntities;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class DirebatFangArrowEntityRenderer extends ProjectileEntityRenderer<DirebatFangArrowEntity> {
    @SuppressWarnings("unused")
    public DirebatFangArrowEntityRenderer(EntityRenderDispatcher entityRenderDispatcher, @Nullable EntityRendererRegistry.Context ctx) {
        super(entityRenderDispatcher);
    }

    @Override
    public Identifier getTexture(DirebatFangArrowEntity entity) {
        return DBEntities.texture("arrow/" + DirebatFangArrowEntity.id);
    }
}
