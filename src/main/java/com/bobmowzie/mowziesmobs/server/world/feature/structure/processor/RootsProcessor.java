package com.bobmowzie.mowziesmobs.server.world.feature.structure.processor;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.TrapdoorBlock;
import net.minecraft.block.enums.BlockHalf;
import net.minecraft.block.enums.SlabType;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.structure.processor.StructureProcessor;
import net.minecraft.structure.processor.StructureProcessorType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.WorldView;

public class RootsProcessor extends StructureProcessor {
    public static final RootsProcessor INSTANCE = new RootsProcessor();
    public static final Codec<RootsProcessor> CODEC = Codec.unit(() -> INSTANCE);

    protected StructureProcessorType<?> getType() {
        return ProcessorHandler.BASE_PROCESSOR;
    }

    @Override
    public StructureTemplate.StructureBlockInfo process(WorldView levelReader, BlockPos jigsawPiecePos, BlockPos jigsawPieceBottomCenterPos, StructureTemplate.StructureBlockInfo blockInfoLocal, StructureTemplate.StructureBlockInfo blockInfoGlobal, StructurePlacementData structurePlacementData) {
        if (levelReader instanceof ChunkRegion worldGenRegion && !worldGenRegion.getCenterPos().equals(new ChunkPos(blockInfoGlobal.pos()))) {
            return blockInfoGlobal;
        }
        Random random = structurePlacementData.getRandom(blockInfoGlobal.pos());
        if (random.nextFloat() < 0.15) {
            if (
                    blockInfoGlobal.state().isOf(Blocks.DARK_OAK_PLANKS) ||
                            blockInfoGlobal.state().isOf(Blocks.DARK_OAK_SLAB) && blockInfoGlobal.state().get(SlabBlock.TYPE) != SlabType.TOP
            ) {

                BlockPos pos = blockInfoGlobal.pos().down();
                BlockState belowState = levelReader.getBlockState(pos);
                if (belowState.isAir()) {
                    levelReader.getChunk(pos).setBlockState(pos, Blocks.HANGING_ROOTS.getDefaultState(), false);
                }
            } else if (
                    blockInfoGlobal.state().isOf(Blocks.DARK_OAK_TRAPDOOR) &&
                            blockInfoGlobal.state().get(TrapdoorBlock.HALF) == BlockHalf.TOP &&
                            !blockInfoGlobal.state().get(TrapdoorBlock.OPEN)
            ) {
                blockInfoGlobal = new StructureTemplate.StructureBlockInfo(blockInfoGlobal.pos(), Blocks.HANGING_ROOTS.getDefaultState(), blockInfoGlobal.nbt());
            }
        }
        return blockInfoGlobal;
    }

}
