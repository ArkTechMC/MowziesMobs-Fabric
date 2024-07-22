package com.bobmowzie.mowziesmobs.server.message;

import com.bobmowzie.mowziesmobs.server.ability.Ability;
import com.bobmowzie.mowziesmobs.server.ability.AbilityHandler;
import com.bobmowzie.mowziesmobs.server.ability.PlayerAbility;
import com.bobmowzie.mowziesmobs.server.capability.AbilityCapability;
import com.bobmowzie.mowziesmobs.server.capability.PlayerCapability;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntitySolarBeam;
import com.bobmowzie.mowziesmobs.server.entity.sculptor.EntitySculptor;
import com.bobmowzie.mowziesmobs.server.entity.umvuthana.EntityUmvuthi;
import com.bobmowzie.mowziesmobs.server.inventory.ContainerSculptorTrade;
import com.bobmowzie.mowziesmobs.server.inventory.ContainerUmvuthiTrade;
import com.bobmowzie.mowziesmobs.server.potion.EffectHandler;
import com.bobmowzie.mowziesmobs.server.power.Power;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.screen.ScreenHandler;

public class ServerNetworkHelper {
    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(StaticVariables.LEFT_MOUSE_DOWN, (server, player, handler, buf, responseSender) -> {
            if (player != null) {
                PlayerCapability.IPlayerCapability capability = PlayerCapability.get(player);
                if (capability != null) {
                    capability.setMouseLeftDown(true);
                    Power[] powers = capability.getPowers();
                    for (Power power : powers)
                        power.onLeftMouseDown(player);
                }
                AbilityCapability.IAbilityCapability abilityCapability = AbilityCapability.get(player);
                if (abilityCapability != null)
                    for (Ability<?> ability : abilityCapability.getAbilities())
                        if (ability instanceof PlayerAbility playerAbility)
                            playerAbility.onLeftMouseDown(player);
            }
        });
        ServerPlayNetworking.registerGlobalReceiver(StaticVariables.LEFT_MOUSE_UP, (server, player, handler, buf, responseSender) -> {
            if (player != null) {
                PlayerCapability.IPlayerCapability capability = PlayerCapability.get(player);
                if (capability != null) {
                    capability.setMouseLeftDown(false);
                    Power[] powers = capability.getPowers();
                    for (Power power : powers)
                        power.onLeftMouseUp(player);
                }
                AbilityCapability.IAbilityCapability abilityCapability = AbilityCapability.get(player);
                if (abilityCapability != null)
                    for (Ability<?> ability : abilityCapability.getAbilities())
                        if (ability instanceof PlayerAbility playerAbility)
                            playerAbility.onLeftMouseUp(player);
            }
        });
        ServerPlayNetworking.registerGlobalReceiver(StaticVariables.RIGHT_MOUSE_DOWN, (server, player, handler, buf, responseSender) -> {
            if (player != null) {
                PlayerCapability.IPlayerCapability capability = PlayerCapability.get(player);
                if (capability != null) {
                    capability.setMouseRightDown(true);
                    Power[] powers = capability.getPowers();
                    for (Power power : powers)
                        power.onRightMouseDown(player);
                }

                AbilityCapability.IAbilityCapability abilityCapability = AbilityCapability.get(player);
                if (abilityCapability != null)
                    for (Ability<?> ability : abilityCapability.getAbilities())
                        if (ability instanceof PlayerAbility playerAbility)
                            playerAbility.onRightMouseDown(player);
            }
        });
        ServerPlayNetworking.registerGlobalReceiver(StaticVariables.RIGHT_MOUSE_UP, (server, player, handler, buf, responseSender) -> {
            if (player != null) {
                PlayerCapability.IPlayerCapability capability = PlayerCapability.get(player);
                if (capability != null) {
                    capability.setMouseRightDown(false);
                    Power[] powers = capability.getPowers();
                    for (Power power : powers)
                        power.onRightMouseUp(player);
                }
                AbilityCapability.IAbilityCapability abilityCapability = AbilityCapability.get(player);
                if (abilityCapability != null)
                    for (Ability<?> ability : abilityCapability.getAbilities())
                        if (ability instanceof PlayerAbility playerAbility)
                            playerAbility.onRightMouseUp(player);
            }
        });
        ServerPlayNetworking.registerGlobalReceiver(StaticVariables.PLAYER_ATTACK_MOB, (server, player, handler, buf, responseSender) -> {
            MessagePlayerAttackMob message = MessagePlayerAttackMob.deserialize(buf);
            if (player != null) {
                Entity entity = player.getWorld().getEntityById(message.getEntityID());
                if (entity != null) player.attack(entity);
            }
        });
        ServerPlayNetworking.registerGlobalReceiver(StaticVariables.PLAYER_SOLAR_BEAM, (server, player, handler, buf, responseSender) -> {
            EntitySolarBeam solarBeam = new EntitySolarBeam(EntityHandler.SOLAR_BEAM, player.getWorld(), player, player.getX(), player.getY() + 1.2f, player.getZ(), (float) ((player.headYaw + 90) * Math.PI / 180), (float) (-player.getPitch() * Math.PI / 180), 55);
            solarBeam.setHasPlayer(true);
            player.getWorld().spawnEntity(solarBeam);
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 80, 2, false, false));
            int duration = player.getStatusEffect(EffectHandler.SUNS_BLESSING).getDuration();
            player.removeStatusEffect(EffectHandler.SUNS_BLESSING);
            int solarBeamCost = ConfigHandler.COMMON.TOOLS_AND_ABILITIES.SUNS_BLESSING.solarBeamCost * 60 * 20;
            if (duration - solarBeamCost > 0)
                player.addStatusEffect(new StatusEffectInstance(EffectHandler.SUNS_BLESSING, duration - solarBeamCost, 0, false, false));
        });
        ServerPlayNetworking.registerGlobalReceiver(StaticVariables.PLAYER_USE_ABILITY, (server, player, handler, buf, responseSender) -> {
            MessagePlayerUseAbility message = MessagePlayerUseAbility.deserialize(buf);
            AbilityCapability.IAbilityCapability abilityCapability = AbilityCapability.get(player);
            if (abilityCapability != null) {
                AbilityHandler.INSTANCE.sendAbilityMessage(player, abilityCapability.getAbilityTypesOnEntity(player)[message.getIndex()]);
            }
        });
        ServerPlayNetworking.registerGlobalReceiver(StaticVariables.SCULPTOR_TRADE, (server, player, handler, buf, responseSender) -> {
            MessageSculptorTrade message = MessageSculptorTrade.deserialize(buf);
            if (player != null) {
                Entity entity = player.getWorld().getEntityById(message.getEntityID());
                if (!(entity instanceof EntitySculptor sculptor))
                    return;
                if (sculptor.getCustomer() != player)
                    return;
                ScreenHandler container = player.currentScreenHandler;
                if (!(container instanceof ContainerSculptorTrade sculptorTrade))
                    return;
                if (sculptor.checkTestObstructed())
                    return;
                boolean satisfied = sculptor.fulfillDesire(container.getSlot(0));
                if (satisfied) {
                    sculptorTrade.returnItems();
                    container.sendContentUpdates();
                    sculptor.setTestingPlayer(player);
                }
            }
        });
        ServerPlayNetworking.registerGlobalReceiver(StaticVariables.UMVUTHI_TRADE, (server, player, handler, buf, responseSender) -> {
            MessageUmvuthiTrade message = MessageUmvuthiTrade.deserialize(buf);
            if (player != null) {
                Entity entity = player.getWorld().getEntityById(message.getEntityID());
                if (!(entity instanceof EntityUmvuthi umvuthi)) {
                    return;
                }
                if (umvuthi.getCustomer() != player) {
                    return;
                }
                ScreenHandler container = player.currentScreenHandler;
                if (!(container instanceof ContainerUmvuthiTrade)) {
                    return;
                }
                boolean satisfied = umvuthi.hasTradedWith(player);
                if (!satisfied) {
                    if (satisfied = umvuthi.fulfillDesire(container.getSlot(0))) {
                        umvuthi.rememberTrade(player);
                        ((ContainerUmvuthiTrade) container).returnItems();
                        container.sendContentUpdates();
                    }
                }
                if (satisfied) {
                    player.addStatusEffect(new StatusEffectInstance(EffectHandler.SUNS_BLESSING, -1, 0, false, false));
                    if (umvuthi.getActiveAbilityType() != EntityUmvuthi.BLESS_ABILITY) {
                        umvuthi.sendAbilityMessage(EntityUmvuthi.BLESS_ABILITY);
                        umvuthi.playSound(MMSounds.ENTITY_UMVUTHI_BLESS, 2, 1);
                    }
                }
            }
        });
    }
}
