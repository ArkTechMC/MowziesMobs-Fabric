package com.bobmowzie.mowziesmobs.client.render.item;

import com.bobmowzie.mowziesmobs.client.model.item.ModelSculptorStaff;
import com.bobmowzie.mowziesmobs.server.item.ItemSculptorStaff;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class RenderSculptorStaff extends GeoItemRenderer<ItemSculptorStaff> implements BuiltinItemRendererRegistry.DynamicItemRenderer {

    public RenderSculptorStaff() {
        super(new ModelSculptorStaff());
    }
}
