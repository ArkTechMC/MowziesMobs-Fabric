package com.bobmowzie.mowziesmobs.server.ability.abilities.player;

import com.bobmowzie.mowziesmobs.server.ability.AbilitySection;
import com.bobmowzie.mowziesmobs.server.ability.AbilityType;
import com.bobmowzie.mowziesmobs.server.ability.PlayerAbility;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntityAxeAttack;
import net.minecraft.entity.player.PlayerEntity;
import software.bernie.geckolib.core.animation.RawAnimation;

import static com.bobmowzie.mowziesmobs.server.entity.effects.EntityAxeAttack.SWING_DURATION_VER;

public class WroughtAxeSlamAbility extends PlayerAbility {
    private static final RawAnimation AXE_SWING_VERTICAL_ANIM = RawAnimation.begin().thenPlay("axe_swing_vertical");
    private EntityAxeAttack axeAttack;

    public WroughtAxeSlamAbility(AbilityType<PlayerEntity, WroughtAxeSlamAbility> abilityType, PlayerEntity user) {
        super(abilityType, user, new AbilitySection[]{
                new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.STARTUP, SWING_DURATION_VER / 2 - 2),
                new AbilitySection.AbilitySectionInstant(AbilitySection.AbilitySectionType.ACTIVE),
                new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.RECOVERY, SWING_DURATION_VER / 2 + 2)
        });
    }

    @Override
    public void start() {
        super.start();
        if (!this.getUser().getWorld().isClient()) {
            EntityAxeAttack axeAttack = new EntityAxeAttack(EntityHandler.AXE_ATTACK, this.getUser().getWorld(), this.getUser(), true);
            axeAttack.updatePositionAndAngles(this.getUser().getX(), this.getUser().getY(), this.getUser().getZ(), this.getUser().getYaw(), this.getUser().getPitch());
            this.getUser().getWorld().spawnEntity(axeAttack);
            this.axeAttack = axeAttack;
        } else {
            this.playAnimation(AXE_SWING_VERTICAL_ANIM);
            this.heldItemMainHandVisualOverride = this.getUser().getMainHandStack();
        }
    }

    @Override
    public void tickUsing() {
        super.tickUsing();
        if (this.getTicksInUse() == SWING_DURATION_VER && this.getUser() instanceof PlayerEntity) {
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
