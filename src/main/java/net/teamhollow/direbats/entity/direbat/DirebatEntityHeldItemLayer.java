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

    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, DirebatEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        matrixStackIn.push();

        matrixStackIn.translate((double) (((DirebatEntityModel) this.getEntityModel()).direbat.rotateAngleX / 16.0F), (double) ((((DirebatEntityModel) this.getEntityModel()).direbat.rotateAngleY / 16.0F) + 1.2F), (double) ((((DirebatEntityModel) this.getEntityModel()).direbat.rotateAngleZ / 16.0f) + .8F));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(180.0F));

        ItemStack itemStack = entitylivingbaseIn.getItemStackFromSlot(EquipmentSlotType.MAINHAND);
        Minecraft.getInstance().getFirstPersonRenderer().renderItemSide(entitylivingbaseIn, itemStack, ItemCameraTransforms.TransformType.GROUND, false, matrixStackIn, bufferIn, packedLightIn);
        matrixStackIn.pop();
    }
}
