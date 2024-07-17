package com.bobmowzie.mowziesmobs.server.item;

import com.bobmowzie.mowziesmobs.server.entity.EntityDart;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemDart extends ArrowItem {
    public ItemDart(Settings properties) {
        super(properties);
    }

    @Override
    public PersistentProjectileEntity createArrow(World world, ItemStack stack, LivingEntity shooter) {
        return new EntityDart(EntityHandler.DART, world, shooter);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World worldIn, List<Text> tooltip, TooltipContext flagIn) {
        super.appendTooltip(stack, worldIn, tooltip, flagIn);
        tooltip.add(Text.translatable(this.getTranslationKey() + ".text.0").setStyle(ItemHandler.TOOLTIP_STYLE));
        tooltip.add(Text.translatable(this.getTranslationKey() + ".text.1").setStyle(ItemHandler.TOOLTIP_STYLE));
    }
}
