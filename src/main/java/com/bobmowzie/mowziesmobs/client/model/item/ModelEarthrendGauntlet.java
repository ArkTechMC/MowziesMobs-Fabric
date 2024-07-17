package com.bobmowzie.mowziesmobs.client.model.item;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.item.ItemEarthrendGauntlet;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class ModelEarthrendGauntlet extends GeoModel<ItemEarthrendGauntlet> {

    @Override
    public Identifier getModelResource(ItemEarthrendGauntlet object) {
        return new Identifier(MowziesMobs.MODID, "geo/earthrend_gauntlet.geo.json");
    }

    @Override
    public Identifier getTextureResource(ItemEarthrendGauntlet object) {
        return new Identifier(MowziesMobs.MODID, "textures/item/earthrend_gauntlet.png");
    }

    @Override
    public Identifier getAnimationResource(ItemEarthrendGauntlet animatable) {
        return new Identifier(MowziesMobs.MODID, "animations/earthrend_gauntlet.animation.json");
    }
}