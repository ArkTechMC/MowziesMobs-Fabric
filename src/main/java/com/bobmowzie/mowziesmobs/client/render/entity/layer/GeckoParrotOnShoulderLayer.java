package com.bobmowzie.mowziesmobs.client.render.entity.layer;

import com.bobmowzie.mowziesmobs.client.model.tools.ModelPartMatrix;
import com.bobmowzie.mowziesmobs.client.render.MowzieRenderUtils;
import com.bobmowzie.mowziesmobs.client.render.entity.player.GeckoRenderPlayer;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.ShoulderParrotFeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.util.math.MatrixStack;

public class GeckoParrotOnShoulderLayer extends ShoulderParrotFeatureRenderer<AbstractClientPlayerEntity> implements IGeckoRenderLayer {
    public GeckoParrotOnShoulderLayer(GeckoRenderPlayer rendererIn, EntityModelLoader modelSet) {
        super(rendererIn, modelSet);
    }

    @Override
    public void render(MatrixStack matrixStackIn, VertexConsumerProvider bufferIn, int packedLightIn, AbstractClientPlayerEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (this.getContextModel().body instanceof ModelPartMatrix) {
            MowzieRenderUtils.transformStackToModelPart(matrixStackIn, (ModelPartMatrix) this.getContextModel().body);
            super.render(matrixStackIn, bufferIn, packedLightIn, entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
            matrixStackIn.pop();
        }
    }
}
