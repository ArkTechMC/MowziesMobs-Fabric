package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieGeoBone;
import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieGeoModel;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.util.RenderUtils;

public abstract class MowzieGeoEntityRenderer<T extends LivingEntity & GeoEntity> extends GeoEntityRenderer<T> {

    protected MowzieGeoEntityRenderer(EntityRendererFactory.Context renderManager, GeoModel<T> modelProvider) {
        super(renderManager, modelProvider);
    }

    @Override
    public RenderLayer getRenderType(T animatable, Identifier texture, @Nullable VertexConsumerProvider bufferSource, float partialTick) {
        return RenderLayer.getEntityCutoutNoCull(texture);
    }

    public MowzieGeoModel<T> getMowzieGeoModel() {
        return (MowzieGeoModel<T>) super.getGeoModel();
    }

    @Override
    protected float getDeathMaxRotation(T animatable) {
        return 0;
    }

    @Override
    public void renderRecursively(MatrixStack poseStack, T animatable, GeoBone bone, RenderLayer renderType, VertexConsumerProvider bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight,
                                  int packedOverlay, float red, float green, float blue, float alpha) {
        poseStack.push();
        RenderUtils.translateMatrixToBone(poseStack, bone);
        RenderUtils.translateToPivotPoint(poseStack, bone);

        boolean rotOverride = false;
        if (bone instanceof MowzieGeoBone) {
            rotOverride = ((MowzieGeoBone) bone).rotMat != null;
        }

        if (rotOverride) {
            MowzieGeoBone mowzieGeoBone = (MowzieGeoBone) bone;
            poseStack.peek().getPositionMatrix().mul(mowzieGeoBone.rotMat);
            poseStack.peek().getNormalMatrix().mul(new Matrix3f(mowzieGeoBone.rotMat));
        } else {
            RenderUtils.rotateMatrixAroundBone(poseStack, bone);
        }

        RenderUtils.scaleMatrixForBone(poseStack, bone);

        if (bone.isTrackingMatrices()) {
            Matrix4f poseState = new Matrix4f(poseStack.peek().getPositionMatrix());
            Matrix4f localMatrix = RenderUtils.invertAndMultiplyMatrices(poseState, this.entityRenderTranslations);

            bone.setModelSpaceMatrix(RenderUtils.invertAndMultiplyMatrices(poseState, this.modelRenderTranslations));
            bone.setLocalSpaceMatrix(RenderUtils.translateMatrix(localMatrix, this.getPositionOffset(this.animatable, 1).toVector3f()));
            bone.setWorldSpaceMatrix(RenderUtils.translateMatrix(new Matrix4f(localMatrix), this.animatable.getPos().toVector3f()));
        }

        RenderUtils.translateAwayFromPivotPoint(poseStack, bone);

        this.renderCubesOfBone(poseStack, bone, buffer, packedLight, packedOverlay, red, green, blue, alpha);

        if (!isReRender)
            this.applyRenderLayersForBone(poseStack, animatable, bone, renderType, bufferSource, buffer, partialTick, packedLight, packedOverlay);

        this.renderChildBones(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);

        poseStack.pop();
    }
}
