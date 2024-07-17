package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.client.model.entity.ModelBoulder;
import com.bobmowzie.mowziesmobs.client.render.entity.layer.BlockLayer;
import com.bobmowzie.mowziesmobs.server.entity.effects.geomancy.EntityBoulderBase;
import com.bobmowzie.mowziesmobs.server.entity.effects.geomancy.EntityGeomancyBase;
import com.bobmowzie.mowziesmobs.client.model.tools.AdvancedModelRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

import java.util.Map;
import java.util.TreeMap;

@Environment(EnvType.CLIENT)
public class RenderBoulder extends EntityRenderer<EntityBoulderBase> {
    private static final Identifier TEXTURE_DIRT = new Identifier("textures/blocks/dirt.png");
    private static final Identifier TEXTURE_STONE = new Identifier("textures/blocks/stone.png");
    private static final Identifier TEXTURE_SANDSTONE = new Identifier("textures/blocks/sandstone.png");
    private static final Identifier TEXTURE_CLAY = new Identifier("textures/blocks/clay.png");
    Map<String, Identifier> texMap;

    ModelBoulder model;

    public RenderBoulder(EntityRendererFactory.Context mgr) {
        super(mgr);
        this.model = new ModelBoulder();
        this.texMap = new TreeMap<String, Identifier>();
        this.texMap.put(Blocks.STONE.getTranslationKey(), TEXTURE_STONE);
        this.texMap.put(Blocks.DIRT.getTranslationKey(), TEXTURE_DIRT);
        this.texMap.put(Blocks.CLAY.getTranslationKey(), TEXTURE_CLAY);
        this.texMap.put(Blocks.SANDSTONE.getTranslationKey(), TEXTURE_SANDSTONE);
    }

    @Override
    public Identifier getTexture(EntityBoulderBase entity) {
//        if (entity.storedBlock != null) {
//            return Minecraft.getInstance().getBlockRendererDispatcher().getModelForState(entity.storedBlock).;
//        }
//        else return TEXTURE_DIRT;
        if (entity.storedBlock != null) {
            Identifier tex = this.texMap.get(entity.storedBlock.getBlock().getTranslationKey());
            if (tex != null) return tex;
        }
        return TEXTURE_DIRT;
    }

    @Override
    public void render(EntityBoulderBase entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, VertexConsumerProvider bufferIn, int packedLightIn) {
        if (entityIn.active) {
            matrixStackIn.push();
            this.model.setAngles(entityIn, 0, 0, entityIn.risingTick + partialTicks, 0, 0);
            BlockRenderManager blockrendererdispatcher = MinecraftClient.getInstance().getBlockRenderManager();
            AdvancedModelRenderer root;
            if (entityIn.boulderSize == EntityGeomancyBase.GeomancyTier.SMALL) root = this.model.boulder0block1;
            else if (entityIn.boulderSize == EntityGeomancyBase.GeomancyTier.MEDIUM) root = this.model.boulder1;
            else if (entityIn.boulderSize == EntityGeomancyBase.GeomancyTier.LARGE) root = this.model.boulder2;
            else root = this.model.boulder3;
            matrixStackIn.translate(-0.5f, 0.5f, -0.5f);
            BlockLayer.processModelRenderer(root, matrixStackIn, bufferIn, packedLightIn, OverlayTexture.DEFAULT_UV, 1, 1, 1, 1, blockrendererdispatcher);
            matrixStackIn.pop();
        }
    }
}
