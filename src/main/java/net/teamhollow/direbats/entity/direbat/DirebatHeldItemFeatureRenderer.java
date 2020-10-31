package net.teamhollow.direbats.entity.direbat;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;

@Environment(EnvType.CLIENT)
public class DirebatHeldItemFeatureRenderer extends FeatureRenderer<DirebatEntity, DirebatEntityModel> {
    public DirebatHeldItemFeatureRenderer(FeatureRendererContext<DirebatEntity, DirebatEntityModel> featureRendererContext) {
        super(featureRendererContext);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, DirebatEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        matrices.push();

        if (entity.isHanging()) matrices.translate(
            this.getContextModel().talons.pitch / 16.0F,
            (this.getContextModel().talons.yaw / 16.0F),
            (this.getContextModel().talons.roll / 16.0f) - 0.4F
        );
        else matrices.translate(
            this.getContextModel().talons.pitch / 16.0F,
            (this.getContextModel().talons.yaw / 16.0F) - 0.2F,
            (this.getContextModel().talons.roll / 16.0f) + 0.25F
        );

        matrices.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(70.0F));

        ItemStack itemStack = entity.getEquippedStack(EquipmentSlot.MAINHAND);
        MinecraftClient.getInstance().getHeldItemRenderer().renderItem(entity, itemStack, ModelTransformation.Mode.GROUND, false, matrices, vertexConsumers, light);

        matrices.pop();
    }
}
