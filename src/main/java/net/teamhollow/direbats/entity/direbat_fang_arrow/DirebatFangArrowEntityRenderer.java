package net.teamhollow.direbats.entity.direbat_fang_arrow;

import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.teamhollow.direbats.init.DBEntities;

public class DirebatFangArrowEntityRenderer extends ArrowRenderer<DirebatFangArrowEntity> {
    public DirebatFangArrowEntityRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn);
    }

    @Override
    public ResourceLocation getEntityTexture(DirebatFangArrowEntity entity) {
        return DBEntities.texture("arrow/" + DirebatFangArrowEntity.id);
    }
}
