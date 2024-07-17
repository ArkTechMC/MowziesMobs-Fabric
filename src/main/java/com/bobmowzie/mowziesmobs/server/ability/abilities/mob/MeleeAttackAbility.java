package com.bobmowzie.mowziesmobs.server.ability.abilities.mob;

import com.bobmowzie.mowziesmobs.server.ability.Ability;
import com.bobmowzie.mowziesmobs.server.ability.AbilitySection;
import com.bobmowzie.mowziesmobs.server.ability.AbilityType;
import com.bobmowzie.mowziesmobs.server.entity.MowzieGeckoEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.sound.SoundEvent;
import software.bernie.geckolib.core.animation.RawAnimation;

public class MeleeAttackAbility<T extends MowzieGeckoEntity> extends Ability<T> {
    protected SoundEvent attackSound;
    protected float knockBackMultiplier = 0;
    protected float range;
    protected float damageMultiplier;
    protected SoundEvent hitSound;
    protected boolean hurtInterrupts;
    protected RawAnimation[] animations;

    public MeleeAttackAbility(AbilityType<T, ? extends MeleeAttackAbility<T>> abilityType, T user, RawAnimation[] animations, SoundEvent attackSound, SoundEvent hitSound, float knockBackMultiplier, float range, float damageMultiplier, int startup, int recovery, boolean hurtInterrupts) {
        super(abilityType, user, new AbilitySection[]{
                new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.STARTUP, startup),
                new AbilitySection.AbilitySectionInstant(AbilitySection.AbilitySectionType.ACTIVE),
                new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.RECOVERY, recovery)
        }, 0);
        this.attackSound = attackSound;
        this.hitSound = hitSound;
        this.knockBackMultiplier = knockBackMultiplier;
        this.damageMultiplier = damageMultiplier;
        this.range = range;
        this.hurtInterrupts = hurtInterrupts;
        this.animations = animations;
    }

    @Override
    public void tickUsing() {
        super.tickUsing();
        if (this.getUser().getTarget() != null) {
            this.getUser().lookAtEntity(this.getUser().getTarget(), 30F, 30F);
            this.getUser().getLookControl().lookAt(this.getUser().getTarget(), 30F, 30F);
        }
        this.getUser().getNavigation().stop();
        this.getUser().getMoveControl().strafeTo(0, 0);
        this.getUser().setStrafing(false);
    }

    @Override
    public void start() {
        super.start();
        RawAnimation animation = this.animations[this.getUser().getRandom().nextInt(this.animations.length)];
        this.playAnimation(animation);
    }

    @Override
    protected void beginSection(AbilitySection section) {
        super.beginSection(section);
        if (section.sectionType == AbilitySection.AbilitySectionType.ACTIVE) {
            LivingEntity entityTarget = this.getUser().getTarget();
            if (entityTarget != null && this.getUser().targetDistance <= this.range) {
                this.getUser().doHurtTarget(entityTarget, this.damageMultiplier, this.knockBackMultiplier);
                this.onAttack(entityTarget, this.damageMultiplier, this.knockBackMultiplier);
                if (this.hitSound != null) {
                    this.getUser().playSound(this.hitSound, 1, 1);
                }
            }
            if (this.attackSound != null) {
                this.getUser().playSound(this.attackSound, 1, 1);
            }
        }
    }

    protected void onAttack(LivingEntity entityTarget, float damageMultiplier, float applyKnockbackMultiplier) {

    }

    @Override
    public boolean damageInterrupts() {
        return this.hurtInterrupts;
    }
}
