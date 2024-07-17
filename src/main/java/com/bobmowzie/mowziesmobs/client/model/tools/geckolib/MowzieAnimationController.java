package com.bobmowzie.mowziesmobs.client.model.tools.geckolib;

import net.minecraft.client.MinecraftClient;
import org.jetbrains.annotations.TestOnly;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.model.CoreGeoModel;
import software.bernie.geckolib.core.animation.Animation;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;

public class MowzieAnimationController<T extends GeoAnimatable> extends AnimationController<T> {
    private final double timingOffset;

    public MowzieAnimationController(T animatable, String name, int transitionLength, AnimationStateHandler<T> animationHandler, double timingOffset) {
        super(animatable, name, transitionLength, animationHandler);
        this.timingOffset = timingOffset;
    }

    // Normally, mobs won't start playing an animation until they are rendering on screen, even if the behavior starts off-screen
    // This combination of playAnimation and the overridden adjustTick fixes this issue

    public void playAnimation(T animatable, RawAnimation animation) {
        this.forceAnimationReset();
        this.setAnimation(animation);
        this.currentAnimation = this.animationQueue.poll();
        this.isJustStarting = true;
        this.adjustTick(animatable.getTick(animatable) + MinecraftClient.getInstance().getPartialTick());
        this.transitionLength = 0;
    }

    @Override
    protected double adjustTick(double tick) {
        if (this.shouldResetTick) {
            if (this.getAnimationState() == State.TRANSITIONING) {
                this.tickOffset = tick;
            } else if (this.getAnimationState() != State.STOPPED) {
                this.tickOffset += this.transitionLength;
            }
            this.shouldResetTick = false;
        }

        double adjustedTick = this.animationSpeedModifier.apply(this.animatable) * Math.max(tick - this.tickOffset, 0) + this.timingOffset;
        if (this.currentAnimation != null && this.currentAnimation.loopType() == Animation.LoopType.LOOP)
            adjustedTick = adjustedTick % this.currentAnimation.animation().length();
        if (adjustedTick == this.timingOffset) this.isJustStarting = true;
        return adjustedTick;
//        // I did this in a fugue state and I have no idea why it works
    }

    public void setLastModel(CoreGeoModel<T> coreGeoModel) {
        this.lastModel = coreGeoModel;
    }

    @TestOnly
    public <E extends GeoEntity> void checkAndReloadAnims() {
        if (
                this.lastModel != null &&
                        this.getCurrentAnimation() != null &&
                        this.getCurrentAnimation().animation() != null
        ) {
            String currentAnimationName = this.getCurrentAnimation().animation().name();
            Animation animation = this.lastModel.getAnimation(this.animatable, currentAnimationName);
            if (!animation.equals(this.getCurrentAnimation().animation())) {
                this.forceAnimationReset();
                this.currentAnimation = this.animationQueue.poll();
                this.isJustStarting = true;
                this.adjustTick(this.animatable.getTick(this.animatable) + MinecraftClient.getInstance().getPartialTick());
                this.transitionLength = 0;
            }
        }
    }
}
