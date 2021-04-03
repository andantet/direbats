package net.teamhollow.direbats.entity.direbat;

import com.google.common.collect.ImmutableList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.CompositeEntityModel;
import net.minecraft.util.math.MathHelper;

@SuppressWarnings("FieldCanBeLocal")
@Environment(EnvType.CLIENT)
public class DirebatEntityModel extends CompositeEntityModel<DirebatEntity> {
    private final ModelPart head;
    private final ModelPart body;
    private final ModelPart leftWing;
    private final ModelPart leftWingOuter;
    private final ModelPart rightWing;
    private final ModelPart rightWingOuter;
    private final ModelPart legs;
    public final ModelPart talons;
    private final ModelPart tailbone;
    public final ModelPart fangs;

    public DirebatEntityModel() {
        textureWidth = 64;
        textureHeight = 48;

        head = new ModelPart(this);
        head.setPivot(0.0F, -17.0F, -3.0F);
        setRotationAngle(head, 0.3491F, 0.0F, 0.0F);
        head.setTextureOffset(16, 41).addCuboid(-5.0F, -11.0F, -4.0F, 4.0F, 6.0F, 1.0F, 0.0F, false);
        head.setTextureOffset(16, 41).addCuboid(1.0F, -11.0F, -4.0F, 4.0F, 6.0F, 1.0F, 0.0F, true);
        head.setTextureOffset(0, 19).addCuboid(-4.0F, -6.0F, -5.0F, 8.0F, 6.0F, 6.0F, 0.0F, false);
        head.setTextureOffset(16, 34).addCuboid(-2.0F, -3.0F, -9.0F, 4.0F, 3.0F, 4.0F, 0.0F, false);

        body = new ModelPart(this);
        body.setPivot(0.0F, -17.4226F, -2.0937F);
        setRotationAngle(body, 0.8727F, 0.0F, 0.0F);
        body.setTextureOffset(0, 0).addCuboid(-4.0F, 0.0F, -3.0F, 8.0F, 13.0F, 6.0F, 0.0F, false);

        leftWing = new ModelPart(this);
        leftWing.setPivot(-4.0F, 1.0F, 2.0F);
        body.addChild(leftWing);
        setRotationAngle(leftWing, 0.0F, 0.5236F, 0.0F);
        leftWing.setTextureOffset(32, 0).addCuboid(-11.0F, 0.0F, 0.0F, 11.0F, 13.0F, 0.0F, 0.0F, true);

        leftWingOuter = new ModelPart(this);
        leftWingOuter.setPivot(-11.0F, 1.0F, 0.0F);
        leftWing.addChild(leftWingOuter);
        setRotationAngle(leftWingOuter, 0.0F, -1.0908F, 0.0F);
        leftWingOuter.setTextureOffset(32, 12).addCuboid(-11.0F, -1.0F, 0.0F, 11.0F, 12.0F, 0.0F, 0.0F, true);

        rightWing = new ModelPart(this);
        rightWing.setPivot(4.0F, 1.0F, 2.0F);
        body.addChild(rightWing);
        setRotationAngle(rightWing, 0.0F, -0.5236F, 0.0F);
        rightWing.setTextureOffset(32, 0).addCuboid(0.0F, 0.0F, 0.0F, 11.0F, 13.0F, 0.0F, 0.0F, false);

        rightWingOuter = new ModelPart(this);
        rightWingOuter.setPivot(11.0F, 1.0F, 0.0F);
        rightWing.addChild(rightWingOuter);
        setRotationAngle(rightWingOuter, 0.0F, 1.0908F, 0.0F);
        rightWingOuter.setTextureOffset(32, 12).addCuboid(0.0F, -1.0F, 0.0F, 11.0F, 13.0F, 0.0F, 0.0F, false);

        legs = new ModelPart(this);
        legs.setPivot(0.0F, 13.0F, 1.0F);
        body.addChild(legs);
        setRotationAngle(legs, -0.9599F, 0.0F, 0.0F);
        legs.setTextureOffset(0, 31).addCuboid(-4.0F, 0.0F, 0.0F, 8.0F, 4.0F, 0.0F, 0.0F, false);

        talons = new ModelPart(this);
        talons.setPivot(0.0F, 4.0F, 0.0F);
        legs.addChild(talons);
        setRotationAngle(talons, -0.4363F, 0.0F, 0.0F);
        talons.setTextureOffset(0, 35).addCuboid(-4.0F, 0.0F, -1.0F, 3.0F, 4.0F, 4.0F, 0.0F, false);
        talons.setTextureOffset(0, 35).addCuboid(1.0F, 0.0F, -1.0F, 3.0F, 4.0F, 4.0F, 0.0F, true);

        tailbone = new ModelPart(this);
        tailbone.setPivot(0.0F, 11.5912F, 3.1233F);
        body.addChild(tailbone);
        setRotationAngle(tailbone, 0.6109F, 0.0F, 0.0F);
        tailbone.setTextureOffset(32, 36).addCuboid(-1.0F, -0.0912F, -2.1233F, 2.0F, 10.0F, 2.0F, 0.0F, false);

        fangs = new ModelPart(this);
        fangs.setPivot(0.0F, 17.0F, 3.0F);
        head.addChild(fangs);
        fangs.setTextureOffset(12, 35).addCuboid(-2.0F, -18.0F, -12.0F, 4.0F, 2.0F, 0.0F, 0.0F, false);
    }

    @Override
    public void setAngles(DirebatEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        this.head.pitch = headPitch * 0.017453292F;
        this.head.yaw = headYaw * 0.017453292F;
        this.head.roll = 0.0F;
        this.body.pitch = 0.7853982F + MathHelper.cos(animationProgress * 0.1F) * 0.15F;
        this.body.yaw = 0.0F;
        this.rightWing.yaw = MathHelper.cos(animationProgress * 0.4F) * (float) Math.PI * 0.4F;
        this.leftWing.yaw = -this.rightWing.yaw;
        this.rightWingOuter.yaw = this.rightWing.yaw * 0.5F;
        this.leftWingOuter.yaw = -this.rightWing.yaw * 0.5F;

        if (entity.isHanging()) {
            this.head.pitch = headPitch * ((float) Math.PI / 180F);
            this.body.pitch = 0.0F;
            this.rightWing.pitch = 0.0F;
            this.rightWing.yaw = 1.5F;
            this.leftWing.pitch = this.rightWing.pitch;
            this.leftWing.yaw = -this.rightWing.yaw;
            this.rightWingOuter.yaw = this.rightWing.yaw + 0.3F;
            this.leftWingOuter.yaw = -this.rightWing.yaw;
        }
    }

    public void setRotationAngle(ModelPart bone, float pitch, float yaw, float roll) {
        bone.pitch = pitch;
        bone.yaw = yaw;
        bone.roll = roll;
    }

    @Override
    public Iterable<ModelPart> getParts() {
        return ImmutableList.of(head, body);
    }
}
