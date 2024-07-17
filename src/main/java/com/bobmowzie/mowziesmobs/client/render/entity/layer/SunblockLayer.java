package com.bobmowzie.mowziesmobs.client.render.entity.layer;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.capability.CapabilityHandler;
import com.bobmowzie.mowziesmobs.server.capability.LivingCapability;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

public class SunblockLayer<T extends LivingEntity, M extends EntityModel<T>> extends FeatureRenderer<T, M> {
    private static final Identifier SUNBLOCK_ARMOR = new Identifier(MowziesMobs.MODID, "textures/entity/sunblock_glow.png");

    public SunblockLayer(FeatureRendererContext<T, M> entityRendererIn) {
        super(entityRendererIn);
    }

    public void render(MatrixStack matrixStackIn, VertexConsumerProvider bufferIn, int packedLightIn, T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        LivingCapability.ILivingCapability livingCapability = CapabilityHandler.getCapability(entitylivingbaseIn, CapabilityHandler.LIVING_CAPABILITY);
        if (livingCapability != null && livingCapability.getHasSunblock()) {
            float f = (float) entitylivingbaseIn.age + partialTicks;
            EntityModel<T> entitymodel = this.getContextModel();
            entitymodel.animateModel(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks);
            this.getContextModel().copyStateTo(entitymodel);
            VertexConsumer ivertexbuilder = bufferIn.getBuffer(RenderLayer.getEnergySwirl(this.getTexture(), this.xOffset(f), f * 0.01F));
            entitymodel.setAngles(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            entitymodel.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.DEFAULT_UV, 1F, 1F, 0.1F, 1.0F);
        }
    }

    protected float xOffset(float p_225634_1_) {
        return p_225634_1_ * 0.02F;
    }

    protected Identifier getTexture() {
        return SUNBLOCK_ARMOR;
    }
}
