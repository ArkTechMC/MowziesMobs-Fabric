package com.bobmowzie.mowziesmobs.client.sound;

import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.sound.MovingSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.entity.Entity;
import net.minecraft.sound.SoundCategory;

/**
 * Created by BobMowzie on 1/9/2019.
 */
@Environment(EnvType.CLIENT)
public class NagaSwoopSound extends MovingSoundInstance {
    private final Entity naga;
    int ticksExisted = 0;
    boolean active = true;

    public NagaSwoopSound(Entity naga) {
        super(MMSounds.ENTITY_NAGA_SWOOP, SoundCategory.HOSTILE, SoundInstance.createRandom());
        this.naga = naga;
        this.volume = 2F;
        this.pitch = 1.2f;
        this.x = (float) naga.getX();
        this.y = (float) naga.getY();
        this.z = (float) naga.getZ();
        this.repeat = false;
    }

    @Override
    public void tick() {
        if (this.naga != null) {
            this.active = true;
            this.x = (float) this.naga.getX();
            this.y = (float) this.naga.getY();
            this.z = (float) this.naga.getZ();
            if (!this.naga.isAlive()) {
                this.active = false;
                this.setDone();
            }
        }
        this.ticksExisted++;
    }
}
