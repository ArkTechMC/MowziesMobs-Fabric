package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.entity.ModelNaga;
import com.bobmowzie.mowziesmobs.client.render.MowzieRenderUtils;
import com.bobmowzie.mowziesmobs.server.entity.naga.EntityNaga;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class RenderNaga extends MobEntityRenderer<EntityNaga, ModelNaga<EntityNaga>> {
    private static final Identifier TEXTURE = new Identifier(MowziesMobs.MODID, "textures/entity/naga.png");

    public RenderNaga(EntityRendererFactory.Context mgr) {
        super(mgr, new ModelNaga<>(), 0);
    }

    @Override
    protected float getLyingAngle(EntityNaga entity) {
        return 0;
    }

    @Override
    public Identifier getTexture(EntityNaga entity) {
        return TEXTURE;
    }

    @Override
    public void render(EntityNaga entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, VertexConsumerProvider bufferIn, int packedLightIn) {
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
        if (entityIn.getAnimation() == EntityNaga.SPIT_ANIMATION && entityIn.mouthPos != null && entityIn.mouthPos.length > 0)
            entityIn.mouthPos[0] = MowzieRenderUtils.getWorldPosFromModel(entityIn, entityYaw, this.getModel().mouthSocket);
    }
}