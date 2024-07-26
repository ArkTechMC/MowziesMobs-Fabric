package com.bobmowzie.mowziesmobs.server.entity.lantern;

import com.bobmowzie.mowziesmobs.client.particle.ParticleCloud;
import com.bobmowzie.mowziesmobs.client.particle.ParticleHandler;
import com.bobmowzie.mowziesmobs.client.particle.ParticleOrb;
import com.bobmowzie.mowziesmobs.client.particle.ParticleVanillaCloudExtended;
import com.bobmowzie.mowziesmobs.client.particle.util.AdvancedParticleBase;
import com.bobmowzie.mowziesmobs.client.particle.util.ParticleComponent;
import com.bobmowzie.mowziesmobs.server.ai.animation.AnimationDieAI;
import com.bobmowzie.mowziesmobs.server.ai.animation.AnimationTakeDamage;
import com.bobmowzie.mowziesmobs.server.ai.animation.SimpleAnimationAI;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import com.bobmowzie.mowziesmobs.server.entity.MowzieLLibraryEntity;
import com.bobmowzie.mowziesmobs.server.loot.LootTableHandler;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import com.iafenvoy.uranus.animation.Animation;
import com.iafenvoy.uranus.animation.AnimationHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import java.util.EnumSet;

/**
 * Created by BobMowzie on 7/24/2018.
 */
public class EntityLantern extends MowzieLLibraryEntity {
    public static final Animation DIE_ANIMATION = Animation.create(25);
    public static final Animation HURT_ANIMATION = Animation.create(10);
    public static final Animation PUFF_ANIMATION = Animation.create(28);
    private static final Animation[] ANIMATIONS = {
            DIE_ANIMATION,
            HURT_ANIMATION,
            PUFF_ANIMATION
    };

    public Vec3d dir;
    private int groundDist = 1;

    @Environment(EnvType.CLIENT)
    private Vec3d[] pos;

    public EntityLantern(EntityType<? extends EntityLantern> type, World world) {
        super(type, world);
        this.dir = null;

        if (world.isClient) {
            this.pos = new Vec3d[1];
        }
        this.moveControl = new MoveHelperController(this);
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MowzieEntity.createAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 4.0D)
                .add(EntityAttributes.GENERIC_FLYING_SPEED, 0.3D)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.2D);
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(2, new SimpleAnimationAI<>(this, PUFF_ANIMATION, false));
        this.goalSelector.add(3, new AnimationTakeDamage<>(this));
        this.goalSelector.add(1, new AnimationDieAI<>(this));
        this.goalSelector.add(5, new RandomFlyGoal(this));
    }

    @Override
    public float getBrightnessAtEyes() {
        return 0xF000F0;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getAnimation() == PUFF_ANIMATION && this.getAnimationTick() == 7) {
            if (this.groundDist == 0) this.groundDist = 1;
            this.setVelocity(this.getVelocity().add(0, 0.2d + 0.2d / this.groundDist, 0));
            if (this.getWorld().isClient) {
                for (int i = 0; i < 5; i++) {
                    ParticleVanillaCloudExtended.spawnVanillaCloud(this.getWorld(), this.getX(), this.getY() + 0.3, this.getZ(), -this.getVelocity().getX() * 0.2 + 0.1 * (this.random.nextFloat() - 0.5), -this.getVelocity().getY() * 0.2 + 0.1 * (this.random.nextFloat() - 0.5), -this.getVelocity().getZ() * 0.2 + 0.1 * (this.random.nextFloat() - 0.5), 0.8d + this.random.nextDouble(), 163d / 256d, 247d / 256d, 74d / 256d, 0.95, 30);
                }
                for (int i = 0; i < 8; i++) {
                    AdvancedParticleBase.spawnParticle(this.getWorld(), ParticleHandler.PIXEL, this.getX(), this.getY() + 0.3, this.getZ(), -this.getVelocity().getX() * 0.2 + 0.2 * (this.random.nextFloat() - 0.5), -this.getVelocity().getY() * 0.2 + 0.1 * (this.random.nextFloat() - 0.5), -this.getVelocity().getZ() * 0.2 + 0.2 * (this.random.nextFloat() - 0.5), true, 0, 0, 0, 0, 4f, 163d / 256d, 247d / 256d, 74d / 256d, 1, 0.9, 17 + this.random.nextFloat() * 10, true, true, new ParticleComponent[]{
                            new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.SCALE, new ParticleComponent.KeyTrack(
                                    new float[]{4f, 0},
                                    new float[]{0.8f, 1}
                            ), false)
                    });
                }
            } else {
                if (this.getMoveHelperController().isMovingTo()) {
                    Vec3d lvt_1_1_ = new Vec3d(this.getMoveControl().getTargetX() - this.getX(), this.getMoveControl().getTargetY() - this.getY(), this.getMoveControl().getTargetZ() - this.getZ());
                    double lvt_2_1_ = lvt_1_1_.length();
                    lvt_1_1_ = lvt_1_1_.normalize();
                    if (this.getMoveHelperController().canReach(lvt_1_1_, MathHelper.ceil(lvt_2_1_))) {
                        this.setVelocity(this.getVelocity().add(lvt_1_1_.multiply(0.2)));
                    }
                }
            }
            this.playSound(MMSounds.ENTITY_LANTERN_PUFF, 0.6f, 1f + this.random.nextFloat() * 0.2f);
        }

        if (!this.getWorld().isClient && this.getAnimation() == NO_ANIMATION) {
            if (this.groundDist < 5 || (this.random.nextInt(13) == 0 && this.groundDist < 16)) {
                AnimationHandler.INSTANCE.sendAnimationMessage(this, PUFF_ANIMATION);
            }
        }

        if (this.groundDist >= 2) this.setVelocity(this.getVelocity().add(0, -0.0055d, 0));

        if (this.age % 5 == 0) {
            BlockPos checkPos = this.getBlockPos();
            int i;
            for (i = 0; i < 16; i++) {
                if (this.getWorld().getBlockState(checkPos).getBlock() != Blocks.AIR) break;
                checkPos = checkPos.down();
            }
            this.groundDist = i;
        }

        if (this.getWorld().isClient && ConfigHandler.CLIENT.glowEffect) {
            this.pos[0] = this.getPos().add(0, this.getHeight() * 0.8, 0);
            if (this.age % 70 == 0) {
                AdvancedParticleBase.spawnParticle(this.getWorld(), ParticleHandler.GLOW, this.pos[0].x, this.pos[0].y, this.pos[0].z, 0, 0, 0, true, 0, 0, 0, 0, 20F, 0.8, 0.95, 0.35, 1, 1, 70, true, true, new ParticleComponent[]{
                        new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.ALPHA, new ParticleComponent.KeyTrack(
                                new float[]{0.0f, 0.8f, 0},
                                new float[]{0, 0.5f, 1}
                        ), false),
                        new ParticleComponent.PinLocation(this.pos)
                });
            }
        }

//        if (getAnimation() == NO_ANIMATION) AnimationHandler.INSTANCE.sendAnimationMessage(this, DIE_ANIMATION);
    }

    @Override
    protected void updatePostDeath() {
        super.updatePostDeath();
        if (this.getAnimationTick() == 1 && this.getWorld().isClient) {
            for (int i = 0; i < 8; i++) {
                this.getWorld().addParticle(ParticleTypes.ITEM_SLIME, this.getX(), this.getY(), this.getZ(), 0.2 * (this.random.nextFloat() - 0.5), 0.2 * (this.random.nextFloat() - 0.5), 0.2 * (this.random.nextFloat() - 0.5));
                this.getWorld().addParticle(new ParticleCloud.CloudData(ParticleHandler.CLOUD, 163f / 256f, 247f / 256f, 74f / 256f, 10f + this.random.nextFloat() * 20f, 30, ParticleCloud.EnumCloudBehavior.GROW, 0.9f), this.getX(), this.getY() + 0.3, this.getZ(), 0.25 * (this.random.nextFloat() - 0.5), 0.25 * (this.random.nextFloat() - 0.5), 0.25 * (this.random.nextFloat() - 0.5));
                this.getWorld().addParticle(new ParticleOrb.OrbData(163f / 256f, 247f / 256f, 74f / 256f, 1.5f, 25), this.getX(), this.getY() + 0.3, this.getZ(), 0.2f * (this.random.nextFloat() - 0.5f), 0.2f * (this.random.nextFloat() - 0.5f), 0.2f * (this.random.nextFloat() - 0.5f));
            }
        }
        if (this.getAnimationTick() == 2) this.playSound(MMSounds.ENTITY_LANTERN_POP, 1f, 0.8f + this.random.nextFloat() * 0.4f);
    }

    @Override
    protected void fall(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
    }

    @Override
    public void travel(Vec3d movement) {
        if (this.isTouchingWater()) {
            this.updateVelocity(0.02F, movement);
            this.move(MovementType.SELF, this.getVelocity());
            this.setVelocity(this.getVelocity().multiply(0.8F));
        } else if (this.isInLava()) {
            this.updateVelocity(0.02F, movement);
            this.move(MovementType.SELF, this.getVelocity());
            this.setVelocity(this.getVelocity().multiply(0.5D));
        } else {
            BlockPos ground = BlockPos.ofFloored(this.getX(), this.getBoundingBox().minY - 1.0D, this.getZ());
            float f = 0.91F;
            if (this.isOnGround()) {
                f = this.getWorld().getBlockState(ground).getFriction(this.getWorld(), ground, this) * 0.91F;
            }

            float f1 = 0.16277137F / (f * f * f);
            f = 0.91F;
            if (this.isOnGround()) {
                f = this.getWorld().getBlockState(ground).getFriction(this.getWorld(), ground, this) * 0.91F;
            }

            this.updateVelocity(this.isOnGround() ? 0.1F * f1 : 0.02F, movement);
            this.move(MovementType.SELF, this.getVelocity());
            this.setVelocity(this.getVelocity().multiply(f));
        }

        this.updateLimbs(true);
    }

    @Override
    protected ConfigHandler.SpawnConfig getSpawnConfig() {
        return ConfigHandler.COMMON.MOBS.LANTERN.spawnConfig;
    }

    /**
     * Returns true if this entity should move as if it were on a ladder (either because it's actually on a ladder, or
     * for AI reasons)
     */
    public boolean isClimbing() {
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
    public Animation[] getAnimations() {
        return ANIMATIONS;
    }

    @Override
    protected Identifier getLootTableId() {
        return LootTableHandler.LANTERN;
    }

    @Override
    protected ConfigHandler.CombatConfig getCombatConfig() {
        return ConfigHandler.COMMON.MOBS.LANTERN.combatConfig;
    }

    public MoveHelperController getMoveHelperController() {
        if (this.getMoveControl() instanceof MoveHelperController)
            return (MoveHelperController) super.getMoveControl();
        return null;
    }

    static class RandomFlyGoal extends Goal {
        private final EntityLantern parentEntity;

        public RandomFlyGoal(EntityLantern p_i45836_1_) {
            this.parentEntity = p_i45836_1_;
            this.setControls(EnumSet.of(Control.MOVE));
        }

        public boolean canStart() {
            MoveControl lvt_1_1_ = this.parentEntity.getMoveControl();
            if (!lvt_1_1_.isMoving()) {
                return true;
            } else {
                double lvt_2_1_ = lvt_1_1_.getTargetX() - this.parentEntity.getX();
                double lvt_4_1_ = lvt_1_1_.getTargetY() - this.parentEntity.getY();
                double lvt_6_1_ = lvt_1_1_.getTargetZ() - this.parentEntity.getZ();
                double lvt_8_1_ = lvt_2_1_ * lvt_2_1_ + lvt_4_1_ * lvt_4_1_ + lvt_6_1_ * lvt_6_1_;
                return lvt_8_1_ < 1.0D || lvt_8_1_ > 3600.0D;
            }
        }

        public boolean shouldContinue() {
            return false;
        }

        public void start() {
            Random lvt_1_1_ = this.parentEntity.getRandom();
            double lvt_2_1_ = this.parentEntity.getX() + (double) ((lvt_1_1_.nextFloat() * 2.0F - 1.0F) * 16.0F);
            double lvt_4_1_ = this.parentEntity.getY() + (double) ((lvt_1_1_.nextFloat() * 2.0F - 1.0F) * 16.0F);
            double lvt_6_1_ = this.parentEntity.getZ() + (double) ((lvt_1_1_.nextFloat() * 2.0F - 1.0F) * 16.0F);
            this.parentEntity.getMoveControl().moveTo(lvt_2_1_, lvt_4_1_, lvt_6_1_, 1.0D);
        }
    }

    static class MoveHelperController extends MoveControl {
        private final EntityLantern parentEntity;
        protected int courseChangeCooldown;

        public MoveHelperController(EntityLantern p_i45838_1_) {
            super(p_i45838_1_);
            this.parentEntity = p_i45838_1_;
        }

        public void tick() {
            if (this.state == State.MOVE_TO) {
                if (this.courseChangeCooldown-- <= 0) {
                    this.courseChangeCooldown += this.parentEntity.getRandom().nextInt(5) + 2;
                    Vec3d lvt_1_1_ = new Vec3d(this.targetX - this.parentEntity.getX(), this.targetY - this.parentEntity.getY(), this.targetZ - this.parentEntity.getZ());
                    double lvt_2_1_ = lvt_1_1_.length();
                    lvt_1_1_ = lvt_1_1_.normalize();
                    if (!this.canReach(lvt_1_1_, MathHelper.ceil(lvt_2_1_))) {
                        this.state = State.WAIT;
                    }
                }

            }
        }

        public boolean canReach(Vec3d p_220673_1_, int p_220673_2_) {
            Box lvt_3_1_ = this.parentEntity.getBoundingBox();

            for (int lvt_4_1_ = 1; lvt_4_1_ < p_220673_2_; ++lvt_4_1_) {
                lvt_3_1_ = lvt_3_1_.offset(p_220673_1_);
                if (!this.parentEntity.getWorld().isSpaceEmpty(this.parentEntity, lvt_3_1_)) {
                    return false;
                }
            }

            return true;
        }

        public boolean isMovingTo() {
            return this.state == State.MOVE_TO;
        }
    }
}
