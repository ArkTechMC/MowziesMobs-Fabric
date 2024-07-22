package com.bobmowzie.mowziesmobs.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LivingEvents {
    public static final Event<BeforeTick> BEFORE_TICK = EventFactory.createArrayBacked(BeforeTick.class, callbacks -> entity -> {
        for (BeforeTick callback : callbacks)
            callback.onLivingEntityBeforeTick(entity);
    });
    public static final Event<AfterTick> AFTER_TICK = EventFactory.createArrayBacked(AfterTick.class, callbacks -> entity -> {
        for (AfterTick callback : callbacks)
            callback.onLivingEntityAfterTick(entity);
    });
    public static final Event<AddEffect> ADD_EFFECT = EventFactory.createArrayBacked(AddEffect.class, callbacks -> (entity, instance, source) -> {
        for (AddEffect callback : callbacks)
            callback.onLivingEntityAddEffect(entity, instance, source);
    });
    public static final Event<RemoveEffect> REMOVE_EFFECT = EventFactory.createArrayBacked(RemoveEffect.class, callbacks -> (entity, instance) -> {
        for (RemoveEffect callback : callbacks)
            callback.onLivingEntityRemoveEffect(entity, instance);
    });

    @FunctionalInterface
    public interface BeforeTick {
        void onLivingEntityBeforeTick(LivingEntity entity);
    }

    @FunctionalInterface
    public interface AfterTick {
        void onLivingEntityAfterTick(LivingEntity entity);
    }

    @FunctionalInterface
    public interface AddEffect {
        void onLivingEntityAddEffect(LivingEntity entity, StatusEffectInstance instance, @Nullable Entity source);
    }

    @FunctionalInterface
    public interface RemoveEffect {
        void onLivingEntityRemoveEffect(LivingEntity entity, @NotNull StatusEffectInstance instance);
    }
}
