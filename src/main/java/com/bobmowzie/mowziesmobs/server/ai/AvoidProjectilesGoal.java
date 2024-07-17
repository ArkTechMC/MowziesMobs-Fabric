package com.bobmowzie.mowziesmobs.server.ai;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.NoPenaltyTargeting;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

// Copied from AvoidEntityGoal
public class AvoidProjectilesGoal extends Goal {
    protected final PathAwareEntity entity;
    protected final float avoidDistance;
    protected final EntityNavigation navigation;
    /**
     * Class of entity this behavior seeks to avoid
     */
    protected final Class<ProjectileEntity> classToAvoid;
    protected final Predicate<ProjectileEntity> avoidTargetSelector;
    private final double farSpeed;
    private final double nearSpeed;
    protected ProjectileEntity avoidTarget;
    protected Path path;
    protected Vec3d dodgeVec;
    private int dodgeTimer = 0;

    public AvoidProjectilesGoal(PathAwareEntity entityIn, Class<ProjectileEntity> classToAvoidIn, float avoidDistanceIn, double farSpeedIn, double nearSpeedIn) {
        this(entityIn, classToAvoidIn, (entity) -> {
            return true;
        }, avoidDistanceIn, farSpeedIn, nearSpeedIn);
    }

    public AvoidProjectilesGoal(PathAwareEntity entityIn, Class<ProjectileEntity> avoidClass, Predicate<ProjectileEntity> targetPredicate, float distance, double nearSpeedIn, double farSpeedIn) {
        this.entity = entityIn;
        this.classToAvoid = avoidClass;
        this.avoidTargetSelector = targetPredicate.and(target -> {
            Vec3d aActualMotion = new Vec3d(target.getX() - target.prevX, target.getY() - target.prevY, target.getZ() - target.prevZ);
            if (aActualMotion.length() < 0.1 || target.age < 0) {
                return false;
            }
            if (!this.entity.getVisibilityCache().canSee(target)) return false;
            float dot = (float) target.getVelocity().normalize().dotProduct(this.entity.getPos().subtract(target.getPos()).normalize());
            return !(dot < 0.8);
        });
        this.avoidDistance = distance;
        this.farSpeed = nearSpeedIn;
        this.nearSpeed = farSpeedIn;
        this.navigation = entityIn.getNavigation();
        this.setControls(EnumSet.of(Control.MOVE));
    }

    /**
     * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
     * method as well.
     */
    public boolean canStart() {
        if (this.dodgeTimer > 0) return false;
        this.avoidTarget = this.getMostMovingTowardsMeEntity(this.classToAvoid, this.avoidTargetSelector, this.entity, this.entity.getBoundingBox().expand(this.avoidDistance, 3.0D, this.avoidDistance));
        if (this.avoidTarget == null) {
            return false;
        } else {
            Vec3d projectilePos = this.guessProjectileDestination(this.avoidTarget);
//            Vector3d vector3d = entity.getPositionVec().subtract(projectilePos);
//            entity.setMotion(entity.getMotion().add(vector3d.normalize().scale(1.0)));

            this.dodgeVec = this.avoidTarget.getVelocity().crossProduct(new Vec3d(0, 1, 0)).normalize().multiply(1);
            Vec3d newPosLeft = this.entity.getPos().add(this.dodgeVec);
            Vec3d newPosRight = this.entity.getPos().add(this.dodgeVec.multiply(-1));
            Vec3d diffLeft = newPosLeft.subtract(projectilePos);
            Vec3d diffRight = newPosRight.subtract(projectilePos);
            if (diffRight.lengthSquared() > diffLeft.lengthSquared()) {
                this.dodgeVec = this.dodgeVec.multiply(-1);
            }
            Vec3d dodgeDest = diffRight.lengthSquared() > diffLeft.lengthSquared() ? newPosRight : newPosLeft;
            Vec3d vector3d = NoPenaltyTargeting.findTo(this.entity, 5, 3, dodgeDest, (float) Math.PI / 2F);
            if (vector3d == null) {
                this.path = null;
                return true;
            } else if (projectilePos.subtract(vector3d).lengthSquared() < projectilePos.subtract(this.entity.getPos()).lengthSquared()) {
                return false;
            } else {
                this.path = this.navigation.findPathTo(vector3d.x, vector3d.y, vector3d.z, 0);
                return true;
            }
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean shouldContinue() {
        return !this.navigation.isIdle() || this.dodgeTimer < 10;
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void start() {
        if (this.path != null) {
            this.navigation.startMovingAlong(this.path, this.farSpeed);
            this.dodgeVec = this.path.getNodePosition(this.entity).subtract(this.entity.getPos()).normalize().multiply(1);
        }
        this.entity.setVelocity(this.entity.getVelocity().add(this.dodgeVec));
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    public void stop() {
        this.avoidTarget = null;
        this.dodgeTimer = 0;
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    public void tick() {
        if (this.entity.squaredDistanceTo(this.avoidTarget) < 49.0D) {
            this.entity.getNavigation().setSpeed(this.nearSpeed);
        } else {
            this.entity.getNavigation().setSpeed(this.farSpeed);
        }
        this.dodgeTimer++;

    }

    private Vec3d guessProjectileDestination(ProjectileEntity projectile) {
        Vec3d vector3d = projectile.getPos();
        Vec3d vector3d1 = vector3d.add(projectile.getVelocity().multiply(50));
        return this.entity.getWorld().raycast(new RaycastContext(vector3d, vector3d1, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, projectile)).getPos();
    }

    @Nullable
    private <T extends ProjectileEntity> T getMostMovingTowardsMeEntity(Class<? extends T> entityClazz, Predicate<? super T> predicate, LivingEntity entity, Box p_225318_10_) {
        return this.getMostMovingTowardsMeEntityFromList(entity.getWorld().getEntitiesByClass(entityClazz, p_225318_10_, predicate), entity);
    }

    private <T extends ProjectileEntity> T getMostMovingTowardsMeEntityFromList(List<? extends T> entities, LivingEntity target) {
        double d0 = -2.0D;
        T t = null;

        for (T t1 : entities) {
            double d1 = t1.getVelocity().normalize().dotProduct(target.getPos().subtract(t1.getPos()).normalize());
            if (d1 > d0) {
                d0 = d1;
                t = t1;
            }
        }

        return t;
    }
}
