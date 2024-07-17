package com.bobmowzie.mowziesmobs.client.render.entity.layer;

import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import software.bernie.geckolib.cache.object.GeoBone;

public interface IGeckoRenderLayer {
    default void renderRecursively(GeoBone bone, MatrixStack matrixStack, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {

    }
}
