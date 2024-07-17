package com.bobmowzie.mowziesmobs.server.ai.animation;

import com.bobmowzie.mowziesmobs.server.entity.LeaderSunstrikeImmune;
import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import com.bobmowzie.mowziesmobs.server.entity.umvuthana.EntityUmvuthi;
import com.ilexiconn.llibrary.server.animation.Animation;
import com.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.EnumSet;
import java.util.List;

public class AnimationRadiusAttack<T extends MowzieEntity & IAnimatedEntity> extends SimpleAnimationAI<T> {
    private final float radius;
    private final float damageMultiplier;
    private final float applyKnockbackMultiplier;
    private final int damageFrame;
    private final boolean pureapplyKnockback;

    public AnimationRadiusAttack(T entity, Animation animation, float radius, float damageMultiplier, float applyKnockbackMultiplier, int damageFrame, boolean pureapplyKnockback) {
        super(entity, animation);
        this.radius = radius;
        this.damageMultiplier = damageMultiplier;
        this.applyKnockbackMultiplier = applyKnockbackMultiplier;
        this.damageFrame = damageFrame;
        this.pureapplyKnockback = pureapplyKnockback;
        this.setControls(EnumSet.of(Control.MOVE, Control.JUMP, Control.LOOK));
    }

    @Override
    public void tick() {
        super.tick();
        if (this.entity.getAnimationTick() == this.damageFrame) {
            List<LivingEntity> hit = this.entity.getEntityLivingBaseNearby(this.radius, 2 * this.radius, this.radius, this.radius);
            for (LivingEntity aHit : hit) {
                if (this.entity instanceof EntityUmvuthi && aHit instanceof LeaderSunstrikeImmune) {
                    continue;
                }
                this.entity.doHurtTarget(aHit, this.damageMultiplier, this.applyKnockbackMultiplier);
                if (this.pureapplyKnockback && !aHit.isInvulnerable()) {
                    if (aHit instanceof PlayerEntity && ((PlayerEntity) aHit).getAbilities().invulnerable) continue;
                    double angle = this.entity.getAngleBetweenEntities(this.entity, aHit);
                    double x = this.applyKnockbackMultiplier * Math.cos(Math.toRadians(angle - 90));
                    double z = this.applyKnockbackMultiplier * Math.sin(Math.toRadians(angle - 90));
                    aHit.setVelocity(x, 0.3, z);
                    if (aHit instanceof ServerPlayerEntity) {
                        ((ServerPlayerEntity) aHit).networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(aHit));
                    }
                }
            }
        }
    }
}