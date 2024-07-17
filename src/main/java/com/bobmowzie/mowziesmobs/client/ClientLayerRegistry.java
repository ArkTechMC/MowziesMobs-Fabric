package com.bobmowzie.mowziesmobs.client;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.render.entity.FrozenRenderHandler;
import com.bobmowzie.mowziesmobs.client.render.entity.layer.SunblockLayer;
import com.bobmowzie.mowziesmobs.client.render.entity.player.GeckoPlayer;
import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import com.google.common.collect.ImmutableList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeRegistry;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.stream.Collectors;

// From https://github.com/Alex-the-666/AlexsMobs/blob/1.19/src/main/java/com/github/alexthe666/alexsmobs/client/ClientLayerRegistry.java
@Environment(EnvType.CLIENT)
public class ClientLayerRegistry {
    @SubscribeEvent
    @Environment(EnvType.CLIENT)
    public static void onAddLayers(EntityRenderersEvent.AddLayers event) {
        List<EntityType<? extends LivingEntity>> entityTypes = ImmutableList.copyOf(
                ForgeRegistries.ENTITY_TYPES.getValues().stream()
                        .filter(DefaultAttributeRegistry::hasDefinitionFor)
                        .map(entityType -> (EntityType<? extends LivingEntity>) entityType)
                        .collect(Collectors.toList()));
        entityTypes.forEach((entityType -> {
            addLayerIfApplicable(entityType, event);
        }));
        for (String skinType : event.getSkins()) {
            event.getSkin(skinType).addLayer(new FrozenRenderHandler.LayerFrozen(event.getSkin(skinType)));
            event.getSkin(skinType).addLayer(new SunblockLayer(event.getSkin(skinType)));
        }

        GeckoPlayer.GeckoPlayerThirdPerson.initRenderer();
    }

    private static void addLayerIfApplicable(EntityType<? extends LivingEntity> entityType, EntityRenderersEvent.AddLayers event) {
        LivingEntityRenderer renderer = null;
        if (entityType != EntityType.ENDER_DRAGON) {
            try {
                renderer = event.getRenderer(entityType);
            } catch (Exception e) {
                if (!entityType.getBaseClass().isAssignableFrom(MowzieEntity.class)) {
                    MowziesMobs.LOGGER.warn("Could not apply layer to " + ForgeRegistries.ENTITY_TYPES.getKey(entityType) + ", has custom renderer that is not LivingEntityRenderer.");
                }
            }
            if (renderer != null) {
                renderer.addFeature(new FrozenRenderHandler.LayerFrozen(renderer));
                renderer.addFeature(new SunblockLayer(renderer));
            }
        }
    }
}
