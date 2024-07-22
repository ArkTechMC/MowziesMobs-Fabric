package com.bobmowzie.mowziesmobs.server.damage;

import com.bobmowzie.mowziesmobs.server.capability.LivingCapability;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;
import org.apache.commons.lang3.tuple.Pair;

public class DamageUtil {
    // TODO: Works for current use cases, but possibly not for future edge cases. Use reflection to get hurt sound for onHit2?
    public static Pair<Boolean, Boolean> dealMixedDamage(LivingEntity target, DamageSource source1, float amount1, DamageSource source2, float amount2) {
        if (target.getWorld().isClient()) return Pair.of(false, false);
        boolean flag1 = source1.getAttacker() != null && target.isTeammate(source1.getAttacker());
        boolean flag2 = source2.getAttacker() != null && target.isTeammate(source2.getAttacker());
        if (flag1 || flag2) return Pair.of(false, false);
        LivingCapability.ILivingCapability lastDamageCapability = LivingCapability.get(target);
        if (lastDamageCapability != null) {
            lastDamageCapability.setLastDamage(-1);
            float damageSoFar = 0;
            float origLastDamage = target.lastDamageTaken;
            boolean hit1 = target.damage(source1, amount1);
            boolean hit1Registered = hit1;
            if (lastDamageCapability.getLastDamage() != -1) {
                hit1Registered = true;
            }
            if (lastDamageCapability.getLastDamage() != 0) {
                damageSoFar += amount1;
            }
            target.lastDamageTaken = Math.max(target.lastDamageTaken - amount1, 0);
            lastDamageCapability.setLastDamage(-1);
            boolean hit2 = target.damage(source2, amount2);
            boolean hit2Registered = hit2;
            if (lastDamageCapability.getLastDamage() != -1) {
                hit2Registered = true;
            }
            if (lastDamageCapability.getLastDamage() != 0) {
                damageSoFar += amount2;
            }
            target.lastDamageTaken = origLastDamage;
            if (damageSoFar > target.lastDamageTaken) target.lastDamageTaken = damageSoFar;

            if (hit2 && hit1Registered) {
                onHit2(target, source2);
                if (target instanceof PlayerEntity) {
                    SoundEvent sound = SoundEvents.ENTITY_PLAYER_HURT;
                    if (source2.isIn(DamageTypeTags.IS_FIRE)) sound = SoundEvents.ENTITY_PLAYER_HURT_ON_FIRE;
                    else if (source2.isIn(DamageTypeTags.IS_DROWNING)) sound = SoundEvents.ENTITY_PLAYER_HURT_DROWN;
                    target.playSound(sound, 1F, getSoundPitch(target));
                }
            }
            return Pair.of(hit1, hit2);
        }
        return Pair.of(false, false);
    }

    private static float getSoundPitch(LivingEntity target) {
        return (target.getRandom().nextFloat() - target.getRandom().nextFloat()) * 0.2F + 1.0F;
    }

    private static void onHit2(LivingEntity target, DamageSource source) {
        if (source.isOf(DamageTypes.THORNS)) {
            target.getWorld().sendEntityStatus(target, (byte) 33);
        } else {
            byte b0;

            if (source.isIn(DamageTypeTags.IS_DROWNING)) {
                b0 = 36;
            } else if (source.isIn(DamageTypeTags.IS_FIRE)) {
                b0 = 37;
            } else {
                b0 = 2;
            }

            target.getWorld().sendEntityStatus(target, b0);
        }

        Entity entity1 = source.getAttacker();
        if (entity1 != null) {
            double d1 = entity1.getX() - target.getX();
            double d0;

            for (d0 = entity1.getZ() - target.getZ(); d1 * d1 + d0 * d0 < 1.0E-4D; d0 = (Math.random() - Math.random()) * 0.01D) {
                d1 = (Math.random() - Math.random()) * 0.01D;
            }

            if (target instanceof PlayerEntity) {
                ((PlayerEntity) target).damageTiltYaw = (float) (MathHelper.atan2(d0, d1) * (180D / Math.PI) - (double) target.getYaw());
            }
            target.takeKnockback(0.4F, d1, d0);
        } else {
            if (target instanceof PlayerEntity) {
                ((PlayerEntity) target).damageTiltYaw = (float) ((int) (Math.random() * 2.0D) * 180);
            }
        }
    }
}
