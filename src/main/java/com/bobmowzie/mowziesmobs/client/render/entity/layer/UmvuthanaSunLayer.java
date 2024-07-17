package com.bobmowzie.mowziesmobs.client.render.entity.layer;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.ability.AbilitySection;
import com.bobmowzie.mowziesmobs.server.entity.umvuthana.EntityUmvuthana;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;
import software.bernie.geckolib.util.RenderUtils;

public class UmvuthanaSunLayer extends GeoRenderLayer<EntityUmvuthana> {
    protected final EntityRenderDispatcher entityRenderDispatcher;

    public UmvuthanaSunLayer(GeoRenderer<EntityUmvuthana> entityRendererIn, EntityRendererFactory.Context context) {
        super(entityRendererIn);
        this.entityRenderDispatcher = context.getRenderDispatcher();
    }

    @Override
    public void renderForBone(MatrixStack poseStack, EntityUmvuthana animatable, GeoBone bone, RenderLayer renderType, VertexConsumerProvider bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        super.renderForBone(poseStack, animatable, bone, renderType, bufferSource, buffer, partialTick, packedLight, packedOverlay);
        if (animatable.deathTime < 27 && animatable.active && !(animatable.getActiveAbilityType() == EntityUmvuthana.TELEPORT_ABILITY && animatable.getActiveAbility().getCurrentSection().sectionType != AbilitySection.AbilitySectionType.RECOVERY)) {
            if (bone.isHidden()) return;
            poseStack.push();
            RenderUtils.translateToPivotPoint(poseStack, bone);
            String boneName = "head";
            if (bone.getName().equals(boneName)) {
                MatrixStack.Entry matrixstack$entry = poseStack.peek();
                Matrix4f matrix4f = matrixstack$entry.getPositionMatrix();
                Vector4f vecTranslation = new Vector4f(0, 0, 0, 1);
                vecTranslation.mul(matrix4f);
                MatrixStack newPoseStack = new MatrixStack();
                newPoseStack.translate(vecTranslation.x(), vecTranslation.y(), vecTranslation.z());
                Vector4f vecScale = new Vector4f(1, 0, 0, 1);
                vecScale.mul(matrix4f);
                float scale = (float) new Vec3d(vecScale.x() - vecTranslation.x(), vecScale.y() - vecTranslation.y(), vecScale.z() - vecTranslation.z()).length();
                newPoseStack.scale(scale, scale, scale);
                VertexConsumer ivertexbuilder = bufferSource.getBuffer(RenderLayer.getEntityTranslucent(new Identifier(MowziesMobs.MODID, "textures/particle/sun_no_glow.png"), true));
                MatrixStack.Entry matrixstack$entry2 = newPoseStack.peek();
                Matrix4f matrix4f2 = matrixstack$entry2.getPositionMatrix();
                Matrix3f matrix3f = matrixstack$entry.getNormalMatrix();
                this.drawSun(matrix4f2, matrix3f, ivertexbuilder, packedLight, animatable.age + partialTick);
            }
            bufferSource.getBuffer(renderType);
            poseStack.pop();
        }
    }

    private void drawSun(Matrix4f matrix4f, Matrix3f matrix3f, VertexConsumer builder, int packedLightIn, float time) {
        float sunRadius = 1.2f + (float) Math.sin(time * 4) * 0.085f;
        this.drawVertex(matrix4f, matrix3f, builder, -sunRadius, -sunRadius, 0, 0, 0, 1, packedLightIn);
        this.drawVertex(matrix4f, matrix3f, builder, -sunRadius, sunRadius, 0, 0, 1, 1, packedLightIn);
        this.drawVertex(matrix4f, matrix3f, builder, sunRadius, sunRadius, 0, 1, 1, 1, packedLightIn);
        this.drawVertex(matrix4f, matrix3f, builder, sunRadius, -sunRadius, 0, 1, 0, 1, packedLightIn);
    }

    public void drawVertex(Matrix4f matrix, Matrix3f normals, VertexConsumer vertexBuilder, float offsetX, float offsetY, float offsetZ, float textureX, float textureY, float alpha, int packedLightIn) {
        vertexBuilder.vertex(matrix, offsetX, offsetY, offsetZ).color(1f, 1f, 1f, 1.0f).texture(textureX, textureY).overlay(OverlayTexture.DEFAULT_UV).light(15728880).normal(normals, 1.0F, 1.0F, 1.0F).next();
    }

}
