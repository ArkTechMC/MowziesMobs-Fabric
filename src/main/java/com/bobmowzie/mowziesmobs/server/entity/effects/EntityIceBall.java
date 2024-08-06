package com.bobmowzie.mowziesmobs.server.entity.effects;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.particle.ParticleCloud;
import com.bobmowzie.mowziesmobs.client.particle.ParticleHandler;
import com.bobmowzie.mowziesmobs.client.particle.ParticleRing;
import com.bobmowzie.mowziesmobs.client.particle.ParticleSnowFlake;
import com.bobmowzie.mowziesmobs.server.capability.AbilityCapability;
import com.bobmowzie.mowziesmobs.server.capability.FrozenCapability;
import com.bobmowzie.mowziesmobs.server.capability.FrozenCapability.IFrozenCapability;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.tag.EntityTypeTags;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by BobMowzie on 9/2/2018.
 */
public class EntityIceBall extends EntityMagicEffect {
    public EntityIceBall(World world) {
        super(EntityHandler.ICE_BALL, world);
    }

    public EntityIceBall(EntityType<? extends EntityIceBall> type, World worldIn) {
        super(type, worldIn);
    }

    public EntityIceBall(EntityType<? extends EntityIceBall> type, World worldIn, LivingEntity caster) {
        super(type, worldIn, caster);
    }

    @Override
    public void tick() {
        super.tick();
        this.move(MovementType.SELF, this.getVelocity());

        if (this.age == 1) {
            if (this.getWorld().isClient) {
                MowziesMobs.PROXY.playIceBreathSound(this);
            }
        }

        List<Entity> entitiesHit = this.getEntitiesNearby(2);
        if (!entitiesHit.isEmpty()) {
            for (Entity entity : entitiesHit) {
                if (entity == this.caster) continue;
                if (entity.getType().isIn(EntityTypeTags.FREEZE_IMMUNE_ENTITY_TYPES) || entity instanceof EnderDragonEntity)
                    continue;
                if (entity.damage(this.getDamageSources().freeze(), (float) (3f * ConfigHandler.COMMON.MOBS.FROSTMAW.combatConfig.attackMultiplier))) {
                    IFrozenCapability capability = FrozenCapability.get((PlayerEntity) entity);
                    if (capability != null)
                        capability.addFreezeProgress((LivingEntity) entity, 1);
                }
            }
        }

        if (!this.getWorld().isSpaceEmpty(this, this.getBoundingBox().expand(0.15))) {
            this.explode();
        }

        if (this.getWorld().isClient) {
            float scale = 2f;
            double x = this.getX();
            double y = this.getY() + this.getHeight() / 2;
            double z = this.getZ();
            double motionX = this.getVelocity().x;
            double motionY = this.getVelocity().y;
            double motionZ = this.getVelocity().z;
            for (int i = 0; i < 4; i++) {
                double xSpeed = scale * 0.01 * (this.random.nextFloat() * 2 - 1);
                double ySpeed = scale * 0.01 * (this.random.nextFloat() * 2 - 1);
                double zSpeed = scale * 0.01 * (this.random.nextFloat() * 2 - 1);
                float value = this.random.nextFloat() * 0.15f;
                this.getWorld().addParticle(new ParticleCloud.CloudData(ParticleHandler.CLOUD, 0.75f + value, 0.75f + value, 1f, scale * (10f + this.random.nextFloat() * 20f), 20, ParticleCloud.EnumCloudBehavior.SHRINK, 1f), x + xSpeed, y + ySpeed, z + zSpeed, xSpeed, ySpeed, zSpeed);
            }
            for (int i = 0; i < 1; i++) {
                double xSpeed = scale * 0.01 * (this.random.nextFloat() * 2 - 1);
                double ySpeed = scale * 0.01 * (this.random.nextFloat() * 2 - 1);
                double zSpeed = scale * 0.01 * (this.random.nextFloat() * 2 - 1);
                this.getWorld().addParticle(new ParticleCloud.CloudData(ParticleHandler.CLOUD, 1f, 1f, 1f, scale * (5f + this.random.nextFloat() * 10f), 40, ParticleCloud.EnumCloudBehavior.SHRINK, 1f), x, y, z, xSpeed, ySpeed, zSpeed);
            }

            for (int i = 0; i < 5; i++) {
                double xSpeed = scale * 0.05 * (this.random.nextFloat() * 2 - 1);
                double ySpeed = scale * 0.05 * (this.random.nextFloat() * 2 - 1);
                double zSpeed = scale * 0.05 * (this.random.nextFloat() * 2 - 1);
                this.getWorld().addParticle(new ParticleSnowFlake.SnowflakeData(40, false), x - 20 * (xSpeed) + motionX, y - 20 * ySpeed + motionY, z - 20 * zSpeed + motionZ, xSpeed, ySpeed, zSpeed);
            }

            float yaw = (float) Math.atan2(motionX, motionZ);
            float pitch = (float) (Math.acos(motionY / Math.sqrt(motionX * motionX + motionY * motionY + motionZ * motionZ)) + Math.PI / 2);
            if (this.age % 3 == 0) {
                this.getWorld().addParticle(new ParticleRing.RingData(yaw, pitch, 40, 0.9f, 0.9f, 1f, 0.4f, scale * 16f, false, ParticleRing.EnumRingBehavior.GROW_THEN_SHRINK), x + 1.5f * motionX, y + 1.5f * motionY, z + 1.5f * motionZ, 0, 0, 0);
            }

            if (this.age == 1) {
                this.getWorld().addParticle(new ParticleRing.RingData(yaw, pitch, 20, 0.9f, 0.9f, 1f, 0.4f, scale * 16f, false, ParticleRing.EnumRingBehavior.GROW), x, y, z, 0, 0, 0);
            }
        }
        if (this.age > 50) this.discard();
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
    }

    public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
        this.setVelocity(x * velocity, y * velocity, z * velocity);
    }

    private void explode() {
        if (this.getWorld().isClient) {
            for (int i = 0; i < 8; i++) {
                Vec3d particlePos = new Vec3d(this.random.nextFloat() * 0.3, 0, 0);
                particlePos = particlePos.rotateY((float) (this.random.nextFloat() * 2 * Math.PI));
                particlePos = particlePos.rotateX((float) (this.random.nextFloat() * 2 * Math.PI));
                float value = this.random.nextFloat() * 0.15f;
                this.getWorld().addParticle(new ParticleCloud.CloudData(ParticleHandler.CLOUD, 0.75f + value, 0.75f + value, 1f, 10f + this.random.nextFloat() * 20f, 40, ParticleCloud.EnumCloudBehavior.GROW, 1f), this.getX() + particlePos.x, this.getY() + particlePos.y, this.getZ() + particlePos.z, particlePos.x, particlePos.y, particlePos.z);
            }
            for (int i = 0; i < 10; i++) {
                Vec3d particlePos = new Vec3d(this.random.nextFloat() * 0.3, 0, 0);
                particlePos = particlePos.rotateY((float) (this.random.nextFloat() * 2 * Math.PI));
                particlePos = particlePos.rotateX((float) (this.random.nextFloat() * 2 * Math.PI));
                this.getWorld().addParticle(new ParticleSnowFlake.SnowflakeData(40, false), this.getX() + particlePos.x, this.getY() + particlePos.y, this.getZ() + particlePos.z, particlePos.x, particlePos.y, particlePos.z);
            }
        }
        this.discard();
    }
}
