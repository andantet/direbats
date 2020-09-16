package net.teamhollow.direbats.entity.direbat;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class DirebatEntityModel extends EntityModel<DirebatEntity> {
    public final ModelPart werebat;
    public final ModelPart head;
    public final ModelPart bone;
    public final ModelPart earbend;
    public final ModelPart Body;
    public final ModelPart leftwing;
    public final ModelPart bone2;
    public final ModelPart rightwing;
    public final ModelPart bone3;
    public final ModelPart legs;
    public final ModelPart talons;
    public final ModelPart tailbone;

    public DirebatEntityModel() {
        textureWidth = 64;
        textureHeight = 64;

        werebat = new ModelPart(this);
        werebat.setPivot(0.0F, 7.0F, 0.0F);
        setRotationAngle(werebat, 0.4363F, 0.0F, 0.0F);

        head = new ModelPart(this);
        head.setPivot(0.0F, 0.0F, 0.0F);
        werebat.addChild(head);
        setRotationAngle(head, -0.0873F, 0.0F, 0.0F);
        head.setTextureOffset(32, 41).addCuboid(-2.0F, -3.0F, -9.0F, 4.0F, 3.0F, 4.0F, 0.0F, false);
        head.setTextureOffset(0, 19).addCuboid(-4.0F, -6.0F, -5.0F, 8.0F, 6.0F, 6.0F, 0.0F, false);

        bone = new ModelPart(this);
        bone.setPivot(0.0F, -1.0F, -9.0F);
        head.addChild(bone);
        setRotationAngle(bone, -0.0873F, 0.0F, 0.0F);
        bone.setTextureOffset(12, 41).addCuboid(-2.0F, 0.0F, 0.0F, 4.0F, 2.0F, 0.0F, 0.0F, false);

        earbend = new ModelPart(this);
        earbend.setPivot(0.0F, -6.0F, -3.0F);
        head.addChild(earbend);
        setRotationAngle(earbend, 0.2618F, 0.0F, 0.0F);
        earbend.setTextureOffset(31, 48).addCuboid(-5.0F, -5.0F, -1.0F, 4.0F, 6.0F, 1.0F, 0.0F, false);
        earbend.setTextureOffset(0, 49).addCuboid(1.0F, -5.0F, -1.0F, 4.0F, 6.0F, 1.0F, 0.0F, false);

        Body = new ModelPart(this);
        Body.setPivot(0.0F, 0.0F, 1.0F);
        werebat.addChild(Body);
        setRotationAngle(Body, 0.4363F, 0.0F, 0.0F);
        Body.setTextureOffset(0, 0).addCuboid(-4.0F, 0.0F, -3.0F, 8.0F, 13.0F, 6.0F, 0.0F, false);

        leftwing = new ModelPart(this);
        leftwing.setPivot(4.0F, 1.0F, 2.0F);
        Body.addChild(leftwing);
        setRotationAngle(leftwing, 0.0F, -0.5236F, 0.3491F);
        leftwing.setTextureOffset(28, 0).addCuboid(0.0F, 0.0F, 0.0F, 11.0F, 13.0F, 0.0F, 0.0F, false);

        bone2 = new ModelPart(this);
        bone2.setPivot(11.0F, 1.0F, 0.0F);
        leftwing.addChild(bone2);
        setRotationAngle(bone2, 0.0F, 1.1345F, 0.0F);
        bone2.setTextureOffset(0, 31).addCuboid(0.0F, -1.0F, 0.0F, 11.0F, 10.0F, 0.0F, 0.0F, false);

        rightwing = new ModelPart(this);
        rightwing.setPivot(-4.0F, 1.0F, 2.0F);
        Body.addChild(rightwing);
        setRotationAngle(rightwing, 0.0F, 0.4363F, -0.2618F);
        rightwing.setTextureOffset(28, 28).addCuboid(-11.0F, 0.0F, 0.0F, 11.0F, 13.0F, 0.0F, 0.0F, false);

        bone3 = new ModelPart(this);
        bone3.setPivot(-11.0F, 1.0F, 0.0F);
        rightwing.addChild(bone3);
        setRotationAngle(bone3, 0.0F, -0.7854F, 0.0F);
        bone3.setTextureOffset(28, 13).addCuboid(-11.0F, -1.0F, 0.0F, 11.0F, 10.0F, 0.0F, 0.0F, false);

        legs = new ModelPart(this);
        legs.setPivot(0.0F, 13.0F, 1.0F);
        Body.addChild(legs);
        setRotationAngle(legs, -0.9599F, 0.0F, 0.0F);
        legs.setTextureOffset(28, 23).addCuboid(-4.0F, 0.0F, 0.0F, 8.0F, 4.0F, 0.0F, 0.0F, false);

        talons = new ModelPart(this);
        talons.setPivot(0.0F, 4.0F, 0.0F);
        legs.addChild(talons);
        setRotationAngle(talons, -0.4363F, 0.0F, 0.0F);
        talons.setTextureOffset(16, 41).addCuboid(-5.0F, 0.0F, -1.0F, 4.0F, 4.0F, 4.0F, 0.0F, false);
        talons.setTextureOffset(0, 41).addCuboid(1.0F, 0.0F, -1.0F, 4.0F, 4.0F, 4.0F, 0.0F, false);

        tailbone = new ModelPart(this);
        tailbone.setPivot(0.0F, 13.0F, 3.0F);
        Body.addChild(tailbone);
        setRotationAngle(tailbone, 0.6109F, 0.0F, 0.0F);
        tailbone.setTextureOffset(46, 46).addCuboid(-1.0F, 0.0F, -2.0F, 2.0F, 10.0F, 2.0F, 0.0F, false);
    }

    @Override
    public void setAngles(DirebatEntity entity, float limbAngle, float limbAngleAmount, float animationProgress, float headYaw, float headPitch) {
        this.head.pivotX = headPitch * 0.017453292F;
        this.head.pivotY = headYaw * 0.017453292F;
        this.head.pivotZ = 0.0F;
        this.head.setPivot(0.0F, 0.0F, 0.0F);
        this.Body.pivotX = 0.7853982F + MathHelper.cos(animationProgress * 0.1F) * 0.15F;
        this.Body.pivotY = 0.0F;
        this.rightwing.pivotY = MathHelper.cos(animationProgress * 1.3F) * (float) Math.PI * 0.25F;
        this.leftwing.pivotY = -this.rightwing.pivotY;
        this.bone3.pivotY = this.rightwing.pivotY * 0.5F;
        this.bone2.pivotY = -this.rightwing.pivotY * 0.5F;

        if (entity.isHanging()) {
            this.head.pivotX = headPitch * ((float) Math.PI / 180F);
            this.Body.pivotX = 0.0F;
            this.rightwing.pivotX = -0.15707964F;
            this.rightwing.pivotY = -1.2566371F;
            this.leftwing.pivotX = this.rightwing.pivotX;
            this.leftwing.pivotY = -this.rightwing.pivotY;
            this.bone3.pivotY = this.rightwing.pivotY;
            this.bone2.pivotY = -this.rightwing.pivotY;
        }
    }

    @Override
    public void render(MatrixStack matrixStackIn, VertexConsumer vertexConsumerIn, int lightIn, int overlayIn, float red, float green, float blue, float alpha) {
        werebat.render(matrixStackIn, vertexConsumerIn, lightIn, overlayIn);
    }

    public void setRotationAngle(ModelPart bone, float x, float y, float z) {
        bone.pivotX = x;
        bone.pivotY = y;
        bone.pivotZ = z;
    }
}
