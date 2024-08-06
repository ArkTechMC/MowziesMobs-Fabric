package com.bobmowzie.mowziesmobs.client.model.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieGeoBone;
import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieGeoModel;
import com.bobmowzie.mowziesmobs.server.entity.umvuthana.EntityUmvuthi;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import org.apache.commons.lang3.tuple.Triple;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.data.EntityModelData;

import java.util.ArrayList;
import java.util.List;

public class ModelUmvuthi extends MowzieGeoModel<EntityUmvuthi> {
    public ModelUmvuthi() {
        super();
    }

    @Override
    public Identifier getModelResource(EntityUmvuthi object) {
        return new Identifier(MowziesMobs.MODID, "geo/umvuthi.geo.json");
    }

    @Override
    public Identifier getTextureResource(EntityUmvuthi object) {
        return new Identifier(MowziesMobs.MODID, "textures/entity/umvuthi.png");
    }

    @Override
    public Identifier getAnimationResource(EntityUmvuthi object) {
        return new Identifier(MowziesMobs.MODID, "animations/umvuthi.animation.json");
    }

    @Override
    public void setCustomAnimations(EntityUmvuthi entity, long instanceId, AnimationState<EntityUmvuthi> animationState) {
        float frame = entity.frame + animationState.getPartialTick();

        MowzieGeoBone rightThigh = this.getMowzieBone("rightThigh");
        MowzieGeoBone leftThigh = this.getMowzieBone("leftThigh");

        float liftLegs = entity.legsUp.getAnimationProgressSinSqrt();
        leftThigh.addRotX(liftLegs);
        rightThigh.addRotX(liftLegs);
        leftThigh.addRotZ(1.5f * liftLegs);
        rightThigh.addRotZ(-1.5f * liftLegs);
        leftThigh.addRotY(-0.5f * liftLegs);
        rightThigh.addRotY(0.5f * liftLegs);

        if (entity.isAlive() && entity.active) {
            MowzieGeoBone neck1 = this.getMowzieBone("neck");
            MowzieGeoBone neck2 = this.getMowzieBone("neck2");
            MowzieGeoBone head = this.getMowzieBone("head");
            MowzieGeoBone[] lookPieces = new MowzieGeoBone[]{neck1, neck2, head};

            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
            float headYaw = MathHelper.wrapDegrees(entityData.netHeadYaw());
            float headPitch = MathHelper.wrapDegrees(entityData.headPitch());
            float maxYaw = 140f;
            headYaw = MathHelper.clamp(headYaw, -maxYaw, maxYaw);
            for (MowzieGeoBone bone : lookPieces) {
                bone.addRotX(headPitch * ((float) Math.PI / 180F) / (float) lookPieces.length);
                bone.addRotY(headYaw * ((float) Math.PI / 180F) / (float) lookPieces.length);
            }

            MowzieGeoBone featherRaiseController = this.getMowzieBone("featherRaiseController");
            MowzieGeoBone rightFoot = this.getMowzieBone("rightFoot");
            MowzieGeoBone rightAnkle = this.getMowzieBone("rightAnkle");
            MowzieGeoBone rightCalf = this.getMowzieBone("rightCalf");
            MowzieGeoBone leftFoot = this.getMowzieBone("leftFoot");
            MowzieGeoBone leftAnkle = this.getMowzieBone("leftAnkle");
            MowzieGeoBone leftCalf = this.getMowzieBone("leftCalf");
            MowzieGeoBone rightHand = this.getMowzieBone("rightHand");
            MowzieGeoBone rightLowerArm = this.getMowzieBone("rightLowerArm");
            MowzieGeoBone rightArmJoint = this.getMowzieBone("rightArmJoint");
            MowzieGeoBone leftHand = this.getMowzieBone("leftHand");
            MowzieGeoBone leftLowerArm = this.getMowzieBone("leftLowerArm");
            MowzieGeoBone leftArmJoint = this.getMowzieBone("leftArmJoint");
            MowzieGeoBone chest = this.getMowzieBone("chest");
            MowzieGeoBone body = this.getMowzieBone("body");
            MowzieGeoBone headJoint = this.getMowzieBone("headJoint");
            float idleSpeed = 0.08f;
            featherRaiseController.addPosX((float) (Math.sin((frame - 1.2) * idleSpeed) * 0.1));
            body.addRotX((float) (Math.sin((frame - 0.0) * idleSpeed) * 0.035));
            chest.addPosY((float) (-0.7 + Math.sin((frame - 0.0) * idleSpeed) * 0.014));
            chest.addRotX((float) (Math.sin((frame - 0.0) * idleSpeed) * -0.017));
            neck1.addRotX((float) (Math.cos((frame - 0.3) * idleSpeed) * -0.052));
            neck2.addRotX((float) (Math.cos((frame - 0.5) * idleSpeed) * -0.052));
            headJoint.addRotX((float) (Math.cos((frame - 0.6) * idleSpeed) * 0.1));
            leftArmJoint.addRotX((float) (Math.sin((frame - 0.0) * idleSpeed) * 0.052));
            leftLowerArm.addRotY((float) (-Math.cos((frame - 0.0) * idleSpeed) * 0.035));
            leftHand.addRotY((float) (-Math.sin((frame - 0.0) * idleSpeed) * 0.087));
            rightArmJoint.addRotX((float) (Math.sin((frame - 0.0) * idleSpeed) * 0.052));
            rightLowerArm.addRotY((float) (Math.cos((frame - 0.0) * idleSpeed) * 0.035));
            rightHand.addRotY((float) (Math.sin((frame - 0.0) * idleSpeed) * 0.087));
            leftThigh.addRotY((float) (Math.cos((frame - 0.0) * idleSpeed) * -0.052) * (1.0f - liftLegs));
            leftThigh.addRotZ((float) (Math.sin((frame - 0.0) * idleSpeed) * -0.052) * (1.0f - liftLegs));
            leftCalf.addRotX((float) (Math.sin((frame - 0.2) * idleSpeed) * 0.087) * (1.0f - liftLegs));
            leftAnkle.addRotX((float) (Math.cos((frame - 0.2) * idleSpeed) * -0.087) * (1.0f - liftLegs));
            leftFoot.addRotX((float) (Math.cos((frame - 0.4) * idleSpeed) * -0.14));
            rightThigh.addRotY((float) (Math.cos((frame - 0.0) * idleSpeed) * 0.052) * (1.0f - liftLegs));
            rightThigh.addRotZ((float) (Math.sin((frame - 0.0) * idleSpeed) * 0.052) * (1.0f - liftLegs));
            rightCalf.addRotX((float) (Math.sin((frame - 0.2) * idleSpeed) * 0.087) * (1.0f - liftLegs));
            rightAnkle.addRotX((float) (Math.cos((frame - 0.2) * idleSpeed) * -0.087) * (1.0f - liftLegs));
            rightFoot.addRotX((float) (Math.cos((frame - 0.4) * idleSpeed) * -0.14));
            leftThigh.addRotY((float) (Math.sin((frame - 0.0) * idleSpeed) * 0.035) * liftLegs);
            rightThigh.addRotY((float) (-Math.sin((frame - 0.0) * idleSpeed) * 0.035) * liftLegs);

            float armAimControl = this.getControllerValue("armAimController");
            leftArmJoint.addRotX(headPitch * ((float) Math.PI / 180F) * armAimControl);
            leftArmJoint.addRotY(headYaw * ((float) Math.PI / 180F) * armAimControl);
        }

        float bellyBounceControl = this.getControllerValue("bellyBounceController");
        float jiggleSpeed = 2.5f;
        float jiggleScale = (float) (bellyBounceControl * 0.1 * Math.cos(jiggleSpeed * frame));
        MowzieGeoBone stomach = this.getMowzieBone("stomach");
        MowzieGeoBone chest = this.getMowzieBone("chest");
        MowzieGeoBone tail = this.getMowzieBone("tail");
        stomach.setScale(stomach.getScaleX() + jiggleScale, stomach.getScaleY() + jiggleScale, stomach.getScaleZ() + jiggleScale);
        chest.addPosY(jiggleScale * 3);
        leftThigh.addPosX(-jiggleScale * 5);
        rightThigh.addPosX(jiggleScale * 5);
        tail.addPosZ(jiggleScale * 4);

        float featherShakeControl = this.getControllerValue("featherShakeController");
        float featherRaiseControl = this.getControllerValue("featherRaiseController");
        List<Triple<MowzieGeoBone, Direction.Axis, Boolean>> feathers = new ArrayList<>();
        feathers.add(Triple.of(this.getMowzieBone("neckFeathersFront1"), Direction.Axis.X, false));
        feathers.add(Triple.of(this.getMowzieBone("neckFeathersFront2"), Direction.Axis.X, false));
        feathers.add(Triple.of(this.getMowzieBone("neckFeathersFront3"), Direction.Axis.X, false));
        feathers.add(Triple.of(this.getMowzieBone("neckFeathersFront4"), Direction.Axis.X, false));
        feathers.add(Triple.of(this.getMowzieBone("neckFeathersBack1"), Direction.Axis.X, true));
        feathers.add(Triple.of(this.getMowzieBone("neckFeathersBack2"), Direction.Axis.X, true));
        feathers.add(Triple.of(this.getMowzieBone("neckFeathersBack3"), Direction.Axis.X, true));
        feathers.add(Triple.of(this.getMowzieBone("neckFeathersBack4"), Direction.Axis.X, true));
        feathers.add(Triple.of(this.getMowzieBone("neckFeathersBack5"), Direction.Axis.X, true));
        feathers.add(Triple.of(this.getMowzieBone("neckFeathersLeft1"), Direction.Axis.Z, true));
        feathers.add(Triple.of(this.getMowzieBone("neckFeathersLeft2"), Direction.Axis.Z, true));
        feathers.add(Triple.of(this.getMowzieBone("neckFeathersLeft3"), Direction.Axis.Z, true));
        feathers.add(Triple.of(this.getMowzieBone("neckFeathersLeft4"), Direction.Axis.Z, true));
        feathers.add(Triple.of(this.getMowzieBone("neckFeathersLeft5"), Direction.Axis.Z, true));
        feathers.add(Triple.of(this.getMowzieBone("neckFeathersRight1"), Direction.Axis.Z, false));
        feathers.add(Triple.of(this.getMowzieBone("neckFeathersRight2"), Direction.Axis.Z, false));
        feathers.add(Triple.of(this.getMowzieBone("neckFeathersRight3"), Direction.Axis.Z, false));
        feathers.add(Triple.of(this.getMowzieBone("neckFeathersRight4"), Direction.Axis.Z, false));
        feathers.add(Triple.of(this.getMowzieBone("neckFeathersRight5"), Direction.Axis.Z, false));
        feathers.add(Triple.of(this.getMowzieBone("chestFeathersFront1"), Direction.Axis.X, false));
        feathers.add(Triple.of(this.getMowzieBone("chestFeathersFront2"), Direction.Axis.X, false));
        feathers.add(Triple.of(this.getMowzieBone("chestFeathersFront3"), Direction.Axis.X, false));
        feathers.add(Triple.of(this.getMowzieBone("chestFeathersLeft1"), Direction.Axis.X, false));
        feathers.add(Triple.of(this.getMowzieBone("chestFeathersLeft2"), Direction.Axis.X, false));
        feathers.add(Triple.of(this.getMowzieBone("chestFeathersLeft3"), Direction.Axis.X, false));
        feathers.add(Triple.of(this.getMowzieBone("chestFeathersRight1"), Direction.Axis.X, false));
        feathers.add(Triple.of(this.getMowzieBone("chestFeathersRight2"), Direction.Axis.X, false));
        feathers.add(Triple.of(this.getMowzieBone("chestFeathersRight3"), Direction.Axis.X, false));
        feathers.add(Triple.of(this.getMowzieBone("bellyFeathersFront1"), Direction.Axis.X, false));
        feathers.add(Triple.of(this.getMowzieBone("bellyFeathersFront2"), Direction.Axis.X, true));
        feathers.add(Triple.of(this.getMowzieBone("bellyFeathersFront3"), Direction.Axis.X, false));
        feathers.add(Triple.of(this.getMowzieBone("bellyFeathersRight1"), Direction.Axis.X, false));
        feathers.add(Triple.of(this.getMowzieBone("bellyFeathersRight2"), Direction.Axis.X, false));
        feathers.add(Triple.of(this.getMowzieBone("bellyFeathersRight3"), Direction.Axis.X, true));
        feathers.add(Triple.of(this.getMowzieBone("bellyFeathersRight4"), Direction.Axis.X, false));
        feathers.add(Triple.of(this.getMowzieBone("bellyFeathersRight5"), Direction.Axis.X, false));
        feathers.add(Triple.of(this.getMowzieBone("bellyFeathersLeft1"), Direction.Axis.X, false));
        feathers.add(Triple.of(this.getMowzieBone("bellyFeathersLeft2"), Direction.Axis.X, false));
        feathers.add(Triple.of(this.getMowzieBone("bellyFeathersLeft3"), Direction.Axis.X, true));
        feathers.add(Triple.of(this.getMowzieBone("bellyFeathersLeft4"), Direction.Axis.X, false));
        feathers.add(Triple.of(this.getMowzieBone("bellyFeathersLeft5"), Direction.Axis.X, false));
        feathers.add(Triple.of(this.getMowzieBone("chestDraperyFront"), Direction.Axis.X, false));
        feathers.add(Triple.of(this.getMowzieBone("chestDraperyRight"), Direction.Axis.Z, false));
        feathers.add(Triple.of(this.getMowzieBone("chestDraperyLeft"), Direction.Axis.Z, true));
        feathers.add(Triple.of(this.getMowzieBone("chestDraperyBack"), Direction.Axis.X, true));

        for (Triple<MowzieGeoBone, Direction.Axis, Boolean> feather : feathers) {
            MowzieGeoBone bone = feather.getLeft();
            float oscillation = (float) (featherShakeControl * 0.13 * Math.cos(1.4 * frame + bone.getPivotY() * -0.15 + bone.getPivotZ() * -0.1)) + featherRaiseControl;
            if (feather.getRight()) oscillation *= -1;
            Direction.Axis axis = feather.getMiddle();
            if (axis == Direction.Axis.X) {
                bone.addRotX(oscillation);
            } else if (axis == Direction.Axis.Y) {
                bone.addRotY(oscillation);
            } else {
                bone.addRotZ(oscillation);
            }
        }

        MowzieGeoBone mask = this.getMowzieBone("mask");
        MowzieGeoBone body = this.getMowzieBone("body");
        mask.setScale(1.0f / (float) body.getScale().x, 1.0f / (float) body.getScale().y, 1.0f / (float) body.getScale().z);

    }
}