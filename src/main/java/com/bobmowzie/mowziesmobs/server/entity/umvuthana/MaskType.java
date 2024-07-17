package com.bobmowzie.mowziesmobs.server.entity.umvuthana;

import com.google.common.base.Defaults;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;

import java.util.EnumMap;
import java.util.Locale;

public enum MaskType {
    FURY(StatusEffects.STRENGTH, 0.9F, 2F, true),
    FEAR(StatusEffects.SPEED),
    RAGE(StatusEffects.HASTE),
    BLISS(StatusEffects.JUMP_BOOST),
    MISERY(StatusEffects.RESISTANCE),
    FAITH(StatusEffects.HEALTH_BOOST, 0.9F, 2F, false);

    public static final int COUNT = MaskType.values().length;

    public final StatusEffect potion;

    public final float entityWidth;

    public final float entityHeight;

    public final boolean canBlock;

    public final String name;

    MaskType(StatusEffect potion) {
        this(potion, 0.8F, 1.8F, false);
    }

    MaskType(StatusEffect potion, float entityWidth, float entityHeight, boolean canBlock) {
        this.potion = potion;
        this.entityWidth = entityWidth;
        this.entityHeight = entityHeight;
        this.canBlock = canBlock;
        this.name = this.name().toLowerCase(Locale.ENGLISH);
    }

    public static MaskType from(int id) {
        if (id < 0 || id >= COUNT) {
            return MISERY;
        }
        return values()[id];
    }

    @SafeVarargs
    public static <T> EnumMap<MaskType, T> newEnumMap(Class<T> type, T... defaultValues) {
        EnumMap<MaskType, T> map = new EnumMap<>(MaskType.class);
        MaskType[] masks = values();
        for (int i = 0; i < masks.length; i++)
            map.put(masks[i], i < defaultValues.length ? defaultValues[i] : Defaults.defaultValue(type));
        return map;
    }
}
