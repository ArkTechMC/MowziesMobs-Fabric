package com.bobmowzie.mowziesmobs.server.world.feature.structure;

import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import net.minecraft.block.BlockState;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.structure.StructurePiecesCollector;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeCoords;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.VerticalBlockSample;
import net.minecraft.world.gen.structure.Structure;

import java.util.Optional;
import java.util.function.Predicate;

public abstract class MowzieStructure extends Structure {
    private final ConfigHandler.GenerationConfig config;
    private Predicate<RegistryEntry<Biome>> allowedBiomes;
    private boolean doCheckHeight;
    private boolean doAvoidWater;
    private boolean doAvoidStructures;

    public MowzieStructure(Config settings, ConfigHandler.GenerationConfig config, Predicate<RegistryEntry<Biome>> allowedBiomes, boolean doCheckHeight, boolean doAvoidWater, boolean doAvoidStructures) {
        super(settings);
        this.config = config;
        this.allowedBiomes = allowedBiomes;
        this.doCheckHeight = doCheckHeight;
        this.doAvoidWater = doAvoidWater;
        this.doAvoidStructures = doAvoidStructures;
    }

    public MowzieStructure(Config settings, ConfigHandler.GenerationConfig config, Predicate<RegistryEntry<Biome>> allowedBiomes) {
        this(settings, config, allowedBiomes, true, true, true);
    }

    public MowzieStructure(Config settings, ConfigHandler.GenerationConfig config) {
        super(settings);
        this.config = config;
    }

    @Override
    public Optional<StructurePosition> getStructurePosition(Context context) {
        if (this.checkLocation(context)) {
            return getStructurePosition(context, Heightmap.Type.WORLD_SURFACE_WG, (builder) -> {
                this.generatePieces(builder, context);
            });
        }
        return Optional.empty();
    }

    public void generatePieces(StructurePiecesCollector builder, Context context) {

    }

    public boolean checkLocation(Context context) {
        return this.checkLocation(context, this.config, this.allowedBiomes, this.doCheckHeight, this.doAvoidWater, this.doAvoidStructures);
    }

    protected boolean checkLocation(Context context, ConfigHandler.GenerationConfig config, Predicate<RegistryEntry<Biome>> allowedBiomes, boolean checkHeight, boolean avoidWater, boolean avoidStructures) {
        if (config.generationDistance < 0) {
            return false;
        }

        ChunkPos chunkPos = context.chunkPos();
        BlockPos centerOfChunk = new BlockPos((chunkPos.x << 4) + 7, 0, (chunkPos.z << 4) + 7);

        int i = chunkPos.getCenterX();
        int j = chunkPos.getCenterZ();
        int k = context.chunkGenerator().getHeightInGround(i, j, Heightmap.Type.WORLD_SURFACE_WG, context.world(), context.noiseConfig());
        RegistryEntry<Biome> biome = context.chunkGenerator().getBiomeSource().getBiome(BiomeCoords.fromBlock(i), BiomeCoords.fromBlock(k), BiomeCoords.fromBlock(j), context.noiseConfig().getMultiNoiseSampler());
        if (!allowedBiomes.test(biome)) {
            return false;
        }

        if (checkHeight) {
            double minHeight = config.heightMin;
            double maxHeight = config.heightMax;
            int landHeight = getMinCornerHeight(context, 16, 16);
            if (minHeight != -65 && landHeight < minHeight) return false;
            if (maxHeight != -65 && landHeight > maxHeight) return false;
        }

        if (avoidWater) {
            ChunkGenerator chunkGenerator = context.chunkGenerator();
            HeightLimitView heightLimitView = context.world();
            int centerHeight = chunkGenerator.getHeight(centerOfChunk.getX(), centerOfChunk.getZ(), Heightmap.Type.WORLD_SURFACE_WG, heightLimitView, context.noiseConfig());
            VerticalBlockSample columnOfBlocks = chunkGenerator.getColumnSample(centerOfChunk.getX(), centerOfChunk.getZ(), heightLimitView, context.noiseConfig());
            BlockState topBlock = columnOfBlocks.getState(centerHeight);
            return topBlock.getFluidState().isEmpty();
        }

        return true;
    }

    @Override
    public GenerationStep.Feature getFeatureGenerationStep() {
        return GenerationStep.Feature.SURFACE_STRUCTURES;
    }

    public ConfigHandler.GenerationConfig getConfig() {
        return this.config;
    }
}
