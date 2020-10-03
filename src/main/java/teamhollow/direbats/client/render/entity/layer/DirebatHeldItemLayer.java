package teamhollow.direbats.client.render.entity.layer;

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
import teamhollow.direbats.client.render.entity.model.DirebatModel;
import teamhollow.direbats.entity.mob.DirebatEntity;

@OnlyIn(Dist.CLIENT)
public class DirebatHeldItemLayer extends LayerRenderer<DirebatEntity, DirebatModel> {
    public DirebatHeldItemLayer(IEntityRenderer<DirebatEntity, DirebatModel> p_i50938_1_) {
        super(p_i50938_1_);
    }

    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, DirebatEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        matrixStackIn.push();

        matrixStackIn.translate((double) (((DirebatModel) this.getEntityModel()).werebat.rotateAngleX / 16.0F), (double) ((((DirebatModel) this.getEntityModel()).werebat.rotateAngleY / 16.0F) + 1.2F), (double) ((((DirebatModel) this.getEntityModel()).werebat.rotateAngleZ / 16.0f) + .8F));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(180.0F));

        ItemStack itemStack = entitylivingbaseIn.getItemStackFromSlot(EquipmentSlotType.MAINHAND);
        Minecraft.getInstance().getFirstPersonRenderer().renderItemSide(entitylivingbaseIn, itemStack, ItemCameraTransforms.TransformType.GROUND, false, matrixStackIn, bufferIn, packedLightIn);
        matrixStackIn.pop();
    }
}
