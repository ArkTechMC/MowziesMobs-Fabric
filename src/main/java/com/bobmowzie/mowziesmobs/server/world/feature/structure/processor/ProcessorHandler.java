package com.bobmowzie.mowziesmobs.server.world.feature.structure.processor;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.structure.processor.StructureProcessorType;
import net.minecraft.util.Identifier;

public class ProcessorHandler {
    public static StructureProcessorType<BaseProcessor> BASE_PROCESSOR = () -> BaseProcessor.CODEC;
    public static StructureProcessorType<BlockSwapProcessor> BLOCK_SWAP_PROCESSOR = () -> BlockSwapProcessor.CODEC;
    public static StructureProcessorType<RootsProcessor> ROOTS_PROCESSOR = () -> RootsProcessor.CODEC;
    public static StructureProcessorType<BaseDecoProcessor> BASE_DECO_PROCESSOR = () -> BaseDecoProcessor.CODEC;

    public static void registerStructureProcessors() {
        register("base_processor", BASE_PROCESSOR);
        register("block_swap_processor", BLOCK_SWAP_PROCESSOR);
        register("roots_processor", ROOTS_PROCESSOR);
        register("base_deco_processor", BASE_DECO_PROCESSOR);
    }

    private static void register(String name, StructureProcessorType<?> codec) {
        Registry.register(Registries.STRUCTURE_PROCESSOR, new Identifier(MowziesMobs.MODID, name), codec);
    }
}
