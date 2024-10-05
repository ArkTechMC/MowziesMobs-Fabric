package com.bobmowzie.mowziesmobs.server.ability.abilities.player;

import com.bobmowzie.mowziesmobs.client.render.entity.player.GeckoPlayer;
import com.bobmowzie.mowziesmobs.server.ability.AbilitySection;
import com.bobmowzie.mowziesmobs.server.ability.AbilityType;
import com.bobmowzie.mowziesmobs.server.ability.PlayerAbility;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntityAxeAttack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Arm;
import software.bernie.geckolib.core.animation.Animation;

import static com.bobmowzie.mowziesmobs.server.entity.effects.EntityAxeAttack.SWING_DURATION_HOR;

public class WroughtAxeSwingAbility extends PlayerAbility {
    private EntityAxeAttack axeAttack;

    public WroughtAxeSwingAbility(AbilityType<PlayerEntity, WroughtAxeSwingAbility> abilityType, PlayerEntity user) {
        super(abilityType, user, new AbilitySection[]{
                new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.STARTUP, SWING_DURATION_HOR / 2 - 2),
                new AbilitySection.AbilitySectionInstant(AbilitySection.AbilitySectionType.ACTIVE),
                new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.RECOVERY, SWING_DURATION_HOR / 2 + 2 + 7)
        });
    }

    @Override
    public void start() {
        super.start();
        if (!this.getUser().getWorld().isClient()) {
            EntityAxeAttack axeAttack = new EntityAxeAttack(EntityHandler.AXE_ATTACK, this.getUser().getWorld(), this.getUser(), false);
            axeAttack.updatePositionAndAngles(this.getUser().getX(), this.getUser().getY(), this.getUser().getZ(), this.getUser().getYaw(), this.getUser().getPitch());
            this.getUser().getWorld().spawnEntity(axeAttack);
            this.axeAttack = axeAttack;
        } else {
            boolean handSide = this.getUser().getMainArm() == Arm.RIGHT;
            this.playAnimation("axe_swing_start_" + (handSide ? "right" : "left"), GeckoPlayer.Perspective.THIRD_PERSON, Animation.LoopType.PLAY_ONCE);
            this.playAnimation("axe_swing_start", GeckoPlayer.Perspective.FIRST_PERSON, Animation.LoopType.PLAY_ONCE);
            this.heldItemMainHandVisualOverride = this.getUser().getMainHandStack();
        }
    }

    @Override
    public void tickUsing() {
        super.tickUsing();
        if (this.getTicksInUse() == SWING_DURATION_HOR && this.getUser() instanceof PlayerEntity) {
            PlayerEntity player = this.getUser();
            player.resetLastAttackedTicks();
        }
    }

    @Override
    public void end() {
        super.end();
        if (this.axeAttack != null) {
            this.axeAttack.discard();
        }
    }

    @Override
    public boolean preventsAttacking() {
        return false;
    }
}
