package com.bobmowzie.mowziesmobs.client.model.entity;

import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieGeoBone;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerEntity;

@Environment(EnvType.CLIENT)
public class ModelGeckoPlayerThirdPerson extends ModelGeckoBiped {

    public MowzieGeoBone bipedLeftArmwear() {
        return this.getMowzieBone("LeftArmLayer");
    }

    public MowzieGeoBone bipedRightArmwear() {
        return this.getMowzieBone("RightArmLayer");
    }

    public MowzieGeoBone bipedLeftLegwear() {
        return this.getMowzieBone("LeftLegLayer");
    }

    public MowzieGeoBone bipedRightLegwear() {
        return this.getMowzieBone("RightLegLayer");
    }

    public MowzieGeoBone bipedBodywear() {
        return this.getMowzieBone("BodyLayer");
    }

    public void setVisible(boolean visible) {
        super.setVisible(visible);
        this.bipedLeftArmwear().setHidden(!visible);
        this.bipedRightArmwear().setHidden(!visible);
        this.bipedLeftLegwear().setHidden(!visible);
        this.bipedRightLegwear().setHidden(!visible);
        this.bipedBodywear().setHidden(!visible);
    }

    @Override
    public void setRotationAngles(PlayerEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float partialTick) {
        super.setRotationAngles(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, partialTick);
        MowzieGeoBone rightArmLayerClassic = this.getMowzieBone("RightArmLayerClassic");
        MowzieGeoBone leftArmLayerClassic = this.getMowzieBone("LeftArmLayerClassic");
        MowzieGeoBone rightArmLayerSlim = this.getMowzieBone("RightArmLayerSlim");
        MowzieGeoBone leftArmLayerSlim = this.getMowzieBone("LeftArmLayerSlim");
        if (this.useSmallArms) {
            rightArmLayerClassic.setHidden(true);
            leftArmLayerClassic.setHidden(true);
            rightArmLayerSlim.setHidden(false);
            leftArmLayerSlim.setHidden(false);
        } else {
            rightArmLayerSlim.setHidden(true);
            leftArmLayerSlim.setHidden(true);
            rightArmLayerClassic.setHidden(false);
            leftArmLayerClassic.setHidden(false);
        }
    }
}