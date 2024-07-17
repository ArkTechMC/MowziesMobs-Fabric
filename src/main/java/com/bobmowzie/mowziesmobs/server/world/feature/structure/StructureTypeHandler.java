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

import java.util.HashSet;
import java.util.Set;

public class StructureTypeHandler {
    public static final Set<RegistryEntry<Biome>> FERROUS_WROUGHTNAUT_BIOMES = new HashSet<>();
    public static final Set<RegistryEntry<Biome>> UMVUTHI_BIOMES = new HashSet<>();    public static StructurePieceType WROUGHTNAUT_CHAMBER_PIECE = registerStructurePieceType("wrought_chamber_template", WroughtnautChamberPieces.Piece::new);
    public static final Set<RegistryEntry<Biome>> FROSTMAW_BIOMES = new HashSet<>();
    public static final Set<RegistryEntry<Biome>> SCULPTOR_BIOMES = new HashSet<>();    public static StructurePieceType UMVUTHANA_GROVE_PIECE = registerStructurePieceType("umvuthana_grove_template", UmvuthanaGrovePieces.Piece::new);
    public static StructureType<WroughtnautChamberStructure> WROUGHTNAUT_CHAMBER = registerStructureType("wrought_chamber", () -> WroughtnautChamberStructure.CODEC);    public static StructurePieceType UMVUTHANA_FIREPIT = registerStructurePieceType("umvuthana_firepit", UmvuthanaGrovePieces.FirepitPiece::new);
    public static StructureType<UmvuthanaGroveStructure> UMVUTHANA_GROVE = registerStructureType("umvuthana_grove", () -> UmvuthanaGroveStructure.CODEC);
    public static StructureType<FrostmawStructure> FROSTMAW = registerStructureType("frostmaw_spawn", () -> FrostmawStructure.CODEC);    public static StructurePieceType FROSTMAW_PIECE = registerStructurePieceType("frostmaw_template", FrostmawPieces.FrostmawPiece::new);
    public static StructureType<MonasteryStructure> MONASTERY = registerStructureType("monastery", () -> MonasteryStructure.CODEC);

    public static BiomeChecker FERROUS_WROUGHTNAUT_BIOME_CHECKER;
    public static BiomeChecker UMVUTHI_BIOME_CHECKER;
    public static BiomeChecker FROSTMAW_BIOME_CHECKER;
    public static BiomeChecker SCULPTOR_BIOME_CHECKER;

    private static <T extends Structure> StructureType<T> registerStructureType(String name, StructureType<T> structure) {
        return Registry.register(Registries.STRUCTURE_TYPE, new Identifier(MowziesMobs.MODID, name), structure);
    }

    private static StructurePieceType registerStructurePieceType(String name, StructurePieceType structurePieceType) {
        return Registry.register(Registries.STRUCTURE_PIECE, new Identifier(MowziesMobs.MODID, name), structurePieceType);
    }

    public static void init() {
    }

    public static void addBiomeSpawns(RegistryEntry<Biome> biomeKey) {
        if (FERROUS_WROUGHTNAUT_BIOME_CHECKER == null)
            FERROUS_WROUGHTNAUT_BIOME_CHECKER = new BiomeChecker(ConfigHandler.COMMON.MOBS.FERROUS_WROUGHTNAUT.generationConfig.biomeConfig);
        if (ConfigHandler.COMMON.MOBS.FERROUS_WROUGHTNAUT.generationConfig.generationDistance >= 0 && FERROUS_WROUGHTNAUT_BIOME_CHECKER.isBiomeInConfig(biomeKey)) {
            //System.out.println("Added Ferrous Wroughtnaut biome: " + biomeName.toString());
            FERROUS_WROUGHTNAUT_BIOMES.add(biomeKey);
        }

        if (UMVUTHI_BIOME_CHECKER == null)
            UMVUTHI_BIOME_CHECKER = new BiomeChecker(ConfigHandler.COMMON.MOBS.UMVUTHI.generationConfig.biomeConfig);
        if (ConfigHandler.COMMON.MOBS.UMVUTHI.generationConfig.generationDistance >= 0 && UMVUTHI_BIOME_CHECKER.isBiomeInConfig(biomeKey)) {
            //System.out.println("Added Barako biome: " + biomeName.toString());
            UMVUTHI_BIOMES.add(biomeKey);
        }

        if (FROSTMAW_BIOME_CHECKER == null)
            FROSTMAW_BIOME_CHECKER = new BiomeChecker(ConfigHandler.COMMON.MOBS.FROSTMAW.generationConfig.biomeConfig);
        if (ConfigHandler.COMMON.MOBS.FROSTMAW.generationConfig.generationDistance >= 0 && FROSTMAW_BIOME_CHECKER.isBiomeInConfig(biomeKey)) {
//            System.out.println("Added frostmaw biome: " + biomeKey.toString());
            FROSTMAW_BIOMES.add(biomeKey);
        }

        if (SCULPTOR_BIOME_CHECKER == null)
            SCULPTOR_BIOME_CHECKER = new BiomeChecker(ConfigHandler.COMMON.MOBS.SCULPTOR.generationConfig.biomeConfig);
        if (ConfigHandler.COMMON.MOBS.SCULPTOR.generationConfig.generationDistance >= 0 && SCULPTOR_BIOME_CHECKER.isBiomeInConfig(biomeKey)) {
            //System.out.println("Added frostmaw biome: " + biomeName.toString());
            SCULPTOR_BIOMES.add(biomeKey);
        }
    }
}
