package com.bobmowzie.mowziesmobs.server.ai.animation;

import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import com.ilexiconn.llibrary.server.animation.Animation;
import com.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.sound.SoundEvent;

import java.util.EnumSet;
import java.util.List;

public class AnimationAreaAttackAI<T extends MowzieEntity & IAnimatedEntity> extends AnimationAttackAI<T> {
    private final float arc;
    private final float height;
    private final boolean faceTarget;

    public AnimationAreaAttackAI(T entity, Animation animation, SoundEvent attackSound, SoundEvent hitSound, float applyKnockback, float range, float height, float arc, float damageMultiplier, int damageFrame) {
        this(entity, animation, attackSound, hitSound, applyKnockback, range, height, arc, damageMultiplier, damageFrame, true);
    }

    public AnimationAreaAttackAI(T entity, Animation animation, SoundEvent attackSound, SoundEvent hitSound, float applyKnockback, float range, float height, float arc, float damageMultiplier, int damageFrame, boolean faceTarget) {
        super(entity, animation, attackSound, hitSound, applyKnockback, range, damageMultiplier, damageFrame);
        this.arc = arc;
        this.height = height;
        this.faceTarget = faceTarget;
        if (faceTarget) this.setControls(EnumSet.of(Control.LOOK));
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void tick() {
        if (this.faceTarget && this.entity.getAnimationTick() < this.damageFrame && this.entityTarget != null) {
            this.entity.lookAtEntity(this.entityTarget, 30F, 30F);
        } else if (this.entity.getAnimationTick() == this.damageFrame) {
            this.hitEntities();
        }
    }

    public void hitEntities() {
        List<LivingEntity> entitiesHit = this.entity.getEntityLivingBaseNearby(this.range, this.height, this.range, this.range);
        boolean hit = false;
        for (LivingEntity entityHit : entitiesHit) {
            float entityHitAngle = (float) ((Math.atan2(entityHit.getZ() - this.entity.getZ(), entityHit.getX() - this.entity.getX()) * (180 / Math.PI) - 90) % 360);
            float entityAttackingAngle = this.entity.bodyYaw % 360;
            if (entityHitAngle < 0) {
                entityHitAngle += 360;
            }
            if (entityAttackingAngle < 0) {
                entityAttackingAngle += 360;
            }
            float entityRelativeAngle = entityHitAngle - entityAttackingAngle;
            float entityHitDistance = (float) Math.sqrt((entityHit.getZ() - this.entity.getZ()) * (entityHit.getZ() - this.entity.getZ()) + (entityHit.getX() - this.entity.getX()) * (entityHit.getX() - this.entity.getX())) - entityHit.getWidth() / 2f;
            if (entityHitDistance <= this.range && (entityRelativeAngle <= this.arc / 2 && entityRelativeAngle >= -this.arc / 2) || (entityRelativeAngle >= 360 - this.arc / 2 || entityRelativeAngle <= -360 + this.arc / 2)) {
                this.entity.doHurtTarget(entityHit, this.damageMultiplier, this.applyKnockbackMultiplier);
                this.onAttack(entityHit, this.damageMultiplier, this.applyKnockbackMultiplier);
                hit = true;
            }
        }
        if (hit && this.hitSound != null) {
            this.entity.playSound(this.hitSound, 1, 1);
        }
        if (this.attackSound != null) this.entity.playSound(this.attackSound, 1, 1);
    }
}
