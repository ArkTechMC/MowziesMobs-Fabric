package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.render.MMRenderType;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntitySolarBeam;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

@Environment(EnvType.CLIENT)
public class RenderSolarBeam extends EntityRenderer<EntitySolarBeam> {
    private static final Identifier TEXTURE = new Identifier(MowziesMobs.MODID, "textures/effects/solar_beam.png");
    private static final float TEXTURE_WIDTH = 256;
    private static final float TEXTURE_HEIGHT = 32;
    private static final float START_RADIUS = 1.3f;
    private static final float BEAM_RADIUS = 1;
    private boolean clearerView = false;

    public RenderSolarBeam(EntityRendererFactory.Context mgr) {
        super(mgr);
    }

    @Override
    public Identifier getTexture(EntitySolarBeam entity) {
        return RenderSolarBeam.TEXTURE;
    }

    @Override
    public void render(EntitySolarBeam solarBeam, float entityYaw, float delta, MatrixStack matrixStackIn, VertexConsumerProvider bufferIn, int packedLightIn) {
        this.clearerView = solarBeam.caster instanceof PlayerEntity && MinecraftClient.getInstance().player == solarBeam.caster && MinecraftClient.getInstance().options.getPerspective() == Perspective.FIRST_PERSON;

        double collidePosX = solarBeam.prevCollidePosX + (solarBeam.collidePosX - solarBeam.prevCollidePosX) * delta;
        double collidePosY = solarBeam.prevCollidePosY + (solarBeam.collidePosY - solarBeam.prevCollidePosY) * delta;
        double collidePosZ = solarBeam.prevCollidePosZ + (solarBeam.collidePosZ - solarBeam.prevCollidePosZ) * delta;
        double posX = solarBeam.prevX + (solarBeam.getX() - solarBeam.prevX) * delta;
        double posY = solarBeam.prevY + (solarBeam.getY() - solarBeam.prevY) * delta;
        double posZ = solarBeam.prevZ + (solarBeam.getZ() - solarBeam.prevZ) * delta;
        float yaw = solarBeam.prevYaw + (solarBeam.renderYaw - solarBeam.prevYaw) * delta;
        float pitch = solarBeam.prevPitch + (solarBeam.renderPitch - solarBeam.prevPitch) * delta;

        float length = (float) Math.sqrt(Math.pow(collidePosX - posX, 2) + Math.pow(collidePosY - posY, 2) + Math.pow(collidePosZ - posZ, 2));
        int frame = MathHelper.floor((solarBeam.appear.getTimer() - 1 + delta) * 2);
        if (frame < 0) {
            frame = 6;
        }
        VertexConsumer ivertexbuilder = bufferIn.getBuffer(MMRenderType.getGlowingEffect(this.getTexture(solarBeam)));

        this.renderStart(frame, matrixStackIn, ivertexbuilder, packedLightIn);
        this.renderBeam(length, 180f / (float) Math.PI * yaw, 180f / (float) Math.PI * pitch, frame, matrixStackIn, ivertexbuilder, packedLightIn);

        matrixStackIn.push();
        matrixStackIn.translate(collidePosX - posX, collidePosY - posY, collidePosZ - posZ);
        this.renderEnd(frame, solarBeam.blockSide, matrixStackIn, ivertexbuilder, packedLightIn);
        matrixStackIn.pop();
    }

    private void renderFlatQuad(int frame, MatrixStack matrixStackIn, VertexConsumer builder, int packedLightIn) {
        float minU = 0 + 16F / TEXTURE_WIDTH * frame;
        float minV = 0;
        float maxU = minU + 16F / TEXTURE_WIDTH;
        float maxV = minV + 16F / TEXTURE_HEIGHT;
        MatrixStack.Entry matrixstack$entry = matrixStackIn.peek();
        Matrix4f matrix4f = matrixstack$entry.getPositionMatrix();
        Matrix3f matrix3f = matrixstack$entry.getNormalMatrix();
        this.drawVertex(matrix4f, matrix3f, builder, -START_RADIUS, -START_RADIUS, 0, minU, minV, 1, packedLightIn);
        this.drawVertex(matrix4f, matrix3f, builder, -START_RADIUS, START_RADIUS, 0, minU, maxV, 1, packedLightIn);
        this.drawVertex(matrix4f, matrix3f, builder, START_RADIUS, START_RADIUS, 0, maxU, maxV, 1, packedLightIn);
        this.drawVertex(matrix4f, matrix3f, builder, START_RADIUS, -START_RADIUS, 0, maxU, minV, 1, packedLightIn);
    }

    private void renderStart(int frame, MatrixStack matrixStackIn, VertexConsumer builder, int packedLightIn) {
        if (this.clearerView) {
            return;
        }
        matrixStackIn.push();
        Quaternionf quat = this.dispatcher.getRotation();
        matrixStackIn.multiply(quat);
        this.renderFlatQuad(frame, matrixStackIn, builder, packedLightIn);
        matrixStackIn.pop();
    }

    private void renderEnd(int frame, Direction side, MatrixStack matrixStackIn, VertexConsumer builder, int packedLightIn) {
        matrixStackIn.push();
        Quaternionf quat = this.dispatcher.getRotation();
        matrixStackIn.multiply(quat);
        this.renderFlatQuad(frame, matrixStackIn, builder, packedLightIn);
        matrixStackIn.pop();
        if (side == null) {
            return;
        }
        matrixStackIn.push();
        Quaternionf sideQuat = side.getRotationQuaternion();
        sideQuat.mul(MathUtils.quatFromRotationXYZ(90, 0, 0, true));
        matrixStackIn.multiply(sideQuat);
        matrixStackIn.translate(0, 0, -0.01f);
        this.renderFlatQuad(frame, matrixStackIn, builder, packedLightIn);
        matrixStackIn.pop();
    }

    private void drawBeam(float length, int frame, MatrixStack matrixStackIn, VertexConsumer builder, int packedLightIn) {
        float minU = 0;
        float minV = 16 / TEXTURE_HEIGHT + 1 / TEXTURE_HEIGHT * frame;
        float maxU = minU + 20 / TEXTURE_WIDTH;
        float maxV = minV + 1 / TEXTURE_HEIGHT;
        MatrixStack.Entry matrixstack$entry = matrixStackIn.peek();
        Matrix4f matrix4f = matrixstack$entry.getPositionMatrix();
        Matrix3f matrix3f = matrixstack$entry.getNormalMatrix();
        float offset = this.clearerView ? -1 : 0;
        this.drawVertex(matrix4f, matrix3f, builder, -BEAM_RADIUS, offset, 0, minU, minV, 1, packedLightIn);
        this.drawVertex(matrix4f, matrix3f, builder, -BEAM_RADIUS, length, 0, minU, maxV, 1, packedLightIn);
        this.drawVertex(matrix4f, matrix3f, builder, BEAM_RADIUS, length, 0, maxU, maxV, 1, packedLightIn);
        this.drawVertex(matrix4f, matrix3f, builder, BEAM_RADIUS, offset, 0, maxU, minV, 1, packedLightIn);
    }

    private void renderBeam(float length, float yaw, float pitch, int frame, MatrixStack matrixStackIn, VertexConsumer builder, int packedLightIn) {
        matrixStackIn.push();
        matrixStackIn.multiply(MathUtils.quatFromRotationXYZ(90, 0, 0, true));
        matrixStackIn.multiply(MathUtils.quatFromRotationXYZ(0, 0, yaw - 90f, true));
        matrixStackIn.multiply(MathUtils.quatFromRotationXYZ(-pitch, 0, 0, true));
        matrixStackIn.push();
        if (!this.clearerView) {
            matrixStackIn.multiply(MathUtils.quatFromRotationXYZ(0, MinecraftClient.getInstance().gameRenderer.getCamera().getPitch() + 90, 0, true));
        }
        this.drawBeam(length, frame, matrixStackIn, builder, packedLightIn);
        matrixStackIn.pop();

        if (!this.clearerView) {
            matrixStackIn.push();
            matrixStackIn.multiply(MathUtils.quatFromRotationXYZ(0, -MinecraftClient.getInstance().gameRenderer.getCamera().getPitch() - 90, 0, true));
            this.drawBeam(length, frame, matrixStackIn, builder, packedLightIn);
            matrixStackIn.pop();
        }
        matrixStackIn.pop();
    }

    public void drawVertex(Matrix4f matrix, Matrix3f normals, VertexConsumer vertexBuilder, float offsetX, float offsetY, float offsetZ, float textureX, float textureY, float alpha, int packedLightIn) {
        vertexBuilder.vertex(matrix, offsetX, offsetY, offsetZ).color(1, 1, 1, 1 * alpha).texture(textureX, textureY).overlay(OverlayTexture.DEFAULT_UV).light(packedLightIn).normal(normals, 0.0F, 1.0F, 0.0F).next();
    }
}
