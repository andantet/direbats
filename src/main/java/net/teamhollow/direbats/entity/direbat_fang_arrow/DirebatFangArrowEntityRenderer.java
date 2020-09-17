package net.teamhollow.direbats.entity.direbat_fang_arrow;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.util.Identifier;
import net.teamhollow.direbats.init.DBEntities;

@Environment(EnvType.CLIENT)
public class DirebatFangArrowEntityRenderer extends ProjectileEntityRenderer<DirebatFangArrowEntity> {
    public DirebatFangArrowEntityRenderer(EntityRenderDispatcher entityRenderDispatcher) {
        super(entityRenderDispatcher);
    }

    public Identifier getTexture(DirebatFangArrowEntity fangArrowEntity) {
        return DBEntities.texture("arrow/" + DirebatFangArrowEntity.id);
    }
}
