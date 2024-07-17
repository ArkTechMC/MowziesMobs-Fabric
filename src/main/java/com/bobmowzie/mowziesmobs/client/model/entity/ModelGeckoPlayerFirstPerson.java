package com.bobmowzie.mowziesmobs.client.model.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieGeoBone;
import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieGeoModel;
import com.bobmowzie.mowziesmobs.client.render.entity.player.GeckoPlayer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.core.animation.AnimationState;

@Environment(EnvType.CLIENT)
public class ModelGeckoPlayerFirstPerson extends MowzieGeoModel<GeckoPlayer> {
    public BipedEntityModel.ArmPose leftArmPose = BipedEntityModel.ArmPose.EMPTY;
    public BipedEntityModel.ArmPose rightArmPose = BipedEntityModel.ArmPose.EMPTY;
    protected boolean useSmallArms;
    private Identifier textureLocation;

    @Override
    public Identifier getAnimationResource(GeckoPlayer animatable) {
        return new Identifier(MowziesMobs.MODID, "animations/animated_player_first_person.animation.json");
    }

    @Override
    public Identifier getModelResource(GeckoPlayer animatable) {
        return new Identifier(MowziesMobs.MODID, "geo/animated_player_first_person.geo.json");
    }

    @Override
    public Identifier getTextureResource(GeckoPlayer animatable) {
        return this.textureLocation;
    }

    public void setUseSmallArms(boolean useSmallArms) {
        this.useSmallArms = useSmallArms;
    }

    public boolean isUsingSmallArms() {
        return this.useSmallArms;
    }

    @Override
    public void setCustomAnimations(GeckoPlayer animatable, long instanceId, AnimationState<GeckoPlayer> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
        if (this.isInitialized()) {
            MowzieGeoBone rightArmLayerClassic = this.getMowzieBone("RightArmLayerClassic");
            MowzieGeoBone leftArmLayerClassic = this.getMowzieBone("LeftArmLayerClassic");
            MowzieGeoBone rightArmLayerSlim = this.getMowzieBone("RightArmLayerSlim");
            MowzieGeoBone leftArmLayerSlim = this.getMowzieBone("LeftArmLayerSlim");
            MowzieGeoBone rightArmClassic = this.getMowzieBone("RightArmClassic");
            MowzieGeoBone leftArmClassic = this.getMowzieBone("LeftArmClassic");
            MowzieGeoBone rightArmSlim = this.getMowzieBone("RightArmSlim");
            MowzieGeoBone leftArmSlim = this.getMowzieBone("LeftArmSlim");
            this.getMowzieBone("LeftHeldItem").setHidden(true);
            this.getMowzieBone("RightHeldItem").setHidden(true);
            rightArmClassic.setHidden(true);
            leftArmClassic.setHidden(true);
            rightArmLayerClassic.setHidden(true);
            leftArmLayerClassic.setHidden(true);
            rightArmSlim.setHidden(true);
            leftArmSlim.setHidden(true);
            rightArmLayerSlim.setHidden(true);
            leftArmLayerSlim.setHidden(true);
        }
    }

    public void setTextureFromPlayer(AbstractClientPlayerEntity player) {
        this.textureLocation = player.getSkinTexture();
    }
}