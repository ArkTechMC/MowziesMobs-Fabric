package com.bobmowzie.mowziesmobs.server.ability.abilities.player;

import com.bobmowzie.mowziesmobs.client.render.entity.player.GeckoPlayer;
import com.bobmowzie.mowziesmobs.server.ability.AbilitySection;
import com.bobmowzie.mowziesmobs.server.ability.AbilityType;
import com.bobmowzie.mowziesmobs.server.ability.PlayerAbility;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Arm;
import software.bernie.geckolib.core.animation.Animation;
import software.bernie.geckolib.core.animation.RawAnimation;

public class SimplePlayerAnimationAbility extends PlayerAbility {
    private final String animationName;
    private boolean separateLeftAndRight;
    private boolean lockHeldItemMainHand;

    public SimplePlayerAnimationAbility(AbilityType<PlayerEntity, SimplePlayerAnimationAbility> abilityType, PlayerEntity user, String animationName, int duration) {
        super(abilityType, user, new AbilitySection[]{
                new AbilitySection.AbilitySectionInstant(AbilitySection.AbilitySectionType.ACTIVE),
                new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.RECOVERY, duration)
        });
        this.animationName = animationName;
    }

    public SimplePlayerAnimationAbility(AbilityType<PlayerEntity, SimplePlayerAnimationAbility> abilityType, PlayerEntity user, String animationName, int duration, boolean separateLeftAndRight, boolean lockHeldItemMainHand) {
        super(abilityType, user, new AbilitySection[]{
                new AbilitySection.AbilitySectionInstant(AbilitySection.AbilitySectionType.ACTIVE),
                new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.RECOVERY, duration)
        });
        this.animationName = animationName;
        this.separateLeftAndRight = separateLeftAndRight;
        this.lockHeldItemMainHand = lockHeldItemMainHand;
    }

    @Override
    public void start() {
        super.start();
        if (this.separateLeftAndRight) {
            boolean handSide = this.getUser().getMainArm() == Arm.RIGHT;
            this.playAnimation(this.animationName + "_" + (handSide ? "right" : "left"), GeckoPlayer.Perspective.THIRD_PERSON, Animation.LoopType.PLAY_ONCE);
            this.playAnimation(this.animationName, GeckoPlayer.Perspective.FIRST_PERSON, Animation.LoopType.PLAY_ONCE);
        } else {
            this.playAnimation(RawAnimation.begin().thenPlay(this.animationName));
        }
        if (this.lockHeldItemMainHand)
            this.heldItemMainHandVisualOverride = this.getUser().getMainHandStack();
    }
}
