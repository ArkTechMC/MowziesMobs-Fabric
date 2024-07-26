package com.bobmowzie.mowziesmobs.server.ai.animation;

import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import com.iafenvoy.uranus.animation.Animation;
import com.iafenvoy.uranus.animation.IAnimatedEntity;

public class AnimationBlockAI<T extends MowzieEntity & IAnimatedEntity> extends SimpleAnimationAI<T> {
    public AnimationBlockAI(T entity, Animation animation) {
        super(entity, animation);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.entity != null && this.entity.blockingEntity != null) {
            this.entity.lookAtEntity(this.entity.blockingEntity, 100, 100);
            this.entity.getLookControl().lookAt(this.entity.blockingEntity, 200F, 30F);
        }
    }
}