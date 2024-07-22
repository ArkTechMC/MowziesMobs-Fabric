package com.bobmowzie.mowziesmobs.server.capability;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.particle.ParticleCloud;
import com.bobmowzie.mowziesmobs.client.particle.ParticleHandler;
import com.bobmowzie.mowziesmobs.client.particle.ParticleSnowFlake;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.frostmaw.EntityFrozenController;
import com.bobmowzie.mowziesmobs.server.potion.EffectHandler;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistryV3;
import dev.onyxstudios.cca.api.v3.component.ComponentV3;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class FrozenCapability {
    public static int MAX_FREEZE_DECAY_DELAY = 10;

    public static IFrozenCapability get(LivingEntity entity){
        return FrozenProvider.get(entity).capability;
    }

    public interface IFrozenCapability {
        boolean getFrozen();

        float getFreezeProgress();

        void setFreezeProgress(float freezeProgress);

        float getFrozenYaw();

        void setFrozenYaw(float frozenYaw);

        float getFrozenPitch();

        void setFrozenPitch(float frozenPitch);

        float getFrozenYawHead();

        void setFrozenYawHead(float frozenYawHead);

        float getFrozenRenderYawOffset();

        void setFrozenRenderYawOffset(float frozenRenderYawOffset);

        float getFrozenSwingProgress();

        void setFrozenSwingProgress(float frozenSwingProgress);

        float getFrozenWalkAnimSpeed();

        void setFrozenWalkAnimSpeed(float frozenWalkAnimSpeed);

        float getFrozenWalkAnimPosition();

        void setFrozenWalkAnimPosition(float frozenWalkAnimPosition);

        boolean prevHasAI();

        void setPrevHasAI(boolean prevHasAI);

        int getFreezeDecayDelay();

        void setFreezeDecayDelay(int freezeDecayDelay);

        boolean getPrevFrozen();

        void setPrevFrozen(boolean prevFrozen);

        UUID getPreAttackTarget();

        void setPreAttackTarget(UUID livingEntity);

        EntityFrozenController getFrozenController();

        void setFrozenController(EntityFrozenController frozenController);

        void addFreezeProgress(LivingEntity entity, float amount);

        void onFreeze(LivingEntity entity);

        void onUnfreeze(LivingEntity entity);

        void tick(LivingEntity entity);

        NbtCompound serializeNBT(NbtCompound tag);

        void deserializeNBT(NbtCompound nbt);
    }

    public static class FrozenCapabilityImp implements IFrozenCapability {
        public boolean frozen;
        public float freezeProgress = 0;
        public float frozenYaw;
        public float frozenPitch;
        public float frozenYawHead;
        public float frozenRenderYawOffset;
        public float frozenSwingProgress;
        public float frozenWalkAnimSpeed;
        public float frozenWalkAnimPosition;
        public boolean prevHasAI = true;
        public UUID prevAttackTarget;

        // After taking freeze progress, this timer needs to reach zero before freeze progress starts to fade
        public int freezeDecayDelay;

        public boolean prevFrozen = false;
        public EntityFrozenController frozenController;

        @Override
        public boolean getFrozen() {
            return this.frozen;
        }

        @Override
        public float getFreezeProgress() {
            return this.freezeProgress;
        }

        @Override
        public void setFreezeProgress(float freezeProgress) {
            this.freezeProgress = freezeProgress;
        }

        @Override
        public float getFrozenYaw() {
            return this.frozenYaw;
        }

        @Override
        public void setFrozenYaw(float frozenYaw) {
            this.frozenYaw = frozenYaw;
        }

        @Override
        public float getFrozenPitch() {
            return this.frozenPitch;
        }

        @Override
        public void setFrozenPitch(float frozenPitch) {
            this.frozenPitch = frozenPitch;
        }

        @Override
        public float getFrozenYawHead() {
            return this.frozenYawHead;
        }

        @Override
        public void setFrozenYawHead(float frozenYawHead) {
            this.frozenYawHead = frozenYawHead;
        }

        @Override
        public float getFrozenRenderYawOffset() {
            return this.frozenRenderYawOffset;
        }

        @Override
        public void setFrozenRenderYawOffset(float frozenRenderYawOffset) {
            this.frozenRenderYawOffset = frozenRenderYawOffset;
        }

        @Override
        public float getFrozenSwingProgress() {
            return this.frozenSwingProgress;
        }

        @Override
        public void setFrozenSwingProgress(float frozenSwingProgress) {
            this.frozenSwingProgress = frozenSwingProgress;
        }

        @Override
        public float getFrozenWalkAnimSpeed() {
            return this.frozenWalkAnimSpeed;
        }

        @Override
        public void setFrozenWalkAnimSpeed(float frozenWalkAnimPosition) {
            this.frozenWalkAnimSpeed = frozenWalkAnimPosition;
        }

        @Override
        public float getFrozenWalkAnimPosition() {
            return this.frozenWalkAnimPosition;
        }

        @Override
        public void setFrozenWalkAnimPosition(float frozenWalkAnimPosition) {
            this.frozenWalkAnimPosition = frozenWalkAnimPosition;
        }

        @Override
        public boolean prevHasAI() {
            return this.prevHasAI;
        }

        @Override
        public void setPrevHasAI(boolean prevHasAI) {
            this.prevHasAI = prevHasAI;
        }

        @Override
        public int getFreezeDecayDelay() {
            return this.freezeDecayDelay;
        }

        @Override
        public void setFreezeDecayDelay(int freezeDecayDelay) {
            this.freezeDecayDelay = freezeDecayDelay;
        }

        @Override
        public boolean getPrevFrozen() {
            return this.prevFrozen;
        }

        @Override
        public void setPrevFrozen(boolean prevFrozen) {
            this.prevFrozen = prevFrozen;
        }

        @Override
        public UUID getPreAttackTarget() {
            return this.prevAttackTarget;
        }

        @Override
        public void setPreAttackTarget(UUID livingEntity) {
            this.prevAttackTarget = livingEntity;
        }

        @Override
        public EntityFrozenController getFrozenController() {
            return this.frozenController;
        }

        @Override
        public void setFrozenController(EntityFrozenController frozenController) {
            this.frozenController = frozenController;
        }

        @Override
        public void addFreezeProgress(LivingEntity entity, float amount) {
            if (!entity.getWorld().isClient && !entity.hasStatusEffect(EffectHandler.FROZEN)) {
                this.freezeProgress += amount;
                this.freezeDecayDelay = MAX_FREEZE_DECAY_DELAY;
            }
        }

        @Override
        public void onFreeze(LivingEntity entity) {
            if (entity != null) {
                this.frozen = true;
                this.frozenController = new EntityFrozenController(EntityHandler.FROZEN_CONTROLLER, entity.getWorld());
                this.frozenController.updatePositionAndAngles(entity.getX(), entity.getY(), entity.getZ(), entity.getYaw(), entity.getPitch());
                entity.getWorld().spawnEntity(this.frozenController);
                this.frozenController.setBodyYaw(entity.bodyYaw);
                this.frozenYaw = entity.getYaw();
                this.frozenPitch = entity.getPitch();
                this.frozenYawHead = entity.headYaw;
                this.frozenWalkAnimSpeed = entity.limbAnimator.getSpeed();
                this.frozenWalkAnimPosition = entity.limbAnimator.getPos();
                this.frozenRenderYawOffset = entity.bodyYaw;
                this.frozenSwingProgress = entity.handSwingProgress;
                entity.startRiding(this.frozenController, true);
                entity.clearActiveItem();

                if (entity instanceof MobEntity mobEntity) {
                    if (mobEntity.getTarget() != null) this.setPreAttackTarget(mobEntity.getTarget().getUuid());
                    this.prevHasAI = !((MobEntity) entity).isAiDisabled();
                    mobEntity.setAiDisabled(true);
                }

                if (entity.getWorld().isClient) {
                    int particleCount = (int) (10 + 1 * entity.getHeight() * entity.getWidth() * entity.getWidth());
                    for (int i = 0; i < particleCount; i++) {
                        double snowX = entity.getX() + entity.getWidth() * entity.getRandom().nextFloat() - entity.getWidth() / 2;
                        double snowZ = entity.getZ() + entity.getWidth() * entity.getRandom().nextFloat() - entity.getWidth() / 2;
                        double snowY = entity.getY() + entity.getHeight() * entity.getRandom().nextFloat();
                        Vec3d motion = new Vec3d(snowX - entity.getX(), snowY - (entity.getY() + entity.getHeight() / 2), snowZ - entity.getZ()).normalize();
                        entity.getWorld().addParticle(new ParticleSnowFlake.SnowflakeData(40, false), snowX, snowY, snowZ, 0.1d * motion.x, 0.1d * motion.y, 0.1d * motion.z);
                    }
                }
                entity.playSound(MMSounds.ENTITY_FROSTMAW_FROZEN_CRASH, 1, 1);
            }
        }

        @Override
        public void onUnfreeze(LivingEntity entity) {
            if (entity != null) {
                this.freezeProgress = 0;
                if (this.frozen) {
                    entity.removeStatusEffectInternal(EffectHandler.FROZEN);
                    this.frozen = false;
                    if (this.frozenController != null) {
                        Vec3d oldPosition = entity.getPos();
                        entity.stopRiding();
                        entity.requestTeleport(oldPosition.getX(), oldPosition.getY(), oldPosition.getZ());
                        this.frozenController.discard();
                    }
                    entity.playSound(MMSounds.ENTITY_FROSTMAW_FROZEN_CRASH, 1, 0.5f);
                    if (entity.getWorld().isClient) {
                        int particleCount = (int) (10 + 1 * entity.getHeight() * entity.getWidth() * entity.getWidth());
                        for (int i = 0; i < particleCount; i++) {
                            double particleX = entity.getX() + entity.getWidth() * entity.getRandom().nextFloat() - entity.getWidth() / 2;
                            double particleZ = entity.getZ() + entity.getWidth() * entity.getRandom().nextFloat() - entity.getWidth() / 2;
                            double particleY = entity.getY() + entity.getHeight() * entity.getRandom().nextFloat() + 0.3f;
                            entity.getWorld().addParticle(new BlockStateParticleEffect(ParticleTypes.BLOCK, Blocks.ICE.getDefaultState()), particleX, particleY, particleZ, 0, 0, 0);
                        }
                    }
                    if (entity instanceof MobEntity) {
                        if (((MobEntity) entity).isAiDisabled() && this.prevHasAI) {
                            ((MobEntity) entity).setAiDisabled(false);
                        }
                        if (this.getPreAttackTarget() != null) {
                            PlayerEntity target = entity.getWorld().getPlayerByUuid(this.getPreAttackTarget());
                            if (target != null) {
                                ((MobEntity) entity).setTarget(target);
                            }
                        }
                    }
                }
            }
        }

        @Override
        public void tick(LivingEntity entity) {
            // Freeze logic
            if (this.getFreezeProgress() >= 1 && !entity.hasStatusEffect(EffectHandler.FROZEN)) {
                entity.addStatusEffect(new StatusEffectInstance(EffectHandler.FROZEN, 50, 0, false, false));
                this.freezeProgress = 1f;
            } else if (this.freezeProgress > 0) {
                entity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 9, MathHelper.floor(this.freezeProgress * 5 + 1), false, false));
            }

            if (this.frozenController == null) {
                Entity riding = entity.getVehicle();
                if (riding instanceof EntityFrozenController) this.frozenController = (EntityFrozenController) riding;
            }

            if (this.frozen) {
                entity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 2, 50, false, false));
                entity.setSneaking(false);

                if (entity.getWorld().isClient && entity.age % 2 == 0) {
                    double cloudX = entity.getX() + entity.getWidth() * entity.getRandom().nextFloat() - entity.getWidth() / 2;
                    double cloudZ = entity.getZ() + entity.getWidth() * entity.getRandom().nextFloat() - entity.getWidth() / 2;
                    double cloudY = entity.getY() + entity.getHeight() * entity.getRandom().nextFloat();
                    entity.getWorld().addParticle(new ParticleCloud.CloudData(ParticleHandler.CLOUD, 0.75f, 0.75f, 1f, 15f, 25, ParticleCloud.EnumCloudBehavior.CONSTANT, 1f), cloudX, cloudY, cloudZ, 0f, -0.01f, 0f);

                    double snowX = entity.getX() + entity.getWidth() * entity.getRandom().nextFloat() - entity.getWidth() / 2;
                    double snowZ = entity.getZ() + entity.getWidth() * entity.getRandom().nextFloat() - entity.getWidth() / 2;
                    double snowY = entity.getY() + entity.getHeight() * entity.getRandom().nextFloat();
                    entity.getWorld().addParticle(new ParticleSnowFlake.SnowflakeData(40, false), snowX, snowY, snowZ, 0d, -0.01d, 0d);
                }
            } else {
                if (!entity.getWorld().isClient && this.getPrevFrozen()) {
                    this.onUnfreeze(entity);
                }
            }

            if (this.freezeDecayDelay <= 0) {
                this.freezeProgress -= 0.1;
                if (this.freezeProgress < 0) this.freezeProgress = 0;
            } else {
                this.freezeDecayDelay--;
            }
            this.prevFrozen = entity.hasStatusEffect(EffectHandler.FROZEN);
        }

        @Override
        public NbtCompound serializeNBT(NbtCompound compound) {
            compound.putFloat("freezeProgress", this.getFreezeProgress());
            compound.putInt("freezeDecayDelay", this.getFreezeDecayDelay());
            compound.putFloat("frozenWalkAnimSpeed", this.getFrozenWalkAnimSpeed());
            compound.putFloat("frozenWalkAnimPosition", this.getFrozenWalkAnimPosition());
            compound.putFloat("frozenRenderYawOffset", this.getFrozenRenderYawOffset());
            compound.putFloat("frozenSwingProgress", this.getFrozenSwingProgress());
            compound.putFloat("frozenPitch", this.getFrozenPitch());
            compound.putFloat("frozenYaw", this.getFrozenYaw());
            compound.putFloat("frozenYawHead", this.getFrozenYawHead());
            compound.putBoolean("prevHasAI", this.prevHasAI());
            if (this.getPreAttackTarget() != null) {
                compound.putUuid("prevAttackTarget", this.getPreAttackTarget());
            }
            compound.putBoolean("frozen", this.frozen);
            compound.putBoolean("prevFrozen", this.prevFrozen);
            return compound;
        }

        @Override
        public void deserializeNBT(NbtCompound compound) {
            this.setFreezeProgress(compound.getFloat("freezeProgress"));
            this.setFreezeDecayDelay(compound.getInt("freezeDecayDelay"));
            this.setFrozenWalkAnimSpeed(compound.getFloat("frozenWalkAnimSpeed"));
            this.setFrozenWalkAnimPosition(compound.getFloat("frozenWalkAnimPosition"));
            this.setFrozenRenderYawOffset(compound.getFloat("frozenRenderYawOffset"));
            this.setFrozenSwingProgress(compound.getFloat("frozenSwingProgress"));
            this.setFrozenPitch(compound.getFloat("frozenPitch"));
            this.setFrozenYaw(compound.getFloat("frozenYaw"));
            this.setFrozenYawHead(compound.getFloat("frozenYawHead"));
            this.setPrevHasAI(compound.getBoolean("prevHasAI"));
            try {
                this.setPreAttackTarget(compound.getUuid("prevAttackTarget"));
            } catch (NullPointerException ignored) {
            }
            this.frozen = compound.getBoolean("frozen");
            this.prevFrozen = compound.getBoolean("prevFrozen");
        }
    }

    public static class FrozenProvider implements ComponentV3, AutoSyncedComponent, CommonTickingComponent {
        protected static final ComponentKey<FrozenProvider> COMPONENT = ComponentRegistryV3.INSTANCE.getOrCreate(new Identifier(MowziesMobs.MODID, "frozen"), FrozenProvider.class);
        private final IFrozenCapability capability = new FrozenCapabilityImp();
        private final LivingEntity entity;

        public FrozenProvider(LivingEntity entity) {
            this.entity = entity;
        }

        public static FrozenProvider get(LivingEntity entity) {
            return COMPONENT.get(entity);
        }

        @Override
        public void tick() {
            this.capability.tick(this.entity);
        }

        @Override
        public void readFromNbt(@NotNull NbtCompound tag) {
            this.capability.deserializeNBT(tag);
        }

        @Override
        public void writeToNbt(@NotNull NbtCompound tag) {
            this.capability.serializeNBT(tag);
        }
    }
}
