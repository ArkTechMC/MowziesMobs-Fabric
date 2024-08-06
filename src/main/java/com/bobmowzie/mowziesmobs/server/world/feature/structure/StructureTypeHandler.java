package com.bobmowzie.mowziesmobs.server.world.feature.structure;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.world.BiomeChecker;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.structure.Structure;
import net.minecraft.world.gen.structure.StructureType;

import java.util.function.Predicate;

public class StructureTypeHandler {
    public static BiomeChecker FERROUS_WROUGHTNAUT_BIOME_CHECKER;
    public static BiomeChecker UMVUTHI_BIOME_CHECKER;
    public static BiomeChecker FROSTMAW_BIOME_CHECKER;
    public static BiomeChecker SCULPTOR_BIOME_CHECKER;
    public static final Predicate<RegistryEntry<Biome>> FERROUS_WROUGHTNAUT_BIOMES = biomeKey->{
        if (FERROUS_WROUGHTNAUT_BIOME_CHECKER == null)
            FERROUS_WROUGHTNAUT_BIOME_CHECKER = new BiomeChecker(ConfigHandler.COMMON.MOBS.FERROUS_WROUGHTNAUT.generationConfig.biomeConfig);
        return ConfigHandler.COMMON.MOBS.FERROUS_WROUGHTNAUT.generationConfig.generationDistance >= 0 && FERROUS_WROUGHTNAUT_BIOME_CHECKER.isBiomeInConfig(biomeKey);
    };
    public static final Predicate<RegistryEntry<Biome>> UMVUTHI_BIOMES = biomeKey->{
        if (UMVUTHI_BIOME_CHECKER == null)
            UMVUTHI_BIOME_CHECKER = new BiomeChecker(ConfigHandler.COMMON.MOBS.UMVUTHI.generationConfig.biomeConfig);
        return  ConfigHandler.COMMON.MOBS.UMVUTHI.generationConfig.generationDistance >= 0 && UMVUTHI_BIOME_CHECKER.isBiomeInConfig(biomeKey);
    };
    public static final Predicate<RegistryEntry<Biome>> FROSTMAW_BIOMES = biomeKey->{
        if (FROSTMAW_BIOME_CHECKER == null)
            FROSTMAW_BIOME_CHECKER = new BiomeChecker(ConfigHandler.COMMON.MOBS.FROSTMAW.generationConfig.biomeConfig);
        return ConfigHandler.COMMON.MOBS.FROSTMAW.generationConfig.generationDistance >= 0 && FROSTMAW_BIOME_CHECKER.isBiomeInConfig(biomeKey);
    };
    public static final Predicate<RegistryEntry<Biome>> SCULPTOR_BIOMES = biomeKey->{
        if (SCULPTOR_BIOME_CHECKER == null)
            SCULPTOR_BIOME_CHECKER = new BiomeChecker(ConfigHandler.COMMON.MOBS.SCULPTOR.generationConfig.biomeConfig);
        return ConfigHandler.COMMON.MOBS.SCULPTOR.generationConfig.generationDistance >= 0 && SCULPTOR_BIOME_CHECKER.isBiomeInConfig(biomeKey);
    };
    public static StructurePieceType WROUGHTNAUT_CHAMBER_PIECE = registerStructurePieceType("wrought_chamber_template", WroughtnautChamberPieces.Piece::new);
    public static StructurePieceType UMVUTHANA_GROVE_PIECE = registerStructurePieceType("umvuthana_grove_template", UmvuthanaGrovePieces.Piece::new);
    public static StructurePieceType UMVUTHANA_FIREPIT = registerStructurePieceType("umvuthana_firepit", UmvuthanaGrovePieces.FirepitPiece::new);
    public static StructurePieceType FROSTMAW_PIECE = registerStructurePieceType("frostmaw_template", FrostmawPieces.FrostmawPiece::new);
    public static StructureType<WroughtnautChamberStructure> WROUGHTNAUT_CHAMBER = registerStructureType("wrought_chamber", () -> WroughtnautChamberStructure.CODEC);
    public static StructureType<UmvuthanaGroveStructure> UMVUTHANA_GROVE = registerStructureType("umvuthana_grove", () -> UmvuthanaGroveStructure.CODEC);
    public static StructureType<FrostmawStructure> FROSTMAW = registerStructureType("frostmaw_spawn", () -> FrostmawStructure.CODEC);
    public static StructureType<MonasteryStructure> MONASTERY = registerStructureType("monastery", () -> MonasteryStructure.CODEC);

    private static <T extends Structure> StructureType<T> registerStructureType(String name, StructureType<T> structure) {
        return Registry.register(Registries.STRUCTURE_TYPE, new Identifier(MowziesMobs.MODID, name), structure);
    }

    private static StructurePieceType registerStructurePieceType(String name, StructurePieceType structurePieceType) {
        return Registry.register(Registries.STRUCTURE_PIECE, new Identifier(MowziesMobs.MODID, name), structurePieceType);
    }

    public static void init() {
    }
}
