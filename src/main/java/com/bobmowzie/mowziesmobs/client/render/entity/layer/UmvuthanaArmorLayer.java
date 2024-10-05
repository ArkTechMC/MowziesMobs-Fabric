package com.bobmowzie.mowziesmobs.client.render.entity.layer;

import com.bobmowzie.mowziesmobs.server.entity.MowzieGeckoEntity;
import com.bobmowzie.mowziesmobs.server.entity.umvuthana.EntityUmvuthana;
import com.iafenvoy.uranus.client.render.armor.IArmorTextureProvider;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.joml.Quaternionf;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;
import software.bernie.geckolib.util.RenderUtils;

public class UmvuthanaArmorLayer extends GeoRenderLayer<EntityUmvuthana> {
    private final BipedEntityModel defaultBipedModel;
    private MowzieGeckoEntity entity;

    public UmvuthanaArmorLayer(GeoRenderer<EntityUmvuthana> entityRendererIn, EntityRendererFactory.Context context) {
        super(entityRendererIn);
        this.defaultBipedModel = new BipedEntityModel(context.getPart(EntityModelLayers.PLAYER_INNER_ARMOR));
    }

    @Override
    public void renderForBone(MatrixStack poseStack, EntityUmvuthana animatable, GeoBone bone, RenderLayer renderType, VertexConsumerProvider bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        super.renderForBone(poseStack, animatable, bone, renderType, bufferSource, buffer, partialTick, packedLight, packedOverlay);
        if (bone.isHidden()) return;
        poseStack.push();
        RenderUtils.translateToPivotPoint(poseStack, bone);
        String boneName = "maskTwitcher";
        String handBoneName = "maskHand";
        if (bone.getName().equals(boneName) || bone.getName().equals(handBoneName)) {
            this.renderArmor(animatable, bufferSource, poseStack, packedLight);
        }
        bufferSource.getBuffer(renderType);
        poseStack.pop();
    }

    private void renderArmor(LivingEntity entityLivingBaseIn, VertexConsumerProvider bufferIn, MatrixStack poseStack, int packedLightIn) {
        ItemStack itemStack = entityLivingBaseIn.getEquippedStack(EquipmentSlot.HEAD);
        if (itemStack.getItem() instanceof ArmorItem armoritem&&armoritem instanceof IArmorTextureProvider provider) {
            if (armoritem.getType() == ArmorItem.Type.HELMET) {
                boolean glintIn = itemStack.hasGlint();
                BipedEntityModel a = this.defaultBipedModel;
                Identifier armorTexture = provider.getArmorTexture(itemStack, entityLivingBaseIn, EquipmentSlot.HEAD, null);
                if (armorTexture != null) {
                    VertexConsumer ivertexbuilder = ItemRenderer.getItemGlintConsumer(bufferIn, RenderLayer.getEntityCutoutNoCull(armorTexture), false, glintIn);
                    poseStack.multiply((new Quaternionf()).rotationXYZ(0.0F, 0.0F, (float) Math.PI));
                    poseStack.scale(1.511f, 1.511f, 1.511f);
                    poseStack.translate(0, -0.55, 0.15);
                    a.render(poseStack, ivertexbuilder, packedLightIn, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
                }
            }
        }
    }
}
