package com.bobmowzie.mowziesmobs.server.ability;

import com.bobmowzie.mowziesmobs.server.ability.abilities.player.*;
import com.bobmowzie.mowziesmobs.server.ability.abilities.player.geomancy.*;
import com.bobmowzie.mowziesmobs.server.ability.abilities.player.heliomancy.SolarBeamAbility;
import com.bobmowzie.mowziesmobs.server.ability.abilities.player.heliomancy.SolarFlareAbility;
import com.bobmowzie.mowziesmobs.server.ability.abilities.player.heliomancy.SunstrikeAbility;
import com.bobmowzie.mowziesmobs.server.ability.abilities.player.heliomancy.SupernovaAbility;
import com.bobmowzie.mowziesmobs.server.capability.AbilityCapability;
import com.bobmowzie.mowziesmobs.server.message.*;
import com.iafenvoy.uranus.ServerHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.Nullable;

public enum AbilityHandler {
    INSTANCE;

    public static final AbilityType<PlayerEntity, FireballAbility> FIREBALL_ABILITY = new AbilityType<>("fireball", FireballAbility::new);
    public static final AbilityType<PlayerEntity, SunstrikeAbility> SUNSTRIKE_ABILITY = new AbilityType<>("sunstrike", SunstrikeAbility::new);
    public static final AbilityType<PlayerEntity, SolarBeamAbility> SOLAR_BEAM_ABILITY = new AbilityType<>("solar_beam", SolarBeamAbility::new);
    public static final AbilityType<PlayerEntity, SolarFlareAbility> SOLAR_FLARE_ABILITY = new AbilityType<>("solar_flare", SolarFlareAbility::new);
    public static final AbilityType<PlayerEntity, SupernovaAbility> SUPERNOVA_ABILITY = new AbilityType<>("supernova", SupernovaAbility::new);
    public static final AbilityType<PlayerEntity, WroughtAxeSwingAbility> WROUGHT_AXE_SWING_ABILITY = new AbilityType<>("wrought_axe_swing", WroughtAxeSwingAbility::new);
    public static final AbilityType<PlayerEntity, WroughtAxeSlamAbility> WROUGHT_AXE_SLAM_ABILITY = new AbilityType<>("wrought_axe_slam", WroughtAxeSlamAbility::new);
    public static final AbilityType<PlayerEntity, IceBreathAbility> ICE_BREATH_ABILITY = new AbilityType<>("ice_breath", IceBreathAbility::new);
    public static final AbilityType<PlayerEntity, SpawnBoulderAbility> SPAWN_BOULDER_ABILITY = new AbilityType<>("spawn_boulder", SpawnBoulderAbility::new);
    public static final AbilityType<PlayerEntity, TunnelingAbility> TUNNELING_ABILITY = new AbilityType<>("tunneling", TunnelingAbility::new);
    public static final AbilityType<PlayerEntity, SimplePlayerAnimationAbility> HIT_BOULDER_ABILITY = new AbilityType<>("hit_boulder", (type, player) ->
            new SimplePlayerAnimationAbility(type, player, "hit_boulder", 10, false, false)
    );
    public static final AbilityType<PlayerEntity, SpawnPillarAbility> SPAWN_PILLAR_ABILITY = new AbilityType<>("spawn_pillar", SpawnPillarAbility::new);
    public static final AbilityType<PlayerEntity, GroundSlamAbility> GROUND_SLAM_ABILITY = new AbilityType<>("ground_slam", GroundSlamAbility::new);
    public static final AbilityType<PlayerEntity, BoulderRollAbility> BOULDER_ROLL_ABILITY = new AbilityType<>("boulder_roll", BoulderRollAbility::new);
    public static final AbilityType<PlayerEntity, FissureAbility> FISSURE_ABILITY = new AbilityType<>("fissure", FissureAbility::new);

    public static final AbilityType<PlayerEntity, SimplePlayerAnimationAbility> BACKSTAB_ABILITY = new AbilityType<>("backstab", (type, player) ->
            new SimplePlayerAnimationAbility(type, player, "backstab", 12, true, true)
    );

    public static final AbilityType<PlayerEntity, RockSlingAbility> ROCK_SLING = new AbilityType<>("rock_sling", RockSlingAbility::new);

    public static final AbilityType<PlayerEntity, ? extends PlayerAbility>[] PLAYER_ABILITIES = new AbilityType[]{
            SUNSTRIKE_ABILITY,
            SOLAR_BEAM_ABILITY,
            SOLAR_FLARE_ABILITY,
            SUPERNOVA_ABILITY,
            WROUGHT_AXE_SWING_ABILITY,
            WROUGHT_AXE_SLAM_ABILITY,
            ICE_BREATH_ABILITY,
            SPAWN_BOULDER_ABILITY,
            SPAWN_PILLAR_ABILITY,
            TUNNELING_ABILITY,
            HIT_BOULDER_ABILITY,
            BOULDER_ROLL_ABILITY,
            ROCK_SLING,
            GROUND_SLAM_ABILITY,
            FISSURE_ABILITY,
            BACKSTAB_ABILITY
    };

    @Nullable
    public Ability<?> getAbility(LivingEntity entity, AbilityType<?, ?> abilityType) {
        AbilityCapability.IAbilityCapability abilityCapability = AbilityCapability.get(entity);
        if (abilityCapability != null)
            return abilityCapability.getAbilityFromType(abilityType);
        return null;
    }

    public <T extends LivingEntity> void sendAbilityMessage(T entity, AbilityType<?, ?> abilityType) {
        if (entity.getWorld().isClient) return;
        AbilityCapability.IAbilityCapability abilityCapability = AbilityCapability.get(entity);
        if (abilityCapability != null) {
            Ability<?> instance = abilityCapability.getAbilityFromType(abilityType);
            if (instance != null && instance.canUse()) {
                abilityCapability.activateAbility(entity, abilityType);
                PacketByteBuf buf = PacketByteBufs.create();
                MessageUseAbility.serialize(new MessageUseAbility(entity.getId(), ArrayUtils.indexOf(abilityCapability.getAbilityTypesOnEntity(entity), abilityType)), buf);
                ServerHelper.sendToAll(StaticVariables.USE_ABILITY, buf);
            }
        }
    }

    public <T extends LivingEntity> void sendInterruptAbilityMessage(T entity, AbilityType<?, ?> abilityType) {
        if (entity.getWorld().isClient) return;
        AbilityCapability.IAbilityCapability abilityCapability = AbilityCapability.get(entity);
        if (abilityCapability != null) {
            Ability<?> instance = abilityCapability.getAbilityMap().get(abilityType);
            if (instance.isUsing()) {
                instance.interrupt();
                PacketByteBuf buf = PacketByteBufs.create();
                MessageInterruptAbility.serialize(new MessageInterruptAbility(entity.getId(), ArrayUtils.indexOf(abilityCapability.getAbilityTypesOnEntity(entity), abilityType)), buf);
                ServerHelper.sendToAll(StaticVariables.INTERRUPT_ABILITY, buf);
            }
        }
    }

    public <T extends PlayerEntity> void sendPlayerTryAbilityMessage(T entity, AbilityType<?, ?> ability) {
        if (!(entity.getWorld().isClient && entity instanceof ClientPlayerEntity)) return;
        AbilityCapability.IAbilityCapability abilityCapability = AbilityCapability.get(entity);
        if (abilityCapability != null) {
            PacketByteBuf buf = PacketByteBufs.create();
            MessagePlayerUseAbility.serialize(new MessagePlayerUseAbility(ArrayUtils.indexOf(abilityCapability.getAbilityTypesOnEntity(entity), ability)), buf);
            ClientPlayNetworking.send(StaticVariables.PLAYER_USE_ABILITY, buf);
        }
    }


    public <T extends LivingEntity> void sendJumpToSectionMessage(T entity, AbilityType<?, ?> abilityType, int sectionIndex) {
        if (entity.getWorld().isClient) return;
        AbilityCapability.IAbilityCapability abilityCapability = AbilityCapability.get(entity);
        if (abilityCapability != null) {
            Ability<?> instance = abilityCapability.getAbilityMap().get(abilityType);
            if (instance.isUsing()) {
                instance.jumpToSection(sectionIndex);
                PacketByteBuf buf = PacketByteBufs.create();
                MessageJumpToAbilitySection.serialize(new MessageJumpToAbilitySection(entity.getId(), ArrayUtils.indexOf(abilityCapability.getAbilityTypesOnEntity(entity), abilityType), sectionIndex), buf);
                ServerHelper.sendToAll(StaticVariables.JUMP_TO_ABILITY_SECTION, buf);
            }
        }
    }
}