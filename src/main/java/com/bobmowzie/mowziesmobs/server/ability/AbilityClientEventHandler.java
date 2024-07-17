package com.bobmowzie.mowziesmobs.server.ability;

import com.bobmowzie.mowziesmobs.server.capability.AbilityCapability;
import io.github.fabricators_of_create.porting_lib.event.client.RenderTickStartCallback;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;

@Environment(EnvType.CLIENT)
public class AbilityClientEventHandler {
    public static void register() {
        RenderTickStartCallback.EVENT.register(AbilityClientEventHandler::onRenderTick);
    }

    public static void onRenderTick() {
        PlayerEntity player = MinecraftClient.getInstance().player;
        if (player != null) {
            AbilityCapability.IAbilityCapability abilityCapability = AbilityHandler.INSTANCE.getAbilityCapability(player);
            if (abilityCapability != null)
                for (Ability<?> ability : abilityCapability.getAbilities())
                    ability.onRenderTick();
        }
    }
}