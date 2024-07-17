package com.bobmowzie.mowziesmobs.server.ai;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.mob.MobEntity;


public class NearestAttackableTargetPredicateGoal<T extends LivingEntity> extends ActiveTargetGoal<T> {

    public NearestAttackableTargetPredicateGoal(MobEntity goalOwnerIn, Class targetClassIn, int targetChanceIn, boolean checkSight, boolean nearbyOnlyIn, TargetPredicate predicate) {
        super(goalOwnerIn, targetClassIn, targetChanceIn, checkSight, nearbyOnlyIn, null);
        this.targetPredicate = predicate;
    }
}
