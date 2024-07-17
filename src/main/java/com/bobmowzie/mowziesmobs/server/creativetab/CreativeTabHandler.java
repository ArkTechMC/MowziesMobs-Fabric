package com.bobmowzie.mowziesmobs.server.creativetab;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class CreativeTabHandler {
    public static ItemGroup CREATIVE_TAB = register("mowziesmobs_tab", FabricItemGroup.builder()
            .icon(ItemHandler.LOGO::getDefaultStack)
            .displayName(Text.translatable("itemGroup.mowziesmobs.creativeTab"))
            .entries((displayParams, output) -> {
                output.addAll(ItemHandler.ITEMS.stream().filter(x -> x != ItemHandler.LOGO).map(ItemStack::new).toList());
            })
            .build());

    public static ItemGroup register(String name, ItemGroup group) {
        return Registry.register(Registries.ITEM_GROUP, new Identifier(MowziesMobs.MODID, name), group);
    }

    public static void init() {
    }
}
