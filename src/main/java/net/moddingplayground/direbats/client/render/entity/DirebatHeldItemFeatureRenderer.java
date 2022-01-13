package net.moddingplayground.direbats.client.render.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3f;
import net.moddingplayground.direbats.client.model.entity.DirebatEntityModel;
import net.moddingplayground.direbats.entity.DirebatEntity;

@Environment(EnvType.CLIENT)
public class DirebatHeldItemFeatureRenderer<T extends DirebatEntity> extends FeatureRenderer<T, DirebatEntityModel<T>> {
    public DirebatHeldItemFeatureRenderer(FeatureRendererContext<T, DirebatEntityModel<T>> ctx) {
        super(ctx);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertices, int light, T entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        matrices.push();

        ModelPart head = this.getContextModel().getHead();
        matrices.translate(head.pivotX / 16.0F, head.pivotY / 16.0F, head.pivotZ / 16.0F);
        matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(headYaw));
        matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(headPitch));
        matrices.translate(0.0D, 0.0D, -0.5D);

        matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(90.0F));

        ItemStack stack = entity.getEquippedStack(EquipmentSlot.MAINHAND);
        MinecraftClient client = MinecraftClient.getInstance();
        client.getHeldItemRenderer().renderItem(entity, stack, ModelTransformation.Mode.GROUND, false, matrices, vertices, light);

        matrices.pop();
    }
}
