package net.moddingplayground.direbats.client.model.entity;

import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.util.math.MathHelper;
import net.moddingplayground.direbats.entity.DirebatEntity;

@SuppressWarnings({ "FieldCanBeLocal", "unused" })
public class DirebatEntityModel<T extends DirebatEntity> extends SinglePartEntityModel<T> {
    private final ModelPart root;

    private final ModelPart body;
    private final ModelPart leftWing;
    private final ModelPart leftWingOuter;
    private final ModelPart rightWing;
    private final ModelPart rightWingOuter;
    private final ModelPart legs;
    private final ModelPart talons;
    private final ModelPart tailbone;
    private final ModelPart head;
    private final ModelPart fangs;

    public DirebatEntityModel(ModelPart root) {
        this.root = root;

        this.body = root.getChild("body");

        this.leftWing = this.body.getChild("left_wing");
        this.leftWingOuter = this.leftWing.getChild("left_wing_outer");

        this.rightWing = this.body.getChild("right_wing");
        this.rightWingOuter = this.rightWing.getChild("right_wing_outer");

        this.legs = this.body.getChild("legs");
        this.talons = this.legs.getChild("talons");

        this.tailbone = this.body.getChild("tailbone");

        this.head = root.getChild("head");
        this.fangs = this.head.getChild("fangs");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot();

        ModelPartData body = root.addChild(
            "body",
            ModelPartBuilder.create()
                            .uv(0, 0)
                            .cuboid(-4.0F, 0.0F, -3.0F, 8.0F, 13.0F, 6.0F),
            ModelTransform.of(0.0F, 6.5774F, 0.9063F, 0.8727F, 0.0F, 0.0F)
        );

        ModelPartData left_wing = body.addChild(
            "left_wing",
            ModelPartBuilder.create()
                            .uv(32, 0)
                            .cuboid(0.0F, 0.0F, 0.0F, 11.0F, 13.0F, 0.0F),
            ModelTransform.of(4.0F, 1.0F, 2.0F, 0.0F, -0.5236F, 0.0F)
        );

        ModelPartData left_wing_outer = left_wing.addChild(
            "left_wing_outer",
            ModelPartBuilder.create()
                            .uv(32, 12)
                            .cuboid(0.0F, -1.0F, 0.0F, 11.0F, 13.0F, 0.0F),
            ModelTransform.of(11.0F, 1.0F, 0.0F, 0.0F, 1.0908F, 0.0F)
        );

        ModelPartData right_wing = body.addChild(
            "right_wing",
            ModelPartBuilder.create()
                            .uv(32, 0)
                            .mirrored()
                            .cuboid(-11.0F, 0.0F, 0.0F, 11.0F, 13.0F, 0.0F),
            ModelTransform.of(-4.0F, 1.0F, 2.0F, 0.0F, 0.5236F, 0.0F)
        );

        ModelPartData right_wing_outer = right_wing.addChild(
            "right_wing_outer",
            ModelPartBuilder.create()
                            .uv(32, 12)
                            .mirrored()
                            .cuboid(-11.0F, -1.0F, 0.0F, 11.0F, 12.0F, 0.0F),
            ModelTransform.of(-11.0F, 1.0F, 0.0F, 0.0F, -1.0908F, 0.0F)
        );

        ModelPartData legs = body.addChild(
            "legs",
            ModelPartBuilder.create()
                            .uv(0, 31)
                            .cuboid(-4.0F, 0.0F, 0.0F, 8.0F, 4.0F, 0.0F),
            ModelTransform.of(0.0F, 13.0F, 1.0F, -0.9599F, 0.0F, 0.0F)
        );

        ModelPartData talons = legs.addChild(
            "talons",
            ModelPartBuilder.create()
                            .uv(0, 35)
                            .cuboid(-4.0F, 0.0F, -1.0F, 3.0F, 4.0F, 4.0F)
                            .uv(0, 35)
                            .mirrored()
                            .cuboid(1.0F, 0.0F, -1.0F, 3.0F, 4.0F, 4.0F),
            ModelTransform.of(0.0F, 4.0F, 0.0F, -0.4363F, 0.0F, 0.0F)
        );

        ModelPartData tailbone = body.addChild(
            "tailbone",
            ModelPartBuilder.create()
                            .uv(32, 36)
                            .cuboid(-1.0F, -0.0912F, -2.1233F, 2.0F, 10.0F, 2.0F),
            ModelTransform.of(0.0F, 11.5912F, 3.1233F, 0.6109F, 0.0F, 0.0F)
        );

        ModelPartData head = root.addChild(
            "head",
            ModelPartBuilder.create()
                            .uv(16, 34)
                            .cuboid(-2.0F, -3.0F, -9.0F, 4.0F, 3.0F, 4.0F)
                            .uv(0, 19)
                            .cuboid(-4.0F, -6.0F, -5.0F, 8.0F, 6.0F, 6.0F)
                            .uv(16, 41)
                            .mirrored()
                            .cuboid(1.0F, -11.0F, -4.0F, 4.0F, 6.0F, 1.0F)
                            .uv(16, 41)
                            .mirrored(false)
                            .cuboid(-5.0F, -11.0F, -4.0F, 4.0F, 6.0F, 1.0F),
            ModelTransform.of(0.0F, 7.0F, 0.0F, 0.3491F, 0.0F, 0.0F)
        );

        ModelPartData fangs = head.addChild(
            "fangs",
            ModelPartBuilder.create()
                            .uv(12, 35)
                            .cuboid(-2.0F, -1.0F, -9.0F, 4.0F, 2.0F, 0.0F),
            ModelTransform.NONE
        );

        return TexturedModelData.of(data, 64, 48);
    }

    public ModelPart getHead() {
        return this.head;
    }

    @Override
    public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        if (entity.isHanging()) {
            this.head.pitch = headPitch * ((float) Math.PI / 180F);
            this.body.pitch = 0.0F;

            this.rightWing.pitch = 0.0F;
            this.leftWing.pitch = 0.0F;

            this.leftWing.yaw = 1.5F;
            this.rightWing.yaw = -this.leftWing.yaw;

            this.leftWingOuter.yaw = 90.0F;
            this.rightWingOuter.yaw = -this.leftWingOuter.yaw;
        } else {
            this.head.pitch = headPitch * (float) (Math.PI / 180);
            this.head.yaw = headYaw * (float) (Math.PI / 180);

            this.body.pitch = (float) (Math.PI / 4) + MathHelper.cos(animationProgress * 0.1F) * 0.15F;

            this.rightWing.yaw = MathHelper.cos(animationProgress * 0.4F) * (float) Math.PI * 0.4F;
            this.leftWing.yaw = -this.rightWing.yaw;

            this.rightWingOuter.yaw = this.rightWing.yaw * 0.5F;
            this.leftWingOuter.yaw  = this.leftWing.yaw  * 0.5F;
        }
    }

    @Override
    public ModelPart getPart() {
        return this.root;
    }
}
