package com.bobmowzie.mowziesmobs.client.model.entity;

import com.bobmowzie.mowziesmobs.client.model.tools.LegArticulator;
import com.bobmowzie.mowziesmobs.server.capability.CapabilityHandler;
import com.bobmowzie.mowziesmobs.server.capability.FrozenCapability;
import com.bobmowzie.mowziesmobs.server.entity.frostmaw.EntityFrostmaw;
import com.bobmowzie.mowziesmobs.client.model.tools.AdvancedModelRenderer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

/**
 * Created by BobMowzie on 5/8/2017.
 */
public class ModelFrostmaw<T extends EntityFrostmaw> extends MowzieEntityModel<T> {
    public AdvancedModelRenderer root;
    public AdvancedModelRenderer waist;
    public AdvancedModelRenderer chest;
    public AdvancedModelRenderer legLeftJoint;
    public AdvancedModelRenderer legRightJoint;
    public AdvancedModelRenderer headJoint;
    public AdvancedModelRenderer armLeftJoint;
    public AdvancedModelRenderer armRightJoint;
    public AdvancedModelRenderer headRotator;
    public AdvancedModelRenderer head;
    public AdvancedModelRenderer jawJoint;
    public AdvancedModelRenderer teethUpper;
    public AdvancedModelRenderer headBack;
    public AdvancedModelRenderer eyeLidRight;
    public AdvancedModelRenderer eyeLidLeft;
    public AdvancedModelRenderer jawRotator;
    public AdvancedModelRenderer jaw;
    public AdvancedModelRenderer tuskLeftJoint;
    public AdvancedModelRenderer tuskRightJoint;
    public AdvancedModelRenderer jawSpikes1;
    public AdvancedModelRenderer jawSpikes2;
    public AdvancedModelRenderer teethLower;
    public AdvancedModelRenderer tuskLeft1;
    public AdvancedModelRenderer tuskLeft2;
    public AdvancedModelRenderer tuskRight1;
    public AdvancedModelRenderer tuskRight2;
    public AdvancedModelRenderer armLeft1;
    public AdvancedModelRenderer armLeftJoint2;
    public AdvancedModelRenderer armLeft2;
    public AdvancedModelRenderer leftHandJoint;
    public AdvancedModelRenderer armLeft2Fur;
    public AdvancedModelRenderer leftHand;
    public AdvancedModelRenderer leftFingersJoint;
    public AdvancedModelRenderer leftThumb;
    public AdvancedModelRenderer leftFingers;
    public AdvancedModelRenderer armRight1;
    public AdvancedModelRenderer armRightJoint2;
    public AdvancedModelRenderer armRight2;
    public AdvancedModelRenderer leftRightJoint;
    public AdvancedModelRenderer armRight2Fur;
    public AdvancedModelRenderer rightHand;
    public AdvancedModelRenderer rightFingersJoint;
    public AdvancedModelRenderer rightThumb;
    public AdvancedModelRenderer rightFingers;
    public AdvancedModelRenderer legLeft1;
    public AdvancedModelRenderer legLeft2;
    public AdvancedModelRenderer leftFoot;
    public AdvancedModelRenderer legLeftFur;
    public AdvancedModelRenderer legRight1;
    public AdvancedModelRenderer legRight2;
    public AdvancedModelRenderer rightFoot;
    public AdvancedModelRenderer legRightFur;
    public AdvancedModelRenderer chestJoint;
    public AdvancedModelRenderer handController;
    public AdvancedModelRenderer swingOffsetController;
    public AdvancedModelRenderer roarController;
    public AdvancedModelRenderer rightHandSocket;
    public AdvancedModelRenderer leftHandSocket;
    public AdvancedModelRenderer mouthSocket;
    public AdvancedModelRenderer iceCrystal;
    public AdvancedModelRenderer iceCrystalJoint;
    public AdvancedModelRenderer iceCrystalHand;
    public AdvancedModelRenderer standUpController;

    public AdvancedModelRenderer headHair;
    public AdvancedModelRenderer hornR1;
    public AdvancedModelRenderer hornR2;
    public AdvancedModelRenderer hornR6;
    public AdvancedModelRenderer hornR7;
    public AdvancedModelRenderer hornR3;
    public AdvancedModelRenderer hornR5;
    public AdvancedModelRenderer hornR4;
    public AdvancedModelRenderer hornL1;
    public AdvancedModelRenderer hornL2;
    public AdvancedModelRenderer hornL3;
    public AdvancedModelRenderer hornL4;
    public AdvancedModelRenderer hornL5;
    public AdvancedModelRenderer hornL6;
    public AdvancedModelRenderer hornL7;
    public AdvancedModelRenderer backHair;
    public AdvancedModelRenderer armRight2FurClump1;
    public AdvancedModelRenderer armRight2FurClump2;
    public AdvancedModelRenderer armLeft2FurClump1;
    public AdvancedModelRenderer armLeft2FurClump2;
    public AdvancedModelRenderer earL;
    public AdvancedModelRenderer earR;

    public ModelFrostmaw() {
        this.textureWidth = 512;
        this.textureHeight = 256;
        this.leftHand = new AdvancedModelRenderer(this, 240, 0);
        this.leftHand.mirror = true;
        this.leftHand.setRotationPoint(-2.0F, 1.0F, -4.5F);
        this.leftHand.addBox(-10.0F, -2.0F, -7.5F, 20, 20, 9, 0.0F);
        setRotateAngle(this.leftHand, 0.0F, 0.3490658503988659F, 0.0F);
        this.legLeft2 = new AdvancedModelRenderer(this, 81, 77);
        this.legLeft2.setRotationPoint(0.0F, 0.0F, -15.0F);
        this.legLeft2.addBox(-7.0F, -6.0F, -12.0F, 14, 14, 16, 0.0F);
        setRotateAngle(this.legLeft2, 1.2217304763960306F, 0.0F, 0.0F);
        this.rightThumb = new AdvancedModelRenderer(this, 63, 0);
        this.rightThumb.mirror = true;
        this.rightThumb.setRotationPoint(10.0F, 0.5F, -5.0F);
        this.rightThumb.addBox(0.0F, -2.5F, -2.5F, 12, 6, 6, 0.0F);
        this.legRight1 = new AdvancedModelRenderer(this, 37, 56);
        this.legRight1.mirror = true;
        this.legRight1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.legRight1.addBox(-4.5F, -4.5F, -17.0F, 9, 11, 21, 0.0F);
        setRotateAngle(this.legRight1, 0.7853981633974483F, 0.6981317007977318F, 0.0F);
        this.tuskRight2 = new AdvancedModelRenderer(this, 0, 80);
        this.tuskRight2.setRotationPoint(0.0F, -6.0F, 0.0F);
        this.tuskRight2.addBox(-10.0F, -2.0F, -2.0F, 11, 4, 4, 0.0F);
        setRotateAngle(this.tuskRight2, -0.8727F, 1.0908F, 0.5236F);
        this.jawRotator = new AdvancedModelRenderer(this, 0, 0);
        this.jawRotator.setRotationPoint(0.0F, 7.353768176172814F, -12.24856180331474F);
        this.jawRotator.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        setRotateAngle(this.jawRotator, 0.0F, 0.7853981633974483F, 0.0F);
        this.leftThumb = new AdvancedModelRenderer(this, 63, 0);
        this.leftThumb.setRotationPoint(-10.0F, 0.5F, -5.0F);
        this.leftThumb.addBox(-12.0F, -2.5F, -2.5F, 12, 6, 6, 0.0F);
        this.armRightJoint2 = new AdvancedModelRenderer(this, 67, 0);
        this.armRightJoint2.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.armRightJoint2.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        setRotateAngle(this.armRightJoint2, -0.1710422666954443F, -0.15079644737231007F, -0.7155849933176751F);
        this.legLeft1 = new AdvancedModelRenderer(this, 37, 56);
        this.legLeft1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.legLeft1.addBox(-4.5F, -4.5F, -17.0F, 9, 11, 21, 0.0F);
        setRotateAngle(this.legLeft1, 0.7853981633974483F, -0.6981317007977318F, 0.0F);
        this.teethUpper = new AdvancedModelRenderer(this, 376, 0);
        this.teethUpper.setRotationPoint(14.0F, 12.0F, -14.0F);
        this.teethUpper.addBox(-13.0F, 0.0F, -13.0F, 26, 6, 26, 0.0F);
        this.leftHandJoint = new AdvancedModelRenderer(this, 8, 0);
        this.leftHandJoint.setRotationPoint(0.0F, 20.0F, 0.0F);
        this.leftHandJoint.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        setRotateAngle(this.leftHandJoint, 0.8726646259971648F, -0.3490658503988659F, -0.2617993877991494F);
        this.jawSpikes2 = new AdvancedModelRenderer(this, 212, 104);
        this.jawSpikes2.setRotationPoint(0.0F, 14.0F, 0.0F);
        this.jawSpikes2.addBox(-19.0F, 0.0F, -19.0F, 38, 9, 38, 0.0F);
        this.rightHand = new AdvancedModelRenderer(this, 240, 0);
        this.rightHand.setRotationPoint(2.0F, 1.0F, -4.5F);
        this.rightHand.addBox(-10.0F, -2.0F, -7.52F, 20, 20, 9, 0.0F);
        setRotateAngle(this.rightHand, 0.0F, -0.3490658503988659F, 0.0F);
        this.armLeft1 = new AdvancedModelRenderer(this, 0, 88);
        this.armLeft1.mirror = true;
        this.armLeft1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.armLeft1.addBox(-8.5F, -10.0F, -8.5F, 17, 34, 17, 0.0F);
        setRotateAngle(this.armLeft1, 0.22759093446006054F, 0.0F, -0.7285004297824331F);
        this.leftFoot = new AdvancedModelRenderer(this, 80, 12);
        this.leftFoot.setRotationPoint(0.0F, 0.2F, -12.0F);
        this.leftFoot.mirror = true;
        this.leftFoot.addBox(-6.5F, -14.75F, -6.3F, 13, 20, 6, 0.0F);
        setRotateAngle(this.leftFoot, -0.4363323129985824F, 0.0F, 0.0F);
        this.rightFingers = new AdvancedModelRenderer(this, 0, 62);
        this.rightFingers.setRotationPoint(0.0F, 0.0F, 7.0F);
        this.rightFingers.addBox(-10.0F, -7.5F, -2.5F, 20, 10, 5, 0.0F);
        this.jawJoint = new AdvancedModelRenderer(this, 0, 0);
        this.jawJoint.setRotationPoint(6.41F, 11.0F, -6.41F);
        this.jawJoint.addBox(0.0F, -5.65F, -4.25F, 0, 0, 0, 0.0F);
        setRotateAngle(this.jawJoint, -0.17453292519943295F, -0.7853981633974483F, 0.0F);
        this.leftFingers = new AdvancedModelRenderer(this, 0, 62);
        this.leftFingers.mirror = true;
        this.leftFingers.setRotationPoint(0.0F, 0.0F, 7.0F);
        this.leftFingers.addBox(-10.0F, -7.5F, -2.5F, 20, 10, 5, 0.0F);
        this.headRotator = new AdvancedModelRenderer(this, 0, 0);
        this.headRotator.setRotationPoint(0.0F, -6.0F, -16.0F);
        this.headRotator.addBox(0.0F, 0.0F, -0.1F, 0, 0, 0, 0.0F);
        setRotateAngle(this.headRotator, 0.0F, 0.7853981633974483F, 0.0F);
        this.armLeft2Fur = new AdvancedModelRenderer(this, 326, 113);
        this.armLeft2Fur.setRotationPoint(0.0F, 28.0F, 0.0F);
        this.armLeft2Fur.addBox(-11.0F, -5.0F, -11.0F, 22, 7, 22, 0.0F);
        this.armRightJoint = new AdvancedModelRenderer(this, 0, 0);
        this.armRightJoint.setRotationPoint(-28.0F, 0.0F, -15.0F);
        this.armRightJoint.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        setRotateAngle(this.armRightJoint, 0.08726646259971647F, 0.0F, 0.0F);
        this.jaw = new AdvancedModelRenderer(this, 242, 52);
        this.jaw.setRotationPoint(0.0F, -5.0F, 0.0F);
        this.jaw.addBox(-19.0F, 0.0F, -19.0F, 38, 14, 38, 0.0F);
        this.eyeLidLeft = new AdvancedModelRenderer(this, 92, 107);
        this.eyeLidLeft.mirror = true;
        this.eyeLidLeft.setRotationPoint(30.01F, -5.0F, -12.5F);
        this.eyeLidLeft.addBox(-7.5F, 0.0F, 0.0F, 15, 14, 0, 0.0F);
        setRotateAngle(this.eyeLidLeft, 0.0F, -1.5707963267948966F, 0.0F);
        this.jawSpikes1 = new AdvancedModelRenderer(this, 380, 48);
        this.jawSpikes1.setRotationPoint(0.0F, 14.0F, 0.0F);
        this.jawSpikes1.addBox(-14.0F, 0.0F, -14.0F, 28, 14, 28, 0.0F);
        this.tuskRightJoint = new AdvancedModelRenderer(this, 0, 0);
        this.tuskRightJoint.setRotationPoint(-12.0F, 0.0F, -19.0F);
        this.tuskRightJoint.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        setRotateAngle(this.tuskRightJoint, 0.0F, 1.5707963267948966F, 0.0F);
        this.armRight2Fur = new AdvancedModelRenderer(this, 326, 113);
        this.armRight2Fur.setRotationPoint(0.0F, 28.0F, 0.0F);
        this.armRight2Fur.addBox(-11.0F, -5.0F, -11.0F, 22, 7, 22, 0.0F);
        this.legRightFur = new AdvancedModelRenderer(this, 144, 77);
        this.legRightFur.mirror = true;
        this.legRightFur.setRotationPoint(0.0F, 0.0F, -3.0F);
        this.legRightFur.addBox(-7.0F, -6.0F, -12.0F, 14, 14, 3, 0.0F);
        this.tuskLeft2 = new AdvancedModelRenderer(this, 0, 80);
        this.tuskLeft2.setRotationPoint(0.0F, -6.0F, 0.0F);
        this.tuskLeft2.addBox(-10.0F, -2.0F, -2.0F, 11, 4, 4, 0.0F);
        setRotateAngle(this.tuskLeft2, 0.8727F, -1.0908F, 0.5236F);
        this.legRightJoint = new AdvancedModelRenderer(this, 0, 0);
        this.legRightJoint.setRotationPoint(-9.0F, 3.14F, 0.0F);
        this.legRightJoint.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        setRotateAngle(this.legRightJoint, -0.6981317007977318F, 0.0F, 0.0F);
        this.tuskRight1 = new AdvancedModelRenderer(this, 68, 109);
        this.tuskRight1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.tuskRight1.addBox(-3.0F, -8.0F, -3.0F, 6, 13, 6, 0.0F);
        setRotateAngle(this.tuskRight1, 0.0F, 0.0F, 2.0943951023931953F);
        this.rightFingersJoint = new AdvancedModelRenderer(this, 0, 47);
        this.rightFingersJoint.setRotationPoint(0.0F, 15.5F, -3.0F);
        this.rightFingersJoint.addBox(-10.0F, -2.5F, 0.0F, 20, 5, 7, 0.0F);
        this.teethLower = new AdvancedModelRenderer(this, 383, 120);
        this.teethLower.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.teethLower.addBox(-14.0F, -6.0F, -17.0F, 31, 6, 31, 0.0F);
        this.chest = new AdvancedModelRenderer(this, 80, 0);
        this.chest.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.chest.addBox(-30.0F, -25.0F, -30.0F, 60, 37, 40, 0.0F);
        setRotateAngle(this.chest, 0.0F, 0.0F, 0.0F);
        this.chestJoint = new AdvancedModelRenderer(this, 80, 0);
        this.chestJoint.setRotationPoint(0.0F, -24.96F, -1.0F);
        setRotateAngle(this.chestJoint, -0.7853981633974483F, 0.0F, 0.0F);
        this.leftFingersJoint = new AdvancedModelRenderer(this, 0, 47);
        this.leftFingersJoint.mirror = true;
        this.leftFingersJoint.setRotationPoint(0.0F, 15.5F, -3.0F);
        this.leftFingersJoint.addBox(-10.0F, -2.5F, 0.0F, 20, 5, 7, 0.0F);
        this.headBack = new AdvancedModelRenderer(this, 0, 139);
        this.headBack.setRotationPoint(14.0F, 12.0F, -14.0F);
        this.headBack.addBox(-16.0F, 0.0F, -16.0F, 32, 6, 32, 0.0F);
        this.legRight2 = new AdvancedModelRenderer(this, 81, 77);
        this.legRight2.mirror = true;
        this.legRight2.setRotationPoint(0.0F, 0.0F, -15.0F);
        this.legRight2.addBox(-7.0F, -6.0F, -12.0F, 14, 14, 16, 0.0F);
        setRotateAngle(this.legRight2, 1.2217304763960306F, 0.0F, 0.0F);
        this.eyeLidRight = new AdvancedModelRenderer(this, 92, 107);
        this.eyeLidRight.setRotationPoint(12.5F, -5.0F, -30.01F);
        this.eyeLidRight.addBox(-7.5F, 0.0F, 0.0F, 15, 14, 0, 0.0F);
        this.armLeftJoint = new AdvancedModelRenderer(this, 0, 0);
        this.armLeftJoint.setRotationPoint(28.0F, 0.0F, -15.0F);
        this.armLeftJoint.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        setRotateAngle(this.armLeftJoint, 0.08726646259971647F, 0.0F, 0.0F);
        this.armLeftJoint2 = new AdvancedModelRenderer(this, 4, 0);
        this.armLeftJoint2.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.armLeftJoint2.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        setRotateAngle(this.armLeftJoint2, -0.1710422666954443F, 0.15079644737231007F, 0.7155849933176751F);
        this.armLeft2 = new AdvancedModelRenderer(this, 112, 109);
        this.armLeft2.mirror = true;
        this.armLeft2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.armLeft2.addBox(-11.0F, -15.0F, -11.0F, 22, 38, 22, 0.0F);
        setRotateAngle(this.armLeft2, -0.8726646259971648F, 0.4363323129985824F, -0.08726646259971647F);
        this.armRight2 = new AdvancedModelRenderer(this, 112, 109);
        this.armRight2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.armRight2.addBox(-11.0F, -15.0F, -11.0F, 22, 38, 22, 0.0F);
        setRotateAngle(this.armRight2, -0.8726646259971648F, -0.4363323129985824F, 0.08726646259971647F);
        this.waist = new AdvancedModelRenderer(this, 0, 0);
        this.waist.setRotationPoint(0.0F, -30.0F, 5.0F);
        this.waist.addBox(-11.5F, -23.0F, -8.5F, 23, 30, 17, 0.0F);
        setRotateAngle(this.waist, 0.6981317007977318F, 0.0F, 0.0F);
        this.head = new AdvancedModelRenderer(this, 280, 0);
        this.head.setRotationPoint(-13.0F, 5.18F, 13.0F);
        this.head.addBox(-2.0F, -5.0F, -30.0F, 32, 17, 32, 0.0F);
        this.armRight1 = new AdvancedModelRenderer(this, 0, 88);
        this.armRight1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.armRight1.addBox(-8.5F, -10.0F, -8.5F, 17, 34, 17, 0.0F);
        setRotateAngle(this.armRight1, 0.22759093446006054F, 0.0F, 0.7285004297824331F);
        this.tuskLeftJoint = new AdvancedModelRenderer(this, 0, 0);
        this.tuskLeftJoint.setRotationPoint(19.0F, 0.0F, 12.0F);
        this.tuskLeftJoint.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        this.headJoint = new AdvancedModelRenderer(this, 0, 0);
        this.headJoint.setRotationPoint(0.0F, -10.0F, -30.0F);
        this.headJoint.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        setRotateAngle(this.headJoint, 0.3490658503988659F, 0.0F, 0.0F);
        this.tuskLeft1 = new AdvancedModelRenderer(this, 68, 109);
        this.tuskLeft1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.tuskLeft1.addBox(-3.0F, -8.0F, -3.0F, 6, 13, 6, 0.0F);
        setRotateAngle(this.tuskLeft1, 0.0F, 0.0F, 2.0943951023931953F);
        this.leftRightJoint = new AdvancedModelRenderer(this, 71, 0);
        this.leftRightJoint.setRotationPoint(0.0F, 20.0F, 0.0F);
        this.leftRightJoint.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        setRotateAngle(this.leftRightJoint, 0.8726646259971648F, 0.3490658503988659F, 0.2617993877991494F);
        this.rightFoot = new AdvancedModelRenderer(this, 80, 12);
        this.rightFoot.mirror = false;
        this.rightFoot.setRotationPoint(0.0F, 0.2F, -12.0F);
        this.rightFoot.addBox(-6.5F, -14.75F, -6.3F, 13, 20, 6, 0.0F);
        setRotateAngle(this.rightFoot, -0.4363323129985824F, 0.0F, 0.0F);
        this.root = new AdvancedModelRenderer(this, 0, 0);
        this.root.setRotationPoint(0.0F, 24.0F, 10.0F);
        this.root.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        this.legLeftFur = new AdvancedModelRenderer(this, 144, 77);
        this.legLeftFur.setRotationPoint(0.0F, 0.0F, -3.0F);
        this.legLeftFur.addBox(-7.0F, -6.0F, -12.0F, 14, 14, 3, 0.0F);
        this.legLeftJoint = new AdvancedModelRenderer(this, 0, 0);
        this.legLeftJoint.setRotationPoint(9.0F, 3.14F, 0.0F);
        this.legLeftJoint.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        setRotateAngle(this.legLeftJoint, -0.6981317007977318F, 0.0F, 0.0F);
        this.iceCrystal = new AdvancedModelRenderer(this, 0, 0);
        this.iceCrystal.setRotationPoint(0, 0, 0);
        this.iceCrystalJoint = new AdvancedModelRenderer(this, 0, 0);
        this.iceCrystalJoint.setRotationPoint(0, 20, -20);
        this.iceCrystalHand = new AdvancedModelRenderer(this, 0, 0);
        this.iceCrystalHand.setRotationPoint(-28.5f, 10, -25.5f);

        this.headHair = new AdvancedModelRenderer(this);
        this.headHair.setRotationPoint(0.0F, -3.0F, -24.0F);
        this.headJoint.addChild(this.headHair);
        setRotateAngle(this.headHair, 0.2182F, 0.0F, 0.0F);
        this.headHair.setTextureOffset(266, 192).addBox(-9.0F, -9.0F, -1.0F, 18, 23, 36, false);
        this.headHair.setScale(0.999f, 1, 1);

        this.hornR1 = new AdvancedModelRenderer(this);
        this.hornR1.setRotationPoint(8.5F, 0.0F, -10.5F);
        this.headJoint.addChild(this.hornR1);
        setRotateAngle(this.hornR1, -0.2327F, 0.7396F, 0.5849F);
        this.hornR1.setTextureOffset(72, 177).addBox(-7.5F, -21.0F, -9.5F, 17, 23, 17, false);

        this.hornR2 = new AdvancedModelRenderer(this);
        this.hornR2.setRotationPoint(0.0F, -14.0F, 2.0F);
        this.hornR1.addChild(this.hornR2);
        setRotateAngle(this.hornR2, -0.3286F, -0.0089F, -0.665F);
        this.hornR2.setTextureOffset(140, 169).addBox(-4.5F, -20.0F, -9.5F, 14, 22, 14, false);

        this.hornR6 = new AdvancedModelRenderer(this);
        this.hornR6.setRotationPoint(0.2003F, 0.5395F, 3.3145F);
        this.hornR2.addChild(this.hornR6);
        setRotateAngle(this.hornR6, -0.397F, 0.3402F, 0.428F);
        this.hornR6.setTextureOffset(0, 226).addBox(-0.5F, -16.0F, -9.5F, 10, 19, 10, false);

        this.hornR7 = new AdvancedModelRenderer(this);
        this.hornR7.setRotationPoint(6.0F, -13.0F, -5.0F);
        this.hornR6.addChild(this.hornR7);
        setRotateAngle(this.hornR7, 0.546F, 0.0303F, 0.2416F);
        this.hornR7.setTextureOffset(455, 0).addBox(-4.6263F, -15.3903F, -1.6013F, 6, 18, 6, false);

        this.hornR3 = new AdvancedModelRenderer(this);
        this.hornR3.setRotationPoint(-3.0F, -14.0F, 2.0F);
        this.hornR2.addChild(this.hornR3);
        setRotateAngle(this.hornR3, -0.3356F, -0.0462F, -0.1705F);
        this.hornR3.setTextureOffset(0, 226).addBox(-0.5F, -20.0F, -9.5F, 10, 20, 10, false);

        this.hornR5 = new AdvancedModelRenderer(this);
        this.hornR5.setRotationPoint(4.0F, -9.0F, -9.0F);
        this.hornR3.addChild(this.hornR5);
        setRotateAngle(this.hornR5, 0.8134F, 0.4571F, -0.1047F);
        this.hornR5.setTextureOffset(405, 193).addBox(-4.6263F, -12.3903F, -0.6013F, 5, 17, 5, false);

        this.hornR4 = new AdvancedModelRenderer(this);
        this.hornR4.setRotationPoint(6.0F, -15.0F, -5.0F);
        this.hornR3.addChild(this.hornR4);
        setRotateAngle(this.hornR4, 0.4363F, -0.3927F, 0.2618F);
        this.hornR4.setTextureOffset(455, 0).addBox(-4.6263F, -19.3903F, -0.6013F, 6, 18, 6, false);

        this.hornL1 = new AdvancedModelRenderer(this);
        this.hornL1.setRotationPoint(-8.5F, 0.0F, -10.5F);
        this.headJoint.addChild(this.hornL1);
        setRotateAngle(this.hornL1, -0.2327F, -0.7396F, -0.5849F);
        this.hornL1.setTextureOffset(72, 177).addBox(-9.5F, -21.0F, -9.5F, 17, 23, 17, true);

        this.hornL2 = new AdvancedModelRenderer(this);
        this.hornL2.setRotationPoint(0.0F, -14.0F, 2.0F);
        this.hornL1.addChild(this.hornL2);
        setRotateAngle(this.hornL2, -0.3286F, 0.0089F, 0.665F);
        this.hornL2.setTextureOffset(140, 169).addBox(-9.5F, -20.0F, -9.5F, 14, 22, 14, true);

        this.hornL3 = new AdvancedModelRenderer(this);
        this.hornL3.setRotationPoint(3.0F, -14.0F, 2.0F);
        this.hornL2.addChild(this.hornL3);
        setRotateAngle(this.hornL3, -0.3356F, 0.0462F, 0.1705F);
        this.hornL3.setTextureOffset(0, 226).addBox(-9.5F, -20.0F, -9.5F, 10, 20, 10, true);

        this.hornL4 = new AdvancedModelRenderer(this);
        this.hornL4.setRotationPoint(-6.0F, -15.0F, -5.0F);
        this.hornL3.addChild(this.hornL4);
        setRotateAngle(this.hornL4, 0.4363F, 0.3927F, -0.2618F);
        this.hornL4.setTextureOffset(455, 0).addBox(-1.3737F, -19.3903F, -0.6013F, 6, 18, 6, true);

        this.hornL5 = new AdvancedModelRenderer(this);
        this.hornL5.setRotationPoint(-4.0F, -9.0F, -9.0F);
        this.hornL3.addChild(this.hornL5);
        setRotateAngle(this.hornL5, 0.8134F, -0.4571F, 0.1047F);
        this.hornL5.setTextureOffset(405, 193).addBox(-0.3737F, -12.3903F, -0.6013F, 5, 17, 5, true);

        this.hornL6 = new AdvancedModelRenderer(this);
        this.hornL6.setRotationPoint(-0.2003F, 0.5395F, 3.3145F);
        this.hornL2.addChild(this.hornL6);
        setRotateAngle(this.hornL6, -0.397F, -0.3402F, -0.428F);
        this.hornL6.setTextureOffset(0, 226).addBox(-9.5F, -16.0F, -9.5F, 10, 19, 10, true);

        this.hornL7 = new AdvancedModelRenderer(this);
        this.hornL7.setRotationPoint(-6.0F, -13.0F, -5.0F);
        this.hornL6.addChild(this.hornL7);
        setRotateAngle(this.hornL7, 0.546F, -0.0303F, -0.2416F);
        this.hornL7.setTextureOffset(455, 0).addBox(-1.3737F, -15.3903F, -1.6013F, 6, 18, 6, true);

        this.earL = new AdvancedModelRenderer(this);
        this.earL.setRotationPoint(-19.0F, 0.0F, -14.0F);
        this.headJoint.addChild(this.earL);
        setRotateAngle(this.earL, -0.3831F, 0.4174F, -0.3721F);
        this.earL.setTextureOffset(396, 102).addBox(-23.0F, -2.0F, -1.0F, 23, 17, 0, false);

        this.earR = new AdvancedModelRenderer(this);
        this.earR.setRotationPoint(19.0F, 0.0F, -14.0F);
        this.headJoint.addChild(this.earR);
        setRotateAngle(this.earR, -0.3831F, -0.4174F, 0.3721F);
        this.earR.setTextureOffset(396, 102).addBox(0.0F, -2.0F, -1.0F, 23, 17, 0, true);

        this.armLeft2FurClump1 = new AdvancedModelRenderer(this);
        this.armLeft2FurClump1.setRotationPoint(10.0F, 23.96F, 10.0F);
        this.armLeft2.addChild(this.armLeft2FurClump1);
        setRotateAngle(this.armLeft2FurClump1, -0.1571F, 0.0F, 0.2269F);
        this.armLeft2FurClump1.setTextureOffset(0, 179).addBox(-18.0F, -26.0F, -18.0F, 18, 26, 18, false);

        this.armLeft2FurClump2 = new AdvancedModelRenderer(this);
        this.armLeft2FurClump2.setRotationPoint(-10.0F, 23.96F, 10.0F);
        this.armLeft2.addChild(this.armLeft2FurClump2);
        setRotateAngle(this.armLeft2FurClump2, -0.3054F, -0.0436F, -0.2531F);
        this.armLeft2FurClump2.setTextureOffset(40, 223).addBox(0.0F, -16.0F, -18.0F, 18, 15, 18, false);

        this.armRight2FurClump1 = new AdvancedModelRenderer(this);
        this.armRight2FurClump1.setRotationPoint(-10.0F, 23.96F, 10.0F);
        this.armRight2.addChild(this.armRight2FurClump1);
        setRotateAngle(this.armRight2FurClump1, -0.1571F, 0.0F, -0.2269F);
        this.armRight2FurClump1.setTextureOffset(0, 179).addBox(0.0F, -26.0F, -18.0F, 18, 26, 18, true);

        this.armRight2FurClump2 = new AdvancedModelRenderer(this);
        this.armRight2FurClump2.setRotationPoint(10.0F, 23.96F, 10.0F);
        this.armRight2.addChild(this.armRight2FurClump2);
        setRotateAngle(this.armRight2FurClump2, -0.3054F, 0.0436F, 0.2531F);
        this.armRight2FurClump2.setTextureOffset(40, 223).addBox(-18.0F, -16.0F, -18.0F, 18, 15, 18, true);

        this.backHair = new AdvancedModelRenderer(this);
        this.backHair.setRotationPoint(0.5F, -24.5373F, -33.3434F);
        this.chest.addChild(this.backHair);
        setRotateAngle(this.backHair, -0.1309F, 0.0F, 0.0F);
        this.backHair.setTextureOffset(374, 180).addBox(-9.5F, -8.2441F, -4.8728F, 18, 25, 51, false);

        this.handController = new AdvancedModelRenderer(this, 0, 0);
        this.swingOffsetController = new AdvancedModelRenderer(this, 0, 0);
        this.roarController = new AdvancedModelRenderer(this, 0, 0);
        this.standUpController = new AdvancedModelRenderer(this, 0, 0);
        this.rightHandSocket = new AdvancedModelRenderer(this);
        this.leftHandSocket = new AdvancedModelRenderer(this);
        this.mouthSocket = new AdvancedModelRenderer(this);

        this.leftHandJoint.addChild(this.leftHand);
        this.legLeft1.addChild(this.legLeft2);
        this.rightHand.addChild(this.rightThumb);
        this.legRightJoint.addChild(this.legRight1);
        this.tuskRight1.addChild(this.tuskRight2);
        this.jawJoint.addChild(this.jawRotator);
        this.leftHand.addChild(this.leftThumb);
        this.armRight1.addChild(this.armRightJoint2);
        this.legLeftJoint.addChild(this.legLeft1);
        this.head.addChild(this.teethUpper);
        this.armLeft2.addChild(this.leftHandJoint);
        this.jaw.addChild(this.jawSpikes2);
        this.leftRightJoint.addChild(this.rightHand);
        this.armLeftJoint.addChild(this.armLeft1);
        this.legLeft2.addChild(this.leftFoot);
        this.rightFingersJoint.addChild(this.rightFingers);
        this.head.addChild(this.jawJoint);
        this.leftFingersJoint.addChild(this.leftFingers);
        this.headJoint.addChild(this.headRotator);
        this.armLeft2.addChild(this.armLeft2Fur);
        this.chest.addChild(this.armRightJoint);
        this.jawRotator.addChild(this.jaw);
        this.head.addChild(this.eyeLidLeft);
        this.jaw.addChild(this.jawSpikes1);
        this.jaw.addChild(this.tuskRightJoint);
        this.armRight2.addChild(this.armRight2Fur);
        this.legRight2.addChild(this.legRightFur);
        this.tuskLeft1.addChild(this.tuskLeft2);
        this.waist.addChild(this.legRightJoint);
        this.tuskRightJoint.addChild(this.tuskRight1);
        this.rightHand.addChild(this.rightFingersJoint);
        this.jaw.addChild(this.teethLower);
        this.waist.addChild(this.chestJoint);
        this.chestJoint.addChild(this.chest);
        this.leftHand.addChild(this.leftFingersJoint);
        this.head.addChild(this.headBack);
        this.legRight1.addChild(this.legRight2);
        this.head.addChild(this.eyeLidRight);
        this.chest.addChild(this.armLeftJoint);
        this.armLeft1.addChild(this.armLeftJoint2);
        this.armLeftJoint2.addChild(this.armLeft2);
        this.armRightJoint2.addChild(this.armRight2);
        this.root.addChild(this.waist);
        this.headRotator.addChild(this.head);
        this.armRightJoint.addChild(this.armRight1);
        this.jaw.addChild(this.tuskLeftJoint);
        this.chest.addChild(this.headJoint);
        this.tuskLeftJoint.addChild(this.tuskLeft1);
        this.armRight2.addChild(this.leftRightJoint);
        this.legRight2.addChild(this.rightFoot);
        this.legLeft2.addChild(this.legLeftFur);
        this.waist.addChild(this.legLeftJoint);
        this.headJoint.addChild(this.iceCrystalJoint);
        this.iceCrystalJoint.addChild(this.iceCrystal);
        this.rightHand.addChild(this.rightHandSocket);
        this.leftHand.addChild(this.leftHandSocket);
        this.headJoint.addChild(this.mouthSocket);

        this.eyeLidLeft.showModel = false;
        this.eyeLidRight.showModel = false;
        this.leftHand.setScale(1.001f, 1.001f, 1.001f);
        this.rightHand.setScale(1.001f, 1.001f, 1.001f);
        this.leftFingersJoint.setScale(1.002f, 1.002f, 1.002f);
        this.rightFingersJoint.setScale(1.002f, 1.002f, 1.002f);
        this.leftThumb.rotateAngleY = (float) (Math.PI);
        this.leftThumb.rotationPointZ = 4;
        this.rightThumb.rotateAngleY = (float) (Math.PI);
        this.rightThumb.rotationPointZ = 4;
        this.iceCrystal.showModel = false;

        this.updateDefaultPose();
    }

    @Override
    public void render(MatrixStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        this.root.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.iceCrystalHand.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }

    @Override
    public void setAngles(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        LegArticulator.articulateQuadruped(entity, entity.legSolver, this.waist, this.headJoint,
                this.legLeft1, this.legLeft2, this.legRight1, this.legRight2, this.armLeftJoint, this.armLeftJoint2, this.armRightJoint, this.armRightJoint2,
                0.6f, 0.6f, -0.65f, -0.65f,
                ageInTicks - entity.age
        );
        this.legLeftJoint.rotateAngleX -= this.waist.rotateAngleX - this.waist.defaultRotationX;
        this.legRightJoint.rotateAngleX -= this.waist.rotateAngleX - this.waist.defaultRotationX;

        super.setAngles(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

        this.mouthSocket.setRotationPoint(0, -10, 8);
        this.mouthSocket.rotationPointZ -= 28;
        this.mouthSocket.rotationPointY += 26;
    }

    @Override
    protected void animate(EntityFrostmaw entity, float limbSwing, float limbSwingAmount, float headYaw, float headPitch, float delta) {
        float frame = entity.age + delta;

        if (entity.getAnimation() == EntityFrostmaw.SWIPE_ANIMATION) {
            this.animator.setAnimation(EntityFrostmaw.SWIPE_ANIMATION);
            if (entity.swingWhichArm) {
                this.animator.startKeyframe(7);
                this.animator.rotate(this.waist, -0.3f, 0.08f, 0);
                this.animator.rotate(this.legRightJoint, 0.3f, 0, 0);
                this.animator.rotate(this.legLeftJoint, 0.3f, 0, 0);
                this.animator.rotate(this.chest, 0, 0.08f, 0.5f);
                this.animator.rotate(this.headJoint, 0.2f, 0, -0.35f);
                this.animator.rotate(this.armLeftJoint, 0.3f, 0, -0.5f);
                this.animator.rotate(this.armLeftJoint, 0.2f, 0, -0.25f);
                this.animator.rotate(this.armLeftJoint2, 0f, 0, 0.18f);
                this.animator.rotate(this.leftHand, -0.1f, 0, 0.05f);

                this.animator.rotate(this.armRightJoint, -1.5f, 0.2f, 0f);
                this.animator.rotate(this.armRight1, 0, 0, 0.5f);
                this.animator.rotate(this.armRight2, 0.4f, 0.4f, 0f);
                this.animator.rotate(this.rightHand, -0.8f, 0.2f, -0.8f);
                this.animator.move(this.handController, 0, 1, 0);
                this.animator.endKeyframe();

                this.animator.setStaticKeyframe(2);

                this.animator.startKeyframe(5);
                this.animator.rotate(this.waist, 0.15f, -0.2f, 0);
                this.animator.rotate(this.chest, 0, -0.2f, -0.5f);
                this.animator.rotate(this.headJoint, -0.15f, 0.35f, 0.4f);
                this.animator.rotate(this.legRightJoint, -0.15f, 0, 0);
                this.animator.rotate(this.legLeftJoint, -0.15f, 0, 0);

                this.animator.rotate(this.armLeftJoint, -0.8f, 0.4f, 0.8f);
                this.animator.move(this.armLeftJoint, 0, 0, 1f);
                this.animator.rotate(this.armLeftJoint, 0.2f, 0, -0.25f);
                this.animator.rotate(this.armLeftJoint2, 1f, 0, -0.2f);
                this.animator.rotate(this.armLeft2, 0, 0.2f, 0);
                this.animator.rotate(this.leftHand, -0.5f, 0, -0.1f);

                this.animator.rotate(this.armRightJoint, -0.8f, -0.4f, 0f);
                this.animator.move(this.armRightJoint, 0, 0, -13);
                this.animator.rotate(this.armRight1, 0, 0, -0.5f);
                this.animator.rotate(this.armRight2, 0.5f, 0.4f, 0f);
                this.animator.rotate(this.rightHand, -0.4f, 0.6f, -0.8f);
                this.animator.move(this.handController, 0, 1, 0);
                this.animator.move(this.swingOffsetController, 7, 0, 0);
                this.animator.endKeyframe();
                this.animator.setStaticKeyframe(4);
                this.animator.resetKeyframe(10);

                float swingFrame = this.swingOffsetController.rotationPointX;
                if (entity.getAnimationTick() <= 14) {
                    this.waist.rotateAngleX += 0.1f * (0.0817 * -swingFrame * (swingFrame - 7));
                    this.legRightJoint.rotateAngleX -= 0.1f * (0.0817 * -swingFrame * (swingFrame - 7));
                    this.legLeftJoint.rotateAngleX -= 0.1f * (0.0817 * -swingFrame * (swingFrame - 7));
                    this.armLeftJoint.rotateAngleX -= 0.2f * (0.0817 * -swingFrame * (swingFrame - 7));
                    this.armLeftJoint.rotationPointY -= 6f * (0.0817 * -swingFrame * (swingFrame - 7));

                    this.armRightJoint.rotateAngleX += 0.4f * (0.0817 * -swingFrame * (swingFrame - 7));
                    this.armRight2.rotateAngleX -= 0.2f * (0.0817 * -swingFrame * (swingFrame - 7));
                    this.armRight2.rotateAngleY += 1f * (0.0817 * -swingFrame * (swingFrame - 7));
                }
            } else {
                this.animator.startKeyframe(7);
                this.animator.rotate(this.waist, -0.3f, -0.08f, 0);
                this.animator.rotate(this.legLeftJoint, 0.3f, 0, 0);
                this.animator.rotate(this.legRightJoint, 0.3f, 0, 0);
                this.animator.rotate(this.chest, 0, -0.08f, -0.5f);
                this.animator.rotate(this.headJoint, 0.2f, 0, 0.35f);
                this.animator.rotate(this.armRightJoint, 0.3f, 0, 0.5f);
                this.animator.rotate(this.armRightJoint, 0.2f, 0, 0.25f);
                this.animator.rotate(this.armRightJoint2, 0f, 0, -0.18f);
                this.animator.rotate(this.rightHand, -0.1f, 0, -0.05f);

                this.animator.rotate(this.armLeftJoint, -1.5f, -0.2f, 0f);
                this.animator.rotate(this.armLeft1, 0, 0, -0.5f);
                this.animator.rotate(this.armLeft2, 0.4f, -0.4f, 0f);
                this.animator.rotate(this.leftHand, -0.8f, -0.2f, 0.8f);
                this.animator.move(this.handController, 1, 0, 0);
                this.animator.endKeyframe();

                this.animator.setStaticKeyframe(2);

                this.animator.startKeyframe(5);
                this.animator.rotate(this.waist, 0.15f, 0.2f, 0);
                this.animator.rotate(this.chest, 0, 0.2f, 0.5f);
                this.animator.rotate(this.headJoint, -0.15f, -0.35f, -0.4f);
                this.animator.rotate(this.legLeftJoint, -0.15f, 0, 0);
                this.animator.rotate(this.legRightJoint, -0.15f, 0, 0);

                this.animator.rotate(this.armRightJoint, -0.8f, -0.4f, -0.8f);
                this.animator.move(this.armRightJoint, 0, 0, -1f);
                this.animator.rotate(this.armRightJoint, 0.2f, 0, 0.25f);
                this.animator.rotate(this.armRightJoint2, 1f, 0, 0.2f);
                this.animator.rotate(this.armRight2, 0, -0.2f, 0);
                this.animator.rotate(this.rightHand, -0.5f, 0, 0.1f);

                this.animator.rotate(this.armLeftJoint, -0.8f, 0.4f, 0f);
                this.animator.move(this.armLeftJoint, 0, 0, -13);
                this.animator.rotate(this.armLeft1, 0, 0, 0.5f);
                this.animator.rotate(this.armLeft2, 0.5f, -0.4f, 0f);
                this.animator.rotate(this.leftHand, -0.4f, -0.6f, 0.8f);
                this.animator.move(this.handController, 1, 0, 0);
                this.animator.move(this.swingOffsetController, 7, 0, 0);
                this.animator.endKeyframe();
                this.animator.setStaticKeyframe(4);
                this.animator.resetKeyframe(10);

                float swingFrame = this.swingOffsetController.rotationPointX;
                if (entity.getAnimationTick() <= 14) {
                    this.waist.rotateAngleX += 0.1f * (0.0817 * -swingFrame * (swingFrame - 7));
                    this.legLeftJoint.rotateAngleX -= 0.1f * (0.0817 * -swingFrame * (swingFrame - 7));
                    this.legRightJoint.rotateAngleX -= 0.1f * (0.0817 * -swingFrame * (swingFrame - 7));
                    this.armRightJoint.rotateAngleX -= 0.2f * (0.0817 * -swingFrame * (swingFrame - 7));
                    this.armRightJoint.rotationPointY -= 6f * (0.0817 * -swingFrame * (swingFrame - 7));

                    this.armLeftJoint.rotateAngleX += 0.4f * (0.0817 * -swingFrame * (swingFrame - 7));
                    this.armLeft2.rotateAngleX -= 0.2f * (0.0817 * -swingFrame * (swingFrame - 7));
                    this.armLeft2.rotateAngleY -= 1f * (0.0817 * -swingFrame * (swingFrame - 7));
                }
            }
        }

        if (entity.getAnimation() == EntityFrostmaw.SWIPE_TWICE_ANIMATION) {
            this.animator.setAnimation(EntityFrostmaw.SWIPE_TWICE_ANIMATION);
            if (entity.swingWhichArm) {
                this.animator.startKeyframe(7);
                this.animator.rotate(this.waist, -0.3f, 0.08f, 0);
                this.animator.rotate(this.legRightJoint, 0.3f, 0, 0);
                this.animator.rotate(this.legLeftJoint, 0.3f, 0, 0);
                this.animator.rotate(this.chest, 0, 0.08f, 0.5f);
                this.animator.rotate(this.headJoint, 0.2f, 0, -0.35f);
                this.animator.rotate(this.armLeftJoint, 0.3f, 0, -0.5f);
                this.animator.rotate(this.armLeftJoint, 0.2f, 0, -0.25f);
                this.animator.rotate(this.armLeftJoint2, 0f, 0, 0.18f);
                this.animator.rotate(this.leftHand, -0.1f, 0, 0.05f);

                this.animator.rotate(this.armRightJoint, -1.5f, 0.2f, 0f);
                this.animator.rotate(this.armRight1, 0, 0, 0.5f);
                this.animator.rotate(this.armRight2, 0.4f, 0.4f, 0f);
                this.animator.rotate(this.rightHand, -0.8f, 0.2f, -0.8f);
                this.animator.move(this.handController, 0, 1, 0);
                this.animator.endKeyframe();

                this.animator.setStaticKeyframe(2);

                this.animator.startKeyframe(6);
                this.animator.rotate(this.waist, -0.15f, -0.2f, 0);
                this.animator.rotate(this.chest, 0, -0.2f, -0.5f);
                this.animator.rotate(this.headJoint, 0.15f, 0.35f, 0.4f);
                this.animator.rotate(this.legRightJoint, 0.15f, 0, 0);
                this.animator.rotate(this.legLeftJoint, 0.15f, 0, 0);

                this.animator.rotate(this.armLeftJoint, -1.5f, -0.2f, 0f);
                this.animator.rotate(this.armLeft1, 0, 0, -0.8f);
                this.animator.rotate(this.armLeft2, 0.4f, -0.4f, 0f);
                this.animator.rotate(this.leftHand, -0.8f, -0.2f, 0.8f);
                this.animator.move(this.handController, 1, 0, 0);

                this.animator.rotate(this.armRightJoint, -0.5f, -0.4f, 0f);
                this.animator.move(this.armRightJoint, 0, 0, -13);
                this.animator.rotate(this.armRight1, 0, 0, -0.5f);
                this.animator.rotate(this.armRight2, 0.5f, 0.4f, 0f);
                this.animator.rotate(this.rightHand, -0.4f, 0.6f, -0.8f);
                this.animator.move(this.handController, 0, 1, 0);
                this.animator.move(this.swingOffsetController, 7, 0, 0);
                this.animator.endKeyframe();

                this.animator.setStaticKeyframe(4);

                this.animator.startKeyframe(6);
                this.animator.rotate(this.waist, 0.15f, 0.2f, 0);
                this.animator.rotate(this.chest, 0, 0.2f, 0.5f);
                this.animator.rotate(this.headJoint, -0.15f, -0.35f, -0.4f);
                this.animator.rotate(this.legLeftJoint, -0.15f, 0, 0);
                this.animator.rotate(this.legRightJoint, -0.15f, 0, 0);

                this.animator.rotate(this.armRightJoint, -0.8f, -0.4f, -0.8f);
                this.animator.move(this.armRightJoint, 0, 0, 1f);
                this.animator.rotate(this.armRightJoint, 0.2f, 0, 0.25f);
                this.animator.rotate(this.armRightJoint2, 1f, 0, 0.2f);
                this.animator.rotate(this.armRight2, 0, -0.2f, 0);
                this.animator.rotate(this.rightHand, -0.5f, 0, 0.1f);

                this.animator.rotate(this.armLeftJoint, -0.8f, 0.4f, 0f);
                this.animator.move(this.armLeftJoint, 0, 0, -13);
                this.animator.rotate(this.armLeft1, 0, 0, 0.5f);
                this.animator.rotate(this.armLeft2, 0.5f, -0.4f, 0f);
                this.animator.rotate(this.leftHand, -0.4f, -0.6f, 0.8f);
                this.animator.move(this.handController, 1, 0, 0);
                this.animator.move(this.swingOffsetController, 0, 0, 0);
                this.animator.endKeyframe();
                this.animator.setStaticKeyframe(4);
                this.animator.resetKeyframe(10);

                float swingFrame = this.swingOffsetController.rotationPointX;
                if (entity.getAnimationTick() <= 15) {
                    this.waist.rotateAngleX += 0.3f * (0.0817 * -swingFrame * (swingFrame - 7));
                    this.headJoint.rotateAngleX -= 0.3f * (0.0817 * -swingFrame * (swingFrame - 7));
                    this.legRightJoint.rotateAngleX -= 0.3f * (0.0817 * -swingFrame * (swingFrame - 7));
                    this.legLeftJoint.rotateAngleX -= 0.3f * (0.0817 * -swingFrame * (swingFrame - 7));
                    this.armLeftJoint.rotateAngleX -= 0.2f * (0.0817 * -swingFrame * (swingFrame - 7));
                    this.armLeftJoint.rotationPointY -= 6f * (0.0817 * -swingFrame * (swingFrame - 7));

                    this.armRightJoint.rotateAngleX += 0.4f * (0.0817 * -swingFrame * (swingFrame - 7));
                    this.armRight2.rotateAngleX -= 0.2f * (0.0817 * -swingFrame * (swingFrame - 7));
                    this.armRight2.rotateAngleY += 1f * (0.0817 * -swingFrame * (swingFrame - 7));
                }
                if (entity.getAnimationTick() > 15 && entity.getAnimationTick() <= 25) {
                    this.waist.rotateAngleX += 0.1f * (0.0817 * -swingFrame * (swingFrame - 7));
                    this.legLeftJoint.rotateAngleX -= 0.1f * (0.0817 * -swingFrame * (swingFrame - 7));
                    this.legRightJoint.rotateAngleX -= 0.1f * (0.0817 * -swingFrame * (swingFrame - 7));
                    this.armRightJoint.rotateAngleX -= 0.2f * (0.0817 * -swingFrame * (swingFrame - 7));
                    this.armRightJoint.rotationPointY -= 6f * (0.0817 * -swingFrame * (swingFrame - 7));

                    this.armLeftJoint.rotateAngleX += 0.4f * (0.0817 * -swingFrame * (swingFrame - 7));
                    this.armLeft2.rotateAngleX -= 0.2f * (0.0817 * -swingFrame * (swingFrame - 7));
                    this.armLeft2.rotateAngleY -= 1f * (0.0817 * -swingFrame * (swingFrame - 7));
                }
            } else {
                this.animator.startKeyframe(7);
                this.animator.rotate(this.waist, -0.3f, -0.08f, 0);
                this.animator.rotate(this.legLeftJoint, 0.3f, 0, 0);
                this.animator.rotate(this.legRightJoint, 0.3f, 0, 0);
                this.animator.rotate(this.chest, 0, -0.08f, -0.5f);
                this.animator.rotate(this.headJoint, 0.2f, 0, 0.35f);
                this.animator.rotate(this.armRightJoint, 0.3f, 0, 0.5f);
                this.animator.rotate(this.armRightJoint, 0.2f, 0, 0.25f);
                this.animator.rotate(this.armRightJoint2, 0f, 0, -0.18f);
                this.animator.rotate(this.rightHand, -0.1f, 0, -0.05f);

                this.animator.rotate(this.armLeftJoint, -1.5f, -0.2f, 0f);
                this.animator.rotate(this.armLeft1, 0, 0, -0.5f);
                this.animator.rotate(this.armLeft2, 0.4f, -0.4f, 0f);
                this.animator.rotate(this.leftHand, -0.8f, -0.2f, 0.8f);
                this.animator.move(this.handController, 1, 0, 0);
                this.animator.endKeyframe();

                this.animator.setStaticKeyframe(2);

                this.animator.startKeyframe(6);
                this.animator.rotate(this.waist, -0.15f, 0.2f, 0);
                this.animator.rotate(this.chest, 0, 0.2f, 0.5f);
                this.animator.rotate(this.headJoint, 0.15f, -0.35f, -0.4f);
                this.animator.rotate(this.legLeftJoint, 0.15f, 0, 0);
                this.animator.rotate(this.legRightJoint, 0.15f, 0, 0);

                this.animator.rotate(this.armRightJoint, -1.5f, 0.2f, 0f);
                this.animator.rotate(this.armRight1, 0, 0, 0.8f);
                this.animator.rotate(this.armRight2, 0.4f, 0.4f, 0f);
                this.animator.rotate(this.rightHand, -0.8f, 0.2f, -0.8f);
                this.animator.move(this.handController, 0, 1, 0);

                this.animator.rotate(this.armLeftJoint, -0.5f, 0.4f, 0f);
                this.animator.move(this.armLeftJoint, 0, 0, -13);
                this.animator.rotate(this.armLeft1, 0, 0, 0.5f);
                this.animator.rotate(this.armLeft2, 0.5f, -0.4f, 0f);
                this.animator.rotate(this.leftHand, -0.4f, -0.6f, 0.8f);
                this.animator.move(this.handController, 1, 0, 0);
                this.animator.move(this.swingOffsetController, 7, 0, 0);
                this.animator.endKeyframe();

                this.animator.setStaticKeyframe(4);

                this.animator.startKeyframe(6);
                this.animator.rotate(this.waist, 0.15f, -0.2f, 0);
                this.animator.rotate(this.chest, 0, -0.2f, -0.5f);
                this.animator.rotate(this.headJoint, -0.15f, 0.35f, 0.4f);
                this.animator.rotate(this.legRightJoint, -0.15f, 0, 0);
                this.animator.rotate(this.legLeftJoint, -0.15f, 0, 0);

                this.animator.rotate(this.armLeftJoint, -0.8f, 0.4f, 0.8f);
                this.animator.move(this.armLeftJoint, 0, 0, 1f);
                this.animator.rotate(this.armLeftJoint, 0.2f, 0, -0.25f);
                this.animator.rotate(this.armLeftJoint2, 1f, 0, -0.2f);
                this.animator.rotate(this.armLeft2, 0, 0.2f, 0);
                this.animator.rotate(this.leftHand, -0.5f, 0, -0.1f);

                this.animator.rotate(this.armRightJoint, -0.8f, -0.4f, 0f);
                this.animator.move(this.armRightJoint, 0, 0, -13);
                this.animator.rotate(this.armRight1, 0, 0, -0.5f);
                this.animator.rotate(this.armRight2, 0.5f, 0.4f, 0f);
                this.animator.rotate(this.rightHand, -0.4f, 0.6f, -0.8f);
                this.animator.move(this.handController, 0, 1, 0);
                this.animator.move(this.swingOffsetController, 0, 0, 0);
                this.animator.endKeyframe();
                this.animator.setStaticKeyframe(4);
                this.animator.resetKeyframe(10);

                float swingFrame = this.swingOffsetController.rotationPointX;
                if (entity.getAnimationTick() <= 15) {
                    this.waist.rotateAngleX += 0.3f * (0.0817 * -swingFrame * (swingFrame - 7));
                    this.headJoint.rotateAngleX -= 0.3f * (0.0817 * -swingFrame * (swingFrame - 7));
                    this.legLeftJoint.rotateAngleX -= 0.3f * (0.0817 * -swingFrame * (swingFrame - 7));
                    this.legRightJoint.rotateAngleX -= 0.3f * (0.0817 * -swingFrame * (swingFrame - 7));
                    this.armRightJoint.rotateAngleX -= 0.2f * (0.0817 * -swingFrame * (swingFrame - 7));
                    this.armRightJoint.rotationPointY -= 6f * (0.0817 * -swingFrame * (swingFrame - 7));

                    this.armLeftJoint.rotateAngleX += 0.4f * (0.0817 * -swingFrame * (swingFrame - 7));
                    this.armLeft2.rotateAngleX -= 0.2f * (0.0817 * -swingFrame * (swingFrame - 7));
                    this.armLeft2.rotateAngleY -= 1f * (0.0817 * -swingFrame * (swingFrame - 7));
                }
                if (entity.getAnimationTick() > 15 && entity.getAnimationTick() <= 25) {
                    this.waist.rotateAngleX += 0.1f * (0.0817 * -swingFrame * (swingFrame - 7));
                    this.legRightJoint.rotateAngleX -= 0.1f * (0.0817 * -swingFrame * (swingFrame - 7));
                    this.legLeftJoint.rotateAngleX -= 0.1f * (0.0817 * -swingFrame * (swingFrame - 7));
                    this.armLeftJoint.rotateAngleX -= 0.2f * (0.0817 * -swingFrame * (swingFrame - 7));
                    this.armLeftJoint.rotationPointY -= 6f * (0.0817 * -swingFrame * (swingFrame - 7));

                    this.armRightJoint.rotateAngleX += 0.4f * (0.0817 * -swingFrame * (swingFrame - 7));
                    this.armRight2.rotateAngleX -= 0.2f * (0.0817 * -swingFrame * (swingFrame - 7));
                    this.armRight2.rotateAngleY += 1f * (0.0817 * -swingFrame * (swingFrame - 7));
                }
            }
        }

        if (entity.getAnimation() == EntityFrostmaw.ROAR_ANIMATION) {
            this.animator.setAnimation(EntityFrostmaw.ROAR_ANIMATION);
            this.animator.startKeyframe(8);
            this.animator.rotate(this.waist, 0.2f, 0, 0);
            this.animator.rotate(this.legRightJoint, -0.2f, 0, 0);
            this.animator.rotate(this.legLeftJoint, -0.2f, 0, 0);
            this.animator.rotate(this.headJoint, 0.3f, 0, 0);

            this.animator.rotate(this.armLeftJoint, 0.15f, 0, 0);
            this.animator.move(this.armLeftJoint, 0, 2, 0);
            this.animator.rotate(this.armLeftJoint2, -0.6f, 0, 0);
            this.animator.rotate(this.leftHand, 0.3f, 0, 0.15f);
            this.animator.rotate(this.armRightJoint, 0.15f, 0, 0);
            this.animator.move(this.armRightJoint, 0, 2, 0);
            this.animator.rotate(this.armRightJoint2, -0.6f, 0, 0);
            this.animator.rotate(this.rightHand, 0.3f, 0, -0.15f);
            this.animator.endKeyframe();

            this.animator.setStaticKeyframe(4);

            this.animator.startKeyframe(5);
            this.animator.rotate(this.waist, -0.2f, 0, 0);
            this.animator.rotate(this.legRightJoint, 0.2f, 0, 0);
            this.animator.rotate(this.legLeftJoint, 0.2f, 0, 0);
            this.animator.rotate(this.headJoint, -0.3f, 0, 0);
            this.animator.rotate(this.jawJoint, 1.3f, 0, 0);
            this.animator.move(this.roarController, 1, 1, 0);

            this.animator.rotate(this.armLeftJoint, -0.4f, 0, 0);
            this.animator.rotate(this.armLeftJoint2, 0.9f, 0, 0);
            this.animator.rotate(this.leftHand, -0.3f, 0, -0.15f);
            this.animator.rotate(this.armRightJoint, -0.4f, 0, 0);
            this.animator.rotate(this.armRightJoint2, 0.9f, 0, 0);
            this.animator.rotate(this.rightHand, -0.3f, 0, 0.15f);
            this.animator.endKeyframe();
            this.animator.setStaticKeyframe(50);
            this.animator.resetKeyframe(8);
        }

        if (entity.getAnimation() == EntityFrostmaw.ICE_BREATH_ANIMATION) {
            this.animator.setAnimation(EntityFrostmaw.ICE_BREATH_ANIMATION);
            this.animator.startKeyframe(10);
            this.animator.rotate(this.waist, -0.2f, 0, 0);
            this.animator.rotate(this.legRightJoint, 0.2f, 0, 0);
            this.animator.rotate(this.legLeftJoint, 0.2f, 0, 0);
            this.animator.rotate(this.headJoint, 0.6f, 0, 0);

            this.animator.rotate(this.armLeftJoint, -0.4f, 0, 0);
            this.animator.rotate(this.armLeftJoint2, 0.9f, 0, 0);
            this.animator.rotate(this.leftHand, -0.3f, 0, -0.15f);
            this.animator.rotate(this.armRightJoint, -0.4f, 0, 0);
            this.animator.rotate(this.armRightJoint2, 0.9f, 0, 0);
            this.animator.rotate(this.rightHand, -0.3f, 0, 0.15f);
            this.animator.endKeyframe();

            this.animator.setStaticKeyframe(4);

            this.animator.startKeyframe(5);
            this.animator.rotate(this.waist, 0.2f, 0, 0);
            this.animator.rotate(this.legRightJoint, -0.2f, 0, 0);
            this.animator.rotate(this.legLeftJoint, -0.2f, 0, 0);
            this.animator.rotate(this.headJoint, -0.9f, 0, 0);
            this.animator.rotate(this.jawJoint, 1.6f, 0, 0);

            this.animator.rotate(this.armLeftJoint, 0.15f, 0, 0);
            this.animator.move(this.armLeftJoint, 0, 2, 0);
            this.animator.rotate(this.armLeftJoint2, -0.6f, 0, 0);
            this.animator.rotate(this.leftHand, 0.3f, 0, 0.15f);
            this.animator.rotate(this.armRightJoint, 0.15f, 0, 0);
            this.animator.move(this.armRightJoint, 0, 2, 0);
            this.animator.rotate(this.armRightJoint2, -0.6f, 0, 0);
            this.animator.rotate(this.rightHand, 0.3f, 0, -0.15f);
            this.animator.move(this.roarController, 1, 1, 0);
            this.animator.endKeyframe();
            this.animator.setStaticKeyframe(65);
            this.animator.resetKeyframe(7);
            this.iceCrystal.rotateAngleY += frame;
        }

        if (entity.getAnimation() == EntityFrostmaw.ICE_BALL_ANIMATION) {
            this.animator.setAnimation(EntityFrostmaw.ICE_BALL_ANIMATION);

            this.animator.startKeyframe(20);
            this.animator.rotate(this.waist, -0.2f, 0, 0);
            this.animator.rotate(this.legRightJoint, 0.2f, 0, 0);
            this.animator.rotate(this.legLeftJoint, 0.2f, 0, 0);
            this.animator.rotate(this.headJoint, -0.1f, 0, 0);
            this.animator.move(this.headJoint, 0, 0, -4);
            this.animator.rotate(this.jawJoint, 1.4f, 0, 0);

            this.animator.rotate(this.armLeftJoint, -0.4f, 0, 0);
            this.animator.rotate(this.armLeftJoint2, 0.9f, 0, 0);
            this.animator.rotate(this.leftHand, -0.3f, 0, -0.15f);
            this.animator.rotate(this.armRightJoint, -0.4f, 0, 0);
            this.animator.rotate(this.armRightJoint2, 0.9f, 0, 0);
            this.animator.rotate(this.rightHand, -0.3f, 0, 0.15f);
            this.animator.endKeyframe();

            this.animator.setStaticKeyframe(8);

            this.animator.startKeyframe(4);
            this.animator.rotate(this.waist, -0.2f, 0, 0);
            this.animator.rotate(this.legRightJoint, 0.2f, 0, 0);
            this.animator.rotate(this.legLeftJoint, 0.2f, 0, 0);
            this.animator.rotate(this.headJoint, 0.6f, 0, 0);

            this.animator.rotate(this.armLeftJoint, -0.4f, 0, 0);
            this.animator.rotate(this.armLeftJoint2, 0.9f, 0, 0);
            this.animator.rotate(this.leftHand, -0.3f, 0, -0.15f);
            this.animator.rotate(this.armRightJoint, -0.4f, 0, 0);
            this.animator.rotate(this.armRightJoint2, 0.9f, 0, 0);
            this.animator.rotate(this.rightHand, -0.3f, 0, 0.15f);
            this.animator.endKeyframe();

            this.animator.setStaticKeyframe(2);

            this.animator.startKeyframe(3);
            this.animator.rotate(this.waist, 0.2f, 0, 0);
            this.animator.rotate(this.legRightJoint, -0.2f, 0, 0);
            this.animator.rotate(this.legLeftJoint, -0.2f, 0, 0);
            this.animator.rotate(this.headJoint, -0.9f, 0, 0);
            this.animator.rotate(this.jawJoint, 1.6f, 0, 0);

            this.animator.rotate(this.armLeftJoint, 0.15f, 0, 0);
            this.animator.move(this.armLeftJoint, 0, 2, 0);
            this.animator.rotate(this.armLeftJoint2, -0.6f, 0, 0);
            this.animator.rotate(this.leftHand, 0.3f, 0, 0.15f);
            this.animator.rotate(this.armRightJoint, 0.15f, 0, 0);
            this.animator.move(this.armRightJoint, 0, 2, 0);
            this.animator.rotate(this.armRightJoint2, -0.6f, 0, 0);
            this.animator.rotate(this.rightHand, 0.3f, 0, -0.15f);
            this.animator.endKeyframe();
            this.animator.setStaticKeyframe(5);
            this.animator.resetKeyframe(7);
            this.iceCrystal.rotateAngleY += frame;
        }

        if (entity.getAnimation() == EntityFrostmaw.ACTIVATE_ANIMATION) {
            this.eyeLidLeft.showModel = false;
            this.eyeLidRight.showModel = false;
            this.animator.setAnimation(EntityFrostmaw.ACTIVATE_ANIMATION);

            this.animator.startKeyframe(0);
            this.animator.rotate(this.root, 0, 0, 0.9f);
            this.animator.move(this.root, -20, 0, 0);
            this.animator.rotate(this.chest, 0.2f, 0.2f, -0.1f);
            this.animator.rotate(this.headJoint, 0, 0, -0.3f);
            this.animator.rotate(this.armRightJoint, 0.2f, -0.5f, -0.8f);
            this.animator.move(this.armRightJoint, 0, 8, 0);
            this.animator.rotate(this.armRightJoint2, 0f, 0.5f, 0);
            this.animator.rotate(this.armRight2, 0, 0.2f, 0);
            this.animator.rotate(this.armLeftJoint, -1.3f, 0, 0);
            this.animator.rotate(this.armLeft1, -0.8f, 0, 0);
            this.animator.rotate(this.armLeftJoint2, 1.3f, 0, 0.3f);
            this.animator.rotate(this.leftHand, -0.65f, 0, 0);
            this.animator.move(this.handController, 0.8f, 0.8f, 0);
            this.animator.rotate(this.rightHand, -1.7f, 0.8f, -3.3f);
            this.animator.rotate(this.legLeftJoint, 0.7f, 0, 0);
            this.animator.rotate(this.legLeft1, -0.6f, 0, 0);
            this.animator.rotate(this.legLeft2, -0.6f, 0, 0.2f);
            this.animator.rotate(this.legLeftJoint, 0, 0.9f, 0.2f);
            this.animator.rotate(this.legLeft2, 0, 0.3f, 0);
            this.animator.rotate(this.legRightJoint, 0, -0.4f, 0);
            this.animator.rotate(this.legRight1, 1.1f, 0, 0.45f);
            this.animator.rotate(this.legRight2, -0.3f, 0, 0);
            this.animator.endKeyframe();

            this.animator.startKeyframe(5);
            this.animator.rotate(this.root, 0, 0, 0.9f);
            this.animator.move(this.root, -20, 0, 0);
            this.animator.rotate(this.chest, 0.2f, 0.2f, -0.1f);
            this.animator.rotate(this.headJoint, 0, 0, -0.3f);
            this.animator.rotate(this.armRightJoint, 0.2f, -0.5f, -0.8f);
            this.animator.move(this.armRightJoint, 0, 8, 0);
            this.animator.rotate(this.armRightJoint2, 0f, 0.5f, 0);
            this.animator.rotate(this.armRight2, 0, 0.2f, 0);
            this.animator.rotate(this.armLeftJoint, -1.3f, 0, 0);
            this.animator.rotate(this.armLeft1, -0.8f, 0, 0);
            this.animator.rotate(this.armLeftJoint2, 1.3f, 0, 0.3f);
            this.animator.rotate(this.leftHand, -0.65f, 0, 0);
            this.animator.move(this.handController, 0.8f, 0.8f, 0);
            this.animator.rotate(this.rightHand, -1.7f, 0.8f, -3.3f);
            this.animator.rotate(this.legLeftJoint, 0.7f, 0, 0);
            this.animator.rotate(this.legLeft1, -0.6f, 0, 0);
            this.animator.rotate(this.legLeft2, -0.6f, 0, 0.2f);
            this.animator.rotate(this.legLeftJoint, 0, 0.9f, 0.2f);
            this.animator.rotate(this.legLeft2, 0, 0.3f, 0);
            this.animator.rotate(this.legRightJoint, 0, -0.4f, 0);
            this.animator.rotate(this.legRight1, 1.1f, 0, 0.45f);
            this.animator.rotate(this.legRight2, -0.3f, 0, 0);

            this.animator.move(this.handController, 0, -0.8f, 0);
            this.animator.endKeyframe();
            this.animator.setStaticKeyframe(10);

            this.animator.startKeyframe(7);
            this.animator.rotate(this.root, 0, 0, 0.9f);
            this.animator.move(this.root, -20, 0, 0);
            this.animator.rotate(this.chest, 0.2f, 0.2f, -0.1f);
            this.animator.rotate(this.headJoint, 0, 0, -0.3f);
            this.animator.rotate(this.armRightJoint, 0.2f, -0.5f, -0.8f);
            this.animator.move(this.armRightJoint, 0, 8, 0);
            this.animator.rotate(this.armRightJoint2, 0f, 0.5f, 0);
            this.animator.rotate(this.armRight2, 0, 0.2f, 0);
            this.animator.rotate(this.armLeftJoint, -1.3f, 0, 0);
            this.animator.rotate(this.armLeft1, -0.8f, 0, 0);
            this.animator.rotate(this.armLeftJoint2, 1.3f, 0, 0.3f);
            this.animator.rotate(this.leftHand, -0.65f, 0, 0);
            this.animator.move(this.handController, 0.8f, 0.8f, 0);
            this.animator.rotate(this.rightHand, -1.7f, 0.8f, -3.3f);
            this.animator.rotate(this.legLeftJoint, 0.7f, 0, 0);
            this.animator.rotate(this.legLeft1, -0.6f, 0, 0);
            this.animator.rotate(this.legLeft2, -0.6f, 0, 0.2f);
            this.animator.rotate(this.legLeftJoint, 0, 0.9f, 0.2f);
            this.animator.rotate(this.legLeft2, 0, 0.3f, 0);
            this.animator.rotate(this.legRightJoint, 0, -0.4f, 0);
            this.animator.rotate(this.legRight1, 1.1f, 0, 0.45f);
            this.animator.rotate(this.legRight2, -0.3f, 0, 0);

            this.animator.move(this.handController, 0, -0.8f, 0);
            this.animator.rotate(this.armRightJoint, 0.1f, 0.2f, 0.8f);
            this.animator.move(this.armRightJoint, 0, -8, 0);
            this.animator.rotate(this.armRight1, -0.1f, 0.2f, 0.4f);
            this.animator.rotate(this.armRight2, 0f, 0.4f, 0f);
            this.animator.rotate(this.armRightJoint2, 0.3f, -0.5f, 0.6f);
            this.animator.rotate(this.rightHand, 0f, 0f, 0.6f);
            this.animator.rotate(this.waist, 0, 0.3f, 0);
            this.animator.rotate(this.legLeftJoint, 0, -0.3f, 0.2f);
            this.animator.rotate(this.legRightJoint, 0, -0.3f, 0.2f);
            this.animator.rotate(this.armLeftJoint, 0f, -0.7f, 0);
            this.animator.rotate(this.armLeft2, -0.15f, 0.35f, 0);
            this.animator.rotate(this.headJoint, -0.4f, 0.3f, 0);
            this.animator.move(this.roarController, 0, 1f, 0);
            this.animator.rotate(this.jawJoint, 1.5f, 0, 0);
            this.animator.endKeyframe();
            this.animator.setStaticKeyframe(2);

            this.animator.startKeyframe(5);
            this.animator.rotate(this.root, 0, 0, 0.9f);
            this.animator.move(this.root, -20, 0, 0);
            this.animator.rotate(this.chest, 0.2f, 0.2f, -0.1f);
            this.animator.rotate(this.headJoint, 0, 0, -0.3f);
            this.animator.rotate(this.armRightJoint, 0.2f, -0.5f, -0.8f);
            this.animator.move(this.armRightJoint, 0, 8, 0);
            this.animator.rotate(this.armRightJoint2, 0f, 0.5f, 0);
            this.animator.rotate(this.armRight2, 0, 0.2f, 0);
            this.animator.rotate(this.armLeftJoint, -1.3f, 0, 0);
            this.animator.rotate(this.armLeft1, -0.8f, 0, 0);
            this.animator.rotate(this.armLeftJoint2, 1.3f, 0, 0.3f);
            this.animator.rotate(this.leftHand, -0.65f, 0, 0);
            this.animator.move(this.handController, 0.8f, 0.8f, 0);
            this.animator.rotate(this.rightHand, -1.7f, 0.8f, -3.3f);
            this.animator.rotate(this.legLeftJoint, 0.7f, 0, 0);
            this.animator.rotate(this.legLeft1, -0.6f, 0, 0);
            this.animator.rotate(this.legLeft2, -0.6f, 0, 0.2f);
            this.animator.rotate(this.legLeftJoint, 0, 0.9f, 0.2f);
            this.animator.rotate(this.legLeft2, 0, 0.3f, 0);
            this.animator.rotate(this.legRightJoint, 0, -0.4f, 0);
            this.animator.rotate(this.legRight1, 1.1f, 0, 0.45f);
            this.animator.rotate(this.legRight2, -0.3f, 0, 0);

            this.animator.move(this.handController, 0, 0f, 0);
            this.animator.rotate(this.armRightJoint, 0.1f, -0.1f, 0.1f);
            this.animator.move(this.armRightJoint, 0, 0, -6);
            this.animator.rotate(this.armRight1, -0.5f, 0.2f, 0.4f);
            this.animator.rotate(this.armRight2, -0.5f, 0.1f, 0.2f);
            this.animator.rotate(this.armRightJoint2, 0.3f, -0.5f, 0.6f);
            this.animator.rotate(this.rightHand, -0.2f, 0f, 0.6f);
            this.animator.rotate(this.waist, 0.2f, 0.3f, 0);
            this.animator.rotate(this.legLeftJoint, -0.1f, -0.2f, 0.4f);
            this.animator.rotate(this.legRightJoint, -0.1f, -0.3f, 0.4f);
            this.animator.rotate(this.armLeftJoint, 0f, -1.1f, -0.3f);
            this.animator.rotate(this.armLeft2, -0.35f, 0.35f, 0.3f);
            this.animator.rotate(this.headJoint, 0.1f, 0.5f, -0.1f);
            this.animator.rotate(this.jawJoint, 0f, 0, 0);
            this.animator.endKeyframe();
            this.animator.setStaticKeyframe(7);

            this.animator.startKeyframe(15);
            this.animator.rotate(this.waist, 0.2f, 0, 0);
            this.animator.rotate(this.legRightJoint, -0.2f, 0, 0);
            this.animator.rotate(this.legLeftJoint, -0.2f, 0, 0);
            this.animator.rotate(this.headJoint, 0.3f, 0, 0);
            this.animator.move(this.armLeftJoint, 0.15f, 2, 0);
            this.animator.rotate(this.armLeftJoint2, -0.6f, 0, 0);
            this.animator.rotate(this.leftHand, 0.3f, 0, 0.15f);
            this.animator.rotate(this.armRightJoint, 0.15f, 0, 0);
            this.animator.move(this.armRightJoint, 0, 2, 0);
            this.animator.rotate(this.armRightJoint2, -0.6f, 0, 0);
            this.animator.rotate(this.rightHand, 0.3f, 0, -0.15f);
            this.animator.endKeyframe();

            this.animator.setStaticKeyframe(4);

            this.animator.startKeyframe(5);

            this.animator.rotate(this.waist, -0.2f, 0, 0);
            this.animator.rotate(this.legRightJoint, 0.2f, 0, 0);
            this.animator.rotate(this.legLeftJoint, 0.2f, 0, 0);
            this.animator.rotate(this.headJoint, -0.3f, 0, 0);
            this.animator.rotate(this.jawJoint, 1.3f, 0, 0);
            this.animator.move(this.roarController, 1, 1, 0);

            this.animator.rotate(this.armLeftJoint, -0.4f, 0, 0);
            this.animator.rotate(this.armLeftJoint2, 0.9f, 0, 0);
            this.animator.rotate(this.leftHand, -0.3f, 0, -0.15f);
            this.animator.rotate(this.armRightJoint, -0.4f, 0, 0);
            this.animator.rotate(this.armRightJoint2, 0.9f, 0, 0);
            this.animator.rotate(this.rightHand, -0.3f, 0, 0.15f);
            this.animator.endKeyframe();
            this.animator.setStaticKeyframe(50);

            this.animator.resetKeyframe(8);
        }
        if (entity.getAnimation() == EntityFrostmaw.ACTIVATE_NO_CRYSTAL_ANIMATION) {
            this.eyeLidLeft.showModel = false;
            this.eyeLidRight.showModel = false;
            this.animator.setAnimation(EntityFrostmaw.ACTIVATE_NO_CRYSTAL_ANIMATION);

            this.animator.startKeyframe(0);
            this.animator.rotate(this.root, 0, 0, 0.9f);
            this.animator.move(this.root, -20, 0, 0);
            this.animator.rotate(this.chest, 0.2f, 0.2f, -0.1f);
            this.animator.rotate(this.headJoint, 0, 0, -0.3f);
            this.animator.rotate(this.armRightJoint, 0.2f, -0.5f, -0.8f);
            this.animator.move(this.armRightJoint, 0, 8, 0);
            this.animator.rotate(this.armRightJoint2, 0f, 0.5f, 0);
            this.animator.rotate(this.armRight2, 0, 0.2f, 0);
            this.animator.rotate(this.armLeftJoint, -1.3f, 0, 0);
            this.animator.rotate(this.armLeft1, -0.8f, 0, 0);
            this.animator.rotate(this.armLeftJoint2, 1.3f, 0, 0.3f);
            this.animator.rotate(this.leftHand, -0.65f, 0, 0);
            this.animator.move(this.handController, 0.8f, 0.8f, 0);
            this.animator.rotate(this.rightHand, -1.7f, 0.8f, -3.3f);
            this.animator.rotate(this.legLeftJoint, 0.7f, 0, 0);
            this.animator.rotate(this.legLeft1, -0.6f, 0, 0);
            this.animator.rotate(this.legLeft2, -0.6f, 0, 0.2f);
            this.animator.rotate(this.legLeftJoint, 0, 0.9f, 0.2f);
            this.animator.rotate(this.legLeft2, 0, 0.3f, 0);
            this.animator.rotate(this.legRightJoint, 0, -0.4f, 0);
            this.animator.rotate(this.legRight1, 1.1f, 0, 0.45f);
            this.animator.rotate(this.legRight2, -0.3f, 0, 0);
            this.animator.endKeyframe();

            this.animator.startKeyframe(5);
            this.animator.rotate(this.root, 0, 0, 0.9f);
            this.animator.move(this.root, -20, 0, 0);
            this.animator.rotate(this.chest, 0.2f, 0.2f, -0.1f);
            this.animator.rotate(this.headJoint, 0, 0, -0.3f);
            this.animator.rotate(this.armRightJoint, 0.2f, -0.5f, -0.8f);
            this.animator.move(this.armRightJoint, 0, 8, 0);
            this.animator.rotate(this.armRightJoint2, 0f, 0.5f, 0);
            this.animator.rotate(this.armRight2, 0, 0.2f, 0);
            this.animator.rotate(this.armLeftJoint, -1.3f, 0, 0);
            this.animator.rotate(this.armLeft1, -0.8f, 0, 0);
            this.animator.rotate(this.armLeftJoint2, 1.3f, 0, 0.3f);
            this.animator.rotate(this.leftHand, -0.65f, 0, 0);
            this.animator.move(this.handController, 0.8f, 0.8f, 0);
            this.animator.rotate(this.rightHand, -1.7f, 0.8f, -3.3f);
            this.animator.rotate(this.legLeftJoint, 0.7f, 0, 0);
            this.animator.rotate(this.legLeft1, -0.6f, 0, 0);
            this.animator.rotate(this.legLeft2, -0.6f, 0, 0.2f);
            this.animator.rotate(this.legLeftJoint, 0, 0.9f, 0.2f);
            this.animator.rotate(this.legLeft2, 0, 0.3f, 0);
            this.animator.rotate(this.legRightJoint, 0, -0.4f, 0);
            this.animator.rotate(this.legRight1, 1.1f, 0, 0.45f);
            this.animator.rotate(this.legRight2, -0.3f, 0, 0);

            this.animator.move(this.handController, 0, -0.8f, 0);
            this.animator.endKeyframe();
            this.animator.setStaticKeyframe(10);

            this.animator.startKeyframe(15);
            this.animator.rotate(this.waist, 0.2f, 0, 0);
            this.animator.rotate(this.legRightJoint, -0.2f, 0, 0);
            this.animator.rotate(this.legLeftJoint, -0.2f, 0, 0);
            this.animator.rotate(this.headJoint, 0.3f, 0, 0);
            this.animator.move(this.armLeftJoint, 0.15f, 2, 0);
            this.animator.rotate(this.armLeftJoint2, -0.6f, 0, 0);
            this.animator.rotate(this.leftHand, 0.3f, 0, 0.15f);
            this.animator.rotate(this.armRightJoint, 0.15f, 0, 0);
            this.animator.move(this.armRightJoint, 0, 2, 0);
            this.animator.rotate(this.armRightJoint2, -0.6f, 0, 0);
            this.animator.rotate(this.rightHand, 0.3f, 0, -0.15f);
            this.animator.endKeyframe();

            this.animator.setStaticKeyframe(4);

            this.animator.startKeyframe(5);

            this.animator.rotate(this.waist, -0.2f, 0, 0);
            this.animator.rotate(this.legRightJoint, 0.2f, 0, 0);
            this.animator.rotate(this.legLeftJoint, 0.2f, 0, 0);
            this.animator.rotate(this.headJoint, -0.3f, 0, 0);
            this.animator.rotate(this.jawJoint, 1.3f, 0, 0);
            this.animator.move(this.roarController, 1, 1, 0);

            this.animator.rotate(this.armLeftJoint, -0.4f, 0, 0);
            this.animator.rotate(this.armLeftJoint2, 0.9f, 0, 0);
            this.animator.rotate(this.leftHand, -0.3f, 0, -0.15f);
            this.animator.rotate(this.armRightJoint, -0.4f, 0, 0);
            this.animator.rotate(this.armRightJoint2, 0.9f, 0, 0);
            this.animator.rotate(this.rightHand, -0.3f, 0, 0.15f);
            this.animator.endKeyframe();
            this.animator.setStaticKeyframe(50);

            this.animator.resetKeyframe(8);
        }
        if (entity.getAnimation() == EntityFrostmaw.DEACTIVATE_ANIMATION) {
            this.animator.setAnimation(EntityFrostmaw.DEACTIVATE_ANIMATION);
            this.animator.startKeyframe(0);
            this.animator.rotate(this.root, 0, 0, -0.9f);
            this.animator.move(this.root, 20, 0, 0);
            this.animator.rotate(this.chest, -0.2f, -0.2f, 0.1f);
            this.animator.rotate(this.headJoint, 0, 0, 0.3f);
            this.animator.rotate(this.armRightJoint, -0.2f, 0.5f, 0.8f);
            this.animator.move(this.armRightJoint, 0, -8, 0);
            this.animator.rotate(this.armRightJoint2, 0f, -0.5f, 0);
            this.animator.rotate(this.armRight2, 0, -0.2f, 0);
            this.animator.rotate(this.armLeftJoint, 1.3f, 0, 0);
            this.animator.rotate(this.armLeft1, 0.8f, 0, 0);
            this.animator.rotate(this.armLeftJoint2, -1.3f, 0, -0.3f);
            this.animator.rotate(this.leftHand, 0.65f, 0, 0);
            this.animator.move(this.handController, -0.8f, -0.8f, 0);
            this.animator.rotate(this.rightHand, 1.7f, -0.8f, 3.3f);
            this.animator.rotate(this.legLeftJoint, -0.7f, 0, 0);
            this.animator.rotate(this.legLeft1, 0.6f, 0, 0);
            this.animator.rotate(this.legLeft2, 0.6f, 0, -0.2f);
            this.animator.rotate(this.legLeftJoint, 0, -0.9f, -0.2f);
            this.animator.rotate(this.legLeft2, 0, -0.3f, 0);
            this.animator.rotate(this.legRightJoint, 0, 0.4f, 0);
            this.animator.rotate(this.legRight1, -1.1f, 0, -0.45f);
            this.animator.rotate(this.legRight2, 0.3f, 0, 0);
            this.animator.endKeyframe();

            this.animator.resetKeyframe(20);
        }
        if (entity.getAnimation() == EntityFrostmaw.LAND_ANIMATION) {
            this.animator.setAnimation(EntityFrostmaw.LAND_ANIMATION);
            this.animator.startKeyframe(4);
            this.animator.rotate(this.waist, 0.2f, 0, 0);
            this.animator.rotate(this.headJoint, -0.2f, 0, 0);
            this.animator.rotate(this.legRightJoint, -0.2f, 0, 0);
            this.animator.rotate(this.legLeftJoint, -0.2f, 0, 0);

            this.animator.rotate(this.armLeftJoint, 0.15f, 0, 0);
            this.animator.move(this.armLeftJoint, 0, 2, 0);
            this.animator.rotate(this.armLeftJoint2, -0.6f, 0, 0);
            this.animator.rotate(this.leftHand, 0.3f, 0, 0.15f);
            this.animator.rotate(this.armRightJoint, 0.15f, 0, 0);
            this.animator.move(this.armRightJoint, 0, 2, 0);
            this.animator.rotate(this.armRightJoint2, -0.6f, 0, 0);
            this.animator.rotate(this.rightHand, 0.3f, 0, -0.15f);
            this.animator.endKeyframe();
            this.animator.resetKeyframe(8);
        }
        if (entity.getAnimation() == EntityFrostmaw.SLAM_ANIMATION) {
            this.animator.setAnimation(EntityFrostmaw.SLAM_ANIMATION);
            this.animator.startKeyframe(6);
            this.animator.rotate(this.waist, 0.2f, 0, 0);
            this.animator.rotate(this.headJoint, -0.2f, 0, 0);
            this.animator.rotate(this.legRightJoint, -0.2f, 0, 0);
            this.animator.rotate(this.legLeftJoint, -0.2f, 0, 0);

            this.animator.rotate(this.armLeftJoint, 0.15f, 0, 0);
            this.animator.move(this.armLeftJoint, 0, 2, 0);
            this.animator.rotate(this.armLeftJoint2, -0.6f, 0, 0);
            this.animator.rotate(this.leftHand, 0.3f, 0, 0.15f);
            this.animator.rotate(this.armRightJoint, 0.15f, 0, 0);
            this.animator.move(this.armRightJoint, 0, 2, 0);
            this.animator.rotate(this.armRightJoint2, -0.6f, 0, 0);
            this.animator.rotate(this.rightHand, 0.3f, 0, -0.15f);
            this.animator.endKeyframe();
            this.animator.setStaticKeyframe(2);
            this.animator.startKeyframe(16);
            this.animator.move(this.standUpController, 1, 0, 0);
            this.animator.rotate(this.waist, -0.6f, 0, 0);
            this.animator.rotate(this.headJoint, 1.2f, 0, 0);
            this.animator.move(this.headJoint, 0, 8, -12);
            this.animator.rotate(this.chest, -0.2f, 0, 0);
            this.animator.move(this.waist, 0, -3, 0);
            this.animator.rotate(this.legLeftJoint, 0.6f, 0, 0);
            this.animator.rotate(this.legRightJoint, 0.6f, 0, 0);
            this.animator.rotate(this.legLeft1, 0.3f, 0, 0);
            this.animator.rotate(this.legLeft2, -0.3f, 0, 0);
            this.animator.move(this.legLeftJoint, 2, 0, 0);
            this.animator.rotate(this.legRight1, 0.3f, 0, 0);
            this.animator.rotate(this.legRight2, -0.3f, 0, 0);
            this.animator.move(this.legRightJoint, -2, 0, 0);

            this.animator.rotate(this.armLeftJoint, 0.5f, 0, 0);
            this.animator.rotate(this.armRightJoint, 0.5f, 0, 0);
            this.animator.rotate(this.armLeft1, 0, 0, -0.1f);
            this.animator.rotate(this.armRight1, 0, 0, 0.1f);
            this.animator.rotate(this.armLeft2, 1f, -1.1f, 0);
            this.animator.rotate(this.armRight2, 1f, 1.1f, 0);
            this.animator.move(this.handController, 0.5f, 0.5f, 0);
            this.animator.rotate(this.leftHand, -0.3f, 0, 0);
            this.animator.rotate(this.rightHand, -0.3f, 0, 0);

            this.animator.endKeyframe();
            this.animator.setStaticKeyframe(50);

            this.animator.startKeyframe(7);
            this.animator.move(this.standUpController, 1, 0, 0);
            this.animator.rotate(this.waist, -0.6f, 0, 0);
            this.animator.rotate(this.headJoint, 1.2f, 0, 0);
            this.animator.move(this.headJoint, 0, 8, -12);
            this.animator.rotate(this.chest, -0.2f, 0, 0);
            this.animator.move(this.waist, 0, -3, 0);
            this.animator.rotate(this.legLeftJoint, 0.6f, 0, 0);
            this.animator.rotate(this.legRightJoint, 0.6f, 0, 0);
            this.animator.rotate(this.legLeft1, 0.3f, 0, 0);
            this.animator.rotate(this.legLeft2, -0.3f, 0, 0);
            this.animator.move(this.legLeftJoint, 2, 0, 0);
            this.animator.rotate(this.legRight1, 0.3f, 0, 0);
            this.animator.rotate(this.legRight2, -0.3f, 0, 0);
            this.animator.move(this.legRightJoint, -2, 0, 0);

            this.animator.rotate(this.armLeftJoint, 0.5f, 0, 0);
            this.animator.rotate(this.armRightJoint, 0.5f, 0, 0);
            this.animator.rotate(this.armLeft1, 0, 0, -0.1f);
            this.animator.rotate(this.armRight1, 0, 0, 0.1f);
            this.animator.rotate(this.armLeft2, 1f, -1.1f, 0);
            this.animator.rotate(this.armRight2, 1f, 1.1f, 0);
            this.animator.move(this.handController, 0.8f, 0.8f, 0);
            this.animator.rotate(this.leftHand, -0.3f, 0, 0);
            this.animator.rotate(this.rightHand, -0.3f, 0, 0);

            this.animator.rotate(this.waist, -0.3f, 0, 0);
            this.animator.rotate(this.legLeftJoint, 0.3f, 0, 0);
            this.animator.rotate(this.legRightJoint, 0.3f, 0, 0);

            this.animator.rotate(this.armRightJoint, -2.2f, 0, 0);
            this.animator.rotate(this.armRight2, -0.6f, 0, -1f);
            this.animator.rotate(this.rightHand, -0.6f, 0, 0.4f);
            this.animator.rotate(this.armLeftJoint, -2.2f, 0, 0);
            this.animator.rotate(this.armLeft2, -0.6f, 0, 1f);
            this.animator.rotate(this.leftHand, -0.6f, 0, -0.4f);

            this.animator.endKeyframe();
            this.animator.setStaticKeyframe(4);
            this.animator.startKeyframe(4);
            this.animator.rotate(this.waist, 0.2f, 0, 0);
            this.animator.rotate(this.legLeftJoint, -0.2f, 0, 0);
            this.animator.rotate(this.legRightJoint, -0.2f, 0, 0);
            this.animator.rotate(this.headJoint, -0.2f, 0, 0);
            this.animator.rotate(this.jawJoint, 0.5f, 0, 0);
            this.animator.move(this.handController, 0.9f, 0.9f, 0);
            this.animator.move(this.roarController, 0, 1, 0);

            this.animator.rotate(this.armRightJoint, -1f, 0, 0);
            this.animator.rotate(this.armRight1, 0, 1.2f, -0.3f);
            this.animator.rotate(this.armRight2, 2.2f, -0.3f, -0.8f);
            this.animator.rotate(this.rightHand, -1.6f, -0.15f, 0.3f);
            this.animator.move(this.armRightJoint, 0, 4, -16);
            this.animator.rotate(this.armLeftJoint, -1f, 0, 0);
            this.animator.rotate(this.armLeft1, 0, -1.2f, 0.3f);
            this.animator.rotate(this.armLeft2, 2.2f, 0.3f, 0.8f);
            this.animator.rotate(this.leftHand, -1.6f, 0.15f, -0.3f);
            this.animator.move(this.armLeftJoint, 0, 4, -16);
            this.animator.endKeyframe();
            this.animator.setStaticKeyframe(8);
            this.animator.resetKeyframe(16);
        }

        if (entity.getAnimation() == EntityFrostmaw.DODGE_ANIMATION) {
            this.animator.setAnimation(EntityFrostmaw.DODGE_ANIMATION);
            this.animator.startKeyframe(5);
            this.animator.rotate(this.waist, 0.2f, 0, 0);
            this.animator.rotate(this.legRightJoint, -0.2f, 0, 0);
            this.animator.rotate(this.legLeftJoint, -0.2f, 0, 0);
            this.animator.rotate(this.headJoint, -0.2f, 0, 0);

            this.animator.rotate(this.armLeftJoint, 0.15f, 0, 0);
            this.animator.move(this.armLeftJoint, 0, 2, 0);
            this.animator.rotate(this.armLeftJoint2, -0.6f, 0, 0);
            this.animator.rotate(this.leftHand, 0.3f, 0, 0.15f);
            this.animator.rotate(this.armRightJoint, 0.15f, 0, 0);
            this.animator.move(this.armRightJoint, 0, 2, 0);
            this.animator.rotate(this.armRightJoint2, -0.6f, 0, 0);
            this.animator.rotate(this.rightHand, 0.3f, 0, -0.15f);
            this.animator.endKeyframe();

            this.animator.setStaticKeyframe(1);

            this.animator.startKeyframe(4);
            this.animator.rotate(this.waist, -0.1f, 0, 0);
            this.animator.rotate(this.legRightJoint, 0.1f, 0, 0);
            this.animator.rotate(this.legLeftJoint, 0.1f, 0, 0);
            this.animator.rotate(this.headJoint, 0.2f, 0, 0);
            this.animator.move(this.waist, 0, 0, 0);
            this.animator.rotate(this.legLeft1, 0.3f, 0, 0);
            this.animator.rotate(this.legLeft2, -0.3f, 0, 0);
            this.animator.move(this.legLeftJoint, 2, 0, 0);
            this.animator.rotate(this.legRight1, 0.3f, 0, 0);
            this.animator.rotate(this.legRight2, -0.3f, 0, 0);
            this.animator.move(this.legRightJoint, -2, 0, 0);

            this.animator.rotate(this.armLeftJoint, -0.4f, 0, 0);
            this.animator.rotate(this.armLeftJoint2, 0.9f, 0, 0);
            this.animator.rotate(this.leftHand, -0.3f, 0, -0.15f);
            this.animator.rotate(this.armRightJoint, -0.4f, 0, 0);
            this.animator.rotate(this.armRightJoint2, 0.9f, 0, 0);
            this.animator.rotate(this.rightHand, -0.3f, 0, 0.15f);
            this.animator.endKeyframe();
            this.animator.resetKeyframe(5);
        }

        if (entity.getAnimation() == EntityFrostmaw.DIE_ANIMATION) {
            this.animator.setAnimation(EntityFrostmaw.DIE_ANIMATION);
            this.animator.startKeyframe(4);
            this.animator.rotate(this.waist, 0.2f, 0, 0);
            this.animator.rotate(this.legRightJoint, -0.2f, 0, 0);
            this.animator.rotate(this.legLeftJoint, -0.2f, 0, 0);
            this.animator.rotate(this.headJoint, 0.3f, 0, 0);

            this.animator.rotate(this.armLeftJoint, 0.15f, 0, 0);
            this.animator.move(this.armLeftJoint, 0, 2, 0);
            this.animator.rotate(this.armLeftJoint2, -0.6f, 0, 0);
            this.animator.rotate(this.leftHand, 0.3f, 0, 0.15f);
            this.animator.rotate(this.armRightJoint, 0.15f, 0, 0);
            this.animator.move(this.armRightJoint, 0, 2, 0);
            this.animator.rotate(this.armRightJoint2, -0.6f, 0, 0);
            this.animator.rotate(this.rightHand, 0.3f, 0, -0.15f);
            this.animator.endKeyframe();

            this.animator.setStaticKeyframe(2);

            this.animator.startKeyframe(5);
            this.animator.rotate(this.waist, -0.2f, 0, 0);
            this.animator.rotate(this.legRightJoint, 0.2f, 0, 0);
            this.animator.rotate(this.legLeftJoint, 0.2f, 0, 0);
            this.animator.rotate(this.headJoint, -0.3f, 0, 0);
            this.animator.rotate(this.jawJoint, 1.3f, 0, 0);
            this.animator.move(this.roarController, 1, 1, 0);

            this.animator.rotate(this.armLeftJoint, -0.4f, 0, 0);
            this.animator.rotate(this.armLeftJoint2, 0.9f, 0, 0);
            this.animator.rotate(this.leftHand, -0.3f, 0, -0.15f);
            this.animator.rotate(this.armRightJoint, -0.4f, 0, 0);
            this.animator.rotate(this.armRightJoint2, 0.9f, 0, 0);
            this.animator.rotate(this.rightHand, -0.3f, 0, 0.15f);
            this.animator.endKeyframe();
            this.animator.setStaticKeyframe(22);

            this.animator.startKeyframe(3);
            this.animator.rotate(this.waist, -0.2f, 0, 0);
            this.animator.rotate(this.legRightJoint, 0.2f, 0, 0);
            this.animator.rotate(this.legLeftJoint, 0.2f, 0, 0);
            this.animator.rotate(this.headJoint, -0.3f, 0, 0);
            this.animator.rotate(this.jawJoint, 1.3f, 0, 0);
            this.animator.move(this.roarController, 0, 1, 0);

            this.animator.rotate(this.armLeftJoint, -0.4f, 0, 0);
            this.animator.rotate(this.armLeftJoint2, 0.9f, 0, 0);
            this.animator.rotate(this.leftHand, -0.3f, 0, -0.15f);
            this.animator.rotate(this.armRightJoint, -0.4f, 0, 0);
            this.animator.rotate(this.armRightJoint2, 0.9f, 0, 0);
            this.animator.rotate(this.rightHand, -0.3f, 0, 0.15f);
            this.animator.endKeyframe();
            this.animator.setStaticKeyframe(12);

            this.animator.startKeyframe(7);
            this.animator.rotate(this.root, 0, 0, 0.9f);
            this.animator.move(this.root, -20, 0, 0);
            this.animator.rotate(this.chest, 0.2f, 0.2f, -0.1f);
            this.animator.rotate(this.headJoint, 0, 0, -0.3f);
            this.animator.rotate(this.armRightJoint, 0.2f, -0.5f, -0.8f);
            this.animator.move(this.armRightJoint, 0, 8, 0);
            this.animator.rotate(this.armRightJoint2, 0f, 0.5f, 0);
            this.animator.rotate(this.armRight2, 0, 0.2f, 0);
            this.animator.rotate(this.armLeftJoint, -1.3f, 0, 0);
            this.animator.rotate(this.armLeft1, -0.8f, 0, 0);
            this.animator.rotate(this.armLeftJoint2, 1.3f, 0, 0.3f);
            this.animator.rotate(this.leftHand, -0.65f, 0, 0);
            this.animator.move(this.handController, 0.8f, 0.8f, 0);
            this.animator.rotate(this.rightHand, -1.5f, 0, -0.2f);
            this.animator.rotate(this.legLeftJoint, 0.7f, 0, 0);
            this.animator.rotate(this.legLeft1, -0.6f, 0, 0);
            this.animator.rotate(this.legLeft2, -0.6f, 0, 0.2f);
            this.animator.rotate(this.legLeftJoint, 0, 0.9f, 0.2f);
            this.animator.rotate(this.legLeft2, 0, 0.3f, 0);
            this.animator.rotate(this.legRightJoint, 0, -0.4f, 0);
            this.animator.rotate(this.legRight1, 1.1f, 0, 0.45f);
            this.animator.rotate(this.legRight2, -0.3f, 0, 0);
            this.animator.endKeyframe();
            this.animator.setStaticKeyframe(40);

            if (entity.getAnimationTick() > 50) {
                this.eyeLidLeft.showModel = true;
                this.eyeLidRight.showModel = true;
            }
        }

//        limbSwing = 0.5f * (entity.tickCount + delta);
//        limbSwingAmount = 1f;
        float globalSpeed = 0.5f;
        float globalHeightQuad = 1.2f * (1 - this.standUpController.rotationPointX);
        float globalDegreeQuad = 0.8f * (1 - this.standUpController.rotationPointX);
        float frontDegree = 1.1f;
        float frontOffset = (float) (Math.PI / 2);
        float globalHeightBi = this.standUpController.rotationPointX;
        float globalDegreeBi = this.standUpController.rotationPointX;

        float lookLimit = 50;
        if (headYaw > lookLimit) {
            headYaw = lookLimit;
        }
        if (headYaw < -lookLimit) {
            headYaw = -lookLimit;
        }

        if (entity.getAnimation() == EntityFrostmaw.ROAR_ANIMATION) {
            headYaw = headYaw / (frame);
            headPitch = headPitch / (frame);
        }
        this.waist.rotationPointZ += 10;

        if (entity.getActive()) {
            if (entity.getAnimation() != EntityFrostmaw.ACTIVATE_ANIMATION && entity.getAnimation() != EntityFrostmaw.ACTIVATE_NO_CRYSTAL_ANIMATION && entity.getAnimation() != EntityFrostmaw.DEACTIVATE_ANIMATION) {
                this.faceTarget(headYaw * (1 - this.standUpController.rotationPointX), headPitch, 1, this.headJoint);
                float yawAmount = headYaw / (180.0F / (float) Math.PI);
                this.headJoint.rotateAngleZ += yawAmount * this.standUpController.rotationPointX;

                //Walk
                //Quadrupedal
                this.bob(this.waist, globalSpeed, globalHeightQuad * 3f, false, limbSwing, limbSwingAmount);
                this.walk(this.waist, globalSpeed, globalHeightQuad * 0.12f, false, frontOffset, 0.08f * (1 - this.standUpController.rotationPointX), limbSwing, limbSwingAmount);
                this.walk(this.headJoint, globalSpeed, globalHeightQuad * 0.12f, true, frontOffset + 0.4f, 0.08f * (1 - this.standUpController.rotationPointX), limbSwing, limbSwingAmount);
                this.walk(this.legLeftJoint, globalSpeed, globalHeightQuad * 0.12f, true, frontOffset, 0.08f * (1 - this.standUpController.rotationPointX), limbSwing, limbSwingAmount);
                this.walk(this.legRightJoint, globalSpeed, globalHeightQuad * 0.12f, true, frontOffset, 0.08f * (1 - this.standUpController.rotationPointX), limbSwing, limbSwingAmount);
                this.walk(this.armLeftJoint, globalSpeed, globalHeightQuad * 0.12f, true, frontOffset, 0.08f * (1 - this.standUpController.rotationPointX), limbSwing, limbSwingAmount);
                this.walk(this.armRightJoint, globalSpeed, globalHeightQuad * 0.12f, true, frontOffset, 0.08f * (1 - this.standUpController.rotationPointX), limbSwing, limbSwingAmount);

                this.swing(this.waist, 0.5f * globalSpeed, globalDegreeQuad * 0.2f, true, 0, 0, limbSwing, limbSwingAmount);
                this.swing(this.headJoint, 0.5f * globalSpeed, globalDegreeQuad * 0.2f, false, 0, 0, limbSwing, limbSwingAmount);

                this.flap(this.waist, 0.5f * globalSpeed, 0.15f * globalHeightQuad, false, 1, 0, limbSwing, limbSwingAmount);
                this.flap(this.legLeft1, 0.5f * globalSpeed, 0.15f * globalHeightQuad, true, 1, 0, limbSwing, limbSwingAmount);
                this.flap(this.legRight1, 0.5f * globalSpeed, 0.15f * globalHeightQuad, true, 1, 0, limbSwing, limbSwingAmount);
                this.flap(this.chest, 0.5f * globalSpeed, 0.15f * globalHeightQuad, true, 1, 0, limbSwing, limbSwingAmount);
                this.swing(this.legLeft1, 0.5F * globalSpeed, 0.2F * globalDegreeQuad, false, 0, 0.6F * (1 - this.standUpController.rotationPointX), limbSwing, limbSwingAmount);
                this.swing(this.legRight1, 0.5F * globalSpeed, 0.2F * globalDegreeQuad, false, 0, -0.6F * (1 - this.standUpController.rotationPointX), limbSwing, limbSwingAmount);
                this.walk(this.legLeft1, 0.5f * globalSpeed, 0.7f * globalDegreeQuad, false, 0, 0.4f * (1 - this.standUpController.rotationPointX), limbSwing, limbSwingAmount);
                this.walk(this.legRight1, 0.5f * globalSpeed, 0.7f * globalDegreeQuad, true, 0, -0.4f * (1 - this.standUpController.rotationPointX), limbSwing, limbSwingAmount);
                this.walk(this.legLeft2, 0.5f * globalSpeed, 0.6f * globalDegreeQuad, false, -1.8f, 0.2f * (1 - this.standUpController.rotationPointX), limbSwing, limbSwingAmount);
                this.walk(this.legRight2, 0.5f * globalSpeed, 0.6f * globalDegreeQuad, true, -1.8f, -0.2f * (1 - this.standUpController.rotationPointX), limbSwing, limbSwingAmount);
                this.walk(this.leftFoot, 0.5f * globalSpeed, 0.4f * globalDegreeQuad, false, -1.5f, 0.4f * globalDegreeQuad, limbSwing, limbSwingAmount);
                this.walk(this.rightFoot, 0.5f * globalSpeed, 0.4f * globalDegreeQuad, true, -1.5f, -0.4f * globalDegreeQuad, limbSwing, limbSwingAmount);

                this.swing(this.chest, 0.5f * globalSpeed, 0.3f * globalDegreeQuad * frontDegree, false, frontOffset, 0, limbSwing, limbSwingAmount);
                this.swing(this.headJoint, 0.5f * globalSpeed, 0.3f * globalDegreeQuad * frontDegree, true, frontOffset, 0, limbSwing, limbSwingAmount);
                this.swing(this.armLeft2, 0.5f * globalSpeed, 0.3f * globalDegreeQuad * frontDegree, true, frontOffset, 0.4f * globalDegreeQuad * frontDegree, limbSwing, limbSwingAmount);
                this.swing(this.armRight2, 0.5f * globalSpeed, 0.3f * globalDegreeQuad * frontDegree, true, frontOffset, -0.4f * globalDegreeQuad * frontDegree, limbSwing, limbSwingAmount);
                this.flap(this.chest, 0.5f * globalSpeed, 0.1f * globalDegreeQuad * frontDegree, true, frontOffset + 1, 0, limbSwing, limbSwingAmount);
                this.flap(this.armLeftJoint, 0.5f * globalSpeed, 0.1f * globalDegreeQuad * frontDegree, false, frontOffset + 1, 0, limbSwing, limbSwingAmount);
                this.flap(this.armRightJoint, 0.5f * globalSpeed, 0.1f * globalDegreeQuad * frontDegree, false, frontOffset + 1, 0, limbSwing, limbSwingAmount);
                this.flap(this.headJoint, 0.5f * globalSpeed, 0.1f * globalDegreeQuad * frontDegree, false, frontOffset + 1, 0, limbSwing, limbSwingAmount);

                this.walk(this.armLeftJoint, 0.5f * globalSpeed, 0.4f * globalDegreeQuad * frontDegree, true, frontOffset, -0.7f * globalDegreeQuad * frontDegree, limbSwing, limbSwingAmount);
                this.walk(this.armRightJoint, 0.5f * globalSpeed, 0.4f * globalDegreeQuad * frontDegree, false, frontOffset, 0.7f * globalDegreeQuad * frontDegree, limbSwing, limbSwingAmount);
                this.walk(this.armLeftJoint2, 0.5f * globalSpeed, 0.4f * globalDegreeQuad * frontDegree, true, frontOffset + 2f, 0.6f * globalDegreeQuad * frontDegree, limbSwing, limbSwingAmount);
                this.walk(this.armRightJoint2, 0.5f * globalSpeed, 0.4f * globalDegreeQuad * frontDegree, false, frontOffset + 2f, -0.6f * globalDegreeQuad * frontDegree, limbSwing, limbSwingAmount);
                this.walk(this.leftHand, 0.5f * globalSpeed, 0.5f * globalDegreeQuad * frontDegree, false, frontOffset + 1.5f, 0.05f * globalDegreeQuad * frontDegree, limbSwing, limbSwingAmount);
                this.walk(this.rightHand, 0.5f * globalSpeed, 0.5f * globalDegreeQuad * frontDegree, true, frontOffset + 1.5f, 0.05f * globalDegreeQuad * frontDegree, limbSwing, limbSwingAmount);

                //Bipedal
                this.flap(this.root, 0.5F * globalSpeed, 0.1f * globalHeightBi, true, -0.7f, 0, limbSwing, limbSwingAmount);
                this.bob(this.waist, globalSpeed, globalHeightBi * 3f, false, limbSwing, limbSwingAmount);
                this.walk(this.waist, globalSpeed, 0.05f * globalHeightBi, false, -1f, 0.1f * this.standUpController.rotationPointX, limbSwing, limbSwingAmount);
                this.walk(this.legLeftJoint, globalSpeed, 0.05f * globalHeightBi, true, -1f, 0.1f * this.standUpController.rotationPointX, limbSwing, limbSwingAmount);
                this.walk(this.legRightJoint, globalSpeed, 0.05f * globalHeightBi, true, -1f, 0.1f * this.standUpController.rotationPointX, limbSwing, limbSwingAmount);
                this.walk(this.chest, globalSpeed, 0.05f * globalHeightBi, false, -2f, 0.05f * this.standUpController.rotationPointX, limbSwing, limbSwingAmount);
                this.walk(this.headJoint, globalSpeed, 0.1f * globalHeightBi, true, -2f, -0.1f * this.standUpController.rotationPointX, limbSwing, limbSwingAmount);
                this.flap(this.chest, 0.5f * globalSpeed, 0.2f * globalHeightBi, false, -1, 0, limbSwing, limbSwingAmount);
                this.flap(this.headJoint, 0.5f * globalSpeed, -0.2f * globalHeightBi, false, -1, 0, limbSwing, limbSwingAmount);

                this.walk(this.armLeftJoint, 0.5f * globalSpeed, 0.2f * globalDegreeBi * frontDegree, false, frontOffset, 0.4f * globalDegreeBi, limbSwing, limbSwingAmount);
                this.walk(this.armLeftJoint2, 0.5f * globalSpeed, 0.2f * globalDegreeBi * frontDegree, true, frontOffset + 2f, 0.6f * globalDegreeBi, limbSwing, limbSwingAmount);
                this.walk(this.leftHand, 0.5f * globalSpeed, 0.2f * globalDegreeBi * frontDegree, true, frontOffset + 1.5f, 0.05f * globalDegreeBi, limbSwing, limbSwingAmount);

                this.walk(this.armRightJoint, 0.5f * globalSpeed, 0.2f * globalDegreeBi * frontDegree, true, frontOffset, -0.4f * globalDegreeBi, limbSwing, limbSwingAmount);
                this.walk(this.armRightJoint2, 0.5f * globalSpeed, 0.2f * globalDegreeBi * frontDegree, false, frontOffset + 2f, -0.6f * globalDegreeBi, limbSwing, limbSwingAmount);
                this.walk(this.rightHand, 0.5f * globalSpeed, 0.2f * globalDegreeBi * frontDegree, false, frontOffset + 1.5f, -0.05f * globalDegreeBi, limbSwing, limbSwingAmount);

                this.swing(this.legLeft1, 0.5F * globalSpeed, 0.2F * globalDegreeBi, false, 0, 0.6F * this.standUpController.rotationPointX, limbSwing, limbSwingAmount);
                this.swing(this.legRight1, 0.5F * globalSpeed, 0.2F * globalDegreeBi, false, 0, -0.6F * this.standUpController.rotationPointX, limbSwing, limbSwingAmount);
                this.walk(this.legLeft1, 0.5f * globalSpeed, 0.7f * globalDegreeBi, false, 0, 0.4f * this.standUpController.rotationPointX, limbSwing, limbSwingAmount);
                this.walk(this.legRight1, 0.5f * globalSpeed, 0.7f * globalDegreeBi, true, 0, -0.4f * this.standUpController.rotationPointX, limbSwing, limbSwingAmount);
                this.walk(this.legLeft2, 0.5f * globalSpeed, 0.6f * globalDegreeBi, false, -1.8f, 0.2f * this.standUpController.rotationPointX, limbSwing, limbSwingAmount);
                this.walk(this.legRight2, 0.5f * globalSpeed, 0.6f * globalDegreeBi, true, -1.8f, -0.2f * this.standUpController.rotationPointX, limbSwing, limbSwingAmount);
                this.walk(this.leftFoot, 0.5f * globalSpeed, 0.4f * globalDegreeBi, false, -1.5f, 0.4f * globalDegreeBi, limbSwing, limbSwingAmount);
                this.walk(this.rightFoot, 0.5f * globalSpeed, 0.4f * globalDegreeBi, true, -1.5f, -0.4f * globalDegreeBi, limbSwing, limbSwingAmount);

                //Idle
                FrozenCapability.IFrozenCapability frozenCapability = CapabilityHandler.getCapability(entity, CapabilityHandler.FROZEN_CAPABILITY);
                boolean frozen = frozenCapability != null && frozenCapability.getFrozen();
                if (!frozen && (entity.getAnimation() != EntityFrostmaw.SLAM_ANIMATION || entity.getAnimationTick() < 118) && entity.getAnimation() != EntityFrostmaw.DIE_ANIMATION) {
                    this.walk(this.waist, 0.08f, 0.05f, false, 0, 0, frame, 1);
                    this.walk(this.headJoint, 0.08f, 0.05f, true, 0.8f, 0, frame, 1);
                    this.walk(this.legRightJoint, 0.08f, 0.05f, true, 0, 0, frame, 1);
                    this.walk(this.legLeftJoint, 0.08f, 0.05f, true, 0, 0, frame, 1);
                    this.walk(this.armLeftJoint, 0.08f, 0.05f, true, 0, 0, frame, 1);
                    this.walk(this.armRightJoint, 0.08f, 0.05f, true, 0, 0, frame, 1);
                    this.walk(this.armLeftJoint2, 0.08f, 0.07f, true, 0, 0, frame, 1);
                    this.walk(this.armRightJoint2, 0.08f, 0.07f, true, 0, 0, frame, 1);
                    this.walk(this.leftHand, 0.08f, 0.07f, false, 0, 0, frame, 1);
                    this.walk(this.rightHand, 0.08f, 0.07f, false, 0, 0, frame, 1);
                    this.armLeftJoint.rotationPointZ += 1.8f * Math.cos(frame * 0.08f);
                    this.armRightJoint.rotationPointZ += 1.8f * Math.cos(frame * 0.08f);
                    this.armLeftJoint.rotationPointY -= 0.4f * Math.cos(frame * 0.08f);
                    this.armRightJoint.rotationPointY -= 0.4f * Math.cos(frame * 0.08f);
                }
            }
            if (entity.getAnimation() != EntityFrostmaw.DIE_ANIMATION) {
                this.eyeLidRight.showModel = false;
                this.eyeLidLeft.showModel = false;
            }
        } else if (entity.getAnimation() != EntityFrostmaw.ACTIVATE_ANIMATION && entity.getAnimation() != EntityFrostmaw.ACTIVATE_NO_CRYSTAL_ANIMATION) {
            this.eyeLidLeft.showModel = true;
            this.eyeLidRight.showModel = true;
            this.root.rotateAngleZ += 0.9f;
            this.root.rotationPointX -= 20;
            this.chest.rotateAngleZ -= 0.1f;
            this.chest.rotateAngleX += 0.2;
            this.chest.rotateAngleY += 0.2;
            this.headJoint.rotateAngleZ -= 0.3f;
            this.armRightJoint.rotateAngleZ -= 0.8f;
            this.armRightJoint.rotateAngleX += 0.2f;
            this.armRightJoint.rotateAngleY -= 0.5f;
            this.armRightJoint.rotationPointY += 8;
            this.armRight2.rotateAngleY += 0.2f;
            this.armLeftJoint.rotateAngleX -= 1.3f;
            this.armLeft1.rotateAngleX -= 0.8f;
            this.armLeftJoint2.rotateAngleX += 1.3f;
            this.armLeftJoint2.rotateAngleZ += 0.3f;
            this.armRightJoint2.rotateAngleY += 0.5f;
            this.leftHand.rotateAngleX -= 0.65f;
            this.handController.rotationPointX += 0.8f;
            this.rightHand.rotateAngleX -= 1.7f;
            this.rightHand.rotateAngleY += 0.8f;
            this.rightHand.rotateAngleZ -= 3.3f;
            this.handController.rotationPointY += 0.8f;
            this.legLeftJoint.rotateAngleX += 0.7f;
            this.legLeft1.rotateAngleX -= 0.6f;
            this.legLeft2.rotateAngleX -= 0.6f;
            this.legLeft2.rotateAngleZ += 0.2f;
            this.legLeftJoint.rotateAngleY += 0.9f;
            this.legLeftJoint.rotateAngleZ += 0.2f;
            this.legLeft2.rotateAngleY += 0.3f;
            this.legRightJoint.rotateAngleY -= 0.4f;
            this.legRight1.rotateAngleX += 1.1f;
            this.legRight1.rotateAngleZ += 0.45f;
            this.legRight2.rotateAngleX -= 0.3f;
//            legRightJoint.rotationPointZ -= 5f;
            this.chest.scaleChildren = false;
            float chestScale = (float) (1.05 + 0.05f * Math.cos(frame * 0.05f));
            this.chest.setScale(chestScale, chestScale, chestScale);
            this.backHair.setScale(chestScale, chestScale, chestScale);
            this.backHair.rotationPointZ -= (chestScale - 1.05) * 20f;
            this.headJoint.rotateAngleX += (float) (0.04f * Math.cos(frame * 0.05f - 1));
        }

        this.jawJoint.rotateAngleX += 0.08 * this.roarController.rotationPointX * Math.cos(2 * frame);
        this.headBack.setScale(1, 1 - this.roarController.rotationPointY, 1);

        this.rightFingersJoint.rotateAngleX -= this.handController.rotationPointY * Math.PI / 2;
        this.rightFingers.rotateAngleX -= this.handController.rotationPointY * Math.PI / 2;
        this.rightThumb.rotateAngleY += (float) (this.handController.rotationPointY * Math.PI);
        this.rightThumb.rotateAngleZ += this.handController.rotationPointY * 0.7f;
        this.rightThumb.rotationPointZ -= this.handController.rotationPointY * 6;

        this.leftFingersJoint.rotateAngleX -= this.handController.rotationPointX * Math.PI / 2;
        this.leftFingers.rotateAngleX -= this.handController.rotationPointX * Math.PI / 2;
        this.leftThumb.rotateAngleY -= (float) (this.handController.rotationPointX * Math.PI);
        this.leftThumb.rotateAngleZ -= this.handController.rotationPointX * 0.7f;
        this.leftThumb.rotationPointZ -= this.handController.rotationPointX * 6;

        this.iceCrystalJoint.rotateAngleX = -this.headJoint.rotateAngleX - this.chest.rotateAngleX - this.waist.rotateAngleX - this.root.rotateAngleX + 0.9f;
//        iceCrystal.rotateAngleY += (float)Math.PI/2;
        this.iceCrystal.rotationPointY += 2 * Math.cos(0.15f * frame) + 5;
        this.iceCrystal.setScale(2);
        this.iceCrystal.rotateAngleZ += (float) Math.PI;
        this.iceCrystal.rotationPointY -= 6 * (1 - Math.min(this.jawJoint.rotateAngleX - this.jawJoint.defaultRotationX, 1.0f));

        this.iceCrystalHand.rotationPointY -= 1.7f * Math.cos(0.1f * frame) - 1;
        this.iceCrystalHand.rotateAngleY -= frame * 0.05f;
        this.iceCrystalHand.rotateAngleZ += Math.PI;

        if (entity.getHasCrystal()) {
            this.iceCrystal.showModel = !(entity.getAnimation() == EntityFrostmaw.ACTIVATE_ANIMATION && entity.getAnimationTick() <= 28);

            this.iceCrystalHand.showModel = !entity.active;
        } else {
            this.iceCrystal.showModel = false;
            this.iceCrystalHand.showModel = false;
        }
    }
}