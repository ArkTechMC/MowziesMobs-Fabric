package com.bobmowzie.mowziesmobs.server.entity.effects;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.tools.ControlledAnimation;
import com.bobmowzie.mowziesmobs.client.particle.ParticleHandler;
import com.bobmowzie.mowziesmobs.client.particle.ParticleOrb;
import com.bobmowzie.mowziesmobs.client.particle.util.AdvancedParticleBase;
import com.bobmowzie.mowziesmobs.client.particle.util.ParticleComponent;
import com.bobmowzie.mowziesmobs.client.render.entity.player.GeckoPlayer;
import com.bobmowzie.mowziesmobs.client.render.entity.player.GeckoRenderPlayer;
import com.bobmowzie.mowziesmobs.server.capability.AbilityCapability;
import com.bobmowzie.mowziesmobs.server.capability.PlayerCapability;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.damage.DamageUtil;
import com.bobmowzie.mowziesmobs.server.entity.LeaderSunstrikeImmune;
import com.bobmowzie.mowziesmobs.server.entity.umvuthana.EntityUmvuthi;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.Perspective;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EntitySolarBeam extends Entity {
    public static final double RADIUS_UMVUTHI = 30;
    public static final double RADIUS_PLAYER = 20;
    private static final TrackedData<Float> YAW = DataTracker.registerData(EntitySolarBeam.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Float> PITCH = DataTracker.registerData(EntitySolarBeam.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Integer> DURATION = DataTracker.registerData(EntitySolarBeam.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Boolean> HAS_PLAYER = DataTracker.registerData(EntitySolarBeam.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Integer> CASTER = DataTracker.registerData(EntitySolarBeam.class, TrackedDataHandlerRegistry.INTEGER);
    public LivingEntity caster;
    public double endPosX, endPosY, endPosZ;
    public double collidePosX, collidePosY, collidePosZ;
    public double prevCollidePosX, prevCollidePosY, prevCollidePosZ;
    public float renderYaw, renderPitch;
    public ControlledAnimation appear = new ControlledAnimation(3);
    public boolean on = true;
    public Direction blockSide = null;
    public float prevYaw;
    public float prevPitch;

    @Environment(EnvType.CLIENT)
    private Vec3d[] attractorPos;

    private boolean didRaytrace;

    public EntitySolarBeam(EntityType<? extends EntitySolarBeam> type, World world) {
        super(type, world);
        this.ignoreCameraFrustum = true;
        if (world.isClient) {
            this.attractorPos = new Vec3d[]{new Vec3d(0, 0, 0)};
        }
    }

    public EntitySolarBeam(EntityType<? extends EntitySolarBeam> type, World world, LivingEntity caster, double x, double y, double z, float yaw, float pitch, int duration) {
        this(type, world);
        this.caster = caster;
        this.setYaw(yaw);
        this.setPitch(pitch);
        this.setDuration(duration);
        this.setPosition(x, y, z);
        this.calculateEndPos();
        MowziesMobs.PROXY.playSolarBeamSound(this);
        if (!world.isClient) {
            this.setCasterID(caster.getId());
        }
    }

    @Override
    public PistonBehavior getPistonBehavior() {
        return PistonBehavior.IGNORE;
    }

    @Override
    public void tick() {
        super.tick();
        this.prevCollidePosX = this.collidePosX;
        this.prevCollidePosY = this.collidePosY;
        this.prevCollidePosZ = this.collidePosZ;
        this.prevYaw = this.renderYaw;
        this.prevPitch = this.renderPitch;
        this.prevX = this.getX();
        this.prevY = this.getY();
        this.prevZ = this.getZ();
        if (this.age == 1 && this.getWorld().isClient) {
            this.caster = (LivingEntity) this.getWorld().getEntityById(this.getCasterID());
        }

        if (!this.getWorld().isClient) {
            if (this.getHasPlayer()) {
                this.updateWithPlayer();
            } else if (this.caster instanceof EntityUmvuthi) {
                this.updateWithUmvuthi();
            }
        }
        if (this.caster != null) {
            this.renderYaw = (float) ((this.caster.headYaw + 90.0d) * Math.PI / 180.0d);
            this.renderPitch = (float) (-this.caster.getPitch() * Math.PI / 180.0d);
        }

        if (!this.on && this.appear.getTimer() == 0) {
            this.discard();
        }
        if (this.on && this.age > 20) {
            this.appear.increaseTimer();
        } else {
            this.appear.decreaseTimer();
        }

        if (this.caster != null && !this.caster.isAlive()) this.discard();

        if (this.getWorld().isClient && this.age <= 10 && this.caster != null) {
            int particleCount = 8;
            while (--particleCount != 0) {
                double radius = 2f * this.caster.getWidth();
                double yaw = this.random.nextFloat() * 2 * Math.PI;
                double pitch = this.random.nextFloat() * 2 * Math.PI;
                double ox = radius * Math.sin(yaw) * Math.sin(pitch);
                double oy = radius * Math.cos(pitch);
                double oz = radius * Math.cos(yaw) * Math.sin(pitch);
                double rootX = this.caster.getX();
                double rootY = this.caster.getY() + this.caster.getHeight() / 2f + 0.3f;
                double rootZ = this.caster.getZ();
                if (this.getHasPlayer()) {
                    if (this.caster instanceof PlayerEntity && !(this.caster == MinecraftClient.getInstance().player && MinecraftClient.getInstance().options.getPerspective() == Perspective.FIRST_PERSON)) {
                        GeckoPlayer geckoPlayer = GeckoPlayer.getGeckoPlayer((PlayerEntity) this.caster, GeckoPlayer.Perspective.THIRD_PERSON);
                        if (geckoPlayer != null) {
                            GeckoRenderPlayer renderPlayer = (GeckoRenderPlayer) geckoPlayer.getPlayerRenderer();
                            if (renderPlayer.betweenHandsPos != null) {
                                rootX += renderPlayer.betweenHandsPos.getX();
                                rootY += renderPlayer.betweenHandsPos.getY();
                                rootZ += renderPlayer.betweenHandsPos.getZ();
                            }
                        }
                    }
                    this.attractorPos[0] = new Vec3d(rootX, rootY, rootZ);
                } else if (this.caster instanceof EntityUmvuthi umvuthi) {
                    if (umvuthi.headPos != null && umvuthi.headPos[0] != null) {
                        this.attractorPos[0] = ((EntityUmvuthi) this.caster).headPos[0];
                        rootX = this.attractorPos[0].getX();
                        rootY = this.attractorPos[0].getY();
                        rootZ = this.attractorPos[0].getZ();
                    }
                } else {
                    this.attractorPos[0] = new Vec3d(rootX, rootY, rootZ);
                }
                AdvancedParticleBase.spawnParticle(this.getWorld(), ParticleHandler.ORB2, rootX + ox, rootY + oy, rootZ + oz, 0, 0, 0, true, 0, 0, 0, 0, 5F, 1, 1, 1, 1, 1, 7, true, false, new ParticleComponent[]{
                        new ParticleComponent.Attractor(this.attractorPos, 1.7f, 0.0f, ParticleComponent.Attractor.EnumAttractorBehavior.EXPONENTIAL),
                        new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.ALPHA, new ParticleComponent.KeyTrack(
                                new float[]{0f, 0.8f},
                                new float[]{0f, 1f}
                        ), false),
                        new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.SCALE, new ParticleComponent.KeyTrack(
                                new float[]{3f, 6f},
                                new float[]{0f, 1f}
                        ), false)
                });
            }
        }
        if (this.age > 20) {
            this.calculateEndPos();
            List<Entity> hit = this.raytraceEntities(this.getWorld(), new Vec3d(this.getX(), this.getY(), this.getZ()), new Vec3d(this.endPosX, this.endPosY, this.endPosZ), false, true, true).entities;
            if (this.blockSide != null) {
                this.spawnExplosionParticles(2);
            }
            if (!this.getWorld().isClient) {
                for (Entity target : hit) {
                    if (this.caster instanceof EntityUmvuthi && target instanceof LeaderSunstrikeImmune) {
                        continue;
                    }
                    float damageFire = 1f;
                    float damageMob = 1.5f;
                    if (this.caster instanceof EntityUmvuthi) {
                        damageFire *= (float) ConfigHandler.COMMON.MOBS.UMVUTHI.combatConfig.attackMultiplier;
                        damageMob *= (float) ConfigHandler.COMMON.MOBS.UMVUTHI.combatConfig.attackMultiplier;
                    }
                    if (this.caster instanceof PlayerEntity) {
                        damageFire *= (float) (ConfigHandler.COMMON.TOOLS_AND_ABILITIES.SUNS_BLESSING.sunsBlessingAttackMultiplier * 0.75);
                        damageMob *= (float) (ConfigHandler.COMMON.TOOLS_AND_ABILITIES.SUNS_BLESSING.sunsBlessingAttackMultiplier * 0.75);
                    }
                    if (target instanceof LivingEntity) {
                        DamageUtil.dealMixedDamage((LivingEntity) target, this.getDamageSources().mobProjectile(this, this.caster), damageMob, this.getDamageSources().onFire(), damageFire);
                    } else {
                        target.damage(this.getDamageSources().mobProjectile(this, this.caster), damageMob);
                    }
                }
            } else {
                if (this.age - 15 < this.getDuration()) {
                    int particleCount = 4;
                    while (particleCount-- > 0) {
                        double radius = 1f;
                        double yaw = (float) (this.random.nextFloat() * 2 * Math.PI);
                        double pitch = (float) (this.random.nextFloat() * 2 * Math.PI);
                        double ox = (float) (radius * Math.sin(yaw) * Math.sin(pitch));
                        double oy = (float) (radius * Math.cos(pitch));
                        double oz = (float) (radius * Math.cos(yaw) * Math.sin(pitch));
                        double o2x = (float) (-1 * Math.cos(this.getYaw()) * Math.cos(this.getPitch()));
                        double o2y = (float) (-1 * Math.sin(this.getPitch()));
                        double o2z = (float) (-1 * Math.sin(this.getYaw()) * Math.cos(this.getPitch()));
                        this.getWorld().addParticle(new ParticleOrb.OrbData((float) (this.collidePosX + o2x + ox), (float) (this.collidePosY + o2y + oy), (float) (this.collidePosZ + o2z + oz), 15), this.getX() + o2x + ox, this.getY() + o2y + oy, this.getZ() + o2z + oz, 0, 0, 0);
                    }
                    particleCount = 4;
                    while (particleCount-- > 0) {
                        double radius = 2f;
                        double yaw = this.random.nextFloat() * 2 * Math.PI;
                        double pitch = this.random.nextFloat() * 2 * Math.PI;
                        double ox = radius * Math.sin(yaw) * Math.sin(pitch);
                        double oy = radius * Math.cos(pitch);
                        double oz = radius * Math.cos(yaw) * Math.sin(pitch);
                        double o2x = -1 * Math.cos(this.getYaw()) * Math.cos(this.getPitch());
                        double o2y = -1 * Math.sin(this.getPitch());
                        double o2z = -1 * Math.sin(this.getYaw()) * Math.cos(this.getPitch());
                        this.getWorld().addParticle(new ParticleOrb.OrbData((float) (this.collidePosX + o2x + ox), (float) (this.collidePosY + o2y + oy), (float) (this.collidePosZ + o2z + oz), 20), this.collidePosX + o2x, this.collidePosY + o2y, this.collidePosZ + o2z, 0, 0, 0);
                    }
                }
            }
        }
        if (this.age - 20 > this.getDuration()) {
            this.on = false;
        }
    }

    private void spawnExplosionParticles(int amount) {
        for (int i = 0; i < amount; i++) {
            final float velocity = 0.1F;
            float yaw = (float) (this.random.nextFloat() * 2 * Math.PI);
            float motionY = this.random.nextFloat() * 0.08F;
            float motionX = velocity * MathHelper.cos(yaw);
            float motionZ = velocity * MathHelper.sin(yaw);
            this.getWorld().addParticle(ParticleTypes.FLAME, this.collidePosX, this.collidePosY + 0.1, this.collidePosZ, motionX, motionY, motionZ);
        }
        for (int i = 0; i < amount / 2; i++) {
            this.getWorld().addParticle(ParticleTypes.LAVA, this.collidePosX, this.collidePosY + 0.1, this.collidePosZ, 0, 0, 0);
        }
    }

    @Override
    protected void initDataTracker() {
        this.getDataTracker().startTracking(YAW, 0F);
        this.getDataTracker().startTracking(PITCH, 0F);
        this.getDataTracker().startTracking(DURATION, 0);
        this.getDataTracker().startTracking(HAS_PLAYER, false);
        this.getDataTracker().startTracking(CASTER, -1);
    }

    public float getYaw() {
        return this.getDataTracker().get(YAW);
    }

    public void setYaw(float yaw) {
        this.getDataTracker().set(YAW, yaw);
    }

    public float getPitch() {
        return this.getDataTracker().get(PITCH);
    }

    public void setPitch(float pitch) {
        this.getDataTracker().set(PITCH, pitch);
    }

    public int getDuration() {
        return this.getDataTracker().get(DURATION);
    }

    public void setDuration(int duration) {
        this.getDataTracker().set(DURATION, duration);
    }

    public boolean getHasPlayer() {
        return this.getDataTracker().get(HAS_PLAYER);
    }

    public void setHasPlayer(boolean player) {
        this.getDataTracker().set(HAS_PLAYER, player);
    }

    public int getCasterID() {
        return this.getDataTracker().get(CASTER);
    }

    public void setCasterID(int id) {
        this.getDataTracker().set(CASTER, id);
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
    }

    private void calculateEndPos() {
        double radius = this.caster instanceof EntityUmvuthi ? RADIUS_UMVUTHI : RADIUS_PLAYER;
        if (this.getWorld().isClient()) {
            this.endPosX = this.getX() + radius * Math.cos(this.renderYaw) * Math.cos(this.renderPitch);
            this.endPosZ = this.getZ() + radius * Math.sin(this.renderYaw) * Math.cos(this.renderPitch);
            this.endPosY = this.getY() + radius * Math.sin(this.renderPitch);
        } else {
            this.endPosX = this.getX() + radius * Math.cos(this.getYaw()) * Math.cos(this.getPitch());
            this.endPosZ = this.getZ() + radius * Math.sin(this.getYaw()) * Math.cos(this.getPitch());
            this.endPosY = this.getY() + radius * Math.sin(this.getPitch());
        }
    }

    public boolean hasDoneRaytrace() {
        return this.didRaytrace;
    }

    public SolarbeamHitResult raytraceEntities(World world, Vec3d from, Vec3d to, boolean stopOnLiquid, boolean ignoreBlockWithoutBoundingBox, boolean returnLastUncollidableBlock) {
        this.didRaytrace = true;
        SolarbeamHitResult result = new SolarbeamHitResult();
        result.setBlockHit(world.raycast(new RaycastContext(from, to, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, this)));
        if (result.blockHit != null) {
            Vec3d hitVec = result.blockHit.getPos();
            this.collidePosX = hitVec.x;
            this.collidePosY = hitVec.y;
            this.collidePosZ = hitVec.z;
            this.blockSide = result.blockHit.getSide();
        } else {
            this.collidePosX = this.endPosX;
            this.collidePosY = this.endPosY;
            this.collidePosZ = this.endPosZ;
            this.blockSide = null;
        }
        List<Entity> entities = world.getNonSpectatingEntities(Entity.class, new Box(Math.min(this.getX(), this.collidePosX), Math.min(this.getY(), this.collidePosY), Math.min(this.getZ(), this.collidePosZ), Math.max(this.getX(), this.collidePosX), Math.max(this.getY(), this.collidePosY), Math.max(this.getZ(), this.collidePosZ)).expand(1, 1, 1));
        for (Entity entity : entities) {
            if (entity == this.caster) {
                continue;
            }
            float pad = entity.getTargetingMargin() + 0.5f;
            Box aabb = entity.getBoundingBox().expand(pad, pad, pad);
            Optional<Vec3d> hit = aabb.raycast(from, to);
            if (aabb.contains(from)) {
                result.addEntityHit(entity);
            } else if (hit.isPresent()) {
                result.addEntityHit(entity);
            }
        }
        return result;
    }

    @Override
    public void pushAwayFrom(Entity entityIn) {
    }

    @Override
    public boolean canHit() {
        return false;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean shouldRender(double distance) {
        return distance < 1024;
    }

    private void updateWithPlayer() {
        this.setYaw((float) ((this.caster.headYaw + 90) * Math.PI / 180.0d));
        this.setPitch((float) (-this.caster.getPitch() * Math.PI / 180.0d));
        Vec3d vecOffset = this.caster.getRotationVector().normalize().multiply(1);
        this.setPosition(this.caster.getX() + vecOffset.getX(), this.caster.getY() + 1.2f + vecOffset.getY(), this.caster.getZ() + vecOffset.getZ());
    }

    private void updateWithUmvuthi() {
        this.setYaw((float) ((this.caster.headYaw + 90) * Math.PI / 180.0d));
        this.setPitch((float) (-this.caster.getPitch() * Math.PI / 180.0d));
        Vec3d vecOffset1 = new Vec3d(0, 0, 0.6).rotateY((float) Math.toRadians(-this.caster.getYaw()));
        Vec3d vecOffset2 = new Vec3d(1.2, 0, 0).rotateY(-this.getYaw()).rotateX(this.getPitch());
        this.setPosition(this.caster.getX() + vecOffset1.getX() + vecOffset2.getX(), this.caster.getY() + 1.5f + vecOffset1.getY() + vecOffset2.getY(), this.caster.getZ() + vecOffset1.getZ() + vecOffset2.getZ());
    }

    @Override
    public void remove(RemovalReason reason) {
        super.remove(reason);
        if (this.caster instanceof PlayerEntity) {
            PlayerCapability.IPlayerCapability playerCapability = AbilityCapability.get((PlayerEntity) this.caster);
            if (playerCapability != null) {
                playerCapability.setUsingSolarBeam(false);
            }
        }
    }

    public static class SolarbeamHitResult {
        private final List<Entity> entities = new ArrayList<>();
        private BlockHitResult blockHit;

        public BlockHitResult getBlockHit() {
            return this.blockHit;
        }

        public void setBlockHit(HitResult rayTraceResult) {
            if (rayTraceResult.getType() == HitResult.Type.BLOCK)
                this.blockHit = (BlockHitResult) rayTraceResult;
        }

        public void addEntityHit(Entity entity) {
            this.entities.add(entity);
        }
    }
}
