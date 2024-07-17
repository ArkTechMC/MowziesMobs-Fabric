package com.bobmowzie.mowziesmobs.client.model.armor;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.item.ItemUmvuthanaMask;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class UmvuthanaMaskModel extends GeoModel<ItemUmvuthanaMask> {
    @Override
    public Identifier getModelResource(ItemUmvuthanaMask object) {
        return new Identifier(MowziesMobs.MODID, "geo/mask_" + object.getMaskType().name + ".geo.json");
    }

    @Override
    public Identifier getTextureResource(ItemUmvuthanaMask object) {
        return new Identifier(MowziesMobs.MODID, "textures/item/umvuthana_mask_" + object.getMaskType().name + ".png");
    }

    @Override
    public Identifier getAnimationResource(ItemUmvuthanaMask animatable) {
        return new Identifier(MowziesMobs.MODID, "animations/umvuthana_mask.animation.json");
    }

    @Override
    public RenderLayer getRenderType(ItemUmvuthanaMask animatable, Identifier texture) {
        return RenderLayer.getEntityCutoutNoCull(texture);
    }
}