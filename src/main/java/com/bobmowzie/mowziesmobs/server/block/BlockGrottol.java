package com.bobmowzie.mowziesmobs.server.block;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Collector;
import java.util.stream.Stream;

public class BlockGrottol extends HorizontalFacingBlock {
    public static final EnumProperty<Variant> VARIANT = EnumProperty.of("variant", Variant.class);

    private static final VoxelShape BOUNDS = VoxelShapes.cuboid(
            0.0625F, 0.0F, 0.0625F,
            0.9375F, 0.9375F, 0.9375F
    );

    public BlockGrottol(Settings properties) {
        super(properties);
        this.setDefaultState(this.getStateManager().getDefaultState()
                .with(FACING, Direction.NORTH)
                .with(VARIANT, Variant.DIAMOND)
        );
    }

    private static boolean hasSupport(BlockView world, BlockPos pos) {
        return world.getBlockState(pos.down()).isSolid();
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(FACING);
        builder.add(VARIANT);
    }

    @Override
    @Deprecated
    public VoxelShape getOutlineShape(BlockState state, BlockView worldIn, BlockPos pos, ShapeContext context) {
        return BOUNDS;
    }

    @Override
    @Deprecated
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return super.canPlaceAt(state, world, pos) && hasSupport(world, pos);
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        if (hasSupport(world, pos)) {
            world.removeBlock(pos, false);
        }
    }

    /*@Override
    public int getMetaFromState(BlockState state) {
        return ((state.getValue(VARIANT).getIndex() << 2) & 0b1100) |
            (state.getValue(FACING).getHorizontalIndex() & 0b0011);
    }

    @Override
    public BlockState getStateFromMeta(int meta) {
        return getDefaultState()
            .withProperty(VARIANT, Variant.valueOf((meta & 0b1100) >> 2))
            .withProperty(FACING, Direction.byHorizontalIndex(meta & 0b0011));
    }*/

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        return this.getDefaultState().with(FACING, context.getHorizontalPlayerFacing().getOpposite());
    }

    public enum Variant implements StringIdentifiable {
        DIAMOND(0, "diamond"),
        BLACK_PINK(1, "black_pink");

        private static final Int2ObjectMap<Variant> LOOKUP = Stream.of(values())
                .collect(Collector.<Variant, Int2ObjectOpenHashMap<Variant>, Int2ObjectMap<Variant>>of(Int2ObjectOpenHashMap::new,
                        (map, variant) -> map.put(variant.getIndex(), variant),
                        (left, right) -> {
                            throw new IllegalStateException();
                        },
                        map -> {
                            map.defaultReturnValue(DIAMOND);
                            return Int2ObjectMaps.unmodifiable(map);
                        }
                ));

        private final int index;

        private final String name;

        Variant(int index, String name) {
            this.index = index;
            this.name = name;
        }

        public static Variant valueOf(int index) {
            return LOOKUP.get(index);
        }

        public final int getIndex() {
            return this.index;
        }

        @Override
        public final String asString() {
            return this.name;
        }
    }
}
