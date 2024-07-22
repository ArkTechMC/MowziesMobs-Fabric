package com.bobmowzie.mowziesmobs.server.entity.umvuthana;

import com.bobmowzie.mowziesmobs.client.particle.ParticleHandler;
import com.bobmowzie.mowziesmobs.client.particle.util.AdvancedParticleBase;
import com.bobmowzie.mowziesmobs.client.particle.util.ParticleComponent;
import com.bobmowzie.mowziesmobs.server.ability.AbilityHandler;
import com.bobmowzie.mowziesmobs.server.capability.PlayerCapability;
import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import com.bobmowzie.mowziesmobs.server.item.ItemUmvuthanaMask;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class EntityUmvuthanaFollowerToPlayer extends EntityUmvuthanaFollower<PlayerEntity> {
    private static final TrackedData<ItemStack> MASK_STORED = DataTracker.registerData(EntityUmvuthanaFollowerToPlayer.class, TrackedDataHandlerRegistry.ITEM_STACK);
    @Environment(EnvType.CLIENT)
    public Vec3d[] feetPos;

    public EntityUmvuthanaFollowerToPlayer(EntityType<? extends EntityUmvuthanaFollowerToPlayer> type, World world) {
        this(type, world, null);
    }

    public EntityUmvuthanaFollowerToPlayer(EntityType<? extends EntityUmvuthanaFollowerToPlayer> type, World world, PlayerEntity leader) {
        super(type, world, PlayerEntity.class, leader);
        this.experiencePoints = 0;
        if (world.isClient) {
            this.feetPos = new Vec3d[]{new Vec3d(0, 0, 0)};
        }
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MowzieEntity.createAttributes().add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 7)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 20);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.getDataTracker().startTracking(MASK_STORED, new ItemStack(ItemHandler.UMVUTHANA_MASK_FURY, 1));
    }

    @Override
    protected SoundEvent getAmbientSound() {
        if (this.random.nextFloat() < 0.5) return null;
        return super.getAmbientSound();
    }

    @Override
    public void tick() {
        if (this.age > 30 && (this.getLeader() == null || this.getLeader().getHealth() <= 0)) {
            this.deactivate();
        }
        super.tick();
        if (this.getWorld().isClient && this.feetPos != null && this.feetPos.length > 0 && this.active) {
            this.feetPos[0] = this.getPos().add(0, 0.05f, 0);
            if (this.age % 10 == 0)
                AdvancedParticleBase.spawnParticle(this.getWorld(), ParticleHandler.RING2, this.feetPos[0].getX(), this.feetPos[0].getY(), this.feetPos[0].getZ(), 0, 0, 0, false, 0, Math.PI / 2f, 0, 0, 1.5F, 1, 223 / 255f, 66 / 255f, 1, 1, 15, true, false, new ParticleComponent[]{
                        new ParticleComponent.PinLocation(this.feetPos),
                        new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.ALPHA, ParticleComponent.KeyTrack.startAndEnd(1f, 0f), false),
                        new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.SCALE, ParticleComponent.KeyTrack.startAndEnd(1f, 10f), false)
                });
        }
    }

    @Override
    protected ActionResult interactMob(PlayerEntity playerIn, Hand hand) {
        if (playerIn == this.leader) {
            this.deactivate();
        }
        return super.interactMob(playerIn, hand);
    }

    private void deactivate() {
        if (this.getActive() && this.getActiveAbilityType() != DEACTIVATE_ABILITY) {
            AbilityHandler.INSTANCE.sendAbilityMessage(this, DEACTIVATE_ABILITY);
            this.playSound(MMSounds.ENTITY_UMVUTHANA_RETRACT, 1, 1);
        }
    }

    @Override
    protected int getGroupCircleTick() {
        PlayerCapability.IPlayerCapability capability = this.getPlayerCapability();
        if (capability == null) return 0;
        return capability.getTribeCircleTick();
    }

    @Override
    protected int getPackSize() {
        PlayerCapability.IPlayerCapability capability = this.getPlayerCapability();
        if (capability == null) return 0;
        return capability.getPackSize();
    }

    @Override
    protected void addAsPackMember() {
        PlayerCapability.IPlayerCapability capability = this.getPlayerCapability();
        if (capability == null) return;
        capability.addPackMember(this);
    }

    @Override
    protected void removeAsPackMember() {
        PlayerCapability.IPlayerCapability capability = this.getPlayerCapability();
        if (capability == null) return;
        capability.removePackMember(this);
    }

    private PlayerCapability.IPlayerCapability getPlayerCapability() {
        return PlayerCapability.get(this.leader);
    }

    @Override
    public boolean isUmvuthiDevoted() {
        return false;
    }

    @Nullable
    @Override
    protected Identifier getLootTableId() {
        return null;
    }

    @Nullable
    public UUID getOwnerId() {
        return this.getLeader() == null ? null : this.getLeader().getUuid();
    }

    @Nullable
    public Entity getOwner() {
        return this.leader;
    }

    public boolean isTeleportFriendlyBlock(int x, int z, int y, int xOffset, int zOffset) {
        BlockPos blockpos = new BlockPos(x + xOffset, y - 1, z + zOffset);
        BlockState iblockstate = this.getWorld().getBlockState(blockpos);
        return iblockstate.allowsSpawning(this.getWorld(), blockpos, this.getType()) && this.getWorld().isAir(blockpos.up()) && this.getWorld().isAir(blockpos.up(2));
    }

    public ItemStack getStoredMask() {
        return this.getDataTracker().get(MASK_STORED);
    }

    public void setStoredMask(ItemStack mask) {
        this.getDataTracker().set(MASK_STORED, mask);
        this.equipStack(EquipmentSlot.HEAD, mask);
    }

    @Override
    protected ItemStack getDeactivatedMask(ItemUmvuthanaMask mask) {
        return this.getStoredMask();
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound compound) {
        super.readCustomDataFromNbt(compound);
        NbtCompound compoundnbt = compound.getCompound("storedMask");
        this.setStoredMask(ItemStack.fromNbt(compoundnbt));
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound compound) {
        super.writeCustomDataToNbt(compound);
        if (!this.getStoredMask().isEmpty()) {
            compound.put("storedMask", this.getStoredMask().writeNbt(new NbtCompound()));
        }
    }
}
