package com.bobmowzie.mowziesmobs.server;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.particle.ParticleHandler;
import com.bobmowzie.mowziesmobs.client.particle.ParticleVanillaCloudExtended;
import com.bobmowzie.mowziesmobs.client.particle.util.AdvancedParticleBase;
import com.bobmowzie.mowziesmobs.client.particle.util.ParticleComponent;
import com.bobmowzie.mowziesmobs.client.particle.util.ParticleRotation;
import com.bobmowzie.mowziesmobs.server.ability.AbilityHandler;
import com.bobmowzie.mowziesmobs.server.ai.AvoidEntityIfNotTamedGoal;
import com.bobmowzie.mowziesmobs.server.capability.AbilityCapability;
import com.bobmowzie.mowziesmobs.server.capability.FrozenCapability;
import com.bobmowzie.mowziesmobs.server.capability.LivingCapability;
import com.bobmowzie.mowziesmobs.server.capability.PlayerCapability;
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
import com.bobmowzie.mowziesmobs.server.message.MessageSunblockEffect;
import com.bobmowzie.mowziesmobs.server.message.StaticVariables;
import com.bobmowzie.mowziesmobs.server.potion.EffectHandler;
import com.bobmowzie.mowziesmobs.server.power.Power;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import com.iafenvoy.uranus.ServerHelper;
import io.github.fabricators_of_create.porting_lib.core.event.BaseEvent;
import io.github.fabricators_of_create.porting_lib.entity.events.CriticalHitEvent;
import io.github.fabricators_of_create.porting_lib.entity.events.LivingEntityEvents;
import io.github.fabricators_of_create.porting_lib.event.common.BlockEvents;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.loader.api.FabricLoader;
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
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.mob.ZombifiedPiglinEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.passive.ParrotEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class ServerEventHandler {
    public static boolean onJoinWorld(Entity entity, World world) {
        if (entity instanceof PlayerEntity || entity instanceof MowzieGeckoEntity) {
            AbilityCapability.IAbilityCapability abilityCapability = AbilityCapability.get((LivingEntity) entity);
            if (abilityCapability != null) abilityCapability.instanceAbilities((LivingEntity) entity);
        }

        if (entity instanceof PlayerEntity player) {
            PlayerCapability.IPlayerCapability playerCapability = PlayerCapability.get(player);
            if (playerCapability != null) playerCapability.addedToWorld(player, world);
        }

        if (world.isClient)
            return true;
        if (entity instanceof ZombieEntity zombie && !(entity instanceof ZombifiedPiglinEntity)) {
            zombie.targetSelector.add(2, new ActiveTargetGoal<>(zombie, EntityFoliaath.class, 0, true, false, null));
            zombie.targetSelector.add(3, new ActiveTargetGoal<>(zombie, EntityUmvuthana.class, 0, true, false, null));
            zombie.targetSelector.add(2, new ActiveTargetGoal<>(zombie, EntityUmvuthi.class, 0, true, false, null));
        }
        if (entity instanceof AbstractSkeletonEntity skeleton) {
            skeleton.targetSelector.add(3, new ActiveTargetGoal<>(skeleton, EntityUmvuthana.class, 0, true, false, null));
            skeleton.targetSelector.add(2, new ActiveTargetGoal<>(skeleton, EntityUmvuthi.class, 0, true, false, null));
        }

        if (entity instanceof ParrotEntity parrot) {
            parrot.goalSelector.add(3, new FleeEntityGoal<>(parrot, EntityFoliaath.class, 6.0F, 1.0D, 1.2D));
        }
        if (entity instanceof AnimalEntity animal) {
            animal.goalSelector.add(3, new AvoidEntityIfNotTamedGoal<>(animal, EntityFoliaath.class, 6.0F, 1.0D, 1.2D));
            animal.goalSelector.add(3, new AvoidEntityIfNotTamedGoal<>(animal, EntityUmvuthana.class, 6.0F, 1.0D, 1.2D));
            animal.goalSelector.add(3, new AvoidEntityIfNotTamedGoal<>(animal, EntityUmvuthi.class, 6.0F, 1.0D, 1.2D));
            animal.goalSelector.add(3, new AvoidEntityIfNotTamedGoal<>(animal, EntityNaga.class, 10.0F, 1.0D, 1.2D));
            animal.goalSelector.add(3, new AvoidEntityIfNotTamedGoal<>(animal, EntityFrostmaw.class, 10.0F, 1.0D, 1.2D));
        }
        if (entity instanceof MerchantEntity merchant) {
            merchant.goalSelector.add(3, new FleeEntityGoal<>(merchant, EntityUmvuthana.class, 6.0F, 1.0D, 1.2D));
            merchant.goalSelector.add(3, new FleeEntityGoal<>(merchant, EntityUmvuthi.class, 6.0F, 1.0D, 1.2D));
            merchant.goalSelector.add(3, new FleeEntityGoal<>(merchant, EntityNaga.class, 10.0F, 1.0D, 1.2D));
            merchant.goalSelector.add(3, new FleeEntityGoal<>(merchant, EntityFrostmaw.class, 10.0F, 1.0D, 1.2D));
        }
        return true;
    }

    public static void onLivingTick(LivingEntity entity) {
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
    }

    public static void onAddPotionEffect(LivingEntity entity, StatusEffectInstance instance, @Nullable Entity source) {
        if (instance.getEffectType() == EffectHandler.SUNBLOCK) {
            if (!entity.getWorld().isClient) {
                PacketByteBuf buf = PacketByteBufs.create();
                MessageSunblockEffect.serialize(new MessageSunblockEffect(entity, true), buf);
                ServerHelper.sendToAll(StaticVariables.SUNBLOCK_EFFECT, buf);
            }
            MowziesMobs.PROXY.playSunblockSound(entity);
        }
        if (instance.getEffectType() == EffectHandler.FROZEN) {
            if (!entity.getWorld().isClient) {
                PacketByteBuf buf = PacketByteBufs.create();
                MessageFreezeEffect.serialize(new MessageFreezeEffect(entity, true), buf);
                ServerHelper.sendToAll(StaticVariables.FREEZE_EFFECT, buf);
                FrozenCapability.IFrozenCapability frozenCapability = FrozenCapability.get(entity);
                if (frozenCapability != null)
                    frozenCapability.onFreeze(entity);
            }
        }
    }

    public static void onRemovePotionEffect(LivingEntity entity, @NotNull StatusEffectInstance instance) {
        if (!entity.getWorld().isClient && instance.getEffectType() == EffectHandler.SUNBLOCK) {
            PacketByteBuf buf = PacketByteBufs.create();
            MessageSunblockEffect.serialize(new MessageSunblockEffect(entity, false), buf);
            ServerHelper.sendToAll(StaticVariables.SUNBLOCK_EFFECT, buf);
        }
        if (!entity.getWorld().isClient && instance.getEffectType() == EffectHandler.FROZEN) {
            PacketByteBuf buf = PacketByteBufs.create();
            MessageFreezeEffect.serialize(new MessageFreezeEffect(entity, false), buf);
            ServerHelper.sendToAll(StaticVariables.FREEZE_EFFECT, buf);
            FrozenCapability.IFrozenCapability frozenCapability = FrozenCapability.get(entity);
            if (frozenCapability != null)
                frozenCapability.onUnfreeze(entity);
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
            FrozenCapability.IFrozenCapability frozenCapability = FrozenCapability.get(livingEntity);
            if (frozenCapability != null) {
                frozenCapability.onUnfreeze(livingEntity);
            }
        }
        if (livingEntity instanceof PlayerEntity player) {
            PlayerCapability.IPlayerCapability playerCapability = PlayerCapability.get(player);
            if (playerCapability != null) {
                Power[] powers = playerCapability.getPowers();
                for (Power power : powers) {
                    power.onTakeDamage(livingEntity, source, damage);
                }
            }
        }

        LivingCapability.ILivingCapability capability = LivingCapability.get(livingEntity);
        if (capability != null) {
            capability.setLastDamage(damage);
        }
        return damage;
    }

    public static void onPlayerTick(PlayerEntity player) {
        PlayerCapability.IPlayerCapability playerCapability = PlayerCapability.get(player);
        if (playerCapability != null) {
            playerCapability.tick(player);

            Power[] powers = playerCapability.getPowers();
            for (Power power : powers) {
                power.tick(player);
            }
        }
    }

    public static void onPlaceBlock(ItemPlacementContext ctx, BlockPos blockPos, BlockState blockState) {
        PlayerEntity player = ctx.getPlayer();
        if (player.hasStatusEffect(EffectHandler.FROZEN))
            return;

        AbilityCapability.IAbilityCapability abilityCapability = AbilityCapability.get(player);
        if (abilityCapability != null && abilityCapability.blockBreakingBuildingPrevented())
            return;

        cheatSculptor(player);

        if (blockState.getBlock() == Blocks.FIRE ||
                blockState.getBlock() == Blocks.TNT ||
                blockState.getBlock() == Blocks.RESPAWN_ANCHOR ||
                blockState.getBlock() == Blocks.DISPENSER ||
                blockState.getBlock() == Blocks.CACTUS
        ) aggroUmvuthana(player);
    }

    public static boolean onFillBucket(World world, PlayerEntity user, Hand hand, ItemStack filledBucket) {
        if (user != null) {
            if (user.hasStatusEffect(EffectHandler.FROZEN))
                return true;
            AbilityCapability.IAbilityCapability abilityCapability = AbilityCapability.get(user);
            if (abilityCapability != null && abilityCapability.interactingPrevented())
                return true;
            if (filledBucket.isOf(Items.LAVA_BUCKET)) aggroUmvuthana(user);
            if (filledBucket.isOf(Items.WATER_BUCKET)) cheatSculptor(user);
        }
        return false;
    }

    public static void onBreakBlock(BlockEvents.BreakEvent event) {
        if (event.getPlayer().hasStatusEffect(EffectHandler.FROZEN)) {
            event.setCanceled(true);
            return;
        }

        PlayerEntity entity = event.getPlayer();
        AbilityCapability.IAbilityCapability abilityCapability = AbilityCapability.get(entity);
        if (abilityCapability != null && abilityCapability.blockBreakingBuildingPrevented()) {
            event.setCanceled(true);
            return;
        }

        cheatSculptor(event.getPlayer());

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
        ) aggroUmvuthana(event.getPlayer());
    }

    public static <T extends Entity> List<T> getEntitiesNearby(Entity startEntity, Class<T> entityClass, double r) {
        return startEntity.getWorld().getEntitiesByClass(entityClass, startEntity.getBoundingBox().expand(r, r, r), e -> e != startEntity && startEntity.distanceTo(e) <= r);
    }

    private static List<LivingEntity> getEntityBaseNearby(LivingEntity user, double distanceX, double distanceY,
                                                          double distanceZ, double radius) {
        List<Entity> list = user.getWorld().getOtherEntities(user, user.getBoundingBox().expand(distanceX, distanceY, distanceZ));
        ArrayList<LivingEntity> nearEntities = list.stream().filter(entityNeighbor -> entityNeighbor instanceof LivingEntity && user.distanceTo(entityNeighbor) <= radius).map(entityNeighbor -> (LivingEntity) entityNeighbor).collect(Collectors.toCollection(ArrayList::new));
        return nearEntities;
    }

//    @SubscribeEvent
//    public void onPlayerInteract(PlayerInteractEvent.RightClickEmpty event) {
//        if (event.isCancelable() && event.getEntity().hasEffect(EffectHandler.FROZEN.get())) {
//            event.setCanceled(true);
//            return;
//        }
//
//        AbilityCapability.IAbilityCapability abilityCapability = AbilityCapability.get(event.getEntity());
//        if (abilityCapability != null && event.isCancelable() && abilityCapability.interactingPrevented()) {
//            event.setCanceled(true);
//            return;
//        }
//
//        PlayerEntity player = event.getEntity();
//        PlayerCapability.IPlayerCapability playerCapability = PlayerCapability.get(player);
//        if (playerCapability != null) {
//
//            if (event.getLevel().isClientSide && player.getInventory().getMainHandStack().isEmpty() && player.hasStatusEffect(EffectHandler.SUNS_BLESSING.get())) {
//                if (player.isSneaking()) {
//                    AbilityHandler.INSTANCE.sendPlayerTryAbilityMessage(event.getEntity(), AbilityHandler.SOLAR_BEAM_ABILITY);
//                } else {
//                    AbilityHandler.INSTANCE.sendPlayerTryAbilityMessage(event.getEntity(), AbilityHandler.SUNSTRIKE_ABILITY);
//                }
//            }
//
//            Power[] powers = playerCapability.getPowers();
//            for (Power power : powers) {
//                power.onRightClickEmpty(event);
//            }
//        }
//    }

    public static ActionResult onPlayerInteractEntity(PlayerEntity player, World world, Hand hand, Entity entity, @Nullable EntityHitResult hitResult) {
        if (player.hasStatusEffect(EffectHandler.FROZEN))
            return ActionResult.FAIL;

        AbilityCapability.IAbilityCapability abilityCapability = AbilityCapability.get(player);
        if (abilityCapability != null && abilityCapability.interactingPrevented())
            return ActionResult.FAIL;

        PlayerCapability.IPlayerCapability playerCapability = PlayerCapability.get(player);
        if (playerCapability != null) {
            Power[] powers = playerCapability.getPowers();
            for (Power power : powers) {
                power.onRightClickEntity(player, world, hand, entity, hitResult);
            }
        }
        return ActionResult.PASS;
    }

    public static ActionResult onPlayerInteractBlock(PlayerEntity player, World world, Hand hand, BlockHitResult hitResult) {
        BlockPos pos = hitResult.getBlockPos();
        if (player.hasStatusEffect(EffectHandler.FROZEN))
            return ActionResult.FAIL;

        AbilityCapability.IAbilityCapability abilityCapability = AbilityCapability.get(player);
        if (abilityCapability != null && abilityCapability.interactingPrevented())
            return ActionResult.FAIL;

        if (player.getWorld().getBlockState(pos).getBlock() instanceof ChestBlock)
            aggroUmvuthana(player);

        PlayerCapability.IPlayerCapability playerCapability = PlayerCapability.get(player);
        if (playerCapability != null) {

            ItemStack item = player.getStackInHand(hand);
            if (item.getItem() == Items.FLINT_AND_STEEL ||
                    item.getItem() == Items.TNT_MINECART
            ) aggroUmvuthana(player);

            if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT && player.getInventory().getMainHandStack().isEmpty() && player.hasStatusEffect(EffectHandler.SUNS_BLESSING) && player.getWorld().getBlockState(pos).createScreenHandlerFactory(player.getWorld(), pos) == null) {
                if (player.isSneaking()) {
                    AbilityHandler.INSTANCE.sendPlayerTryAbilityMessage(player, AbilityHandler.SOLAR_BEAM_ABILITY);
                } else {
                    AbilityHandler.INSTANCE.sendPlayerTryAbilityMessage(player, AbilityHandler.SUNSTRIKE_ABILITY);
                }
            }
            if (player.getMainHandStack().isOf(ItemHandler.WROUGHT_AXE) && player.getWorld().getBlockState(pos).createScreenHandlerFactory(player.getWorld(), pos) != null) {
                player.resetLastAttackedTicks();
                return ActionResult.SUCCESS;
            }
            Power[] powers = playerCapability.getPowers();
            for (Power power : powers) {
                power.onRightClickBlock(player, world, hand, hitResult);
            }
        }
        return ActionResult.PASS;
    }

//    @SubscribeEvent
//    public void onPlayerLeftClick(PlayerInteractEvent.LeftClickEmpty event) {
//        double range = 6.5;
//        PlayerEntity player = event.getEntity();
//        PlayerCapability.IPlayerCapability playerCapability = PlayerCapability.get(player);
//        if (player.getMainHandStack() != null && player.getMainHandStack().getItem() == ItemHandler.SPEAR) {
//            LivingEntity entityHit = ItemSpear.raytraceEntities(player.getEntityWorld(), player, range);
//            if (entityHit != null) {
//                PacketByteBuf buf = PacketByteBufs.create();
//                MessagePlayerAttackMob.serialize(new MessagePlayerAttackMob(entityHit), buf);
//                ServerHelper.sendToAll(StaticVariables.PLAYER_ATTACK_MOB, buf);
//            }
//        }
//        if (playerCapability != null) {
//            Power[] powers = playerCapability.getPowers();
//            for (Power power : powers) {
//                power.onLeftClickEmpty(event);
//            }
//        }
//    }

    public static float onLivingDamage(LivingEntity entity, DamageSource source, float amount) {
        if (entity.getHealth() <= amount && entity.hasStatusEffect(EffectHandler.FROZEN)) {
            entity.removeStatusEffectInternal(EffectHandler.FROZEN);
            FrozenCapability.IFrozenCapability frozenCapability = FrozenCapability.get(entity);
            PacketByteBuf buf = PacketByteBufs.create();
            MessageFreezeEffect.serialize(new MessageFreezeEffect(entity, false), buf);
            ServerHelper.sendToAll(StaticVariables.FREEZE_EFFECT, buf);
            if (frozenCapability != null) {
                frozenCapability.onUnfreeze(entity);
            }
        }
        return amount;
    }

    public static TypedActionResult<ItemStack> onPlayerUseItem(PlayerEntity player, World world, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        if (player.hasStatusEffect(EffectHandler.FROZEN))
            return TypedActionResult.fail(stack);

        AbilityCapability.IAbilityCapability abilityCapability = AbilityCapability.get(player);
        if (abilityCapability != null && abilityCapability.itemUsePrevented(stack))
            return TypedActionResult.fail(stack);

        PlayerCapability.IPlayerCapability playerCapability = PlayerCapability.get(player);
        if (playerCapability != null) {
            Power[] powers = playerCapability.getPowers();
            for (Power power : powers) {
                power.onRightClickWithItem(player, world, hand);
            }
        }
        return TypedActionResult.pass(stack);
    }

    public static ActionResult onPlayerLeftClickBlock(PlayerEntity player, World world, Hand hand, BlockPos pos, Direction direction) {
        if (player.hasStatusEffect(EffectHandler.FROZEN))
            return ActionResult.FAIL;

        AbilityCapability.IAbilityCapability abilityCapability = AbilityCapability.get(player);
        if (abilityCapability != null && abilityCapability.blockBreakingBuildingPrevented())
            return ActionResult.FAIL;

        PlayerCapability.IPlayerCapability playerCapability = PlayerCapability.get(player);
        if (playerCapability != null) {
            Power[] powers = playerCapability.getPowers();
            for (Power power : powers) {
                power.onLeftClickBlock(player, world, hand, pos, direction);
            }
        }
        return ActionResult.PASS;
    }

    public static void onLivingJump(LivingEntityEvents.LivingJumpEvent event) {
        if (event.getEntity() instanceof LivingEntity) {
            LivingEntity entity = event.getEntity();
            if (entity.hasStatusEffect(EffectHandler.FROZEN) && entity.isOnGround()) {
                entity.setVelocity(entity.getVelocity().multiply(1, 0, 1));
            }
        }

        if (event.getEntity() instanceof PlayerEntity player) {
            PlayerCapability.IPlayerCapability playerCapability = PlayerCapability.get(player);
            if (playerCapability != null) {
                Power[] powers = playerCapability.getPowers();
                for (Power power : powers) {
                    power.onJump(event);
                }
            }
        }
    }

    public static ActionResult onPlayerAttack(PlayerEntity player, World world, Hand hand, Entity entity, @Nullable EntityHitResult hitResult) {
        if (player.hasStatusEffect(EffectHandler.FROZEN))
            return ActionResult.FAIL;

        AbilityCapability.IAbilityCapability abilityCapability = AbilityCapability.get(player);
        if (abilityCapability != null && abilityCapability.attackingPrevented())
            return ActionResult.FAIL;

        PlayerCapability.IPlayerCapability playerCapability = PlayerCapability.get(player);
        if (playerCapability != null) {
            playerCapability.setPrevCooledAttackStrength(player.getAttackCooldownProgress(0.5f));

            Power[] powers = playerCapability.getPowers();
            for (Power power : powers) {
                power.onLeftClickEntity(player, world, hand, entity, hitResult);
            }

            if (entity instanceof ItemFrameEntity itemFrame) {
                if (itemFrame.getHeldItemStack().getItem() instanceof ItemUmvuthanaMask) {
                    aggroUmvuthana(player);
                }
            }
            if (entity instanceof LeaderSunstrikeImmune)
                aggroUmvuthana(player);

            if (!(entity instanceof LivingEntity)) return ActionResult.PASS;
            if (entity instanceof EntityUmvuthanaFollowerToPlayer) return ActionResult.PASS;
            if (!player.getWorld().isClient) {
                for (int i = 0; i < playerCapability.getPackSize(); i++) {
                    EntityUmvuthanaFollowerToPlayer umvuthana = playerCapability.getUmvuthanaPack().get(i);
                    LivingEntity living = (LivingEntity) entity;
                    if (umvuthana.getMaskType() != MaskType.FAITH) {
                        if (!living.isInvulnerable()) umvuthana.setTarget(living);
                    }
                }
            }
        }
        return ActionResult.PASS;
    }

    public static void checkCritEvent(CriticalHitEvent event) {
        ItemStack weapon = event.getEntity().getMainHandStack();
        PlayerEntity attacker = event.getEntity();
        PlayerCapability.IPlayerCapability playerCapability = PlayerCapability.get(event.getEntity());
        if (playerCapability != null && playerCapability.getPrevCooledAttackStrength() == 1.0f && !weapon.isEmpty() && event.getTarget() instanceof LivingEntity target) {
            if (weapon.getItem() instanceof ItemNagaFangDagger) {
                Vec3d lookDir = new Vec3d(target.getRotationVector().x, 0, target.getRotationVector().z).normalize();
                Vec3d vecBetween = new Vec3d(target.getX() - event.getEntity().getX(), 0, target.getZ() - event.getEntity().getZ()).normalize();
                double dot = lookDir.dotProduct(vecBetween);
                if (dot > 0.7) {
                    event.setResult(BaseEvent.Result.ALLOW);
                    event.setDamageModifier((float) ConfigHandler.COMMON.TOOLS_AND_ABILITIES.NAGA_FANG_DAGGER.backstabDamageMultiplier);
                    target.playSound(MMSounds.ENTITY_NAGA_ACID_HIT, 1f, 1.2f);
                    AbilityHandler.INSTANCE.sendAbilityMessage(attacker, AbilityHandler.BACKSTAB_ABILITY);

                    if (target.getWorld().isClient()) {
                        Vec3d ringOffset = attacker.getRotationVector().multiply(-target.getWidth() / 2.f);
                        ParticleRotation.OrientVector rotation = new ParticleRotation.OrientVector(ringOffset);
                        Vec3d pos = target.getPos().add(0, target.getHeight() / 2f, 0).add(ringOffset);
                        AdvancedParticleBase.spawnParticle(target.getWorld(), ParticleHandler.RING_SPARKS, pos.getX(), pos.getY(), pos.getZ(), 0, 0, 0, rotation, 3.5F, 0.83f, 1, 0.39f, 1, 1, 6, false, true, new ParticleComponent[]{
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
                            AdvancedParticleBase.spawnParticle(target.getWorld(), ParticleHandler.PIXEL, pos.getX(), pos.getY(), pos.getZ(), particlePos.x * explodeSpeed, particlePos.y * explodeSpeed, particlePos.z * explodeSpeed, true, 0, 0, 0, 0, 3f, 0.07d + value, 0.25d + value, 0.07d + value, 1d, 0.6, life * 0.95, false, true);
                        }
                        for (int i = 0; i < 6; i++) {
                            Vec3d particlePos = new Vec3d(rand.nextFloat() * 0.25, 0, 0);
                            particlePos = particlePos.rotateY((float) (rand.nextFloat() * 2 * Math.PI));
                            particlePos = particlePos.rotateX((float) (rand.nextFloat() * 2 * Math.PI));
                            double value = rand.nextFloat() * 0.1f;
                            double life = rand.nextFloat() * 5f + 10f;
                            AdvancedParticleBase.spawnParticle(target.getWorld(), ParticleHandler.BUBBLE, pos.getX(), pos.getY(), pos.getZ(), particlePos.x * explodeSpeed, particlePos.y * explodeSpeed, particlePos.z * explodeSpeed, true, 0, 0, 0, 0, 3f, 0.25d + value, 0.75d + value, 0.25d + value, 1d, 0.6, life * 0.95, false, true);
                        }
                    }
                }
            } else if (weapon.getItem() instanceof ItemSpear) {
                if (target instanceof AnimalEntity && target.getMaxHealth() <= 30 && attacker.getWorld().getRandom().nextFloat() <= 0.334) {
                    event.setResult(BaseEvent.Result.ALLOW);
                    event.setDamageModifier(400);
                }
            }
        }
    }

    public static boolean onRideEntity(Entity vehicle, Entity passenger) {
        return !(vehicle instanceof EntityUmvuthi) && !(vehicle instanceof EntityFrostmaw) && !(vehicle instanceof EntityWroughtnaut);
    }

    public static void onPlayerRespawn(ServerPlayerEntity player) {
        List<MowzieEntity> mobs = getEntitiesNearby(player, MowzieEntity.class, 40);
        for (MowzieEntity mob : mobs)
            if (mob.resetHealthOnPlayerRespawn())
                mob.setHealth(mob.getMaxHealth());
    }

    private static void aggroUmvuthana(PlayerEntity player) {
        List<EntityUmvuthi> barakos = getEntitiesNearby(player, EntityUmvuthi.class, 50);
        for (EntityUmvuthi barako : barakos) {
            if (barako.getTarget() == null || !(barako.getTarget() instanceof PlayerEntity)) {
                if (!player.isCreative() && !player.isSpectator() && player.getBlockPos().getSquaredDistance(barako.getPositionTarget()) < 900) {
                    if (barako.canTarget(player)) barako.setMisbehavedPlayerId(player.getUuid());
                }
            }
        }
        List<EntityUmvuthanaMinion> barakoas = getEntitiesNearby(player, EntityUmvuthanaMinion.class, 50);
        for (EntityUmvuthanaMinion barakoa : barakoas) {
            if (barakoa.getTarget() == null || !(barakoa.getTarget() instanceof PlayerEntity)) {
                if (player.getBlockPos().getSquaredDistance(barakoa.getPositionTarget()) < 900) {
                    if (barakoa.canTarget(player)) barakoa.setMisbehavedPlayerId(player.getUuid());
                }
            }
        }
    }

    private static void cheatSculptor(PlayerEntity player) {
        List<EntitySculptor> sculptors = player.getWorld().getEntitiesByClass(EntitySculptor.class, player.getBoundingBox().expand(EntitySculptor.TEST_RADIUS + 3, EntitySculptor.TEST_HEIGHT, EntitySculptor.TEST_RADIUS + 3), EntitySculptor::isTesting);
        for (EntitySculptor sculptor : sculptors) {
            sculptor.playerCheated();
        }
    }
}
