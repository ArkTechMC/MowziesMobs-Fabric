package com.bobmowzie.mowziesmobs.client.model;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.armor.WroughtHelmModel;
import com.bobmowzie.mowziesmobs.client.render.block.GongRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

public class LayerHandler {
    public static final EntityModelLayer WROUGHT_HELM_LAYER = register("wrought_helm", "main");
    public static final EntityModelLayer GONG_LAYER = register("gong", "main");

    public static void registerLayer() {
        EntityModelLayerRegistry.registerModelLayer(WROUGHT_HELM_LAYER, WroughtHelmModel::createArmorLayer);
        EntityModelLayerRegistry.registerModelLayer(GONG_LAYER, GongRenderer::createBodyLayer);
    }

    private static EntityModelLayer register(String model, String layer) {
        return new EntityModelLayer(new Identifier(MowziesMobs.MODID, model), layer);
    }
}