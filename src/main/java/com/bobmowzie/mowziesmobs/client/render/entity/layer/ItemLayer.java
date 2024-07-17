package com.bobmowzie.mowziesmobs.client.render.entity.layer;

import com.bobmowzie.mowziesmobs.client.render.MowzieRenderUtils;
import com.bobmowzie.mowziesmobs.client.model.tools.AdvancedModelRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public class ItemLayer<T extends LivingEntity, M extends EntityModel<T>> extends FeatureRenderer<T, M> {
    private AdvancedModelRenderer modelRenderer;
    private ItemStack itemstack;
    private final ModelTransformationMode transformType;

    public ItemLayer(FeatureRendererContext<T, M> renderer, AdvancedModelRenderer modelRenderer, ItemStack itemstack, ModelTransformationMode transformType) {
        super(renderer);
        this.itemstack = itemstack;
        this.modelRenderer = modelRenderer;
        this.transformType = transformType;
    }

    @Override
    public void render(MatrixStack matrixStackIn, VertexConsumerProvider bufferIn, int packedLightIn, T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!this.modelRenderer.showModel || this.modelRenderer.isHidden()) return;
        matrixStackIn.push();
        MowzieRenderUtils.matrixStackFromModel(matrixStackIn, this.getModelRenderer());
        MinecraftClient.getInstance().getEntityRenderDispatcher().getHeldItemRenderer().renderItem(entitylivingbaseIn, this.getItemstack(), this.transformType, false, matrixStackIn, bufferIn, packedLightIn);
        matrixStackIn.pop();
    }

    public ItemStack getItemstack() {
        return this.itemstack;
    }

    public void setItemstack(ItemStack itemstack) {
        this.itemstack = itemstack;
    }

    public AdvancedModelRenderer getModelRenderer() {
        return this.modelRenderer;
    }

    public void setModelRenderer(AdvancedModelRenderer modelRenderer) {
        this.modelRenderer = modelRenderer;
    }
}
