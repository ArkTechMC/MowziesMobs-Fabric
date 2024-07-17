package com.bobmowzie.mowziesmobs.client.render.entity.layer;

import com.bobmowzie.mowziesmobs.client.model.tools.BlockModelRenderer;
import com.bobmowzie.mowziesmobs.client.model.tools.AdvancedModelRenderer;
import com.ilexiconn.llibrary.client.model.tools.BasicModelRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;

public class BlockLayer<T extends Entity, M extends EntityModel<T>> extends FeatureRenderer<T, M> {
    private final AdvancedModelRenderer root;

    public BlockLayer(FeatureRendererContext<T, M> renderer, AdvancedModelRenderer root) {
        super(renderer);
        this.root = root;
    }

    public static void processModelRenderer(AdvancedModelRenderer modelRenderer, MatrixStack matrixStackIn, VertexConsumerProvider bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha, BlockRenderManager dispatcher) {
        if (modelRenderer.showModel) {
            if (modelRenderer instanceof BlockModelRenderer || !modelRenderer.childModels.isEmpty()) {
                matrixStackIn.push();

                modelRenderer.translateRotate(matrixStackIn);
                if (!modelRenderer.isHidden() && modelRenderer instanceof BlockModelRenderer blockModelRenderer) {
                    dispatcher.renderBlockAsEntity(blockModelRenderer.getBlockState(), matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
                }

                // Render children
                for (BasicModelRenderer child : modelRenderer.childModels) {
                    if (child instanceof AdvancedModelRenderer) {
                        processModelRenderer((AdvancedModelRenderer) child, matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha, dispatcher);
                    }
                }

                matrixStackIn.pop();
            }
        }
    }

    @Override
    public void render(MatrixStack matrixStackIn, VertexConsumerProvider bufferIn, int packedLightIn, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        matrixStackIn.push();
        int packedOverlay = 0;
        if (entity instanceof LivingEntity) LivingEntityRenderer.getOverlay((LivingEntity) entity, 0.0F);
        BlockRenderManager blockrendererdispatcher = MinecraftClient.getInstance().getBlockRenderManager();
        processModelRenderer(this.root, matrixStackIn, bufferIn, packedLightIn, packedOverlay, 1, 1, 1, 1, blockrendererdispatcher);
        matrixStackIn.pop();
    }
}
