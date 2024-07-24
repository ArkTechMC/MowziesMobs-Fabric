package com.bobmowzie.mowziesmobs.server.world.spawn;

import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import com.bobmowzie.mowziesmobs.server.world.BiomeChecker;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.WorldView;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.util.TriPredicate;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class SpawnHandler {
    public static final Map<EntityType<?>, ConfigHandler.SpawnConfig> spawnConfigs = new HashMap<>();
    public static BiomeChecker FOLIAATH_BIOME_CHECKER;
    public static BiomeChecker UMVUTHANA_RAPTOR_BIOME_CHECKER;
    public static BiomeChecker GROTTOL_BIOME_CHECKER;
    public static BiomeChecker LANTERN_BIOME_CHECKER;
    public static BiomeChecker NAGA_BIOME_CHECKER;

    static {
        spawnConfigs.put(EntityHandler.FOLIAATH, ConfigHandler.COMMON.MOBS.FOLIAATH.spawnConfig);
        spawnConfigs.put(EntityHandler.UMVUTHANA_RAPTOR, ConfigHandler.COMMON.MOBS.UMVUTHANA.spawnConfig);
        spawnConfigs.put(EntityHandler.LANTERN, ConfigHandler.COMMON.MOBS.LANTERN.spawnConfig);
        spawnConfigs.put(EntityHandler.NAGA, ConfigHandler.COMMON.MOBS.NAGA.spawnConfig);
        spawnConfigs.put(EntityHandler.GROTTOL, ConfigHandler.COMMON.MOBS.GROTTOL.spawnConfig);
    }

    public static void registerSpawnPlacementTypes() {
        SpawnRestriction.Location.create("MMSPAWN", new TriPredicate<WorldView, BlockPos, EntityType<? extends MobEntity>>() {
            @Override
            public boolean test(WorldView t, BlockPos pos, EntityType<? extends MobEntity> entityType) {
                BlockState block = t.getBlockState(pos.down());
                if (block.getBlock() == Blocks.BEDROCK || block.getBlock() == Blocks.BARRIER || !block.blocksMovement())
                    return false;
                BlockState iblockstateUp = t.getBlockState(pos);
                BlockState iblockstateUp2 = t.getBlockState(pos.up());
                return SpawnHelper.isClearForSpawn(t, pos, iblockstateUp, iblockstateUp.getFluidState(), entityType) && SpawnHelper.isClearForSpawn(t, pos.up(), iblockstateUp2, iblockstateUp2.getFluidState(), entityType);
            }
        });

        SpawnRestriction.Location mmSpawn = SpawnRestriction.Location.valueOf("MMSPAWN");
        SpawnRestriction.register(EntityHandler.FOLIAATH, mmSpawn, Heightmap.Type.MOTION_BLOCKING, MowzieEntity::spawnPredicate);
        SpawnRestriction.register(EntityHandler.LANTERN, mmSpawn, Heightmap.Type.MOTION_BLOCKING, MowzieEntity::spawnPredicate);
        SpawnRestriction.register(EntityHandler.UMVUTHANA_RAPTOR, mmSpawn, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MowzieEntity::spawnPredicate);
        SpawnRestriction.register(EntityHandler.NAGA, SpawnRestriction.Location.NO_RESTRICTIONS, Heightmap.Type.MOTION_BLOCKING, MowzieEntity::spawnPredicate);
        SpawnRestriction.register(EntityHandler.GROTTOL, mmSpawn, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MowzieEntity::spawnPredicate);
        SpawnRestriction.register(EntityHandler.UMVUTHANA_CRANE, mmSpawn, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MowzieEntity::spawnPredicate);
    }

    public static void addBiomeSpawns(RegistryEntry<Biome> biomeKey) {
        if (FOLIAATH_BIOME_CHECKER == null)
            FOLIAATH_BIOME_CHECKER = new BiomeChecker(ConfigHandler.COMMON.MOBS.FOLIAATH.spawnConfig.biomeConfig);
        if (ConfigHandler.COMMON.MOBS.FOLIAATH.spawnConfig.spawnRate > 0 && FOLIAATH_BIOME_CHECKER.isBiomeInConfig(biomeKey)) {
//              System.out.println("Added foliaath biome: " + biomeName.toString());
            registerEntityWorldSpawn(EntityHandler.FOLIAATH, ConfigHandler.COMMON.MOBS.FOLIAATH.spawnConfig, SpawnGroup.MONSTER);
        }

        if (UMVUTHANA_RAPTOR_BIOME_CHECKER == null)
            UMVUTHANA_RAPTOR_BIOME_CHECKER = new BiomeChecker(ConfigHandler.COMMON.MOBS.UMVUTHANA.spawnConfig.biomeConfig);
        if (ConfigHandler.COMMON.MOBS.UMVUTHANA.spawnConfig.spawnRate > 0 && UMVUTHANA_RAPTOR_BIOME_CHECKER.isBiomeInConfig(biomeKey)) {
//              System.out.println("Added Barakoa biome: " + biomeName.toString());
            registerEntityWorldSpawn(EntityHandler.UMVUTHANA_RAPTOR, ConfigHandler.COMMON.MOBS.UMVUTHANA.spawnConfig, SpawnGroup.MONSTER);
        }

        if (GROTTOL_BIOME_CHECKER == null)
            GROTTOL_BIOME_CHECKER = new BiomeChecker(ConfigHandler.COMMON.MOBS.GROTTOL.spawnConfig.biomeConfig);
        if (ConfigHandler.COMMON.MOBS.GROTTOL.spawnConfig.spawnRate > 0 && GROTTOL_BIOME_CHECKER.isBiomeInConfig(biomeKey)) {
//              System.out.println("Added grottol biome: " + biomeName.toString());
            registerEntityWorldSpawn(EntityHandler.GROTTOL, ConfigHandler.COMMON.MOBS.GROTTOL.spawnConfig, SpawnGroup.MONSTER);
        }

        if (LANTERN_BIOME_CHECKER == null)
            LANTERN_BIOME_CHECKER = new BiomeChecker(ConfigHandler.COMMON.MOBS.LANTERN.spawnConfig.biomeConfig);
        if (ConfigHandler.COMMON.MOBS.LANTERN.spawnConfig.spawnRate > 0 && LANTERN_BIOME_CHECKER.isBiomeInConfig(biomeKey)) {
//              System.out.println("Added lantern biome: " + biomeName.toString());
            registerEntityWorldSpawn(EntityHandler.LANTERN, ConfigHandler.COMMON.MOBS.LANTERN.spawnConfig, SpawnGroup.AMBIENT);
        }

        if (NAGA_BIOME_CHECKER == null)
            NAGA_BIOME_CHECKER = new BiomeChecker(ConfigHandler.COMMON.MOBS.NAGA.spawnConfig.biomeConfig);
        if (ConfigHandler.COMMON.MOBS.NAGA.spawnConfig.spawnRate > 0 && NAGA_BIOME_CHECKER.isBiomeInConfig(biomeKey)) {
//              System.out.println("Added naga biome: " + biomeName.toString());
            registerEntityWorldSpawn(EntityHandler.NAGA, ConfigHandler.COMMON.MOBS.NAGA.spawnConfig, SpawnGroup.MONSTER);
        }
    }

    private static void registerEntityWorldSpawn(Predicate<BiomeSelectionContext> biomeSelector, EntityType<?> entity, ConfigHandler.SpawnConfig spawnConfig, SpawnGroup classification) {
        BiomeModifications.addSpawn(biomeSelector, classification, entity, spawnConfig.spawnRate, spawnConfig.minGroupSize, spawnConfig.maxGroupSize);
    }
}