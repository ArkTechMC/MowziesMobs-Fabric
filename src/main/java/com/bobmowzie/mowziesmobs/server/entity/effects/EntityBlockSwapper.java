package com.bobmowzie.mowziesmobs.server.entity.effects;

import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.sculptor.EntitySculptor;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Created by BobMowzie on 7/8/2018.
 */
public class EntityBlockSwapper extends Entity {
    private static final TrackedData<BlockState> ORIG_BLOCK_STATE = DataTracker.registerData(EntityBlockSwapper.class, TrackedDataHandlerRegistry.BLOCK_STATE);
    private static final TrackedData<Integer> RESTORE_TIME = DataTracker.registerData(EntityBlockSwapper.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<BlockPos> POS = DataTracker.registerData(EntityBlockSwapper.class, TrackedDataHandlerRegistry.BLOCK_POS);
    protected int duration;
    protected boolean breakParticlesEnd;
    private BlockPos pos;

    public EntityBlockSwapper(EntityType<? extends EntityBlockSwapper> type, World world) {
        super(type, world);
        this.breakParticlesEnd = false;
    }

    public EntityBlockSwapper(EntityType<? extends EntityBlockSwapper> type, World world, BlockPos pos, BlockState newBlock, int duration, boolean breakParticlesStart, boolean breakParticlesEnd) {
        super(type, world);
        this.setStorePos(pos);
        this.setRestoreTime(duration);
        this.breakParticlesEnd = breakParticlesEnd;
        this.setPosition(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
        if (!world.isClient) {
            this.setOrigBlock(world.getBlockState(pos));
            if (breakParticlesStart) world.breakBlock(pos, false);
            world.setBlockState(pos, newBlock, 19);
        }
        List<EntityBlockSwapper> swappers = world.getNonSpectatingEntities(EntityBlockSwapper.class, this.getBoundingBox());
        for (EntityBlockSwapper swapper : swappers) {
            if (swapper == this) continue;
            if (swapper instanceof EntityBlockSwapperSculptor swapperSculptor) {
                this.setOrigBlock(swapperSculptor.getOrigBlockAtLocation(pos));
            } else {
                this.setOrigBlock(swapper.getOrigBlock());
                swapper.discard();
            }
        }
    }

    public static void swapBlock(World world, BlockPos pos, BlockState newBlock, int duration, boolean breakParticlesStart, boolean breakParticlesEnd) {
        if (!world.isClient) {
            EntityBlockSwapper swapper = new EntityBlockSwapper(EntityHandler.BLOCK_SWAPPER, world, pos, newBlock, duration, breakParticlesStart, breakParticlesEnd);
            world.spawnEntity(swapper);
        }
    }

    public boolean isBlockPosInsideSwapper(BlockPos pos) {
        return pos.equals(this.getStorePos());
    }

    @Override
    public boolean shouldRender(double p_145770_1_, double p_145770_3_, double p_145770_5_) {
        return false;
    }

    @Override
    protected void initDataTracker() {
        this.getDataTracker().startTracking(ORIG_BLOCK_STATE, Blocks.DIRT.getDefaultState());
        this.getDataTracker().startTracking(RESTORE_TIME, 20);
        this.getDataTracker().startTracking(POS, new BlockPos(0, 0, 0));
    }

    public int getRestoreTime() {
        return this.dataTracker.get(RESTORE_TIME);
    }

    public void setRestoreTime(int restoreTime) {
        this.dataTracker.set(RESTORE_TIME, restoreTime);
        this.duration = restoreTime;
    }

    public BlockPos getStorePos() {
        return this.dataTracker.get(POS);
    }

    public void setStorePos(BlockPos bpos) {
        this.dataTracker.set(POS, bpos);
        this.pos = bpos;
    }

    @Nullable
    public BlockState getOrigBlock() {
        return this.getDataTracker().get(ORIG_BLOCK_STATE);
    }

    public void setOrigBlock(BlockState block) {
        this.getDataTracker().set(ORIG_BLOCK_STATE, block);
    }

    public void restoreBlock() {
        List<EntityBlockSwapper> swappers = this.getWorld().getNonSpectatingEntities(EntityBlockSwapper.class, this.getBoundingBox());
        if (!this.getWorld().isClient) {
            boolean canReplace = true;
            for (EntityBlockSwapper swapper : swappers) {
                if (swapper == this) continue;
                if (swapper.isBlockPosInsideSwapper(this.pos)) {
                    canReplace = false;
                    break;
                }
            }
            if (canReplace) {
                if (this.breakParticlesEnd) this.getWorld().breakBlock(this.pos, false);
                this.getWorld().setBlockState(this.pos, this.getOrigBlock(), 19);
            }
            this.discard();
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.canRestoreBlock()) this.restoreBlock();
    }

    protected boolean canRestoreBlock() {
        return this.age > this.duration && this.getWorld().getNonSpectatingEntities(PlayerEntity.class, this.getBoundingBox()).isEmpty();
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound compound) {
        BlockState blockState = this.getDataTracker().get(ORIG_BLOCK_STATE);
        compound.put("block", NbtHelper.fromBlockState(blockState));
        compound.putInt("restoreTime", this.getRestoreTime());
        compound.putInt("storePosX", this.getStorePos().getX());
        compound.putInt("storePosY", this.getStorePos().getY());
        compound.putInt("storePosZ", this.getStorePos().getZ());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound compound) {
        NbtElement blockNBT = compound.get("block");
        if (blockNBT != null) {
            BlockState blockState = NbtHelper.toBlockState(this.getWorld().createCommandRegistryWrapper(RegistryKeys.BLOCK), (NbtCompound) blockNBT);
            this.setOrigBlock(blockState);
        }
        this.setRestoreTime(compound.getInt("restoreTime"));
        this.setStorePos(new BlockPos(
                compound.getInt("storePosX"),
                compound.getInt("storePosY"),
                compound.getInt("storePosZ")
        ));
    }

    // Like the regular block swapper, but clears out a whole cylinder for the sculptor's test
    public static class EntityBlockSwapperSculptor extends EntityBlockSwapper {
        private final int height;
        private final int radius;
        private final BlockState[][][] origStates;

        public EntityBlockSwapperSculptor(EntityType<? extends EntityBlockSwapperSculptor> type, World world) {
            super(type, world);
            this.breakParticlesEnd = false;
            this.height = EntitySculptor.TEST_HEIGHT + 3;
            this.radius = EntitySculptor.TEST_RADIUS;
            this.origStates = new BlockState[this.height][this.radius * 2][this.radius * 2];
            this.setBoundingBox(this.calculateBoundingBox());
        }

        public EntityBlockSwapperSculptor(EntityType<? extends EntityBlockSwapperSculptor> type, World world, BlockPos pos, BlockState newBlock, int duration, boolean breakParticlesStart, boolean breakParticlesEnd) {
            super(type, world);
            this.height = EntitySculptor.TEST_HEIGHT + 3;
            this.radius = EntitySculptor.TEST_RADIUS;
            this.origStates = new BlockState[this.height][this.radius * 2][this.radius * 2];
            this.setStorePos(pos);
            this.setRestoreTime(duration);
            this.breakParticlesEnd = breakParticlesEnd;
            this.setPosition(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
            this.setBoundingBox(this.calculateBoundingBox());

            // Get any other sculptor block swappers it overlaps with. We need special logic to make sure they can't replace each other's blocks
            List<EntityBlockSwapperSculptor> swapperSculptors = world.getNonSpectatingEntities(EntityBlockSwapperSculptor.class, this.getBoundingBox());

            // Loop over the blocks inside this one and replace them
            if (!world.isClient) {
                for (int k = 0; k < this.height; k++) {
                    for (int i = -this.radius; i < this.radius; i++) {
                        for (int j = -this.radius; j < this.radius; j++) {
                            BlockPos thisPos = pos.add(i, k, j);
                            if (this.isBlockPosInsideSwapper(thisPos)) {
                                if (world.getBlockState(thisPos).getBlock() == Blocks.BEDROCK) continue;
                                this.origStates[k][i + this.radius][j + this.radius] = world.getBlockState(thisPos);
                                if (breakParticlesStart) world.breakBlock(thisPos, false);
                                world.setBlockState(thisPos, newBlock, 19);
                                for (EntityBlockSwapperSculptor swapper : swapperSculptors) {
                                    if (swapper == this) continue;
                                    if (swapper.getOrigBlockAtLocation(thisPos) != null) {
                                        this.origStates[k][i + this.radius][j + this.radius] = swapper.getOrigBlockAtLocation(thisPos);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Now handle any regular block swappers it overlaps with
            List<EntityBlockSwapperSculptor> swappers = world.getNonSpectatingEntities(EntityBlockSwapperSculptor.class, this.getBoundingBox());
            if (!swappers.isEmpty()) {
                for (EntityBlockSwapper swapper : swappers) {
                    if (swapper == this) continue;
                    if (!(swapper instanceof EntityBlockSwapperSculptor)) {
                        this.setOrigBlockAtLocation(swapper.getStorePos(), swapper.getOrigBlock());
                    }
                }
            }
        }

        @Override
        public boolean isBlockPosInsideSwapper(BlockPos pos) {
            return new Vec2f(pos.getX() - this.getStorePos().getX(), pos.getZ() - this.getStorePos().getZ()).length() < EntitySculptor.testRadiusAtHeight(pos.getY() - this.getY()) && this.getBoundingBox().contains(new Vec3d(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5));
        }

        public void setOrigBlockAtLocation(BlockPos pos, BlockState state) {
            if (this.isBlockPosInsideSwapper(pos)) {
                BlockPos indices = this.posToArrayIndices(pos);
                this.origStates[indices.getY()][indices.getX()][indices.getZ()] = state;
            }
        }

        public BlockState getOrigBlockAtLocation(BlockPos pos) {
            if (this.isBlockPosInsideSwapper(pos)) {
                BlockPos indices = this.posToArrayIndices(pos);
                return this.origStates[indices.getY()][indices.getX()][indices.getZ()];
            }
            return null;
        }

        protected BlockPos posToArrayIndices(BlockPos pos) {
            return pos.subtract(this.getStorePos()).add(this.radius, 0, this.radius);
        }

        @Override
        protected Box calculateBoundingBox() {
            return EntityDimensions.changing(this.radius * 2, this.height).getBoxAt(this.getPos());
        }

        @Override
        public void restoreBlock() {
            if (!this.getWorld().isClient) {
                List<EntityBlockSwapper> swappers = this.getWorld().getNonSpectatingEntities(EntityBlockSwapper.class, this.getBoundingBox());
                for (int k = 0; k < this.height; k++) {
                    for (int i = -this.radius; i < this.radius; i++) {
                        for (int j = -this.radius; j < this.radius; j++) {
                            if (!this.getWorld().isClient) {
                                BlockPos thisPos = this.getStorePos().add(i, k, j);
                                if (this.isBlockPosInsideSwapper(thisPos)) {
                                    boolean canReplace = true;
                                    for (EntityBlockSwapper swapper : swappers) {
                                        if (swapper == this) continue;
                                        if (swapper.isBlockPosInsideSwapper(thisPos)) {
                                            canReplace = false;
                                            break;
                                        }
                                    }
                                    if (canReplace) {
                                        BlockState restoreState = this.origStates[k][i + this.radius][j + this.radius];
                                        if (restoreState != null) {
                                            if (this.breakParticlesEnd) this.getWorld().breakBlock(thisPos, false);
                                            this.getWorld().setBlockState(thisPos, restoreState, 19);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                this.discard();
            }
        }

        @Override
        protected boolean canRestoreBlock() {
            return this.age > this.duration && this.getWorld().getEntitiesByClass(EntitySculptor.class, this.getBoundingBox(), EntitySculptor::isTesting).isEmpty();
        }

        @Override
        public void writeCustomDataToNbt(NbtCompound compound) {
            compound.putInt("restoreTime", this.getRestoreTime());
            compound.putInt("storePosX", this.getStorePos().getX());
            compound.putInt("storePosY", this.getStorePos().getY());
            compound.putInt("storePosZ", this.getStorePos().getZ());
            for (int i = 0; i < this.radius * 2; i++) {
                for (int j = 0; j < this.radius * 2; j++) {
                    for (int k = 0; k < this.height; k++) {
                        BlockState block = this.origStates[k][i][j];
                        if (block != null)
                            compound.put("block_" + i + "_" + j + "_" + k, NbtHelper.fromBlockState(block));
                    }
                }
            }
        }

        @Override
        public void readCustomDataFromNbt(NbtCompound compound) {
            this.setRestoreTime(compound.getInt("restoreTime"));
            this.setStorePos(new BlockPos(
                    compound.getInt("storePosX"),
                    compound.getInt("storePosY"),
                    compound.getInt("storePosZ")
            ));
            for (int i = 0; i < this.radius * 2; i++) {
                for (int j = 0; j < this.radius * 2; j++) {
                    for (int k = 0; k < this.height; k++) {
                        NbtElement blockNBT = compound.get("block_" + i + "_" + j + "_" + k);
                        if (blockNBT != null) {
                            BlockState blockState = NbtHelper.toBlockState(this.getWorld().createCommandRegistryWrapper(RegistryKeys.BLOCK), (NbtCompound) blockNBT);
                            this.origStates[k][i][j] = blockState;
                        }
                    }
                }
            }
        }
    }
}
