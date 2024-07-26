package com.bobmowzie.mowziesmobs.server.ability.abilities.player.geomancy;

import com.bobmowzie.mowziesmobs.client.particle.ParticleHandler;
import com.bobmowzie.mowziesmobs.client.particle.util.AdvancedParticleBase;
import com.bobmowzie.mowziesmobs.client.particle.util.ParticleComponent;
import com.bobmowzie.mowziesmobs.server.ability.Ability;
import com.bobmowzie.mowziesmobs.server.ability.AbilitySection;
import com.bobmowzie.mowziesmobs.server.ability.AbilityType;
import com.bobmowzie.mowziesmobs.server.ability.PlayerAbility;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.effects.geomancy.EntityGeomancyBase;
import com.bobmowzie.mowziesmobs.server.entity.effects.geomancy.EntityRockSling;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import software.bernie.geckolib.core.animation.RawAnimation;


public class RockSlingAbility extends PlayerAbility {
    public static final double SPAWN_BOULDER_REACH = 5;
    private static final RawAnimation ROCK_SLING_ANIM = RawAnimation.begin().thenPlay("rock_sling_right");
    public BlockPos spawnBoulderPos = new BlockPos(0, 0, 0);
    public Vec3d lookPos = new Vec3d(0, 0, 0);
    private BlockState spawnBoulderBlock = Blocks.DIRT.getDefaultState();
    private final int damage = 3;

    public RockSlingAbility(AbilityType<PlayerEntity, ? extends Ability> abilityType, PlayerEntity user) {
        super(abilityType, user, new AbilitySection[]{
                new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.STARTUP, 5),
                new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.ACTIVE, 10),
                new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.RECOVERY, 5)
        });
    }

    @Override
    public void start() {
        super.start();
        Vec3d from = this.getUser().getCameraPosVec(1.0f);
        Vec3d to = from.add(this.getUser().getRotationVector().multiply(SPAWN_BOULDER_REACH));
        BlockHitResult result = this.getUser().getWorld().raycast(new RaycastContext(from, to, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, this.getUser()));
        if (result.getType() == HitResult.Type.BLOCK) {
            this.lookPos = result.getPos();
        }

        this.spawnBoulderPos = result.getBlockPos();
        this.spawnBoulderBlock = this.getUser().getWorld().getBlockState(this.spawnBoulderPos);
        this.playAnimation(ROCK_SLING_ANIM);

        if (this.getUser().getWorld().isClient()) {
            AdvancedParticleBase.spawnParticle(this.getUser().getWorld(), ParticleHandler.RING2, (float) this.getUser().getX(), (float) this.getUser().getY() + 0.01f, (float) this.getUser().getZ(), 0, 0, 0, false, 0, Math.PI / 2f, 0, 0, 3.5F, 0.83f, 1, 0.39f, 1, 1, 10, true, true, new ParticleComponent[]{
                    new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.ALPHA, ParticleComponent.KeyTrack.startAndEnd(0f, 0.7f), false),
                    new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.SCALE, ParticleComponent.KeyTrack.startAndEnd(0, (0.8f + 2.7f * 20f / 60f) * 10f), false)
            });
        } else {
            for (int i = 0; i < 3; i++) {
                Vec3d spawnPos = new Vec3d(0D, -1D, 2D).rotateY((float) Math.toRadians(-this.getUser().getYaw())).rotateY((float) Math.toRadians(-90 + (i * 80))).add(this.getUser().getPos());
                EntityRockSling boulder = new EntityRockSling(EntityHandler.ROCK_SLING, this.getUser().getWorld(), this.getUser(), this.spawnBoulderBlock, this.spawnBoulderPos, EntityGeomancyBase.GeomancyTier.values()[1]);
                boulder.setPosition(spawnPos.getX() + 0.5F, spawnPos.getY() + 2, spawnPos.getZ() + 0.5F);
                boulder.setLaunchVec(this.getUser().getRotationVec(1f).multiply(1f, 0.9f, 1f));
                boulder.setTravelling(true);
                boulder.setDamage(4);
                if (!this.getUser().getWorld().isClient && boulder.checkCanSpawn()) {
                    this.getUser().getWorld().spawnEntity(boulder);
                }
            }
        }
    }
}
