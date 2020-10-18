package net.teamhollow.direbats.entity.direbat;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;

public class DirebatEntityModel extends EntityModel<DirebatEntity> {
    public final ModelRenderer direbat;
    public final ModelRenderer head;
    public final ModelRenderer bone;
    public final ModelRenderer earbend;
    public final ModelRenderer body;
    public final ModelRenderer leftWing;
    public final ModelRenderer bone2;
    public final ModelRenderer rightWing;
    public final ModelRenderer bone3;
    public final ModelRenderer legs;
    public final ModelRenderer talons;
    public final ModelRenderer tail;

    public DirebatEntityModel() {
        textureWidth = 64;
        textureHeight = 64;

        direbat = new ModelRenderer(this);
        direbat.setRotationPoint(0.0F, 7.0F, 0.0F);
        setRotationAngle(direbat, 0.4363F, 0.0F, 0.0F);


        head = new ModelRenderer(this);
        head.setRotationPoint(0.0F, 0.0F, 0.0F);
        direbat.addChild(head);
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

        body = new ModelRenderer(this);
        body.setRotationPoint(0.0F, 0.0F, 1.0F);
        direbat.addChild(body);
        setRotationAngle(body, 0.4363F, 0.0F, 0.0F);
        body.setTextureOffset(0, 0).addBox(-4.0F, 0.0F, -3.0F, 8.0F, 13.0F, 6.0F, 0.0F, false);

        leftWing = new ModelRenderer(this);
        leftWing.setRotationPoint(4.0F, 1.0F, 2.0F);
        body.addChild(leftWing);
        setRotationAngle(leftWing, 0.0F, -0.5236F, 0.3491F);
        leftWing.setTextureOffset(28, 0).addBox(0.0F, 0.0F, 0.0F, 11.0F, 13.0F, 0.0F, 0.0F, false);

        bone2 = new ModelRenderer(this);
        bone2.setRotationPoint(11.0F, 1.0F, 0.0F);
        leftWing.addChild(bone2);
        setRotationAngle(bone2, 0.0F, 1.1345F, 0.0F);
        bone2.setTextureOffset(0, 31).addBox(0.0F, -1.0F, 0.0F, 11.0F, 10.0F, 0.0F, 0.0F, false);

        rightWing = new ModelRenderer(this);
        rightWing.setRotationPoint(-4.0F, 1.0F, 2.0F);
        body.addChild(rightWing);
        setRotationAngle(rightWing, 0.0F, 0.4363F, -0.2618F);
        rightWing.setTextureOffset(28, 28).addBox(-11.0F, 0.0F, 0.0F, 11.0F, 13.0F, 0.0F, 0.0F, false);

        bone3 = new ModelRenderer(this);
        bone3.setRotationPoint(-11.0F, 1.0F, 0.0F);
        rightWing.addChild(bone3);
        setRotationAngle(bone3, 0.0F, -0.7854F, 0.0F);
        bone3.setTextureOffset(28, 13).addBox(-11.0F, -1.0F, 0.0F, 11.0F, 10.0F, 0.0F, 0.0F, false);

        legs = new ModelRenderer(this);
        legs.setRotationPoint(0.0F, 13.0F, 1.0F);
        body.addChild(legs);
        setRotationAngle(legs, -0.9599F, 0.0F, 0.0F);
        legs.setTextureOffset(28, 23).addBox(-4.0F, 0.0F, 0.0F, 8.0F, 4.0F, 0.0F, 0.0F, false);

        talons = new ModelRenderer(this);
        talons.setRotationPoint(0.0F, 4.0F, 0.0F);
        legs.addChild(talons);
        setRotationAngle(talons, -0.4363F, 0.0F, 0.0F);
        talons.setTextureOffset(16, 41).addBox(-5.0F, 0.0F, -1.0F, 4.0F, 4.0F, 4.0F, 0.0F, false);
        talons.setTextureOffset(0, 41).addBox(1.0F, 0.0F, -1.0F, 4.0F, 4.0F, 4.0F, 0.0F, false);

        tail = new ModelRenderer(this);
        tail.setRotationPoint(0.0F, 13.0F, 3.0F);
        body.addChild(tail);
        setRotationAngle(tail, 0.6109F, 0.0F, 0.0F);
        tail.setTextureOffset(46, 46).addBox(-1.0F, 0.0F, -2.0F, 2.0F, 10.0F, 2.0F, 0.0F, false);
    }

    @Override
    public void setRotationAngles(DirebatEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.head.rotateAngleX = headPitch * 0.017453292F;
        this.head.rotateAngleY = netHeadYaw * 0.017453292F;
        this.head.rotateAngleZ = 0.0F;
        this.head.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.body.rotateAngleX = 0.7853982F + MathHelper.cos(ageInTicks * 0.1F) * 0.15F;
        this.body.rotateAngleY = 0.0F;
        this.rightWing.rotateAngleY = MathHelper.cos(ageInTicks * 1.3F) * (float) Math.PI * 0.25F;
        this.leftWing.rotateAngleY = -this.rightWing.rotateAngleY;
        this.bone3.rotateAngleY = this.rightWing.rotateAngleY * 0.5F;
        this.bone2.rotateAngleY = -this.rightWing.rotateAngleY * 0.5F;

        if (entityIn.isHanging()) {
            this.head.rotateAngleX = headPitch * ((float) Math.PI / 180F);
            this.body.rotateAngleX = 0.0F;
            this.rightWing.rotateAngleX = -0.15707964F;
            this.rightWing.rotateAngleY = -1.2566371F;
            this.leftWing.rotateAngleX = this.rightWing.rotateAngleX;
            this.leftWing.rotateAngleY = -this.rightWing.rotateAngleY;
            this.bone3.rotateAngleY = this.rightWing.rotateAngleY;
            this.bone2.rotateAngleY = -this.rightWing.rotateAngleY;
        }
    }

    @Override
    public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        direbat.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
    }

    public void setRotationAngle(ModelRenderer bone, float x, float y, float z) {
        bone.rotateAngleX = x;
        bone.rotateAngleY = y;
        bone.rotateAngleZ = z;
    }
}
