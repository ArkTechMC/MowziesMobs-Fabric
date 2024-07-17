package com.bobmowzie.mowziesmobs.server.entity.effects;

import com.bobmowzie.mowziesmobs.client.particle.ParticleHandler;
import com.bobmowzie.mowziesmobs.client.particle.util.AdvancedParticleBase;
import com.bobmowzie.mowziesmobs.client.particle.util.ParticleComponent;
import com.bobmowzie.mowziesmobs.client.particle.util.ParticleComponent.KeyTrack;
import com.bobmowzie.mowziesmobs.client.particle.util.ParticleComponent.PropertyControl.EnumParticleProperty;
import com.bobmowzie.mowziesmobs.client.particle.util.RibbonComponent;
import com.bobmowzie.mowziesmobs.client.particle.util.RibbonComponent.PropertyOverLength;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.damage.DamageUtil;
import com.bobmowzie.mowziesmobs.server.entity.LeaderSunstrikeImmune;
import com.bobmowzie.mowziesmobs.server.entity.umvuthana.EntityUmvuthi;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class EntitySuperNova extends EntityMagicEffect {
    public static int DURATION = 40;

    public EntitySuperNova(EntityType<? extends EntitySuperNova> type, World world) {
        super(type, world);
    }

    public EntitySuperNova(EntityType<? extends EntitySuperNova> type, World world, LivingEntity caster, double x, double y, double z) {
        super(type, world, caster);
        this.setPosition(x, y, z);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.caster == null || this.caster.isRemoved() || !this.caster.isAlive()) this.discard();

        if (this.age == 1) {
            EntityCameraShake.cameraShake(this.getWorld(), this.getPos(), 30, 0.05f, 10, 30);
            this.playSound(MMSounds.ENTITY_SUPERNOVA_END, 3f, 1f);
            if (this.getWorld().isClient) {
                float scale = 8.2f;
                for (int i = 0; i < 15; i++) {
                    float phaseOffset = this.random.nextFloat();
                    AdvancedParticleBase.spawnParticle(this.getWorld(), ParticleHandler.ARROW_HEAD, this.getX(), this.getY(), this.getZ(), 0, 0, 0, false, 0, 0, 0, 0, 8F, 0.95, 0.9, 0.35, 1, 1, 30, true, true, new ParticleComponent[]{
                            new ParticleComponent.Orbit(new Vec3d[]{this.getPos().add(0, this.getHeight() / 2, 0)}, KeyTrack.startAndEnd(0 + phaseOffset, 1.6f + phaseOffset), new KeyTrack(
                                    new float[]{0.2f * scale, 0.63f * scale, 0.87f * scale, 0.974f * scale, 0.998f * scale, scale},
                                    new float[]{0, 0.15f, 0.3f, 0.45f, 0.6f, 0.75f}
                            ), KeyTrack.startAndEnd(this.random.nextFloat() * 2 - 1, this.random.nextFloat() * 2 - 1), KeyTrack.startAndEnd(this.random.nextFloat() * 2 - 1, this.random.nextFloat() * 2 - 1), KeyTrack.startAndEnd(this.random.nextFloat() * 2 - 1, this.random.nextFloat() * 2 - 1), false),
                            new RibbonComponent(ParticleHandler.RIBBON_FLAT, 10, 0, 0, 0, 0.2F, 0.95, 0.9, 0.35, 1, true, true, new ParticleComponent[]{
                                    new PropertyOverLength(PropertyOverLength.EnumRibbonProperty.SCALE, KeyTrack.startAndEnd(1, 0)),
                                    new ParticleComponent.PropertyControl(EnumParticleProperty.ALPHA, KeyTrack.startAndEnd(1, 0), false)
                            }),
                            new ParticleComponent.PropertyControl(EnumParticleProperty.ALPHA, KeyTrack.startAndEnd(1, 0), false),
                            new ParticleComponent.FaceMotion()
                    });
                }
            }
        }

        if (this.caster != null) {
            float ageFrac = this.age / (float) (EntitySuperNova.DURATION);
            float scale = (float) Math.pow(ageFrac, 0.5) * 5f;
            this.setBoundingBox(this.getBoundingBox().expand(scale));
            this.setPosition(this.prevX, this.prevY, this.prevZ);
            List<Entity> hitList = this.getEntitiesNearbyCube(Entity.class, scale);
            for (Entity entity : hitList) {
                if (this.caster == entity) continue;
                if (this.caster instanceof EntityUmvuthi && entity instanceof LeaderSunstrikeImmune) continue;
                if (entity instanceof LivingEntity livingEntity) {
                    if (this.caster.canTarget(livingEntity)) {
                        float damageFire = 4f;
                        float damageMob = 4f;
                        if (this.caster instanceof EntityUmvuthi) {
                            damageFire *= (float) ConfigHandler.COMMON.MOBS.UMVUTHI.combatConfig.attackMultiplier;
                            damageMob *= (float) ConfigHandler.COMMON.MOBS.UMVUTHI.combatConfig.attackMultiplier;
                        }
                        if (this.caster instanceof PlayerEntity) {
                            damageFire *= (float) (ConfigHandler.COMMON.TOOLS_AND_ABILITIES.SUNS_BLESSING.sunsBlessingAttackMultiplier * 0.8);
                            damageMob *= (float) (ConfigHandler.COMMON.TOOLS_AND_ABILITIES.SUNS_BLESSING.sunsBlessingAttackMultiplier * 0.8);
                        }
                        boolean hitWithFire = DamageUtil.dealMixedDamage(livingEntity, this.getDamageSources().mobProjectile(this, this.caster), damageMob, this.getDamageSources().onFire(), damageFire).getRight();
                        if (hitWithFire) {
                            Vec3d diff = livingEntity.getPos().subtract(this.getPos());
                            diff = diff.normalize();
                            livingEntity.takeKnockback(0.4f, -diff.x, -diff.z);
                            livingEntity.setOnFireFor(5);
                        }
                    }
                } else {
                    entity.damage(this.getDamageSources().mobProjectile(this, this.caster), 4);
                }
            }
        }
        if (this.age > DURATION) this.discard();
    }

    @Override
    public float getBrightnessAtEyes() {
        return 15728880;
    }
}
