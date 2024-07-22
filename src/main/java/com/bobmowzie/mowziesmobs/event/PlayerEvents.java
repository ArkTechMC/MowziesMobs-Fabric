package com.bobmowzie.mowziesmobs.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;

public class PlayerEvents {
    public static final Event<BeforeTick> BEFORE_TICK = EventFactory.createArrayBacked(BeforeTick.class, callbacks -> entity -> {
        for (BeforeTick callback : callbacks)
            callback.onPlayerEntityBeforeTick(entity);
    });
    public static final Event<AfterTick> AFTER_TICK = EventFactory.createArrayBacked(AfterTick.class, callbacks -> entity -> {
        for (AfterTick callback : callbacks)
            callback.onPlayerEntityAfterTick(entity);
    });
    public static final Event<Respawn> RESPAWN = EventFactory.createArrayBacked(Respawn.class, callbacks -> player -> {
        for (Respawn callback : callbacks)
            callback.onPlayerRespawn(player);
    });

    @FunctionalInterface
    public interface BeforeTick {
        void onPlayerEntityBeforeTick(PlayerEntity entity);
    }

    @FunctionalInterface
    public interface AfterTick {
        void onPlayerEntityAfterTick(PlayerEntity entity);
    }

    @FunctionalInterface
    public interface Respawn {
        void onPlayerRespawn(ServerPlayerEntity player);
    }
}
