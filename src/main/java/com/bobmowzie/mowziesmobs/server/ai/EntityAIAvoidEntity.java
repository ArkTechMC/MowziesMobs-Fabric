package com.bobmowzie.mowziesmobs.server.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.NoPenaltyTargeting;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

public final class EntityAIAvoidEntity<T extends Entity> extends Goal {
    private final PathAwareEntity entity;

    private final Class<T> avoidClass;

    private final float distance;

    private final Predicate<T> predicate;

    private final double speed;

    private final EntityNavigation navigator;

    private T avoiding;

    private Path path;

    public EntityAIAvoidEntity(PathAwareEntity entity, Class<T> avoidClass, float distance, double speed) {
        this(entity, avoidClass, e -> true, distance, speed);
    }

    public EntityAIAvoidEntity(PathAwareEntity entity, Class<T> avoidClass, Predicate<? super T> predicate, float distance, double speed) {
        this.entity = entity;
        this.avoidClass = avoidClass;
        this.distance = distance;
        Predicate<T> visible = e -> e.isAlive() && entity.getVisibilityCache().canSee(e);
        Predicate<T> targetable = e -> !(e instanceof PlayerEntity) || !e.isSpectator() && !((PlayerEntity) e).isCreative();
        this.predicate = targetable.and(predicate).and(visible);
        this.speed = speed;
        this.navigator = entity.getNavigation();
        this.setControls(EnumSet.of(Control.MOVE));
    }

    @Override
    public boolean canStart() {
        List<T> entities = this.entity.getWorld().getEntitiesByClass(this.avoidClass, this.entity.getBoundingBox().expand(this.distance, 3, this.distance), this.predicate);
        if (entities.isEmpty()) {
            return false;
        }
        this.avoiding = entities.get(this.entity.getRandom().nextInt(entities.size()));
        Vec3d pos = NoPenaltyTargeting.findFrom(this.entity, (int) (this.distance + 1), (int) (this.distance / 2 + 1), new Vec3d(this.avoiding.getX(), this.avoiding.getY(), this.avoiding.getZ()));
        if (pos == null) {
            return false;
        }
        if (this.avoiding.squaredDistanceTo(pos.x, pos.y, pos.z) < this.avoiding.squaredDistanceTo(this.entity)) {
            return false;
        }
        this.path = this.navigator.findPathTo(BlockPos.ofFloored(pos), 0);
        return this.path != null;
    }

    @Override
    public boolean shouldContinue() {
        return !this.navigator.isIdle();
    }

    @Override
    public void start() {
        this.navigator.startMovingAlong(this.path, this.speed);
    }

    @Override
    public void stop() {
        this.avoiding = null;
    }

    @Override
    public void tick() {
        this.entity.getNavigation().setSpeed(this.speed);
    }
}