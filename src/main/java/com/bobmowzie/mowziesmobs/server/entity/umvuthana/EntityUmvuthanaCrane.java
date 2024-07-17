package com.bobmowzie.mowziesmobs.server.entity.umvuthana;

import com.bobmowzie.mowziesmobs.server.ability.AbilityHandler;
import com.bobmowzie.mowziesmobs.server.ai.AvoidProjectilesGoal;
import com.bobmowzie.mowziesmobs.server.ai.NearestAttackableTargetPredicateGoal;
import com.bobmowzie.mowziesmobs.server.ai.UmvuthanaHurtByTargetAI;
import com.bobmowzie.mowziesmobs.server.item.UmvuthanaMask;
import com.bobmowzie.mowziesmobs.server.potion.EffectHandler;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.FleeEntityGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.*;

import java.util.EnumSet;
import java.util.List;

public class EntityUmvuthanaCrane extends EntityUmvuthanaMinion {
    public boolean hasTriedOrSucceededTeleport = true;
    private int teleportAttempts = 0;

    public EntityUmvuthanaCrane(EntityType<? extends EntityUmvuthanaMinion> type, World world) {
        super(type, world);
        this.setWeapon(3);
        this.setMask(MaskType.FAITH);
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(1, new TeleportToSafeSpotGoal(this));
        this.goalSelector.add(1, new AvoidProjectilesGoal(this, ProjectileEntity.class, target -> this.getActiveAbilityType() == HEAL_ABILITY, 5.0F, 0.8D, 0.6D));
        this.goalSelector.add(4, new HealTargetGoal(this));
        this.goalSelector.add(6, new FleeEntityGoal<PlayerEntity>(this, PlayerEntity.class, 7.0F, 0.8D, 0.6D, target -> {
            if (target instanceof PlayerEntity) {
                if (this.getWorld().getDifficulty() == Difficulty.PEACEFUL) return false;
                if (this.getTarget() == target) return true;
                if (this.getTarget() instanceof EntityUmvuthi) return false;
                if (this.getActiveAbilityType() != null) return false;
                ItemStack headArmorStack = ((PlayerEntity) target).getInventory().armor.get(3);
                return !(headArmorStack.getItem() instanceof UmvuthanaMask) || target == this.getMisbehavedPlayer();
            }
            return true;
        }) {
            @Override
            public void stop() {
                super.stop();
                EntityUmvuthanaCrane.this.setMisbehavedPlayerId(null);
            }
        });
        this.goalSelector.add(6, new FleeEntityGoal<>(this, ZombieEntity.class, 7.0F, 0.8D, 0.6D));
        this.goalSelector.add(6, new FleeEntityGoal<>(this, AbstractSkeletonEntity.class, 7.0F, 0.8D, 0.6D));
    }

    @Override
    protected void registerTargetGoals() {
        this.targetSelector.add(3, new UmvuthanaHurtByTargetAI(this, true));
        this.targetSelector.add(2, new NearestAttackableTargetPredicateGoal<EntityUmvuthi>(this, EntityUmvuthi.class, 0, false, false, TargetPredicate.createNonAttackable().setBaseMaxDistance(this.getAttributeValue(EntityAttributes.GENERIC_FOLLOW_RANGE) * 2).setPredicate(target -> {
            if (!this.active) return false;
            if (target instanceof MobEntity) {
                return ((MobEntity) target).getTarget() != null || target.getHealth() < target.getMaxHealth();
            }
            return false;
        }).ignoreVisibility().ignoreDistanceScalingFactor()) {
            @Override
            public boolean shouldContinue() {
                LivingEntity livingentity = this.mob.getTarget();
                if (livingentity == null) {
                    livingentity = this.target;
                }
                boolean targetHasTarget = false;
                if (livingentity instanceof MobEntity) targetHasTarget = ((MobEntity) livingentity).getTarget() != null;
                boolean canHeal = true;
                if (this.mob instanceof EntityUmvuthana) canHeal = ((EntityUmvuthana) this.mob).canHeal(livingentity);
                return super.shouldContinue() && (livingentity.getHealth() < livingentity.getMaxHealth() || targetHasTarget) && canHeal;
            }

            @Override
            protected double getFollowRange() {
                return super.getFollowRange() * 2;
            }
        });
    }

    @Override
    public void tick() {
        super.tick();
        if (this.active && this.teleportAttempts > 3 && (this.getTarget() == null || !this.getTarget().isAlive()))
            this.hasTriedOrSucceededTeleport = true;
//        if (getActiveAbilityType() == HEAL_ABILITY && !canHeal(getTarget())) AbilityHandler.INSTANCE.sendInterruptAbilityMessage(this, HEAL_ABILITY);

//        if (getActiveAbility() == null) AbilityHandler.INSTANCE.sendAbilityMessage(this, HEAL_ABILITY);
    }

    @Override
    public boolean canHeal(LivingEntity entity) {
        return entity instanceof EntityUmvuthi;
    }

    @Override
    public void onDeath(DamageSource cause) {
        // If healing Umvuthi, set the attack target of any mob targeting the crane to Umvuthi
        if (this.getTarget() instanceof EntityUmvuthi) {
            List<MobEntity> targetingMobs = this.getWorld().getEntitiesByClass(MobEntity.class, this.getBoundingBox().expand(30), (e) -> e.getTarget() == this);
            if (cause.getAttacker() instanceof MobEntity damagingMob) {
                if (damagingMob.getTarget() == this && !targetingMobs.contains(damagingMob)) {
                    targetingMobs.add(damagingMob);
                }
            }
            for (MobEntity mob : targetingMobs) {
                mob.setTarget(this.getTarget());
            }
        }
        super.onDeath(cause);
    }

    @Override
    protected void sunBlockTarget() {
        LivingEntity target = this.getTarget();
        if (target != null) {
            EffectHandler.addOrCombineEffect(target, EffectHandler.SUNBLOCK, 20, 0, true, false);
            if (target.age % 20 == 0) target.heal(0.15f);
        }
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        boolean teleporting = this.getActiveAbilityType() == TELEPORT_ABILITY && this.getActiveAbility().getTicksInUse() <= 16;
        return super.isInvulnerableTo(source) || ((!this.active || teleporting || !this.hasTriedOrSucceededTeleport) && !source.isOf(DamageTypes.OUT_OF_WORLD) && this.timeUntilDeath != 0);
    }

    @Override
    public void setOnFireFor(int seconds) {
        boolean teleporting = this.getActiveAbilityType() == TELEPORT_ABILITY && this.getActiveAbility().getTicksInUse() <= 16;
        if (!this.active || teleporting || !this.hasTriedOrSucceededTeleport) return;
        super.setOnFireFor(seconds);
    }

    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason reason, EntityData livingData, NbtCompound compound) {
        this.setMask(MaskType.FAITH);
        this.setWeapon(3);
        return super.initialize(world, difficulty, reason, livingData, compound);
    }

    public static class HealTargetGoal extends Goal {
        private final EntityUmvuthana entity;

        public HealTargetGoal(EntityUmvuthana entityIn) {
            this.entity = entityIn;
            this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
        }

        @Override
        public boolean shouldContinue() {
            return this.entity.canHeal(this.entity.getTarget());
        }

        @Override
        public boolean canStart() {
            if (!this.entity.active) return false;
            return this.entity.canHeal(this.entity.getTarget());
        }

        @Override
        public void start() {
            super.start();
            AbilityHandler.INSTANCE.sendAbilityMessage(this.entity, EntityUmvuthana.HEAL_ABILITY);
        }
    }

    public class TeleportToSafeSpotGoal extends Goal {
        private final EntityUmvuthanaCrane entity;

        public TeleportToSafeSpotGoal(EntityUmvuthanaCrane entityIn) {
            this.entity = entityIn;
            this.setControls(EnumSet.of(Control.MOVE));
        }

        @Override
        public boolean canStart() {
            if (!this.entity.active) return false;
            if (this.entity.getActiveAbilityType() == TELEPORT_ABILITY) return false;
            if (this.entity.getTarget() != null && this.entity.canHeal(this.entity.getTarget()) && (
                    (this.entity.targetDistance >= 0 && this.entity.targetDistance < 11) || !EntityUmvuthanaCrane.this.hasTriedOrSucceededTeleport
            )) {
                return this.findTeleportLocation();
            }
            return false;
        }

        @Override
        public void start() {
            super.start();
            EntityUmvuthanaCrane.this.hasTriedOrSucceededTeleport = true;
            AbilityHandler.INSTANCE.sendAbilityMessage(this.entity, TELEPORT_ABILITY);
        }

        private boolean findTeleportLocation() {
            int i;
            int j;
            int k;
            if (this.entity.getPositionTargetRange() > -1) {
                i = MathHelper.floor(this.entity.getPositionTarget().getX());
                j = MathHelper.floor(this.entity.getPositionTarget().getY());
                k = MathHelper.floor(this.entity.getPositionTarget().getZ());
            } else if (this.entity.getTarget() != null) {
                i = MathHelper.floor(this.entity.getTarget().getX());
                j = MathHelper.floor(this.entity.getTarget().getY());
                k = MathHelper.floor(this.entity.getTarget().getZ());
            } else {
                i = MathHelper.floor(this.entity.getX());
                j = MathHelper.floor(this.entity.getY());
                k = MathHelper.floor(this.entity.getZ());
            }
            boolean foundPosition = false;
            for (int l = 0; l < 50; ++l) {
                double radius = Math.pow(EntityUmvuthanaCrane.this.random.nextFloat(), 1.35) * 25;
                double angle = EntityUmvuthanaCrane.this.random.nextFloat() * Math.PI * 2;
                int i1 = i + (int) (Math.cos(angle) * radius);
                int j1 = j + MathHelper.nextInt(this.entity.random, 0, 15) * MathHelper.nextInt(this.entity.random, -1, 1);
                int k1 = k + (int) (Math.sin(angle) * radius);
                BlockPos blockpos = new BlockPos(i1, j1, k1);
                Vec3d newPos = new Vec3d(i1, j1, k1);
                Vec3d offset = newPos.subtract(this.entity.getPos());
                Box newBB = this.entity.getBoundingBox().offset(offset);
                if (this.testBlock(blockpos, newBB) && this.entity.getWorld().getNonSpectatingEntities(EntityUmvuthi.class, newBB.expand(7)).isEmpty()) {
                    this.entity.teleportDestination = newPos.add(0, 0, 0);
                    if (this.entity.teleportAttempts >= 3) foundPosition = true;
                    if (this.entity.getWorld().getNonSpectatingEntities(EntityUmvuthanaCrane.class, newBB.expand(5)).isEmpty()) {
                        if (this.entity.teleportAttempts >= 2) foundPosition = true;
                        if (!this.entity.getWorld().isPlayerInRange(i1, j1, k1, 5) && !this.entity.getWorld().containsFluid(newBB)) {
                            if (this.entity.teleportAttempts >= 1) foundPosition = true;
                            LivingEntity target = EntityUmvuthanaCrane.this.getTarget();
                            if (target instanceof MobEntity && ((MobEntity) target).getTarget() != null) {
                                if (!this.canEntityBeSeenFromLocation(((MobEntity) target).getTarget(), newPos)) {
                                    return true;
                                }
                            } else return true;
                        }
                    }
                }
            }
            this.entity.teleportAttempts++;
            if (this.entity.teleportAttempts > 3) EntityUmvuthanaCrane.this.hasTriedOrSucceededTeleport = true;
            return foundPosition;
        }

        public boolean canEntityBeSeenFromLocation(Entity entityIn, Vec3d location) {
            Vec3d vector3d = new Vec3d(location.getX(), location.getY() + this.entity.getStandingEyeHeight(), location.getZ());
            Vec3d vector3d1 = new Vec3d(entityIn.getX(), entityIn.getEyeY(), entityIn.getZ());
            return this.entity.getWorld().raycast(new RaycastContext(vector3d, vector3d1, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, this.entity)).getType() != HitResult.Type.BLOCK;
        }

        public boolean testBlock(BlockPos blockpos, Box aabb) {
            World world = this.entity.getWorld();
            if (world.isChunkLoaded(blockpos)) {
                BlockPos blockpos1 = blockpos.down();
                BlockState blockstate = world.getBlockState(blockpos1);
                return blockstate.isSolid() && blockstate.blocksMovement() && world.isSpaceEmpty(aabb);
            }
            return false;
        }
    }
}
