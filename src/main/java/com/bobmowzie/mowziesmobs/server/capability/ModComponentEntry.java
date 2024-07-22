package com.bobmowzie.mowziesmobs.server.capability;

import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

public class ModComponentEntry implements EntityComponentInitializer {
    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerFor(LivingEntity.class, LivingCapability.LivingProvider.COMPONENT, LivingCapability.LivingProvider::new);
        registry.registerFor(LivingEntity.class, FrozenCapability.FrozenProvider.COMPONENT, FrozenCapability.FrozenProvider::new);
        registry.registerFor(PlayerEntity.class, PlayerCapability.PlayerProvider.COMPONENT, PlayerCapability.PlayerProvider::new);
        registry.registerFor(PlayerEntity.class, AbilityCapability.AbilityProvider.COMPONENT, AbilityCapability.AbilityProvider::new);
    }
}
