package com.bobmowzie.mowziesmobs.server.potion;

import com.bobmowzie.mowziesmobs.server.block.ICopiedBlockProperties;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import com.bobmowzie.mowziesmobs.server.tag.TagHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectCategory;

public class EffectGeomancy extends MowzieEffect {
    public EffectGeomancy() {
        super(StatusEffectCategory.BENEFICIAL, 0xCDFF78);
    }

    public static boolean isBlockUseable(BlockState blockState) {
        if (blockState.isIn(TagHandler.GEOMANCY_USEABLE)) return true;

        ICopiedBlockProperties properties = (ICopiedBlockProperties) blockState.getBlock().settings;
        Block baseBlock = properties.getBaseBlock();
        if (baseBlock != null) {
            return baseBlock.getDefaultState().isIn(TagHandler.GEOMANCY_USEABLE);
        }

        return false;
    }

    public static boolean canUse(LivingEntity entity) {
        return (entity.getMainHandStack().isOf(ItemHandler.EARTHREND_GAUNTLET) ||
                entity.getMainHandStack().isEmpty() ||
                entity.getOffHandStack().isOf(ItemHandler.EARTHREND_GAUNTLET))
                && entity.hasStatusEffect(EffectHandler.GEOMANCY);
    }
}
