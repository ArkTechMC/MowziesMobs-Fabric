package com.bobmowzie.mowziesmobs.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;

public interface UseEmptyCallback {
    Event<UseEmptyCallback> EVENT = EventFactory.createArrayBacked(UseEmptyCallback.class, callbacks -> (player, hand) -> {
        for (UseEmptyCallback callback : callbacks)
            callback.onUseAir(player, hand);
    });

    void onUseAir(PlayerEntity player, Hand hand);
}
