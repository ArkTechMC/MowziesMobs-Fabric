package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.entity.ModelUmvuthi;
import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieGeoBone;
import com.bobmowzie.mowziesmobs.client.render.MMRenderType;
import com.bobmowzie.mowziesmobs.client.render.entity.layer.GeckoSunblockLayer;
import com.bobmowzie.mowziesmobs.client.render.entity.layer.UmvuthiSunLayer;
import com.bobmowzie.mowziesmobs.server.entity.umvuthana.EntityUmvuthi;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3d;

@Environment(EnvType.CLIENT)
public class RenderUmvuthi extends MowzieGeoEntityRenderer<EntityUmvuthi> {
    public static final Identifier SUN = new Identifier(MowziesMobs.MODID, "textures/effects/sun_effect.png");
    public static final float BURST_RADIUS = 3.5f;
    public static final int BURST_FRAME_COUNT = 10;
    public static final int BURST_START_FRAME = 12;
    private static final Identifier TEXTURE = new Identifier(MowziesMobs.MODID, "textures/entity/umvuthi.png");
    private VertexConsumerProvider source;
    private EntityUmvuthi entity;

    public RenderUmvuthi(EntityRendererFactory.Context mgr) {
        super(mgr, new ModelUmvuthi());
        this.addRenderLayer(new FrozenRenderHandler.GeckoLayerFrozen<>(this, mgr));
        this.addRenderLayer(new GeckoSunblockLayer(this, mgr));
        this.addRenderLayer(new UmvuthiSunLayer(this));

        this.shadowRadius = 1.0f;
    }

    public static void drawBurst(Matrix4f matrix4f, Matrix3f matrix3f, VertexConsumer builder, float tick, int packedLightIn) {
        int dissapateFrame = 6;
        float firstSpeed = 2f;
        float secondSpeed = 1f;
        int frame = ((int) (tick * firstSpeed) <= dissapateFrame) ? (int) (tick * firstSpeed) : (int) (dissapateFrame + (tick - dissapateFrame / firstSpeed) * secondSpeed);
        if (frame > BURST_FRAME_COUNT) {
            frame = BURST_FRAME_COUNT;
        }
        float minU = 0.0625f * frame;
        float maxU = minU + 0.0625f;
        float minV = 0.5f;
        float maxV = minV + 0.5f;
        float offset = 0.219f * (frame % 2);
        float opacity = (tick < 8) ? 0.8f : 0.4f;
        drawVertex(matrix4f, matrix3f, builder, -BURST_RADIUS + offset, -BURST_RADIUS + offset, 0, minU, minV, opacity, packedLightIn);
        drawVertex(matrix4f, matrix3f, builder, -BURST_RADIUS + offset, BURST_RADIUS + offset, 0, minU, maxV, opacity, packedLightIn);
        drawVertex(matrix4f, matrix3f, builder, BURST_RADIUS + offset, BURST_RADIUS + offset, 0, maxU, maxV, opacity, packedLightIn);
        drawVertex(matrix4f, matrix3f, builder, BURST_RADIUS + offset, -BURST_RADIUS + offset, 0, maxU, minV, opacity, packedLightIn);
    }

    public static void drawVertex(Matrix4f matrix, Matrix3f normals, VertexConsumer vertexBuilder, float offsetX, float offsetY, float offsetZ, float textureX, float textureY, float alpha, int packedLightIn) {
        vertexBuilder.vertex(matrix, offsetX, offsetY, offsetZ).color(1, 1, 1, 1 * alpha).texture(textureX, textureY).overlay(OverlayTexture.DEFAULT_UV).light(packedLightIn).normal(normals, 0.0F, 1.0F, 0.0F).next();
    }

    @Override
    public Identifier getTexture(EntityUmvuthi entity) {
        return this.getMowzieGeoModel().getTextureResource(entity);
    }

    //    @Override
//    public void renderRecursively(GeoBone bone, PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
//        super.renderRecursively(bone, poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
//        if (bone.getName().equals("sun")){
//            Vector3d pos = bone.getWorldPosition();
//            Vec3 vec = new Vec3(pos.x,pos.y,pos.z);
//            if(this.entity!=null){
//                if(this.entity.level.isClientSide && this.entity.getRandom().nextFloat() < 0.5f){
//                    this.entity.level.addParticle(ParticleTypes.FLAME, vec.x, vec.y, vec.z, 0d, 0d, 0d );
//                }
//            }
//        }
//    }

    @Override
    public void render(EntityUmvuthi umvuthi, float entityYaw, float delta, MatrixStack matrixStackIn, VertexConsumerProvider bufferIn, int packedLightIn) {
        this.source = bufferIn;
        this.entity = umvuthi;
        if (!umvuthi.isInvisible()) {
            if (umvuthi.getActiveAbilityType() == EntityUmvuthi.SOLAR_FLARE_ABILITY && umvuthi.getActiveAbility().getTicksInUse() > BURST_START_FRAME && umvuthi.getActiveAbility().getTicksInUse() < BURST_START_FRAME + BURST_FRAME_COUNT - 1) {
                matrixStackIn.push();
                Quaternionf quat = MinecraftClient.getInstance().getEntityRenderDispatcher().getRotation();
                matrixStackIn.multiply(quat);
                matrixStackIn.translate(0, 1, 0);
                matrixStackIn.scale(0.8f, 0.8f, 0.8f);
                VertexConsumer ivertexbuilder = bufferIn.getBuffer(MMRenderType.getSolarFlare(RenderSunstrike.TEXTURE));
                MatrixStack.Entry matrixstack$entry = matrixStackIn.peek();
                Matrix4f matrix4f = matrixstack$entry.getPositionMatrix();
                Matrix3f matrix3f = matrixstack$entry.getNormalMatrix();
                drawBurst(matrix4f, matrix3f, ivertexbuilder, umvuthi.getActiveAbility().getTicksInUse() - BURST_START_FRAME + delta, packedLightIn);
                matrixStackIn.pop();
            }
        }

//        matrixStackIn.pushPose();
//        VertexConsumer ivertexbuilder = bufferIn.getBuffer(MMRenderType.getSolarFlare( new ResourceLocation(MowziesMobs.MODID, "textures/effects/super_nova_8.png")));
//        PoseStack.Pose matrixstack$entry = matrixStackIn.last();
//        matrixStackIn.scale(1.2f,1.2f,1.2f);
//        Matrix4f matrix4f = matrixstack$entry.pose();
//        Matrix3f matrix3f = matrixstack$entry.normal();
//        drawSun(matrix4f, matrix3f, ivertexbuilder, packedLightIn);
//        matrixStackIn.popPose();
        super.render(umvuthi, entityYaw, delta, matrixStackIn, bufferIn, packedLightIn);

        MowzieGeoBone sunRender = this.getMowzieGeoModel().getMowzieBone("sun_render");
        Vector3d sunRenderPos = sunRender.getWorldPosition();
        if (umvuthi.headPos != null && umvuthi.headPos.length > 0)
            umvuthi.headPos[0] = new Vec3d(sunRenderPos.x, sunRenderPos.y, sunRenderPos.z);

        if (umvuthi.getActiveAbilityType() == EntityUmvuthi.SUPERNOVA_ABILITY && umvuthi.betweenHandPos != null && umvuthi.betweenHandPos.length > 0) {
            Vector3d novaRenderPos = this.getMowzieGeoModel().getMowzieBone("superNovaCenter").getWorldPosition();
            // Blend between sun position and animated supernova position
            float blendStart = 4;
            float blendDuration = 4;
            int ticksInUse = umvuthi.getActiveAbility().getTicksInUse();
            if (ticksInUse <= blendDuration + blendStart) {
                Vec3d sunRenderPosVec3 = new Vec3d(sunRenderPos.x, sunRenderPos.y, sunRenderPos.z);
                Vec3d novaRenderPosVec3 = new Vec3d(novaRenderPos.x, novaRenderPos.y, novaRenderPos.z);
                float alpha = (umvuthi.getActiveAbility().getTicksInUse() + delta - blendStart) / (blendDuration);
                alpha = Math.max(0, alpha);
                Vec3d newPos = novaRenderPosVec3.add(sunRenderPosVec3.subtract(novaRenderPosVec3).multiply(1.0 - alpha));
                novaRenderPos.set(newPos.x, newPos.y, newPos.z);
            }
            umvuthi.betweenHandPos[0] = new Vec3d(novaRenderPos.x, novaRenderPos.y, novaRenderPos.z);
        }

        if (!MinecraftClient.getInstance().isPaused()) {
            MowzieGeoBone mask = this.getMowzieGeoModel().getMowzieBone("maskTwitcher");
            umvuthi.updateRattleSound(mask.getRotZ());
        }
    }

    @Override
    public boolean shouldRender(EntityUmvuthi umvuthi, Frustum frustum, double p_114493_, double p_114494_, double p_114495_) {
        boolean result = super.shouldRender(umvuthi, frustum, p_114493_, p_114494_, p_114495_);
        if (!result) umvuthi.headPos[0] = umvuthi.getPos().add(0, umvuthi.getStandingEyeHeight(), 0);
        return result;
    }
}
