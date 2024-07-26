package com.bobmowzie.mowziesmobs.server.ability.abilities.player.geomancy;

import com.bobmowzie.mowziesmobs.server.ability.*;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.effects.geomancy.EntityPillar;
import com.bobmowzie.mowziesmobs.server.potion.EffectGeomancy;
import io.github.fabricators_of_create.porting_lib.entity.events.LivingEntityEvents;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import software.bernie.geckolib.core.animation.RawAnimation;

public class SpawnPillarAbility extends PlayerAbility {
    private static final RawAnimation PILLAR_SPAWN_ANIM = RawAnimation.begin().thenPlay("pillar_spawn");
    private static final int MAX_DURATION = 120;
    private static final int MAX_RANGE_TO_GROUND = 12;
    private BlockPos spawnPillarPos;
    private BlockState spawnPillarBlock;
    private EntityPillar pillar;

    public SpawnPillarAbility(AbilityType<PlayerEntity, ? extends Ability> abilityType, PlayerEntity user) {
        super(abilityType, user, new AbilitySection[]{
                new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.STARTUP, 2),
                new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.ACTIVE, MAX_DURATION)
        });
    }

    @Override
    public void start() {
        super.start();
        this.playAnimation(PILLAR_SPAWN_ANIM);
        this.getUser().setVelocity(this.getUser().getVelocity().add(0d, -2d, 0d));
    }

    @Override
    public boolean tryAbility() {
        Vec3d from = this.getUser().getPos();
        Vec3d to = from.subtract(0, MAX_RANGE_TO_GROUND, 0);
        BlockHitResult result = this.getUser().getWorld().raycast(new RaycastContext(from, to, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, this.getUser()));
        if (result.getType() != HitResult.Type.MISS) {
            this.spawnPillarPos = result.getBlockPos();
            this.spawnPillarBlock = this.getUser().getWorld().getBlockState(this.spawnPillarPos);
            if (result.getSide() != Direction.UP) {
                BlockState blockAbove = this.getUser().getWorld().getBlockState(this.spawnPillarPos.up());
                if (blockAbove.shouldSuffocate(this.getUser().getWorld(), this.spawnPillarPos.up()) || blockAbove.isAir())
                    return false;
            }
            return EffectGeomancy.isBlockUseable(this.spawnPillarBlock);
        }
        return false;
    }

    @Override
    public void tickUsing() {
        super.tickUsing();
    }

    @Override
    protected void beginSection(AbilitySection section) {
        if (section.sectionType == AbilitySection.AbilitySectionType.ACTIVE) {
            this.spawnPillar();
        }
    }

    private void spawnPillar() {
        //playAnimation("spawn_boulder_instant", false);

        this.pillar = new EntityPillar(EntityHandler.PILLAR, this.getUser().getWorld(), this.getUser(), this.spawnPillarBlock, this.spawnPillarPos);
        this.pillar.setPosition(this.spawnPillarPos.getX() + 0.5F, this.spawnPillarPos.getY() + 1, this.spawnPillarPos.getZ() + 0.5F);
        if (!this.getUser().getWorld().isClient && this.pillar.checkCanSpawn()) {
            this.getUser().getWorld().spawnEntity(this.pillar);
        }
    }

    @Override
    public void end() {
        super.end();
        if (this.pillar != null) this.pillar.stopRising();
        this.pillar = null;
    }

    @Override
    public boolean canUse() {
        return EffectGeomancy.canUse(this.getUser()) && super.canUse();
    }

    @Override
    public void onJump(LivingEntityEvents.LivingJumpEvent event) {
        super.onJump(event);
        if (this.getUser().isInSneakingPose()) {
            if (!event.getEntity().getWorld().isClient)
                AbilityHandler.INSTANCE.sendAbilityMessage(event.getEntity(), AbilityHandler.SPAWN_PILLAR_ABILITY);
        }
    }

    @Override
    public void onSneakUp(PlayerEntity player) {
        super.onSneakUp(player);
        if (this.getCurrentSection().sectionType == AbilitySection.AbilitySectionType.ACTIVE && this.isUsing()) {
            if (this.pillar != null) this.pillar.stopRising();
            this.nextSection();
        }
    }
}
