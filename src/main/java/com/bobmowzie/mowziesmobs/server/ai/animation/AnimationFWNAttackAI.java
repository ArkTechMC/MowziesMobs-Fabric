package com.bobmowzie.mowziesmobs.server.ai.animation;

import com.bobmowzie.mowziesmobs.server.entity.wroughtnaut.EntityWroughtnaut;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import com.ilexiconn.llibrary.server.animation.Animation;
import com.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class AnimationFWNAttackAI extends AnimationAI<EntityWroughtnaut> {
    private final float arc;
    protected float applyKnockback = 1;
    protected float range;

    public AnimationFWNAttackAI(EntityWroughtnaut entity, float applyKnockback, float range, float arc) {
        super(entity);
        this.applyKnockback = applyKnockback;
        this.range = range;
        this.arc = arc;
    }

    @Override
    protected boolean test(Animation animation) {
        return animation == EntityWroughtnaut.ATTACK_ANIMATION || animation == EntityWroughtnaut.ATTACK_TWICE_ANIMATION || animation == EntityWroughtnaut.ATTACK_THRICE_ANIMATION;
    }

    @Override
    public void start() {
        super.start();
        if (this.entity.getAnimation() == EntityWroughtnaut.ATTACK_ANIMATION)
            this.entity.playSound(MMSounds.ENTITY_WROUGHT_PRE_SWING_1, 1.5F, 1F);
    }

    @Override
    public void stop() {
        super.stop();
    }

    private boolean shouldFollowUp(float bonusRange) {
        LivingEntity entityTarget = this.entity.getTarget();
        if (entityTarget != null && entityTarget.isAlive()) {
            Vec3d targetMoveVec = entityTarget.getVelocity();
            Vec3d betweenEntitiesVec = this.entity.getPos().subtract(entityTarget.getPos());
            boolean targetComingCloser = targetMoveVec.dotProduct(betweenEntitiesVec) > 0;
            return this.entity.targetDistance < this.range + bonusRange || (this.entity.targetDistance < this.range + 5 + bonusRange && targetComingCloser);
        }
        return false;
    }

    @Override
    public void tick() {
        LivingEntity entityTarget = this.entity.getTarget();
        this.entity.setVelocity(0, this.entity.getVelocity().y, 0);
        if (this.entity.getAnimation() == EntityWroughtnaut.ATTACK_ANIMATION) {
            if (this.entity.getAnimationTick() < 23 && entityTarget != null) {
                this.entity.lookAtEntity(entityTarget, 30F, 30F);
            } else {
                this.entity.setYaw(this.entity.prevYaw);
            }
            if (this.entity.getAnimationTick() == 6) {
                this.entity.playSound(MMSounds.ENTITY_WROUGHT_CREAK, 0.5F, 1);
            } else if (this.entity.getAnimationTick() == 25) {
                this.entity.playSound(MMSounds.ENTITY_WROUGHT_WHOOSH, 1.2F, 1);
            } else if (this.entity.getAnimationTick() == 27) {
                this.entity.playSound(MMSounds.ENTITY_WROUGHT_SWING_1, 1.5F, 1);
                List<LivingEntity> entitiesHit = this.entity.getEntityLivingBaseNearby(this.range, 3, this.range, this.range);
                float damage = (float) this.entity.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).getValue();
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
                        entityHit.damage(this.entity.getDamageSources().mobAttack(this.entity), damage);
                        if (entityHit.isBlocking())
                            entityHit.getActiveItem().damage(400, entityHit, player -> player.sendToolBreakStatus(entityHit.getActiveHand()));
                        entityHit.setVelocity(entityHit.getVelocity().x * this.applyKnockback, entityHit.getVelocity().y, entityHit.getVelocity().z * this.applyKnockback);
                        hit = true;
                    }
                }
                if (hit) {
                    this.entity.playSound(MMSounds.ENTITY_WROUGHT_AXE_HIT, 1, 0.5F);
                }
            } else if (this.entity.getAnimationTick() == 37 && this.shouldFollowUp(2.5f) && this.entity.getHealthRatio() <= 0.9 && this.entity.getRandom().nextFloat() < 0.6F) {
                AnimationHandler.INSTANCE.sendAnimationMessage(this.entity, EntityWroughtnaut.ATTACK_TWICE_ANIMATION);
            }
        } else if (this.entity.getAnimation() == EntityWroughtnaut.ATTACK_TWICE_ANIMATION) {
            if (this.entity.getAnimationTick() < 7 && entityTarget != null) {
                this.entity.lookAtEntity(entityTarget, 30F, 30F);
            } else {
                this.entity.setYaw(this.entity.prevYaw);
            }
            if (this.entity.getAnimationTick() == 10) {
                this.entity.playSound(MMSounds.ENTITY_WROUGHT_WHOOSH, 1.2F, 1);
            } else if (this.entity.getAnimationTick() == 12) {
                this.entity.playSound(MMSounds.ENTITY_WROUGHT_SWING_3, 1.5F, 1);
                List<LivingEntity> entitiesHit = this.entity.getEntityLivingBaseNearby(this.range - 0.3, 3, this.range - 0.3, this.range - 0.3);
                float damage = (float) this.entity.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).getValue();
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
                    float entityHitDistance = (float) Math.sqrt((entityHit.getZ() - this.entity.getZ()) * (entityHit.getZ() - this.entity.getZ()) + (entityHit.getX() - this.entity.getX()) * (entityHit.getX() - this.entity.getX()));
                    if (entityHitDistance <= this.range - 0.3 && (entityRelativeAngle <= this.arc / 2 && entityRelativeAngle >= -this.arc / 2) || (entityRelativeAngle >= 360 - this.arc / 2 || entityRelativeAngle <= -360 + this.arc / 2)) {
                        entityHit.damage(this.entity.getDamageSources().mobAttack(this.entity), damage);
                        if (entityHit.isBlocking())
                            entityHit.getActiveItem().damage(400, entityHit, player -> player.sendToolBreakStatus(entityHit.getActiveHand()));
                        entityHit.setVelocity(entityHit.getVelocity().x * this.applyKnockback, entityHit.getVelocity().y, entityHit.getVelocity().z * this.applyKnockback);
                        hit = true;
                    }
                }
                if (hit) {
                    this.entity.playSound(MMSounds.ENTITY_WROUGHT_AXE_HIT, 1, 0.5F);
                }
            } else if (this.entity.getAnimationTick() == 23 && this.shouldFollowUp(3.5f) && this.entity.getHealthRatio() <= 0.6 && this.entity.getRandom().nextFloat() < 0.6f) {
                AnimationHandler.INSTANCE.sendAnimationMessage(this.entity, EntityWroughtnaut.ATTACK_THRICE_ANIMATION);
            }
        } else if (this.entity.getAnimation() == EntityWroughtnaut.ATTACK_THRICE_ANIMATION) {
            if (this.entity.getAnimationTick() == 1) this.entity.playSound(MMSounds.ENTITY_WROUGHT_PRE_SWING_3, 1.2F, 1f);
            if (this.entity.getAnimationTick() < 22 && entityTarget != null) {
                this.entity.lookAtEntity(entityTarget, 30F, 30F);
            } else {
                this.entity.setYaw(this.entity.prevYaw);
            }
            if (this.entity.getAnimationTick() == 20) {
                this.entity.playSound(MMSounds.ENTITY_WROUGHT_WHOOSH, 1.2F, 0.9f);
            } else if (this.entity.getAnimationTick() == 24) {
                this.entity.playSound(MMSounds.ENTITY_WROUGHT_GRUNT_3, 1.5F, 1.13f);
                this.entity.move(MovementType.SELF, new Vec3d(Math.cos(Math.toRadians(this.entity.getYaw() + 90)), 0, Math.sin(Math.toRadians(this.entity.getYaw() + 90))));
                List<LivingEntity> entitiesHit = this.entity.getEntityLivingBaseNearby(this.range + 0.2, 3, this.range + 0.2, this.range + 0.2);
                float damage = (float) this.entity.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).getValue();
                boolean hit = false;
                for (LivingEntity entityHit : entitiesHit) {
                    float entityHitDistance = (float) Math.sqrt((entityHit.getZ() - this.entity.getZ()) * (entityHit.getZ() - this.entity.getZ()) + (entityHit.getX() - this.entity.getX()) * (entityHit.getX() - this.entity.getX()));
                    if (entityHitDistance <= this.range + 0.2) {
                        entityHit.damage(this.entity.getDamageSources().mobAttack(this.entity), damage);
                        if (entityHit.isBlocking())
                            entityHit.getActiveItem().damage(400, entityHit, player -> player.sendToolBreakStatus(entityHit.getActiveHand()));
                        entityHit.setVelocity(entityHit.getVelocity().x * this.applyKnockback, entityHit.getVelocity().y, entityHit.getVelocity().z * this.applyKnockback);
                        hit = true;
                    }
                }
                if (hit) {
                    this.entity.playSound(MMSounds.ENTITY_WROUGHT_AXE_HIT, 1, 0.5F);
                }
            }
        }
    }
}
