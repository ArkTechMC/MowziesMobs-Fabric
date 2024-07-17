package com.bobmowzie.mowziesmobs.client.sound;

import com.bobmowzie.mowziesmobs.server.entity.effects.EntitySolarBeam;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.sound.MovingSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.sound.SoundCategory;

@Environment(EnvType.CLIENT)
public class SolarBeamSound extends MovingSoundInstance {
    private final EntitySolarBeam solarBeam;
    boolean endLocation = false;

    public SolarBeamSound(EntitySolarBeam solarBeam, boolean endLocation) {
        super(MMSounds.LASER, SoundCategory.NEUTRAL, SoundInstance.createRandom());
        this.solarBeam = solarBeam;
        this.volume = 2F;
        this.pitch = 1.0F;
        this.endLocation = endLocation;
    }

    @Override
    public void tick() {
        if (!this.solarBeam.isAlive()) {
            this.setDone();
        }
        if (this.endLocation && this.solarBeam.hasDoneRaytrace()) {
            this.x = (float) this.solarBeam.collidePosX;
            this.y = (float) this.solarBeam.collidePosY;
            this.z = (float) this.solarBeam.collidePosZ;
        } else {
            this.x = (float) this.solarBeam.getX();
            this.y = (float) this.solarBeam.getY();
            this.z = (float) this.solarBeam.getZ();
        }
    }
}