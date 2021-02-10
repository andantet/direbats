package net.teamhollow.direbats.entity.direbat;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;
import net.teamhollow.direbats.init.DBEntities;

public class DirebatEntityRenderer extends MobRenderer<DirebatEntity, DirebatEntityModel> {
    public DirebatEntityRenderer(EntityRendererManager entityRenderDispatcher) {
        super(entityRenderDispatcher, new DirebatEntityModel(), 0.5f);
        this.addLayer(new DirebatEntityHeldItemLayer(this));
    }

    @Override
    public ResourceLocation getEntityTexture(DirebatEntity entity) {
        return DBEntities.texture(DirebatEntity.id + "/" + DirebatEntity.id + (entity.isAggressive() ? "_angry" : ""));
    }

    @Override
    protected void preRenderCallback(DirebatEntity entity, MatrixStack matrixStackIn, float partialTickTime) {
        matrixStackIn.translate(0.0D, 1.9D, 0.0D);

        super.preRenderCallback(entity, matrixStackIn, partialTickTime);
    }

    @Override
    protected void applyRotations(DirebatEntity entity, MatrixStack matrices, float ageInTicks, float rotationYaw, float partialTicks) {
        if (entity.isHanging()) {
            matrices.rotate(Vector3f.XP.rotationDegrees(180F));
            matrices.translate(0.0D, -0.6F, 0.0D);

        } else {
            matrices.translate(0.0D, MathHelper.cos(ageInTicks * 0.3F) * 0.1F, 0.0D);
        }

        super.applyRotations(entity, matrices, ageInTicks, rotationYaw, partialTicks);
    }
}
