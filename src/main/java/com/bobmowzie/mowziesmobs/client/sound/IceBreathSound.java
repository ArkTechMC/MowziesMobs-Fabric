package com.bobmowzie.mowziesmobs.client.sound;

import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import com.iafenvoy.uranus.client.model.tools.ControlledAnimation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.sound.MovingSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.entity.Entity;
import net.minecraft.sound.SoundCategory;

@Environment(EnvType.CLIENT)
public class IceBreathSound extends MovingSoundInstance {
    private final Entity iceBreath;
    int ticksExisted = 0;
    ControlledAnimation volumeControl;
    boolean active = true;

    public IceBreathSound(Entity icebreath) {
        super(MMSounds.ENTITY_FROSTMAW_ICEBREATH, SoundCategory.NEUTRAL, SoundInstance.createRandom());
        this.iceBreath = icebreath;
        this.volume = 3F;
        this.pitch = 1f;
        this.x = (float) icebreath.getX();
        this.y = (float) icebreath.getY();
        this.z = (float) icebreath.getZ();
        this.volumeControl = new ControlledAnimation(10);
        this.repeat = true;
    }

    @Override
    public void tick() {
        if (this.active) this.volumeControl.increaseTimer();
        else this.volumeControl.decreaseTimer();
        this.volume = this.volumeControl.getAnimationFraction();
        if (this.volumeControl.getAnimationFraction() <= 0.05)
            this.setDone();
        if (this.iceBreath != null) {
            this.active = true;
            this.x = (float) this.iceBreath.getX();
            this.y = (float) this.iceBreath.getY();
            this.z = (float) this.iceBreath.getZ();
            if (!this.iceBreath.isAlive()) {
                this.active = false;
            }
        } else {
            this.active = false;
        }
        this.ticksExisted++;
    }
}
