package com.bobmowzie.mowziesmobs.server.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.MobEntity;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class LookAtTargetGoal extends Goal {
    protected final MobEntity mob;
    protected final float lookDistance;
    private final boolean onlyHorizontal;
    @Nullable
    protected Entity lookAt;

    public LookAtTargetGoal(MobEntity mob, float distance) {
        this(mob, distance, false);
    }

    public LookAtTargetGoal(MobEntity mob, float distance, boolean onlyHorizontal) {
        this.mob = mob;
        this.lookDistance = distance;
        this.onlyHorizontal = onlyHorizontal;
        this.setControls(EnumSet.of(Control.LOOK));
    }

    public boolean canStart() {
        if (this.mob.getTarget() != null) {
            this.lookAt = this.mob.getTarget();
        }
        return this.lookAt != null;
    }

    public boolean shouldContinue() {
        if (this.lookAt == null) {
            return false;
        } else if (this.lookAt != this.mob.getTarget()) {
            return false;
        } else if (!this.lookAt.isAlive()) {
            return false;
        } else return !(this.mob.squaredDistanceTo(this.lookAt) > (double) (this.lookDistance * this.lookDistance));
    }

    public void stop() {
        this.lookAt = null;
    }

    public void tick() {
        if (this.lookAt.isAlive()) {
            double d0 = this.onlyHorizontal ? this.mob.getEyeY() : this.lookAt.getEyeY();
            this.mob.getLookControl().lookAt(this.lookAt.getX(), d0, this.lookAt.getZ());
        }
    }
}
