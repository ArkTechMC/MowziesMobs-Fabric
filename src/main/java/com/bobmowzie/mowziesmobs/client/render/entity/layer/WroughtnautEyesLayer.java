package com.bobmowzie.mowziesmobs.client.render.entity.layer;

import com.bobmowzie.mowziesmobs.client.model.entity.ModelWroughtnaut;
import com.bobmowzie.mowziesmobs.client.render.MMRenderType;
import com.bobmowzie.mowziesmobs.server.entity.wroughtnaut.EntityWroughtnaut;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;

public class WroughtnautEyesLayer<T extends EntityWroughtnaut> extends FeatureRenderer<T, ModelWroughtnaut<T>> {
    private final EntityModel<T> model = new ModelWroughtnaut<>(true);

    public WroughtnautEyesLayer(FeatureRendererContext<T, ModelWroughtnaut<T>> renderer) {
        super(renderer);
    }

    public void render(MatrixStack matrixStackIn, VertexConsumerProvider bufferIn, int packedLightIn, T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!entitylivingbaseIn.isInvisible()) {
            this.getContextModel().copyStateTo(this.model);
            this.model.animateModel(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks);
            this.model.setAngles(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            VertexConsumer ivertexbuilder = bufferIn.getBuffer(MMRenderType.getEyes(this.getTexture(entitylivingbaseIn)));
            this.model.render(matrixStackIn, ivertexbuilder, packedLightIn, LivingEntityRenderer.getOverlay(entitylivingbaseIn, 0.0F), 1.0F, 1.0F, 1.0F, 1.0F);
        }
    }
}