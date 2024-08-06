package com.bobmowzie.mowziesmobs.server.world.spawn;

import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import com.bobmowzie.mowziesmobs.server.world.BiomeChecker;
import com.iafenvoy.uranus.util.function.predicate.Predicate3;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.WorldView;

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

    private static final Predicate3<WorldView, BlockPos, EntityType<? extends MobEntity>> spawnPredicate = (t, pos, entityType) -> {
        BlockState block = t.getBlockState(pos.down());
        if (block.getBlock() == Blocks.BEDROCK || block.getBlock() == Blocks.BARRIER || !block.blocksMovement())
            return false;
        BlockState iblockstateUp = t.getBlockState(pos);
        BlockState iblockstateUp2 = t.getBlockState(pos.up());
        return SpawnHelper.isClearForSpawn(t, pos, iblockstateUp, iblockstateUp.getFluidState(), entityType) && SpawnHelper.isClearForSpawn(t, pos.up(), iblockstateUp2, iblockstateUp2.getFluidState(), entityType);
    };

    public static void registerSpawnPlacementTypes() {
        SpawnRestriction.register(EntityHandler.FOLIAATH, SpawnRestriction.Location.NO_RESTRICTIONS, Heightmap.Type.MOTION_BLOCKING, (type, world, spawnReason, pos, random) -> MowzieEntity.spawnPredicate(type, world, spawnReason, pos, random) && spawnPredicate.test(world, pos, type));
        SpawnRestriction.register(EntityHandler.LANTERN, SpawnRestriction.Location.NO_RESTRICTIONS, Heightmap.Type.MOTION_BLOCKING, (type, world, spawnReason, pos, random) -> MowzieEntity.spawnPredicate(type, world, spawnReason, pos, random) && spawnPredicate.test(world, pos, type));
        SpawnRestriction.register(EntityHandler.UMVUTHANA_RAPTOR, SpawnRestriction.Location.NO_RESTRICTIONS, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, (type, world, spawnReason, pos, random) -> MowzieEntity.spawnPredicate(type, world, spawnReason, pos, random) && spawnPredicate.test(world, pos, type));
        SpawnRestriction.register(EntityHandler.NAGA, SpawnRestriction.Location.NO_RESTRICTIONS, Heightmap.Type.MOTION_BLOCKING, MowzieEntity::spawnPredicate);
        SpawnRestriction.register(EntityHandler.GROTTOL, SpawnRestriction.Location.NO_RESTRICTIONS, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, (type, world, spawnReason, pos, random) -> MowzieEntity.spawnPredicate(type, world, spawnReason, pos, random) && spawnPredicate.test(world, pos, type));
        SpawnRestriction.register(EntityHandler.UMVUTHANA_CRANE, SpawnRestriction.Location.NO_RESTRICTIONS, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, (type, world, spawnReason, pos, random) -> MowzieEntity.spawnPredicate(type, world, spawnReason, pos, random) && spawnPredicate.test(world, pos, type));
    }

    public static void addBiomeSpawns() {
        if (FOLIAATH_BIOME_CHECKER == null)
            FOLIAATH_BIOME_CHECKER = new BiomeChecker(ConfigHandler.COMMON.MOBS.FOLIAATH.spawnConfig.biomeConfig);
        if (ConfigHandler.COMMON.MOBS.FOLIAATH.spawnConfig.spawnRate > 0)
            registerEntityWorldSpawn(x -> FOLIAATH_BIOME_CHECKER.isBiomeInConfig(x.getBiomeRegistryEntry()), EntityHandler.FOLIAATH, ConfigHandler.COMMON.MOBS.FOLIAATH.spawnConfig, SpawnGroup.MONSTER);

        if (UMVUTHANA_RAPTOR_BIOME_CHECKER == null)
            UMVUTHANA_RAPTOR_BIOME_CHECKER = new BiomeChecker(ConfigHandler.COMMON.MOBS.UMVUTHANA.spawnConfig.biomeConfig);
        if (ConfigHandler.COMMON.MOBS.UMVUTHANA.spawnConfig.spawnRate > 0)
            registerEntityWorldSpawn(x -> UMVUTHANA_RAPTOR_BIOME_CHECKER.isBiomeInConfig(x.getBiomeRegistryEntry()), EntityHandler.UMVUTHANA_RAPTOR, ConfigHandler.COMMON.MOBS.UMVUTHANA.spawnConfig, SpawnGroup.MONSTER);

        if (GROTTOL_BIOME_CHECKER == null)
            GROTTOL_BIOME_CHECKER = new BiomeChecker(ConfigHandler.COMMON.MOBS.GROTTOL.spawnConfig.biomeConfig);
        if (ConfigHandler.COMMON.MOBS.GROTTOL.spawnConfig.spawnRate > 0)
            registerEntityWorldSpawn(x -> GROTTOL_BIOME_CHECKER.isBiomeInConfig(x.getBiomeRegistryEntry()), EntityHandler.GROTTOL, ConfigHandler.COMMON.MOBS.GROTTOL.spawnConfig, SpawnGroup.MONSTER);

        if (LANTERN_BIOME_CHECKER == null)
            LANTERN_BIOME_CHECKER = new BiomeChecker(ConfigHandler.COMMON.MOBS.LANTERN.spawnConfig.biomeConfig);
        if (ConfigHandler.COMMON.MOBS.LANTERN.spawnConfig.spawnRate > 0)
            registerEntityWorldSpawn(x -> LANTERN_BIOME_CHECKER.isBiomeInConfig(x.getBiomeRegistryEntry()), EntityHandler.LANTERN, ConfigHandler.COMMON.MOBS.LANTERN.spawnConfig, SpawnGroup.AMBIENT);

        if (NAGA_BIOME_CHECKER == null)
            NAGA_BIOME_CHECKER = new BiomeChecker(ConfigHandler.COMMON.MOBS.NAGA.spawnConfig.biomeConfig);
        if (ConfigHandler.COMMON.MOBS.NAGA.spawnConfig.spawnRate > 0)
            registerEntityWorldSpawn(x -> NAGA_BIOME_CHECKER.isBiomeInConfig(x.getBiomeRegistryEntry()), EntityHandler.NAGA, ConfigHandler.COMMON.MOBS.NAGA.spawnConfig, SpawnGroup.MONSTER);
    }

    private static void registerEntityWorldSpawn(Predicate<BiomeSelectionContext> biomeSelector, EntityType<?> entity, ConfigHandler.SpawnConfig spawnConfig, SpawnGroup classification) {
        BiomeModifications.addSpawn(biomeSelector, classification, entity, spawnConfig.spawnRate, spawnConfig.minGroupSize, spawnConfig.maxGroupSize);
    }
}