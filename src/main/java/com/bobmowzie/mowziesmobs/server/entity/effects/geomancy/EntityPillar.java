package com.bobmowzie.mowziesmobs.server.entity.effects.geomancy;

import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntityMagicEffect;
import com.bobmowzie.mowziesmobs.server.entity.sculptor.EntitySculptor;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.List;

public class EntityPillar extends EntityGeomancyBase {
    public static final float RISING_SPEED = 0.25f;
    public static final HashMap<GeomancyTier, Integer> SIZE_MAP = new HashMap<>();
    private static final TrackedData<Float> HEIGHT = DataTracker.registerData(EntityPillar.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Boolean> RISING = DataTracker.registerData(EntityPillar.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> FALLING = DataTracker.registerData(EntityPillar.class, TrackedDataHandlerRegistry.BOOLEAN);

    static {
        SIZE_MAP.put(GeomancyTier.NONE, 1);
        SIZE_MAP.put(GeomancyTier.SMALL, 2);
        SIZE_MAP.put(GeomancyTier.MEDIUM, 3);
        SIZE_MAP.put(GeomancyTier.LARGE, 4);
        SIZE_MAP.put(GeomancyTier.HUGE, 5);
    }

    public float prevPrevHeight = 0;
    public float prevHeight = 0;
    private EntityPillarPiece currentPiece;

    public EntityPillar(EntityType<? extends EntityMagicEffect> type, World worldIn) {
        super(type, worldIn);
    }

    public EntityPillar(EntityType<? extends EntityPillar> type, World world, LivingEntity caster, BlockState blockState, BlockPos pos) {
        super(type, world, caster, blockState, pos);
        this.setDeathTime(300);
    }

    public boolean checkCanSpawn() {
        if (!this.getWorld().getNonSpectatingEntities(EntityPillar.class, this.getBoundingBox().contract(0.01)).isEmpty())
            return false;
        return this.getWorld().isSpaceEmpty(this, this.getBoundingBox().contract(0.01));
    }

    @Override
    public boolean collidesWith(Entity p_20303_) {
        return false;
    }

    @Override
    public boolean isCollidable() {
        return false;
    }

    @Override
    public void tick() {
        this.prevPrevHeight = this.prevHeight;
        this.prevHeight = this.getHeight();

        if (!this.getWorld().isClient()) {
            if (this.isRising()) {
                float height = this.getHeight();

                if (height == 0.0) {
                    this.currentPiece = new EntityPillarPiece(EntityHandler.PILLAR_PIECE, this.getWorld(), this, new Vec3d(this.getX(), this.getY() - 1.0f, this.getZ()));
                    this.getWorld().spawnEntity(this.currentPiece);
                }

                height += RISING_SPEED;
                this.setHeight(height);

                if (Math.floor(height) > Math.floor(this.prevHeight)) {
                    this.currentPiece = new EntityPillarPiece(EntityHandler.PILLAR_PIECE, this.getWorld(), this, new Vec3d(this.getX(), this.getY() + Math.floor(height) - 1.0f, this.getZ()));
                    this.getWorld().spawnEntity(this.currentPiece);
                }

                // If this pillar is not owned by a sculptor, check nearby boulders for tier upgrades
                if (!(this.caster instanceof EntitySculptor)) {
                    List<EntityBoulderProjectile> boulders = this.getWorld().getNonSpectatingEntities(EntityBoulderProjectile.class, this.getBoundingBox().contract(0.1f));
                    for (EntityBoulderProjectile boulder : boulders) {
                        if (!boulder.isTravelling() && boulder.getTier().ordinal() > this.getTier().ordinal()) {
                            this.setTier(boulder.getTier());
                            boulder.explode();
                        }
                    }
                }
            } else if (this.isFalling()) {
                float height = this.getHeight();
                height -= RISING_SPEED;
                this.setHeight(height);
                if (height <= 0.0) {
                    this.remove(RemovalReason.DISCARDED);
                }
            }
        }

        this.setBoundingBox(this.calculateBoundingBox());

        Box popUpBounds = this.getBoundingBox().contract(0.1f);
        List<Entity> popUpEntities = this.getWorld().getOtherEntities(this, popUpBounds);
        for (Entity entity : popUpEntities) {
            if (entity.canHit() && !(entity instanceof EntityBoulderBase) && !(entity instanceof EntityPillar) && !(entity instanceof EntityPillarPiece)) {
                double belowAmount = entity.getY() - (this.getY() + this.getHeight());
                if (belowAmount < 0.0) entity.move(MovementType.PISTON, new Vec3d(0, -belowAmount, 0));
            }
        }
        super.tick();
        if (this.hasSyncedCaster && (this.caster == null || this.caster.isRemoved())) this.explode();
    }

    @Override
    protected Box calculateBoundingBox() {
        if (this.age <= 1) return super.calculateBoundingBox();
        float f = SIZE_MAP.get(this.getTier()) / 2.0F - 0.05f;
        return new Box(this.getX() - (double) f, this.getY(), this.getZ() - (double) f, this.getX() + (double) f, this.getY() + this.getHeight() - 0.05f, this.getZ() + (double) f);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.getDataTracker().startTracking(HEIGHT, 0.0f);
        this.getDataTracker().startTracking(RISING, true);
        this.getDataTracker().startTracking(FALLING, false);
    }

    public float getHeight() {
        return this.getDataTracker().get(HEIGHT);
    }

    public void setHeight(float height) {
        this.getDataTracker().set(HEIGHT, height);
    }

    public void stopRising() {
        this.getDataTracker().set(RISING, false);
        this.setBoundingBox(this.calculateBoundingBox());
        this.currentPiece = new EntityPillarPiece(EntityHandler.PILLAR_PIECE, this.getWorld(), this, new Vec3d(this.getX(), this.getY() + this.getHeight() - 1.0f, this.getZ()));
        this.getWorld().spawnEntity(this.currentPiece);
    }

    public boolean isRising() {
        return this.getDataTracker().get(RISING);
    }

    public void startFalling() {
        this.getDataTracker().set(RISING, false);
        this.getDataTracker().set(FALLING, true);
    }

    public void startRising() {
        this.getDataTracker().set(RISING, true);
        this.getDataTracker().set(FALLING, false);
    }

    public boolean isFalling() {
        return this.getDataTracker().get(FALLING);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound compound) {
        super.writeCustomDataToNbt(compound);
        compound.putFloat("height", this.getHeight());
        compound.putBoolean("rising", this.isRising());
        compound.putBoolean("falling", this.isFalling());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound compound) {
        super.readCustomDataFromNbt(compound);
        this.setHeight(compound.getFloat("height"));
        this.getDataTracker().set(RISING, compound.getBoolean("rising"));
        this.getDataTracker().set(FALLING, compound.getBoolean("falling"));
    }

    @Override
    public boolean doRemoveTimer() {
        return !(this.caster instanceof EntitySculptor);
    }

    public static class EntityPillarSculptor extends EntityPillar {

        public EntityPillarSculptor(EntityType<? extends EntityPillarSculptor> type, World worldIn) {
            super(type, worldIn);
        }

        public EntityPillarSculptor(EntityType<? extends EntityPillarSculptor> type, World world, LivingEntity caster, BlockState blockState, BlockPos pos) {
            super(type, world, caster, blockState, pos);
            this.setDeathTime(300);
        }

        public double getDesiredHeight() {
            return EntitySculptor.TEST_HEIGHT;
        }

        @Override
        public void tick() {
            if (this.caster instanceof EntitySculptor sculptor) {
                if (sculptor.getPillar() == null) sculptor.setPillar(this);
            }
            super.tick();
            if (this.getHeight() >= this.getDesiredHeight()) {
                this.stopRising();
            }
        }

        @Override
        public void stopRising() {
            super.stopRising();
            if (this.caster instanceof EntitySculptor sculptor) {
                sculptor.numLivePaths = 0;
            }
        }
    }
}
