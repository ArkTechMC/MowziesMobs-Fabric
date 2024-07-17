package com.bobmowzie.mowziesmobs.client.model.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.entity.effects.geomancy.EntityRockSling;
import net.minecraft.block.Blocks;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

import java.util.Map;
import java.util.TreeMap;

public class ModelRockSling extends GeoModel<EntityRockSling> {
    private static final Identifier TEXTURE_DIRT = new Identifier("textures/block/dirt.png");
    private static final Identifier TEXTURE_STONE = new Identifier("textures/block/stone.png");
    private static final Identifier TEXTURE_SANDSTONE = new Identifier("textures/block/sandstone.png");
    private static final Identifier TEXTURE_CLAY = new Identifier("textures/block/clay.png");
    static Map<String, Identifier> texMap;

    public ModelRockSling() {
        super();
        texMap = new TreeMap<>();
        texMap.put(Blocks.STONE.getTranslationKey(), TEXTURE_STONE);
        texMap.put(Blocks.DIRT.getTranslationKey(), TEXTURE_DIRT);
        texMap.put(Blocks.CLAY.getTranslationKey(), TEXTURE_CLAY);
        texMap.put(Blocks.SANDSTONE.getTranslationKey(), TEXTURE_SANDSTONE);
    }

    @Override
    public Identifier getAnimationResource(EntityRockSling entity) {
        return new Identifier(MowziesMobs.MODID, "animations/rock_sling.animation.json");
    }

    @Override
    public Identifier getModelResource(EntityRockSling entity) {
        return new Identifier(MowziesMobs.MODID, "geo/rock_sling.geo.json");

    }

    @Override
    public Identifier getTextureResource(EntityRockSling entity) {
        if (entity.storedBlock != null) {
            Identifier tex = texMap.get(entity.storedBlock.getBlock().getTranslationKey());
            if (tex != null) return tex;
        }
        return TEXTURE_DIRT;
    }

    @Override
    public RenderLayer getRenderType(EntityRockSling animatable, Identifier texture) {
        return RenderLayer.getEntityCutout(texture);
    }
}
