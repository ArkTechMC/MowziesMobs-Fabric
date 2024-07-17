package com.bobmowzie.mowziesmobs.server.block;

import com.bobmowzie.mowziesmobs.server.block.entity.BlockEntityHandler;
import com.bobmowzie.mowziesmobs.server.block.entity.GongBlockEntity;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.*;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

public class GongBlock extends BlockWithEntity {
    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;
    //    public static final EnumProperty<BellAttachType> ATTACHMENT = BlockStateProperties.BELL_ATTACHMENT;
    public static final BooleanProperty POWERED = Properties.POWERED;
    private static final VoxelShape NORTH_SOUTH_SHAPE = Block.createCuboidShape(0.0D, 0.0D, 4.0D, 16.0D, 16.0D, 12.0D);
    private static final VoxelShape EAST_WEST_SHAPE = Block.createCuboidShape(4.0D, 0.0D, 0.0D, 12.0D, 16.0D, 16.0D);

    public GongBlock(Settings properties) {
        super(properties);
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH).with(POWERED, Boolean.FALSE));
    }

    public void neighborUpdate(BlockState p_49729_, World p_49730_, BlockPos p_49731_, Block p_49732_, BlockPos p_49733_, boolean p_49734_) {
        boolean flag = p_49730_.isReceivingRedstonePower(p_49731_);
        if (flag != p_49729_.get(POWERED)) {
            if (flag) {
                this.attemptToRing(p_49730_, p_49731_, null);
            }

            p_49730_.setBlockState(p_49731_, p_49729_.with(POWERED, Boolean.valueOf(flag)), 3);
        }

    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    public void onProjectileHit(World p_49708_, BlockState p_49709_, BlockHitResult p_49710_, ProjectileEntity p_49711_) {
        Entity entity = p_49711_.getOwner();
        PlayerEntity player = entity instanceof PlayerEntity ? (PlayerEntity) entity : null;
        this.onHit(p_49708_, p_49709_, p_49710_, player, true);
    }

    public ActionResult onUse(BlockState p_49722_, World p_49723_, BlockPos p_49724_, PlayerEntity p_49725_, Hand p_49726_, BlockHitResult p_49727_) {
        return this.onHit(p_49723_, p_49722_, p_49727_, p_49725_, true) ? ActionResult.success(p_49723_.isClient) : ActionResult.PASS;
    }

    public boolean onHit(World p_49702_, BlockState p_49703_, BlockHitResult p_49704_, @Nullable PlayerEntity p_49705_, boolean p_49706_) {
        Direction direction = p_49704_.getSide();
        BlockPos blockpos = p_49704_.getBlockPos();
        boolean flag = !p_49706_ || this.isProperHit(p_49703_, direction, p_49704_.getPos().y - (double) blockpos.getY());
        if (flag) {
            this.attemptToRing(p_49705_, p_49702_, blockpos, direction);

            return true;
        } else {
            return false;
        }
    }

    private boolean isProperHit(BlockState p_49740_, Direction p_49741_, double p_49742_) {
        if (p_49741_.getAxis() != Direction.Axis.Y && !(p_49742_ > (double) 0.8124F)) {
            Direction direction = p_49740_.get(FACING);
            return direction.getAxis() == p_49741_.getAxis();
        } else {
            return false;
        }
    }

    public boolean attemptToRing(World p_49713_, BlockPos p_49714_, @Nullable Direction p_49715_) {
        return this.attemptToRing(null, p_49713_, p_49714_, p_49715_);
    }

    public boolean attemptToRing(@Nullable Entity p_152189_, World p_152190_, BlockPos p_152191_, @Nullable Direction p_152192_) {
        BlockEntity blockentity = p_152190_.getBlockEntity(p_152191_);
        if (!p_152190_.isClient && blockentity instanceof GongBlockEntity) {
            if (p_152192_ == null) {
                p_152192_ = p_152190_.getBlockState(p_152191_).get(FACING);
            }

            ((GongBlockEntity) blockentity).onHit(p_152192_);
            p_152190_.playSound(null, p_152191_, MMSounds.BLOCK_GONG, SoundCategory.BLOCKS, 2.0F, 1.0F);
            p_152190_.emitGameEvent(p_152189_, GameEvent.BLOCK_CHANGE, p_152191_);
            return true;
        } else {
            return false;
        }
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> p_49751_) {
        p_49751_.add(FACING, POWERED);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new GongBlockEntity(pos, state);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World p_152194_, BlockState p_152195_, BlockEntityType<T> p_152196_) {
        return checkType(p_152196_, BlockEntityHandler.GONG_BLOCK_ENTITY, GongBlockEntity::tick);
    }

    public VoxelShape getCollisionShape(BlockState p_49760_, BlockView p_49761_, BlockPos p_49762_, ShapeContext p_49763_) {
        return this.getVoxelShape(p_49760_);
    }

    public VoxelShape getOutlineShape(BlockState p_49755_, BlockView p_49756_, BlockPos p_49757_, ShapeContext p_49758_) {
        return this.getVoxelShape(p_49755_);
    }

    public BlockRenderType getRenderType(BlockState blockState) {
        return BlockRenderType.MODEL;
    }

    private VoxelShape getVoxelShape(BlockState p_49767_) {
        Direction direction = p_49767_.get(FACING);
        return direction != Direction.NORTH && direction != Direction.SOUTH ? EAST_WEST_SHAPE : NORTH_SOUTH_SHAPE;
    }

    public boolean canPathfindThrough(BlockState p_49717_, BlockView p_49718_, BlockPos p_49719_, NavigationType p_49720_) {
        return false;
    }

    public PistonBehavior getPistonPushReaction(BlockState p_49765_) {
        return PistonBehavior.DESTROY;
    }

    @Nullable
    public BlockState getPlacementState(ItemPlacementContext context) {
        Direction direction = context.getSide();
        BlockPos blockpos = context.getBlockPos();
        Direction.Axis direction$axis = direction.getAxis();
        if (direction$axis == Direction.Axis.Y) {
            BlockState blockstate = this.getDefaultState().with(FACING, context.getHorizontalPlayerFacing());
            if (blockstate.canPlaceAt(context.getWorld(), blockpos)) {
                return blockstate;
            }
        } else {
            BlockState blockstate1 = this.getDefaultState().with(FACING, direction.getOpposite());
            if (blockstate1.canPlaceAt(context.getWorld(), context.getBlockPos())) {
                return blockstate1;
            }
        }

        return null;
    }

    public void onPlaced(World level, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack itemStack) {
        super.onPlaced(level, pos, state, entity, itemStack);
        if (!level.isClient) {
            for (int i = 0; i < 3; i++) {
                BlockPos abovePos = pos.up(i);
                BlockPos blockpos1 = abovePos.offset(state.get(FACING).rotateYClockwise());
                BlockPos blockpos2 = abovePos;
                BlockPos blockpos3 = abovePos.offset(state.get(FACING).rotateYCounterclockwise());
                BlockState defaultGongPart = BlockHandler.GONG_PART.getDefaultState();
                level.setBlockState(blockpos1, defaultGongPart.with(FACING, state.get(FACING)).with(GongPartBlock.PART, GongPart.SIDE_LEFT).with(GongPartBlock.Y_OFFSET, i), 3);
                level.setBlockState(blockpos3, defaultGongPart.with(FACING, state.get(FACING)).with(GongPartBlock.PART, GongPart.SIDE_RIGHT).with(GongPartBlock.Y_OFFSET, i), 3);
                if (blockpos2 != pos) {
                    level.setBlockState(blockpos2, defaultGongPart.with(FACING, state.get(FACING)).with(GongPartBlock.PART, GongPart.CENTER).with(GongPartBlock.Y_OFFSET, i), 3);
                }
                level.updateNeighbors(abovePos, Blocks.AIR);
                state.updateNeighbors(level, abovePos, 3);
            }
        }

    }

    public void onBreak(World level, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!level.isClient && player.isCreative()) {
            for (int i = 0; i < 2; i++) {
                BlockPos abovePos = pos.up(i);
                BlockPos blockpos1 = abovePos.offset(state.get(FACING).rotateYClockwise());
                BlockPos blockpos2 = abovePos;
                BlockPos blockpos3 = abovePos.offset(state.get(FACING).rotateYCounterclockwise());
                BlockPos[] toBreakPoses = {blockpos1, blockpos2, blockpos3};
                for (BlockPos toBreakPos : toBreakPoses) {
                    BlockState blockstate = level.getBlockState(toBreakPos);
                    if (blockstate.isOf(BlockHandler.GONG_PART)) {
                        level.setBlockState(toBreakPos, Blocks.AIR.getDefaultState(), 35);
                        level.syncWorldEvent(player, 2001, toBreakPos, Block.getRawIdFromState(blockstate));
                    }
                }
            }
        }

        super.onBreak(level, pos, state, player);
    }

    public enum GongPart implements StringIdentifiable {
        SIDE_LEFT("side_left"),
        SIDE_RIGHT("side_right"),
        CENTER("center");

        private final String name;

        GongPart(String name) {
            this.name = name;
        }

        public String toString() {
            return this.name;
        }

        public String asString() {
            return this.name;
        }
    }

    public static class GongPartBlock extends HorizontalFacingBlock {

        public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;
        public static final BooleanProperty POWERED = Properties.POWERED;
        public static final EnumProperty<GongPart> PART = EnumProperty.of("gong_part", GongPart.class);
        public static final IntProperty Y_OFFSET = IntProperty.of("y_offset", 0, 2);

        protected GongPartBlock(Settings properties) {
            super(properties);
            this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH).with(POWERED, Boolean.FALSE).with(PART, GongPart.CENTER).with(Y_OFFSET, 0));
        }

        protected void appendProperties(StateManager.Builder<Block, BlockState> p_49751_) {
            p_49751_.add(FACING, POWERED, PART, Y_OFFSET);
        }

        public void onProjectileHit(World level, BlockState state, BlockHitResult hitResult, ProjectileEntity projectile) {
            BlockPos pos = hitResult.getBlockPos();
            BlockPos basePos = this.getBasePos(state, pos);
            BlockState baseState = level.getBlockState(basePos);
            if (baseState.isOf(BlockHandler.GONG)) {
                BlockHitResult baseHitResult = new BlockHitResult(hitResult.getPos().add(basePos.getX() - pos.getX(), basePos.getY() - pos.getY(), basePos.getZ() - pos.getZ()), hitResult.getSide(), basePos, hitResult.isInsideBlock());
                baseState.getBlock().onProjectileHit(level, baseState, baseHitResult, projectile);
            }
        }

        public ActionResult onUse(BlockState state, World level, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hitResult) {
            BlockPos basePos = this.getBasePos(state, pos);
            BlockState baseState = level.getBlockState(basePos);
            if (baseState.isOf(BlockHandler.GONG)) {
                BlockHitResult baseHitResult = new BlockHitResult(hitResult.getPos().add(basePos.getX() - pos.getX(), basePos.getY() - pos.getY(), basePos.getZ() - pos.getZ()), hitResult.getSide(), basePos, hitResult.isInsideBlock());
                return baseState.getBlock().onUse(baseState, level, pos, player, hand, baseHitResult);
            }
            return super.onUse(state, level, pos, player, hand, hitResult);
        }

        private BlockPos getBasePos(BlockState state, BlockPos pos) {
            BlockPos toReturn = pos.down(state.get(Y_OFFSET));
            if (state.get(PART) == GongPart.SIDE_LEFT) {
                toReturn = toReturn.offset(state.get(FACING).rotateYCounterclockwise());
            } else if (state.get(PART) == GongPart.SIDE_RIGHT) {
                toReturn = toReturn.offset(state.get(FACING).rotateYClockwise());
            }
            return toReturn;
        }

        @Override
        public void onBreak(World level, BlockPos pos, BlockState state, PlayerEntity player) {
            BlockPos basePos = this.getBasePos(state, pos);
            BlockState baseState = level.getBlockState(basePos);
            if (baseState.isOf(BlockHandler.GONG)) {
                level.setBlockState(basePos, Blocks.AIR.getDefaultState(), 35);
                level.syncWorldEvent(player, 2001, basePos, Block.getRawIdFromState(state));
            }
        }

        public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState state1, WorldAccess level, BlockPos pos, BlockPos pos1) {
            BlockPos basePos = this.getBasePos(state, pos);
            BlockState baseState = level.getBlockState(basePos);
            if (!baseState.isOf(BlockHandler.GONG)) {
                return Blocks.AIR.getDefaultState();
            }
            return super.getStateForNeighborUpdate(state, direction, state1, level, pos, pos1);
        }

        public VoxelShape getCollisionShape(BlockState p_49760_, BlockView p_49761_, BlockPos p_49762_, ShapeContext p_49763_) {
            return this.getVoxelShape(p_49760_);
        }

        public VoxelShape getOutlineShape(BlockState p_49755_, BlockView p_49756_, BlockPos p_49757_, ShapeContext p_49758_) {
            return this.getVoxelShape(p_49755_);
        }

        public BlockRenderType getRenderType(BlockState blockState) {
            return BlockRenderType.MODEL;
        }

        private VoxelShape getVoxelShape(BlockState p_49767_) {
            Direction direction = p_49767_.get(FACING);
            return direction != Direction.NORTH && direction != Direction.SOUTH ? EAST_WEST_SHAPE : NORTH_SOUTH_SHAPE;
        }

        public boolean canPathfindThrough(BlockState p_49717_, BlockView p_49718_, BlockPos p_49719_, NavigationType p_49720_) {
            return false;
        }

        public PistonBehavior getPistonPushReaction(BlockState p_49765_) {
            return PistonBehavior.DESTROY;
        }

        @Override
        public Item asItem() {
            return BlockHandler.GONG.asItem();
        }
    }
}
