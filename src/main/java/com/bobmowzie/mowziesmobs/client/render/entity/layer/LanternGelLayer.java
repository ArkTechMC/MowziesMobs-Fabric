package com.bobmowzie.mowziesmobs.client.render.entity.layer;

import com.bobmowzie.mowziesmobs.client.model.entity.ModelLantern;
import com.bobmowzie.mowziesmobs.server.entity.lantern.EntityLantern;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;

public class LanternGelLayer<T extends EntityLantern> extends FeatureRenderer<T, ModelLantern<T>> {
    private final EntityModel<T> model = new ModelLantern<>(true);

    public LanternGelLayer(FeatureRendererContext<T, ModelLantern<T>> renderer) {
        super(renderer);
    }

    public void render(MatrixStack matrixStackIn, VertexConsumerProvider bufferIn, int packedLightIn, T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!entitylivingbaseIn.isInvisible()) {
            this.getContextModel().copyStateTo(this.model);
            this.model.animateModel(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks);
            this.model.setAngles(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            VertexConsumer ivertexbuilder = bufferIn.getBuffer(RenderLayer.getEntityTranslucent(this.getTexture(entitylivingbaseIn)));
            this.model.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
        }
    }
}
