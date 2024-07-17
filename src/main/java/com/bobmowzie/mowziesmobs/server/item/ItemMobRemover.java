package com.bobmowzie.mowziesmobs.server.item;

import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemMobRemover extends Item {
    public ItemMobRemover(Settings properties) {
        super(properties);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (target instanceof MowzieEntity) {
            if (!target.getWorld().isClient)
                target.discard();
            return true;
        }
        return false;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World worldIn, List<Text> tooltip, TooltipContext flagIn) {
        super.appendTooltip(stack, worldIn, tooltip, flagIn);
        tooltip.add(Text.translatable(this.getTranslationKey() + ".text.0").setStyle(ItemHandler.TOOLTIP_STYLE));
    }
}