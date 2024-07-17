package com.bobmowzie.mowziesmobs.client.model.entity;

import com.bobmowzie.mowziesmobs.server.entity.effects.EntityPoisonBall;
import com.bobmowzie.mowziesmobs.client.model.tools.AdvancedModelBase;
import com.bobmowzie.mowziesmobs.client.model.tools.AdvancedModelRenderer;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;

public class ModelPoisonBall<T extends EntityPoisonBall> extends AdvancedModelBase<T> {
    private final AdvancedModelRenderer inner;
    private final AdvancedModelRenderer outer;

    public ModelPoisonBall() {
        this.textureWidth = 32;
        this.textureHeight = 32;

        this.inner = new AdvancedModelRenderer(this, 0, 16);
        this.inner.setRotationPoint(0.0F, 3.5F, 0.0F);
        this.inner.addBox(-3.0F, -3.0F, -3.0F, 6, 6, 6, 0.0F, false);

        this.outer = new AdvancedModelRenderer(this, 0, 0);
        this.outer.setRotationPoint(0.0F, 3.5F, 0.0F);
        this.outer.addBox(-4.0F, -4.0F, -4.0F, 8, 8, 8, 0.0F, false);

        this.inner.setOpacity(1f);
        this.outer.setOpacity(0.6f);
    }

    @Override
    public void render(MatrixStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        this.inner.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.outer.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }

    @Override
    public void setAngles(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        EntityPoisonBall poisonBall = entityIn;
        float delta = ageInTicks - entityIn.age;
        Vec3d prevV = new Vec3d(poisonBall.prevMotionX, poisonBall.prevMotionY, poisonBall.prevMotionZ);
        Vec3d dv = prevV.add(poisonBall.getVelocity().subtract(prevV).multiply(delta));
        double d = Math.sqrt(dv.x * dv.x + dv.y * dv.y + dv.z * dv.z);
        if (d != 0) {
            double a = dv.y / d;
            a = Math.max(-1, Math.min(1, a));
            float pitch = -(float) Math.asin(a);
            this.inner.rotateAngleX = pitch + (float) Math.PI / 2f;
            this.outer.rotateAngleX = pitch + (float) Math.PI / 2f;
        }
    }

    public void setRotationAngle(ModelPart modelRenderer, float x, float y, float z) {
        modelRenderer.pivotX = x;
        modelRenderer.pivotY = y;
        modelRenderer.pivotZ = z;
    }
}