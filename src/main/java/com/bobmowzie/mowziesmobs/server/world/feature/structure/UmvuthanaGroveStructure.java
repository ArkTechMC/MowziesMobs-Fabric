package com.bobmowzie.mowziesmobs.server.world.feature.structure;

import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.mojang.serialization.Codec;
import net.minecraft.structure.StructurePiece;
import net.minecraft.structure.StructurePiecesCollector;
import net.minecraft.structure.StructureTemplateManager;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.ChunkRandom;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.noise.NoiseConfig;
import net.minecraft.world.gen.structure.StructureType;

// Edited from Telepathic Grunt's base code

public class UmvuthanaGroveStructure extends MowzieStructure {
    public static final Codec<UmvuthanaGroveStructure> CODEC = createCodec(UmvuthanaGroveStructure::new);

    public UmvuthanaGroveStructure(Config settings) {
        super(settings, ConfigHandler.COMMON.MOBS.UMVUTHI.generationConfig, StructureTypeHandler.UMVUTHI_BIOMES);
    }

    private static boolean startPlatform(ChunkGenerator generator, StructureTemplateManager templateManagerIn, StructurePiecesCollector builder, BlockPos housePos, ChunkRandom random) {
        BlockRotation rotation = BlockRotation.values()[random.nextInt(BlockRotation.values().length)];
        StructurePiece newPlatform = UmvuthanaGrovePieces.addPlatform(templateManagerIn, housePos, rotation, builder, random);
        return newPlatform != null;
    }

    private static BlockPos posToSurface(ChunkGenerator generator, BlockPos pos, HeightLimitView heightAccessor, NoiseConfig state) {
        int surfaceY = generator.getHeight(pos.getX(), pos.getZ(), Heightmap.Type.WORLD_SURFACE_WG, heightAccessor, state);
        return new BlockPos(pos.getX(), surfaceY - 1, pos.getZ());
    }

    @Override
    public void generatePieces(StructurePiecesCollector builder, Context context) {
        BlockRotation rotation = BlockRotation.values()[context.random().nextInt(BlockRotation.values().length)];

        //Turns the chunk coordinates into actual coordinates we can use. (Gets center of that chunk)
        int x = (context.chunkPos().x << 4) + 7;
        int z = (context.chunkPos().z << 4) + 7;
        BlockPos centerPos = new BlockPos(x, 1, z);

        ChunkGenerator generator = context.chunkGenerator();
        HeightLimitView heightLimitView = context.world();
        ChunkRandom random = context.random();

        int surfaceY = generator.getHeight(centerPos.getX(), centerPos.getZ(), Heightmap.Type.WORLD_SURFACE_WG, heightLimitView, context.noiseConfig());
        int oceanFloorY = generator.getHeight(centerPos.getX(), centerPos.getZ(), Heightmap.Type.OCEAN_FLOOR_WG, heightLimitView, context.noiseConfig());
        if (oceanFloorY < surfaceY) return;

        //Firepit
        BlockPos firepitPos = posToSurface(generator, centerPos, heightLimitView, context.noiseConfig());
        builder.addPiece(new UmvuthanaGrovePieces.FirepitPiece(context.structureTemplateManager(), BlockRotation.values()[random.nextInt(BlockRotation.values().length)], firepitPos));

        //Throne
        BlockPos offset = new BlockPos(0, 0, 9);
        offset = offset.rotate(rotation);
        BlockPos thronePos = posToSurface(generator, centerPos.add(offset), heightLimitView, context.noiseConfig());
        UmvuthanaGrovePieces.addPiece(UmvuthanaGrovePieces.THRONE, context.structureTemplateManager(), thronePos, rotation, builder, context.random());

        //Platforms
        int numHouses = random.nextInt(2) + 2;
        for (int i = 1; i <= numHouses; i++) {
            for (int j = 0; j < 30; j++) {
                float distance = random.nextInt(8) + 13;
                int angle = random.nextInt(360);
                BlockPos housePos = BlockPos.ofFloored(centerPos.getX() + distance * Math.sin(Math.toRadians(angle)), 0, centerPos.getZ() + distance * Math.cos(Math.toRadians(angle)));
                housePos = posToSurface(generator, housePos, heightLimitView, context.noiseConfig());
                int houseOceanFloorY = generator.getHeight(housePos.getX(), housePos.getZ(), Heightmap.Type.OCEAN_FLOOR_WG, heightLimitView, context.noiseConfig());
                if (houseOceanFloorY < housePos.getY()) continue;
                if (startPlatform(generator, context.structureTemplateManager(), builder, housePos, context.random()))
                    break;
            }
        }

        //Trees
        int numTrees = random.nextInt(3) + 2;
        for (int i = 1; i <= numTrees; i++) {
            for (int j = 0; j < 30; j++) {
                float distance = random.nextInt(14) + 13;
                int angle = random.nextInt(360);
                BlockPos treePos = BlockPos.ofFloored(centerPos.getX() + distance * Math.sin(Math.toRadians(angle)), 0, centerPos.getZ() + distance * Math.cos(Math.toRadians(angle)));
                treePos = posToSurface(generator, treePos, heightLimitView, context.noiseConfig());
                int treeOceanFloorY = generator.getHeight(treePos.getX(), treePos.getZ(), Heightmap.Type.OCEAN_FLOOR_WG, heightLimitView, context.noiseConfig());
                if (treeOceanFloorY < treePos.getY()) continue;
                int whichTree = random.nextInt(UmvuthanaGrovePieces.TREES.length);
                StructurePiece tree = UmvuthanaGrovePieces.addPieceCheckBounds(UmvuthanaGrovePieces.TREES[whichTree], context.structureTemplateManager(), treePos, BlockRotation.values()[random.nextInt(BlockRotation.values().length)], builder, context.random());
                if (tree != null) break;
            }
        }

        //Small firepits
        int numFirepits = random.nextInt(3) + 2;
        for (int i = 1; i <= numFirepits; i++) {
            int distance;
            int angle;
            for (int j = 1; j <= 10; j++) {
                distance = random.nextInt(15) + 8;
                angle = random.nextInt(360);
                BlockPos pitPos = BlockPos.ofFloored(centerPos.getX() + distance * Math.sin(Math.toRadians(angle)), 0, centerPos.getZ() + distance * Math.cos(Math.toRadians(angle)));
                pitPos = posToSurface(generator, pitPos, heightLimitView, context.noiseConfig());
                int pitOceanFloorY = generator.getHeight(pitPos.getX(), pitPos.getZ(), Heightmap.Type.OCEAN_FLOOR_WG, heightLimitView, context.noiseConfig());
                if (pitOceanFloorY < pitPos.getY()) continue;
                Identifier whichPit = UmvuthanaGrovePieces.FIREPIT_SMALL[random.nextInt(UmvuthanaGrovePieces.FIREPIT_SMALL.length)];
                StructurePiece piece = UmvuthanaGrovePieces.addPieceCheckBounds(whichPit, context.structureTemplateManager(), pitPos, BlockRotation.values()[random.nextInt(BlockRotation.values().length)], builder, context.random());
                if (piece != null) {
                    break;
                }
            }
        }

        //Stakes
        int numStakes = random.nextInt(10) + 7;
        for (int i = 1; i <= numStakes; i++) {
            int distance;
            int angle;
            for (int j = 1; j <= 10; j++) {
                distance = random.nextInt(15) + 8;
                angle = random.nextInt(360);
                BlockPos stakePos = BlockPos.ofFloored(centerPos.getX() + distance * Math.sin(Math.toRadians(angle)), 0, centerPos.getZ() + distance * Math.cos(Math.toRadians(angle)));
                stakePos = posToSurface(generator, stakePos, heightLimitView, context.noiseConfig());
                int stakeOceanFloorY = generator.getHeight(stakePos.getX(), stakePos.getZ(), Heightmap.Type.OCEAN_FLOOR_WG, heightLimitView, context.noiseConfig());
                if (stakeOceanFloorY < stakePos.getY()) continue;
                Identifier whichSpike = UmvuthanaGrovePieces.SPIKES[random.nextInt(UmvuthanaGrovePieces.SPIKES.length)];
                StructurePiece piece = UmvuthanaGrovePieces.addPieceCheckBounds(whichSpike, context.structureTemplateManager(), stakePos, BlockRotation.values()[random.nextInt(BlockRotation.values().length)], builder, context.random());
                if (piece != null) {
                    break;
                }
            }
        }
    }

    @Override
    public StructureType<?> getType() {
        return StructureTypeHandler.UMVUTHANA_GROVE;
    }
}
