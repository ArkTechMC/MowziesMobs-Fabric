package com.bobmowzie.mowziesmobs.server.world.feature.structure.jigsaw;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.mojang.serialization.Codec;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.structure.pool.StructurePoolElement;
import net.minecraft.structure.pool.StructurePoolElementType;
import net.minecraft.util.Identifier;

public class JigsawHandler {
    public static StructurePoolElementType<MowziePoolElement> MOWZIE_ELEMENT;
    public static StructurePoolElementType<FallbackPoolElement> FALLBACK_ELEMENT;

    public static void registerJigsawElements() {
        MOWZIE_ELEMENT = register("mowzie_element", MowziePoolElement.CODEC);
        FALLBACK_ELEMENT = register("fallback_element", FallbackPoolElement.CODEC);
    }

    private static <P extends StructurePoolElement> StructurePoolElementType<P> register(String name, Codec<P> codec) {
        return Registry.register(Registries.STRUCTURE_POOL_ELEMENT, new Identifier(MowziesMobs.MODID, name), () -> codec);
    }
}
