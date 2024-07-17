package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.entity.ModelGrottol;
import com.bobmowzie.mowziesmobs.server.entity.grottol.EntityGrottol;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

/**
 * Created by BobMowzie on 5/8/2017.
 */
public class RenderGrottol extends MobEntityRenderer<EntityGrottol, ModelGrottol<EntityGrottol>> {
    private static final Identifier TEXTURE = new Identifier(MowziesMobs.MODID, "textures/entity/grottol.png");
    private static final Identifier TEXTURE_DEEPSLATE = new Identifier(MowziesMobs.MODID, "textures/entity/grottol_deepslate.png");

    public RenderGrottol(EntityRendererFactory.Context mgr) {
        super(mgr, new ModelGrottol<>(), 0.6f);
    }

    @Override
    protected float getLyingAngle(EntityGrottol entity) {
        return 0;
    }

    @Override
    public Identifier getTexture(EntityGrottol entity) {
        return entity.getDeepslate() ? RenderGrottol.TEXTURE_DEEPSLATE : RenderGrottol.TEXTURE;
    }

    /*@Override
    public void doRender(EntityGrottol entity, double x, double y, double z, float yaw, float delta) {
        if (entity.hasMinecartBlockDisplay()) {
            if (!renderOutlines) {
                renderName(entity, x, y, z);
            }
        } else {
            super.doRender(entity, x, y, z, yaw, delta);
        }
    }

    @Override
    public void doRenderShadowAndFire(Entity entity, double x, double y, double z, float yaw, float delta) {
        if (!(entity instanceof EntityGrottol) || !((EntityGrottol) entity).hasMinecartBlockDisplay()) {
            super.doRenderShadowAndFire(entity, x, y, z, yaw, delta);
        }
    }*/
}
