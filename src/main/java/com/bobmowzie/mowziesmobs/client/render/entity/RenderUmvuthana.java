package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.client.model.entity.ModelUmvuthana;
import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieGeoBone;
import com.bobmowzie.mowziesmobs.client.render.entity.layer.GeckoSunblockLayer;
import com.bobmowzie.mowziesmobs.client.render.entity.layer.UmvuthanaArmorLayer;
import com.bobmowzie.mowziesmobs.client.render.entity.layer.UmvuthanaSunLayer;
import com.bobmowzie.mowziesmobs.server.entity.umvuthana.EntityUmvuthana;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3d;

@Environment(EnvType.CLIENT)
public class RenderUmvuthana extends MowzieGeoEntityRenderer<EntityUmvuthana> {
    public RenderUmvuthana(EntityRendererFactory.Context renderManager) {
        super(renderManager, new ModelUmvuthana());
        this.addRenderLayer(new FrozenRenderHandler.GeckoLayerFrozen<>(this, renderManager));
        this.addRenderLayer(new GeckoSunblockLayer<>(this, renderManager));
        this.addRenderLayer(new UmvuthanaArmorLayer(this, renderManager));
        this.addRenderLayer(new UmvuthanaSunLayer(this, renderManager));
        this.shadowRadius = 0.6f;
    }

    @Override
    public void render(EntityUmvuthana entity, float entityYaw, float partialTick, MatrixStack poseStack, VertexConsumerProvider bufferSource, int packedLight) {
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        MowzieGeoBone head = this.getMowzieGeoModel().getMowzieBone("head");
        Vector3d worldPos = head.getWorldPosition();
        if (entity.headPos != null && entity.headPos.length > 0)
            entity.headPos[0] = new Vec3d(worldPos.x, worldPos.y, worldPos.z);

        if (!MinecraftClient.getInstance().isPaused()) {
            MowzieGeoBone mask = this.getMowzieGeoModel().getMowzieBone("maskTwitcher");
            entity.updateRattleSound(mask.getRotZ());
        }
    }

    @Override
    public boolean shouldRender(EntityUmvuthana entity, Frustum p_114492_, double p_114493_, double p_114494_, double p_114495_) {
        boolean result = super.shouldRender(entity, p_114492_, p_114493_, p_114494_, p_114495_);
        if (!result) entity.headPos[0] = entity.getPos().add(0, entity.getStandingEyeHeight(), 0);
        return result;
    }
}
