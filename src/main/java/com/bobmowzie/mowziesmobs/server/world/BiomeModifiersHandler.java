package com.bobmowzie.mowziesmobs.server.world;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.world.feature.structure.StructureTypeHandler;
import com.bobmowzie.mowziesmobs.server.world.spawn.SpawnHandler;
import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.biome.v1.BiomeModification;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.minecraft.registry.Registries;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BiomeModifiersHandler {
    public static void init(){
        SpawnHandler.addBiomeSpawns(biome);
        StructureTypeHandler.addBiomeSpawns(biome);
    }
}
