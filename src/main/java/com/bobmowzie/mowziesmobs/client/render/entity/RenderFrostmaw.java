package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.entity.ModelFrostmaw;
import com.bobmowzie.mowziesmobs.client.render.MowzieRenderUtils;
import com.bobmowzie.mowziesmobs.client.render.entity.layer.ItemLayer;
import com.bobmowzie.mowziesmobs.server.entity.frostmaw.EntityFrostmaw;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

/**
 * Created by BobMowzie on 5/8/2017.
 */
public class RenderFrostmaw extends MobEntityRenderer<EntityFrostmaw, ModelFrostmaw<EntityFrostmaw>> {
    private static final Identifier TEXTURE = new Identifier(MowziesMobs.MODID, "textures/entity/frostmaw.png");

    public RenderFrostmaw(EntityRendererFactory.Context mgr) {
        super(mgr, new ModelFrostmaw<>(), 3.5f);
        this.addFeature(new ItemLayer<>(this, this.getModel().iceCrystalHand, ItemHandler.ICE_CRYSTAL.getDefaultStack(), ModelTransformationMode.GROUND));
        this.addFeature(new ItemLayer<>(this, this.getModel().iceCrystal, ItemHandler.ICE_CRYSTAL.getDefaultStack(), ModelTransformationMode.GROUND));
    }

    @Override
    protected float getLyingAngle(EntityFrostmaw entity) {
        return 0;
    }

    @Override
    public Identifier getTexture(EntityFrostmaw entity) {
        return RenderFrostmaw.TEXTURE;
    }

    @Override
    public void render(EntityFrostmaw entity, float entityYaw, float partialTicks, MatrixStack matrixStackIn, VertexConsumerProvider bufferIn, int packedLightIn) {
        super.render(entity, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
        if (entity.getAnimation() == EntityFrostmaw.SWIPE_ANIMATION || entity.getAnimation() == EntityFrostmaw.SWIPE_TWICE_ANIMATION || entity.getAnimation() == EntityFrostmaw.ICE_BREATH_ANIMATION || entity.getAnimation() == EntityFrostmaw.ICE_BALL_ANIMATION || !entity.getActive()) {
            Vec3d rightHandPos = MowzieRenderUtils.getWorldPosFromModel(entity, entityYaw, this.model.rightHandSocket);
            Vec3d leftHandPos = MowzieRenderUtils.getWorldPosFromModel(entity, entityYaw, this.model.leftHandSocket);
            Vec3d mouthPos = MowzieRenderUtils.getWorldPosFromModel(entity, entityYaw, this.model.mouthSocket);
            entity.setSocketPosArray(0, rightHandPos);
            entity.setSocketPosArray(1, leftHandPos);
            entity.setSocketPosArray(2, mouthPos);
        }
    }
}
