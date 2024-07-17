package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class RenderDart extends ProjectileEntityRenderer {
    private static final Identifier TEXTURE = new Identifier(MowziesMobs.MODID, "textures/entity/dart.png");

    public RenderDart(EntityRendererFactory.Context mgr) {
        super(mgr);
    }

    @Override
    public Identifier getTexture(Entity entity) {
        return TEXTURE;
    }
}