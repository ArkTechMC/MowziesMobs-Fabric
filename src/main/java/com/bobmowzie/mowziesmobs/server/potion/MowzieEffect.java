package com.bobmowzie.mowziesmobs.server.potion;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.util.Identifier;

public class MowzieEffect extends StatusEffect {
    private static final Identifier TEXTURE = new Identifier(MowziesMobs.MODID, "textures/gui/container/potions.png");

    public MowzieEffect(StatusEffectCategory type, int liquidColor) {
        super(type, liquidColor);
    }

    @Override
    public boolean canApplyUpdateEffect(int id, int amplifier) {
        return true;
    }
}
