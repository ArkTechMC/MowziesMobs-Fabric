package com.bobmowzie.mowziesmobs.client.sound;

import com.bobmowzie.mowziesmobs.server.ability.AbilityHandler;
import com.bobmowzie.mowziesmobs.server.ability.AbilitySection;
import com.bobmowzie.mowziesmobs.server.ability.abilities.player.geomancy.SpawnBoulderAbility;
import com.bobmowzie.mowziesmobs.server.capability.AbilityCapability;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.sound.MovingSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;

@Environment(EnvType.CLIENT)
public class SpawnBoulderChargeSound extends MovingSoundInstance {
    private final LivingEntity user;
    private final SpawnBoulderAbility ability;

    public SpawnBoulderChargeSound(LivingEntity user) {
        super(MMSounds.EFFECT_GEOMANCY_BOULDER_CHARGE, SoundCategory.PLAYERS, SoundInstance.createRandom());
        this.user = user;
        this.volume = 1F;
        this.pitch = 0.95f;
        this.x = (float) user.getX();
        this.y = (float) user.getY();
        this.z = (float) user.getZ();

        AbilityCapability.IAbilityCapability capability = AbilityCapability.get(user);
        if (capability != null) {
            this.ability = (SpawnBoulderAbility) capability.getAbilityMap().get(AbilityHandler.SPAWN_BOULDER_ABILITY);
        } else this.ability = null;
    }

    @Override
    public void tick() {
        if (this.ability == null) {
            this.setDone();
            return;
        }
        if (!this.ability.isUsing() || this.ability.getCurrentSection().sectionType != AbilitySection.AbilitySectionType.STARTUP) {
            this.setDone();
        }
    }
}
