package com.bobmowzie.mowziesmobs.mixin;

import com.bobmowzie.mowziesmobs.event.ItemEvents;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BucketItem.class)
public class BucketItemMixin {
    @Inject(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;incrementStat(Lnet/minecraft/stat/Stat;)V", ordinal = 0), cancellable = true)
    private void onFillSuccess(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir, @Local(ordinal = 1) ItemStack filledStack) {
        if (ItemEvents.FILL_BUCKET.invoker().onFillBucket(world, user, hand, filledStack))
            cir.setReturnValue(TypedActionResult.fail(filledStack));
    }
}
