package com.bobmowzie.mowziesmobs.client;

import com.bobmowzie.mowziesmobs.client.model.LayerHandler;
import com.bobmowzie.mowziesmobs.client.particle.ParticleHandler;
import com.bobmowzie.mowziesmobs.server.ability.AbilityClientEventHandler;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import com.bobmowzie.mowziesmobs.server.message.ClientNetworkHelper;
import io.github.fabricators_of_create.porting_lib.entity.events.PlayerTickEvents;
import io.github.fabricators_of_create.porting_lib.event.client.*;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.util.Identifier;

public class MowziesMobsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ParticleHandler.registerParticles();
        LayerHandler.registerLayer();
        EntityRendererHandler.registerEntityRenderer();
        ClientNetworkHelper.register();
        AbilityClientEventHandler.register();

        RenderHandCallback.EVENT.register(ClientEventHandler::onHandRender);
        LivingEntityRenderEvents.PRE.register(ClientEventHandler::renderLivingEvent);
        PlayerTickEvents.END.register(ClientEventHandler::onPlayerTick);
        RenderTickStartCallback.EVENT.register(ClientEventHandler::onRenderTick);
        OverlayRenderCallback.EVENT.register(ClientEventHandler::onRenderOverlay);
        WorldRenderEvents.BEFORE_BLOCK_OUTLINE.register(ClientEventHandler::onRenderLevelStage);
        ClientTickEvents.END_WORLD_TICK.register(ClientEventHandler::onLevelTick);
        CameraSetupCallback.EVENT.register(ClientEventHandler::onSetupCamera);
        FieldOfViewEvents.COMPUTE.register(ClientEventHandler::updateFOV);
        EntityAddedLayerCallback.EVENT.register(ClientLayerRegistry::onAddLayers);

        ModelPredicateProviderRegistry.register(ItemHandler.BLOWGUN.asItem(), new Identifier("pulling"), (itemStack, world, livingEntity, seed) -> livingEntity != null && livingEntity.isUsingItem() && livingEntity.getActiveItem() == itemStack ? 1 : 0);
    }
}
