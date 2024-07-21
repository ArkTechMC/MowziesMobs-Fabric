package com.bobmowzie.mowziesmobs.client;

import com.bobmowzie.mowziesmobs.client.model.LayerHandler;
import com.bobmowzie.mowziesmobs.client.particle.ParticleHandler;
import com.bobmowzie.mowziesmobs.server.message.ClientNetworkHelper;
import net.fabricmc.api.ClientModInitializer;

public class MowziesMobsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ParticleHandler.registerParticles();
        LayerHandler.registerLayer();
        EntityRendererHandler.registerEntityRenderer();
        ClientNetworkHelper.register();
    }
}
