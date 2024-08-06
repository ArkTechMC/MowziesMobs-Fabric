package com.bobmowzie.mowziesmobs.client.render.entity.layer;

import com.bobmowzie.mowziesmobs.client.model.entity.ModelBipedAnimated;
import com.bobmowzie.mowziesmobs.client.render.entity.MowzieGeoArmorRenderer;
import com.google.common.collect.Maps;
import com.iafenvoy.uranus.client.render.armor.IArmorTextureProvider;
import net.minecraft.client.model.Model;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.model.BakedModelManager;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.item.trim.ArmorTrim;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.Map;

public class GeckoArmorLayer<T extends LivingEntity, M extends BipedEntityModel<T>, A extends BipedEntityModel<T>> extends ArmorFeatureRenderer<T, M, A> {
    public GeckoArmorLayer(FeatureRendererContext<T, M> layerParent, A innerModel, A outerModel, BakedModelManager modelManager) {
        super(layerParent, innerModel, outerModel, modelManager);
    }

    @Override
    public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, T livingEntity, float f, float g, float h, float j, float k, float l) {
        super.render(matrixStack, vertexConsumerProvider, i, livingEntity, f, g, h, j, k, l);
    }

    @Override
    public void renderArmor(MatrixStack poseStack, VertexConsumerProvider bufferSource, T entity, EquipmentSlot equipmentSlot, int p_117123_, A baseModel) {
        ItemStack itemstack = entity.getEquippedStack(equipmentSlot);
        if (itemstack.getItem() instanceof ArmorItem armoritem) {
            if (armoritem.getType().getEquipmentSlot() == equipmentSlot) {
                if (baseModel instanceof BipedEntityModel<?>) {
                    this.getContextModel().copyBipedStateTo(baseModel);
                    this.getContextModel().copyBipedStateTo(baseModel);
                    this.setVisible(baseModel, equipmentSlot);
                    this.setVisible(baseModel, equipmentSlot);
                    boolean flag = this.usesInnerModel(equipmentSlot);
                    boolean flag1 = itemstack.hasGlint();
                    ModelBipedAnimated.setUseMatrixMode(baseModel, true);
                    if (armoritem instanceof net.minecraft.item.DyeableItem) {
                        int i = ((net.minecraft.item.DyeableItem) armoritem).getColor(itemstack);
                        float f = (float) (i >> 16 & 255) / 255.0F;
                        float f1 = (float) (i >> 8 & 255) / 255.0F;
                        float f2 = (float) (i & 255) / 255.0F;
                        //this.getArmorResource(entity, itemstack, equipmentSlot, null)
                        this.renderModel(poseStack, bufferSource, p_117123_, baseModel, f, f1, f2, this.getArmorResource(entity, itemstack, equipmentSlot, null));
                        this.renderModel(poseStack, bufferSource, p_117123_, baseModel, 1.0F, 1.0F, 1.0F, this.getArmorResource(entity, itemstack, equipmentSlot, "overlay"));
                    } else {
                        this.renderModel(poseStack, bufferSource, p_117123_,  baseModel, 1.0F, 1.0F, 1.0F, this.getArmorResource(entity, itemstack, equipmentSlot, null));
                    }

                    ArmorTrim.getTrim(entity.getWorld().getRegistryManager(), itemstack).ifPresent((p_289638_) -> {
                        ModelBipedAnimated.setUseMatrixMode(baseModel, true);
                        this.renderTrim(armoritem.getMaterial(), poseStack, bufferSource, p_117123_, p_289638_, baseModel, flag);
                    });
                    if (itemstack.hasGlint()) {
                        ModelBipedAnimated.setUseMatrixMode(baseModel, true);
                        this.renderGlint(poseStack, bufferSource, p_117123_, baseModel);
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

    //FIXME: standalone file
    private static final Map<Identifier, Identifier> ARMOR_TEXTURE_CACHE = Maps.newHashMap();

    private Identifier getArmorResource(Entity entity, ItemStack stack, EquipmentSlot slot, @Nullable String type) {
        ArmorItem item = (ArmorItem) stack.getItem();
        String texture = item.getMaterial().getName();
        String domain = "minecraft";
        int idx = texture.indexOf(58);
        if (idx != -1) {
            domain = texture.substring(0, idx);
            texture = texture.substring(idx + 1);
        }

        Identifier s1 = new Identifier(domain,String.format(Locale.ROOT, "textures/models/armor/%s_layer_%d%s.png", texture, this.usesInnerModel(slot) ? 2 : 1, type == null ? "" : String.format(Locale.ROOT, "_%s", type)));
        s1 = getArmorTexture(entity, stack, s1, slot, type);
        return ARMOR_TEXTURE_CACHE.computeIfAbsent(s1, s -> s);
    }

    public static Identifier getArmorTexture(Entity entity, ItemStack armor, Identifier _default, EquipmentSlot slot, String type) {
        if (armor.getItem() instanceof IArmorTextureProvider provider)
            return provider.getArmorTexture(armor, entity, slot, type);
        return _default;
    }
}
