package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.entity.ModelSuperNova;
import com.bobmowzie.mowziesmobs.client.render.MMRenderType;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntitySuperNova;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class RenderSuperNova extends EntityRenderer<EntitySuperNova> {
    public static final Identifier TEXTURE = new Identifier(MowziesMobs.MODID, "textures/effects/super_nova.png");
    public static final Identifier[] TEXTURES = new Identifier[]{
            new Identifier(MowziesMobs.MODID, "textures/effects/super_nova_1.png"),
            new Identifier(MowziesMobs.MODID, "textures/effects/super_nova_2.png"),
            new Identifier(MowziesMobs.MODID, "textures/effects/super_nova_3.png"),
            new Identifier(MowziesMobs.MODID, "textures/effects/super_nova_4.png"),
            new Identifier(MowziesMobs.MODID, "textures/effects/super_nova_5.png"),
            new Identifier(MowziesMobs.MODID, "textures/effects/super_nova_6.png"),
            new Identifier(MowziesMobs.MODID, "textures/effects/super_nova_7.png"),
            new Identifier(MowziesMobs.MODID, "textures/effects/super_nova_8.png"),
            new Identifier(MowziesMobs.MODID, "textures/effects/super_nova_9.png"),
            new Identifier(MowziesMobs.MODID, "textures/effects/super_nova_10.png"),
            new Identifier(MowziesMobs.MODID, "textures/effects/super_nova_11.png"),
            new Identifier(MowziesMobs.MODID, "textures/effects/super_nova_12.png"),
            new Identifier(MowziesMobs.MODID, "textures/effects/super_nova_13.png"),
            new Identifier(MowziesMobs.MODID, "textures/effects/super_nova_14.png"),
            new Identifier(MowziesMobs.MODID, "textures/effects/super_nova_15.png"),
            new Identifier(MowziesMobs.MODID, "textures/effects/super_nova_16.png")
    };
    public ModelSuperNova<EntitySuperNova> model;

    public RenderSuperNova(EntityRendererFactory.Context mgr) {
        super(mgr);
        this.model = new ModelSuperNova<>();
    }

    @Override
    public Identifier getTexture(EntitySuperNova entity) {
        int index = entity.age % TEXTURES.length;
        return TEXTURES[index];
    }

    @Override
    public void render(EntitySuperNova entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, VertexConsumerProvider bufferIn, int packedLightIn) {
        matrixStackIn.push();
        VertexConsumer ivertexbuilder = bufferIn.getBuffer(MMRenderType.getGlowingEffect(this.getTexture(entityIn)));
        this.model.setAngles(entityIn, 0, 0, entityIn.age + partialTicks, 0, 0);
        this.model.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.DEFAULT_UV, 1, 1, 1, 1);
        matrixStackIn.pop();
    }
}
