package com.bobmowzie.mowziesmobs.server.ability.abilities.player.heliomancy;

import com.bobmowzie.mowziesmobs.server.ability.Ability;
import com.bobmowzie.mowziesmobs.server.ability.AbilitySection;
import com.bobmowzie.mowziesmobs.server.ability.AbilityType;
import com.bobmowzie.mowziesmobs.server.ability.PlayerAbility;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.potion.EffectHandler;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;

public abstract class HeliomancyAbilityBase extends PlayerAbility {
    public HeliomancyAbilityBase(AbilityType<PlayerEntity, ? extends Ability> abilityType, PlayerEntity user, AbilitySection[] sectionTrack, int cooldownMax) {
        super(abilityType, user, sectionTrack, cooldownMax);
    }

    public HeliomancyAbilityBase(AbilityType<PlayerEntity, ? extends Ability> abilityType, PlayerEntity user, AbilitySection[] sectionTrack) {
        super(abilityType, user, sectionTrack);
    }

    @Override
    public void start() {
        super.start();
        StatusEffectInstance effectInstance = this.getUser().getStatusEffect(EffectHandler.SUNS_BLESSING);
        if (effectInstance != null && effectInstance.isInfinite()) {
            this.getUser().removeStatusEffect(EffectHandler.SUNS_BLESSING);
            this.getUser().addStatusEffect(new StatusEffectInstance(EffectHandler.SUNS_BLESSING, ConfigHandler.COMMON.TOOLS_AND_ABILITIES.SUNS_BLESSING.effectDuration * 60 * 20, 0, false, false));
        }
    }

    @Override
    public boolean canUse() {
        if (this.getUser() == null || !this.getUser().getInventory().getMainHandStack().isEmpty()) return false;
        return this.getUser().hasStatusEffect(EffectHandler.SUNS_BLESSING) && super.canUse();
    }
}
