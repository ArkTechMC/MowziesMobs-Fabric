package com.bobmowzie.mowziesmobs.server.ability.abilities.player.geomancy;

import com.bobmowzie.mowziesmobs.client.particle.ParticleDecal;
import com.bobmowzie.mowziesmobs.client.particle.ParticleHandler;
import com.bobmowzie.mowziesmobs.client.particle.util.ParticleComponent;
import com.bobmowzie.mowziesmobs.server.ability.Ability;
import com.bobmowzie.mowziesmobs.server.ability.AbilitySection;
import com.bobmowzie.mowziesmobs.server.ability.AbilityType;
import com.bobmowzie.mowziesmobs.server.ability.PlayerAbility;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class FissureAbility extends PlayerAbility {
    public FissureAbility(AbilityType<PlayerEntity, ? extends Ability<?>> abilityType, PlayerEntity user) {
        super(abilityType, user, new AbilitySection[]{
                new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.STARTUP, 2),
                new AbilitySection.AbilitySectionInfinite(AbilitySection.AbilitySectionType.ACTIVE),
                new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.RECOVERY, 21)
        });
    }

    @Override
    public void onRightClickBlock(PlayerEntity player, World world, Hand hand, BlockHitResult hitResult) {
        super.onRightClickBlock(player, world, hand, hitResult);
        if (player.getStackInHand(hand).isEmpty()) {
            float rotation = (float) Math.toRadians(this.getUser().headYaw + 180f);
            Vec3d pos = hitResult.getPos();
            ParticleDecal.spawnDecal(this.getUser().getWorld(), ParticleHandler.GROUND_CRACK, pos.getX(), pos.getY() + 0.01, pos.getZ(), 0, 0, 0, rotation, 2, 1F, 1F, 1F, 1F, 0, 200, false, 32, 64, new ParticleComponent[]{});
        }
    }
}
