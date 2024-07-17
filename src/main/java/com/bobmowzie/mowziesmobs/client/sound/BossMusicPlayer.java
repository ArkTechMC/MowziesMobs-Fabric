package com.bobmowzie.mowziesmobs.client.sound;

import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;

public class BossMusicPlayer {
    public static BossMusicSound bossMusic;

    public static void playBossMusic(MowzieEntity entity) {
        if (!ConfigHandler.CLIENT.playBossMusic) return;

        SoundEvent soundEvent = entity.getBossMusic();
        if (soundEvent != null && entity.isAlive()) {
            PlayerEntity player = MinecraftClient.getInstance().player;
            if (bossMusic != null) {
                float f2 = MinecraftClient.getInstance().options.getSoundVolume(SoundCategory.MUSIC);
                if (f2 <= 0) {
                    bossMusic = null;
                } else if (bossMusic.getBoss() == entity && !entity.canPlayerHearMusic(player)) {
                    bossMusic.setBoss(null);
                } else if (bossMusic.getBoss() == null && bossMusic.getSoundEvent() == soundEvent) {
                    bossMusic.setBoss(entity);
                }
            } else {
                if (entity.canPlayerHearMusic(player)) {
                    bossMusic = new BossMusicSound(entity.getBossMusic(), entity);
                }
            }
            if (bossMusic != null && !MinecraftClient.getInstance().getSoundManager().isPlaying(bossMusic)) {
                MinecraftClient.getInstance().getSoundManager().play(bossMusic);
            }
        }
    }

    public static void stopBossMusic(MowzieEntity entity) {
        if (!ConfigHandler.CLIENT.playBossMusic.get()) return;

        if (bossMusic != null && bossMusic.getBoss() == entity)
            bossMusic.setBoss(null);
    }
}
