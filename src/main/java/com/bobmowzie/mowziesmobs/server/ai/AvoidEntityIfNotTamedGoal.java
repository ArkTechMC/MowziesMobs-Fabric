package com.bobmowzie.mowziesmobs.server.ai;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.FleeEntityGoal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.TameableEntity;

public class AvoidEntityIfNotTamedGoal<T extends LivingEntity> extends FleeEntityGoal<T> {
    public AvoidEntityIfNotTamedGoal(PathAwareEntity entityIn, Class classToAvoidIn, float avoidDistanceIn, double farSpeedIn, double nearSpeedIn) {
        super(entityIn, classToAvoidIn, avoidDistanceIn, farSpeedIn, nearSpeedIn);
    }

    @Override
    public boolean canStart() {
        boolean isTamed;
        isTamed = this.mob instanceof TameableEntity && ((TameableEntity) this.mob).isTamed();
        return super.canStart() && !isTamed;
    }
}
