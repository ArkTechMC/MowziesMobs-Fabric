package com.bobmowzie.mowziesmobs.server.entity.effects.geomancy;

import com.bobmowzie.mowziesmobs.client.particle.ParticleHandler;
import com.bobmowzie.mowziesmobs.client.particle.util.AdvancedParticleBase;
import com.bobmowzie.mowziesmobs.client.particle.util.ParticleComponent;
import com.bobmowzie.mowziesmobs.client.particle.util.ParticleRotation;
import com.bobmowzie.mowziesmobs.server.ability.AbilityHandler;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntityCameraShake;
import com.bobmowzie.mowziesmobs.server.potion.EffectGeomancy;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import com.google.common.collect.Iterables;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BobMowzie on 4/14/2017.
 */
public class EntityBoulderProjectile extends EntityBoulderBase {
    private static final TrackedData<Vector3f> SHOOT_DIRECTION = DataTracker.registerData(EntityBoulderProjectile.class, TrackedDataHandlerRegistry.VECTOR3F);
    protected final List<Entity> ridingEntities = new ArrayList<Entity>();
    protected boolean travelling = false;
    protected float speed = 1.5f;
    protected int damage = 8;
    private boolean didShootParticles = false;

    public EntityBoulderProjectile(EntityType<? extends EntityBoulderProjectile> type, World world) {
        super(type, world);
    }

    public EntityBoulderProjectile(EntityType<? extends EntityBoulderProjectile> type, World world, LivingEntity caster, BlockState blockState, BlockPos pos, GeomancyTier tier) {
        super(type, world, caster, blockState, pos, tier);
    }

    public void setSizeParams() {
        super.setSizeParams();
        GeomancyTier size = this.getTier();
        if (size == GeomancyTier.MEDIUM) {
            this.damage = 12;
            this.speed = 1.2f;
        } else if (size == GeomancyTier.LARGE) {
            this.damage = 16;
            this.speed = 1f;
        } else if (size == GeomancyTier.HUGE) {
            this.damage = 20;
            this.speed = 0.65f;
        }

        if (this.caster instanceof PlayerEntity)
            this.damage *= ConfigHandler.COMMON.TOOLS_AND_ABILITIES.EARTHREND_GAUNTLET.attackMultiplier;
    }

    public float getSpeed() {
        return this.speed;
    }

    @Override
    protected @NotNull Box calculateBoundingBox() {
        Box boundingBox = super.calculateBoundingBox();
        if (!this.travelling) boundingBox = boundingBox.stretch(0, -0.5, 0);
        return boundingBox;
    }

    @Override
    public void tick() {
        if (this.startActive() && this.age == 1) this.activate();
        super.tick();
        if (this.caster == null || this.caster.isRemoved()) this.explode();
        if (this.ridingEntities != null) this.ridingEntities.clear();
        List<Entity> onTopOfEntities = this.getWorld().getOtherEntities(this, this.getBoundingBox().shrink(0, this.getHeight() - 1, 0).offset(new Vec3d(0, this.getHeight() - 0.5, 0)).expand(0.6, 0.5, 0.6));
        for (Entity entity : onTopOfEntities) {
            if (entity != null && entity.canHit() && !(entity instanceof EntityBoulderProjectile) && entity.getY() >= this.getY() + 0.2)
                this.ridingEntities.add(entity);
        }
        if (this.travelling) {
            for (Entity entity : this.ridingEntities) {
                entity.move(MovementType.SHULKER_BOX, this.getVelocity().add(0, 0.1, 0));
            }
        }

        // Hit entities
        List<Entity> entitiesHit = this.getEntitiesNearby(1.7);
        if (this.travelling && !entitiesHit.isEmpty()) {
            for (Entity entity : entitiesHit) {
                if (this.getWorld().isClient) continue;
                if (entity == this.caster) continue;
                if (entity.noClip) continue;
                if (!entity.canBeHitByProjectile()) continue;
                if (!this.travellingBlockedBy(entity)) continue;
                if (this.ridingEntities != null && this.ridingEntities.contains(entity)) continue;
                if (this.caster != null) entity.damage(this.getDamageSources().mobProjectile(this, this.caster), this.damage);
                else entity.damage(this.getDamageSources().generic(), this.damage);
                if (this.isAlive() && this.boulderSize != GeomancyTier.HUGE) this.explode();
            }
        }

        // Hit other boulders
        this.handleHitOtherBoulders();

        // Hit blocks
        if (this.travelling) {
            if (
                    !this.getWorld().getOtherEntities(this, this.getBoundingBox().expand(0.1), (e) -> (this.ridingEntities == null || !this.ridingEntities.contains(e)) && e.isCollidable() && this.travellingBlockedBy(e)).isEmpty() ||
                            Iterables.size(this.getWorld().getBlockCollisions(this, this.getBoundingBox().expand(0.1))) > 0
            ) {
                this.explode();
            }
        }

        if (this.getWorld().isClient() && this.getDataTracker().get(SHOOT_DIRECTION).length() > 0 && !this.didShootParticles) {
            Vec3d ringOffset = new Vec3d(this.getDataTracker().get(SHOOT_DIRECTION)).multiply(-1).normalize();
            ParticleRotation.OrientVector rotation = new ParticleRotation.OrientVector(ringOffset);
            AdvancedParticleBase.spawnAlwaysVisibleParticle(this.getWorld(), ParticleHandler.RING2, 64, (float) this.getX() + (float) ringOffset.x, (float) this.getY() + 0.5f + (float) ringOffset.y, (float) this.getZ() + (float) ringOffset.z, 0, 0, 0, rotation, 3.5F, 0.83f, 1, 0.39f, 1, 1, (int) (5 + 2 * this.getWidth()), true, true, new ParticleComponent[]{
                    new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.ALPHA, ParticleComponent.KeyTrack.startAndEnd(0.7f, 0f), false),
                    new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.SCALE, ParticleComponent.KeyTrack.startAndEnd(0f, (1.0f + 0.5f * this.getWidth()) * 8f * this.getShootRingParticleScale()), false)
            });
            this.didShootParticles = true;
        }
    }

    protected boolean startActive() {
        return true;
    }

    protected boolean travellingBlockedBy(Entity entity) {
        return true;
    }

    protected void handleHitOtherBoulders() {
        List<EntityBoulderProjectile> bouldersHit = this.getWorld().getNonSpectatingEntities(EntityBoulderProjectile.class, this.getBoundingBox().expand(0.2, 0.2, 0.2).offset(this.getVelocity().normalize().multiply(0.5)));
        if (this.travelling && !bouldersHit.isEmpty()) {
            for (EntityBoulderProjectile entity : bouldersHit) {
                if (!entity.travelling && this.travellingBlockedBy(entity)) {
                    entity.handleAttack(this);
                    this.explode();
                }
            }
        }
    }

    @Override
    public boolean canHit() {
        return true;
    }

    public void shoot(Vec3d shootDirection) {
        this.setVelocity(shootDirection);
        if (!this.travelling) this.setDeathTime(60);
        this.travelling = true;
        this.setBoundingBox(this.getType().createSimpleBoundingBox(this.getX(), this.getY(), this.getZ()));

        if (this.boulderSize == GeomancyTier.SMALL) {
            this.playSound(MMSounds.EFFECT_GEOMANCY_HIT_SMALL, 1.5f, 1.3f);
            this.playSound(MMSounds.EFFECT_GEOMANCY_MAGIC_SMALL, 1.5f, 0.9f);
        } else if (this.boulderSize == GeomancyTier.MEDIUM) {
            this.playSound(MMSounds.EFFECT_GEOMANCY_HIT_SMALL, 1.5f, 0.9f);
            this.playSound(MMSounds.EFFECT_GEOMANCY_MAGIC_SMALL, 1.5f, 0.5f);
        } else if (this.boulderSize == GeomancyTier.LARGE) {
            this.playSound(MMSounds.EFFECT_GEOMANCY_HIT_SMALL, 1.5f, 0.5f);
            this.playSound(MMSounds.EFFECT_GEOMANCY_MAGIC_BIG, 1.5f, 1.3f);
            EntityCameraShake.cameraShake(this.getWorld(), this.getPos(), 10, 0.05f, 0, 20);
        } else if (this.boulderSize == GeomancyTier.HUGE) {
            this.playSound(MMSounds.EFFECT_GEOMANCY_HIT_MEDIUM_1, 1.5f, 1f);
            this.playSound(MMSounds.EFFECT_GEOMANCY_MAGIC_BIG, 1.5f, 0.9f);
            EntityCameraShake.cameraShake(this.getWorld(), this.getPos(), 15, 0.05f, 0, 20);
        }

        this.getDataTracker().set(SHOOT_DIRECTION, shootDirection.toVector3f());
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.getDataTracker().startTracking(SHOOT_DIRECTION, new Vector3f(0, 0, 0));
    }

    @Override
    public void handleStatus(byte id) {
        super.handleStatus(id);
    }

    protected float getShootRingParticleScale() {
        return 1.0f;
    }

    @Override
    public boolean handleAttack(Entity entityIn) {
        if (this.risingTick > this.finishedRisingTick - 1 && !this.travelling) {
            if (entityIn instanceof PlayerEntity player && EffectGeomancy.canUse((PlayerEntity) entityIn)) {
                if (this.ridingEntities.contains(player)) {
                    Vec3d lateralLookVec = Vec3d.fromPolar(0, player.getYaw()).normalize();
                    this.shoot(new Vec3d(this.speed * 0.5 * lateralLookVec.x, this.getVelocity().y, this.speed * 0.5 * lateralLookVec.z));
                } else {
                    this.shoot(player.getRotationVector().multiply(this.speed * 0.5));
                }
                AbilityHandler.INSTANCE.sendAbilityMessage(player, AbilityHandler.HIT_BOULDER_ABILITY);
            } else if (entityIn instanceof EntityBoulderProjectile boulder && ((EntityBoulderProjectile) entityIn).travelling) {
                Vec3d thisPos = this.getPos();
                Vec3d boulderPos = boulder.getPos();
                Vec3d velVec = thisPos.subtract(boulderPos).normalize();
                this.shoot(velVec.multiply(this.speed * 0.5));
            }
        }
        return super.handleAttack(entityIn);
    }

    public boolean isTravelling() {
        return this.travelling;
    }

    public void setTravelling(boolean travel) {
        this.travelling = travel;
    }

    public void setDamage(int dam) {
        this.damage = dam;
    }
}
