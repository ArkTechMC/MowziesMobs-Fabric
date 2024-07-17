package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.entity.ModelFoliaath;
import com.bobmowzie.mowziesmobs.server.entity.foliaath.EntityFoliaath;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class RenderFoliaath extends MobEntityRenderer<EntityFoliaath, ModelFoliaath<EntityFoliaath>> {
    private static final Identifier TEXTURE = new Identifier(MowziesMobs.MODID, "textures/entity/foliaath.png");

    public RenderFoliaath(EntityRendererFactory.Context mgr) {
        super(mgr, new ModelFoliaath<>(), 0);
    }

    @Override
    protected float getLyingAngle(EntityFoliaath entity) {
        return 0;
    }

    @Override
    public Identifier getTexture(EntityFoliaath entity) {
        return RenderFoliaath.TEXTURE;
    }

    @Override
    public void render(EntityFoliaath entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, VertexConsumerProvider bufferIn, int packedLightIn) {
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }
}
