package com.bobmowzie.mowziesmobs.client;

import com.bobmowzie.mowziesmobs.client.model.LayerHandler;
import com.bobmowzie.mowziesmobs.client.particle.ParticleHandler;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import com.bobmowzie.mowziesmobs.server.message.ClientNetworkHelper;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.util.Identifier;

public class MowziesMobsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ParticleHandler.registerParticles();
        LayerHandler.registerLayer();
        EntityRendererHandler.registerEntityRenderer();
        ClientNetworkHelper.register();

        ModelPredicateProviderRegistry.register(ItemHandler.BLOWGUN.asItem(), new Identifier("pulling"), (itemStack, world, livingEntity, seed) -> livingEntity != null && livingEntity.isUsingItem() && livingEntity.getActiveItem() == itemStack ? 1 : 0);
    }
}
