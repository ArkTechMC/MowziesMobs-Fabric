package com.bobmowzie.mowziesmobs.server.world.feature.structure;

import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.structure.StructurePiecesCollector;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.VerticalBlockSample;
import net.minecraft.world.gen.noise.NoiseConfig;
import net.minecraft.world.gen.structure.StructureType;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Nullable;

public class WroughtnautChamberStructure extends MowzieStructure {
    public static final Codec<WroughtnautChamberStructure> CODEC = createCodec(WroughtnautChamberStructure::new);

    public WroughtnautChamberStructure(Config settings) {
        super(settings, ConfigHandler.COMMON.MOBS.FERROUS_WROUGHTNAUT.generationConfig, StructureTypeHandler.FERROUS_WROUGHTNAUT_BIOMES, false, false, true);
    }

    @Nullable
    public static Pair<BlockPos, BlockRotation> tryWroughtChamber(ChunkGenerator generator, HeightLimitView heightAccessor, int x, int surfaceY, int z, NoiseConfig state) {
        int xzCheckDistance = 8; // Always starts at chunk center, so it can safely check 8 blocks in any direction

        int heightMax = ConfigHandler.COMMON.MOBS.FERROUS_WROUGHTNAUT.generationConfig.heightMax;
        int heightMin = ConfigHandler.COMMON.MOBS.FERROUS_WROUGHTNAUT.generationConfig.heightMin;
        if (heightMax == -65 || heightMax > surfaceY) heightMax = surfaceY;
        if (heightMin == -65) heightMin = -64;

        for (int dx = -xzCheckDistance; dx < xzCheckDistance; dx += 2) {
            for (int dz = -xzCheckDistance; dz < xzCheckDistance; dz += 2) {
                // Check for air to find a cave
                BlockPos airPos = null;
                VerticalBlockSample column = generator.getColumnSample(x + dx, z + dz, heightAccessor, state);
                for (int y = heightMax; y > heightMin; y--) {
                    if (!column.getState(y).isSolid()) {
                        airPos = new BlockPos(x + dx, y, z + dz);
                        break;
                    }
                }

                // If air was found, check down to find the cave floor
                if (airPos != null) {
                    BlockPos groundPos = null;
                    for (int y = airPos.getY(); y > heightMin; y--) {
                        if (column.getState(y).isSolid()) {
                            // Found floor of cave
                            groundPos = airPos.withY(y);
                            break;
                        }
                    }

                    // If ground was found, search for wall in all 4 horizontal directions
                    if (groundPos != null) {
                        for (Direction dir : Direction.Type.HORIZONTAL) {
                            BlockPos.Mutable checkWallPos = groundPos.up().mutableCopy();
                            for (int d = 1; d <= xzCheckDistance; d++) {
                                checkWallPos.move(dir);
                                VerticalBlockSample wallCheckColumn = generator.getColumnSample(checkWallPos.getX(), checkWallPos.getZ(), heightAccessor, state);
                                int wallBaseY = checkWallPos.getY() - 1;

                                // Check upwards to see if four blocks up are solid. If not, checkWallPos moves up to the new floor
                                for (int wallHeightCount = 1; true; wallHeightCount++) {
                                    BlockState wallBlock = wallCheckColumn.getState(checkWallPos.getY());
                                    if (!wallBlock.isSolid()) {
                                        // If not solid, no wall. Move checkWallPos in dir direction and check again
                                        break;
                                    }
                                    if (wallHeightCount == 4) {
                                        // Found a wall! Return its position and rotation
                                        BlockRotation rotation = switch (dir) {
                                            case NORTH -> BlockRotation.COUNTERCLOCKWISE_90;
                                            case EAST -> BlockRotation.NONE;
                                            case WEST -> BlockRotation.CLOCKWISE_180;
                                            default -> BlockRotation.CLOCKWISE_90;
                                        };
                                        return Pair.of(new BlockPos(checkWallPos.getX(), wallBaseY, checkWallPos.getZ()), rotation);
                                    }
                                    checkWallPos.move(Direction.UP);
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    @Override
    public void generatePieces(StructurePiecesCollector builder, Context context) {
        int x = context.chunkPos().getCenterX();
        int z = context.chunkPos().getCenterZ();
        int y = context.chunkGenerator().getHeightInGround(x, z, Heightmap.Type.OCEAN_FLOOR_WG, context.world(), context.noiseConfig());
        Pair<BlockPos, BlockRotation> tryResult = tryWroughtChamber(context.chunkGenerator(), context.world(), x, y, z, context.noiseConfig());
        if (tryResult == null) return;
        BlockPos pos = tryResult.getLeft();
        BlockRotation rotation = tryResult.getRight();
        BlockPos rotationOffset = new BlockPos(0, 0, -9).rotate(rotation);
        pos = pos.add(rotationOffset);
        WroughtnautChamberPieces.start(context.structureTemplateManager(), pos, rotation, builder);
    }

    @Override
    public GenerationStep.Feature getFeatureGenerationStep() {
        return GenerationStep.Feature.UNDERGROUND_STRUCTURES;
    }

    @Override
    public StructureType<?> getType() {
        return StructureTypeHandler.WROUGHTNAUT_CHAMBER;
    }
}
