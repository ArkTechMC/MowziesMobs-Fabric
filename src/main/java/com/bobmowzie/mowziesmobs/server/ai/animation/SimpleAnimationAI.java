package com.bobmowzie.mowziesmobs.server.ai.animation;

import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import com.iafenvoy.uranus.animation.Animation;
import com.iafenvoy.uranus.animation.IAnimatedEntity;

import java.util.EnumSet;


public class SimpleAnimationAI<T extends MowzieEntity & IAnimatedEntity> extends AnimationAI<T> {
    protected final Animation animation;

    public SimpleAnimationAI(T entity, Animation animation) {
        super(entity);
        this.animation = animation;
    }

    public SimpleAnimationAI(T entity, Animation animation, boolean interruptsAI) {
        super(entity, interruptsAI);
        this.animation = animation;
    }

    public SimpleAnimationAI(T entity, Animation animation, boolean interruptsAI, boolean hurtInterruptsAnimation) {
        super(entity, interruptsAI, hurtInterruptsAnimation);
        this.animation = animation;
    }

    public SimpleAnimationAI(T entity, Animation animation, EnumSet<Control> interruptFlagTypes) {
        super(entity);
        this.animation = animation;
        this.setControls(interruptFlagTypes);
    }

    @Override
    protected boolean test(Animation animation) {
        return animation == this.animation;
    }
}
