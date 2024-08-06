package com.bobmowzie.mowziesmobs.server.entity.naga;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.tools.dynamics.DynamicChain;
import com.bobmowzie.mowziesmobs.client.particle.ParticleVanillaCloudExtended;
import com.bobmowzie.mowziesmobs.server.ai.animation.AnimationProjectileAttackAI;
import com.bobmowzie.mowziesmobs.server.ai.animation.SimpleAnimationAI;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import com.bobmowzie.mowziesmobs.server.entity.MowzieLLibraryEntity;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntityPoisonBall;
import com.bobmowzie.mowziesmobs.server.loot.LootTableHandler;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import com.bobmowzie.mowziesmobs.server.util.MowzieMathUtil;
import com.iafenvoy.uranus.animation.Animation;
import com.iafenvoy.uranus.animation.AnimationHandler;
import com.iafenvoy.uranus.client.model.tools.ControlledAnimation;
import io.github.fabricators_of_create.porting_lib.attributes.PortingLibAttributes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.AboveGroundTargeting;
import net.minecraft.entity.ai.NoPenaltySolidTargeting;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.ai.control.LookControl;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.BirdNavigation;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.*;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.List;

/**
 * Created by BobMowzie on 9/9/2018.
 */
public class EntityNaga extends MowzieLLibraryEntity implements RangedAttackMob, Monster, Flutterer {
    public static final Animation FLAP_ANIMATION = Animation.create(25);
    public static final Animation DODGE_ANIMATION = Animation.create(10);
    public static final Animation SPIT_ANIMATION = Animation.create(50);
    public static final Animation SWOOP_ANIMATION = Animation.create(54);
    public static final Animation HURT_TO_FALL_ANIMATION = Animation.create(20);
    public static final Animation LAND_ANIMATION = Animation.create(8);
    public static final Animation GET_UP_ANIMATION = Animation.create(33);
    public static final Animation TAIL_DEMO_ANIMATION = Animation.create(80);
    public static final Animation DIE_AIR_ANIMATION = Animation.create(70);
    public static final Animation DIE_GROUND_ANIMATION = Animation.create(70);
    public static final int ROAR_DURATION = 30;
    private static final TrackedData<Boolean> ATTACKING = DataTracker.registerData(EntityNaga.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Float> BANKING = DataTracker.registerData(EntityNaga.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Float> PREV_BANKING = DataTracker.registerData(EntityNaga.class, TrackedDataHandlerRegistry.FLOAT);
    public static int MAX_DIST_FROM_HOME = 50;
    public static int SPIT_COOLDOWN_MAX = 120;
    public static int SWOOP_COOLDOWN_MAX = 90;
    public static int GROUND_TIMER_MAX = 60;
    private final ControlledAnimation hoverAnim = new ControlledAnimation(10);
    private final ControlledAnimation flapAnim = new ControlledAnimation(10);
    @Environment(EnvType.CLIENT)
    public DynamicChain dc;
    @Environment(EnvType.CLIENT)
    public Vec3d[] mouthPos;
    public float hoverAnimFrac;
    public float prevHoverAnimFrac;
    public float flapAnimFrac;
    public float prevFlapAnimFrac;
    @Environment(EnvType.CLIENT)
    public float shoulderRot;
    @Environment(EnvType.CLIENT)
    public float banking;
    @Environment(EnvType.CLIENT)
    public float prevBanking;
    public int roarAnimation = 0;
    public EnumNagaMovement movement = EnumNagaMovement.GLIDING;
    public double prevMotionX;
    public double prevMotionY;
    public double prevMotionZ;
    public int spitCooldown = 0;
    public int swoopCooldown = 0;
    public float swoopTargetCorrectY;
    public float swoopTargetCorrectX;
    public int onGroundTimer = 0;
    public boolean interrupted = false;
    private boolean hasFlapSoundPlayed = false;

    public EntityNaga(EntityType<? extends EntityNaga> type, World world) {
        super(type, world);
        if (world.isClient) {
            this.dc = new DynamicChain(this);
            this.mouthPos = new Vec3d[]{new Vec3d(0, 0, 0)};
        }
        this.experiencePoints = 10;

        this.moveControl = new NagaMoveHelper(this);
        this.lookControl = new NagaLookController(this);
        this.setPathfindingPenalty(PathNodeType.DANGER_FIRE, -1.0F);
        this.setPathfindingPenalty(PathNodeType.WATER, -1.0F);
        this.setPathfindingPenalty(PathNodeType.WATER_BORDER, -1.0F);
        this.setPathfindingPenalty(PathNodeType.FENCE, -1.0F);
        this.setPathfindingPenalty(PathNodeType.WALKABLE, -1.0F);
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MowzieEntity.createAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 30.0D)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 12.0D)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 40)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 4.0D);
    }

    public float getPathfindingFavor(BlockPos pos, WorldView worldIn) {
        return worldIn.getBlockState(pos).isAir() ? 10.0F : 0.0F;
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(0, new FlyOutOfWaterGoal(this));
        this.goalSelector.add(5, new WanderGoal());
        this.goalSelector.add(4, new AIFlyAroundTarget(this));
        this.goalSelector.add(3, new AIFlyTowardsTarget(this));
        this.goalSelector.add(6, new LookAroundGoal(this));
        this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.targetSelector.add(1, new RevengeGoal(this));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, 0, true, true, target -> target.getBlockPos().isWithinDistance(this.getPositionTarget(), this.getPositionTargetRange())) {
            @Override
            public boolean shouldContinue() {
                return super.shouldContinue() && EntityNaga.this.getTarget() != null && EntityNaga.this.getTarget().getBlockPos().isWithinDistance(EntityNaga.this.getPositionTarget(), EntityNaga.this.getPositionTargetRange()) && EntityNaga.this.getAnimation() == NO_ANIMATION;
            }
        });
        this.goalSelector.add(2, new SimpleAnimationAI<>(this, FLAP_ANIMATION, false) {
            @Override
            public void start() {
                super.start();
                EntityNaga.this.playSound(MMSounds.ENTITY_NAGA_FLAP_1, 2, (float) (0.85 + EntityNaga.this.random.nextFloat() * 0.2));
            }

            @Override
            public void tick() {
                super.tick();
                if (EntityNaga.this.getAnimationTick() >= 4 && EntityNaga.this.getAnimationTick() <= 9) {
                    EntityNaga.this.setVelocity(EntityNaga.this.getVelocity().add(0, 0.1, 0));
                }
            }
        });
        this.goalSelector.add(2, new SimpleAnimationAI<>(this, DODGE_ANIMATION, false));
        this.goalSelector.add(2, new AnimationProjectileAttackAI<>(this, SPIT_ANIMATION, 30, null) {
            @Override
            public void start() {
                super.start();
                EntityNaga.this.playSound(MMSounds.ENTITY_NAGA_ACID_CHARGE, 2, 1);
            }

            @Override
            public void tick() {
                super.tick();
                if (EntityNaga.this.interrupted) return;
                //if (getAnimationTick() == 1) playSound(MMSounds.ENTITY_NAGA_ACID_CHARGE, 2, 1);
                if (EntityNaga.this.getAnimationTick() < 9)
                    EntityNaga.this.setVelocity(EntityNaga.this.getVelocity().add(0, 0.015, 0));
//                if (getAnimationTick() == 28) motionY -= 0.2;
            }
        });
        this.goalSelector.add(2, new SimpleAnimationAI<>(this, SWOOP_ANIMATION, true) {
            @Override
            public void start() {
                super.start();
                EntityNaga.this.playSound(MMSounds.ENTITY_NAGA_FLAP_1, 2, 0.7f);
            }

            @Override
            public void tick() {
                super.tick();
                if (EntityNaga.this.interrupted) return;

                Vec3d v = new Vec3d(0, 0, 0);

                int phase1Length = 15;
                int phase2Length = 21;
                LivingEntity target = EntityNaga.this.getTarget();
                if (EntityNaga.this.getAnimationTick() < phase1Length) {
                    if (target != null) {
                        this.entity.lookAtEntity(target, 100, 100);
                        this.entity.getLookControl().lookAt(target, 30F, 30F);
                    }
                }
                if (EntityNaga.this.getAnimationTick() < 23 + phase2Length) {
                    if (EntityNaga.this.getAnimationTick() >= 1 && EntityNaga.this.getAnimationTick() < 1 + phase1Length) {
                        float frame = (EntityNaga.this.getAnimationTick() - 1) / (float) phase1Length;
                        v = v.add(new Vec3d(
                                1.9f * Math.sin(frame * Math.PI * 2) * frame,
                                -1.9f * Math.sin(frame * Math.PI * 2) * frame,
                                0.8f * Math.sin(frame * Math.PI * 2)
                        ));
                    } else if (EntityNaga.this.getAnimationTick() >= 16) {
                        if (EntityNaga.this.getAnimationTick() == 23) {
                            if (target != null) {
                                EntityNaga.this.swoopTargetCorrectY = 0.09f * (float) Math.abs(EntityNaga.this.getY() - target.getY());
                                EntityNaga.this.swoopTargetCorrectX = 0.1f * (float) Math.sqrt((EntityNaga.this.getX() - target.getX()) * (EntityNaga.this.getX() - target.getX()) + (EntityNaga.this.getZ() - target.getZ()) * (EntityNaga.this.getZ() - target.getZ()));
                                if (EntityNaga.this.swoopTargetCorrectX > 1.8f)
                                    EntityNaga.this.swoopTargetCorrectX = 1.8f;
                                if (EntityNaga.this.swoopTargetCorrectY > 2f) EntityNaga.this.swoopTargetCorrectY = 2f;
                            } else {
                                EntityNaga.this.swoopTargetCorrectX = EntityNaga.this.swoopTargetCorrectY = 1;
                            }
                        }
                        if (EntityNaga.this.getAnimationTick() >= 23 && EntityNaga.this.getAnimationTick() < 23 + phase2Length) {
                            float frame = (EntityNaga.this.getAnimationTick() - 23) / (float) phase2Length;
                            v = v.add(new Vec3d(
                                    EntityNaga.this.swoopTargetCorrectX * 1.4 * (1 - Math.exp(2 * (frame - 1))),
                                    EntityNaga.this.swoopTargetCorrectY * -1.5 * (Math.cos(frame * Math.PI) * (1 - Math.exp(7 * (frame - 1)))),
                                    0
                            ));

                            List<LivingEntity> entitiesHit = EntityNaga.this.getEntityLivingBaseNearby(4, 4, 4, 4);
                            for (LivingEntity entityHit : entitiesHit) {
                                if (entityHit instanceof EntityNaga) continue;
                                EntityNaga.this.tryAttack(entityHit);
                            }
                        }
                    }

                    v = v.rotateY((float) Math.toRadians(-EntityNaga.this.getYaw() - 90));
                    EntityNaga.this.setVelocity(v.x, v.y, v.z);
                }

                if (EntityNaga.this.getAnimationTick() == 22) MowziesMobs.PROXY.playNagaSwoopSound(this.entity);

                if (EntityNaga.this.getAnimationTick() == 7)
                    EntityNaga.this.playSound(MMSounds.ENTITY_NAGA_GRUNT_3, 2, 1f);
                if (EntityNaga.this.getAnimationTick() == 22)
                    EntityNaga.this.playSound(MMSounds.ENTITY_NAGA_ROAR_1, 3, 1f);
            }
        });
        this.goalSelector.add(2, new SimpleAnimationAI<>(this, HURT_TO_FALL_ANIMATION, true) {
            @Override
            public void tick() {
                System.out.println("Hello");
            }
        });
        this.goalSelector.add(2, new SimpleAnimationAI<>(this, LAND_ANIMATION, true) {
            @Override
            public void start() {
                super.start();
                EntityNaga.this.playSound(MMSounds.MISC_GROUNDHIT_2, 1.5f, 1);
            }
        });
        this.goalSelector.add(1, new SimpleAnimationAI<>(this, GET_UP_ANIMATION, true) {
            @Override
            public void tick() {
                super.tick();
                if (EntityNaga.this.getAnimationTick() == 13)
                    EntityNaga.this.playSound(MMSounds.ENTITY_NAGA_FLAP_1, 2f, 1);

                if (EntityNaga.this.getAnimationTick() == 15) {
                    EntityNaga.this.setVelocity(EntityNaga.this.getVelocity().add(0, 1.6, 0));
                }
            }
        });
        this.goalSelector.add(2, new SimpleAnimationAI<>(this, TAIL_DEMO_ANIMATION, false));
    }

    @Override
    protected EntityNavigation createNavigation(World worldIn) {
        BirdNavigation flyingpathnavigator = new BirdNavigation(this, worldIn) {
            public boolean isValidPosition(BlockPos pos) {
                return !this.world.getBlockState(pos.down()).isAir();
            }
        };
        flyingpathnavigator.setCanPathThroughDoors(false);
        flyingpathnavigator.setCanSwim(false);
        flyingpathnavigator.setCanEnterOpenDoors(false);
        return flyingpathnavigator;
    }

    @Override
    public boolean isInAir() {
        return !this.isOnGround();
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.getDataTracker().startTracking(ATTACKING, false);
        this.getDataTracker().startTracking(BANKING, 0.0f);
        this.getDataTracker().startTracking(PREV_BANKING, 0.0f);
    }

    @Override
    public boolean shouldRender(double distance) {
        return distance < 16600;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public Box getVisibilityBoundingBox() {
        return super.getVisibilityBoundingBox().expand(12.0D);
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        if (this.getAnimation() != NO_ANIMATION) return null;
        int r = this.random.nextInt(4);
        if (r == 0) {
            this.playSound(MMSounds.ENTITY_NAGA_ROAR.get(this.random.nextInt(4)), 5, 1);
            this.roarAnimation = 0;
        } else if (r <= 2) {
            this.playSound(MMSounds.ENTITY_NAGA_GROWL.get(this.random.nextInt(3)), 4, 1);
        }
        return null;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        this.playSound(MMSounds.ENTITY_NAGA_GRUNT.get(this.random.nextInt(3)), 2, 1);
        return null;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        this.playSound(MMSounds.ENTITY_NAGA_ROAR.get(this.random.nextInt(4)), 3, 1);
        return null;
    }

    @Override
    public void tick() {
        this.prevMotionX = this.getVelocity().x;
        this.prevMotionY = this.getVelocity().y;
        this.prevMotionZ = this.getVelocity().z;
        this.prevHoverAnimFrac = this.hoverAnimFrac;
        this.prevFlapAnimFrac = this.flapAnimFrac;

        super.tick();
//        setDead();
        this.bodyYaw = this.getYaw();
        if (this.getWorld().isClient()) {
            this.banking = this.getBanking();
            this.prevBanking = this.getPrevBanking();
        }

        if (this.spitCooldown > 0) this.spitCooldown--;
        if (this.swoopCooldown > 0) this.swoopCooldown--;
        if (this.onGroundTimer > 0) this.onGroundTimer--;
        if (this.roarAnimation < ROAR_DURATION) this.roarAnimation++;

        if (this.getAnimation() == null) AnimationHandler.INSTANCE.sendAnimationMessage(this, NO_ANIMATION);

        if (this.hasStatusEffect(StatusEffects.POISON)) this.removeStatusEffectInternal(StatusEffects.POISON);

//        if (tickCount == 1) {
//            System.out.println("Naga at " + position());
//        }

        if (!this.getWorld().isClient) {
            if (this.getTarget() != null && this.targetDistance < 29.5 && this.movement != EnumNagaMovement.FALLEN && this.movement != EnumNagaMovement.FALLING) {
                this.setAttacking(true);
                if (this.getAnimation() == NO_ANIMATION && this.swoopCooldown == 0 && this.random.nextInt(80) == 0 && this.getY() - this.getTarget().getY() > 0) {
                    this.interrupted = false;
//                    System.out.println("Swoop");
                    AnimationHandler.INSTANCE.sendAnimationMessage(this, SWOOP_ANIMATION);
                    this.swoopCooldown = SWOOP_COOLDOWN_MAX;
                } else if (this.getAnimation() == NO_ANIMATION && this.spitCooldown == 0 && this.random.nextInt(80) == 0) {
                    this.interrupted = false;
//                    System.out.println("Spit");
                    AnimationHandler.INSTANCE.sendAnimationMessage(this, SPIT_ANIMATION);
                    this.spitCooldown = SPIT_COOLDOWN_MAX;
                }
            } else {
                this.setAttacking(false);
            }
        }

        if (this.movement != EnumNagaMovement.FALLING && this.movement != EnumNagaMovement.FALLEN) {
            if (this.isAttacking()) {
                this.movement = EnumNagaMovement.HOVERING;
                this.hoverAnim.increaseTimer();

                if (this.getAnimation() == NO_ANIMATION && !this.getWorld().isClient) {
                    List<ProjectileEntity> projectilesNearby = this.getEntitiesNearby(ProjectileEntity.class, 30);
                    for (ProjectileEntity a : projectilesNearby) {
                        Vec3d aActualMotion = new Vec3d(a.getX() - a.prevX, a.getY() - a.prevY, a.getZ() - a.prevZ);
                        if (aActualMotion.length() < 0.1 || a.age <= 1) {
                            continue;
                        }

                        float dot = (float) a.getVelocity().normalize().dotProduct(this.getPos().subtract(a.getPos()).normalize());
                        if (dot > 0.96) {
                            Vec3d dodgeVec = a.getVelocity().crossProduct(new Vec3d(0, 1, 0)).normalize().multiply(1.2);
                            Vec3d newPosLeft = this.getPos().add(dodgeVec.multiply(2));
                            Vec3d newPosRight = this.getPos().add(dodgeVec.multiply(-2));
                            Vec3d diffLeft = newPosLeft.subtract(a.getPos());
                            Vec3d diffRight = newPosRight.subtract(a.getPos());
                            if (diffRight.dotProduct(a.getVelocity()) > diffLeft.dotProduct(a.getVelocity())) {
                                dodgeVec = dodgeVec.multiply(-1);
                            }
                            this.setVelocity(this.getVelocity().add(dodgeVec));
                            AnimationHandler.INSTANCE.sendAnimationMessage(this, DODGE_ANIMATION);
                        }
                    }
                }
            } else {
                this.movement = EnumNagaMovement.GLIDING;
                this.hoverAnim.decreaseTimer();
                this.flapAnim.decreaseTimer();
            }
        } else if (this.movement == EnumNagaMovement.FALLING) {
            if (this.isOnGround() || this.isInLava() || this.isTouchingWater()) {
                AnimationHandler.INSTANCE.sendAnimationMessage(this, EntityNaga.LAND_ANIMATION);
                this.movement = EnumNagaMovement.FALLEN;
                this.onGroundTimer = GROUND_TIMER_MAX;
                this.setVelocity(0, this.getVelocity().y, 0);
                this.getNavigation().stop();
            }
        }

        if (this.movement == EnumNagaMovement.FALLEN) {
            if (this.onGroundTimer <= 0 && this.getAnimation() == NO_ANIMATION) {
                this.setAnimationTick(0);
                AnimationHandler.INSTANCE.sendAnimationMessage(this, EntityNaga.GET_UP_ANIMATION);
            }
        }

        if (this.getAnimation() == SWOOP_ANIMATION && this.getAnimationTick() < 43) {
            this.hoverAnim.increaseTimer();
            this.flapAnim.decreaseTimer();
        } else if (this.getAnimation() == HURT_TO_FALL_ANIMATION) {
            this.flapAnim.decreaseTimer();
            this.hoverAnim.increaseTimer();
        } else if (this.getAnimation() == LAND_ANIMATION) {
            this.flapAnim.decreaseTimer();
            this.hoverAnim.increaseTimer();
        } else if (this.getAnimation() == GET_UP_ANIMATION && this.getAnimationTick() < 26) {
            this.flapAnim.decreaseTimer();
            this.hoverAnim.increaseTimer();
        } else if (this.movement == EnumNagaMovement.FALLEN) {
            this.flapAnim.decreaseTimer();
            this.hoverAnim.increaseTimer();
            this.setYaw(this.prevYaw);
            this.headYaw = this.prevHeadYaw;
            this.setPitch(this.prevPitch);
        } else if (this.movement == EnumNagaMovement.FALLING) {
            this.flapAnim.decreaseTimer();
            this.hoverAnim.increaseTimer();
        } else {
            this.flapAnim.increaseTimer();
        }

        if (this.getAnimation() == SPIT_ANIMATION && this.getWorld().isClient && this.mouthPos != null && !this.interrupted) {
            if (this.getAnimationTick() == 33) {
//            System.out.println(mouthPos);
                float explodeSpeed = 2.4f;
                for (int i = 0; i < 25; i++) {
                    Vec3d particlePos = new Vec3d(0.25, 0, 0);
                    particlePos = particlePos.rotateY((float) (this.random.nextFloat() * 2 * Math.PI));
                    particlePos = particlePos.rotateX((float) (this.random.nextFloat() * 2 * Math.PI));
                    double value = this.random.nextFloat() * 0.1f;
                    double life = this.random.nextFloat() * 10f + 20f;
                    ParticleVanillaCloudExtended.spawnVanillaCloud(this.getWorld(), particlePos.x + this.mouthPos[0].x, particlePos.y + this.mouthPos[0].y, particlePos.z + this.mouthPos[0].z, particlePos.x * explodeSpeed, particlePos.y * explodeSpeed, particlePos.z * explodeSpeed, 1, 0.25d + value, 0.75d + value, 0.25d + value, 0.6, life);
                }
            }

            if (this.getAnimationTick() <= 15 && this.mouthPos != null && !this.interrupted) {
//            System.out.println(mouthPos);

                int howMany = 4;
                for (int i = 0; i < howMany; i++) {
                    Vec3d particlePos = new Vec3d(3, 0, 0);
                    particlePos = particlePos.rotateY((float) (this.random.nextFloat() * 2 * Math.PI));
                    particlePos = particlePos.rotateX((float) (this.random.nextFloat() * 2 * Math.PI));
                    double value = this.random.nextFloat() * 0.15f;
                    ParticleVanillaCloudExtended.spawnVanillaCloudDestination(this.getWorld(), particlePos.x + this.mouthPos[0].x, particlePos.y + this.mouthPos[0].y, particlePos.z + this.mouthPos[0].z, 0, 0, 0, 1, 0.25d + value, 0.75d + value, 0.25d + value, 0.9, 15, this.mouthPos);
                }
            }
        }

        if (this.getAnimation() == HURT_TO_FALL_ANIMATION && this.getAnimationTick() == 17) {
            this.movement = EnumNagaMovement.FALLING;
        }

        if (this.getAnimation() == GET_UP_ANIMATION && this.getAnimationTick() == 26) {
            this.movement = EnumNagaMovement.HOVERING;
        }

        if (this.getWorld().isClient && this.movement == EnumNagaMovement.HOVERING && this.flapAnim.getAnimationFraction() >= 0.5) {

            if (this.shoulderRot > 0.9) this.hasFlapSoundPlayed = false;

            if (this.shoulderRot <= 0.7 && !this.hasFlapSoundPlayed) {
                this.getWorld().playSound(this.getX(), this.getY(), this.getZ(), MMSounds.ENTITY_NAGA_FLAP_1, SoundCategory.HOSTILE, 2, (float) (0.85 + this.random.nextFloat() * 0.2), false);
                this.hasFlapSoundPlayed = true;
            }
        }

        this.hoverAnimFrac = this.hoverAnim.getAnimationProgressSinSqrt();
        this.flapAnimFrac = this.flapAnim.getAnimationProgressSinSqrt();

        if (!this.getWorld().isClient && this.getWorld().getDifficulty() == Difficulty.PEACEFUL) {
            this.discard();
        }

//        setAttacking(true);
//        getNavigator().clearPath();
//        setMotion(new Vector3d(0, 0, 0));
//        setPosition(getPosX(), 10, getPosZ());
//
//        if (getAnimation() == NO_ANIMATION) {
//            AnimationHandler.INSTANCE.sendAnimationMessage(this, TAIL_DEMO_ANIMATION);
//        }
    }

    @Override
    public EntityData initialize(ServerWorldAccess worldIn, LocalDifficulty difficultyIn, SpawnReason reason, EntityData spawnDataIn, NbtCompound dataTag) {
        this.setPositionTarget(this.getBlockPos(), MAX_DIST_FROM_HOME);
        return super.initialize(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    @Override
    protected ConfigHandler.SpawnConfig getSpawnConfig() {
        return ConfigHandler.COMMON.MOBS.NAGA.spawnConfig;
    }

    @Override
    protected ConfigHandler.CombatConfig getCombatConfig() {
        return ConfigHandler.COMMON.MOBS.NAGA.combatConfig;
    }

    @Override
    public boolean canSpawn(WorldAccess world, SpawnReason reason) {
        boolean flag = super.canSpawn(world, reason);
        this.setPosition(this.getX(), this.getY() + 5, this.getZ());
        return flag && world.getDifficulty() != Difficulty.PEACEFUL;
    }

    @Override
    public boolean canSpawn(WorldView worldIn) {
        boolean liquid = !this.getWorld().containsFluid(this.getBoundingBox());
        boolean worldCollision = this.getWorld().isSpaceEmpty(this, this.getBoundingBox());
        boolean mobCollision = this.getWorld().doesNotIntersectEntities(this);

        return liquid && worldCollision && mobCollision;
    }

    @Override
    protected void updatePostDeath() {
        super.updatePostDeath();
        if (this.deathTime == 15 && this.movement != EnumNagaMovement.FALLEN) this.movement = EnumNagaMovement.FALLING;
    }

    @Override
    public boolean damage(DamageSource source, float damage) {
        boolean flag = super.damage(source, damage);
        boolean isSpitting = this.getAnimation() == SPIT_ANIMATION && this.getAnimationTick() < 30;
        boolean isSwooping = this.getAnimation() == SWOOP_ANIMATION && this.getAnimationTick() < 25;
        if (flag && this.movement != EnumNagaMovement.FALLING && (isSpitting || isSwooping) && damage > 0) {
            AnimationHandler.INSTANCE.sendAnimationMessage(this, EntityNaga.HURT_TO_FALL_ANIMATION);
            this.interrupted = true;
        }
        return flag;
    }

    @Override
    public void attack(LivingEntity target, float distanceFactor) {
        if (this.interrupted) return;
        Vec3d projectilePos = new Vec3d(1, -0.7, 0);
        projectilePos = projectilePos.rotateY((float) Math.toRadians(-this.getYaw() - 90));
        projectilePos = projectilePos.add(this.getPos());
        projectilePos = projectilePos.add(new Vec3d(0, 0, 1).rotateX((float) Math.toRadians(-this.getPitch())).rotateY((float) Math.toRadians(-this.headYaw)));
        projectilePos = projectilePos.add(new Vec3d(0, 0, 0));
        EntityPoisonBall poisonBall = new EntityPoisonBall(EntityHandler.POISON_BALL, this.getWorld(), this);
        poisonBall.setPosition(projectilePos.x, projectilePos.y, projectilePos.z);
        Vec3d look = this.getRotationVector();
        Vec3d dir = new Vec3d(look.x, 0, look.z).normalize();
        if (target != null) {
            float dy = (float) (projectilePos.y - target.getY());
            float dx = (float) (projectilePos.x - target.getX());
            float dz = (float) (projectilePos.z - target.getZ());
            float dist = (float) Math.sqrt(dx * dx + dz * dz);
            float timeGuess = (float) Math.sqrt(2 * dy / EntityPoisonBall.GRAVITY);
            float speed = Math.min(dist / timeGuess, 0.9f);
            poisonBall.shoot(dir.x * speed, 0.1, dir.z * speed, 1f, 0);
        }
        this.getWorld().spawnEntity(poisonBall);

        this.playSound(MMSounds.ENTITY_NAGA_ACID_SPIT, 2, 1);
        this.playSound(MMSounds.ENTITY_NAGA_ACID_SPIT_HISS, 2, 1);
    }

    @Override
    public Animation getDeathAnimation() {
        if (this.movement == EnumNagaMovement.FALLEN) return DIE_GROUND_ANIMATION;
        else return DIE_AIR_ANIMATION;
    }

    @Override
    public Animation getHurtAnimation() {
        return null;
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[]{FLAP_ANIMATION, DODGE_ANIMATION, SWOOP_ANIMATION, SPIT_ANIMATION, HURT_TO_FALL_ANIMATION, LAND_ANIMATION, GET_UP_ANIMATION, DIE_AIR_ANIMATION, DIE_GROUND_ANIMATION, TAIL_DEMO_ANIMATION};
    }

    @Override
    public boolean handleFallDamage(float distance, float damageMultiplier, DamageSource source) {
        if (this.movement == EnumNagaMovement.FALLING) {
            return super.handleFallDamage(distance, damageMultiplier, source);
        }
        return false;
    }

    protected void fall(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
        if (this.movement == EnumNagaMovement.FALLING) {
            super.fall(y, onGroundIn, state, pos);
        }
    }

    public void travel(Vec3d motion) {
        double d0 = 0.08D;
        EntityAttributeInstance gravity = this.getAttributeInstance(PortingLibAttributes.ENTITY_GRAVITY);
        boolean flag = this.getVelocity().y <= 0.0D;
//        if (flag && this.isPotionActive(Effects.SLOW_FALLING)) {
//            if (!gravity.hasModifier(SLOW_FALLING)) gravity.applyNonPersistentModifier(SLOW_FALLING);
//            this.fallDistance = 0.0F;
//        } else if (gravity.hasModifier(SLOW_FALLING)) {
//            gravity.removeModifier(SLOW_FALLING);
//        } TODO: SLOW_FALLING has private access. Skip?
        d0 = gravity.getValue();

        FluidState fluidstate = this.getWorld().getFluidState(this.getBlockPos());
        if (this.isTouchingWater() && this.shouldSwimInFluids() && !this.canWalkOnFluid(fluidstate)) {
            double d8 = this.getY();
            float f5 = this.isSprinting() ? 0.9F : this.getBaseMovementSpeedMultiplier();
            float f6 = 0.02F;
            float f7 = (float) EnchantmentHelper.getDepthStrider(this);
            if (f7 > 3.0F) {
                f7 = 3.0F;
            }

            if (!this.isOnGround()) {
                f7 *= 0.5F;
            }

            if (f7 > 0.0F) {
                f5 += (0.54600006F - f5) * f7 / 3.0F;
                f6 += (this.getMovementSpeed() - f6) * f7 / 3.0F;
            }

            if (this.hasStatusEffect(StatusEffects.DOLPHINS_GRACE)) {
                f5 = 0.96F;
            }

            f6 *= (float) this.getAttributeInstance(PortingLibAttributes.SWIM_SPEED).getValue();
            this.updateVelocity(f6, motion);
            this.move(MovementType.SELF, this.getVelocity());
            Vec3d vector3d6 = this.getVelocity();
            if (this.horizontalCollision && this.isClimbing()) {
                vector3d6 = new Vec3d(vector3d6.x, 0.2D, vector3d6.z);
            }

            this.setVelocity(vector3d6.multiply(f5, 0.8F, f5));
            Vec3d vec32 = this.applyFluidMovingSpeed(d0, flag, this.getVelocity());
            this.setVelocity(vec32);
            if (this.horizontalCollision && this.doesNotCollide(vec32.x, vec32.y + (double) 0.6F - this.getY() + d8, vec32.z)) {
                this.setVelocity(vec32.x, 0.3F, vec32.z);
            }
        } else if (this.isInLava() && this.shouldSwimInFluids() && !this.canWalkOnFluid(fluidstate)) {
            double d7 = this.getY();
            this.updateVelocity(0.02F, motion);
            this.move(MovementType.SELF, this.getVelocity());
            if (this.getFluidHeight(FluidTags.LAVA) <= this.getSwimHeight()) {
                this.setVelocity(this.getVelocity().multiply(0.5D, 0.8F, 0.5D));
                Vec3d vector3d3 = this.applyFluidMovingSpeed(d0, flag, this.getVelocity());
                this.setVelocity(vector3d3);
            } else {
                this.setVelocity(this.getVelocity().multiply(0.5D));
            }

            if (!this.hasNoGravity()) {
                this.setVelocity(this.getVelocity().add(0.0D, -d0 / 4.0D, 0.0D));
            }

            Vec3d vector3d4 = this.getVelocity();
            if (this.horizontalCollision && this.doesNotCollide(vector3d4.x, vector3d4.y + (double) 0.6F - this.getY() + d7, vector3d4.z)) {
                this.setVelocity(vector3d4.x, 0.3F, vector3d4.z);
            }
        } else if (this.movement == EnumNagaMovement.HOVERING) {
            BlockPos ground = new BlockPos((int) this.getX(), (int) (this.getBoundingBox().minY - 1.0D), (int) this.getZ());
            float f = 0.91F;
            if (this.isOnGround()) {
                f = this.getWorld().getBlockState(ground).getBlock().getSlipperiness() * 0.91F;
            }

            float f1 = 0.16277137F / (f * f * f);
            f = 0.91F;
            if (this.isOnGround()) {
                f = this.getWorld().getBlockState(ground).getBlock().getSlipperiness() * 0.91F;
            }

            this.updateVelocity(this.isOnGround() ? 0.1F * f1 : 0.02F, motion);
            this.move(MovementType.SELF, this.getVelocity());
            this.setVelocity(this.getVelocity().multiply(f));

            BlockPos destination = this.getNavigation().getTargetPos();
            if (destination != null) {
                double dx = destination.getX() - this.getX();
                double dy = destination.getY() - this.getY();
                double dz = destination.getZ() - this.getZ();
                double distanceToDest = Math.sqrt(dx * dx + dy * dy + dz * dz);
                if (distanceToDest < 0.1 && this.getAnimation() == NO_ANIMATION) {
                    this.setVelocity(0, 0, 0);
                }
            }
        } else if (this.movement == EnumNagaMovement.GLIDING) {
            Vec3d vec3 = this.getVelocity();
            if (vec3.y > -0.5D) {
                this.fallDistance = 1.0F;
            }

            Vec3d moveDirection = this.getRotationVector();
            moveDirection = moveDirection.normalize();
            float f6 = this.getPitch() * ((float) Math.PI / 180F);
            double d9 = Math.sqrt(moveDirection.x * moveDirection.x + moveDirection.z * moveDirection.z);
            double d11 = Math.sqrt(vec3.horizontalLengthSquared());
            double d12 = moveDirection.length();
            float f3 = MathHelper.cos(f6);
            f3 = (float) ((double) f3 * (double) f3 * Math.min(1.0D, d12 / 0.4D));
            vec3 = this.getVelocity().add(0.0D, d0 * (-1.0D + (double) f3 * 0.75D), 0.0D);
            if (vec3.y < 0.0D && d9 > 0.0D) {
                double d3 = vec3.y * -0.1D * (double) f3;
                vec3 = vec3.add(moveDirection.x * d3 / d9, d3, moveDirection.z * d3 / d9);
            }

            if (f6 < 0.0F && d9 > 0.0D) {
                double d13 = d11 * (double) (-MathHelper.sin(f6)) * 0.04D;
                vec3 = vec3.add(-moveDirection.x * d13 / d9, d13 * 3.2D, -moveDirection.z * d13 / d9);
            }

            if (d9 > 0.0D) {
                vec3 = vec3.add((moveDirection.x / d9 * d11 - vec3.x) * 0.1D, 0.0D, (moveDirection.z / d9 * d11 - vec3.z) * 0.1D);
            }

            this.setVelocity(vec3.multiply(0.99F, 0.98F, 0.99F));
            this.move(MovementType.SELF, this.getVelocity());
            if (moveDirection.getY() < 0 && this.getAnimation() == NO_ANIMATION)
                AnimationHandler.INSTANCE.sendAnimationMessage(this, FLAP_ANIMATION);

        } else if (this.movement == EnumNagaMovement.FALLING || this.movement == EnumNagaMovement.FALLEN || this.isAiDisabled()) {
            BlockPos blockpos = this.getVelocityAffectingPos();
            float f2 = this.getWorld().getBlockState(this.getVelocityAffectingPos()).getBlock().getSlipperiness();
            float f3 = this.isOnGround() ? f2 * 0.91F : 0.91F;
            Vec3d vec35 = this.applyMovementInput(motion, f2);
            double d2 = vec35.y;
            if (this.hasStatusEffect(StatusEffects.LEVITATION)) {
                d2 += (0.05D * (double) (this.getStatusEffect(StatusEffects.LEVITATION).getAmplifier() + 1) - vec35.y) * 0.2D;
            } else if (this.getWorld().isClient && !this.getWorld().isChunkLoaded(blockpos)) {
                if (this.getY() > (double) this.getWorld().getBottomY()) {
                    d2 = -0.1D;
                } else {
                    d2 = 0.0D;
                }
            } else if (!this.hasNoGravity()) {
                d2 -= d0;
            }

            if (this.hasNoDrag()) {
                this.setVelocity(vec35.x, d2, vec35.z);
            } else {
                this.setVelocity(vec35.x * (double) f3, d2 * (double) 0.98F, vec35.z * (double) f3);
            }
        }

        this.updateLimbs(true);
    }

    /**
     * Returns true if this entity should move as if it were on a ladder (either because it's actually on a ladder, or
     * for AI reasons)
     */
    public boolean isClimbing() {
        return false;
    }

    @Override
    public boolean isAttacking() {
        return this.getDataTracker().get(ATTACKING);
    }

    public void setAttacking(boolean attacking) {
        this.getDataTracker().set(ATTACKING, attacking);
    }

    public float getBanking() {
        return this.getDataTracker().get(BANKING);
    }

    public void setBanking(float banking) {
        this.getDataTracker().set(BANKING, banking);
    }

    public float getPrevBanking() {
        return this.getDataTracker().get(PREV_BANKING);
    }

    public void setPrevBanking(float prevBanking) {
        this.getDataTracker().set(PREV_BANKING, prevBanking);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound compound) {
        super.writeCustomDataToNbt(compound);
        compound.putInt("HomePosX", this.getPositionTarget().getX());
        compound.putInt("HomePosY", this.getPositionTarget().getY());
        compound.putInt("HomePosZ", this.getPositionTarget().getZ());
        compound.putInt("HomeDist", (int) this.getPositionTargetRange());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound compound) {
        super.readCustomDataFromNbt(compound);
        int i = compound.getInt("HomePosX");
        int j = compound.getInt("HomePosY");
        int k = compound.getInt("HomePosZ");
        int dist = compound.getInt("HomeDist");
        this.setPositionTarget(new BlockPos(i, j, k), dist);
    }

    @Override
    protected Identifier getLootTableId() {
        return LootTableHandler.NAGA;
    }

    public enum EnumNagaMovement {
        GLIDING,
        HOVERING,
        SWIMMING,
        FALLING,
        FALLEN
    }

    static class AILookAround extends Goal {
        private final EntityNaga parentEntity;

        public AILookAround(EntityNaga naga) {
            this.parentEntity = naga;
            this.setControls(EnumSet.of(Control.LOOK));
        }

        /**
         * Returns whether the EntityAIBase should begin execution.
         */
        public boolean canStart() {
            return true;
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void tick() {
            if (this.parentEntity.getTarget() == null) {
                Vec3d motion = this.parentEntity.getVelocity();
                this.parentEntity.setYaw(-((float) MathHelper.atan2(motion.x, motion.z)) * (180F / (float) Math.PI));
                this.parentEntity.bodyYaw = this.parentEntity.getYaw();
            } else {
                LivingEntity entitylivingbase = this.parentEntity.getTarget();
                if (entitylivingbase.squaredDistanceTo(this.parentEntity) < 1600.0D) {
                    double d1 = entitylivingbase.getX() - this.parentEntity.getX();
                    double d2 = entitylivingbase.getZ() - this.parentEntity.getZ();
                    this.parentEntity.setYaw(-((float) MathHelper.atan2(d1, d2)) * (180F / (float) Math.PI));
                    this.parentEntity.bodyYaw = this.parentEntity.getYaw();
                }
            }
        }
    }

    class WanderGoal extends Goal {
        private boolean seesGround;

        WanderGoal() {
            this.setControls(EnumSet.of(Control.MOVE));
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean canStart() {
            return EntityNaga.this.navigation.isIdle() && EntityNaga.this.getTarget() == null;
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean shouldContinue() {
            return EntityNaga.this.navigation.isFollowingPath() && !EntityNaga.this.navigation.getTargetPos().isWithinDistance(EntityNaga.this.getBlockPos(), 4) && EntityNaga.this.getTarget() == null;
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void start() {
            Vec3d vector3d = this.getRandomLocation();
            if (vector3d != null) {
                EntityNaga.this.navigation.setRangeMultiplier(0.5F);
                EntityNaga.this.navigation.startMovingAlong(EntityNaga.this.navigation.findPathTo(BlockPos.ofFloored(vector3d), 1), 1.0D);
            }
        }

        @Override
        public void tick() {
            super.tick();
            if (!this.seesGround) {
                Vec3d newLocation = this.getRandomLocation();
                if (newLocation != null && this.seesGround) {
                    EntityNaga.this.navigation.startMovingAlong(EntityNaga.this.navigation.findPathTo(BlockPos.ofFloored(newLocation), 1), 1.0D);
                }
            }
        }

        @Override
        public void stop() {
            super.stop();
            EntityNaga.this.getNavigation().stop();
        }

        @Nullable
        private Vec3d getRandomLocation() {
            Vec3d vector3d;
//            if (EntityNaga.this.isHiveValid() && !EntityNaga.this.isWithinDistance(EntityNaga.this.hivePos, 22)) {
//                Vector3d vector3d1 = Vector3d.copyCentered(EntityNaga.this.hivePos);
//                vector3d = vector3d1.subtract(EntityNaga.this.getPositionVec()).normalize();
//            } else {
            vector3d = EntityNaga.this.getRotationVec(0.0F);
//            }
            Vec3d position = AboveGroundTargeting.find(EntityNaga.this, 24, 24, vector3d.x, vector3d.z, ((float) Math.PI / 2F), 18, 8);
            if (position == null) {
                Vec3d sumPos = EntityNaga.this.getPos().add(vector3d);
                position = NoPenaltySolidTargeting.find(EntityNaga.this, 24, 8, -8, sumPos.x, sumPos.z, ((float) Math.PI / 2F));
                this.seesGround = false;
            } else {
                this.seesGround = true;
            }
            if (position == null || !EntityNaga.this.getWorld().isAir(BlockPos.ofFloored(position).down())) return null;
            Vec3d offset = position.subtract(EntityNaga.this.getPos());
            Box newBB = EntityNaga.this.getBoundingBox().offset(offset);
            if (EntityNaga.this.getWorld().isSpaceEmpty(newBB) && EntityNaga.this.getWorld().raycast(new RaycastContext(EntityNaga.this.getPos(), position, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, EntityNaga.this)).getType() != HitResult.Type.BLOCK)
                return position;
            return null;
        }
    }

    class AIFlyAroundTarget extends Goal {
        private final EntityNaga parentEntity;

        public AIFlyAroundTarget(EntityNaga naga) {
            this.parentEntity = naga;
            this.setControls(EnumSet.of(Control.MOVE));
        }

        /**
         * Returns whether the EntityAIBase should begin execution.
         */
        public boolean canStart() {
            if (this.parentEntity.getTarget() != null) {
                if (this.parentEntity.getNavigation().isIdle()) {
                    return this.parentEntity.random.nextInt(60) == 0;
                }
            }
            return false;
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean shouldContinue() {
            if (EntityNaga.this.getTarget() == null) return false;
            BlockPos navigatorPos = this.parentEntity.getNavigation().getTargetPos();
            if (navigatorPos == null) {
                return false;
            }
            double dx = navigatorPos.getX() - this.parentEntity.getX();
            double dy = navigatorPos.getY() - this.parentEntity.getY();
            double dz = navigatorPos.getZ() - this.parentEntity.getZ();
            double distanceToDest = Math.sqrt(dx * dx + dy * dy + dz * dz);
            if (distanceToDest > 60.D) return false;

            LivingEntity target = this.parentEntity.getTarget();
            double dx2 = navigatorPos.getX() - target.getX();
            double dy2 = navigatorPos.getY() - target.getY();
            double dz2 = navigatorPos.getZ() - target.getZ();
            double distanceDestToTarget = Math.sqrt(dx2 * dx2 + dy2 * dy2 + dz2 * dz2);
            if (distanceDestToTarget > 20.D || distanceDestToTarget < 5) return false;

            return EntityNaga.this.navigation.isFollowingPath() && !EntityNaga.this.navigation.getTargetPos().isWithinDistance(EntityNaga.this.getBlockPos(), 1);
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void start() {
            Random random = this.parentEntity.getRandom();
            LivingEntity target = this.parentEntity.getTarget();
            float yaw = (float) (random.nextFloat() * Math.PI * 2);
            float radius = 16;
            double d0 = target.getX() + Math.cos(yaw) * radius;
            double d1 = target.getY() + 8 + random.nextFloat() * 5;
            double d2 = target.getZ() + Math.sin(yaw) * radius;
//            while (parentEntity.world.hasWater(new BlockPos(d0, d1, d2))) {
//                d1 += 1;
//            }
            double speed = this.parentEntity.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).getValue();
            if (!this.parentEntity.getWorld().isWater(BlockPos.ofFloored(d0, d1, d2)))
                this.parentEntity.getNavigation().startMovingTo(d0, d1, d2, speed);
        }

        @Override
        public void stop() {
            super.stop();
            EntityNaga.this.getNavigation().stop();
        }
    }

    class AIFlyTowardsTarget extends Goal {
        private final EntityNaga parentEntity;

        public AIFlyTowardsTarget(EntityNaga naga) {
            this.parentEntity = naga;
            this.setControls(EnumSet.of(Control.MOVE));
        }

        /**
         * Returns whether the EntityAIBase should begin execution.
         */
        public boolean canStart() {
            return this.parentEntity.getTarget() != null && EntityNaga.this.squaredDistanceTo(this.parentEntity.getTarget()) >= 870.25D;
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean shouldContinue() {
            if (EntityNaga.this.getTarget() == null) return false;
            if (EntityNaga.this.getTarget().squaredDistanceTo(this.parentEntity) <= 500.0D) return false;
            BlockPos navigatorPos = this.parentEntity.getNavigation().getTargetPos();
            if (navigatorPos == null) {
                return false;
            }
            double dx = navigatorPos.getX() - this.parentEntity.getX();
            double dy = navigatorPos.getY() - this.parentEntity.getY();
            double dz = navigatorPos.getZ() - this.parentEntity.getZ();
            double distanceToDest = Math.sqrt(dx * dx + dy * dy + dz * dz);
            if (distanceToDest > 60.D) return false;

            return EntityNaga.this.navigation.isFollowingPath();
        }

        @Override
        public void tick() {
            super.tick();
            LivingEntity target = this.parentEntity.getTarget();
            double speed = this.parentEntity.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).getValue();
            BlockPos targetPos = target.getBlockPos().up(8);
            if (!this.parentEntity.getWorld().isWater(targetPos))
                this.parentEntity.getNavigation().startMovingTo(targetPos.getX(), targetPos.getY(), targetPos.getZ(), speed);
        }

        @Override
        public void stop() {
            super.stop();
            EntityNaga.this.getNavigation().stop();
        }
    }

    public class FlyOutOfWaterGoal extends Goal {
        private final EntityNaga entity;

        public FlyOutOfWaterGoal(EntityNaga entityIn) {
            this.entity = entityIn;
            this.setControls(EnumSet.of(Control.MOVE));
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean canStart() {
            return this.entity.movement != EnumNagaMovement.FALLING && this.entity.movement != EnumNagaMovement.FALLEN && this.entity.isTouchingWater() && this.entity.getFluidHeight(FluidTags.WATER) > this.entity.getSwimHeight() || this.entity.isInLava();
        }

        @Override
        public void start() {
            if (this.entity.getAnimation() == NO_ANIMATION)
                AnimationHandler.INSTANCE.sendAnimationMessage(this.entity, EntityNaga.FLAP_ANIMATION);
        }

        @Override
        public boolean shouldContinue() {
            return false;
        }
    }

    class NagaMoveHelper extends MoveControl {
        private final EntityNaga parentEntity;
        private int courseChangeCooldown;

        private float speedFactor = 0.1F;

        public NagaMoveHelper(EntityNaga naga) {
            super(naga);
            this.parentEntity = naga;
        }

        public void tick() {
            this.speedFactor = 1;

            if (EntityNaga.this.movement == EnumNagaMovement.GLIDING) {
                if (this.state == State.MOVE_TO) {
                    if (EntityNaga.this.horizontalCollision) {
                        EntityNaga naga = EntityNaga.this;
                        naga.setYaw(naga.getYaw() + 180.0F);
                        this.speedFactor = 0.1F;
                        EntityNaga.this.getNavigation().stop();
                    }

                    float orbitOffsetDiffX = (float) (EntityNaga.this.getNavigation().getTargetPos().getX() - EntityNaga.this.getX());
                    float orbitOffsetDiffY = (float) (EntityNaga.this.getNavigation().getTargetPos().getY() - EntityNaga.this.getY());
                    float orbitOffsetDiffZ = (float) (EntityNaga.this.getNavigation().getTargetPos().getZ() - EntityNaga.this.getZ());
                    double horizontalDistToOrbitOffset = MathHelper.sqrt(orbitOffsetDiffX * orbitOffsetDiffX + orbitOffsetDiffZ * orbitOffsetDiffZ);
                    double yFractionReduction = 1.0D - (double) MathHelper.abs(orbitOffsetDiffY * 0.7F) / horizontalDistToOrbitOffset;
                    orbitOffsetDiffX = (float) ((double) orbitOffsetDiffX * yFractionReduction);
                    orbitOffsetDiffZ = (float) ((double) orbitOffsetDiffZ * yFractionReduction);
                    horizontalDistToOrbitOffset = MathHelper.sqrt(orbitOffsetDiffX * orbitOffsetDiffX + orbitOffsetDiffZ * orbitOffsetDiffZ);
                    double distToOrbitOffset = MathHelper.sqrt(orbitOffsetDiffX * orbitOffsetDiffX + orbitOffsetDiffZ * orbitOffsetDiffZ + orbitOffsetDiffY * orbitOffsetDiffY);
                    float rotationYaw = EntityNaga.this.getYaw();
                    float desiredRotationYaw = (float) MathHelper.atan2(orbitOffsetDiffZ, orbitOffsetDiffX);
                    float rotationYawWrapped = MathHelper.wrapDegrees(EntityNaga.this.getYaw() + 90.0F);
                    float desiredRotationYawWrapped = MathHelper.wrapDegrees(desiredRotationYaw * 57.295776F);
                    EntityNaga.this.setYaw(MathHelper.stepUnwrappedAngleTowards(rotationYawWrapped, desiredRotationYawWrapped, 4.0F) - 90.0F);
                    float newBanking = MowzieMathUtil.approachDegreesSmooth(EntityNaga.this.getBanking(), EntityNaga.this.getPrevBanking(), EntityNaga.this.getYaw() - rotationYaw, 0.5f, 0.1f);
                    EntityNaga.this.setPrevBanking(EntityNaga.this.getBanking());
                    EntityNaga.this.setBanking(newBanking);
                    EntityNaga.this.bodyYaw = EntityNaga.this.getYaw();
//                if (MathHelper.degreesDifferenceAbs(rotationYaw, EntityNaga.this.rotationYaw) < 3.0F) {
//                    this.speedFactor = MathHelper.approach(this.speedFactor, 1.8F, 0.005F * (1.8F / this.speedFactor));
//                } else {
//                    this.speedFactor = MathHelper.approach(this.speedFactor, 0.2F, 0.025F);
//                }

                    float desiredPitch = (float) (-(MathHelper.atan2(-orbitOffsetDiffY, horizontalDistToOrbitOffset) * 57.2957763671875D));
                    EntityNaga.this.setPitch(MathHelper.stepUnwrappedAngleTowards(EntityNaga.this.getPitch(), desiredPitch, 8));
                    float rotationYaw1 = EntityNaga.this.getYaw() + 90.0F;
                    double xMotion = (double) (this.speedFactor * MathHelper.cos(rotationYaw1 * 0.017453292F)) * Math.abs((double) orbitOffsetDiffX / distToOrbitOffset);
                    double yMotion = (double) (this.speedFactor * MathHelper.sin(rotationYaw1 * 0.017453292F)) * Math.abs((double) orbitOffsetDiffZ / distToOrbitOffset);
                    double zMotion = (double) (this.speedFactor * MathHelper.sin(desiredPitch * 0.017453292F)) * Math.abs((double) orbitOffsetDiffY / distToOrbitOffset);
                    Vec3d motion = EntityNaga.this.getVelocity();
                    EntityNaga.this.setVelocity(motion.add((new Vec3d(xMotion, zMotion, yMotion)).subtract(motion).multiply(0.1D)));
                }
            } else if (EntityNaga.this.movement == EnumNagaMovement.HOVERING) {
                if (EntityNaga.this.getAnimation() == NO_ANIMATION || EntityNaga.this.getAnimation() == SPIT_ANIMATION) {
                    LivingEntity target = EntityNaga.this.getTarget();
                    if (target != null && EntityNaga.this.squaredDistanceTo(this.parentEntity) < 1600.0D) {
                        EntityNaga.this.lookAtEntity(target, 100, 100);
                    }

                    if (this.state == State.MOVE_TO) {
                        if (this.courseChangeCooldown-- <= 0) {
                            this.courseChangeCooldown += this.parentEntity.getRandom().nextInt(5) + 2;
                            Vec3d Vector3d = new Vec3d(this.targetX - this.parentEntity.getX(), this.targetY - this.parentEntity.getY(), this.targetZ - this.parentEntity.getZ());
                            double d0 = Vector3d.length();
                            Vector3d = Vector3d.normalize();
                            if (this.checkCollisions(Vector3d, MathHelper.ceil(d0))) {
                                this.parentEntity.setVelocity(this.parentEntity.getVelocity().add(Vector3d.multiply(0.1D)));
                            } else {
                                this.state = State.WAIT;
                            }
                        }
                    }

                }
            }
        }

        public boolean checkCollisions(Vec3d p_220673_1_, int p_220673_2_) {
            Box axisalignedbb = this.parentEntity.getBoundingBox();

            for (int i = 1; i < p_220673_2_; ++i) {
                axisalignedbb = axisalignedbb.offset(p_220673_1_);
                if (!this.parentEntity.getWorld().isSpaceEmpty(this.parentEntity, axisalignedbb)) {
                    return false;
                }
            }

            return true;
        }
    }

    class NagaLookController extends LookControl {
        public NagaLookController(MobEntity entity) {
            super(entity);
        }

        public void tick() {
            if (this.lookAtTimer > 0 && this.getTargetYaw().isPresent()) {
                --this.lookAtTimer;
                this.entity.headYaw = this.changeAngle(this.entity.headYaw, this.getTargetYaw().get(), this.maxYawChange);
            } else {
                this.entity.headYaw = this.changeAngle(this.entity.headYaw, this.entity.bodyYaw, 10.0F);
            }

            if (!this.entity.getNavigation().isIdle()) {
                this.entity.headYaw = MathHelper.clampAngle(this.entity.headYaw, this.entity.bodyYaw, (float) this.entity.getMaxHeadRotation());
            }
        }
    }
}
