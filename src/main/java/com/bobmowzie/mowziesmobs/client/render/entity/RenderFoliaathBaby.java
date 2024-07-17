package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.entity.ModelFoliaathBaby;
import com.bobmowzie.mowziesmobs.server.entity.foliaath.EntityBabyFoliaath;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class RenderFoliaathBaby extends MobEntityRenderer<EntityBabyFoliaath, ModelFoliaathBaby<EntityBabyFoliaath>> {
    private static final Identifier TEXTURE = new Identifier(MowziesMobs.MODID, "textures/entity/foliaath_baby.png");

    public RenderFoliaathBaby(EntityRendererFactory.Context mgr) {
        super(mgr, new ModelFoliaathBaby<>(), 0);
    }

    @Override
    protected float getLyingAngle(EntityBabyFoliaath entity) {
        return 0;
    }

    @Override
    public Identifier getTexture(EntityBabyFoliaath entity) {
        return RenderFoliaathBaby.TEXTURE;
    }
}