package com.bobmowzie.mowziesmobs.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.player.PlayerEntity;

public class PlayerEvents {
    public static final Event<BeforeTick> BEFORE_TICK = EventFactory.createArrayBacked(BeforeTick.class, callbacks -> entity -> {
        for (BeforeTick callback : callbacks)
            callback.onPlayerEntityBeforeTick(entity);
    });
    public static final Event<AfterTick> AFTER_TICK = EventFactory.createArrayBacked(AfterTick.class, callbacks -> entity -> {
        for (AfterTick callback : callbacks)
            callback.onPlayerEntityAfterTick(entity);
    });

    @FunctionalInterface
    public interface BeforeTick {
        void onPlayerEntityBeforeTick(PlayerEntity entity);
    }

    @FunctionalInterface
    public interface AfterTick {
        void onPlayerEntityAfterTick(PlayerEntity entity);
    }
}
