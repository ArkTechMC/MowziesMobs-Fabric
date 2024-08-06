package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.client.model.tools.MathUtils;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntityFallingBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class RenderFallingBlock extends EntityRenderer<EntityFallingBlock> {
    public RenderFallingBlock(EntityRendererFactory.Context mgr) {
        super(mgr);
    }

    @Override
    public void render(EntityFallingBlock entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, VertexConsumerProvider bufferIn, int packedLightIn) {
        BlockRenderManager dispatcher = MinecraftClient.getInstance().getBlockRenderManager();
        matrixStackIn.push();
        matrixStackIn.translate(0, 0.5f, 0);
        if (entityIn.getMode() == EntityFallingBlock.EnumFallingBlockMode.MOBILE) {
            matrixStackIn.multiply(MathUtils.quatFromRotationXYZ(0, MathHelper.lerp(partialTicks, entityIn.prevYaw, entityIn.getYaw()), 0, true));
            matrixStackIn.multiply(MathUtils.quatFromRotationXYZ(MathHelper.lerp(partialTicks, entityIn.prevPitch, entityIn.getPitch()), 0, 0, true));
        } else {
            matrixStackIn.translate(0, MathHelper.lerp(partialTicks, entityIn.prevAnimY, entityIn.animY), 0);
            matrixStackIn.translate(0, -1, 0);
        }
        matrixStackIn.translate(-0.5f, -0.5f, -0.5f);
        dispatcher.renderBlockAsEntity(entityIn.getBlock(), matrixStackIn, bufferIn, packedLightIn, OverlayTexture.DEFAULT_UV);
        matrixStackIn.pop();
    }

    @Override
    public Identifier getTexture(EntityFallingBlock entity) {
        return null;
    }
}
