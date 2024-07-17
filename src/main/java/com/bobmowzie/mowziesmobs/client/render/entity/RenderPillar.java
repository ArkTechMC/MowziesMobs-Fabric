package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.client.model.entity.ModelPillar;
import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieGeoBone;
import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieGeoModel;
import com.bobmowzie.mowziesmobs.server.entity.effects.geomancy.EntityGeomancyBase;
import com.bobmowzie.mowziesmobs.server.entity.effects.geomancy.EntityPillar;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.cache.object.GeoCube;
import software.bernie.geckolib.util.RenderUtils;

@Environment(EnvType.CLIENT)
public class RenderPillar extends RenderGeomancyBase<EntityPillar> {
    private static final Identifier TEXTURE_DIRT = new Identifier("textures/blocks/dirt.png");

    public RenderPillar(EntityRendererFactory.Context mgr) {
        super(mgr, new ModelPillar());
    }

    @Override
    public Identifier getTexture(EntityPillar entity) {
        return TEXTURE_DIRT;
    }

    @Override
    public void preRender(MatrixStack poseStack, EntityPillar animatable, BakedGeoModel model, VertexConsumerProvider bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
        EntityGeomancyBase.GeomancyTier tier = this.getEntity().getTier();
        MowzieGeoModel<EntityPillar> mowzieModel = (MowzieGeoModel<EntityPillar>) this.getGeoModel();
        MowzieGeoBone tier1 = mowzieModel.getMowzieBone("tier1");
        MowzieGeoBone tier2 = mowzieModel.getMowzieBone("tier2");
        MowzieGeoBone tier3 = mowzieModel.getMowzieBone("tier3");
        MowzieGeoBone tier4 = mowzieModel.getMowzieBone("tier4");
        MowzieGeoBone tier5 = mowzieModel.getMowzieBone("tier5");
        tier1.setChildrenHidden(true);
        tier2.setChildrenHidden(true);
        tier3.setChildrenHidden(true);
        tier4.setChildrenHidden(true);
        tier5.setChildrenHidden(true);
        if (tier == EntityGeomancyBase.GeomancyTier.NONE) tier1.setChildrenHidden(false);
        else if (tier == EntityGeomancyBase.GeomancyTier.SMALL) tier2.setChildrenHidden(false);
        else if (tier == EntityGeomancyBase.GeomancyTier.MEDIUM) tier3.setChildrenHidden(false);
        else if (tier == EntityGeomancyBase.GeomancyTier.LARGE) tier4.setChildrenHidden(false);
        else tier5.setChildrenHidden(false);
    }

    @Override
    public void render(EntityPillar pillar, float entityYaw, float partialTick, MatrixStack poseStack, VertexConsumerProvider bufferSource, int packedLight) {
        float height = pillar.prevPrevHeight + (pillar.prevHeight - pillar.prevPrevHeight) * partialTick;
        poseStack.translate(0, height - 0.5f, 0);

        int numRenders = (int) Math.ceil(pillar.getHeight()) + 1;
        for (int i = 0; i < numRenders; i++) {
            poseStack.translate(0, -1, 0);
            super.render(pillar, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        }
    }

    @Override
    public void renderRecursively(MatrixStack poseStack, EntityPillar animatable, GeoBone bone, RenderLayer renderType, VertexConsumerProvider bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        poseStack.push();
        RenderUtils.translateMatrixToBone(poseStack, bone);
        RenderUtils.translateToPivotPoint(poseStack, bone);
        RenderUtils.rotateMatrixAroundBone(poseStack, bone);
        RenderUtils.scaleMatrixForBone(poseStack, bone);
        this.renderCubesOfBone(poseStack, bone, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        this.renderChildBones(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
        poseStack.pop();
    }

    @Override
    public void renderCube(MatrixStack poseStack, GeoCube cube, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        poseStack.translate(-0.5, 0.5f, -0.5);
        BlockRenderManager blockrendererdispatcher = MinecraftClient.getInstance().getBlockRenderManager();
        blockrendererdispatcher.renderBlockAsEntity(this.getEntity().getBlock(), poseStack, this.getCurrentMultiBufferSource(), packedLight, packedOverlay);
    }
}
