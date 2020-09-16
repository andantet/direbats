package net.teamhollow.direbats.entity.direbat;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.teamhollow.direbats.init.DBEntities;

@Environment(EnvType.CLIENT)
public class DirebatEntityRenderer extends MobEntityRenderer<DirebatEntity, DirebatEntityModel> {
    public DirebatEntityRenderer(EntityRenderDispatcher entityRenderDispatcher) {
        super(entityRenderDispatcher, new DirebatEntityModel(), 0.5f);
        this.addFeature(new DirebatHeldItemLayer(this));
    }

    @Override
    public Identifier getTexture(DirebatEntity entity) {
        return DBEntities.texture(DirebatEntity.id + "/" + DirebatEntity.id);
    }

    @Override
    protected void scale(DirebatEntity entitylivingbaseIn, MatrixStack matrixStackIn, float partialTickTime) {
        matrixStackIn.translate(0.0D, 0.40000000149011612D, 0.0D);
        super.scale(entitylivingbaseIn, matrixStackIn, partialTickTime);
    }

    @Override
    protected void setupTransforms(DirebatEntity entityLiving, MatrixStack matrixStackIn, float ageInTicks, float rotationYaw, float partialTicks) {
        if (entityLiving.isHanging()) {
            matrixStackIn.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(180F));
            matrixStackIn.translate(0.0D, (double) -0.35F, 0.0D);
        } else {
            matrixStackIn.translate(0.0D, (double) (MathHelper.cos(ageInTicks * 0.3F) * 0.1F), 0.0D);
        }

        super.setupTransforms(entityLiving, matrixStackIn, ageInTicks, rotationYaw, partialTicks);
    }
}
