package com.bobmowzie.mowziesmobs.client.render.entity.layer;

import com.bobmowzie.mowziesmobs.server.entity.MowzieGeckoEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.BlockAndItemGeoLayer;
import software.bernie.geckolib.util.RenderUtils;

public class GeckoItemlayer<T extends MowzieGeckoEntity> extends BlockAndItemGeoLayer<T> {
    protected Matrix4f dispatchedMat = new Matrix4f();
    protected Matrix4f renderEarlyMat = new Matrix4f();


    private MowzieGeckoEntity entity;
    private final String boneName;
    private final ItemStack renderedItem;

    public GeckoItemlayer(GeoRenderer<T> rendererIn, String boneName, ItemStack renderedItem) {
        super(rendererIn);
        this.boneName = boneName;
        this.renderedItem = renderedItem;
    }

    @Override
    protected ItemStack getStackForBone(GeoBone bone, MowzieGeckoEntity animatable) {
        if (!bone.isHidden() && bone.getName().equals(this.boneName)) {
            return this.renderedItem;
        }
        return null;
    }

    @Override
    protected ModelTransformationMode getTransformTypeForStack(GeoBone bone, ItemStack stack, MowzieGeckoEntity animatable) {
        return switch (bone.getName()) {
            default -> ModelTransformationMode.THIRD_PERSON_RIGHT_HAND;
        };
    }

    @Override
    public void renderForBone(MatrixStack poseStack, T animatable, GeoBone bone, RenderLayer renderType, VertexConsumerProvider bufferSource,
                              VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        ItemStack stack = this.getStackForBone(bone, animatable);
        BlockState blockState = this.getBlockForBone(bone, animatable);

        if (stack == null && blockState == null)
            return;

        poseStack.push();
        RenderUtils.translateToPivotPoint(poseStack, bone);

        if (stack != null)
            this.renderStackForBone(poseStack, bone, stack, animatable, bufferSource, partialTick, packedLight, packedOverlay);

        if (blockState != null)
            this.renderBlockForBone(poseStack, bone, blockState, animatable, bufferSource, partialTick, packedLight, packedOverlay);

        buffer = bufferSource.getBuffer(renderType);

        poseStack.pop();
    }


//    @Override
//    public void render(PoseStack poseStack, T animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
//        for (GeoBone group : bakedModel.topLevelBones()) {
//            renderRecursively(animatable, group, poseStack, bufferSource, packedLight, packedOverlay);
//        }
//    }
//
//    public void renderRecursively(MowzieGeckoEntity entity, GeoBone bone, PoseStack poseStack, MultiBufferSource buffer, int packedLight,
//                                  int packedOverlay) {
//        poseStack.pushPose();
//        RenderUtils.translateMatrixToBone(poseStack, bone);
//        RenderUtils.translateToPivotPoint(poseStack, bone);
//
//        RenderUtils.rotateMatrixAroundBone(poseStack, bone);
//
//        RenderUtils.scaleMatrixForBone(poseStack, bone);
//
//
//        if(bone.getName().equals(this.boneName) && !bone.isHidden()){
//            poseStack.scale(1.5f,1.5f,1.5f);
//            Minecraft.getInstance().getItemRenderer().renderStatic(this.renderedItem, ItemDisplayContext.THIRD_PERSON_RIGHT_HAND,
//                    packedLight, packedOverlay, poseStack, buffer, entity.level(), entity.getId());
//        }
//
//        RenderUtils.translateAwayFromPivotPoint(poseStack, bone);
//
//        if (!bone.isHidden()) {
//            for (GeoBone childBone : bone.getChildBones()) {
//                renderRecursively(entity,childBone, poseStack, buffer, packedLight, packedOverlay);
//            }
//        }
//
//
//
//        poseStack.popPose();
//    }

    public Vec3d getRenderOffset(MowzieGeckoEntity p_114483_, float p_114484_) {
        return Vec3d.ZERO;
    }

}
