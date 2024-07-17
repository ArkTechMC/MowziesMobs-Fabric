package com.bobmowzie.mowziesmobs.server.entity.umvuthana;

import com.bobmowzie.mowziesmobs.server.ai.UmvuthanaHurtByTargetAI;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.LeaderSunstrikeImmune;
import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.*;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class EntityUmvuthanaRaptor extends EntityUmvuthana implements LeaderSunstrikeImmune, Monster {
    private final List<EntityUmvuthanaFollowerToRaptor> pack = new ArrayList<>();

    private final int packRadius = 3;

    public EntityUmvuthanaRaptor(EntityType<? extends EntityUmvuthanaRaptor> type, World world) {
        super(type, world);
        this.setMask(MaskType.FURY);
        this.experiencePoints = 8;
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MowzieEntity.createAttributes().add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 6)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 10);
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(3, new UmvuthanaHurtByTargetAI(this, true));
    }

    @Override
    protected void registerTargetGoals() {
        this.registerHuntingTargetGoals();
    }

    @Override
    protected boolean canHoldVaryingWeapons() {
        return false;
    }

    @Override
    public void tick() {
        super.tick();

        for (int i = 0; i < this.pack.size(); i++) {
            this.pack.get(i).index = i;
        }

        if (!this.getWorld().isClient && this.pack != null) {
            float theta = (2 * (float) Math.PI / this.pack.size());
            for (int i = 0; i < this.pack.size(); i++) {
                EntityUmvuthanaFollowerToRaptor hunter = this.pack.get(i);
                if (hunter.getTarget() == null) {
                    hunter.getNavigation().startMovingTo(this.getX() + this.packRadius * MathHelper.cos(theta * i), this.getY(), this.getZ() + this.packRadius * MathHelper.sin(theta * i), 0.45);
                    if (this.distanceTo(hunter) > 20 && this.isOnGround()) {
                        hunter.setPosition(this.getX() + this.packRadius * MathHelper.cos(theta * i), this.getY(), this.getZ() + this.packRadius * MathHelper.sin(theta * i));
                    }
                }
            }
        }

        if (!this.getWorld().isClient && this.getWorld().getDifficulty() == Difficulty.PEACEFUL) {
            this.discard();
        }
    }

    @Override
    public void remove(RemovalReason reason) {
        if (this.age == 0) {
            this.pack.forEach(EntityUmvuthanaFollowerToRaptor::setShouldSetDead);
        }
        this.pack.forEach(EntityUmvuthanaFollowerToRaptor::removeLeader);
        super.remove(reason);
    }

    @Override
    public boolean canSpawn(WorldView worldReader) {
        if (this.age == 0) {
            return !worldReader.containsFluid(this.getBoundingBox()) && worldReader.isSpaceEmpty(this);
        } else {
            return !worldReader.containsFluid(this.getBoundingBox()) && worldReader.isSpaceEmpty(this) && this.getWorld().doesNotIntersectEntities(this);
        }
    }

    public void removePackMember(EntityUmvuthanaFollowerToRaptor tribeHunter) {
        this.pack.remove(tribeHunter);
        this.sortPackMembers();
    }

    public void addPackMember(EntityUmvuthanaFollowerToRaptor tribeHunter) {
        this.pack.add(tribeHunter);
        this.sortPackMembers();
    }

    private void sortPackMembers() {
        double theta = 2 * Math.PI / this.pack.size();
        for (int i = 0; i < this.pack.size(); i++) {
            int nearestIndex = -1;
            double smallestDiffSq = Double.MAX_VALUE;
            double targetTheta = theta * i;
            double x = this.getX() + this.packRadius * Math.cos(targetTheta);
            double z = this.getZ() + this.packRadius * Math.sin(targetTheta);
            for (int n = 0; n < this.pack.size(); n++) {
                EntityUmvuthanaFollowerToRaptor tribeHunter = this.pack.get(n);
                double diffSq = (x - tribeHunter.getX()) * (x - tribeHunter.getX()) + (z - tribeHunter.getZ()) * (z - tribeHunter.getZ());
                if (diffSq < smallestDiffSq) {
                    smallestDiffSq = diffSq;
                    nearestIndex = n;
                }
            }
            if (nearestIndex == -1) {
                throw new ArithmeticException("All pack members have NaN x and z?");
            }
            this.pack.add(i, this.pack.remove(nearestIndex));
        }
    }

    public int getPackSize() {
        return this.pack.size();
    }

    @Nullable
    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason reason, @Nullable EntityData livingData, @Nullable NbtCompound compound) {
        int size = this.random.nextInt(2) + 3;
        float theta = (2 * (float) Math.PI / size);
        for (int i = 0; i <= size; i++) {
            EntityUmvuthanaFollowerToRaptor tribeHunter = new EntityUmvuthanaFollowerToRaptor(EntityHandler.UMVUTHANA_FOLLOWER_TO_RAPTOR, this.getWorld(), this);
            tribeHunter.setPosition(this.getX() + 0.1 * MathHelper.cos(theta * i), this.getY(), this.getZ() + 0.1 * MathHelper.sin(theta * i));
            int weapon = this.random.nextInt(3) == 0 ? 1 : 0;
            tribeHunter.setWeapon(weapon);
            world.spawnEntity(tribeHunter);
        }
        return super.initialize(world, difficulty, reason, livingData, compound);
    }

    @Override
    public void onDeath(DamageSource source) {
        super.onDeath(source);
        this.pack.forEach(EntityUmvuthanaFollowerToRaptor::removeLeader);
    }

    @Override
    protected ConfigHandler.SpawnConfig getSpawnConfig() {
        return ConfigHandler.COMMON.MOBS.UMVUTHANA.spawnConfig;
    }

    @Override
    public boolean canSpawn(WorldAccess world, SpawnReason reason) {
        List<LivingEntity> nearby = this.getEntityLivingBaseNearby(30, 10, 30, 30);
        for (LivingEntity nearbyEntity : nearby) {
            if (nearbyEntity instanceof EntityUmvuthanaRaptor || nearbyEntity instanceof VillagerEntity || nearbyEntity instanceof EntityUmvuthi || nearbyEntity instanceof AnimalEntity) {
                return false;
            }
        }
        return super.canSpawn(world, reason) && world.getDifficulty() != Difficulty.PEACEFUL;
    }

    public int getLimitPerChunk() {
        return 1;
    }

    @Override
    public void checkDespawn() {
        super.checkDespawn();
        if (this.getWorld().getDifficulty() == Difficulty.PEACEFUL && this.isDisallowedInPeaceful()) {
            this.discard();
        } else if (!this.isPersistent() && !this.cannotDespawn()) {
//            Entity entity = this.getWorld().getClosestPlayer(this, -1.0D);
//            net.minecraftforge.eventbus.api.Event.Result result = net.minecraftforge.event.ForgeEventFactory.canEntityDespawn(this, (ServerWorldAccess) this.getWorld());
//            if (result == net.minecraftforge.eventbus.api.Event.Result.DENY) {
//                this.despawnCounter = 0;
//                entity = null;
//            } else if (result == net.minecraftforge.eventbus.api.Event.Result.ALLOW) {
//                this.discard();
//                entity = null;
//            }
//            if (entity != null) {
//                double d0 = entity.squaredDistanceTo(this);
//                if (d0 > 16384.0D && this.canImmediatelyDespawn(d0) && this.pack != null) {
//                    this.pack.forEach(EntityUmvuthanaFollowerToRaptor::setShouldSetDead);
//                    this.discard();
//                }
//
//                if (this.despawnCounter > 600 && this.random.nextInt(800) == 0 && d0 > 1024.0D && this.canImmediatelyDespawn(d0) && this.pack != null) {
//                    this.pack.forEach(EntityUmvuthanaFollowerToRaptor::setShouldSetDead);
//                    this.discard();
//                } else if (d0 < 1024.0D) {
//                    this.despawnCounter = 0;
//                }
//            }
            this.discard();
        } else {
            this.despawnCounter = 0;
        }


        if (this.getWorld().getDifficulty() == Difficulty.PEACEFUL && this.isDisallowedInPeaceful()) {
            this.discard();
        } else if (!this.isPersistent() && !this.cannotDespawn()) {
//            Entity entity = this.getWorld().getClosestPlayer(this, -1.0D);
//            net.minecraftforge.eventbus.api.Event.Result result = net.minecraftforge.event.ForgeEventFactory.canEntityDespawn(this, (ServerWorldAccess) this.getWorld());
//            if (result == net.minecraftforge.eventbus.api.Event.Result.DENY) {
//                this.despawnCounter = 0;
//                entity = null;
//            } else if (result == net.minecraftforge.eventbus.api.Event.Result.ALLOW) {
//                if (this.pack != null) this.pack.forEach(EntityUmvuthanaFollowerToRaptor::setShouldSetDead);
//                this.discard();
//                entity = null;
//            }
//            if (entity != null) {
//                double d0 = entity.squaredDistanceTo(this);
//                int i = this.getType().getSpawnGroup().getImmediateDespawnRange();
//                int j = i * i;
//                if (d0 > (double) j && this.canImmediatelyDespawn(d0)) {
//                    if (this.pack != null) this.pack.forEach(EntityUmvuthanaFollowerToRaptor::setShouldSetDead);
//                    this.discard();
//                }
//
//                int k = this.getType().getSpawnGroup().getDespawnStartRange();
//                int l = k * k;
//                if (this.despawnCounter > 600 && this.random.nextInt(800) == 0 && d0 > (double) l && this.canImmediatelyDespawn(d0)) {
//                    if (this.pack != null) this.pack.forEach(EntityUmvuthanaFollowerToRaptor::setShouldSetDead);
//                    this.discard();
//                } else if (d0 < (double) l) {
//                    this.despawnCounter = 0;
//                }
//            }
            this.discard();
        } else {
            this.despawnCounter = 0;
        }
    }
}