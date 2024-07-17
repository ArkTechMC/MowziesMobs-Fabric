package com.bobmowzie.mowziesmobs.client.sound;

import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import com.ilexiconn.llibrary.client.model.tools.ControlledAnimation;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.MovingSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;

public class BossMusicSound extends MovingSoundInstance {
    private final SoundEvent soundEvent;
    ControlledAnimation volumeControl;
    private MowzieEntity boss;
    private int ticksExisted = 0;
    private int timeUntilFade;

    public BossMusicSound(SoundEvent sound, MowzieEntity boss) {
        super(sound, SoundCategory.MUSIC, SoundInstance.createRandom());
        this.boss = boss;
        this.soundEvent = sound;
        this.attenuationType = AttenuationType.NONE;
        this.repeat = true;
        this.repeatDelay = 0;
        this.x = boss.getX();
        this.y = boss.getY();
        this.z = boss.getZ();

        this.volumeControl = new ControlledAnimation(40);
        this.volumeControl.setTimer(20);
        this.volume = this.volumeControl.getAnimationFraction();
        this.timeUntilFade = 80;
    }

    public boolean canPlay() {
        return BossMusicPlayer.bossMusic == this;
    }

    public void tick() {
        // If the music should stop playing
        if (this.boss == null || !this.boss.isAlive() || this.boss.isSilent()) {
            // If the boss is dead, skip the fade timer and fade out right away
            if (this.boss != null && !this.boss.isAlive()) this.timeUntilFade = 0;
            this.boss = null;
            if (this.timeUntilFade > 0) this.timeUntilFade--;
            else this.volumeControl.decreaseTimer();
        }
        // If the music should keep playing
        else {
            this.volumeControl.increaseTimer();
            this.timeUntilFade = 60;
        }

        if (this.volumeControl.getAnimationFraction() < 0.025) {
            this.setDone();
            BossMusicPlayer.bossMusic = null;
        }

        this.volume = this.volumeControl.getAnimationFraction();

        if (this.ticksExisted % 100 == 0) {
            MinecraftClient.getInstance().getMusicTracker().stop();
        }
        this.ticksExisted++;
    }

    public MowzieEntity getBoss() {
        return this.boss;
    }

    public void setBoss(MowzieEntity boss) {
        this.boss = boss;
    }

    public SoundEvent getSoundEvent() {
        return this.soundEvent;
    }
}