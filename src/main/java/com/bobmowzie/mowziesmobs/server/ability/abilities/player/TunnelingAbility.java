package com.bobmowzie.mowziesmobs.server.ability.abilities.player;

import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieGeoBone;
import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieGeoModel;
import com.bobmowzie.mowziesmobs.client.particle.ParticleHandler;
import com.bobmowzie.mowziesmobs.client.particle.util.AdvancedParticleBase;
import com.bobmowzie.mowziesmobs.client.particle.util.ParticleComponent;
import com.bobmowzie.mowziesmobs.client.render.entity.player.GeckoPlayer;
import com.bobmowzie.mowziesmobs.server.ability.Ability;
import com.bobmowzie.mowziesmobs.server.ability.AbilitySection;
import com.bobmowzie.mowziesmobs.server.ability.AbilityType;
import com.bobmowzie.mowziesmobs.server.ability.PlayerAbility;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntityBlockSwapper;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntityFallingBlock;
import com.bobmowzie.mowziesmobs.server.item.ItemEarthrendGauntlet;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import com.bobmowzie.mowziesmobs.server.potion.EffectGeomancy;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

import java.util.List;

public class TunnelingAbility extends PlayerAbility {
    private static final RawAnimation FALL_ANIM = RawAnimation.begin().thenPlayAndHold("tunneling_fall");
    private static final RawAnimation DRILL_ANIM = RawAnimation.begin().thenLoop("tunneling_drill");
    public boolean prevUnderground;
    public BlockState justDug = Blocks.DIRT.getDefaultState();
    boolean underground = false;
    private int doubleTapTimer = 0;
    private float spinAmount = 0;
    private float pitch = 0;
    private int timeUnderground = 0;
    private int timeAboveGround = 0;
    private Hand whichHand;
    private ItemStack gauntletStack;

    public TunnelingAbility(AbilityType<PlayerEntity, ? extends Ability> abilityType, PlayerEntity user) {
        super(abilityType, user, new AbilitySection[]{
                new AbilitySection.AbilitySectionInfinite(AbilitySection.AbilitySectionType.ACTIVE)
        });
    }

    @Override
    public void tickNotUsing() {
        super.tickNotUsing();
        if (this.doubleTapTimer > 0) this.doubleTapTimer--;
    }

    public void playGauntletAnimation() {
        if (this.getUser() != null && !this.getLevel().isClient()) {
            if (this.gauntletStack != null && this.gauntletStack.getItem() == ItemHandler.EARTHREND_GAUNTLET) {
                PlayerEntity player = this.getUser();
                ItemHandler.EARTHREND_GAUNTLET.triggerAnim(player, GeoItem.getOrAssignId(this.gauntletStack, (ServerWorld) player.getWorld()), ItemEarthrendGauntlet.CONTROLLER_NAME, ItemEarthrendGauntlet.OPEN_ANIM_NAME);
            }
        }
    }

    public void stopGauntletAnimation() {
        if (this.getUser() != null && !this.getLevel().isClient()) {
            if (this.gauntletStack != null && this.gauntletStack.getItem() == ItemHandler.EARTHREND_GAUNTLET) {
                PlayerEntity player = this.getUser();
                ItemHandler.EARTHREND_GAUNTLET.triggerAnim(player, GeoItem.getOrAssignId(this.gauntletStack, (ServerWorld) player.getWorld()), ItemEarthrendGauntlet.CONTROLLER_NAME, ItemEarthrendGauntlet.IDLE_ANIM_NAME);
            }
        }
    }

    @Override
    public void start() {
        super.start();
        this.underground = false;
        this.prevUnderground = false;
        if (this.getUser().isOnGround()) this.getUser().addVelocity(0, 0.8f, 0);
        this.whichHand = this.getUser().getActiveHand();
        this.gauntletStack = this.getUser().getActiveItem();
        if (this.getUser().getWorld().isClient()) {
            this.spinAmount = 0;
            this.pitch = 0;
        }
    }

    public boolean damageGauntlet() {
        ItemStack stack = this.getUser().getActiveItem();
        if (stack.getItem() == ItemHandler.EARTHREND_GAUNTLET) {
            Hand handIn = this.getUser().getActiveHand();
            if (stack.getDamage() + 5 < stack.getMaxDamage()) {
                stack.damage(5, this.getUser(), p -> p.sendToolBreakStatus(handIn));
                return true;
            } else {
                if (ConfigHandler.COMMON.TOOLS_AND_ABILITIES.EARTHREND_GAUNTLET.breakable) {
                    stack.damage(5, this.getUser(), p -> p.sendToolBreakStatus(handIn));
                }
                return false;
            }
        }
        return false;
    }

    public void restoreGauntlet(ItemStack stack) {
        if (stack.getItem() == ItemHandler.EARTHREND_GAUNTLET) {
            if (!ConfigHandler.COMMON.TOOLS_AND_ABILITIES.EARTHREND_GAUNTLET.breakable) {
                stack.setDamage(Math.max(stack.getDamage() - 1, 0));
            }
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.isUsing() && this.getUser() instanceof PlayerEntity) {
            PlayerEntity player = this.getUser();
            for (ItemStack stack : player.getInventory().main) {
                this.restoreGauntlet(stack);
            }
            for (ItemStack stack : player.getInventory().offHand) {
                this.restoreGauntlet(stack);
            }
        }
    }

    @Override
    public void tickUsing() {
        super.tickUsing();
        this.getUser().fallDistance = 0;
        this.getUser().getAbilities().flying = false;
        this.underground = !this.getUser().getWorld().getNonSpectatingEntities(EntityBlockSwapper.class, this.getUser().getBoundingBox().expand(0.3)).isEmpty();
        Vec3d lookVec = this.getUser().getRotationVector();
        float tunnelSpeed = 0.3f;
        ItemStack stack = this.getUser().getActiveItem();
        boolean usingGauntlet = stack.getItem() == ItemHandler.EARTHREND_GAUNTLET;
        if (this.underground) {
            this.timeUnderground++;
            if (usingGauntlet && this.damageGauntlet()) {
                this.getUser().setVelocity(lookVec.normalize().multiply(tunnelSpeed));
            } else {
                this.getUser().setVelocity(lookVec.multiply(0.3, 0, 0.3).add(0, 1, 0).normalize().multiply(tunnelSpeed));
            }

            List<LivingEntity> entitiesHit = this.getEntityLivingBaseNearby(this.getUser(), 2, 2, 2, 2);
            for (LivingEntity entityHit : entitiesHit) {
                DamageSource damageSource = this.getUser().getDamageSources().playerAttack(this.getUser());
                entityHit.damage(damageSource, (float) (6 * ConfigHandler.COMMON.TOOLS_AND_ABILITIES.EARTHREND_GAUNTLET.attackMultiplier));
            }
        } else {
            this.timeAboveGround++;
            this.getUser().setVelocity(this.getUser().getVelocity().subtract(0, 0.07, 0));
            if (this.getUser().getVelocity().getY() < -1.3)
                this.getUser().setVelocity(this.getUser().getVelocity().getX(), -1.3, this.getUser().getVelocity().getZ());
        }

        if ((this.underground && (this.prevUnderground || lookVec.y < 0) && this.timeAboveGround > 5) || (this.getTicksInUse() > 1 && usingGauntlet && lookVec.y < 0 && stack.getDamage() + 5 < stack.getMaxDamage())) {
            if (this.getUser().age % 16 == 0)
                this.getUser().playSound(MMSounds.EFFECT_GEOMANCY_RUMBLE.get(this.rand.nextInt(3)), 0.6f, 0.5f + this.rand.nextFloat() * 0.2f);
            Vec3d userCenter = this.getUser().getPos().add(0, this.getUser().getHeight() / 2f, 0);
            float radius = 2f;
            Box aabb = new Box(-radius, -radius, -radius, radius, radius, radius);
            aabb = aabb.offset(userCenter);
            for (int i = 0; i < this.getUser().getVelocity().length() * 4; i++) {
                for (int x = (int) Math.floor(aabb.minX); x <= Math.floor(aabb.maxX); x++) {
                    for (int y = (int) Math.floor(aabb.minY); y <= Math.floor(aabb.maxY); y++) {
                        for (int z = (int) Math.floor(aabb.minZ); z <= Math.floor(aabb.maxZ); z++) {
                            Vec3d posVec = new Vec3d(x, y, z);
                            if (posVec.add(0.5, 0.5, 0.5).subtract(userCenter).lengthSquared() > radius * radius)
                                continue;
                            Vec3d motionScaled = this.getUser().getVelocity().normalize().multiply(i);
                            posVec = posVec.add(motionScaled);
                            BlockPos pos = new BlockPos((int) posVec.x, (int) posVec.y, (int) posVec.z);
                            BlockState blockState = this.getUser().getWorld().getBlockState(pos);
                            if (EffectGeomancy.isBlockUseable(blockState) && blockState.getBlock() != Blocks.BEDROCK) {
                                this.justDug = blockState;
                                EntityBlockSwapper.swapBlock(this.getUser().getWorld(), pos, Blocks.AIR.getDefaultState(), 15, false, false);
                            }
                        }
                    }
                }
            }
        }
        if (!this.prevUnderground && this.underground) {
            this.timeUnderground = 0;
            this.getUser().playSound(MMSounds.EFFECT_GEOMANCY_BREAK_MEDIUM.get(this.rand.nextInt(3)), 1f, 0.9f + this.rand.nextFloat() * 0.1f);
            if (this.getUser().getWorld().isClient)
                AdvancedParticleBase.spawnParticle(this.getUser().getWorld(), ParticleHandler.RING2, (float) this.getUser().getX(), (float) this.getUser().getY() + 0.02f, (float) this.getUser().getZ(), 0, 0, 0, false, 0, Math.PI / 2f, 0, 0, 3.5F, 0.83f, 1, 0.39f, 1, 1, 10, true, true, new ParticleComponent[]{
                        new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.ALPHA, ParticleComponent.KeyTrack.startAndEnd(1f, 0f), false),
                        new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.SCALE, ParticleComponent.KeyTrack.startAndEnd(10f, 30f), false)
                });
            this.playGauntletAnimation();
        }
        if (this.prevUnderground && !this.underground) {
            this.timeAboveGround = 0;
            this.getUser().playSound(MMSounds.EFFECT_GEOMANCY_BREAK, 1f, 0.9f + this.rand.nextFloat() * 0.1f);
            if (this.getUser().getWorld().isClient)
                AdvancedParticleBase.spawnParticle(this.getUser().getWorld(), ParticleHandler.RING2, (float) this.getUser().getX(), (float) this.getUser().getY() + 0.02f, (float) this.getUser().getZ(), 0, 0, 0, false, 0, Math.PI / 2f, 0, 0, 3.5F, 0.83f, 1, 0.39f, 1, 1, 10, true, true, new ParticleComponent[]{
                        new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.ALPHA, ParticleComponent.KeyTrack.startAndEnd(1f, 0f), false),
                        new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.SCALE, ParticleComponent.KeyTrack.startAndEnd(10f, 30f), false)
                });
            if (this.timeUnderground > 10)
                this.getUser().setVelocity(this.getUser().getVelocity().multiply(10f));
            else
                this.getUser().setVelocity(this.getUser().getVelocity().multiply(3, 7, 3));

            for (int i = 0; i < 6; i++) {
                if (this.justDug == null) this.justDug = Blocks.DIRT.getDefaultState();
                EntityFallingBlock fallingBlock = new EntityFallingBlock(EntityHandler.FALLING_BLOCK, this.getUser().getWorld(), 80, this.justDug);
                fallingBlock.setPosition(this.getUser().getX(), this.getUser().getY() + 1, this.getUser().getZ());
                fallingBlock.setVelocity(this.getUser().getRandom().nextFloat() * 0.8f - 0.4f, 0.4f + this.getUser().getRandom().nextFloat() * 0.8f, this.getUser().getRandom().nextFloat() * 0.8f - 0.4f);
                this.getUser().getWorld().spawnEntity(fallingBlock);
            }
            this.stopGauntletAnimation();
        }
        this.prevUnderground = this.underground;
    }

    @Override
    public void end() {
        super.end();
        this.stopGauntletAnimation();
    }

    @Override
    public boolean canUse() {
        return super.canUse();
    }

    @Override
    protected boolean canContinueUsing() {
        ItemStack stack = this.getUser().getActiveItem();
        boolean usingGauntlet = stack.getItem() == ItemHandler.EARTHREND_GAUNTLET;
        if (this.whichHand == null) return false;
        return (this.getTicksInUse() <= 1 || !(this.getUser().isOnGround() || (this.getUser().isTouchingWater() && !usingGauntlet)) || this.underground) && this.getUser().getStackInHand(this.whichHand).getItem() == ItemHandler.EARTHREND_GAUNTLET && super.canContinueUsing();
    }

    @Override
    public boolean preventsItemUse(ItemStack stack) {
        if (stack.getItem() == ItemHandler.EARTHREND_GAUNTLET) return false;
        return super.preventsItemUse(stack);
    }

    @Override
    public <E extends GeoEntity> PlayState animationPredicate(AnimationState<E> e, GeckoPlayer.Perspective perspective) {
        e.getController().transitionLength(4);
        if (perspective == GeckoPlayer.Perspective.THIRD_PERSON) {
            float yMotionThreshold = this.getUser() == MinecraftClient.getInstance().player ? 1 : 2;
            if (!this.underground && this.getUser().getActiveItem().getItem() != ItemHandler.EARTHREND_GAUNTLET && this.getUser().getVelocity().getY() < yMotionThreshold) {
                e.getController().setAnimation(FALL_ANIM);
            } else {
                e.getController().setAnimation(DRILL_ANIM);
            }
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void codeAnimations(MowzieGeoModel<? extends GeoEntity> model, float partialTick) {
        super.codeAnimations(model, partialTick);
        float faceMotionController = 1f - model.getControllerValueInverted("FaceVelocityController");
        Vec3d moveVec = this.getUser().getVelocity().normalize();
        this.pitch = (float) MathHelper.lerp(0.3 * partialTick, this.pitch, moveVec.getY());
        MowzieGeoBone com = model.getMowzieBone("CenterOfMass");
        com.setRotX((float) (-Math.PI / 2f + Math.PI / 2f * this.pitch) * faceMotionController);

        float spinSpeed = 0.35f;
        if (faceMotionController < 1 && this.spinAmount < Math.PI * 2f - 0.01 && this.spinAmount > 0.01) {
            float f = (float) ((Math.PI * 2f - this.spinAmount) / (Math.PI * 2f));
            f = (float) Math.pow(f, 0.5);
            this.spinAmount += partialTick * spinSpeed * f;
            if (this.spinAmount > Math.PI * 2f) {
                this.spinAmount = 0;
            }
        } else {
            this.spinAmount += faceMotionController * partialTick * spinSpeed;
            this.spinAmount = (float) (this.spinAmount % (Math.PI * 2));
        }
        MowzieGeoBone waist = model.getMowzieBone("Waist");
        waist.addRotY(-this.spinAmount);
    }

    @Override
    public NbtCompound writeNBT() {
        NbtCompound compound = super.writeNBT();
        if (this.isUsing() && this.whichHand != null) {
            compound.putInt("whichHand", this.whichHand.ordinal());
        }
        return compound;
    }

    @Override
    public void readNBT(NbtElement nbt) {
        super.readNBT(nbt);
        if (this.isUsing()) {
            NbtCompound compound = (NbtCompound) nbt;
            this.whichHand = Hand.values()[compound.getInt("whichHand")];
        }
    }
}
