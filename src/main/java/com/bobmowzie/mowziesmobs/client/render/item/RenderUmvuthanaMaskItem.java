package com.bobmowzie.mowziesmobs.client.render.item;

import com.bobmowzie.mowziesmobs.client.model.armor.UmvuthanaMaskModel;
import com.bobmowzie.mowziesmobs.server.item.ItemUmvuthanaMask;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class RenderUmvuthanaMaskItem extends GeoItemRenderer<ItemUmvuthanaMask> implements BuiltinItemRendererRegistry.DynamicItemRenderer {

    public RenderUmvuthanaMaskItem() {
        super(new UmvuthanaMaskModel());
    }
}
