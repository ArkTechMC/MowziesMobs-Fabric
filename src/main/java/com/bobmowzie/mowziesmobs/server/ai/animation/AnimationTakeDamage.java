package com.bobmowzie.mowziesmobs.server.ai.animation;

import com.bobmowzie.mowziesmobs.server.entity.MowzieLLibraryEntity;
import com.iafenvoy.uranus.animation.IAnimatedEntity;

import java.util.EnumSet;

public class AnimationTakeDamage<T extends MowzieLLibraryEntity & IAnimatedEntity> extends SimpleAnimationAI<T> {
    public AnimationTakeDamage(T entity) {
        super(entity, entity.getHurtAnimation());
        this.setControls(EnumSet.of(Control.MOVE, Control.JUMP));
    }
}
