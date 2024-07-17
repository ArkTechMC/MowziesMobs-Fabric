package com.bobmowzie.mowziesmobs.client.model.entity;

import com.bobmowzie.mowziesmobs.server.entity.grottol.EntityGrottol;
import com.bobmowzie.mowziesmobs.client.model.tools.AdvancedModelRenderer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

/**
 * Created by BobMowzie on 7/3/2018.
 */

public class ModelGrottol<T extends EntityGrottol> extends MowzieEntityModel<T> {
    public AdvancedModelRenderer body;
    public AdvancedModelRenderer head;
    public AdvancedModelRenderer crystal1;
    public AdvancedModelRenderer crystal2;
    public AdvancedModelRenderer crystal3;
    public AdvancedModelRenderer crystal4;
    public AdvancedModelRenderer crystal5;
    public AdvancedModelRenderer crystal6;
    public AdvancedModelRenderer clawLeftJoint;
    public AdvancedModelRenderer crystal7;
    public AdvancedModelRenderer clawRightJoint;
    public AdvancedModelRenderer leg1LeftJoint;
    public AdvancedModelRenderer leg2LeftJoint;
    public AdvancedModelRenderer leg3LeftJoint;
    public AdvancedModelRenderer leg1RightJoint;
    public AdvancedModelRenderer leg2RightJoint;
    public AdvancedModelRenderer leg3RightJoint;
    public AdvancedModelRenderer eyeLeft;
    public AdvancedModelRenderer eyeRight;
    public AdvancedModelRenderer clawLeftUpper;
    public AdvancedModelRenderer clawLeftLower;
    public AdvancedModelRenderer clawLeft;
    public AdvancedModelRenderer clawRightUpper;
    public AdvancedModelRenderer clawRightLower;
    public AdvancedModelRenderer clawRight;
    public AdvancedModelRenderer leg1LeftUpper;
    public AdvancedModelRenderer leg1LeftLower;
    public AdvancedModelRenderer foot1Left;
    public AdvancedModelRenderer leg2LeftUpper;
    public AdvancedModelRenderer leg2LeftLower;
    public AdvancedModelRenderer foot2Left;
    public AdvancedModelRenderer leg3LeftUpper;
    public AdvancedModelRenderer leg3LeftLower;
    public AdvancedModelRenderer foot3Left;
    public AdvancedModelRenderer leg1RightUpper;
    public AdvancedModelRenderer leg1RightLower;
    public AdvancedModelRenderer foot1Right;
    public AdvancedModelRenderer leg2RightUpper;
    public AdvancedModelRenderer leg2RightLower;
    public AdvancedModelRenderer foot2Right;
    public AdvancedModelRenderer leg3RightUpper;
    public AdvancedModelRenderer leg3RightLower;
    public AdvancedModelRenderer foot3Right;
    public AdvancedModelRenderer dieAnimController;

    public ModelGrottol() {
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.crystal7 = new AdvancedModelRenderer(this, 0, 17);
        this.crystal7.setRotationPoint(-3.6F, -2.4F, 1.7F);
        this.crystal7.addBox(-1.5F, -5.0F, -1.5F, 3, 8, 3, 0.0F);
        setRotateAngle(this.crystal7, -1.3203415791337103F, 0.9105382707654417F, -1.9577358219620393F);
        this.clawRightJoint = new AdvancedModelRenderer(this, 0, 0);
        this.clawRightJoint.setRotationPoint(-2.0F, 2.0F, -3.5F);
        this.clawRightJoint.addBox(0.0F, -1.0F, -0.5F, 0, 0, 0, 0.0F);
        setRotateAngle(this.clawRightJoint, 0.17453292519943295F, -0.7740535232594852F, 0.0F);
        this.leg1RightUpper = new AdvancedModelRenderer(this, 0, 0);
        this.leg1RightUpper.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.leg1RightUpper.addBox(-3.0F, -1.0F, -0.5F, 3, 1, 1, 0.0F);
        setRotateAngle(this.leg1RightUpper, 0.0F, 0.0F, -0.6373942428283291F);
        this.leg1LeftUpper = new AdvancedModelRenderer(this, 0, 0);
        this.leg1LeftUpper.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.leg1LeftUpper.addBox(0.0F, -1.0F, -0.5F, 3, 1, 1, 0.0F);
        setRotateAngle(this.leg1LeftUpper, 0.0F, 0.0F, 0.6373942428283291F);
        this.leg3LeftJoint = new AdvancedModelRenderer(this, 0, 0);
        this.leg3LeftJoint.setRotationPoint(3.5F, 2.0F, 1.5F);
        this.leg3LeftJoint.addBox(0.0F, -1.0F, -0.5F, 0, 0, 0, 0.0F);
        setRotateAngle(this.leg3LeftJoint, 0.17453292519943295F, -0.6829473363053812F, 0.0F);
        this.clawLeft = new AdvancedModelRenderer(this, 42, 0);
        this.clawLeft.setRotationPoint(3.5F, 0.0F, 0.0F);
        this.clawLeft.addBox(-1.0F, -1.5F, -5.0F, 2, 3, 6, 0.0F);
        setRotateAngle(this.clawLeft, 0.27314402793711257F, 0.27314402793711257F, -0.045553093477052F);
        this.leg3RightLower = new AdvancedModelRenderer(this, 0, 0);
        this.leg3RightLower.setRotationPoint(-3.3F, -0.5F, 0.0F);
        this.leg3RightLower.addBox(-3.0F, -0.5F, -0.5F, 3, 1, 1, 0.0F);
        setRotateAngle(this.leg3RightLower, 0.0F, 0.0F, 1.4486232791552935F);
        this.leg3RightJoint = new AdvancedModelRenderer(this, 0, 0);
        this.leg3RightJoint.setRotationPoint(-3.5F, 2.0F, 1.5F);
        this.leg3RightJoint.addBox(0.0F, -1.0F, -0.5F, 0, 0, 0, 0.0F);
        setRotateAngle(this.leg3RightJoint, 0.17453292519943295F, 0.6829473363053812F, 0.0F);
        this.clawRightUpper = new AdvancedModelRenderer(this, 0, 2);
        this.clawRightUpper.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.clawRightUpper.addBox(-3.0F, -1.0F, -0.5F, 4, 1, 1, 0.0F);
        setRotateAngle(this.clawRightUpper, 0.0F, 0.0F, -0.6373942428283291F);
        this.foot3Left = new AdvancedModelRenderer(this, 44, 9);
        this.foot3Left.setRotationPoint(3.2F, 0.0F, 0.0F);
        this.foot3Left.addBox(-1.0F, -1.0F, -1.0F, 2, 5, 2, 0.0F);
        setRotateAngle(this.foot3Left, 0.0F, 0.0F, 0.45378560551852565F);
        this.crystal5 = new AdvancedModelRenderer(this, 0, 17);
        this.crystal5.setRotationPoint(4.0F, -3.0F, -3.5F);
        this.crystal5.addBox(-1.5F, -4.5F, -1.5F, 3, 6, 3, 0.0F);
        setRotateAngle(this.crystal5, 0.5562364326105927F, 0.8134979643545569F, 1.392423677241076F);
        this.clawLeftJoint = new AdvancedModelRenderer(this, 0, 0);
        this.clawLeftJoint.setRotationPoint(2.0F, 2.0F, -3.5F);
        this.clawLeftJoint.addBox(0.0F, -1.0F, -0.5F, 0, 0, 0, 0.0F);
        setRotateAngle(this.clawLeftJoint, 0.17453292519943295F, 0.7740535232594852F, 0.0F);
        this.clawLeftLower = new AdvancedModelRenderer(this, 0, 2);
        this.clawLeftLower.setRotationPoint(3.0F, -0.5F, 0.0F);
        this.clawLeftLower.addBox(0.0F, -0.5F, -0.5F, 4, 1, 1, 0.0F);
        setRotateAngle(this.clawLeftLower, 0.0F, 0.0F, -0.9105382707654417F);
        this.crystal4 = new AdvancedModelRenderer(this, 0, 17);
        this.crystal4.setRotationPoint(-4.5F, -3.0F, -2.0F);
        this.crystal4.addBox(-1.5F, -5.0F, -1.5F, 3, 7, 3, 0.0F);
        setRotateAngle(this.crystal4, 0.22759093446006054F, -0.31869712141416456F, -0.5462880558742251F);
        this.foot2Left = new AdvancedModelRenderer(this, 52, 9);
        this.foot2Left.setRotationPoint(3.3F, 0.0F, 0.0F);
        this.foot2Left.addBox(-1.0F, -1.0F, -1.0F, 2, 6, 2, 0.0F);
        setRotateAngle(this.foot2Left, 0.0F, 0.0F, 0.3543018381548489F);
        this.clawRightLower = new AdvancedModelRenderer(this, 0, 2);
        this.clawRightLower.setRotationPoint(-3.0F, -0.5F, 0.0F);
        this.clawRightLower.addBox(-4.0F, -0.5F, -0.5F, 4, 1, 1, 0.0F);
        setRotateAngle(this.clawRightLower, 0.0F, 0.0F, 0.9105382707654417F);
        this.clawLeftUpper = new AdvancedModelRenderer(this, 0, 2);
        this.clawLeftUpper.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.clawLeftUpper.addBox(-1.0F, -1.0F, -0.5F, 4, 1, 1, 0.0F);
        setRotateAngle(this.clawLeftUpper, 0.0F, 0.0F, 0.6373942428283291F);
        this.leg1LeftJoint = new AdvancedModelRenderer(this, 0, 0);
        this.leg1LeftJoint.setRotationPoint(3.2F, 2.0F, -1.8F);
        this.leg1LeftJoint.addBox(0.0F, -1.0F, -0.5F, 0, 0, 0, 0.0F);
        setRotateAngle(this.leg1LeftJoint, 0.17453292519943295F, 0.45378560551852565F, 0.0F);
        this.crystal1 = new AdvancedModelRenderer(this, 12, 15);
        this.crystal1.setRotationPoint(2.0F, -4.0F, -1.0F);
        this.crystal1.addBox(-2.0F, -11.0F, -2.0F, 4, 13, 4, 0.0F);
        setRotateAngle(this.crystal1, 0.045553093477052F, -0.6829473363053812F, 0.22759093446006054F);
        this.clawRight = new AdvancedModelRenderer(this, 42, 0);
        this.clawRight.setRotationPoint(-3.5F, 0.0F, 0.0F);
        this.clawRight.addBox(-1.0F, -1.5F, -5.0F, 2, 3, 6, 0.0F);
        setRotateAngle(this.clawRight, 0.27314402793711257F, -0.27314402793711257F, 0.045553093477052F);
        this.leg2LeftUpper = new AdvancedModelRenderer(this, 0, 2);
        this.leg2LeftUpper.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.leg2LeftUpper.addBox(-0.7F, -1.0F, -0.5F, 4, 1, 1, 0.0F);
        setRotateAngle(this.leg2LeftUpper, 0.0F, 0.0F, 0.5061454830783556F);
        this.leg1LeftLower = new AdvancedModelRenderer(this, 0, 0);
        this.leg1LeftLower.setRotationPoint(3.0F, -0.5F, 0.0F);
        this.leg1LeftLower.addBox(0.0F, -0.5F, -0.5F, 3, 1, 1, 0.0F);
        setRotateAngle(this.leg1LeftLower, 0.0F, 0.0F, -1.2747884856566583F);
        this.leg2RightUpper = new AdvancedModelRenderer(this, 0, 2);
        this.leg2RightUpper.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.leg2RightUpper.addBox(-3.3F, -1.0F, -0.5F, 4, 1, 1, 0.0F);
        setRotateAngle(this.leg2RightUpper, 0.0F, 0.0F, -0.5061454830783556F);
        this.crystal3 = new AdvancedModelRenderer(this, 0, 17);
        this.crystal3.setRotationPoint(0.0F, -3.0F, -4.8F);
        this.crystal3.addBox(-1.5F, -7.0F, -1.5F, 3, 9, 3, 0.0F);
        setRotateAngle(this.crystal3, 0.5918411493512771F, 0.31869712141416456F, 0.22759093446006054F);
        this.eyeLeft = new AdvancedModelRenderer(this, 0, 4);
        this.eyeLeft.mirror = true;
        this.eyeLeft.setRotationPoint(2.0F, -0.5F, -2.5F);
        this.eyeLeft.addBox(-1.0F, -1.0F, -1.0F, 2, 2, 2, 0.0F);
        setRotateAngle(this.eyeLeft, -1.0471975511965976F, 1.7453292519943295F, 2.530727415391778F);
        this.head = new AdvancedModelRenderer(this, 34, 0);
        this.head.setRotationPoint(0.0F, 1.5F, -5.0F);
        this.head.addBox(-2.0F, -1.0F, -3.0F, 4, 3, 2, 0.0F);
        setRotateAngle(this.head, 0.17453292519943295F, 0.0F, 0.0F);
        this.foot1Right = new AdvancedModelRenderer(this, 44, 9);
        this.foot1Right.setRotationPoint(-3.0F, 0.0F, 0.0F);
        this.foot1Right.addBox(-1.0F, -1.0F, -1.0F, 2, 5, 2, 0.0F);
        setRotateAngle(this.foot1Right, 0.0F, 0.0F, -0.27314402793711257F);
        this.leg2RightLower = new AdvancedModelRenderer(this, 0, 0);
        this.leg2RightLower.setRotationPoint(-3.3F, -0.5F, 0.0F);
        this.leg2RightLower.addBox(-3.0F, -0.5F, -0.5F, 3, 1, 1, 0.0F);
        setRotateAngle(this.leg2RightLower, 0.0F, 0.0F, 1.509709802975095F);
        this.foot3Right = new AdvancedModelRenderer(this, 44, 9);
        this.foot3Right.setRotationPoint(-3.2F, 0.0F, 0.0F);
        this.foot3Right.addBox(-1.0F, -1.0F, -1.0F, 2, 5, 2, 0.0F);
        setRotateAngle(this.foot3Right, 0.0F, 0.0F, -0.45378560551852565F);
        this.crystal6 = new AdvancedModelRenderer(this, 0, 17);
        this.crystal6.setRotationPoint(2.0F, -3.0F, 3.0F);
        this.crystal6.addBox(-1.5F, -5.0F, -1.5F, 3, 8, 3, 0.0F);
        setRotateAngle(this.crystal6, -0.5918411493512771F, -0.31869712141416456F, 0.5462880558742251F);
        this.foot2Right = new AdvancedModelRenderer(this, 52, 9);
        this.foot2Right.setRotationPoint(-3.3F, 0.0F, 0.0F);
        this.foot2Right.addBox(-1.0F, -1.0F, -1.0F, 2, 6, 2, 0.0F);
        setRotateAngle(this.foot2Right, 0.0F, 0.0F, -0.3508111796508603F);
        this.crystal2 = new AdvancedModelRenderer(this, 0, 17);
        this.crystal2.setRotationPoint(-2.0F, -3.0F, 1.0F);
        this.crystal2.addBox(-1.5F, -10.0F, -1.5F, 3, 12, 3, 0.0F);
        setRotateAngle(this.crystal2, 0.6373942428283291F, 2.0032889154390916F, 0.40980330836826856F);
        this.body = new AdvancedModelRenderer(this, 0, 0);
        this.body.setRotationPoint(0.0F, 19.0F, 0.0F);
        this.body.addBox(-6.0F, -3.0F, -6.0F, 12, 5, 10, 0.0F);
        setRotateAngle(this.body, -0.17453292519943295F, 0.0F, 0.0F);
        this.foot1Left = new AdvancedModelRenderer(this, 44, 9);
        this.foot1Left.setRotationPoint(3.0F, 0.0F, 0.0F);
        this.foot1Left.addBox(-1.0F, -1.0F, -1.0F, 2, 5, 2, 0.0F);
        setRotateAngle(this.foot1Left, 0.0F, 0.0F, 0.27314402793711257F);
        this.leg2LeftLower = new AdvancedModelRenderer(this, 0, 0);
        this.leg2LeftLower.setRotationPoint(3.3F, -0.5F, 0.0F);
        this.leg2LeftLower.addBox(0.0F, -0.5F, -0.5F, 3, 1, 1, 0.0F);
        setRotateAngle(this.leg2LeftLower, 0.0F, 0.0F, -1.509709802975095F);
        this.leg1RightLower = new AdvancedModelRenderer(this, 0, 0);
        this.leg1RightLower.setRotationPoint(-3.0F, -0.5F, 0.0F);
        this.leg1RightLower.addBox(-3.0F, -0.5F, -0.5F, 3, 1, 1, 0.0F);
        setRotateAngle(this.leg1RightLower, 0.0F, 0.0F, 1.2747884856566583F);
        this.leg2RightJoint = new AdvancedModelRenderer(this, 0, 0);
        this.leg2RightJoint.setRotationPoint(-3.8F, 2.0F, -0.1F);
        this.leg2RightJoint.addBox(0.0F, -1.0F, -0.5F, 0, 0, 0, 0.0F);
        setRotateAngle(this.leg2RightJoint, 0.17453292519943295F, 0.04363323129985824F, 0.0F);
        this.leg3RightUpper = new AdvancedModelRenderer(this, 0, 2);
        this.leg3RightUpper.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.leg3RightUpper.addBox(-3.3F, -1.0F, -0.5F, 4, 1, 1, 0.0F);
        setRotateAngle(this.leg3RightUpper, 0.0F, 0.0F, -0.41887902047863906F);
        this.eyeRight = new AdvancedModelRenderer(this, 0, 4);
        this.eyeRight.setRotationPoint(-2.0F, -0.5F, -2.5F);
        this.eyeRight.addBox(-1.0F, -1.0F, -1.0F, 2, 2, 2, 0.0F);
        setRotateAngle(this.eyeRight, -1.0471975511965976F, -1.7453292519943295F, -2.530727415391778F);
        this.leg3LeftUpper = new AdvancedModelRenderer(this, 0, 2);
        this.leg3LeftUpper.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.leg3LeftUpper.addBox(-0.7F, -1.0F, -0.5F, 4, 1, 1, 0.0F);
        setRotateAngle(this.leg3LeftUpper, 0.0F, 0.0F, 0.41887902047863906F);
        this.leg1RightJoint = new AdvancedModelRenderer(this, 0, 0);
        this.leg1RightJoint.setRotationPoint(-3.2F, 2.0F, -2.0F);
        this.leg1RightJoint.addBox(0.0F, -1.0F, -0.5F, 0, 0, 0, 0.0F);
        setRotateAngle(this.leg1RightJoint, 0.17453292519943295F, -0.40142572795869574F, 0.0F);
        this.leg3LeftLower = new AdvancedModelRenderer(this, 0, 0);
        this.leg3LeftLower.setRotationPoint(3.3F, -0.5F, 0.0F);
        this.leg3LeftLower.addBox(0.0F, -0.5F, -0.5F, 3, 1, 1, 0.0F);
        setRotateAngle(this.leg3LeftLower, 0.0F, 0.0F, -1.4486232791552935F);
        this.leg2LeftJoint = new AdvancedModelRenderer(this, 0, 0);
        this.leg2LeftJoint.setRotationPoint(3.8F, 2.0F, -0.1F);
        this.leg2LeftJoint.addBox(0.0F, -1.0F, -0.5F, 0, 0, 0, 0.0F);
        setRotateAngle(this.leg2LeftJoint, 0.17453292519943295F, -0.04363323129985824F, 0.0F);
        this.dieAnimController = new AdvancedModelRenderer(this, 0, 0);
        this.dieAnimController.setRotationPoint(0, 0, 0);
        this.body.addChild(this.crystal7);
        this.body.addChild(this.clawRightJoint);
        this.leg1RightJoint.addChild(this.leg1RightUpper);
        this.leg1LeftJoint.addChild(this.leg1LeftUpper);
        this.body.addChild(this.leg3LeftJoint);
        this.clawLeftLower.addChild(this.clawLeft);
        this.leg3RightUpper.addChild(this.leg3RightLower);
        this.body.addChild(this.leg3RightJoint);
        this.clawRightJoint.addChild(this.clawRightUpper);
        this.leg3LeftLower.addChild(this.foot3Left);
        this.body.addChild(this.crystal5);
        this.body.addChild(this.clawLeftJoint);
        this.clawLeftUpper.addChild(this.clawLeftLower);
        this.body.addChild(this.crystal4);
        this.leg2LeftLower.addChild(this.foot2Left);
        this.clawRightUpper.addChild(this.clawRightLower);
        this.clawLeftJoint.addChild(this.clawLeftUpper);
        this.body.addChild(this.leg1LeftJoint);
        this.body.addChild(this.crystal1);
        this.clawRightLower.addChild(this.clawRight);
        this.leg2LeftJoint.addChild(this.leg2LeftUpper);
        this.leg1LeftUpper.addChild(this.leg1LeftLower);
        this.leg2RightJoint.addChild(this.leg2RightUpper);
        this.body.addChild(this.crystal3);
        this.head.addChild(this.eyeLeft);
        this.body.addChild(this.head);
        this.leg1RightLower.addChild(this.foot1Right);
        this.leg2RightUpper.addChild(this.leg2RightLower);
        this.leg3RightLower.addChild(this.foot3Right);
        this.body.addChild(this.crystal6);
        this.leg2RightLower.addChild(this.foot2Right);
        this.body.addChild(this.crystal2);
        this.leg1LeftLower.addChild(this.foot1Left);
        this.leg2LeftUpper.addChild(this.leg2LeftLower);
        this.leg1RightUpper.addChild(this.leg1RightLower);
        this.body.addChild(this.leg2RightJoint);
        this.leg3RightJoint.addChild(this.leg3RightUpper);
        this.head.addChild(this.eyeRight);
        this.leg3LeftJoint.addChild(this.leg3LeftUpper);
        this.body.addChild(this.leg1RightJoint);
        this.leg3LeftUpper.addChild(this.leg3LeftLower);
        this.body.addChild(this.leg2LeftJoint);
        this.updateDefaultPose();
    }

    @Override
    public void render(MatrixStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        this.body.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }

    @Override
    public void setAngles(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        super.setAngles(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

        netHeadYaw = Math.min(netHeadYaw, 30f);
        netHeadYaw = Math.max(netHeadYaw, -30f);
        this.faceTarget(netHeadYaw, headPitch, 1, this.head);

        if (limbSwingAmount > 0.5) limbSwingAmount = 0.5f;
        float globalSpeed = 1.5f;
        float globalDegree = 0.5f;
        this.swing(this.leg1LeftJoint, globalSpeed, globalDegree * 1.2f, false, 0, 0.1f, limbSwing, limbSwingAmount);
        this.flap(this.leg1LeftUpper, globalSpeed, globalDegree * 0.5f, true, 1.57f, 0.2f, limbSwing, limbSwingAmount);
        this.flap(this.leg1LeftLower, globalSpeed, globalDegree * 0.8f, true, 1.57f, 0f, limbSwing, limbSwingAmount);
        this.flap(this.foot1Left, globalSpeed, globalDegree * 1.3f, false, 1.57f, 0f, limbSwing, limbSwingAmount);

        this.swing(this.leg2LeftJoint, globalSpeed, globalDegree * 1.2f, false, 0 + 1.57f, 0.1f, limbSwing, limbSwingAmount);
        this.flap(this.leg2LeftUpper, globalSpeed, globalDegree * 0.5f, true, 1.57f + 1.57f, 0.2f, limbSwing, limbSwingAmount);
        this.flap(this.leg2LeftLower, globalSpeed, globalDegree * 0.8f, true, 1.57f + 1.57f, 0f, limbSwing, limbSwingAmount);
        this.flap(this.foot2Left, globalSpeed, globalDegree * 1.3f, false, 1.57f + 1.57f, 0f, limbSwing, limbSwingAmount);

        this.swing(this.leg3LeftJoint, globalSpeed, globalDegree * 1.2f, false, 0 + 1.57f * 2, 0.1f, limbSwing, limbSwingAmount);
        this.flap(this.leg3LeftUpper, globalSpeed, globalDegree * 0.5f, true, 1.57f + 1.57f * 2, 0.2f, limbSwing, limbSwingAmount);
        this.flap(this.leg3LeftLower, globalSpeed, globalDegree * 0.8f, true, 1.57f + 1.57f * 2, 0f, limbSwing, limbSwingAmount);
        this.flap(this.foot3Left, globalSpeed, globalDegree * 1.3f, false, 1.57f + 1.57f * 2, 0f, limbSwing, limbSwingAmount);

        this.swing(this.leg1RightJoint, globalSpeed, globalDegree * 1.2f, false, 0, -0.1f, limbSwing, limbSwingAmount);
        this.flap(this.leg1RightUpper, globalSpeed, globalDegree * 0.5f, true, 1.57f, -0.2f, limbSwing, limbSwingAmount);
        this.flap(this.leg1RightLower, globalSpeed, globalDegree * 0.8f, true, 1.57f, 0f, limbSwing, limbSwingAmount);
        this.flap(this.foot1Right, globalSpeed, globalDegree * 1.3f, false, 1.57f, 0f, limbSwing, limbSwingAmount);

        this.swing(this.leg2RightJoint, globalSpeed, globalDegree * 1.2f, false, 0 + 1.57f, -0.1f, limbSwing, limbSwingAmount);
        this.flap(this.leg2RightUpper, globalSpeed, globalDegree * 0.5f, true, 1.57f + 1.57f, -0.2f, limbSwing, limbSwingAmount);
        this.flap(this.leg2RightLower, globalSpeed, globalDegree * 0.8f, true, 1.57f + 1.57f, 0f, limbSwing, limbSwingAmount);
        this.flap(this.foot2Right, globalSpeed, globalDegree * 1.3f, false, 1.57f + 1.57f, 0f, limbSwing, limbSwingAmount);

        this.swing(this.leg3RightJoint, globalSpeed, globalDegree * 1.2f, false, 0 + 1.57f * 2, -0.1f, limbSwing, limbSwingAmount);
        this.flap(this.leg3RightUpper, globalSpeed, globalDegree * 0.5f, true, 1.57f + 1.57f * 2, -0.2f, limbSwing, limbSwingAmount);
        this.flap(this.leg3RightLower, globalSpeed, globalDegree * 0.8f, true, 1.57f + 1.57f * 2, 0f, limbSwing, limbSwingAmount);
        this.flap(this.foot3Right, globalSpeed, globalDegree * 1.3f, false, 1.57f + 1.57f * 2, 0f, limbSwing, limbSwingAmount);
    }

    @Override
    protected void animate(T entity, float limbSwing, float limbSwingAmount, float headYaw, float headPitch, float delta) {
//        setRotationAngles(entity, limbSwing, limbSwingAmount, headYaw, headPitch, delta);
        float frame = entity.frame + delta;

        if (entity.getAnimation() == EntityGrottol.IDLE_ANIMATION) {
            this.animator.setAnimation(EntityGrottol.IDLE_ANIMATION);

            this.animator.startKeyframe(6);//6
            this.animator.move(this.body, 1, -2, 0);
            this.animator.rotate(this.body, 0, 0, 0.1f);
            this.animator.rotate(this.head, 0, 0, 0.6f);
            this.animator.rotate(this.clawLeftUpper, 0.2f, -0.3f, 0.3f);
            this.animator.rotate(this.clawLeft, -0.3f, 0.0f, -0.3f);
            this.animator.rotate(this.clawRightUpper, 0.2f, 0.3f, -0.3f);
            this.animator.rotate(this.clawRight, -0.3f, 0.0f, 0.3f);
            this.animator.rotate(this.leg1RightUpper, 0, 0, -0.3f);
            this.animator.rotate(this.leg1RightLower, 0, 0, -0.45f);
            this.animator.rotate(this.foot1Right, 0, 0, 0.7f);
            this.animator.rotate(this.leg2RightUpper, 0, 0, -0.3f);
            this.animator.rotate(this.leg2RightLower, 0, 0, -0.35f);
            this.animator.rotate(this.foot2Right, 0, 0, 0.5f);
            this.animator.rotate(this.leg3RightUpper, 0, 0, -0.3f);
            this.animator.rotate(this.leg3RightLower, 0, 0, -0.45f);
            this.animator.rotate(this.foot3Right, 0, 0, 0.7f);
            this.animator.rotate(this.leg1LeftUpper, 0, 0, 0.2f);
            this.animator.rotate(this.leg1LeftLower, 0, 0, 0.1f);
            this.animator.rotate(this.foot1Left, 0, 0, -0.1f);
            this.animator.rotate(this.leg2LeftUpper, 0, 0, 0.1f);
            this.animator.rotate(this.leg2LeftLower, 0, 0, 0.1f);
            this.animator.rotate(this.foot2Left, 0, 0, 0f);
            this.animator.rotate(this.leg3LeftUpper, 0, 0, 0.2f);
            this.animator.rotate(this.leg3LeftLower, 0, 0, 0.1f);
            this.animator.rotate(this.foot3Left, 0, 0, -0.1f);
            this.animator.endKeyframe();

            this.animator.setStaticKeyframe(9);//15

            this.animator.startKeyframe(3);//19
            this.animator.move(this.body, 1, -2, 0);
            this.animator.rotate(this.body, 0, 0, 0.1f);
            this.animator.rotate(this.head, 0.5f, 0.3f, 0.4f);
            this.animator.rotate(this.clawLeftUpper, 0.2f, -0.3f, 0.3f);
            this.animator.rotate(this.clawLeft, -0.3f, 0.0f, -0.3f);
            this.animator.rotate(this.clawRightUpper, 0.2f, 0.3f, -0.3f);
            this.animator.rotate(this.clawRight, -0.3f, 0.0f, 0.3f);
            this.animator.rotate(this.leg1RightUpper, 0, 0, -0.3f);
            this.animator.rotate(this.leg1RightLower, 0, 0, -0.45f);
            this.animator.rotate(this.foot1Right, 0, 0, 0.7f);
            this.animator.rotate(this.leg2RightUpper, 0, 0, -0.3f);
            this.animator.rotate(this.leg2RightLower, 0, 0, -0.35f);
            this.animator.rotate(this.foot2Right, 0, 0, 0.5f);
            this.animator.rotate(this.leg3RightUpper, 0, 0, -0.3f);
            this.animator.rotate(this.leg3RightLower, 0, 0, -0.45f);
            this.animator.rotate(this.foot3Right, 0, 0, 0.7f);
            this.animator.rotate(this.leg1LeftUpper, 0, 0, 0.2f);
            this.animator.rotate(this.leg1LeftLower, 0, 0, 0.1f);
            this.animator.rotate(this.foot1Left, 0, 0, -0.1f);
            this.animator.rotate(this.leg2LeftUpper, 0, 0, 0.1f);
            this.animator.rotate(this.leg2LeftLower, 0, 0, 0.1f);
            this.animator.rotate(this.foot2Left, 0, 0, 0f);
            this.animator.rotate(this.leg3LeftUpper, 0, 0, 0.2f);
            this.animator.rotate(this.leg3LeftLower, 0, 0, 0.1f);
            this.animator.rotate(this.foot3Left, 0, 0, -0.1f);
            this.animator.endKeyframe();

            this.animator.setStaticKeyframe(3);//22

            this.animator.startKeyframe(4);//26
            this.animator.rotate(this.clawRightUpper, 0, 0.2f, 0.4f);
            this.animator.rotate(this.clawRightLower, 0, 0, 0.4f);
            this.animator.rotate(this.clawRight, 0.5f, 0.3f, -0.4f);
            this.animator.rotate(this.clawLeftUpper, -0.15f, -0.15f, -0.1f);
            this.animator.rotate(this.head, 0.4f, 0.2f, 0.4f);
            this.animator.endKeyframe();

            this.animator.setStaticKeyframe(2);//28

            this.animator.startKeyframe(1);//29
            this.animator.rotate(this.clawRightUpper, 0, 0.2f, -0.05f);
            this.animator.rotate(this.clawRightLower, 0, 0, -0.05f);
            this.animator.rotate(this.clawRight, 0.5f, 0.3f, -0.4f);
            this.animator.rotate(this.clawLeftUpper, -0.15f, -0.15f, -0.1f);
            this.animator.rotate(this.head, 0.4f, 0.2f, 0.4f);
            this.animator.endKeyframe();

            this.animator.setStaticKeyframe(1);//30

            this.animator.startKeyframe(2);//32
            this.animator.rotate(this.clawRightUpper, 0, 0.2f, 0.4f);
            this.animator.rotate(this.clawRightLower, 0, 0, 0.4f);
            this.animator.rotate(this.clawRight, 0.5f, 0.3f, -0.4f);
            this.animator.rotate(this.clawLeftUpper, -0.15f, -0.15f, -0.1f);
            this.animator.rotate(this.head, 0.4f, 0.2f, 0.4f);
            this.animator.endKeyframe();

            this.animator.setStaticKeyframe(1);//33

            this.animator.startKeyframe(1);//34
            this.animator.rotate(this.clawRightUpper, 0, 0.2f, -0.05f);
            this.animator.rotate(this.clawRightLower, 0, 0, -0.05f);
            this.animator.rotate(this.clawRight, 0.5f, 0.3f, -0.4f);
            this.animator.rotate(this.clawLeftUpper, -0.15f, -0.15f, -0.1f);
            this.animator.rotate(this.head, 0.4f, 0.2f, 0.4f);
            this.animator.endKeyframe();

            this.animator.setStaticKeyframe(1);

            this.animator.startKeyframe(2);
            this.animator.rotate(this.clawRightUpper, 0, 0.2f, 0.4f);
            this.animator.rotate(this.clawRightLower, 0, 0, 0.4f);
            this.animator.rotate(this.clawRight, 0.5f, 0.3f, -0.4f);
            this.animator.rotate(this.clawLeftUpper, -0.15f, -0.15f, -0.1f);
            this.animator.rotate(this.head, 0.4f, 0.2f, 0.4f);
            this.animator.endKeyframe();

            this.animator.setStaticKeyframe(7);

            this.animator.resetKeyframe(4);
        }

        if (entity.getAnimation() == EntityGrottol.DIE_ANIMATION) {
            this.animator.setAnimation(EntityGrottol.DIE_ANIMATION);

            this.animator.startKeyframe(7);
            this.animator.rotate(this.body, -3.5f, 0, 0);
            this.animator.rotate(this.head, 1f, 0, 0);
            this.animator.move(this.dieAnimController, 1, 1, 0);
            this.animator.endKeyframe();

            this.animator.startKeyframe(2);
            this.animator.rotate(this.body, -3.5f, 0, 0);
            this.animator.move(this.body, 0, -5, 0);
            this.animator.rotate(this.head, 1f, 0, 0);
            this.animator.move(this.dieAnimController, 1, 1, 0);
            this.animator.endKeyframe();

            this.animator.startKeyframe(2);
            this.animator.rotate(this.body, -3.5f, 0, 0);
            this.animator.rotate(this.head, 1f, 0, 0);
            this.animator.move(this.dieAnimController, 1, 1, 0);
            this.animator.endKeyframe();

            this.animator.setStaticKeyframe(18);

            this.animator.startKeyframe(12);
            this.animator.rotate(this.body, -3.5f, 0, 0);
            this.animator.rotate(this.head, 1f, 0, 0);
            this.animator.move(this.dieAnimController, 1, 0, 0);
            this.animator.rotate(this.leg1LeftUpper, 0, 0, 0.7f);
            this.animator.rotate(this.leg2LeftUpper, 0, 0, 0.7f);
            this.animator.rotate(this.leg3LeftUpper, 0, 0, 0.7f);
            this.animator.rotate(this.leg1RightUpper, 0, 0, -0.7f);
            this.animator.rotate(this.leg2RightUpper, 0, 0, -0.7f);
            this.animator.rotate(this.leg3RightUpper, 0, 0, -0.7f);
            this.animator.rotate(this.clawLeftUpper, 0, 0, 0.7f);
            this.animator.rotate(this.clawRightUpper, 0, 0, -0.7f);
            this.animator.rotate(this.clawLeft, 1, 0, 1);
            this.animator.rotate(this.clawRight, 1, 0, -1);
            this.animator.endKeyframe();

            this.animator.setStaticKeyframe(12);

            this.body.rotationPointY -= 18 * (-4 * this.dieAnimController.rotationPointX * this.dieAnimController.rotationPointX + 4 * this.dieAnimController.rotationPointX);
            float globalSpeed = 1f;
            float globalDegree = 0.5f * this.dieAnimController.rotationPointY;
            this.flap(this.body, globalSpeed, globalDegree * 0.2f, true, 0, 0, frame, 1);
            this.body.rotationPointX -= 1 * globalDegree * Math.cos(frame * globalSpeed);
            this.flap(this.leg1LeftUpper, globalSpeed, globalDegree * 0.5f, true, 1.57f, -0.9f, frame, 1);
            this.flap(this.leg1LeftLower, globalSpeed, globalDegree * 0.8f, true, 1.57f + 0.5f, 0.4f, frame, 1);
            this.flap(this.foot1Left, globalSpeed, globalDegree * 0.5f, false, 1.57f + 1f, -0.1f, frame, 1);

            this.flap(this.leg2LeftUpper, globalSpeed, globalDegree * 0.5f, true, 1.57f + 1.57f, -0.9f, frame, 1);
            this.flap(this.leg2LeftLower, globalSpeed, globalDegree * 0.8f, true, 1.57f + 1.57f + 0.5f, 0.4f, frame, 1);
            this.flap(this.foot2Left, globalSpeed, globalDegree * 0.5f, false, 1.57f + 1.57f + 1f, -0.1f, frame, 1);

            this.flap(this.leg3LeftUpper, globalSpeed, globalDegree * 0.5f, true, 1.57f + 1.57f * 2, -0.9f, frame, 1);
            this.flap(this.leg3LeftLower, globalSpeed, globalDegree * 0.8f, true, 1.57f + 1.57f * 2 + 0.5f, 0.4f, frame, 1);
            this.flap(this.foot3Left, globalSpeed, globalDegree * 0.5f, false, 1.57f + 1.57f * 2 + 1f, -0.1f, frame, 1);

            this.flap(this.leg1RightUpper, globalSpeed, globalDegree * 0.5f, true, 1.57f, 0.9f, frame, 1);
            this.flap(this.leg1RightLower, globalSpeed, globalDegree * 0.8f, true, 1.57f + 0.5f, -0.4f, frame, 1);
            this.flap(this.foot1Right, globalSpeed, globalDegree * 0.5f, false, 1.57f + 1f, 0.1f, frame, 1);

            this.flap(this.leg2RightUpper, globalSpeed, globalDegree * 0.5f, true, 1.57f + 1.57f, 0.9f, frame, 1);
            this.flap(this.leg2RightLower, globalSpeed, globalDegree * 0.8f, true, 1.57f + 1.57f + 0.5f, -0.4f, frame, 1);
            this.flap(this.foot2Right, globalSpeed, globalDegree * 0.5f, false, 1.57f + 1.57f + 1f, 0.1f, frame, 1);

            this.flap(this.leg3RightUpper, globalSpeed, globalDegree * 0.5f, true, 1.57f + 1.57f * 2, 0.9f, frame, 1);
            this.flap(this.leg3RightLower, globalSpeed, globalDegree * 0.8f, true, 1.57f + 1.57f * 2 + 0.5f, -0.4f, frame, 1);
            this.flap(this.foot3Right, globalSpeed, globalDegree * 0.5f, false, 1.57f + 1.57f * 2 + 1f, 0.1f, frame, 1);

            this.flap(this.clawLeftUpper, globalSpeed, globalDegree * 0.5f, true, 0, -0.3f, frame, 1);
            this.flap(this.clawLeftLower, globalSpeed, globalDegree * 0.5f, true, 0.5f, 0, frame, 1);

            this.flap(this.clawRightUpper, globalSpeed, globalDegree * 0.5f, true, 0, 0.3f, frame, 1);
            this.flap(this.clawRightLower, globalSpeed, globalDegree * 0.5f, true, 0.5f, 0, frame, 1);
        }

        if (entity.getAnimation() == EntityGrottol.BURROW_ANIMATION) {
            this.animator.setAnimation(EntityGrottol.BURROW_ANIMATION);

            this.animator.startKeyframe(4);
            this.animator.rotate(this.body, 0.2f, 0.4f, 0);
            this.animator.move(this.body, 0, 1f, 0);
            this.animator.rotate(this.clawLeftJoint, 0, 0.4f, 0);
            this.animator.rotate(this.clawLeftUpper, 0, 0, -0.2f);
            this.animator.rotate(this.clawLeftLower, 0, 0, 0.7f);
            this.animator.rotate(this.clawLeft, 0.7f, 0, -1);
            this.animator.rotate(this.clawRightJoint, 0, 0.2f, 0);
            this.animator.rotate(this.clawRightUpper, 0, 0, 1.3f);
            this.animator.rotate(this.clawRightLower, 0, 0, -0.7f);
            this.animator.rotate(this.clawRight, 0.3f, 0, 1);
            this.animator.endKeyframe();

            this.animator.startKeyframe(4);
            this.animator.rotate(this.body, 0.4f, -0.4f, 0);
            this.animator.move(this.body, 0, 3f, 0);
            this.animator.rotate(this.clawRightJoint, 0, -0.4f, 0);
            this.animator.rotate(this.clawRightUpper, 0, 0, 0.2f);
            this.animator.rotate(this.clawRightLower, 0, 0, -0.7f);
            this.animator.rotate(this.clawRight, 0.7f, 0, 1);
            this.animator.rotate(this.clawLeftJoint, 0, -0.2f, 0);
            this.animator.rotate(this.clawLeftUpper, 0, 0, -1.3f);
            this.animator.rotate(this.clawLeftLower, 0, 0, 0.7f);
            this.animator.rotate(this.clawLeft, 0.3f, 0, -1);
            this.animator.endKeyframe();

            this.animator.startKeyframe(4);
            this.animator.rotate(this.body, 0.6f, 0.4f, 0);
            this.animator.move(this.body, 0, 7f, 0);
            this.animator.rotate(this.clawLeftJoint, 0, 0.4f, 0);
            this.animator.rotate(this.clawLeftUpper, 0, 0, -0.2f);
            this.animator.rotate(this.clawLeftLower, 0, 0, 0.7f);
            this.animator.rotate(this.clawLeft, 0.7f, 0, -1);
            this.animator.rotate(this.clawRightJoint, 0, 0.2f, 0);
            this.animator.rotate(this.clawRightUpper, 0, 0, 1.3f);
            this.animator.rotate(this.clawRightLower, 0, 0, -0.7f);
            this.animator.rotate(this.clawRight, 0.3f, 0, 1);
            this.animator.endKeyframe();

            this.animator.startKeyframe(4);
            this.animator.rotate(this.body, 0.6f, -0.4f, 0);
            this.animator.move(this.body, 0, 13f, 0);
            this.animator.rotate(this.clawRightJoint, 0, -0.4f, 0);
            this.animator.rotate(this.clawRightUpper, 0, 0, 0.2f);
            this.animator.rotate(this.clawRightLower, 0, 0, -0.7f);
            this.animator.rotate(this.clawRight, 0.7f, 0, 1);
            this.animator.rotate(this.clawLeftJoint, 0, -0.2f, 0);
            this.animator.rotate(this.clawLeftUpper, 0, 0, -1.3f);
            this.animator.rotate(this.clawLeftLower, 0, 0, 0.7f);
            this.animator.rotate(this.clawLeft, 0.3f, 0, -1);
            this.animator.endKeyframe();

            this.animator.startKeyframe(4);
            this.animator.rotate(this.body, 0.6f, 0.4f, 0);
            this.animator.move(this.body, 0, 21.6f, 0);
            this.animator.rotate(this.clawLeftJoint, 0, 0.4f, 0);
            this.animator.rotate(this.clawLeftUpper, 0, 0, -0.2f);
            this.animator.rotate(this.clawLeftLower, 0, 0, 0.7f);
            this.animator.rotate(this.clawLeft, 0.7f, 0, -1);
            this.animator.rotate(this.clawRightJoint, 0, 0.2f, 0);
            this.animator.rotate(this.clawRightUpper, 0, 0, 1.3f);
            this.animator.rotate(this.clawRightLower, 0, 0, -0.7f);
            this.animator.rotate(this.clawRight, 0.3f, 0, 1);
            this.animator.endKeyframe();

            this.animator.setStaticKeyframe(40);
        }
    }
}
