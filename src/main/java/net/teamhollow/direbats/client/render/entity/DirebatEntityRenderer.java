package net.teamhollow.direbats.client.render.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;
import net.teamhollow.direbats.client.model.DirebatsEntityModelLayers;
import net.teamhollow.direbats.client.model.entity.DirebatEntityModel;
import net.teamhollow.direbats.entity.DirebatEntity;

import static net.teamhollow.direbats.client.util.ClientUtil.entityTexture;

@Environment(EnvType.CLIENT)
public class DirebatEntityRenderer<T extends DirebatEntity> extends MobEntityRenderer<T, DirebatEntityModel<T>> {
    public static final Identifier TEXTURE = entityTexture("direbat/direbat");
    public static final Identifier TEXTURE_ANGRY = entityTexture("direbat/direbat_angry");

    public DirebatEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new DirebatEntityModel<>(ctx.getPart(DirebatsEntityModelLayers.DIREBAT)), 0.5f);
        this.addFeature(new DirebatHeldItemFeatureRenderer<>(this));
    }

    @Override
    public Identifier getTexture(T entity) {
        return entity.isAttacking() ? TEXTURE_ANGRY : TEXTURE;
    }

    @Override
    protected void setupTransforms(T entity, MatrixStack matrices, float animationProgress, float bodyYaw, float tickDelta) {
        super.setupTransforms(entity, matrices, animationProgress, bodyYaw, tickDelta);
        if (entity.isHanging()) {
            matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(180F));
            matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180F));
            matrices.translate(0.0D, -1.0D, 0.0D);
        } else {
            float bob = MathHelper.cos(animationProgress * 0.25F) * 0.1F;
            float offset = entity.getDimensions(entity.getPose()).height / 2;
            matrices.translate(0.0D, bob - offset, 0.0D);
        }
    }
}
