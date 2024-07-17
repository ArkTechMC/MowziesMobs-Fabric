package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.entity.ModelSculptor;
import com.bobmowzie.mowziesmobs.client.render.entity.layer.GeckoItemlayer;
import com.bobmowzie.mowziesmobs.client.render.entity.layer.GeckoSunblockLayer;
import com.bobmowzie.mowziesmobs.server.entity.sculptor.EntitySculptor;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.cache.object.BakedGeoModel;

public class RenderSculptor extends MowzieGeoEntityRenderer<EntitySculptor> {
    public final Identifier staff_geo_location = new Identifier(MowziesMobs.MODID, "geo/sculptor_staff.geo.json");
    public final Identifier staff_tex_location = new Identifier(MowziesMobs.MODID, "textures/item/sculptor_staff.png");
    public int staffController = 0;

    public RenderSculptor(EntityRendererFactory.Context renderManager) {
        super(renderManager, new ModelSculptor());
        this.addRenderLayer(new FrozenRenderHandler.GeckoLayerFrozen<>(this, renderManager));
        this.addRenderLayer(new GeckoSunblockLayer<>(this, renderManager));
        this.addRenderLayer(new GeckoItemlayer<>(this, "backItem", new ItemStack(ItemHandler.SCULPTOR_STAFF, 1)));
        this.addRenderLayer(new GeckoItemlayer<>(this, "itemHandLeft", new ItemStack(ItemHandler.SCULPTOR_STAFF, 1)));
        this.addRenderLayer(new GeckoItemlayer<>(this, "itemHandRight", new ItemStack(ItemHandler.SCULPTOR_STAFF, 1)));
        this.shadowRadius = 0.7f;

    }

    @Override
    public void preRender(MatrixStack poseStack, EntitySculptor animatable, BakedGeoModel model, VertexConsumerProvider bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
        model.getBone("gauntletRotator").get().setHidden(true);
        model.getBone("gauntletUnparented").get().setHidden(true);
    }

    @Override
    public void render(EntitySculptor entity, float entityYaw, float partialTick, MatrixStack poseStack, VertexConsumerProvider bufferSource, int packedLight) {
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        this.staffController = (int) this.model.getBone("staffController").get().getPosX();
    }
}
