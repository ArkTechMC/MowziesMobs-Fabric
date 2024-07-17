package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.capability.CapabilityHandler;
import com.bobmowzie.mowziesmobs.server.capability.FrozenCapability;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.*;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

/**
 * Created by BobMowzie on 6/28/2017.
 */
public enum FrozenRenderHandler {
    INSTANCE;

    private static final Identifier FROZEN_TEXTURE = new Identifier(MowziesMobs.MODID, "textures/entity/frozen.png");

    @SubscribeEvent
    public void onRenderHand(RenderHandEvent event) {
        event.getPoseStack().pushPose();

        PlayerEntity player = MinecraftClient.getInstance().player;

        if (player != null) {
            FrozenCapability.IFrozenCapability frozenCapability = CapabilityHandler.getCapability(player, CapabilityHandler.FROZEN_CAPABILITY);
            if (frozenCapability != null && frozenCapability.getFrozen()) {
                boolean isMainHand = event.getHand() == Hand.MAIN_HAND;
                if (isMainHand && !player.isInvisible() && event.getItemStack().isEmpty()) {
                    Arm enumhandside = isMainHand ? player.getMainArm() : player.getMainArm().getOpposite();
                    this.renderArmFirstPersonFrozen(event.getPoseStack(), event.getMultiBufferSource(), event.getPackedLight(), event.getEquipProgress(), event.getSwingProgress(), enumhandside);
                    event.setCanceled(true);
                }
            }
        }

        event.getPoseStack().popPose();
    }

    /**
     * From ItemRenderer#renderArmFirstPerson
     *
     * @param swingProgress
     * @param equippedProgress
     * @param side
     */
    private void renderArmFirstPersonFrozen(MatrixStack matrixStackIn, VertexConsumerProvider bufferIn, int combinedLightIn, float equippedProgress, float swingProgress, Arm side) {
        MinecraftClient mc = MinecraftClient.getInstance();
        EntityRenderDispatcher renderManager = mc.getEntityRenderDispatcher();
        MinecraftClient.getInstance().getTextureManager().bindTexture(FROZEN_TEXTURE);
        boolean flag = side != Arm.LEFT;
        float f = flag ? 1.0F : -1.0F;
        float f1 = MathHelper.sqrt(swingProgress);
        float f2 = -0.3F * MathHelper.sin(f1 * (float) Math.PI);
        float f3 = 0.4F * MathHelper.sin(f1 * ((float) Math.PI * 2F));
        float f4 = -0.4F * MathHelper.sin(swingProgress * (float) Math.PI);
        matrixStackIn.translate(f * (f2 + 0.64000005F), f3 + -0.6F + equippedProgress * -0.6F, f4 + -0.71999997F);
        matrixStackIn.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(f * 45.0F));
        float f5 = MathHelper.sin(swingProgress * swingProgress * (float) Math.PI);
        float f6 = MathHelper.sin(f1 * (float) Math.PI);
        matrixStackIn.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(f * f6 * 70.0F));
        matrixStackIn.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(f * f5 * -20.0F));
        AbstractClientPlayerEntity abstractclientplayerentity = mc.player;
        mc.getTextureManager().bindTexture(abstractclientplayerentity.getSkinTexture());
        matrixStackIn.translate(f * -1.0F, 3.6F, 3.5D);
        matrixStackIn.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(f * 120.0F));
        matrixStackIn.multiply(RotationAxis.POSITIVE_X.rotationDegrees(200.0F));
        matrixStackIn.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(f * -135.0F));
        matrixStackIn.translate(f * 5.6F, 0.0D, 0.0D);
        PlayerEntityRenderer playerrenderer = (PlayerEntityRenderer) renderManager.getRenderer(abstractclientplayerentity);
        if (flag) {
            playerrenderer.renderRightArm(matrixStackIn, bufferIn, combinedLightIn, abstractclientplayerentity);
            matrixStackIn.scale(1.02f, 1.02f, 1.02f);
            this.renderRightArm(matrixStackIn, bufferIn, combinedLightIn, abstractclientplayerentity, playerrenderer.getModel());
        } else {
            playerrenderer.renderLeftArm(matrixStackIn, bufferIn, combinedLightIn, abstractclientplayerentity);
            matrixStackIn.scale(1.02f, 1.02f, 1.02f);
            this.renderLeftArm(matrixStackIn, bufferIn, combinedLightIn, abstractclientplayerentity, playerrenderer.getModel());
        }
    }

    public void renderRightArm(MatrixStack matrixStackIn, VertexConsumerProvider bufferIn, int combinedLightIn, AbstractClientPlayerEntity playerIn, PlayerEntityModel<AbstractClientPlayerEntity> model) {
        this.renderItem(matrixStackIn, bufferIn, combinedLightIn, playerIn, (model).rightArm, (model).rightSleeve, model);
    }

    public void renderLeftArm(MatrixStack matrixStackIn, VertexConsumerProvider bufferIn, int combinedLightIn, AbstractClientPlayerEntity playerIn, PlayerEntityModel<AbstractClientPlayerEntity> model) {
        this.renderItem(matrixStackIn, bufferIn, combinedLightIn, playerIn, (model).leftArm, (model).leftSleeve, model);
    }

    private void renderItem(MatrixStack matrixStackIn, VertexConsumerProvider bufferIn, int combinedLightIn, AbstractClientPlayerEntity playerIn, ModelPart rendererArmIn, ModelPart rendererArmwearIn, PlayerEntityModel<AbstractClientPlayerEntity> model) {
        this.setModelVisibilities(playerIn, model);
        model.handSwingProgress = 0.0F;
        model.sneaking = false;
        model.leaningPitch = 0.0F;
        model.setAngles(playerIn, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        rendererArmwearIn.pitch = 0.0F;
        rendererArmwearIn.render(matrixStackIn, bufferIn.getBuffer(RenderLayer.getEntityTranslucent(FROZEN_TEXTURE)), combinedLightIn, OverlayTexture.DEFAULT_UV, 1, 1, 1, 0.8f);
    }

    private void setModelVisibilities(AbstractClientPlayerEntity clientPlayer, PlayerEntityModel<AbstractClientPlayerEntity> playermodel) {
        if (clientPlayer.isSpectator()) {
            playermodel.setVisible(false);
            playermodel.head.visible = true;
            playermodel.hat.visible = true;
        } else {
            playermodel.setVisible(true);
            playermodel.hat.visible = clientPlayer.isPartVisible(PlayerModelPart.HAT);
            playermodel.jacket.visible = clientPlayer.isPartVisible(PlayerModelPart.JACKET);
            playermodel.leftPants.visible = clientPlayer.isPartVisible(PlayerModelPart.LEFT_PANTS_LEG);
            playermodel.rightPants.visible = clientPlayer.isPartVisible(PlayerModelPart.RIGHT_PANTS_LEG);
            playermodel.leftSleeve.visible = clientPlayer.isPartVisible(PlayerModelPart.LEFT_SLEEVE);
            playermodel.rightSleeve.visible = clientPlayer.isPartVisible(PlayerModelPart.RIGHT_SLEEVE);
            playermodel.sneaking = clientPlayer.isInSneakingPose();
        }
    }

    public static class LayerFrozen<T extends LivingEntity, M extends EntityModel<T>> extends FeatureRenderer<T, M> {
        private final LivingEntityRenderer<T, M> renderer;

        public LayerFrozen(LivingEntityRenderer<T, M> renderer) {
            super(renderer);
            this.renderer = renderer;
        }

        @Override
        public void render(MatrixStack matrixStackIn, VertexConsumerProvider bufferIn, int packedLightIn, LivingEntity living, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            FrozenCapability.IFrozenCapability frozenCapability = CapabilityHandler.getCapability(living, CapabilityHandler.FROZEN_CAPABILITY);
            if (frozenCapability != null && frozenCapability.getFrozen()) {
                EntityModel model = this.renderer.getModel();

                float transparency = 1;
                VertexConsumer ivertexbuilder = bufferIn.getBuffer(RenderLayer.getEntityTranslucent(FROZEN_TEXTURE));
                model.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.DEFAULT_UV, 1, 1, 1, transparency);
            }
        }
    }

    public static class GeckoLayerFrozen<T extends LivingEntity & GeoEntity> extends GeoRenderLayer<T> {

        public GeckoLayerFrozen(GeoRenderer<T> entityRendererIn, EntityRendererFactory.Context context) {
            super(entityRendererIn);
        }

        @Override
        public void render(MatrixStack poseStack, T animatable, BakedGeoModel bakedModel, RenderLayer renderType, VertexConsumerProvider bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
            FrozenCapability.IFrozenCapability frozenCapability = CapabilityHandler.getCapability(animatable, CapabilityHandler.FROZEN_CAPABILITY);
            if (frozenCapability != null && frozenCapability.getFrozen()) {
                RenderLayer frozenRenderType = RenderLayer.getEntityTranslucent(FROZEN_TEXTURE);

                this.getRenderer().reRender(this.getDefaultBakedModel(animatable),
                        poseStack, bufferSource, animatable, renderType, bufferSource.getBuffer(frozenRenderType),
                        partialTick, packedLight, OverlayTexture.DEFAULT_UV, 1, 1, 1, 1);
            }
        }
    }
}