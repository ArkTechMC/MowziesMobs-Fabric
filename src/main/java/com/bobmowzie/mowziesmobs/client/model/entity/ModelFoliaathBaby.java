package com.bobmowzie.mowziesmobs.client.model.entity;

import com.bobmowzie.mowziesmobs.server.entity.foliaath.EntityBabyFoliaath;
import com.bobmowzie.mowziesmobs.client.model.tools.AdvancedModelRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

@Environment(EnvType.CLIENT)
public class ModelFoliaathBaby<T extends EntityBabyFoliaath> extends MowzieEntityModel<T> {
    public AdvancedModelRenderer infantBase;
    public AdvancedModelRenderer juvenileBase;
    public AdvancedModelRenderer infantLeaf1;
    public AdvancedModelRenderer infantLeaf2;
    public AdvancedModelRenderer infantLeaf3;
    public AdvancedModelRenderer infantLeaf4;
    public AdvancedModelRenderer juvenileLeaf1;
    public AdvancedModelRenderer juvenileLeaf2;
    public AdvancedModelRenderer juvenileLeaf3;
    public AdvancedModelRenderer juvenileLeaf4;
    public AdvancedModelRenderer mouthBase;
    public AdvancedModelRenderer mouth1;
    public AdvancedModelRenderer mouth2;
    public AdvancedModelRenderer mouthCover;
    public AdvancedModelRenderer teeth1;
    public AdvancedModelRenderer teeth2;

    public ModelFoliaathBaby() {
        this.textureWidth = 64;
        this.textureHeight = 16;
        this.juvenileLeaf3 = new AdvancedModelRenderer(this, 27, 0);
        this.juvenileLeaf3.setRotationPoint(-1.0F, 0.0F, 1.0F);
        this.juvenileLeaf3.addBox(-2.0F, 0.0F, -7.0F, 4, 0, 7, 0.0F);
        setRotateAngle(this.juvenileLeaf3, -0.3490658503988659F, 2.356194490192345F, 0.0F);
        this.mouthBase = new AdvancedModelRenderer(this, 13, 0);
        this.mouthBase.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.mouthBase.addBox(-1.5F, -1.0F, -1.5F, 3, 1, 3, 0.0F);
        this.mouth1 = new AdvancedModelRenderer(this, 20, 0);
        this.mouth1.setRotationPoint(0.5F, -1.0F, 0.0F);
        this.mouth1.addBox(0.0F, -5.0F, -2.5F, 2, 5, 5, 0.0F);
        setRotateAngle(this.mouth1, 0.0F, 0.0F, 0F);
        this.infantLeaf3 = new AdvancedModelRenderer(this, -3, 0);
        this.infantLeaf3.setRotationPoint(0.2F, 0.0F, 0.2F);
        this.infantLeaf3.addBox(-1.0F, 0.0F, -3.0F, 2, 0, 3, 0.0F);
        setRotateAngle(this.infantLeaf3, -0.5235987755982988F, -2.356194490192345F, 0.0F);
        this.infantBase = new AdvancedModelRenderer(this, 0, 0);
        this.infantBase.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.infantBase.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        this.infantLeaf2 = new AdvancedModelRenderer(this, -3, 0);
        this.infantLeaf2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.infantLeaf2.addBox(-1.0F, 0.0F, -3.0F, 2, 0, 3, 0.0F);
        setRotateAngle(this.infantLeaf2, -0.5235987755982988F, 0.7853981633974483F, 0.0F);
        this.juvenileLeaf2 = new AdvancedModelRenderer(this, 27, 0);
        this.juvenileLeaf2.setRotationPoint(-1.0F, 0.0F, -1.0F);
        this.juvenileLeaf2.addBox(-2.0F, 0.0F, -7.0F, 4, 0, 7, 0.0F);
        setRotateAngle(this.juvenileLeaf2, -0.3490658503988659F, 0.7853981633974483F, 0.0F);
        this.juvenileBase = new AdvancedModelRenderer(this, 0, 0);
        this.juvenileBase.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.juvenileBase.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        this.teeth1 = new AdvancedModelRenderer(this, 49, 2);
        this.teeth1.setRotationPoint(-1.0F, 0.0F, 0.0F);
        this.teeth1.addBox(0.0F, -5.0F, -2.5F, 1, 5, 5, 0.0F);
        this.juvenileLeaf4 = new AdvancedModelRenderer(this, 27, 0);
        this.juvenileLeaf4.setRotationPoint(1.0F, 0.0F, 1.0F);
        this.juvenileLeaf4.addBox(-2.0F, 0.0F, -7.0F, 4, 0, 7, 0.0F);
        setRotateAngle(this.juvenileLeaf4, -0.3490658503988659F, 3.9269908169872414F, 0.0F);
        this.juvenileLeaf1 = new AdvancedModelRenderer(this, 27, 0);
        this.juvenileLeaf1.setRotationPoint(1.0F, 0.0F, -1.0F);
        this.juvenileLeaf1.addBox(-2.0F, 0.0F, -7.0F, 4, 0, 7, 0.0F);
        setRotateAngle(this.juvenileLeaf1, -0.3490658503988659F, -0.7853981633974483F, 0.0F);
        this.teeth2 = new AdvancedModelRenderer(this, 37, 2);
        this.teeth2.setRotationPoint(-1.0F, 0.0F, 0.0F);
        this.teeth2.addBox(0.0F, -5.0F, -2.5F, 1, 5, 5, 0.0F);
        this.mouth2 = new AdvancedModelRenderer(this, 20, 0);
        this.mouth2.setRotationPoint(-0.5F, -1.0F, 0.0F);
        this.mouth2.addBox(0.0F, -5.0F, -2.5F, 2, 5, 5, 0.0F);
        setRotateAngle(this.mouth2, 0.0F, 3.141592653589793F, 0F);
        this.infantLeaf1 = new AdvancedModelRenderer(this, -3, 0);
        this.infantLeaf1.setRotationPoint(0.2F, 0.0F, -0.2F);
        this.infantLeaf1.addBox(-1.0F, 0.0F, -3.0F, 2, 0, 3, 0.0F);
        setRotateAngle(this.infantLeaf1, -0.5235987755982988F, -0.7853981633974483F, 0.0F);
        this.infantLeaf4 = new AdvancedModelRenderer(this, -3, 0);
        this.infantLeaf4.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.infantLeaf4.addBox(-1.0F, 0.0F, -3.0F, 2, 0, 3, 0.0F);
        setRotateAngle(this.infantLeaf4, -0.5235987755982988F, -3.9269908169872414F, 0.0F);
        this.mouthCover = new AdvancedModelRenderer(this, 0, 0);
        this.mouthCover.setRotationPoint(0.0F, -1.0F, 0.0F);
        this.mouthCover.addBox(-2.0F, -1.0F, -2.5F, 4, 1, 5, 0.0F);
        this.juvenileBase.addChild(this.juvenileLeaf3);
        this.juvenileBase.addChild(this.mouthBase);
        this.mouthBase.addChild(this.mouth1);
        this.infantBase.addChild(this.infantLeaf3);
        this.infantBase.addChild(this.infantLeaf2);
        this.juvenileBase.addChild(this.juvenileLeaf2);
        this.mouth1.addChild(this.teeth1);
        this.juvenileBase.addChild(this.juvenileLeaf4);
        this.juvenileBase.addChild(this.juvenileLeaf1);
        this.mouth2.addChild(this.teeth2);
        this.mouthBase.addChild(this.mouth2);
        this.infantBase.addChild(this.infantLeaf1);
        this.infantBase.addChild(this.infantLeaf4);
        this.mouthBase.addChild(this.mouthCover);

        //parts = Lists.newArrayList(infantBase, juvenileBase, infantLeaf1, infantLeaf2, infantLeaf3, infantLeaf4, juvenileLeaf1, juvenileLeaf2, juvenileLeaf3, juvenileLeaf4, mouthBase, mouth1, mouth2, mouthCover, teeth1, teeth2);
        this.updateDefaultPose();
    }

    @Override
    public void render(MatrixStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        this.infantBase.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.juvenileBase.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }

    public void setDefaultAngles(EntityBabyFoliaath entity, float limbSwing, float limbSwingAmount, float headYaw, float headPitch, float delta) {
        this.resetToDefaultPose();
        float frame = entity.frame + delta;
        float openMouthProgress = entity.activate.getAnimationProgressSinSqrt();
        this.mouth1.rotateAngleZ += 0.5 * openMouthProgress;
        this.mouth2.rotateAngleZ -= 0.5 * openMouthProgress;
        this.walk(this.juvenileLeaf1, 1F, 0.07F * openMouthProgress, false, 0, 0, frame, 1F);
        this.walk(this.juvenileLeaf2, 1F, 0.07F * openMouthProgress, false, 0, 0, frame, 1F);
        this.walk(this.juvenileLeaf3, 1F, 0.07F * openMouthProgress, false, 0, 0, frame, 1F);
        this.walk(this.juvenileLeaf4, 1F, 0.07F * openMouthProgress, false, 0, 0, frame, 1F);
        this.flap(this.mouth1, 1F, 0.07F * openMouthProgress, false, -1, 0, frame, 1F);
        this.flap(this.mouth2, 1F, -0.07F * openMouthProgress, false, -1, 0, frame, 1F);
        this.infantBase.showModel = !(this.juvenileBase.showModel = !entity.getInfant());
    }

    @Override
    protected void animate(EntityBabyFoliaath entity, float limbSwing, float limbSwingAmount, float headYaw, float headPitch, float delta) {
        this.setDefaultAngles(entity, limbSwing, limbSwingAmount, headYaw, headPitch, delta);
        this.animator.setAnimation(EntityBabyFoliaath.EAT_ANIMATION);
        this.animator.startKeyframe(2);
        this.animator.rotate(this.mouth1, 0, 0, 0.5F);
        this.animator.rotate(this.mouth2, 0, 0, -0.5F);
        this.animator.endKeyframe();
        this.animator.resetKeyframe(2);
        this.animator.startKeyframe(2);
        this.animator.rotate(this.mouth1, 0, 0, 0.5F);
        this.animator.rotate(this.mouth2, 0, 0, -0.5F);
        this.animator.endKeyframe();
        this.animator.resetKeyframe(2);
        this.animator.startKeyframe(2);
        this.animator.rotate(this.mouth1, 0, 0, 0.5F);
        this.animator.rotate(this.mouth2, 0, 0, -0.5F);
        this.animator.endKeyframe();
        this.animator.resetKeyframe(2);
        this.animator.startKeyframe(2);
        this.animator.rotate(this.mouth1, 0, 0, 0.5F);
        this.animator.rotate(this.mouth2, 0, 0, -0.5F);
        this.animator.endKeyframe();
        this.animator.resetKeyframe(2);
        this.animator.startKeyframe(2);
        this.animator.rotate(this.mouth1, 0, 0, 0.5F);
        this.animator.rotate(this.mouth2, 0, 0, -0.5F);
        this.animator.endKeyframe();
        this.animator.resetKeyframe(2);
    }
}
