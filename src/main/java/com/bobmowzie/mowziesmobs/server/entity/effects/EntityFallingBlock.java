package com.bobmowzie.mowziesmobs.server.entity.effects;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.World;

public class EntityFallingBlock extends Entity {
    private static final TrackedData<BlockState> BLOCK_STATE = DataTracker.registerData(EntityFallingBlock.class, TrackedDataHandlerRegistry.BLOCK_STATE);
    private static final TrackedData<Integer> DURATION = DataTracker.registerData(EntityFallingBlock.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> TICKS_EXISTED = DataTracker.registerData(EntityFallingBlock.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<String> MODE = DataTracker.registerData(EntityFallingBlock.class, TrackedDataHandlerRegistry.STRING);
    private static final TrackedData<Float> ANIM_V_Y = DataTracker.registerData(EntityFallingBlock.class, TrackedDataHandlerRegistry.FLOAT);
    public static float GRAVITY = 0.1f;
    public double prevMotionX, prevMotionY, prevMotionZ;
    public float animY = 0;
    public float prevAnimY = 0;
    public EntityFallingBlock(EntityType<?> entityTypeIn, World worldIn) {
        super(entityTypeIn, worldIn);
        this.setBlock(Blocks.DIRT.getDefaultState());
        this.setDuration(70);
    }

    public EntityFallingBlock(EntityType<?> entityTypeIn, World worldIn, int duration, BlockState blockState) {
        super(entityTypeIn, worldIn);
        this.setBlock(blockState);
        this.setDuration(duration);
    }

    public EntityFallingBlock(EntityType<?> entityTypeIn, World worldIn, BlockState blockState, float vy) {
        super(entityTypeIn, worldIn);
        this.setBlock(blockState);
        this.setMode(EnumFallingBlockMode.POPUP_ANIM);
        this.setAnimVY(vy);
    }

    @Override
    public void onAddedToWorld() {
        if (this.getVelocity().getX() > 0 || this.getVelocity().getZ() > 0)
            this.setYaw((float) ((180f / Math.PI) * Math.atan2(this.getVelocity().getX(), this.getVelocity().getZ())));
        this.setPitch(this.getPitch() + this.random.nextFloat() * 360);
        super.onAddedToWorld();
    }

    @Override
    public void tick() {
        if (this.getMode() == EnumFallingBlockMode.POPUP_ANIM) {
            this.setVelocity(0, 0, 0);
        }
        this.prevMotionX = this.getVelocity().x;
        this.prevMotionY = this.getVelocity().y;
        this.prevMotionZ = this.getVelocity().z;
        super.tick();
        if (this.getMode() == EnumFallingBlockMode.MOBILE) {
            this.setVelocity(this.getVelocity().subtract(0, GRAVITY, 0));
            if (this.isOnGround()) this.setVelocity(this.getVelocity().multiply(0.7));
            else this.setPitch(this.getPitch() + 15);
            this.move(MovementType.SELF, this.getVelocity());

            if (this.age > this.getDuration()) this.discard();
        } else {
            float animVY = this.getAnimVY();
            this.prevAnimY = this.animY;
            this.animY += animVY;
            this.setAnimVY(animVY - GRAVITY);
            if (this.animY < -0.5) this.discard();
        }
    }

    @Override
    protected void initDataTracker() {
        this.getDataTracker().startTracking(BLOCK_STATE, Blocks.DIRT.getDefaultState());
        this.getDataTracker().startTracking(DURATION, 70);
        this.getDataTracker().startTracking(TICKS_EXISTED, 0);
        this.getDataTracker().startTracking(MODE, EnumFallingBlockMode.MOBILE.toString());
        this.getDataTracker().startTracking(ANIM_V_Y, 1f);
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound compound) {
        NbtElement blockStateCompound = compound.get("block");
        if (blockStateCompound != null) {
            BlockState blockState = NbtHelper.toBlockState(this.getWorld().createCommandRegistryWrapper(RegistryKeys.BLOCK), (NbtCompound) blockStateCompound);
            this.setBlock(blockState);
        }
        this.setDuration(compound.getInt("duration"));
        this.age = compound.getInt("ticksExisted");
        this.getDataTracker().set(MODE, compound.getString("mode"));
        this.setAnimVY(compound.getFloat("vy"));

    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound compound) {
        BlockState blockState = this.getBlock();
        if (blockState != null) compound.put("block", NbtHelper.fromBlockState(blockState));
        compound.putInt("duration", this.getDuration());
        compound.putInt("ticksExisted", this.age);
        compound.putString("mode", this.getDataTracker().get(MODE));
        compound.putFloat("vy", this.getDataTracker().get(ANIM_V_Y));
    }

    public BlockState getBlock() {
        return this.getDataTracker().get(BLOCK_STATE);
    }

    public void setBlock(BlockState block) {
        this.getDataTracker().set(BLOCK_STATE, block);
    }

    public int getDuration() {
        return this.getDataTracker().get(DURATION);
    }

    public void setDuration(int duration) {
        this.getDataTracker().set(DURATION, duration);
    }

    public int getTicksExisted() {
        return this.getDataTracker().get(TICKS_EXISTED);
    }

    public void setTicksExisted(int ticksExisted) {
        this.getDataTracker().set(TICKS_EXISTED, ticksExisted);
    }

    public EnumFallingBlockMode getMode() {
        String mode = this.getDataTracker().get(MODE);
        if (mode.isEmpty()) return EnumFallingBlockMode.MOBILE;
        return EnumFallingBlockMode.valueOf(this.getDataTracker().get(MODE));
    }

    private void setMode(EnumFallingBlockMode mode) {
        this.getDataTracker().set(MODE, mode.toString());
    }

    public float getAnimVY() {
        return this.getDataTracker().get(ANIM_V_Y);
    }

    private void setAnimVY(float vy) {
        this.getDataTracker().set(ANIM_V_Y, vy);
    }

    public enum EnumFallingBlockMode {
        MOBILE,
        POPUP_ANIM
    }
}
