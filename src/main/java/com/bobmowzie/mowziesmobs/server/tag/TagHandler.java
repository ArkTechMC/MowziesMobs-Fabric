package com.bobmowzie.mowziesmobs.server.tag;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;

public class TagHandler {
    public static final TagKey<Item> CAN_HIT_GROTTOL = TagKey.of(RegistryKeys.ITEM, new Identifier(MowziesMobs.MODID, "can_hit_grottol"));

    public static final TagKey<Block> CAN_GROTTOL_DIG = TagKey.of(RegistryKeys.BLOCK, new Identifier(MowziesMobs.MODID, "can_grottol_dig"));
    public static final TagKey<Block> GEOMANCY_USEABLE = TagKey.of(RegistryKeys.BLOCK, new Identifier(MowziesMobs.MODID, "geomancy_useable"));

    public static final TagKey<EntityType<?>> UMVUTHANA = TagKey.of(RegistryKeys.ENTITY_TYPE, new Identifier(MowziesMobs.MODID, "umvuthana"));
    public static final TagKey<EntityType<?>> UMVUTHANA_UMVUTHI_ALIGNED = TagKey.of(RegistryKeys.ENTITY_TYPE, new Identifier(MowziesMobs.MODID, "umvuthana_umvuthi_aligned"));

    public static final TagKey<Biome> HAS_MOWZIE_STRUCTURE = TagKey.of(RegistryKeys.BIOME, new Identifier(MowziesMobs.MODID, "has_structure/has_mowzie_structure"));
    public static final TagKey<Biome> IS_MAGICAL = TagKey.of(RegistryKeys.BIOME, new Identifier(MowziesMobs.MODID, "is_magical"));
}
