package com.bobmowzie.mowziesmobs.server.entity.umvuthana;

import com.bobmowzie.mowziesmobs.server.ai.NearestAttackableTargetPredicateGoal;
import com.bobmowzie.mowziesmobs.server.potion.EffectHandler;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;

public class EntityUmvuthanaCraneToPlayer extends EntityUmvuthanaFollowerToPlayer {

    public EntityUmvuthanaCraneToPlayer(EntityType<? extends EntityUmvuthanaCraneToPlayer> type, World world) {
        this(type, world, null);
    }

    public EntityUmvuthanaCraneToPlayer(EntityType<? extends EntityUmvuthanaCraneToPlayer> type, World world, PlayerEntity leader) {
        super(type, world, leader);
        this.setMask(MaskType.FAITH);
        this.setWeapon(3);
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(4, new EntityUmvuthanaCrane.HealTargetGoal(this));
    }

    @Override
    protected void registerTargetGoals() {
        super.registerTargetGoals();
        this.targetSelector.add(2, new NearestAttackableTargetPredicateGoal<PlayerEntity>(this, PlayerEntity.class, 0, true, true, TargetPredicate.createNonAttackable().setBaseMaxDistance(this.getAttributeValue(EntityAttributes.GENERIC_FOLLOW_RANGE)).setPredicate(target -> {
            if (!this.active) return false;
            if (target != this.getLeader()) return false;
            return this.healAICheckTarget(target);
        }).ignoreDistanceScalingFactor()) {
            @Override
            public boolean shouldContinue() {
                LivingEntity livingentity = this.mob.getTarget();
                if (livingentity == null) {
                    livingentity = this.target;
                }
                return super.shouldContinue() && this.mob instanceof EntityUmvuthanaCraneToPlayer && ((EntityUmvuthanaCraneToPlayer) this.mob).healAICheckTarget(livingentity);
            }
        });
    }

    private boolean healAICheckTarget(LivingEntity livingentity) {
        if (livingentity != this.getLeader()) return false;
        boolean targetHasTarget = livingentity.getAttacking() != null && (livingentity.age - livingentity.getLastAttackTime() < 120 || livingentity.squaredDistanceTo(livingentity.getAttacking()) < 256);
        if (livingentity.getAttacking() instanceof EntityUmvuthanaFollowerToPlayer) targetHasTarget = false;
        boolean canHeal = this.canHeal(livingentity);
        boolean survivalMode = !livingentity.isSpectator() && !((PlayerEntity) livingentity).isCreative();
        return (livingentity.getHealth() < livingentity.getMaxHealth() || targetHasTarget) && canHeal && survivalMode;
    }

    public boolean canHeal(LivingEntity entity) {
        return entity == this.leader && entity != null && this.squaredDistanceTo(entity) < 256.0;
    }

    @Override
    protected void sunBlockTarget() {
        LivingEntity target = this.getTarget();
        if (target != null && target == this.getLeader()) {
            EffectHandler.addOrCombineEffect(target, EffectHandler.SUNBLOCK, 20, 0, true, false);
        }
    }

    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason reason, EntityData livingData, NbtCompound compound) {
        this.setMask(MaskType.FAITH);
        this.setWeapon(3);
        return super.initialize(world, difficulty, reason, livingData, compound);
    }
}
