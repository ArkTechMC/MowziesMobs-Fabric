package com.bobmowzie.mowziesmobs.server.entity.umvuthana;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public abstract class EntityUmvuthanaFollower<L extends LivingEntity> extends EntityUmvuthana {
    protected static final Optional<UUID> ABSENT_LEADER = Optional.empty();

    private static final TrackedData<Optional<UUID>> LEADER = DataTracker.registerData(EntityUmvuthanaFollower.class, TrackedDataHandlerRegistry.OPTIONAL_UUID);

    private final Class<L> leaderClass;

    public int index;
    public boolean shouldSetDead;
    protected L leader;

    public EntityUmvuthanaFollower(EntityType<? extends EntityUmvuthanaFollower> type, World world, Class<L> leaderClass) {
        this(type, world, leaderClass, null);
    }

    public EntityUmvuthanaFollower(EntityType<? extends EntityUmvuthanaFollower> type, World world, Class<L> leaderClass, L leader) {
        super(type, world);
        this.leaderClass = leaderClass;
        if (leader != null) {
            this.setLeaderUUID(leader.getUuid());
        }
        this.shouldSetDead = false;
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.getDataTracker().startTracking(LEADER, ABSENT_LEADER);
    }

    public Optional<UUID> getLeaderUUID() {
        return this.getDataTracker().get(LEADER);
    }

    public void setLeaderUUID(UUID uuid) {
        this.setLeaderUUID(Optional.of(uuid));
    }

    public void setLeaderUUID(Optional<UUID> uuid) {
        this.getDataTracker().set(LEADER, uuid);
    }

//    @Override
//    public ItemStack getPickedResult(HitResult target) {
//        return new ItemStack(ItemHandler.UMVUTHANA_SPAWN_EGG);
//    }

    @Override
    public void tick() {
        super.tick();
        if (this.leader == null && this.getLeaderUUID().isPresent()) {
            this.leader = this.getLeader();
            if (this.leader != null) {
                this.addAsPackMember();
            }
        }
        if (this.shouldSetDead) this.discard();
    }

    @Override
    protected Vec3d updateCirclingPosition(float radius, float speed) {
        LivingEntity target = this.getTarget();
        if (this.leader != null && target != null) {
            return this.circleEntityPosition(target, radius, speed, true, this.getGroupCircleTick(), (float) ((this.index + 1) * (Math.PI * 2) / (this.getPackSize() + 1)));
        } else {
            return super.updateCirclingPosition(radius, speed);
        }
    }

    @Override
    public void onDeath(DamageSource source) {
        super.onDeath(source);
        if (this.leader != null) {
            this.removeAsPackMember();
        }
    }

    public void setShouldSetDead() {
        this.shouldSetDead = true;
    }

    @Override
    public void remove(RemovalReason reason) {
        if (this.leader != null) {
            this.removeAsPackMember();
        }
        super.remove(reason);
    }

    public L getLeader() {
        Optional<UUID> uuid = this.getLeaderUUID();
        if (uuid.isPresent()) {
            List<L> potentialLeaders = this.getWorld().getNonSpectatingEntities(this.leaderClass, this.getBoundingBox().expand(32, 32, 32));
            for (L entity : potentialLeaders) {
                if (uuid.get().equals(entity.getUuid())) {
                    return entity;
                }
            }
        }
        return null;
    }

    @Override
    public boolean cannotDespawn() {
        return this.leader != null;
    }

    protected abstract int getGroupCircleTick();

    protected abstract int getPackSize();

    protected abstract void addAsPackMember();

    protected abstract void removeAsPackMember();

    @Override
    public void writeCustomDataToNbt(NbtCompound compound) {
        super.writeCustomDataToNbt(compound);
        Optional<UUID> leader = this.getLeaderUUID();
        leader.ifPresent(value -> compound.putString("leaderUUID", value.toString()));
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound compound) {
        super.readCustomDataFromNbt(compound);
        String uuid = compound.getString("leaderUUID");
        if (uuid.isEmpty()) {
            this.setLeaderUUID(ABSENT_LEADER);
        } else {
            this.setLeaderUUID(UUID.fromString(uuid));
        }
    }
}
