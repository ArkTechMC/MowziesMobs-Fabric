package com.bobmowzie.mowziesmobs.client.sound;

import com.bobmowzie.mowziesmobs.server.entity.umvuthana.EntityUmvuthana;
import com.bobmowzie.mowziesmobs.server.potion.EffectHandler;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import com.iafenvoy.uranus.client.model.tools.ControlledAnimation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.sound.MovingSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.entity.LivingEntity;
import net.minecraft.sound.SoundCategory;

@Environment(EnvType.CLIENT)
public class SunblockSound extends MovingSoundInstance {
    private final LivingEntity entity;
    int ticksExisted = 0;
    ControlledAnimation volumeControl;
    boolean active = true;

    public SunblockSound(LivingEntity entity) {
        super(MMSounds.ENTITY_UMVUTHANA_HEAL_LOOP, SoundCategory.NEUTRAL, SoundInstance.createRandom());
        this.entity = entity;
        this.volume = 4F;
        this.pitch = 1f;
        this.x = (float) entity.getX();
        this.y = (float) entity.getY();
        this.z = (float) entity.getZ();
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
        if (this.entity != null) {
            this.active = true;
            this.x = (float) this.entity.getX();
            this.y = (float) this.entity.getY();
            this.z = (float) this.entity.getZ();
            boolean umvuthanaHealing = false;
            if (this.entity instanceof EntityUmvuthana umvuthana) {
                umvuthanaHealing = umvuthana.getActiveAbilityType() == EntityUmvuthana.HEAL_ABILITY;
            }
            boolean hasSunblock = this.entity.hasStatusEffect(EffectHandler.SUNBLOCK);
            this.active = umvuthanaHealing || hasSunblock;
            if (!this.entity.isAlive()) {
                this.active = false;
            }
        } else {
            this.active = false;
        }
        this.ticksExisted++;
    }
}
