package com.bobmowzie.mowziesmobs.server.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class GongBlockEntity extends BlockEntity {
    public int ticks;
    public boolean shaking;
    public Direction clickDirection;
    public Direction facing;

    public GongBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityHandler.GONG_BLOCK_ENTITY, pos, state);
        this.facing = state.get(Properties.HORIZONTAL_FACING);
    }

    public static void tick(World level, BlockPos pos, BlockState blockState, GongBlockEntity entity) {
        if (entity.shaking) {
            ++entity.ticks;
        }

        if (entity.ticks >= 148) {
            entity.shaking = false;
            entity.ticks = 0;
        }
    }

    public boolean onSyncedBlockEvent(int p_58837_, int p_58838_) {
        if (p_58837_ == 1) {
            this.clickDirection = Direction.byId(p_58838_);
            this.ticks = 0;
            this.shaking = true;
            return true;
        } else {
            return super.onSyncedBlockEvent(p_58837_, p_58838_);
        }
    }

    public void onHit(Direction p_58835_) {
        BlockPos blockpos = this.getPos();
        this.clickDirection = p_58835_;
        if (this.shaking) {
            this.ticks = 0;
        } else {
            this.shaking = true;
        }

        this.world.addSyncedBlockEvent(blockpos, this.getCachedState().getBlock(), 1, p_58835_.getId());
    }

//    @Override
//    public Box getRenderBoundingBox() {
//        Box bounds = super.getRenderBoundingBox();
//        bounds = bounds.stretch(new Vec3d(facing.rotateYClockwise().getUnitVector()));
//        bounds = bounds.stretch(new Vec3d(facing.rotateYCounterclockwise().getUnitVector()));
//        bounds = bounds.stretch(0, 2, 0);
//        return bounds;
//    }
}
