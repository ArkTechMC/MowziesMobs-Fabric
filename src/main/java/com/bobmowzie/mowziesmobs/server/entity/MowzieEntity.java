package com.bobmowzie.mowziesmobs.server.entity;

import com.bobmowzie.mowziesmobs.client.model.tools.IntermittentAnimation;
import com.bobmowzie.mowziesmobs.client.sound.BossMusicPlayer;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.world.spawn.SpawnHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.structure.StructureSet;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.chunk.placement.StructurePlacementCalculator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public abstract class MowzieEntity extends PathAwareEntity implements IntermittentAnimatableEntity {
    private static final byte START_IA_HEALTH_UPDATE_ID = 4;
    private static final byte MUSIC_PLAY_ID = 67;
    private static final byte MUSIC_STOP_ID = 68;
    private static final UUID HEALTH_CONFIG_MODIFIER_UUID = UUID.fromString("eff1c400-910c-11ec-b909-0242ac120002");
    private static final UUID ATTACK_CONFIG_MODIFIER_UUID = UUID.fromString("f76a7c90-910c-11ec-b909-0242ac120002");
    private static final TrackedData<Boolean> STRAFING = DataTracker.registerData(MowzieEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private final List<IntermittentAnimation<?>> intermittentAnimations = new ArrayList<>();
    private final MMBossInfoServer bossInfo = new MMBossInfoServer(this);
    public int frame;
    public float targetDistance = -1;
    public float targetAngle = -1;
    public boolean active;
    public LivingEntity blockingEntity = null;
    public boolean playsHurtAnimation = true;
    public boolean hurtInterruptsAnimation = false;
    @Environment(EnvType.CLIENT)
    public Vec3d[] socketPosArray;
    public boolean renderingInGUI = false;
    protected boolean dropAfterDeathAnim = true;
    protected boolean prevOnGround;
    protected boolean prevPrevOnGround;
    protected boolean willLandSoon;
    private int killDataRecentlyHit;
    private DamageSource killDataCause;
    private PlayerEntity killDataAttackingPlayer;

    public MowzieEntity(EntityType<? extends MowzieEntity> type, World world) {
        super(type, world);
        if (world.isClient) {
            this.socketPosArray = new Vec3d[]{};
        }

        // Load config attribute multipliers
        ConfigHandler.CombatConfig combatConfig = this.getCombatConfig();
        if (combatConfig != null) {
            EntityAttributeInstance maxHealthAttr = this.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
            if (maxHealthAttr != null) {
                double difference = maxHealthAttr.getBaseValue() * this.getCombatConfig().healthMultiplier - maxHealthAttr.getBaseValue();
                maxHealthAttr.addTemporaryModifier(new EntityAttributeModifier(HEALTH_CONFIG_MODIFIER_UUID, "Health config multiplier", difference, EntityAttributeModifier.Operation.ADDITION));
                this.setHealth(this.getMaxHealth());
            }

            EntityAttributeInstance attackDamageAttr = this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE);
            if (attackDamageAttr != null) {
                double difference = attackDamageAttr.getBaseValue() * this.getCombatConfig().attackMultiplier - attackDamageAttr.getBaseValue();
                attackDamageAttr.addTemporaryModifier(new EntityAttributeModifier(ATTACK_CONFIG_MODIFIER_UUID, "Attack config multiplier", difference, EntityAttributeModifier.Operation.ADDITION));
            }
        }
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_ATTACK_DAMAGE);
    }

    public static boolean spawnPredicate(EntityType type, WorldAccess world, SpawnReason reason, BlockPos spawnPos, Random rand) {
        ConfigHandler.SpawnConfig spawnConfig = SpawnHandler.spawnConfigs.get(type);
        if (spawnConfig != null) {
            if (rand.nextDouble() > spawnConfig.extraRarity) return false;

            // Dimension check
            List<? extends String> dimensionNames = spawnConfig.dimensions;
            Identifier currDimensionName = ((ServerWorld) world).getRegistryKey().getValue();
            if (!dimensionNames.contains(currDimensionName.toString())) {
                return false;
            }

            // Height check
            float heightMax = spawnConfig.heightMax;
            float heightMin = spawnConfig.heightMin;
            if (spawnPos.getY() > heightMax && heightMax >= -64) {
                return false;
            }
            if (spawnPos.getY() < heightMin && heightMin >= -64) {
                return false;
            }

            // Light level check
            if (spawnConfig.needsDarkness && !HostileEntity.isSpawnDark((ServerWorldAccess) world, spawnPos, rand)) {
                return false;
            }

            // Block check
            BlockState block = world.getBlockState(spawnPos.down());
            Identifier blockName = Registries.BLOCK.getId(block.getBlock());
            List<? extends String> allowedBlocks = spawnConfig.allowedBlocks;
            List<? extends String> allowedBlockTags = spawnConfig.allowedBlockTags;
            if (!allowedBlocks.isEmpty() && !allowedBlocks.contains(blockName.toString()) && !allowedBlocks.contains(blockName.getPath()))
                return false;
            if (!allowedBlockTags.isEmpty() && !isBlockTagAllowed(allowedBlockTags, block)) return false;

            // See sky
            if (spawnConfig.needsSeeSky && !world.isSkyVisibleAllowingSea(spawnPos)) {
                return false;
            }
            if (spawnConfig.needsCantSeeSky && world.isSkyVisibleAllowingSea(spawnPos)) {
                return false;
            }

            List<? extends String> avoidStructures = spawnConfig.avoidStructures;
            Registry<StructureSet> structureSetRegistry = world.getRegistryManager().get(RegistryKeys.STRUCTURE_SET);
            ServerWorld serverLevel = (ServerWorld) world;
            StructurePlacementCalculator generatorState = serverLevel.getChunkManager().getStructurePlacementCalculator();
            ChunkPos chunkPos = new ChunkPos(spawnPos);
            for (String structureName : avoidStructures) {
                Optional<StructureSet> structureSetOptional = structureSetRegistry.getOrEmpty(new Identifier(structureName));
                if (structureSetOptional.isEmpty()) continue;
                Optional<RegistryKey<StructureSet>> resourceKeyOptional = structureSetRegistry.getKey(structureSetOptional.get());
                if (resourceKeyOptional.isEmpty()) continue;
                Optional<RegistryEntry.Reference<StructureSet>> holderOptional = structureSetRegistry.getEntry(resourceKeyOptional.get());
                if (holderOptional.isEmpty()) continue;
                if (generatorState.canGenerate(holderOptional.get(), chunkPos.x, chunkPos.z, 3)) {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean isBlockTagAllowed(List<? extends String> allowedBlockTags, BlockState block) {
        for (String allowedBlockTag : allowedBlockTags) {
            TagKey<Block> tagKey = TagKey.of(RegistryKeys.BLOCK, new Identifier(allowedBlockTag));
            if (block.isIn(tagKey)) return true;
        }
        return false;
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(STRAFING, false);
    }

    public boolean isStrafing() {
        return this.dataTracker.get(STRAFING);
    }

    public void setStrafing(boolean strafing) {
        this.dataTracker.set(STRAFING, strafing);
    }

    protected ConfigHandler.SpawnConfig getSpawnConfig() {
        return null;
    }

    protected ConfigHandler.CombatConfig getCombatConfig() {
        return null;
    }

    protected boolean isWithinDistance(BlockPos pos, int distance) {
        return pos.isWithinDistance(this.getBlockPos(), distance);
    }

    @Override
    public void tick() {
        this.prevPrevOnGround = this.prevOnGround;
        this.prevOnGround = this.isOnGround();
        super.tick();
        this.frame++;
        if (this.age % 4 == 0) this.bossInfo.update();
        if (this.getTarget() != null) {
            this.targetDistance = this.distanceTo(this.getTarget()) - this.getTarget().getWidth() / 2f;
            this.targetAngle = (float) this.getAngleBetweenEntities(this, this.getTarget());
        }
        this.willLandSoon = !this.isOnGround() && this.getWorld().isSpaceEmpty(this.getBoundingBox().offset(this.getVelocity()));

        if (!this.getWorld().isClient && this.getBossMusic() != null) {
            if (this.canPlayMusic()) {
                this.getWorld().sendEntityStatus(this, MUSIC_PLAY_ID);
            } else {
                this.getWorld().sendEntityStatus(this, MUSIC_STOP_ID);
            }
        }
    }

    protected boolean canPlayMusic() {
        return !this.isSilent() && this.getTarget() instanceof PlayerEntity;
    }

    public boolean canPlayerHearMusic(PlayerEntity player) {
        return player != null
                && this.canTarget(player)
                && this.distanceTo(player) < 2500;
    }

    @Override
    protected void mobTick() {
        super.mobTick();
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.prevYaw = this.getYaw();
        this.prevBodyYaw = this.bodyYaw = this.prevHeadYaw = this.headYaw;
    }

    @Override
    public boolean tryAttack(Entity entityIn) {
        return this.doHurtTarget(entityIn, 1.0F, 1.0f);
    }

    @Override
    public EntityData initialize(ServerWorldAccess worldIn, LocalDifficulty difficultyIn, SpawnReason reason, EntityData spawnDataIn, NbtCompound dataTag) {
//        System.out.println("Spawned " + getName().getString() + " at " + getPosition());
//        System.out.println("Block " + world.getBlockState(getPosition().down()).toString());
        return super.initialize(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    public boolean doHurtTarget(Entity entityIn, float damageMultiplier, float applyKnockbackMultiplier) {
        return this.doHurtTarget(entityIn, damageMultiplier, applyKnockbackMultiplier, false);
    }

    public boolean doHurtTarget(Entity entityIn, float damageMultiplier, float applyKnockbackMultiplier, boolean canDisableShield) { // Copied from mob class
        float f = (float) this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).getValue() * damageMultiplier;
        float f1 = (float) this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_KNOCKBACK).getValue();
        if (entityIn instanceof LivingEntity) {
            f += EnchantmentHelper.getAttackDamage(this.getMainHandStack(), ((LivingEntity) entityIn).getGroup());
            f1 += (float) EnchantmentHelper.getKnockback(this);
        }

        int i = EnchantmentHelper.getFireAspect(this);
        if (i > 0) {
            entityIn.setOnFireFor(i * 4);
        }

        boolean flag = entityIn.damage(this.getDamageSources().mobAttack(this), f);
        if (flag) {
            entityIn.setVelocity(entityIn.getVelocity().multiply(applyKnockbackMultiplier, 1.0, applyKnockbackMultiplier));
            if (f1 > 0.0F && entityIn instanceof LivingEntity) {
                ((LivingEntity) entityIn).takeKnockback(f1 * 0.5F, MathHelper.sin(this.getYaw() * ((float) Math.PI / 180F)), -MathHelper.cos(this.getYaw() * ((float) Math.PI / 180F)));
                this.setVelocity(this.getVelocity().multiply(0.6D, 1.0D, 0.6D));
            }

            if (entityIn instanceof PlayerEntity player) {
                if (canDisableShield)
                    this.maybeDisableShield(player, player.isUsingItem() ? player.getActiveItem() : ItemStack.EMPTY);
            }

            this.applyDamageEffects(this, entityIn);
            this.onAttacking(entityIn);
        }

        return flag;
    }

    private void maybeDisableShield(PlayerEntity player, ItemStack itemStackBlock) { // Copied from mob class
        if (!itemStackBlock.isEmpty() && itemStackBlock.isOf(Items.SHIELD)) {
            float f = 0.25F + (float) EnchantmentHelper.getEfficiency(this) * 0.05F;
            if (this.random.nextFloat() < f) {
                player.getItemCooldownManager().set(Items.SHIELD, 100);
                this.getWorld().sendEntityStatus(player, (byte) 30);
            }
        }
    }

    public float getHealthRatio() {
        return this.getHealth() / this.getMaxHealth();
    }

    public double getAngleBetweenEntities(Entity first, Entity second) {
        return Math.atan2(second.getZ() - first.getZ(), second.getX() - first.getX()) * (180 / Math.PI) + 90;
    }

    public double getDotProductBodyFacingEntity(Entity second) {
        Vec3d vecBetween = second.getPos().subtract(this.getPos());
        vecBetween = vecBetween.normalize();
        return vecBetween.dotProduct(Vec3d.fromPolar(0, this.bodyYaw).normalize());
    }

    public List<PlayerEntity> getPlayersNearby(double distanceX, double distanceY, double distanceZ, double radius) {
        List<Entity> nearbyEntities = this.getWorld().getOtherEntities(this, this.getBoundingBox().expand(distanceX, distanceY, distanceZ));
        List<PlayerEntity> listEntityPlayers = nearbyEntities.stream().filter(entityNeighbor -> entityNeighbor instanceof PlayerEntity && this.distanceTo(entityNeighbor) <= radius + entityNeighbor.getWidth() / 2f).map(entityNeighbor -> (PlayerEntity) entityNeighbor).collect(Collectors.toList());
        return listEntityPlayers;
    }

    public List<LivingEntity> getAttackableEntityLivingBaseNearby(double distanceX, double distanceY, double distanceZ, double radius) {
        List<Entity> nearbyEntities = this.getWorld().getOtherEntities(this, this.getBoundingBox().expand(distanceX, distanceY, distanceZ));
        List<LivingEntity> listEntityLivingBase = nearbyEntities.stream().filter(entityNeighbor -> entityNeighbor instanceof LivingEntity && ((LivingEntity) entityNeighbor).isMobOrPlayer() && (!(entityNeighbor instanceof PlayerEntity) || !((PlayerEntity) entityNeighbor).isCreative()) && this.distanceTo(entityNeighbor) <= radius + entityNeighbor.getWidth() / 2f).map(entityNeighbor -> (LivingEntity) entityNeighbor).collect(Collectors.toList());
        return listEntityLivingBase;
    }

    public List<LivingEntity> getEntityLivingBaseNearby(double distanceX, double distanceY, double distanceZ, double radius) {
        return this.getEntitiesNearby(LivingEntity.class, distanceX, distanceY, distanceZ, radius);
    }

    public <T extends Entity> List<T> getEntitiesNearby(Class<T> entityClass, double r) {
        return this.getWorld().getEntitiesByClass(entityClass, this.getBoundingBox().expand(r, r, r), e -> e != this && this.distanceTo(e) <= r + e.getWidth() / 2f);
    }

    public <T extends Entity> List<T> getEntitiesNearby(Class<T> entityClass, double dX, double dY, double dZ, double r) {
        return this.getWorld().getEntitiesByClass(entityClass, this.getBoundingBox().expand(dX, dY, dZ), e -> e != this && this.distanceTo(e) <= r + e.getWidth() / 2f && e.getY() <= this.getY() + dY);
    }

    @Override
    protected void updatePostDeath() { // Copied from entityLiving
        ++this.deathTime;
        int deathDuration = this.getDeathDuration();
        if (this.deathTime >= deathDuration && !this.getWorld().isClient()) {
            this.attackingPlayer = this.killDataAttackingPlayer;
            this.playerHitTimer = this.killDataRecentlyHit;
            if (this.dropAfterDeathAnim && this.killDataCause != null) {
                this.drop(this.killDataCause);
            }
            this.getWorld().sendEntityStatus(this, (byte) 60);
            this.remove(RemovalReason.KILLED);
        }
    }

    protected abstract int getDeathDuration();

    @Override
    protected void drop(DamageSource source) {
        if (!this.dropAfterDeathAnim || this.deathTime > 0) {
            super.drop(source);
        }
    }

    @Override
    public void onDeath(DamageSource cause) {
        if (!this.dead) {
            this.killDataCause = cause;
            this.killDataRecentlyHit = this.playerHitTimer;
            this.killDataAttackingPlayer = this.attackingPlayer;
        }
        super.onDeath(cause);
        if (!this.isRemoved()) {
            this.bossInfo.update();
        }
    }

    protected void addIntermittentAnimation(IntermittentAnimation animation) {
        animation.setID((byte) this.intermittentAnimations.size());
        this.intermittentAnimations.add(animation);
    }

    @Override
    public void handleStatus(byte id) {
        if (id >= START_IA_HEALTH_UPDATE_ID && id - START_IA_HEALTH_UPDATE_ID < this.intermittentAnimations.size()) {
            this.intermittentAnimations.get(id - START_IA_HEALTH_UPDATE_ID).start();
        } else if (id == MUSIC_PLAY_ID) {
            BossMusicPlayer.playBossMusic(this);
        } else if (id == MUSIC_STOP_ID) {
            BossMusicPlayer.stopBossMusic(this);
        } else super.handleStatus(id);
    }

    @Override
    public byte getOffsetEntityState() {
        return START_IA_HEALTH_UPDATE_ID;
    }

    public Vec3d circleEntityPosition(Entity target, float radius, float speed, boolean direction, int circleFrame, float offset) {
        int directionInt = direction ? 1 : -1;
        double t = directionInt * circleFrame * 0.5 * speed / radius + offset;
        Vec3d movePos = target.getPos().add(radius * Math.cos(t), 0, radius * Math.sin(t));
        return movePos;
    }

    protected void repelEntities(float x, float y, float z, float radius) {
        List<LivingEntity> nearbyEntities = this.getEntityLivingBaseNearby(x, y, z, radius);
        for (Entity entity : nearbyEntities) {
            if (entity.canHit() && !entity.noClip) {
                double angle = (this.getAngleBetweenEntities(this, entity) + 90) * Math.PI / 180;
                entity.setVelocity(-0.1 * Math.cos(angle), entity.getVelocity().y, -0.1 * Math.sin(angle));
            }
        }
    }

    @Override
    public void onStartedTrackingBy(ServerPlayerEntity player) {
        super.onStartedTrackingBy(player);
        this.bossInfo.addPlayer(player);
    }

    @Override
    public void onStoppedTrackingBy(ServerPlayerEntity player) {
        super.onStoppedTrackingBy(player);
        this.bossInfo.removePlayer(player);
    }

    @Override
    public void readNbt(NbtCompound compound) {
        super.readNbt(compound);
        if (this.hasCustomName()) {
            this.bossInfo.setName(this.getDisplayName());
        }
    }

    @Override
    public void setCustomName(Text name) {
        super.setCustomName(name);
        this.bossInfo.setName(this.getDisplayName());
    }

    public boolean hasBossBar() {
        return false;
    }

    public boolean resetHealthOnPlayerRespawn() {
        return false;
    }

    protected BossBar.Color bossBarColor() {
        return BossBar.Color.PURPLE;
    }

    @Environment(EnvType.CLIENT)
    public void setSocketPosArray(int index, Vec3d pos) {
        if (this.socketPosArray != null && this.socketPosArray.length > index) {
            this.socketPosArray[index] = pos;
        }
    }

    public boolean canBePushedByEntity(Entity entity) {
        return true;
    }

    // TODO: Copied from parent classes
    @Override
    public void pushAwayFrom(Entity entityIn) {
        if (!this.isSleeping()) {
            if (!this.isConnectedThroughVehicle(entityIn)) {
                if (!entityIn.noClip && !this.noClip) {
                    double d0 = entityIn.getX() - this.getX();
                    double d1 = entityIn.getZ() - this.getZ();
                    double d2 = MathHelper.absMax(d0, d1);
                    if (d2 >= (double) 0.01F) {
                        d2 = Math.sqrt(d2);
                        d0 = d0 / d2;
                        d1 = d1 / d2;
                        double d3 = 1.0D / d2;
                        if (d3 > 1.0D) {
                            d3 = 1.0D;
                        }

                        d0 = d0 * d3;
                        d1 = d1 * d3;
                        d0 = d0 * (double) 0.05F;
                        d1 = d1 * (double) 0.05F;
                        if (!this.hasPassengers()) {
                            if (this.canBePushedByEntity(entityIn)) {
                                this.addVelocity(-d0, 0.0D, -d1);
                            }
                        }

                        if (!entityIn.hasPassengers()) {
                            entityIn.addVelocity(d0, 0.0D, d1);
                        }
                    }

                }
            }
        }
    }

    public SoundEvent getBossMusic() {
        return null;
    }
}
