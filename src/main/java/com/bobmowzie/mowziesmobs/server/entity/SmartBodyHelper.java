package com.bobmowzie.mowziesmobs.server.entity;

import net.minecraft.entity.ai.control.BodyControl;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.MathHelper;

public class SmartBodyHelper extends BodyControl {
    private static final float MAX_ROTATE = 75;

    private static final int HISTORY_SIZE = 10;

    private final MobEntity entity;
    private final double[] histPosX = new double[HISTORY_SIZE];
    private final double[] histPosZ = new double[HISTORY_SIZE];
    private int rotateTime;
    private float targetYawHead;

    public SmartBodyHelper(MobEntity entity) {
        super(entity);
        this.entity = entity;
    }

    public static float approach(float target, float current, float limit) {
        float delta = MathHelper.wrapDegrees(current - target);
        if (delta < -limit) {
            delta = -limit;
        } else if (delta >= limit) {
            delta = limit;
        }
        return target + delta * 0.55F;
    }

    @Override
    public void tick() {
        for (int i = this.histPosX.length - 1; i > 0; i--) {
            this.histPosX[i] = this.histPosX[i - 1];
            this.histPosZ[i] = this.histPosZ[i - 1];
        }
        this.histPosX[0] = this.entity.getX();
        this.histPosZ[0] = this.entity.getZ();
        double dx = this.delta(this.histPosX);
        double dz = this.delta(this.histPosZ);
        double distSq = dx * dx + dz * dz;
        if (distSq > 2.5e-7) {
            boolean isStrafing = false;
            if (this.entity instanceof MowzieEntity) {
                isStrafing = ((MowzieEntity) this.entity).isStrafing();
            }
            if (!isStrafing) {
                double moveAngle = (float) MathHelper.atan2(dz, dx) * (180 / (float) Math.PI) - 90;
                this.entity.bodyYaw += MathHelper.wrapDegrees(moveAngle - this.entity.bodyYaw) * 0.6F;
                this.targetYawHead = this.entity.headYaw;
                this.rotateTime = 0;
            } else {
                super.tick();
            }
        } else if (this.entity.getPassengerList().isEmpty() || !(this.entity.getPassengerList().get(0) instanceof MobEntity)) {
            float limit = MAX_ROTATE;
            if (Math.abs(this.entity.headYaw - this.targetYawHead) > 15) {
                this.rotateTime = 0;
                this.targetYawHead = this.entity.headYaw;
            } else {
                this.rotateTime++;
                final int speed = 10;
                if (this.rotateTime > speed) {
                    limit = Math.max(1 - (this.rotateTime - speed) / (float) speed, 0) * MAX_ROTATE;
                }
            }
            this.entity.bodyYaw = approach(this.entity.headYaw, this.entity.bodyYaw, limit);
        }
    }

    private double delta(double[] arr) {
        return this.mean(arr, 0) - this.mean(arr, HISTORY_SIZE / 2);
    }

    private double mean(double[] arr, int start) {
        double mean = 0;
        for (int i = 0; i < HISTORY_SIZE / 2; i++) {
            mean += arr[i + start];
        }
        return mean / arr.length;
    }
}
