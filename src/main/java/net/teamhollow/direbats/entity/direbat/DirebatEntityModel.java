package net.teamhollow.direbats.entity.direbat;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;

public class DirebatEntityModel extends EntityModel<DirebatEntity> {
    public final ModelPart werebat;
    private final ModelPart head;
    private final ModelPart bone;
    private final ModelPart earbend;
    private final ModelPart body;
    private final ModelPart leftWing;
    private final ModelPart bone2;
    private final ModelPart rightWing;
    private final ModelPart bone3;
    private final ModelPart legs;
    private final ModelPart talons;
    private final ModelPart tailbone;

    public DirebatEntityModel() {
        textureWidth = 64;
        textureHeight = 64;

        werebat = new ModelPart(this);
        werebat.setPivot(0.0F, 7.0F, -4.0F);
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

        body = new ModelPart(this);
        body.setPivot(0.0F, 0.0F, 1.0F);
        werebat.addChild(body);
        setRotationAngle(body, 0.4363F, 0.0F, 0.0F);
        body.setTextureOffset(0, 0).addCuboid(-4.0F, 0.0F, -3.0F, 8.0F, 13.0F, 6.0F, 0.0F, false);

        leftWing = new ModelPart(this);
        leftWing.setPivot(4.0F, 1.0F, 2.0F);
        body.addChild(leftWing);
        setRotationAngle(leftWing, 0.0F, -0.5236F, 0.3491F);
        leftWing.setTextureOffset(28, 0).addCuboid(0.0F, 0.0F, 0.0F, 11.0F, 13.0F, 0.0F, 0.0F, false);

        bone2 = new ModelPart(this);
        bone2.setPivot(11.0F, 1.0F, 0.0F);
        leftWing.addChild(bone2);
        setRotationAngle(bone2, 0.0F, 1.1345F, 0.0F);
        bone2.setTextureOffset(0, 31).addCuboid(0.0F, -1.0F, 0.0F, 11.0F, 10.0F, 0.0F, 0.0F, false);

        rightWing = new ModelPart(this);
        rightWing.setPivot(-4.0F, 1.0F, 2.0F);
        body.addChild(rightWing);
        setRotationAngle(rightWing, 0.0F, 0.4363F, -0.2618F);
        rightWing.setTextureOffset(28, 28).addCuboid(-11.0F, 0.0F, 0.0F, 11.0F, 13.0F, 0.0F, 0.0F, false);

        bone3 = new ModelPart(this);
        bone3.setPivot(-11.0F, 1.0F, 0.0F);
        rightWing.addChild(bone3);
        setRotationAngle(bone3, 0.0F, -0.7854F, 0.0F);
        bone3.setTextureOffset(28, 13).addCuboid(-11.0F, -1.0F, 0.0F, 11.0F, 10.0F, 0.0F, 0.0F, false);

        legs = new ModelPart(this);
        legs.setPivot(0.0F, 13.0F, 1.0F);
        body.addChild(legs);
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
        body.addChild(tailbone);
        setRotationAngle(tailbone, 0.6109F, 0.0F, 0.0F);
        tailbone.setTextureOffset(46, 46).addCuboid(-1.0F, 0.0F, -2.0F, 2.0F, 10.0F, 2.0F, 0.0F, false);
    }

    @Override
    public void setAngles(DirebatEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.head.pivotX = headPitch * 0.017453292F;
        this.head.pivotY = netHeadYaw * 0.017453292F;
        this.head.pivotZ = 0.0F;
        this.head.setPivot(0.0F, 0.0F, 0.0F);
        this.body.pivotX = 0.7853982F + MathHelper.cos(ageInTicks * 0.1F) * 0.15F;
        this.body.pivotY = 0.0F;
        this.rightWing.pivotY = MathHelper.cos(ageInTicks * 1.3F) * (float) Math.PI * 0.25F;
        this.leftWing.pivotY = -this.rightWing.pivotY;
        this.bone3.pivotY = this.rightWing.pivotY * 0.5F;
        this.bone2.pivotY = -this.rightWing.pivotY * 0.5F;

        if (entity.isHanging()) {
            this.head.pivotX = headPitch * ((float) Math.PI / 180F);
            this.body.pivotX = 0.0F;
            this.rightWing.pivotX = -0.15707964F;
            this.rightWing.pivotY = -1.2566371F;
            this.leftWing.pivotX = this.rightWing.pivotX;
            this.leftWing.pivotY = -this.rightWing.pivotY;
            this.bone3.pivotY = this.rightWing.pivotY;
            this.bone2.pivotY = -this.rightWing.pivotY;
        }
    }

    @Override
    public void render(MatrixStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        werebat.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    public void setRotationAngle(ModelPart bone, float x, float y, float z) {
        bone.pitch = x;
        bone.yaw = y;
        bone.roll = z;
    }
}
