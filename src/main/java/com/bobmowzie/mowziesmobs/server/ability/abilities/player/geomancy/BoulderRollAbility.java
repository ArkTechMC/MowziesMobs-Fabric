package com.bobmowzie.mowziesmobs.server.ability.abilities.player.geomancy;

import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieGeoBone;
import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieGeoModel;
import com.bobmowzie.mowziesmobs.client.render.entity.player.GeckoPlayer;
import com.bobmowzie.mowziesmobs.server.ability.*;
import com.bobmowzie.mowziesmobs.server.potion.EffectHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

public class BoulderRollAbility extends PlayerAbility {
    private static final int START_UP = 15;
    float spinAmount = 0;

    private final RawAnimation ROLL_ANIM = RawAnimation.begin().thenLoop("boulder_roll_loop_still");

    public BoulderRollAbility(AbilityType<PlayerEntity, ? extends Ability> abilityType, PlayerEntity user) {
        super(abilityType, user, new AbilitySection[]{
                new AbilitySection.AbilitySectionInfinite(AbilitySection.AbilitySectionType.ACTIVE)
        });
    }

    @Override
    protected void beginSection(AbilitySection section) {
        super.beginSection(section);
    }

    @Override
    public <E extends GeoEntity> PlayState animationPredicate(AnimationState<E> e, GeckoPlayer.Perspective perspective) {
        e.getController().transitionLength(0);
        if (perspective == GeckoPlayer.Perspective.THIRD_PERSON) {
            e.getController().setAnimation(this.ROLL_ANIM);
        }
        return PlayState.CONTINUE;
    }


    @Override
    public void tickUsing() {
        super.tickUsing();
        if (this.getCurrentSection().sectionType == AbilitySection.AbilitySectionType.STARTUP) {
            this.getUser().setVelocity(this.getUser().getRotationVec(1f).normalize().multiply(0.3d, 0d, 0.3d));
        }
        if (this.getCurrentSection().sectionType == AbilitySection.AbilitySectionType.ACTIVE) {
            //playAnimation("boulder_roll_loop", true);
            this.getUser().setVelocity(this.getUser().getRotationVec(1f).normalize().multiply(1d, 0d, 1d));
        }
    }

    @Override
    public boolean tryAbility() {
        return super.tryAbility();
    }

    @Override
    public void onRightClickEmpty(PlayerEntity player, Hand hand) {
        super.onRightClickEmpty(player, hand);
        AbilityHandler.INSTANCE.sendPlayerTryAbilityMessage(player, AbilityHandler.BOULDER_ROLL_ABILITY);
    }

    @Override
    public void onRightMouseUp(PlayerEntity player) {
        super.onRightMouseUp(player);
        if (this.isUsing()) this.nextSection();
    }

    @Override
    public boolean canUse() {
        if (this.getUser() != null && !this.getUser().getInventory().getMainHandStack().isEmpty()) return false;
        return this.getUser().hasStatusEffect(EffectHandler.GEOMANCY) && this.getUser().isSprinting() && super.canUse();
    }

    @Override
    public void codeAnimations(MowzieGeoModel<? extends GeoEntity> model, float partialTick) {
        super.codeAnimations(model, partialTick);
        float spinSpeed = 0.35f;
        this.spinAmount += partialTick * spinSpeed;
        MowzieGeoBone centerOfMass = model.getMowzieBone("CenterOfMass");
        centerOfMass.addRotX(-this.spinAmount);
    }
}
