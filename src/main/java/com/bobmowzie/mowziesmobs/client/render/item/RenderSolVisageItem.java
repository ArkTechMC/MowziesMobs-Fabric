package com.bobmowzie.mowziesmobs.client.render.item;

import com.bobmowzie.mowziesmobs.client.model.armor.SolVisageModel;
import com.bobmowzie.mowziesmobs.server.item.ItemSolVisage;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class RenderSolVisageItem extends GeoItemRenderer<ItemSolVisage> implements BuiltinItemRendererRegistry.DynamicItemRenderer {

    public RenderSolVisageItem() {
        super(new SolVisageModel());
    }

    @Override
    public RenderLayer getRenderType(ItemSolVisage animatable, Identifier texture, @Nullable VertexConsumerProvider bufferSource, float partialTick) {
        return RenderLayer.getArmorCutoutNoCull(texture);
    }
}
