package net.teamhollow.direbats.entity.direbat;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;

@SuppressWarnings("FieldCanBeLocal")
public class DirebatEntityModel extends SegmentedModel<DirebatEntity> {
    private final ModelRenderer head;
    private final ModelRenderer body;
    private final ModelRenderer leftWing;
    private final ModelRenderer leftWingOuter;
    private final ModelRenderer rightWing;
    private final ModelRenderer rightWingOuter;
    private final ModelRenderer legs;
    public final ModelRenderer talons;
    private final ModelRenderer tailbone;
    public final ModelRenderer fangs;

    public DirebatEntityModel() {
        textureWidth = 64;
        textureHeight = 48;

        head = new ModelRenderer(this);
        head.setRotationPoint(0.0F, -17.0F, -3.0F);
        setRotationAngle(head, 0.3491F, 0.0F, 0.0F);
        head.setTextureOffset(16, 41).addBox(-5.0F, -11.0F, -4.0F, 4.0F, 6.0F, 1.0F, 0.0F, false);
        head.setTextureOffset(16, 41).addBox(1.0F, -11.0F, -4.0F, 4.0F, 6.0F, 1.0F, 0.0F, true);
        head.setTextureOffset(0, 19).addBox(-4.0F, -6.0F, -5.0F, 8.0F, 6.0F, 6.0F, 0.0F, false);
        head.setTextureOffset(16, 34).addBox(-2.0F, -3.0F, -9.0F, 4.0F, 3.0F, 4.0F, 0.0F, false);

        body = new ModelRenderer(this);
        body.setRotationPoint(0.0F, -17.4226F, -2.0937F);
        setRotationAngle(body, 0.8727F, 0.0F, 0.0F);
        body.setTextureOffset(0, 0).addBox(-4.0F, 0.0F, -3.0F, 8.0F, 13.0F, 6.0F, 0.0F, false);

        leftWing = new ModelRenderer(this);
        leftWing.setRotationPoint(-4.0F, 1.0F, 2.0F);
        body.addChild(leftWing);
        setRotationAngle(leftWing, 0.0F, 0.5236F, 0.0F);
        leftWing.setTextureOffset(32, 0).addBox(-11.0F, 0.0F, 0.0F, 11.0F, 13.0F, 0.0F, 0.0F, true);

        leftWingOuter = new ModelRenderer(this);
        leftWingOuter.setRotationPoint(-11.0F, 1.0F, 0.0F);
        leftWing.addChild(leftWingOuter);
        setRotationAngle(leftWingOuter, 0.0F, -1.0908F, 0.0F);
        leftWingOuter.setTextureOffset(32, 12).addBox(-11.0F, -1.0F, 0.0F, 11.0F, 12.0F, 0.0F, 0.0F, true);

        rightWing = new ModelRenderer(this);
        rightWing.setRotationPoint(4.0F, 1.0F, 2.0F);
        body.addChild(rightWing);
        setRotationAngle(rightWing, 0.0F, -0.5236F, 0.0F);
        rightWing.setTextureOffset(32, 0).addBox(0.0F, 0.0F, 0.0F, 11.0F, 13.0F, 0.0F, 0.0F, false);

        rightWingOuter = new ModelRenderer(this);
        rightWingOuter.setRotationPoint(11.0F, 1.0F, 0.0F);
        rightWing.addChild(rightWingOuter);
        setRotationAngle(rightWingOuter, 0.0F, 1.0908F, 0.0F);
        rightWingOuter.setTextureOffset(32, 12).addBox(0.0F, -1.0F, 0.0F, 11.0F, 13.0F, 0.0F, 0.0F, false);

        legs = new ModelRenderer(this);
        legs.setRotationPoint(0.0F, 13.0F, 1.0F);
        body.addChild(legs);
        setRotationAngle(legs, -0.9599F, 0.0F, 0.0F);
        legs.setTextureOffset(0, 31).addBox(-4.0F, 0.0F, 0.0F, 8.0F, 4.0F, 0.0F, 0.0F, false);

        talons = new ModelRenderer(this);
        talons.setRotationPoint(0.0F, 4.0F, 0.0F);
        legs.addChild(talons);
        setRotationAngle(talons, -0.4363F, 0.0F, 0.0F);
        talons.setTextureOffset(0, 35).addBox(-4.0F, 0.0F, -1.0F, 3.0F, 4.0F, 4.0F, 0.0F, false);
        talons.setTextureOffset(0, 35).addBox(1.0F, 0.0F, -1.0F, 3.0F, 4.0F, 4.0F, 0.0F, true);

        tailbone = new ModelRenderer(this);
        tailbone.setRotationPoint(0.0F, 11.5912F, 3.1233F);
        body.addChild(tailbone);
        setRotationAngle(tailbone, 0.6109F, 0.0F, 0.0F);
        tailbone.setTextureOffset(32, 36).addBox(-1.0F, -0.0912F, -2.1233F, 2.0F, 10.0F, 2.0F, 0.0F, false);

        fangs = new ModelRenderer(this);
        fangs.setRotationPoint(0.0F, 17.0F, 3.0F);
        head.addChild(fangs);
        fangs.setTextureOffset(12, 35).addBox(-2.0F, -18.0F, -12.0F, 4.0F, 2.0F, 0.0F, 0.0F, false);
    }

    @Override
    public void setRotationAngles(DirebatEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.head.rotateAngleX = headPitch * 0.017453292F;
        this.head.rotateAngleY = netHeadYaw * 0.017453292F;
        this.head.rotateAngleZ = 0.0F;
        this.body.rotateAngleX = 0.7853982F + MathHelper.cos(ageInTicks * 0.1F) * 0.15F;
        this.body.rotateAngleY = 0.0F;
        this.rightWing.rotateAngleY = MathHelper.cos(ageInTicks * 0.4F) * (float) Math.PI * 0.4F;
        this.leftWing.rotateAngleY = -this.rightWing.rotateAngleY;
        this.rightWingOuter.rotateAngleY = this.rightWing.rotateAngleY * 0.5F;
        this.leftWingOuter.rotateAngleY = -this.rightWing.rotateAngleY * 0.5F;

        if (entityIn.isHanging()) {
            this.head.rotateAngleX = headPitch * ((float) Math.PI / 180F);
            this.body.rotateAngleX = 0.0F;
            this.rightWing.rotateAngleX = 0.0F;
            this.rightWing.rotateAngleY = 1.5F;
            this.leftWing.rotateAngleX = this.rightWing.rotateAngleX;
            this.leftWing.rotateAngleY = -this.rightWing.rotateAngleY;
            this.rightWingOuter.rotateAngleY = this.rightWing.rotateAngleY + 0.3F;
            this.leftWingOuter.rotateAngleY = -this.rightWing.rotateAngleY;
        }
    }

    @Override
    public Iterable<ModelRenderer> getParts() {
        return ImmutableList.of(head, body);
    }

    public void setRotationAngle(ModelRenderer bone, float x, float y, float z) {
        bone.rotateAngleX = x;
        bone.rotateAngleY = y;
        bone.rotateAngleZ = z;
    }
}
