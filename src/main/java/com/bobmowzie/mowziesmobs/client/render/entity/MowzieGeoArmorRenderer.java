package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.client.model.tools.MathUtils;
import com.bobmowzie.mowziesmobs.client.model.tools.ModelPartMatrix;
import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieGeoBone;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ArmorItem;
import org.joml.Matrix4f;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.util.RenderUtils;

public class MowzieGeoArmorRenderer<T extends ArmorItem & GeoItem> extends GeoArmorRenderer<T> {
    public boolean usingCustomPlayerAnimations = false;

    public MowzieGeoArmorRenderer(GeoModel<T> modelProvider) {
        super(modelProvider);
    }

    @Override
    public void render(MatrixStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        this.usingCustomPlayerAnimations = false;
    }

    @Override
    public void renderRecursively(MatrixStack poseStack, T animatable, GeoBone bone, RenderLayer renderType, VertexConsumerProvider bufferIn, VertexConsumer buffer, boolean isReRender, float partialTick,
                                  int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        poseStack.push();
        if (this.usingCustomPlayerAnimations && bone instanceof MowzieGeoBone && ((MowzieGeoBone) bone).isForceMatrixTransform()) {
            MatrixStack.Entry last = poseStack.peek();
            last.getPositionMatrix().identity();
            last.getNormalMatrix().identity();
            Matrix4f matrix4f = bone.getWorldSpaceMatrix();
            last.getPositionMatrix().mul(matrix4f);
            last.getNormalMatrix().mul(bone.getWorldSpaceNormal());
            poseStack.multiply(MathUtils.quatFromRotationXYZ(0, 0, 180, true));
            poseStack.translate(0, -1.5, 0);
        } else {
            RenderUtils.prepMatrixForBone(poseStack, bone);
        }
        this.renderCubesOfBone(poseStack, bone, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        this.renderChildBones(poseStack, animatable, bone, renderType, bufferIn, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
        poseStack.pop();
//        super.renderRecursively(poseStack, animatable, bone, renderType, bufferIn, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }

    public void copyFrom(ModelPart modelPart, GeoBone geoBone, float offsetX, float offsetY, float offsetZ) {
        if (this.usingCustomPlayerAnimations && modelPart instanceof ModelPartMatrix other && geoBone instanceof MowzieGeoBone thisBone) {
            thisBone.setWorldSpaceNormal(other.getWorldNormal());
            thisBone.setWorldSpaceMatrix(other.getWorldXform());
            thisBone.setForceMatrixTransform(true);
        } else {
            RenderUtils.matchModelPartRot(modelPart, geoBone);
            geoBone.updatePosition(modelPart.pivotX + offsetX, -modelPart.pivotY + offsetY, modelPart.pivotZ + offsetZ);
        }
    }

    @Override
    protected void applyBaseTransformations(BipedEntityModel<?> baseModel) {
        if (this.getHeadBone() != null) {
            ModelPart headPart = baseModel.head;
            this.copyFrom(headPart, this.getHeadBone(), 0, 0, 0);
        }

        if (this.getBodyBone() != null) {
            ModelPart bodyPart = baseModel.body;
            this.copyFrom(bodyPart, this.getBodyBone(), 0, 0, 0);
        }

        if (this.getRightArmBone() != null) {
            ModelPart rightArmPart = baseModel.rightArm;
            this.copyFrom(rightArmPart, this.getRightArmBone(), 5, 2, 0);
        }

        if (this.getLeftArmBone() != null) {
            ModelPart leftArmPart = baseModel.leftArm;
            this.copyFrom(leftArmPart, this.getLeftArmBone(), -5, 2, 0);
        }

        if (this.getRightLegBone() != null) {
            ModelPart rightLegPart = baseModel.rightLeg;
            this.copyFrom(rightLegPart, this.getRightLegBone(), 2, 12, 0);

            if (this.getRightBootBone() != null) {
                this.copyFrom(rightLegPart, this.getRightBootBone(), 2, 12, 0);
            }
        }

        if (this.getLeftLegBone() != null) {
            ModelPart leftLegPart = baseModel.leftLeg;
            this.copyFrom(leftLegPart, this.getLeftLegBone(), -2, 12, 0);

            if (this.getLeftBootBone() != null) {
                this.copyFrom(leftLegPart, this.getLeftBootBone(), -2, 12, 0);
            }
        }
    }
}
