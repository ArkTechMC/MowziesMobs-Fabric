package com.bobmowzie.mowziesmobs.server.potion;

import net.minecraft.entity.effect.StatusEffectCategory;

/**
 * Created by BobMowzie on 1/9/2019.
 */
public class EffectPoisonResist extends MowzieEffect {
    public EffectPoisonResist() {
        super(StatusEffectCategory.BENEFICIAL, 0x66ff33);
    }
}
