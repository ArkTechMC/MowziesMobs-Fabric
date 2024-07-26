package com.bobmowzie.mowziesmobs.server.ai.animation;

import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import com.iafenvoy.uranus.animation.Animation;
import com.iafenvoy.uranus.animation.AnimationHandler;
import com.iafenvoy.uranus.animation.IAnimatedEntity;
import net.minecraft.entity.ai.goal.Goal;

import java.util.EnumSet;

public abstract class AnimationAI<T extends MowzieEntity & IAnimatedEntity> extends Goal {
    protected final T entity;

    protected final boolean hurtInterruptsAnimation;

    protected AnimationAI(T entity) {
        this(entity, true, false);
    }

    protected AnimationAI(T entity, boolean interruptsAI) {
        this(entity, interruptsAI, false);
    }

    protected AnimationAI(T entity, boolean interruptsAI, boolean hurtInterruptsAnimation) {
        this.entity = entity;
        if (interruptsAI) this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
        this.hurtInterruptsAnimation = hurtInterruptsAnimation;
    }

    @Override
    public boolean canStart() {
        return this.test(this.entity.getAnimation());
    }

    @Override
    public void start() {
        this.entity.hurtInterruptsAnimation = this.hurtInterruptsAnimation;
    }

    @Override
    public boolean shouldContinue() {
        return this.test(this.entity.getAnimation()) && this.entity.getAnimationTick() < this.entity.getAnimation().getDuration();
    }

    @Override
    public void stop() {
        if (this.test(this.entity.getAnimation())) {
            AnimationHandler.INSTANCE.sendAnimationMessage(this.entity, IAnimatedEntity.NO_ANIMATION);
        }
    }

    @Override
    public boolean shouldRunEveryTick() {
        return true;
    }

    protected abstract boolean test(Animation animation);
}
