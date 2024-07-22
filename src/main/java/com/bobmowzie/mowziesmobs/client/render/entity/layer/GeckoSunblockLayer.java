package com.bobmowzie.mowziesmobs.client.render.entity.layer;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.capability.LivingCapability;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class GeckoSunblockLayer<T extends LivingEntity & GeoEntity> extends GeoRenderLayer<T> {
    private static final Identifier SUNBLOCK_ARMOR = new Identifier(MowziesMobs.MODID, "textures/entity/sunblock_glow.png");

    public GeckoSunblockLayer(GeoRenderer<T> entityRendererIn, EntityRendererFactory.Context context) {
        super(entityRendererIn);
    }

    @Override
    public void render(MatrixStack poseStack, T animatable, BakedGeoModel bakedModel, RenderLayer renderType, VertexConsumerProvider bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        LivingCapability.ILivingCapability livingCapability = LivingCapability.get(animatable);
        if (livingCapability != null && livingCapability.getHasSunblock()) {
            float f = (float) animatable.age + partialTick;
            RenderLayer renderTypeSwirl = RenderLayer.getEnergySwirl(this.getTexture(), this.xOffset(f), f * 0.01F);

            this.getRenderer().reRender(this.getDefaultBakedModel(animatable),
                    poseStack, bufferSource, animatable, renderTypeSwirl, bufferSource.getBuffer(renderTypeSwirl), partialTick,
                    packedLight, OverlayTexture.DEFAULT_UV, 1F, 1F, 0.1F, 1.0F);
        }
    }

    protected float xOffset(float p_225634_1_) {
        return p_225634_1_ * 0.02F;
    }

    protected Identifier getTexture() {
        return SUNBLOCK_ARMOR;
    }
}
