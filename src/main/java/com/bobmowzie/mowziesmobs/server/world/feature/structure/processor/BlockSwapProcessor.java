package com.bobmowzie.mowziesmobs.server.world.feature.structure.processor;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.structure.processor.StructureProcessor;
import net.minecraft.structure.processor.StructureProcessorType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.WorldView;

import java.util.List;

public class BlockSwapProcessor extends StructureProcessor {
    public static final Codec<BlockSwapProcessor> CODEC = RecordCodecBuilder.create(instance -> instance
            .group(
                    BlockState.CODEC.listOf().fieldOf("to_replace").forGetter(config -> config.toReplace),
                    BlockStateRandomizer.CODEC.fieldOf("replace_with").forGetter(config -> config.replaceWith),
                    Codec.BOOL.optionalFieldOf("copy_properties", true).forGetter(config -> config.copyProperties)
            ).apply(instance, instance.stable(BlockSwapProcessor::new)));

    List<BlockState> toReplace;
    BlockStateRandomizer replaceWith;
    boolean copyProperties;

    public BlockSwapProcessor(List<BlockState> toReplace, BlockStateRandomizer replaceWith, boolean copyProperties) {
        this.toReplace = toReplace;
        this.replaceWith = replaceWith;
        this.copyProperties = copyProperties;
    }

    protected StructureProcessorType<?> getType() {
        return ProcessorHandler.BASE_PROCESSOR;
    }

    @Override
    public StructureTemplate.StructureBlockInfo process(WorldView levelReader, BlockPos jigsawPiecePos, BlockPos jigsawPieceBottomCenterPos, StructureTemplate.StructureBlockInfo blockInfoLocal, StructureTemplate.StructureBlockInfo blockInfoGlobal, StructurePlacementData structurePlacementData) {
        for (BlockState toReplaceState : this.toReplace) {
            if (blockInfoGlobal.state().isOf(toReplaceState.getBlock())) {
                if (levelReader instanceof ChunkRegion worldGenRegion && !worldGenRegion.getCenterPos().equals(new ChunkPos(blockInfoGlobal.pos()))) {
                    return blockInfoGlobal;
                }
                Random random = structurePlacementData.getRandom(blockInfoGlobal.pos());
                BlockState newState = this.replaceWith.chooseRandomState(random);
                if (this.copyProperties) {
                    newState = newState.getBlock().getStateWithProperties(blockInfoGlobal.state());
                }
                blockInfoGlobal = new StructureTemplate.StructureBlockInfo(blockInfoGlobal.pos(), newState, blockInfoGlobal.nbt());
                break;
            }
        }
        return blockInfoGlobal;
    }
}