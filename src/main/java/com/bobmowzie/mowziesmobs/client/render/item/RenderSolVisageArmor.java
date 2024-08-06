package com.bobmowzie.mowziesmobs.client.render.item;

import com.bobmowzie.mowziesmobs.client.model.armor.SolVisageModel;
import com.bobmowzie.mowziesmobs.client.render.entity.MowzieGeoArmorRenderer;
import com.bobmowzie.mowziesmobs.server.item.ItemSolVisage;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public class RenderSolVisageArmor extends MowzieGeoArmorRenderer<ItemSolVisage> implements ArmorRenderer {

    public RenderSolVisageArmor() {
        super(new SolVisageModel());
    }

    @Override
    public void setAngles(Entity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        super.animateModel((LivingEntity) entity, limbAngle, limbDistance, animationProgress);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, ItemStack stack, LivingEntity entity, EquipmentSlot slot, int light, BipedEntityModel<LivingEntity> contextModel) {
        if (slot == EquipmentSlot.HEAD)
            this.prepForRender(entity, stack, slot, contextModel);
        //FIXME: overlay
        this.render(matrices, vertexConsumers.getBuffer(RenderLayers.getItemLayer(stack, false)), light, OverlayTexture.DEFAULT_UV, 1, 1, 1, 1);
    }
}
