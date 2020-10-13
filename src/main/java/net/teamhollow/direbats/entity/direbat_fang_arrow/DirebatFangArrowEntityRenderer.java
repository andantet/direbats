package net.teamhollow.direbats.entity.direbat_fang_arrow;

import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class DirebatFangArrowEntityRenderer extends ArrowRenderer<DirebatFangArrowEntity> {
    public static final ResourceLocation RES_ARROW = new ResourceLocation("textures/entity/projectiles/arrow.png");

    public DirebatFangArrowEntityRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn);
    }

    @Override
    public ResourceLocation getEntityTexture(DirebatFangArrowEntity entity) {
        return RES_ARROW;
    }
}
