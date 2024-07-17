package com.bobmowzie.mowziesmobs.server.world.feature.structure.processor;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
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

public class BaseProcessor extends StructureProcessor {
    public static final BaseProcessor INSTANCE = new BaseProcessor();
    public static final Codec<BaseProcessor> CODEC = Codec.unit(() -> INSTANCE);

    protected StructureProcessorType<?> getType() {
        return ProcessorHandler.BASE_PROCESSOR;
    }

    @Override
    public StructureTemplate.StructureBlockInfo process(WorldView levelReader, BlockPos jigsawPiecePos, BlockPos jigsawPieceBottomCenterPos, StructureTemplate.StructureBlockInfo blockInfoLocal, StructureTemplate.StructureBlockInfo blockInfoGlobal, StructurePlacementData structurePlacementData) {
        if (blockInfoGlobal.state().isOf(Blocks.COBBLED_DEEPSLATE)) {
            if (levelReader instanceof ChunkRegion worldGenRegion && !worldGenRegion.getCenterPos().equals(new ChunkPos(blockInfoGlobal.pos()))) {
                return blockInfoGlobal;
            }

            BlockPos.Mutable mutable = blockInfoGlobal.pos().mutableCopy().move(Direction.DOWN);
            BlockState currBlockState = levelReader.getBlockState(mutable);
            Random random = structurePlacementData.getRandom(blockInfoGlobal.pos());

            blockInfoGlobal = new StructureTemplate.StructureBlockInfo(blockInfoGlobal.pos(), this.chooseRandomState(random), blockInfoGlobal.nbt());
            while (mutable.getY() > levelReader.getBottomY()
                    && mutable.getY() < levelReader.getTopY()
                    && !currBlockState.isSolid()) {
                levelReader.getChunk(mutable).setBlockState(mutable, this.chooseRandomState(random), false);

                // Update to next position
                mutable.move(Direction.DOWN);
                currBlockState = levelReader.getBlockState(mutable);
            }
        }
        return blockInfoGlobal;
    }

    public BlockState chooseRandomState(Random random) {
        float v = random.nextFloat();
        if (v > 0.7) return Blocks.POLISHED_DEEPSLATE.getDefaultState();
        else return Blocks.COBBLED_DEEPSLATE.getDefaultState();
    }

}
