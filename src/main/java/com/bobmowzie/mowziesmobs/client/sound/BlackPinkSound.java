package com.bobmowzie.mowziesmobs.client.sound;

import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.sound.MovingSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.sound.SoundCategory;

@Environment(EnvType.CLIENT)
public final class BlackPinkSound extends MovingSoundInstance {
    private final AbstractMinecartEntity minecart;

    public BlackPinkSound(AbstractMinecartEntity minecart) {
        super(MMSounds.MUSIC_BLACK_PINK, SoundCategory.NEUTRAL, SoundInstance.createRandom());
        this.minecart = minecart;
    }

    @Override
    public void tick() {
        if (this.minecart.isAlive()) {
            this.x = (float) this.minecart.getX();
            this.y = (float) this.minecart.getY();
            this.z = (float) this.minecart.getZ();
        } else {
            this.setDone();
        }
    }
}
