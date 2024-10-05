package com.bobmowzie.mowziesmobs.client.model.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.tools.RigUtils;
import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieGeoBone;
import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieGeoModel;
import com.bobmowzie.mowziesmobs.server.entity.sculptor.EntitySculptor;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3d;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.data.EntityModelData;

import java.util.HashMap;
import java.util.Map;

public class ModelSculptor extends MowzieGeoModel<EntitySculptor> {
    public ModelSculptor() {
        super();
    }

    private static Vec3d Vec3(Vector3d vec) {
        return new Vec3d(vec.x, vec.y, vec.z);
    }

    @Override
    public Identifier getModelResource(EntitySculptor object) {
        return new Identifier(MowziesMobs.MODID, "geo/sculptor.geo.json");
    }

    @Override
    public Identifier getTextureResource(EntitySculptor object) {
        return new Identifier(MowziesMobs.MODID, "textures/entity/sculptor.png");
    }

    @Override
    public Identifier getAnimationResource(EntitySculptor object) {
        return new Identifier(MowziesMobs.MODID, "animations/sculptor.animation.json");
    }

    @Override
    public void setCustomAnimations(EntitySculptor entity, long instanceId, AnimationState<EntitySculptor> animationState) {
        GeoBone head = this.getBone("head").get();
        GeoBone chestJoint = this.getBone("chestJoint").get();
        GeoBone handClosedL = this.getBone("handClosedLeft").get();
        GeoBone handClosedR = this.getBone("handClosedRight").get();
        GeoBone handOpenL = this.getBone("handOpenLeft").get();
        GeoBone handOpenR = this.getBone("handOpenRight").get();
        GeoBone backCloth = this.getBone("clothBack").get();

        EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
        float torsoAimController = this.getControllerValue("torsoAimController");
        head.setRotX(head.getRotX() + entityData.headPitch() * ((float) Math.PI / 180F) * (1.0f - torsoAimController));
        head.setRotY(head.getRotY() + entityData.netHeadYaw() * ((float) Math.PI / 180F) * (1.0f - torsoAimController));
        chestJoint.setRotX(chestJoint.getRotX() + entityData.headPitch() * ((float) Math.PI / 180F) * torsoAimController);
        chestJoint.setRotY(chestJoint.getRotY() + entityData.netHeadYaw() * ((float) Math.PI / 180F) * torsoAimController);

        if (entity.isAlive()) {
            this.idleAnim(entity, animationState);
        }

        float handControllerLeft = this.getControllerValue("handControllerLeft");
        float handControllerRight = this.getControllerValue("handControllerRight");
        boolean handLOpen = handControllerLeft == 0;
        boolean handROpen = handControllerRight == 0;

        handClosedL.setHidden(handLOpen);
        handClosedR.setHidden(handROpen);
        handOpenL.setHidden(!handLOpen);
        handOpenR.setHidden(!handROpen);

        backCloth.setHidden(false);

        this.beadsCorrections(entity);
        this.skirtCorrections(entity);

        this.staffRendering(entity);
    }

    private void beadsCorrections(EntitySculptor entity) {
        Map<MowzieGeoBone, Vec3d> beadsDirections = new HashMap<>();
        beadsDirections.put(this.getMowzieBone("bead1"), new Vec3d(0, -1, 0));
        beadsDirections.put(this.getMowzieBone("bead2"), new Vec3d(0, -1, 0));
        beadsDirections.put(this.getMowzieBone("bead3"), new Vec3d(0, -1, 0));
        beadsDirections.put(this.getMowzieBone("bead4"), new Vec3d(0, -0.5, 0.25));
        beadsDirections.put(this.getMowzieBone("bead5"), new Vec3d(0, -0.5, 0.25));
        beadsDirections.put(this.getMowzieBone("bead6"), new Vec3d(0, -0.5, 1));
        beadsDirections.put(this.getMowzieBone("bead7"), new Vec3d(0, -0.5, 1));
        beadsDirections.put(this.getMowzieBone("bead8"), new Vec3d(0, -0.25, 0.75));
        beadsDirections.put(this.getMowzieBone("bead9"), new Vec3d(0, -0.25, 0.75));

        GeoBone headJoint = this.getBone("headJoint").get();
        Vec3d headPos = new Vec3d(headJoint.getPivotX(), headJoint.getPivotY(), headJoint.getPivotZ());
        GeoBone head = this.getBone("head").get();
        Vec3d headDir = new Vec3d(0, 0, -1).normalize();
        headDir = headDir.rotateY(head.getRotY()).rotateX(head.getRotX());
        for (Map.Entry<MowzieGeoBone, Vec3d> beadDir : beadsDirections.entrySet()) {
            MowzieGeoBone bead = beadDir.getKey();
            Vec3d beadPos = new Vec3d(bead.getPivotX(), bead.getPivotY(), bead.getPivotZ());
            Vec3d dir = beadPos.subtract(headPos).normalize().multiply(1, -1, 1);
            double dot = dir.dotProduct(headDir);
            dot = Math.pow(Math.max(dot, 0), 3.5);
            Vec3d moveDir = beadDir.getValue().normalize();
            bead.addPos(moveDir.multiply(dot * 3));
        }
    }

    private void skirtCorrections(EntitySculptor entity) {
        MowzieGeoBone headJoint = this.getMowzieBone("headJoint");
        MowzieGeoBone thighR = this.getMowzieBone("thighRight");
        MowzieGeoBone thighJointR = this.getMowzieBone("thighJointRight");
        MowzieGeoBone thighJointL = this.getMowzieBone("thighJointLeft");
        MowzieGeoBone calfR = this.getMowzieBone("calfRight");
        MowzieGeoBone thighL = this.getMowzieBone("thighLeft");
        MowzieGeoBone calfL = this.getMowzieBone("calfLeft");
        MowzieGeoBone footR = this.getMowzieBone("footRight");

        MowzieGeoBone skirtBack = this.getMowzieBone("skirtBack");
        MowzieGeoBone skirtFront = this.getMowzieBone("skirtFront");
        MowzieGeoBone skirtL = this.getMowzieBone("skirtLeft");
        MowzieGeoBone skirtR = this.getMowzieBone("skirtRight");
        MowzieGeoBone skirtJointL = this.getMowzieBone("skirtJointLeft");
        MowzieGeoBone skirtJointR = this.getMowzieBone("skirtJointRight");
        MowzieGeoBone skirtJoint2L = this.getMowzieBone("skirtJoint2Left");
        MowzieGeoBone skirtJoint2R = this.getMowzieBone("skirtJoint2Right");
        MowzieGeoBone skirtEndR = this.getMowzieBone("skirtEndRight");
        MowzieGeoBone skirtEndL = this.getMowzieBone("skirtEndLeft");

        MowzieGeoBone skirtLocFrontR = this.getMowzieBone("skirtFrontLocRight");
        MowzieGeoBone skirtLocFrontL = this.getMowzieBone("skirtFrontLocLeft");
        MowzieGeoBone skirtLocBackR = this.getMowzieBone("skirtBackLocRight");
        MowzieGeoBone skirtLocBackL = this.getMowzieBone("skirtBackLocLeft");

        headJoint.setHidden(false);

        Vec3d thighToKneeR = Vec3(calfR.getModelPosition()).subtract(Vec3(thighR.getModelPosition())).normalize();
        Vec3d thighToKneeL = Vec3(calfL.getModelPosition()).subtract(Vec3(thighL.getModelPosition())).normalize();

        skirtEndL.addPos(-0.2f, -1.5f, 0);
        skirtEndR.addPos(0.2f, -1.5f, 0);
        Vec3d thighToSkirtEndR = Vec3(skirtEndR.getModelPosition()).subtract(Vec3(thighR.getModelPosition())).normalize();
        Vec3d thighToSkirtEndL = Vec3(skirtEndL.getModelPosition()).subtract(Vec3(thighL.getModelPosition())).normalize();
        float rightDot = (float) thighToKneeR.dotProduct(new Vec3d(0, -1, 0));
        rightDot = (float) Math.pow(Math.max(rightDot, 0), 3);
        float leftDot = (float) thighToKneeL.dotProduct(new Vec3d(0, -1, 0));
        leftDot = (float) Math.pow(Math.max(leftDot, 0), 3);
        skirtJointR.addPos(Math.max(-0.9f * rightDot, -0.7f), 0, Math.max(-0.7f * rightDot, -0.5f));
        skirtJointL.addPos(-Math.max(-0.9f * leftDot, -0.7f), 0, Math.max(-0.7f * leftDot, -0.5f));


        Quaternionf rightRot = RigUtils.betweenVectors(thighToSkirtEndR, thighToKneeR);
        Quaternionf leftRot = RigUtils.betweenVectors(thighToSkirtEndL, thighToKneeL);
        Matrix4f rightMat = (new Matrix4f()).rotate(rightRot);
        Matrix4f leftMat = (new Matrix4f()).rotate(leftRot);
        skirtJoint2R.setModelRotationMat(rightMat);
        skirtJoint2L.setModelRotationMat(leftMat);

        Vec3d average = thighToKneeL.add(thighToKneeR).multiply(2).multiply(0, 1, 1).normalize();
        float angleAv = (float) MathHelper.atan2(average.getY(), average.getZ());
        skirtBack.setRotX(skirtBack.getRotX() - angleAv + 3.48f);
        skirtFront.setRotX(skirtFront.getRotX() - Math.min(angleAv, -2) + 3.48f);
        Vec3d skirtFrontDiff = Vec3(skirtLocFrontL.getModelPosition()).subtract(Vec3(skirtLocFrontR.getModelPosition()));
        skirtFront.setScaleX(Math.max((float) (0.3f + 0.15f * skirtFrontDiff.length()), 0.4f));
        Vec3d skirtBackDiff = Vec3(skirtLocBackL.getModelPosition()).subtract(Vec3(skirtLocBackR.getModelPosition()));
        skirtBack.setScaleX((float) (0.15f + 0.1f * skirtBackDiff.length()));
        float angleF = (float) MathHelper.atan2(skirtFrontDiff.normalize().getZ(), skirtFrontDiff.normalize().getX());
        if (angleF < 0.001 || angleF > 3.141) angleF = 0;
        skirtFront.setRotY(angleF * 0.6f);
        skirtFront.addPos(0.5f * (float) (skirtLocFrontR.getModelPosition().x() + skirtFrontDiff.multiply(0.5).x), 0, 0);
        float angleB = (float) MathHelper.atan2(skirtBackDiff.normalize().getZ(), skirtBackDiff.normalize().getX());
        skirtBack.setRotY(angleB * 0.5f);
        skirtBack.addPos(0.5f * (float) (skirtLocBackR.getModelPosition().x() + skirtBackDiff.multiply(0.5).x), 0, 0);
        float bothDots = (float) Math.pow(rightDot * leftDot, 1);
        float f = Math.min(1, bothDots * 2);
        skirtR.addRot(0, MathHelper.clamp(angleF, -0.5f, 0.5f) * (1 - f) - bothDots * 0.4f, 0);
        skirtL.addRot(0, MathHelper.clamp(angleF, -0.5f, 0.5f) * (1 - f) + bothDots * 0.4f, 0);

        MowzieGeoBone frontCloth = this.getMowzieBone("clothFront");
        MowzieGeoBone frontCloth2 = this.getMowzieBone("clothFront2");

        frontCloth.setRot(skirtFront.getRot());
        Matrix4f mat = frontCloth.getModelRotationMat();
        mat.invert();
        frontCloth2.setModelRotationMat(mat);
    }

    private void staffRendering(EntitySculptor entity) {
        MowzieGeoBone itemHandLeft = this.getMowzieBone("itemHandLeft");
        MowzieGeoBone itemHandRight = this.getMowzieBone("itemHandRight");
        MowzieGeoBone backItem = this.getMowzieBone("backItem");

        itemHandLeft.setHidden(true);
        itemHandRight.setHidden(true);
        backItem.setHidden(true);

        MowzieGeoBone staffController = this.getMowzieBone("staffController");

        switch ((int) staffController.getPosX()) {
            case -1:
                itemHandRight.setHidden(false);
                break;
            case 0:
                backItem.setHidden(false);
                break;
            case 1:
                itemHandLeft.setHidden(false);
                break;
        }

        itemHandLeft.setScale(1.2f);
        itemHandRight.setScale(1.2f);
        backItem.setScale(1.2f);
    }

    private void idleAnim(EntitySculptor entity, AnimationState<?> animationState) {
        float frame = entity.frame + animationState.getPartialTick();

        MowzieGeoBone eyebrowRight = this.getMowzieBone("eyebrowRight");
        MowzieGeoBone eyebrowLeft = this.getMowzieBone("eyebrowLeft");
        MowzieGeoBone clothFront2 = this.getMowzieBone("clothFront2");
        MowzieGeoBone clothBack = this.getMowzieBone("clothBack");
        MowzieGeoBone footLeft = this.getMowzieBone("footLeft");
        MowzieGeoBone calfLeft = this.getMowzieBone("calfLeft");
        MowzieGeoBone thighLeft = this.getMowzieBone("thighLeft");
        MowzieGeoBone headJoint = this.getMowzieBone("headJoint");
        MowzieGeoBone chest = this.getMowzieBone("chest");
        MowzieGeoBone footRight = this.getMowzieBone("footRight");
        MowzieGeoBone calfRight = this.getMowzieBone("calfRight");
        MowzieGeoBone thighRight = this.getMowzieBone("thighRight");
        MowzieGeoBone mouth = this.getMowzieBone("mouth");
        MowzieGeoBone handLeft = this.getMowzieBone("handLeft");
        MowzieGeoBone lowerArmLeft = this.getMowzieBone("lowerArmLeft");
        MowzieGeoBone shoulderLeft = this.getMowzieBone("shoulderLeft");
        MowzieGeoBone handRight = this.getMowzieBone("handRight");
        MowzieGeoBone lowerArmRight = this.getMowzieBone("lowerArmRight");
        MowzieGeoBone shoulderRight = this.getMowzieBone("shoulderRight");
        MowzieGeoBone body = this.getMowzieBone("body");

        float idleAnim = this.getControllerValue("idleAnimController");
        float idleSpeed = 0.08f;

        eyebrowRight.addPosY(idleAnim * (float) (Math.sin((frame * idleSpeed + 0.4)) * 0.1));
        eyebrowLeft.addPosY(idleAnim * (float) (Math.sin((frame * idleSpeed + 0.4)) * 0.1));
        clothFront2.addRotX(idleAnim * (float) (Math.sin((frame * idleSpeed - 1)) * 0.05));
        clothBack.addRotX(idleAnim * (float) (-Math.sin((frame * idleSpeed - 1)) * 0.05));
        footLeft.addRotZ(idleAnim * (float) (-0.175 - Math.sin((frame * idleSpeed + 0.5)) * 0.035));
        calfLeft.addRotZ(idleAnim * (float) (0.175 + Math.sin((frame * idleSpeed + 0.5)) * 0.035));
        thighLeft.addRotZ(idleAnim * (float) (0.035 - Math.sin((frame * idleSpeed + 1.5)) * 0.035));
        headJoint.addRotX(idleAnim * (float) (Math.sin((frame * idleSpeed + 1.5)) * 0.017));
        chest.addRotX(idleAnim * (float) (-Math.sin((frame * idleSpeed + 1) * idleSpeed) * 0.017));
        footRight.addRotY(-0.1309f);
        footRight.addRotZ(idleAnim * (float) (-0.35 + Math.sin((frame * idleSpeed + 0.5)) * 0.035));
        calfRight.addRotZ(idleAnim * (float) (0.567 - Math.sin((frame * idleSpeed + 0.5)) * 0.035));
        thighRight.addRotZ(idleAnim * (float) (-0.297 + Math.sin((frame * idleSpeed + 1.5)) * 0.035));
        mouth.addPosY(idleAnim * (float) (-Math.sin((frame * idleSpeed + 0.5)) * 0.1f));
        mouth.setScaleY((float) (mouth.getScaleZ() + idleAnim * Math.sin((frame * idleSpeed + 0.5)) * 0.05f));
        handLeft.addRotY(idleAnim * (float) (-Math.sin((frame * idleSpeed + 1)) * 0.05));
        lowerArmLeft.addRotX(idleAnim * (float) (-Math.sin((frame * idleSpeed + 0.5)) * 0.05));
        lowerArmLeft.addRotY(idleAnim * (float) (-Math.sin((frame * idleSpeed + 0.5)) * 0.05));
        shoulderLeft.addRotZ(idleAnim * (float) (Math.sin((frame * idleSpeed - 0.5)) * 0.05));
        handRight.addRotY(idleAnim * (float) (Math.sin((frame * idleSpeed + 1)) * 0.05));
        lowerArmRight.addRotX(idleAnim * (float) (-Math.sin((frame * idleSpeed + 0.5)) * 0.05));
        lowerArmRight.addRotY(idleAnim * (float) (Math.sin((frame * idleSpeed + 0.5)) * 0.05));
        shoulderRight.addRotZ(idleAnim * (float) (-Math.sin((frame * idleSpeed - 0.5)) * 0.05));
        body.addRotX(idleAnim * (float) (-Math.cos((frame * idleSpeed + 0.5)) * 0.017));
        body.addPosY(idleAnim * (float) (Math.sin(frame * idleSpeed) * 1));
    }
}