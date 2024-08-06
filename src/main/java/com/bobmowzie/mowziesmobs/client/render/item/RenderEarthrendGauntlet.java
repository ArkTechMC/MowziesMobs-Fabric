package com.bobmowzie.mowziesmobs.client.render.item;

import com.bobmowzie.mowziesmobs.client.model.item.ModelEarthrendGauntlet;
import com.bobmowzie.mowziesmobs.server.item.ItemEarthrendGauntlet;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class RenderEarthrendGauntlet extends GeoItemRenderer<ItemEarthrendGauntlet> implements BuiltinItemRendererRegistry.DynamicItemRenderer {

    public RenderEarthrendGauntlet() {
        super(new ModelEarthrendGauntlet());
    }

    @Override
    public void render(ItemStack stack, ModelTransformationMode transformType, MatrixStack poseStack, VertexConsumerProvider bufferSource, int packedLight, int packedOverlay) {
        if (!this.getGeoModel().getAnimationProcessor().getRegisteredBones().isEmpty()) {
            if (transformType == ModelTransformationMode.THIRD_PERSON_LEFT_HAND ||
                    transformType == ModelTransformationMode.FIRST_PERSON_LEFT_HAND) {
                this.getGeoModel().getBone("root").get().setHidden(true);
                this.getGeoModel().getBone("rootFlipped").get().setHidden(false);
            } else {
                this.getGeoModel().getBone("root").get().setHidden(false);
                this.getGeoModel().getBone("rootFlipped").get().setHidden(true);
            }
        }
        super.render(stack, transformType, poseStack, bufferSource, packedLight, packedOverlay);
    }
}
