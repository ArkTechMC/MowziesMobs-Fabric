package com.bobmowzie.mowziesmobs.client.model.entity;

import com.bobmowzie.mowziesmobs.client.model.tools.AdvancedModelBase;
import com.bobmowzie.mowziesmobs.client.model.tools.AdvancedModelRenderer;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntitySuperNova;
import com.iafenvoy.uranus.client.model.tools.BasicModelRenderer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

public class ModelSuperNova<T extends EntitySuperNova> extends AdvancedModelBase<T> {
    private final AdvancedModelRenderer body1;
    private final AdvancedModelRenderer body2;
    private final AdvancedModelRenderer body3;
    private final AdvancedModelRenderer body4;

    public ModelSuperNova() {
        this.textureWidth = 64;
        this.textureHeight = 64;

        this.body1 = new AdvancedModelRenderer(this, 0, 0);
        this.body1.setRotationPoint(0.0F, 16.0F, 0.0F);
        this.body1.addBox(-32.0F, -32.0F, -32.0F, 64, 64, 64, 0.0F, false);
        this.body2 = new AdvancedModelRenderer(this, 0, 0);
        this.body2.setRotationPoint(0.0F, 16.0F, 0.0F);
        this.body2.addBox(-32.0F, -32.0F, -32.0F, 64, 64, 64, 0.0F, false);
        this.setRotationAngle(this.body2, (float) Math.PI / 2, (float) Math.PI / 2, (float) Math.PI / 2);
        this.body3 = new AdvancedModelRenderer(this, 0, 0);
        this.body3.setRotationPoint(0.0F, 16.0F, 0.0F);
        this.body3.addBox(-32.0F, -32.0F, -32.0F, 64, 64, 64, 0.0F, false);
        this.setRotationAngle(this.body3, (float) Math.PI, (float) Math.PI, (float) Math.PI);
        this.body4 = new AdvancedModelRenderer(this, 0, 0);
        this.body4.setRotationPoint(0.0F, 16.0F, 0.0F);
        this.body4.addBox(-32.0F, -32.0F, -32.0F, 64, 64, 64, 0.0F, false);
        this.setRotationAngle(this.body4, 1.5f * (float) Math.PI, 1.5f * (float) Math.PI, 1.5f * (float) Math.PI);
    }

    @Override
    public void render(MatrixStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        this.body4.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.body3.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.body2.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.body1.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }

    @Override
    public void setAngles(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        float ageFrac = ageInTicks / (float) (EntitySuperNova.DURATION);
        float scale = (float) (Math.pow(ageFrac, 0.5) * 3 + 0.05 * Math.cos(ageInTicks * 3));
        float opacity = (float) Math.max((1.0 - Math.pow(ageFrac, 4f)) * 0.4f, 0.00001);
        this.body1.setOpacity(opacity);
        this.body2.setOpacity(opacity);
        this.body3.setOpacity(opacity);
        this.body4.setOpacity(opacity);

        this.body4.setScale(scale * 0.4f, scale * 0.4f, scale * 0.4f);
        this.body3.setScale(scale * 0.6f, scale * 0.6f, scale * 0.6f);
        this.body2.setScale(scale * 0.8f, scale * 0.8f, scale * 0.8f);
        this.body1.setScale(scale, scale, scale);
    }

    public void setRotationAngle(BasicModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}