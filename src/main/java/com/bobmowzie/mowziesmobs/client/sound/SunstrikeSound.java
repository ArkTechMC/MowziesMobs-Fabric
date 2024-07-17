package com.bobmowzie.mowziesmobs.client.sound;

import com.bobmowzie.mowziesmobs.server.entity.effects.EntitySunstrike;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.sound.MovingSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.sound.SoundCategory;

@Environment(EnvType.CLIENT)
public class SunstrikeSound extends MovingSoundInstance {
    private final EntitySunstrike sunstrike;

    public SunstrikeSound(EntitySunstrike sunstrike) {
        super(MMSounds.SUNSTRIKE, SoundCategory.NEUTRAL, SoundInstance.createRandom());
        this.sunstrike = sunstrike;
        this.volume = 1.5F;
        this.pitch = 1.1F;
        this.x = (float) sunstrike.getX();
        this.y = (float) sunstrike.getY();
        this.z = (float) sunstrike.getZ();
    }

    @Override
    public void tick() {
        if (!this.sunstrike.isAlive()) {
            this.setDone();
        }
    }
}
