package com.bobmowzie.mowziesmobs.client.model.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieGeoModel;
import com.bobmowzie.mowziesmobs.server.entity.effects.geomancy.EntityPillar;
import net.minecraft.util.Identifier;

public class ModelPillar extends MowzieGeoModel<EntityPillar> {
    public ModelPillar() {
        super();
    }

    @Override
    public Identifier getModelResource(EntityPillar object) {
        return new Identifier(MowziesMobs.MODID, "geo/geomancy_pillar.geo.json");
    }

    @Override
    public Identifier getTextureResource(EntityPillar object) {
        return null;
    }

    @Override
    public Identifier getAnimationResource(EntityPillar object) {
        return null;
    }
}