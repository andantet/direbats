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
public class DirebatHeldItemLayer extends FeatureRenderer<DirebatEntity, DirebatEntityModel> {
    public DirebatHeldItemLayer(FeatureRendererContext<DirebatEntity, DirebatEntityModel> featureRendererContext) {
        super(featureRendererContext);
    }

    public void render(MatrixStack stack, VertexConsumerProvider vertexConsumer, int packedLight, DirebatEntity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        stack.push();

        stack.translate(
            (double) (((DirebatEntityModel) this.getContextModel()).werebat.pivotX / 16.0F),
            (double) ((((DirebatEntityModel) this.getContextModel()).werebat.pivotY / 16.0F) + 1.2F),
            (double) ((((DirebatEntityModel) this.getContextModel()).werebat.pivotZ / 16.0f) + .8F)
        );
        stack.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(180.0F));

        ItemStack itemStack = entity.getEquippedStack(EquipmentSlot.MAINHAND);
        MinecraftClient.getInstance().getHeldItemRenderer().renderItem(entity, itemStack, ModelTransformation.Mode.GROUND, false, stack, vertexConsumer, packedLight);
        stack.pop();
    }
}
