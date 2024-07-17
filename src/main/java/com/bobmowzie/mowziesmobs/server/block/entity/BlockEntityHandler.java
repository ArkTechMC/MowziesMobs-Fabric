package com.bobmowzie.mowziesmobs.server.block.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.block.BlockHandler;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public final class BlockEntityHandler {
    private static <T extends BlockEntity> BlockEntityType<T> register(String entityName, BlockEntityType.Builder<T> builder) {
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(MowziesMobs.MODID, entityName), builder.build(null));
    }    public static BlockEntityType<GongBlockEntity> GONG_BLOCK_ENTITY = register("gong_entity", BlockEntityType.Builder.create(GongBlockEntity::new, BlockHandler.GONG));

    public static void init() {
    }


}
