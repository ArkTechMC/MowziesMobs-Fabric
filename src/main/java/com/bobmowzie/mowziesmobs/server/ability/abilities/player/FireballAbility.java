package com.bobmowzie.mowziesmobs.server.ability.abilities.player;

import com.bobmowzie.mowziesmobs.server.ability.AbilitySection;
import com.bobmowzie.mowziesmobs.server.ability.AbilityType;
import com.bobmowzie.mowziesmobs.server.ability.PlayerAbility;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.util.math.Vec3d;

public class FireballAbility extends PlayerAbility {
    public FireballAbility(AbilityType<PlayerEntity, FireballAbility> abilityType, PlayerEntity user) {
        super(abilityType, user, new AbilitySection[]{
                new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.STARTUP, 20),
                new AbilitySection.AbilitySectionInstant(AbilitySection.AbilitySectionType.ACTIVE)
        }, 20);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getTicksInUse() == 20) {
            LivingEntity user = this.getUser();
            Vec3d lookVec = user.getRotationVector();
            SmallFireballEntity smallfireballentity = new SmallFireballEntity(user.getWorld(), user, lookVec.x, lookVec.y, lookVec.z);
            smallfireballentity.setPosition(smallfireballentity.getX(), user.getBodyY(0.5D) + 0.5D, smallfireballentity.getZ());
            user.getWorld().spawnEntity(smallfireballentity);
        }
    }
}
