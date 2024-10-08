package com.bobmowzie.mowziesmobs.server.ai.animation;

import com.bobmowzie.mowziesmobs.server.entity.MowzieLLibraryEntity;
import com.iafenvoy.uranus.animation.IAnimatedEntity;

public class AnimationDieAI<T extends MowzieLLibraryEntity & IAnimatedEntity> extends SimpleAnimationAI<T> {
    public AnimationDieAI(T entity) {
        super(entity, entity.getDeathAnimation(), false);
    }
}
