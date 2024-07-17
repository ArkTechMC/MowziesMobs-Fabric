package com.bobmowzie.mowziesmobs.server.potion;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public final class EffectHandler {
    public static final EffectSunsBlessing SUNS_BLESSING = register("suns_blessing", new EffectSunsBlessing());
    public static final EffectGeomancy GEOMANCY = register("geomancy", new EffectGeomancy());
    public static final EffectFrozen FROZEN = register("frozen", new EffectFrozen());
    public static final EffectPoisonResist POISON_RESIST = register("poison_resist", new EffectPoisonResist());
    public static final EffectSunblock SUNBLOCK = register("sunblock", new EffectSunblock());

    public static <T extends StatusEffect> T register(String name, T effect) {
        return Registry.register(Registries.STATUS_EFFECT, new Identifier(MowziesMobs.MODID, name), effect);
    }

    public static void init() {
    }

    public static void addOrCombineEffect(LivingEntity entity, StatusEffect effect, int duration, int amplifier, boolean ambient, boolean showParticles) {
        if (effect == null) return;
        StatusEffectInstance effectInst = entity.getStatusEffect(effect);
        StatusEffectInstance newEffect = new StatusEffectInstance(effect, duration, amplifier, ambient, showParticles);
        if (effectInst != null) effectInst.upgrade(newEffect);
        else entity.addStatusEffect(newEffect);
    }
}
