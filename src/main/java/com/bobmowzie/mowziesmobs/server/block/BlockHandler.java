package com.bobmowzie.mowziesmobs.server.block;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public final class BlockHandler {
    public static final Block PAINTED_ACACIA = registerBlockAndItem("painted_acacia", new Block(AbstractBlock.Settings.copy(Blocks.ACACIA_PLANKS).strength(2.0F, 3.0F).sounds(BlockSoundGroup.WOOD)));
    public static final Block PAINTED_ACACIA_SLAB = registerBlockAndItem("painted_acacia_slab", new SlabBlock(AbstractBlock.Settings.copy(PAINTED_ACACIA)));
    public static final Block THATCH = registerBlockAndItem("thatch_block", new HayBlock(AbstractBlock.Settings.copy(Blocks.HAY_BLOCK)));
    public static final Block GONG = register("gong", new GongBlock(AbstractBlock.Settings.copy(Blocks.GOLD_BLOCK).requiresTool().strength(3.0F).sounds(BlockSoundGroup.ANVIL)));
    public static final Block GONG_PART = register("gong_part", new GongBlock.GongPartBlock(AbstractBlock.Settings.copy(Blocks.GOLD_BLOCK).requiresTool().strength(3.0F).sounds(BlockSoundGroup.ANVIL)));
    public static final Block RAKED_SAND = register("raked_sand", new RakedSandBlock(14406560, AbstractBlock.Settings.copy(Blocks.SAND), Blocks.SAND.getDefaultState()));
    public static final Block RED_RAKED_SAND = register("red_raked_sand", new RakedSandBlock(11098145, AbstractBlock.Settings.copy(Blocks.RED_SAND), Blocks.RED_SAND.getDefaultState()));
    public static final Block CLAWED_LOG = registerBlockAndItem("clawed_log", new Block(AbstractBlock.Settings.copy(Blocks.ACACIA_PLANKS)));
    //public static final RegistryObject<BlockGrottol> GROTTOL = REG.register("grottol", () -> new BlockGrottol(AbstractBlock.Settings.copy(Material.STONE).noDrops()));

    public static Block registerBlockAndItem(String name, Block block) {
        Block blockObj = register(name, block);
        Registry.register(Registries.ITEM, name, new BlockItem(blockObj, new Item.Settings()));
        return blockObj;
    }

    public static Block register(String name, Block block) {
        return Registry.register(Registries.BLOCK, new Identifier(MowziesMobs.MODID, name), block);
    }

    public static void init() {
        FireBlock fireblock = (FireBlock) Blocks.FIRE;
        fireblock.registerFlammableBlock(THATCH, 60, 20);
        fireblock.registerFlammableBlock(PAINTED_ACACIA, 5, 20);
        fireblock.registerFlammableBlock(PAINTED_ACACIA_SLAB, 5, 20);
        fireblock.registerFlammableBlock(CLAWED_LOG, 5, 5);

        FuelRegistry.INSTANCE.add(BlockHandler.CLAWED_LOG,300);
        FuelRegistry.INSTANCE.add(BlockHandler.PAINTED_ACACIA,300);
        FuelRegistry.INSTANCE.add(BlockHandler.PAINTED_ACACIA_SLAB,150);
        FuelRegistry.INSTANCE.add(BlockHandler.THATCH,100);
    }
}