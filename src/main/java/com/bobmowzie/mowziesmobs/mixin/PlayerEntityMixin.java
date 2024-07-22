package com.bobmowzie.mowziesmobs.mixin;

import com.bobmowzie.mowziesmobs.event.PlayerEvents;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    @Inject(method = "tick", at = @At("HEAD"))
    private void beforeTick(CallbackInfo ci) {
        PlayerEvents.BEFORE_TICK.invoker().onPlayerEntityBeforeTick((PlayerEntity) (Object) this);
    }

    @Inject(method = "tick", at = @At("RETURN"))
    private void afterTick(CallbackInfo ci) {
        PlayerEvents.AFTER_TICK.invoker().onPlayerEntityAfterTick((PlayerEntity) (Object) this);
    }
}
