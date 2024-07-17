package com.bobmowzie.mowziesmobs.server.ai;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.NoPenaltyTargeting;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.EnumSet;
import java.util.List;

public class MMAIAvoidEntity<U extends PathAwareEntity, T extends Entity> extends Goal {
    private static final double NEAR_DISTANCE = 7.0D;

    protected final U entity;

    private final Predicate<T> selector;

    private final double farSpeed;

    private final double nearSpeed;

    private final float evadeDistance;

    private final Class<T> avoidedEntityType;

    private final int horizontalEvasion;

    private final int verticalEvasion;

    private final int numChecks;

    private T entityEvading;

    private Path entityPathEntity;

    public MMAIAvoidEntity(U entity, Class<T> avoidedEntityType, float evadeDistance, double farSpeed, double nearSpeed) {
        this(entity, avoidedEntityType, Predicates.alwaysTrue(), evadeDistance, farSpeed, nearSpeed, 10, 12, 7);
    }

    public MMAIAvoidEntity(U entity, Class<T> avoidedEntityType, float evadeDistance, double farSpeed, double nearSpeed, int numChecks, int horizontalEvasion, int verticalEvasion) {
        this(entity, avoidedEntityType, Predicates.alwaysTrue(), evadeDistance, farSpeed, nearSpeed, numChecks, horizontalEvasion, verticalEvasion);
    }

    public MMAIAvoidEntity(U entity, Class<T> avoidedEntityType, Predicate<? super T> predicate, float evadeDistance, double farSpeed, double nearSpeed, int numChecks, int horizontalEvasion, int verticalEvasion) {
        this.entity = entity;
        this.selector = e -> e != null &&
                EntityPredicates.EXCEPT_CREATIVE_OR_SPECTATOR.test(e) &&
                e.isAlive() &&
                entity.getVisibilityCache().canSee(e) &&
                !entity.isTeammate(e) &&
                predicate.test(e);
        this.avoidedEntityType = avoidedEntityType;
        this.evadeDistance = evadeDistance;
        this.farSpeed = farSpeed;
        this.nearSpeed = nearSpeed;
        this.numChecks = numChecks;
        this.horizontalEvasion = horizontalEvasion;
        this.verticalEvasion = verticalEvasion;
        this.setControls(EnumSet.of(Control.MOVE));
    }

    @Override
    public boolean canStart() {
        List<T> entities = this.entity.getWorld().getEntitiesByClass(this.avoidedEntityType, this.entity.getBoundingBox().expand(this.evadeDistance, 3.0D, this.evadeDistance), this.selector);
        if (entities.isEmpty()) {
            this.onSafe();
            return false;
        }
        this.entityEvading = entities.get(0);
        for (int n = 0; n < this.numChecks; n++) {
            Vec3d pos = NoPenaltyTargeting.findFrom(this.entity, this.horizontalEvasion, this.verticalEvasion, this.entityEvading.getPos());
            if (pos != null && !(this.entityEvading.squaredDistanceTo(pos.x, pos.y, pos.z) < this.entityEvading.squaredDistanceTo(this.entity))) {
                this.entityPathEntity = this.entity.getNavigation().findPathTo(BlockPos.ofFloored(pos), 0);
                if (this.entityPathEntity != null) {
                    return true;
                }
            }
        }
        this.onPathNotFound();
        return false;
    }

    protected void onSafe() {
    }

    protected void onPathNotFound() {
    }

    @Override
    public boolean shouldContinue() {
        return !this.entity.getNavigation().isIdle();
    }

    @Override
    public void start() {
        this.entity.getNavigation().startMovingAlong(this.entityPathEntity, this.farSpeed);
    }

    @Override
    public void stop() {
        this.entityEvading = null;
    }

    @Override
    public void tick() {
        this.entity.getNavigation().setSpeed(this.entity.squaredDistanceTo(this.entityEvading) < NEAR_DISTANCE * NEAR_DISTANCE ? this.nearSpeed : this.farSpeed);
    }
}