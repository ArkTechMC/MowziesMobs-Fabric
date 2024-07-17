package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.entity.ModelPoisonBall;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntityPoisonBall;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

public class RenderPoisonBall extends EntityRenderer<EntityPoisonBall> {
    public static final Identifier TEXTURE = new Identifier(MowziesMobs.MODID, "textures/effects/poison_ball.png");
    public ModelPoisonBall model;

    public RenderPoisonBall(EntityRendererFactory.Context mgr) {
        super(mgr);
        this.model = new ModelPoisonBall();
    }

    @Override
    public Identifier getTexture(EntityPoisonBall entity) {
        return TEXTURE;
    }

    @Override
    public void render(EntityPoisonBall entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, VertexConsumerProvider bufferIn, int packedLightIn) {
        matrixStackIn.push();
        matrixStackIn.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(entityYaw));
        VertexConsumer ivertexbuilder = bufferIn.getBuffer(RenderLayer.getEntityTranslucent(this.getTexture(entityIn)));
        this.model.setAngles(entityIn, 0, 0, entityIn.age + partialTicks, 0, 0);
        this.model.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.DEFAULT_UV, 1, 1, 1, 1);
        matrixStackIn.pop();
    }
}
