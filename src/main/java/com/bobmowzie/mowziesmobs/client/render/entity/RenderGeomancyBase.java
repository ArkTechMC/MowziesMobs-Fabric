package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieGeoModel;
import com.bobmowzie.mowziesmobs.server.entity.effects.geomancy.EntityGeomancyBase;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class RenderGeomancyBase<T extends EntityGeomancyBase & GeoEntity> extends GeoEntityRenderer<T> {
    private static final Identifier TEXTURE_STONE = new Identifier("textures/blocks/stone.png");
    private VertexConsumerProvider multiBufferSource;
    private T entity;

    protected RenderGeomancyBase(EntityRendererFactory.Context context, MowzieGeoModel<T> modelProvider) {
        super(context, modelProvider);
    }

    @Override
    public Identifier getTexture(EntityGeomancyBase entity) {
        return TEXTURE_STONE;
    }

    @Override
    public void preRender(MatrixStack poseStack, T animatable, BakedGeoModel model, VertexConsumerProvider bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.multiBufferSource = bufferSource;
        this.entity = animatable;
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }

    public VertexConsumerProvider getCurrentMultiBufferSource() {
        return this.multiBufferSource;
    }

    public void setCurrentMultiBufferSource(VertexConsumerProvider rtb) {
        this.multiBufferSource = rtb;
    }

    public T getEntity() {
        return this.entity;
    }
}
