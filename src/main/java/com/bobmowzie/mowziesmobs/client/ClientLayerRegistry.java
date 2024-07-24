package com.bobmowzie.mowziesmobs.client;

import com.bobmowzie.mowziesmobs.client.render.entity.FrozenRenderHandler;
import com.bobmowzie.mowziesmobs.client.render.entity.layer.SunblockLayer;
import com.bobmowzie.mowziesmobs.client.render.entity.player.GeckoPlayer;
import com.google.common.collect.ImmutableList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.Registries;

import java.util.List;
import java.util.Map;

// From https://github.com/Alex-the-666/AlexsMobs/blob/1.19/src/main/java/com/github/alexthe666/alexsmobs/client/ClientLayerRegistry.java
@Environment(EnvType.CLIENT)
public class ClientLayerRegistry {
    public static void onAddLayers(final Map<EntityType<?>, EntityRenderer<?>> renderers, final Map<String, EntityRenderer<? extends PlayerEntity>> skinMap) {
        List<EntityType<? extends LivingEntity>> entityTypes = ImmutableList.copyOf(Registries.ENTITY_TYPE
                .stream()
                .filter(DefaultAttributeRegistry::hasDefinitionFor)
                .map(entityType -> (EntityType<? extends LivingEntity>) entityType)
                .toList());
        entityTypes.forEach(entityType -> addLayerIfApplicable(entityType, renderers.get(entityType)));
        for (EntityRenderer<? extends PlayerEntity> renderer : skinMap.values())
            if (renderer instanceof LivingEntityRenderer<?, ?> livingEntityRenderer) {
                livingEntityRenderer.addFeature(new FrozenRenderHandler.LayerFrozen(livingEntityRenderer));
                livingEntityRenderer.addFeature(new SunblockLayer(livingEntityRenderer));
            }
        GeckoPlayer.GeckoPlayerThirdPerson.initRenderer();
    }

    private static void addLayerIfApplicable(EntityType<? extends LivingEntity> entityType, EntityRenderer<?> renderer) {
        if (entityType != EntityType.ENDER_DRAGON && renderer instanceof LivingEntityRenderer<?, ?> livingEntityRenderer) {
            livingEntityRenderer.addFeature(new FrozenRenderHandler.LayerFrozen(livingEntityRenderer));
            livingEntityRenderer.addFeature(new SunblockLayer(livingEntityRenderer));
        }
    }
}
