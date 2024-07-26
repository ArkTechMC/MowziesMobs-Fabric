package com.bobmowzie.mowziesmobs.server.entity.grottol;

import com.bobmowzie.mowziesmobs.client.particle.ParticleHandler;
import com.bobmowzie.mowziesmobs.server.advancement.AdvancementHandler;
import com.bobmowzie.mowziesmobs.server.ai.EntityAIGrottolFindMinecart;
import com.bobmowzie.mowziesmobs.server.ai.MMAIAvoidEntity;
import com.bobmowzie.mowziesmobs.server.ai.MMEntityMoveHelper;
import com.bobmowzie.mowziesmobs.server.ai.MMPathNavigateGround;
import com.bobmowzie.mowziesmobs.server.ai.animation.AnimationDieAI;
import com.bobmowzie.mowziesmobs.server.ai.animation.AnimationTakeDamage;
import com.bobmowzie.mowziesmobs.server.ai.animation.SimpleAnimationAI;
import com.bobmowzie.mowziesmobs.server.block.ICopiedBlockProperties;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import com.bobmowzie.mowziesmobs.server.entity.MowzieLLibraryEntity;
import com.bobmowzie.mowziesmobs.server.entity.grottol.ai.EntityAIGrottolIdle;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import com.bobmowzie.mowziesmobs.server.loot.LootTableHandler;
import com.bobmowzie.mowziesmobs.server.potion.EffectHandler;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import com.bobmowzie.mowziesmobs.server.tag.TagHandler;
import com.iafenvoy.uranus.animation.Animation;
import com.iafenvoy.uranus.animation.AnimationHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WanderAroundGoal;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.entity.vehicle.MinecartEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

/**
 * Created by BobMowzie on 7/3/2018.
 */
public class EntityGrottol extends MowzieLLibraryEntity {
    public static final Animation DIE_ANIMATION = Animation.create(73);
    public static final Animation HURT_ANIMATION = Animation.create(10);
    public static final Animation IDLE_ANIMATION = EntityAIGrottolIdle.animation();
    public static final Animation BURROW_ANIMATION = Animation.create(20);
    private static final Animation[] ANIMATIONS = {
            DIE_ANIMATION,
            HURT_ANIMATION,
            IDLE_ANIMATION,
            BURROW_ANIMATION
    };
    private static final TrackedData<Boolean> DEEPSLATE = DataTracker.registerData(EntityGrottol.class, TrackedDataHandlerRegistry.BOOLEAN);
    private final BlackPinkRailLine reader = BlackPinkRailLine.create();
    public int fleeTime = 0;
    private int timeSinceFlee = 50;
    private int timeSinceMinecart = 0;
    private EnumDeathType death = EnumDeathType.NORMAL;

    private int timeSinceDeflectSound = 0;

    public EntityGrottol(EntityType<? extends EntityGrottol> type, World world) {
        super(type, world);
        this.experiencePoints = 15;
        this.setStepHeight(1.15F);

        this.moveControl = new MMEntityMoveHelper(this, 45);
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MowzieEntity.createAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 20)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 1);
    }

    public static boolean isBlockRail(Block block) {
        return block == Blocks.RAIL || block == Blocks.ACTIVATOR_RAIL || block == Blocks.POWERED_RAIL || block == Blocks.DETECTOR_RAIL;
    }

    private static boolean isMinecart(Entity entity) {
        return entity instanceof MinecartEntity;
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.setPathfindingPenalty(PathNodeType.DANGER_OTHER, 1);
        this.setPathfindingPenalty(PathNodeType.WATER, 3);
        this.setPathfindingPenalty(PathNodeType.WATER_BORDER, 3);
        this.setPathfindingPenalty(PathNodeType.LAVA, 1);
        this.setPathfindingPenalty(PathNodeType.DANGER_FIRE, 1);
        this.setPathfindingPenalty(PathNodeType.DAMAGE_FIRE, 1);
        this.setPathfindingPenalty(PathNodeType.DANGER_OTHER, 1);
        this.setPathfindingPenalty(PathNodeType.DAMAGE_OTHER, 1);
        this.goalSelector.add(3, new SwimGoal(this));
        this.goalSelector.add(4, new WanderAroundGoal(this, 0.3));
        this.goalSelector.add(1, new EntityAIGrottolFindMinecart(this));
        this.goalSelector.add(2, new MMAIAvoidEntity<EntityGrottol, PlayerEntity>(this, PlayerEntity.class, 16f, 0.5, 0.7) {
            private int fleeCheckCounter = 0;

            @Override
            protected void onSafe() {
                this.fleeCheckCounter = 0;
            }

            @Override
            protected void onPathNotFound() {
                if (this.fleeCheckCounter < 4) {
                    this.fleeCheckCounter++;
                } else if (EntityGrottol.this.getAnimation() == NO_ANIMATION) {
                    AnimationHandler.INSTANCE.sendAnimationMessage(this.entity, EntityGrottol.BURROW_ANIMATION);
                }
            }

            @Override
            public void tick() {
                super.tick();
                this.entity.fleeTime++;
            }

            @Override
            public void stop() {
                super.stop();
                this.entity.timeSinceFlee = 0;
                this.fleeCheckCounter = 0;
            }
        });
        this.goalSelector.add(8, new LookAroundGoal(this));
        this.goalSelector.add(8, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(1, new AnimationTakeDamage<>(this));
        this.goalSelector.add(1, new AnimationDieAI<>(this));
        this.goalSelector.add(5, new EntityAIGrottolIdle(this));
        this.goalSelector.add(2, new SimpleAnimationAI<>(this, BURROW_ANIMATION, false));
    }

    @Override
    public int getSafeFallDistance() {
        return 256;
    }

//    @Override
//    public float getWalkTargetValue(BlockPos pos) {
//        return (float) pos.distSqr(this.position(), true);
//    }

    @Override
    public int getLimitPerChunk() {
        return 1;
    }

    @Override
    protected EntityNavigation createNavigation(World world) {
        return new MMPathNavigateGround(this, world);
    }

    @Override
    public boolean isPushedByFluids() {
        return false;
    }

    @Override
    protected float getBaseMovementSpeedMultiplier() {
        return 1;
    }

    @Override
    public boolean canBreatheInWater() {
        return true;
    }

    @Override
    public boolean doesRenderOnFire() {
        return false;
    }

    @Override
    public boolean canMoveVoluntarily() {
        return super.canMoveVoluntarily() && !this.isInMinecart();
    }

    @Override
    protected ConfigHandler.SpawnConfig getSpawnConfig() {
        return ConfigHandler.COMMON.MOBS.GROTTOL.spawnConfig;
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.getDataTracker().startTracking(DEEPSLATE, false);
    }

    @Override
    public boolean canSpawn(WorldAccess world, SpawnReason reason) {
        return this.getEntitiesNearby(EntityGrottol.class, 20, 20, 20, 20).isEmpty() && super.canSpawn(world, reason);
    }

    @Override
    public boolean handleAttack(Entity entity) {
        if (entity instanceof PlayerEntity player) {
            if (EnchantmentHelper.getLevel(Enchantments.SILK_TOUCH, player.getMainHandStack()) > 0) {
                if (!this.getWorld().isClient && this.isAlive()) {
                    this.dropStack(ItemHandler.CAPTURED_GROTTOL.create(this), 0.0F);
                    BlockState state = Blocks.STONE.getDefaultState();
                    BlockSoundGroup sound = state.getBlock().getSoundGroup(state);
                    this.getWorld().playSound(
                            null,
                            this.getX(), this.getY(), this.getZ(),
                            sound.getBreakSound(),
                            this.getSoundCategory(),
                            (sound.getVolume() + 1.0F) / 2.0F,
                            sound.getPitch() * 0.8F
                    );
                    if (this.getWorld() instanceof ServerWorld) {
                        ((ServerWorld) this.getWorld()).spawnParticles(new BlockStateParticleEffect(ParticleTypes.BLOCK, state),
                                this.getX(), this.getY() + this.getHeight() / 2.0D, this.getZ(),
                                32,
                                this.getWidth() / 4.0F, this.getHeight() / 4.0F, this.getWidth() / 4.0F,
                                0.05D
                        );
                    }
                    this.discard();
                    if (player instanceof ServerPlayerEntity)
                        AdvancementHandler.GROTTOL_KILL_SILK_TOUCH_TRIGGER.trigger((ServerPlayerEntity) player);
                }
                return true;
            }
        }
        return super.handleAttack(entity);
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        Entity entity = source.getAttacker();
        if (entity instanceof PlayerEntity player && !source.isIn(DamageTypeTags.IS_PROJECTILE)) {
            if (player.getMainHandStack().isSuitableFor(Blocks.DIAMOND_ORE.getDefaultState()) || player.getMainHandStack().isIn(TagHandler.CAN_HIT_GROTTOL)) {
                if (EnchantmentHelper.getLevel(Enchantments.FORTUNE, player.getMainHandStack()) > 0) {
                    this.death = EnumDeathType.FORTUNE_PICKAXE;
                    if (player instanceof ServerPlayerEntity)
                        AdvancementHandler.GROTTOL_KILL_FORTUNE_TRIGGER.trigger((ServerPlayerEntity) player);
                } else {
                    this.death = EnumDeathType.PICKAXE;
                }
                return super.damage(source, this.getHealth());
            } else {
                if (this.timeSinceDeflectSound >= 5) {
                    this.timeSinceDeflectSound = 0;
                    this.playSound(MMSounds.ENTITY_GROTTOL_UNDAMAGED, 0.4F, 2.0F);
                }
                return false;
            }
        } else if (entity instanceof MobEntity) {
            return false;
        }
        return super.damage(source, amount);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.getWorld().isClient) {
            Entity e = this.getVehicle();
            if (isMinecart(e)) {
                AbstractMinecartEntity minecart = (AbstractMinecartEntity) e;
                this.reader.accept(minecart);
                boolean onRail = isBlockRail(this.getWorld().getBlockState(e.getBlockPos()).getBlock());
                if ((this.timeSinceMinecart > 3 && e.getVelocity().length() < 0.001) || !onRail) {
                    minecart.removeAllPassengers();
                    this.timeSinceMinecart = 0;
                } else if (onRail) {
                    if (minecart.getVelocity().length() < 0.001)
                        minecart.setVelocity(minecart.getRotationVecClient().multiply(2.7));
                    else minecart.setVelocity(minecart.getVelocity().normalize().multiply(2.7));
                    this.timeSinceMinecart++;
                }
            }
        }
//        if (ticksExisted == 1) System.out.println("Grottle at " + getPosition());

        //Sparkle particles
        if (this.getWorld().isClient && this.isAlive() && this.random.nextInt(15) == 0) {
            double x = this.getX() + 0.5f * (2 * this.random.nextFloat() - 1f);
            double y = this.getY() + 0.8f + 0.3f * (2 * this.random.nextFloat() - 1f);
            double z = this.getZ() + 0.5f * (2 * this.random.nextFloat() - 1f);
            if (this.isBlackPinkInYourArea()) {
                this.getWorld().addParticle(ParticleTypes.NOTE, x, y, z, this.random.nextDouble() / 2, 0, 0);
            } else {
                this.getWorld().addParticle(ParticleHandler.SPARKLE, x, y, z, 0, 0, 0);
            }
        }

        //Footstep Sounds
        float moveX = (float) (this.getX() - this.prevX);
        float moveZ = (float) (this.getZ() - this.prevZ);
        float speed = MathHelper.sqrt(moveX * moveX + moveZ * moveZ);
        if (this.frame % 6 == 0 && speed > 0.05) {
            this.playSound(MMSounds.ENTITY_GROTTOL_STEP, 1F, 1.8f);
        }

        if (this.timeSinceFlee < 50) {
            this.timeSinceFlee++;
        } else {
            this.fleeTime = 0;
        }

        if (this.timeSinceDeflectSound < 5) this.timeSinceDeflectSound++;

        // AI Task
        if (!this.getWorld().isClient && this.fleeTime >= 55 && this.getAnimation() == NO_ANIMATION && !this.isAiDisabled() && !this.hasStatusEffect(EffectHandler.FROZEN)) {
            BlockState blockBeneath = this.getWorld().getBlockState(this.getBlockPos().down());
            if (this.isBlockDiggable(blockBeneath)) {
                AnimationHandler.INSTANCE.sendAnimationMessage(this, BURROW_ANIMATION);
            }
        }
        if (!this.getWorld().isClient && this.getAnimation() == BURROW_ANIMATION) {
            if (this.getAnimationTick() % 4 == 3) {
                this.playSound(MMSounds.ENTITY_GROTTOL_BURROW, 1, 0.8f + this.random.nextFloat() * 0.4f);
                BlockState blockBeneath = this.getWorld().getBlockState(this.getBlockPos().down());
                if (this.isBlockDiggable(blockBeneath)) {
                    Vec3d pos = new Vec3d(0.5D, 0.05D, 0.0D).rotateY((float) Math.toRadians(-this.bodyYaw - 90));
                    if (this.getWorld() instanceof ServerWorld) {
                        ((ServerWorld) this.getWorld()).spawnParticles(new BlockStateParticleEffect(ParticleTypes.BLOCK, blockBeneath),
                                this.getX() + pos.x, this.getY() + pos.y, this.getZ() + pos.z,
                                8,
                                0.25D, 0.025D, 0.25D,
                                0.1D
                        );
                    }
                }
            }
        }
    }

    @Override
    protected void onAnimationFinish(Animation animation) {
        if (animation == BURROW_ANIMATION) {
            this.discard();
        }
    }

    private boolean isBlackPinkInYourArea() {
        Entity e = this.getVehicle();
        /*if (isMinecart(e)) {
            BlockState state = ((AbstractMinecartEntity) e).getDisplayTile();
            return state.getBlock() == BlockHandler.GROTTOL.get() && state.get(BlockGrottol.VARIANT) == BlockGrottol.Variant.BLACK_PINK;
        }*/
        return false;
    }

    public boolean isInMinecart() {
        return isMinecart(this.getVehicle());
    }

    /*public boolean hasMinecartBlockDisplay() {
        Entity entity = getRidingEntity();
        return isMinecart(entity) && ((AbstractMinecartEntity) entity).getDisplayTile().getBlock() == BlockHandler.GROTTOL.get();
    }*/

    @Override
    protected void pushAway(Entity entity) {
        if (!isMinecart(entity)) {
            super.pushAway(entity);
        }
    }

    @Override
    public boolean startRiding(Entity entity, boolean force) {
        /*if (isMinecart(entity)) {
                AbstractMinecartEntity minecart = (AbstractMinecartEntity) entity;
                if (minecart.getDisplayTile().getBlock() != BlockHandler.GROTTOL.get()) {
                    minecart.setDisplayTile(BlockHandler.GROTTOL.get().getDefaultState());
                    minecart.setDisplayTileOffset(minecart.getDefaultDisplayTileOffset());
                }
            }*/
        return super.startRiding(entity, force);
    }

    @Override
    public void stopRiding() {
//        Entity entity = this.getRidingEntity();
        super.stopRiding();
//        if (isMinecart(entity)) {
//            ((AbstractMinecartEntity) entity).setHasDisplayTile(false);
//        }
    }

    @Override
    protected SoundEvent getDeathSound() {
        this.playSound(MMSounds.ENTITY_GROTTOL_DIE, 1f, 1.3f);
        return null;
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
    public Animation[] getAnimations() {
        return ANIMATIONS;
    }

    public EnumDeathType getDeathType() {
        return this.death;
    }

    @Override
    protected Identifier getLootTableId() {
        return LootTableHandler.GROTTOL;
    }

    @Override
    protected ConfigHandler.CombatConfig getCombatConfig() {
        return ConfigHandler.COMMON.MOBS.GROTTOL.combatConfig;
    }

    public boolean isBlockDiggable(BlockState blockState) {
        if (blockState.isIn(TagHandler.CAN_GROTTOL_DIG)) return true;

        ICopiedBlockProperties properties = (ICopiedBlockProperties) blockState.getBlock().settings;
        Block baseBlock = properties.getBaseBlock();
        if (baseBlock != null) {
            return baseBlock.getDefaultState().isIn(TagHandler.CAN_GROTTOL_DIG);
        }

        return false;
    }

    public boolean getDeepslate() {
        return this.getDataTracker().get(DEEPSLATE);
    }

    public void setDeepslate(boolean deepslate) {
        this.getDataTracker().set(DEEPSLATE, deepslate);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound compound) {
        super.writeCustomDataToNbt(compound);
        compound.putBoolean("deepslate", this.getDeepslate());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound compound) {
        super.readCustomDataFromNbt(compound);
        this.setDeepslate(compound.getBoolean("deepslate"));
    }

    @Override
    public EntityData initialize(ServerWorldAccess worldIn, LocalDifficulty difficultyIn, SpawnReason reason, EntityData spawnDataIn, NbtCompound dataTag) {
        if (this.getY() < 8 && reason != SpawnReason.MOB_SUMMONED) this.setDeepslate(true);
        return super.initialize(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    public enum EnumDeathType {
        NORMAL,
        PICKAXE,
        FORTUNE_PICKAXE
    }
}
