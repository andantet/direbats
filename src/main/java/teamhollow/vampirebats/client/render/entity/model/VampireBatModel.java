package teamhollow.direbats.client.render.entity.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;
import teamhollow.direbats.entity.mob.DirebatEntity;

public class DirebatModel extends EntityModel<DirebatEntity> {
    public final ModelRenderer werebat;
    public final ModelRenderer head;
    public final ModelRenderer bone;
    public final ModelRenderer earbend;
    public final ModelRenderer Body;
    public final ModelRenderer leftwing;
    public final ModelRenderer bone2;
    public final ModelRenderer rightwing;
    public final ModelRenderer bone3;
    public final ModelRenderer legs;
    public final ModelRenderer talons;
    public final ModelRenderer tailbone;

    public DirebatModel() {
        textureWidth = 64;
        textureHeight = 64;
        werebat = new ModelRenderer(this);
        werebat.setRotationPoint(0.0F, 7.0F, 0.0F);
        setRotationAngle(werebat, 0.4363F, 0.0F, 0.0F);


        head = new ModelRenderer(this);
        head.setRotationPoint(0.0F, 0.0F, 0.0F);
        werebat.addChild(head);
        setRotationAngle(head, -0.0873F, 0.0F, 0.0F);
        head.setTextureOffset(32, 41).addBox(-2.0F, -3.0F, -9.0F, 4.0F, 3.0F, 4.0F, 0.0F, false);
        head.setTextureOffset(0, 19).addBox(-4.0F, -6.0F, -5.0F, 8.0F, 6.0F, 6.0F, 0.0F, false);

        bone = new ModelRenderer(this);
        bone.setRotationPoint(0.0F, -1.0F, -9.0F);
        head.addChild(bone);
        setRotationAngle(bone, -0.0873F, 0.0F, 0.0F);
        bone.setTextureOffset(12, 41).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 2.0F, 0.0F, 0.0F, false);

        earbend = new ModelRenderer(this);
        earbend.setRotationPoint(0.0F, -6.0F, -3.0F);
        head.addChild(earbend);
        setRotationAngle(earbend, 0.2618F, 0.0F, 0.0F);
        earbend.setTextureOffset(31, 48).addBox(-5.0F, -5.0F, -1.0F, 4.0F, 6.0F, 1.0F, 0.0F, false);
        earbend.setTextureOffset(0, 49).addBox(1.0F, -5.0F, -1.0F, 4.0F, 6.0F, 1.0F, 0.0F, false);

        Body = new ModelRenderer(this);
        Body.setRotationPoint(0.0F, 0.0F, 1.0F);
        werebat.addChild(Body);
        setRotationAngle(Body, 0.4363F, 0.0F, 0.0F);
        Body.setTextureOffset(0, 0).addBox(-4.0F, 0.0F, -3.0F, 8.0F, 13.0F, 6.0F, 0.0F, false);

        leftwing = new ModelRenderer(this);
        leftwing.setRotationPoint(4.0F, 1.0F, 2.0F);
        Body.addChild(leftwing);
        setRotationAngle(leftwing, 0.0F, -0.5236F, 0.3491F);
        leftwing.setTextureOffset(28, 0).addBox(0.0F, 0.0F, 0.0F, 11.0F, 13.0F, 0.0F, 0.0F, false);

        bone2 = new ModelRenderer(this);
        bone2.setRotationPoint(11.0F, 1.0F, 0.0F);
        leftwing.addChild(bone2);
        setRotationAngle(bone2, 0.0F, 1.1345F, 0.0F);
        bone2.setTextureOffset(0, 31).addBox(0.0F, -1.0F, 0.0F, 11.0F, 10.0F, 0.0F, 0.0F, false);

        rightwing = new ModelRenderer(this);
        rightwing.setRotationPoint(-4.0F, 1.0F, 2.0F);
        Body.addChild(rightwing);
        setRotationAngle(rightwing, 0.0F, 0.4363F, -0.2618F);
        rightwing.setTextureOffset(28, 28).addBox(-11.0F, 0.0F, 0.0F, 11.0F, 13.0F, 0.0F, 0.0F, false);

        bone3 = new ModelRenderer(this);
        bone3.setRotationPoint(-11.0F, 1.0F, 0.0F);
        rightwing.addChild(bone3);
        setRotationAngle(bone3, 0.0F, -0.7854F, 0.0F);
        bone3.setTextureOffset(28, 13).addBox(-11.0F, -1.0F, 0.0F, 11.0F, 10.0F, 0.0F, 0.0F, false);

        legs = new ModelRenderer(this);
        legs.setRotationPoint(0.0F, 13.0F, 1.0F);
        Body.addChild(legs);
        setRotationAngle(legs, -0.9599F, 0.0F, 0.0F);
        legs.setTextureOffset(28, 23).addBox(-4.0F, 0.0F, 0.0F, 8.0F, 4.0F, 0.0F, 0.0F, false);

        talons = new ModelRenderer(this);
        talons.setRotationPoint(0.0F, 4.0F, 0.0F);
        legs.addChild(talons);
        setRotationAngle(talons, -0.4363F, 0.0F, 0.0F);
        talons.setTextureOffset(16, 41).addBox(-5.0F, 0.0F, -1.0F, 4.0F, 4.0F, 4.0F, 0.0F, false);
        talons.setTextureOffset(0, 41).addBox(1.0F, 0.0F, -1.0F, 4.0F, 4.0F, 4.0F, 0.0F, false);

        tailbone = new ModelRenderer(this);
        tailbone.setRotationPoint(0.0F, 13.0F, 3.0F);
        Body.addChild(tailbone);
        setRotationAngle(tailbone, 0.6109F, 0.0F, 0.0F);
        tailbone.setTextureOffset(46, 46).addBox(-1.0F, 0.0F, -2.0F, 2.0F, 10.0F, 2.0F, 0.0F, false);
    }

    @Override
    public void setRotationAngles(DirebatEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.head.rotateAngleX = headPitch * 0.017453292F;
        this.head.rotateAngleY = netHeadYaw * 0.017453292F;
        this.head.rotateAngleZ = 0.0F;
        this.head.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.Body.rotateAngleX = 0.7853982F + MathHelper.cos(ageInTicks * 0.1F) * 0.15F;
        this.Body.rotateAngleY = 0.0F;
        this.rightwing.rotateAngleY = MathHelper.cos(ageInTicks * 1.3F) * (float) Math.PI * 0.25F;
        this.leftwing.rotateAngleY = -this.rightwing.rotateAngleY;
        this.bone3.rotateAngleY = this.rightwing.rotateAngleY * 0.5F;
        this.bone2.rotateAngleY = -this.rightwing.rotateAngleY * 0.5F;

        if (entityIn.isHanging()) {
            this.head.rotateAngleX = headPitch * ((float) Math.PI / 180F);
            this.Body.rotateAngleX = 0.0F;
            this.rightwing.rotateAngleX = -0.15707964F;
            this.rightwing.rotateAngleY = -1.2566371F;
            this.leftwing.rotateAngleX = this.rightwing.rotateAngleX;
            this.leftwing.rotateAngleY = -this.rightwing.rotateAngleY;
            this.bone3.rotateAngleY = this.rightwing.rotateAngleY;
            this.bone2.rotateAngleY = -this.rightwing.rotateAngleY;
        }
    }

    @Override
    public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        werebat.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
    }

    public void setRotationAngle(ModelRenderer bone, float x, float y, float z) {
        bone.rotateAngleX = x;
        bone.rotateAngleY = y;
        bone.rotateAngleZ = z;
    }
}
