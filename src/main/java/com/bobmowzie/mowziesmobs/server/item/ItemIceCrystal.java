package com.bobmowzie.mowziesmobs.server.item;

import com.bobmowzie.mowziesmobs.server.ability.AbilityHandler;
import com.bobmowzie.mowziesmobs.server.capability.AbilityCapability;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Created by BobMowzie on 6/6/2017.
 */
public class ItemIceCrystal extends Item {
    public ItemIceCrystal(Settings properties) {
        super(properties.maxCount(ConfigHandler.COMMON.TOOLS_AND_ABILITIES.ICE_CRYSTAL.durability));
    }

    @Override
    public boolean isDamageable() {
        return true;
    }

    @Override
    public TypedActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack stack = playerIn.getStackInHand(handIn);
        AbilityCapability.IAbilityCapability abilityCapability = AbilityHandler.INSTANCE.getAbilityCapability(playerIn);
        if (abilityCapability != null) {
            playerIn.setCurrentHand(handIn);
            if (stack.getDamage() + 5 < stack.getMaxDamage() || ConfigHandler.COMMON.TOOLS_AND_ABILITIES.ICE_CRYSTAL.breakable) {
                if (!worldIn.isClient())
                    AbilityHandler.INSTANCE.sendAbilityMessage(playerIn, AbilityHandler.ICE_BREATH_ABILITY);
                stack.damage(5, playerIn, p -> p.sendToolBreakStatus(handIn));
                playerIn.setCurrentHand(handIn);
                return new TypedActionResult<ItemStack>(ActionResult.SUCCESS, playerIn.getStackInHand(handIn));
            } else {
                abilityCapability.getAbilityMap().get(AbilityHandler.ICE_BREATH_ABILITY).end();
            }
        }
        return super.use(worldIn, playerIn, handIn);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft) {
//        if (entityLiving instanceof Player) {
//            Ability iceBreathAbility = AbilityHandler.INSTANCE.getAbility(entityLiving, AbilityHandler.ICE_BREATH_ABILITY);
//            if (iceBreathAbility != null && iceBreathAbility.isUsing()) {
//                iceBreathAbility.end();
//            }
//        }
        super.onStoppedUsing(stack, worldIn, entityLiving, timeLeft);
    }

    public int getMaxUseTime(ItemStack stack) {
        return 72000;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World worldIn, List<Text> tooltip, TooltipContext flagIn) {
        super.appendTooltip(stack, worldIn, tooltip, flagIn);
        tooltip.add(Text.translatable(this.getTranslationKey() + ".text.0").setStyle(ItemHandler.TOOLTIP_STYLE));
        if (!ConfigHandler.COMMON.TOOLS_AND_ABILITIES.ICE_CRYSTAL.breakable) {
            tooltip.add(Text.translatable(this.getTranslationKey() + ".text.1").setStyle(ItemHandler.TOOLTIP_STYLE));
        }
    }
}
