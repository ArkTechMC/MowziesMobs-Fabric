package com.bobmowzie.mowziesmobs.server.entity.wroughtnaut;

import com.bobmowzie.mowziesmobs.server.ai.MMPathNavigateGround;
import com.bobmowzie.mowziesmobs.server.ai.WroughtnautAttackAI;
import com.bobmowzie.mowziesmobs.server.ai.animation.*;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import com.bobmowzie.mowziesmobs.server.entity.MowzieLLibraryEntity;
import com.bobmowzie.mowziesmobs.server.entity.SmartBodyHelper;
import com.bobmowzie.mowziesmobs.server.loot.LootTableHandler;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import com.iafenvoy.uranus.animation.Animation;
import com.iafenvoy.uranus.animation.AnimationHandler;
import com.iafenvoy.uranus.client.model.tools.ControlledAnimation;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.control.BodyControl;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class EntityWroughtnaut extends MowzieLLibraryEntity implements Monster {
    public static final Animation DIE_ANIMATION = Animation.create(130);

    public static final Animation HURT_ANIMATION = Animation.create(15);

    public static final Animation ATTACK_ANIMATION = Animation.create(50);

    public static final Animation ATTACK_TWICE_ANIMATION = Animation.create(36);

    public static final Animation ATTACK_THRICE_ANIMATION = Animation.create(59);

    public static final Animation VERTICAL_ATTACK_ANIMATION = Animation.create(105);

    public static final Animation STOMP_ATTACK_ANIMATION = Animation.create(40);

    public static final Animation ACTIVATE_ANIMATION = Animation.create(45);

    public static final Animation DEACTIVATE_ANIMATION = Animation.create(15);

    private static final Animation[] ANIMATIONS = {
            DIE_ANIMATION,
            HURT_ANIMATION,
            ATTACK_ANIMATION,
            ATTACK_TWICE_ANIMATION,
            ATTACK_THRICE_ANIMATION,
            VERTICAL_ATTACK_ANIMATION,
            STOMP_ATTACK_ANIMATION,
            ACTIVATE_ANIMATION,
            DEACTIVATE_ANIMATION
    };

    private static final float[][] VERTICAL_ATTACK_BLOCK_OFFSETS = {
            {-0.1F, -0.1F},
            {-0.1F, 0.1F},
            {0.1F, 0.1F},
            {0.1F, -0.1F}
    };

    private static final TrackedData<Optional<BlockPos>> REST_POSITION = DataTracker.registerData(EntityWroughtnaut.class, TrackedDataHandlerRegistry.OPTIONAL_BLOCK_POS);

    private static final TrackedData<Boolean> ACTIVE = DataTracker.registerData(EntityWroughtnaut.class, TrackedDataHandlerRegistry.BOOLEAN);

    private static final TrackedData<Boolean> ALWAYS_ACTIVE = DataTracker.registerData(EntityWroughtnaut.class, TrackedDataHandlerRegistry.BOOLEAN);

    public ControlledAnimation walkAnim = new ControlledAnimation(10);

    public boolean swingDirection;

    public boolean vulnerable;
    public Vec3d leftEyePos, rightEyePos;
    public Vec3d leftEyeRot, rightEyeRot;
    private CeilingDisturbance disturbance;

    public EntityWroughtnaut(EntityType<? extends EntityWroughtnaut> type, World world) {
        super(type, world);
        this.experiencePoints = 30;
        this.active = false;
        this.setStepHeight(1);
//        rightEyePos = new Vector3d(0, 0, 0);
//        leftEyePos = new Vector3d(0, 0, 0);
//        rightEyeRot = new Vector3d(0, 0, 0);
//        leftEyeRot = new Vector3d(0, 0, 0);

        this.dropAfterDeathAnim = true;
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MowzieEntity.createAttributes().add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 30)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 40)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 1);
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(1, new AnimationFWNAttackAI(this, 4F, 5F, 100F));
        this.goalSelector.add(1, new AnimationFWNVerticalAttackAI(this, VERTICAL_ATTACK_ANIMATION, MMSounds.ENTITY_WROUGHT_WHOOSH, 1F, 5F, 40F));
        this.goalSelector.add(1, new AnimationFWNStompAttackAI(this, STOMP_ATTACK_ANIMATION));
        this.goalSelector.add(1, new AnimationTakeDamage<>(this));
        this.goalSelector.add(1, new AnimationDieAI<>(this));
        this.goalSelector.add(1, new AnimationActivateAI<>(this, ACTIVATE_ANIMATION));
        this.goalSelector.add(1, new AnimationDeactivateAI<>(this, DEACTIVATE_ANIMATION));
        this.goalSelector.add(2, new WroughtnautAttackAI(this));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, 0, true, false, null));
    }

    @Override
    protected float getActiveEyeHeight(EntityPose poseIn, EntityDimensions sizeIn) {
        return sizeIn.height * 0.98F;
    }

    @Override
    protected EntityNavigation createNavigation(World world) {
        return new MMPathNavigateGround(this, world);
    }

    @Override
    protected BodyControl createBodyControl() {
        return new SmartBodyHelper(this);
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return MMSounds.ENTITY_WROUGHT_HURT_1;
    }

    @Override
    public SoundEvent getDeathSound() {
        this.playSound(MMSounds.ENTITY_WROUGHT_SCREAM, 1f, 1f);
        return null;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return this.getAnimation() == NO_ANIMATION && this.isActive() ? MMSounds.ENTITY_WROUGHT_AMBIENT : null;
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        Entity entitySource = source.getAttacker();
        if (entitySource != null) {
            if ((!this.active || this.getTarget() == null) && entitySource instanceof LivingEntity && !(entitySource instanceof PlayerEntity && ((PlayerEntity) entitySource).isCreative()) && !(entitySource instanceof EntityWroughtnaut))
                this.setTarget((LivingEntity) entitySource);
            if (this.vulnerable) {
                int arc = 220;
                float entityHitAngle = (float) ((Math.atan2(entitySource.getZ() - this.getZ(), entitySource.getX() - this.getX()) * (180 / Math.PI) - 90) % 360);
                float entityAttackingAngle = this.bodyYaw % 360;
                if (entityHitAngle < 0) {
                    entityHitAngle += 360;
                }
                if (entityAttackingAngle < 0) {
                    entityAttackingAngle += 360;
                }
                float entityRelativeAngle = entityHitAngle - entityAttackingAngle;
                if ((entityRelativeAngle <= arc / 2f && entityRelativeAngle >= -arc / 2f) || (entityRelativeAngle >= 360 - arc / 2f || entityRelativeAngle <= -arc + 90f / 2f)) {
                    this.playSound(MMSounds.ENTITY_WROUGHT_UNDAMAGED, 0.4F, 2);
                    return false;
                } else {
                    this.setAnimation(NO_ANIMATION);
                    return super.damage(source, amount);
                }
            } else {
                this.playSound(MMSounds.ENTITY_WROUGHT_UNDAMAGED, 0.4F, 2);
            }
        } else if (source.isIn(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            return super.damage(source, amount);
        }
        return false;
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
    public boolean doesRenderOnFire() {
        return false;
    }

    @Override
    public boolean isPushedByFluids() {
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        this.walkAnim.updatePrevTimer();

//        if (getAnimation() == NO_ANIMATION) {
//            setActive(true);
//            swingDirection = true;
//            AnimationHandler.INSTANCE.sendAnimationMessage(this, VERTICAL_ATTACK_ANIMATION);
//            getNavigator().clearPath();
//        }

        if (this.getTarget() != null && (!this.getTarget().isAlive() || this.getTarget().getHealth() <= 0))
            this.setTarget(null);

        if (!this.getWorld().isClient) {
            if (this.isAlwaysActive()) {
                this.setActive(true);
                this.active = true;
            } else if (this.getAnimation() == NO_ANIMATION && !this.isAiDisabled()) {
                if (this.isActive()) {
                    if (this.getTarget() == null && this.forwardSpeed == 0 && this.isAtRestPos()) {
                        AnimationHandler.INSTANCE.sendAnimationMessage(this, DEACTIVATE_ANIMATION);
                        this.setActive(false);
                    }
                } else if (this.getTarget() != null && this.targetDistance <= 4.5) {
                    AnimationHandler.INSTANCE.sendAnimationMessage(this, ACTIVATE_ANIMATION);
                    this.setActive(true);
                }
            }
        }

        if (!this.isActive()) {
//            posX = prevPosX;
//            posZ = prevPosZ;
            this.setVelocity(0, this.getVelocity().y, 0);
            this.setYaw(this.prevYaw);
        }
//        else if (world.isRemote && leftEyePos != null && rightEyePos != null) {
//            MowzieParticleBase.spawnParticle(world, MMParticle.EYE, leftEyePos.x, leftEyePos.y, leftEyePos.z, 0, 0, 0, false, leftEyeRot.y + 1.5708, 0, 0, 0, 5f, 0.8f, 0.1f, 0.1f, 1f, 1, 10, false, new ParticleComponent[]{new ParticleComponent.PropertyControl(EnumParticleProperty.ALPHA, ParticleComponent.KeyTrack.startAndEnd(1, 0), false)});
//            MowzieParticleBase.spawnParticle(world, MMParticle.EYE, rightEyePos.x, rightEyePos.y, rightEyePos.z, 0, 0, 0, false, rightEyeRot.y, 0, 0, 0, 5f, 0.8f, 0.1f, 0.1f, 1f, 1, 10, false, new ParticleComponent[]{new ParticleComponent.PropertyControl(EnumParticleProperty.ALPHA, ParticleComponent.KeyTrack.startAndEnd(1, 0), false)});
//        }
        if (this.getAnimation() != NO_ANIMATION || !this.isActive()) {
            this.headYaw = this.bodyYaw = this.getYaw();
        }

        if (!this.isAlwaysActive() && this.getTarget() == null && this.getNavigation().isIdle() && !this.isAtRestPos() && this.isActive())
            this.updateRestPos();

        if (this.getAnimation() == ATTACK_ANIMATION && this.getAnimationTick() == 1) {
            this.swingDirection = this.random.nextBoolean();
        } else if (this.getAnimation() == ACTIVATE_ANIMATION) {
            int tick = this.getAnimationTick();
            if (tick == 1) {
                this.playSound(MMSounds.ENTITY_WROUGHT_GRUNT_2, 1, 1);
            } else if (tick == 27 || tick == 44) {
                this.playSound(MMSounds.ENTITY_WROUGHT_STEP, 0.5F, 0.5F);
            }
        } else if (this.getAnimation() == VERTICAL_ATTACK_ANIMATION && this.getAnimationTick() == 29) {
            this.doVerticalAttackHitFX();
        } else if (this.getAnimation() == STOMP_ATTACK_ANIMATION) {
            this.doStompFX();
        }

        float moveX = (float) (this.getX() - this.prevX);
        float moveZ = (float) (this.getZ() - this.prevZ);
        float speed = MathHelper.sqrt(moveX * moveX + moveZ * moveZ);
        if (speed > 0.01) {
            if (this.getAnimation() == NO_ANIMATION) {
                this.walkAnim.increaseTimer();
            }
        } else {
            this.walkAnim.decreaseTimer();
        }
        if (this.getAnimation() != NO_ANIMATION) {
            this.walkAnim.decreaseTimer(2);
        }

        if (this.getWorld().isClient && this.frame % 20 == 1 && speed > 0.03 && this.getAnimation() == NO_ANIMATION && this.isActive()) {
            this.getWorld().playSound(this.getX(), this.getY(), this.getZ(), MMSounds.ENTITY_WROUGHT_STEP, this.getSoundCategory(), 0.5F, 0.5F, false);
        }

        this.repelEntities(1.7F, 4, 1.7F, 1.7F);

        if (!this.active && !this.getWorld().isClient && this.getAnimation() != ACTIVATE_ANIMATION) {
            if (ConfigHandler.COMMON.MOBS.FERROUS_WROUGHTNAUT.healsOutOfBattle) this.heal(0.3f);
        }

        if (this.disturbance != null) {
            if (this.disturbance.update()) {
                this.disturbance = null;
            }
        }

//        if (!this.world.isRemote) {
//            Path path = this.getNavigator().getPath();
//            if (path != null) {
//                for (int i = 0; i < path.getCurrentPathLength(); i++) {
//                    Vector3d p = path.getVectorFromIndex(this, i);
//                    ((WorldServer) this.world).spawnParticle(EnumParticleTypes.BLOCK_DUST, p.x, p.y + 0.1D, p.z, 1, 0.1D, 0.0D, 0.1D, 0.01D, Block.getIdFromBlock(i < path.getCurrentPathIndex() ? Blocks.GOLD_BLOCK : i == path.getCurrentPathIndex() ? Blocks.DIAMOND_BLOCK : Blocks.DIRT));
//                }
//            }
//        }
    }

    @Override
    protected void repelEntities(float x, float y, float z, float radius) {
        super.repelEntities(x, y, z, radius);
    }

    @Override
    public boolean canBePushedByEntity(Entity entity) {
        return false;
    }

    private boolean isAtRestPos() {
        Optional<BlockPos> restPos = this.getRestPos();
        if (restPos.isPresent()) {
            return restPos.get().getSquaredDistance(this.getBlockPos()) < 36;
        }
        return false;
    }

    private void updateRestPos() {
        boolean reassign = true;
        if (this.getRestPos().isPresent()) {
            BlockPos pos = this.getRestPos().get();
            if (this.getNavigation().startMovingTo(pos.getX(), pos.getY(), pos.getZ(), 0.2)) {
                reassign = false;
            }
        }
        if (reassign) {
            this.setRestPos(this.getBlockPos());
        }
    }

    private void doVerticalAttackHitFX() {
        double theta = (this.bodyYaw - 4) * (Math.PI / 180);
        double perpX = Math.cos(theta);
        double perpZ = Math.sin(theta);
        theta += Math.PI / 2;
        double vecX = Math.cos(theta);
        double vecZ = Math.sin(theta);
        double x = this.getX() + 4.2 * vecX;
        double y = this.getBoundingBox().minY + 0.1;
        double z = this.getZ() + 4.2 * vecZ;
        int hitY = MathHelper.floor(this.getY() - 0.2);
        for (int t = 0; t < VERTICAL_ATTACK_BLOCK_OFFSETS.length; t++) {
            float ox = VERTICAL_ATTACK_BLOCK_OFFSETS[t][0], oy = VERTICAL_ATTACK_BLOCK_OFFSETS[t][1];
            int hitX = MathHelper.floor(x + ox);
            int hitZ = MathHelper.floor(z + oy);
            BlockPos hit = new BlockPos(hitX, hitY, hitZ);
            BlockState block = this.getWorld().getBlockState(hit);
            if (block.getRenderType() != BlockRenderType.INVISIBLE) {
                for (int n = 0; n < 6; n++) {
                    double pa = this.random.nextDouble() * 2 * Math.PI;
                    double pd = this.random.nextDouble() * 0.6 + 0.1;
                    double px = x + Math.cos(pa) * pd;
                    double pz = z + Math.sin(pa) * pd;
                    double magnitude = this.random.nextDouble() * 4 + 5;
                    double velX = perpX * magnitude;
                    double velY = this.random.nextDouble() * 3 + 6;
                    double velZ = perpZ * magnitude;
                    if (vecX * (pz - this.getZ()) - vecZ * (px - this.getX()) > 0) {
                        velX = -velX;
                        velZ = -velZ;
                    }
                    this.getWorld().addParticle(new BlockStateParticleEffect(ParticleTypes.BLOCK, block), px, y, pz, velX, velY, velZ);
                }
            }
        }
        int hitX = MathHelper.floor(x);
        int ceilY = MathHelper.floor(this.getBoundingBox().maxY);
        int hitZ = MathHelper.floor(z);
        final int maxHeight = 5;
        int height = maxHeight;
        BlockPos.Mutable pos = new BlockPos.Mutable();
        for (; height-- > 0; ceilY++) {
            pos.set(hitX, ceilY, hitZ);
            if (this.getWorld().getBlockState(pos).blocksMovement()) {
                break;
            }
        }
        float strength = height / (float) maxHeight;
        if (strength >= 0) {
            int radius = MathHelper.ceil(MathHelper.sqrt(1 - strength * strength) * maxHeight);
            this.disturbance = new CeilingDisturbance(hitX, ceilY, hitZ, radius, this.random.nextInt(5) + 3, this.random.nextInt(60) + 20);
        }
    }

    private void doStompFX() {
        double perpFacing = this.bodyYaw * (Math.PI / 180);
        double facingAngle = perpFacing + Math.PI / 2;
        int tick = this.getAnimationTick();
        final int maxDistance = 6;
        if (tick > 9 && tick < 17) {
            if (tick == 12) {
                final double infront = 1.47, side = -0.21;
                double vx = Math.cos(facingAngle) * infront;
                double vz = Math.sin(facingAngle) * infront;
                double perpX = Math.cos(perpFacing);
                double perpZ = Math.sin(perpFacing);
                double fx = this.getX() + vx + perpX * side;
                double fy = this.getBoundingBox().minY + 0.1;
                double fz = this.getZ() + vz + perpZ * side;
                int amount = 16 + this.getWorld().random.nextInt(8);
                while (amount-- > 0) {
                    double theta = this.getWorld().random.nextDouble() * Math.PI * 2;
                    double dist = this.getWorld().random.nextDouble() * 0.1 + 0.25;
                    double sx = Math.cos(theta);
                    double sz = Math.sin(theta);
                    double px = fx + sx * dist;
                    double py = fy + this.getWorld().random.nextDouble() * 0.1;
                    double pz = fz + sz * dist;
                    this.getWorld().addParticle(ParticleTypes.SMOKE, px, py, pz, sx * 0.065, 0, sz * 0.065);
                }
            }
            if (tick % 2 == 0) {
                int distance = tick / 2 - 2;
                double spread = Math.PI * 2;
                int arcLen = MathHelper.ceil(distance * spread);
                for (int i = 0; i < arcLen; i++) {
                    double theta = (i / (arcLen - 1.0) - 0.5) * spread + facingAngle;
                    double vx = Math.cos(theta);
                    double vz = Math.sin(theta);
                    double px = this.getX() + vx * distance;
                    double pz = this.getZ() + vz * distance;
                    float factor = 1 - distance / (float) maxDistance;
                    if (this.getWorld().random.nextBoolean()) {
                        int amount = this.getWorld().random.nextInt(5);
                        while (amount-- > 0) {
                            double velX = vx * 0.075;
                            double velY = factor * 0.3 + 0.025;
                            double velZ = vz * 0.075;
                            this.getWorld().addParticle(ParticleTypes.CLOUD, px + this.getWorld().random.nextFloat() * 2 - 1, this.getBoundingBox().minY + 0.1 + this.getWorld().random.nextFloat() * 1.5, pz + this.getWorld().random.nextFloat() * 2 - 1, velX, velY, velZ);
                        }
                    }
                }
            }
        }
    }

    @Nullable
    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason reason, @Nullable EntityData livingData, @Nullable NbtCompound compound) {
        this.setRestPos(this.getBlockPos());
        return super.initialize(world, difficulty, reason, livingData, compound);
    }

    @Override
    public boolean cannotDespawn() {
        return true;
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.getDataTracker().startTracking(REST_POSITION, Optional.empty());
        this.getDataTracker().startTracking(ACTIVE, false);
        this.getDataTracker().startTracking(ALWAYS_ACTIVE, false);
    }

    public Optional<BlockPos> getRestPos() {
        return this.getDataTracker().get(REST_POSITION);
    }

    public void setRestPos(BlockPos pos) {
        this.getDataTracker().set(REST_POSITION, Optional.of(pos));
    }

    public boolean isActive() {
        return this.getDataTracker().get(ACTIVE);
    }

    public void setActive(boolean isActive) {
        this.getDataTracker().set(ACTIVE, isActive);
    }

    public boolean isAlwaysActive() {
        return this.getDataTracker().get(ALWAYS_ACTIVE);
    }

    public void setAlwaysActive(boolean isAlwaysActive) {
        this.getDataTracker().set(ALWAYS_ACTIVE, isAlwaysActive);
        if (isAlwaysActive) {
            this.goalSelector.add(7, new WanderAroundFarGoal(this, 0.2D));
            this.goalSelector.add(8, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
            this.goalSelector.add(8, new LookAroundGoal(this));
        }
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound compound) {
        super.writeCustomDataToNbt(compound);
        Optional<BlockPos> restPos = this.getRestPos();
        if (restPos.isPresent()) {
            compound.put("restPos", NbtHelper.fromBlockPos(this.getRestPos().get()));
        }
        compound.putBoolean("active", this.isActive());
        compound.putBoolean("alwaysActive", this.isAlwaysActive());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound compound) {
        super.readCustomDataFromNbt(compound);
        if (compound.contains("restPos")) {
            this.setRestPos(NbtHelper.toBlockPos(compound.getCompound("restPos")));
        }
        this.setActive(compound.getBoolean("active"));
        this.active = this.isActive();
        this.setAlwaysActive(compound.getBoolean("alwaysActive"));
    }

    @Override
    protected ConfigHandler.CombatConfig getCombatConfig() {
        return ConfigHandler.COMMON.MOBS.FERROUS_WROUGHTNAUT.combatConfig;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
    }

    @Override
    public Animation[] getAnimations() {
        return ANIMATIONS;
    }

    @Nullable
    @Override
    protected Identifier getLootTableId() {
        return LootTableHandler.FERROUS_WROUGHTNAUT;
    }

    @Override
    public boolean hasBossBar() {
        return ConfigHandler.COMMON.MOBS.FERROUS_WROUGHTNAUT.hasBossBar;
    }

    @Override
    protected BossBar.Color bossBarColor() {
        return BossBar.Color.RED;
    }

    @Override
    public SoundEvent getBossMusic() {
        return MMSounds.MUSIC_FERROUS_WROUGHTNAUT_THEME;
    }

    @Override
    protected boolean canPlayMusic() {
        return super.canPlayMusic() && (this.active || this.getAnimation() == ACTIVATE_ANIMATION);
    }

    @Override
    public boolean resetHealthOnPlayerRespawn() {
        return ConfigHandler.COMMON.MOBS.FERROUS_WROUGHTNAUT.resetHealthWhenRespawn;
    }

    private class CeilingDisturbance {
        private final int ceilX, ceilY, ceilZ;

        private final int radius;
        private final int duration;
        private int delay;
        private int remainingTicks;

        public CeilingDisturbance(int x, int y, int z, int radius, int delay, int remainingTicks) {
            this.ceilX = x;
            this.ceilY = y;
            this.ceilZ = z;
            this.radius = radius;
            this.delay = delay;
            this.remainingTicks = remainingTicks;
            this.duration = remainingTicks;
        }

        public boolean update() {
            if (--this.delay > 0) {
                return false;
            }
            float t = this.remainingTicks / (float) this.duration;
            int amount = MathHelper.ceil((1 - MathHelper.sqrt(1 - t * t)) * this.radius * this.radius * 0.15F);
            boolean playSound = true;
            BlockPos.Mutable pos = new BlockPos.Mutable();
            while (amount-- > 0) {
                double theta = EntityWroughtnaut.this.random.nextDouble() * Math.PI * 2;
                double dist = EntityWroughtnaut.this.random.nextDouble() * this.radius;
                double x = this.ceilX + Math.cos(theta) * dist;
                double y = this.ceilY - 0.1 - EntityWroughtnaut.this.random.nextDouble() * 0.3;
                double z = this.ceilZ + Math.sin(theta) * dist;
                int blockX = MathHelper.floor(x);
                int blockZ = MathHelper.floor(z);
                pos.set(blockX, this.ceilY, blockZ);
                BlockState block = EntityWroughtnaut.this.getWorld().getBlockState(pos);
                if (block.getRenderType() != BlockRenderType.INVISIBLE) {
                    EntityWroughtnaut.this.getWorld().addParticle(new BlockStateParticleEffect(ParticleTypes.FALLING_DUST, block), x, y, z, 0, 0, 0);
                    if (playSound && EntityWroughtnaut.this.random.nextFloat() < 0.075F) {
                        BlockSoundGroup sound = block.getBlock().getSoundGroup(block);
                        EntityWroughtnaut.this.getWorld().playSound(EntityWroughtnaut.this.getX(), EntityWroughtnaut.this.getY(), EntityWroughtnaut.this.getZ(), sound.getBreakSound(), SoundCategory.BLOCKS, sound.getVolume() * 2, sound.getPitch() * 0.6F, false);
                        playSound = false;
                    }
                }
            }
            return --this.remainingTicks <= 0;
        }
    }
}