package dev.andante.direbats.client.render.entity

import dev.andante.direbats.Direbats
import dev.andante.direbats.client.render.entity.feature.DirebatHeldItemFeatureRenderer
import dev.andante.direbats.client.render.entity.model.DirebatEntityModel
import dev.andante.direbats.client.render.entity.model.DirebatsEntityModelLayers
import dev.andante.direbats.entity.DirebatEntity
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.render.entity.MobEntityRenderer
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.RotationAxis

/**
 * Represents the renderer for a [DirebatEntity].
 */
@Environment(EnvType.CLIENT)
class DirebatEntityRenderer(context: EntityRendererFactory.Context) : MobEntityRenderer<DirebatEntity, DirebatEntityModel>(
        context,
        DirebatEntityModel(context.getPart(DirebatsEntityModelLayers.DIREBAT)),
        0.5f
    ) {
    init {
        addFeature(DirebatHeldItemFeatureRenderer(this))
    }

    override fun getTexture(entity: DirebatEntity): Identifier {
        return if (entity.isAttacking) TEXTURE_ANGRY else TEXTURE
    }

    override fun setupTransforms(
        entity: DirebatEntity,
        matrices: MatrixStack,
        animationProgress: Float,
        bodyYaw: Float,
        tickDelta: Float
    ) {
        super.setupTransforms(entity, matrices, animationProgress, bodyYaw, tickDelta)
        if (entity.hanging) {
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180f))
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180f))
            matrices.translate(0.0, -1.0, 0.0)
        } else {
            val bob = MathHelper.cos(animationProgress * 0.25f) * 0.1
            val offset = entity.getDimensions(entity.pose).height / 2.0
            matrices.translate(0.0, bob - offset, 0.0)
        }
    }

    companion object {
        val TEXTURE = Identifier(Direbats.MOD_ID, "textures/entity/direbat/direbat.png")
        val TEXTURE_ANGRY = Identifier(Direbats.MOD_ID, "textures/entity/direbat/direbat_angry.png")
    }
}
