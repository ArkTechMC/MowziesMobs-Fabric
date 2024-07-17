package com.bobmowzie.mowziesmobs.server.entity.effects.geomancy;

import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Optional;
import java.util.UUID;

public class EntityPillarPiece extends Entity {
    private static final TrackedData<Optional<UUID>> PILLAR = DataTracker.registerData(EntityPillarPiece.class, TrackedDataHandlerRegistry.OPTIONAL_UUID);
    private static final TrackedData<Integer> TIER = DataTracker.registerData(EntityPillarPiece.class, TrackedDataHandlerRegistry.INTEGER);

    private EntityPillar pillar;

    public EntityPillarPiece(EntityType<?> type, World level) {
        super(type, level);
    }

    public EntityPillarPiece(EntityType<?> type, World level, EntityPillar pillar, Vec3d position) {
        super(type, level);
        this.pillar = pillar;
        setTier(pillar.getTier());
        this.updatePosition(position.x, position.y, position.z);
    }

    @Override
    public boolean isCollidable() {
        return true;
    }

    @Override
    public boolean collidesWith(Entity entity) {
        if (entity instanceof EntityPillar || entity instanceof EntityPillarPiece) return false;
        return super.collidesWith(entity);
    }

    @Override
    public void tick() {
        if (!getWorld().isClient()) {
            if (pillar == null) {
                pillar = getPillar();
                if (pillar == null) {
                    remove(RemovalReason.DISCARDED);
                    return;
                }
            }
            if (pillar.isRemoved()) {
                remove(RemovalReason.DISCARDED);
                return;
            }
            if (pillar.isFalling() && pillar.getHeight() + pillar.getY() < getY() + 1) {
                remove(RemovalReason.DISCARDED);
                return;
            }
            setTier(pillar.getTier());
        }

        super.tick();
        setBoundingBox(calculateBoundingBox());
    }

    @Override
    protected Box calculateBoundingBox() {
        float f = EntityPillar.SIZE_MAP.get(getTier()) / 2.0F;
        return new Box(getX() - (double) f, getY(), getZ() - (double) f, getX() + (double) f, getY() + 1, getZ() + (double) f);
    }

    @Override
    protected void initDataTracker() {
        getDataTracker().startTracking(PILLAR, Optional.empty());
        getDataTracker().startTracking(TIER, 0);
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound compound) {
        setPillarUUID(compound.getUuid("pillar"));
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound compound) {
        if (pillar != null) compound.putUuid("pillar", pillar.getUuid());
    }

    @Override
    public boolean doesRenderOnFire() {
        return false;
    }

    @Override
    public PistonBehavior getPistonBehavior() {
        return PistonBehavior.BLOCK;
    }

    @Override
    public boolean isImmuneToExplosion() {
        return true;
    }

    public Optional<UUID> getPillarUUID() {
        return getDataTracker().get(PILLAR);
    }

    public void setPillarUUID(UUID uuid) {
        this.getDataTracker().set(PILLAR, Optional.of(uuid));
    }

    public EntityPillar getPillar() {
        Optional<UUID> uuid = this.getPillarUUID();
        if (uuid.isPresent() && !this.getWorld().isClient) {
            return (EntityPillar) ((ServerWorld) this.getWorld()).getEntity(uuid.get());
        }
        return null;
    }

    public EntityGeomancyBase.GeomancyTier getTier() {
        return EntityGeomancyBase.GeomancyTier.values()[this.dataTracker.get(TIER)];
    }

    public void setTier(EntityGeomancyBase.GeomancyTier size) {
        this.dataTracker.set(TIER, size.ordinal());
    }
}
