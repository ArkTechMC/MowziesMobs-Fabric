package com.bobmowzie.mowziesmobs.client.render.entity.layer;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.entity.umvuthana.EntityUmvuthi;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;
import software.bernie.geckolib.util.RenderUtils;

public class UmvuthiSunLayer extends GeoRenderLayer<EntityUmvuthi> {
    private final Vec3d v1 = new Vec3d(-2, 1, -1);
    private final Vec3d v2 = new Vec3d(0, 1, -1);
    private final Vec3d v3 = new Vec3d(-1, 0, 1);
    private final Vec3d v4 = new Vec3d(-1, 0, -3);
    private final Vec3d v5 = new Vec3d(-3, -1, 0);
    private final Vec3d v6 = new Vec3d(-3, -1, -2);
    private final Vec3d v7 = new Vec3d(1, -1, 0);
    private final Vec3d v8 = new Vec3d(1, -1, -2);
    private final Vec3d v9 = new Vec3d(0, -3, -1);
    private final Vec3d v10 = new Vec3d(-2, -3, -1);
    private final Vec3d v11 = new Vec3d(-1, -2, 1);
    private final Vec3d v12 = new Vec3d(-1, -2, -3);
    private final Vec3d[] POS = {
            // Face 1
            this.v1,
            this.v2,
            this.v3,
            this.v1,
            // Face 2
            this.v1,
            this.v2,
            this.v4,
            this.v1,
            // Face 3
            this.v1,
            this.v5,
            this.v6,
            this.v1,
            // Face 4
            this.v2,
            this.v7,
            this.v8,
            this.v2,
            // Face 5
            this.v2,
            this.v8,
            this.v4,
            this.v2,
            // Face 6
            this.v1,
            this.v4,
            this.v6,
            this.v1,
            // Face 7
            this.v1,
            this.v5,
            this.v3,
            this.v1,
            // Face 8
            this.v2,
            this.v3,
            this.v7,
            this.v2,
            // Face 9
            this.v9,
            this.v7,
            this.v8,
            this.v9,
            // Face 10
            this.v5,
            this.v6,
            this.v10,
            this.v5,
            // Face 11
            this.v9,
            this.v10,
            this.v11,
            this.v9,
            // Face 12
            this.v9,
            this.v10,
            this.v12,
            this.v9,
            // Face 13
            this.v4,
            this.v6,
            this.v12,
            this.v4,
            // Face 14
            this.v4,
            this.v8,
            this.v12,
            this.v4,
            // Face 15
            this.v3,
            this.v5,
            this.v11,
            this.v3,
            // Face 16
            this.v3,
            this.v7,
            this.v11,
            this.v3,
            // Face 17
            this.v5,
            this.v10,
            this.v11,
            this.v5,
            // Face 18
            this.v7,
            this.v9,
            this.v11,
            this.v7,
            // Face 19
            this.v8,
            this.v9,
            this.v12,
            this.v8,
            // Face 20
            this.v6,
            this.v10,
            this.v12,
            this.v6,
    };
    protected Matrix4f dispatchedMat = new Matrix4f();
    protected Matrix4f renderEarlyMat = new Matrix4f();

    public UmvuthiSunLayer(GeoRenderer<EntityUmvuthi> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void renderForBone(MatrixStack poseStack, EntityUmvuthi umvuthi, GeoBone bone, RenderLayer renderType, VertexConsumerProvider bufferSource, VertexConsumer buffer, float partialTicks, int packedLight, int packedOverlay) {
        super.renderForBone(poseStack, umvuthi, bone, renderType, bufferSource, buffer, partialTicks, packedLight, packedOverlay);
        if (umvuthi.shouldRenderSun()) {
            if (bone.isHidden()) return;
            poseStack.push();
            RenderUtils.translateToPivotPoint(poseStack, bone);
            if (bone.getName().equals("sun_render")) {
                poseStack.translate(0.06d, 0d, -0.0d);
                poseStack.scale(0.06f, 0.06f, 0.06f);
                VertexConsumer ivertexbuilder = bufferSource.getBuffer(RenderLayer.getEntityTranslucent(new Identifier(MowziesMobs.MODID, "textures/effects/sun_effect.png"), true));
                MatrixStack.Entry matrixstack$entry = poseStack.peek();
                Matrix4f matrix4f = matrixstack$entry.getPositionMatrix();
                Matrix3f matrix3f = matrixstack$entry.getNormalMatrix();

                // Blend the sun back to full size after supernova
                float scaleMult = 1f;
                if (umvuthi.getActiveAbilityType() == EntityUmvuthi.SUPERNOVA_ABILITY && umvuthi.getActiveAbility().getTicksInUse() > 90) {
                    scaleMult = (umvuthi.getActiveAbility().getTicksInUse() + partialTicks - 90f) / 10f;
                    scaleMult = MathHelper.clamp(scaleMult, 0f, 1f);
                }

                this.drawSun(matrix4f, matrix3f, ivertexbuilder, umvuthi.age + partialTicks, scaleMult);
            }
            bufferSource.getBuffer(renderType);
            poseStack.pop();
        }
    }

    @Override
    public void render(MatrixStack poseStack, EntityUmvuthi entityLivingBaseIn, BakedGeoModel bakedModel, RenderLayer renderType, VertexConsumerProvider bufferIn, VertexConsumer buffer, float partialTicks, int packedLight, int packedOverlay) {

    }

    private void drawSun(Matrix4f matrix4f, Matrix3f matrix3f, VertexConsumer builder, float time, float scaleMultiplier) {
        float scale = (0.9f + (float) Math.sin(time * 4) * 0.07f) * scaleMultiplier;
        for (int i = 0; i < 4; i++) {
            for (Vec3d vec : this.POS) {
                vec = vec.multiply(1f + (scale * i), 1f + (scale * i), 1f + (scale * i));
                builder.vertex(matrix4f, (float) vec.x + (scale * i), (float) vec.y + (scale * i), (float) vec.z + (scale * i))
                        .color(1f, 1f, .4f, 0.2f)
                        .texture(0.0f, 0.5f)
                        .overlay(OverlayTexture.DEFAULT_UV)
                        .light(15728880)
                        .normal(matrix3f, 1f, 1f, 1f)
                        .next();
            }
        }
        for (Vec3d vec : this.POS) {
            builder.vertex(matrix4f, (float) vec.x * 1.2f * scaleMultiplier, (float) vec.y * 1.2f * scaleMultiplier, (float) vec.z * 1.2f * scaleMultiplier)
                    .color(1f, 1f, 1f, 1f)
                    .texture(0.0f, 0.5f)
                    .overlay(OverlayTexture.DEFAULT_UV)
                    .light(15728880)
                    .normal(matrix3f, 1f, 1f, 1f)
                    .next();
        }
    }
}
