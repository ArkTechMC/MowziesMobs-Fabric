package com.bobmowzie.mowziesmobs.server.ai.animation;

import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import com.ilexiconn.llibrary.server.animation.Animation;
import com.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.sound.SoundEvent;

public class AnimationProjectileAttackAI<T extends MowzieEntity & IAnimatedEntity & RangedAttackMob> extends SimpleAnimationAI<T> {
    private final int attackFrame;
    private final SoundEvent attackSound;

    public AnimationProjectileAttackAI(T entity, Animation animation, int attackFrame, SoundEvent attackSound) {
        this(entity, animation, attackFrame, attackSound, false);
    }

    public AnimationProjectileAttackAI(T entity, Animation animation, int attackFrame, SoundEvent attackSound, boolean hurtInterrupts) {
        super(entity, animation, true, hurtInterrupts);
        this.attackFrame = attackFrame;
        this.attackSound = attackSound;
    }

    @Override
    public void tick() {
        super.tick();
        LivingEntity entityTarget = this.entity.getTarget();
        if (entityTarget != null) {
            this.entity.lookAtEntity(entityTarget, 100, 100);
            this.entity.getLookControl().lookAt(entityTarget, 30F, 30F);
            if (this.entity.getAnimationTick() == this.attackFrame) {
                this.entity.attack(entityTarget, 0);
                if (this.attackSound != null) {
                    this.entity.playSound(this.attackSound, 1, 1);
                }
            }
        }
    }
}
