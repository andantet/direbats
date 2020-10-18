package net.teamhollow.direbats.entity.direbat;

import com.google.common.collect.ImmutableList;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.CompositeEntityModel;
import net.minecraft.util.math.MathHelper;

public class DirebatEntityModel extends CompositeEntityModel<DirebatEntity> {
    public final ModelPart direbat;
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
    private final ModelPart tail;

    public DirebatEntityModel() {
        textureWidth = 64;
        textureHeight = 64;

        direbat = new ModelPart(this);
        direbat.setPivot(0.0F, 7.0F, -4.0F);
        setRotationAngle(direbat, 0.4363F, 0.0F, 0.0F);

        head = new ModelPart(this);
        head.setPivot(0.0F, 0.0F, 0.0F);
        direbat.addChild(head);
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
        direbat.addChild(body);
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

        tail = new ModelPart(this);
        tail.setPivot(0.0F, 13.0F, 3.0F);
        body.addChild(tail);
        setRotationAngle(tail, 0.6109F, 0.0F, 0.0F);
        tail.setTextureOffset(46, 46).addCuboid(-1.0F, 0.0F, -2.0F, 2.0F, 10.0F, 2.0F, 0.0F, false);
    }

    @Override
    public void setAngles(DirebatEntity entity, float limbAngle, float limbAngleAmount, float animationProgress, float headYaw, float headPitch) {
        this.head.pitch = headPitch * 0.017453292F;
        this.head.yaw = headYaw * 0.017453292F;
        this.head.roll = 0.0F;
        this.head.setPivot(0.0F, 0.0F, 0.0F);
        this.body.pitch = 0.7853982F + MathHelper.cos(animationProgress * 0.1F) * 0.15F;
        this.body.yaw = 0.0F;
        this.rightWing.yaw = MathHelper.cos(animationProgress * 1.3F) * (float) Math.PI * 0.25F;
        this.leftWing.yaw = -this.rightWing.yaw;
        this.bone3.yaw = this.rightWing.yaw * 0.5F;
        this.bone2.yaw = -this.rightWing.yaw * 0.5F;

        if (entity.isHanging()) {
            this.head.pitch = headPitch * ((float) Math.PI / 180F);
            this.body.pitch = 0.0F;
            this.rightWing.pitch = -0.15707964F;
            this.rightWing.yaw = -1.2566371F;
            this.leftWing.pitch = this.rightWing.pitch;
            this.leftWing.yaw = -this.rightWing.yaw;
            this.bone3.yaw = this.rightWing.yaw;
            this.bone2.yaw = -this.rightWing.yaw;
        }
    }

    public void setRotationAngle(ModelPart bone, float x, float y, float z) {
        bone.pitch = x;
        bone.yaw = y;
        bone.roll = z;
    }

    @Override
    public Iterable<ModelPart> getParts() {
        return ImmutableList.of(direbat);
    }
}
