package com.bobmowzie.mowziesmobs.server.entity.umvuthana;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.tools.MathUtils;
import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieAnimationController;
import com.bobmowzie.mowziesmobs.client.particle.ParticleHandler;
import com.bobmowzie.mowziesmobs.client.particle.util.*;
import com.bobmowzie.mowziesmobs.server.ability.Ability;
import com.bobmowzie.mowziesmobs.server.ability.AbilityHandler;
import com.bobmowzie.mowziesmobs.server.ability.AbilitySection;
import com.bobmowzie.mowziesmobs.server.ability.AbilityType;
import com.bobmowzie.mowziesmobs.server.ability.abilities.mob.BlockAbility;
import com.bobmowzie.mowziesmobs.server.ability.abilities.mob.DieAbility;
import com.bobmowzie.mowziesmobs.server.ability.abilities.mob.HurtAbility;
import com.bobmowzie.mowziesmobs.server.ability.abilities.mob.MeleeAttackAbility;
import com.bobmowzie.mowziesmobs.server.ability.abilities.player.SimpleAnimationAbility;
import com.bobmowzie.mowziesmobs.server.ai.EntityAIAvoidEntity;
import com.bobmowzie.mowziesmobs.server.ai.UseAbilityAI;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import com.bobmowzie.mowziesmobs.server.entity.MowzieGeckoEntity;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntitySunstrike;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import com.bobmowzie.mowziesmobs.server.item.ItemUmvuthanaMask;
import com.bobmowzie.mowziesmobs.server.item.UmvuthanaMask;
import com.bobmowzie.mowziesmobs.server.loot.LootTableHandler;
import com.bobmowzie.mowziesmobs.server.potion.EffectHandler;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.control.BodyControl;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;

import java.util.EnumSet;

public abstract class EntityUmvuthana extends MowzieGeckoEntity {
    public static final AbilityType<EntityUmvuthana, DieAbility<EntityUmvuthana>> DIE_ABILITY = new AbilityType<>("umvuthana_die", (type, entity) -> new DieAbility<>(type, entity, RawAnimation.begin().thenPlay("die"), 70) {
        @Override
        public void tickUsing() {
            super.tickUsing();
            if (this.getTicksInUse() == 1)
                this.getUser().playSound(MMSounds.ENTITY_UMVUTHANA_HURT, this.getUser().getSoundVolume(), this.getUser().getSoundPitch());
            if (this.getTicksInUse() == 15)
                this.getUser().playSound(MMSounds.ENTITY_UMVUTHANA_RETRACT, this.getUser().getSoundVolume(), 1);
        }
    });
    public static final AbilityType<EntityUmvuthana, UmvuthanaHurtAbility> HURT_ABILITY = new AbilityType<>("umvuthana_hurt", UmvuthanaHurtAbility::new);
    public static final AbilityType<EntityUmvuthana, UmvuthanaAttackAbility> ATTACK_ABILITY = new AbilityType<>("umvuthana_attack", UmvuthanaAttackAbility::new);
    public static final AbilityType<EntityUmvuthana, SimpleAnimationAbility<EntityUmvuthana>> ROAR_ABILITY = new AbilityType<>("umvuthana_roar", (type, entity) -> new SimpleAnimationAbility<>(type, entity, RawAnimation.begin().thenPlay("roar"), 35, true) {
        @Override
        public void tickUsing() {
            super.tickUsing();
            if (this.getTicksInUse() == 2)
                this.getUser().playSound(MMSounds.ENTITY_UMVUTHANA_ROAR, this.getUser().getSoundVolume() + 0.5f, this.getUser().getSoundPitch());
        }
    });
    public static final AbilityType<EntityUmvuthana, SimpleAnimationAbility<EntityUmvuthana>> ALERT_ABILITY = new AbilityType<>("umvuthana_alert", (type, entity) -> new SimpleAnimationAbility<>(type, entity, RawAnimation.begin().thenPlay("alert"), 15, true) {
        int soundFrame;

        @Override
        public void start() {
            super.start();
            this.soundFrame = this.rand.nextInt(7);
        }

        @Override
        public void tickUsing() {
            super.tickUsing();
            if (this.soundFrame == this.getTicksInUse())
                this.getUser().playSound(MMSounds.ENTITY_UMVUTHANA_ALERT, this.getUser().getSoundVolume(), this.getUser().getSoundPitch());
            if (this.getUser().getTarget() != null) {
//                this.getUser().lookAt(this.getUser().getTarget(), 30F, 30F);
                this.getUser().lookControl.lookAt(this.getUser().getTarget(), 30F, 30F);
            }
        }

        @Override
        public void end() {
            super.end();
            if (this.rand.nextFloat() < 0.2) this.getUser().sendAbilityMessage(ROAR_ABILITY);
        }
    });
    public static final AbilityType<EntityUmvuthana, SimpleAnimationAbility<EntityUmvuthana>> ACTIVATE_ABILITY = new AbilityType<>("umvuthana_activate", (type, entity) -> new SimpleAnimationAbility<>(type, entity, RawAnimation.begin().thenPlay("emerge"), 21) {
        @Override
        public void tickUsing() {
            super.tickUsing();
            if (this.getTicksInUse() == 5) this.getUser().playSound(MMSounds.ENTITY_UMVUTHANA_EMERGE, 1, 1);
            if (this.getTicksInUse() == 10) this.getUser().active = true;
        }
    });
    public static final AbilityType<EntityUmvuthana, BlockAbility<EntityUmvuthana>> BLOCK_ABILITY = new AbilityType<>("umvuthana_block", (type, entity) -> new BlockAbility<>(type, entity, RawAnimation.begin().thenPlay("block"), 10));
    public static final AbilityType<EntityUmvuthana, UmvuthanaBlockCounterAbility> BLOCK_COUNTER_ABILITY = new AbilityType<>("umvuthana_block_counter", UmvuthanaBlockCounterAbility::new);
    public static final AbilityType<EntityUmvuthana, UmvuthanaTeleportAbility> TELEPORT_ABILITY = new AbilityType<>("umvuthana_teleport", UmvuthanaTeleportAbility::new);
    public static final AbilityType<EntityUmvuthana, UmvuthanaHealAbility> HEAL_ABILITY = new AbilityType<>("umvuthana_heal", UmvuthanaHealAbility::new);
    private static final TrackedData<Boolean> DANCING = DataTracker.registerData(EntityUmvuthana.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Integer> MASK = DataTracker.registerData(EntityUmvuthana.class, TrackedDataHandlerRegistry.INTEGER);
    public static final AbilityType<EntityUmvuthana, SimpleAnimationAbility<EntityUmvuthana>> DEACTIVATE_ABILITY = new AbilityType<>("umvuthana_deactivate", (type, entity) -> new SimpleAnimationAbility<>(type, entity, RawAnimation.begin().thenPlay("retract"), 11) {
        @Override
        public void end() {
            super.end();
            this.getUser().discard();
            ItemUmvuthanaMask mask = getMaskFromType(this.getUser().getMaskType());
            if (!this.getUser().getWorld().isClient) {
                ItemEntity itemEntity = this.getUser().dropStack(this.getUser().getDeactivatedMask(mask), 1.5f);
                if (itemEntity != null) {
                    ItemStack item = itemEntity.getStack();
                    item.setDamage((int) Math.ceil((1.0f - this.getUser().getHealthRatio()) * item.getMaxDamage()));
                    item.setCustomName(this.getUser().getCustomName());
                }
            }
        }
    });
    private static final TrackedData<Integer> WEAPON = DataTracker.registerData(EntityUmvuthana.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Boolean> ACTIVE = DataTracker.registerData(EntityUmvuthana.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Float> HEALPOSX = DataTracker.registerData(EntityUmvuthana.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Float> HEALPOSY = DataTracker.registerData(EntityUmvuthana.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Float> HEALPOSZ = DataTracker.registerData(EntityUmvuthana.class, TrackedDataHandlerRegistry.FLOAT);
    private static final byte FOOTSTEP_ID = 69;
    private static final RawAnimation WALK_AGGRESSIVE_ANIM = RawAnimation.begin().thenLoop("walk_aggressive");
    private static final RawAnimation IDLE_AGGRESSIVE_ANIM = RawAnimation.begin().thenLoop("idle_aggressive");
    private static final RawAnimation WALK_NEUTRAL_ANIM = RawAnimation.begin().thenLoop("walk_neutral");
    private static final RawAnimation IDLE_NEUTRAL_ANIM = RawAnimation.begin().thenLoop("idle_neutral");
    private static final RawAnimation TUMBLE_ANIM = RawAnimation.begin().thenLoop("tumble");
    private static final RawAnimation INACTIVE_ANIM = RawAnimation.begin().thenLoop("inactive");
    private static final RawAnimation MASK_TWITCH_ANIM = RawAnimation.begin().thenLoop("mask_twitch");
    private static final RawAnimation RUN_SWITCH_ANIM = RawAnimation.begin().thenLoop("run_switch");
    private static final RawAnimation WALK_SWITCH_ANIM = RawAnimation.begin().thenLoop("walk_switch");
    public int timeUntilDeath = -1;
    @Environment(EnvType.CLIENT)
    public Vec3d[] staffPos;
    @Environment(EnvType.CLIENT)
    public Vec3d[] headPos;
    @Environment(EnvType.CLIENT)
    public Vec3d[] barakoPos;
    @Environment(EnvType.CLIENT)
    public Vec3d[] myPos;
    protected int circleTick = 0;
    protected boolean attacking = false;
    protected Vec3d teleportDestination;
    protected MowzieAnimationController<MowzieGeckoEntity> walkRunController = new MowzieAnimationController<>(this, "walk_run_controller", 4, this::predicateWalkRun, 0);
    int maskTimingOffset = this.random.nextBetweenExclusive(0, 150);
    protected AnimationController<MowzieGeckoEntity> maskController = new MowzieAnimationController<>(this, "mask_controller", 1, this::predicateMask, this.maskTimingOffset);

    private boolean circleDirection = true;
    private int ticksWithoutTarget;
    private int blockCount = 0;
    private int footstepCounter = 0;
    private float prevMaskRot = 0;
    private boolean rattling = false;

    public EntityUmvuthana(EntityType<? extends EntityUmvuthana> type, World world) {
        super(type, world);
        this.setMask(MaskType.from(MathHelper.nextInt(this.random, 1, 4)));
        this.setStepHeight(1);
        this.circleTick += this.random.nextInt(200);
        this.frame += this.random.nextInt(50);
        this.experiencePoints = 6;
        this.active = false;

        if (world.isClient) {
            this.staffPos = new Vec3d[]{new Vec3d(0, 0, 0)};
            this.barakoPos = new Vec3d[]{new Vec3d(0, 0, 0)};
            this.myPos = new Vec3d[]{new Vec3d(0, 0, 0)};
            this.headPos = new Vec3d[]{new Vec3d(0, 0, 0)};
        }
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MowzieEntity.createAttributes().add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 3)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 8);
    }

    public static ItemUmvuthanaMask getMaskFromType(MaskType maskType) {
        return switch (maskType) {
            case BLISS -> ItemHandler.UMVUTHANA_MASK_BLISS;
            case FEAR -> ItemHandler.UMVUTHANA_MASK_FEAR;
            case FURY -> ItemHandler.UMVUTHANA_MASK_FURY;
            case MISERY -> ItemHandler.UMVUTHANA_MASK_MISERY;
            case RAGE -> ItemHandler.UMVUTHANA_MASK_RAGE;
            case FAITH -> ItemHandler.UMVUTHANA_MASK_FAITH;
        };
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.setPathfindingPenalty(PathNodeType.DAMAGE_FIRE, -8);
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(0, new UseAbilityAI<>(this, ACTIVATE_ABILITY));
        this.goalSelector.add(0, new UseAbilityAI<>(this, DEACTIVATE_ABILITY));
        this.goalSelector.add(1, new UseAbilityAI<>(this, DIE_ABILITY));
        this.goalSelector.add(2, new UseAbilityAI<>(this, HURT_ABILITY, false));
        this.goalSelector.add(3, new EntityAIAvoidEntity<>(this, EntitySunstrike.class, EntitySunstrike::isStriking, 3, 0.7F));
        this.goalSelector.add(2, new UseAbilityAI<>(this, ATTACK_ABILITY));
        this.goalSelector.add(2, new UseAbilityAI<>(this, ALERT_ABILITY));
        this.goalSelector.add(2, new UseAbilityAI<>(this, ROAR_ABILITY));
        this.goalSelector.add(2, new UseAbilityAI<>(this, BLOCK_ABILITY));
        this.goalSelector.add(2, new UseAbilityAI<>(this, BLOCK_COUNTER_ABILITY));
        this.goalSelector.add(2, new UseAbilityAI<>(this, TELEPORT_ABILITY));
        this.goalSelector.add(2, new UseAbilityAI<>(this, HEAL_ABILITY, false));
        this.goalSelector.add(7, new WanderAroundFarGoal(this, 0.4));
        this.goalSelector.add(8, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(8, new LookAtEntityGoal(this, EntityUmvuthana.class, 8.0F));
        this.goalSelector.add(8, new LookAtEntityGoal(this, EntityUmvuthi.class, 8.0F));
        this.goalSelector.add(8, new LookAroundGoal(this));
        this.goalSelector.add(5, new CircleAttackGoal(this, 6.5F));
        this.registerTargetGoals();
    }

    protected void registerTargetGoals() {

    }

    protected void registerHuntingTargetGoals() {
        this.targetSelector.add(5, new ActiveTargetGoal<>(this, AnimalEntity.class, 200, true, false, target -> {
            float volume = target.getWidth() * target.getWidth() * target.getHeight();
            return (target.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE) == null || target.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE) < 3.0D) && volume > 0.1 && volume < 6;
        }));
        this.targetSelector.add(5, new ActiveTargetGoal<>(this, ZombieEntity.class, 0, true, false, (e) -> !(e instanceof ZombifiedPiglinEntity)));
        this.targetSelector.add(5, new ActiveTargetGoal<>(this, AbstractSkeletonEntity.class, 0, true, false, null));
        this.targetSelector.add(5, new ActiveTargetGoal<>(this, ZoglinEntity.class, 0, true, false, null));
        this.targetSelector.add(6, new FleeEntityGoal<>(this, CreeperEntity.class, 6.0F, 1.0D, 1.2D));
        this.targetSelector.add(4, new ActiveTargetGoal<>(this, PlayerEntity.class, 0, true, true, target -> {
            if (target instanceof PlayerEntity) {
                if (this.getWorld().getDifficulty() == Difficulty.PEACEFUL) return false;
                ItemStack headArmorStack = ((PlayerEntity) target).getInventory().armor.get(3);
                return !(headArmorStack.getItem() instanceof UmvuthanaMask);
            }
            return true;
        }));
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        super.registerControllers(controllers);
        controllers.add(this.maskController);
        controllers.add(this.walkRunController);
    }

    protected <E extends GeoEntity> PlayState predicateMask(AnimationState<E> event) {
        if (this.isAlive() && this.active && this.getActiveAbilityType() != HEAL_ABILITY) {
            event.getController().setAnimation(MASK_TWITCH_ANIM);
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    protected <E extends GeoEntity> PlayState predicateWalkRun(AnimationState<E> event) {
        float threshold = 0.9f;
        AnimationProcessor.QueuedAnimation currentAnim = event.getController().getCurrentAnimation();
        if (currentAnim != null && currentAnim.animation().name().equals("run_switch")) {
            threshold = 0.7f;
        }

        if (event.getLimbSwingAmount() > threshold && !this.isStrafing()) {
            event.getController().setAnimation(RUN_SWITCH_ANIM);
        } else {
            event.getController().setAnimation(WALK_SWITCH_ANIM);
        }
        return PlayState.CONTINUE;
    }

    @Override
    protected <E extends GeoEntity> void loopingAnimations(AnimationState<E> event) {
        if (this.active) {
            if (this.isAttacking()) {
                event.getController().transitionLength(4);
                if (event.isMoving()) {
                    event.getController().setAnimation(WALK_AGGRESSIVE_ANIM);
                } else {
                    event.getController().setAnimation(IDLE_AGGRESSIVE_ANIM);
                }
            } else {
                event.getController().transitionLength(4);
                if (event.isMoving()) {
                    event.getController().setAnimation(WALK_NEUTRAL_ANIM);
                } else {
                    event.getController().setAnimation(IDLE_NEUTRAL_ANIM);
                }
            }
        } else {
            event.getController().transitionLength(0);
            if (!this.isOnGround() && !this.isInLava() && !this.isTouchingWater()) {
                event.getController().setAnimation(TUMBLE_ANIM);
            } else {
                event.getController().setAnimation(INACTIVE_ANIM);
            }
        }
    }

    @Override
    protected BodyControl createBodyControl() {
//        return new SmartBodyHelper(this);
        return super.createBodyControl();
    }

    @Override
    public AbilityType getDeathAbility() {
        return DIE_ABILITY;
    }

    @Override
    public AbilityType getHurtAbility() {
        return HURT_ABILITY;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        if (this.getActiveAbilityType() == DEACTIVATE_ABILITY) {
            return null;
        }
        int i = MathHelper.nextInt(this.random, 0, MMSounds.ENTITY_UMVUTHANA_IDLE.size());
        if (i < MMSounds.ENTITY_UMVUTHANA_IDLE.size()) {
            return MMSounds.ENTITY_UMVUTHANA_IDLE.get(i);
        }
        return null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return this.active ? MMSounds.ENTITY_UMVUTHANA_HURT : null;
    }

    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason reason, EntityData livingData, NbtCompound compound) {
        if (this.canHoldVaryingWeapons()) {
            this.setWeapon(this.random.nextInt(3) == 0 ? 1 : 0);
        }
        if (reason == SpawnReason.COMMAND && !(this instanceof EntityUmvuthanaRaptor) && !(this instanceof EntityUmvuthanaCrane) && !(this instanceof EntityUmvuthanaCraneToPlayer))
            this.setMask(MaskType.from(MathHelper.nextInt(this.random, 1, 4)));
        return super.initialize(world, difficulty, reason, livingData, compound);
    }

    protected boolean canHoldVaryingWeapons() {
        return true;
    }

    protected Vec3d updateCirclingPosition(float radius, float speed) {
        LivingEntity target = this.getTarget();
        if (target != null) {
            if (this.random.nextInt(200) == 0) {
                this.circleDirection = !this.circleDirection;
            }
            if (this.circleDirection) {
                this.circleTick++;
            } else {
                this.circleTick--;
            }
            return this.circleEntityPosition(target, radius, speed, true, this.circleTick, 0);
        }
        return null;
    }

    @Override
    public void tick() {
        super.tick();

        if (this.getWorld().isClient()) {
            if (this.deathTime < 20 && this.active && !(this.getActiveAbilityType() == TELEPORT_ABILITY && this.getActiveAbility().getCurrentSection().sectionType != AbilitySection.AbilitySectionType.RECOVERY)) {
                if (this.age % 10 == 1) {
                    AdvancedParticleBase.spawnParticle(this.getWorld(), ParticleHandler.GLOW, this.getX(), this.getY(), this.getZ(), 0, 0, 0, true, 0, 0, 0, 0, 0F, 1, 1, 0.3, 0.4, 1, 9, true, false, new ParticleComponent[]{
                            new ParticleComponent.PinLocation(this.headPos),
                            new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.SCALE, ParticleComponent.KeyTrack.oscillate(9, 10, 12), false)
                    });
                }
                if (this.headPos != null && this.headPos.length > 0 && this.headPos[0] != null) {
                    if (this.random.nextFloat() < 0.3F) {
                        int amount = this.random.nextInt(2) + 1;
                        while (amount-- > 0) {
                            float theta = this.random.nextFloat() * MathUtils.TAU;
                            float r = this.random.nextFloat() * 0.4F;
                            float x = r * MathHelper.cos(theta);
                            float z = r * MathHelper.sin(theta);
                            this.getWorld().addParticle(ParticleTypes.SMOKE, this.headPos[0].getX() + x, this.headPos[0].getY() + 0.1, this.headPos[0].getZ() + z, 0, 0, 0);
                        }
                    }
                }
            }
        }

        if (this.getTarget() != null) {
            if (this.getTarget().isRemoved() || this.getTarget().isDead()) {
                this.setTarget(null);
            }
        }

        if (this.getActiveAbilityType() != BLOCK_ABILITY && this.blockCount > 0 && this.age % 10 == 0)
            this.blockCount--;

        if (!this.getWorld().isClient && this.active && !this.getActive()) {
            this.setActive(true);
        }
        this.active = this.getActive();
        if (!this.active) {
            this.getNavigation().stop();
            this.setYaw(this.prevYaw);
            this.bodyYaw = this.getYaw();
            if ((this.isOnGround() || this.isTouchingWater() || this.isInLava()) && this.getActiveAbility() == null) {
                this.sendAbilityMessage(ACTIVATE_ABILITY);
            }
            return;
        }
        if (this.getActiveAbility() != null) {
            this.getNavigation().stop();
            this.headYaw = this.bodyYaw = this.getYaw();
        }

        if (this.getTarget() != null && this.ticksWithoutTarget > 3) {
            this.sendAbilityMessage(ALERT_ABILITY);
        }

        if (this.getTarget() == null) {
            this.ticksWithoutTarget++;
        } else {
            this.ticksWithoutTarget = 0;
        }

        if (this.timeUntilDeath > 0) this.timeUntilDeath--;
        else if (this.timeUntilDeath == 0) {
            this.damage(this.getDamageSources().indirectMagic(this, null), this.getHealth() + 1);
        }

//        if (getActiveAbility() == null) AbilityHandler.INSTANCE.sendAbilityMessage(this, BLOCK_COUNTER_ABILITY);
    }

    public void updateRattleSound(float maskRot) {
        if (!this.rattling) {
            if (Math.abs(maskRot - this.prevMaskRot) > 0.06) {
                this.getWorld().playSound(this.getX(), this.getY(), this.getZ(), MMSounds.ENTITY_UMVUTHANA_RATTLE, SoundCategory.HOSTILE, 0.03f, this.getSoundPitch(), false);
            }
        } else {
            if (Math.abs(maskRot - this.prevMaskRot) < 0.00000001) {
                this.rattling = false;
            }
        }
        this.prevMaskRot = maskRot;
    }

    @Override
    protected float calculateNextStepSoundDistance() {
        if (this.isOnGround()) this.getWorld().sendEntityStatus(this, FOOTSTEP_ID);
        return super.calculateNextStepSoundDistance();
    }

    @Override
    public void handleStatus(byte id) {
        if (id == FOOTSTEP_ID && ConfigHandler.CLIENT.umvuthanaFootprints) {
            this.footstepCounter++;
            double rotation = Math.toRadians(this.bodyYaw + 180f);
            Vec3d offset = new Vec3d(0, 0, this.footstepCounter % 2 == 0 ? 0.3 : -0.3).rotateY((float) rotation);
            World world = this.getWorld();
            int spriteSize = 8;
            int bufferSize = 32;
            ParticleComponent[] components = new ParticleComponent[]{
                    new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.RED, new ParticleComponent.KeyTrack(
                            new float[]{0.995f, 0.05f},
                            new float[]{0, 0.3f}
                    ), false),
                    new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.GREEN, new ParticleComponent.KeyTrack(
                            new float[]{0.95f, 0.05f},
                            new float[]{0, 0.3f}
                    ), false),
                    new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.BLUE, new ParticleComponent.KeyTrack(
                            new float[]{0.1f, 0.05f},
                            new float[]{0, 0.3f}
                    ), false),
                    new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.ALPHA, new ParticleComponent.KeyTrack(
                            new float[]{1f, 0.8f},
                            new float[]{0, 0.3f}
                    ), false),
                    new ParticleComponent() {
                        @Override
                        public void postUpdate(AdvancedParticleBase particle) {
                            super.postUpdate(particle);
                            if (particle.getAge() < 80 && EntityUmvuthana.this.random.nextFloat() < 0.3F) {
                                int amount = 1;
                                while (amount-- > 0) {
                                    float theta = EntityUmvuthana.this.random.nextFloat() * MathUtils.TAU;
                                    float r = EntityUmvuthana.this.random.nextFloat() * 0.2F;
                                    float x = r * MathHelper.cos(theta);
                                    float z = r * MathHelper.sin(theta);
                                    EntityUmvuthana.this.getWorld().addParticle(ParticleTypes.SMOKE, particle.getPosX() + x, particle.getPosY() + 0.05, particle.getPosZ() + z, 0, 0, 0);
                                }
                            }
                        }
                    }
            };
            world.addParticle(new DecalParticleData(ParticleHandler.STRIX_FOOTPRINT, rotation, 1F, 1, 0.95, 0.1, 1, 1, 200, true, spriteSize, bufferSize, components), this.getX() + offset.getX(), this.getY() + 0.01, this.getZ() + offset.getZ(), 0, 0, 0);
        } else super.handleStatus(id);
    }

    protected ItemStack getDeactivatedMask(ItemUmvuthanaMask mask) {
        return new ItemStack(mask);
    }

    @Override
    protected SoundEvent getDeathSound() {
        this.playSound(MMSounds.ENTITY_UMVUTHANA_DIE, 1f, 0.95f + this.random.nextFloat() * 0.1f);
        return null;
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.getDataTracker().startTracking(DANCING, false);
        this.getDataTracker().startTracking(MASK, 0);
        this.getDataTracker().startTracking(WEAPON, 0);
        this.getDataTracker().startTracking(ACTIVE, true);
        this.getDataTracker().startTracking(HEALPOSX, 0f);
        this.getDataTracker().startTracking(HEALPOSY, 0f);
        this.getDataTracker().startTracking(HEALPOSZ, 0f);
    }

    public boolean getDancing() {
        return this.getDataTracker().get(DANCING);
    }

    public void setDancing(boolean dancing) {
        this.getDataTracker().set(DANCING, dancing);
    }

    public MaskType getMaskType() {
        return MaskType.from(this.getDataTracker().get(MASK));
    }

    public void setMask(MaskType type) {
        this.getDataTracker().set(MASK, type.ordinal());
        this.equipStack(EquipmentSlot.HEAD, getMaskFromType(type).getDefaultStack());
        this.setEquipmentDropChance(EquipmentSlot.HEAD, 0);
    }

    public int getWeapon() {
        return this.getDataTracker().get(WEAPON);
    }

    public void setWeapon(int type) {
        this.getDataTracker().set(WEAPON, type);
    }

    public boolean getActive() {
        return this.getDataTracker().get(ACTIVE);
    }

    public void setActive(boolean active) {
        this.getDataTracker().set(ACTIVE, active);
    }

    public Vec3d getHealPos() {
        return new Vec3d(this.getDataTracker().get(HEALPOSX), this.getDataTracker().get(HEALPOSY), this.getDataTracker().get(HEALPOSZ));
    }

    public void setHealPos(Vec3d vec) {
        this.getDataTracker().set(HEALPOSX, (float) vec.x);
        this.getDataTracker().set(HEALPOSY, (float) vec.y);
        this.getDataTracker().set(HEALPOSZ, (float) vec.z);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound compound) {
        super.writeCustomDataToNbt(compound);
        compound.putInt("mask", this.getMaskType().ordinal());
        compound.putInt("weapon", this.getWeapon());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound compound) {
        super.readCustomDataFromNbt(compound);
        this.setMask(MaskType.from(compound.getInt("mask")));
        this.setWeapon(compound.getInt("weapon"));
    }

    @Override
    public boolean damage(DamageSource source, float damage) {
        if (this.getActiveAbilityType() == DEACTIVATE_ABILITY) {
            return false;
        }
        Entity entity = source.getAttacker();
        if (source == this.getDamageSources().hotFloor()) return false;
        boolean angleFlag = true;
        if (entity != null) {
            int arc = 220;
            Vec3d entityPos = entity.getPos();
            float entityHitAngle = (float) ((Math.atan2(entityPos.getZ() - this.getZ(), entityPos.getX() - this.getX()) * (180 / Math.PI) - 90) % 360);
            float entityAttackingAngle = this.bodyYaw % 360;
            if (entityHitAngle < 0) {
                entityHitAngle += 360;
            }
            if (entityAttackingAngle < 0) {
                entityAttackingAngle += 360;
            }
            float entityRelativeAngle = entityHitAngle - entityAttackingAngle;
            angleFlag = (entityRelativeAngle <= arc / 2.0 && entityRelativeAngle >= -arc / 2.0) || (entityRelativeAngle >= 360 - arc / 2.0 || entityRelativeAngle <= -arc + 90 / 2.0);
        }
        if (angleFlag && this.getMaskType().canBlock && entity instanceof LivingEntity && (this.getActiveAbility() == null || this.getActiveAbilityType() == HURT_ABILITY || this.getActiveAbilityType() == BLOCK_ABILITY) && !source.isIn(DamageTypeTags.BYPASSES_ARMOR)) {
            this.blockingEntity = (LivingEntity) entity;
            this.playSound(MMSounds.ENTITY_WROUGHT_UNDAMAGED, 0.4F, 2);
            if (this.blockingEntity == this.getTarget() && this.random.nextFloat() < MathHelper.clamp(this.blockCount / 5.0, 0.0, 1.0) && this.distanceTo(this.blockingEntity) < 4) {
                AbilityHandler.INSTANCE.sendAbilityMessage(this, BLOCK_COUNTER_ABILITY);
                this.blockCount = 0;
            } else {
                AbilityHandler.INSTANCE.sendAbilityMessage(this, BLOCK_ABILITY);
                this.blockCount++;
            }
            return false;
        }
        return super.damage(source, damage);
    }

    @Override
    protected Identifier getLootTableId() {
        switch (this.getMaskType()) {
            case BLISS:
                return LootTableHandler.UMVUTHANA_BLISS;
            case FEAR:
                return LootTableHandler.UMVUTHANA_FEAR;
            case FURY:
                return LootTableHandler.UMVUTHANA_FURY;
            case MISERY:
                return LootTableHandler.UMVUTHANA_MISERY;
            case RAGE:
                return LootTableHandler.UMVUTHANA_RAGE;
            case FAITH:
                return LootTableHandler.UMVUTHANA_FAITH;
        }
        return LootTableHandler.UMVUTHANA_FURY;
    }

    @Override
    public boolean canHit() {
        return this.active;
    }

    @Override
    public boolean handleFallDamage(float distance, float damageMultipler, DamageSource source) {
        if (this.active) {
            return super.handleFallDamage(distance, damageMultipler, source);
        }
        return false;
    }

    @Override
    protected ConfigHandler.CombatConfig getCombatConfig() {
        return ConfigHandler.COMMON.MOBS.UMVUTHANA.combatConfig;
    }

    public boolean isUmvuthiDevoted() {
        return true;
    }

    public int randomizeWeapon() {
        return this.random.nextInt(3) == 0 ? 1 : 0;
    }

    public boolean canHeal(LivingEntity entity) {
        return false;
    }

    protected void sunBlockTarget() {

    }

    @Override
    public AbilityType<?, ?>[] getAbilities() {
        return new AbilityType[]{DIE_ABILITY, HURT_ABILITY, ATTACK_ABILITY, ALERT_ABILITY, ROAR_ABILITY, ACTIVATE_ABILITY, DEACTIVATE_ABILITY, BLOCK_ABILITY, BLOCK_COUNTER_ABILITY, TELEPORT_ABILITY, HEAL_ABILITY};
    }

    protected static class CircleAttackGoal extends Goal {
        private final EntityUmvuthana mob;
        private final float attackRadius;
        protected boolean attacking = false;
        private int strafingLeftRightMul;
        private int strafingFrontBackMul;
        private boolean chasing = false;
        private int timeSinceAttack = 0;

        public CircleAttackGoal(EntityUmvuthana mob, float attackRadius) {
            this.mob = mob;
            this.attackRadius = attackRadius;
            this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
        }

        public boolean canStart() {
            return this.mob.getTarget() != null;
        }

        public boolean shouldContinue() {
            return (this.canStart() || !this.mob.getNavigation().isIdle());
        }

        public void start() {
            super.start();
            this.mob.setAttacking(true);
            this.timeSinceAttack = this.mob.random.nextInt(80);
        }

        public void stop() {
            super.stop();
            this.mob.setAttacking(false);
            this.mob.setStrafing(false);
            this.mob.getMoveControl().strafeTo(0, 0);
            this.attacking = false;
        }

        public boolean shouldRunEveryTick() {
            return true;
        }

        public void tick() {
            LivingEntity target = this.mob.getTarget();
            if (target != null) {
                if (this.timeSinceAttack < 80) {
                    this.timeSinceAttack++;
                }

                double distToTarget = this.mob.distanceTo(target);

                float frontBackDistBuffer = 2f;
                float leftRightDistBuffer = 1.5f;
                if (this.chasing && distToTarget <= this.attackRadius) {
                    this.chasing = false;
                }
                if (!this.chasing && distToTarget >= this.attackRadius + frontBackDistBuffer) {
                    this.chasing = true;
                }

                // Chasing
                if (this.chasing) {
                    this.mob.getNavigation().startMovingTo(target, 0.6);
                    this.mob.getLookControl().lookAt(target, 30.0F, 30.0F);
                    this.mob.setStrafing(false);
                    this.mob.getMoveControl().strafeTo(0, 0);
                } else {
                    // In range
                    if (!this.attacking && this.mob.getActiveAbility() == null) {
                        this.mob.getNavigation().stop();
                        float strafeSpeed = 0.5f;
                        Vec3d circlePos = this.mob.updateCirclingPosition(this.attackRadius, strafeSpeed - 0.2f);
                        double distToCirclePos = this.mob.getPos().distanceTo(circlePos);

                        if (distToCirclePos <= leftRightDistBuffer) {
                            this.mob.setStrafing(true);

                            if (distToTarget > this.attackRadius + 0.5) {
                                this.strafingFrontBackMul = 1;
                            } else if (distToTarget < this.attackRadius - 0.5) {
                                this.strafingFrontBackMul = -1;
                            } else {
                                this.strafingFrontBackMul = 0;
                            }

                            Vec3d toTarget = target.getPos().subtract(this.mob.getPos()).multiply(1, 0, 1).normalize();
                            Vec3d toCirclePos = circlePos.subtract(this.mob.getPos()).multiply(1, 0, 1).normalize();
                            Vec3d cross = toTarget.crossProduct(toCirclePos);
                            if (cross.y > 0) this.strafingLeftRightMul = 1;
                            else if (cross.y < 0) this.strafingLeftRightMul = -1;
                            else this.strafingLeftRightMul = 0;

                            float distScale = (float) Math.min(Math.pow(distToCirclePos * 1f / leftRightDistBuffer, 0.7), 1.0);

                            this.mob.getMoveControl().strafeTo(this.strafingFrontBackMul * strafeSpeed, this.strafingLeftRightMul * strafeSpeed * distScale);
                            this.mob.lookAtEntity(target, 30.0F, 30.0F);

                            if (this.mob.random.nextFloat() < 0.002) {
                                this.mob.sendAbilityMessage(ROAR_ABILITY);
                            }
                        } else {
                            this.mob.setStrafing(false);
                            this.mob.getMoveControl().strafeTo(0, 0);
                            this.mob.getNavigation().startMovingTo(circlePos.x, circlePos.y, circlePos.z, 0.53);
                            this.mob.getLookControl().lookAt(target, 30.0F, 30.0F);
                        }
                    } else {
                        this.mob.getMoveControl().strafeTo(0, 0);
                        this.mob.setStrafing(false);
                    }

                    // Attacking logic
                    if (this.mob.random.nextInt(80) == 0 && this.timeSinceAttack >= 80 && this.mob.getVisibilityCache().canSee(target)) {
                        this.attacking = true;
                    }
                    if (this.attacking && this.mob.getActiveAbility() == null) {
                        this.mob.getNavigation().startMovingTo(target, 0.5);
                        if (distToTarget <= 3.75 && this.mob.getVisibilityCache().canSee(target)) {
                            this.attacking = false;
                            this.timeSinceAttack = 0;
                            AbilityHandler.INSTANCE.sendAbilityMessage(this.mob, ATTACK_ABILITY);
                        }
                    }
                }
            }
        }
    }

    private static class UmvuthanaAttackAbility extends MeleeAttackAbility<EntityUmvuthana> {

        public UmvuthanaAttackAbility(AbilityType<EntityUmvuthana, ? extends MeleeAttackAbility<EntityUmvuthana>> abilityType, EntityUmvuthana user) {
            super(abilityType, user, new RawAnimation[]{RawAnimation.begin().thenPlay("attack_slash_left"), RawAnimation.begin().thenPlay("attack_slash_right")}, null, null, 1, 3.0f, 1, 13, 9, true);
        }

        @Override
        public void tickUsing() {
            super.tickUsing();
            if (this.getTicksInUse() == 5)
                this.getUser().setVelocity(this.getUser().getVelocity().add(this.getUser().getRotationVecClient().normalize().multiply(0.5)));
            if (this.getTicksInUse() == 1) {
                int i = this.rand.nextInt(MMSounds.ENTITY_UMVUTHANA_ATTACK.size());
                this.getUser().playSound(MMSounds.ENTITY_UMVUTHANA_ATTACK.get(i), 1, this.rand.nextFloat(0.9f, 1.1f));
            }
        }
    }

    private static class UmvuthanaBlockCounterAbility extends MeleeAttackAbility<EntityUmvuthana> {

        public UmvuthanaBlockCounterAbility(AbilityType<EntityUmvuthana, UmvuthanaBlockCounterAbility> abilityType, EntityUmvuthana user) {
            super(abilityType, user, new RawAnimation[]{RawAnimation.begin().thenPlay("block_counter")}, null, null, 3, 2.2f, 1.2f, 7, 11, false);
        }

        @Override
        public void start() {
            super.start();
            this.getUser().setInvulnerable(true);
        }

        @Override
        public void end() {
            super.end();
            this.getUser().setInvulnerable(false);
        }

        @Override
        public void tickUsing() {
            super.tickUsing();
            if (this.getTicksInUse() == 5) {
                float distToTarget = 1.0f;
                if (this.getUser().getTarget() != null)
                    distToTarget = MathHelper.clamp(this.getUser().distanceTo(this.getUser().getTarget()) / 2.0f - 1.0f, 0.0f, 1.0f);
                this.getUser().setVelocity(this.getUser().getVelocity().add(this.getUser().getRotationVecClient().normalize().multiply(1.6 * distToTarget)));
            }
            if (this.getTicksInUse() == 0) {
                this.getUser().playSound(MMSounds.ENTITY_UMVUTHANA_ATTACK_BIG, 1, this.rand.nextFloat(0.9f, 1.1f));
            }
        }

        @Override
        public boolean canCancelActiveAbility() {
            return super.canCancelActiveAbility() || this.getUser().getActiveAbility() instanceof BlockAbility<?> || this.getUser().getActiveAbility() instanceof HurtAbility<?>;
        }
    }

    private static class UmvuthanaHurtAbility extends HurtAbility<EntityUmvuthana> {

        private static final RawAnimation HURT_RIGHT_AGGRESSIVE_ANIM = RawAnimation.begin().thenPlay("hurt_right_aggressive");
        private static final RawAnimation HURT_LEFT_AGGRESSIVE_ANIM = RawAnimation.begin().thenPlay("hurt_left_aggressive");
        private static final RawAnimation HURT_RIGHT_NEUTRAL_ANIM = RawAnimation.begin().thenPlay("hurt_right_neutral");
        private static final RawAnimation HURT_LEFT_NEUTRAL_ANIM = RawAnimation.begin().thenPlay("hurt_left_neutral");

        public UmvuthanaHurtAbility(AbilityType<EntityUmvuthana, UmvuthanaHurtAbility> abilityType, EntityUmvuthana user) {
            super(abilityType, user, RawAnimation.begin(), 12);
        }

        @Override
        public RawAnimation getAnimation() {
            if (this.getUser().isAttacking()) {
                if (this.getUser().random.nextBoolean()) {
                    return HURT_RIGHT_AGGRESSIVE_ANIM;
                } else {
                    return HURT_LEFT_AGGRESSIVE_ANIM;
                }
            } else {
                if (this.getUser().random.nextBoolean()) {
                    return HURT_RIGHT_NEUTRAL_ANIM;
                } else {
                    return HURT_LEFT_NEUTRAL_ANIM;
                }
            }
        }
    }

    private static class UmvuthanaTeleportAbility extends Ability<EntityUmvuthana> {
        private static final RawAnimation TELEPORT_START_ANIM = RawAnimation.begin().then("teleport_start", Animation.LoopType.PLAY_ONCE);
        private static final RawAnimation TELEPORT_LOOP_ANIM = RawAnimation.begin().thenLoop("teleport_loop");
        private static final RawAnimation TELEPORT_END_ANIM = RawAnimation.begin().then("teleport_end", Animation.LoopType.PLAY_ONCE);
        private static final int ACTIVE_DURATION = 7;
        private Vec3d teleportStart;

        public UmvuthanaTeleportAbility(AbilityType<EntityUmvuthana, ? extends Ability> abilityType, EntityUmvuthana user) {
            super(abilityType, user, new AbilitySection[]{
                    new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.STARTUP, 7),
                    new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.ACTIVE, ACTIVE_DURATION),
                    new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.RECOVERY, 13)
            });
        }

        @Override
        protected void beginSection(AbilitySection section) {
            super.beginSection(section);
            if (section.sectionType == AbilitySection.AbilitySectionType.STARTUP) {
                this.playAnimation(TELEPORT_START_ANIM);
            }
            if (section.sectionType == AbilitySection.AbilitySectionType.ACTIVE) {
                this.teleportStart = this.getUser().getPos();
                this.playAnimation(TELEPORT_LOOP_ANIM);
            }
            if (section.sectionType == AbilitySection.AbilitySectionType.RECOVERY) {
                this.playAnimation(TELEPORT_END_ANIM);
            }
        }

        @Override
        protected void endSection(AbilitySection section) {
            super.endSection(section);
            if (section.sectionType == AbilitySection.AbilitySectionType.ACTIVE && this.getUser().teleportDestination != null) {
                this.getUser().requestTeleport(this.getUser().teleportDestination.getX(), this.getUser().teleportDestination.getY(), this.getUser().teleportDestination.getZ());
                this.getUser().setVelocity(0, 0, 0);
                this.getUser().getNavigation().stop();
            }
        }

        @Override
        public void tickUsing() {
            super.tickUsing();
            if (this.getTicksInUse() == 2)
                this.getUser().playSound(MMSounds.ENTITY_UMVUTHANA_TELEPORT.get(this.rand.nextInt(3)), 3f, 1);
            if (this.getTicksInUse() == 16)
                this.getUser().playSound(MMSounds.ENTITY_UMVUTHANA_TELEPORT.get(this.rand.nextInt(3)), 3f, 1.2f);

            if (this.getCurrentSection().sectionType == AbilitySection.AbilitySectionType.ACTIVE) {
                if (this.teleportStart != null && this.getUser().teleportDestination != null) {
                    float t = this.getTicksInSection() / (float) (ACTIVE_DURATION);
                    t = (float) (0.5 - 0.5 * Math.cos(t * Math.PI));
                    Vec3d newPos = this.teleportStart.add(this.getUser().teleportDestination.subtract(this.teleportStart).multiply(t));
                    this.getUser().requestTeleport(newPos.getX(), newPos.getY(), newPos.getZ());
                    this.getUser().getNavigation().stop();
                }
            }

            if (this.getUser().getTarget() != null)
                this.getUser().getLookControl().lookAt(this.getUser().getTarget(), 30, 30);

            if (this.getUser().getWorld().isClient) {
                this.getUser().myPos[0] = this.getUser().getPos().add(0, 1.2f, 0);
                if (this.getTicksInUse() == 5) {
                    ParticleComponent.KeyTrack keyTrack1 = ParticleComponent.KeyTrack.oscillate(0, 2, 24);
                    ParticleComponent.KeyTrack keyTrack2 = new ParticleComponent.KeyTrack(new float[]{0, 18, 18, 0}, new float[]{0, 0.2f, 0.8f, 1});
                    AdvancedParticleBase.spawnParticle(this.getUser().getWorld(), ParticleHandler.SUN, this.getUser().getX(), this.getUser().getY(), this.getUser().getZ(), 0, 0, 0, true, 0, 0, 0, 0, 0F, 1, 1, 1, 1, 1, 15, true, false, new ParticleComponent[]{
                            new ParticleComponent.PinLocation(this.getUser().myPos),
                            new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.SCALE, keyTrack2, false),
                            new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.SCALE, keyTrack1, true),
                            new RibbonComponent(ParticleHandler.RIBBON_FLAT, 10, 0, 0, 0, 0.12F, 0.95, 0.9, 0.35, 0.75, true, true, new ParticleComponent[]{
                                    new RibbonComponent.PropertyOverLength(RibbonComponent.PropertyOverLength.EnumRibbonProperty.SCALE, ParticleComponent.KeyTrack.startAndEnd(1, 0))
                            }),
                    });
                }
                this.getUser().myPos[0] = this.getUser().getPos().add(0, 1.2f, 0);
                if (this.getTicksInUse() == 4 || this.getTicksInUse() == 18) {
                    int num = 5;
                    for (int i = 0; i < num * num; i++) {
                        Vec3d v = new Vec3d((0.3 + 0.15 * this.rand.nextFloat()) * 0.8, 0, 0);
                        float increment = (float) Math.PI * 2f / (float) num;
//                        v = v.rotatePitch(increment * i);
                        v = v.rotateY(increment * this.rand.nextFloat() + increment * (i / (float) num));
                        v = v.rotateZ(increment * this.rand.nextFloat() + increment * (i % num));
                        AdvancedParticleBase.spawnParticle(this.getUser().getWorld(), ParticleHandler.PIXEL, this.getUser().myPos[0].getX(), this.getUser().myPos[0].getY(), this.getUser().myPos[0].getZ(), v.getX(), v.getY(), v.getZ(), true, 0, 0, 0, 0, 4f, 0.98, 0.94, 0.39, 1, 0.8, 6 + this.rand.nextFloat() * 4, true, false, new ParticleComponent[]{
                                new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.SCALE, new ParticleComponent.KeyTrack(
                                        new float[]{4f, 0},
                                        new float[]{0.8f, 1}
                                ), false)
                        });
                    }
                }
            }
        }

        @Override
        public boolean canCancelActiveAbility() {
            return this.getUser().getActiveAbility() instanceof UmvuthanaHealAbility;
        }
    }

    private static class UmvuthanaHealAbility extends Ability<EntityUmvuthana> {

        private static final RawAnimation HEAL_START_ANIM = RawAnimation.begin().thenPlay("heal_start");
        private static final RawAnimation HEAL_LOOP_ANIM = RawAnimation.begin().thenLoop("heal_loop");
        private static final RawAnimation HEAL_END_ANIM = RawAnimation.begin().thenPlay("heal_end");

        public UmvuthanaHealAbility(AbilityType<EntityUmvuthana, ? extends Ability> abilityType, EntityUmvuthana user) {
            super(abilityType, user, new AbilitySection[]{
                    new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.STARTUP, 6),
                    new AbilitySection.AbilitySectionInfinite(AbilitySection.AbilitySectionType.ACTIVE),
                    new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.RECOVERY, 16)
            });
        }

        @Override
        protected void beginSection(AbilitySection section) {
            if (section.sectionType == AbilitySection.AbilitySectionType.STARTUP) {
                this.playAnimation(HEAL_START_ANIM);
            } else if (section.sectionType == AbilitySection.AbilitySectionType.ACTIVE) {
            } else {
                this.playAnimation(HEAL_END_ANIM);
            }
        }

        @Override
        public void tickUsing() {
            super.tickUsing();
            if (this.getUser().getTarget() != null) {
                this.getUser().getLookControl().lookAt(this.getUser().getTarget(), this.getUser().getMaxHeadRotation(), this.getUser().getMaxLookPitchChange());
                this.getUser().lookAtEntity(this.getUser().getTarget(), this.getUser().getMaxHeadRotation(), this.getUser().getMaxLookPitchChange());
            }
            if (this.getTicksInUse() == 6) {
                this.getUser().playSound(MMSounds.ENTITY_UMVUTHANA_HEAL_START.get(this.rand.nextInt(3)), 4, 1);
                MowziesMobs.PROXY.playSunblockSound(this.getUser());
            }

            if (this.getTicksInUse() >= 6) {
                EffectHandler.addOrCombineEffect(this.getUser(), StatusEffects.GLOWING, 5, 0, false, false);
            }

            if (this.getUser().getWorld().isClient && this.getTicksInUse() == 5 && this.getUser().headPos != null && this.getUser().headPos.length >= 1)
                this.getUser().headPos[0] = this.getUser().getPos().add(0, this.getUser().getStandingEyeHeight(), 0);

            if (this.getTicksInUse() == 12) {
                this.playAnimation(HEAL_LOOP_ANIM);
            }

            if (this.getCurrentSection().sectionType == AbilitySection.AbilitySectionType.ACTIVE) {
                this.spawnHealParticles();
                this.getUser().sunBlockTarget();
                if (!this.getLevel().isClient() && this.getUser().getTarget() == null) {
                    AbilityHandler.INSTANCE.sendJumpToSectionMessage(this.getUser(), this.getAbilityType(), 2);
                }
            }
        }

        public void spawnHealParticles() {
            if (this.getUser().getTarget() != null) {
                this.getUser().setHealPos(this.getUser().getTarget().getPos().add(new Vec3d(0, this.getUser().getTarget().getHeight() / 2f, 0)));
            }
            if (this.getUser().getWorld().isClient && this.getUser().barakoPos != null) {
                this.getUser().barakoPos[0] = this.getUser().getHealPos();
                if (this.getUser().headPos != null && this.getUser().headPos[0] != null) {
                    double dist = Math.max(this.getUser().barakoPos[0].distanceTo(this.getUser().headPos[0]), 0.01);
                    double radius = 0.5f;
                    double yaw = this.rand.nextFloat() * 2 * Math.PI;
                    double pitch = this.rand.nextFloat() * 2 * Math.PI;
                    double ox = radius * Math.sin(yaw) * Math.sin(pitch);
                    double oy = radius * Math.cos(pitch);
                    double oz = radius * Math.cos(yaw) * Math.sin(pitch);
                    if (this.getTicksInUse() % 5 == 0)
                        AdvancedParticleBase.spawnParticle(this.getUser().getWorld(), ParticleHandler.ARROW_HEAD, this.getUser().headPos[0].getX(), this.getUser().headPos[0].getY(), this.getUser().headPos[0].getZ(), 0, 0, 0, false, 0, 0, 0, 0, 3.5F, 0.95, 0.9, 0.35, 0.75, 1, Math.min(2 * dist, 60), true, false, new ParticleComponent[]{
                                new ParticleComponent.Attractor(this.getUser().barakoPos, 0.5f, 0.2f, ParticleComponent.Attractor.EnumAttractorBehavior.LINEAR),
                                new RibbonComponent(ParticleHandler.RIBBON_FLAT, 10, 0, 0, 0, 0.12F, 0.95, 0.9, 0.35, 0.75, true, true, new ParticleComponent[]{
                                        new RibbonComponent.PropertyOverLength(RibbonComponent.PropertyOverLength.EnumRibbonProperty.SCALE, ParticleComponent.KeyTrack.startAndEnd(1, 0))
                                }),
                                new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.POS_X, new ParticleComponent.Oscillator(0, (float) ox, (float) (1 * dist), 2.5f), true),
                                new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.POS_Y, new ParticleComponent.Oscillator(0, (float) oy, (float) (1 * dist), 2.5f), true),
                                new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.POS_Z, new ParticleComponent.Oscillator(0, (float) oz, (float) (1 * dist), 2.5f), true),
                                new ParticleComponent.FaceMotion(),
                                new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.ALPHA, new ParticleComponent.KeyTrack(new float[]{0, 0, 1}, new float[]{0, 0.05f, 0.06f}), false),
                        });
                    if (this.getTicksInUse() % 5 == 0)
                        AdvancedParticleBase.spawnParticle(this.getUser().getWorld(), ParticleHandler.RING2, this.getUser().headPos[0].getX(), this.getUser().headPos[0].getY(), this.getUser().headPos[0].getZ(), 0, 0, 0, true, 0, 0, 0, 0, 1.5F, 1, 223 / 255f, 66 / 255f, 1, 1, 15, true, false, new ParticleComponent[]{
                                new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.ALPHA, ParticleComponent.KeyTrack.startAndEnd(1f, 0f), false),
                                new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.SCALE, ParticleComponent.KeyTrack.startAndEnd(1f, 10f), false)
                        });
                    int spawnFreq = 5;
                    if (this.getTicksInUse() % spawnFreq == 0) {
                        World world = this.getUser().getWorld();
                        ParticleComponent[] components = new ParticleComponent[]{
                                new RibbonComponent.BeamPinning(this.getUser().headPos, this.getUser().barakoPos),
                                new RibbonComponent.PanTexture(0, 1)
                        };
                        ParticleRotation rotation = new ParticleRotation.FaceCamera((float) 0);
                        world.addParticle(new RibbonParticleData(ParticleHandler.RIBBON_SQUIGGLE, rotation, 0.5F, 0.95, 0.9, 0.35, 0.75, 1, spawnFreq, true, (int) (0.5 * dist), components), this.getUser().headPos[0].getX(), this.getUser().headPos[0].getY(), this.getUser().headPos[0].getZ(), 0, 0, 0);
                    }
                }
            }
        }

        @Override
        public boolean damageInterrupts() {
            return false;
        }
    }
}
