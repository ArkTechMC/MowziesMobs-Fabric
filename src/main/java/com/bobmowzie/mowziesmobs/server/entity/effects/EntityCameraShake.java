package com.bobmowzie.mowziesmobs.server.entity.effects;

import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityCameraShake extends Entity {
    private static final TrackedData<Float> RADIUS = DataTracker.registerData(EntityCameraShake.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Float> MAGNITUDE = DataTracker.registerData(EntityCameraShake.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Integer> DURATION = DataTracker.registerData(EntityCameraShake.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> FADE_DURATION = DataTracker.registerData(EntityCameraShake.class, TrackedDataHandlerRegistry.INTEGER);

    public EntityCameraShake(EntityType<?> type, World world) {
        super(type, world);
    }

    public EntityCameraShake(World world, Vec3d position, float radius, float magnitude, int duration, int fadeDuration) {
        super(EntityHandler.CAMERA_SHAKE, world);
        this.setRadius(radius);
        this.setMagnitude(magnitude);
        this.setDuration(duration);
        this.setFadeDuration(fadeDuration);
        this.setPosition(position.getX(), position.getY(), position.getZ());
    }

    public static void cameraShake(World world, Vec3d position, float radius, float magnitude, int duration, int fadeDuration) {
        if (!world.isClient) {
            EntityCameraShake cameraShake = new EntityCameraShake(world, position, radius, magnitude, duration, fadeDuration);
            world.spawnEntity(cameraShake);
        }
    }

    @Environment(EnvType.CLIENT)
    public float getShakeAmount(PlayerEntity player, float delta) {
        float ticksDelta = this.age + delta;
        float timeFrac = 1.0f - (ticksDelta - this.getDuration()) / (this.getFadeDuration() + 1.0f);
        float baseAmount = ticksDelta < this.getDuration() ? this.getMagnitude() : timeFrac * timeFrac * this.getMagnitude();
        Vec3d playerPos = player.getCameraPosVec(delta);
        float distFrac = (float) (1.0f - MathHelper.clamp(this.getPos().distanceTo(playerPos) / this.getRadius(), 0, 1));
        return baseAmount * distFrac * distFrac;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.age > this.getDuration() + this.getFadeDuration()) this.discard();
    }

    @Override
    protected void initDataTracker() {
        this.getDataTracker().startTracking(RADIUS, 10.0f);
        this.getDataTracker().startTracking(MAGNITUDE, 1.0f);
        this.getDataTracker().startTracking(DURATION, 0);
        this.getDataTracker().startTracking(FADE_DURATION, 5);
    }

    public float getRadius() {
        return this.getDataTracker().get(RADIUS);
    }

    public void setRadius(float radius) {
        this.getDataTracker().set(RADIUS, radius);
    }

    public float getMagnitude() {
        return this.getDataTracker().get(MAGNITUDE);
    }

    public void setMagnitude(float magnitude) {
        this.getDataTracker().set(MAGNITUDE, magnitude);
    }

    public int getDuration() {
        return this.getDataTracker().get(DURATION);
    }

    public void setDuration(int duration) {
        this.getDataTracker().set(DURATION, duration);
    }

    public int getFadeDuration() {
        return this.getDataTracker().get(FADE_DURATION);
    }

    public void setFadeDuration(int fadeDuration) {
        this.getDataTracker().set(FADE_DURATION, fadeDuration);
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound compound) {
        this.setRadius(compound.getFloat("radius"));
        this.setMagnitude(compound.getFloat("magnitude"));
        this.setDuration(compound.getInt("duration"));
        this.setFadeDuration(compound.getInt("fade_duration"));
        this.age = compound.getInt("ticks_existed");
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound compound) {
        compound.putFloat("radius", this.getRadius());
        compound.putFloat("magnitude", this.getMagnitude());
        compound.putInt("duration", this.getDuration());
        compound.putInt("fade_duration", this.getFadeDuration());
        compound.putInt("ticks_existed", this.age);
    }
}
