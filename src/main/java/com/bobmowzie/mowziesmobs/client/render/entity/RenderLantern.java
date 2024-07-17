package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.entity.ModelLantern;
import com.bobmowzie.mowziesmobs.client.render.entity.layer.LanternGelLayer;
import com.bobmowzie.mowziesmobs.server.entity.lantern.EntityLantern;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

/**
 * Created by BobMowzie on 5/8/2017.
 */
public class RenderLantern extends MobEntityRenderer<EntityLantern, ModelLantern<EntityLantern>> {
    private static final Identifier TEXTURE = new Identifier(MowziesMobs.MODID, "textures/entity/mmlantern.png");

    public RenderLantern(EntityRendererFactory.Context mgr) {
        super(mgr, new ModelLantern<>(), 0.6f);
        this.addFeature(new LanternGelLayer<>(this));
    }

    @Override
    protected float getLyingAngle(EntityLantern entity) {
        return 0;
    }

    @Override
    public Identifier getTexture(EntityLantern entity) {
        return RenderLantern.TEXTURE;
    }

    @Override
    protected int getBlockLight(EntityLantern lantern, BlockPos pos) {
        return 15;
    }
}
