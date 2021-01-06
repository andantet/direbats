package net.teamhollow.direbats.entity.direbat;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class DirebatEntityHeldItemLayer extends LayerRenderer<DirebatEntity, DirebatEntityModel> {
    public DirebatEntityHeldItemLayer(IEntityRenderer<DirebatEntity, DirebatEntityModel> entityRenderDispatcher) {
        super(entityRenderDispatcher);
    }

    @Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, DirebatEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        matrixStackIn.push();

        if (entitylivingbaseIn.isHanging())
            matrixStackIn.translate(
                    this.getEntityModel().fangs.rotateAngleX,
                    this.getEntityModel().fangs.rotateAngleY - 1.1F,
                    this.getEntityModel().fangs.rotateAngleZ - 0.7F
            );
        else {
            matrixStackIn.translate(this.getEntityModel().talons.rotateAngleX / 16.0F, this.getEntityModel().talons.rotateAngleY / 16.0F - 0.2F, this.getEntityModel().talons.rotateAngleZ / 16.0f + 0.25F);
            matrixStackIn.rotate(Vector3f.XP.rotationDegrees(70.0F));
        }
        ItemStack itemStack = entitylivingbaseIn.getItemStackFromSlot(EquipmentSlotType.MAINHAND);
        Minecraft.getInstance().getFirstPersonRenderer().renderItemSide(entitylivingbaseIn, itemStack, ItemCameraTransforms.TransformType.GROUND, false, matrixStackIn, bufferIn, packedLightIn);
        matrixStackIn.pop();
    }
}
