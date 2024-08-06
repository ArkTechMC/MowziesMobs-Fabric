package com.bobmowzie.mowziesmobs.client.render.entity.player;

import com.bobmowzie.mowziesmobs.client.model.entity.ModelGeckoPlayerFirstPerson;
import com.bobmowzie.mowziesmobs.client.model.tools.MathUtils;
import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieGeoBone;
import com.bobmowzie.mowziesmobs.client.render.MowzieRenderUtils;
import com.bobmowzie.mowziesmobs.server.ability.Ability;
import com.bobmowzie.mowziesmobs.server.ability.AbilitySection;
import com.bobmowzie.mowziesmobs.server.ability.PlayerAbility;
import com.bobmowzie.mowziesmobs.server.capability.AbilityCapability;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector4f;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.util.RenderUtils;

import java.util.HashMap;

@Environment(EnvType.CLIENT)
public class GeckoFirstPersonRenderer extends HeldItemRenderer implements GeoRenderer<GeckoPlayer> {
    public static GeckoPlayer.GeckoPlayerFirstPerson GECKO_PLAYER_FIRST_PERSON;
    private static final HashMap<Class<? extends GeckoPlayer>, GeckoFirstPersonRenderer> modelsToLoad = new HashMap<>();
    public VertexConsumerProvider rtb;
    public Vec3d particleEmitterRoot;
    boolean mirror;
    private final ModelGeckoPlayerFirstPerson geoModel;

    public GeckoFirstPersonRenderer(MinecraftClient mcIn, ModelGeckoPlayerFirstPerson geoModel) {
        super(mcIn, mcIn.getEntityRenderDispatcher(), mcIn.getItemRenderer());
        this.geoModel = geoModel;
    }

    public HashMap<Class<? extends GeckoPlayer>, GeckoFirstPersonRenderer> getModelsToLoad() {
        return modelsToLoad;
    }

    public void renderItemInFirstPerson(AbstractClientPlayerEntity player, float pitch, float partialTicks, Hand handIn, float swingProgress, ItemStack stack, float equippedProgress, MatrixStack matrixStackIn, VertexConsumerProvider bufferIn, int combinedLightIn, GeckoPlayer geckoPlayer) {
        this.rtb = bufferIn;

        boolean flag = handIn == Hand.MAIN_HAND;
        Arm handside = flag ? player.getMainArm() : player.getMainArm().getOpposite();
        this.mirror = player.getMainArm() == Arm.LEFT;

        if (flag) {
            this.geoModel.setTextureFromPlayer(player);
            AnimationState<GeckoPlayer> animationState = new AnimationState<>(geckoPlayer, 0, 0, partialTicks, false);
            long instanceId = this.getInstanceId(geckoPlayer);

            AnimatableManager<GeckoPlayer> animatableManager = geckoPlayer.getAnimatableInstanceCache().getManagerForId(instanceId);
            animationState.setData(DataTickets.TICK, geckoPlayer.getTick(geckoPlayer) + animatableManager.getFirstTickTime() + +MinecraftClient.getInstance().getTickDelta());
            AbstractClientPlayerEntity entity = (AbstractClientPlayerEntity) geckoPlayer.getPlayer();
            animationState.setData(DataTickets.ENTITY, entity);
            this.geoModel.addAdditionalStateData(geckoPlayer, instanceId, animationState::setData);
            this.geoModel.handleAnimations(geckoPlayer, instanceId, animationState);

            RenderLayer rendertype = RenderLayer.getItemEntityTranslucentCull(this.getTextureLocation(geckoPlayer));
            VertexConsumer ivertexbuilder = bufferIn.getBuffer(rendertype);
            matrixStackIn.translate(0, -2, -1);
            this.actuallyRender(matrixStackIn, geckoPlayer, this.getGeoModel().getBakedModel(this.getGeoModel().getModelResource(geckoPlayer)), rendertype, bufferIn, ivertexbuilder, false, partialTicks, combinedLightIn, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
        }

        PlayerAbility.HandDisplay handDisplay = PlayerAbility.HandDisplay.DEFAULT;
        float offHandEquipProgress = 0.0f;
        AbilityCapability.IAbilityCapability abilityCapability = AbilityCapability.get(player);
        if (abilityCapability != null && abilityCapability.getActiveAbility() != null) {
            Ability ability = abilityCapability.getActiveAbility();
            if (ability instanceof PlayerAbility playerAbility) {
                ItemStack stackOverride = flag ? playerAbility.heldItemMainHandOverride() : playerAbility.heldItemOffHandOverride();
                if (stackOverride != null) stack = stackOverride;

                handDisplay = flag ? playerAbility.getFirstPersonMainHandDisplay() : playerAbility.getFirstPersonOffHandDisplay();
            }

            if (ability.getCurrentSection().sectionType == AbilitySection.AbilitySectionType.STARTUP)
                offHandEquipProgress = MathHelper.clamp(1f - (ability.getTicksInSection() + partialTicks) / 5f, 0f, 1f);
            else if (ability.getCurrentSection().sectionType == AbilitySection.AbilitySectionType.RECOVERY && ability.getCurrentSection() instanceof AbilitySection.AbilitySectionDuration)
                offHandEquipProgress = MathHelper.clamp((ability.getTicksInSection() + partialTicks - ((AbilitySection.AbilitySectionDuration) ability.getCurrentSection()).duration + 5) / 5f, 0f, 1f);
        }

        if (this.geoModel.isInitialized()) {
            if (handDisplay != PlayerAbility.HandDisplay.DONT_RENDER) {
                int sideMult = handside == Arm.RIGHT ? -1 : 1;
                if (this.mirror) handside = handside.getOpposite();
                String sideName = handside == Arm.RIGHT ? "Right" : "Left";
                String boneName = sideName + "Arm";
                MowzieGeoBone bone = this.geoModel.getMowzieBone(boneName);

                MatrixStack newMatrixStack = new MatrixStack();

                float fixedPitchController = 1f - this.geoModel.getControllerValueInverted("FixedPitchController" + sideName);
                newMatrixStack.multiply(new Quaternionf(RotationAxis.POSITIVE_X.rotationDegrees(pitch * fixedPitchController)));

                newMatrixStack.peek().getNormalMatrix().mul(bone.getWorldSpaceNormal());
                newMatrixStack.peek().getPositionMatrix().mul(bone.getWorldSpaceMatrix());
                newMatrixStack.translate(sideMult * 0.547, 0.7655, 0.625);

                if (this.mirror) handside = handside.getOpposite();

                if (stack.isEmpty() && !flag && handDisplay == PlayerAbility.HandDisplay.FORCE_RENDER && !player.isInvisible()) {
                    newMatrixStack.translate(0, -1 * offHandEquipProgress, 0);
                    super.renderArmHoldingItem(newMatrixStack, bufferIn, combinedLightIn, 0.0f, 0.0f, handside);
                } else {
                    super.renderFirstPersonItem(player, partialTicks, pitch, handIn, 0.0f, stack, 0.0f, newMatrixStack, bufferIn, combinedLightIn);
                }
            }

            MatrixStack toWorldSpace = new MatrixStack();
            toWorldSpace.translate(player.getX(), player.getY() + player.getStandingEyeHeight(), player.getZ());
            toWorldSpace.multiply(MathUtils.quatFromRotationXYZ(0, -player.getYaw() + 180, 0, true));
            toWorldSpace.multiply(MathUtils.quatFromRotationXYZ(-player.getPitch(), 0, 0, true));
            MowzieGeoBone particleEmitterRootBone = this.geoModel.getMowzieBone("ParticleEmitterRoot");
            Vector4f emitterRootPos = new Vector4f(0, 0, 0, 1);
            emitterRootPos.mul(particleEmitterRootBone.getWorldSpaceMatrix());
            emitterRootPos.mul(toWorldSpace.peek().getPositionMatrix());
            this.particleEmitterRoot = new Vec3d(emitterRootPos.x(), emitterRootPos.y(), emitterRootPos.z());
        }
    }

    public void setSmallArms() {
        this.geoModel.setUseSmallArms(true);
    }

    public ModelGeckoPlayerFirstPerson getAnimatedPlayerModel() {
        return this.geoModel;
    }

    @Override
    public GeoModel<GeckoPlayer> getGeoModel() {
        return this.geoModel;
    }

    @Override
    public GeckoPlayer getAnimatable() {
        return null;
    }

    @Override
    public Identifier getTextureLocation(GeckoPlayer geckoPlayer) {
        return ((AbstractClientPlayerEntity) geckoPlayer.getPlayer()).getSkinTexture();
    }

    @Override
    public void fireCompileRenderLayersEvent() {

    }

    @Override
    public boolean firePreRenderEvent(MatrixStack poseStack, BakedGeoModel model, VertexConsumerProvider bufferSource, float partialTick, int packedLight) {
        return false;
    }

    @Override
    public void firePostRenderEvent(MatrixStack poseStack, BakedGeoModel model, VertexConsumerProvider bufferSource, float partialTick, int packedLight) {

    }

    @Override
    public void updateAnimatedTextureFrame(GeckoPlayer animatable) {

    }

    @Override
    public void renderRecursively(MatrixStack matrixStack, GeckoPlayer animatable, GeoBone bone, RenderLayer renderType, VertexConsumerProvider bufferIn, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        matrixStack.push();
        if (this.mirror) {
            MowzieRenderUtils.translateMirror(matrixStack, bone);
            MowzieRenderUtils.moveToPivotMirror(matrixStack, bone);
            MowzieRenderUtils.rotateMirror(matrixStack, bone);
            RenderUtils.scaleMatrixForBone(matrixStack, bone);
        } else {
            RenderUtils.translateMatrixToBone(matrixStack, bone);
            RenderUtils.translateToPivotPoint(matrixStack, bone);
            RenderUtils.rotateMatrixAroundBone(matrixStack, bone);
            RenderUtils.scaleMatrixForBone(matrixStack, bone);
        }
        // Record xform matrices for relevant bones
        if (bone instanceof MowzieGeoBone mowzieBone) {
            if (mowzieBone.getName().equals("LeftArm") || mowzieBone.getName().equals("RightArm") || mowzieBone.getName().equals("ParticleEmitterRoot")) {
                matrixStack.push();
                MatrixStack.Entry entry = matrixStack.peek();
                mowzieBone.setWorldSpaceNormal(new Matrix3f(entry.getNormalMatrix()));
                mowzieBone.setWorldSpaceMatrix(new Matrix4f(entry.getPositionMatrix()));
                matrixStack.pop();
            }
        }
        if (this.mirror) {
            MowzieRenderUtils.translateAwayFromPivotPointMirror(matrixStack, bone);
        } else {
            RenderUtils.translateAwayFromPivotPoint(matrixStack, bone);
        }
        this.renderCubesOfBone(matrixStack, bone, buffer, packedLightIn, packedOverlayIn, red, green, blue, alpha);

        if (!isReRender)
            this.applyRenderLayersForBone(matrixStack, animatable, bone, renderType, bufferIn, buffer, partialTick, packedLightIn, packedOverlayIn);

        this.renderChildBones(matrixStack, animatable, bone, renderType, bufferIn, buffer, isReRender, partialTick, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        matrixStack.pop();
    }
}
