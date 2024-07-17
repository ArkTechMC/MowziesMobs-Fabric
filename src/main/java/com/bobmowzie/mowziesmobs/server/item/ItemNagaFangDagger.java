package com.bobmowzie.mowziesmobs.server.item;

import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterials;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemNagaFangDagger extends MowzieToolItem {
    public ItemNagaFangDagger(Settings properties) {
        super(-2 + ConfigHandler.COMMON.TOOLS_AND_ABILITIES.NAGA_FANG_DAGGER.toolConfig.attackDamageValue, -4f + ConfigHandler.COMMON.TOOLS_AND_ABILITIES.NAGA_FANG_DAGGER.toolConfig.attackSpeedValue, ToolMaterials.STONE, BlockTags.HOE_MINEABLE, properties);
    }

    public boolean canMine(BlockState state, World level, BlockPos pos, PlayerEntity player) {
        return !player.isCreative();
    }

//    @Override
//    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
//        if (enchantment == Enchantments.SWEEPING) return false;
//        return enchantment.target == EnchantmentTarget.WEAPON || enchantment.target == EnchantmentTarget.BREAKABLE;
//    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (super.postHit(stack, target, attacker)) {
            target.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, ConfigHandler.COMMON.TOOLS_AND_ABILITIES.NAGA_FANG_DAGGER.poisonDuration, 3, false, true));
            return true;
        }
        return false;
    }

    @Override
    public boolean canRepair(ItemStack toRepair, ItemStack repair) {
        Item item = repair.getItem();
        return item instanceof ItemNagaFang;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World worldIn, List<Text> tooltip, TooltipContext flagIn) {
        super.appendTooltip(stack, worldIn, tooltip, flagIn);
        tooltip.add(Text.translatable(this.getTranslationKey() + ".text.0").setStyle(ItemHandler.TOOLTIP_STYLE));
        tooltip.add(Text.translatable(this.getTranslationKey() + ".text.1").setStyle(ItemHandler.TOOLTIP_STYLE));
    }

    @Override
    public ConfigHandler.ToolConfig getConfig() {
        return ConfigHandler.COMMON.TOOLS_AND_ABILITIES.NAGA_FANG_DAGGER.toolConfig;
    }
}
