package com.bobmowzie.mowziesmobs.server.ability;

import com.bobmowzie.mowziesmobs.server.capability.AbilityCapability;
import io.github.fabricators_of_create.porting_lib.event.client.InteractEvents;
import io.github.fabricators_of_create.porting_lib.event.client.RenderTickStartCallback;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.HitResult;

@Environment(EnvType.CLIENT)
public class AbilityClientEventHandler {
    public static void register() {
        RenderTickStartCallback.EVENT.register(AbilityClientEventHandler::onRenderTick);
        InteractEvents.USE.register(AbilityClientEventHandler::onPlayerInteract);
    }

    public static void onRenderTick() {
        PlayerEntity player = MinecraftClient.getInstance().player;
        if (player != null) {
            AbilityCapability.IAbilityCapability abilityCapability = AbilityCapability.get(player);
            if (abilityCapability != null)
                for (Ability<?> ability : abilityCapability.getAbilities())
                    ability.onRenderTick();
        }
    }

    public static ActionResult onPlayerInteract(MinecraftClient mc, HitResult hit, Hand hand) {
        AbilityCapability.IAbilityCapability abilityCapability = AbilityCapability.get(mc.player);
        if (abilityCapability != null)
            for (Ability<?> ability : abilityCapability.getAbilities())
                if (ability instanceof PlayerAbility playerAbility)
                    playerAbility.onRightClickEmpty(mc.player, hand);
        return ActionResult.PASS;
    }

}