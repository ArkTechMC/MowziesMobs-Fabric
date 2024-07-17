package com.bobmowzie.mowziesmobs.server.potion;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectCategory;

public class EffectSunblock extends MowzieEffect {
    public EffectSunblock() {
        super(StatusEffectCategory.BENEFICIAL, 0xFFDF42);
    }

    @Override
    public void applyUpdateEffect(LivingEntity entityLivingBaseIn, int amplifier) {
        super.applyUpdateEffect(entityLivingBaseIn, amplifier);
        int k = 50 >> amplifier;
        if (k > 0 && entityLivingBaseIn.age % k == 0) {
            if (entityLivingBaseIn.getHealth() < entityLivingBaseIn.getMaxHealth()) {
                entityLivingBaseIn.heal(1.0F);
            }
        }
    }
}
