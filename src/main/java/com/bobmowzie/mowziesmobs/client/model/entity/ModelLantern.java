package com.bobmowzie.mowziesmobs.client.model.entity;

import com.bobmowzie.mowziesmobs.server.entity.lantern.EntityLantern;
import com.bobmowzie.mowziesmobs.client.model.tools.AdvancedModelRenderer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

/**
 * Created by BobMowzie on 7/24/2018.
 */
public class ModelLantern<T extends EntityLantern> extends MowzieEntityModel<T> {
    public AdvancedModelRenderer body;
    public AdvancedModelRenderer center;
    public AdvancedModelRenderer bubbles;
    public AdvancedModelRenderer bubble1;
    public AdvancedModelRenderer bubble2;
    public AdvancedModelRenderer bubble3;
    public AdvancedModelRenderer bubble4;
    public AdvancedModelRenderer bottomBits;
    public AdvancedModelRenderer stem;
    public AdvancedModelRenderer bottomBit1;
    public AdvancedModelRenderer bottomBit2;
    public AdvancedModelRenderer bottomBit3;
    public AdvancedModelRenderer bottomBit4;
    public AdvancedModelRenderer leaf1;
    public AdvancedModelRenderer leaf2;
    public AdvancedModelRenderer leaf3;
    public AdvancedModelRenderer leaf4;
    public AdvancedModelRenderer stem1;
    public AdvancedModelRenderer stem2;
    public AdvancedModelRenderer scaleController;

    public ModelLantern() {
        this(false);
    }

    public ModelLantern(boolean isGelLayer) {
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.leaf1 = new AdvancedModelRenderer(this, -16, 42);
        this.leaf1.mirror = true;
        this.leaf1.setRotationPoint(0.0F, 0.0F, 2.0F);
        this.leaf1.addBox(-6.0F, 0.0F, -16.0F, 12, 0, 16, 0.0F);
        setRotateAngle(this.leaf1, -0.2617993877991494F, 3.141592653589793F, 0.0F);
        this.stem2 = new AdvancedModelRenderer(this, 0, 20);
        this.stem2.setRotationPoint(0.0F, -3.0F, 0.0F);
        this.stem2.addBox(0.0F, -10.0F, -5.0F, 0, 10, 10, 0.0F);
        setRotateAngle(this.stem2, 0.0F, 0.7853981633974483F, 0.0F);
        this.body = new AdvancedModelRenderer(this, 1, 0);
        this.body.setRotationPoint(0.0F, 11.0F, 0.0F);
        this.body.addBox(-7.5F, -7.5F, -7.5F, 15, 15, 15, 0.0F);
        this.center = new AdvancedModelRenderer(this, 40, 51);
        this.center.setRotationPoint(0.0F, 11.0F, 0.0F);
        this.center.addBox(-3F, -3F, -3F, 6, 6, 6, 0.0F);
        this.bottomBit4 = new AdvancedModelRenderer(this, 46, 0);
        this.bottomBit4.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bottomBit4.addBox(-2.0F, 0.0F, -2.0F, 4, 7, 4, 0.0F);
        setRotateAngle(this.bottomBit4, 1.0471975511965976F, 4.71238898038469F, 0.0F);
        this.leaf4 = new AdvancedModelRenderer(this, 8, 30);
        this.leaf4.setRotationPoint(2.0F, 0.0F, 0.0F);
        this.leaf4.addBox(0.0F, 0.0F, -6.0F, 16, 0, 12, 0.0F);
        setRotateAngle(this.leaf4, 0.0F, 0.0F, -0.2617993877991494F);
        this.leaf2 = new AdvancedModelRenderer(this, 8, 30);
        this.leaf2.setRotationPoint(-2.0F, 0.0F, 0.0F);
        this.leaf2.addBox(0.0F, 0.0F, -6.0F, 16, 0, 12, 0.0F);
        setRotateAngle(this.leaf2, 0.0F, 3.141592653589793F, 0.2617993877991494F);
        this.bubble3 = new AdvancedModelRenderer(this, 0, 0);
        this.bubble3.setRotationPoint(-2.0F, 4.0F, -2.9F);
        this.bubble3.addBox(-1.5F, -1.5F, -1.5F, 3, 3, 3, 0.0F);
        setRotateAngle(this.bubble3, -0.091106186954104F, 1.7756979809790308F, 0.40980330836826856F);
        this.bubble4 = new AdvancedModelRenderer(this, 0, 0);
        this.bubble4.setRotationPoint(3.0F, -1.8F, -2.4F);
        this.bubble4.addBox(-1.5F, -1.5F, -1.5F, 3, 3, 3, 0.0F);
        setRotateAngle(this.bubble4, -0.7740535232594852F, 0.136659280431156F, 0.40980330836826856F);
        this.bubble1 = new AdvancedModelRenderer(this, 0, 7);
        this.bubble1.setRotationPoint(2.6F, 2.5F, 2.8F);
        this.bubble1.addBox(-2.0F, -2.0F, -2.0F, 4, 4, 4, 0.0F);
        setRotateAngle(this.bubble1, 0.27314402793711257F, 0.6829473363053812F, 0.5462880558742251F);
        this.bottomBit3 = new AdvancedModelRenderer(this, 46, 0);
        this.bottomBit3.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bottomBit3.addBox(-2.0F, 0.0F, -2.0F, 4, 7, 4, 0.0F);
        setRotateAngle(this.bottomBit3, 1.0471975511965976F, 3.141592653589793F, 0.0F);
        this.stem1 = new AdvancedModelRenderer(this, 40, 42);
        this.stem1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.stem1.addBox(-3.0F, -3.0F, -3.0F, 6, 3, 6, 0.0F);
        this.bubble2 = new AdvancedModelRenderer(this, 0, 7);
        this.bubble2.setRotationPoint(-2.8F, -3.0F, 1.8F);
        this.bubble2.addBox(-2.0F, -2.0F, -2.0F, 4, 4, 4, 0.0F);
        setRotateAngle(this.bubble2, 1.3203415791337103F, 1.5025539530419183F, 0.5462880558742251F);
        this.bottomBit2 = new AdvancedModelRenderer(this, 46, 0);
        this.bottomBit2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bottomBit2.addBox(-2.0F, 0.0F, -2.0F, 4, 7, 4, 0.0F);
        setRotateAngle(this.bottomBit2, 1.0471975511965976F, 1.5707963267948966F, 0.0F);
        this.bottomBit1 = new AdvancedModelRenderer(this, 46, 0);
        this.bottomBit1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bottomBit1.addBox(-2.0F, 0.0F, -2.0F, 4, 7, 4, 0.0F);
        setRotateAngle(this.bottomBit1, 1.0471975511965976F, 0.0F, 0.0F);
        this.leaf3 = new AdvancedModelRenderer(this, -16, 42);
        this.leaf3.setRotationPoint(0.0F, 0.0F, -2.0F);
        this.leaf3.addBox(-6.0F, 0.0F, -16.0F, 12, 0, 16, 0.0F);
        setRotateAngle(this.leaf3, -0.2617993877991494F, 0.0F, 0.0F);
        this.bottomBits = new AdvancedModelRenderer(this, 0, 0);
        this.bottomBits.setRotationPoint(0.0F, 7.5F, 0.0F);
        this.bottomBits.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        setRotateAngle(this.bottomBits, 0.0F, 0.7853981633974483F, 0.0F);
        this.stem = new AdvancedModelRenderer(this, 0, 0);
        this.stem.setRotationPoint(0.0F, -7.51F, 0.0F);
        this.stem.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        setRotateAngle(this.stem, 0.0F, 0.7853981633974483F, 0.0F);
        this.bubbles = new AdvancedModelRenderer(this, 0, 0);
        this.bubbles.setRotationPoint(0.0F, 11.0F, 0.0F);
        this.bubbles.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        this.scaleController = new AdvancedModelRenderer(this, 0, 0);
        this.scaleController.setRotationPoint(1, 1, 1);
        this.stem.addChild(this.leaf1);
        this.stem1.addChild(this.stem2);
        this.bottomBits.addChild(this.bottomBit4);
        this.stem.addChild(this.leaf4);
        this.stem.addChild(this.leaf2);
        this.bottomBits.addChild(this.bottomBit3);
        this.stem.addChild(this.stem1);
        this.bottomBits.addChild(this.bottomBit2);
        this.bottomBits.addChild(this.bottomBit1);
        this.stem.addChild(this.leaf3);
        this.body.addChild(this.bottomBits);
        this.body.addChild(this.stem);
        this.bubbles.addChild(this.bubble1);
        this.bubbles.addChild(this.bubble2);
        this.bubbles.addChild(this.bubble3);
        this.bubbles.addChild(this.bubble4);

        this.leaf1.rotationPointX += 0.5;
        this.leaf2.rotationPointZ -= 0.5;
        this.leaf3.rotationPointX += 0.5;
        this.leaf4.rotationPointZ += 0.5;
        this.stem2.rotationPointX += 0.3536;
        this.stem2.rotationPointZ += 0.3536;

        this.updateDefaultPose();

        this.bubbles.setShouldScaleChildren(true);
        this.body.setOpacity(0.6f);
        this.center.setOpacity(0.5f);
        this.center.setScale(2, 2, 2);
        this.bottomBit1.setOpacity(0.7f);
        this.bottomBit2.setOpacity(0.7f);
        this.bottomBit3.setOpacity(0.7f);
        this.bottomBit4.setOpacity(0.7f);

        if (isGelLayer) {
            this.stem.setIsHidden(true);
        } else {
            this.stem.showModel = true;
            this.body.setIsHidden(true);
            this.bubbles.showModel = false;
            this.bottomBits.showModel = false;
            this.center.showModel = false;
        }
    }

    @Override
    public void render(MatrixStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        this.bubbles.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.center.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.body.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }

    public void setDefaultAngles() {
        this.resetToDefaultPose();
        this.body.setRotationPoint(0.0F, 11.0F, 0.0F);
        this.center.setRotationPoint(0.0F, 11.0F, 0.0F);
        this.bottomBit2.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotateAngle(this.bottomBit2, 1.0471975511965976F, 1.5707963267948966F, 0.0F);
        this.bottomBit1.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotateAngle(this.bottomBit1, 1.0471975511965976F, 0.0F, 0.0F);
        this.bottomBit3.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotateAngle(this.bottomBit3, 1.0471975511965976F, 3.141592653589793F, 0.0F);
        this.bottomBit4.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotateAngle(this.bottomBit4, 1.0471975511965976F, 4.71238898038469F, 0.0F);

        this.body.rotateAngleX = 0;
        this.bubbles.rotateAngleX = 0;
        this.center.rotateAngleX = 0;
    }

    @Override
    protected void animate(EntityLantern entity, float limbSwing, float limbSwingAmount, float headYaw, float headPitch, float delta) {
        this.setDefaultAngles();
        float frame = entity.frame + delta;

        if (entity.getAnimation() == EntityLantern.PUFF_ANIMATION) {
            this.animator.setAnimation(EntityLantern.PUFF_ANIMATION);
            this.animator.startKeyframe(7);
            this.animator.move(this.scaleController, 0.4f, -0.4f, 0.4f);
            this.animator.move(this.body, 0f, -3f, 0f);
            this.animator.move(this.bubbles, 0f, -3f, 0f);
            this.animator.move(this.center, 0f, -3f, 0f);
            this.animator.move(this.stem, 0f, 3f, 0f);
            this.animator.move(this.bottomBits, 0f, -3f, 0f);
            this.animator.rotate(this.leaf1, -0.2f, 0f, 0f);
            this.animator.rotate(this.leaf2, 0f, 0f, 0.2f);
            this.animator.rotate(this.leaf3, -0.2f, 0f, 0f);
            this.animator.rotate(this.leaf4, 0f, 0f, -0.2f);
            this.animator.endKeyframe();
            this.animator.startKeyframe(3);
            this.animator.move(this.scaleController, -0.45f, 0.6f, -0.45f);
            this.animator.move(this.body, 0f, 4.5f, 0f);
            this.animator.move(this.bubbles, 0f, 4.5f, 0f);
            this.animator.move(this.center, 0f, 4.5f, 0f);
            this.animator.move(this.stem, 0f, -4.5f, 0f);
            this.animator.move(this.bottomBits, 0f, 4f, 0f);
            this.animator.rotate(this.leaf1, 0.3f, 0f, 0f);
            this.animator.rotate(this.leaf2, 0f, 0f, -0.3f);
            this.animator.rotate(this.leaf3, 0.3f, 0f, 0f);
            this.animator.rotate(this.leaf4, 0f, 0f, 0.3f);
            this.animator.rotate(this.bottomBit1, -0.9f, 0f, 0f);
            this.animator.rotate(this.bottomBit2, -0.9f, 0f, 0f);
            this.animator.rotate(this.bottomBit3, -0.9f, 0f, 0f);
            this.animator.rotate(this.bottomBit4, -0.9f, 0f, 0f);
            this.animator.endKeyframe();
            this.animator.resetKeyframe(7);
        }
        if (entity.getAnimation() == EntityLantern.DIE_ANIMATION) {
            this.animator.setAnimation(EntityLantern.DIE_ANIMATION);
            this.animator.startKeyframe(3);
            this.animator.move(this.scaleController, -0.5f, -0.5f, -0.5f);
            this.animator.move(this.bottomBits, 0f, -3f, 0f);
            this.animator.move(this.stem, 0f, 3f, 0f);
            this.animator.endKeyframe();
            this.animator.startKeyframe(3);
            this.animator.move(this.scaleController, 1.3f, 1.3f, 1.3f);
            this.animator.move(this.bottomBits, 0f, 3f, 0f);
            this.animator.move(this.stem, 0f, -3f, 0f);
            this.animator.endKeyframe();
        }

        this.body.setScale(this.scaleController.rotationPointX, this.scaleController.rotationPointY, this.scaleController.rotationPointZ);
        this.bubbles.setShouldScaleChildren(true);
        this.bubbles.setScale(this.scaleController.rotationPointX, this.scaleController.rotationPointY, this.scaleController.rotationPointZ);
        this.center.setScale(this.scaleController.rotationPointX * 2, this.scaleController.rotationPointY * 2, this.scaleController.rotationPointZ * 2);
    }
}
