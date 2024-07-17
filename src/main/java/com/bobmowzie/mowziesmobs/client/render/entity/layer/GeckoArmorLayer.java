package com.bobmowzie.mowziesmobs.client.render.entity.layer;

import com.bobmowzie.mowziesmobs.client.model.entity.ModelBipedAnimated;
import com.bobmowzie.mowziesmobs.client.render.entity.MowzieGeoArmorRenderer;
import net.minecraft.client.model.Model;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.model.BakedModelManager;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.item.trim.ArmorTrim;
import net.minecraft.util.Identifier;

public class GeckoArmorLayer<T extends LivingEntity, M extends BipedEntityModel<T>, A extends BipedEntityModel<T>> extends ArmorFeatureRenderer<T, M, A> {
    public GeckoArmorLayer(FeatureRendererContext<T, M> layerParent, A innerModel, A outerModel, BakedModelManager modelManager) {
        super(layerParent, innerModel, outerModel, modelManager);
    }

    @Override
    protected void renderArmor(MatrixStack poseStack, VertexConsumerProvider bufferSource, T entity, EquipmentSlot equipmentSlot, int p_117123_, A baseModel) {
        ItemStack itemstack = entity.getEquippedStack(equipmentSlot);
        if (itemstack.getItem() instanceof ArmorItem armoritem) {
            if (armoritem.getType().getEquipmentSlot() == equipmentSlot) {
                Model model = getArmorModelHook(entity, itemstack, equipmentSlot, baseModel);
                if (model instanceof BipedEntityModel<?>) {
                    BipedEntityModel<T> humanoidModel = (BipedEntityModel<T>) model;
                    this.getContextModel().copyBipedStateTo(baseModel);
                    this.getContextModel().copyBipedStateTo(humanoidModel);
                    this.setVisible(baseModel, equipmentSlot);
                    this.setVisible((A) humanoidModel, equipmentSlot);
                    boolean flag = this.usesInnerModel(equipmentSlot);
                    boolean flag1 = itemstack.hasGlint();
                    ModelBipedAnimated.setUseMatrixMode(humanoidModel, true);
                    if (armoritem instanceof net.minecraft.item.DyeableItem) {
                        int i = ((net.minecraft.item.DyeableItem) armoritem).getColor(itemstack);
                        float f = (float) (i >> 16 & 255) / 255.0F;
                        float f1 = (float) (i >> 8 & 255) / 255.0F;
                        float f2 = (float) (i & 255) / 255.0F;
                        this.renderModel(poseStack, bufferSource, p_117123_, model, f, f1, f2, this.getArmorResource(entity, itemstack, equipmentSlot, null));
                        this.renderModel(poseStack, bufferSource, p_117123_, model, 1.0F, 1.0F, 1.0F, this.getArmorResource(entity, itemstack, equipmentSlot, "overlay"));
                    } else {
                        this.renderModel(poseStack, bufferSource, p_117123_, model, 1.0F, 1.0F, 1.0F, this.getArmorResource(entity, itemstack, equipmentSlot, null));
                    }

                    ArmorTrim.getTrim(entity.getWorld().getRegistryManager(), itemstack).ifPresent((p_289638_) -> {
                        ModelBipedAnimated.setUseMatrixMode(humanoidModel, true);
                        this.renderTrim(armoritem.getMaterial(), poseStack, bufferSource, p_117123_, p_289638_, model, flag);
                    });
                    if (itemstack.hasGlint()) {
                        ModelBipedAnimated.setUseMatrixMode(humanoidModel, true);
                        this.renderGlint(poseStack, bufferSource, p_117123_, model);
                    }
                }
            }
        }
    }

    private void renderModel(MatrixStack p_117107_, VertexConsumerProvider p_117108_, int p_117109_, net.minecraft.client.model.Model p_117112_, float p_117114_, float p_117115_, float p_117116_, Identifier armorResource) {
        VertexConsumer vertexconsumer = p_117108_.getBuffer(RenderLayer.getArmorCutoutNoCull(armorResource));
        if (p_117112_ instanceof MowzieGeoArmorRenderer<?>)
            ((MowzieGeoArmorRenderer<?>) p_117112_).usingCustomPlayerAnimations = true;
        p_117112_.render(p_117107_, vertexconsumer, p_117109_, OverlayTexture.DEFAULT_UV, p_117114_, p_117115_, p_117116_, 1.0F);
    }

    private void renderTrim(ArmorMaterial p_289690_, MatrixStack p_289687_, VertexConsumerProvider p_289643_, int p_289683_, ArmorTrim p_289692_, net.minecraft.client.model.Model p_289663_, boolean p_289651_) {
        Sprite textureatlassprite = this.armorTrimsAtlas.getSprite(p_289651_ ? p_289692_.getLeggingsModelId(p_289690_) : p_289692_.getGenericModelId(p_289690_));
        VertexConsumer vertexconsumer = textureatlassprite.getTextureSpecificVertexConsumer(p_289643_.getBuffer(TexturedRenderLayers.getArmorTrims()));
        p_289663_.render(p_289687_, vertexconsumer, p_289683_, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
    }

    private void renderGlint(MatrixStack p_289673_, VertexConsumerProvider p_289654_, int p_289649_, net.minecraft.client.model.Model p_289659_) {
        p_289659_.render(p_289673_, p_289654_.getBuffer(RenderLayer.getArmorEntityGlint()), p_289649_, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
    }
}
