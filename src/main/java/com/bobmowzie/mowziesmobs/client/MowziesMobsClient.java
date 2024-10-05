package com.bobmowzie.mowziesmobs.client;

import com.bobmowzie.mowziesmobs.client.model.LayerHandler;
import com.bobmowzie.mowziesmobs.client.particle.ParticleHandler;
import com.bobmowzie.mowziesmobs.client.render.item.*;
import com.bobmowzie.mowziesmobs.event.PlayerEvents;
import com.bobmowzie.mowziesmobs.server.ability.AbilityClientEventHandler;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import com.bobmowzie.mowziesmobs.server.message.ClientNetworkHelper;
import io.github.fabricators_of_create.porting_lib.event.client.*;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class MowziesMobsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ParticleHandler.registerParticles();
        LayerHandler.registerLayer();
        EntityRendererHandler.registerEntityRenderer();
        ClientNetworkHelper.register();
        AbilityClientEventHandler.register();

        ModelLoadingPlugin.register(context -> {
            List<Identifier> list = new ArrayList<>();
            EntityRendererHandler.onRegisterModels(list);
            context.addModels(list);
            context.modifyModelAfterBake().register(MMModels::onModelBakeEvent);
        });

        RenderHandCallback.EVENT.register(ClientEventHandler::onHandRender);
        LivingEntityRenderEvents.PRE.register(ClientEventHandler::renderLivingEvent);
        PlayerEvents.AFTER_TICK.register(ClientEventHandler::onPlayerTick);
        RenderTickStartCallback.EVENT.register(ClientEventHandler::onRenderTick);
        OverlayRenderCallback.EVENT.register(ClientEventHandler::onRenderOverlay);
        WorldRenderEvents.BEFORE_BLOCK_OUTLINE.register(ClientEventHandler::onRenderLevelStage);
        ClientTickEvents.END_WORLD_TICK.register(ClientEventHandler::onLevelTick);
        CameraSetupCallback.EVENT.register(ClientEventHandler::onSetupCamera);
        FieldOfViewEvents.COMPUTE.register(ClientEventHandler::updateFOV);
        EntityAddedLayerCallback.EVENT.register(ClientLayerRegistry::onAddLayers);

        BuiltinItemRendererRegistry.INSTANCE.register(ItemHandler.EARTHREND_GAUNTLET, new RenderEarthrendGauntlet());
        BuiltinItemRendererRegistry.INSTANCE.register(ItemHandler.UMVUTHANA_MASK_BLISS, new RenderUmvuthanaMaskItem());
        BuiltinItemRendererRegistry.INSTANCE.register(ItemHandler.UMVUTHANA_MASK_RAGE, new RenderUmvuthanaMaskItem());
        BuiltinItemRendererRegistry.INSTANCE.register(ItemHandler.UMVUTHANA_MASK_FAITH, new RenderUmvuthanaMaskItem());
        BuiltinItemRendererRegistry.INSTANCE.register(ItemHandler.UMVUTHANA_MASK_FEAR, new RenderUmvuthanaMaskItem());
        BuiltinItemRendererRegistry.INSTANCE.register(ItemHandler.UMVUTHANA_MASK_FURY, new RenderUmvuthanaMaskItem());
        BuiltinItemRendererRegistry.INSTANCE.register(ItemHandler.UMVUTHANA_MASK_MISERY, new RenderUmvuthanaMaskItem());
        BuiltinItemRendererRegistry.INSTANCE.register(ItemHandler.SOL_VISAGE, new RenderSolVisageItem());
        BuiltinItemRendererRegistry.INSTANCE.register(ItemHandler.SCULPTOR_STAFF, new RenderSculptorStaff());

        ModelPredicateProviderRegistry.register(ItemHandler.BLOWGUN.asItem(), new Identifier("pulling"), (itemStack, world, livingEntity, seed) -> livingEntity != null && livingEntity.isUsingItem() && livingEntity.getActiveItem() == itemStack ? 1 : 0);
    }

    public static void bootstrapItemRenderers() {
        ArmorRenderer.register(new RenderUmvuthanaMaskArmor(), ItemHandler.UMVUTHANA_MASK_BLISS, ItemHandler.UMVUTHANA_MASK_RAGE, ItemHandler.UMVUTHANA_MASK_FAITH, ItemHandler.UMVUTHANA_MASK_FEAR, ItemHandler.UMVUTHANA_MASK_FURY, ItemHandler.UMVUTHANA_MASK_MISERY);
        ArmorRenderer.register(new RenderSolVisageArmor(), ItemHandler.SOL_VISAGE);
        ArmorRenderer.register(new RenderWroughtHelmArmor(), ItemHandler.WROUGHT_HELMET);
    }
}
