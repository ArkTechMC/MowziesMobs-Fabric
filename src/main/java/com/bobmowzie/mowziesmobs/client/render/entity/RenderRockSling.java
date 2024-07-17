package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.client.model.entity.ModelRockSling;
import com.bobmowzie.mowziesmobs.server.entity.effects.geomancy.EntityRockSling;
import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class RenderRockSling extends GeoEntityRenderer<EntityRockSling> {

    public RenderRockSling(EntityRendererFactory.Context renderManager) {
        super(renderManager, new ModelRockSling());
    }
}
