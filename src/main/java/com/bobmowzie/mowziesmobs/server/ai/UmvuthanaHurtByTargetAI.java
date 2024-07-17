package com.bobmowzie.mowziesmobs.server.ai;

import com.bobmowzie.mowziesmobs.server.entity.umvuthana.EntityUmvuthana;
import com.bobmowzie.mowziesmobs.server.entity.umvuthana.EntityUmvuthi;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.scoreboard.AbstractTeam;
import net.minecraft.util.math.Box;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class UmvuthanaHurtByTargetAI extends RevengeGoal {
    private final boolean checkSight;
    private int unseenTicks;

    public UmvuthanaHurtByTargetAI(PathAwareEntity entity, boolean checkSight, Class<?>... p_26040_) {
        super(entity, p_26040_);
        this.setGroupRevenge();
        this.checkSight = checkSight;
    }

    @Override
    protected void callSameTypeForRevenge() {
        double d0 = this.getFollowRange();
        Box aabb = Box.from(this.mob.getPos()).expand(d0, 10.0D, d0);
        List<? extends PathAwareEntity> listUmvuthana = this.mob.getWorld().getEntitiesByClass(EntityUmvuthana.class, aabb, EntityPredicates.EXCEPT_SPECTATOR.and(e ->
                ((EntityUmvuthana) e).isUmvuthiDevoted()));
        List<? extends PathAwareEntity> listUmvuthi = this.mob.getWorld().getEntitiesByClass(EntityUmvuthi.class, aabb, EntityPredicates.EXCEPT_SPECTATOR);
        List<PathAwareEntity> list = new ArrayList<>();
        list.addAll(listUmvuthana);
        list.addAll(listUmvuthi);
        Iterator iterator = list.iterator();

        while (true) {
            MobEntity mob;
            while (true) {
                if (!iterator.hasNext()) {
                    return;
                }

                mob = (MobEntity) iterator.next();
                if (this.mob != mob && mob.getTarget() == null && (!(this.mob instanceof TameableEntity) || ((TameableEntity) this.mob).getOwner() == ((TameableEntity) mob).getOwner()) && !mob.isTeammate(this.mob.getAttacker())) {
                    if (this.noHelpTypes == null) {
                        break;
                    }

                    boolean flag = false;

                    for (Class<?> oclass : this.noHelpTypes) {
                        if (mob.getClass() == oclass) {
                            flag = true;
                            break;
                        }
                    }

                    if (!flag) {
                        break;
                    }
                }
            }

            this.setMobEntityTarget(mob, this.mob.getAttacker());
        }
    }

    @Override
    protected double getFollowRange() {
        return super.getFollowRange() * 1.7;
    }

    public boolean shouldContinue() {
        LivingEntity livingentity = this.mob.getTarget();
        if (livingentity == null) {
            livingentity = this.target;
        }

        if (livingentity == null) {
            return false;
        } else if (!this.mob.canTarget(livingentity)) {
            return false;
        } else {
            AbstractTeam team = this.mob.getScoreboardTeam();
            AbstractTeam team1 = livingentity.getScoreboardTeam();
            if (team != null && team1 == team) {
                return false;
            } else {
                double d0 = this.getFollowRange();
                if (this.mob.squaredDistanceTo(livingentity) > d0 * d0) {
                    return false;
                } else {
                    if (this.checkSight) {
                        if (this.mob.getVisibilityCache().canSee(livingentity)) {
                            this.unseenTicks = 0;
                        } else if (++this.unseenTicks > toGoalTicks(this.maxTimeWithoutVisibility)) {
                            return false;
                        }
                    }

                    this.mob.setTarget(livingentity);
                    return true;
                }
            }
        }
    }

    public void start() {
        super.start();
        this.unseenTicks = 0;
    }
}