package com.bobmowzie.mowziesmobs.server.entity.effects;

import com.bobmowzie.mowziesmobs.client.particle.ParticleHandler;
import com.bobmowzie.mowziesmobs.client.particle.ParticleVanillaCloudExtended;
import com.bobmowzie.mowziesmobs.client.particle.util.AdvancedParticleBase;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.naga.EntityNaga;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by BobMowzie on 11/16/2018.
 */
public class EntityPoisonBall extends EntityMagicEffect {
    private static final byte EXPLOSION_PARTICLES_ID = 69;

    public static float GRAVITY = 0.05f;

    public double prevMotionX, prevMotionY, prevMotionZ;

    public EntityPoisonBall(EntityType<? extends EntityPoisonBall> type, World worldIn) {
        super(type, worldIn);
    }

    public EntityPoisonBall(EntityType<? extends EntityPoisonBall> type, World worldIn, LivingEntity caster) {
        super(type, worldIn, caster);
    }

    public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
        this.setVelocity(x * velocity, y * velocity, z * velocity);
    }

    @Override
    public void tick() {
        this.prevMotionX = this.getVelocity().x;
        this.prevMotionY = this.getVelocity().y;
        this.prevMotionZ = this.getVelocity().z;

        super.tick();
        this.setVelocity(this.getVelocity().subtract(0, GRAVITY, 0));
        this.move(MovementType.SELF, this.getVelocity());

        this.setYaw(-((float) MathHelper.atan2(this.getVelocity().x, this.getVelocity().z)) * (180F / (float) Math.PI));

        List<Entity> entitiesHit = this.getEntitiesNearby(1);
        if (!entitiesHit.isEmpty()) {
            for (Entity entity : entitiesHit) {
                if (entity == this.caster) continue;
                if (entity instanceof EntityNaga) continue;
                if (entity.damage(this.getDamageSources().indirectMagic(this, this.caster), (float) (3 * ConfigHandler.COMMON.MOBS.NAGA.combatConfig.attackMultiplier)) && entity instanceof LivingEntity) {
                    ((LivingEntity) entity).addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 80, 1, false, true));
                }
            }
        }

        if (!this.getWorld().isSpaceEmpty(this, this.getBoundingBox().expand(0.1))) this.explode();

        if (this.getWorld().isClient) {
            float scale = 1f;
            int steps = 4;
            double motionX = this.getVelocity().x;
            double motionY = this.getVelocity().y;
            double motionZ = this.getVelocity().z;
            for (int step = 0; step < steps; step++) {
                double x = this.prevX + step * (this.getX() - this.prevX) / (double) steps;
                double y = this.prevY + step * (this.getY() - this.prevY) / (double) steps + this.getHeight() / 2f;
                double z = this.prevZ + step * (this.getZ() - this.prevZ) / (double) steps;
                for (int i = 0; i < 1; i++) {
                    double xSpeed = scale * 0.02 * (this.random.nextFloat() * 2 - 1);
                    double ySpeed = scale * 0.02 * (this.random.nextFloat() * 2 - 1);
                    double zSpeed = scale * 0.02 * (this.random.nextFloat() * 2 - 1);
                    double value = this.random.nextFloat() * 0.1f;
                    double life = this.random.nextFloat() * 10f + 15f;
                    World world = this.getWorld();
                    world.addParticle(new ParticleVanillaCloudExtended.VanillaCloudData((float) (double) scale, (float) (0.25d + value), (float) (0.75d + value), (float) (0.25d + value), (float) 0.99, (float) life, null), x - motionX * 0.5, y - motionY * 0.5, z - motionZ * 0.5, xSpeed, ySpeed, zSpeed);
                }
                for (int i = 0; i < 2; i++) {
                    double xSpeed = scale * 0.06 * (this.random.nextFloat() * 2 - 1);
                    double ySpeed = scale * 0.06 * (this.random.nextFloat() * 2 - 1);
                    double zSpeed = scale * 0.06 * (this.random.nextFloat() * 2 - 1);
                    double value = this.random.nextFloat() * 0.1f;
                    double life = this.random.nextFloat() * 5f + 10f;
                    AdvancedParticleBase.spawnParticle(this.getWorld(), ParticleHandler.PIXEL, x + xSpeed - motionX * 0.5, y + ySpeed - motionY * 0.5, z + zSpeed - motionZ * 0.5, xSpeed, ySpeed, zSpeed, true, 0, 0, 0, 0, scale * 3f, 0.07d + value, 0.25d + value, 0.07d + value, 1d, 0.99, life * 0.9, false, true);
                }
                for (int i = 0; i < 1; i++) {
                    if (this.random.nextFloat() < 0.9f) {
                        double xSpeed = scale * 0.06 * (this.random.nextFloat() * 2 - 1);
                        double ySpeed = scale * 0.06 * (this.random.nextFloat() * 2 - 1);
                        double zSpeed = scale * 0.06 * (this.random.nextFloat() * 2 - 1);
                        double value = this.random.nextFloat() * 0.1f;
                        double life = this.random.nextFloat() * 5f + 10f;
                        AdvancedParticleBase.spawnParticle(this.getWorld(), ParticleHandler.BUBBLE, x - motionX * 0.5, y - motionY * 0.5, z - motionZ * 0.5, xSpeed, ySpeed, zSpeed, true, 0, 0, 0, 0, 3f, 0.25d + value, 0.75d + value, 0.25d + value, 1d, 0.85, life, false, true);
                    }
                }
            }
        }
        if (this.age > 50) this.discard();
    }

    private void explode() {
        this.getWorld().sendEntityStatus(this, EXPLOSION_PARTICLES_ID);

        this.playSound(MMSounds.ENTITY_NAGA_ACID_HIT, 1, 1);

        List<Entity> entitiesHit = this.getEntitiesNearby(2);
        if (!entitiesHit.isEmpty()) {
            for (Entity entity : entitiesHit) {
                if (entity == this.caster) continue;
                if (entity instanceof EntityNaga) continue;
                if (entity.damage(this.getDamageSources().indirectMagic(this, this.caster), (float) (3 * ConfigHandler.COMMON.MOBS.NAGA.combatConfig.attackMultiplier)) && entity instanceof LivingEntity livingEntity) {
                    livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 80, 0, false, true));
                }
            }
        }

        this.discard();
    }

    private void spawnExplosionParticles() {
        if (this.getWorld().isClient) {
            float explodeSpeed = 3.5f;
            for (int i = 0; i < 26; i++) {
                Vec3d particlePos = new Vec3d(this.random.nextFloat() * 0.25, 0, 0);
                particlePos = particlePos.rotateY((float) (this.random.nextFloat() * 2 * Math.PI));
                particlePos = particlePos.rotateX((float) (this.random.nextFloat() * 2 * Math.PI));
                double value = this.random.nextFloat() * 0.1f;
                double life = this.random.nextFloat() * 17f + 30f;
                World world = this.getWorld();
                world.addParticle(new ParticleVanillaCloudExtended.VanillaCloudData((float) (double) 1, (float) (0.25d + value), (float) (0.75d + value), (float) (0.25d + value), (float) 0.6, (float) life, null), this.getX(), this.getY(), this.getZ(), particlePos.x * explodeSpeed, particlePos.y * explodeSpeed, particlePos.z * explodeSpeed);
            }
            for (int i = 0; i < 26; i++) {
                Vec3d particlePos = new Vec3d(this.random.nextFloat() * 0.25, 0, 0);
                particlePos = particlePos.rotateY((float) (this.random.nextFloat() * 2 * Math.PI));
                particlePos = particlePos.rotateX((float) (this.random.nextFloat() * 2 * Math.PI));
                double value = this.random.nextFloat() * 0.1f;
                double life = this.random.nextFloat() * 5f + 10f;
                AdvancedParticleBase.spawnParticle(this.getWorld(), ParticleHandler.PIXEL, this.getX() + particlePos.x, this.getY() + particlePos.y, this.getZ() + particlePos.z, particlePos.x * explodeSpeed, particlePos.y * explodeSpeed, particlePos.z * explodeSpeed, true, 0, 0, 0, 0, 3f, 0.07d + value, 0.25d + value, 0.07d + value, 1d, 0.6, life * 0.95, false, true);
            }
            for (int i = 0; i < 23; i++) {
                Vec3d particlePos = new Vec3d(this.random.nextFloat() * 0.25, 0, 0);
                particlePos = particlePos.rotateY((float) (this.random.nextFloat() * 2 * Math.PI));
                particlePos = particlePos.rotateX((float) (this.random.nextFloat() * 2 * Math.PI));
                double value = this.random.nextFloat() * 0.1f;
                double life = this.random.nextFloat() * 10f + 20f;
                AdvancedParticleBase.spawnParticle(this.getWorld(), ParticleHandler.BUBBLE, this.getX() + particlePos.x, this.getY() + particlePos.y, this.getZ() + particlePos.z, particlePos.x * explodeSpeed, particlePos.y * explodeSpeed, particlePos.z * explodeSpeed, true, 0, 0, 0, 0, 3f, 0.25d + value, 0.75d + value, 0.25d + value, 1d, 0.6, life * 0.95, false, true);
            }
        }
    }

    @Override
    public void handleStatus(byte id) {
        if (id == EXPLOSION_PARTICLES_ID) {
            this.spawnExplosionParticles();
        } else super.handleStatus(id);
    }
}
