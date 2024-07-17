package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.entity.ModelAxeAttack;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntityAxeAttack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.joml.AxisAngle4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

@Environment(EnvType.CLIENT)
public class RenderAxeAttack extends EntityRenderer<EntityAxeAttack> {
    public static Identifier TEXTURE = new Identifier(MowziesMobs.MODID, "textures/entity/wroughtnaut.png");

    ModelAxeAttack model;

    public RenderAxeAttack(EntityRendererFactory.Context mgr) {
        super(mgr);
        this.model = new ModelAxeAttack();
    }

    @Override
    public Identifier getTexture(EntityAxeAttack entity) {
        return TEXTURE;
    }

    @Override
    public void render(EntityAxeAttack axe, float entityYaw, float delta, MatrixStack matrixStackIn, VertexConsumerProvider bufferIn, int packedLightIn) {
        if (!ConfigHandler.CLIENT.customPlayerAnims) {
            PlayerEntity player = MinecraftClient.getInstance().player;
            if (player != null && player == axe.getCaster()) {
                matrixStackIn.push();
                Vec3d prevAxePos = new Vec3d(axe.lastRenderX, axe.lastRenderY, axe.lastRenderZ);
                Vec3d prevPlayerPos = new Vec3d(player.lastRenderX, player.lastRenderY, player.lastRenderZ);
                Vec3d axePos = prevAxePos.add(axe.getPos().subtract(prevAxePos).multiply(delta));
                Vec3d playerPos = prevPlayerPos.add(player.getPos().subtract(prevPlayerPos).multiply(delta));
                Vec3d deltaPos = axePos.subtract(playerPos).multiply(-1);
                matrixStackIn.translate(deltaPos.getX(), deltaPos.getY(), deltaPos.getZ());
                matrixStackIn.multiply(new Quaternionf(new AxisAngle4f(player.getYaw() * (float) Math.PI / 180f, new Vector3f(0, -1, 0))));
                VertexConsumer ivertexbuilder = bufferIn.getBuffer(RenderLayer.getEntitySolid(TEXTURE));
                this.model.setAngles(axe, 0, 0, axe.age + delta, 0, 0);
                this.model.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.DEFAULT_UV, 1, 1, 1, 1);
                matrixStackIn.pop();
            }
        }
    }
}
