package com.bobmowzie.mowziesmobs.client.model.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieGeoBone;
import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieGeoModel;
import com.bobmowzie.mowziesmobs.server.entity.umvuthana.EntityUmvuthana;
import com.bobmowzie.mowziesmobs.server.entity.umvuthana.MaskType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.data.EntityModelData;

public class ModelUmvuthana extends MowzieGeoModel<EntityUmvuthana> {
    public ModelUmvuthana() {
        super();
    }

    @Override
    public Identifier getModelResource(EntityUmvuthana object) {
        return new Identifier(MowziesMobs.MODID, "geo/umvuthana.geo.json");
    }

    @Override
    public Identifier getTextureResource(EntityUmvuthana entity) {
        boolean isElite = entity.getMaskType() == MaskType.FAITH || entity.getMaskType() == MaskType.FURY;
        return new Identifier(MowziesMobs.MODID, isElite ? "textures/entity/umvuthana_elite.png" : "textures/entity/umvuthana.png");
    }

    @Override
    public Identifier getAnimationResource(EntityUmvuthana object) {
        return new Identifier(MowziesMobs.MODID, "animations/umvuthana.animation.json");
    }

    @Override
    public void setCustomAnimations(EntityUmvuthana entity, long instanceId, AnimationState<EntityUmvuthana> animationState) {
        boolean isRaptor = entity.getMaskType() == MaskType.FURY;
        boolean isElite = entity.getMaskType() == MaskType.FAITH || isRaptor;
        this.getMowzieBone("crestRight").setHidden(!isElite);
        this.getMowzieBone("crestLeft").setHidden(!isElite);
        this.getMowzieBone("crest1").setHidden(!isElite);
        this.getMowzieBone("leftIndexTalon").setHidden(!isRaptor);
        this.getMowzieBone("leftIndexClaw").setHidden(isRaptor);
        this.getMowzieBone("rightIndexTalon").setHidden(!isRaptor);
        this.getMowzieBone("rightIndexClaw").setHidden(isRaptor);
        MowzieGeoBone root = this.getMowzieBone("root");
        if (isElite) {
            root.multiplyScale(0.93f, 0.93f, 0.93f);
        } else {
            root.multiplyScale(0.83f, 0.83f, 0.83f);
        }

        MowzieGeoBone mask = this.getMowzieBone("mask");
        MowzieGeoBone hips = this.getMowzieBone("hips");
        if (entity.getActiveAbilityType() != EntityUmvuthana.TELEPORT_ABILITY) {
            mask.setScale(1.0f / (float) hips.getScale().x, 1.0f / (float) hips.getScale().y, 1.0f / (float) hips.getScale().z);
        }

        if (entity.isAlive() && entity.active) {
            MowzieGeoBone head = this.getMowzieBone("head");
            MowzieGeoBone neck = this.getMowzieBone("neck");
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
            float headYaw = MathHelper.wrapDegrees(entityData.netHeadYaw());
            float headPitch = MathHelper.wrapDegrees(entityData.headPitch());
            head.addRotX(headPitch * ((float) Math.PI / 180F) / 2f);
            head.addRotY(headYaw * ((float) Math.PI / 180F) / 2f);
            neck.addRotX(headPitch * ((float) Math.PI / 180F) / 2f);
            neck.addRotY(headYaw * ((float) Math.PI / 180F) / 2f);
        }

        MowzieGeoBone maskHand = this.getMowzieBone("maskHand");
        MowzieGeoBone maskTwitcher = this.getMowzieBone("maskTwitcher");
        float maskPlaceSwitch = this.getControllerValue("maskPlacementSwitchController");
        if (maskPlaceSwitch == 1.0) {
            maskTwitcher.setHidden(true);
            maskHand.setHidden(false);
        } else {
            maskTwitcher.setHidden(false);
            maskHand.setHidden(true);
        }

        float animSpeed = 1.4f;
        float limbSwing = animationState.getLimbSwing();
        float limbSwingAmount = animationState.getLimbSwingAmount();

//        limbSwing = 0.5f * (entity.tickCount + customPredicate.getPartialTick());
//        limbSwingAmount = 1f;
//        float angle = 0.03f * (entity.tickCount + customPredicate.getPartialTick());
//        Vec3 moveVec = new Vec3(1.0, 0, 0);
//        moveVec = moveVec.yRot(angle);

        Vec3d moveVec = entity.getVelocity().normalize().rotateY((float) Math.toRadians(entity.bodyYaw + 90.0));
        float forward = (float) Math.max(0, new Vec3d(1.0, 0, 0).dotProduct(moveVec));
        float backward = (float) Math.max(0, new Vec3d(-1.0, 0, 0).dotProduct(moveVec));
        float left = (float) Math.max(0, new Vec3d(0, 0, -1.0).dotProduct(moveVec));
        float right = (float) Math.max(0, new Vec3d(0, 0, 1.0).dotProduct(moveVec));
        limbSwingAmount *= 2;
        limbSwingAmount = Math.min(0.7f, limbSwingAmount);
        float locomotionAnimController = this.getControllerValue("locomotionAnimController");
        float runAnim = this.getControllerValue("walkRunSwitchController");
        float walkAnim = 1.0f - runAnim;
        this.walkForwardAnim(forward * locomotionAnimController * walkAnim, limbSwing, limbSwingAmount, animSpeed);
        this.walkBackwardAnim(backward * locomotionAnimController * walkAnim, limbSwing, limbSwingAmount, animSpeed);
        this.walkLeftAnim(left * locomotionAnimController * walkAnim, limbSwing, limbSwingAmount, animSpeed);
        this.walkRightAnim(right * locomotionAnimController * walkAnim, limbSwing, limbSwingAmount, animSpeed);

        this.runAnim(locomotionAnimController * runAnim, limbSwing, limbSwingAmount, animSpeed);
    }

    private void runAnim(float blend, float limbSwing, float limbSwingAmount, float speed) {
        MowzieGeoBone head = this.getMowzieBone("head");
        MowzieGeoBone neck = this.getMowzieBone("neck");
        MowzieGeoBone hips = this.getMowzieBone("hips");
        MowzieGeoBone stomach = this.getMowzieBone("stomach");
        MowzieGeoBone chest = this.getMowzieBone("chest");
        MowzieGeoBone leftThigh = this.getMowzieBone("leftThigh");
        MowzieGeoBone leftShin = this.getMowzieBone("leftShin");
        MowzieGeoBone leftAnkle = this.getMowzieBone("leftAnkle");
        MowzieGeoBone leftFoot = this.getMowzieBone("leftFoot");
        MowzieGeoBone leftToesBack = this.getMowzieBone("leftToesBack");
        MowzieGeoBone rightThigh = this.getMowzieBone("rightThigh");
        MowzieGeoBone rightShin = this.getMowzieBone("rightShin");
        MowzieGeoBone rightAnkle = this.getMowzieBone("rightAnkle");
        MowzieGeoBone rightFoot = this.getMowzieBone("rightFoot");
        MowzieGeoBone rightToesBack = this.getMowzieBone("rightToesBack");
        MowzieGeoBone leftArm = this.getMowzieBone("leftArm");
        MowzieGeoBone leftForeArm = this.getMowzieBone("leftForeArm");
        MowzieGeoBone leftHand = this.getMowzieBone("leftHand");
        MowzieGeoBone rightArm = this.getMowzieBone("rightArm");
        MowzieGeoBone rightForeArm = this.getMowzieBone("rightForeArm");
        MowzieGeoBone rightHand = this.getMowzieBone("rightHand");

        float globalHeight = 1.5f;
        float globalDegree = 1.7f;
        speed *= 0.8;

        hips.addPosY(blend * (float) (Math.cos(limbSwing * speed - 1.7) * 2f * globalHeight + 4 * globalHeight) * limbSwingAmount);
        hips.addRotX(blend * -0.4f * limbSwingAmount * globalHeight);
        hips.addRotY(blend * (float) (Math.cos(limbSwing * speed * 0.5 + 1.0) * -0.1f * globalHeight) * limbSwingAmount);
        chest.addRotY(blend * (float) (-Math.cos(limbSwing * speed * 0.5 + 1.0) * -0.2f * globalHeight) * limbSwingAmount);
        stomach.addRotX(blend * -(float) (Math.cos(limbSwing * speed + 1.4 - 1.7) * 0.025 * globalHeight) * limbSwingAmount);
        neck.addRotY(blend * (float) (Math.cos(limbSwing * speed * 0.5 + 1.0) * -0.1f * globalHeight) * limbSwingAmount);
        neck.addRotX(blend * -(float) (Math.cos(limbSwing * speed - 1.5) * 0.25 * globalHeight - 0.2 * globalHeight) * limbSwingAmount);
        head.addRotX(blend * (float) (Math.cos(limbSwing * speed + 0.175 - 1.7) * 0.25 * globalHeight + 0.2 * globalHeight) * limbSwingAmount);

        leftThigh.addRotX(blend * (float) (Math.cos(limbSwing * speed * 0.5 + 1.5) * 0.55f * globalDegree) * limbSwingAmount);
        leftThigh.addRotY(blend * (float) (Math.cos(limbSwing * speed * 0.5 + 1.5) * 0.1f * globalDegree - 0.2f) * limbSwingAmount);
        leftShin.addRotX(blend * (float) (Math.cos(limbSwing * speed * 0.5 + 2.50) * -0.7f * globalDegree) * limbSwingAmount);
        leftAnkle.addRotX(blend * (float) (Math.cos(limbSwing * speed * 0.5 + 2.50) * 1.1f * globalDegree + 0.1f * globalDegree) * limbSwingAmount);
        leftFoot.addRotX(blend * (float) (Math.cos(limbSwing * speed * 0.5 + 3.5) * -1f * globalDegree - 1.1f * globalDegree) * limbSwingAmount);
        leftToesBack.addRotX(blend * (float) (Math.cos(limbSwing * speed * 0.5 + 3.1) * 1.6f * globalDegree + 1.8f * globalDegree) * limbSwingAmount);
        leftToesBack.addRotX(blend * (float) (Math.cos(limbSwing * speed * 1 + 0.1) * 0.3f * globalDegree) * limbSwingAmount);

        rightThigh.addRotX(blend * (float) (-Math.cos(limbSwing * speed * 0.5 + 1.5) * 0.55f * globalDegree) * limbSwingAmount);
        rightThigh.addRotY(blend * (float) (Math.cos(limbSwing * speed * 0.5 + 1.5) * 0.1f * globalDegree + 0.2f) * limbSwingAmount);
        rightShin.addRotX(blend * (float) (-Math.cos(limbSwing * speed * 0.5 + 2.50) * -0.7f * globalDegree) * limbSwingAmount);
        rightAnkle.addRotX(blend * (float) (-Math.cos(limbSwing * speed * 0.5 + 2.50) * 1.1f * globalDegree + 0.1f * globalDegree) * limbSwingAmount);
        rightFoot.addRotX(blend * (float) (-Math.cos(limbSwing * speed * 0.5 + 3.5) * -1f * globalDegree - 1.1f * globalDegree) * limbSwingAmount);
        rightToesBack.addRotX(blend * (float) (-Math.cos(limbSwing * speed * 0.5 + 3.1) * 1.6f * globalDegree + 1.8f * globalDegree) * limbSwingAmount);
        rightToesBack.addRotX(blend * (float) (-Math.cos(limbSwing * speed * 1 + 0.1) * 0.3f * globalDegree) * limbSwingAmount);

        leftArm.addRotY(blend * (float) -(Math.cos(limbSwing * speed + 0.52) * 0.09 * globalHeight) * limbSwingAmount);
        leftArm.addRotZ(blend * (float) (Math.cos(limbSwing * speed + 0.52) * 0.09 * globalHeight) * limbSwingAmount);
        leftForeArm.addRotX(blend * (float) (Math.cos(limbSwing * speed - 1.0) * 0.05 * globalHeight) * limbSwingAmount);

        rightArm.addRotY(blend * (float) (Math.cos(limbSwing * speed + 0.52) * 0.09 * globalHeight) * limbSwingAmount);
        rightArm.addRotZ(blend * (float) -(Math.cos(limbSwing * speed + 0.52) * 0.09 * globalHeight) * limbSwingAmount);
        rightForeArm.addRotX(blend * (float) (Math.cos(limbSwing * speed - 1.0) * 0.05 * globalHeight) * limbSwingAmount);
    }

    private void walkForwardAnim(float blend, float limbSwing, float limbSwingAmount, float speed) {
        MowzieGeoBone head = this.getMowzieBone("head");
        MowzieGeoBone neck = this.getMowzieBone("neck");
        MowzieGeoBone hips = this.getMowzieBone("hips");
        MowzieGeoBone stomach = this.getMowzieBone("stomach");
        MowzieGeoBone chest = this.getMowzieBone("chest");
        MowzieGeoBone leftThigh = this.getMowzieBone("leftThigh");
        MowzieGeoBone leftShin = this.getMowzieBone("leftShin");
        MowzieGeoBone leftAnkle = this.getMowzieBone("leftAnkle");
        MowzieGeoBone leftFoot = this.getMowzieBone("leftFoot");
        MowzieGeoBone leftToesBack = this.getMowzieBone("leftToesBack");
        MowzieGeoBone rightThigh = this.getMowzieBone("rightThigh");
        MowzieGeoBone rightShin = this.getMowzieBone("rightShin");
        MowzieGeoBone rightAnkle = this.getMowzieBone("rightAnkle");
        MowzieGeoBone rightFoot = this.getMowzieBone("rightFoot");
        MowzieGeoBone rightToesBack = this.getMowzieBone("rightToesBack");
        MowzieGeoBone leftArm = this.getMowzieBone("leftArm");
        MowzieGeoBone leftForeArm = this.getMowzieBone("leftForeArm");
        MowzieGeoBone leftHand = this.getMowzieBone("leftHand");
        MowzieGeoBone rightArm = this.getMowzieBone("rightArm");
        MowzieGeoBone rightForeArm = this.getMowzieBone("rightForeArm");
        MowzieGeoBone rightHand = this.getMowzieBone("rightHand");

        float globalHeight = 1.5f;
        float globalDegree = 1.5f;

        hips.addPosY(blend * (float) (Math.cos(limbSwing * speed) * 1.5f * globalHeight) * limbSwingAmount);
        hips.addRotX(blend * -0.18f * limbSwingAmount * globalHeight);
        hips.addRotY(blend * (float) (Math.cos(limbSwing * speed * 0.5 + 1.0) * -0.1f * globalHeight) * limbSwingAmount);
        chest.addRotY(blend * (float) (-Math.cos(limbSwing * speed * 0.5 + 1.0) * -0.2f * globalHeight) * limbSwingAmount);
        stomach.addRotX(blend * -(float) (Math.cos(limbSwing * speed + 1.4) * 0.025 * globalHeight) * limbSwingAmount);
        neck.addRotY(blend * (float) (Math.cos(limbSwing * speed * 0.5 + 1.0) * -0.1f * globalHeight) * limbSwingAmount);
        neck.addRotX(blend * -(float) (Math.cos(limbSwing * speed) * 0.175 * globalHeight) * limbSwingAmount);
        head.addRotX(blend * (float) (Math.cos(limbSwing * speed + 0.175) * 0.175 * globalHeight + 0.18 * globalHeight) * limbSwingAmount);

        leftThigh.addRotX(blend * (float) (Math.cos(limbSwing * speed * 0.5 + 1.5) * 0.55f * globalDegree) * limbSwingAmount);
        leftThigh.addRotY(blend * (float) (Math.cos(limbSwing * speed * 0.5 + 1.5) * 0.1f * globalDegree - 0.15f) * limbSwingAmount);
        leftShin.addRotX(blend * (float) (Math.cos(limbSwing * speed * 0.5 + 2.40) * -0.7f * globalDegree) * limbSwingAmount);
        leftAnkle.addRotX(blend * (float) (Math.cos(limbSwing * speed * 0.5 + 2.40) * 1.1f * globalDegree + 0.1f * globalDegree) * limbSwingAmount);
        leftFoot.addRotX(blend * (float) (Math.cos(limbSwing * speed * 0.5 + 2.5) * -1.3f * globalDegree - 0.4f * globalDegree) * limbSwingAmount);
        leftFoot.addRotX(blend * (float) (Math.cos(limbSwing * speed * 1 - 0.2) * -0.2f * globalDegree) * limbSwingAmount);
        leftToesBack.addRotX(blend * (float) (Math.cos(limbSwing * speed * 0.5 + 3.1) * 1.6f * globalDegree + 1.4f * globalDegree) * limbSwingAmount);
        leftToesBack.addRotX(blend * (float) (Math.cos(limbSwing * speed * 1 + 0.1) * 0.3f * globalDegree) * limbSwingAmount);

        rightThigh.addRotX(blend * -(float) (Math.cos(limbSwing * speed * 0.5 + 1.5) * 0.55f * globalDegree) * limbSwingAmount);
        rightThigh.addRotY(blend * (float) (Math.cos(limbSwing * speed * 0.5 + 1.5) * 0.1f * globalDegree + 0.15f) * limbSwingAmount);
        rightShin.addRotX(blend * -(float) (Math.cos(limbSwing * speed * 0.5 + 2.40) * -0.7f * globalDegree) * limbSwingAmount);
        rightAnkle.addRotX(blend * -(float) (Math.cos(limbSwing * speed * 0.5 + 2.40) * 1.1f * globalDegree - 0.1f * globalDegree) * limbSwingAmount);
        rightFoot.addRotX(blend * -(float) (Math.cos(limbSwing * speed * 0.5 + 2.5) * -1.3f * globalDegree + 0.4f * globalDegree) * limbSwingAmount);
        rightFoot.addRotX(blend * (float) (Math.cos(limbSwing * speed * 1 - 0.2) * -0.2f * globalDegree) * limbSwingAmount);
        rightToesBack.addRotX(blend * -(float) (Math.cos(limbSwing * speed * 0.5 + 3.1) * 1.6f * globalDegree - 1.4f * globalDegree) * limbSwingAmount);
        rightToesBack.addRotX(blend * (float) (Math.cos(limbSwing * speed * 1 + 0.1) * 0.3f * globalDegree) * limbSwingAmount);

        leftArm.addRotY(blend * (float) -(Math.cos(limbSwing * speed + 0.52) * 0.0707 * globalHeight) * limbSwingAmount);
        leftArm.addRotZ(blend * (float) (Math.cos(limbSwing * speed + 0.52) * 0.0707 * globalHeight) * limbSwingAmount);
        leftForeArm.addRotX(blend * (float) (Math.cos(limbSwing * speed - 1.0) * 0.03 * globalHeight) * limbSwingAmount);

        rightArm.addRotY(blend * (float) (Math.cos(limbSwing * speed + 0.52) * 0.0707 * globalHeight) * limbSwingAmount);
        rightArm.addRotZ(blend * (float) -(Math.cos(limbSwing * speed + 0.52) * 0.0707 * globalHeight) * limbSwingAmount);
        rightForeArm.addRotX(blend * (float) (Math.cos(limbSwing * speed - 1.0) * 0.03 * globalHeight) * limbSwingAmount);
    }

    private void walkBackwardAnim(float blend, float limbSwing, float limbSwingAmount, float speed) {
        MowzieGeoBone head = this.getMowzieBone("head");
        MowzieGeoBone neck = this.getMowzieBone("neck");
        MowzieGeoBone hips = this.getMowzieBone("hips");
        MowzieGeoBone stomach = this.getMowzieBone("stomach");
        MowzieGeoBone chest = this.getMowzieBone("chest");
        MowzieGeoBone leftThigh = this.getMowzieBone("leftThigh");
        MowzieGeoBone leftShin = this.getMowzieBone("leftShin");
        MowzieGeoBone leftAnkle = this.getMowzieBone("leftAnkle");
        MowzieGeoBone leftFoot = this.getMowzieBone("leftFoot");
        MowzieGeoBone leftToesBack = this.getMowzieBone("leftToesBack");
        MowzieGeoBone rightThigh = this.getMowzieBone("rightThigh");
        MowzieGeoBone rightShin = this.getMowzieBone("rightShin");
        MowzieGeoBone rightAnkle = this.getMowzieBone("rightAnkle");
        MowzieGeoBone rightFoot = this.getMowzieBone("rightFoot");
        MowzieGeoBone rightToesBack = this.getMowzieBone("rightToesBack");
        MowzieGeoBone leftArm = this.getMowzieBone("leftArm");
        MowzieGeoBone leftForeArm = this.getMowzieBone("leftForeArm");
        MowzieGeoBone leftHand = this.getMowzieBone("leftHand");
        MowzieGeoBone rightArm = this.getMowzieBone("rightArm");
        MowzieGeoBone rightForeArm = this.getMowzieBone("rightForeArm");
        MowzieGeoBone rightHand = this.getMowzieBone("rightHand");

        float globalHeight = 1.5f;
        float globalDegree = 1.5f;

        hips.addPosY(blend * (float) (Math.cos(limbSwing * speed) * 1.5f * globalHeight) * limbSwingAmount);
        hips.addRotX(blend * 0.18f * limbSwingAmount * globalHeight);
        hips.addRotY(blend * (float) (Math.cos(limbSwing * speed * 0.5 + 1.0) * 0.1f * globalHeight) * limbSwingAmount);
        chest.addRotY(blend * (float) (Math.cos(limbSwing * speed * 0.5 + 1.0) * -0.2f * globalHeight) * limbSwingAmount);
        stomach.addRotX(blend * -(float) (Math.cos(limbSwing * speed + 1.4) * 0.025 * globalHeight) * limbSwingAmount);
        neck.addRotY(blend * (float) (Math.cos(limbSwing * speed * 0.5 + 1.0) * 0.1f * globalHeight) * limbSwingAmount);
        neck.addRotX(blend * -(float) (Math.cos(limbSwing * speed) * 0.175 * globalHeight) * limbSwingAmount);
        head.addRotX(blend * (float) (Math.cos(limbSwing * speed + 0.175) * 0.175 * globalHeight - 0.18 * globalHeight) * limbSwingAmount);

        leftThigh.addRotX(blend * (float) (Math.cos(limbSwing * speed * 0.5 - 1.5) * 0.55f * globalDegree - 0.3 * globalDegree) * limbSwingAmount);
        leftThigh.addRotY(blend * (float) (Math.cos(limbSwing * speed * 0.5 - 1.5) * 0.1f * globalDegree - 0.15f) * limbSwingAmount);
        leftShin.addRotX(blend * (float) (Math.cos(limbSwing * speed * 0.5 - 2.40) * -0.7f * globalDegree) * limbSwingAmount);
        leftAnkle.addRotX(blend * (float) (Math.cos(limbSwing * speed * 0.5 - 2.40) * 1.1f * globalDegree + 0.1f * globalDegree) * limbSwingAmount);
        leftFoot.addRotX(blend * (float) (Math.cos(limbSwing * speed * 0.5 - 2.5) * -1.3f * globalDegree - 0.4f * globalDegree) * limbSwingAmount);
        leftFoot.addRotX(blend * (float) (Math.cos(limbSwing * speed * 1 + 0.2) * -0.2f * globalDegree) * limbSwingAmount);
        leftToesBack.addRotX(blend * (float) (Math.cos(limbSwing * speed * 0.5 - 3.1) * 1.6f * globalDegree + 1.4f * globalDegree) * limbSwingAmount);
        leftToesBack.addRotX(blend * (float) (Math.cos(limbSwing * speed * 1 - 0.1) * 0.3f * globalDegree) * limbSwingAmount);

        rightThigh.addRotX(blend * -(float) (Math.cos(limbSwing * speed * 0.5 - 1.5) * 0.55f * globalDegree + 0.3 * globalDegree) * limbSwingAmount);
        rightThigh.addRotY(blend * (float) (Math.cos(limbSwing * speed * 0.5 - 1.5) * 0.1f * globalDegree + 0.15f) * limbSwingAmount);
        rightShin.addRotX(blend * -(float) (Math.cos(limbSwing * speed * 0.5 - 2.40) * -0.7f * globalDegree) * limbSwingAmount);
        rightAnkle.addRotX(blend * -(float) (Math.cos(limbSwing * speed * 0.5 - 2.40) * 1.1f * globalDegree - 0.1f * globalDegree) * limbSwingAmount);
        rightFoot.addRotX(blend * -(float) (Math.cos(limbSwing * speed * 0.5 - 2.5) * -1.3f * globalDegree + 0.4f * globalDegree) * limbSwingAmount);
        rightFoot.addRotX(blend * (float) (Math.cos(limbSwing * speed * 1 + 0.2) * -0.2f * globalDegree) * limbSwingAmount);
        rightToesBack.addRotX(blend * -(float) (Math.cos(limbSwing * speed * 0.5 - 3.1) * 1.6f * globalDegree - 1.4f * globalDegree) * limbSwingAmount);
        rightToesBack.addRotX(blend * (float) (Math.cos(limbSwing * speed * 1 - 0.1) * 0.3f * globalDegree) * limbSwingAmount);

        leftArm.addRotY(blend * (float) -(Math.cos(limbSwing * speed + 0.52) * 0.0707 * globalHeight) * limbSwingAmount);
        leftArm.addRotZ(blend * (float) (Math.cos(limbSwing * speed + 0.52) * 0.0707 * globalHeight) * limbSwingAmount);
        leftForeArm.addRotX(blend * (float) (Math.cos(limbSwing * speed - 1.0) * 0.03 * globalHeight) * limbSwingAmount);

        rightArm.addRotY(blend * (float) (Math.cos(limbSwing * speed + 0.52) * 0.0707 * globalHeight) * limbSwingAmount);
        rightArm.addRotZ(blend * (float) -(Math.cos(limbSwing * speed + 0.52) * 0.0707 * globalHeight) * limbSwingAmount);
        rightForeArm.addRotX(blend * (float) (Math.cos(limbSwing * speed - 1.0) * 0.03 * globalHeight) * limbSwingAmount);
    }

    private void walkLeftAnim(float blend, float limbSwing, float limbSwingAmount, float speed) {
        MowzieGeoBone head = this.getMowzieBone("head");
        MowzieGeoBone neck = this.getMowzieBone("neck");
        MowzieGeoBone hips = this.getMowzieBone("hips");
        MowzieGeoBone stomach = this.getMowzieBone("stomach");
        MowzieGeoBone chest = this.getMowzieBone("chest");
        MowzieGeoBone leftThigh = this.getMowzieBone("leftThigh");
        MowzieGeoBone leftShin = this.getMowzieBone("leftShin");
        MowzieGeoBone leftAnkle = this.getMowzieBone("leftAnkle");
        MowzieGeoBone leftFoot = this.getMowzieBone("leftFoot");
        MowzieGeoBone leftToesBack = this.getMowzieBone("leftToesBack");
        MowzieGeoBone rightThigh = this.getMowzieBone("rightThigh");
        MowzieGeoBone rightShin = this.getMowzieBone("rightShin");
        MowzieGeoBone rightAnkle = this.getMowzieBone("rightAnkle");
        MowzieGeoBone rightFoot = this.getMowzieBone("rightFoot");
        MowzieGeoBone rightToesBack = this.getMowzieBone("rightToesBack");
        MowzieGeoBone leftArm = this.getMowzieBone("leftArm");
        MowzieGeoBone leftForeArm = this.getMowzieBone("leftForeArm");
        MowzieGeoBone leftHand = this.getMowzieBone("leftHand");
        MowzieGeoBone rightArm = this.getMowzieBone("rightArm");
        MowzieGeoBone rightForeArm = this.getMowzieBone("rightForeArm");
        MowzieGeoBone rightHand = this.getMowzieBone("rightHand");

        float globalHeight = 1.5f;
        float globalDegree = 1.5f;

        hips.addPosY(blend * (float) (Math.cos(limbSwing * speed) * 1.5f * globalHeight) * limbSwingAmount);
        hips.addRotX(blend * -0.1f * limbSwingAmount * globalHeight);
        hips.addRotY(blend * (float) (Math.cos(limbSwing * speed * 0.5 + 1.0) * -0.1f * globalHeight) * limbSwingAmount);
        hips.addRotZ(blend * 0.08f * limbSwingAmount * globalHeight);
        chest.addRotY(blend * (float) (-Math.cos(limbSwing * speed * 0.5 + 1.0) * -0.2f * globalHeight) * limbSwingAmount);
        stomach.addRotX(blend * -(float) (Math.cos(limbSwing * speed + 1.4) * 0.025 * globalHeight) * limbSwingAmount);
        stomach.addRotZ(blend * -(float) (Math.cos(limbSwing * speed * 0.5 + 1.4) * 0.02 * globalHeight) * limbSwingAmount);
        stomach.addRotZ(blend * -(float) (Math.cos(limbSwing * speed - 0.5) * 0.02 * globalHeight) * limbSwingAmount);
        neck.addRotY(blend * (float) (Math.cos(limbSwing * speed * 0.5 + 1.0) * -0.1f * globalHeight) * limbSwingAmount);
        neck.addRotX(blend * -(float) (Math.cos(limbSwing * speed) * 0.175 * globalHeight) * limbSwingAmount);
        head.addRotX(blend * (float) (Math.cos(limbSwing * speed + 0.175) * 0.175 * globalHeight + 0.1 * globalHeight) * limbSwingAmount);
        head.addRotZ(blend * (float) (Math.cos(limbSwing * speed * 0.5 + 1.4) * 0.02 * globalHeight) * limbSwingAmount);
        head.addRotZ(blend * (float) (Math.cos(limbSwing * speed - 0.5) * 0.02 * globalHeight) * limbSwingAmount);
        head.addRotZ(blend * -0.03f * limbSwingAmount * globalHeight);

        leftThigh.addRotX(blend * -0.05f * limbSwingAmount * globalHeight);
        leftThigh.addRotZ(blend * -(float) (Math.cos(limbSwing * speed * 0.5 + 1.5) * 0.55f * globalDegree + 0.05 * globalDegree) * limbSwingAmount);
        leftThigh.addRotY(blend * (float) (Math.cos(limbSwing * speed * 0.5 + 1.5) * 0.1f * globalDegree - 0.15) * limbSwingAmount);
        leftShin.addRotX(blend * (float) (Math.cos(limbSwing * speed * 0.5 + 2.40) * -0.7f * globalDegree) * limbSwingAmount);
        leftAnkle.addRotX(blend * (float) (Math.cos(limbSwing * speed * 0.5 + 2.40) * 1.1f * globalDegree + 0.1f * globalDegree) * limbSwingAmount);
        leftFoot.addRotX(blend * (float) (Math.cos(limbSwing * speed * 0.5 + 2.5) * -1.3f * globalDegree - 0.6f * globalDegree) * limbSwingAmount);
        leftFoot.addRotX(blend * (float) (Math.cos(limbSwing * speed * 1 - 0.2) * -0.2f * globalDegree) * limbSwingAmount);
        leftFoot.addRotY(blend * (float) (Math.cos(limbSwing * speed * 0.5 + 2.5) * -0.4f * globalDegree) * limbSwingAmount);
        leftFoot.addRotY(blend * (float) (Math.cos(limbSwing * speed * 1 - 0.2) * -0.2f * globalDegree) * limbSwingAmount);
        leftToesBack.addRotX(blend * (float) (Math.cos(limbSwing * speed * 0.5 + 3.1) * 1.6f * globalDegree + 1.4f * globalDegree) * limbSwingAmount);
        leftToesBack.addRotX(blend * (float) (Math.cos(limbSwing * speed * 1 + 0.1) * 0.3f * globalDegree) * limbSwingAmount);

        rightThigh.addRotX(blend * 0.05f * limbSwingAmount * globalHeight);
        rightThigh.addRotZ(blend * (float) (Math.cos(limbSwing * speed * 0.5 + 1.5) * 0.55f * globalDegree - 0.05 * globalDegree) * limbSwingAmount);
        rightThigh.addRotY(blend * (float) (Math.cos(limbSwing * speed * 0.5 + 1.5) * 0.1f * globalDegree + 0.15) * limbSwingAmount);
        rightShin.addRotX(blend * -(float) (Math.cos(limbSwing * speed * 0.5 + 2.40) * -0.7f * globalDegree) * limbSwingAmount);
        rightAnkle.addRotX(blend * -(float) (Math.cos(limbSwing * speed * 0.5 + 2.40) * 1.1f * globalDegree - 0.1f * globalDegree) * limbSwingAmount);
        rightFoot.addRotX(blend * -(float) (Math.cos(limbSwing * speed * 0.5 + 2.5) * -1.3f * globalDegree + 0.6f * globalDegree) * limbSwingAmount);
        rightFoot.addRotX(blend * (float) (Math.cos(limbSwing * speed * 1 - 0.2) * -0.2f * globalDegree) * limbSwingAmount);
        rightFoot.addRotY(blend * -(float) (Math.cos(limbSwing * speed * 0.5 + 2.5) * -0.4f * globalDegree) * limbSwingAmount);
        rightFoot.addRotY(blend * (float) (Math.cos(limbSwing * speed * 1 - 0.2) * -0.2f * globalDegree) * limbSwingAmount);
        rightToesBack.addRotX(blend * -(float) (Math.cos(limbSwing * speed * 0.5 + 3.1) * 1.6f * globalDegree - 1.4f * globalDegree) * limbSwingAmount);
        rightToesBack.addRotX(blend * (float) (Math.cos(limbSwing * speed * 1 + 0.1) * 0.3f * globalDegree) * limbSwingAmount);

        leftShin.addRotX(blend * -(float) (Math.pow(Math.cos(limbSwing * 0.25 * speed - 0.6), 12) * 0.6f * globalHeight) * limbSwingAmount);
        leftAnkle.addRotX(blend * (float) (Math.pow(Math.cos(limbSwing * 0.25 * speed - 0.6), 12) * 0.6f * globalHeight) * limbSwingAmount);

        leftArm.addRotY(blend * (float) -(Math.cos(limbSwing * speed + 0.52) * 0.0707 * globalHeight) * limbSwingAmount);
        leftArm.addRotZ(blend * (float) (Math.cos(limbSwing * speed + 0.52) * 0.0707 * globalHeight) * limbSwingAmount);
        leftForeArm.addRotX(blend * (float) (Math.cos(limbSwing * speed - 1.0) * 0.03 * globalHeight) * limbSwingAmount);

        rightArm.addRotY(blend * (float) (Math.cos(limbSwing * speed + 0.52) * 0.0707 * globalHeight) * limbSwingAmount);
        rightArm.addRotZ(blend * (float) -(Math.cos(limbSwing * speed + 0.52) * 0.0707 * globalHeight) * limbSwingAmount);
        rightForeArm.addRotX(blend * (float) (Math.cos(limbSwing * speed - 1.0) * 0.03 * globalHeight) * limbSwingAmount);
    }

    private void walkRightAnim(float blend, float limbSwing, float limbSwingAmount, float speed) {
        MowzieGeoBone head = this.getMowzieBone("head");
        MowzieGeoBone neck = this.getMowzieBone("neck");
        MowzieGeoBone hips = this.getMowzieBone("hips");
        MowzieGeoBone stomach = this.getMowzieBone("stomach");
        MowzieGeoBone chest = this.getMowzieBone("chest");
        MowzieGeoBone leftThigh = this.getMowzieBone("leftThigh");
        MowzieGeoBone leftShin = this.getMowzieBone("leftShin");
        MowzieGeoBone leftAnkle = this.getMowzieBone("leftAnkle");
        MowzieGeoBone leftFoot = this.getMowzieBone("leftFoot");
        MowzieGeoBone leftToesBack = this.getMowzieBone("leftToesBack");
        MowzieGeoBone rightThigh = this.getMowzieBone("rightThigh");
        MowzieGeoBone rightShin = this.getMowzieBone("rightShin");
        MowzieGeoBone rightAnkle = this.getMowzieBone("rightAnkle");
        MowzieGeoBone rightFoot = this.getMowzieBone("rightFoot");
        MowzieGeoBone rightToesBack = this.getMowzieBone("rightToesBack");
        MowzieGeoBone leftArm = this.getMowzieBone("leftArm");
        MowzieGeoBone leftForeArm = this.getMowzieBone("leftForeArm");
        MowzieGeoBone leftHand = this.getMowzieBone("leftHand");
        MowzieGeoBone rightArm = this.getMowzieBone("rightArm");
        MowzieGeoBone rightForeArm = this.getMowzieBone("rightForeArm");
        MowzieGeoBone rightHand = this.getMowzieBone("rightHand");

        float globalHeight = 1.5f;
        float globalDegree = 1.5f;

        hips.addPosY(blend * (float) (Math.cos(limbSwing * speed) * 1.5f * globalHeight) * limbSwingAmount);
        hips.addRotX(blend * -0.1f * limbSwingAmount * globalHeight);
        hips.addRotY(blend * (float) (Math.cos(limbSwing * speed * 0.5 + 1.0) * -0.1f * globalHeight) * limbSwingAmount);
        hips.addRotZ(blend * -0.08f * limbSwingAmount * globalHeight);
        chest.addRotY(blend * (float) (-Math.cos(limbSwing * speed * 0.5 + 1.0) * -0.2f * globalHeight) * limbSwingAmount);
        stomach.addRotX(blend * -(float) (Math.cos(limbSwing * speed + 1.4) * 0.025 * globalHeight) * limbSwingAmount);
        stomach.addRotZ(blend * (float) (Math.cos(limbSwing * speed * 0.5 + 1.4) * 0.02 * globalHeight) * limbSwingAmount);
        stomach.addRotZ(blend * (float) (Math.cos(limbSwing * speed - 0.5) * 0.02 * globalHeight) * limbSwingAmount);
        neck.addRotY(blend * (float) (Math.cos(limbSwing * speed * 0.5 + 1.0) * -0.1f * globalHeight) * limbSwingAmount);
        neck.addRotX(blend * -(float) (Math.cos(limbSwing * speed) * 0.175 * globalHeight) * limbSwingAmount);
        head.addRotX(blend * (float) (Math.cos(limbSwing * speed + 0.175) * 0.175 * globalHeight + 0.1 * globalHeight) * limbSwingAmount);
        head.addRotZ(blend * -(float) (Math.cos(limbSwing * speed * 0.5 + 1.4) * 0.02 * globalHeight) * limbSwingAmount);
        head.addRotZ(blend * -(float) (Math.cos(limbSwing * speed - 0.5) * 0.02 * globalHeight) * limbSwingAmount);
        head.addRotZ(blend * 0.03f * limbSwingAmount * globalHeight);

        leftThigh.addRotX(blend * 0.05f * limbSwingAmount * globalHeight);
        leftThigh.addRotZ(blend * (float) (Math.cos(limbSwing * speed * 0.5 + 1.5) * 0.55f * globalDegree + 0.05 * globalDegree) * limbSwingAmount);
        leftThigh.addRotY(blend * (float) (Math.cos(limbSwing * speed * 0.5 + 1.5) * 0.1f * globalDegree - 0.15) * limbSwingAmount);
        leftShin.addRotX(blend * (float) (Math.cos(limbSwing * speed * 0.5 + 2.40) * -0.7f * globalDegree) * limbSwingAmount);
        leftAnkle.addRotX(blend * (float) (Math.cos(limbSwing * speed * 0.5 + 2.40) * 1.1f * globalDegree + 0.1f * globalDegree) * limbSwingAmount);
        leftFoot.addRotX(blend * (float) (Math.cos(limbSwing * speed * 0.5 + 2.5) * -1.3f * globalDegree - 0.6f * globalDegree) * limbSwingAmount);
        leftFoot.addRotX(blend * (float) (Math.cos(limbSwing * speed * 1 - 0.2) * -0.2f * globalDegree) * limbSwingAmount);
        leftFoot.addRotY(blend * -(float) (Math.cos(limbSwing * speed * 0.5 + 2.5) * -0.4f * globalDegree) * limbSwingAmount);
        leftFoot.addRotY(blend * -(float) (Math.cos(limbSwing * speed * 1 - 0.2) * -0.2f * globalDegree) * limbSwingAmount);
        leftToesBack.addRotX(blend * (float) (Math.cos(limbSwing * speed * 0.5 + 3.1) * 1.6f * globalDegree + 1.4f * globalDegree) * limbSwingAmount);
        leftToesBack.addRotX(blend * (float) (Math.cos(limbSwing * speed * 1 + 0.1) * 0.3f * globalDegree) * limbSwingAmount);

        rightThigh.addRotX(blend * -0.05f * limbSwingAmount * globalHeight);
        rightThigh.addRotZ(blend * -(float) (Math.cos(limbSwing * speed * 0.5 + 1.5) * 0.55f * globalDegree - 0.05 * globalDegree) * limbSwingAmount);
        rightThigh.addRotY(blend * (float) (Math.cos(limbSwing * speed * 0.5 + 1.5) * 0.1f * globalDegree + 0.15) * limbSwingAmount);
        rightShin.addRotX(blend * -(float) (Math.cos(limbSwing * speed * 0.5 + 2.40) * -0.7f * globalDegree) * limbSwingAmount);
        rightAnkle.addRotX(blend * -(float) (Math.cos(limbSwing * speed * 0.5 + 2.40) * 1.1f * globalDegree - 0.1f * globalDegree) * limbSwingAmount);
        rightFoot.addRotX(blend * -(float) (Math.cos(limbSwing * speed * 0.5 + 2.5) * -1.3f * globalDegree + 0.6f * globalDegree) * limbSwingAmount);
        rightFoot.addRotX(blend * (float) (Math.cos(limbSwing * speed * 1 - 0.2) * -0.2f * globalDegree) * limbSwingAmount);
        rightFoot.addRotY(blend * (float) (Math.cos(limbSwing * speed * 0.5 + 2.5) * -0.4f * globalDegree) * limbSwingAmount);
        rightFoot.addRotY(blend * -(float) (Math.cos(limbSwing * speed * 1 - 0.2) * -0.2f * globalDegree) * limbSwingAmount);
        rightToesBack.addRotX(blend * -(float) (Math.cos(limbSwing * speed * 0.5 + 3.1) * 1.6f * globalDegree - 1.4f * globalDegree) * limbSwingAmount);
        rightToesBack.addRotX(blend * (float) (Math.cos(limbSwing * speed * 1 + 0.1) * 0.3f * globalDegree) * limbSwingAmount);

        rightShin.addRotX(blend * -(float) (Math.pow(Math.cos(limbSwing * 0.25 * speed - 0.6 + Math.PI / 2.0), 12) * 0.6f * globalHeight) * limbSwingAmount);
        rightAnkle.addRotX(blend * (float) (Math.pow(Math.cos(limbSwing * 0.25 * speed - 0.6 + Math.PI / 2.0), 12) * 0.6f * globalHeight) * limbSwingAmount);

        leftArm.addRotY(blend * (float) -(Math.cos(limbSwing * speed + 0.52) * 0.0707 * globalHeight) * limbSwingAmount);
        leftArm.addRotZ(blend * (float) (Math.cos(limbSwing * speed + 0.52) * 0.0707 * globalHeight) * limbSwingAmount);
        leftForeArm.addRotX(blend * (float) (Math.cos(limbSwing * speed - 1.0) * 0.03 * globalHeight) * limbSwingAmount);

        rightArm.addRotY(blend * (float) (Math.cos(limbSwing * speed + 0.52) * 0.0707 * globalHeight) * limbSwingAmount);
        rightArm.addRotZ(blend * (float) -(Math.cos(limbSwing * speed + 0.52) * 0.0707 * globalHeight) * limbSwingAmount);
        rightForeArm.addRotX(blend * (float) (Math.cos(limbSwing * speed - 1.0) * 0.03 * globalHeight) * limbSwingAmount);
    }

}