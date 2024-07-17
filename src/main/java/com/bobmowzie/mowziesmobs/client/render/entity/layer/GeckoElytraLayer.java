package com.bobmowzie.mowziesmobs.client.render.entity.layer;

import com.bobmowzie.mowziesmobs.client.model.tools.ModelPartMatrix;
import com.bobmowzie.mowziesmobs.client.render.MowzieRenderUtils;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.ElytraFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;

public class GeckoElytraLayer extends ElytraFeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {
    public GeckoElytraLayer(FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> rendererIn, EntityModelLoader modelSet) {
        super(rendererIn, modelSet);
    }

    public void render(MatrixStack matrixStackIn, VertexConsumerProvider bufferIn, int packedLightIn, AbstractClientPlayerEntity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (this.getContextModel().body instanceof ModelPartMatrix) {
            MowzieRenderUtils.transformStackToModelPart(matrixStackIn, (ModelPartMatrix) this.getContextModel().body);
            super.render(matrixStackIn, bufferIn, packedLightIn, entity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
            matrixStackIn.pop();
        }
    }
}
