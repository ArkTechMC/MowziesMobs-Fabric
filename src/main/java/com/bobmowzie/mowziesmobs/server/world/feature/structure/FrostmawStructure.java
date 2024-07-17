package com.bobmowzie.mowziesmobs.server.world.feature.structure;

import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.tag.TagHandler;
import com.mojang.serialization.Codec;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.structure.StructurePiecesCollector;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.StructureTerrainAdaptation;
import net.minecraft.world.gen.structure.Structure;
import net.minecraft.world.gen.structure.StructureType;

import java.util.HashMap;

public class FrostmawStructure extends MowzieStructure {
    public static final Codec<FrostmawStructure> CODEC = createCodec(FrostmawStructure::new);

    public FrostmawStructure(Config settings) {
        super(settings, ConfigHandler.COMMON.MOBS.FROSTMAW.generationConfig, StructureTypeHandler.FROSTMAW_BIOMES);
    }

    public static FrostmawStructure buildStructureConfig(Registerable<Structure> context) {
        return new FrostmawStructure(
                new Config(
                        context.getRegistryLookup(RegistryKeys.BIOME).getOrThrow(TagHandler.HAS_MOWZIE_STRUCTURE),
                        new HashMap<>(),
                        GenerationStep.Feature.SURFACE_STRUCTURES,
                        StructureTerrainAdaptation.BEARD_THIN
                )
        );
    }

    @Override
    public void generatePieces(StructurePiecesCollector builder, Context context) {
        int x = context.chunkPos().getStartX();
        int z = context.chunkPos().getStartZ();
        int y = context.chunkGenerator().getHeightOnGround(x, z, Heightmap.Type.WORLD_SURFACE_WG, context.world(), context.noiseConfig());
        BlockPos blockpos = new BlockPos(x, y, z);
        BlockRotation rotation = BlockRotation.random(context.random());
        FrostmawPieces.addPieces(context.structureTemplateManager(), blockpos, rotation, builder, context.random());
    }

    @Override
    public StructureType<?> getType() {
        return StructureTypeHandler.FROSTMAW;
    }
}
