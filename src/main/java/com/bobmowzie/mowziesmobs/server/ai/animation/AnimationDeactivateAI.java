package com.bobmowzie.mowziesmobs.server.ai.animation;

import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import com.iafenvoy.uranus.animation.Animation;
import com.iafenvoy.uranus.animation.IAnimatedEntity;

public class AnimationDeactivateAI<T extends MowzieEntity & IAnimatedEntity> extends SimpleAnimationAI<T> {
    public AnimationDeactivateAI(T entity, Animation animation) {
        super(entity, animation);
    }

    @Override
    public void stop() {
        super.stop();
        this.entity.active = false;
    }
}
