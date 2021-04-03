package net.teamhollow.direbats.entity.direbat;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.teamhollow.direbats.init.DBEntities;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class DirebatEntityRenderer extends MobEntityRenderer<DirebatEntity, DirebatEntityModel> {
    @SuppressWarnings("unused")
    public DirebatEntityRenderer(EntityRenderDispatcher entityRenderDispatcher, @Nullable EntityRendererRegistry.Context ctx) {
        super(entityRenderDispatcher, new DirebatEntityModel(), 0.5f);
        this.addFeature(new DirebatHeldItemFeatureRenderer(this));
    }

    @Override
    public Identifier getTexture(DirebatEntity entity) {
        return DBEntities.texture(DirebatEntity.id + "/" + DirebatEntity.id + (entity.isAttacking() ? "_angry" : ""));
    }

    @Override
    protected void scale(DirebatEntity entity, MatrixStack matrices, float amount) {
        matrices.translate(0.0D, 1.9D, 0.0D);
        super.scale(entity, matrices, amount);
    }

    @Override
    protected void setupTransforms(DirebatEntity entity, MatrixStack matrices, float animationProgress, float bodyYaw, float tickDelta) {
        if (entity.isHanging()) {
            matrices.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(180F));
            matrices.translate(0.0D, -0.6F, 0.0D);
        } else {
            matrices.translate(0.0D, MathHelper.cos(animationProgress * 0.3F) * 0.1F, 0.0D);
        }

        super.setupTransforms(entity, matrices, animationProgress, bodyYaw, tickDelta);
    }
}
