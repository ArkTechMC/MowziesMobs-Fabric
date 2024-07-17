package com.bobmowzie.mowziesmobs.server.ai.animation;

import com.bobmowzie.mowziesmobs.server.entity.effects.EntityCameraShake;
import com.bobmowzie.mowziesmobs.server.entity.wroughtnaut.EntityWroughtnaut;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import com.ilexiconn.llibrary.server.animation.Animation;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.sound.SoundEvent;

import java.util.List;

public class AnimationFWNVerticalAttackAI extends AnimationAttackAI<EntityWroughtnaut> {
    private final float arc;

    public AnimationFWNVerticalAttackAI(EntityWroughtnaut entity, Animation animation, SoundEvent sound, float applyKnockback, float range, float arc) {
        super(entity, animation, sound, null, applyKnockback, range, 0, 0);
        this.arc = arc;
    }

    @Override
    public void start() {
        super.start();
        this.entity.playSound(MMSounds.ENTITY_WROUGHT_PRE_SWING_2, 1.5F, 1F);
    }

    @Override
    public void tick() {
        this.entity.setVelocity(0, this.entity.getVelocity().y, 0);
        if (this.entity.getAnimationTick() < 21 && this.entityTarget != null) {
            this.entity.lookAtEntity(this.entityTarget, 30F, 30F);
        } else {
            this.entity.setYaw(this.entity.prevYaw);
        }

        if (this.entity.getAnimationTick() == 6) {
            this.entity.playSound(MMSounds.ENTITY_WROUGHT_CREAK, 0.5F, 1F);
        } else if (this.entity.getAnimationTick() == 25) {
            this.entity.playSound(this.attackSound, 1.2F, 1);
        } else if (this.entity.getAnimationTick() == 27) {
            this.entity.playSound(MMSounds.ENTITY_WROUGHT_SWING_2, 1.5F, 1F);
            List<LivingEntity> entitiesHit = this.entity.getEntityLivingBaseNearby(this.range, 3, this.range, this.range);
            float damage = (float) this.entity.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).getValue();
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
                float entityHitDistance = (float) Math.sqrt((entityHit.getZ() - this.entity.getZ()) * (entityHit.getZ() - this.entity.getZ()) + (entityHit.getX() - this.entity.getX()) * (entityHit.getX() - this.entity.getX()));
                if (entityHitDistance <= this.range && (entityRelativeAngle <= this.arc / 2 && entityRelativeAngle >= -this.arc / 2) || (entityRelativeAngle >= 360 - this.arc / 2 || entityRelativeAngle <= -360 + this.arc / 2)) {
                    entityHit.damage(this.entity.getDamageSources().mobAttack(this.entity), damage * 1.5F);
                    if (entityHit.isBlocking())
                        entityHit.getActiveItem().damage(400, entityHit, player -> player.sendToolBreakStatus(entityHit.getActiveHand()));
                    entityHit.setVelocity(entityHit.getVelocity().x * this.applyKnockbackMultiplier, entityHit.getVelocity().y, entityHit.getVelocity().z * this.applyKnockbackMultiplier);
                }
            }
        } else if (this.entity.getAnimationTick() == 28) {
            this.entity.playSound(MMSounds.ENTITY_WROUGHT_AXE_LAND, 1, 0.5F);
            EntityCameraShake.cameraShake(this.entity.getWorld(), this.entity.getPos(), 20, 0.3f, 0, 10);
        } else if (this.entity.getAnimationTick() == 44) {
            this.entity.playSound(MMSounds.ENTITY_WROUGHT_PULL_1, 1, 1F);
            this.entity.playSound(MMSounds.ENTITY_WROUGHT_CREAK, 0.5F, 1F);
        } else if (this.entity.getAnimationTick() == 75) {
            this.entity.playSound(MMSounds.ENTITY_WROUGHT_PULL_5, 1, 1F);
        } else if (this.entity.getAnimationTick() == 83) {
            this.entity.playSound(MMSounds.ENTITY_WROUGHT_RELEASE_2, 1, 1F);
        }
        if (this.entity.getAnimationTick() > 26 && this.entity.getAnimationTick() < 85) {
            this.entity.vulnerable = true;
            this.entity.setYaw(this.entity.prevYaw);
            this.entity.bodyYaw = this.entity.prevBodyYaw;
        } else {
            this.entity.vulnerable = false;
        }
    }

    @Override
    public void stop() {
        super.stop();
        this.entity.vulnerable = false;
    }
}
