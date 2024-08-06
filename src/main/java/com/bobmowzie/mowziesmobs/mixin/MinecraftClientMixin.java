package com.bobmowzie.mowziesmobs.mixin;

import com.bobmowzie.mowziesmobs.event.UseEmptyCallback;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.HitResult;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
    @Shadow
    @Nullable
    public HitResult crosshairTarget;

    @Shadow
    @Nullable
    public ClientPlayerEntity player;

    @Inject(method = "doItemUse", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isItemEnabled(Lnet/minecraft/resource/featuretoggle/FeatureSet;)Z"))
    private void hookItemUseEmpty(CallbackInfo ci, @Local(name = "itemStack") ItemStack itemstack, @Local(name = "hand") Hand hand) {
        if (itemstack.isEmpty() && (this.crosshairTarget == null || this.crosshairTarget.getType() == HitResult.Type.MISS)) {
            UseEmptyCallback.EVENT.invoker().onUseAir(this.player, hand);
        }
    }
}
