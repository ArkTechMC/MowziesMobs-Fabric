package com.bobmowzie.mowziesmobs.server.inventory;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class ContainerHandler {
    public static <T extends ScreenHandler> ScreenHandlerType<T> register(String name, ScreenHandlerType<T> type) {
        return Registry.register(Registries.SCREEN_HANDLER, new Identifier(MowziesMobs.MODID, name), type);
    }

    public static final ScreenHandlerType<ContainerUmvuthanaTrade> CONTAINER_UMVUTHANA_TRADE = register("umvuthana_trade", new ScreenHandlerType<>(ContainerUmvuthanaTrade::new, FeatureFlags.VANILLA_FEATURES));

    public static void init() {
    }

    public static final ScreenHandlerType<ContainerUmvuthiTrade> CONTAINER_UMVUTHI_TRADE = register("umvuthi_trade", new ScreenHandlerType<>(ContainerUmvuthiTrade::new, FeatureFlags.VANILLA_FEATURES));
    public static final ScreenHandlerType<ContainerSculptorTrade> CONTAINER_SCULPTOR_TRADE = register("sculptor_trade", new ScreenHandlerType<>(ContainerSculptorTrade::new, FeatureFlags.VANILLA_FEATURES));
}
