package com.bobmowzie.mowziesmobs.server.ability;

import com.bobmowzie.mowziesmobs.event.UseEmptyCallback;
import com.bobmowzie.mowziesmobs.server.capability.AbilityCapability;
import com.iafenvoy.uranus.event.LivingEntityEvents;
import io.github.fabricators_of_create.porting_lib.entity.events.PlayerInteractionEvents;
import net.fabricmc.fabric.api.event.player.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class AbilityCommonEventHandler {
    public static void register() {
        UseBlockCallback.EVENT.register(AbilityCommonEventHandler::onPlayerRightClickBlock);
        UseItemCallback.EVENT.register(AbilityCommonEventHandler::onPlayerRightClickItem);
        UseEntityCallback.EVENT.register(AbilityCommonEventHandler::onPlayerRightClickEntity);
        PlayerInteractionEvents.LEFT_CLICK_EMPTY.register(AbilityCommonEventHandler::onPlayerLeftClickEmpty);
        AttackBlockCallback.EVENT.register(AbilityCommonEventHandler::onPlayerLeftClickBlock);
        AttackEntityCallback.EVENT.register(AbilityCommonEventHandler::onLeftClickEntity);
        LivingEntityEvents.DAMAGE.register(AbilityCommonEventHandler::onTakeDamage);
        io.github.fabricators_of_create.porting_lib.entity.events.LivingEntityEvents.LivingJumpEvent.JUMP.register(AbilityCommonEventHandler::onJump);
        UseEmptyCallback.EVENT.register(AbilityCommonEventHandler::onPlayerInteract);
    }

    public static void onPlayerInteract(PlayerEntity player, Hand hand) {
        AbilityCapability.IAbilityCapability abilityCapability = AbilityCapability.get(player);
        if (abilityCapability != null)
            for (Ability<?> ability : abilityCapability.getAbilities())
                if (ability instanceof PlayerAbility playerAbility)
                    playerAbility.onRightClickEmpty(player, hand);
    }

    public static ActionResult onPlayerRightClickBlock(PlayerEntity player, World world, Hand hand, BlockHitResult hitResult) {
        AbilityCapability.IAbilityCapability abilityCapability = AbilityCapability.get(player);
        if (abilityCapability != null)
            for (Ability<?> ability : abilityCapability.getAbilities())
                if (ability instanceof PlayerAbility playerAbility)
                    playerAbility.onRightClickBlock(player, world, hand, hitResult);
        return ActionResult.PASS;
    }

    public static TypedActionResult<ItemStack> onPlayerRightClickItem(PlayerEntity player, World world, Hand hand) {
        AbilityCapability.IAbilityCapability abilityCapability = AbilityCapability.get(player);
        if (abilityCapability != null)
            for (Ability<?> ability : abilityCapability.getAbilities())
                if (ability instanceof PlayerAbility playerAbility)
                    playerAbility.onRightClickWithItem(player, world, hand);
        return TypedActionResult.pass(player.getStackInHand(hand));
    }

    public static ActionResult onPlayerRightClickEntity(PlayerEntity player, World world, Hand hand, Entity entity, @Nullable EntityHitResult hitResult) {
        AbilityCapability.IAbilityCapability abilityCapability = AbilityCapability.get(player);
        if (abilityCapability != null)
            for (Ability<?> ability : abilityCapability.getAbilities())
                if (ability instanceof PlayerAbility playerAbility)
                    playerAbility.onRightClickEntity(player, world, hand, entity, hitResult);
        return ActionResult.PASS;
    }

    public static void onPlayerLeftClickEmpty(PlayerInteractionEvents.LeftClickEmpty event) {
        PlayerEntity player = event.getEntity();
        AbilityCapability.IAbilityCapability abilityCapability = AbilityCapability.get(player);
        if (abilityCapability != null)
            for (Ability<?> ability : abilityCapability.getAbilities())
                if (ability instanceof PlayerAbility playerAbility)
                    playerAbility.onLeftClickEmpty(event);
    }

    public static ActionResult onPlayerLeftClickBlock(PlayerEntity player, World world, Hand hand, BlockPos pos, Direction direction) {
        AbilityCapability.IAbilityCapability abilityCapability = AbilityCapability.get(player);
        if (abilityCapability != null)
            for (Ability<?> ability : abilityCapability.getAbilities())
                if (ability instanceof PlayerAbility playerAbility)
                    playerAbility.onLeftClickBlock(player, world, hand, pos, direction);
        return ActionResult.PASS;
    }

    public static ActionResult onLeftClickEntity(PlayerEntity player, World world, Hand hand, Entity entity, @Nullable EntityHitResult hitResult) {
        AbilityCapability.IAbilityCapability abilityCapability = AbilityCapability.get(player);
        if (abilityCapability != null)
            for (Ability<?> ability : abilityCapability.getAbilities())
                if (ability instanceof PlayerAbility playerAbility)
                    playerAbility.onLeftClickEntity(player, world, hand, entity, hitResult);
        return ActionResult.PASS;
    }

    public static float onTakeDamage(LivingEntity entity, DamageSource source, float amount) {
        AbilityCapability.IAbilityCapability abilityCapability = AbilityCapability.get(entity);
        if (abilityCapability != null)
            for (Ability<?> ability : abilityCapability.getAbilities())
                ability.onTakeDamage(entity, source, amount);
        return amount;
    }

    public static void onJump(io.github.fabricators_of_create.porting_lib.entity.events.LivingEntityEvents.LivingJumpEvent event) {
        LivingEntity player = event.getEntity();
        AbilityCapability.IAbilityCapability abilityCapability = AbilityCapability.get(player);
        if (abilityCapability != null)
            for (Ability<?> ability : abilityCapability.getAbilities())
                if (ability instanceof PlayerAbility playerAbility)
                    playerAbility.onJump(event);
    }
}
