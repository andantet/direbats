package net.teamhollow.direbats.entity.fang_arrow;

import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class FangArrowEntityRenderer extends ArrowRenderer<FangArrowEntity> {
    public static final ResourceLocation RES_ARROW = new ResourceLocation("textures/entity/projectiles/arrow.png");

    public FangArrowEntityRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn);
    }

    @Override
    public ResourceLocation getEntityTexture(FangArrowEntity entity) {
        return RES_ARROW;
    }
}
