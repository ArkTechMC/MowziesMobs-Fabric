package com.bobmowzie.mowziesmobs.server.entity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;

public class LegSolver {
    public final Leg[] legs;

    public LegSolver(Leg... legs) {
        this.legs = legs;
    }

    public final void update(LivingEntity entity) {
        double sideTheta = entity.bodyYaw / (180 / Math.PI);
        double sideX = Math.cos(sideTheta);
        double sideZ = Math.sin(sideTheta);
        double forwardTheta = sideTheta + Math.PI / 2;
        double forwardX = Math.cos(forwardTheta);
        double forwardZ = Math.sin(forwardTheta);
        for (Leg leg : this.legs) {
            leg.update(entity, sideX, sideZ, forwardX, forwardZ);
        }
    }

    public static final class Leg {
        public final float forward;

        public final float side;

        private float height;

        private float prevHeight;

        public Leg(float forward, float side) {
            this.forward = forward;
            this.side = side;
        }

        public float getHeight(float delta) {
            return this.prevHeight + (this.height - this.prevHeight) * delta;
        }

        public void update(LivingEntity entity, double sideX, double sideZ, double forwardX, double forwardZ) {
            this.prevHeight = this.height;
            this.height = this.settle(entity, entity.getX() + sideX * this.side + forwardX * this.forward, entity.getY(), entity.getZ() + sideZ * this.side + forwardZ * this.forward, this.height);
        }

        private float settle(LivingEntity entity, double x, double y, double z, float height) {
            BlockPos pos = BlockPos.ofFloored(x, y + 1e-3, z);
            float dist = this.getDistance(entity.getWorld(), pos);
            if (1 - dist < 1e-3) {
                dist = this.getDistance(entity.getWorld(), pos.down()) + (float) y % 1;
            } else {
                dist -= 1 - (y % 1);
            }
            if (entity.isOnGround() && height <= dist) {
                return height == dist ? height : Math.min(height + 0.3F, dist);
            } else if (height > 0) {
                return Math.max(height - 0.4F, dist);
            }
            return height;
        }

        private float getDistance(World world, BlockPos pos) {
            BlockState state = world.getBlockState(pos);
            VoxelShape shape = state.getSidesShape(world, pos);
            float f = 0;
            if (!shape.isEmpty()) {
                Box aabb = shape.getBoundingBox();
                f = (float) aabb.maxY;
            }
            return 1 - Math.min(f, 1);
        }
    }
}
