package com.bobmowzie.mowziesmobs.server.entity.foliaath;

import com.bobmowzie.mowziesmobs.client.model.tools.IntermittentAnimation;
import com.bobmowzie.mowziesmobs.server.ai.animation.AnimationAttackAI;
import com.bobmowzie.mowziesmobs.server.ai.animation.AnimationDieAI;
import com.bobmowzie.mowziesmobs.server.ai.animation.AnimationTakeDamage;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import com.bobmowzie.mowziesmobs.server.entity.MowzieLLibraryEntity;
import com.bobmowzie.mowziesmobs.server.loot.LootTableHandler;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import com.iafenvoy.uranus.animation.Animation;
import com.iafenvoy.uranus.animation.AnimationHandler;
import com.iafenvoy.uranus.client.model.tools.ControlledAnimation;
import net.minecraft.block.BlockState;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

public class EntityFoliaath extends MowzieLLibraryEntity implements Monster {
    public static final Animation DIE_ANIMATION = Animation.create(50);
    public static final Animation HURT_ANIMATION = Animation.create(10);
    public static final Animation ATTACK_ANIMATION = Animation.create(14);
    private static final TrackedData<Boolean> CAN_DESPAWN = DataTracker.registerData(EntityFoliaath.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Integer> ACTIVATE_TARGET = DataTracker.registerData(EntityFoliaath.class, TrackedDataHandlerRegistry.INTEGER);
    private static final int ACTIVATE_DURATION = 30;
    public IntermittentAnimation<EntityFoliaath> openMouth = new IntermittentAnimation<>(this, 15, 30, 50, !this.getWorld().isClient);
    public ControlledAnimation activate = new ControlledAnimation(ACTIVATE_DURATION);
    public ControlledAnimation deathFlail = new ControlledAnimation(5);
    public ControlledAnimation stopDance = new ControlledAnimation(10);
    public int lastTimeDecrease = 0;
    private int resettingTargetTimer = 0;
    private double prevOpenMouth;
    private double prevActivate;
    private int activateTarget;

    public EntityFoliaath(EntityType<? extends EntityFoliaath> type, World world) {
        super(type, world);
        this.experiencePoints = 5;
        this.addIntermittentAnimation(this.openMouth);
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MowzieEntity.createAttributes().add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 8)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 10)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 1);
    }

    protected boolean isMovementNoisy() {
        return false;
    }

    @Override
    public PistonBehavior getPistonBehavior() {
        return PistonBehavior.IGNORE;
    }

    @Override
    public boolean isPushedByFluids() {
        return false;
    }

    @Override
    public void addVelocity(double x, double y, double z) {
        super.addVelocity(0, y, 0);
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(1, new AnimationAttackAI<>(this, ATTACK_ANIMATION, MMSounds.ENTITY_FOLIAATH_BITE_1, null, 2, 4F, 1, 3));
        this.goalSelector.add(1, new AnimationTakeDamage<>(this));
        this.goalSelector.add(1, new AnimationDieAI<>(this));
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, LivingEntity.class, 0, true, false, e ->
                        (PathAwareEntity.class.isAssignableFrom(e.getClass()) || e instanceof PlayerEntity) && !(e instanceof EntityFoliaath || e instanceof EntityBabyFoliaath || e instanceof CreeperEntity)) {
                    @Override
                    public boolean shouldContinue() {
                        this.findClosestTarget();
                        if (this.targetEntity != EntityFoliaath.this.getTarget()) return false;
                        return super.shouldContinue();
                    }
                }
        );
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.getDataTracker().startTracking(CAN_DESPAWN, true);
        this.getDataTracker().startTracking(ACTIVATE_TARGET, 0);
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return MMSounds.ENTITY_FOLIAATH_HURT;
    }

    @Override
    public SoundEvent getDeathSound() {
        return MMSounds.ENTITY_FOLIAATH_DIE;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        this.activate.updatePrevTimer();
        this.deathFlail.updatePrevTimer();
        this.stopDance.updatePrevTimer();
        this.openMouth.updatePrevTimer();
        this.setVelocity(0, this.getVelocity().y, 0);
        // Open mouth animation
        if (this.getAnimation() == NO_ANIMATION && !this.activate.canIncreaseTimer()) {
            this.openMouth.update();
        } else {
            this.openMouth.stop();
        }

        if (this.activate.getAnimationFraction() >= 0.8F) {
            if (!this.active) {
                this.active = true;
            }
        } else if (this.activate.getAnimationFraction() < 0.8F) {
            if (this.active) {
                this.active = false;
            }
        }

        // Sounds
        if (this.frame % 13 == 3 && this.getAnimation() != DIE_ANIMATION) {
            if (this.openMouth.getTimeRunning() >= 10) {
                this.playSound(MMSounds.ENTITY_FOLIAATH_PANT_1, 1, 1);
            } else if (this.activate.getTimer() >= 25) {
                this.playSound(MMSounds.ENTITY_FOLIAATH_PANT_2, 1, 1);
            }
        }

        int openMouthTime = this.openMouth.getTimeRunning();
        if (this.prevOpenMouth - openMouthTime < 0) {
            if (openMouthTime == 1) {
                this.playSound(MMSounds.ENTITY_FOLIAATH_RUSTLE, 1, 1);
            } else if (openMouthTime == 13) {
                this.playSound(MMSounds.ENTITY_FOLIAATH_GRUNT, 1, 1);
            }
        }

        this.prevOpenMouth = openMouthTime;

        int activateTime = this.activate.getTimer();
        if (!this.getWorld().isClient) {
            SoundEvent sound = null;
            if (this.prevActivate - activateTime < 0) {
                sound = switch (activateTime) {
                    case 1 -> MMSounds.ENTITY_FOLIAATH_RUSTLE;
                    case 5 -> MMSounds.ENTITY_FOLIAATH_MERGE;
                    default -> sound;
                };
            } else if (this.prevActivate - activateTime > 0) {
                sound = switch (activateTime) {
                    case 24 -> MMSounds.ENTITY_FOLIAATH_RETREAT;
                    case 28 -> MMSounds.ENTITY_FOLIAATH_RUSTLE;
                    default -> sound;
                };
            }
            if (sound != null) {
                this.playSound(sound, 1, 1);
            }
        }

        this.prevActivate = activateTime;

        // Targetting, attacking, and activating
        this.bodyYaw = 0;
        this.setYaw(0);

        if (this.resettingTargetTimer > 0 && !this.getWorld().isClient) {
            this.headYaw = this.prevHeadYaw;
        }

        if (this.getTarget() != null) {
            this.headYaw = this.targetAngle;

            if (this.targetDistance <= 4 && this.getTarget().getY() - this.getY() >= -1 && this.getTarget().getY() - this.getY() <= 2 && this.getAnimation() == NO_ANIMATION && this.active) {
                AnimationHandler.INSTANCE.sendAnimationMessage(this, ATTACK_ANIMATION);
            }

            if (this.targetDistance <= 10.5 && this.getTarget().getY() - this.getY() >= -1.5 && this.getTarget().getY() - this.getY() <= 2) {
                this.setActivateTarget(ACTIVATE_DURATION);
                this.lastTimeDecrease = 0;
            } else if (this.lastTimeDecrease <= 30 && this.getAnimation() == NO_ANIMATION) {
                this.setActivateTarget(0);
                this.lastTimeDecrease++;
            }
        } else if (!this.getWorld().isClient && this.lastTimeDecrease <= 30 && this.getAnimation() == NO_ANIMATION && this.resettingTargetTimer == 0) {
            this.setActivateTarget(0);
            this.lastTimeDecrease++;
        }

        if (this.getAnimation() == DIE_ANIMATION) {
            if (this.getAnimationTick() <= 12) {
                this.deathFlail.increaseTimer();
            } else {
                this.deathFlail.decreaseTimer();
            }
            this.stopDance.increaseTimer();
            this.setActivateTarget(ACTIVATE_DURATION);
        }

        if (this.resettingTargetTimer > 0) {
            this.resettingTargetTimer--;
        }

        if (this.activateTarget == activateTime) {
            this.activateTarget = this.getActivateTarget();
        } else if (activateTime < this.activateTarget && this.activate.canIncreaseTimer() || activateTime > this.activateTarget && this.activate.canDecreaseTimer()) {
            this.activate.increaseTimer(activateTime < this.activateTarget ? 1 : -2);
        }

        if (!this.getWorld().isClient && this.getWorld().getDifficulty() == Difficulty.PEACEFUL) {
            this.discard();
        }
    }

    @Override
    public boolean damage(DamageSource damageSource, float amount) {
        this.openMouth.resetTimeRunning();
        return (damageSource.isIn(DamageTypeTags.BYPASSES_INVULNERABILITY) || this.active) && super.damage(damageSource, amount);
    }

    @Override
    public Animation getDeathAnimation() {
        return DIE_ANIMATION;
    }

    @Override
    public Animation getHurtAnimation() {
        return HURT_ANIMATION;
    }

    @Override
    public boolean canHit() {
        return true;
    }

    @Override
    protected ConfigHandler.SpawnConfig getSpawnConfig() {
        return ConfigHandler.COMMON.MOBS.FOLIAATH.spawnConfig;
    }

    @Override
    protected ConfigHandler.CombatConfig getCombatConfig() {
        return ConfigHandler.COMMON.MOBS.FOLIAATH.combatConfig;
    }

    private boolean isInTree(WorldAccess world) {
        int x = MathHelper.floor(this.getX());
        int y = MathHelper.floor(this.getBoundingBox().minY);
        int z = MathHelper.floor(this.getZ());
        BlockPos pos = new BlockPos(x, y, z);
        BlockState floor = world.getBlockState(pos.down());
        if (floor.isIn(BlockTags.LEAVES)) {
            for (int i = 2; i < 4; i++) {
                BlockState toCheck = world.getBlockState(pos.down(i));
                if (!(toCheck.isIn(BlockTags.LEAVES) || toCheck.isIn(BlockTags.LOGS) || toCheck.isAir())) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean canSpawn(WorldAccess world, SpawnReason reason) {
        return !this.isInTree(world) && super.canSpawn(world, reason) && this.getEntitiesNearby(AnimalEntity.class, 5, 5, 5, 5).isEmpty() && world.getDifficulty() != Difficulty.PEACEFUL;
    }

    @Override
    public boolean onKilledOther(ServerWorld world, LivingEntity killedEntity) {
        this.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 300, 1, true, true));
        return super.onKilledOther(world, killedEntity);
    }

    @Override
    public boolean cannotDespawn() {
        return !this.getDataTracker().get(CAN_DESPAWN);
    }

    public void setCanDespawn(boolean canDespawn) {
        this.getDataTracker().set(CAN_DESPAWN, canDespawn);
    }

    public int getActivateTarget() {
        return this.getDataTracker().get(ACTIVATE_TARGET);
    }

    public void setActivateTarget(int activateTarget) {
        this.getDataTracker().set(ACTIVATE_TARGET, activateTarget);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound compound) {
        super.writeCustomDataToNbt(compound);
        compound.putBoolean("canDespawn", this.getDataTracker().get(CAN_DESPAWN));
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound compound) {
        super.readCustomDataFromNbt(compound);
        this.setCanDespawn(compound.getBoolean("canDespawn"));
    }

    @Override
    protected void playStepSound(BlockPos p_180429_1_, BlockState p_180429_2_) {

    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[]{DIE_ANIMATION, HURT_ANIMATION, ATTACK_ANIMATION};
    }

    @Override
    protected Identifier getLootTableId() {
        return LootTableHandler.FOLIAATH;
    }
}
