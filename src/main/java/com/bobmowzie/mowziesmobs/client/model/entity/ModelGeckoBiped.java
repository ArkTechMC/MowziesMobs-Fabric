package com.bobmowzie.mowziesmobs.client.model.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieGeoBone;
import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieGeoModel;
import com.bobmowzie.mowziesmobs.client.render.entity.player.GeckoPlayer;
import com.bobmowzie.mowziesmobs.server.capability.AbilityCapability;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import software.bernie.geckolib.cache.object.GeoBone;

@Environment(EnvType.CLIENT)
public class ModelGeckoBiped extends MowzieGeoModel<GeckoPlayer> {
    public boolean isSitting = false;
    public boolean isChild = true;
    public float swingProgress;
    public boolean isSneak;
    public float swimAnimation;
    public BipedEntityModel.ArmPose leftArmPose = BipedEntityModel.ArmPose.EMPTY;
    public BipedEntityModel.ArmPose rightArmPose = BipedEntityModel.ArmPose.EMPTY;
    protected boolean useSmallArms = false;
    private Identifier textureLocation;

    public static void breathAnim(MowzieGeoBone rightArm, MowzieGeoBone leftArm, float ageInTicks, float armBreathAmount) {
        rightArm.addRotZ(armBreathAmount * MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F);
        leftArm.addRotZ(armBreathAmount * -MathHelper.cos(ageInTicks * 0.09F) * 0.05F - 0.05F);
        rightArm.addRotX(armBreathAmount * MathHelper.sin(ageInTicks * 0.067F) * 0.05F);
        leftArm.addRotX(armBreathAmount * -MathHelper.sin(ageInTicks * 0.067F) * 0.05F);
    }

    @Override
    public Identifier getAnimationResource(GeckoPlayer animatable) {
        return new Identifier(MowziesMobs.MODID, "animations/animated_player.animation.json");
    }

    @Override
    public Identifier getModelResource(GeckoPlayer animatable) {
        return new Identifier(MowziesMobs.MODID, "geo/animated_player.geo.json");
    }

    @Override
    public Identifier getTextureResource(GeckoPlayer animatable) {
        return this.textureLocation;
    }

    public void setTextureFromPlayer(AbstractClientPlayerEntity player) {
        this.textureLocation = player.getSkinTexture();
    }

    public void setUseSmallArms(boolean useSmallArms) {
        this.useSmallArms = useSmallArms;
    }

    public boolean isUsingSmallArms() {
        return this.useSmallArms;
    }

    public MowzieGeoBone bipedHead() {
        return this.getMowzieBone("Head");
    }

    public MowzieGeoBone bipedHeadwear() {
        return this.getMowzieBone("HatLayer");
    }

    public MowzieGeoBone bipedBody() {
        return this.getMowzieBone("Body");
    }

    public MowzieGeoBone bipedRightArm() {
        return this.getMowzieBone("RightArm");
    }

    public MowzieGeoBone bipedLeftArm() {
        return this.getMowzieBone("LeftArm");
    }

    public MowzieGeoBone bipedRightLeg() {
        return this.getMowzieBone("RightLeg");
    }

    public MowzieGeoBone bipedLeftLeg() {
        return this.getMowzieBone("LeftLeg");
    }

    public void setVisible(boolean visible) {
        this.bipedHead().setHidden(!visible);
        this.bipedHeadwear().setHidden(!visible);
        this.bipedBody().setHidden(!visible);
        this.bipedRightArm().setHidden(!visible);
        this.bipedLeftArm().setHidden(!visible);
        this.bipedRightLeg().setHidden(!visible);
        this.bipedLeftLeg().setHidden(!visible);
    }

    public void setRotationAngles() {
        MowzieGeoBone head = this.getMowzieBone("Head");
        MowzieGeoBone neck = this.getMowzieBone("Neck");
        float yaw = 0;
        float pitch = 0;
        float roll = 0;
        GeoBone parent = neck.getParent();
        while (parent != null) {
            pitch += parent.getRotX();
            yaw += parent.getRotY();
            roll += parent.getRotZ();
            parent = parent.getParent();
        }
        neck.addRot(-yaw, -pitch, -roll);
    }

    public void setRotationAngles(PlayerEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float partialTick) {
        if (!this.isInitialized()) return;
        if (MinecraftClient.getInstance().isPaused()) return;

        MowzieGeoBone rightArmClassic = this.getMowzieBone("RightArmClassic");
        MowzieGeoBone leftArmClassic = this.getMowzieBone("LeftArmClassic");
        MowzieGeoBone rightArmSlim = this.getMowzieBone("RightArmSlim");
        MowzieGeoBone leftArmSlim = this.getMowzieBone("LeftArmSlim");
        if (this.useSmallArms) {
            rightArmClassic.setHidden(true);
            leftArmClassic.setHidden(true);
            rightArmSlim.setHidden(false);
            leftArmSlim.setHidden(false);
        } else {
            rightArmSlim.setHidden(true);
            leftArmSlim.setHidden(true);
            rightArmClassic.setHidden(false);
            leftArmClassic.setHidden(false);
        }

        this.swimAnimation = entityIn.getLeaningPitch(partialTick);

        float headLookAmount = this.getControllerValueInverted("HeadLookController");
        float armLookAmount = 1f - this.getControllerValueInverted("ArmPitchController");
        float armLookAmountRight = this.getBone("ArmPitchController").get().getPosY();
        float armLookAmountLeft = this.getBone("ArmPitchController").get().getPosZ();
        boolean flag = entityIn.getRoll() > 4;
        boolean flag1 = entityIn.isInSwimmingPose();
        this.bipedHead().addRotY(headLookAmount * -netHeadYaw * ((float) Math.PI / 180F));
        this.getMowzieBone("LeftClavicle").addRotY(Math.min(armLookAmount + armLookAmountLeft, 1) * -netHeadYaw * ((float) Math.PI / 180F));
        this.getMowzieBone("RightClavicle").addRotY(Math.min(armLookAmount + armLookAmountRight, 1) * -netHeadYaw * ((float) Math.PI / 180F));
        if (flag) {
            this.bipedHead().addRotX((-(float) Math.PI / 4F));
        } else if (this.swimAnimation > 0.0F) {
            if (flag1) {
                this.bipedHead().addRotX(headLookAmount * this.rotLerpRad(this.swimAnimation, this.bipedHead().getRotX(), (-(float) Math.PI / 4F)));
            } else {
                this.bipedHead().addRotX(headLookAmount * this.rotLerpRad(this.swimAnimation, this.bipedHead().getRotX(), headPitch * ((float) Math.PI / 180F)));
            }
        } else {
            this.bipedHead().addRotX(headLookAmount * -headPitch * ((float) Math.PI / 180F));
            this.getMowzieBone("LeftClavicle").addRotX(Math.min(armLookAmount + armLookAmountLeft, 1) * -headPitch * ((float) Math.PI / 180F));
            this.getMowzieBone("RightClavicle").addRotX(Math.min(armLookAmount + armLookAmountRight, 1) * -headPitch * ((float) Math.PI / 180F));
        }

        float f = 1.0F;
        if (flag) {
            f = (float) entityIn.getVelocity().lengthSquared();
            f = f / 0.2F;
            f = f * f * f;
        }

        if (f < 1.0F) {
            f = 1.0F;
        }

        float legWalkAmount = this.getControllerValueInverted("LegWalkController");
        float armSwingAmount = this.getControllerValueInverted("ArmSwingController");
        float armSwingAmountRight = 1.0f - this.getBone("ArmSwingController").get().getPosY();
        float armSwingAmountLeft = 1.0f - this.getBone("ArmSwingController").get().getPosZ();
        this.bipedRightArm().addRotX(armSwingAmount * armSwingAmountRight * MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 2.0F * limbSwingAmount * 0.5F / f);
        this.bipedLeftArm().addRotX(armSwingAmount * armSwingAmountLeft * MathHelper.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F / f);
        this.bipedRightLeg().addRotX(legWalkAmount * MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount / f);
        this.bipedLeftLeg().addRotX(legWalkAmount * MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount / f);

        if (this.isSitting) {
            this.bipedRightArm().setRotX(this.bipedRightArm().getRotX() + (-(float) Math.PI / 5F));
            this.bipedLeftArm().setRotX(this.bipedRightArm().getRotX() + (-(float) Math.PI / 5F));
            this.bipedRightLeg().setRotX(-1.4137167F);
            this.bipedRightLeg().setRotY(((float) Math.PI / 10F));
            this.bipedRightLeg().setRotZ(0.07853982F);
            this.bipedLeftLeg().setRotX(-1.4137167F);
            this.bipedLeftLeg().setRotY((-(float) Math.PI / 10F));
            this.bipedLeftLeg().setRotZ(-0.07853982F);
            this.getMowzieBone("Waist").setRot(0, 0, 0);
            this.getMowzieBone("Root").setRot(0, 0, 0);
        }

        boolean flag2 = entityIn.getMainArm() == Arm.RIGHT;
        boolean flag3 = flag2 ? this.leftArmPose.isTwoHanded() : this.rightArmPose.isTwoHanded();
        if (flag2 != flag3) {
            this.poseLeftArm(entityIn);
            this.poseRightArm(entityIn);
        } else {
            this.poseRightArm(entityIn);
            this.poseLeftArm(entityIn);
        }

//		this.swingAnim(entityIn, ageInTicks);

        float sneakController = this.getControllerValueInverted("CrouchController");
        if (this.isSneak) {
            this.bipedBody().addRotX(-0.5F * sneakController);
            this.getMowzieBone("Neck").addRotX(0.5F * sneakController);
            this.bipedRightArm().addRot(0.4F * sneakController, 0, 0);
            this.bipedLeftArm().addRot(0.4F * sneakController, 0, 0);
            this.bipedHead().addPosY(-1F * sneakController);
            this.bipedBody().addPos(0, -1.5F * sneakController, 1.7f * sneakController);
            this.getMowzieBone("Waist").addPos(0, -0.2f * sneakController, 4F * sneakController);
            this.bipedLeftArm().addRotX(-0.4f * sneakController);
            this.bipedLeftArm().addPos(0, 0.2f * sneakController, -1f * sneakController);
            this.bipedRightArm().addRotX(-0.4f * sneakController);
            this.bipedRightArm().addPos(0, 0.2f * sneakController, -1f * sneakController);

            this.getMowzieBone("Waist").addPosY(2f * (1f - sneakController));
        }

        float armBreathAmount = this.getControllerValueInverted("ArmBreathController");
        breathAnim(this.bipedRightArm(), this.bipedLeftArm(), ageInTicks, armBreathAmount);

//		if (this.swimAnimation > 0.0F) {
//			float f1 = limbSwing % 26.0F;
//			HandSide handside = this.getMainHand(entityIn);
//			float f2 = handside == HandSide.RIGHT && this.swingProgress > 0.0F ? 0.0F : this.swimAnimation;
//			float f3 = handside == HandSide.LEFT && this.swingProgress > 0.0F ? 0.0F : this.swimAnimation;
//			if (f1 < 14.0F) {
//				this.bipedLeftArm().setRotX(this.rotLerpRad(f3, this.bipedLeftArm().getRotX(), 0.0F));
//				this.bipedRightArm().setRotX(MathHelper.lerp(f2, this.bipedRightArm().getRotX(), 0.0F));
//				this.bipedLeftArm().setRotY(this.rotLerpRad(f3, this.bipedLeftArm().getRotY(), (float)Math.PI));
//				this.bipedRightArm().setRotY(MathHelper.lerp(f2, this.bipedRightArm().getRotY(), (float)Math.PI));
//				this.bipedLeftArm().setRotZ(this.rotLerpRad(f3, this.bipedLeftArm().getRotZ(), (float)Math.PI + 1.8707964F * this.getArmAngleSq(f1) / this.getArmAngleSq(14.0F)));
//				this.bipedRightArm().setRotZ(MathHelper.lerp(f2, this.bipedRightArm().getRotZ(), (float)Math.PI - 1.8707964F * this.getArmAngleSq(f1) / this.getArmAngleSq(14.0F)));
//			} else if (f1 >= 14.0F && f1 < 22.0F) {
//				float f6 = (f1 - 14.0F) / 8.0F;
//				this.bipedLeftArm().setRotX(this.rotLerpRad(f3, this.bipedLeftArm().getRotX(), ((float)Math.PI / 2F) * f6));
//				this.bipedRightArm().setRotX(MathHelper.lerp(f2, this.bipedRightArm().getRotX(), ((float)Math.PI / 2F) * f6));
//				this.bipedLeftArm().setRotY(this.rotLerpRad(f3, this.bipedLeftArm().getRotY(), (float)Math.PI));
//				this.bipedRightArm().setRotY(MathHelper.lerp(f2, this.bipedRightArm().getRotY(), (float)Math.PI));
//				this.bipedLeftArm().setRotZ(this.rotLerpRad(f3, this.bipedLeftArm().getRotZ(), 5.012389F - 1.8707964F * f6));
//				this.bipedRightArm().setRotZ(MathHelper.lerp(f2, this.bipedRightArm().getRotZ(), 1.2707963F + 1.8707964F * f6));
//			} else if (f1 >= 22.0F && f1 < 26.0F) {
//				float f4 = (f1 - 22.0F) / 4.0F;
//				this.bipedLeftArm().setRotX(this.rotLerpRad(f3, this.bipedLeftArm().getRotX(), ((float)Math.PI / 2F) - ((float)Math.PI / 2F) * f4));
//				this.bipedRightArm().setRotX(MathHelper.lerp(f2, this.bipedRightArm().getRotX(), ((float)Math.PI / 2F) - ((float)Math.PI / 2F) * f4));
//				this.bipedLeftArm().setRotY(this.rotLerpRad(f3, this.bipedLeftArm().getRotY(), (float)Math.PI));
//				this.bipedRightArm().setRotY(MathHelper.lerp(f2, this.bipedRightArm().getRotY(), (float)Math.PI));
//				this.bipedLeftArm().setRotZ(this.rotLerpRad(f3, this.bipedLeftArm().getRotZ(), (float)Math.PI));
//				this.bipedRightArm().setRotZ(MathHelper.lerp(f2, this.bipedRightArm().getRotZ(), (float)Math.PI));
//			}
//
//			float f7 = 0.3F;
//			float f5 = 0.33333334F;
//			this.bipedLeftLeg().setRotX(MathHelper.lerp(this.swimAnimation, this.bipedLeftLeg().getRotX(), 0.3F * MathHelper.cos(limbSwing * 0.33333334F + (float)Math.PI)));
//			this.bipedRightLeg().setRotX(MathHelper.lerp(this.swimAnimation, this.bipedRightLeg().getRotX(), 0.3F * MathHelper.cos(limbSwing * 0.33333334F)));
//		}

        AbilityCapability.IAbilityCapability abilityCapability = AbilityCapability.get(entityIn);
        if (abilityCapability != null && abilityCapability.getActiveAbility() != null) {
            abilityCapability.codeAnimations(this, partialTick);
        }
    }

    protected MowzieGeoBone getArmForSide(Arm side) {
        return side == Arm.LEFT ? this.bipedLeftArm() : this.bipedRightArm();
    }

    protected float rotLerpRad(float angleIn, float maxAngleIn, float mulIn) {
        float f = (mulIn - maxAngleIn) % ((float) Math.PI * 2F);
        if (f < -(float) Math.PI) {
            f += ((float) Math.PI * 2F);
        }

        if (f >= (float) Math.PI) {
            f -= ((float) Math.PI * 2F);
        }

        return maxAngleIn + angleIn * f;
    }

    private float getArmAngleSq(float limbSwing) {
        return -65.0F * limbSwing + limbSwing * limbSwing;
    }

    protected Arm getMainHand(PlayerEntity entityIn) {
        Arm handside = entityIn.getMainArm();
        return entityIn.preferredHand == Hand.MAIN_HAND ? handside : handside.getOpposite();
    }

    private void poseRightArm(PlayerEntity p_241654_1_) {
        float armSwingAmount = this.getControllerValueInverted("ArmSwingController");
        switch (this.rightArmPose) {
            case EMPTY:
                break;
            case BLOCK:
                this.bipedRightArm().addRotX(0.9424779F * armSwingAmount);
                break;
            case ITEM:
                this.bipedRightArm().addRotX(((float) Math.PI / 10F) * armSwingAmount);
                break;
        }

    }

    private void poseLeftArm(PlayerEntity p_241655_1_) {
        float armSwingAmount = this.getControllerValueInverted("ArmSwingController");
        switch (this.leftArmPose) {
            case EMPTY:
                break;
            case BLOCK:
                this.bipedLeftArm().addRotX(0.9424779F * armSwingAmount);
                break;
            case ITEM:
                this.bipedLeftArm().addRotX(((float) Math.PI / 10F) * armSwingAmount);
                break;
        }
    }
}