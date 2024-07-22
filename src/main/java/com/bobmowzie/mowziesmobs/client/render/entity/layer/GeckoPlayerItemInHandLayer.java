package com.bobmowzie.mowziesmobs.client.render.entity.layer;

import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieGeoBone;
import com.bobmowzie.mowziesmobs.client.render.entity.player.GeckoRenderPlayer;
import com.bobmowzie.mowziesmobs.server.ability.Ability;
import com.bobmowzie.mowziesmobs.server.ability.PlayerAbility;
import com.bobmowzie.mowziesmobs.server.capability.AbilityCapability;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.math.RotationAxis;

@Environment(EnvType.CLIENT)
public class GeckoPlayerItemInHandLayer extends FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> implements IGeckoRenderLayer {
    private final GeckoRenderPlayer renderPlayerAnimated;

    public GeckoPlayerItemInHandLayer(GeckoRenderPlayer entityRendererIn) {
        super(entityRendererIn);
        this.renderPlayerAnimated = entityRendererIn;
    }

    @Override
    public void render(MatrixStack matrixStackIn, VertexConsumerProvider bufferIn, int packedLightIn, AbstractClientPlayerEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!this.renderPlayerAnimated.getAnimatedPlayerModel().isInitialized()) return;
        boolean flag = entitylivingbaseIn.getMainArm() == Arm.RIGHT;
        ItemStack mainHandStack = entitylivingbaseIn.getMainHandStack();
        ItemStack offHandStack = entitylivingbaseIn.getOffHandStack();
        AbilityCapability.IAbilityCapability abilityCapability = AbilityCapability.get(entitylivingbaseIn);
        if (abilityCapability != null && abilityCapability.getActiveAbility() != null) {
            Ability ability = abilityCapability.getActiveAbility();
            if (ability instanceof PlayerAbility playerAbility) {
                mainHandStack = playerAbility.heldItemMainHandOverride() != null ? playerAbility.heldItemMainHandOverride() : mainHandStack;
                offHandStack = playerAbility.heldItemOffHandOverride() != null ? playerAbility.heldItemOffHandOverride() : offHandStack;
            }
        }
        ItemStack itemstack = flag ? offHandStack : mainHandStack;
        ItemStack itemstack1 = flag ? mainHandStack : offHandStack;
        if (!itemstack.isEmpty() || !itemstack1.isEmpty()) {
            matrixStackIn.push();
            if (this.getContextModel().child) {
                float f = 0.5F;
                matrixStackIn.translate(0.0D, 0.75D, 0.0D);
                matrixStackIn.scale(0.5F, 0.5F, 0.5F);
            }

            this.renderArmWithItem(entitylivingbaseIn, itemstack1, ModelTransformationMode.THIRD_PERSON_RIGHT_HAND, Arm.RIGHT, matrixStackIn, bufferIn, packedLightIn);
            this.renderArmWithItem(entitylivingbaseIn, itemstack, ModelTransformationMode.THIRD_PERSON_LEFT_HAND, Arm.LEFT, matrixStackIn, bufferIn, packedLightIn);
            matrixStackIn.pop();
        }
    }

    private void renderArmWithItem(LivingEntity entity, ItemStack itemStack, ModelTransformationMode transformType, Arm side, MatrixStack matrixStack, VertexConsumerProvider buffer, int packedLightIn) {
        if (!itemStack.isEmpty()) {
            String boneName = side == Arm.RIGHT ? "RightHeldItem" : "LeftHeldItem";
            MowzieGeoBone bone = this.renderPlayerAnimated.getAnimatedPlayerModel().getMowzieBone(boneName);
            MatrixStack newMatrixStack = new MatrixStack();
            newMatrixStack.peek().getNormalMatrix().mul(bone.getWorldSpaceNormal());
            newMatrixStack.peek().getPositionMatrix().mul(bone.getWorldSpaceMatrix());
            newMatrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-90.0F));
            boolean flag = side == Arm.LEFT;
            MinecraftClient.getInstance().getEntityRenderDispatcher().getHeldItemRenderer().renderItem(entity, itemStack, transformType, flag, newMatrixStack, buffer, packedLightIn);
        }
    }
}
