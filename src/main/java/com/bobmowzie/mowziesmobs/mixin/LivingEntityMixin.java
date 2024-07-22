package com.bobmowzie.mowziesmobs.mixin;

import com.bobmowzie.mowziesmobs.event.LivingEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(method = "tick", at = @At("HEAD"))
    private void beforeTick(CallbackInfo ci) {
        LivingEvents.BEFORE_TICK.invoker().onLivingEntityBeforeTick((LivingEntity) (Object) this);
    }

    @Inject(method = "tick", at = @At("RETURN"))
    private void afterTick(CallbackInfo ci) {
        LivingEvents.AFTER_TICK.invoker().onLivingEntityAfterTick((LivingEntity) (Object) this);
    }

    @Inject(method = "addStatusEffect(Lnet/minecraft/entity/effect/StatusEffectInstance;Lnet/minecraft/entity/Entity;)Z", at = @At(value = "INVOKE", target = "Ljava/util/Map;get(Ljava/lang/Object;)Ljava/lang/Object;"))
    private void onAddEffect(StatusEffectInstance effect, Entity source, CallbackInfoReturnable<Boolean> cir) {
        LivingEvents.ADD_EFFECT.invoker().onLivingEntityAddEffect((LivingEntity) (Object) this, effect, source);
    }

    @Inject(method = "onStatusEffectRemoved", at = @At("HEAD"))
    private void onClearEffect(StatusEffectInstance effect, CallbackInfo ci) {
        LivingEvents.REMOVE_EFFECT.invoker().onLivingEntityRemoveEffect((LivingEntity) (Object) this, effect);
    }
}
