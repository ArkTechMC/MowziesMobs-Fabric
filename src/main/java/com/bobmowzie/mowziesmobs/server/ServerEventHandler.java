package com.bobmowzie.mowziesmobs.server;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.particle.ParticleHandler;
import com.bobmowzie.mowziesmobs.client.particle.ParticleVanillaCloudExtended;
import com.bobmowzie.mowziesmobs.client.particle.util.AdvancedParticleBase;
import com.bobmowzie.mowziesmobs.client.particle.util.ParticleComponent;
import com.bobmowzie.mowziesmobs.client.particle.util.ParticleRotation;
import com.bobmowzie.mowziesmobs.server.ability.AbilityHandler;
import com.bobmowzie.mowziesmobs.server.ai.AvoidEntityIfNotTamedGoal;
import com.bobmowzie.mowziesmobs.server.block.BlockHandler;
import com.bobmowzie.mowziesmobs.server.capability.*;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.LeaderSunstrikeImmune;
import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import com.bobmowzie.mowziesmobs.server.entity.MowzieGeckoEntity;
import com.bobmowzie.mowziesmobs.server.entity.foliaath.EntityFoliaath;
import com.bobmowzie.mowziesmobs.server.entity.frostmaw.EntityFrostmaw;
import com.bobmowzie.mowziesmobs.server.entity.naga.EntityNaga;
import com.bobmowzie.mowziesmobs.server.entity.sculptor.EntitySculptor;
import com.bobmowzie.mowziesmobs.server.entity.umvuthana.*;
import com.bobmowzie.mowziesmobs.server.entity.wroughtnaut.EntityWroughtnaut;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import com.bobmowzie.mowziesmobs.server.item.ItemNagaFangDagger;
import com.bobmowzie.mowziesmobs.server.item.ItemSpear;
import com.bobmowzie.mowziesmobs.server.item.ItemUmvuthanaMask;
import com.bobmowzie.mowziesmobs.server.message.MessageFreezeEffect;
import com.bobmowzie.mowziesmobs.server.message.MessagePlayerAttackMob;
import com.bobmowzie.mowziesmobs.server.message.MessageSunblockEffect;
import com.bobmowzie.mowziesmobs.server.message.StaticVariables;
import com.bobmowzie.mowziesmobs.server.potion.EffectHandler;
import com.bobmowzie.mowziesmobs.server.power.Power;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import com.iafenvoy.uranus.ServerHelper;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.FleeEntityGoal;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.passive.ParrotEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityMountEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class ServerEventHandler {
    public static boolean onJoinWorld(Entity entity, World world) {
        if (entity instanceof PlayerEntity || entity instanceof MowzieGeckoEntity) {
            AbilityCapability.IAbilityCapability abilityCapability = AbilityHandler.INSTANCE.getAbilityCapability((LivingEntity) entity);
            if (abilityCapability != null) abilityCapability.instanceAbilities((LivingEntity) entity);
        }

        if (entity instanceof PlayerEntity player) {
            PlayerCapability.IPlayerCapability playerCapability = CapabilityHandler.getCapability(player, CapabilityHandler.PLAYER_CAPABILITY);
            if (playerCapability != null) playerCapability.addedToWorld(player, world);
        }

        if (world.isClient)
            return false;
        if (entity instanceof ZombieEntity && !(entity instanceof ZombifiedPiglinEntity)) {
            ((PathAwareEntity) entity).targetSelector.add(2, new ActiveTargetGoal<>((PathAwareEntity) entity, EntityFoliaath.class, 0, true, false, null));
            ((PathAwareEntity) entity).targetSelector.add(3, new ActiveTargetGoal<>((PathAwareEntity) entity, EntityUmvuthana.class, 0, true, false, null));
            ((PathAwareEntity) entity).targetSelector.add(2, new ActiveTargetGoal<>((PathAwareEntity) entity, EntityUmvuthi.class, 0, true, false, null));
        }
        if (entity instanceof AbstractSkeletonEntity skeleton) {
            skeleton.targetSelector.add(3, new ActiveTargetGoal<>((PathAwareEntity) entity, EntityUmvuthana.class, 0, true, false, null));
            skeleton.targetSelector.add(2, new ActiveTargetGoal<>((PathAwareEntity) entity, EntityUmvuthi.class, 0, true, false, null));
        }

        if (entity instanceof ParrotEntity parrot) {
            parrot.goalSelector.add(3, new FleeEntityGoal<>((PathAwareEntity) entity, EntityFoliaath.class, 6.0F, 1.0D, 1.2D));
        }
        if (entity instanceof AnimalEntity animal) {
            animal.goalSelector.add(3, new AvoidEntityIfNotTamedGoal<>((PathAwareEntity) entity, EntityFoliaath.class, 6.0F, 1.0D, 1.2D));
            animal.goalSelector.add(3, new AvoidEntityIfNotTamedGoal<>((PathAwareEntity) entity, EntityUmvuthana.class, 6.0F, 1.0D, 1.2D));
            animal.goalSelector.add(3, new AvoidEntityIfNotTamedGoal<>((PathAwareEntity) entity, EntityUmvuthi.class, 6.0F, 1.0D, 1.2D));
            animal.goalSelector.add(3, new AvoidEntityIfNotTamedGoal<>((PathAwareEntity) entity, EntityNaga.class, 10.0F, 1.0D, 1.2D));
            animal.goalSelector.add(3, new AvoidEntityIfNotTamedGoal<>((PathAwareEntity) entity, EntityFrostmaw.class, 10.0F, 1.0D, 1.2D));
        }
        if (entity instanceof MerchantEntity merchant) {
            merchant.goalSelector.add(3, new FleeEntityGoal<>((PathAwareEntity) entity, EntityUmvuthana.class, 6.0F, 1.0D, 1.2D));
            merchant.goalSelector.add(3, new FleeEntityGoal<>((PathAwareEntity) entity, EntityUmvuthi.class, 6.0F, 1.0D, 1.2D));
            merchant.goalSelector.add(3, new FleeEntityGoal<>((PathAwareEntity) entity, EntityNaga.class, 10.0F, 1.0D, 1.2D));
            merchant.goalSelector.add(3, new FleeEntityGoal<>((PathAwareEntity) entity, EntityFrostmaw.class, 10.0F, 1.0D, 1.2D));
        }
        return false;
    }

    @SubscribeEvent
    public void onLivingTick(LivingEvent.LivingTickEvent event) {
        if (event.getEntity() instanceof LivingEntity) {
            LivingEntity entity = (LivingEntity) event.getEntity();

            if (entity.getStatusEffect(EffectHandler.POISON_RESIST) != null && entity.getStatusEffect(StatusEffects.POISON) != null) {
                entity.removeStatusEffectInternal(StatusEffects.POISON);
            }

            if (!entity.getWorld().isClient) {
                Item headItemStack = entity.getEquippedStack(EquipmentSlot.HEAD).getItem();
                if (headItemStack instanceof ItemUmvuthanaMask mask) {
                    EffectHandler.addOrCombineEffect(entity, mask.getPotion(), 50, 0, true, false);
                }
            }

            if (entity instanceof MobEntity mob && !(entity instanceof EntityUmvuthanaCrane)) {
                if (mob.getTarget() instanceof EntityUmvuthi && mob.getTarget().hasStatusEffect(EffectHandler.SUNBLOCK)) {
                    EntityUmvuthanaCrane sunblocker = mob.getWorld().getClosestEntity(EntityUmvuthanaCrane.class, TargetPredicate.DEFAULT, mob, mob.getX(), mob.getY() + mob.getStandingEyeHeight(), mob.getZ(), mob.getBoundingBox().expand(40.0D, 15.0D, 40.0D));
                    mob.setTarget(sunblocker);
                }
            }

            FrozenCapability.IFrozenCapability frozenCapability = CapabilityHandler.getCapability(entity, CapabilityHandler.FROZEN_CAPABILITY);
            if (frozenCapability != null) {
                frozenCapability.tick(entity);
            }
            LivingCapability.ILivingCapability livingCapability = CapabilityHandler.getCapability(entity, CapabilityHandler.LIVING_CAPABILITY);
            if (livingCapability != null) {
                livingCapability.tick(entity);
            }
            AbilityCapability.IAbilityCapability abilityCapability = CapabilityHandler.getCapability(entity, CapabilityHandler.ABILITY_CAPABILITY);
            if (abilityCapability != null) {
                abilityCapability.tick(entity);
            }
        }
    }

    @SubscribeEvent
    public void onAddPotionEffect(MobEffectEvent.Added event) {
        if (event.getEffectInstance().getEffect() == EffectHandler.SUNBLOCK) {
            if (!event.getEntity().level().isClientSide()) {
                PacketByteBuf buf = PacketByteBufs.create();
                MessageSunblockEffect.serialize(new MessageSunblockEffect(event.getEntity(), true), buf);
                ServerHelper.sendToAll(StaticVariables.SUNBLOCK_EFFECT, buf);
            }
            MowziesMobs.PROXY.playSunblockSound(event.getEntity());
        }
        if (event.getEffectInstance().getEffect() == EffectHandler.FROZEN) {
            if (!event.getEntity().level().isClientSide()) {
                PacketByteBuf buf = PacketByteBufs.create();
                MessageFreezeEffect.serialize(new MessageFreezeEffect(event.getEntity(), true), buf);
                ServerHelper.sendToAll(StaticVariables.FREEZE_EFFECT, buf);
                FrozenCapability.IFrozenCapability frozenCapability = CapabilityHandler.getCapability(event.getEntity(), CapabilityHandler.FROZEN_CAPABILITY);
                if (frozenCapability != null) {
                    frozenCapability.onFreeze(event.getEntity());
                }
            }
        }
    }

    @SubscribeEvent
    public void onRemovePotionEffect(MobEffectEvent.Remove event) {
        if (event.getEffectInstance() == null)
            return;
        if (!event.getEntity().level().isClientSide() && event.getEffectInstance().getEffect() == EffectHandler.SUNBLOCK) {
            PacketByteBuf buf = PacketByteBufs.create();
            MessageSunblockEffect.serialize(new MessageSunblockEffect(event.getEntity(), false), buf);
            ServerHelper.sendToAll(StaticVariables.SUNBLOCK_EFFECT, buf);
        }
        if (!event.getEntity().level().isClientSide() && event.getEffectInstance().getEffect() == EffectHandler.FROZEN) {
            PacketByteBuf buf = PacketByteBufs.create();
            MessageFreezeEffect.serialize(new MessageFreezeEffect(event.getEntity(), false), buf);
            ServerHelper.sendToAll(StaticVariables.FREEZE_EFFECT, buf);
            FrozenCapability.IFrozenCapability frozenCapability = CapabilityHandler.getCapability(event.getEntity(), CapabilityHandler.FROZEN_CAPABILITY);
            if (frozenCapability != null) {
                frozenCapability.onUnfreeze(event.getEntity());
            }
        }
    }

    @SubscribeEvent
    public void onPotionEffectExpire(MobEffectEvent.Expired event) {
        StatusEffectInstance effectInstance = event.getEffectInstance();
        if (!event.getEntity().level().isClientSide() && effectInstance != null && effectInstance.getEffectType() == EffectHandler.SUNBLOCK) {
            PacketByteBuf buf = PacketByteBufs.create();
            MessageSunblockEffect.serialize(new MessageSunblockEffect(event.getEntity(), false), buf);
            ServerHelper.sendToAll(StaticVariables.SUNBLOCK_EFFECT, buf);
        }
        if (!event.getEntity().level().isClientSide() && effectInstance != null && effectInstance.getEffectType() == EffectHandler.FROZEN) {
            PacketByteBuf buf = PacketByteBufs.create();
            MessageFreezeEffect.serialize(new MessageFreezeEffect(event.getEntity(), false), buf);
            ServerHelper.sendToAll(StaticVariables.FREEZE_EFFECT, buf);
            FrozenCapability.IFrozenCapability frozenCapability = CapabilityHandler.getCapability(event.getEntity(), CapabilityHandler.FROZEN_CAPABILITY);
            if (frozenCapability != null) {
                frozenCapability.onUnfreeze(event.getEntity());
            }
        }
    }

    public static float onLivingHurt(LivingEntity livingEntity, DamageSource source, float damage) {
        // Copied from LivingEntity's applyPotionDamageCalculations
        if (source == null || livingEntity == null) return damage;
        if (!source.isIn(DamageTypeTags.BYPASSES_RESISTANCE)) {
            if (livingEntity.hasStatusEffect(EffectHandler.SUNBLOCK) && !source.isOf(DamageTypes.OUT_OF_WORLD)) {
                int i = (livingEntity.getStatusEffect(EffectHandler.SUNBLOCK).getAmplifier() + 2) * 5;
                int j = 25 - i;
                float f = damage * (float) j;
                float f1 = damage;
                damage = Math.max(f / 25.0F, 0.0F);
                float f2 = f1 - damage;
                if (f2 > 0.0F && f2 < 3.4028235E37F) {
                    if (livingEntity instanceof ServerPlayerEntity serverPlayer) {
                        serverPlayer.increaseStat(Stats.DAMAGE_RESISTED, Math.round(f2 * 10.0F));
                    } else if (source.getAttacker() instanceof ServerPlayerEntity serverPlayer) {
                        serverPlayer.increaseStat(Stats.DAMAGE_DEALT_RESISTED, Math.round(f2 * 10.0F));
                    }
                }
            }
        }

        if (source.isIn(DamageTypeTags.IS_FIRE)) {
            livingEntity.removeStatusEffectInternal(EffectHandler.FROZEN);
            PacketByteBuf buf = PacketByteBufs.create();
            MessageFreezeEffect.serialize(new MessageFreezeEffect(livingEntity, false), buf);
            ServerHelper.sendToAll(StaticVariables.FREEZE_EFFECT, buf);
            FrozenCapability.IFrozenCapability frozenCapability = CapabilityHandler.getCapability(livingEntity, CapabilityHandler.FROZEN_CAPABILITY);
            if (frozenCapability != null) {
                frozenCapability.onUnfreeze(livingEntity);
            }
        }
        if (livingEntity instanceof PlayerEntity) {
            PlayerCapability.IPlayerCapability playerCapability = CapabilityHandler.getCapability(livingEntity, CapabilityHandler.PLAYER_CAPABILITY);
            if (playerCapability != null) {
                Power[] powers = playerCapability.getPowers();
                for (Power power : powers) {
                    power.onTakeDamage(livingEntity, source, damage);
                }
            }
        }

        LivingCapability.ILivingCapability capability = CapabilityHandler.getCapability(livingEntity, CapabilityHandler.LIVING_CAPABILITY);
        if (capability != null) {
            capability.setLastDamage(damage);
        }
        return damage;
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.START || event.player == null) {
            return;
        }
        PlayerEntity player = event.player;
        PlayerCapability.IPlayerCapability playerCapability = CapabilityHandler.getCapability(player, CapabilityHandler.PLAYER_CAPABILITY);
        if (playerCapability != null) {
            playerCapability.tick(player);

            Power[] powers = playerCapability.getPowers();
            for (Power power : powers) {
                power.tick(event);
            }
        }
    }

    @SubscribeEvent
    public void onUseItem(LivingEntityUseItemEvent event) {
        LivingEntity living = event.getEntity();
        if (event.isCancelable() && living.hasStatusEffect(EffectHandler.FROZEN)) {
            event.setCanceled(true);
            return;
        }

        AbilityCapability.IAbilityCapability abilityCapability = AbilityHandler.INSTANCE.getAbilityCapability(living);
        if (abilityCapability != null && event.isCancelable() && abilityCapability.itemUsePrevented(event.getItem())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onPlaceBlock(BlockEvent.EntityPlaceEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof LivingEntity living) {
            if (event.isCancelable() && living.hasStatusEffect(EffectHandler.FROZEN.get())) {
                event.setCanceled(true);
                return;
            }

            AbilityCapability.IAbilityCapability abilityCapability = AbilityHandler.INSTANCE.getAbilityCapability(living);
            if (abilityCapability != null && event.isCancelable() && abilityCapability.blockBreakingBuildingPrevented()) {
                event.setCanceled(true);
                return;
            }

            if (entity instanceof PlayerEntity) {
                this.cheatSculptor((PlayerEntity) entity);

                BlockState block = event.getPlacedBlock();
                if (
                        block.getBlock() == Blocks.FIRE ||
                                block.getBlock() == Blocks.TNT ||
                                block.getBlock() == Blocks.RESPAWN_ANCHOR ||
                                block.getBlock() == Blocks.DISPENSER ||
                                block.getBlock() == Blocks.CACTUS
                ) {
                    this.aggroUmvuthana((PlayerEntity) entity);
                }
            }
        }
    }

    @SubscribeEvent
    public void onFillBucket(FillBucketEvent event) {
        LivingEntity living = event.getEntity();
        if (living != null) {
            if (event.isCancelable() && living.hasStatusEffect(EffectHandler.FROZEN)) {
                event.setCanceled(true);
                return;
            }

            AbilityCapability.IAbilityCapability abilityCapability = AbilityHandler.INSTANCE.getAbilityCapability(event.getEntity());
            if (abilityCapability != null && event.isCancelable() && abilityCapability.interactingPrevented()) {
                event.setCanceled(true);
                return;
            }

            if (event.getEmptyBucket().getItem() == Items.LAVA_BUCKET) {
                this.aggroUmvuthana(event.getEntity());
            }

            if (event.getEmptyBucket().getItem() == Items.WATER_BUCKET) {
                this.cheatSculptor(event.getEntity());
            }
        }
    }

    @SubscribeEvent
    public void onBreakBlock(BlockEvent.BreakEvent event) {
        if (event.isCancelable() && event.getPlayer().hasEffect(EffectHandler.FROZEN.get())) {
            event.setCanceled(true);
            return;
        }

        AbilityCapability.IAbilityCapability abilityCapability = AbilityHandler.INSTANCE.getAbilityCapability(event.getPlayer());
        if (abilityCapability != null && event.isCancelable() && abilityCapability.blockBreakingBuildingPrevented()) {
            event.setCanceled(true);
            return;
        }

        this.cheatSculptor(event.getPlayer());

        BlockState block = event.getState();
        if (block.getBlock() == Blocks.GOLD_BLOCK ||
                block.isIn(BlockTags.PLANKS) ||
                block.isIn(BlockTags.LOGS) ||
                block.isIn(BlockTags.LEAVES) ||
                block.getBlock() == Blocks.LIGHT_GRAY_TERRACOTTA ||
                block.getBlock() == Blocks.RED_TERRACOTTA ||
                block.getBlock() == Blocks.SMOOTH_RED_SANDSTONE_SLAB ||
                block.getBlock() == Blocks.SMOOTH_RED_SANDSTONE ||
                block.getBlock() == Blocks.SMOOTH_RED_SANDSTONE_STAIRS ||
                block.getBlock() == Blocks.CAMPFIRE ||
                block.getBlock() == Blocks.IRON_BARS ||
                block.getBlock() == Blocks.SKELETON_SKULL ||
                block.getBlock() == Blocks.TORCH
        ) {
            this.aggroUmvuthana(event.getPlayer());
        }
    }

    public <T extends Entity> List<T> getEntitiesNearby(Entity startEntity, Class<T> entityClass, double r) {
        return startEntity.getWorld().getEntitiesByClass(entityClass, startEntity.getBoundingBox().expand(r, r, r), e -> e != startEntity && startEntity.distanceTo(e) <= r);
    }

    private List<LivingEntity> getEntityBaseNearby(LivingEntity user, double distanceX, double distanceY, double distanceZ, double radius) {
        List<Entity> list = user.getWorld().getOtherEntities(user, user.getBoundingBox().expand(distanceX, distanceY, distanceZ));
        ArrayList<LivingEntity> nearEntities = list.stream().filter(entityNeighbor -> entityNeighbor instanceof LivingEntity && user.distanceTo(entityNeighbor) <= radius).map(entityNeighbor -> (LivingEntity) entityNeighbor).collect(Collectors.toCollection(ArrayList::new));
        return nearEntities;
    }

    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent.RightClickEmpty event) {
        if (event.isCancelable() && event.getEntity().hasEffect(EffectHandler.FROZEN.get())) {
            event.setCanceled(true);
            return;
        }

        AbilityCapability.IAbilityCapability abilityCapability = AbilityHandler.INSTANCE.getAbilityCapability(event.getEntity());
        if (abilityCapability != null && event.isCancelable() && abilityCapability.interactingPrevented()) {
            event.setCanceled(true);
            return;
        }

        PlayerEntity player = event.getEntity();
        PlayerCapability.IPlayerCapability playerCapability = CapabilityHandler.getCapability(player, CapabilityHandler.PLAYER_CAPABILITY);
        if (playerCapability != null) {

            if (event.getLevel().isClientSide && player.getInventory().getMainHandStack().isEmpty() && player.hasStatusEffect(EffectHandler.SUNS_BLESSING.get())) {
                if (player.isSneaking()) {
                    AbilityHandler.INSTANCE.sendPlayerTryAbilityMessage(event.getEntity(), AbilityHandler.SOLAR_BEAM_ABILITY);
                } else {
                    AbilityHandler.INSTANCE.sendPlayerTryAbilityMessage(event.getEntity(), AbilityHandler.SUNSTRIKE_ABILITY);
                }
            }

            Power[] powers = playerCapability.getPowers();
            for (Power power : powers) {
                power.onRightClickEmpty(event);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent.EntityInteract event) {
        if (event.isCancelable() && event.getEntity().hasEffect(EffectHandler.FROZEN.get())) {
            event.setCanceled(true);
            return;
        }

        AbilityCapability.IAbilityCapability abilityCapability = AbilityHandler.INSTANCE.getAbilityCapability(event.getEntity());
        if (abilityCapability != null && event.isCancelable() && abilityCapability.interactingPrevented()) {
            event.setCanceled(true);
            return;
        }

        PlayerCapability.IPlayerCapability playerCapability = CapabilityHandler.getCapability(event.getEntity(), CapabilityHandler.PLAYER_CAPABILITY);
        if (playerCapability != null) {
            Power[] powers = playerCapability.getPowers();
            for (Power power : powers) {
                power.onRightClickEntity(event);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent.RightClickBlock event) {
        if (event.isCancelable() && event.getEntity().hasEffect(EffectHandler.FROZEN.get())) {
            event.setCanceled(true);
            return;
        }

        AbilityCapability.IAbilityCapability abilityCapability = AbilityHandler.INSTANCE.getAbilityCapability(event.getEntity());
        if (abilityCapability != null && event.isCancelable() && abilityCapability.interactingPrevented()) {
            event.setCanceled(true);
            return;
        }

        PlayerEntity player = event.getEntity();
        if (player.getWorld().getBlockState(event.getPos()).getBlock() instanceof ChestBlock) {
            this.aggroUmvuthana(player);
        }

        PlayerCapability.IPlayerCapability playerCapability = CapabilityHandler.getCapability(player, CapabilityHandler.PLAYER_CAPABILITY);
        if (playerCapability != null) {

            ItemStack item = event.getItemStack();
            if (
                    item.getItem() == Items.FLINT_AND_STEEL ||
                            item.getItem() == Items.TNT_MINECART
            ) {
                this.aggroUmvuthana(player);
            }

            if (event.getSide() == LogicalSide.CLIENT && player.getInventory().getMainHandStack().isEmpty() && player.hasStatusEffect(EffectHandler.SUNS_BLESSING.get()) && player.getWorld().getBlockState(event.getPos()).getMenuProvider(player.getWorld(), event.getPos()) == null) {
                if (player.isSneaking()) {
                    AbilityHandler.INSTANCE.sendPlayerTryAbilityMessage(event.getEntity(), AbilityHandler.SOLAR_BEAM_ABILITY);
                } else {
                    AbilityHandler.INSTANCE.sendPlayerTryAbilityMessage(event.getEntity(), AbilityHandler.SUNSTRIKE_ABILITY);
                }
            }
            if (player.getMainHandStack().isIn(ItemHandler.WROUGHT_AXE.get()) && player.getWorld().getBlockState(event.getPos()).getMenuProvider(player.getWorld(), event.getPos()) != null) {
                player.resetLastAttackedTicks();
                return;
            }
            Power[] powers = playerCapability.getPowers();
            for (Power power : powers) {
                power.onRightClickBlock(event);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerLeftClick(PlayerInteractEvent.LeftClickEmpty event) {
        double range = 6.5;
        PlayerEntity player = event.getEntity();
        PlayerCapability.IPlayerCapability playerCapability = CapabilityHandler.getCapability(player, CapabilityHandler.PLAYER_CAPABILITY);
        if (player.getMainHandStack() != null && player.getMainHandStack().getItem() == ItemHandler.SPEAR) {
            LivingEntity entityHit = ItemSpear.raytraceEntities(player.getEntityWorld(), player, range);
            if (entityHit != null) {
                PacketByteBuf buf = PacketByteBufs.create();
                MessagePlayerAttackMob.serialize(new MessagePlayerAttackMob(entityHit), buf);
                ServerHelper.sendToAll(StaticVariables.PLAYER_ATTACK_MOB, buf);
            }
        }
        if (playerCapability != null) {
            Power[] powers = playerCapability.getPowers();
            for (Power power : powers) {
                power.onLeftClickEmpty(event);
            }
        }
    }

    @SubscribeEvent
    public void onLivingDamage(LivingDamageEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity.getHealth() <= event.getAmount() && entity.hasStatusEffect(EffectHandler.FROZEN)) {
            entity.removeStatusEffectInternal(EffectHandler.FROZEN);
            FrozenCapability.IFrozenCapability frozenCapability = CapabilityHandler.getCapability(entity, CapabilityHandler.FROZEN_CAPABILITY);
            PacketByteBuf buf = PacketByteBufs.create();
            MessageFreezeEffect.serialize(new MessageFreezeEffect(event.getEntity(), false), buf);
            ServerHelper.sendToAll(StaticVariables.FREEZE_EFFECT, buf);
            if (frozenCapability != null) {
                frozenCapability.onUnfreeze(entity);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent.RightClickItem event) {
        if (event.isCancelable() && event.getEntity().hasEffect(EffectHandler.FROZEN.get())) {
            event.setCanceled(true);
            return;
        }

        AbilityCapability.IAbilityCapability abilityCapability = AbilityHandler.INSTANCE.getAbilityCapability(event.getEntity());
        if (abilityCapability != null && event.isCancelable() && abilityCapability.itemUsePrevented(event.getItemStack())) {
            event.setCanceled(true);
            return;
        }

        PlayerCapability.IPlayerCapability playerCapability = CapabilityHandler.getCapability(event.getEntity(), CapabilityHandler.PLAYER_CAPABILITY);
        if (playerCapability != null) {
            Power[] powers = playerCapability.getPowers();
            for (Power power : powers) {
                power.onRightClickWithItem(event);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerLeftClick(PlayerInteractEvent.LeftClickBlock event) {
        PlayerEntity player = event.getEntity();
        if (event.isCancelable() && player.hasStatusEffect(EffectHandler.FROZEN.get())) {
            event.setCanceled(true);
            return;
        }

        AbilityCapability.IAbilityCapability abilityCapability = AbilityHandler.INSTANCE.getAbilityCapability(event.getEntity());
        if (abilityCapability != null && event.isCancelable() && abilityCapability.blockBreakingBuildingPrevented()) {
            event.setCanceled(true);
            return;
        }

        PlayerCapability.IPlayerCapability playerCapability = CapabilityHandler.getCapability(player, CapabilityHandler.PLAYER_CAPABILITY);
        if (playerCapability != null) {
            Power[] powers = playerCapability.getPowers();
            for (Power power : powers) {
                power.onLeftClickBlock(event);
            }
        }
    }

    @SubscribeEvent
    public void onLivingJump(LivingEvent.LivingJumpEvent event) {
        if (event.getEntity() instanceof LivingEntity) {
            LivingEntity entity = (LivingEntity) event.getEntity();
            if (entity.hasStatusEffect(EffectHandler.FROZEN.get()) && entity.isOnGround()) {
                entity.setVelocity(entity.getVelocity().multiply(1, 0, 1));
            }
        }

        if (event.getEntity() instanceof PlayerEntity) {
            PlayerCapability.IPlayerCapability playerCapability = CapabilityHandler.getCapability(event.getEntity(), CapabilityHandler.PLAYER_CAPABILITY);
            if (playerCapability != null) {
                Power[] powers = playerCapability.getPowers();
                for (Power power : powers) {
                    power.onJump(event);
                }
            }
        }
    }

    @SubscribeEvent
    public void onPlayerAttack(AttackEntityEvent event) {
        if (event.isCancelable() && event.getEntity().hasEffect(EffectHandler.FROZEN.get())) {
            event.setCanceled(true);
            return;
        }

        if (event.getEntity() != null) {
            AbilityCapability.IAbilityCapability abilityCapability = AbilityHandler.INSTANCE.getAbilityCapability(event.getEntity());
            if (abilityCapability != null && event.isCancelable() && abilityCapability.attackingPrevented()) {
                event.setCanceled(true);
                return;
            }

            PlayerCapability.IPlayerCapability playerCapability = CapabilityHandler.getCapability(event.getEntity(), CapabilityHandler.PLAYER_CAPABILITY);
            if (playerCapability != null) {
                playerCapability.setPrevCooledAttackStrength(event.getEntity().getAttackStrengthScale(0.5f));

                Power[] powers = playerCapability.getPowers();
                for (Power power : powers) {
                    power.onLeftClickEntity(event);
                }

                if (event.getTarget() instanceof ItemFrameEntity itemFrame) {
                    if (itemFrame.getHeldItemStack().getItem() instanceof ItemUmvuthanaMask) {
                        this.aggroUmvuthana(event.getEntity());
                    }
                }
                if (event.getTarget() instanceof LeaderSunstrikeImmune) {
                    this.aggroUmvuthana(event.getEntity());
                }

                if (!(event.getTarget() instanceof LivingEntity)) return;
                if (event.getTarget() instanceof EntityUmvuthanaFollowerToPlayer) return;
                if (!event.getEntity().level().isClientSide()) {
                    for (int i = 0; i < playerCapability.getPackSize(); i++) {
                        EntityUmvuthanaFollowerToPlayer umvuthana = playerCapability.getUmvuthanaPack().get(i);
                        LivingEntity living = (LivingEntity) event.getTarget();
                        if (umvuthana.getMaskType() != MaskType.FAITH) {
                            if (!living.isInvulnerable()) umvuthana.setTarget(living);
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void checkCritEvent(CriticalHitEvent event) {
        ItemStack weapon = event.getEntity().getMainHandItem();
        PlayerEntity attacker = event.getEntity();
        PlayerCapability.IPlayerCapability playerCapability = CapabilityHandler.getCapability(event.getEntity(), CapabilityHandler.PLAYER_CAPABILITY);
        if (playerCapability != null && playerCapability.getPrevCooledAttackStrength() == 1.0f && !weapon.isEmpty() && event.getTarget() instanceof LivingEntity) {
            LivingEntity target = (LivingEntity) event.getTarget();
            if (weapon.getItem() instanceof ItemNagaFangDagger) {
                Vec3d lookDir = new Vec3d(target.getRotationVector().x, 0, target.getRotationVector().z).normalize();
                Vec3d vecBetween = new Vec3d(target.getX() - event.getEntity().getX(), 0, target.getZ() - event.getEntity().getZ()).normalize();
                double dot = lookDir.dotProduct(vecBetween);
                if (dot > 0.7) {
                    event.setResult(Event.Result.ALLOW);
                    event.setDamageModifier(ConfigHandler.COMMON.TOOLS_AND_ABILITIES.NAGA_FANG_DAGGER.backstabDamageMultiplier.get().floatValue());
                    target.playSound(MMSounds.ENTITY_NAGA_ACID_HIT.get(), 1f, 1.2f);
                    AbilityHandler.INSTANCE.sendAbilityMessage(attacker, AbilityHandler.BACKSTAB_ABILITY);

                    if (target.getWorld().isClient() && target != null && attacker != null) {
                        Vec3d ringOffset = attacker.getRotationVector().multiply(-target.getWidth() / 2.f);
                        ParticleRotation.OrientVector rotation = new ParticleRotation.OrientVector(ringOffset);
                        Vec3d pos = target.getPos().add(0, target.getHeight() / 2f, 0).add(ringOffset);
                        AdvancedParticleBase.spawnParticle(target.getWorld(), ParticleHandler.RING_SPARKS.get(), pos.getX(), pos.getY(), pos.getZ(), 0, 0, 0, rotation, 3.5F, 0.83f, 1, 0.39f, 1, 1, 6, false, true, new ParticleComponent[]{
                                new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.ALPHA, new ParticleComponent.KeyTrack(new float[]{1f, 1f, 0f}, new float[]{0f, 0.5f, 1f}), false),
                                new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.SCALE, ParticleComponent.KeyTrack.startAndEnd(0f, 15f), false)
                        });
                        Random rand = attacker.getWorld().getRandom();
                        float explodeSpeed = 2.5f;
                        for (int i = 0; i < 10; i++) {
                            Vec3d particlePos = new Vec3d(rand.nextFloat() * 0.25, 0, 0);
                            particlePos = particlePos.rotateY((float) (rand.nextFloat() * 2 * Math.PI));
                            particlePos = particlePos.rotateX((float) (rand.nextFloat() * 2 * Math.PI));
                            double value = rand.nextFloat() * 0.1f;
                            double life = rand.nextFloat() * 8f + 15f;
                            ParticleVanillaCloudExtended.spawnVanillaCloud(target.getWorld(), pos.getX(), pos.getY(), pos.getZ(), particlePos.x * explodeSpeed, particlePos.y * explodeSpeed, particlePos.z * explodeSpeed, 1, 0.25d + value, 0.75d + value, 0.25d + value, 0.6, life);
                        }
                        for (int i = 0; i < 10; i++) {
                            Vec3d particlePos = new Vec3d(rand.nextFloat() * 0.25, 0, 0);
                            particlePos = particlePos.rotateY((float) (rand.nextFloat() * 2 * Math.PI));
                            particlePos = particlePos.rotateX((float) (rand.nextFloat() * 2 * Math.PI));
                            double value = rand.nextFloat() * 0.1f;
                            double life = rand.nextFloat() * 2.5f + 5f;
                            AdvancedParticleBase.spawnParticle(target.getWorld(), ParticleHandler.PIXEL.get(), pos.getX(), pos.getY(), pos.getZ(), particlePos.x * explodeSpeed, particlePos.y * explodeSpeed, particlePos.z * explodeSpeed, true, 0, 0, 0, 0, 3f, 0.07d + value, 0.25d + value, 0.07d + value, 1d, 0.6, life * 0.95, false, true);
                        }
                        for (int i = 0; i < 6; i++) {
                            Vec3d particlePos = new Vec3d(rand.nextFloat() * 0.25, 0, 0);
                            particlePos = particlePos.rotateY((float) (rand.nextFloat() * 2 * Math.PI));
                            particlePos = particlePos.rotateX((float) (rand.nextFloat() * 2 * Math.PI));
                            double value = rand.nextFloat() * 0.1f;
                            double life = rand.nextFloat() * 5f + 10f;
                            AdvancedParticleBase.spawnParticle(target.getWorld(), ParticleHandler.BUBBLE.get(), pos.getX(), pos.getY(), pos.getZ(), particlePos.x * explodeSpeed, particlePos.y * explodeSpeed, particlePos.z * explodeSpeed, true, 0, 0, 0, 0, 3f, 0.25d + value, 0.75d + value, 0.25d + value, 1d, 0.6, life * 0.95, false, true);
                        }
                    }
                }
            } else if (weapon.getItem() instanceof ItemSpear) {
                if (target instanceof AnimalEntity && target.getMaxHealth() <= 30 && attacker.getWorld().getRandom().nextFloat() <= 0.334) {
                    event.setResult(Event.Result.ALLOW);
                    event.setDamageModifier(400);
                }
            }
        }
    }

    @SubscribeEvent
    public void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof LivingEntity) {
            event.addCapability(new Identifier(MowziesMobs.MODID, "frozen"), new FrozenCapability.FrozenProvider());
            event.addCapability(new Identifier(MowziesMobs.MODID, "last_damage"), new LivingCapability.LivingProvider());
            event.addCapability(new Identifier(MowziesMobs.MODID, "ability"), new AbilityCapability.AbilityProvider());
        }
        if (event.getObject() instanceof PlayerEntity) {
            event.addCapability(new Identifier(MowziesMobs.MODID, "player"), new PlayerCapability.PlayerProvider());
        }
    }

    @SubscribeEvent
    public void onRideEntity(EntityMountEvent event) {
        if (event.getEntityMounting() instanceof EntityUmvuthi || event.getEntityMounting() instanceof EntityFrostmaw || event.getEntityMounting() instanceof EntityWroughtnaut)
            event.setCanceled(true);
    }

    @SubscribeEvent
    public void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        List<MowzieEntity> mobs = this.getEntitiesNearby(event.getEntity(), MowzieEntity.class, 40);
        for (MowzieEntity mob : mobs) {
            if (mob.resetHealthOnPlayerRespawn()) {
                mob.setHealth(mob.getMaxHealth());
            }
        }
    }

    @SubscribeEvent
    public void onFurnaceFuelBurnTimeEvent(FurnaceFuelBurnTimeEvent event) {
        if (event.getItemStack().is(BlockHandler.CLAWED_LOG.get().asItem()) || event.getItemStack().is(BlockHandler.PAINTED_ACACIA.get().asItem())) {
            event.setBurnTime(300);
        } else if (event.getItemStack().is(BlockHandler.PAINTED_ACACIA_SLAB.get().asItem())) {
            event.setBurnTime(150);
        } else if (event.getItemStack().is(BlockHandler.THATCH.get().asItem())) {
            event.setBurnTime(100);
        }
    }

    private void aggroUmvuthana(PlayerEntity player) {
        List<EntityUmvuthi> barakos = this.getEntitiesNearby(player, EntityUmvuthi.class, 50);
        for (EntityUmvuthi barako : barakos) {
            if (barako.getTarget() == null || !(barako.getTarget() instanceof PlayerEntity)) {
                if (!player.isCreative() && !player.isSpectator() && player.getBlockPos().getSquaredDistance(barako.getPositionTarget()) < 900) {
                    if (barako.canTarget(player)) barako.setMisbehavedPlayerId(player.getUuid());
                }
            }
        }
        List<EntityUmvuthanaMinion> barakoas = this.getEntitiesNearby(player, EntityUmvuthanaMinion.class, 50);
        for (EntityUmvuthanaMinion barakoa : barakoas) {
            if (barakoa.getTarget() == null || !(barakoa.getTarget() instanceof PlayerEntity)) {
                if (player.getBlockPos().getSquaredDistance(barakoa.getPositionTarget()) < 900) {
                    if (barakoa.canTarget(player)) barakoa.setMisbehavedPlayerId(player.getUuid());
                }
            }
        }
    }

    private void cheatSculptor(PlayerEntity player) {
        List<EntitySculptor> sculptors = player.getWorld().getEntitiesByClass(EntitySculptor.class, player.getBoundingBox().expand(EntitySculptor.TEST_RADIUS + 3, EntitySculptor.TEST_HEIGHT, EntitySculptor.TEST_RADIUS + 3), EntitySculptor::isTesting);
        for (EntitySculptor sculptor : sculptors) {
            sculptor.playerCheated();
        }
    }
}
