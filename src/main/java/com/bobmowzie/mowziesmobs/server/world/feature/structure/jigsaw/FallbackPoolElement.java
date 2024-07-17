package com.bobmowzie.mowziesmobs.server.world.feature.structure.jigsaw;

import com.mojang.serialization.Codec;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.structure.StructureTemplateManager;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePoolElement;
import net.minecraft.structure.pool.StructurePoolElementType;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;

import java.util.Collections;
import java.util.List;

public class FallbackPoolElement extends StructurePoolElement {
    public static final FallbackPoolElement INSTANCE = new FallbackPoolElement();
    public static final Codec<FallbackPoolElement> CODEC = Codec.unit(() -> {
        return FallbackPoolElement.INSTANCE;
    });

    private FallbackPoolElement() {
        super(StructurePool.Projection.TERRAIN_MATCHING);
    }

    public Vec3i getStart(StructureTemplateManager p_210191_, BlockRotation p_210192_) {
        return Vec3i.ZERO;
    }

    public List<StructureTemplate.StructureBlockInfo> getStructureBlockInfos(StructureTemplateManager p_210198_, BlockPos p_210199_, BlockRotation p_210200_, Random p_210201_) {
        return Collections.emptyList();
    }

    public BlockBox getBoundingBox(StructureTemplateManager p_210194_, BlockPos p_210195_, BlockRotation p_210196_) {
        throw new IllegalStateException("Invalid call to MowzieFallbackElement.getBoundingBox, filter me!");
    }

    public boolean generate(StructureTemplateManager p_210180_, StructureWorldAccess p_210181_, StructureAccessor p_210182_, ChunkGenerator p_210183_, BlockPos p_210184_, BlockPos p_210185_, BlockRotation p_210186_, BlockBox p_210187_, Random p_210188_, boolean p_210189_) {
        return true;
    }

    public StructurePoolElementType<?> getType() {
        return StructurePoolElementType.EMPTY_POOL_ELEMENT;
    }

    public String toString() {
        return "Fallback";
    }
}
