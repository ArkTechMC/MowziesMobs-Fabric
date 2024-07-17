package com.bobmowzie.mowziesmobs.client.model.armor;
// Made with Blockbench 4.2.3
// Exported for Minecraft version 1.17 - 1.18 with Mojang mappings
// Paste this class into your mod and generate all required imports

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.LivingEntity;

@Environment(EnvType.CLIENT)
public class WroughtHelmModel<T extends LivingEntity> extends BipedEntityModel<T> {
    public WroughtHelmModel(ModelPart root) {
        super(root);
    }

    public static TexturedModelData createArmorLayer() {
        Dilation deformation = Dilation.NONE;
        ModelData meshdefinition = BipedEntityModel.getModelData(deformation, 0.0F);
        ModelPartData partdefinition = meshdefinition.getRoot();
        ModelPartData head = partdefinition.getChild("head");

        ModelPartData tuskRight1 = head.addChild("tuskRight1", ModelPartBuilder.create().uv(40, 24).cuboid(-1.5F, -1.5F, -5.0F, 3.0F, 3.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(-2.5F, -1.5F, -2.5F, 0.4363F, 0.7854F, 0.0F));

        ModelPartData tuskRight2 = tuskRight1.addChild("tuskRight2", ModelPartBuilder.create().uv(34, 4).cuboid(-2.0F, -2.0F, -5.0F, 2.0F, 2.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(1.0F, 1.5F, -5.0F, -0.8727F, 0.0F, 0.0F));

        ModelPartData hornRight1 = head.addChild("hornRight1", ModelPartBuilder.create().uv(8, 3).mirrored().cuboid(-1.5F, -1.5F, -6.0F, 3.0F, 3.0F, 6.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(3.0F, -8.0F, -3.0F, -0.3491F, -0.7854F, 0.0F));

        ModelPartData hornLeft = hornRight1.addChild("hornLeft", ModelPartBuilder.create().uv(43, 12).mirrored().cuboid(0.0F, -2.0F, -6.0F, 2.0F, 2.0F, 6.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(-1.0F, 1.5F, -6.0F, -1.2217F, 0.0F, 0.0F));

        ModelPartData tuskLeft1 = head.addChild("tuskLeft1", ModelPartBuilder.create().uv(40, 24).cuboid(-1.5F, -1.5F, -5.0F, 3.0F, 3.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(2.5F, -1.5F, -2.5F, 0.4363F, -0.7854F, 0.0F));

        ModelPartData tuskLeft2 = tuskLeft1.addChild("tuskLeft2", ModelPartBuilder.create().uv(34, 4).cuboid(-2.0F, -2.0F, -5.0F, 2.0F, 2.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(1.0F, 1.5F, -5.0F, -0.8727F, 0.0F, 0.0F));

        ModelPartData hornLeft1 = head.addChild("hornLeft1", ModelPartBuilder.create().uv(8, 3).mirrored().cuboid(-1.5F, -1.5F, -6.0F, 3.0F, 3.0F, 6.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(-3.0F, -8.0F, -3.0F, -0.3491F, 0.7854F, 0.0F));

        ModelPartData hornRight = hornLeft1.addChild("hornRight", ModelPartBuilder.create().uv(30, 12).mirrored().cuboid(0.0F, -2.0F, -8.0F, 2.0F, 2.0F, 8.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(-1.0F, 1.5F, -6.0F, -1.2217F, 0.0F, 0.0F));

        ModelPartData shape1 = head.addChild("shape1", ModelPartBuilder.create().uv(0, 12).cuboid(-5.0F, -10.0F, -5.0F, 10.0F, 10.0F, 10.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        return TexturedModelData.of(meshdefinition, 64, 32);
    }
}