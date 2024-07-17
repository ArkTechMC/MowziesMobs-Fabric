package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.entity.ModelIceBall;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntityIceBall;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

public class RenderIceBall extends EntityRenderer<EntityIceBall> {
    public static final Identifier TEXTURE = new Identifier(MowziesMobs.MODID, "textures/effects/ice_ball.png");
    public ModelIceBall model;

    public RenderIceBall(EntityRendererFactory.Context mgr) {
        super(mgr);
        this.model = new ModelIceBall();
    }

    @Override
    public Identifier getTexture(EntityIceBall entity) {
        return TEXTURE;
    }

    @Override
    public void render(EntityIceBall entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, VertexConsumerProvider bufferIn, int packedLightIn) {
        matrixStackIn.push();
        matrixStackIn.multiply(RotationAxis.NEGATIVE_Y.rotation(entityYaw * (float) Math.PI / 180f));
        matrixStackIn.translate(0, -0.5f, 0);
        VertexConsumer ivertexbuilder = bufferIn.getBuffer(RenderLayer.getEntityTranslucent(TEXTURE));
        this.model.setAngles(entityIn, 0, 0, entityIn.age + partialTicks, 0, 0);
        this.model.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.DEFAULT_UV, 1, 1, 1, 1);
        matrixStackIn.pop();
    }
}
