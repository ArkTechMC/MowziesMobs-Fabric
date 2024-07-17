package com.bobmowzie.mowziesmobs.client.model.entity;

import com.bobmowzie.mowziesmobs.server.entity.wroughtnaut.EntityWroughtnaut;
import com.bobmowzie.mowziesmobs.client.model.tools.AdvancedModelRenderer;
import com.ilexiconn.llibrary.client.model.tools.BasicModelRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class ModelWroughtnaut<T extends EntityWroughtnaut> extends MowzieEntityModel<T> {
    public AdvancedModelRenderer waist;
    public AdvancedModelRenderer groin;
    public AdvancedModelRenderer stomachJoint;
    public AdvancedModelRenderer groinJoint;
    public AdvancedModelRenderer stomach;
    public AdvancedModelRenderer chestJoint;
    public AdvancedModelRenderer chest;
    public AdvancedModelRenderer neck;
    public AdvancedModelRenderer shoulderRightJoint;
    public AdvancedModelRenderer shoulderLeftJoint;
    public AdvancedModelRenderer head;
    public AdvancedModelRenderer helmet;
    public AdvancedModelRenderer tuskRight1;
    public AdvancedModelRenderer tuskLeft1;
    public AdvancedModelRenderer hornRight1;
    public AdvancedModelRenderer hornLeft1;
    public AdvancedModelRenderer tuskRight2;
    public AdvancedModelRenderer tuskLeft2;
    public AdvancedModelRenderer hornRight2;
    public AdvancedModelRenderer hornLeft2;
    public AdvancedModelRenderer shoulderRight;
    public AdvancedModelRenderer upperArmRightJoint;
    public AdvancedModelRenderer upperArmRight;
    public AdvancedModelRenderer lowerArmRightJoint;
    public AdvancedModelRenderer elbowRightJoint;
    public AdvancedModelRenderer lowerArmRight;
    public AdvancedModelRenderer handRightJoint;
    public AdvancedModelRenderer handRight;
    public AdvancedModelRenderer axeBase;
    public AdvancedModelRenderer axeHandle;
    public AdvancedModelRenderer axeBladeRight;
    public AdvancedModelRenderer axeBladeLeft;
    public AdvancedModelRenderer axeBladeRight1;
    public AdvancedModelRenderer axeBladeRight2;
    public AdvancedModelRenderer axeBladeRight3;
    public AdvancedModelRenderer axeBladeLeft1;
    public AdvancedModelRenderer axeBladeLeft2;
    public AdvancedModelRenderer axeBladeLeft3;
    public AdvancedModelRenderer elbowRight;
    public AdvancedModelRenderer shoulderLeft;
    public AdvancedModelRenderer upperArmLeftJoint;
    public AdvancedModelRenderer upperArmLeft;
    public AdvancedModelRenderer lowerArmLeftJoint;
    public AdvancedModelRenderer elbowLeftJoint;
    public AdvancedModelRenderer lowerArmLeft;
    public AdvancedModelRenderer handLeftJoint;
    public AdvancedModelRenderer handLeft;
    public AdvancedModelRenderer elbowLeft;
    public AdvancedModelRenderer groinFront;
    public AdvancedModelRenderer groinBack;
    public AdvancedModelRenderer thighRightJoint;
    public AdvancedModelRenderer thighLeftJoint;
    public AdvancedModelRenderer thighRightJoint2;
    public AdvancedModelRenderer thighRight;
    public AdvancedModelRenderer calfRightJoint;
    public AdvancedModelRenderer kneeRight;
    public AdvancedModelRenderer calfRight;
    public AdvancedModelRenderer footRightJoint;
    public AdvancedModelRenderer footRight;
    public AdvancedModelRenderer thighLeftJoint2;
    public AdvancedModelRenderer thighLeft;
    public AdvancedModelRenderer calfLeftJoint;
    public AdvancedModelRenderer kneeLeft;
    public AdvancedModelRenderer calfLeft;
    public AdvancedModelRenderer footLeftJoint;
    public AdvancedModelRenderer footLeft;
    public AdvancedModelRenderer sword;
    public AdvancedModelRenderer swordJoint;
    public AdvancedModelRenderer rootBox;
    public AdvancedModelRenderer waistBendController;
    public AdvancedModelRenderer eyeRight;
    public AdvancedModelRenderer eyeLeft;
    public AdvancedModelRenderer eyeRightSocket;
    public AdvancedModelRenderer eyeLeftSocket;

    public ModelWroughtnaut() {
        this(false);
    }

    public ModelWroughtnaut(boolean eyesLayer) {
        this.textureWidth = 128;
        this.textureHeight = 128;

        this.axeHandle = new AdvancedModelRenderer(this, 0, 22);
        this.axeHandle.setRotationPoint(3.0F, 0.0F, 1.0F);
        this.axeHandle.addBox(-1.5F, -44.0F, -1.5F, 3, 50, 3, 0.0F);
        this.stomach = new AdvancedModelRenderer(this, 80, 63);
        this.stomach.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.stomach.addBox(-6.0F, -13.7F, -6.0F, 12, 17, 12, 0.0F);
        setRotateAngle(this.stomach, 0.0F, 0.7853981633974483F, 0.0F);
        this.elbowRight = new AdvancedModelRenderer(this, 70, 24);
        this.elbowRight.setRotationPoint(0.0F, 0.0F, -1.4F);
        this.elbowRight.addBox(-3.0F, -3.0F, 0.0F, 6, 6, 5, 0.0F);
        this.calfRight = new AdvancedModelRenderer(this, 0, 75);
        this.calfRight.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.calfRight.addBox(-4.5F, 0.0F, -0.5F, 5, 12, 5, 0.0F);
        setRotateAngle(this.calfRight, 0.0F, 0.7853981633974483F, 0.0F);
        this.lowerArmRightJoint = new AdvancedModelRenderer(this, 0, 0);
        this.lowerArmRightJoint.setRotationPoint(15.0F, 0.0F, 0.0F);
        this.lowerArmRightJoint.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        setRotateAngle(this.lowerArmRightJoint, 0.7853981633974483F, 0.0F, 0.0F);
        this.kneeLeft = new AdvancedModelRenderer(this, 24, 80);
        this.kneeLeft.setRotationPoint(0.0F, 13.0F, 0.0F);
        this.kneeLeft.addBox(-3.0F, -1.7F, -3.0F, 6, 4, 6, 0.0F);
        setRotateAngle(this.kneeLeft, -0.5235987755982988F, -0.7853981633974483F, 0.0F);
        this.elbowLeftJoint = new AdvancedModelRenderer(this, 0, 0);
        this.elbowLeftJoint.setRotationPoint(15.0F, -1.6F, -1.7F);
        this.elbowLeftJoint.addBox(-3.0F, -3.0F, 0.0F, 0, 0, 0, 0.0F);
        setRotateAngle(this.elbowLeftJoint, -0.7853981633974483F, 0.0F, 0.0F);
        this.upperArmLeft = new AdvancedModelRenderer(this, 24, 40);
        this.upperArmLeft.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.upperArmLeft.addBox(-5.0F, -4.0F, -4.0F, 20, 8, 8, 0.0F);
        setRotateAngle(this.upperArmLeft, 0.7853981633974483F, 0.0F, 0.0F);
        this.handLeftJoint = new AdvancedModelRenderer(this, 0, 0);
        this.handLeftJoint.setRotationPoint(16.0F, 1.0F, 1.0F);
        this.handLeftJoint.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        setRotateAngle(this.handLeftJoint, 0.7853981633974483F, 0.0F, 0.0F);
        this.lowerArmLeft = new AdvancedModelRenderer(this, 86, 29);
        this.lowerArmLeft.setRotationPoint(0.0F, -1.5F, 0.0F);
        this.lowerArmLeft.addBox(0.0F, -2.0F, -2.0F, 15, 6, 6, 0.0F);
        setRotateAngle(this.lowerArmLeft, -0.7853981633974483F, 0.0F, 0.0F);
        this.shoulderRightJoint = new AdvancedModelRenderer(this, 0, 0);
        this.shoulderRightJoint.setRotationPoint(10.0F, 4.0F, 14.9F);
        this.shoulderRightJoint.addBox(-4.0F, -7.0F, -5.0F, 0, 0, 0, 0.0F);
        setRotateAngle(this.shoulderRightJoint, -1.0471975511965976F, 0.0F, 0.0F);
        this.groinBack = new AdvancedModelRenderer(this, 0, 92);
        this.groinBack.setRotationPoint(0.0F, 0.0F, 7.0F);
        this.groinBack.addBox(-5.0F, 0.0F, -1.0F, 10, 12, 2, 0.0F);
        setRotateAngle(this.groinBack, 0.17453292519943295F, 0.0F, 0.0F);
        this.thighLeftJoint2 = new AdvancedModelRenderer(this, 0, 0);
        this.thighLeftJoint2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.thighLeftJoint2.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        setRotateAngle(this.thighLeftJoint2, -0.8726646259971648F, 0.0F, 0.0F);
        this.helmet = new AdvancedModelRenderer(this, 32, 20);
        this.helmet.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.helmet.addBox(-4.0F, -10.0F, -4.0F, 8, 12, 8, 0.0F);
        this.lowerArmRight = new AdvancedModelRenderer(this, 86, 29);
        this.lowerArmRight.setRotationPoint(0.0F, 1.5F, 0.0F);
        this.lowerArmRight.addBox(0.0F, -4.0F, -4.0F, 15, 6, 6, 0.0F);
        setRotateAngle(this.lowerArmRight, -0.7853981633974483F, 0.0F, 0.0F);
        this.handLeft = new AdvancedModelRenderer(this, 98, 14);
        this.handLeft.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.handLeft.addBox(-2.0F, -4.0F, -2.0F, 8, 8, 7, 0.0F);
        setRotateAngle(this.handLeft, 0.0F, 0.7853981633974483F, 0.0F);
        this.calfLeftJoint = new AdvancedModelRenderer(this, 0, 0);
        this.calfLeftJoint.setRotationPoint(0.0F, 14.5F, 0.0F);
        this.calfLeftJoint.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        setRotateAngle(this.calfLeftJoint, 1.5707963267948966F, -0.7853981633974483F, 0.0F);
        this.chestJoint = new AdvancedModelRenderer(this, 0, 0);
        this.chestJoint.setRotationPoint(0.0F, -14.0F, 0.0F);
        this.chestJoint.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        setRotateAngle(this.chestJoint, 0.0F, -0.7853981633974483F, 0.0F);
        this.tuskRight2 = new AdvancedModelRenderer(this, 110, 97);
        this.tuskRight2.setRotationPoint(6.0F, 1.5F, 0.0F);
        this.tuskRight2.addBox(0.0F, -2.0F, -1.0F, 7, 2, 2, 0.0F);
        setRotateAngle(this.tuskRight2, 0.0F, 0.0F, -0.8726646259971648F);
        this.groinJoint = new AdvancedModelRenderer(this, 0, 0);
        this.groinJoint.setRotationPoint(0.0F, 6.0F, 0.0F);
        this.groinJoint.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        setRotateAngle(this.groinJoint, 0.0F, -0.7853981633974483F, 0.0F);
        this.upperArmRightJoint = new AdvancedModelRenderer(this, 0, 0);
        this.upperArmRightJoint.setRotationPoint(5.0F, 1.0F, 1.0F);
        this.upperArmRightJoint.addBox(0.0F, -4.0F, -4.0F, 0, 0, 0, 0.0F);
        this.axeBladeRight3 = new AdvancedModelRenderer(this, 56, 0);
        this.axeBladeRight3.setRotationPoint(17.7F, 2.3F, -0.01F);
        this.axeBladeRight3.addBox(-5.5F, 0.0F, -1.0F, 11, 17, 2, 0.0F);
        setRotateAngle(this.axeBladeRight3, 0.0F, 0.0F, 2.6179938779914944F);
        this.footRightJoint = new AdvancedModelRenderer(this, 0, 0);
        this.footRightJoint.setRotationPoint(-2.0F, 11.0F, 2.0F);
        this.footRightJoint.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        setRotateAngle(this.footRightJoint, 0.0F, -0.7853981633974483F, 0.0F);
        this.head = new AdvancedModelRenderer(this, 0, 0);
        this.head.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.head.addBox(-3.0F, -9.0F, -3.0F, 6, 10, 6, 0.0F);
        setRotateAngle(this.head, 0.0F, 0.7853981633974483F, 0.0F);
        this.hornLeft1 = new AdvancedModelRenderer(this, 12, 17);
        this.hornLeft1.setRotationPoint(-2.5F, -8.05F, -3.0F);
        this.hornLeft1.addBox(-1.5F, -1.5F, -8.0F, 3, 3, 8, 0.0F);
        setRotateAngle(this.hornLeft1, -0.3490658503988659F, 0.0F, 0.0F);
        this.thighRightJoint2 = new AdvancedModelRenderer(this, 0, 0);
        this.thighRightJoint2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.thighRightJoint2.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        setRotateAngle(this.thighRightJoint2, -0.8726646259971648F, 0.0F, 0.0F);
        this.thighRightJoint = new AdvancedModelRenderer(this, 0, 0);
        this.thighRightJoint.setRotationPoint(5.0F, 0.0F, 0.0F);
        this.thighRightJoint.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        setRotateAngle(this.thighRightJoint, 0.0F, -0.7853981633974483F, 0.0F);
        this.tuskLeft1 = new AdvancedModelRenderer(this, 13, 60);
        this.tuskLeft1.setRotationPoint(-2.5F, 0.5F, -3.0F);
        this.tuskLeft1.addBox(-1.5F, -1.5F, -6.0F, 3, 3, 6, 0.0F);
        setRotateAngle(this.tuskLeft1, 0.4363323129985824F, 0.0F, 0.0F);
        this.kneeRight = new AdvancedModelRenderer(this, 24, 80);
        this.kneeRight.setRotationPoint(0.0F, 13.0F, 0.0F);
        this.kneeRight.addBox(-3.0F, -1.7F, -3.0F, 6, 4, 6, 0.0F);
        setRotateAngle(this.kneeRight, -0.5235987755982988F, -0.7853981633974483F, 0.0F);
        this.axeBladeLeft1 = new AdvancedModelRenderer(this, 84, 0);
        this.axeBladeLeft1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.axeBladeLeft1.addBox(0.0F, -4.5F, -1.0F, 10, 8, 2, 0.0F);
        this.waist = new AdvancedModelRenderer(this, 64, 41);
        this.waist.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.waist.addBox(-8.0F, 0.0F, -8.0F, 16, 6, 16, 0.0F);
        setRotateAngle(this.waist, 0.0F, 0.7853981633974483F, 0.0F);
        this.hornLeft2 = new AdvancedModelRenderer(this, 30, 0);
        this.hornLeft2.setRotationPoint(-1.0F, 1.5F, -8.0F);
        this.hornLeft2.addBox(0.0F, -2.0F, -11.0F, 2, 2, 11, 0.0F);
        setRotateAngle(this.hornLeft2, -1.2217304763960306F, 0.0F, 0.0F);
        this.shoulderLeft = new AdvancedModelRenderer(this, 21, 56);
        this.shoulderLeft.mirror = true;
        this.shoulderLeft.setRotationPoint(0.0F, 0.0F, 1.0F);
        this.shoulderLeft.addBox(-4.0F, -7.0F, -7.5F, 15, 10, 13, 0.0F);
        setRotateAngle(this.shoulderLeft, 0.0F, 3.141592653589793F, 0.0F);
        this.axeBladeRight = new AdvancedModelRenderer(this, 0, 0);
        this.axeBladeRight.setRotationPoint(0.0F, -37.0F, 0.0F);
        this.axeBladeRight.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        setRotateAngle(this.axeBladeRight, 0.0F, -0.7853981633974483F, 0.0F);
        this.calfLeft = new AdvancedModelRenderer(this, 0, 75);
        this.calfLeft.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.calfLeft.addBox(-4.5F, 0.0F, -0.5F, 5, 12, 5, 0.0F);
        setRotateAngle(this.calfLeft, 0.0F, 0.7853981633974483F, 0.0F);
        this.elbowRightJoint = new AdvancedModelRenderer(this, 0, 0);
        this.elbowRightJoint.setRotationPoint(15.0F, 1.6F, 1.7F);
        this.elbowRightJoint.addBox(-3.0F, -3.0F, 0.0F, 0, 0, 0, 0.0F);
        setRotateAngle(this.elbowRightJoint, -0.7853981633974483F, 0.0F, 0.0F);
        this.axeBladeLeft = new AdvancedModelRenderer(this, 0, 0);
        this.axeBladeLeft.setRotationPoint(0.0F, -37.0F, 0.0F);
        this.axeBladeLeft.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        setRotateAngle(this.axeBladeLeft, 0.0F, -3.9269908169872414F, 0.0F);
        this.thighRight = new AdvancedModelRenderer(this, 26, 90);
        this.thighRight.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.thighRight.addBox(-3.5F, 0.0F, -3.5F, 7, 13, 7, 0.0F);
        setRotateAngle(this.thighRight, 0.0F, 0.7853981633974483F, 0.0F);
        this.shoulderRight = new AdvancedModelRenderer(this, 21, 56);
        this.shoulderRight.setRotationPoint(0.0F, 0.0F, 1.0F);
        this.shoulderRight.addBox(-4.0F, -7.0F, -5.5F, 15, 10, 13, 0.0F);
        this.axeBladeRight1 = new AdvancedModelRenderer(this, 84, 0);
        this.axeBladeRight1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.axeBladeRight1.addBox(0.0F, -4.5F, -1.0F, 10, 8, 2, 0.0F);
        this.hornRight2 = new AdvancedModelRenderer(this, 16, 44);
        this.hornRight2.setRotationPoint(8.0F, 1.5F, 0.0F);
        this.hornRight2.addBox(0.0F, -2.0F, -1.0F, 6, 2, 2, 0.0F);
        setRotateAngle(this.hornRight2, 0.0F, 0.0F, -1.2217304763960306F);
        this.thighLeftJoint = new AdvancedModelRenderer(this, 0, 0);
        this.thighLeftJoint.setRotationPoint(-5.0F, 0.0F, 0.0F);
        this.thighLeftJoint.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        setRotateAngle(this.thighLeftJoint, 0.0F, 0.7853981633974483F, 0.0F);
        this.axeBladeLeft3 = new AdvancedModelRenderer(this, 56, 0);
        this.axeBladeLeft3.setRotationPoint(17.7F, 2.3F, -0.01F);
        this.axeBladeLeft3.addBox(-5.5F, 0.0F, -1.0F, 11, 17, 2, 0.01F);
        setRotateAngle(this.axeBladeLeft3, 0.0F, 0.0F, 2.6179938779914944F);
        this.footLeftJoint = new AdvancedModelRenderer(this, 0, 0);
        this.footLeftJoint.setRotationPoint(-2.0F, 11.0F, 2.0F);
        this.footLeftJoint.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        setRotateAngle(this.footLeftJoint, 0.0F, -0.7853981633974483F, 0.0F);
        this.chest = new AdvancedModelRenderer(this, 36, 92);
        this.chest.setRotationPoint(0.0F, 0.0F, -9.0F);
        this.chest.addBox(-14.0F, 0.0F, 0.0F, 28, 18, 18, 0.0F);
        setRotateAngle(this.chest, 0.7853981633974483F, 0.0F, 0.0F);
        this.tuskLeft2 = new AdvancedModelRenderer(this, 110, 101);
        this.tuskLeft2.setRotationPoint(-1.0F, 1.5F, -6.0F);
        this.tuskLeft2.addBox(0.0F, -2.0F, -7.0F, 2, 2, 7, 0.0F);
        setRotateAngle(this.tuskLeft2, -0.8726646259971648F, 0.0F, 0.0F);
        this.upperArmLeftJoint = new AdvancedModelRenderer(this, 0, 0);
        this.upperArmLeftJoint.setRotationPoint(5.0F, 1.0F, -1.0F);
        this.upperArmLeftJoint.addBox(0.0F, -4.0F, -4.0F, 0, 0, 0, 0.0F);
        this.axeBase = new AdvancedModelRenderer(this, 0, 0);
        this.axeBase.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.axeBase.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        this.handRightJoint = new AdvancedModelRenderer(this, 0, 0);
        this.handRightJoint.setRotationPoint(16.0F, -1.0F, -1.0F);
        this.handRightJoint.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        setRotateAngle(this.handRightJoint, 0.7853981633974483F, 0.0F, 0.0F);
        this.footLeft = new AdvancedModelRenderer(this, 48, 79);
        this.footLeft.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.footLeft.addBox(-3.0F, 0.0F, -8.0F, 6, 3, 10, 0.0F);
        setRotateAngle(this.footLeft, -0.6981317007977318F, 0.0F, 0.0F);
        this.handRight = new AdvancedModelRenderer(this, 98, 14);
        this.handRight.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.handRight.addBox(-2.0F, -4.0F, -2.0F, 8, 8, 7, 0.0F);
        setRotateAngle(this.handRight, 0.0F, 0.7853981633974483F, 0.0F);
        this.lowerArmLeftJoint = new AdvancedModelRenderer(this, 0, 0);
        this.lowerArmLeftJoint.setRotationPoint(15.0F, 0.0F, 0.0F);
        this.lowerArmLeftJoint.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        setRotateAngle(this.lowerArmLeftJoint, 0.7853981633974483F, 0.0F, 0.0F);
        this.hornRight1 = new AdvancedModelRenderer(this, 34, 13);
        this.hornRight1.setRotationPoint(3.0F, -8.05F, 2.5F);
        this.hornRight1.addBox(0.0F, -1.5F, -1.5F, 8, 3, 3, 0.0F);
        setRotateAngle(this.hornRight1, 0.0F, 0.0F, -0.3490658503988659F);
        this.tuskRight1 = new AdvancedModelRenderer(this, 64, 63);
        this.tuskRight1.setRotationPoint(3.0F, 0.5F, 2.5F);
        this.tuskRight1.addBox(0.0F, -1.5F, -1.5F, 6, 3, 3, 0.0F);
        setRotateAngle(this.tuskRight1, 0.0F, 0.0F, 0.4363323129985824F);
        this.footRight = new AdvancedModelRenderer(this, 48, 79);
        this.footRight.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.footRight.addBox(-3.0F, 0.0F, -8.0F, 6, 3, 10, 0.0F);
        setRotateAngle(this.footRight, -0.6981317007977318F, 0.0F, 0.0F);
        this.thighLeft = new AdvancedModelRenderer(this, 26, 90);
        this.thighLeft.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.thighLeft.addBox(-3.5F, 0.0F, -3.5F, 7, 13, 7, 0.0F);
        setRotateAngle(this.thighLeft, 0.0F, 0.7853981633974483F, 0.0F);
        this.groinFront = new AdvancedModelRenderer(this, 0, 92);
        this.groinFront.setRotationPoint(0.0F, 0.0F, -7.0F);
        this.groinFront.addBox(-5.0F, 0.0F, -1.0F, 10, 12, 2, 0.0F);
        setRotateAngle(this.groinFront, -0.17453292519943295F, 0.0F, 0.0F);
        this.elbowLeft = new AdvancedModelRenderer(this, 70, 24);
        this.elbowLeft.setRotationPoint(0.0F, 0.0F, -3.6F);
        this.elbowLeft.addBox(-3.0F, -3.0F, 0.0F, 6, 6, 5, 0.0F);
        this.axeBladeRight2 = new AdvancedModelRenderer(this, 56, 0);
        this.axeBladeRight2.mirror = true;
        this.axeBladeRight2.setRotationPoint(17.7F, -3.2F, 0.01F);
        this.axeBladeRight2.addBox(-5.5F, 0.0F, -1.0F, 11, 17, 2, 0.0F);
        setRotateAngle(this.axeBladeRight2, 0.0F, 0.0F, 0.5235987755982988F);
        this.shoulderLeftJoint = new AdvancedModelRenderer(this, 0, 0);
        this.shoulderLeftJoint.setRotationPoint(-10.0F, 4.0F, 14.9F);
        this.shoulderLeftJoint.addBox(-4.0F, -7.0F, -5.0F, 0, 0, 0, 0.0F);
        setRotateAngle(this.shoulderLeftJoint, -1.0471975511965976F, 0.0F, 0.0F);
        this.stomachJoint = new AdvancedModelRenderer(this, 0, 0);
        this.stomachJoint.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.stomachJoint.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        setRotateAngle(this.stomachJoint, 0.2617993877991494F, -0.7853981633974483F, 0.0F);
        this.axeBladeLeft2 = new AdvancedModelRenderer(this, 56, 0);
        this.axeBladeLeft2.mirror = true;
        this.axeBladeLeft2.setRotationPoint(17.7F, -3.2F, 0.01F);
        this.axeBladeLeft2.addBox(-5.5F, 0.0F, -1.0F, 11, 17, 2, 0.0F);
        setRotateAngle(this.axeBladeLeft2, 0.0F, 0.0F, 0.5235987755982988F);
        this.calfRightJoint = new AdvancedModelRenderer(this, 0, 0);
        this.calfRightJoint.setRotationPoint(0.0F, 14.5F, 0.0F);
        this.calfRightJoint.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        setRotateAngle(this.calfRightJoint, 1.5707963267948966F, -0.7853981633974483F, 0.0F);
        this.upperArmRight = new AdvancedModelRenderer(this, 24, 40);
        this.upperArmRight.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.upperArmRight.addBox(-5.0F, -4.0F, -4.0F, 20, 8, 8, 0.0F);
        setRotateAngle(this.upperArmRight, 0.7853981633974483F, 0.0F, 0.0F);
        this.groin = new AdvancedModelRenderer(this, 0, 106);
        this.groin.setRotationPoint(0.0F, 4.0F, 0.0F);
        this.groin.addBox(-3.0F, -5.5F, -5.5F, 6, 11, 11, 0.0F);
        setRotateAngle(this.groin, -0.7853981633974483F, 0.0F, 0.0F);
        this.neck = new AdvancedModelRenderer(this, 0, 0);
        this.neck.setRotationPoint(0.0F, -1.4F, 15.1F);
        this.neck.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        setRotateAngle(this.neck, -1.0471975511965976F, 0.0F, 0.0F);
        this.waistBendController = new AdvancedModelRenderer(this, 0, 0);
        this.waistBendController.setRotationPoint(0.0F, 0F, 0F);
        this.waistBendController.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        setRotateAngle(this.waistBendController, 0.0F, 0.0F, 0.0F);
        this.swordJoint = new AdvancedModelRenderer(this, 0, 0);
        this.swordJoint.setRotationPoint(0F, -3F, 10F);
        this.swordJoint.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        setRotateAngle(this.swordJoint, 0.0F, -0.7853981633974483F, 0.0F);
        this.sword = new AdvancedModelRenderer(this, 82, 10);
        this.sword.setRotationPoint(0F, 0F, 0F);
//        this.sword.add3DTexture(-11f, 0, -11f, 11, 11);
        setRotateAngle(this.sword, 0.0F, 0F, 0.0F);
        this.rootBox = new AdvancedModelRenderer(this, 0, 0);
        this.rootBox.setRotationPoint(0.0F, -1.0F, 0.0F);
        this.rootBox.addBox(0F, 0F, 0F, 0, 0, 0, 0.0F);
        setRotateAngle(this.rootBox, 0.0F, 0F, 0.0F);
        this.eyeLeft = new AdvancedModelRenderer(this, 0, 0);
        this.eyeLeft.setRotationPoint(-4, -4, 4);
        this.eyeLeft.addBox(-1, -0.5F, 0, 2, 1, 0, 0.0F);
        this.eyeRight = new AdvancedModelRenderer(this, 0, 0);
        this.eyeRight.setRotationPoint(4, -4, 4);
        this.eyeRight.addBox(-1, -0.5F, 0, 2, 1, 0, 0.0F);
        this.eyeRightSocket = new AdvancedModelRenderer(this, 0, 0);
        this.eyeLeftSocket = new AdvancedModelRenderer(this, 0, 0);

        this.rootBox.addChild(this.waist);
        this.waist.addChild(this.groinJoint);
        this.upperArmRightJoint.addChild(this.upperArmRight);
        this.helmet.addChild(this.tuskRight1);
        this.shoulderRight.addChild(this.upperArmRightJoint);
        this.axeBase.addChild(this.axeHandle);
        this.footRightJoint.addChild(this.footRight);
        this.head.addChild(this.helmet);
        this.calfRight.addChild(this.footRightJoint);
        this.axeBladeLeft.addChild(this.axeBladeLeft1);
        this.thighRightJoint.addChild(this.thighRightJoint2);
        this.axeBladeRight.addChild(this.axeBladeRight2);
        this.elbowLeftJoint.addChild(this.elbowLeft);
        this.lowerArmRight.addChild(this.handRightJoint);
        this.upperArmLeft.addChild(this.elbowLeftJoint);
        this.lowerArmLeft.addChild(this.handLeftJoint);
        this.axeBladeLeft.addChild(this.axeBladeLeft3);
        this.axeBladeLeft.addChild(this.axeBladeLeft2);
        this.axeHandle.addChild(this.axeBladeLeft);
        this.shoulderLeftJoint.addChild(this.shoulderLeft);
        this.thighLeftJoint.addChild(this.thighLeftJoint2);
        this.upperArmRight.addChild(this.lowerArmRightJoint);
        this.chest.addChild(this.shoulderRightJoint);
        this.upperArmLeftJoint.addChild(this.upperArmLeft);
        this.calfLeft.addChild(this.footLeftJoint);
        this.handLeftJoint.addChild(this.handLeft);
        this.footLeftJoint.addChild(this.footLeft);
        this.thighLeft.addChild(this.calfLeftJoint);
        this.tuskLeft1.addChild(this.tuskLeft2);
        this.stomach.addChild(this.chestJoint);
        this.groinJoint.addChild(this.thighLeftJoint);
        this.handRight.addChild(this.axeBase);
        this.chestJoint.addChild(this.chest);
        this.calfLeftJoint.addChild(this.calfLeft);
        this.chest.addChild(this.shoulderLeftJoint);
        this.elbowRightJoint.addChild(this.elbowRight);
        this.thighRightJoint2.addChild(this.thighRight);
        this.groinJoint.addChild(this.groinFront);
        this.tuskRight1.addChild(this.tuskRight2);
        this.handRightJoint.addChild(this.handRight);
        this.helmet.addChild(this.hornLeft1);
        this.shoulderRightJoint.addChild(this.shoulderRight);
        this.stomachJoint.addChild(this.stomach);
        this.axeBladeRight.addChild(this.axeBladeRight3);
        this.lowerArmLeftJoint.addChild(this.lowerArmLeft);
        this.calfRightJoint.addChild(this.calfRight);
        this.groinJoint.addChild(this.groinBack);
        this.hornLeft1.addChild(this.hornLeft2);
        this.thighRight.addChild(this.calfRightJoint);
        this.groinJoint.addChild(this.thighRightJoint);
        this.helmet.addChild(this.tuskLeft1);
        this.thighRight.addChild(this.kneeRight);
        this.thighLeft.addChild(this.kneeLeft);
        this.thighLeftJoint2.addChild(this.thighLeft);
        this.chest.addChild(this.neck);
        this.neck.addChild(this.head);
        this.upperArmRight.addChild(this.elbowRightJoint);
        this.hornRight1.addChild(this.hornRight2);
        this.lowerArmRightJoint.addChild(this.lowerArmRight);
        this.waist.addChild(this.stomachJoint);
        this.axeHandle.addChild(this.axeBladeRight);
        this.shoulderLeft.addChild(this.upperArmLeftJoint);
        this.helmet.addChild(this.hornRight1);
        this.axeBladeRight.addChild(this.axeBladeRight1);
        this.upperArmLeft.addChild(this.lowerArmLeftJoint);
        this.head.addChild(this.eyeLeft);
        this.head.addChild(this.eyeRight);
        this.groin.addChild(this.groinJoint);
        this.stomach.addChild(this.swordJoint);
        this.swordJoint.addChild(this.sword);
        this.head.addChild(this.eyeRightSocket);
        this.head.addChild(this.eyeLeftSocket);

        if (eyesLayer) {
            for (BasicModelRenderer box : this.boxList) {
                if (box instanceof AdvancedModelRenderer) {
                    ((AdvancedModelRenderer) box).setIsHidden(true);
                }
            }
            this.eyeLeft.setIsHidden(false);
            this.eyeRight.setIsHidden(false);
        }

        //Corrections
        this.groin.rotateAngleY -= 45 * Math.PI / 180;
        this.eyeRight.rotationPointZ -= 7.2;
        this.eyeLeft.rotationPointZ -= 4;
        this.eyeRight.rotationPointX -= 4;
        this.eyeLeft.rotationPointX += 7.2;
        this.eyeRight.rotationPointY -= 1;
        this.eyeLeft.rotationPointY -= 1;
        this.eyeLeft.rotateAngleY -= Math.PI / 2f;

        this.eyeLeftSocket.setRotationPoint(this.eyeLeft.rotationPointX, this.eyeLeft.rotationPointY, this.eyeLeft.rotationPointZ);
        setRotateAngle(this.eyeLeft, this.eyeLeft.rotateAngleX, this.eyeLeft.rotateAngleY, this.eyeLeft.rotateAngleZ);
        this.eyeRightSocket.setRotationPoint(this.eyeRight.rotationPointX, this.eyeRight.rotationPointY, this.eyeRight.rotationPointZ);
        setRotateAngle(this.eyeRight, this.eyeRight.rotateAngleX, this.eyeRight.rotateAngleY, this.eyeRight.rotateAngleZ);

        this.updateDefaultPose();
    }

    @Override
    public void render(MatrixStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        this.rootBox.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }

    public void setDefaultAngles(EntityWroughtnaut entity, float limbSwing, float limbSwingAmount, float headYaw, float headPitch, float delta) {
        this.resetToDefaultPose();

        if (entity.isActive()) {
            this.eyeLeft.showModel = true;
            this.eyeRight.showModel = true;
        } else {
            this.eyeLeft.showModel = false;
            this.eyeRight.showModel = false;
        }

        if (entity.getAnimation() != EntityWroughtnaut.ACTIVATE_ANIMATION && entity.getAnimation() != EntityWroughtnaut.DEACTIVATE_ANIMATION) {
            if (entity.isActive() || entity.getAnimation() == EntityWroughtnaut.DIE_ANIMATION) {
                this.shoulderLeft.rotateAngleZ -= 0.4;
                this.shoulderRight.rotateAngleZ += 0.35;
                this.shoulderLeft.rotateAngleY -= 0.3;
                this.shoulderRight.rotateAngleY += 0.2;
                this.upperArmLeft.rotateAngleZ += 0.5;
                this.upperArmRight.rotateAngleZ += 0.5;
                this.upperArmLeft.rotateAngleY -= 0.5;
                this.upperArmRight.rotateAngleY += 0.3;
                this.lowerArmLeft.rotateAngleZ += 1.5;
                this.lowerArmRight.rotateAngleZ -= 1.3;
                this.lowerArmLeft.rotateAngleY += 0.3;
                this.lowerArmRight.rotateAngleY += 0.7;
                this.handRight.rotateAngleZ += 0.3;
                this.handRight.rotateAngleX -= 0.2;
                this.handLeftJoint.rotateAngleX += 1.6;
                this.handLeftJoint.rotateAngleZ -= 0.5;
                this.axeHandle.rotateAngleY += 0.8;

                float y = MathHelper.wrapDegrees(headYaw) / (180f / (float) Math.PI);
                this.head.rotateAngleY += MathHelper.clamp(y, (float) -Math.PI / 4, (float) Math.PI / 4);
                this.neck.rotateAngleX += (headPitch > 0.0F ? headPitch * 1.4F : headPitch) / (180f / (float) Math.PI);
            } else {
                this.shoulderLeft.rotateAngleZ -= 0.4;
                this.shoulderRight.rotateAngleZ += 0.4;
                this.shoulderLeft.rotateAngleY -= 0.4;
                this.shoulderRight.rotateAngleY += 0.4;
                this.upperArmLeft.rotateAngleZ += 0.5;
                this.upperArmRight.rotateAngleZ += 0.5;
                this.upperArmLeft.rotateAngleY -= 0.5;
                this.upperArmRight.rotateAngleY += 0.5;
                this.lowerArmLeft.rotateAngleZ += 1.5;
                this.lowerArmRight.rotateAngleZ -= 1.5;
                this.lowerArmLeft.rotateAngleY += 1;
                this.lowerArmRight.rotateAngleY += 1;
                this.axeBase.rotationPointY += 30;
                this.axeBase.rotationPointZ += 3.5;
                this.axeBase.rotateAngleY += 0.35;
                this.handRight.rotateAngleX -= 0.9;
                this.handRightJoint.rotateAngleZ -= 0.88;
                this.handRightJoint.rotateAngleX -= 0.2;
                this.handLeftJoint.rotateAngleY -= 0.3;
                this.handLeftJoint.rotateAngleX += 0.8;
                this.neck.rotateAngleX += 0.5;
                this.rootBox.rotationPointY -= 5;
                this.thighRightJoint.rotateAngleX += 0.35;
                this.thighRightJoint.rotateAngleY += 0.5;
                this.thighLeftJoint.rotateAngleX += 0.35;
                this.thighLeftJoint.rotateAngleY -= 0.5;
                this.calfRightJoint.rotateAngleX -= 0.6;
                this.calfLeftJoint.rotateAngleX -= 0.6;
                this.footLeft.rotateAngleX += 0.25;
                this.footRight.rotateAngleX += 0.25;
            }
        }

        float frame = entity.frame + delta;
        limbSwing = (float) ((4 * Math.PI * frame - 30 * MathHelper.sin((float) (0.1 * Math.PI * (frame - 9))) - 27 * Math.PI) / (4 * Math.PI)) + 5f;
        float walkTimerInterp = entity.walkAnim.getPrevTimer() + (entity.walkAnim.getTimer() - entity.walkAnim.getPrevTimer()) * delta;
        limbSwingAmount = (float) Math.pow(MathHelper.sin((float) (walkTimerInterp * Math.PI * 0.05)), 2);

        float globalSpeed = (float) (Math.PI * 0.05);
        float globalDegree = 0.8F;
        float height = 2F;

        //groinJoint.rotationPointY -= 1 * limbSwingAmount;
        this.waist.rotationPointZ -= limbSwingAmount * 3f * Math.pow(Math.sin(globalSpeed * (frame - 13)), 2);
        this.bob(this.waist, 2F * globalSpeed, 1 * height, false, limbSwing, limbSwingAmount);
        this.swing(this.waist, globalSpeed, 0.3F * globalDegree, false, 0, 0, limbSwing, limbSwingAmount);
        this.swing(this.stomachJoint, globalSpeed, 0.6F * globalDegree, true, 0, 0, limbSwing, limbSwingAmount);
        this.swing(this.head, globalSpeed, 0.3F * globalDegree, false, 0, 0, limbSwing, limbSwingAmount);

        this.swing(this.thighLeftJoint, globalSpeed, 0.4F * globalDegree, true, 0, 0.5F, limbSwing, limbSwingAmount);
        this.walk(this.thighLeftJoint, globalSpeed, 0.4F * globalDegree, false, 0, 0.3F * globalDegree, limbSwing, limbSwingAmount);
        this.walk(this.calfLeftJoint, globalSpeed, 0.5F * globalDegree, false, -2.2F, 0.1F * globalDegree, limbSwing, limbSwingAmount);
        this.walk(this.footLeftJoint, globalSpeed, 0.4F * globalDegree, false, -2.1F, 0.26F * globalDegree, limbSwing, limbSwingAmount);

        this.swing(this.thighRightJoint, globalSpeed, 0.4F * globalDegree, true, 0, -0.5F, limbSwing, limbSwingAmount);
        this.walk(this.thighRightJoint, globalSpeed, 0.4F * globalDegree, true, 0, -0.3F * globalDegree, limbSwing, limbSwingAmount);
        this.walk(this.calfRightJoint, globalSpeed, 0.5F * globalDegree, true, -2.2F, -0.1F * globalDegree, limbSwing, limbSwingAmount);
        this.walk(this.footRightJoint, globalSpeed, 0.4F * globalDegree, true, -2.1F, -0.26F * globalDegree, limbSwing, limbSwingAmount);

        this.walk(this.groinFront, 2F * globalSpeed, 0.2F * 0.8F, true, -0.5F, 0.1F, limbSwing, limbSwingAmount);
        this.walk(this.groinBack, 2F * globalSpeed, 0.2F * 0.8F, false, -0.5F, 0.1F, limbSwing, limbSwingAmount);
        this.walk(this.neck, 2F * globalSpeed, 0.1F * 0.8F, true, -0.5F, 0.1F, limbSwing, limbSwingAmount);

        this.flap(this.shoulderLeft, 2F * globalSpeed, 0.05F * 0.8F, false, -0.5F, 0F, limbSwing, limbSwingAmount);
        this.flap(this.shoulderRight, 2F * globalSpeed, 0.05F * 0.8F, true, -0.5F, 0F, limbSwing, limbSwingAmount);
        this.swing(this.shoulderLeft, globalSpeed, 0.1F * 0.8F, true, 0F, -0.3F, limbSwing, limbSwingAmount);
        this.swing(this.shoulderRight, globalSpeed, 0.1F * 0.8F, true, 0F, -0.3F, limbSwing, limbSwingAmount);
        this.flap(this.upperArmLeftJoint, 2F * globalSpeed, 0.05F * 0.8F, true, -1F, 0F, limbSwing, limbSwingAmount);
        this.flap(this.upperArmRightJoint, 2F * globalSpeed, 0.05F * 0.8F, true, -1F, 0F, limbSwing, limbSwingAmount);
        this.flap(this.handLeft, 2 * globalSpeed, 0.2F, false, -0.5F, 0.5F, limbSwing, limbSwingAmount);
        this.walk(this.lowerArmLeftJoint, 2 * globalSpeed, 0.1F, true, -0.5F, 0, limbSwing, limbSwingAmount);
        this.walk(this.handLeft, 2 * globalSpeed, 0.2F, false, -0.5F, 0.6F, limbSwing, limbSwingAmount);
        this.lowerArmLeftJoint.rotateAngleY -= 0.65F * limbSwingAmount;

//        sword.rotationPointZ += 4;
//        sword.rotationPointX -= 5;
//        sword.rotationPointY -= 3;
        this.swordJoint.rotationPointX += 2;
//        swordJoint.rotationPointY -= 5;
        this.swordJoint.rotationPointZ -= 3;
        this.swordJoint.rotateAngleY += 1.3;
        this.swordJoint.rotateAngleX -= 0.5;
        this.swordJoint.rotateAngleZ -= 0.5;
//
//        sword.rotateAngleY += Math.PI / 2;
        this.sword.rotationPointX -= 8;
        this.sword.rotationPointZ -= 5.5;
        this.sword.rotationPointY -= 5;

        this.sword.setScale(1.7f);
    }

    @Override
    protected void animate(EntityWroughtnaut entity, float limbSwing, float limbSwingAmount, float headYaw, float headPitch, float delta) {
        this.setDefaultAngles(entity, limbSwing, limbSwingAmount, headYaw, headPitch, delta);

        float entityFrame = entity.frame + delta;
        if (entity.getAnimation() == EntityWroughtnaut.ATTACK_ANIMATION) {
            if (!entity.swingDirection) {
                this.animator.setAnimation(EntityWroughtnaut.ATTACK_ANIMATION);
                this.animator.setStaticKeyframe(6);
                this.animator.startKeyframe(15);
                this.animator.rotate(this.stomachJoint, -0.2F, 0.5F, 0);
                this.animator.rotate(this.waist, 0, 0.5F, -0.2F);
                this.animator.move(this.waist, -3, 1F, 1);
                this.animator.rotate(this.head, 0F, -0.8F, 0);
                this.animator.rotate(this.neck, 0F, 0F, 0);

                this.animator.rotate(this.shoulderRight, 0, 0.1F, 0);
                this.animator.rotate(this.upperArmRightJoint, -0.5F, 0.2F, 0);
                this.animator.rotate(this.lowerArmRightJoint, 0.2F, 0.3F, 0);
                this.animator.rotate(this.handRight, -0.5F, 0.3F, 0.1F);
                this.animator.rotate(this.axeHandle, 0, 0.5F, 0);

                this.animator.rotate(this.shoulderLeft, 0, 0F, 0);
                this.animator.rotate(this.upperArmLeftJoint, 0.2F, 0F, 0);
                this.animator.rotate(this.lowerArmLeftJoint, 0.1F, -0.3F, 0);
                this.animator.rotate(this.handLeft, 0F, 0.5F, -0.3F);

                this.animator.rotate(this.thighRightJoint, -0.7F, 0, 0);
                this.animator.rotate(this.thighRightJoint2, 0F, 0.2F, 0.5F);
                this.animator.rotate(this.calfRightJoint, 0.3F, 0, 0);

                this.animator.rotate(this.thighLeftJoint, -0.2F, 0, 0);
                this.animator.rotate(this.thighLeftJoint2, 0F, -0.5F, -0.05F);
                this.animator.rotate(this.calfLeftJoint, 0.1F, 0, 0);
                this.animator.rotate(this.footLeftJoint, -0.1F, 0, 0);
                this.animator.endKeyframe();

                this.animator.setStaticKeyframe(5);

                this.animator.startKeyframe(6);
                this.animator.rotate(this.stomachJoint, 0.3F, -1.3F, 0);
                this.animator.rotate(this.waist, 0, 0.5F, 0F);
                this.animator.move(this.waist, 2, 2, -7);
                this.animator.rotate(this.head, 0F, 0.8F, 0);
                this.animator.move(this.waistBendController, 7, 0, 0);

                this.animator.rotate(this.shoulderRight, 0, -0.6F, 0);
                this.animator.rotate(this.upperArmRightJoint, -0.8F, -0.9F, 0);
                this.animator.rotate(this.lowerArmRightJoint, 0.4F, 0.9F, 0F);
                this.animator.rotate(this.handRight, -0.8F, 0F, 0F);
                this.animator.rotate(this.axeHandle, 0, 1.2F, 0);

                this.animator.rotate(this.shoulderLeft, 0, -0.7F, 0);
                this.animator.move(this.shoulderLeft, 1, 0F, 0);
                this.animator.rotate(this.upperArmLeftJoint, 0.2F, -0.5F, 0);
                this.animator.move(this.upperArmLeft, 5F, 2F, 5F);
                this.animator.rotate(this.lowerArmLeftJoint, -0.5F, -0.3F, -0.7F);
                this.animator.move(this.lowerArmLeft, 0F, 2F, 0F);
                this.animator.rotate(this.handLeft, 2F, -0.3F, 0.5F);
                this.animator.rotate(this.handLeftJoint, 0F, -0.5F, -0.5F);

                this.animator.rotate(this.thighRightJoint, -0.4F, 0, 0);
                this.animator.rotate(this.thighRightJoint2, 0F, 0.2F, 0);
                this.animator.rotate(this.calfRightJoint, -0.1F, 0, 0);
                this.animator.rotate(this.footRightJoint, 0.5F, 0, 0);

                this.animator.rotate(this.thighLeftJoint, 0F, 0, 0);
                this.animator.rotate(this.thighLeftJoint2, 0F, -0.3F, 0.5F);
                this.animator.rotate(this.calfLeftJoint, -0.1F, 0, 0);
                this.animator.rotate(this.footLeftJoint, 0F, 0, 0);

                this.animator.endKeyframe();
                this.animator.setStaticKeyframe(8);
                this.animator.resetKeyframe(10);
                float frame = this.waistBendController.rotationPointX;
                if (entity.getAnimationTick() <= 33) {
                    this.stomachJoint.rotateAngleX += 0.06 * -frame * (frame - 7);
                    this.neck.rotateAngleX -= 0.06 * -frame * (frame - 7);
                }
            } else {
                this.animator.setAnimation(EntityWroughtnaut.ATTACK_ANIMATION);
                this.animator.setStaticKeyframe(6);
                this.animator.startKeyframe(15);
                this.animator.rotate(this.stomachJoint, 0.2F, -0.5F, 0);
                this.animator.rotate(this.waist, 0, -0.5F, 0.2F);
                this.animator.move(this.waist, 3, 1F, -1);
                this.animator.rotate(this.head, 0F, 0.8F, 0);
                this.animator.rotate(this.neck, 0F, 0F, 0);

                this.animator.rotate(this.shoulderRight, 0, -0.6F, 0);
                this.animator.rotate(this.upperArmRightJoint, -0.8F, -0.9F, 0);
                this.animator.rotate(this.lowerArmRightJoint, 0.4F, 0.9F, 0F);
                this.animator.rotate(this.handRight, -0.8F, 0F, 0F);
                this.animator.rotate(this.axeHandle, 0, 1.2F, 0);

                this.animator.rotate(this.shoulderLeft, 0, -0.7F, 0);
                this.animator.move(this.shoulderLeft, 1, 0F, 0);
                this.animator.rotate(this.upperArmLeftJoint, 0.2F, -0.5F, 0);
                this.animator.move(this.upperArmLeft, 5F, 2F, 5F);
                this.animator.rotate(this.lowerArmLeftJoint, -0.5F, -0.3F, -0.7F);
                this.animator.move(this.lowerArmLeft, 0F, 3F, 0F);
                this.animator.rotate(this.handLeft, 2F, -0.3F, 0.5F);
                this.animator.rotate(this.handLeftJoint, 0F, -0.5F, -0.5F);

                this.animator.rotate(this.thighLeftJoint, -0.7F, 0, 0);
                this.animator.rotate(this.thighLeftJoint2, 0F, -0.2F, -0.5F);
                this.animator.rotate(this.calfLeftJoint, 0.3F, 0, 0);

                this.animator.rotate(this.thighRightJoint, -0.2F, 0, 0);
                this.animator.rotate(this.thighRightJoint2, 0F, 0.5F, 0.05F);
                this.animator.rotate(this.calfRightJoint, 0.1F, 0, 0);
                this.animator.rotate(this.footRightJoint, -0.1F, 0, 0);
                this.animator.endKeyframe();

                this.animator.setStaticKeyframe(5);

                this.animator.startKeyframe(6);
                this.animator.rotate(this.stomachJoint, 0.3F, 1.3F, 0);
                this.animator.rotate(this.waist, 0, -0.5F, 0F);
                this.animator.move(this.waist, -2, 2, -7);
                this.animator.rotate(this.head, 0F, -0.8F, 0);
                this.animator.move(this.waistBendController, 7, 0, 0);

                this.animator.rotate(this.shoulderRight, 0, 0.1F, 0);
                this.animator.rotate(this.upperArmRightJoint, -0.5F, 0.2F, 0);
                this.animator.rotate(this.lowerArmRightJoint, 0.2F, 0.3F, 0);
                this.animator.rotate(this.handRight, -0.5F, 0.3F, 0.1F);
                this.animator.rotate(this.axeHandle, 0, 0.5F, 0);

                this.animator.rotate(this.shoulderLeft, 0, 0F, 0);
                this.animator.rotate(this.upperArmLeftJoint, 0.2F, 0F, 0);
                this.animator.rotate(this.lowerArmLeftJoint, 0.1F, -0.3F, 0);
                this.animator.rotate(this.handLeft, 0F, 0.5F, -0.3F);

                this.animator.rotate(this.thighLeftJoint, -0.4F, 0, 0);
                this.animator.rotate(this.thighLeftJoint2, 0F, -0.2F, 0);
                this.animator.rotate(this.calfLeftJoint, -0.1F, 0, 0);
                this.animator.rotate(this.footLeftJoint, 0.5F, 0, 0);

                this.animator.rotate(this.thighRightJoint, 0F, 0, 0);
                this.animator.rotate(this.thighRightJoint2, 0F, 0.3F, -0.5F);
                this.animator.rotate(this.calfRightJoint, -0.1F, 0, 0);
                this.animator.rotate(this.footRightJoint, 0F, 0, 0);

                this.animator.endKeyframe();
                this.animator.setStaticKeyframe(8);
                this.animator.resetKeyframe(10);
                float frame = this.waistBendController.rotationPointX;
                if (entity.getAnimationTick() <= 33) {
                    this.stomachJoint.rotateAngleX += 0.06 * -frame * (frame - 7);
                    this.neck.rotateAngleX -= 0.06 * -frame * (frame - 7);
                }
            }
        } else if (entity.getAnimation() == EntityWroughtnaut.ATTACK_TWICE_ANIMATION) {
            if (!entity.swingDirection) {
                this.animator.setAnimation(EntityWroughtnaut.ATTACK_TWICE_ANIMATION);

                this.animator.startKeyframe(0);
                this.animator.rotate(this.stomachJoint, 0.3F, -1.3F, 0);
                this.animator.rotate(this.waist, 0, 0.5F, 0F);
                this.animator.move(this.waist, 2, 2, -7);
                this.animator.rotate(this.head, 0F, 0.8F, 0);

                this.animator.rotate(this.shoulderRight, 0, -0.6F, 0);
                this.animator.rotate(this.upperArmRightJoint, -0.8F, -0.9F, 0);
                this.animator.rotate(this.lowerArmRightJoint, 0.4F, 0.9F, 0F);
                this.animator.rotate(this.handRight, -0.8F, 0F, 0F);
                this.animator.rotate(this.axeHandle, 0, 1.2F, 0);

                this.animator.rotate(this.shoulderLeft, 0, -0.7F, 0);
                this.animator.move(this.shoulderLeft, 1, 0F, 0);
                this.animator.rotate(this.upperArmLeftJoint, 0.2F, -0.5F, 0);
                this.animator.move(this.upperArmLeft, 5F, 2F, 5F);
                this.animator.rotate(this.lowerArmLeftJoint, -0.5F, -0.3F, -0.7F);
                this.animator.move(this.lowerArmLeft, 0F, 2F, 0F);
                this.animator.rotate(this.handLeft, 2F, -0.3F, 0.5F);
                this.animator.rotate(this.handLeftJoint, 0F, -0.5F, -0.5F);

                this.animator.rotate(this.thighRightJoint, -0.4F, 0, 0);
                this.animator.rotate(this.thighRightJoint2, 0F, 0.2F, 0);
                this.animator.rotate(this.calfRightJoint, -0.1F, 0, 0);
                this.animator.rotate(this.footRightJoint, 0.5F, 0, 0);

                this.animator.rotate(this.thighLeftJoint, 0F, 0, 0);
                this.animator.rotate(this.thighLeftJoint2, 0F, -0.3F, 0.5F);
                this.animator.rotate(this.calfLeftJoint, -0.1F, 0, 0);
                this.animator.rotate(this.footLeftJoint, 0F, 0, 0);
                this.animator.endKeyframe();

                this.animator.setStaticKeyframe(12);

                this.animator.startKeyframe(6);
                this.animator.rotate(this.stomachJoint, 0.3F, 0.5F, 0);
                this.animator.rotate(this.waist, 0, 0.5F, 0F);
                this.animator.move(this.waist, 2, 2, -7);
                this.animator.rotate(this.head, 0F, -0.8F, 0);
                this.animator.move(this.waistBendController, 7, 0, 0);

                this.animator.rotate(this.shoulderRight, 0, 0.1F, 0);
                this.animator.rotate(this.upperArmRightJoint, -0.5F, 0.2F, 0);
                this.animator.rotate(this.lowerArmRightJoint, 0.2F, 0.3F, 0);
                this.animator.rotate(this.handRight, -0.5F, 0.3F, 0.1F);
                this.animator.rotate(this.axeHandle, 0, 0.5F, 0);

                this.animator.rotate(this.shoulderLeft, 0, 0F, 0);
                this.animator.rotate(this.upperArmLeftJoint, 0.2F, 0F, 0);
                this.animator.rotate(this.lowerArmLeftJoint, 0.1F, -0.3F, 0);
                this.animator.rotate(this.handLeft, 0F, 0.5F, -0.3F);

                this.animator.rotate(this.thighRightJoint, -0.4F, 0, 0);
                this.animator.rotate(this.thighRightJoint2, 0F, 0.2F, 0);
                this.animator.rotate(this.calfRightJoint, -0.1F, 0, 0);
                this.animator.rotate(this.footRightJoint, 0.5F, 0, 0);

                this.animator.rotate(this.thighLeftJoint, 0F, 0, 0);
                this.animator.rotate(this.thighLeftJoint2, 0F, -0.3F, 0.5F);
                this.animator.rotate(this.calfLeftJoint, -0.1F, 0, 0);
                this.animator.rotate(this.footLeftJoint, 0F, 0, 0);

                this.animator.endKeyframe();
                this.animator.setStaticKeyframe(8);
                this.animator.resetKeyframe(10);
                float frame = this.waistBendController.rotationPointX;
                if (entity.getAnimationTick() > 11 && entity.getAnimationTick() < 18) {
                    this.stomachJoint.rotateAngleX += 0.06 * -frame * (frame - 7);
                    this.neck.rotateAngleX -= 0.06 * -frame * (frame - 7);
                }
            } else {
                this.animator.setAnimation(EntityWroughtnaut.ATTACK_TWICE_ANIMATION);

                this.animator.startKeyframe(0);
                this.animator.rotate(this.stomachJoint, 0.3F, 1.3F, 0);
                this.animator.rotate(this.waist, 0, -0.5F, 0F);
                this.animator.move(this.waist, -2, 2, -7);
                this.animator.rotate(this.head, 0F, -0.8F, 0);

                this.animator.rotate(this.shoulderRight, 0, 0.1F, 0);
                this.animator.rotate(this.upperArmRightJoint, -0.5F, 0.2F, 0);
                this.animator.rotate(this.lowerArmRightJoint, 0.2F, 0.3F, 0);
                this.animator.rotate(this.handRight, -0.5F, 0.3F, 0.1F);
                this.animator.rotate(this.axeHandle, 0, 0.5F, 0);

                this.animator.rotate(this.shoulderLeft, 0, 0F, 0);
                this.animator.rotate(this.upperArmLeftJoint, 0.2F, 0F, 0);
                this.animator.rotate(this.lowerArmLeftJoint, 0.1F, -0.3F, 0);
                this.animator.rotate(this.handLeft, 0F, 0.5F, -0.3F);

                this.animator.rotate(this.thighLeftJoint, -0.4F, 0, 0);
                this.animator.rotate(this.thighLeftJoint2, 0F, -0.2F, 0);
                this.animator.rotate(this.calfLeftJoint, -0.1F, 0, 0);
                this.animator.rotate(this.footLeftJoint, 0.5F, 0, 0);

                this.animator.rotate(this.thighRightJoint, 0F, 0, 0);
                this.animator.rotate(this.thighRightJoint2, 0F, 0.3F, -0.5F);
                this.animator.rotate(this.calfRightJoint, -0.1F, 0, 0);
                this.animator.rotate(this.footRightJoint, 0F, 0, 0);
                this.animator.endKeyframe();

                this.animator.setStaticKeyframe(12);

                this.animator.startKeyframe(6);
                this.animator.rotate(this.stomachJoint, 0.3F, -0.5F, 0);
                this.animator.rotate(this.waist, 0, -0.5F, 0F);
                this.animator.move(this.waist, -2, 2, -7);
                this.animator.rotate(this.head, 0F, 0.8F, 0);
                this.animator.move(this.waistBendController, 7, 0, 0);

                this.animator.rotate(this.shoulderRight, 0, -0.6F, 0);
                this.animator.rotate(this.upperArmRightJoint, -0.8F, -0.9F, 0);
                this.animator.rotate(this.lowerArmRightJoint, 0.4F, 0.9F, 0F);
                this.animator.rotate(this.handRight, -0.8F, 0F, 0F);
                this.animator.rotate(this.axeHandle, 0, 1.2F, 0);

                this.animator.rotate(this.shoulderLeft, 0, -0.7F, 0);
                this.animator.move(this.shoulderLeft, 1, 0F, 0);
                this.animator.rotate(this.upperArmLeftJoint, 0.2F, -0.5F, 0);
                this.animator.move(this.upperArmLeft, 5F, 2F, 5F);
                this.animator.rotate(this.lowerArmLeftJoint, -0.5F, -0.3F, -0.7F);
                this.animator.move(this.lowerArmLeft, 0F, 2F, 0F);
                this.animator.rotate(this.handLeft, 2F, -0.3F, 0.5F);
                this.animator.rotate(this.handLeftJoint, 0F, -0.5F, -0.5F);

                this.animator.rotate(this.thighLeftJoint, -0.4F, 0, 0);
                this.animator.rotate(this.thighLeftJoint2, 0F, -0.2F, 0);
                this.animator.rotate(this.calfLeftJoint, -0.1F, 0, 0);
                this.animator.rotate(this.footLeftJoint, 0.5F, 0, 0);

                this.animator.rotate(this.thighRightJoint, 0F, 0, 0);
                this.animator.rotate(this.thighRightJoint2, 0F, 0.3F, -0.5F);
                this.animator.rotate(this.calfRightJoint, -0.1F, 0, 0);
                this.animator.rotate(this.footRightJoint, 0F, 0, 0);
                this.animator.endKeyframe();
                this.animator.setStaticKeyframe(8);
                this.animator.resetKeyframe(10);
                float frame = this.waistBendController.rotationPointX;
                if (entity.getAnimationTick() > 11 && entity.getAnimationTick() < 18) {
                    this.stomachJoint.rotateAngleX += 0.06 * -frame * (frame - 7);
                    this.neck.rotateAngleX -= 0.06 * -frame * (frame - 7);
                }
            }
        } else if (entity.getAnimation() == EntityWroughtnaut.ATTACK_THRICE_ANIMATION) {
            this.animator.setAnimation(EntityWroughtnaut.ATTACK_THRICE_ANIMATION);
            if (!entity.swingDirection) {
                this.animator.startKeyframe(0);
                this.animator.rotate(this.stomachJoint, 0.3F, 0.5F, 0);
                this.animator.rotate(this.waist, 0, 0.5F, 0F);
                this.animator.move(this.waist, 2, 2, -7);
                this.animator.rotate(this.head, 0F, -0.8F, 0);

                this.animator.rotate(this.shoulderRight, 0, 0.1F, 0);
                this.animator.rotate(this.upperArmRightJoint, -0.5F, 0.2F, 0);
                this.animator.rotate(this.lowerArmRightJoint, 0.2F, 0.3F, 0);
                this.animator.rotate(this.handRight, -0.5F, 0.3F, 0.1F);
                this.animator.rotate(this.axeHandle, 0, 0.5F, 0);

                this.animator.rotate(this.shoulderLeft, 0, 0F, 0);
                this.animator.rotate(this.upperArmLeftJoint, 0.2F, 0F, 0);
                this.animator.rotate(this.lowerArmLeftJoint, 0.1F, -0.3F, 0);
                this.animator.rotate(this.handLeft, 0F, 0.5F, -0.3F);

                this.animator.rotate(this.thighRightJoint, -0.4F, 0, 0);
                this.animator.rotate(this.thighRightJoint2, 0F, 0.2F, 0);
                this.animator.rotate(this.calfRightJoint, -0.1F, 0, 0);
                this.animator.rotate(this.footRightJoint, 0.5F, 0, 0);

                this.animator.rotate(this.thighLeftJoint, 0F, 0, 0);
                this.animator.rotate(this.thighLeftJoint2, 0F, -0.3F, 0.5F);
                this.animator.rotate(this.calfLeftJoint, -0.1F, 0, 0);
                this.animator.rotate(this.footLeftJoint, 0F, 0, 0);
                this.animator.endKeyframe();

                //Swing 3
                this.animator.startKeyframe(15);
                this.animator.rotate(this.stomachJoint, -0.2F, 0.5F, 0);
                this.animator.rotate(this.waist, 0, 0.5F, -0.2F);
                this.animator.move(this.waist, -3, 1F, 1);
                this.animator.rotate(this.head, 0F, -0.8F, 0);
                this.animator.rotate(this.neck, 0F, 0F, 0);

                this.animator.rotate(this.shoulderRight, 0, 0.1F, 0);
                this.animator.rotate(this.upperArmRightJoint, -0.5F, 0.2F, 0);
                this.animator.rotate(this.lowerArmRightJoint, 0.2F, 0.3F, 0);
                this.animator.rotate(this.handRight, -0.5F, 0.3F, 0.1F);
                this.animator.rotate(this.axeHandle, 0, 0.5F, 0);

                this.animator.rotate(this.shoulderLeft, 0, 0F, 0);
                this.animator.rotate(this.upperArmLeftJoint, 0.2F, 0F, 0);
                this.animator.rotate(this.lowerArmLeftJoint, 0.1F, -0.3F, 0);
                this.animator.rotate(this.handLeft, 0F, 0.5F, -0.3F);

                this.animator.rotate(this.thighRightJoint, -0.7F, 0, 0);
                this.animator.rotate(this.thighRightJoint2, 0F, 0.2F, 0.5F);
                this.animator.rotate(this.calfRightJoint, 0.3F, 0, 0);

                this.animator.rotate(this.thighLeftJoint, -0.2F, 0, 0);
                this.animator.rotate(this.thighLeftJoint2, 0F, -0.5F, -0.05F);
                this.animator.rotate(this.calfLeftJoint, 0.1F, 0, 0);
                this.animator.rotate(this.footLeftJoint, -0.1F, 0, 0);
                this.animator.endKeyframe();

                this.animator.setStaticKeyframe(6);

                this.animator.startKeyframe(12);
                this.animator.rotate(this.rootBox, 0, -(float) Math.PI * 2, 0);
                this.animator.rotate(this.stomachJoint, 0.3F, -1.3F, 0);
                this.animator.rotate(this.waist, 0, 0.5F, 0F);
                this.animator.move(this.waist, 2, 2, -7);
                this.animator.rotate(this.head, 0F, 0.8F, 0);
                this.animator.move(this.waistBendController, 7, 0, 0);

                this.animator.rotate(this.shoulderRight, 0, -0.6F, 0);
                this.animator.rotate(this.upperArmRightJoint, -1.0F, -0.5F, 0);
                this.animator.rotate(this.lowerArmRightJoint, -0.1F, -0.7F, 0F);
                this.animator.rotate(this.handRight, -0.6F, 0.6F, 0F);
                this.animator.rotate(this.axeHandle, 0, 1.2F, 0);

                this.animator.rotate(this.shoulderLeft, 0, 0.9F, 0);
                this.animator.rotate(this.upperArmLeftJoint, 0.5F, 0.6F, 0f);
                this.animator.rotate(this.lowerArmLeftJoint, -0.4F, -0.6F, 0F);
                this.animator.rotate(this.handLeft, -0.7F, -0.4F, -0.8F);
                this.animator.rotate(this.handLeftJoint, -0.4F, -0.1F, 0.2F);

                this.animator.rotate(this.thighRightJoint, -0.4F, 0, 0);
                this.animator.rotate(this.thighRightJoint2, 0F, 0.2F, 0);
                this.animator.rotate(this.calfRightJoint, -0.1F, 0, 0);
                this.animator.rotate(this.footRightJoint, 0.5F, 0, 0);

                this.animator.rotate(this.thighLeftJoint, 0F, 0, 0);
                this.animator.rotate(this.thighLeftJoint2, 0F, -0.3F, 0.5F);
                this.animator.rotate(this.calfLeftJoint, -0.1F, 0, 0);
                this.animator.rotate(this.footLeftJoint, 0F, 0, 0);
                this.animator.endKeyframe();

                this.animator.startKeyframe(0);
                this.animator.rotate(this.stomachJoint, 0.3F, -1.3F, 0);
                this.animator.rotate(this.waist, 0, 0.5F, 0F);
                this.animator.move(this.waist, 2, 2, -7);
                this.animator.rotate(this.head, 0F, 0.8F, 0);
                this.animator.move(this.waistBendController, 7, 0, 0);

                this.animator.rotate(this.shoulderRight, 0, -0.6F, 0);
                this.animator.rotate(this.upperArmRightJoint, -1.0F, -0.5F, 0);
                this.animator.rotate(this.lowerArmRightJoint, -0.1F, -0.7F, 0F);
                this.animator.rotate(this.handRight, -0.6F, 0.6F, 0F);
                this.animator.rotate(this.axeHandle, 0, 1.2F, 0);

                this.animator.rotate(this.shoulderLeft, 0, 0.9F, 0);
                this.animator.rotate(this.upperArmLeftJoint, 0.5F, 0.6F, 0f);
                this.animator.rotate(this.lowerArmLeftJoint, -0.4F, -0.6F, 0F);
                this.animator.rotate(this.handLeft, -0.7F, -0.4F, -0.8F);
                this.animator.rotate(this.handLeftJoint, -0.4F, -0.1F, 0.2F);

                this.animator.rotate(this.thighRightJoint, -0.4F, 0, 0);
                this.animator.rotate(this.thighRightJoint2, 0F, 0.2F, 0);
                this.animator.rotate(this.calfRightJoint, -0.1F, 0, 0);
                this.animator.rotate(this.footRightJoint, 0.5F, 0, 0);

                this.animator.rotate(this.thighLeftJoint, 0F, 0, 0);
                this.animator.rotate(this.thighLeftJoint2, 0F, -0.3F, 0.5F);
                this.animator.rotate(this.calfLeftJoint, -0.1F, 0, 0);
                this.animator.rotate(this.footLeftJoint, 0F, 0, 0);
                this.animator.endKeyframe();
                this.animator.setStaticKeyframe(11);

                this.animator.resetKeyframe(15);
            } else {
                this.animator.startKeyframe(0);
                this.animator.rotate(this.stomachJoint, 0.3F, -0.5F, 0);
                this.animator.rotate(this.waist, 0, -0.5F, 0F);
                this.animator.move(this.waist, -2, 2, -7);
                this.animator.rotate(this.head, 0F, 0.8F, 0);

                this.animator.rotate(this.shoulderRight, 0, -0.6F, 0);
                this.animator.rotate(this.upperArmRightJoint, -0.8F, -0.9F, 0);
                this.animator.rotate(this.lowerArmRightJoint, 0.4F, 0.9F, 0F);
                this.animator.rotate(this.handRight, -0.8F, 0F, 0F);
                this.animator.rotate(this.axeHandle, 0, 1.2F, 0);

                this.animator.rotate(this.shoulderLeft, 0, -0.7F, 0);
                this.animator.move(this.shoulderLeft, 1, 0F, 0);
                this.animator.rotate(this.upperArmLeftJoint, 0.2F, -0.5F, 0);
                this.animator.move(this.upperArmLeft, 5F, 2F, 5F);
                this.animator.rotate(this.lowerArmLeftJoint, -0.5F, -0.3F, -0.7F);
                this.animator.move(this.lowerArmLeft, 0F, 2F, 0F);
                this.animator.rotate(this.handLeft, 2F, -0.3F, 0.5F);
                this.animator.rotate(this.handLeftJoint, 0F, -0.5F, -0.5F);

                this.animator.rotate(this.thighLeftJoint, -0.4F, 0, 0);
                this.animator.rotate(this.thighLeftJoint2, 0F, -0.2F, 0);
                this.animator.rotate(this.calfLeftJoint, -0.1F, 0, 0);
                this.animator.rotate(this.footLeftJoint, 0.5F, 0, 0);

                this.animator.rotate(this.thighRightJoint, 0F, 0, 0);
                this.animator.rotate(this.thighRightJoint2, 0F, 0.3F, -0.5F);
                this.animator.rotate(this.calfRightJoint, -0.1F, 0, 0);
                this.animator.rotate(this.footRightJoint, 0F, 0, 0);
                this.animator.endKeyframe();

                //Swing 3
                this.animator.startKeyframe(15);
                this.animator.rotate(this.stomachJoint, 0F, -0.7F, 0);
                this.animator.rotate(this.waist, 0, -0.6F, 0.2F);
                this.animator.move(this.waist, 4, -4, 0);
                this.animator.rotate(this.head, 0F, 1.2F, 0);

                this.animator.rotate(this.shoulderRight, 0, -0.6F, 0);
                this.animator.rotate(this.upperArmRightJoint, -0.8F, -0.9F, 0);
                this.animator.rotate(this.lowerArmRightJoint, 0.4F, 0.9F, 0F);
                this.animator.rotate(this.handRight, -0.8F, 0F, 0F);
                this.animator.rotate(this.axeHandle, 0, 1.2F, 0);

                this.animator.rotate(this.shoulderLeft, 0, -0.7F, 0);
                this.animator.move(this.shoulderLeft, 1, 0F, 0);
                this.animator.rotate(this.upperArmLeftJoint, 0.2F, -0.5F, 0);
                this.animator.move(this.upperArmLeft, 5F, 2F, 5F);
                this.animator.rotate(this.lowerArmLeftJoint, -0.5F, -0.3F, -0.7F);
                this.animator.move(this.lowerArmLeft, 0F, 2F, 0F);
                this.animator.rotate(this.handLeft, 2F, -0.3F, 0.5F);
                this.animator.rotate(this.handLeftJoint, 0F, -0.5F, -0.5F);

                this.animator.rotate(this.thighLeftJoint, -0.7F, 0, -0.3f);
                this.animator.rotate(this.thighLeftJoint2, 0F, -0.6F, 0);
                this.animator.rotate(this.calfLeftJoint, -0.1F, 0, 0);
                this.animator.rotate(this.footLeftJoint, 0.5F, 0, 0);

                this.animator.rotate(this.thighRightJoint, -0.2F, 0, 0.3f);
                this.animator.rotate(this.thighRightJoint2, 0F, 0.3F, -0.3F);
                this.animator.rotate(this.calfRightJoint, -0.4F, 0, 0);
                this.animator.rotate(this.footRightJoint, 0.2F, 0, 0);
                this.animator.endKeyframe();

                this.animator.setStaticKeyframe(6);

                this.animator.startKeyframe(12);
                this.animator.rotate(this.rootBox, 0, (float) Math.PI * 2, 0);
                this.animator.rotate(this.stomachJoint, 0.3F, 1.3F, 0);
                this.animator.rotate(this.waist, 0, -0.5F, 0F);
                this.animator.move(this.waist, -2, 2, -7);
                this.animator.rotate(this.head, 0F, -0.8F, 0);
                this.animator.move(this.waistBendController, 7, 0, 0);

                this.animator.rotate(this.shoulderRight, 0, 0.6F, 0);
                this.animator.rotate(this.upperArmRightJoint, -0.5F, 0.8F, 0);
                this.animator.rotate(this.lowerArmRightJoint, 0.2F, -0.8F, 0);
                this.animator.rotate(this.handRight, -0.5F, 0.3F, 0.1F);
                this.animator.rotate(this.axeHandle, 0, 0.5F, 0);

                this.animator.rotate(this.shoulderLeft, 0, 0.6F, 0);
                this.animator.rotate(this.upperArmLeftJoint, 0.5F, -0.1F, 0);
                this.animator.rotate(this.lowerArmLeftJoint, 0.0F, 0.4F, -0.1f);
                this.animator.rotate(this.handLeft, 0.1F, 0.35F, -0.35F);

                this.animator.rotate(this.thighLeftJoint, -0.4F, 0, 0);
                this.animator.rotate(this.thighLeftJoint2, 0F, -0.2F, 0);
                this.animator.rotate(this.calfLeftJoint, -0.1F, 0, 0);
                this.animator.rotate(this.footLeftJoint, 0.5F, 0, 0);

                this.animator.rotate(this.thighRightJoint, 0F, 0, 0);
                this.animator.rotate(this.thighRightJoint2, 0F, 0.3F, -0.5F);
                this.animator.rotate(this.calfRightJoint, -0.1F, 0, 0);
                this.animator.rotate(this.footRightJoint, 0F, 0, 0);
                this.animator.endKeyframe();

                this.animator.startKeyframe(0);
                this.animator.rotate(this.stomachJoint, 0.3F, 1.3F, 0);
                this.animator.rotate(this.waist, 0, -0.5F, 0F);
                this.animator.move(this.waist, -2, 2, -7);
                this.animator.rotate(this.head, 0F, -0.8F, 0);

                this.animator.rotate(this.shoulderRight, 0, 0.6F, 0);
                this.animator.rotate(this.upperArmRightJoint, -0.5F, 0.8F, 0);
                this.animator.rotate(this.lowerArmRightJoint, 0.2F, -0.8F, 0);
                this.animator.rotate(this.handRight, -0.5F, 0.3F, 0.1F);
                this.animator.rotate(this.axeHandle, 0, 0.5F, 0);

                this.animator.rotate(this.shoulderLeft, 0, 0.6F, 0);
                this.animator.rotate(this.upperArmLeftJoint, 0.5F, -0.1F, 0);
                this.animator.rotate(this.lowerArmLeftJoint, 0.0F, 0.4F, -0.1f);
                this.animator.rotate(this.handLeft, 0.1F, 0.35F, -0.35F);

                this.animator.rotate(this.thighLeftJoint, -0.4F, 0, 0);
                this.animator.rotate(this.thighLeftJoint2, 0F, -0.2F, 0);
                this.animator.rotate(this.calfLeftJoint, -0.1F, 0, 0);
                this.animator.rotate(this.footLeftJoint, 0.5F, 0, 0);

                this.animator.rotate(this.thighRightJoint, 0F, 0, 0);
                this.animator.rotate(this.thighRightJoint2, 0F, 0.3F, -0.5F);
                this.animator.rotate(this.calfRightJoint, -0.1F, 0, 0);
                this.animator.rotate(this.footRightJoint, 0F, 0, 0);
                this.animator.endKeyframe();
                this.animator.setStaticKeyframe(11);

                this.animator.resetKeyframe(15);
            }
            float frame = this.waistBendController.rotationPointX;
            if (entity.getAnimationTick() > 14 && entity.getAnimationTick() < 33) {
                this.stomachJoint.rotateAngleX += 0.05 * -frame * (frame - 7);
                this.neck.rotateAngleX -= 0.05 * -frame * (frame - 7);
            }
        } else if (entity.getAnimation() == EntityWroughtnaut.VERTICAL_ATTACK_ANIMATION) {
            this.animator.setAnimation(EntityWroughtnaut.VERTICAL_ATTACK_ANIMATION);
            this.animator.setStaticKeyframe(6);
            this.animator.startKeyframe(15);
            this.animator.rotate(this.stomachJoint, -0.65F, 0F, 0);
            this.animator.move(this.waist, 0F, 0F, 6);
            this.animator.rotate(this.neck, 0.65F, 0F, 0);

            this.animator.rotate(this.shoulderRight, 0, 1F, 0);
            this.animator.rotate(this.upperArmRightJoint, 0F, 0.3F, -1F);
            this.animator.rotate(this.lowerArmRightJoint, -1.5F, 0F, 0.3F);
            this.animator.rotate(this.handRight, 0F, 0.2F, -0.5F);
            this.animator.rotate(this.axeHandle, 0, -1.7F, 0);

            this.animator.rotate(this.shoulderLeft, 0, -1F, 0);
            this.animator.rotate(this.upperArmLeftJoint, -0.5F, -0.3F, -1F);
            this.animator.rotate(this.lowerArmLeftJoint, 1.7F, 0F, -0.1F);
            this.animator.rotate(this.handLeft, -1.3F, -0.6F, -0.6F);

            this.animator.rotate(this.thighRightJoint, -0.7F, 0, 0);
            this.animator.rotate(this.thighRightJoint2, 0F, 0.2F, 0.5F);
            this.animator.rotate(this.calfRightJoint, 0.3F, 0, 0);

            this.animator.rotate(this.thighLeftJoint, -0.1F, -0.2F, 0);
            this.animator.rotate(this.calfLeftJoint, -0.3F, 0, 0);
            this.animator.rotate(this.footLeftJoint, 0.3F, 0, 0);

            this.animator.endKeyframe();

            this.animator.setStaticKeyframe(5);

            this.animator.startKeyframe(4);
            this.animator.rotate(this.stomachJoint, 0.65F, 0F, 0);
            this.animator.move(this.waist, 0F, 4F, -3);
            this.animator.rotate(this.neck, 0.3F, 0F, 0);

            this.animator.rotate(this.shoulderRight, 0, 1F, 0);
            this.animator.rotate(this.upperArmRightJoint, 0F, 0.3F, -1F);
            this.animator.rotate(this.upperArmRight, 0F, 0F, 1.5F);
            this.animator.rotate(this.lowerArmRightJoint, -1.5F, 0F, 0.3F);
            this.animator.rotate(this.lowerArmRight, 0F, 0F, 0.7F);
            this.animator.rotate(this.handRight, 0F, 0.2F, -0.5F);
            this.animator.rotate(this.axeHandle, 0, -1.7F, 0);

            this.animator.rotate(this.shoulderLeft, 0, 0.3F, -0.4F);
            this.animator.rotate(this.upperArmLeftJoint, -0.5F, 0.1F, -0.2F);
            this.animator.rotate(this.upperArmLeft, -0.2F, 0.3F, -0.5F);
            this.animator.rotate(this.lowerArmLeft, 0, -0.3F, -1.5F);
            this.animator.rotate(this.handLeftJoint, -1.6F, 0, 0.5F);
            this.animator.rotate(this.shoulderLeft, 0, 0.2F, 0.35F);
            this.animator.rotate(this.upperArmLeft, 0, -0.3F, 0.5F);
            this.animator.rotate(this.lowerArmLeft, 0, 1.5F, 1.3F);
            this.animator.rotate(this.handLeft, -0.2F, 0, 0.3F);

            this.animator.rotate(this.shoulderLeft, 0, -1F, 0);
            this.animator.rotate(this.upperArmLeftJoint, 0F, 0.3F, -1F);
            this.animator.rotate(this.upperArmLeft, 0F, 0F, 1.5F);
            this.animator.rotate(this.lowerArmLeftJoint, 1F, 0F, -0.5F);
            this.animator.rotate(this.lowerArmLeft, 0F, 0F, -0.7F);
            this.animator.rotate(this.handLeft, 0.7F, 0F, -0.5F);

            this.animator.rotate(this.thighRightJoint, -0.6F, 0.4F, 0);
            this.animator.rotate(this.thighRightJoint2, 0F, 0F, 0);
            this.animator.rotate(this.calfRightJoint, -0.1F, 0, 0);
            this.animator.rotate(this.footRightJoint, 0.7F, 0, 0);

            this.animator.rotate(this.thighLeftJoint, -0.2F, 0, 0);
            this.animator.rotate(this.calfLeftJoint, 0.3F, 0, 0);
            this.animator.rotate(this.footLeftJoint, -0.1F, 0, 0);

            this.animator.endKeyframe();
            this.animator.setStaticKeyframe(15);

            this.animator.startKeyframe(5);
            this.animator.rotate(this.stomachJoint, 0.2F, 0F, 0);
            this.animator.move(this.waist, 0F, 4F, -3);
            this.animator.rotate(this.neck, -0.5F, 0F, 0);

            this.animator.rotate(this.shoulderRight, 0, 1F, 0);
            this.animator.rotate(this.upperArmRightJoint, 0F, 0.3F, -1F);
            this.animator.rotate(this.upperArmRight, 0F, 0.15F, 1.7F);
            this.animator.move(this.upperArmRightJoint, 0F, 7F, 0F);
            this.animator.move(this.upperArmRight, 5F, 0F, 0F);
            this.animator.rotate(this.lowerArmRightJoint, -2F, 0F, 0.8F);
            this.animator.rotate(this.lowerArmRight, 0F, 0F, 0.7F);
            this.animator.rotate(this.handRight, 0F, 0.6F, -0.5F);
            this.animator.rotate(this.handRightJoint, 0.2F, 0F, 0F);
            this.animator.rotate(this.axeHandle, -0.14F, -1.72F, 0.12F);

            this.animator.rotate(this.shoulderLeft, 0, 0.3F, -0.4F);
            this.animator.rotate(this.upperArmLeftJoint, -0.5F, 0.1F, -0.2F);
            this.animator.rotate(this.upperArmLeft, -0.2F, 0.5F, -0.5F);
            this.animator.rotate(this.lowerArmLeft, 0, -0.3F, -1.5F);
            this.animator.rotate(this.handLeftJoint, -1.6F, 0, 0.5F);
            this.animator.rotate(this.shoulderLeft, 0, 0.2F, 0.35F);
            this.animator.rotate(this.upperArmLeft, 0, -0.3F, 0.5F);
            this.animator.rotate(this.lowerArmLeft, 0, 1.5F, 1.3F);
            this.animator.rotate(this.handLeft, -0.2F, 0, 0.3F);

            this.animator.rotate(this.shoulderLeft, 0, -1F, 0);
            this.animator.rotate(this.upperArmLeftJoint, 0F, -0.3F, -1F);
            this.animator.rotate(this.upperArmLeft, 0F, -0.9F, 1.5F);
            this.animator.move(this.upperArmLeftJoint, 0F, 5F, 0F);
            this.animator.rotate(this.lowerArmLeftJoint, 1.2F, 0.3F, -0.8F);
            this.animator.rotate(this.lowerArmLeft, -0.5F, -0.7F, -1.5F);
            this.animator.rotate(this.handLeft, 0.7F, 0F, -0.5F);

            this.animator.rotate(this.thighRightJoint, -0.6F, 0.4F, 0);
            this.animator.rotate(this.thighRightJoint2, 0F, 0F, 0);
            this.animator.rotate(this.calfRightJoint, -0.1F, 0, 0);
            this.animator.rotate(this.footRightJoint, 0.7F, 0, 0);

            this.animator.rotate(this.thighLeftJoint, -0.2F, 0, 0);
            this.animator.rotate(this.calfLeftJoint, 0.3F, 0, 0);
            this.animator.rotate(this.footLeftJoint, -0.1F, 0, 0);

            this.animator.move(this.waistBendController, 1, 0, 0);
            this.animator.endKeyframe();

            this.animator.setStaticKeyframe(15);

            this.animator.startKeyframe(7);
            this.animator.rotate(this.stomachJoint, 0.65F, 0F, 0);
            this.animator.move(this.waist, 0F, 4F, -3);
            this.animator.rotate(this.neck, 0.3F, 0F, 0);

            this.animator.rotate(this.shoulderRight, 0, 1F, 0);
            this.animator.rotate(this.upperArmRightJoint, 0F, 0.3F, -1F);
            this.animator.rotate(this.upperArmRight, 0F, 0F, 1.5F);
            this.animator.rotate(this.lowerArmRightJoint, -1.5F, 0F, 0.3F);
            this.animator.rotate(this.lowerArmRight, 0F, 0F, 0.7F);
            this.animator.rotate(this.handRight, 0F, 0.2F, -0.5F);
            this.animator.rotate(this.axeHandle, 0, -1.7F, 0);

            this.animator.rotate(this.shoulderLeft, 0, 0.3F, -0.4F);
            this.animator.rotate(this.upperArmLeftJoint, -0.5F, 0.1F, -0.2F);
            this.animator.rotate(this.upperArmLeft, -0.2F, 0.3F, -0.5F);
            this.animator.rotate(this.lowerArmLeft, 0, -0.3F, -1.5F);
            this.animator.rotate(this.handLeftJoint, -1.6F, 0, 0.5F);
            this.animator.rotate(this.shoulderLeft, 0, 0.2F, 0.35F);
            this.animator.rotate(this.upperArmLeft, 0, -0.3F, 0.5F);
            this.animator.rotate(this.lowerArmLeft, 0, 1.5F, 1.3F);
            this.animator.rotate(this.handLeft, -0.2F, 0, 0.3F);

            this.animator.rotate(this.shoulderLeft, 0, -1F, 0);
            this.animator.rotate(this.upperArmLeftJoint, 0F, 0.3F, -1F);
            this.animator.rotate(this.upperArmLeft, 0F, 0F, 1.5F);
            this.animator.rotate(this.lowerArmLeftJoint, 1F, 0F, -0.5F);
            this.animator.rotate(this.lowerArmLeft, 0F, 0F, -0.7F);
            this.animator.rotate(this.handLeft, 0.7F, 0F, -0.5F);

            this.animator.rotate(this.thighRightJoint, -0.6F, 0.4F, 0);
            this.animator.rotate(this.thighRightJoint2, 0F, 0F, 0);
            this.animator.rotate(this.calfRightJoint, -0.1F, 0, 0);
            this.animator.rotate(this.footRightJoint, 0.7F, 0, 0);

            this.animator.rotate(this.thighLeftJoint, -0.2F, 0, 0);
            this.animator.rotate(this.calfLeftJoint, 0.3F, 0, 0);
            this.animator.rotate(this.footLeftJoint, -0.1F, 0, 0);

            this.animator.endKeyframe();

            this.animator.setStaticKeyframe(5);

            this.animator.startKeyframe(5);
            this.animator.rotate(this.stomachJoint, 0.2F, 0F, 0);
            this.animator.move(this.waist, 0F, 4F, -3);
            this.animator.rotate(this.neck, -0.5F, 0F, 0);

            this.animator.rotate(this.shoulderRight, 0, 1F, 0);
            this.animator.rotate(this.upperArmRightJoint, 0F, 0.3F, -1F);
            this.animator.rotate(this.upperArmRight, 0F, 0.15F, 1.7F);
            this.animator.move(this.upperArmRightJoint, 0F, 7F, 0F);
            this.animator.move(this.upperArmRight, 5F, 0F, 0F);
            this.animator.rotate(this.lowerArmRightJoint, -2F, 0F, 0.8F);
            this.animator.rotate(this.lowerArmRight, 0F, 0F, 0.7F);
            this.animator.rotate(this.handRight, 0F, 0.6F, -0.5F);
            this.animator.rotate(this.handRightJoint, 0.2F, 0F, 0F);
            this.animator.rotate(this.axeHandle, -0.14F, -1.72F, 0.12F);

            this.animator.rotate(this.shoulderLeft, 0, 0.3F, -0.4F);
            this.animator.rotate(this.upperArmLeftJoint, -0.5F, 0.1F, -0.2F);
            this.animator.rotate(this.upperArmLeft, -0.2F, 0.5F, -0.5F);
            this.animator.rotate(this.lowerArmLeft, 0, -0.3F, -1.5F);
            this.animator.rotate(this.handLeftJoint, -1.6F, 0, 0.5F);
            this.animator.rotate(this.shoulderLeft, 0, 0.2F, 0.35F);
            this.animator.rotate(this.upperArmLeft, 0, -0.3F, 0.5F);
            this.animator.rotate(this.lowerArmLeft, 0, 1.5F, 1.3F);
            this.animator.rotate(this.handLeft, -0.2F, 0, 0.3F);

            this.animator.rotate(this.shoulderLeft, 0, -1F, 0);
            this.animator.rotate(this.upperArmLeftJoint, 0F, -0.3F, -1F);
            this.animator.rotate(this.upperArmLeft, 0F, -0.9F, 1.5F);
            this.animator.move(this.upperArmLeftJoint, 0F, 5F, 0F);
            this.animator.rotate(this.lowerArmLeftJoint, 1.2F, 0.3F, -0.8F);
            this.animator.rotate(this.lowerArmLeft, -0.5F, -0.7F, -1.5F);
            this.animator.rotate(this.handLeft, 0.7F, 0F, -0.5F);

            this.animator.rotate(this.thighRightJoint, -0.6F, 0.4F, 0);
            this.animator.rotate(this.thighRightJoint2, 0F, 0F, 0);
            this.animator.rotate(this.calfRightJoint, -0.1F, 0, 0);
            this.animator.rotate(this.footRightJoint, 0.7F, 0, 0);

            this.animator.rotate(this.thighLeftJoint, -0.2F, 0, 0);
            this.animator.rotate(this.calfLeftJoint, 0.3F, 0, 0);
            this.animator.rotate(this.footLeftJoint, -0.1F, 0, 0);

            this.animator.move(this.waistBendController, 1, 0, 0);
            this.animator.endKeyframe();

            this.animator.setStaticKeyframe(5);

            this.animator.startKeyframe(5);
            this.animator.rotate(this.stomachJoint, -0.5F, 0F, 0);
            this.animator.move(this.waist, 0F, 0F, 6);
            this.animator.rotate(this.neck, 0.65F, 0F, 0);

            this.animator.rotate(this.shoulderRight, 0, 1F, 0);
            this.animator.rotate(this.upperArmRightJoint, 0F, 0.3F, -1F);
            this.animator.rotate(this.upperArmRight, 0F, 0F, 1.3F);
            this.animator.rotate(this.lowerArmRightJoint, -1.5F, 0F, 0.3F);
            this.animator.rotate(this.handRight, 0F, 0.2F, -0.5F);
            this.animator.rotate(this.axeHandle, 0, -1.7F, 0);

            this.animator.rotate(this.shoulderLeft, 0, -1F, 0);
            this.animator.rotate(this.upperArmLeftJoint, -0.5F, -0.3F, -1F);
            this.animator.rotate(this.upperArmLeft, -0.5F, 0F, 1.3F);
            this.animator.rotate(this.lowerArmLeftJoint, 1.7F, 0F, -0.1F);
            this.animator.rotate(this.handLeft, -1.3F, -0.6F, -0.6F);

            this.animator.rotate(this.thighRightJoint, -0.7F, 0, 0);
            this.animator.rotate(this.thighRightJoint2, 0F, 0.2F, 0.5F);
            this.animator.rotate(this.calfRightJoint, 0.3F, 0, 0);

            this.animator.rotate(this.thighLeftJoint, -0.1F, -0.2F, 0);
            this.animator.rotate(this.calfLeftJoint, -0.3F, 0, 0);
            this.animator.rotate(this.footLeftJoint, 0.3F, 0, 0);

            this.animator.endKeyframe();

            this.animator.setStaticKeyframe(3);

            this.animator.resetKeyframe(10);
            this.neck.rotateAngleX += Math.sin(entityFrame * 2) * this.waistBendController.rotationPointX * 0.1;
        } else if (entity.getAnimation() == EntityWroughtnaut.HURT_ANIMATION) {
            this.animator.setAnimation(EntityWroughtnaut.HURT_ANIMATION);
            this.animator.startKeyframe(0);
            this.animator.rotate(this.stomachJoint, 0.65F, 0F, 0);
            this.animator.move(this.waist, 0F, 4F, -3);
            this.animator.rotate(this.neck, 0.3F, 0F, 0);

            this.animator.rotate(this.shoulderRight, 0, 1F, 0);
            this.animator.rotate(this.upperArmRightJoint, 0F, 0.3F, -1F);
            this.animator.rotate(this.upperArmRight, 0F, 0F, 1.5F);
            this.animator.rotate(this.lowerArmRightJoint, -1.5F, 0F, 0.3F);
            this.animator.rotate(this.lowerArmRight, 0F, 0F, 0.7F);
            this.animator.rotate(this.handRight, 0F, 0.2F, -0.5F);
            this.animator.rotate(this.axeHandle, 0, -1.7F, 0);

            this.animator.rotate(this.shoulderLeft, 0, 0.3F, 0.4F);
            this.animator.rotate(this.upperArmLeft, 0, 0.5F, -0.5F);
            this.animator.rotate(this.lowerArmLeft, 0, -0.3F, -1.5F);
            this.animator.rotate(this.handLeftJoint, -1.6F, 0, 0.5F);
            this.animator.rotate(this.shoulderLeft, 0, 0.2F, 0.35F);
            this.animator.rotate(this.upperArmLeft, 0, -0.3F, 0.5F);
            this.animator.rotate(this.lowerArmLeft, 0, 1.5F, 1.3F);
            this.animator.rotate(this.handLeft, -0.2F, 0, 0.3F);

            this.animator.rotate(this.shoulderLeft, 0, -1F, 0);
            this.animator.rotate(this.upperArmLeftJoint, 0F, 0.3F, -1F);
            this.animator.rotate(this.upperArmLeft, 0F, 0F, 1.5F);
            this.animator.rotate(this.lowerArmLeftJoint, 1F, 0F, -0.5F);
            this.animator.rotate(this.lowerArmLeft, 0F, 0F, -0.7F);
            this.animator.rotate(this.handLeft, 0.7F, 0F, -0.5F);

            this.animator.rotate(this.thighRightJoint, -0.6F, 0.4F, 0);
            this.animator.rotate(this.thighRightJoint2, 0F, 0F, 0);
            this.animator.rotate(this.calfRightJoint, -0.1F, 0, 0);
            this.animator.rotate(this.footRightJoint, 0.7F, 0, 0);

            this.animator.rotate(this.thighLeftJoint, -0.2F, 0, 0);
            this.animator.rotate(this.calfLeftJoint, 0.3F, 0, 0);
            this.animator.rotate(this.footLeftJoint, -0.1F, 0, 0);
            this.animator.endKeyframe();

            this.animator.startKeyframe(4);
            this.animator.rotate(this.stomachJoint, -0.4F, 0, 0);
            this.animator.rotate(this.neck, -0.4F, 0, 0);

            this.animator.rotate(this.shoulderLeft, 0, 0.5F, 0.75F);
            this.animator.rotate(this.upperArmLeft, 0, 1.2F, -0.3F);
            this.animator.rotate(this.lowerArmLeft, 0, 0.9F, -0.2F);
            this.animator.rotate(this.lowerArmLeftJoint, 0, 0F, -0.3F);
            this.animator.rotate(this.handLeftJoint, -1.6F, 0, 0.5F);
            this.animator.rotate(this.handLeft, -0.2F, 0, 0.3F);

            this.animator.rotate(this.shoulderRight, 0, -0.5F, -0.75F);
            this.animator.rotate(this.upperArmRight, 0, -1F, -0.3F);
            this.animator.rotate(this.lowerArmRight, 0, 0.9F, -0.2F);
            this.animator.rotate(this.lowerArmRightJoint, 0, 0F, -0.3F);
            this.animator.rotate(this.handRightJoint, -1F, 0, -0.5F);
            this.animator.rotate(this.handRight, -0.2F, 0, 0.3F);

            this.animator.endKeyframe();
            this.animator.setStaticKeyframe(2);
            this.animator.resetKeyframe(9);
        } else if (entity.getAnimation() == EntityWroughtnaut.DIE_ANIMATION) {
            this.animator.setAnimation(EntityWroughtnaut.DIE_ANIMATION);
            this.animator.startKeyframe(5);
            this.animator.rotate(this.stomachJoint, -0.4F, 0, 0);
            this.animator.rotate(this.neck, -0.4F, 0, 0);

            this.animator.rotate(this.shoulderLeft, 0, 0.5F, 0.75F);
            this.animator.rotate(this.upperArmLeft, 0, 1.2F, -0.3F);
            this.animator.rotate(this.lowerArmLeft, 0, 0.9F, -0.2F);
            this.animator.rotate(this.lowerArmLeftJoint, 0, 0F, -0.3F);
            this.animator.rotate(this.handLeftJoint, -1.6F, 0, 0.5F);
            this.animator.rotate(this.handLeft, -0.2F, 0, 0.3F);

            this.animator.rotate(this.shoulderRight, 0, -0.5F, -0.75F);
            this.animator.rotate(this.upperArmRight, 0, -1F, -0.3F);
            this.animator.rotate(this.lowerArmRight, 0, 0.9F, -0.2F);
            this.animator.rotate(this.lowerArmRightJoint, 0, 0F, -0.3F);
            this.animator.rotate(this.handRightJoint, -1F, 0, -0.5F);
            this.animator.rotate(this.handRight, -0.2F, 0, 0.3F);
            this.animator.endKeyframe();

            this.animator.startKeyframe(8);
            this.animator.rotate(this.stomachJoint, -0.4F, 0, 0);
            this.animator.rotate(this.neck, -0.4F, 0, 0);

            this.animator.rotate(this.shoulderLeft, 0, 0.5F, 0.75F);
            this.animator.rotate(this.upperArmLeft, 0, 1.2F, -0.3F);
            this.animator.rotate(this.lowerArmLeft, 0, 0.9F, -0.2F);
            this.animator.rotate(this.lowerArmLeftJoint, 0, 0F, -0.3F);
            this.animator.rotate(this.handLeftJoint, -1.6F, 0, 0.5F);
            this.animator.rotate(this.handLeft, -0.2F, 0, 0.3F);

            this.animator.rotate(this.shoulderRight, 0, -0.5F, -0.75F);
            this.animator.rotate(this.upperArmRight, 0, -1F, -0.3F);
            this.animator.rotate(this.lowerArmRight, 0, 0.9F, -0.2F);
            this.animator.rotate(this.lowerArmRightJoint, 0, 0F, -0.3F);
            this.animator.rotate(this.handRightJoint, -1F, 0, -0.5F);
            this.animator.rotate(this.handRight, -0.2F, 0, 0.3F);

            this.animator.move(this.waistBendController, 1, 0, 0);
            this.animator.endKeyframe();

            this.animator.setStaticKeyframe(30);

            this.animator.startKeyframe(10);
            this.animator.rotate(this.stomachJoint, -0.4F, 0, 0);
            this.animator.rotate(this.neck, -0.4F, 0, 0);

            this.animator.rotate(this.shoulderLeft, 0, 0.5F, 0.75F);
            this.animator.rotate(this.upperArmLeft, 0, 1.2F, -0.3F);
            this.animator.rotate(this.lowerArmLeft, 0, 0.9F, -0.2F);
            this.animator.rotate(this.lowerArmLeftJoint, 0, 0F, -0.3F);
            this.animator.rotate(this.handLeftJoint, -1.6F, 0, 0.5F);
            this.animator.rotate(this.handLeft, -0.2F, 0, 0.3F);

            this.animator.rotate(this.shoulderRight, 0, -0.5F, -0.75F);
            this.animator.rotate(this.upperArmRight, 0, -1F, -0.3F);
            this.animator.rotate(this.lowerArmRight, 0, 0.9F, -0.2F);
            this.animator.rotate(this.lowerArmRightJoint, 0, 0F, -0.3F);
            this.animator.rotate(this.handRightJoint, -1F, 0, -0.5F);
            this.animator.rotate(this.handRight, -0.2F, 0, 0.3F);
            this.animator.endKeyframe();

            this.animator.setStaticKeyframe(10);

            this.animator.startKeyframe(5);
            this.animator.move(this.rootBox, 0, 7, -10);
            this.animator.rotate(this.stomachJoint, -0.6F, 0, 0);
            this.animator.rotate(this.neck, -0.4F, 0, 0);

            this.animator.rotate(this.shoulderLeft, 0, 0.5F, 0.75F);
            this.animator.rotate(this.upperArmLeft, 0, 1.2F, -0.3F);
            this.animator.rotate(this.lowerArmLeft, 0, 0.9F, -0.2F);
            this.animator.rotate(this.lowerArmLeftJoint, 0, 0F, -0.3F);
            this.animator.rotate(this.handLeftJoint, -1.6F, 0, 0.5F);
            this.animator.rotate(this.handLeft, -0.2F, 0, 0.3F);

            this.animator.rotate(this.shoulderRight, 0, -0.5F, -0.75F);
            this.animator.rotate(this.upperArmRight, 0, -1F, -0.3F);
            this.animator.rotate(this.lowerArmRight, 0, 0.9F, -0.2F);
            this.animator.rotate(this.lowerArmRightJoint, 0, 0F, -0.3F);
            this.animator.rotate(this.handRightJoint, -1F, 0, -0.5F);
            this.animator.rotate(this.handRight, -0.2F, 0, 0.3F);

            this.animator.rotate(this.thighRightJoint, 0.3F, 0.5F, -0.3F);
            this.animator.rotate(this.calfRightJoint, 0.5F, 0, 0);
            this.animator.rotate(this.footRightJoint, 1.7F, 0, 0);

            this.animator.rotate(this.thighLeftJoint, 0.3F, -0.5F, 0.3F);
            this.animator.rotate(this.calfLeftJoint, 0.5F, 0, 0);
            this.animator.rotate(this.footLeftJoint, 1.7F, 0, 0);
            this.animator.endKeyframe();

            this.animator.setStaticKeyframe(13);

            this.animator.startKeyframe(7);
            this.animator.move(this.rootBox, 0, 15, -33);
            this.animator.rotate(this.rootBox, 1.5F, 0, 0);

            this.animator.rotate(this.shoulderLeft, 0, 0.5F, 0.75F);
            this.animator.rotate(this.shoulderLeftJoint, 0, 0, 0.6F);
            this.animator.rotate(this.upperArmLeft, 2.4F, 0.6F, -0.3F);
            this.animator.rotate(this.lowerArmLeft, 0, 0.9F, -0.2F);
            this.animator.rotate(this.lowerArmLeftJoint, 0, 0F, -0.3F);
            this.animator.rotate(this.handLeftJoint, -1.6F, 0, 0.5F);
            this.animator.rotate(this.handLeft, -0.2F, 0, 0.3F);

            this.animator.rotate(this.shoulderRight, 0, -0.5F, -0.75F);
            this.animator.rotate(this.shoulderRightJoint, 0, 0, -0.6F);
            this.animator.rotate(this.upperArmRight, -2.7F, -0.5F, 0.4F);
            this.animator.rotate(this.lowerArmRight, 0, 0.9F, -0.2F);
            this.animator.rotate(this.lowerArmRightJoint, 0, 0F, -0.3F);
            this.animator.rotate(this.handRightJoint, 0.4F, 0.1F, -0.7F);
            this.animator.rotate(this.handRight, -0.2F, 0, 0.3F);
            this.animator.rotate(this.axeHandle, 0F, -0.5F, 0F);

            this.animator.rotate(this.thighRightJoint, 0.3F, 0.5F, -0.3F);
            this.animator.rotate(this.calfRightJoint, -0.8F, 0.1F, 0);
            this.animator.rotate(this.calfRight, 0, 0, 0.3F);
            this.animator.rotate(this.footRightJoint, 1.7F, 0, 0);

            this.animator.rotate(this.thighLeftJoint, 0.3F, -0.5F, 0.3F);
            this.animator.rotate(this.calfLeftJoint, -0.8F, -0.1F, 0);
            this.animator.rotate(this.calfLeft, 0, 0, -0.3F);
            this.animator.rotate(this.footLeftJoint, 1.7F, 0, 0);
            this.animator.endKeyframe();

            this.animator.setStaticKeyframe(40);

            this.swing(this.stomachJoint, 0.5F, 0.2F * this.waistBendController.rotationPointX, false, 0, 0, entityFrame, 1F);
            this.walk(this.neck, 1.5F, 0.1F * this.waistBendController.rotationPointX, false, 0F, 0, entityFrame, 1F);
            this.swing(this.shoulderRight, 1.5F, 0.05F * this.waistBendController.rotationPointX, true, 0F, 0, entityFrame, 1F);
            this.swing(this.shoulderLeft, 1.5F, 0.05F * this.waistBendController.rotationPointX, false, 0F, 0, entityFrame, 1F);
        } else if (entity.getAnimation() == EntityWroughtnaut.ACTIVATE_ANIMATION) {
            this.animator.setAnimation(EntityWroughtnaut.ACTIVATE_ANIMATION);
            this.animator.startKeyframe(0);
            this.animator.rotate(this.shoulderLeft, 0, -0.4F, -0.4F);
            this.animator.rotate(this.shoulderRight, 0, 0.4F, 0.4F);
            this.animator.rotate(this.upperArmRight, 0, 0.5F, 0.5F);
            this.animator.rotate(this.upperArmLeft, 0, -0.5F, 0.5F);
            this.animator.rotate(this.lowerArmLeft, 0, 1, 1.5F);
            this.animator.rotate(this.lowerArmRight, 0, 1, -1.5F);
            this.animator.move(this.axeBase, 0, 30, 3.5F);
            this.animator.rotate(this.axeBase, 0, 0.35F, 0);
            this.animator.rotate(this.handRight, -0.9F, 0, 0);
            this.animator.rotate(this.handRightJoint, -0.2F, 0, -0.88F);
            this.animator.rotate(this.handLeftJoint, 0.8F, -0.3F, 0F);
            this.animator.rotate(this.neck, 0.5F, 0, 0);
            this.animator.move(this.rootBox, 0, -5, 0);
            this.animator.rotate(this.thighRightJoint, 0.35F, 0.5F, 0);
            this.animator.rotate(this.thighLeftJoint, 0.35F, -0.5F, 0);
            this.animator.rotate(this.calfRightJoint, -0.6F, 0, 0);
            this.animator.rotate(this.calfLeftJoint, -0.6F, 0, 0);
            this.animator.rotate(this.footLeft, 0.25F, 0, 0);
            this.animator.rotate(this.footRight, 0.25F, 0, 0);
            this.animator.endKeyframe();

            this.animator.startKeyframe(10);
            this.animator.rotate(this.shoulderLeft, 0, -0.4F, -0.4F);
            this.animator.rotate(this.shoulderRight, 0, 0.4F, 0.4F);
            this.animator.rotate(this.upperArmRight, 0, 0.5F, 0.5F);
            this.animator.rotate(this.upperArmLeft, 0, -0.5F, 0.5F);
            this.animator.rotate(this.lowerArmLeft, 0, 1, 1.5F);
            this.animator.rotate(this.lowerArmRight, 0, 1, -1.5F);
            this.animator.move(this.axeBase, 0, 30, 3.5F);
            this.animator.rotate(this.axeBase, 0, 0.35F, 0);
            this.animator.rotate(this.handRight, -0.9F, 0, 0);
            this.animator.rotate(this.handRightJoint, -0.2F, 0, -0.88F);
            this.animator.rotate(this.handLeftJoint, 0.8F, -0.3F, 0F);
            this.animator.move(this.rootBox, 0, -5, 0);
            this.animator.rotate(this.thighRightJoint, 0.35F, 0.5F, 0);
            this.animator.rotate(this.thighLeftJoint, 0.35F, -0.5F, 0);
            this.animator.rotate(this.calfRightJoint, -0.6F, 0, 0);
            this.animator.rotate(this.calfLeftJoint, -0.6F, 0, 0);
            this.animator.rotate(this.footLeft, 0.25F, 0, 0);
            this.animator.rotate(this.footRight, 0.25F, 0, 0);
            this.animator.endKeyframe();

            this.animator.setStaticKeyframe(9);

            this.animator.startKeyframe(26);
            this.animator.rotate(this.shoulderLeft, 0, -0.3F, -0.4F);
            this.animator.rotate(this.shoulderRight, 0, 0.2F, 0.35F);
            this.animator.rotate(this.upperArmLeft, 0, -0.5F, 0.5F);
            this.animator.rotate(this.upperArmRight, 0, 0.3F, 0.5F);
            this.animator.rotate(this.lowerArmLeft, 0, 0.3F, 1.5F);
            this.animator.rotate(this.lowerArmRight, 0, 0.7F, -1.3F);
            this.animator.rotate(this.handRight, -0.2F, 0, 0.3F);
            this.animator.rotate(this.handLeftJoint, 1.6F, 0, -0.5F);
            this.animator.rotate(this.axeHandle, 0, 0.8F, 0);
            this.animator.move(this.waistBendController, 26, 0, 0);
            this.animator.endKeyframe();

            this.animator.setStaticKeyframe(20);

            float frame = this.waistBendController.rotationPointX;
            if (entity.getAnimationTick() <= 27) {
                this.thighRightJoint.rotateAngleX += 0.01 * frame * (frame - 13);
                this.calfRightJoint.rotateAngleX -= 0.02 * frame * (frame - 13);
            }
            if (entity.getAnimationTick() >= 28) {
                this.thighLeftJoint.rotateAngleX += 0.01 * (frame - 13) * (frame - 26);
                this.calfLeftJoint.rotateAngleX -= 0.02 * (frame - 13) * (frame - 26);
            }
            this.rootBox.rotateAngleZ -= 0.05 * frame * (frame - 13) * (frame - 26) / 845;
        } else if (entity.getAnimation() == EntityWroughtnaut.DEACTIVATE_ANIMATION) {
            this.animator.setAnimation(EntityWroughtnaut.DEACTIVATE_ANIMATION);
            this.animator.startKeyframe(0);
            this.animator.rotate(this.shoulderLeft, 0, -0.3F, -0.4F);
            this.animator.rotate(this.shoulderRight, 0, 0.2F, 0.35F);
            this.animator.rotate(this.upperArmLeft, 0, -0.5F, 0.5F);
            this.animator.rotate(this.upperArmRight, 0, 0.3F, 0.5F);
            this.animator.rotate(this.lowerArmLeft, 0, 0.3F, 1.5F);
            this.animator.rotate(this.lowerArmRight, 0, 0.7F, -1.3F);
            this.animator.rotate(this.handRight, -0.2F, 0, 0.3F);
            this.animator.rotate(this.handLeftJoint, 1.6F, 0, -0.5F);
            this.animator.rotate(this.axeHandle, 0, 0.8F, 0);
            this.animator.endKeyframe();

            this.animator.startKeyframe(15);
            this.animator.rotate(this.shoulderLeft, 0, -0.4F, -0.4F);
            this.animator.rotate(this.shoulderRight, 0, 0.4F, 0.4F);
            this.animator.rotate(this.upperArmRight, 0, 0.5F, 0.5F);
            this.animator.rotate(this.upperArmLeft, 0, -0.5F, 0.5F);
            this.animator.rotate(this.lowerArmLeft, 0, 1, 1.5F);
            this.animator.rotate(this.lowerArmRight, 0, 1, -1.5F);
            this.animator.move(this.axeBase, 0, 30, 3.5F);
            this.animator.rotate(this.axeBase, 0, 0.35F, 0);
            this.animator.rotate(this.handRight, -0.9F, 0, 0);
            this.animator.rotate(this.handRightJoint, -0.2F, 0, -0.88F);
            this.animator.rotate(this.handLeftJoint, 0.8F, -0.3F, 0F);
            this.animator.rotate(this.neck, 0.5F, 0, 0);
            this.animator.move(this.rootBox, 0, -5, 0);
            this.animator.rotate(this.thighRightJoint, 0.35F, 0.5F, 0);
            this.animator.rotate(this.thighLeftJoint, 0.35F, -0.5F, 0);
            this.animator.rotate(this.calfRightJoint, -0.6F, 0, 0);
            this.animator.rotate(this.calfLeftJoint, -0.6F, 0, 0);
            this.animator.rotate(this.footLeft, 0.25F, 0, 0);
            this.animator.rotate(this.footRight, 0.25F, 0, 0);
            this.animator.endKeyframe();

            this.animator.setStaticKeyframe(20);
        } else if (entity.getAnimation() == EntityWroughtnaut.STOMP_ATTACK_ANIMATION) {
            this.animator.setAnimation(EntityWroughtnaut.STOMP_ATTACK_ANIMATION);
            this.animator.startKeyframe(12);
            this.animator.move(this.waist, 0, -4.5F, 0);
            this.animator.rotate(this.thighRight, 0.25F, 0, 0.15F);
            this.animator.rotate(this.calfRightJoint, -0.55F, 0, 0.1F);
            this.animator.rotate(this.footRight, 0.35F, 0, 0);
            this.animator.rotate(this.stomachJoint, -0.2F, 0, 0);
            this.animator.rotate(this.thighLeft, -0.4F, -0.7F, -0.6F);
            this.animator.rotate(this.groinFront, -0.4F, -0.3F, 0.1F);
            this.animator.rotate(this.groinBack, -0.1F, 0, 0);
            this.animator.rotate(this.neck, -0.25F, 0, 0);
            this.animator.rotate(this.shoulderRightJoint, -0.4F, 0, 0);
            this.animator.rotate(this.upperArmRightJoint, 0.3F, 0.1F, -0.2F);
            this.animator.rotate(this.shoulderLeftJoint, -0.4F, 0, 0);
            this.animator.rotate(this.upperArmLeftJoint, -0.45F, 0.3F, 0.3F);
            this.animator.rotate(this.handLeftJoint, 0, 0, -0.3F);
            this.animator.endKeyframe();
            this.animator.startKeyframe(2);
            this.animator.move(this.waist, 0, 0, -8);
            this.animator.rotate(this.thighLeft, 0, 0, 0);
            this.animator.rotate(this.thighLeftJoint, -0.35F, -0.9F, 0);
            this.animator.rotate(this.calfLeftJoint, -0.45F, 0, 0);
            this.animator.rotate(this.footLeft, 0.75F, 0, 0);
            this.animator.rotate(this.thighRightJoint, 0.6F, 0.25F, -0.3F);
            this.animator.rotate(this.footRight, -0.25F, 0.2F, 0);
            this.animator.rotate(this.calfRightJoint, -0.15F, 0, 0);
            this.animator.rotate(this.groinFront, -0.6F, -0.8F, 0.2F);
            this.animator.rotate(this.groinBack, 0.3F, 0, 0);
            this.animator.rotate(this.neck, 0.6F, 0, 0);
            this.animator.rotate(this.shoulderRightJoint, 0.1F, 0, 0);
            this.animator.rotate(this.upperArmRightJoint, 0.3F, 0, -0.1F);
            this.animator.rotate(this.lowerArmRightJoint, -0.4F, 0.2F, 0);
            this.animator.rotate(this.shoulderLeftJoint, 0.1F, 0, 0);
            this.animator.rotate(this.axeHandle, 0, 0.3F, 0.4F);
            this.animator.endKeyframe();
            this.animator.startKeyframe(3);
            this.animator.move(this.waist, 0, 0, -8);
            this.animator.rotate(this.thighLeft, 0, 0, 0);
            this.animator.rotate(this.thighLeftJoint, -0.35F, -0.9F, 0);
            this.animator.rotate(this.calfLeftJoint, -0.45F, 0, 0);
            this.animator.rotate(this.footLeft, 0.75F, 0, 0);
            this.animator.rotate(this.thighRightJoint, 0.6F, 0.25F, -0.3F);
            this.animator.rotate(this.footRight, -0.25F, 0.2F, 0);
            this.animator.rotate(this.calfRightJoint, -0.15F, 0, 0);
            this.animator.rotate(this.groinFront, -0.8F, -0.8F, 0);
            this.animator.rotate(this.groinBack, -0.2F, 0, 0);
            this.animator.rotate(this.neck, 0.6F, 0, 0);
            this.animator.rotate(this.shoulderLeftJoint, -0.1F, 0, 0);
            this.animator.rotate(this.shoulderRightJoint, -0.1F, 0, 0);
            this.animator.endKeyframe();
            this.animator.startKeyframe(6);
            this.animator.move(this.waist, 0, 0, -8);
            this.animator.rotate(this.thighLeft, 0, 0, 0);
            this.animator.rotate(this.thighLeftJoint, -0.35F, -0.9F, 0);
            this.animator.rotate(this.calfLeftJoint, -0.45F, 0, 0);
            this.animator.rotate(this.footLeft, 0.75F, 0, 0);
            this.animator.rotate(this.thighRightJoint, 0.6F, 0.25F, -0.3F);
            this.animator.rotate(this.footRight, -0.25F, 0.2F, 0);
            this.animator.rotate(this.calfRightJoint, -0.15F, 0, 0);
            this.animator.rotate(this.groinFront, -0.5F, -0.8F, 0);
            this.animator.rotate(this.neck, 0.6F, 0, 0);
            this.animator.endKeyframe();
            this.animator.setStaticKeyframe(2);
            this.animator.startKeyframe(6);
            this.animator.move(this.waist, 0, 0, -5);
            this.animator.rotate(this.thighLeft, -0.15F, 0.1F, 0.0F);
            this.animator.rotate(this.thighLeftJoint, -0.1F, -0.2F, 0.2F);
            this.animator.rotate(this.calfLeftJoint, 0.3F, 0, 0.1F);
            this.animator.rotate(this.footLeft, 0.35F, 0, 0);
            this.animator.rotate(this.thighRightJoint, 0.3F, 0.1F, -0.2F);
            this.animator.rotate(this.footRight, -0.1F, 0.1F, 0);
            this.animator.rotate(this.groinFront, -0.2F, -0.1F, 0);
            this.animator.rotate(this.neck, 0.2F, 0, 0);
            this.animator.endKeyframe();
            this.animator.startKeyframe(4);
            this.animator.rotate(this.groinBack, -0.2F, 0, 0);
            this.animator.endKeyframe();
            this.animator.resetKeyframe(5);
        }
    }
}
