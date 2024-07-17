package com.bobmowzie.mowziesmobs.server.entity.effects;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.particle.ParticleCloud;
import com.bobmowzie.mowziesmobs.client.particle.ParticleHandler;
import com.bobmowzie.mowziesmobs.client.particle.ParticleRing;
import com.bobmowzie.mowziesmobs.client.particle.ParticleSnowFlake;
import com.bobmowzie.mowziesmobs.server.ability.AbilityHandler;
import com.bobmowzie.mowziesmobs.server.capability.AbilityCapability;
import com.bobmowzie.mowziesmobs.server.capability.CapabilityHandler;
import com.bobmowzie.mowziesmobs.server.capability.FrozenCapability;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.frostmaw.EntityFrostmaw;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.tag.EntityTypeTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by BobMowzie on 5/25/2017.
 */
public class EntityIceBreath extends EntityMagicEffect {
    private static final int RANGE = 10;
    private static final int ARC = 45;
    private static final int DAMAGE_PER_HIT = 1;

    public EntityIceBreath(EntityType<? extends EntityIceBreath> type, World world) {
        super(type, world);
    }

    public EntityIceBreath(EntityType<? extends EntityIceBreath> type, World world, LivingEntity caster) {
        super(type, world, caster);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.age == 1) {
            if (this.getWorld().isClient) {
                MowziesMobs.PROXY.playIceBreathSound(this);
            }
        }
        if (this.caster == null) this.discard();
        if (this.caster != null && !this.caster.isAlive()) this.discard();
        if (this.age == 1) this.playSound(MMSounds.ENTITY_FROSTMAW_ICEBREATH_START, 1, 0.6f);
        if (this.caster instanceof PlayerEntity player) {
            this.updatePositionAndAngles(player.getX(), player.getY() + player.getActiveEyeHeight(player.getPose(), player.getDimensions(player.getPose())) - 0.5f, player.getZ(), player.getYaw(), player.getPitch());
            AbilityCapability.IAbilityCapability abilityCapability = AbilityHandler.INSTANCE.getAbilityCapability(player);
            if (abilityCapability != null && !abilityCapability.getAbilityFromType(AbilityHandler.ICE_BREATH_ABILITY).isUsing()) {
                this.discard();
            }
        }

        float yaw = (float) Math.toRadians(-this.getYaw());
        float pitch = (float) Math.toRadians(-this.getPitch());
        float spread = 0.25f;
        float speed = 0.56f;
        float xComp = (float) (Math.sin(yaw) * Math.cos(pitch));
        float yComp = (float) (Math.sin(pitch));
        float zComp = (float) (Math.cos(yaw) * Math.cos(pitch));
        if (this.getWorld().isClient) {
            Camera camera = MinecraftClient.getInstance().gameRenderer.getCamera();
            boolean overrideLimiter = camera.getPos().squaredDistanceTo(this.getX(), this.getY(), this.getZ()) < 64 * 64;
            if (this.age % 8 == 0) {
                this.getWorld().addImportantParticle(new ParticleRing.RingData(yaw, -pitch, 40, 1f, 1f, 1f, 1f, 110f * spread, false, ParticleRing.EnumRingBehavior.GROW), overrideLimiter, this.getX(), this.getY(), this.getZ(), 0.5f * xComp, 0.5f * yComp, 0.5f * zComp);
            }

            for (int i = 0; i < 6; i++) {
                double xSpeed = speed * 1f * xComp;// + (spread * (rand.nextFloat() * 2 - 1) * (1 - Math.abs(xComp)));
                double ySpeed = speed * 1f * yComp;// + (spread * (rand.nextFloat() * 2 - 1) * (1 - Math.abs(yComp)));
                double zSpeed = speed * 1f * zComp;// + (spread * (rand.nextFloat() * 2 - 1) * (1 - Math.abs(zComp)));
                this.getWorld().addParticle(new ParticleSnowFlake.SnowflakeData(37f, true), this.getX(), this.getY(), this.getZ(), xSpeed, ySpeed, zSpeed);
            }
            for (int i = 0; i < 5; i++) {
                double xSpeed = speed * xComp + (spread * 0.7 * (this.random.nextFloat() * 2 - 1) * (Math.sqrt(1 - xComp * xComp)));
                double ySpeed = speed * yComp + (spread * 0.7 * (this.random.nextFloat() * 2 - 1) * (Math.sqrt(1 - yComp * yComp)));
                double zSpeed = speed * zComp + (spread * 0.7 * (this.random.nextFloat() * 2 - 1) * (Math.sqrt(1 - zComp * zComp)));
                float value = this.random.nextFloat() * 0.15f;
                this.getWorld().addImportantParticle(new ParticleCloud.CloudData(ParticleHandler.CLOUD, 0.75f + value, 0.75f + value, 1f, 10f + this.random.nextFloat() * 20f, 40, ParticleCloud.EnumCloudBehavior.GROW, 1f), overrideLimiter, this.getX(), this.getY(), this.getZ(), xSpeed, ySpeed, zSpeed);
            }
        }
        if (this.age > 10) this.hitEntities();
        if (this.age > 10) this.freezeBlocks();

        if (this.age > 65 && !(this.caster instanceof PlayerEntity)) this.discard();
    }

    public void hitEntities() {
        List<Entity> entitiesHit = this.getEntityLivingBaseNearby(RANGE, RANGE, RANGE, RANGE);
        float damage = DAMAGE_PER_HIT;
        if (this.caster instanceof EntityFrostmaw)
            damage *= (float) ConfigHandler.COMMON.MOBS.FROSTMAW.combatConfig.attackMultiplier;
        if (this.caster instanceof PlayerEntity)
            damage *= (float) ConfigHandler.COMMON.TOOLS_AND_ABILITIES.ICE_CRYSTAL.attackMultiplier;
        for (Entity entityHit : entitiesHit) {
            if (entityHit == this.caster) continue;

            if (entityHit.getType().isIn(EntityTypeTags.FREEZE_IMMUNE_ENTITY_TYPES) || entityHit instanceof EnderDragonEntity)
                continue;

            float entityHitYaw = (float) ((Math.atan2(entityHit.getZ() - this.getZ(), entityHit.getX() - this.getX()) * (180 / Math.PI) - 90) % 360);
            float entityAttackingYaw = this.getYaw() % 360;
            if (entityHitYaw < 0) {
                entityHitYaw += 360;
            }
            if (entityAttackingYaw < 0) {
                entityAttackingYaw += 360;
            }
            float entityRelativeYaw = entityHitYaw - entityAttackingYaw;

            float xzDistance = (float) Math.sqrt((entityHit.getZ() - this.getZ()) * (entityHit.getZ() - this.getZ()) + (entityHit.getX() - this.getX()) * (entityHit.getX() - this.getX()));
            double hitY = entityHit.getY() + entityHit.getHeight() / 2.0;
            float entityHitPitch = (float) ((Math.atan2((hitY - this.getY()), xzDistance) * (180 / Math.PI)) % 360);
            float entityAttackingPitch = -this.getPitch() % 360;
            if (entityHitPitch < 0) {
                entityHitPitch += 360;
            }
            if (entityAttackingPitch < 0) {
                entityAttackingPitch += 360;
            }
            float entityRelativePitch = entityHitPitch - entityAttackingPitch;

            float entityHitDistance = (float) Math.sqrt((entityHit.getZ() - this.getZ()) * (entityHit.getZ() - this.getZ()) + (entityHit.getX() - this.getX()) * (entityHit.getX() - this.getX()) + (hitY - this.getY()) * (hitY - this.getY()));

            boolean inRange = entityHitDistance <= RANGE;
            boolean yawCheck = (entityRelativeYaw <= ARC / 2f && entityRelativeYaw >= -ARC / 2f) || (entityRelativeYaw >= 360 - ARC / 2f || entityRelativeYaw <= -360 + ARC / 2f);
            boolean pitchCheck = (entityRelativePitch <= ARC / 2f && entityRelativePitch >= -ARC / 2f) || (entityRelativePitch >= 360 - ARC / 2f || entityRelativePitch <= -360 + ARC / 2f);
            boolean frostmawCloseCheck = this.caster instanceof EntityFrostmaw && entityHitDistance <= 2;
            if (inRange && yawCheck && pitchCheck || frostmawCloseCheck) {
                // Do raycast check to prevent damaging through walls
                if (!this.raytraceCheckEntity(entityHit)) continue;

                if (entityHit.damage(this.getDamageSources().freeze(), damage) && entityHit instanceof LivingEntity) {
                    entityHit.setVelocity(entityHit.getVelocity().multiply(0.25, 1, 0.25));
                    FrozenCapability.IFrozenCapability capability = CapabilityHandler.getCapability(entityHit, CapabilityHandler.FROZEN_CAPABILITY);
                    if (capability != null) capability.addFreezeProgress((LivingEntity) entityHit, 0.23f);
                }
            }
        }
    }

    public void freezeBlocks() {
        int checkDist = 10;
        for (int i = (int) this.getX() - checkDist; i < (int) this.getX() + checkDist; i++) {
            for (int j = (int) this.getY() - checkDist; j < (int) this.getY() + checkDist; j++) {
                for (int k = (int) this.getZ() - checkDist; k < (int) this.getZ() + checkDist; k++) {
                    BlockPos pos = new BlockPos(i, j, k);

                    BlockState blockState = this.getWorld().getBlockState(pos);
                    BlockState blockStateAbove = this.getWorld().getBlockState(pos.up());
                    if (blockState.getBlock() != Blocks.WATER || blockStateAbove.getBlock() != Blocks.AIR) {
                        continue;
                    }

                    float blockHitYaw = (float) ((Math.atan2(pos.getZ() - this.getZ(), pos.getX() - this.getX()) * (180 / Math.PI) - 90) % 360);
                    float entityAttackingYaw = this.getYaw() % 360;
                    if (blockHitYaw < 0) {
                        blockHitYaw += 360;
                    }
                    if (entityAttackingYaw < 0) {
                        entityAttackingYaw += 360;
                    }
                    float blockRelativeYaw = blockHitYaw - entityAttackingYaw;

                    float xzDistance = (float) Math.sqrt((pos.getZ() - this.getZ()) * (pos.getZ() - this.getZ()) + (pos.getX() - this.getX()) * (pos.getX() - this.getX()));
                    float blockHitPitch = (float) ((Math.atan2((pos.getY() - this.getY()), xzDistance) * (180 / Math.PI)) % 360);
                    float entityAttackingPitch = -this.getPitch() % 360;
                    if (blockHitPitch < 0) {
                        blockHitPitch += 360;
                    }
                    if (entityAttackingPitch < 0) {
                        entityAttackingPitch += 360;
                    }
                    float blockRelativePitch = blockHitPitch - entityAttackingPitch;

                    float blockHitDistance = (float) Math.sqrt((pos.getZ() - this.getZ()) * (pos.getZ() - this.getZ()) + (pos.getX() - this.getX()) * (pos.getX() - this.getX()) + (pos.getY() - this.getY()) * (pos.getY() - this.getY()));

                    boolean inRange = blockHitDistance <= RANGE;
                    boolean yawCheck = (blockRelativeYaw <= ARC / 2f && blockRelativeYaw >= -ARC / 2f) || (blockRelativeYaw >= 360 - ARC / 2f || blockRelativeYaw <= -360 + ARC / 2f);
                    boolean pitchCheck = (blockRelativePitch <= ARC / 2f && blockRelativePitch >= -ARC / 2f) || (blockRelativePitch >= 360 - ARC / 2f || blockRelativePitch <= -360 + ARC / 2f);
                    if (inRange && yawCheck && pitchCheck) {
                        EntityBlockSwapper.swapBlock(this.getWorld(), pos, Blocks.ICE.getDefaultState(), 140, false, false);
                    }
                }
            }
        }
    }

    public List<Entity> getEntityLivingBaseNearby(double distanceX, double distanceY, double distanceZ, double radius) {
        return this.getEntitiesNearby(Entity.class, distanceX, distanceY, distanceZ, radius);
    }

    public <T extends Entity> List<T> getEntitiesNearby(Class<T> entityClass, double dX, double dY, double dZ, double r) {
        return this.getWorld().getEntitiesByClass(entityClass, this.getBoundingBox().expand(dX, dY, dZ), e -> e != this && this.distanceTo(e) <= r + e.getWidth() / 2f && e.getY() <= this.getY() + dY);
    }
}
