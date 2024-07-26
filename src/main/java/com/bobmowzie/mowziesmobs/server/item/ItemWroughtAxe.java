package com.bobmowzie.mowziesmobs.server.item;

import com.bobmowzie.mowziesmobs.server.ability.AbilityHandler;
import com.bobmowzie.mowziesmobs.server.capability.AbilityCapability;
import com.bobmowzie.mowziesmobs.server.capability.PlayerCapability;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.iafenvoy.uranus.object.item.ISwingable;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterials;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemWroughtAxe extends MowzieAxeItem implements ISwingable {
    public ItemWroughtAxe(Settings properties) {
        super(ToolMaterials.IRON, -3 + ConfigHandler.COMMON.TOOLS_AND_ABILITIES.AXE_OF_A_THOUSAND_METALS.toolConfig.attackDamageValue, -4f + ConfigHandler.COMMON.TOOLS_AND_ABILITIES.AXE_OF_A_THOUSAND_METALS.toolConfig.attackSpeedValue, properties);
    }

    @Override
    public boolean canRepair(ItemStack itemStack, ItemStack itemStackMaterial) {
        return ConfigHandler.COMMON.TOOLS_AND_ABILITIES.AXE_OF_A_THOUSAND_METALS.breakable && super.canRepair(itemStack, itemStackMaterial);
    }

    @Override
    public boolean isEnchantable(ItemStack p_77616_1_) {
        return true;
    }

    @Override
    public boolean onEntitySwing(ItemStack itemStack, Entity entity) {
        PlayerCapability.IPlayerCapability playerCapability = AbilityCapability.get((PlayerEntity) entity);
        return playerCapability != null && playerCapability.getUntilAxeSwing() > 0;
    }

    @Override
    public boolean onSwingHand(ItemStack itemStack, World world, double v, double v1, double v2) {
        return false;
    }

    @Override
    public boolean postHit(ItemStack heldItemStack, LivingEntity entityHit, LivingEntity attacker) {
        if (ConfigHandler.COMMON.TOOLS_AND_ABILITIES.AXE_OF_A_THOUSAND_METALS.breakable)
            heldItemStack.damage(2, attacker, p -> p.sendToolBreakStatus(Hand.MAIN_HAND));
        if (!entityHit.getWorld().isClient) {
            entityHit.playSound(SoundEvents.BLOCK_ANVIL_LAND, 0.3F, 0.5F);
        }
        PlayerCapability.IPlayerCapability playerCapability = AbilityCapability.get((PlayerEntity) attacker);
        return playerCapability == null || (!playerCapability.getAxeCanAttack() && playerCapability.getUntilAxeSwing() > 0);
    }

    @Override
    public boolean isDamageable() {
        return ConfigHandler.COMMON.TOOLS_AND_ABILITIES.AXE_OF_A_THOUSAND_METALS.breakable;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        if (hand == Hand.MAIN_HAND && player.getAttackCooldownProgress(0.5F) == 1.0f) {
            PlayerCapability.IPlayerCapability playerCapability = PlayerCapability.get(player);
            if (playerCapability != null && playerCapability.getUntilAxeSwing() <= 0) {
                boolean verticalAttack = player.isSneaking() && player.isOnGround();
                if (verticalAttack)
                    AbilityHandler.INSTANCE.sendAbilityMessage(player, AbilityHandler.WROUGHT_AXE_SLAM_ABILITY);
                else
                    AbilityHandler.INSTANCE.sendAbilityMessage(player, AbilityHandler.WROUGHT_AXE_SWING_ABILITY);
                playerCapability.setVerticalSwing(verticalAttack);
                playerCapability.setUntilAxeSwing(30);
                player.setCurrentHand(hand);
                if (ConfigHandler.COMMON.TOOLS_AND_ABILITIES.AXE_OF_A_THOUSAND_METALS.breakable && !player.getAbilities().creativeMode)
                    player.getStackInHand(hand).damage(2, player, p -> p.sendToolBreakStatus(hand));
            }
            return new TypedActionResult<ItemStack>(ActionResult.SUCCESS, player.getStackInHand(hand));
        }
        return super.use(world, player, hand);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World worldIn, List<Text> tooltip, TooltipContext flagIn) {
        super.appendTooltip(stack, worldIn, tooltip, flagIn);
        tooltip.add(Text.translatable(this.getTranslationKey() + ".text.0").setStyle(ItemHandler.TOOLTIP_STYLE));
        tooltip.add(Text.translatable(this.getTranslationKey() + ".text.1").setStyle(ItemHandler.TOOLTIP_STYLE));
        tooltip.add(Text.translatable(this.getTranslationKey() + ".text.2").setStyle(ItemHandler.TOOLTIP_STYLE));
    }

    @Override
    public ConfigHandler.ToolConfig getConfig() {
        return ConfigHandler.COMMON.TOOLS_AND_ABILITIES.AXE_OF_A_THOUSAND_METALS.toolConfig;
    }
}
