package com.bobmowzie.mowziesmobs.server.ability.abilities.player.geomancy;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.particle.ParticleHandler;
import com.bobmowzie.mowziesmobs.client.particle.util.AdvancedParticleBase;
import com.bobmowzie.mowziesmobs.client.particle.util.ParticleComponent;
import com.bobmowzie.mowziesmobs.server.ability.*;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.effects.geomancy.EntityBoulderBase;
import com.bobmowzie.mowziesmobs.server.entity.effects.geomancy.EntityBoulderProjectile;
import com.bobmowzie.mowziesmobs.server.entity.effects.geomancy.EntityGeomancyBase;
import com.bobmowzie.mowziesmobs.server.potion.EffectGeomancy;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import software.bernie.geckolib.core.animation.RawAnimation;

public class SpawnBoulderAbility extends PlayerAbility {
    public static final double SPAWN_BOULDER_REACH = 5;
    private static final RawAnimation SPAWN_BOULDER_START_ANIM = RawAnimation.begin().thenPlay("spawn_boulder_start");
    private static final RawAnimation SPAWN_BOULDER_INSTANT_ANIM = RawAnimation.begin().thenPlay("spawn_boulder_instant");
    private static final RawAnimation SPAWN_BOULDER_END_ANIM = RawAnimation.begin().thenPlay("spawn_boulder_end");
    private static final int MAX_CHARGE = 60;
    public BlockPos spawnBoulderPos = new BlockPos(0, 0, 0);
    public Vec3d lookPos = new Vec3d(0, 0, 0);
    private BlockState spawnBoulderBlock = Blocks.DIRT.getDefaultState();
    private int spawnBoulderCharge = 0;

    public SpawnBoulderAbility(AbilityType<PlayerEntity, ? extends Ability> abilityType, PlayerEntity user) {
        super(abilityType, user, new AbilitySection[]{
                new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.STARTUP, MAX_CHARGE),
                new AbilitySection.AbilitySectionInstant(AbilitySection.AbilitySectionType.ACTIVE),
                new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.RECOVERY, 12)
        });
    }

    @Override
    public void start() {
        super.start();
        this.playAnimation(SPAWN_BOULDER_START_ANIM);
        if (!MinecraftClient.getInstance().options.useKey.isPressed()) this.nextSection();
    }

    @Override
    public boolean tryAbility() {
        Vec3d from = this.getUser().getCameraPosVec(1.0f);
        Vec3d to = from.add(this.getUser().getRotationVector().multiply(SPAWN_BOULDER_REACH));
        BlockHitResult result = this.getUser().getWorld().raycast(new RaycastContext(from, to, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, this.getUser()));
        if (result.getType() == HitResult.Type.BLOCK) {
            this.lookPos = result.getPos();
        }

        this.spawnBoulderPos = result.getBlockPos();
        this.spawnBoulderBlock = this.getUser().getWorld().getBlockState(this.spawnBoulderPos);
        if (result.getSide() == Direction.DOWN) return false;
        BlockState blockAbove = this.getUser().getWorld().getBlockState(this.spawnBoulderPos.up());
        if (blockAbove.blocksMovement())
            return false;
        return EffectGeomancy.isBlockUseable(this.spawnBoulderBlock);
    }

    @Override
    public void tickUsing() {
        super.tickUsing();
        if (this.getCurrentSection().sectionType == AbilitySection.AbilitySectionType.STARTUP) {
            this.spawnBoulderCharge++;
            if (this.spawnBoulderCharge > 2)
                this.getUser().addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 3, 3, false, false));
            if (this.spawnBoulderCharge == 1 && this.getUser().getWorld().isClient)
                MowziesMobs.PROXY.playBoulderChargeSound(this.getUser());
            if ((this.spawnBoulderCharge + 10) % 10 == 0 && this.spawnBoulderCharge < 40) {
                if (this.getUser().getWorld().isClient) {
                    AdvancedParticleBase.spawnParticle(this.getUser().getWorld(), ParticleHandler.RING2, (float) this.getUser().getX(), (float) this.getUser().getY() + this.getUser().getHeight() / 2f, (float) this.getUser().getZ(), 0, 0, 0, false, 0, Math.PI / 2f, 0, 0, 3.5F, 0.83f, 1, 0.39f, 1, 1, 10, true, true, new ParticleComponent[]{
                            new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.ALPHA, ParticleComponent.KeyTrack.startAndEnd(0f, 0.7f), false),
                            new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.SCALE, ParticleComponent.KeyTrack.startAndEnd((0.8f + 2.7f * this.spawnBoulderCharge / 60f) * 10f, 0), false)
                    });
                }
            }
            if (this.spawnBoulderCharge == 50) {
                if (this.getUser().getWorld().isClient) {
                    AdvancedParticleBase.spawnParticle(this.getUser().getWorld(), ParticleHandler.RING2, (float) this.getUser().getX(), (float) this.getUser().getY() + this.getUser().getHeight() / 2f, (float) this.getUser().getZ(), 0, 0, 0, true, 0, 0, 0, 0, 3.5F, 0.83f, 1, 0.39f, 1, 1, 20, true, true, new ParticleComponent[]{
                            new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.ALPHA, ParticleComponent.KeyTrack.startAndEnd(0.7f, 0f), false),
                            new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.SCALE, ParticleComponent.KeyTrack.startAndEnd(0, 40f), false)
                    });
                }
                this.getUser().playSound(MMSounds.EFFECT_GEOMANCY_MAGIC_SMALL, 1, 1f);
            }

            int size = this.getBoulderSize() + 1;
            EntityDimensions dim = EntityBoulderBase.SIZE_MAP.get(EntityGeomancyBase.GeomancyTier.values()[size + 1]);
            if (
                    !this.getUser().getWorld().isSpaceEmpty(dim.getBoxAt(this.spawnBoulderPos.getX() + 0.5F, this.spawnBoulderPos.getY() + 2, this.spawnBoulderPos.getZ() + 0.5F))
                            || this.getUser().squaredDistanceTo(this.spawnBoulderPos.getX(), this.spawnBoulderPos.getY(), this.spawnBoulderPos.getZ()) > 36
            ) {
                this.nextSection();
            }
        }
    }

    @Override
    protected void beginSection(AbilitySection section) {
        if (section.sectionType == AbilitySection.AbilitySectionType.ACTIVE) {
            this.spawnBoulder();
        }
    }

    private int getBoulderSize() {
        return (int) Math.min(Math.max(0, Math.floor(this.spawnBoulderCharge / 10.f) - 1), 2);
    }

    private void spawnBoulder() {
        if (this.spawnBoulderCharge <= 2) {
            this.playAnimation(SPAWN_BOULDER_INSTANT_ANIM);
        } else {
            this.playAnimation(SPAWN_BOULDER_END_ANIM);
        }

        int size = this.getBoulderSize();
        if (this.spawnBoulderCharge >= 60) size = 3;
        EntityBoulderProjectile boulder = new EntityBoulderProjectile(EntityHandler.BOULDER_PROJECTILE, this.getUser().getWorld(), this.getUser(), this.spawnBoulderBlock, this.spawnBoulderPos, EntityGeomancyBase.GeomancyTier.values()[size + 1]);
        boulder.setPosition(this.spawnBoulderPos.getX() + 0.5F, this.spawnBoulderPos.getY() + 2, this.spawnBoulderPos.getZ() + 0.5F);
        if (!this.getUser().getWorld().isClient && boulder.checkCanSpawn()) {
            this.getUser().getWorld().spawnEntity(boulder);
        }

        if (this.spawnBoulderCharge > 2) {
            Vec3d playerEyes = this.getUser().getCameraPosVec(1);
            Vec3d vec = playerEyes.subtract(this.lookPos).normalize();
            float yaw = (float) Math.atan2(vec.z, vec.x);
            float pitch = (float) Math.asin(vec.y);
            this.getUser().setYaw((float) (yaw * 180f / Math.PI + 90));
            this.getUser().setPitch((float) (pitch * 180f / Math.PI));
        }
        this.spawnBoulderCharge = 0;
    }

    @Override
    public void onRightMouseUp(PlayerEntity player) {
        super.onRightMouseUp(player);
        if (this.isUsing() && this.getCurrentSection().sectionType == AbilitySection.AbilitySectionType.STARTUP) {
            if (player.squaredDistanceTo(this.spawnBoulderPos.getX(), this.spawnBoulderPos.getY(), this.spawnBoulderPos.getZ()) < 36) {
                this.nextSection();
            } else {
                this.spawnBoulderCharge = 0;
            }
        }
    }

    @Override
    public boolean canUse() {
        return EffectGeomancy.canUse(this.getUser()) && super.canUse();
    }

    @Override
    public void end() {
        this.spawnBoulderCharge = 0;
        super.end();
    }

    @Override
    public void readNBT(NbtElement nbt) {
        super.readNBT(nbt);
        if (this.getCurrentSection().sectionType == AbilitySection.AbilitySectionType.STARTUP)
            this.spawnBoulderCharge = this.getTicksInSection();
    }

    @Override
    public void onRightClickBlock(PlayerEntity player, World world, Hand hand, BlockHitResult hitResult) {
        super.onRightClickBlock(player, world, hand, hitResult);
        if (!world.isClient)
            AbilityHandler.INSTANCE.sendAbilityMessage(player, AbilityHandler.SPAWN_BOULDER_ABILITY);
    }

    @Override
    public void onRightClickEmpty(PlayerEntity player, Hand hand) {
        super.onRightClickEmpty(player, hand);
        AbilityHandler.INSTANCE.sendPlayerTryAbilityMessage(player, AbilityHandler.SPAWN_BOULDER_ABILITY);
    }

    @Override
    public void onRenderTick() {
        super.onRenderTick();
        if (this.isUsing() && this.getCurrentSection().sectionType == AbilitySection.AbilitySectionType.STARTUP && this.getTicksInSection() > 1) {
            Vec3d playerEyes = this.getUser().getCameraPosVec(MinecraftClient.getInstance().getTickDelta());
            Vec3d vec = playerEyes.subtract(this.lookPos).normalize();
            float yaw = (float) Math.atan2(vec.z, vec.x);
            float pitch = (float) Math.asin(vec.y);
            this.getUser().setYaw((float) (yaw * 180f / Math.PI + 90));
            this.getUser().setPitch((float) (pitch * 180f / Math.PI));
            this.getUser().headYaw = this.getUser().getYaw();
            this.getUser().prevYaw = this.getUser().getYaw();
            this.getUser().prevPitch = this.getUser().getPitch();
            this.getUser().prevHeadYaw = this.getUser().headYaw;
        }
    }
}
