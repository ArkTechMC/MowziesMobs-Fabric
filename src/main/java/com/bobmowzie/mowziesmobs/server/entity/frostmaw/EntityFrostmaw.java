package com.bobmowzie.mowziesmobs.server.entity.frostmaw;

import com.bobmowzie.mowziesmobs.client.particle.ParticleCloud;
import com.bobmowzie.mowziesmobs.client.particle.ParticleHandler;
import com.bobmowzie.mowziesmobs.client.particle.ParticleRing;
import com.bobmowzie.mowziesmobs.client.particle.ParticleSnowFlake;
import com.bobmowzie.mowziesmobs.server.advancement.AdvancementHandler;
import com.bobmowzie.mowziesmobs.server.ai.MMEntityMoveHelper;
import com.bobmowzie.mowziesmobs.server.ai.MMPathNavigateGround;
import com.bobmowzie.mowziesmobs.server.ai.animation.*;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.LegSolverQuadruped;
import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import com.bobmowzie.mowziesmobs.server.entity.MowzieLLibraryEntity;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntityCameraShake;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntityIceBall;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntityIceBreath;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import com.bobmowzie.mowziesmobs.server.loot.LootTableHandler;
import com.bobmowzie.mowziesmobs.server.potion.EffectHandler;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import com.ilexiconn.llibrary.server.animation.Animation;
import com.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.*;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.List;

/**
 * Created by BobMowzie on 5/8/2017.
 */
public class EntityFrostmaw extends MowzieLLibraryEntity implements Monster {
    public static final Animation DIE_ANIMATION = Animation.create(94);
    public static final Animation HURT_ANIMATION = Animation.create(0);
    public static final Animation ROAR_ANIMATION = Animation.create(76);
    public static final Animation SWIPE_ANIMATION = Animation.create(28);
    public static final Animation SWIPE_TWICE_ANIMATION = Animation.create(57);
    public static final Animation ICE_BREATH_ANIMATION = Animation.create(92);
    public static final Animation ICE_BALL_ANIMATION = Animation.create(50);
    public static final Animation ACTIVATE_ANIMATION = Animation.create(118);
    public static final Animation ACTIVATE_NO_CRYSTAL_ANIMATION = Animation.create(100);
    public static final Animation DEACTIVATE_ANIMATION = Animation.create(25);
    public static final Animation DODGE_ANIMATION = Animation.create(15);
    public static final Animation LAND_ANIMATION = Animation.create(14);
    public static final Animation SLAM_ANIMATION = Animation.create(113);
    public static final int ICE_BREATH_COOLDOWN = 260;
    public static final int ICE_BALL_COOLDOWN = 200;
    public static final int SLAM_COOLDOWN = 500;
    public static final int DODGE_COOLDOWN = 200;
    private static final TrackedData<Boolean> ACTIVE = DataTracker.registerData(EntityFrostmaw.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> HAS_CRYSTAL = DataTracker.registerData(EntityFrostmaw.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> ALWAYS_ACTIVE = DataTracker.registerData(EntityFrostmaw.class, TrackedDataHandlerRegistry.BOOLEAN);
    public EntityIceBreath iceBreath;

    public boolean swingWhichArm = false;
    public LegSolverQuadruped legSolver;
    private Vec3d prevRightHandPos = new Vec3d(0, 0, 0);
    private Vec3d prevLeftHandPos = new Vec3d(0, 0, 0);
    private int iceBreathCooldown = 0;
    private int iceBallCooldown = 0;
    private int slamCooldown = 0;
    private int timeWithoutTarget;
    private int shouldDodgeMeasure = 0;
    private int dodgeCooldown = 0;
    private boolean shouldDodge;
    private float dodgeYaw = 0;
    private boolean wantsToIceBreathAfterDodging = false;
    private Vec3d prevTargetPos = new Vec3d(0, 0, 0);
    private boolean shouldPlayLandAnimation = false;

    public EntityFrostmaw(EntityType<? extends EntityFrostmaw> type, World world) {
        super(type, world);
        this.setStepHeight(1);
        this.frame += this.random.nextInt(50);
        this.legSolver = new LegSolverQuadruped(1f, 2f, -1, 1.5f);
        if (world.isClient)
            this.socketPosArray = new Vec3d[]{new Vec3d(0, 0, 0), new Vec3d(0, 0, 0), new Vec3d(0, 0, 0)};
        this.active = false;
        this.playsHurtAnimation = false;
        this.setYaw(this.bodyYaw = this.random.nextFloat() * 360);
        this.experiencePoints = 60;

        this.moveControl = new MMEntityMoveHelper(this, 7);
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MowzieEntity.createAttributes().add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 10)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 250)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 1)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 50)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3D);
    }

    private static boolean isInventoryFull(PlayerInventory inventory) {
        for (ItemStack itemstack : inventory.main) {
            if (itemstack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(5, new GoToWalkTargetGoal(this, 1.0D));
        this.goalSelector.add(7, new WanderAroundFarGoal(this, 1.0D));
        this.goalSelector.add(8, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(8, new LookAroundGoal(this));
        this.goalSelector.add(2, new AnimationAreaAttackAI<EntityFrostmaw>(this, SWIPE_ANIMATION, null, null, 2, 6.5f, 6, 135, 1, 9) {
            @Override
            public void start() {
                super.start();
            }
        });
        this.goalSelector.add(2, new AnimationAreaAttackAI<EntityFrostmaw>(this, SWIPE_TWICE_ANIMATION, null, null, 1, 6.5f, 6, 135, 1, 9) {
            @Override
            public void start() {
                super.start();
            }

            @Override
            public void tick() {
                super.tick();
                if (EntityFrostmaw.this.getAnimationTick() == 21) {
                    this.hitEntities();
                }
                if (EntityFrostmaw.this.getAnimationTick() == 16) {
                    EntityFrostmaw.this.playSound(MMSounds.ENTITY_FROSTMAW_WHOOSH, 2, 0.7f);
                }
                if (EntityFrostmaw.this.getAnimationTick() == 6) {
                    EntityFrostmaw.this.playSound(MMSounds.ENTITY_FROSTMAW_WHOOSH, 2, 0.8f);
                }
                if (EntityFrostmaw.this.getTarget() != null) EntityFrostmaw.this.lookControl.lookAt(EntityFrostmaw.this.getTarget(), 30, 30);
            }

            @Override
            protected void onAttack(LivingEntity entityTarget, float damageMultiplier, float applyKnockbackMultiplier) {
                super.onAttack(entityTarget, damageMultiplier, applyKnockbackMultiplier);
                if (EntityFrostmaw.this.getAnimationTick() == 21 && entityTarget instanceof PlayerEntity player) {
                    if (player.isBlocking()) player.disableShield(true);
                }
            }
        });
        this.goalSelector.add(2, new SimpleAnimationAI<>(this, ICE_BREATH_ANIMATION, true));
        this.goalSelector.add(2, new SimpleAnimationAI<>(this, ICE_BALL_ANIMATION, true) {
            @Override
            public void start() {
                super.start();
                EntityFrostmaw.this.playSound(MMSounds.ENTITY_FROSTMAW_ICEBALL_CHARGE, 2, 0.9f);
            }
        });
        this.goalSelector.add(2, new SimpleAnimationAI<>(this, ROAR_ANIMATION, false));
        this.goalSelector.add(2, new AnimationActivateAI<>(this, ACTIVATE_ANIMATION) {
            @Override
            public void start() {
                super.start();
                EntityFrostmaw.this.playSound(MMSounds.ENTITY_FROSTMAW_WAKEUP, 1, 1);
            }
        });
        this.goalSelector.add(2, new AnimationActivateAI<>(this, ACTIVATE_NO_CRYSTAL_ANIMATION) {
            @Override
            public void start() {
                super.start();
                EntityFrostmaw.this.playSound(MMSounds.ENTITY_FROSTMAW_WAKEUP, 1, 1);
            }
        });
        this.goalSelector.add(2, new AnimationDeactivateAI<>(this, DEACTIVATE_ANIMATION));
        this.goalSelector.add(2, new SimpleAnimationAI<>(this, LAND_ANIMATION, false));
        this.goalSelector.add(2, new SimpleAnimationAI<>(this, SLAM_ANIMATION, EnumSet.of(Goal.Control.LOOK)));
        this.goalSelector.add(2, new SimpleAnimationAI<>(this, DODGE_ANIMATION, EnumSet.of(Goal.Control.MOVE, Goal.Control.JUMP)));
        this.goalSelector.add(3, new AnimationTakeDamage<>(this));
        this.goalSelector.add(1, new AnimationDieAI<>(this));
        this.targetSelector.add(1, new ActiveTargetGoal<>(this, PlayerEntity.class, 0, true, false, null));
    }

    @Override
    protected EntityNavigation createNavigation(World world) {
        return new MMPathNavigateGround(this, world);
    }

    @Override
    protected float getBaseMovementSpeedMultiplier() {
        return 0.98F;
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.getDataTracker().startTracking(ACTIVE, false);
        this.getDataTracker().startTracking(HAS_CRYSTAL, true);
        this.getDataTracker().startTracking(ALWAYS_ACTIVE, false);
    }

    @Override
    public void playAmbientSound() {
        if (!this.active) return;
        int i = this.random.nextInt(4);
        super.playAmbientSound();
        if (i == 0 && this.getAnimation() == NO_ANIMATION) {
            AnimationHandler.INSTANCE.sendAnimationMessage(this, ROAR_ANIMATION);
            return;
        }
        if (i < MMSounds.ENTITY_FROSTMAW_LIVING.size())
            this.playSound(MMSounds.ENTITY_FROSTMAW_LIVING.get(i), 2, 0.8f + this.random.nextFloat() * 0.3f);
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return super.getAmbientSound();
    }

    @Override
    public boolean canBePushedByEntity(Entity entity) {
        return false;
    }

    @Override
    public boolean isPushedByFluids() {
        return false;
    }

    @Override
    protected void repelEntities(float x, float y, float z, float radius) {
        List<PlayerEntity> nearbyEntities = this.getPlayersNearby(x, y, z, radius);
        for (Entity entity : nearbyEntities) {
            double angle = (this.getAngleBetweenEntities(this, entity) + 90) * Math.PI / 180;
            entity.setVelocity(-0.1 * Math.cos(angle), entity.getVelocity().y, -0.1 * Math.sin(angle));
        }
    }

    @Override
    public void tick() {
//        if (ticksExisted == 1)
//            System.out.println("Spawned " + getName().getFormattedText() + " at " + getPosition());
        this.setYaw(this.bodyYaw);
        super.tick();
        this.repelEntities(3.8f, 3.8f, 3.8f, 3.8f);

        if (this.getTarget() != null && (!this.getTarget().isAlive() || this.getTarget().getHealth() <= 0)) this.setTarget(null);

        if (this.isAlwaysActive()) {
            this.setActive(true);
        }

        if (this.getActive() && this.getAnimation() != ACTIVATE_ANIMATION && this.getAnimation() != ACTIVATE_NO_CRYSTAL_ANIMATION) {
            this.legSolver.update(this);

            if (this.getAnimation() == SWIPE_ANIMATION || this.getAnimation() == SWIPE_TWICE_ANIMATION) {
                if (this.getAnimationTick() == 3) {
                    int i = MathHelper.nextInt(this.random, 0, MMSounds.ENTITY_FROSTMAW_ATTACK.size());
                    if (i < MMSounds.ENTITY_FROSTMAW_ATTACK.size()) {
                        this.playSound(MMSounds.ENTITY_FROSTMAW_ATTACK.get(i), 2, 0.9f + this.random.nextFloat() * 0.2f);
                    }
                }
            }

            if (this.getAnimation() == SWIPE_ANIMATION) {
                if (this.getAnimationTick() == 6) {
                    this.playSound(MMSounds.ENTITY_FROSTMAW_WHOOSH, 2, 0.8f);
                }
                if (this.getTarget() != null) this.lookControl.lookAt(this.getTarget(), 30, 30);
            }

            if (this.getAnimation() == ROAR_ANIMATION) {
                if (this.getAnimationTick() == 10) {
                    this.playSound(MMSounds.ENTITY_FROSTMAW_ROAR, 4, 1);
                    EntityCameraShake.cameraShake(this.getWorld(), this.getPos(), 45, 0.03f, 60, 20);
                }
                if (this.getAnimationTick() >= 8 && this.getAnimationTick() < 65) {
                    this.doRoarEffects();
                }
            }

            if (this.getAnimation() == LAND_ANIMATION) {
                if (this.getAnimationTick() == 3) {
                    this.playSound(MMSounds.ENTITY_FROSTMAW_LAND, 3, 0.9f);
                }
            }

            if (this.getAnimation() == SLAM_ANIMATION) {
                if (this.getAnimationTick() == 82) {
                    this.playSound(MMSounds.ENTITY_FROSTMAW_LIVING_1, 2, 1);
                }
                if (this.getTarget() != null) this.lookControl.lookAt(this.getTarget(), 30, 30);
                if (this.getAnimationTick() == 82) {
                    int i = MathHelper.nextInt(this.random, 0, MMSounds.ENTITY_FROSTMAW_ATTACK.size() - 1);
                    if (i < MMSounds.ENTITY_FROSTMAW_ATTACK.size()) {
                        this.playSound(MMSounds.ENTITY_FROSTMAW_ATTACK.get(i), 2, 0.9f + this.random.nextFloat() * 0.2f);
                    }
                    this.playSound(MMSounds.ENTITY_FROSTMAW_WHOOSH, 2, 0.7f);
                }
                if (this.getAnimationTick() == 87) {
                    this.playSound(MMSounds.ENTITY_FROSTMAW_LAND, 3, 1f);
                    float radius = 4;
                    float slamPosX = (float) (this.getX() + radius * Math.cos(Math.toRadians(this.getYaw() + 90)));
                    float slamPosZ = (float) (this.getZ() + radius * Math.sin(Math.toRadians(this.getYaw() + 90)));
                    if (this.getWorld().isClient)
                        this.getWorld().addParticle(new ParticleRing.RingData(0f, (float) Math.PI / 2f, 17, 1f, 1f, 1f, 1f, 60f, false, ParticleRing.EnumRingBehavior.GROW), slamPosX, this.getY() + 0.2f, slamPosZ, 0, 0, 0);
                    Box hitBox = new Box(slamPosX - 0.5f, this.getY() - 0.5f, slamPosZ - 0.5f, slamPosX + 0.5f, this.getY() + 0.5f, slamPosZ + 0.5f).expand(3, 3, 3);
                    List<LivingEntity> entitiesHit = this.getWorld().getNonSpectatingEntities(LivingEntity.class, hitBox);
                    for (LivingEntity entity : entitiesHit) {
                        if (entity != this && entity.getPos().squaredDistanceTo(slamPosX, this.getY(), slamPosZ) <= 9) {
                            this.doHurtTarget(entity, 4f, 1);
                            if (entity.isBlocking())
                                entity.getActiveItem().damage(400, entity, p -> p.sendToolBreakStatus(entity.getActiveHand()));
                        }
                    }
                    EntityCameraShake.cameraShake(this.getWorld(), new Vec3d(slamPosX, this.getY(), slamPosZ), 30, 0.1f, 0, 20);
                }
            }
            if (this.getAnimation() == DODGE_ANIMATION && !this.getWorld().isClient) {
                this.getNavigation().stop();
                if (this.getAnimationTick() == 2) {
                    this.dodgeYaw = (float) Math.toRadians(this.targetAngle + 90 + this.random.nextFloat() * 150 - 75);
                }
                if (this.getAnimationTick() == 6 && (this.isOnGround() || this.isInLava() || this.isTouchingWater())) {
                    float speed = 1.7f;
                    Vec3d m = this.getVelocity().add(speed * Math.cos(this.dodgeYaw), 0, speed * Math.sin(this.dodgeYaw));
                    this.setVelocity(m.x, 0.6, m.z);
                }
                if (this.getTarget() != null) this.lookControl.lookAt(this.getTarget(), 30, 30);
            }

            if (this.getAnimation() == ICE_BREATH_ANIMATION) {
                if (this.getTarget() != null) {
                    this.lookControl.lookAt(this.getTarget(), 30, 30);
                    this.lookAtEntity(this.getTarget(), 30, 30);
                    this.headYaw = this.bodyYaw = this.getYaw();
                }
                Vec3d mouthPos = new Vec3d(2.3, 2.65, 0);
                mouthPos = mouthPos.rotateY((float) Math.toRadians(-this.getYaw() - 90));
                mouthPos = mouthPos.add(this.getPos());
                mouthPos = mouthPos.add(new Vec3d(0, 0, 1).rotateX((float) Math.toRadians(-this.getPitch())).rotateY((float) Math.toRadians(-this.headYaw)));
                if (this.getAnimationTick() == 13) {
                    this.iceBreath = new EntityIceBreath(EntityHandler.ICE_BREATH, this.getWorld(), this);
                    this.iceBreath.updatePositionAndAngles(mouthPos.x, mouthPos.y, mouthPos.z, this.headYaw, this.getPitch() + 10);
                    if (!this.getWorld().isClient) this.getWorld().spawnEntity(this.iceBreath);
                }
                if (this.iceBreath != null)
                    this.iceBreath.updatePositionAndAngles(mouthPos.x, mouthPos.y, mouthPos.z, this.headYaw, this.getPitch() + 10);
            }

            if (this.getAnimation() == ICE_BALL_ANIMATION) {
                if (this.getTarget() != null) this.lookControl.lookAt(this.getTarget(), 15, 15);
                Vec3d projectilePos = new Vec3d(2.0, 1.9, 0);
                projectilePos = projectilePos.rotateY((float) Math.toRadians(-this.getYaw() - 90));
                projectilePos = projectilePos.add(this.getPos());
                projectilePos = projectilePos.add(new Vec3d(0, 0, 1).rotateX((float) Math.toRadians(-this.getPitch())).rotateY((float) Math.toRadians(-this.headYaw)));
                if (this.getWorld().isClient) {
                    Vec3d mouthPos = this.socketPosArray[2];
                    if (this.getAnimationTick() < 12) {
                        for (int i = 0; i < 6; i++) {
                            Vec3d particlePos = new Vec3d(3.5, 0, 0);
                            particlePos = particlePos.rotateY((float) (this.random.nextFloat() * 2 * Math.PI));
                            particlePos = particlePos.rotateX((float) (this.random.nextFloat() * 2 * Math.PI));
                            float value = this.random.nextFloat() * 0.15f;
                            this.getWorld().addParticle(new ParticleCloud.CloudData(ParticleHandler.CLOUD, 0.75f + value, 0.75f + value, 1f, 5f + this.random.nextFloat() * 15f, 30, ParticleCloud.EnumCloudBehavior.CONSTANT, 1f), mouthPos.x + particlePos.x, mouthPos.y + particlePos.y, mouthPos.z + particlePos.z, -0.1 * particlePos.x, -0.1 * particlePos.y, -0.1 * particlePos.z);
                        }
                        for (int i = 0; i < 8; i++) {
                            Vec3d particlePos = new Vec3d(3.5, 0, 0);
                            particlePos = particlePos.rotateY((float) (this.random.nextFloat() * 2 * Math.PI));
                            particlePos = particlePos.rotateX((float) (this.random.nextFloat() * 2 * Math.PI));
                            this.getWorld().addParticle(new ParticleSnowFlake.SnowflakeData(40, false), mouthPos.x + particlePos.x, mouthPos.y + particlePos.y, mouthPos.z + particlePos.z, -0.07 * particlePos.x, -0.07 * particlePos.y, -0.07 * particlePos.z);
                        }
                    }
                }

                if (this.getAnimationTick() == 32) {
                    if (this.getTarget() != null)
                        this.prevTargetPos = this.getTarget().getPos().add(new Vec3d(0f, this.getTarget().getHeight() / 2.0, 0f));
                }
                if (this.getAnimationTick() == 33) {
                    this.playSound(MMSounds.ENTITY_FROSTMAW_ICEBALL_SHOOT, 2, 0.7f);

                    EntityIceBall iceBall = new EntityIceBall(EntityHandler.ICE_BALL, this.getWorld(), this);
                    iceBall.updatePositionAndAngles(projectilePos.x, projectilePos.y, projectilePos.z, this.headYaw, this.getPitch() + 10);
                    float projSpeed = 1.6f;
                    if (this.getTarget() != null) {
                        float ticksUntilHit = this.targetDistance / projSpeed;
                        Vec3d targetPos = this.getTarget().getPos().add(new Vec3d(0f, this.getTarget().getHeight() / 2.0, 0f));
                        Vec3d targetMovement = targetPos.subtract(this.prevTargetPos).multiply(ticksUntilHit * 0.95);
                        targetMovement = targetMovement.subtract(0, targetMovement.y, 0);
                        Vec3d futureTargetPos = targetPos.add(targetMovement);
                        Vec3d projectileMid = projectilePos.add(new Vec3d(0, iceBall.getHeight() / 2.0, 0));
                        Vec3d shootVec = futureTargetPos.subtract(projectileMid).normalize();
                        iceBall.shoot(shootVec.x, shootVec.y, shootVec.z, projSpeed, 0);
                    } else {
                        iceBall.shoot(this.getRotationVector().x, this.getRotationVector().y, this.getRotationVector().z, projSpeed, 0);
                    }
                    if (!this.getWorld().isClient) this.getWorld().spawnEntity(iceBall);
                }
            }

            this.spawnSwipeParticles();

            if (this.fallDistance > 0.2 && !this.isOnGround() && this.getRecentDamageSource() != null && !this.getRecentDamageSource().isOf(DamageTypes.LAVA))
                this.shouldPlayLandAnimation = true;
            if (this.isOnGround() && this.shouldPlayLandAnimation && this.getAnimation() != DODGE_ANIMATION) {
                if (!this.getWorld().isClient && this.getAnimation() == NO_ANIMATION) {
                    AnimationHandler.INSTANCE.sendAnimationMessage(this, LAND_ANIMATION);
                }
                this.shouldPlayLandAnimation = false;
            }

            if (this.getTarget() != null) {
                this.timeWithoutTarget = 0;

                float entityHitAngle = (float) ((Math.atan2(this.getTarget().getZ() - this.getZ(), this.getTarget().getX() - this.getX()) * (180 / Math.PI) - 90) % 360);
                float entityAttackingAngle = this.bodyYaw % 360;
                if (entityHitAngle < 0) {
                    entityHitAngle += 360;
                }
                if (entityAttackingAngle < 0) {
                    entityAttackingAngle += 360;
                }
                float entityRelativeAngle = entityHitAngle - entityAttackingAngle;
                if (this.getNavigation().isIdle() && !((entityRelativeAngle <= 30 / 2f && entityRelativeAngle >= -30 / 2f) || (entityRelativeAngle >= 360 - 30 / 2f || entityRelativeAngle <= -360 + 30 / 2f))) {
                    this.getNavigation().startMovingTo(this.getTarget(), 0.85);
                }

                if (this.shouldDodgeMeasure >= 16) this.shouldDodge = true;
                if (this.getTarget().hasStatusEffect(EffectHandler.FROZEN)) this.shouldDodge = false;
                if (this.targetDistance < 4 && this.shouldDodge && this.getAnimation() == NO_ANIMATION) {
                    this.shouldDodge = false;
                    this.dodgeCooldown = DODGE_COOLDOWN;
                    this.shouldDodgeMeasure = 0;
                    if (this.getHasCrystal()) {
                        this.wantsToIceBreathAfterDodging = true;
                    }
                    this.iceBreathCooldown = 0;
                    AnimationHandler.INSTANCE.sendAnimationMessage(this, DODGE_ANIMATION);
                }

                if (this.targetDistance > 5.5 && !(this.getAnimation() == ICE_BREATH_ANIMATION && this.targetDistance < 7.5)) {
                    if (this.getAnimation() != SLAM_ANIMATION) this.getNavigation().startMovingTo(this.getTarget(), 1);
                    else this.getNavigation().startMovingTo(this.getTarget(), 0.95);
                } else this.getNavigation().stop();
                if (this.targetDistance <= 8.5 && this.getAnimation() == NO_ANIMATION && this.slamCooldown <= 0 && this.random.nextInt(4) == 0 && this.getHealthRatio() < 0.6) {
                    AnimationHandler.INSTANCE.sendAnimationMessage(this, SLAM_ANIMATION);
                    this.slamCooldown = SLAM_COOLDOWN;
                }
                if (this.targetDistance <= 6.5 && this.getAnimation() == NO_ANIMATION && !this.wantsToIceBreathAfterDodging) {
                    if (this.random.nextInt(4) == 0)
                        AnimationHandler.INSTANCE.sendAnimationMessage(this, SWIPE_TWICE_ANIMATION);
                    else AnimationHandler.INSTANCE.sendAnimationMessage(this, SWIPE_ANIMATION);
                }
                if (this.targetDistance <= 13.5 && this.getAnimation() == NO_ANIMATION && this.iceBreathCooldown <= 0 && this.getHasCrystal() && (this.isOnGround() || this.touchingWater)) {
                    AnimationHandler.INSTANCE.sendAnimationMessage(this, ICE_BREATH_ANIMATION);
                    this.iceBreathCooldown = ICE_BREATH_COOLDOWN;
                    this.wantsToIceBreathAfterDodging = false;
                }
                if (this.targetDistance >= 14.5 && this.getAnimation() == NO_ANIMATION && this.iceBallCooldown <= 0 && this.getHasCrystal() && (this.isOnGround() || this.touchingWater)) {
                    AnimationHandler.INSTANCE.sendAnimationMessage(this, ICE_BALL_ANIMATION);
                    this.iceBallCooldown = ICE_BALL_COOLDOWN;
                }
            } else if (!this.getWorld().isClient) {
                if (!this.isAlwaysActive()) {
                    this.timeWithoutTarget++;
                    if (this.timeWithoutTarget > 1200 || this.getWorld().getDifficulty() == Difficulty.PEACEFUL) {
                        this.timeWithoutTarget = 0;
                        if (this.getAnimation() == NO_ANIMATION) {
                            AnimationHandler.INSTANCE.sendAnimationMessage(this, DEACTIVATE_ANIMATION);
                            this.setActive(false);
                        }
                    }
                }
            }
        } else {
            this.getNavigation().stop();
            this.setVelocity(0, this.getVelocity().y, 0);
            this.bodyYaw = this.prevBodyYaw;
            if (!this.getWorld().isClient && this.getAnimation() != ACTIVATE_ANIMATION) {
                if (ConfigHandler.COMMON.MOBS.FROSTMAW.healsOutOfBattle) this.heal(0.3f);
            }
            if (this.getTarget() != null && this.getTarget().hasStatusEffect(StatusEffects.INVISIBILITY)) {
                this.setTarget(null);
            }
            if (!this.getAttackableEntityLivingBaseNearby(8, 8, 8, 8).isEmpty() && this.getTarget() != null && this.getAnimation() == NO_ANIMATION) {
                if (this.getWorld().getDifficulty() != Difficulty.PEACEFUL) {
                    if (this.getHasCrystal()) AnimationHandler.INSTANCE.sendAnimationMessage(this, ACTIVATE_ANIMATION);
                    else AnimationHandler.INSTANCE.sendAnimationMessage(this, ACTIVATE_NO_CRYSTAL_ANIMATION);
                    this.setActive(true);
                }
            }

            if (ConfigHandler.COMMON.MOBS.FROSTMAW.stealableIceCrystal && this.getHasCrystal() && this.age > 20 && this.getAnimation() == NO_ANIMATION) {
                Vec3d crystalPos = new Vec3d(1.6, 0.4, 1.8);
                crystalPos = crystalPos.rotateY((float) Math.toRadians(-this.getYaw() - 90));
                crystalPos = crystalPos.add(this.getPos());
                for (PlayerEntity player : this.getPlayersNearby(8, 8, 8, 8)) {
                    if (player.getPos().distanceTo(crystalPos) <= 1.8 && (player.isCreative() || player.isInvisible()) && !isInventoryFull(player.getInventory())) {
                        player.getInventory().offerOrDrop(new ItemStack(ItemHandler.ICE_CRYSTAL));
                        this.setHasCrystal(false);
                        if (this.getWorld().getDifficulty() != Difficulty.PEACEFUL) {
                            AnimationHandler.INSTANCE.sendAnimationMessage(this, ACTIVATE_NO_CRYSTAL_ANIMATION);
                            this.setActive(true);
                        }
                        if (player instanceof ServerPlayerEntity)
                            AdvancementHandler.STEAL_ICE_CRYSTAL_TRIGGER.trigger((ServerPlayerEntity) player);
                        break;
                    }
                }
            }
        }

        if (this.getAnimation() == ACTIVATE_ANIMATION || this.getAnimation() == ACTIVATE_NO_CRYSTAL_ANIMATION) {
            //if (getAnimationTick() == 1) playSound(MMSounds.ENTITY_FROSTMAW_WAKEUP, 1, 1);
            if (this.getAnimation() == ACTIVATE_ANIMATION && this.getAnimationTick() == 18)
                this.playSound(MMSounds.ENTITY_FROSTMAW_ATTACK.get(0), 1.5f, 1);
            if ((this.getAnimation() == ACTIVATE_ANIMATION && this.getAnimationTick() == 52) || (this.getAnimation() == ACTIVATE_NO_CRYSTAL_ANIMATION && this.getAnimationTick() == 34)) {
                this.playSound(MMSounds.ENTITY_FROSTMAW_ROAR, 4, 1);
                EntityCameraShake.cameraShake(this.getWorld(), this.getPos(), 45, 0.03f, 60, 20);
            }
            if ((this.getAnimation() == ACTIVATE_ANIMATION && this.getAnimationTick() >= 51 && this.getAnimationTick() < 108) || (this.getAnimation() == ACTIVATE_NO_CRYSTAL_ANIMATION && this.getAnimationTick() >= 33 && this.getAnimationTick() < 90)) {
                this.doRoarEffects();
            }
        }

        if (this.getWorld().isClient) {
            if ((this.getAnimation() == SWIPE_ANIMATION || this.getAnimation() == SWIPE_TWICE_ANIMATION) && this.getAnimationTick() == 1) {
                this.swingWhichArm = this.random.nextBoolean();
            }
        }

        //Footstep Sounds
        float moveX = (float) (this.getX() - this.prevX);
        float moveZ = (float) (this.getZ() - this.prevZ);
        float speed = MathHelper.sqrt(moveX * moveX + moveZ * moveZ);
        if (this.frame % 16 == 5 && speed > 0.05 && this.active) {
            this.playSound(MMSounds.ENTITY_FROSTMAW_STEP, 3F, 0.8F + this.random.nextFloat() * 0.2f);
            EntityCameraShake.cameraShake(this.getWorld(), this.getPos(), 20, 0.03f, 0, 10);
        }

        //Breathing sounds
        if (this.frame % 118 == 1 && !this.active) {
            int i = MathHelper.nextInt(this.random, 0, 1);
            this.playSound(MMSounds.ENTITY_FROSTMAW_BREATH.get(i), 1.5F, 1.1F + this.random.nextFloat() * 0.1f);
        }

//        if (getAnimation() == NO_ANIMATION && onGround()) {
//            AnimationHandler.INSTANCE.sendAnimationMessage(this, SLAM_ANIMATION);
//            setActive(true);
//        }

        if (this.iceBreathCooldown > 0) this.iceBreathCooldown--;
        if (this.iceBallCooldown > 0) this.iceBallCooldown--;
        if (this.slamCooldown > 0) this.slamCooldown--;
        if (this.shouldDodgeMeasure > 0 && this.age % 7 == 0) this.shouldDodgeMeasure--;
        if (this.dodgeCooldown > 0) this.dodgeCooldown--;
        this.prevYaw = this.getYaw();
    }

    private void doRoarEffects() {
        if (this.getHasCrystal()) {
            List<LivingEntity> entities = this.getEntityLivingBaseNearby(10, 3, 10, 10);
            for (LivingEntity entity : entities) {
                if (entity == this) continue;
                double angle = (this.getAngleBetweenEntities(this, entity) + 90) * Math.PI / 180;
                double distance = this.distanceTo(entity) - 4;
                entity.setVelocity(entity.getVelocity().add(Math.min(1 / (distance * distance), 1) * -1 * Math.cos(angle), 0, Math.min(1 / (distance * distance), 1) * -1 * Math.sin(angle)));
            }
            if (this.getAnimationTick() % 12 == 0 && this.getWorld().isClient) {
                int particleCount = 15;
                for (int i = 1; i <= particleCount; i++) {
                    double yaw = i * 360.f / particleCount;
                    double speed = 0.9;
                    double xSpeed = speed * Math.cos(Math.toRadians(yaw));
                    double zSpeed = speed * Math.sin(Math.toRadians(yaw));
                    this.getWorld().addParticle(new ParticleCloud.CloudData(ParticleHandler.CLOUD, 0.75f, 0.75f, 1f, 40f, 22, ParticleCloud.EnumCloudBehavior.GROW, 1f), this.getX(), this.getY() + 1f, this.getZ(), xSpeed, 0, zSpeed);
                }
                for (int i = 1; i <= particleCount; i++) {
                    double yaw = i * 360.f / particleCount;
                    double speed = 0.65;
                    double xSpeed = speed * Math.cos(Math.toRadians(yaw));
                    double zSpeed = speed * Math.sin(Math.toRadians(yaw));
                    this.getWorld().addParticle(new ParticleCloud.CloudData(ParticleHandler.CLOUD, 0.75f, 0.75f, 1f, 35f, 22, ParticleCloud.EnumCloudBehavior.GROW, 1f), this.getX(), this.getY() + 1f, this.getZ(), xSpeed, 0, zSpeed);
                }
            }
        }
    }

    @Override
    public boolean canSpawn(WorldAccess world, SpawnReason reason) {
        List<LivingEntity> nearby = this.getEntityLivingBaseNearby(100, 100, 100, 100);
        for (LivingEntity nearbyEntity : nearby) {
            if (nearbyEntity instanceof EntityFrostmaw || nearbyEntity instanceof VillagerEntity) {
                return false;
            }
        }
        return super.canSpawn(world, reason);
    }

    public int getLimitPerChunk() {
        return 1;
    }

    @Nullable
    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason reason, @Nullable EntityData livingData, @Nullable NbtCompound compound) {
        this.setHasCrystal(true);
        return super.initialize(world, difficulty, reason, livingData, compound);
    }

    private void spawnSwipeParticles() {
        if (this.getWorld().isClient && this.getHasCrystal()) {
            double motionX = this.getVelocity().getX();
            double motionY = this.getVelocity().getY();
            double motionZ = this.getVelocity().getZ();

            int snowflakeDensity = 4;
            float snowflakeRandomness = 0.5f;
            int cloudDensity = 2;
            float cloudRandomness = 0.5f;
            if (this.getAnimation() == SWIPE_ANIMATION || this.getAnimation() == SWIPE_TWICE_ANIMATION) {
                Vec3d rightHandPos = this.socketPosArray[0];
                Vec3d leftHandPos = this.socketPosArray[1];
                if (this.getAnimation() == SWIPE_ANIMATION) {
                    if (this.getAnimationTick() > 8 && this.getAnimationTick() < 14) {
                        if (this.swingWhichArm) {
                            double length = this.prevRightHandPos.subtract(rightHandPos).length();
                            int numClouds = (int) Math.floor(2 * length);
                            for (int i = 0; i < numClouds; i++) {
                                double x = this.prevRightHandPos.x + i * (rightHandPos.x - this.prevRightHandPos.x) / numClouds;
                                double y = this.prevRightHandPos.y + i * (rightHandPos.y - this.prevRightHandPos.y) / numClouds;
                                double z = this.prevRightHandPos.z + i * (rightHandPos.z - this.prevRightHandPos.z) / numClouds;
                                for (int j = 0; j < snowflakeDensity; j++) {
                                    float xOffset = snowflakeRandomness * (2 * this.random.nextFloat() - 1);
                                    float yOffset = snowflakeRandomness * (2 * this.random.nextFloat() - 1);
                                    float zOffset = snowflakeRandomness * (2 * this.random.nextFloat() - 1);
                                    this.getWorld().addParticle(new ParticleSnowFlake.SnowflakeData(40, false), x + xOffset, y + yOffset, z + zOffset, motionX, motionY - 0.01f, motionZ);
                                }
                                for (int j = 0; j < cloudDensity; j++) {
                                    float xOffset = cloudRandomness * (2 * this.random.nextFloat() - 1);
                                    float yOffset = cloudRandomness * (2 * this.random.nextFloat() - 1);
                                    float zOffset = cloudRandomness * (2 * this.random.nextFloat() - 1);
                                    float value = this.random.nextFloat() * 0.1f;
                                    this.getWorld().addParticle(new ParticleCloud.CloudData(ParticleHandler.CLOUD, 0.8f + value, 0.8f + value, 1f, (float) (10d + this.random.nextDouble() * 10d), 40, ParticleCloud.EnumCloudBehavior.SHRINK, 1f), x + xOffset, y + yOffset, z + zOffset, motionX, motionY, motionZ);
                                }
                            }
                        } else {
                            double length = this.prevLeftHandPos.subtract(leftHandPos).length();
                            int numClouds = (int) Math.floor(2.5 * length);
                            for (int i = 0; i < numClouds; i++) {
                                double x = this.prevLeftHandPos.x + i * (leftHandPos.x - this.prevLeftHandPos.x) / numClouds;
                                double y = this.prevLeftHandPos.y + i * (leftHandPos.y - this.prevLeftHandPos.y) / numClouds;
                                double z = this.prevLeftHandPos.z + i * (leftHandPos.z - this.prevLeftHandPos.z) / numClouds;
                                for (int j = 0; j < snowflakeDensity; j++) {
                                    float xOffset = snowflakeRandomness * (2 * this.random.nextFloat() - 1);
                                    float yOffset = snowflakeRandomness * (2 * this.random.nextFloat() - 1);
                                    float zOffset = snowflakeRandomness * (2 * this.random.nextFloat() - 1);
                                    this.getWorld().addParticle(new ParticleSnowFlake.SnowflakeData(40, false), x + xOffset, y + yOffset, z + zOffset, motionX, motionY - 0.01f, motionZ);
                                }
                                for (int j = 0; j < cloudDensity; j++) {
                                    float xOffset = cloudRandomness * (2 * this.random.nextFloat() - 1);
                                    float yOffset = cloudRandomness * (2 * this.random.nextFloat() - 1);
                                    float zOffset = cloudRandomness * (2 * this.random.nextFloat() - 1);
                                    float value = this.random.nextFloat() * 0.1f;
                                    this.getWorld().addParticle(new ParticleCloud.CloudData(ParticleHandler.CLOUD, 0.8f + value, 0.8f + value, 1f, (float) (10d + this.random.nextDouble() * 10d), 40, ParticleCloud.EnumCloudBehavior.SHRINK, 1f), x + xOffset, y + yOffset, z + zOffset, motionX, motionY, motionZ);
                                }
                            }
                        }
                    }
                } else {
                    if ((this.swingWhichArm && this.getAnimationTick() > 8 && this.getAnimationTick() < 14) || (!this.swingWhichArm && this.getAnimationTick() > 19 && this.getAnimationTick() < 25)) {
                        double length = this.prevRightHandPos.subtract(rightHandPos).length();
                        int numClouds = (int) Math.floor(2 * length);
                        for (int i = 0; i < numClouds; i++) {
                            double x = this.prevRightHandPos.x + i * (rightHandPos.x - this.prevRightHandPos.x) / numClouds;
                            double y = this.prevRightHandPos.y + i * (rightHandPos.y - this.prevRightHandPos.y) / numClouds;
                            double z = this.prevRightHandPos.z + i * (rightHandPos.z - this.prevRightHandPos.z) / numClouds;
                            for (int j = 0; j < snowflakeDensity; j++) {
                                float xOffset = snowflakeRandomness * (2 * this.random.nextFloat() - 1);
                                float yOffset = snowflakeRandomness * (2 * this.random.nextFloat() - 1);
                                float zOffset = snowflakeRandomness * (2 * this.random.nextFloat() - 1);
                                this.getWorld().addParticle(new ParticleSnowFlake.SnowflakeData(40, false), x + xOffset, y + yOffset, z + zOffset, motionX, motionY - 0.01f, motionZ);
                            }
                            for (int j = 0; j < cloudDensity; j++) {
                                float xOffset = cloudRandomness * (2 * this.random.nextFloat() - 1);
                                float yOffset = cloudRandomness * (2 * this.random.nextFloat() - 1);
                                float zOffset = cloudRandomness * (2 * this.random.nextFloat() - 1);
                                float value = this.random.nextFloat() * 0.1f;
                                this.getWorld().addParticle(new ParticleCloud.CloudData(ParticleHandler.CLOUD, 0.8f + value, 0.8f + value, 1f, (float) (10d + this.random.nextDouble() * 10d), 40, ParticleCloud.EnumCloudBehavior.SHRINK, 1f), x + xOffset, y + yOffset, z + zOffset, motionX, motionY, motionZ);
                            }
                        }
                    } else if ((!this.swingWhichArm && this.getAnimationTick() > 8 && this.getAnimationTick() < 14) || (this.swingWhichArm && this.getAnimationTick() > 19 && this.getAnimationTick() < 25)) {
                        double length = this.prevLeftHandPos.subtract(leftHandPos).length();
                        int numClouds = (int) Math.floor(2.5 * length);
                        for (int i = 0; i < numClouds; i++) {
                            double x = this.prevLeftHandPos.x + i * (leftHandPos.x - this.prevLeftHandPos.x) / numClouds;
                            double y = this.prevLeftHandPos.y + i * (leftHandPos.y - this.prevLeftHandPos.y) / numClouds;
                            double z = this.prevLeftHandPos.z + i * (leftHandPos.z - this.prevLeftHandPos.z) / numClouds;
                            for (int j = 0; j < snowflakeDensity; j++) {
                                float xOffset = snowflakeRandomness * (2 * this.random.nextFloat() - 1);
                                float yOffset = snowflakeRandomness * (2 * this.random.nextFloat() - 1);
                                float zOffset = snowflakeRandomness * (2 * this.random.nextFloat() - 1);
                                this.getWorld().addParticle(new ParticleSnowFlake.SnowflakeData(40, false), x + xOffset, y + yOffset, z + zOffset, motionX, motionY - 0.01f, motionZ);
                            }
                            for (int j = 0; j < cloudDensity; j++) {
                                float xOffset = cloudRandomness * (2 * this.random.nextFloat() - 1);
                                float yOffset = cloudRandomness * (2 * this.random.nextFloat() - 1);
                                float zOffset = cloudRandomness * (2 * this.random.nextFloat() - 1);
                                float value = this.random.nextFloat() * 0.1f;
                                this.getWorld().addParticle(new ParticleCloud.CloudData(ParticleHandler.CLOUD, 0.8f + value, 0.8f + value, 1f, (float) (10d + this.random.nextDouble() * 10d), 40, ParticleCloud.EnumCloudBehavior.SHRINK, 1f), x + xOffset, y + yOffset, z + zOffset, motionX, motionY, motionZ);
                            }
                        }
                    }
                }
                this.prevLeftHandPos = leftHandPos;
                this.prevRightHandPos = rightHandPos;
            }
        }
    }

    @Override
    public boolean damage(DamageSource source, float damage) {
        if (source.isIn(DamageTypeTags.IS_FALL)) return false;
        if (source.isOf(DamageTypes.LAVA) && this.getAnimation() == NO_ANIMATION) {
            AnimationHandler.INSTANCE.sendAnimationMessage(this, DODGE_ANIMATION);
        }

        if (source.isIn(DamageTypeTags.IS_FIRE)) damage *= 1.25f;

        if (source.getSource() instanceof PersistentProjectileEntity) {
            this.playSound(MMSounds.ENTITY_WROUGHT_UNDAMAGED, 0.4F, 2);
            Entity entity = source.getAttacker();
            if (entity != null && entity instanceof LivingEntity && (!(entity instanceof PlayerEntity) || !((PlayerEntity) entity).isCreative()) && this.getTarget() == null && !(entity instanceof EntityFrostmaw))
                this.setTarget((LivingEntity) entity);
            if (!this.getActive()) {
                if (this.getAnimation() != DIE_ANIMATION) {
                    if (this.getWorld().getDifficulty() != Difficulty.PEACEFUL) {
                        if (this.getHasCrystal()) AnimationHandler.INSTANCE.sendAnimationMessage(this, ACTIVATE_ANIMATION);
                        else AnimationHandler.INSTANCE.sendAnimationMessage(this, ACTIVATE_NO_CRYSTAL_ANIMATION);
                    }
                }
                if (this.getWorld().getDifficulty() != Difficulty.PEACEFUL) this.setActive(true);
            }
            return false;
        }

        boolean attack = super.damage(source, damage);

        if (attack) {
            this.shouldDodgeMeasure += damage;
            Entity entity = source.getAttacker();
            if (entity != null && entity instanceof LivingEntity && (!(entity instanceof PlayerEntity) || !((PlayerEntity) entity).isCreative()) && this.getTarget() == null && !(entity instanceof EntityFrostmaw))
                this.setTarget((LivingEntity) entity);
            if (!this.getActive()) {
                if (this.getAnimation() != DIE_ANIMATION && this.getWorld().getDifficulty() != Difficulty.PEACEFUL) {
                    if (this.getHasCrystal()) AnimationHandler.INSTANCE.sendAnimationMessage(this, ACTIVATE_ANIMATION);
                    else AnimationHandler.INSTANCE.sendAnimationMessage(this, ACTIVATE_NO_CRYSTAL_ANIMATION);
                }
                if (this.getWorld().getDifficulty() != Difficulty.PEACEFUL) this.setActive(true);
            }
        }

        return attack;
    }

    @Override
    protected void updatePostDeath() {
        super.updatePostDeath();
        if (this.getAnimationTick() == 5) {
            this.playSound(MMSounds.ENTITY_FROSTMAW_DIE, 2.5f, 1);
        } else if (this.getAnimationTick() == 53) {
            this.playSound(MMSounds.ENTITY_FROSTMAW_LAND, 2.5f, 1);
        }
    }

    @Override
    public Animation getDeathAnimation() {
        return DIE_ANIMATION;
    }

    @Override
    public Animation getHurtAnimation() {
        return HURT_ANIMATION;
    }

    public boolean getActive() {
        this.active = this.getDataTracker().get(ACTIVE);
        return this.active;
    }

    public void setActive(boolean active) {
        this.getDataTracker().set(ACTIVE, active);
    }

    public boolean getHasCrystal() {
        return this.getDataTracker().get(HAS_CRYSTAL);
    }

    public void setHasCrystal(boolean hasCrystal) {
        this.getDataTracker().set(HAS_CRYSTAL, hasCrystal);
    }

    public boolean isAlwaysActive() {
        return this.getDataTracker().get(ALWAYS_ACTIVE);
    }

    public void setAlwaysActive(boolean isAlwaysActive) {
        this.getDataTracker().set(ALWAYS_ACTIVE, isAlwaysActive);
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[]{DIE_ANIMATION, HURT_ANIMATION, ROAR_ANIMATION, SWIPE_ANIMATION, SWIPE_TWICE_ANIMATION, ICE_BREATH_ANIMATION, ICE_BALL_ANIMATION, ACTIVATE_ANIMATION, ACTIVATE_NO_CRYSTAL_ANIMATION, DEACTIVATE_ANIMATION, SLAM_ANIMATION, LAND_ANIMATION, DODGE_ANIMATION};
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound compound) {
        super.readCustomDataFromNbt(compound);
        if (compound.contains("has_crystal")) {
            this.setHasCrystal(compound.getBoolean("has_crystal"));
        }
        this.setActive(compound.getBoolean("active"));
        this.setAlwaysActive(compound.getBoolean("alwaysActive"));
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound compound) {
        super.writeCustomDataToNbt(compound);
        compound.putBoolean("has_crystal", this.getHasCrystal());
        compound.putBoolean("active", this.getActive());
        compound.putBoolean("alwaysActive", this.isAlwaysActive());
    }

    @Override
    public boolean cannotDespawn() {
        return this.getHasCrystal();
    }

    @Override
    public boolean hasBossBar() {
        return ConfigHandler.COMMON.MOBS.FROSTMAW.hasBossBar;
    }

    @Override
    protected BossBar.Color bossBarColor() {
        return BossBar.Color.WHITE;
    }

    @Override
    protected Identifier getLootTableId() {
        return LootTableHandler.FROSTMAW;
    }

    @Override
    protected ConfigHandler.CombatConfig getCombatConfig() {
        return ConfigHandler.COMMON.MOBS.FROSTMAW.combatConfig;
    }

    @Override
    public Vec3d getVelocity() {
        if (!this.getActive()) return super.getVelocity().multiply(0, 1, 0);
        return super.getVelocity();
    }

    @Override
    public SoundEvent getBossMusic() {
        return MMSounds.MUSIC_FROSTMAW_THEME;
    }

    @Override
    protected boolean canPlayMusic() {
        return super.canPlayMusic() && (this.active || this.getAnimation() == ACTIVATE_ANIMATION);
    }

    @Override
    public boolean resetHealthOnPlayerRespawn() {
        return ConfigHandler.COMMON.MOBS.FROSTMAW.resetHealthWhenRespawn;
    }
}
