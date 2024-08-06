package com.bobmowzie.mowziesmobs.server.ability.abilities.player.geomancy;

import com.bobmowzie.mowziesmobs.client.particle.ParticleHandler;
import com.bobmowzie.mowziesmobs.client.particle.util.AdvancedParticleBase;
import com.bobmowzie.mowziesmobs.client.particle.util.ParticleComponent;
import com.bobmowzie.mowziesmobs.server.ability.*;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntityCameraShake;
import com.bobmowzie.mowziesmobs.server.potion.EffectGeomancy;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import software.bernie.geckolib.core.animation.RawAnimation;

public class GroundSlamAbility extends PlayerAbility {
    private static final RawAnimation GROUND_POUND_LOOP_ANIM = RawAnimation.begin().thenLoop("ground_pound_loop");
    private static final RawAnimation GROUND_POUND_LAND_ANIM = RawAnimation.begin().thenPlay("ground_pound_land");
    public GroundSlamAbility(AbilityType<PlayerEntity, ? extends Ability> abilityType, PlayerEntity user) {
        super(abilityType, user, new AbilitySection[]{
                new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.STARTUP, 2),
                new AbilitySection.AbilitySectionInfinite(AbilitySection.AbilitySectionType.ACTIVE),
                new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.RECOVERY, 21)
        });
    }

    @Override
    public void start() {
        super.start();
        this.playAnimation(GROUND_POUND_LOOP_ANIM);

    }

    @Override
    public void tickUsing() {
        super.tickUsing();
        if (this.getCurrentSection().sectionType == AbilitySection.AbilitySectionType.STARTUP) {
            //getUser().setDeltaMovement(0d,0d,0d);
        }
        if (this.getCurrentSection().sectionType == AbilitySection.AbilitySectionType.ACTIVE) {
            this.getUser().setVelocity(0d, -1.5d, 0d);
            if (this.getUser().isOnGround()) {
                this.nextSection();
                for (LivingEntity livingentity : this.getUser().getWorld().getNonSpectatingEntities(LivingEntity.class, this.getUser().getBoundingBox().expand(5.2D, 2.0D, 5.2D))) {
                    livingentity.damage(this.getUser().getDamageSources().mobAttack(this.getUser()), 10f);
                }

                EntityCameraShake.cameraShake(this.getUser().getWorld(), this.getUser().getPos(), 45, 0.09f, 20, 20);

                BlockState blockBeneath = this.getUser().getWorld().getBlockState(this.getUser().getBlockPos());
                if (this.getUser().getWorld().isClient) {
                    this.getUser().playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 1.5f, 1.0f);
                    for (int i = 0; i < 50; i++) {
                        this.getUser().getWorld().addParticle(new BlockStateParticleEffect(ParticleTypes.BLOCK, blockBeneath), this.getUser().getParticleX(5.8D), this.getUser().getBlockY() + 0.1f, this.getUser().getParticleZ(5.8D), 0, 0.38d, 0);
                        this.getUser().getWorld().addParticle(ParticleTypes.POOF, this.getUser().getParticleX(5f), this.getUser().getY(), this.getUser().getParticleZ(5f), 0d, 0.08d, 0d);
                    }
                    AdvancedParticleBase.spawnParticle(this.getUser().getWorld(), ParticleHandler.RING2, (float) this.getUser().getX(), (float) this.getUser().getY() + 0.01f, (float) this.getUser().getZ(), 0, 0, 0, false, 0, Math.PI / 2f, 0, 0, 3.5F, 0.83f, 1, 0.39f, 1, 1, 10, true, true, new ParticleComponent[]{
                            new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.ALPHA, ParticleComponent.KeyTrack.startAndEnd(0.8f, 0.0f), false),
                            new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.SCALE, ParticleComponent.KeyTrack.startAndEnd(0, (0.8f + 2.7f * 20f / 60f) * 80f), false)
                    });
                }
            }
        }
        if (this.getCurrentSection().sectionType == AbilitySection.AbilitySectionType.RECOVERY) {
            //playAnimation("ground_pound_land", true);
            this.getUser().setVelocity(0d, 0d, 0d);
        }
    }

    @Override
    public boolean canUse() {
        if (this.getUser() instanceof PlayerEntity && !this.getUser().getInventory().getMainHandStack().isEmpty())
            return false;
        return EffectGeomancy.canUse(this.getUser()) && this.getUser().fallDistance > 2 && super.canUse();
    }

    @Override
    public void nextSection() {
        super.nextSection();
        if (this.getCurrentSection().sectionType == AbilitySection.AbilitySectionType.ACTIVE) {
            //playAnimation("ground_pound_loop", true);
        }
        if (this.getCurrentSection().sectionType == AbilitySection.AbilitySectionType.RECOVERY) {
            this.playAnimation(GROUND_POUND_LAND_ANIM);
        }
    }

    @Override
    public void onRightClickEmpty(PlayerEntity player, Hand hand) {
        super.onRightClickEmpty(player, hand);
        if (!this.getUser().isOnGround() && this.getUser().isInSneakingPose()) {
            AbilityHandler.INSTANCE.sendPlayerTryAbilityMessage(player, AbilityHandler.GROUND_SLAM_ABILITY);
        }
    }
}
