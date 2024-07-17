package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.client.model.entity.ModelDynamicsTester;
import com.bobmowzie.mowziesmobs.server.entity.EntityDynamicsTester;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

/**
 * Created by BobMowzie on 8/30/2018.
 */
public class RenderDynamicsTester extends MobEntityRenderer<EntityDynamicsTester, ModelDynamicsTester<EntityDynamicsTester>> {
    private static final Identifier TEXTURE_STONE = new Identifier("textures/blocks/stone.png");

    public RenderDynamicsTester(EntityRendererFactory.Context mgr) {
        super(mgr, new ModelDynamicsTester<>(), 0.5f);
    }

    @Override
    protected float getLyingAngle(EntityDynamicsTester entity) {
        return 0;
    }

    @Override
    public Identifier getTexture(EntityDynamicsTester entity) {
        return TEXTURE_STONE;
    }
}
