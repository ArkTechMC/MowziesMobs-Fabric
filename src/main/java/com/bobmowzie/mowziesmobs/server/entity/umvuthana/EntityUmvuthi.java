package com.bobmowzie.mowziesmobs.server.entity.umvuthana;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.tools.MathUtils;
import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieAnimationController;
import com.bobmowzie.mowziesmobs.client.particle.ParticleHandler;
import com.bobmowzie.mowziesmobs.client.particle.ParticleOrb;
import com.bobmowzie.mowziesmobs.client.particle.util.AdvancedParticleBase;
import com.bobmowzie.mowziesmobs.client.particle.util.ParticleComponent;
import com.bobmowzie.mowziesmobs.client.particle.util.ParticleComponent.PropertyControl.EnumParticleProperty;
import com.bobmowzie.mowziesmobs.server.ability.Ability;
import com.bobmowzie.mowziesmobs.server.ability.AbilitySection;
import com.bobmowzie.mowziesmobs.server.ability.AbilityType;
import com.bobmowzie.mowziesmobs.server.ability.abilities.mob.DieAbility;
import com.bobmowzie.mowziesmobs.server.ability.abilities.mob.HurtAbility;
import com.bobmowzie.mowziesmobs.server.ability.abilities.player.SimpleAnimationAbility;
import com.bobmowzie.mowziesmobs.server.advancement.AdvancementHandler;
import com.bobmowzie.mowziesmobs.server.ai.LookAtTargetGoal;
import com.bobmowzie.mowziesmobs.server.ai.NearestAttackableTargetPredicateGoal;
import com.bobmowzie.mowziesmobs.server.ai.UmvuthanaHurtByTargetAI;
import com.bobmowzie.mowziesmobs.server.ai.UseAbilityAI;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.LeaderSunstrikeImmune;
import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import com.bobmowzie.mowziesmobs.server.entity.MowzieGeckoEntity;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntitySolarBeam;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntitySunstrike;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntitySuperNova;
import com.bobmowzie.mowziesmobs.server.inventory.ContainerUmvuthiTrade;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import com.bobmowzie.mowziesmobs.server.item.UmvuthanaMask;
import com.bobmowzie.mowziesmobs.server.loot.LootTableHandler;
import com.bobmowzie.mowziesmobs.server.potion.EffectHandler;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import com.iafenvoy.uranus.client.model.tools.ControlledAnimation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.mob.ZombifiedPiglinEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.scoreboard.Team;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.ServerConfigHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.Uuids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Difficulty;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;

import java.util.*;

public class EntityUmvuthi extends MowzieGeckoEntity implements LeaderSunstrikeImmune, Monster {
    public static final AbilityType<EntityUmvuthi, DieAbility<EntityUmvuthi>> DIE_ABILITY = new AbilityType<>("umvuthi_die", (type, entity) -> new DieAbility<>(type, entity, RawAnimation.begin().thenPlay("death"), 115) {
        @Override
        public void tickUsing() {
            super.tickUsing();
            if (this.getTicksInUse() == 1)
                this.getUser().playSound(MMSounds.ENTITY_UMVUTHI_HURT, this.getUser().getSoundVolume(), this.getUser().getSoundPitch());
            if (this.getTicksInUse() == 14)
                this.getUser().playSound(MMSounds.ENTITY_UMVUTHI_DIE, this.getUser().getSoundVolume(), 1);
            if (this.getTicksInUse() == 80)
                this.getUser().playSound(MMSounds.MISC_METAL_IMPACT, this.getUser().getSoundVolume(), 1);
        }
    });
    public static final AbilityType<EntityUmvuthi, HurtAbility<EntityUmvuthi>> HURT_ABILITY = new AbilityType<>("umvuthi_hurt", (type, entity) -> new HurtAbility<>(type, entity, RawAnimation.begin().thenPlay("hurt"), 13, 10));
    public static final AbilityType<EntityUmvuthi, SimpleAnimationAbility<EntityUmvuthi>> BELLY_ABILITY = new AbilityType<>("umvuthi_belly", (type, entity) -> new SimpleAnimationAbility<>(type, entity, RawAnimation.begin().thenPlay("belly_drum"), 40, true) {
        @Override
        public void tickUsing() {
            super.tickUsing();
            if (this.getTicksInUse() == 9 || this.getTicksInUse() == 29) {
                this.getUser().playSound(MMSounds.ENTITY_UMVUTHI_BELLY, 3f, 1f);
            }
        }
    });
    public static final AbilityType<EntityUmvuthi, SimpleAnimationAbility<EntityUmvuthi>> TALK_ABILITY = new AbilityType<>("umvuthi_talk", (type, entity) -> new SimpleAnimationAbility<>(type, entity, RawAnimation.begin().thenPlay("talk"), 23, true));
    public static final AbilityType<EntityUmvuthi, SimpleAnimationAbility<EntityUmvuthi>> ROAR_ABILITY = new AbilityType<>("umvuthi_roar", (type, entity) -> new SimpleAnimationAbility<>(type, entity, RawAnimation.begin().thenPlay("roar"), 70, false) {
        @Override
        public void tickUsing() {
            super.tickUsing();
            if (this.getTicksInUse() == 2) {
                this.getUser().playSound(MMSounds.ENTITY_UMVUTHI_ROAR, 3f, 1f);
            }
        }
    });
    public static final AbilityType<EntityUmvuthi, SunstrikeAbility> SUNSTRIKE_ABILITY = new AbilityType<>("umvuthi_sunstrike", SunstrikeAbility::new);
    public static final AbilityType<EntityUmvuthi, SolarFlareAbility> SOLAR_FLARE_ABILITY = new AbilityType<>("umvuthi_flare", SolarFlareAbility::new);
    public static final AbilityType<EntityUmvuthi, SpawnFollowersAbility> SPAWN_ABILITY = new AbilityType<>("umvuthi_spawn", (type, entity) -> new SpawnFollowersAbility(type, entity, false));
    public static final AbilityType<EntityUmvuthi, SpawnFollowersAbility> SPAWN_SUNBLOCKERS_ABILITY = new AbilityType<>("umvuthi_spawn_healers", (type, entity) -> new SpawnFollowersAbility(type, entity, true));
    public static final AbilityType<EntityUmvuthi, SolarBeamAbility> SOLAR_BEAM_ABILITY = new AbilityType<>("umvuthi_solar_beam", SolarBeamAbility::new);
    public static final AbilityType<EntityUmvuthi, SimpleAnimationAbility<EntityUmvuthi>> BLESS_ABILITY = new AbilityType<>("umvuthi_bless", (type, entity) -> new SimpleAnimationAbility<>(type, entity, RawAnimation.begin().thenPlay("bless"), 84) {
        @Override
        public boolean canCancelActiveAbility() {
            return this.getUser().getActiveAbilityType() == ROAR_ABILITY || this.getUser().getActiveAbilityType() == TALK_ABILITY || this.getUser().getActiveAbilityType() == BELLY_ABILITY;
        }
    });
    public static final AbilityType<EntityUmvuthi, SupernovaAbility> SUPERNOVA_ABILITY = new AbilityType<>("umvuthi_supernova", SupernovaAbility::new);
    private static final int MAX_HEALTH = 150;
    private static final int SUNSTRIKE_PAUSE_MAX = 50;
    private static final int SUNSTRIKE_PAUSE_MIN = 30;
    private static final int LASER_PAUSE = 230;
    private static final int SUPERNOVA_PAUSE = 230;
    private static final int UMVUTHANA_PAUSE = 200;
    private static final int ROAR_PAUSE = 300;
    private static final int HEAL_PAUSE = 75;
    private static final int HEALTH_LOST_BETWEEN_SUNBLOCKERS = 45;
    private static final TrackedData<Integer> DIRECTION = DataTracker.registerData(EntityUmvuthi.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> DIALOGUE = DataTracker.registerData(EntityUmvuthi.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Boolean> ANGRY = DataTracker.registerData(EntityUmvuthi.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<ItemStack> DESIRES = DataTracker.registerData(EntityUmvuthi.class, TrackedDataHandlerRegistry.ITEM_STACK);
    private static final TrackedData<NbtCompound> TRADED_PLAYERS = DataTracker.registerData(EntityUmvuthi.class, TrackedDataHandlerRegistry.NBT_COMPOUND);
    private static final TrackedData<Float> HEALTH_LOST = DataTracker.registerData(EntityUmvuthi.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Optional<UUID>> MISBEHAVED_PLAYER = DataTracker.registerData(EntityUmvuthi.class, TrackedDataHandlerRegistry.OPTIONAL_UUID);
    private static final TrackedData<Boolean> IS_TRADING = DataTracker.registerData(EntityUmvuthi.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TargetPredicate GIVE_ACHIEVEMENT_PRED = TargetPredicate.createAttackable().ignoreDistanceScalingFactor();
    private static final RawAnimation MASK_TWITCH_ANIM = RawAnimation.begin().thenLoop("mask_twitch");
    private static final RawAnimation BLINK_ANIM = RawAnimation.begin().thenLoop("blink");
    public ControlledAnimation legsUp = new ControlledAnimation(15);
    public ControlledAnimation angryEyebrow = new ControlledAnimation(5);
    public int umvuthanaSpawnCount = 0;
    public PlayerEntity blessingPlayer;
    @Environment(EnvType.CLIENT)
    public Vec3d[] betweenHandPos;
    @Environment(EnvType.CLIENT)
    public Vec3d[] headPos;
    @Environment(EnvType.CLIENT)
    public Vec3d[] blessingPlayerPos;
    protected AnimationController<MowzieGeckoEntity> maskController = new MowzieAnimationController<>(this, "mask_controller", 1, this::predicateMask, 0.0);
    protected AnimationController<MowzieGeckoEntity> blinkController = new MowzieAnimationController<>(this, "blink_controller", 1, this::predicateBlink, 0.0);
    private PlayerEntity customer;
    // TODO: use Direction!
    private int direction = 0;
    private boolean blocksByFeet = true;
    private int timeUntilSunstrike = 0;
    private int timeUntilLaser = 0;
    private int timeUntilUmvuthana = 0;
    private int timeUntilRoar = 0;
    private int timeUntilSupernova = 0;
    private int timeUntilHeal = 0;
    private UmvuthanaHurtByTargetAI hurtByTargetAI;
    private float prevMaskRot = 0;
    private boolean rattling = false;

    public EntityUmvuthi(EntityType<? extends EntityUmvuthi> type, World world) {
        super(type, world);
        if (this.getDirectionData() == 0) {
            this.setDirection(this.random.nextInt(4) + 1);
        }
        this.experiencePoints = 45;

        if (world.isClient) {
            this.headPos = new Vec3d[]{new Vec3d(0, 0, 0)};
            this.betweenHandPos = new Vec3d[]{new Vec3d(0, 0, 0)};
            this.blessingPlayerPos = new Vec3d[]{new Vec3d(0, 0, 0)};
        }

        this.active = true;
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MowzieEntity.createAttributes().add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 2)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, MAX_HEALTH)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 1)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 40);
    }

    private static boolean canPayFor(ItemStack stack, ItemStack worth) {
        return stack.getItem() == worth.getItem() && stack.getCount() >= worth.getCount();
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.hurtByTargetAI = new UmvuthanaHurtByTargetAI(this, false);
        this.targetSelector.add(3, this.hurtByTargetAI);
        this.targetSelector.add(4, new NearestAttackableTargetPredicateGoal<PlayerEntity>(this, PlayerEntity.class, 0, false, true, (TargetPredicate.createAttackable().setBaseMaxDistance(this.getAttributeValue(EntityAttributes.GENERIC_FOLLOW_RANGE)).setPredicate(target -> {
            if (target instanceof PlayerEntity) {
                if (this.getWorld().getDifficulty() == Difficulty.PEACEFUL) return false;
                ItemStack headArmorStack = ((PlayerEntity) target).getInventory().armor.get(3);
                return !(headArmorStack.getItem() instanceof UmvuthanaMask) || target == this.getMisbehavedPlayer();
            }
            return true;
        }).ignoreVisibility())) {
            @Override
            public void stop() {
                super.stop();
                EntityUmvuthi.this.setMisbehavedPlayerId(null);
            }
        });
        this.targetSelector.add(5, new ActiveTargetGoal<>(this, IronGolemEntity.class, 0, false, false, null));
        this.targetSelector.add(5, new ActiveTargetGoal<>(this, ZombieEntity.class, 0, false, false, (e) -> !(e instanceof ZombifiedPiglinEntity)));
        this.targetSelector.add(5, new ActiveTargetGoal<>(this, AbstractSkeletonEntity.class, 0, false, false, null));
        this.goalSelector.add(1, new UseAbilityAI<>(this, DIE_ABILITY));
        this.goalSelector.add(2, new UseAbilityAI<>(this, HURT_ABILITY, false));
        this.goalSelector.add(6, new UseAbilityAI<>(this, BELLY_ABILITY, false));
        this.goalSelector.add(2, new UseAbilityAI<>(this, SUNSTRIKE_ABILITY));
        this.goalSelector.add(2, new UseAbilityAI<>(this, SOLAR_FLARE_ABILITY));
        this.goalSelector.add(2, new UseAbilityAI<>(this, SOLAR_BEAM_ABILITY));
        this.goalSelector.add(2, new UseAbilityAI<>(this, SUPERNOVA_ABILITY));
        this.goalSelector.add(2, new UseAbilityAI<>(this, SPAWN_ABILITY));
        this.goalSelector.add(2, new UseAbilityAI<>(this, SPAWN_SUNBLOCKERS_ABILITY));
        this.goalSelector.add(2, new UseAbilityAI<>(this, BLESS_ABILITY));
        this.goalSelector.add(2, new UseAbilityAI<>(this, TALK_ABILITY, false));
        this.goalSelector.add(2, new UseAbilityAI<>(this, ROAR_ABILITY, true));
        this.goalSelector.add(5, new LookAtTargetGoal(this, 24.0F));
        this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 10.0F));
        this.goalSelector.add(7, new LookAtEntityGoal(this, EntityUmvuthana.class, 8.0F));
        this.goalSelector.add(8, new LookAroundGoal(this));
    }

    @Override
    protected float getActiveEyeHeight(EntityPose poseIn, EntityDimensions sizeIn) {
        return super.getActiveEyeHeight(poseIn, sizeIn);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        super.registerControllers(controllers);
        controllers.add(this.maskController);
        controllers.add(this.blinkController);
    }

    protected <E extends GeoEntity> PlayState predicateMask(AnimationState<E> state) {
        if (this.isAlive() && this.getActiveAbilityType() != SOLAR_BEAM_ABILITY && this.getActiveAbilityType() != SUPERNOVA_ABILITY && this.getActiveAbilityType() != SPAWN_ABILITY && this.getActiveAbilityType() != SPAWN_SUNBLOCKERS_ABILITY) {
            state.getController().setAnimation(MASK_TWITCH_ANIM);
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    protected <E extends GeoEntity> PlayState predicateBlink(AnimationState<E> event) {
        if (this.isAlive() && this.getActiveAbilityType() != SOLAR_BEAM_ABILITY) {
            event.getController().setAnimation(BLINK_ANIM);
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    @Override
    protected <E extends GeoEntity> void loopingAnimations(AnimationState<E> event) {
        event.getController().transitionLength(4);
        super.loopingAnimations(event);
    }

    @Override
    public boolean cannotDespawn() {
        return true;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        if (this.getActiveAbility() == null) {
            this.sendAbilityMessage(TALK_ABILITY);
            return MMSounds.ENTITY_UMVUTHI_IDLE;
        }
        return null;
    }

    public void updateRattleSound(float maskRot) {
        if (!this.rattling) {
            if (Math.abs(maskRot - this.prevMaskRot) > 0.05) {
                this.getWorld().playSound(this.getX(), this.getY(), this.getZ(), MMSounds.ENTITY_UMVUTHANA_RATTLE, SoundCategory.HOSTILE, 0.04f, this.getSoundPitch() * 0.75f, false);
            }
        } else {
            if (Math.abs(maskRot - this.prevMaskRot) < 0.00000001) {
                this.rattling = false;
            }
        }
        this.prevMaskRot = maskRot;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return MMSounds.ENTITY_UMVUTHI_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return null;
    }

    @Override
    public boolean canBePushedByEntity(Entity entity) {
        return false;
    }

    public boolean shouldRenderSun() {
        return this.deathTime < 85 && !(this.getActiveAbilityType() == EntityUmvuthi.SUPERNOVA_ABILITY && this.getActiveAbility().getTicksInUse() > 5 && this.getActiveAbility().getTicksInUse() <= 90);
    }

    @Override
    public void tick() {
        this.legsUp.increaseTimer();
        this.angryEyebrow.increaseTimer();
        this.setVelocity(0, this.getVelocity().y, 0);
        super.tick();
        if (this.age == 1) {
            this.direction = this.getDirectionData();
        }
        if (!(this.getActiveAbilityType() == SOLAR_FLARE_ABILITY && this.getActiveAbility().getTicksInUse() >= 12 && this.getActiveAbility().getTicksInUse() <= 14))
            this.repelEntities(1.2f, 1.2f, 1.2f, 1.2f);
        this.setYaw((this.direction - 1) * 90);
        this.bodyYaw = this.getYaw();
//        this.posX = prevPosX;
//        this.posZ = prevPosZ;

        if (this.getWorld().isClient()) {
            if (this.shouldRenderSun()) {
                if (this.headPos != null && this.headPos.length > 0 && this.headPos[0] != null) {
                    if (this.age % 10 == 1) {
                        AdvancedParticleBase.spawnParticle(this.getWorld(), ParticleHandler.GLOW, this.getX(), this.getY(), this.getZ(), 0, 0, 0, true, 0, 0, 0, 0, 0F, 1, 1, 0.3, 0.4, 1, 9, true, false, new ParticleComponent[]{
                                new ParticleComponent.PinLocation(this.headPos),
                                new ParticleComponent.PropertyControl(EnumParticleProperty.SCALE, ParticleComponent.KeyTrack.oscillate(12.5f, 13.5f, 12), false)
                        });
                    }
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

        if (!this.getWorld().isClient && this.getHealthLost() >= HEALTH_LOST_BETWEEN_SUNBLOCKERS && this.getActiveAbility() == null && !this.isAiDisabled() && this.getEntitiesNearby(EntityUmvuthanaCrane.class, 40).size() < 3) {
            this.sendAbilityMessage(SPAWN_SUNBLOCKERS_ABILITY);
            this.setHealthLost(0);
        }
        if (this.getTarget() != null) {
            LivingEntity target = this.getTarget();
            this.setAngry(true);
            float entityHitAngle = (float) ((Math.atan2(target.getZ() - this.getZ(), target.getX() - this.getX()) * (180 / Math.PI) - 90) % 360);
            float entityAttackingAngle = this.getYaw() % 360;
            if (entityHitAngle < 0) {
                entityHitAngle += 360;
            }
            if (entityAttackingAngle < 0) {
                entityAttackingAngle += 360;
            }
            float entityRelativeAngle = Math.abs(entityHitAngle - entityAttackingAngle);
            Vec3d betweenEntitiesVec = this.getPos().subtract(target.getPos());
            boolean targetComingCloser = target.getVelocity().dotProduct(betweenEntitiesVec) > 0 && target.getVelocity().lengthSquared() > 0.015;

            // Attacks
            if (this.getActiveAbility() == null && !this.isAiDisabled() && this.random.nextInt(80) == 0 && (this.targetDistance > 5.5 || this.hasStatusEffect(EffectHandler.SUNBLOCK)) && this.timeUntilUmvuthana <= 0 && this.getEntitiesNearby(EntityUmvuthana.class, 50).size() < 4) {
                this.sendAbilityMessage(SPAWN_ABILITY);
                this.timeUntilUmvuthana = UMVUTHANA_PAUSE;
            } else if (this.getActiveAbility() == null && !this.isAiDisabled() && this.getHealthRatio() <= 0.6 && this.timeUntilLaser <= 0 && (entityRelativeAngle < 60 || entityRelativeAngle > 300) && this.getVisibilityCache().canSee(target) && this.targetDistance < EntitySolarBeam.RADIUS_UMVUTHI) {
                this.sendAbilityMessage(SOLAR_BEAM_ABILITY);
                this.timeUntilLaser = LASER_PAUSE;
            } else if (this.getActiveAbility() == null && !this.isAiDisabled() && this.getHealthRatio() <= 0.6 && !this.hasStatusEffect(EffectHandler.SUNBLOCK) && this.timeUntilSupernova <= 0 && this.targetDistance <= 10.5) {
                this.sendAbilityMessage(SUPERNOVA_ABILITY);
                this.timeUntilSupernova = SUPERNOVA_PAUSE;
            } else if (this.getActiveAbility() == null && !this.isAiDisabled() && ((this.targetDistance <= 6f && targetComingCloser) || this.targetDistance < 4.f)) {
                this.sendAbilityMessage(SOLAR_FLARE_ABILITY);
            } else if (this.getActiveAbility() == null && !this.isAiDisabled() && this.timeUntilSunstrike <= 0) {
                this.sendAbilityMessage(SUNSTRIKE_ABILITY);
                this.timeUntilSunstrike = this.getTimeUntilSunstrike();
            }

            if (this.hurtByTargetAI != null && !this.hurtByTargetAI.shouldContinue()) {
                this.hurtByTargetAI.stop();
            }
        } else {
            if (!this.getWorld().isClient) {
                this.setAngry(false);
            }
        }

        if (this.age % 20 == 0) {
            this.blocksByFeet = this.checkBlocksByFeet();
        }

        if (this.blocksByFeet) {
            this.legsUp.increaseTimer();
        } else {
            this.legsUp.decreaseTimer();
        }

        if (this.getAngry()) {
            this.angryEyebrow.increaseTimer();
        } else {
            this.angryEyebrow.decreaseTimer();
        }

        if (this.getActiveAbility() == null && !this.isAiDisabled() && this.getTarget() == null && this.random.nextInt(200) == 0) {
            this.sendAbilityMessage(BELLY_ABILITY);
        }

        if (this.getActiveAbility() == null && !this.isAiDisabled() && this.getTarget() == null && this.timeUntilRoar <= 0 && this.random.nextInt(300) == 0) {
            this.sendAbilityMessage(ROAR_ABILITY);
            this.timeUntilRoar = ROAR_PAUSE;
        }

//        if (getActiveAbilityType() == TALK_ABILITY && getActiveAbility().getTicksInUse() == 1) {
//            whichDialogue = getWhichDialogue();
//        }

        if (this.getActiveAbilityType() == SOLAR_FLARE_ABILITY) {
            this.headYaw = this.getYaw();
//            if (getActiveAbility().getTicksInUse() == 1) {
//                this.playSound(MMSounds.ENTITY_UMVUTHI_BURST, 1.7f, 1.5f);
//            }
            if (this.getActiveAbility().getTicksInUse() == 10) {
                if (this.getWorld().isClient) {
                    this.spawnExplosionParticles(30);
                }
                this.playSound(MMSounds.ENTITY_UMVUTHI_ATTACK, 1.7f, 0.9f);
            }
            if (this.getActiveAbility().getTicksInUse() <= 6 && this.getWorld().isClient) {
                int particleCount = 8;
                while (--particleCount != 0) {
                    double radius = 2f;
                    double yaw = this.random.nextFloat() * 2 * Math.PI;
                    double pitch = this.random.nextFloat() * 2 * Math.PI;
                    double ox = radius * Math.sin(yaw) * Math.sin(pitch);
                    double oy = radius * Math.cos(pitch);
                    double oz = radius * Math.cos(yaw) * Math.sin(pitch);
                    float offsetX = (float) (-0.3 * Math.sin(this.getYaw() * Math.PI / 180));
                    float offsetZ = (float) (-0.3 * Math.cos(this.getYaw() * Math.PI / 180));
                    float offsetY = 1;
                    this.getWorld().addParticle(new ParticleOrb.OrbData((float) this.getX() + offsetX, (float) this.getY() + offsetY, (float) this.getZ() + offsetZ, 6), this.getX() + ox + offsetX, this.getY() + offsetY + oy, this.getZ() + oz + offsetZ, 0, 0, 0);
                }
            }
        }

        if (this.getActiveAbilityType() == BLESS_ABILITY) {
            this.headYaw = this.getYaw();

            if (this.getActiveAbility().getTicksInUse() == 1) {
                this.blessingPlayer = this.getCustomer();
            }
            if (this.getWorld().isClient && this.blessingPlayer != null) {
                this.blessingPlayerPos[0] = this.blessingPlayer.getPos().add(new Vec3d(0, this.blessingPlayer.getHeight() / 2f, 0));
                if (this.getActiveAbility().getTicksInUse() > 5 && this.getActiveAbility().getTicksInUse() < 40) {
                    int particleCount = 2;
                    while (--particleCount != 0) {
                        double radius = 0.7f;
                        double yaw = this.random.nextFloat() * 2 * Math.PI;
                        double pitch = this.random.nextFloat() * 2 * Math.PI;
                        double ox = radius * Math.sin(yaw) * Math.sin(pitch);
                        double oy = radius * Math.cos(pitch);
                        double oz = radius * Math.cos(yaw) * Math.sin(pitch);
                        AdvancedParticleBase.spawnParticle(this.getWorld(), ParticleHandler.ORB2, this.getX() + ox, this.getY() + 0.8f + oy, this.getZ() + oz, 0, 0, 0, true, 0, 0, 0, 0, 5F, 1, 1, 1, 1, 1, 20, true, true, new ParticleComponent[]{
                                new ParticleComponent.Attractor(this.blessingPlayerPos, 0.5f, 0.2f, ParticleComponent.Attractor.EnumAttractorBehavior.LINEAR),
                                new ParticleComponent.PropertyControl(EnumParticleProperty.POS_X, new ParticleComponent.Oscillator(0, (float) ox, 6f, 2.5f), true),
                                new ParticleComponent.PropertyControl(EnumParticleProperty.POS_Y, new ParticleComponent.Oscillator(0, (float) oy, 6f, 2.5f), true),
                                new ParticleComponent.PropertyControl(EnumParticleProperty.POS_Z, new ParticleComponent.Oscillator(0, (float) oz, 6f, 2.5f), true),
                                new ParticleComponent.PropertyControl(EnumParticleProperty.ALPHA, new ParticleComponent.KeyTrack(
                                        new float[]{0f, 1f},
                                        new float[]{0, 0.8f}
                                ), false)
                        });
                    }
                }
                if (this.getActiveAbility().getTicksInUse() % 15 == 0) {
                    AdvancedParticleBase.spawnParticle(this.getWorld(), ParticleHandler.RING2, this.getX(), this.getY() + 0.8f, this.getZ(), 0, 0, 0, true, 0, 0, 0, 0, 3.5F, 1, 223 / 255f, 66 / 255f, 1, 1, 15, true, true, new ParticleComponent[]{
                            new ParticleComponent.PropertyControl(EnumParticleProperty.ALPHA, ParticleComponent.KeyTrack.startAndEnd(1f, 0f), false),
                            new ParticleComponent.PropertyControl(EnumParticleProperty.SCALE, ParticleComponent.KeyTrack.startAndEnd(5f, 35f), false)
                    });
                }
            }
        }

        if (this.age % 40 == 0) {
            for (PlayerEntity player : this.getPlayersNearby(15, 15, 15, 15)) {
                ItemStack headArmorStack = player.getInventory().armor.get(3);
                if (this.getTarget() != player && this.isTarget(player, GIVE_ACHIEVEMENT_PRED) && headArmorStack.getItem() instanceof UmvuthanaMask) {
                    if (player instanceof ServerPlayerEntity)
                        AdvancementHandler.SNEAK_VILLAGE_TRIGGER.trigger((ServerPlayerEntity) player);
                }
            }
        }

        if (!this.getWorld().isClient && this.getTarget() == null && this.getActiveAbilityType() != SOLAR_BEAM_ABILITY && this.getActiveAbilityType() != SUPERNOVA_ABILITY) {
            this.timeUntilHeal--;
            if (ConfigHandler.COMMON.MOBS.UMVUTHI.healsOutOfBattle && this.timeUntilHeal <= 0) this.heal(0.3f);
            if (this.getHealth() == this.getMaxHealth()) this.setHealthLost(0);
        } else {
            this.timeUntilHeal = HEAL_PAUSE;
        }

        if (this.timeUntilSunstrike > 0) {
            this.timeUntilSunstrike--;
        }
        if (this.timeUntilLaser > 0 && this.getActiveAbilityType() != SUPERNOVA_ABILITY) {
            this.timeUntilLaser--;
        }
        if (this.timeUntilUmvuthana > 0) {
            this.timeUntilUmvuthana--;
        }
        if (this.timeUntilSupernova > 0 && this.getActiveAbilityType() != SOLAR_BEAM_ABILITY) {
            this.timeUntilSupernova--;
        }
        if (this.timeUntilRoar > 0) {
            this.timeUntilRoar--;
        }

//        if (getActiveAbility() == null && tickCount % 60 == 0) {
//            sendAbilityMessage(SOLAR_BEAM_ABILITY);
//        }
    }

    @Override
    public AbilityType getHurtAbility() {
        return HURT_ABILITY;
    }

    @Override
    public AbilityType getDeathAbility() {
        return DIE_ABILITY;
    }

    @Override
    public boolean damage(DamageSource source, float damage) {
        if (source == this.getWorld().getDamageSources().hotFloor()) return false;
        if (this.hasStatusEffect(EffectHandler.SUNBLOCK) && !source.isIn(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            if (source.getSource() != null) this.playSound(MMSounds.ENTITY_WROUGHT_UNDAMAGED, 0.4F, 2);
            return false;
        }
        this.timeUntilHeal = HEAL_PAUSE;
        float prevHealth = this.getHealth();
        boolean superResult = super.damage(source, damage);
        if (superResult) {
            float diffHealth = prevHealth - this.getHealth();
            this.setHealthLost(this.getHealthLost() + diffHealth);
        }
        return superResult;
    }

    private boolean checkBlocksByFeet() {
        BlockState blockLeft;
        BlockState blockRight;
        BlockPos posLeft;
        BlockPos posRight;
        if (this.direction == 1) {
            posLeft = new BlockPos(MathHelper.floor(this.getX()) + 1, Math.round((float) (this.getY() - 1)), MathHelper.floor(this.getZ()) + 1);
            posRight = new BlockPos(MathHelper.floor(this.getX()) - 1, Math.round((float) (this.getY() - 1)), MathHelper.floor(this.getZ()) + 1);
            blockLeft = this.getWorld().getBlockState(posLeft);
            blockRight = this.getWorld().getBlockState(posRight);
        } else if (this.direction == 2) {
            posLeft = new BlockPos(MathHelper.floor(this.getX()) - 1, Math.round((float) (this.getY() - 1)), MathHelper.floor(this.getZ()) + 1);
            posRight = new BlockPos(MathHelper.floor(this.getX()) - 1, Math.round((float) (this.getY() - 1)), MathHelper.floor(this.getZ()) - 1);
            blockLeft = this.getWorld().getBlockState(posLeft);
            blockRight = this.getWorld().getBlockState(posRight);
        } else if (this.direction == 3) {
            posLeft = new BlockPos(MathHelper.floor(this.getX()) - 1, Math.round((float) (this.getY() - 1)), MathHelper.floor(this.getZ()) - 1);
            posRight = new BlockPos(MathHelper.floor(this.getX()) + 1, Math.round((float) (this.getY() - 1)), MathHelper.floor(this.getZ()) - 1);
            blockLeft = this.getWorld().getBlockState(posLeft);
            blockRight = this.getWorld().getBlockState(posRight);
        } else if (this.direction == 4) {
            posLeft = new BlockPos(MathHelper.floor(this.getX()) + 1, Math.round((float) (this.getY() - 1)), MathHelper.floor(this.getZ()) - 1);
            posRight = new BlockPos(MathHelper.floor(this.getX()) + 1, Math.round((float) (this.getY() - 1)), MathHelper.floor(this.getZ()) + 1);
            blockLeft = this.getWorld().getBlockState(posLeft);
            blockRight = this.getWorld().getBlockState(posRight);
        } else {
            return false;
        }
//        System.out.println(direction + ", " + (MathHelper.floor(posX) - 1) + ", " + Math.round((float) (posY - 1)) + ", " + MathHelper.floor(posZ) + 1);
        return blockLeft.blocksMovement() || blockRight.blocksMovement();
    }

    private void spawnExplosionParticles(int amount) {
        for (int i = 0; i < amount; i++) {
            final float velocity = 0.25F;
            float yaw = i * (MathUtils.TAU / amount);
            float vy = this.random.nextFloat() * 0.1F - 0.05f;
            float vx = velocity * MathHelper.cos(yaw);
            float vz = velocity * MathHelper.sin(yaw);
            this.getWorld().addParticle(ParticleTypes.FLAME, this.getX(), this.getY() + 1, this.getZ(), vx, vy, vz);
        }
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.getDataTracker().startTracking(DIRECTION, 0);
        this.getDataTracker().startTracking(DIALOGUE, 0);
        this.getDataTracker().startTracking(ANGRY, false);
        Item tradeItem = Registries.ITEM.get(new Identifier(ConfigHandler.COMMON.MOBS.UMVUTHI.whichItem));
        this.getDataTracker().startTracking(DESIRES, new ItemStack(tradeItem, ConfigHandler.COMMON.MOBS.UMVUTHI.howMany));
        this.getDataTracker().startTracking(TRADED_PLAYERS, new NbtCompound());
        this.getDataTracker().startTracking(HEALTH_LOST, 0.f);
        this.getDataTracker().startTracking(MISBEHAVED_PLAYER, Optional.empty());
        this.getDataTracker().startTracking(IS_TRADING, false);
    }

    public int getDirectionData() {
        return this.getDataTracker().get(DIRECTION);
    }

    public void setDirection(int direction) {
        this.getDataTracker().set(DIRECTION, direction);
    }

    public int getWhichDialogue() {
        return this.getDataTracker().get(DIALOGUE);
    }

    public void setWhichDialogue(int dialogue) {
        this.getDataTracker().set(DIALOGUE, dialogue);
    }

    public boolean getAngry() {
        return this.getDataTracker().get(ANGRY);
    }

    public void setAngry(boolean angry) {
        this.getDataTracker().set(ANGRY, angry);
    }

    public ItemStack getDesires() {
        return this.getDataTracker().get(DESIRES);
    }

    public void setDesires(ItemStack stack) {
        this.getDataTracker().set(DESIRES, stack);
    }

    public void setTradedPlayersCompound(NbtList players) {
        NbtCompound compound = new NbtCompound();
        compound.put("players", players);
        this.getDataTracker().set(TRADED_PLAYERS, compound);
    }

    public Set<UUID> getTradedPlayers() {
        Set<UUID> tradedPlayers = new HashSet<>();
        NbtCompound compound = this.getDataTracker().get(TRADED_PLAYERS);
        NbtList players = compound.getList("players", NbtElement.INT_ARRAY_TYPE);
        for (NbtElement player : players) {
            tradedPlayers.add(NbtHelper.toUuid(player));
        }
        return tradedPlayers;
    }

    public float getHealthLost() {
        return this.getDataTracker().get(HEALTH_LOST);
    }

    public void setHealthLost(float amount) {
        this.getDataTracker().set(HEALTH_LOST, amount);
    }

    public boolean doesItemSatisfyDesire(ItemStack stack) {
        return canPayFor(stack, this.getDesires());
    }

    public boolean fulfillDesire(Slot input) {
        ItemStack desires = this.getDesires();
        if (canPayFor(input.getStack(), desires)) {
            input.takeStack(desires.getCount());
            return true;
        }
        return false;
    }

    public boolean hasTradedWith(PlayerEntity player) {
        return this.getTradedPlayers().contains(Uuids.getUuidFromProfile(player.getGameProfile()));
    }

    public void rememberTrade(PlayerEntity player) {
        UUID uuid = Uuids.getUuidFromProfile(player.getGameProfile());
        NbtCompound compound = this.getDataTracker().get(TRADED_PLAYERS);
        NbtList players = compound.getList("players", NbtElement.INT_ARRAY_TYPE);
        players.add(NbtHelper.fromUuid(uuid));
        compound.put("players", players);
        this.getDataTracker().set(TRADED_PLAYERS, compound);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound compound) {
        super.writeCustomDataToNbt(compound);
        compound.putInt("direction", this.getDirectionData());
        NbtCompound compoundTradedPlayers = this.getDataTracker().get(TRADED_PLAYERS);
        NbtList players = compoundTradedPlayers.getList("players", NbtElement.INT_ARRAY_TYPE);
        compound.put("players", players);
        compound.putInt("HomePosX", this.getPositionTarget().getX());
        compound.putInt("HomePosY", this.getPositionTarget().getY());
        compound.putInt("HomePosZ", this.getPositionTarget().getZ());
        compound.putFloat("healthLost", this.getHealthLost());
        if (this.getMisbehavedPlayerId() != null) {
            compound.putUuid("MisbehavedPlayer", this.getMisbehavedPlayerId());
        }
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound compound) {
        super.readCustomDataFromNbt(compound);
        this.setDirection(compound.getInt("direction"));
        NbtList players = compound.getList("players", NbtElement.INT_ARRAY_TYPE);
        this.setTradedPlayersCompound(players);
        int i = compound.getInt("HomePosX");
        int j = compound.getInt("HomePosY");
        int k = compound.getInt("HomePosZ");
        this.setPositionTarget(new BlockPos(i, j, k), -1);
        this.setHealthLost(compound.getInt("healthLost"));
        UUID uuid;
        if (compound.containsUuid("MisbehavedPlayer")) {
            uuid = compound.getUuid("MisbehavedPlayer");
        } else {
            String s = compound.getString("MisbehavedPlayer");
            uuid = ServerConfigHandler.getPlayerUuidByName(this.getServer(), s);
        }

        if (uuid != null) {
            try {
                this.setMisbehavedPlayerId(uuid);
            } catch (Throwable ignored) {

            }
        }
    }

    @Nullable
    public UUID getMisbehavedPlayerId() {
        return this.dataTracker.get(MISBEHAVED_PLAYER).orElse(null);
    }

    public void setMisbehavedPlayerId(@Nullable UUID p_184754_1_) {
        this.dataTracker.set(MISBEHAVED_PLAYER, Optional.ofNullable(p_184754_1_));
    }

    @Nullable
    public LivingEntity getMisbehavedPlayer() {
        try {
            UUID uuid = this.getMisbehavedPlayerId();
            return uuid == null ? null : this.getWorld().getPlayerByUuid(uuid);
        } catch (IllegalArgumentException illegalargumentexception) {
            return null;
        }
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState blockState) {
    }

    private int getTimeUntilSunstrike() {
        float damageRatio = 1 - this.getHealthRatio();
        if (damageRatio > 0.6) {
            damageRatio = 0.6f;
        }
        return (int) (SUNSTRIKE_PAUSE_MAX - (damageRatio / 0.6f) * (SUNSTRIKE_PAUSE_MAX - SUNSTRIKE_PAUSE_MIN));
    }

    @Override
    public AbilityType<?, ?>[] getAbilities() {
        return new AbilityType[]{DIE_ABILITY, HURT_ABILITY, BELLY_ABILITY, TALK_ABILITY, SUNSTRIKE_ABILITY, SOLAR_FLARE_ABILITY, SPAWN_ABILITY, SPAWN_SUNBLOCKERS_ABILITY, SOLAR_BEAM_ABILITY, BLESS_ABILITY, SUPERNOVA_ABILITY, ROAR_ABILITY};
    }

    @Override
    public void onDeath(DamageSource cause) {
        super.onDeath(cause);
        List<EntityUmvuthana> umvuthana = this.getEntitiesNearby(EntityUmvuthana.class, 30, 20, 30, 30);
        for (EntityUmvuthana entityUmvuthana : umvuthana) {
            if (entityUmvuthana.isUmvuthiDevoted()) {
                if (entityUmvuthana instanceof EntityUmvuthanaCrane)
                    ((EntityUmvuthanaCrane) entityUmvuthana).hasTriedOrSucceededTeleport = true;
                entityUmvuthana.timeUntilDeath = this.random.nextInt(20);
            }
        }

        super.onDeath(cause);
    }

    public boolean isTrading() {
        return this.dataTracker.get(IS_TRADING);
    }

    public void setTrading(boolean trading) {
        this.dataTracker.set(IS_TRADING, trading);
    }

    public PlayerEntity getCustomer() {
        return this.customer;
    }

    public void setCustomer(PlayerEntity customer) {
        this.setTrading(customer != null);
        this.customer = customer;
    }

    public void openGUI(PlayerEntity playerEntity) {
        this.setCustomer(playerEntity);
        MowziesMobs.PROXY.setReferencedMob(this);
        if (!this.getWorld().isClient && this.getTarget() == null && this.isAlive()) {
            playerEntity.openHandledScreen(new NamedScreenHandlerFactory() {
                @Override
                public ScreenHandler createMenu(int id, PlayerInventory playerInventory, PlayerEntity player) {
                    return new ContainerUmvuthiTrade(id, EntityUmvuthi.this, playerInventory);
                }

                @Override
                public Text getDisplayName() {
                    return EntityUmvuthi.this.getDisplayName();
                }
            });
        }
    }

    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        if (this.canTradeWith(player) && this.getTarget() == null && this.isAlive()) {
            this.openGUI(player);
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }

    public boolean canTradeWith(PlayerEntity player) {
        if (this.isTrading() || this.getHealth() <= 0) {
            return false;
        }
        ItemStack headStack = player.getInventory().armor.get(3);
        return headStack.getItem() instanceof UmvuthanaMask;
    }

    @Override
    public boolean hasBossBar() {
        return ConfigHandler.COMMON.MOBS.UMVUTHI.hasBossBar;
    }

    @Override
    protected BossBar.Color bossBarColor() {
        return BossBar.Color.YELLOW;
    }

    @Override
    protected Identifier getLootTableId() {
        return LootTableHandler.UMVUTHI;
    }

    @Override
    protected ConfigHandler.CombatConfig getCombatConfig() {
        return ConfigHandler.COMMON.MOBS.UMVUTHI.combatConfig;
    }

    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason reason, EntityData livingData, NbtCompound compound) {
        if (reason == SpawnReason.SPAWN_EGG) {
            // Try to guess which player spawned Umvuthi, rotate towards them
            List<PlayerEntity> players = this.getPlayersNearby(5, 5, 5, 5);
            if (!players.isEmpty()) {
                PlayerEntity closestPlayer = players.get(0);
                float closestPlayerDist = 6;
                for (PlayerEntity player : players) {
                    if (player.getMainHandStack().getItem() == ItemHandler.UMVUTHI_SPAWN_EGG || player.getMainHandStack().getItem() == ItemHandler.UMVUTHI_SPAWN_EGG) {
                        float thisDist = this.distanceTo(player);
                        if (thisDist < closestPlayerDist) {
                            closestPlayer = player;
                            closestPlayerDist = thisDist;
                        }
                    }
                }
                float angle = (float) this.getAngleBetweenEntities(this, closestPlayer) + 225;
                int direction = (int) (angle / 90) % 4 + 1;
                this.setDirection(direction);
            }
        }
        if (reason != SpawnReason.STRUCTURE) this.setPositionTarget(this.getBlockPos(), -1);
        return super.initialize(world, difficulty, reason, livingData, compound);
    }

    @Override
    public boolean handleFallDamage(float p_147187_, float p_147188_, DamageSource p_147189_) {
        return false;
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
    public SoundEvent getBossMusic() {
        return MMSounds.MUSIC_UMVUTHI_THEME;
    }

    @Override
    public boolean resetHealthOnPlayerRespawn() {
        return ConfigHandler.COMMON.MOBS.UMVUTHI.resetHealthWhenRespawn;
    }

    public static class SunstrikeAbility extends Ability<EntityUmvuthi> {
        private static final RawAnimation SUN_STRIKE_ANIM = RawAnimation.begin().then("sun_strike", Animation.LoopType.PLAY_ONCE);
        private static final int STARTUP_DURATION = 9;
        public double prevX;
        public double prevZ;
        protected LivingEntity entityTarget;
        private int newX;
        private int newZ;
        private int y;

        public SunstrikeAbility(AbilityType abilityType, EntityUmvuthi user) {
            super(abilityType, user, new AbilitySection[]{
                    new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.STARTUP, STARTUP_DURATION),
                    new AbilitySection.AbilitySectionInstant(AbilitySection.AbilitySectionType.ACTIVE),
                    new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.RECOVERY, 12)
            });
        }

        @Override
        public void start() {
            super.start();
            this.entityTarget = this.getUser().getTarget();
            if (this.entityTarget != null) {
                this.prevX = this.entityTarget.getX();
                this.prevZ = this.entityTarget.getZ();
            }
            this.playAnimation(SUN_STRIKE_ANIM);
        }

        @Override
        public void tickUsing() {
            super.tickUsing();
            if (!this.getUser().getWorld().isClient()) {
                if (this.entityTarget == null) {
                    return;
                }

                if (this.getTicksInUse() == STARTUP_DURATION - 2) {
                    double x = this.entityTarget.getX();
                    this.y = MathHelper.floor(this.entityTarget.getY() - 1);
                    double z = this.entityTarget.getZ();
                    double vx = (x - this.prevX) / STARTUP_DURATION;
                    double vz = (z - this.prevZ) / STARTUP_DURATION;
                    int t = EntitySunstrike.STRIKE_EXPLOSION + 3;
                    this.newX = MathHelper.floor(x + vx * t);
                    this.newZ = MathHelper.floor(z + vz * t);
                    double dx = this.newX - this.getUser().getX();
                    double dz = this.newZ - this.getUser().getZ();
                    double dist2ToUmvuthi = dx * dx + dz * dz;
                    if (dist2ToUmvuthi < 3) {
                        this.newX = MathHelper.floor(this.entityTarget.getX());
                        this.newZ = MathHelper.floor(this.entityTarget.getZ());
                    }
                    for (int i = 0; i < 5; i++) {
                        if (!this.getUser().getWorld().isSkyVisibleAllowingSea(new BlockPos(this.newX, this.y, this.newZ))) {
                            this.y++;
                        } else {
                            break;
                        }
                    }
                }

                if (this.getTicksInUse() < STARTUP_DURATION - 2) {
                    this.getUser().getLookControl().lookAt(this.entityTarget, 30, 30);
                }
                if (this.getTicksInUse() >= STARTUP_DURATION - 2) {
                    this.getUser().getLookControl().lookAt(this.newX, this.y + this.entityTarget.getStandingEyeHeight(), this.newZ, 50, 50);
                }
            }
        }

        @Override
        protected void beginSection(AbilitySection section) {
            super.beginSection(section);
            if (!this.getUser().getWorld().isClient()) {
                if (section.sectionType == AbilitySection.AbilitySectionType.ACTIVE) {
                    this.getUser().playSound(MMSounds.ENTITY_UMVUTHI_ATTACK, 1.4f, 1);
                    EntitySunstrike sunstrike = new EntitySunstrike(EntityHandler.SUNSTRIKE, this.getUser().getWorld(), this.getUser(), this.newX, this.y, this.newZ);
                    sunstrike.onSummon();
                    this.getUser().getWorld().spawnEntity(sunstrike);
                }
            }
        }
    }

    public static class SolarBeamAbility extends Ability<EntityUmvuthi> {
        private static final RawAnimation SOLAR_BEAM_ANIM = RawAnimation.begin().then("solar_beam", Animation.LoopType.PLAY_ONCE);
        protected LivingEntity entityTarget;
        private EntitySolarBeam solarBeam;

        public SolarBeamAbility(AbilityType abilityType, EntityUmvuthi user) {
            super(abilityType, user, new AbilitySection[]{
                    new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.STARTUP, 22),
                    new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.ACTIVE, 68),
                    new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.RECOVERY, 10)
            });
        }

        @Override
        public void start() {
            super.start();
            this.entityTarget = this.getUser().getTarget();
            this.playAnimation(SOLAR_BEAM_ANIM);
        }

        @Override
        public void tickUsing() {
            super.tickUsing();
            float radius1 = 0.8f;
            EntityUmvuthi entity = this.getUser();
            if (this.getTicksInUse() == 4 && !entity.getWorld().isClient) {
                this.solarBeam = new EntitySolarBeam(EntityHandler.SOLAR_BEAM, this.getUser().getWorld(), entity, entity.getX() + radius1 * Math.sin(-entity.getYaw() * Math.PI / 180), entity.getY() + 1.4, entity.getZ() + radius1 * Math.cos(-entity.getYaw() * Math.PI / 180), (float) ((entity.headYaw + 90) * Math.PI / 180), (float) (-entity.getPitch() * Math.PI / 180), 55);
                entity.getWorld().spawnEntity(this.solarBeam);
            }
            if (this.getTicksInUse() >= 22) {
                if (this.entityTarget != null) {
                    entity.getLookControl().lookAt(this.entityTarget.getX(), this.entityTarget.getY() + this.entityTarget.getHeight() / 2, this.entityTarget.getZ(), 2, 90);
                }
            }
        }
    }

    public static class SolarFlareAbility extends Ability<EntityUmvuthi> {
        private static final RawAnimation FLARE_ANIM = RawAnimation.begin().then("flare", Animation.LoopType.PLAY_ONCE);
        public static AbilitySection[] SECTION_TRACK = new AbilitySection[]{
                new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.STARTUP, 12),
                new AbilitySection.AbilitySectionInstant(AbilitySection.AbilitySectionType.ACTIVE),
                new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.RECOVERY, 18)
        };

        public SolarFlareAbility(AbilityType abilityType, EntityUmvuthi user) {
            super(abilityType, user, SECTION_TRACK);
        }

        @Override
        public void start() {
            super.start();
            this.getUser().playSound(MMSounds.ENTITY_UMVUTHI_BURST, 1.7f, 1.5f);
            this.playAnimation(FLARE_ANIM);
        }

        @Override
        protected void beginSection(AbilitySection section) {
            super.beginSection(section);
            if (section.sectionType == AbilitySection.AbilitySectionType.ACTIVE) {
                EntityUmvuthi entity = this.getUser();
                float radius = 4f;
                List<LivingEntity> hit = entity.getEntityLivingBaseNearby(radius, 2 * radius, radius, radius);
                for (LivingEntity aHit : hit) {
                    if (aHit instanceof LeaderSunstrikeImmune) {
                        continue;
                    }
                    entity.doHurtTarget(aHit, 1f, 3f);
                    if (!aHit.isInvulnerable()) {
                        if (aHit instanceof PlayerEntity && ((PlayerEntity) aHit).getAbilities().invulnerable) continue;
                        double knockback = 3;
                        double angle = entity.getAngleBetweenEntities(entity, aHit);
                        double x = knockback * Math.cos(Math.toRadians(angle - 90));
                        double z = knockback * Math.sin(Math.toRadians(angle - 90));
                        aHit.setVelocity(x, 0.3, z);
                        if (aHit instanceof ServerPlayerEntity) {
                            ((ServerPlayerEntity) aHit).networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(aHit));
                        }
                    }
                }
            }
        }
    }

    public static class SpawnFollowersAbility extends Ability<EntityUmvuthi> {
        private static final RawAnimation SPAWN_STRIX_ANIM = RawAnimation.begin().then("spawn_strix", Animation.LoopType.PLAY_ONCE);
        private final boolean spawnSunblockers;

        public SpawnFollowersAbility(AbilityType abilityType, EntityUmvuthi user, boolean spawnSunblockers) {
            super(abilityType, user, new AbilitySection[]{
                    new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.STARTUP, 6),
                    new AbilitySection.AbilitySectionInstant(AbilitySection.AbilitySectionType.ACTIVE),
                    new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.RECOVERY, 11),
                    new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.STARTUP, 6),
                    new AbilitySection.AbilitySectionInstant(AbilitySection.AbilitySectionType.ACTIVE),
                    new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.RECOVERY, 11),
                    new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.STARTUP, 6),
                    new AbilitySection.AbilitySectionInstant(AbilitySection.AbilitySectionType.ACTIVE),
                    new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.RECOVERY, 11)
            });
            this.spawnSunblockers = spawnSunblockers;
        }

        @Override
        public void start() {
            super.start();
            this.getUser().umvuthanaSpawnCount++;
            this.getUser().playSound(MMSounds.ENTITY_UMVUTHANA_INHALE, 1.2f, 0.5f);
            this.playAnimation(SPAWN_STRIX_ANIM);
        }

        @Override
        protected void beginSection(AbilitySection section) {
            super.beginSection(section);
            EntityUmvuthi entity = this.getUser();
            if (section.sectionType == AbilitySection.AbilitySectionType.STARTUP) {
                this.getUser().playSound(MMSounds.ENTITY_UMVUTHANA_INHALE, 1.2f, 0.5f);
                this.playAnimation(SPAWN_STRIX_ANIM);
            }
            if (section.sectionType == AbilitySection.AbilitySectionType.ACTIVE) {
                if (!this.getUser().getWorld().isClient()) {
                    entity.playSound(MMSounds.ENTITY_UMVUTHI_BELLY, 1.5f, 1);
                    entity.playSound(MMSounds.ENTITY_UMVUTHANA_BLOWDART, 1.5f, 0.5f);
                    double angle = entity.headYaw;
                    if (angle < 0) {
                        angle = angle + 360;
                    }
                    if (angle - entity.getYaw() > 70) {
                        angle = 70 + entity.getYaw();
                    } else if (angle - entity.getYaw() < -70) {
                        angle = -70 + entity.getYaw();
                    }
                    EntityUmvuthanaMinion umvuthana;
                    if (this.spawnSunblockers) {
                        umvuthana = new EntityUmvuthanaCrane(EntityHandler.UMVUTHANA_CRANE, entity.getWorld());
                        ((EntityUmvuthanaCrane) umvuthana).hasTriedOrSucceededTeleport = false;
                    } else
                        umvuthana = new EntityUmvuthanaMinion(EntityHandler.UMVUTHANA_MINION, entity.getWorld());
                    umvuthana.updatePositionAndAngles(entity.getX() + 2 * Math.sin(-angle * (Math.PI / 180)), entity.getY() + 2.5, entity.getZ() + 2 * Math.cos(-angle * (Math.PI / 180)), entity.headYaw, 0);
                    umvuthana.setActive(false);
                    umvuthana.active = false;
                    umvuthana.initialize((ServerWorldAccess) entity.getEntityWorld(), entity.getWorld().getLocalDifficulty(umvuthana.getBlockPos()), SpawnReason.MOB_SUMMONED, null, null);
                    umvuthana.setPositionTarget(entity.getPositionTarget(), 25);
                    if (entity.getScoreboardTeam() instanceof Team) {
                        umvuthana.getWorld().getScoreboard().addPlayerToTeam(umvuthana.getEntityName(), (Team) entity.getScoreboardTeam());
                    }
                    entity.getWorld().spawnEntity(umvuthana);
                    umvuthana.setVelocity(0.7 * Math.sin(-angle * (Math.PI / 180)), 0.5, 0.7 * Math.cos(-angle * (Math.PI / 180)));
                    if (!this.spawnSunblockers) {
                        umvuthana.setTarget(entity.getTarget());
                        if (entity.getTarget() instanceof PlayerEntity) {
                            umvuthana.setMisbehavedPlayerId(entity.getTarget().getUuid());
                        }
                    }
                }
            }
        }

        @Override
        protected void endSection(AbilitySection section) {
            super.endSection(section);
            if (section.sectionType == AbilitySection.AbilitySectionType.RECOVERY) {
                if (this.getUser().targetDistance <= 6 && this.getUser().getTarget() != null && !this.spawnSunblockers) {
                    this.interrupt();
                }
            }
        }
    }

    public static class SupernovaAbility extends Ability<EntityUmvuthi> {
        private static final RawAnimation SUPERNOVA_ANIM = RawAnimation.begin().then("supernova", Animation.LoopType.PLAY_ONCE);
        private static final ParticleComponent.KeyTrack superNovaKeyTrack1 = new ParticleComponent.KeyTrack(
                new float[]{0, 25f, 32f, 0},
                new float[]{0, 0.6f, 0.85f, 1}
        );
        private static final ParticleComponent.KeyTrack superNovaKeyTrack2 = ParticleComponent.KeyTrack.oscillate(0, 7, 24);
        public static AbilitySection[] SECTION_TRACK = new AbilitySection[]{
                new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.STARTUP, 44),
                new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.ACTIVE, 40),
                new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.RECOVERY, 16)
        };

        public SupernovaAbility(AbilityType abilityType, EntityUmvuthi user) {
            super(abilityType, user, SECTION_TRACK);
        }

        @Environment(EnvType.CLIENT)
        public static void superNovaEffects(Ability activeAbility, Vec3d[] pinLocation, World level) {
            // Darken sky
            PlayerEntity clientPlayer = MinecraftClient.getInstance().player;
            if (clientPlayer == null) return;
            double distToCaster = activeAbility.getUser().getPos().squaredDistanceTo(clientPlayer.getPos());
            if (distToCaster < 1000) {
                MinecraftClient.getInstance().gameRenderer.skyDarkness += 0.06f;
                if (MinecraftClient.getInstance().gameRenderer.skyDarkness > 1.0f)
                    MinecraftClient.getInstance().gameRenderer.skyDarkness = 1.0f;
            }

            // Particle effects
            if (pinLocation == null || pinLocation.length == 0 || pinLocation[0] == null) return;
            int ticksInUse = activeAbility.getTicksInUse();
            LivingEntity user = activeAbility.getUser();
            Random random = user.getRandom();

            if (ticksInUse == 1) {
                AdvancedParticleBase.spawnParticle(level, ParticleHandler.SUN, user.getX(), user.getY(), user.getZ(), 0, 0, 0, true, 0, 0, 0, 0, 0F, 1, 1, 1, 1, 1, 33, true, true, new ParticleComponent[]{
                        new ParticleComponent.PinLocation(pinLocation),
                        new ParticleComponent.PropertyControl(EnumParticleProperty.SCALE, superNovaKeyTrack1, false),
                        new ParticleComponent.PropertyControl(EnumParticleProperty.SCALE, superNovaKeyTrack2, true)
                });
            }
            if (ticksInUse == 33) {
                AdvancedParticleBase.spawnParticle(level, ParticleHandler.SUN_NOVA, user.getX(), user.getY(), user.getZ(), 0, 0, 0, true, 0, 0, 0, 0, 20F, 1, 1, 1, 0, 1, 13, true, true, new ParticleComponent[]{
                        new ParticleComponent.PinLocation(pinLocation),
                        new ParticleComponent.PropertyControl(EnumParticleProperty.SCALE, new ParticleComponent.KeyTrack(
                                new float[]{11f, 7f, 5.5f, 1f, 30},
                                new float[]{0, 0.15f, 0.8f, 0.89f, 1}
                        ), false),
                        new ParticleComponent.PropertyControl(EnumParticleProperty.ALPHA, new ParticleComponent.KeyTrack(
                                new float[]{0f, 1f, 1f, 0f},
                                new float[]{0, 0.15f, 0.89f, 1}
                        ), false),
                        new ParticleComponent.PropertyControl(EnumParticleProperty.PARTICLE_ANGLE, ParticleComponent.KeyTrack.startAndEnd(0f, -6f), false)
                });
            }
            if (ticksInUse == 32) {
                AdvancedParticleBase.spawnParticle(level, ParticleHandler.FLARE, user.getX(), user.getY(), user.getZ(), 0, 0, 0, true, 0, 0, 0, 0, 5F, 1, 1, 1, 0.7, 1, 3, true, true, new ParticleComponent[]{
                        new ParticleComponent.PinLocation(pinLocation),
                        new ParticleComponent.PropertyControl(EnumParticleProperty.POS_Y, ParticleComponent.constant(-0.15f), true),
                        new ParticleComponent.PropertyControl(EnumParticleProperty.SCALE, new ParticleComponent.KeyTrack(
                                new float[]{0f, 22f, 0f},
                                new float[]{0, 0.2f, 1}
                        ), false)
                });
            }
            if (ticksInUse > 30 && ticksInUse < 41) {
                for (int i = 0; i < 6; i++) {
                    float phaseOffset = random.nextFloat();
                    double value = random.nextDouble() * 0.3 + 0.05;
                    AdvancedParticleBase.spawnParticle(level, ParticleHandler.PIXEL, pinLocation[0].x, pinLocation[0].y, pinLocation[0].z, 0, 0, 0, true, 0, 0, 0, 0, 5F, value, value, value, 1, 1, 6, false, true, new ParticleComponent[]{
                            new ParticleComponent.PropertyControl(EnumParticleProperty.SCALE, new ParticleComponent.KeyTrack(
                                    new float[]{0f, 3f},
                                    new float[]{0, 0.2f}
                            ), false),
                            new ParticleComponent.Orbit(pinLocation, ParticleComponent.KeyTrack.startAndEnd(0 + phaseOffset, -0.4f + phaseOffset), ParticleComponent.KeyTrack.startAndEnd(0.5f + random.nextFloat(), 0), ParticleComponent.constant(0), ParticleComponent.constant(0), ParticleComponent.constant(0), true),
                    });
                }
            }
            if (ticksInUse > 1 && ticksInUse < 27) {
                for (int i = 0; i < 6; i++) {
                    Vec3d particlePos = new Vec3d(random.nextFloat() * 5, 0, 0);
                    particlePos = particlePos.rotateY((float) (random.nextFloat() * 2 * Math.PI));
                    particlePos = particlePos.rotateX((float) (random.nextFloat() * 2 * Math.PI));
                    particlePos = particlePos.add(pinLocation[0]);
                    double value = random.nextDouble() * 0.5 + 0.1;
                    AdvancedParticleBase.spawnParticle(level, ParticleHandler.PIXEL, particlePos.x, particlePos.y, particlePos.z, 0, 0, 0, true, 0, 0, 0, 0, 5F, value, value, value, 1, 1, 7, false, true, new ParticleComponent[]{
                            new ParticleComponent.Attractor(pinLocation, 1.1f, 1f, ParticleComponent.Attractor.EnumAttractorBehavior.EXPONENTIAL),
                            new ParticleComponent.PropertyControl(EnumParticleProperty.SCALE, new ParticleComponent.KeyTrack(
                                    new float[]{0f, 3.5f},
                                    new float[]{0, 0.2f}
                            ), false)
                    });
                }
            }
            float timeFrac = Math.min((float) ticksInUse / 20f, 1f);
            if (ticksInUse > 1 && ticksInUse < 25 && ticksInUse % (int) (4 * (1 - timeFrac) + 1) == 0) {
                AdvancedParticleBase.spawnParticle(level, ParticleHandler.RING_SPARKS, user.getX(), user.getY(), user.getZ(), 0, 0, 0, true, 0, 0, 0, random.nextFloat() * (float) Math.PI * 2, 5F, 1, 1, 1, 1, 1, 6 + random.nextFloat() * 3, true, true, new ParticleComponent[]{
                        new ParticleComponent.PinLocation(pinLocation),
                        new ParticleComponent.PropertyControl(EnumParticleProperty.SCALE, ParticleComponent.KeyTrack.startAndEnd(10f + 20f * timeFrac * timeFrac + 10f * random.nextFloat() * timeFrac, 0f), false),
                        new ParticleComponent.PropertyControl(EnumParticleProperty.ALPHA, ParticleComponent.KeyTrack.startAndEnd(0f, 0.7f), false)
                });
            }
            if (ticksInUse == 14) {
                AdvancedParticleBase.spawnParticle(level, ParticleHandler.FLARE, user.getX(), user.getY(), user.getZ(), 0, 0, 0, true, 0, 0, 0, 0, 5F, 1, 1, 1, 1, 1, 18, true, true, new ParticleComponent[]{
                        new ParticleComponent.PinLocation(pinLocation),
                        new ParticleComponent.PropertyControl(EnumParticleProperty.POS_Y, ParticleComponent.constant(-0.1f), true),
                        new ParticleComponent.PropertyControl(EnumParticleProperty.SCALE, new ParticleComponent.KeyTrack(
                                new float[]{0f, 35f, 0f},
                                new float[]{0, 0.8f, 1}
                        ), false),
                        new ParticleComponent.PropertyControl(EnumParticleProperty.SCALE, new ParticleComponent.Oscillator(-5, 5, 42, 0), true)
                });
            }

            if (ticksInUse == 32) {
                AdvancedParticleBase.spawnParticle(level, ParticleHandler.BURST_IN, user.getX(), user.getY(), user.getZ(), 0, 0, 0, false, 0, Math.PI / 2f, 0, 0, 5F, 0, 0, 0, 1, 1, 10, true, true, new ParticleComponent[]{
                        new ParticleComponent.PinLocation(pinLocation),
                        new ParticleComponent.PropertyControl(EnumParticleProperty.SCALE, ParticleComponent.KeyTrack.startAndEnd(25f, 0f), false),
                        new ParticleComponent.PropertyControl(EnumParticleProperty.ALPHA, ParticleComponent.KeyTrack.startAndEnd(0f, 1f), false),
                        new ParticleComponent.PropertyControl(EnumParticleProperty.SCALE, new ParticleComponent.Oscillator(-2, 2, 42, 0), true),
                });
            }

            if (ticksInUse == 44) {
                float scale = 85f;
                AdvancedParticleBase.spawnParticle(level, ParticleHandler.RING_BIG, pinLocation[0].x, pinLocation[0].y, pinLocation[0].z, 0, 0, 0, false, 0, Math.PI / 2f, 0, 0, 5F, 1, 1, 1, 1, 1, 40, true, true, new ParticleComponent[]{
                        new ParticleComponent.PropertyControl(EnumParticleProperty.SCALE, new ParticleComponent.KeyTrack(
                                new float[]{0.0f * scale, 0.59f * scale, 0.87f * scale, 0.974f * scale, 0.998f * scale, scale},
                                new float[]{0, 0.2f, 0.4f, 0.6f, 0.8f, 1f}
                        ), false),
                        new ParticleComponent.PropertyControl(EnumParticleProperty.ALPHA, ParticleComponent.KeyTrack.startAndEnd(1f, 0f), false),
                });
                scale = 120f;
                AdvancedParticleBase.spawnParticle(level, ParticleHandler.GLOW, pinLocation[0].x, pinLocation[0].y, pinLocation[0].z, 0, 0, 0, true, 0, 0, 0, 0, 5F, 0.95, 0.9, 0.35, 1, 1, 40, true, true, new ParticleComponent[]{
                        new ParticleComponent.PropertyControl(EnumParticleProperty.SCALE, new ParticleComponent.KeyTrack(
                                new float[]{0.0f * scale, 0.59f * scale, 0.87f * scale, 0.974f * scale, 0.998f * scale, scale},
                                new float[]{0, 0.2f, 0.4f, 0.6f, 0.8f, 1f}
                        ), false),
                        new ParticleComponent.PropertyControl(EnumParticleProperty.ALPHA, ParticleComponent.KeyTrack.startAndEnd(1f, 0f), false),
                });
            }
        }

        @Override
        public void start() {
            super.start();
            this.getUser().playSound(MMSounds.ENTITY_SUPERNOVA_START, 3f, 1f);
            this.playAnimation(SUPERNOVA_ANIM);
        }

        @Override
        public void tickUsing() {
            super.tickUsing();
            if (this.getTicksInUse() == 30) {
                this.getUser().playSound(MMSounds.ENTITY_SUPERNOVA_BLACKHOLE, 2f, 1.2f);
            }

            if (this.getTicksInUse() < 30) {
                List<LivingEntity> entities = this.getUser().getEntityLivingBaseNearby(16, 16, 16, 16);
                for (LivingEntity inRange : entities) {
                    if (inRange instanceof LeaderSunstrikeImmune) continue;
                    if (inRange instanceof PlayerEntity && ((PlayerEntity) inRange).getAbilities().invulnerable)
                        continue;
                    Vec3d diff = inRange.getPos().subtract(this.getUser().getPos().add(0, 3, 0));
                    diff = diff.normalize().multiply(0.03);
                    inRange.setVelocity(inRange.getVelocity().subtract(diff));

                    if (inRange.getY() < this.getUser().getY() + 3)
                        inRange.setVelocity(inRange.getVelocity().add(0, 0.075, 0));
                }
            }

            if (this.getCurrentSection().sectionType == AbilitySection.AbilitySectionType.STARTUP) {
                this.getUser().addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 2, 1, false, false));
            }

            if (this.getTicksInUse() == 40) {
                this.getUser().playSound(MMSounds.ENTITY_UMVUTHI_ROAR, 3f, 1f);
            }

            if (this.getLevel().isClient) {
                superNovaEffects(this, this.getUser().betweenHandPos, this.getLevel());
            }
        }

        @Override
        protected void beginSection(AbilitySection section) {
            super.beginSection(section);
            if (section.sectionType == AbilitySection.AbilitySectionType.ACTIVE) {
                if (!this.getUser().getWorld().isClient) {
                    Vec3d offset = new Vec3d(1.1f, 0, 0);
                    offset = offset.rotateY((float) Math.toRadians(-this.getUser().getYaw() - 90));
                    EntitySuperNova superNova = new EntitySuperNova(EntityHandler.SUPER_NOVA, this.getUser().getWorld(), this.getUser(), this.getUser().getX() + offset.x, this.getUser().getY() + 0.05, this.getUser().getZ() + offset.z);
                    this.getUser().getWorld().spawnEntity(superNova);
                }
            }
        }
    }
}
