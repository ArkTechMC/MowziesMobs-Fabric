package com.bobmowzie.mowziesmobs.server.entity.grottol.ai;

import com.bobmowzie.mowziesmobs.server.ai.animation.SimpleAnimationAI;
import com.bobmowzie.mowziesmobs.server.entity.grottol.EntityGrottol;
import com.iafenvoy.uranus.animation.Animation;
import com.iafenvoy.uranus.animation.AnimationHandler;
import com.iafenvoy.uranus.animation.IAnimatedEntity;
import net.minecraft.sound.SoundEvents;

public class EntityAIGrottolIdle extends SimpleAnimationAI<EntityGrottol> {
    private static final Animation ANIMATION = Animation.create(47);

    public EntityAIGrottolIdle(EntityGrottol entity) {
        super(entity, ANIMATION, false);
    }

    public static Animation animation() {
        return ANIMATION;
    }

    @Override
    public boolean canStart() {
        return this.entity.getAnimation() == IAnimatedEntity.NO_ANIMATION && this.entity.getRandom().nextInt(180) == 0;
    }

    @Override
    public void start() {
        AnimationHandler.INSTANCE.sendAnimationMessage(this.entity, ANIMATION);
        super.start();
    }

    @Override
    public void tick() {
        super.tick();
        if (this.entity.getAnimationTick() == 28 || this.entity.getAnimationTick() == 33) {
            this.entity.playSound(SoundEvents.BLOCK_STONE_STEP, 0.5F, 1.4F);
        }
    }
}
