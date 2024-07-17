package com.bobmowzie.mowziesmobs.server.item;

import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Created by BobMowzie on 1/10/2019.
 */
public class ItemNagaFang extends Item {
    public ItemNagaFang(Settings properties) {
        super(properties);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World worldIn, List<Text> tooltip, TooltipContext flagIn) {
        super.appendTooltip(stack, worldIn, tooltip, flagIn);
        tooltip.add(Text.translatable(this.getTranslationKey() + ".text.0").setStyle(ItemHandler.TOOLTIP_STYLE));
    }

    public boolean canMine(BlockState p_43291_, World p_43292_, BlockPos p_43293_, PlayerEntity p_43294_) {
        return !p_43294_.isCreative();
    }
}
