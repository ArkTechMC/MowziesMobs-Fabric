package com.bobmowzie.mowziesmobs.server.message;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.ClientProxy;
import com.bobmowzie.mowziesmobs.server.ability.Ability;
import com.bobmowzie.mowziesmobs.server.ability.AbilityType;
import com.bobmowzie.mowziesmobs.server.block.BlockGrottol;
import com.bobmowzie.mowziesmobs.server.capability.AbilityCapability;
import com.bobmowzie.mowziesmobs.server.capability.FrozenCapability;
import com.bobmowzie.mowziesmobs.server.capability.LivingCapability;
import com.bobmowzie.mowziesmobs.server.entity.ILinkedEntity;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ClientNetworkHelper {
    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(StaticVariables.BLACK_PINK_IN_YOUR_AREA, (client, handler, buf, responseSender) -> {
            MessageBlackPinkInYourArea message = MessageBlackPinkInYourArea.deserialize(buf);
            ClientWorld world = MinecraftClient.getInstance().world;
            assert world != null;
            Entity entity = world.getEntityById(message.getEntityID());
            if (entity instanceof AbstractMinecartEntity minecart) {
                MowziesMobs.PROXY.playBlackPinkSound(minecart);
                BlockState state = Blocks.STONE.getDefaultState().with(BlockGrottol.VARIANT, BlockGrottol.Variant.BLACK_PINK);
                BlockPos pos = minecart.getBlockPos();
                final float scale = 0.75F;
                double x = minecart.getX();
                double y = minecart.getY() + 0.375F + 0.5F + (minecart.getDefaultBlockOffset() - 8) / 16.0F * scale;
                double z = minecart.getZ();
                BlockSoundGroup sound = state.getBlock().getSoundGroup(state);
                world.playSound(x, y, z, sound.getBreakSound(), minecart.getSoundCategory(), (sound.getVolume() + 1.0F) / 2.0F, sound.getPitch() * 0.8F, false);
                MowziesMobs.PROXY.minecartParticles(world, minecart, scale, x, y, z, state, pos);
            }
        });
        ClientPlayNetworking.registerGlobalReceiver(StaticVariables.FREEZE_EFFECT, (client, handler, buf, responseSender) -> {
            MessageFreezeEffect message = MessageFreezeEffect.deserialize(buf);
            if (MinecraftClient.getInstance().world != null) {
                Entity entity = MinecraftClient.getInstance().world.getEntityById(message.getEntityID());
                if (entity instanceof LivingEntity living) {
                    FrozenCapability.IFrozenCapability livingCapability = FrozenCapability.get(living);
                    if (livingCapability != null) {
                        if (message.isFrozen()) livingCapability.onFreeze(living);
                        else livingCapability.onUnfreeze(living);
                    }
                }
            }
        });
        ClientPlayNetworking.registerGlobalReceiver(StaticVariables.INTERRUPT_ABILITY, (client, handler, buf, responseSender) -> {
            MessageInterruptAbility message = MessageInterruptAbility.deserialize(buf);
            LivingEntity entity = (LivingEntity) MinecraftClient.getInstance().world.getEntityById(message.getEntityID());
            if (entity != null) {
                AbilityCapability.IAbilityCapability abilityCapability = AbilityCapability.get((PlayerEntity) entity);
                if (abilityCapability != null) {
                    AbilityType<?, ?> abilityType = abilityCapability.getAbilityTypesOnEntity(entity)[message.getIndex()];
                    Ability<?> instance = abilityCapability.getAbilityMap().get(abilityType);
                    if (instance.isUsing()) instance.interrupt();
                }
            }
        });
        ClientPlayNetworking.registerGlobalReceiver(StaticVariables.JUMP_TO_ABILITY_SECTION, (client, handler, buf, responseSender) -> {
            MessageJumpToAbilitySection message = MessageJumpToAbilitySection.deserialize(buf);
            LivingEntity entity = (LivingEntity) MinecraftClient.getInstance().world.getEntityById(message.getEntityID());
            if (entity != null) {
                AbilityCapability.IAbilityCapability abilityCapability = AbilityCapability.get((PlayerEntity) entity);
                if (abilityCapability != null) {
                    AbilityType<?, ?> abilityType = abilityCapability.getAbilityTypesOnEntity(entity)[message.getIndex()];
                    Ability<?> instance = abilityCapability.getAbilityMap().get(abilityType);
                    if (instance.isUsing()) instance.jumpToSection(message.getSectionIndex());
                }
            }
        });
        ClientPlayNetworking.registerGlobalReceiver(StaticVariables.LINK_ENTITIES, (client, handler, buf, responseSender) -> {
            MessageLinkEntities message = MessageLinkEntities.deserialize(buf);
            World world = MinecraftClient.getInstance().world;
            if (world != null) {
                Entity entitySource = world.getEntityById(message.getSourceID());
                Entity entityTarget = world.getEntityById(message.getTargetID());
                if (entitySource instanceof ILinkedEntity linked && entityTarget != null)
                    linked.link(entityTarget);
            }
        });
        ClientPlayNetworking.registerGlobalReceiver(StaticVariables.SUNBLOCK_EFFECT, (client, handler, buf, responseSender) -> {
            MessageSunblockEffect message = MessageSunblockEffect.deserialize(buf);
            if (MinecraftClient.getInstance().world != null) {
                Entity entity = MinecraftClient.getInstance().world.getEntityById(message.getEntityID());
                if (entity instanceof LivingEntity living) {
                    LivingCapability.ILivingCapability livingCapability = LivingCapability.get(living);
                    if (livingCapability != null)
                        livingCapability.setHasSunblock(message.hasSunblock());
                }
            }
        });

        ClientPlayNetworking.registerGlobalReceiver(StaticVariables.UPDATE_BOSS_BAR, (client, handler, buf, responseSender) -> {
            MessageUpdateBossBar message = MessageUpdateBossBar.deserialize(buf);
            if (message.getRegistryName() == null) {
                ClientProxy.bossBarRegistryNames.remove(message.getBossID());
            } else {
                ClientProxy.bossBarRegistryNames.put(message.getBossID(), message.getRegistryName());
            }
        });
        ClientPlayNetworking.registerGlobalReceiver(StaticVariables.USE_ABILITY, (client, handler, buf, responseSender) -> {
            MessageUseAbility message = MessageUseAbility.deserialize(buf);
            LivingEntity entity = (LivingEntity) MinecraftClient.getInstance().world.getEntityById(message.getEntityID());
            if (entity != null) {
                AbilityCapability.IAbilityCapability abilityCapability = AbilityCapability.get((PlayerEntity) entity);
                if (abilityCapability != null)
                    abilityCapability.activateAbility(entity, abilityCapability.getAbilityTypesOnEntity(entity)[message.getIndex()]);
            }
        });
    }
}
