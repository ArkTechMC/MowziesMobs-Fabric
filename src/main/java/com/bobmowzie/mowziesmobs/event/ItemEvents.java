package com.bobmowzie.mowziesmobs.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class ItemEvents {
    public static final Event<FillBucket> FILL_BUCKET = EventFactory.createArrayBacked(FillBucket.class, callbacks -> (world, user, hand, filledBucket) -> {
        for (FillBucket callback : callbacks)
            if (callback.onFillBucket(world, user, hand, filledBucket))
                return true;
        return false;
    });

    @FunctionalInterface
    public interface FillBucket {
        boolean onFillBucket(World world, PlayerEntity user, Hand hand, ItemStack filledBucket);
    }
}
