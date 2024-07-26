package com.bobmowzie.mowziesmobs.client.model.entity;

import com.bobmowzie.mowziesmobs.client.model.tools.AdvancedModelBase;
import com.bobmowzie.mowziesmobs.client.model.tools.AdvancedModelRenderer;
import com.bobmowzie.mowziesmobs.client.model.tools.MMModelAnimator;
import com.bobmowzie.mowziesmobs.server.entity.EntityDynamicsTester;
import com.iafenvoy.uranus.client.model.tools.BasicModelRenderer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

/**
 * Created by BobMowzie on 8/30/2018.
 */
public class ModelDynamicsTester<T extends EntityDynamicsTester> extends AdvancedModelBase<T> {
    private final MMModelAnimator animator;
    public AdvancedModelRenderer root;
    public AdvancedModelRenderer body1;
    public AdvancedModelRenderer body2;
    public AdvancedModelRenderer body3;
    public AdvancedModelRenderer body4;
    public AdvancedModelRenderer body5;
    public AdvancedModelRenderer body6;
    public AdvancedModelRenderer[] body;
    public AdvancedModelRenderer[] bodydynamic;

    public ModelDynamicsTester() {
        this.animator = MMModelAnimator.create();
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.root = new AdvancedModelRenderer(this, 1, 0);
        this.root.setRotationPoint(0F, 0.0F, -16F);
        this.root.addBox(-8F, -8F, -8F, 16, 16, 16, 0.0F);
        this.body1 = new AdvancedModelRenderer(this, 1, 0);
        this.body1.setRotationPoint(0F, 0.0F, 0.0F);
        this.body1.addBox(-5F, -5F, 0F, 10, 10, 8, 0.0F);
        this.body2 = new AdvancedModelRenderer(this, 1, 0);
        this.body2.setRotationPoint(0F, 0.0F, 8.0F);
        this.body2.addBox(-4F, -4F, 0F, 8, 8, 8, 0.0F);
        this.body3 = new AdvancedModelRenderer(this, 1, 0);
        this.body3.setRotationPoint(0F, 0.0F, 8.0F);
        this.body3.addBox(-3F, -3F, 0F, 6, 6, 8, 0.0F);
        this.body4 = new AdvancedModelRenderer(this, 1, 0);
        this.body4.setRotationPoint(0F, 0.0F, 8.0F);
        this.body4.addBox(-2F, -2F, 0F, 4, 4, 8, 0.0F);
        this.body5 = new AdvancedModelRenderer(this, 1, 0);
        this.body5.setRotationPoint(0F, 0.0F, 8.0F);
        this.body5.addBox(-1F, -1F, 0F, 2, 2, 8, 0.0F);
        this.body6 = new AdvancedModelRenderer(this, 1, 0);
        this.body6.setRotationPoint(0F, 0.0F, 8.0F);
        this.updateDefaultPose();

        this.root.addChild(this.body1);
        this.body1.addChild(this.body2);
        this.body2.addChild(this.body3);
        this.body3.addChild(this.body4);
        this.body4.addChild(this.body5);
        this.body5.addChild(this.body6);

        this.body = new AdvancedModelRenderer[]{
                this.body1,
                this.body2,
                this.body3,
                this.body4,
                this.body5,
                this.body6
        };
    }

    @Override
    public void render(MatrixStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        this.body1.showModel = false;
        this.body2.showModel = false;
        this.body3.showModel = false;
        this.body4.showModel = false;
        this.body5.showModel = false;
        this.body6.showModel = false;
//        if (entity.dc != null) entity.dc.render(f5, bodydynamic);
        this.root.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(BasicModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

    @Override
    public void setAngles(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
//        bob(body1, 0.3f, 16, false, entity.ticksExisted + LLibrary.PROXY.getPartialTicks(), 1F);
        this.root.rotationPointZ += 16;
    }
}
