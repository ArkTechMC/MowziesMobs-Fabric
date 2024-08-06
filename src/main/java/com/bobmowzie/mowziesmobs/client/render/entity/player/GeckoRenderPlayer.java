package com.bobmowzie.mowziesmobs.client.render.entity.player;

import com.bobmowzie.mowziesmobs.client.model.entity.ModelBipedAnimated;
import com.bobmowzie.mowziesmobs.client.model.entity.ModelGeckoPlayerThirdPerson;
import com.bobmowzie.mowziesmobs.client.model.entity.ModelPlayerAnimated;
import com.bobmowzie.mowziesmobs.client.model.tools.MathUtils;
import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieGeoBone;
import com.bobmowzie.mowziesmobs.client.render.entity.FrozenRenderHandler;
import com.bobmowzie.mowziesmobs.client.render.entity.layer.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.PlayerModelPart;
import net.minecraft.client.render.entity.feature.*;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.cache.texture.AnimatableTexture;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.util.RenderUtils;

import java.util.HashMap;

@Environment(EnvType.CLIENT)
public class GeckoRenderPlayer extends PlayerEntityRenderer implements GeoRenderer<GeckoPlayer> {

    private static final HashMap<Class<? extends GeckoPlayer>, GeckoRenderPlayer> modelsToLoad = new HashMap<>();
    public VertexConsumerProvider rtb;
    public Vec3d betweenHandsPos;
    public Vec3d particleEmitterRoot;
    private final ModelGeckoPlayerThirdPerson geoModel;
    private final Matrix4f worldRenderMat;
    private boolean isInvisible = false;

    private GeckoPlayer animatable;

    public GeckoRenderPlayer(EntityRendererFactory.Context context, boolean slim, ModelGeckoPlayerThirdPerson geoModel) {
        super(context, slim);

        ModelPlayerAnimated<AbstractClientPlayerEntity> modelPlayerAnimated = new ModelPlayerAnimated<>(context.getPart(slim ? EntityModelLayers.PLAYER_SLIM : EntityModelLayers.PLAYER), slim);
        ModelPlayerAnimated.setUseMatrixMode(modelPlayerAnimated, true);
        this.model = modelPlayerAnimated;

        this.features.clear();
        this.addFeature(new GeckoArmorLayer<>(this, new ModelBipedAnimated<>(context.getPart(slim ? EntityModelLayers.PLAYER_SLIM_INNER_ARMOR : EntityModelLayers.PLAYER_INNER_ARMOR)), new ModelBipedAnimated<>(context.getPart(slim ? EntityModelLayers.PLAYER_SLIM_OUTER_ARMOR : EntityModelLayers.PLAYER_OUTER_ARMOR)), context.getModelManager()));
        this.addFeature(new GeckoPlayerItemInHandLayer(this));
        this.addFeature(new StuckArrowsFeatureRenderer<>(context, this));
        this.addFeature(new Deadmau5FeatureRenderer(this));
        this.addFeature(new GeckoCapeLayer(this));
        this.addFeature(new HeadFeatureRenderer<>(this, context.getModelLoader(), context.getHeldItemRenderer()));
        this.addFeature(new GeckoElytraLayer(this, context.getModelLoader()));
        this.addFeature(new GeckoParrotOnShoulderLayer(this, context.getModelLoader()));
        this.addFeature(new TridentRiptideFeatureRenderer<>(this, context.getModelLoader()));
        this.addFeature(new StuckStingersFeatureRenderer<>(this));
        this.addFeature(new FrozenRenderHandler.LayerFrozen<>(this));
        this.addFeature(new SolarFlareLayer(this));
        this.addFeature(new SunblockLayer<>(this));


        this.geoModel = geoModel;
        this.geoModel.setUseSmallArms(slim);

        this.worldRenderMat = new Matrix4f();
        this.worldRenderMat.identity();
    }

    private static float getFacingAngle(Direction facingIn) {
        switch (facingIn) {
            case SOUTH:
                return 90.0F;
            case WEST:
                return 0.0F;
            case NORTH:
                return 270.0F;
            case EAST:
                return 180.0F;
            default:
                return 0.0F;
        }
    }

    public ModelGeckoPlayerThirdPerson getGeckoModel() {
        return this.geoModel;
    }

    public HashMap<Class<? extends GeckoPlayer>, GeckoRenderPlayer> getModelsToLoad() {
        return modelsToLoad;
    }

    public void render(AbstractClientPlayerEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, VertexConsumerProvider bufferIn, int packedLightIn, GeckoPlayer geckoPlayer) {
        this.rtb = bufferIn;
        this.setModelVisibilities(entityIn);
        this.renderLiving(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn, geckoPlayer);
    }

    private void setModelVisibilities(AbstractClientPlayerEntity clientPlayer) {
        ModelGeckoPlayerThirdPerson playermodel = this.getGeckoModel();
        if (playermodel.isInitialized()) {
            if (clientPlayer.isSpectator()) {
                playermodel.setVisible(false);
                playermodel.bipedHead().setHidden(false);
                playermodel.bipedHeadwear().setHidden(false);
            } else {
                playermodel.setVisible(true);
                playermodel.bipedHeadwear().setHidden(!clientPlayer.isPartVisible(PlayerModelPart.HAT));
                playermodel.bipedBodywear().setHidden(!clientPlayer.isPartVisible(PlayerModelPart.JACKET));
                playermodel.bipedLeftLegwear().setHidden(!clientPlayer.isPartVisible(PlayerModelPart.LEFT_PANTS_LEG));
                playermodel.bipedRightLegwear().setHidden(!clientPlayer.isPartVisible(PlayerModelPart.RIGHT_PANTS_LEG));
                playermodel.bipedLeftArmwear().setHidden(!clientPlayer.isPartVisible(PlayerModelPart.LEFT_SLEEVE));
                playermodel.bipedRightArmwear().setHidden(!clientPlayer.isPartVisible(PlayerModelPart.RIGHT_SLEEVE));
                playermodel.isSneak = clientPlayer.isInSneakingPose();
                BipedEntityModel.ArmPose bipedmodel$armpose = getArmPose(clientPlayer, Hand.MAIN_HAND);
                BipedEntityModel.ArmPose bipedmodel$armpose1 = getArmPose(clientPlayer, Hand.OFF_HAND);
                if (bipedmodel$armpose.isTwoHanded()) {
                    bipedmodel$armpose1 = clientPlayer.getOffHandStack().isEmpty() ? BipedEntityModel.ArmPose.EMPTY : BipedEntityModel.ArmPose.ITEM;
                }

                if (clientPlayer.getMainArm() == Arm.RIGHT) {
                    this.geoModel.rightArmPose = bipedmodel$armpose;
                    this.geoModel.leftArmPose = bipedmodel$armpose1;
                } else {
                    this.geoModel.rightArmPose = bipedmodel$armpose1;
                    this.geoModel.leftArmPose = bipedmodel$armpose;
                }
            }
        }
    }

    public void renderLiving(AbstractClientPlayerEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, VertexConsumerProvider bufferIn, int packedLightIn, GeckoPlayer geckoPlayer) {
        this.animatable = geckoPlayer;

        matrixStackIn.push();

        MinecraftClient minecraft = MinecraftClient.getInstance();
        boolean flag = this.isVisible(entityIn);
        boolean flag1 = !flag && !entityIn.isInvisibleTo(minecraft.player);
        boolean flag2 = minecraft.hasOutline(entityIn);
        this.isInvisible = !flag && !flag1 && !flag2;
        RenderLayer rendertype = this.getRenderLayer(entityIn, flag, flag1, flag2);
        if (this.isInvisible) {
            rendertype = this.model.getLayer(this.getTextureLocation(geckoPlayer));
        }
        if (rendertype != null) {
            VertexConsumer ivertexbuilder = bufferIn.getBuffer(rendertype);
            this.defaultRender(matrixStackIn, this.animatable, bufferIn, rendertype, ivertexbuilder, 0, partialTicks, packedLightIn);
        }

        matrixStackIn.pop();
        this.renderEntity(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    @Override
    public int getPackedOverlay(GeckoPlayer animatable, float u, float partialTick) {
        AbstractClientPlayerEntity player = (AbstractClientPlayerEntity) animatable.getPlayer();
        return getOverlay(player, this.getAnimationCounter(player, partialTick));
    }

    @Override
    public void actuallyRender(MatrixStack poseStack, GeckoPlayer animatable, BakedGeoModel model, RenderLayer renderType, VertexConsumerProvider bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        AbstractClientPlayerEntity entity = (AbstractClientPlayerEntity) animatable.getPlayer();
        this.model.handSwingProgress = this.getHandSwingProgress(entity, partialTick);

        boolean shouldSit = entity.hasVehicle() && (entity.getVehicle() != null);
        this.model.riding = shouldSit;
        this.model.child = entity.isBaby();
        float f_lerpBodyRot = MathHelper.lerpAngleDegrees(partialTick, entity.prevBodyYaw, entity.bodyYaw);
        float f1_lerpHeadRot = MathHelper.lerpAngleDegrees(partialTick, entity.prevHeadYaw, entity.headYaw);
        float f2_netHeadYaw = f1_lerpHeadRot - f_lerpBodyRot;
        if (shouldSit && entity.getVehicle() instanceof LivingEntity livingentity) {
            f_lerpBodyRot = MathHelper.lerpAngleDegrees(partialTick, livingentity.prevBodyYaw, livingentity.bodyYaw);
            f2_netHeadYaw = f1_lerpHeadRot - f_lerpBodyRot;
            float f3 = MathHelper.wrapDegrees(f2_netHeadYaw);
            if (f3 < -85.0F) {
                f3 = -85.0F;
            }

            if (f3 >= 85.0F) {
                f3 = 85.0F;
            }

            f_lerpBodyRot = f1_lerpHeadRot - f3;
            if (f3 * f3 > 2500.0F) {
                f_lerpBodyRot += f3 * 0.2F;
            }

            f2_netHeadYaw = f1_lerpHeadRot - f_lerpBodyRot;
        }

        float f6 = MathHelper.lerp(partialTick, entity.prevPitch, entity.getPitch());
        if (shouldFlipUpsideDown(entity)) {
            f6 *= -1.0F;
            f2_netHeadYaw *= -1.0F;
        }

        if (entity.isInPose(EntityPose.SLEEPING)) {
            Direction direction = entity.getSleepingDirection();
            if (direction != null) {
                float f4 = entity.getEyeHeight(EntityPose.STANDING) - 0.1F;
                poseStack.translate((float) (-direction.getOffsetX()) * f4, 0.0F, (float) (-direction.getOffsetZ()) * f4);
            }
        }

        float f7 = this.getAnimationProgress(entity, partialTick);
//        this.setupRotations(entity, poseStack, f7, f_lerpBodyRot, partialTick);   This is where the vanilla function gets called. We move it lower down after animations are handled
        this.scale(entity, poseStack, partialTick);
        float f8_limbSwingAmount = 0.0F;
        float f5_limbSwing = 0.0F;
        if (!shouldSit && entity.isAlive()) {
            f8_limbSwingAmount = entity.limbAnimator.getSpeed(partialTick);
            f5_limbSwing = entity.limbAnimator.getPos(partialTick);
            if (entity.isBaby()) {
                f5_limbSwing *= 3.0F;
            }

            if (f8_limbSwingAmount > 1.0F) {
                f8_limbSwingAmount = 1.0F;
            }
        }

        if (!isReRender) {
            float headPitch = MathHelper.lerp(partialTick, entity.prevPitch, entity.getPitch());
            float motionThreshold = this.getMotionAnimThreshold(animatable);
            Vec3d velocity = entity.getVelocity();
            float avgVelocity = (float) (Math.abs(velocity.x) + Math.abs(velocity.z)) / 2f;
            AnimationState<GeckoPlayer> animationState = new AnimationState<GeckoPlayer>(animatable, f5_limbSwing, f8_limbSwingAmount, partialTick, avgVelocity >= motionThreshold && f8_limbSwingAmount != 0);
            long instanceId = this.getInstanceId(animatable);

            AnimatableManager<GeckoPlayer> animatableManager = animatable.getAnimatableInstanceCache().getManagerForId(instanceId);
            animationState.setData(DataTickets.TICK, animatable.getTick(animatable) + animatableManager.getFirstTickTime() + MinecraftClient.getInstance().getTickDelta());
            animationState.setData(DataTickets.ENTITY, entity);
            animationState.setData(DataTickets.ENTITY_MODEL_DATA, new EntityModelData(shouldSit, entity.isBaby(), -f2_netHeadYaw, -headPitch));
            this.getGeckoModel().addAdditionalStateData(animatable, instanceId, animationState::setData);
            this.getGeckoModel().handleAnimations(animatable, instanceId, animationState);
        }

        if (this.geoModel.isInitialized()) {
            this.setupRotations(entity, poseStack, f7, f_lerpBodyRot, partialTick, f1_lerpHeadRot);
            float bodyRotateAmount = this.geoModel.getControllerValueInverted("BodyRotateController");
            this.geoModel.setRotationAngles(entity, f5_limbSwing, f8_limbSwingAmount, f7, MathHelper.lerpAngleDegrees(bodyRotateAmount, 0, f2_netHeadYaw), f6, partialTick);

            MowzieGeoBone leftHeldItem = this.geoModel.getMowzieBone("LeftHeldItem");
            MowzieGeoBone rightHeldItem = this.geoModel.getMowzieBone("RightHeldItem");

            Matrix4f worldMatInverted = new Matrix4f(poseStack.peek().getPositionMatrix());
            worldMatInverted.invert();
            Matrix3f worldNormInverted = new Matrix3f(poseStack.peek().getNormalMatrix());
            worldNormInverted.invert();
            MatrixStack toWorldSpace = new MatrixStack();
            toWorldSpace.multiply(MathUtils.quatFromRotationXYZ(0, -f_lerpBodyRot + 180, 0, true));
            toWorldSpace.translate(0, -1.5f, 0);
            toWorldSpace.peek().getNormalMatrix().mul(worldNormInverted);
            toWorldSpace.peek().getPositionMatrix().mul(worldMatInverted);

            Vector4f leftHeldItemPos = new Vector4f(0, 0, 0, 1);
            leftHeldItemPos.mul(leftHeldItem.getWorldSpaceMatrix());
            leftHeldItemPos.mul(toWorldSpace.peek().getPositionMatrix());
            Vec3d leftHeldItemPos3 = new Vec3d(leftHeldItemPos.x(), leftHeldItemPos.y(), leftHeldItemPos.z());

            Vector4f rightHeldItemPos = new Vector4f(0, 0, 0, 1);
            rightHeldItemPos.mul(rightHeldItem.getWorldSpaceMatrix());
            rightHeldItemPos.mul(toWorldSpace.peek().getPositionMatrix());
            Vec3d rightHeldItemPos3 = new Vec3d(rightHeldItemPos.x(), rightHeldItemPos.y(), rightHeldItemPos.z());

            this.betweenHandsPos = rightHeldItemPos3.add(leftHeldItemPos3.subtract(rightHeldItemPos3).multiply(0.5));

            MowzieGeoBone particleEmitterRootBone = this.geoModel.getMowzieBone("ParticleEmitterRoot");
            Vector4f emitterRootPos = new Vector4f(0, 0, 0, 1);
            emitterRootPos.mul(particleEmitterRootBone.getWorldSpaceMatrix());
            emitterRootPos.mul(toWorldSpace.peek().getPositionMatrix());
            this.particleEmitterRoot = new Vec3d(emitterRootPos.x(), emitterRootPos.y(), emitterRootPos.z());
        }

        poseStack.translate(0, 0.01f, 0);

//        this.modelRenderTranslations = new Matrix4f(poseStack.last().pose());

        if (!entity.isInvisibleTo(MinecraftClient.getInstance().player))
            GeoRenderer.super.actuallyRender(poseStack, animatable, model, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);

        ModelPlayerAnimated.copyFromGeckoModel(this.model, this.geoModel);

        if (!entity.isSpectator()) {
            for (FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> layerrenderer : this.features) {
                layerrenderer.render(poseStack, bufferSource, packedLight, entity, f5_limbSwing, f8_limbSwingAmount, partialTick, f7, f2_netHeadYaw, f6);
            }
        }
    }

    public void renderEntity(AbstractClientPlayerEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, VertexConsumerProvider bufferIn, int packedLightIn) {
        if (this.hasLabel(entityIn))
            this.renderLabelIfPresent(entityIn, entityIn.getCustomName(), matrixStackIn, bufferIn, packedLightIn);
    }

    protected void setupRotations(AbstractClientPlayerEntity entityLiving, MatrixStack matrixStackIn, float ageInTicks, float rotationYaw, float partialTicks, float headYaw) {
        float f = entityLiving.getLeaningPitch(partialTicks);
        if (entityLiving.isFallFlying()) {
            this.applyRotationsLivingRenderer(entityLiving, matrixStackIn, ageInTicks, rotationYaw, partialTicks, headYaw);
            float f1 = (float) entityLiving.getRoll() + partialTicks;
            float f2 = MathHelper.clamp(f1 * f1 / 100.0F, 0.0F, 1.0F);
            if (!entityLiving.isUsingRiptide()) {
                matrixStackIn.multiply(RotationAxis.POSITIVE_X.rotationDegrees(f2 * (-90.0F - entityLiving.getPitch())));
            }

            Vec3d vector3d = entityLiving.getRotationVec(partialTicks);
            Vec3d vector3d1 = entityLiving.getVelocity();
            double d0 = vector3d1.horizontalLengthSquared();
            double d1 = vector3d.horizontalLengthSquared();
            if (d0 > 0.0D && d1 > 0.0D) {
                double d2 = (vector3d1.x * vector3d.x + vector3d1.z * vector3d.z) / Math.sqrt(d0 * d1);
                double d3 = vector3d1.x * vector3d.z - vector3d1.z * vector3d.x;
                matrixStackIn.multiply(RotationAxis.POSITIVE_Y.rotation((float) (Math.signum(d3) * Math.acos(d2))));
            }
        } else if (f > 0.0F) {
            float swimController = this.geoModel.getControllerValueInverted("SwimController");
            this.applyRotationsLivingRenderer(entityLiving, matrixStackIn, ageInTicks, rotationYaw, partialTicks, headYaw);
            float f3 = entityLiving.isTouchingWater() ? -90.0F - entityLiving.getPitch() : -90.0F;// || entityLiving.isInFluidType((fluidType, height) -> entityLiving.canSwimInFluidType(fluidType)) ? -90.0F - entityLiving.getPitch() : -90.0F;
            float f4 = MathHelper.lerp(f, 0.0F, f3) * swimController;
            matrixStackIn.multiply(RotationAxis.POSITIVE_X.rotationDegrees(f4));
            if (entityLiving.isInSwimmingPose()) {
                matrixStackIn.translate(0.0D, -1.0D, 0.3F);
            }
        } else {
            this.applyRotationsLivingRenderer(entityLiving, matrixStackIn, ageInTicks, rotationYaw, partialTicks, headYaw);
        }
    }

    protected void applyRotationsLivingRenderer(AbstractClientPlayerEntity entityLiving, MatrixStack matrixStackIn, float ageInTicks, float rotationYaw, float partialTicks, float headYaw) {
        if (this.isShaking(entityLiving)) {
            rotationYaw += (float) (Math.cos((double) entityLiving.age * 3.25D) * Math.PI * (double) 0.4F);
        }

        EntityPose pose = entityLiving.getPose();
        if (pose != EntityPose.SLEEPING) {
            float bodyRotateAmount = this.geoModel.getControllerValueInverted("BodyRotateController");
            matrixStackIn.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F - MathHelper.lerpAngleDegrees(bodyRotateAmount, headYaw, rotationYaw)));
        }

        if (entityLiving.deathTime > 0) {
            float f = ((float) entityLiving.deathTime + partialTicks - 1.0F) / 20.0F * 1.6F;
            f = MathHelper.sqrt(f);
            if (f > 1.0F) {
                f = 1.0F;
            }

            matrixStackIn.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(f * this.getLyingAngle(entityLiving)));
        } else if (entityLiving.isUsingRiptide()) {
            matrixStackIn.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-90.0F - entityLiving.getPitch()));
            matrixStackIn.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(((float) entityLiving.age + partialTicks) * -75.0F));
        } else if (pose == EntityPose.SLEEPING) {
            Direction direction = entityLiving.getSleepingDirection();
            float f1 = direction != null ? getFacingAngle(direction) : rotationYaw;
            matrixStackIn.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(f1));
            matrixStackIn.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(this.getLyingAngle(entityLiving)));
            matrixStackIn.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(270.0F));
        } else if (shouldFlipUpsideDown(entityLiving)) {
            matrixStackIn.translate(0.0F, entityLiving.getHeight() + 0.1F, 0.0F);
            matrixStackIn.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(180.0F));
        }
    }

    @Override
    public GeoModel<GeckoPlayer> getGeoModel() {
        return this.geoModel;
    }

    @Override
    public GeckoPlayer getAnimatable() {
        return this.animatable;
    }

    public ModelGeckoPlayerThirdPerson getAnimatedPlayerModel() {
        return this.geoModel;
    }

    @Override
    public Identifier getTextureLocation(GeckoPlayer geckoPlayer) {
        return this.getTexture((AbstractClientPlayerEntity) geckoPlayer.getPlayer());
    }

    @Override
    public void renderRecursively(MatrixStack poseStack, GeckoPlayer animatable, GeoBone bone, RenderLayer renderType, VertexConsumerProvider bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        poseStack.push();
        RenderUtils.translateMatrixToBone(poseStack, bone);
        RenderUtils.translateToPivotPoint(poseStack, bone);
        RenderUtils.rotateMatrixAroundBone(poseStack, bone);
        RenderUtils.scaleMatrixForBone(poseStack, bone);
        // Record xform matrices for relevant bones
        if (bone instanceof MowzieGeoBone mowzieBone) {
            if (
                    mowzieBone.getName().equals("LeftHeldItem") || mowzieBone.getName().equals("RightHeldItem") ||
                            mowzieBone.getName().equals("Head") ||
                            mowzieBone.getName().equals("Body") ||
                            mowzieBone.getName().equals("BodyLayer") ||
                            mowzieBone.getName().equals("LeftArm") ||
                            mowzieBone.getName().equals("RightArm") ||
                            mowzieBone.getName().equals("RightLeg") ||
                            mowzieBone.getName().equals("LeftLeg") ||
                            mowzieBone.getName().equals("ParticleEmitterRoot")
            ) {
                poseStack.push();
                if (!mowzieBone.getName().equals("LeftHeldItem") && !mowzieBone.getName().equals("RightHeldItem")) {
                    poseStack.scale(-1.0F, -1.0F, 1.0F);
                }
                if (mowzieBone.getName().equals("Body")) {
                    poseStack.translate(0, -0.75, 0);
                }
                if (mowzieBone.getName().equals("LeftArm")) {
                    poseStack.translate(-0.075, 0, 0);
                }
                if (mowzieBone.getName().equals("RightArm")) {
                    poseStack.translate(0.075, 0, 0);
                }
                MatrixStack.Entry entry = poseStack.peek();
                mowzieBone.setWorldSpaceNormal(new Matrix3f(entry.getNormalMatrix()));
                mowzieBone.setWorldSpaceMatrix(new Matrix4f(entry.getPositionMatrix()));
                poseStack.pop();
            }
        }
        RenderUtils.translateAwayFromPivotPoint(poseStack, bone);
        this.renderCubesOfBone(poseStack, bone, buffer, packedLight, packedOverlay, red, green, blue, alpha);

        if (!isReRender)
            this.applyRenderLayersForBone(poseStack, animatable, bone, renderType, bufferSource, buffer, partialTick, packedLight, packedOverlay);

        this.renderChildBones(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
        poseStack.pop();

        for (FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> layerrenderer : this.features) {
            if (layerrenderer instanceof IGeckoRenderLayer)
                ((IGeckoRenderLayer) layerrenderer).renderRecursively(bone, poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        }
    }

    /**
     * Update the current frame of a {@link AnimatableTexture potentially animated} texture used by this GeoRenderer.<br>
     * This should only be called immediately prior to rendering, and only
     *
     * @see AnimatableTexture#setAndUpdate
     */
    @Override
    public void updateAnimatedTextureFrame(GeckoPlayer animatable) {
        AnimatableTexture.setAndUpdate(this.getTextureLocation(animatable));
    }

    /**
     * Create and fire the relevant {@code CompileLayers} event hook for this renderer
     */
    @Override
    public void fireCompileRenderLayersEvent() {
    }

    /**
     * Create and fire the relevant {@code Pre-Render} event hook for this renderer.<br>
     *
     * @return Whether the renderer should proceed based on the cancellation state of the event
     */
    @Override
    public boolean firePreRenderEvent(MatrixStack poseStack, BakedGeoModel model, VertexConsumerProvider bufferSource, float partialTick, int packedLight) {
        return true;
    }

    /**
     * Create and fire the relevant {@code Post-Render} event hook for this renderer
     */
    @Override
    public void firePostRenderEvent(MatrixStack poseStack, BakedGeoModel model, VertexConsumerProvider bufferSource, float partialTick, int packedLight) {
    }
}
