package com.bobmowzie.mowziesmobs.client.model.item;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.item.ItemSculptorStaff;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class ModelSculptorStaff extends GeoModel<ItemSculptorStaff> {

    @Override
    public Identifier getModelResource(ItemSculptorStaff object) {
        return new Identifier(MowziesMobs.MODID, "geo/sculptor_staff.geo.json");
    }

    @Override
    public Identifier getTextureResource(ItemSculptorStaff object) {
        return new Identifier(MowziesMobs.MODID, "textures/item/sculptor_staff.png");
    }

    @Override
    public Identifier getAnimationResource(ItemSculptorStaff animatable) {
        return new Identifier(MowziesMobs.MODID, "animations/sculptor_staff.animation.json");
    }
}