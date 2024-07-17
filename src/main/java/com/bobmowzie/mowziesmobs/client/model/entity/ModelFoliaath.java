package com.bobmowzie.mowziesmobs.client.model.entity;

import com.bobmowzie.mowziesmobs.server.capability.CapabilityHandler;
import com.bobmowzie.mowziesmobs.server.capability.FrozenCapability;
import com.bobmowzie.mowziesmobs.server.entity.foliaath.EntityFoliaath;
import com.bobmowzie.mowziesmobs.client.model.tools.AdvancedModelRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class ModelFoliaath<T extends EntityFoliaath> extends MowzieEntityModel<T> {
    public AdvancedModelRenderer bigLeaf2Base;
    public AdvancedModelRenderer bigLeaf1Base;
    public AdvancedModelRenderer bigLeaf4Base;
    public AdvancedModelRenderer bigLeaf3Base;
    public AdvancedModelRenderer stem1Base;
    public AdvancedModelRenderer stem1Joint;
    public AdvancedModelRenderer stem2;
    public AdvancedModelRenderer stem3;
    public AdvancedModelRenderer stem4;
    public AdvancedModelRenderer headBase;
    public AdvancedModelRenderer mouthTop1;
    public AdvancedModelRenderer leaf1Head;
    public AdvancedModelRenderer leaf2Head;
    public AdvancedModelRenderer leaf3Head;
    public AdvancedModelRenderer leaf4Head;
    public AdvancedModelRenderer leaf5Head;
    public AdvancedModelRenderer leaf6Head;
    public AdvancedModelRenderer leaf7Head;
    public AdvancedModelRenderer leaf8Head;
    public AdvancedModelRenderer tongue1Base;
    public AdvancedModelRenderer mouthBack;
    public AdvancedModelRenderer mouthBottom1;
    public AdvancedModelRenderer mouthTop2;
    public AdvancedModelRenderer teethTop1;
    public AdvancedModelRenderer teethTop2;
    public AdvancedModelRenderer tongue2;
    public AdvancedModelRenderer tongue3;
    public AdvancedModelRenderer mouthBottom2;
    public AdvancedModelRenderer teethBottom1;
    public AdvancedModelRenderer teethBottom2;
    public AdvancedModelRenderer bigLeaf2End;
    public AdvancedModelRenderer bigLeaf1End;
    public AdvancedModelRenderer bigLeaf4End;
    public AdvancedModelRenderer bigLeaf3End;
    public AdvancedModelRenderer[] stemParts;
    public AdvancedModelRenderer[] tongueParts;
    public AdvancedModelRenderer[] leafParts1;
    public AdvancedModelRenderer[] leafParts2;
    public AdvancedModelRenderer[] leafParts3;
    public AdvancedModelRenderer[] leafParts4;
    private float activeProgress;

    public ModelFoliaath() {
        this.textureWidth = 128;
        this.textureHeight = 64;

        this.headBase = new AdvancedModelRenderer(this, 80, 15);
        this.headBase.setRotationPoint(0.0F, -10.0F, 0.0F);
        this.headBase.addBox(-3.0F, -3.0F, 0.0F, 6, 6, 2, 0.0F);
        setRotateAngle(this.headBase, 1.3658946726107624F, 0.0F, 0.0F);
        this.leaf6Head = new AdvancedModelRenderer(this, 0, 18);
        this.leaf6Head.setRotationPoint(0.0F, 0.0F, 2.0F);
        this.leaf6Head.addBox(-3.5F, -19.0F, 0.0F, 7, 19, 0, 0.0F);
        setRotateAngle(this.leaf6Head, 0.6829473363053812F, 0.0F, 3.9269908169872414F);
        this.bigLeaf3End = new AdvancedModelRenderer(this, 64, 0);
        this.bigLeaf3End.setRotationPoint(0.0F, -14.0F, 0.0F);
        this.bigLeaf3End.addBox(-4.0F, -14.0F, 0.0F, 8, 14, 0, 0.0F);
        setRotateAngle(this.bigLeaf3End, -1.2292353921796064F, 0.0F, 0.0F);
        this.stem1Base = new AdvancedModelRenderer(this, 0, 0);
        this.stem1Base.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.stem1Base.addBox(-1.5F, -15.0F, -1.5F, 3, 15, 3, 0.0F);
        setRotateAngle(this.stem1Base, 0.136659280431156F, 0.0F, 0.0F);
        this.stem1Joint = new AdvancedModelRenderer(this, 0, 0);
        this.stem1Joint.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.stem1Joint.addBox(0F, 0F, 0F, 0, 0, 0, 0.0F);
        setRotateAngle(this.stem1Joint, 0.0F, 0.0F, 0.0F);
        this.teethBottom2 = new AdvancedModelRenderer(this, 15, 22);
        this.teethBottom2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.teethBottom2.addBox(-3.0F, 0.0F, 0.0F, 6, 3, 7, 0.0F);
        this.tongue1Base = new AdvancedModelRenderer(this, 40, 26);
        this.tongue1Base.setRotationPoint(0.0F, 0.0F, 2.0F);
        this.tongue1Base.addBox(-3.0F, -1.0F, 0.0F, 6, 2, 6, 0.0F);
        this.bigLeaf2Base = new AdvancedModelRenderer(this, 64, 14);
        this.bigLeaf2Base.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.bigLeaf2Base.addBox(-4.0F, -14.0F, 0.0F, 8, 14, 0, 0.0F);
        setRotateAngle(this.bigLeaf2Base, -0.6829473363053812F, 2.356194490192345F, 0.0F);
        this.mouthBack = new AdvancedModelRenderer(this, 54, 37);
        this.mouthBack.setRotationPoint(0.0F, -0.5F, 2.0F);
        this.mouthBack.addBox(-6.0F, -4.5F, 0.0F, 12, 9, 2, 0.0F);
        this.bigLeaf1End = new AdvancedModelRenderer(this, 64, 0);
        this.bigLeaf1End.setRotationPoint(0.0F, -14.0F, 0.0F);
        this.bigLeaf1End.addBox(-4.0F, -14.0F, 0.0F, 8, 14, 0, 0.0F);
        setRotateAngle(this.bigLeaf1End, -1.2292353921796064F, 0.0F, 0.0F);
        this.leaf4Head = new AdvancedModelRenderer(this, 0, 18);
        this.leaf4Head.setRotationPoint(0.0F, 0.0F, 2.0F);
        this.leaf4Head.addBox(-3.5F, -19.0F, 0.0F, 7, 19, 0, 0.0F);
        setRotateAngle(this.leaf4Head, 0.6829473363053812F, 0.0F, 2.356194490192345F);
        this.mouthBottom2 = new AdvancedModelRenderer(this, 36, 16);
        this.mouthBottom2.setRotationPoint(0.0F, 0.0F, 12.0F);
        this.mouthBottom2.addBox(-3.0F, -2.0F, 0.0F, 6, 2, 7, 0.0F);
        this.leaf5Head = new AdvancedModelRenderer(this, 0, 18);
        this.leaf5Head.setRotationPoint(0.0F, 0.0F, 2.0F);
        this.leaf5Head.addBox(-3.5F, -19.0F, 0.0F, 7, 19, 0, 0.0F);
        setRotateAngle(this.leaf5Head, 0.6829473363053812F, 0.0F, 3.141592653589793F);
        this.leaf3Head = new AdvancedModelRenderer(this, 0, 18);
        this.leaf3Head.setRotationPoint(0.0F, 0.0F, 2.0F);
        this.leaf3Head.addBox(-3.5F, -19.0F, 0.0F, 7, 19, 0, 0.0F);
        setRotateAngle(this.leaf3Head, 0.6829473363053812F, 0.0F, 1.5707963267948966F);
        this.bigLeaf1Base = new AdvancedModelRenderer(this, 64, 14);
        this.bigLeaf1Base.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.bigLeaf1Base.addBox(-4.0F, -14.0F, 0.0F, 8, 14, 0, 0.0F);
        setRotateAngle(this.bigLeaf1Base, -0.6829473363053812F, 0.7853981633974483F, 0.0F);
        this.bigLeaf4End = new AdvancedModelRenderer(this, 64, 0);
        this.bigLeaf4End.setRotationPoint(0.0F, -14.0F, 0.0F);
        this.bigLeaf4End.addBox(-4.0F, -14.0F, 0.0F, 8, 14, 0, 0.0F);
        setRotateAngle(this.bigLeaf4End, -1.2292353921796064F, 0.0F, 0.0F);
        this.mouthBottom1 = new AdvancedModelRenderer(this, 16, 0);
        this.mouthBottom1.setRotationPoint(0.0F, 1.0F, 2.0F);
        this.mouthBottom1.addBox(-6.0F, -4.0F, 0.0F, 12, 4, 12, 0.0F);
        setRotateAngle(this.mouthBottom1, 0.0F, 0.0F, 3.141592653589793F);
        this.leaf8Head = new AdvancedModelRenderer(this, 0, 18);
        this.leaf8Head.setRotationPoint(0.0F, 0.0F, 2.0F);
        this.leaf8Head.addBox(-3.5F, -19.0F, 0.0F, 7, 19, 0, 0.0F);
        setRotateAngle(this.leaf8Head, 0.6829473363053812F, 0.0F, 5.497787143782138F);
        this.leaf2Head = new AdvancedModelRenderer(this, 0, 18);
        this.leaf2Head.setRotationPoint(0.0F, 0.0F, 2.0F);
        this.leaf2Head.addBox(-3.5F, -19.0F, 0.0F, 7, 19, 0, 0.0F);
        setRotateAngle(this.leaf2Head, 0.6829473363053812F, 0.0F, 0.7853981633974483F);
        this.teethBottom1 = new AdvancedModelRenderer(this, 80, 0);
        this.teethBottom1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.teethBottom1.addBox(-6.0F, 0.0F, 0.0F, 12, 3, 12, 0.0F);
        this.tongue3 = new AdvancedModelRenderer(this, 80, 24);
        this.tongue3.setRotationPoint(0.0F, 0.0F, 6.0F);
        this.tongue3.addBox(-2.0F, -1.0F, 0.0F, 4, 2, 5, 0.0F);
        this.leaf1Head = new AdvancedModelRenderer(this, 0, 18);
        this.leaf1Head.setRotationPoint(0.0F, 0.0F, 2.0F);
        this.leaf1Head.addBox(-3.5F, -19.0F, 0.0F, 7, 19, 0, 0.0F);
        setRotateAngle(this.leaf1Head, 0.6829473363053812F, 0.0F, 0.0F);
        this.teethTop2 = new AdvancedModelRenderer(this, 15, 22);
        this.teethTop2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.teethTop2.addBox(-3.0F, 0.0F, 0.0F, 6, 3, 7, 0.0F);
        this.stem3 = new AdvancedModelRenderer(this, 0, 0);
        this.stem3.setRotationPoint(0.0F, -15.0F, 0.0F);
        this.stem3.addBox(-1.5F, -13.0F, -1.5F, 3, 13, 3, 0.0F);
        setRotateAngle(this.stem3, -1.1383037381507017F, 0.0F, 0.0F);
        this.stem2 = new AdvancedModelRenderer(this, 0, 0);
        this.stem2.setRotationPoint(0.0F, -15.0F, 0.0F);
        this.stem2.addBox(-1.5F, -15.0F, -1.5F, 3, 15, 3, 0.0F);
        setRotateAngle(this.stem2, 0.36425021489121656F, 0.0F, 0.0F);
        this.bigLeaf3Base = new AdvancedModelRenderer(this, 64, 14);
        this.bigLeaf3Base.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.bigLeaf3Base.addBox(-4.0F, -14.0F, 0.0F, 8, 14, 0, 0.0F);
        setRotateAngle(this.bigLeaf3Base, -0.6829473363053812F, 3.9269908169872414F, 0.0F);
        this.bigLeaf4Base = new AdvancedModelRenderer(this, 64, 14);
        this.bigLeaf4Base.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.bigLeaf4Base.addBox(-4.0F, -14.0F, 0.0F, 8, 14, 0, 0.0F);
        setRotateAngle(this.bigLeaf4Base, -0.6829473363053812F, 5.497787143782138F, 0.0F);
        this.tongue2 = new AdvancedModelRenderer(this, 40, 26);
        this.tongue2.setRotationPoint(0.0F, 0.0F, 6.0F);
        this.tongue2.addBox(-3.0F, -1.0F, 0.0F, 6, 2, 6, 0.0F);
        this.mouthTop2 = new AdvancedModelRenderer(this, 36, 16);
        this.mouthTop2.setRotationPoint(0.0F, 0.0F, 12.0F);
        this.mouthTop2.addBox(-3.0F, -2.0F, 0.0F, 6, 2, 7, 0.0F);
        this.bigLeaf2End = new AdvancedModelRenderer(this, 64, 0);
        this.bigLeaf2End.setRotationPoint(0.0F, -14.0F, 0.0F);
        this.bigLeaf2End.addBox(-4.0F, -14.0F, 0.0F, 8, 14, 0, 0.0F);
        setRotateAngle(this.bigLeaf2End, -1.2292353921796064F, 0.0F, 0.0F);
        this.stem4 = new AdvancedModelRenderer(this, 0, 0);
        this.stem4.setRotationPoint(0.0F, -13.0F, 0.0F);
        this.stem4.addBox(-1.5F, -10.0F, -1.5F, 3, 10, 3, 0.0F);
        setRotateAngle(this.stem4, -0.9105382707654417F, 0.0F, 0.0F);
        this.teethTop1 = new AdvancedModelRenderer(this, 80, 0);
        this.teethTop1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.teethTop1.addBox(-6.0F, 0.0F, 0.0F, 12, 3, 12, 0.0F);
        this.mouthTop1 = new AdvancedModelRenderer(this, 16, 0);
        this.mouthTop1.setRotationPoint(0.0F, -2.0F, 2.0F);
        this.mouthTop1.addBox(-6.0F, -4.0F, 0.0F, 12, 4, 12, 0.0F);
        this.leaf7Head = new AdvancedModelRenderer(this, 0, 18);
        this.leaf7Head.setRotationPoint(0.0F, 0.0F, 2.0F);
        this.leaf7Head.addBox(-3.5F, -19.0F, 0.0F, 7, 19, 0, 0.0F);
        setRotateAngle(this.leaf7Head, 0.6829473363053812F, 0.0F, 4.71238898038469F);
        this.stem4.addChild(this.headBase);
        this.headBase.addChild(this.leaf6Head);
        this.bigLeaf3Base.addChild(this.bigLeaf3End);
        this.mouthBottom2.addChild(this.teethBottom2);
        this.headBase.addChild(this.tongue1Base);
        this.headBase.addChild(this.mouthBack);
        this.bigLeaf1Base.addChild(this.bigLeaf1End);
        this.headBase.addChild(this.leaf4Head);
        this.mouthBottom1.addChild(this.mouthBottom2);
        this.headBase.addChild(this.leaf5Head);
        this.headBase.addChild(this.leaf3Head);
        this.bigLeaf4Base.addChild(this.bigLeaf4End);
        this.headBase.addChild(this.mouthBottom1);
        this.headBase.addChild(this.leaf8Head);
        this.headBase.addChild(this.leaf2Head);
        this.mouthBottom1.addChild(this.teethBottom1);
        this.tongue2.addChild(this.tongue3);
        this.headBase.addChild(this.leaf1Head);
        this.mouthTop2.addChild(this.teethTop2);
        this.stem2.addChild(this.stem3);
        this.stem1Base.addChild(this.stem2);
        this.stem1Joint.addChild(this.stem1Base);
        this.tongue1Base.addChild(this.tongue2);
        this.mouthTop1.addChild(this.mouthTop2);
        this.bigLeaf2Base.addChild(this.bigLeaf2End);
        this.stem3.addChild(this.stem4);
        this.mouthTop1.addChild(this.teethTop1);
        this.headBase.addChild(this.mouthTop1);
        this.headBase.addChild(this.leaf7Head);

        this.stemParts = new AdvancedModelRenderer[]{this.headBase, this.stem4, this.stem3, this.stem2, this.stem1Base};
        this.tongueParts = new AdvancedModelRenderer[]{this.tongue1Base, this.tongue2, this.tongue3};
        this.leafParts1 = new AdvancedModelRenderer[]{this.bigLeaf1End, this.bigLeaf1Base};
        this.leafParts2 = new AdvancedModelRenderer[]{this.bigLeaf2End, this.bigLeaf2Base};
        this.leafParts3 = new AdvancedModelRenderer[]{this.bigLeaf3End, this.bigLeaf3Base};
        this.leafParts4 = new AdvancedModelRenderer[]{this.bigLeaf4End, this.bigLeaf4Base};

        this.stem1Joint.rotationPointY += 2;
        this.stem1Joint.rotateAngleX += 0.05;
        this.stem2.rotateAngleX += 0.3;
        this.stem3.rotateAngleX += -0.1;
        this.headBase.rotateAngleX += -0.35;
        this.stem1Base.setRotationPoint(0, 0, 0);

        this.updateDefaultPose();
    }

    @Override
    public void render(MatrixStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        float leafScale = 1.25F;
        this.bigLeaf2Base.rotationPointY -= 3.5;
        this.bigLeaf1Base.rotationPointY -= 3.5;
        this.bigLeaf3Base.rotationPointY -= 3.5;
        this.bigLeaf4Base.rotationPointY -= 3.5;
        matrixStackIn.push();
        matrixStackIn.scale(leafScale, leafScale, leafScale);
        this.bigLeaf2Base.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.bigLeaf1Base.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.bigLeaf3Base.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.bigLeaf4Base.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        matrixStackIn.pop();
        matrixStackIn.push();
        matrixStackIn.translate(0, 1.4F - 1.4F * this.activeProgress, 0);
        matrixStackIn.scale(this.activeProgress, this.activeProgress, this.activeProgress);
        this.stem1Joint.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        matrixStackIn.pop();
    }

    public void setDefaultAngles(EntityFoliaath entity, float limbSwing, float limbSwingAmount, float headYaw, float headPitch, float delta) {
        this.resetToDefaultPose();

        this.stem1Joint.rotateAngleY += (headYaw / (180f / (float) Math.PI));

        this.activeProgress = entity.activate.getAnimationProgressSinSqrt(delta);
        float activeIntermittent = entity.activate.getAnimationProgressSinSqrt(delta) - entity.activate.getAnimationProgressSinToTenWithoutReturn(delta);
        float activeComplete = this.activeProgress - activeIntermittent;
        float stopDance = entity.stopDance.getAnimationProgressSinSqrt(delta) - (entity.stopDance.getAnimationProgressSinSqrt(delta) - entity.stopDance.getAnimationProgressSinToTenWithoutReturn(delta));
        float frame = entity.frame + delta;

        float globalSpeed = 0.9f;
        FrozenCapability.IFrozenCapability frozenCapability = CapabilityHandler.getCapability(entity, CapabilityHandler.FROZEN_CAPABILITY);
        boolean frozen = frozenCapability != null && frozenCapability.getFrozen();
        if (!frozen) {
            this.flap(this.stem1Base, 0.25F * globalSpeed, 0.15F * (activeComplete - stopDance), false, 0F, 0F, frame, 1F);
            this.walk(this.stem1Base, 0.5F * globalSpeed, 0.05F * (activeComplete - stopDance), false, 0F, 0F, frame, 1F);
            this.walk(this.stem2, 0.5F * globalSpeed, 0.05F * (activeComplete - stopDance), false, 0.5F, 0F, frame, 1F);
            this.walk(this.stem3, 0.5F * globalSpeed, 0.07F * (activeComplete - stopDance), false, 1F, 0F, frame, 1F);
            this.walk(this.stem4, 0.5F * globalSpeed, 0.05F * (activeComplete - stopDance), false, 1.5F, 0F, frame, 1F);
            this.walk(this.headBase, 0.5F * globalSpeed, 0.15F * (activeComplete - stopDance), true, 1.3F, 0F, frame, 1F);
            this.headBase.rotateAngleY += this.rotateBox(0.25F * globalSpeed, 0.15F * (activeComplete - stopDance), false, 0F, 0F, frame, 1F);
            this.walk(this.leaf1Head, 0.5F * globalSpeed, 0.15F * (activeComplete - stopDance), false, 3F, -0.1F, frame, 1F);
            this.walk(this.leaf2Head, 0.5F * globalSpeed, 0.15F * (activeComplete - stopDance), false, 3F, -0.1F, frame, 1F);
            this.walk(this.leaf3Head, 0.5F * globalSpeed, 0.15F * (activeComplete - stopDance), false, 3F, -0.1F, frame, 1F);
            this.walk(this.leaf4Head, 0.5F * globalSpeed, 0.15F * (activeComplete - stopDance), false, 3F, -0.1F, frame, 1F);
            this.walk(this.leaf5Head, 0.5F * globalSpeed, 0.15F * (activeComplete - stopDance), false, 3F, -0.1F, frame, 1F);
            this.walk(this.leaf6Head, 0.5F * globalSpeed, 0.15F * (activeComplete - stopDance), false, 3F, -0.1F, frame, 1F);
            this.walk(this.leaf7Head, 0.5F * globalSpeed, 0.15F * (activeComplete - stopDance), false, 3F, -0.1F, frame, 1F);
            this.walk(this.leaf8Head, 0.5F * globalSpeed, 0.15F * (activeComplete - stopDance), false, 3F, -0.1F, frame, 1F);
            this.chainWave(this.leafParts1, 0.5F * globalSpeed, 0.13F * (activeComplete - stopDance), 2, frame, 1F);
            this.chainWave(this.leafParts2, 0.5F * globalSpeed, 0.13F * (activeComplete - stopDance), 2, frame, 1F);
            this.chainWave(this.leafParts3, 0.5F * globalSpeed, 0.13F * (activeComplete - stopDance), 2, frame, 1F);
            this.chainWave(this.leafParts4, 0.5F * globalSpeed, 0.13F * (activeComplete - stopDance), 2, frame, 1F);

            //Open Mouth Animation
            float openMouthProgress = entity.openMouth.getAnimationProgressSinSqrt(delta);
            float openMouthIntermittent = entity.openMouth.getAnimationProgressSinSqrt(delta) - entity.openMouth.getAnimationProgressSinToTenWithoutReturn(delta);
            float headLeafRotation = 0.2F * openMouthProgress - 0.8F * openMouthIntermittent;
            this.mouthTop1.rotateAngleX -= 0.3 * openMouthIntermittent;
            this.mouthBottom1.rotateAngleX -= 0.3 * openMouthIntermittent;
            this.mouthTop2.rotateAngleX += 0.3 * openMouthIntermittent;
            this.mouthBottom2.rotateAngleX += 0.3 * openMouthIntermittent;
            this.stem2.rotateAngleX += 0.9 * openMouthIntermittent;
            this.stem3.rotateAngleX -= 0.6 * openMouthIntermittent;
            this.stem4.rotateAngleX -= 0.6 * openMouthIntermittent;
            this.headBase.rotateAngleX += 0.6 * openMouthIntermittent;
            this.flap(this.headBase, 1.5F, 0.6F * openMouthIntermittent, false, 0F, 0F, frame, 1F);
            this.mouthTop1.rotateAngleX += 0.15 * openMouthProgress;
            this.mouthBottom1.rotateAngleX += 0.15 * openMouthProgress;
            this.chainWave(this.tongueParts, 0.5F * globalSpeed, -0.15F * (openMouthProgress - openMouthIntermittent), -2, frame, 1F);
            this.tongue1Base.rotateAngleY += 0.3 * (openMouthProgress - openMouthIntermittent);
            this.tongue2.rotateAngleY += 0.4 * (openMouthProgress - openMouthIntermittent);
            this.tongue2.rotateAngleX -= 0.1 * (openMouthProgress - openMouthIntermittent);
            this.tongue3.rotateAngleX -= 0.5 * (openMouthProgress - openMouthIntermittent);
            this.stem1Base.rotateAngleX -= 0.2 * openMouthProgress;
            this.stem2.rotateAngleX -= 0.1 * openMouthProgress;
            this.stem3.rotateAngleX -= 0.1 * openMouthProgress;
            this.stem4.rotateAngleX -= 0.1 * openMouthProgress;
            this.headBase.rotateAngleX += 0.6 * openMouthProgress;
            this.leaf1Head.rotateAngleX -= headLeafRotation;
            this.leaf2Head.rotateAngleX -= headLeafRotation;
            this.leaf3Head.rotateAngleX -= headLeafRotation;
            this.leaf4Head.rotateAngleX -= headLeafRotation;
            this.leaf5Head.rotateAngleX -= headLeafRotation;
            this.leaf6Head.rotateAngleX -= headLeafRotation;
            this.leaf7Head.rotateAngleX -= headLeafRotation;
            this.leaf8Head.rotateAngleX -= headLeafRotation;
        }

        //Activate Animation
        this.chainFlap(this.stemParts, 0.7F, 0.2F * 2 * activeIntermittent, 2F, frame, 1F);
        this.chainSwing(this.tongueParts, 0.7F, 0.6F * 2 * activeIntermittent, -2F, frame, 1F);
        this.chainWave(this.leafParts1, 1.5F, 0.1F * 2 * activeIntermittent, 0, frame, 1F);
        this.chainWave(this.leafParts2, 1.5F, 0.1F * 2 * activeIntermittent, 0, frame, 1F);
        this.chainWave(this.leafParts3, 1.5F, 0.1F * 2 * activeIntermittent, 0, frame, 1F);
        this.chainWave(this.leafParts4, 1.5F, 0.1F * 2 * activeIntermittent, 0, frame, 1F);
        this.stem1Base.rotateAngleX -= 0.1 * 2 * activeIntermittent;
        this.stem2.rotateAngleX -= 0.5 * 2 * activeIntermittent;
        this.stem3.rotateAngleX += 0.9 * 2 * activeIntermittent;
        this.stem4.rotateAngleX += 0.6 * 2 * activeIntermittent;
        this.headBase.rotateAngleX += 0.6 * 2 * activeIntermittent;
        this.mouthTop1.rotateAngleX += 0.4 * 2 * activeIntermittent;
        this.mouthBottom1.rotateAngleX += 0.4 * 2 * activeIntermittent;
    }

    @Override
    protected void animate(EntityFoliaath entity, float limbSwing, float limbSwingAmount, float headYaw, float headPitch, float delta) {
        this.setDefaultAngles(entity, limbSwing, limbSwingAmount, headYaw, headPitch, delta);
        //Bite
        this.animator.setAnimation(EntityFoliaath.ATTACK_ANIMATION);
        this.animator.startKeyframe(3);
        this.animator.rotate(this.stem1Base, 0.4F, 0, 0);
        this.animator.rotate(this.stem2, -0.3F, 0, 0);
        this.animator.rotate(this.stem3, 0.2F, 0, 0);
        this.animator.rotate(this.stem4, 0.2F, 0, 0);
        this.animator.rotate(this.headBase, -0.6F, 0, 0);
        this.animator.rotate(this.mouthTop1, 0.8F, 0, 0);
        this.animator.rotate(this.mouthBottom1, 0.8F, 0, 0);
        this.animator.rotate(this.tongue1Base, -0.2F, 0, 0);
        this.animator.rotate(this.tongue2, -0.5F, 0, 0);
        this.animator.move(this.tongue2, 0, -0.3F, 0);
        this.animator.rotate(this.tongue3, 0.4F, 0, 0);
        this.animator.endKeyframe();
        this.animator.setStaticKeyframe(1);
        this.animator.startKeyframe(2);
        this.animator.rotate(this.stem1Base, -0.6F, 0, 0);
        this.animator.rotate(this.stem2, -1.2F, 0, 0);
        this.animator.rotate(this.stem3, 0.8F, 0, 0);
        this.animator.rotate(this.stem4, 0.8F, 0, 0);
        this.animator.rotate(this.headBase, 0.4F, 0, 0);
        this.animator.rotate(this.mouthTop1, -0.1F, 0, 0);
        this.animator.rotate(this.mouthBottom1, -0.1F, 0, 0);
        this.animator.rotate(this.mouthTop2, 0.15F, 0, 0);
        this.animator.rotate(this.mouthBottom2, 0.15F, 0, 0);
        this.animator.endKeyframe();
        this.animator.setStaticKeyframe(3);
        this.animator.resetKeyframe(5);

        this.animator.setAnimation(EntityFoliaath.HURT_ANIMATION);
        this.animator.startKeyframe(3);
        this.animator.rotate(this.stem2, 0.6F, 0, 0);
        this.animator.rotate(this.stem3, -0.4F, 0, 0);
        this.animator.rotate(this.stem4, -0.4F, 0, 0);
        this.animator.rotate(this.headBase, -0.2F, 0, 0);
        this.animator.rotate(this.leaf1Head, 0.6F, 0, 0);
        this.animator.rotate(this.leaf2Head, 0.6F, 0, 0);
        this.animator.rotate(this.leaf3Head, 0.6F, 0, 0);
        this.animator.rotate(this.leaf4Head, 0.6F, 0, 0);
        this.animator.rotate(this.leaf5Head, 0.6F, 0, 0);
        this.animator.rotate(this.leaf6Head, 0.6F, 0, 0);
        this.animator.rotate(this.leaf7Head, 0.6F, 0, 0);
        this.animator.rotate(this.leaf8Head, 0.6F, 0, 0);
        this.animator.endKeyframe();
        this.animator.resetKeyframe(7);

        float deathFlailProgress = entity.deathFlail.getAnimationProgressSinSqrt(delta);
        this.chainFlap(this.stemParts, 0.7F, 0.2F * deathFlailProgress, 2F, entity.frame + delta, 1F);
        this.chainSwing(this.tongueParts, 0.7F, 0.6F * deathFlailProgress, -2F, entity.frame + delta, 1F);
        this.chainWave(this.leafParts1, 1.5F, 0.1F * deathFlailProgress, 0, entity.frame + delta, 1F);
        this.chainWave(this.leafParts2, 1.5F, 0.1F * deathFlailProgress, 0, entity.frame + delta, 1F);
        this.chainWave(this.leafParts3, 1.5F, 0.1F * deathFlailProgress, 0, entity.frame + delta, 1F);
        this.chainWave(this.leafParts4, 1.5F, 0.1F * deathFlailProgress, 0, entity.frame + delta, 1F);
        this.animator.setAnimation(EntityFoliaath.DIE_ANIMATION);
        this.animator.startKeyframe(4);
        this.animator.rotate(this.stem1Base, -0.1F, 0, 0);
        this.animator.rotate(this.stem2, -0.5F, 0, 0);
        this.animator.rotate(this.stem3, 0.9F, 0, 0);
        this.animator.rotate(this.stem4, 0.6F, 0, 0);
        this.animator.rotate(this.headBase, 0.6F, 0, 0);
        this.animator.rotate(this.mouthTop1, 0.4F, 0, 0);
        this.animator.rotate(this.mouthBottom1, 0.4F, 0, 0);
        this.animator.endKeyframe();
        this.animator.setStaticKeyframe(10);
        this.animator.startKeyframe(5);
        this.animator.rotate(this.stem1Base, -0.1F, 0, 0);
        this.animator.rotate(this.stem2, -0.5F, 0, 0);
        this.animator.rotate(this.stem3, 0.9F, 0, 0);
        this.animator.rotate(this.stem4, 0.6F, 0, 0);
        this.animator.rotate(this.headBase, 0.6F, 0, 0);
        this.animator.rotate(this.stem1Joint, 0, -0.4F, 0);
        this.animator.rotate(this.stem1Base, -0.6F, 0, 0);
        this.animator.rotate(this.stem2, -0.7F, 0, 0);
        this.animator.rotate(this.stem3, -0.6F, 0, 0);
        this.animator.rotate(this.stem4, -0.6F, 0, 0);
        this.animator.rotate(this.headBase, 1.25F, 0, 0);
        this.animator.rotate(this.mouthTop1, 0.1F, -0.05F, 0);
        this.animator.rotate(this.tongue1Base, 0, 0.3F, 0);
        this.animator.rotate(this.tongue2, -0.1F, 0.4F, 0);
        this.animator.rotate(this.tongue3, -0.5F, 0F, 0);
        this.animator.rotate(this.leaf1Head, 0.7F, 0, 0);
        this.animator.rotate(this.leaf2Head, 0.7F, 0, 0);
        this.animator.rotate(this.leaf3Head, 0.7F, 0, 0);
        this.animator.rotate(this.leaf4Head, 0.7F, 0, 0);
        this.animator.rotate(this.leaf5Head, 0.7F, 0, 0);
        this.animator.rotate(this.leaf6Head, 0.7F, 0, 0);
        this.animator.rotate(this.leaf7Head, 0.7F, 0, 0);
        this.animator.rotate(this.leaf8Head, 0.7F, 0, 0);
        this.animator.endKeyframe();
        this.animator.startKeyframe(2);
        this.animator.rotate(this.stem1Base, -0.1F, 0, 0);
        this.animator.rotate(this.stem2, -0.5F, 0, 0);
        this.animator.rotate(this.stem3, 0.9F, 0, 0);
        this.animator.rotate(this.stem4, 0.6F, 0, 0);
        this.animator.rotate(this.headBase, 0.6F, 0, 0);
        this.animator.rotate(this.stem1Joint, 0, -0.4F, 0);
        this.animator.rotate(this.stem1Base, -0.5F, 0, 0);
        this.animator.rotate(this.stem2, -0.6F, 0, 0);
        this.animator.rotate(this.stem3, -0.5F, 0, 0);
        this.animator.rotate(this.stem4, -0.5F, 0, 0);
        this.animator.rotate(this.headBase, 0.7F, 0, 0);
        this.animator.rotate(this.mouthTop1, 0.1F, -0.05F, 0);
        this.animator.rotate(this.tongue1Base, 0, 0.3F, 0);
        this.animator.rotate(this.tongue2, -0.1F, 0.4F, 0);
        this.animator.rotate(this.tongue3, -0.5F, 0F, 0);
        this.animator.rotate(this.leaf1Head, 0.7F, 0, 0);
        this.animator.rotate(this.leaf2Head, 0.7F, 0, 0);
        this.animator.rotate(this.leaf3Head, 0.7F, 0, 0);
        this.animator.rotate(this.leaf4Head, 0.7F, 0, 0);
        this.animator.rotate(this.leaf5Head, 0.7F, 0, 0);
        this.animator.rotate(this.leaf6Head, 0.7F, 0, 0);
        this.animator.rotate(this.leaf7Head, 0.7F, 0, 0);
        this.animator.rotate(this.leaf8Head, 0.7F, 0, 0);
        this.animator.endKeyframe();
        this.animator.startKeyframe(2);
        this.animator.rotate(this.stem1Base, -0.1F, 0, 0);
        this.animator.rotate(this.stem2, -0.5F, 0, 0);
        this.animator.rotate(this.stem3, 0.9F, 0, 0);
        this.animator.rotate(this.stem4, 0.6F, 0, 0);
        this.animator.rotate(this.headBase, 0.6F, 0, 0);
        this.animator.rotate(this.stem1Joint, 0, -0.4F, 0);
        this.animator.rotate(this.stem1Base, -0.6F, 0, 0);
        this.animator.rotate(this.stem2, -0.7F, 0, 0);
        this.animator.rotate(this.stem3, -0.6F, 0, 0);
        this.animator.rotate(this.stem4, -0.6F, 0, 0);
        this.animator.rotate(this.headBase, 1.25F, 0, 0);
        this.animator.rotate(this.mouthTop1, 0.1F, -0.05F, 0);
        this.animator.rotate(this.tongue1Base, 0, 0.3F, 0);
        this.animator.rotate(this.tongue2, -0.1F, 0.4F, 0);
        this.animator.rotate(this.tongue3, -0.5F, 0F, 0);
        this.animator.rotate(this.leaf1Head, 0.7F, 0, 0);
        this.animator.rotate(this.leaf2Head, 0.7F, 0, 0);
        this.animator.rotate(this.leaf3Head, 0.7F, 0, 0);
        this.animator.rotate(this.leaf4Head, 0.7F, 0, 0);
        this.animator.rotate(this.leaf5Head, 0.7F, 0, 0);
        this.animator.rotate(this.leaf6Head, 0.7F, 0, 0);
        this.animator.rotate(this.leaf7Head, 0.7F, 0, 0);
        this.animator.rotate(this.leaf8Head, 0.7F, 0, 0);
        this.animator.endKeyframe();
        this.animator.setStaticKeyframe(27);
    }

    public float rotateBox(float speed, float degree, boolean invert, float offset, float weight, float f, float f1) {
        if (invert) {
            return -MathHelper.cos(f * speed + offset) * degree * f1 + weight * f1;
        } else {
            return MathHelper.cos(f * speed + offset) * degree * f1 + weight * f1;
        }
    }
}
