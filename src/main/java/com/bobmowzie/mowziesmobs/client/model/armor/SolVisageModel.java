package com.bobmowzie.mowziesmobs.client.model.armor;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.item.ItemSolVisage;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class SolVisageModel extends GeoModel<ItemSolVisage> {
    @Override
    public Identifier getModelResource(ItemSolVisage object) {
        return new Identifier(MowziesMobs.MODID, "geo/sol_visage.geo.json");
    }

    @Override
    public Identifier getTextureResource(ItemSolVisage object) {
        return new Identifier(MowziesMobs.MODID, "textures/entity/umvuthi.png");
    }

    @Override
    public Identifier getAnimationResource(ItemSolVisage animatable) {
        return new Identifier(MowziesMobs.MODID, "animations/sol_visage.animation.json");
    }

    @Override
    public RenderLayer getRenderType(ItemSolVisage animatable, Identifier texture) {
        return RenderLayer.getEntityCutoutNoCull(texture);
    }
}