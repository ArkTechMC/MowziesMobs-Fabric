package com.bobmowzie.mowziesmobs.server.ai.animation;

import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import com.iafenvoy.uranus.animation.Animation;
import com.iafenvoy.uranus.animation.IAnimatedEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.sound.SoundEvent;

import java.util.EnumSet;

public class AnimationAttackAI<T extends MowzieEntity & IAnimatedEntity> extends SimpleAnimationAI<T> {
    protected LivingEntity entityTarget;
    protected SoundEvent attackSound;
    protected float applyKnockbackMultiplier = 1;
    protected float range;
    protected float damageMultiplier;
    protected int damageFrame;
    protected SoundEvent hitSound;

    public AnimationAttackAI(T entity, Animation animation, SoundEvent attackSound, SoundEvent hitSound, float applyKnockback, float range, float damageMultiplier, int damageFrame) {
        this(entity, animation, attackSound, hitSound, applyKnockback, range, damageMultiplier, damageFrame, false);
    }

    public AnimationAttackAI(T entity, Animation animation, SoundEvent attackSound, SoundEvent hitSound, float applyKnockbackMultiplier, float range, float damageMultiplier, int damageFrame, boolean hurtInterrupts) {
        super(entity, animation, false, hurtInterrupts);
        this.entityTarget = null;
        this.attackSound = attackSound;
        this.applyKnockbackMultiplier = applyKnockbackMultiplier;
        this.range = range;
        this.damageMultiplier = damageMultiplier;
        this.damageFrame = damageFrame;
        this.hitSound = hitSound;
        this.setControls(EnumSet.of(Control.LOOK));
    }

    @Override
    public void start() {
        super.start();
        this.entityTarget = this.entity.getTarget();
    }

    @Override
    public void tick() {
        super.tick();
        if (this.entity.getAnimationTick() < this.damageFrame && this.entityTarget != null) {
            this.entity.lookAtEntity(this.entityTarget, 30F, 30F);
        }
        if (this.entity.getAnimationTick() == this.damageFrame) {
            if (this.entityTarget != null && this.entity.targetDistance <= this.range) {
                this.entity.doHurtTarget(this.entityTarget, this.damageMultiplier, this.applyKnockbackMultiplier);
                this.onAttack(this.entityTarget, this.damageMultiplier, this.applyKnockbackMultiplier);
                if (this.hitSound != null) {
                    this.entity.playSound(this.hitSound, 1, 1);
                }
            }
            if (this.attackSound != null) {
                this.entity.playSound(this.attackSound, 1, 1);
            }
        }
    }

    protected void onAttack(LivingEntity entityTarget, float damageMultiplier, float applyKnockbackMultiplier) {

    }
}
