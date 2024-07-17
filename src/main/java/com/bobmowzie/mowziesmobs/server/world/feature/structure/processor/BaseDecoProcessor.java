package com.bobmowzie.mowziesmobs.server.world.feature.structure.processor;

import com.mojang.serialization.Codec;
import net.minecraft.block.*;
import net.minecraft.block.enums.BlockHalf;
import net.minecraft.block.enums.SlabType;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.structure.processor.StructureProcessor;
import net.minecraft.structure.processor.StructureProcessorType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

public class BaseDecoProcessor extends StructureProcessor {
    public static final BaseDecoProcessor INSTANCE = new BaseDecoProcessor();
    public static final Codec<BaseDecoProcessor> CODEC = Codec.unit(() -> INSTANCE);

    private static final BlockState trapDoorBottom = Blocks.DARK_OAK_TRAPDOOR.getDefaultState();
    private static final BlockState trapDoorTop = Blocks.DARK_OAK_TRAPDOOR.getDefaultState().with(TrapdoorBlock.HALF, BlockHalf.TOP);
    private static final BlockState slabBottom = Blocks.DARK_OAK_SLAB.getDefaultState();
    private static final BlockState slabTop = Blocks.DARK_OAK_SLAB.getDefaultState().with(SlabBlock.TYPE, SlabType.TOP);
    private static final BlockState woodStairs = Blocks.DARK_OAK_STAIRS.getDefaultState().with(StairsBlock.HALF, BlockHalf.TOP);
    private static final BlockState wall = Blocks.DEEPSLATE_BRICK_WALL.getDefaultState();
    private static final BlockState button = Blocks.DARK_OAK_BUTTON.getDefaultState();
    private static final BlockState stoneStairs = Blocks.COBBLED_DEEPSLATE_STAIRS.getDefaultState();

    private static final BlockState[][] DECO = {
            {trapDoorBottom, slabBottom, trapDoorBottom, slabBottom, trapDoorBottom, slabBottom, trapDoorBottom},
            {woodStairs, trapDoorTop, slabTop, trapDoorTop, slabTop, trapDoorTop, woodStairs},
            {wall, button, null, null, null, button, wall},
            {wall, stoneStairs, stoneStairs, stoneStairs, stoneStairs, stoneStairs, wall}
    };

    protected StructureProcessorType<?> getType() {
        return ProcessorHandler.BASE_DECO_PROCESSOR;
    }

    @Nullable
    @Override
    public StructureTemplate.StructureBlockInfo process(WorldView levelReader, BlockPos jigsawPiecePos, BlockPos jigsawPieceBottomCenterPos, StructureTemplate.StructureBlockInfo blockInfoLocal, StructureTemplate.StructureBlockInfo blockInfoGlobal, StructurePlacementData structurePlacementData) {
        if (blockInfoGlobal.state().isOf(Blocks.PURPUR_STAIRS)) {
            if (levelReader instanceof ChunkRegion worldGenRegion && !worldGenRegion.getCenterPos().equals(new ChunkPos(blockInfoGlobal.pos()))) {
                return blockInfoGlobal;
            }

            Direction facing = blockInfoGlobal.state().get(StairsBlock.FACING).getOpposite();
            facing = structurePlacementData.getRotation().rotate(facing);
            Random random = structurePlacementData.getRandom(blockInfoGlobal.pos());

            blockInfoGlobal = new StructureTemplate.StructureBlockInfo(blockInfoGlobal.pos(), Blocks.RED_TERRACOTTA.getDefaultState(), blockInfoGlobal.nbt());
            for (int x = 0; x < 7; x++) {
                for (int y = 0; y < 4; y++) {
                    BlockState state = DECO[y][x];
                    if (state == null) continue;
                    if (state.getBlock() == Blocks.COBBLED_DEEPSLATE_STAIRS) {
                        state = this.chooseRandomState(random);
                    }

                    BlockPos pos = blockInfoGlobal.pos().offset(facing);
                    pos = pos.offset(facing.rotateYClockwise(), x - 3);
                    pos = pos.offset(Direction.UP, 1 - y);

                    if (levelReader.getBlockState(pos).isSolid()) continue;
                    if (levelReader.getBlockState(pos.down()).getBlock() == Blocks.DARK_OAK_PLANKS) continue;
                    if (levelReader.getBlockState(pos.down()).getBlock() == Blocks.STRIPPED_BIRCH_LOG) continue;
                    if (levelReader.getBlockState(pos.down()).getBlock() == Blocks.BIRCH_PLANKS) continue;

                    if (state.contains(HorizontalFacingBlock.FACING)) {
                        if (state.getBlock() instanceof StairsBlock) {
                            state = state.with(HorizontalFacingBlock.FACING, facing.getOpposite());
                        } else {
                            state = state.with(HorizontalFacingBlock.FACING, facing);
                        }
                    }
                    levelReader.getChunk(pos).setBlockState(pos, state, false);
                }
            }
        }
        return blockInfoGlobal;
    }

    public BlockState chooseRandomState(Random random) {
        float v = random.nextFloat();
        if (v > 0.7) return Blocks.POLISHED_DEEPSLATE_STAIRS.getDefaultState();
        else return Blocks.COBBLED_DEEPSLATE_STAIRS.getDefaultState();
    }

}
