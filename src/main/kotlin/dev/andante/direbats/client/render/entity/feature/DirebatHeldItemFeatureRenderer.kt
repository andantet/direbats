package dev.andante.direbats.client.render.entity.feature

import dev.andante.direbats.client.render.entity.model.DirebatEntityModel
import dev.andante.direbats.entity.DirebatEntity
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.OverlayTexture
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.feature.FeatureRenderer
import net.minecraft.client.render.entity.feature.FeatureRendererContext
import net.minecraft.client.render.model.json.ModelTransformation
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.EquipmentSlot
import net.minecraft.util.math.Vec3f

/**
 * Represents the renderer for a Direbat's held item.
 */
@Environment(EnvType.CLIENT)
class DirebatHeldItemFeatureRenderer(context: FeatureRendererContext<DirebatEntity, DirebatEntityModel>) : FeatureRenderer<DirebatEntity, DirebatEntityModel>(context) {
    override fun render(
        matrices: MatrixStack,
        vertices: VertexConsumerProvider,
        light: Int,
        entity: DirebatEntity,
        limbAngle: Float,
        limbDistance: Float,
        tickDelta: Float,
        animationProgress: Float,
        headYaw: Float,
        headPitch: Float
    ) {

        MinecraftClient.getInstance()?.run {
            matrices.push()

            contextModel.head.run {
                matrices.translate(pivotX / 16.0, pivotY / 16.0, pivotZ / 16.0)
            }

            matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(headYaw))
            matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(headPitch))
            matrices.translate(0.0, 0.0, -0.5)

            matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(90.0f))

            val stack = entity.getEquippedStack(EquipmentSlot.MAINHAND)
            itemRenderer.renderItem(entity, stack, ModelTransformation.Mode.GROUND, false, matrices, vertices, world, light, OverlayTexture.DEFAULT_UV, entity.id)

            matrices.pop()
        }
    }
}
