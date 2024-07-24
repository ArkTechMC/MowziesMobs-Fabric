package com.bobmowzie.mowziesmobs.mixin;

import com.bobmowzie.mowziesmobs.client.ClientProxy;
import com.bobmowzie.mowziesmobs.client.gui.CustomBossBar;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.BossBarHud;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BossBarHud.class)
public class BossBarHudMixin {
    @Inject(method = "renderBossBar(Lnet/minecraft/client/gui/DrawContext;IILnet/minecraft/entity/boss/BossBar;II)V", at = @At("HEAD"), cancellable = true)
    private void onRenderHud(DrawContext context, int x, int y, BossBar bossBar, int width, int height, CallbackInfo ci) {
        if (!ConfigHandler.CLIENT.customBossBars) return;
        Identifier bossRegistryName = ClientProxy.bossBarRegistryNames.getOrDefault(bossBar.getUuid(), null);
        if (bossRegistryName == null) return;
        CustomBossBar customBossBar = CustomBossBar.customBossBars.getOrDefault(bossRegistryName, null);
        if (customBossBar == null) return;
        customBossBar.renderBossBar(context, x, y, bossBar, width, height);
        ci.cancel();
    }
}
