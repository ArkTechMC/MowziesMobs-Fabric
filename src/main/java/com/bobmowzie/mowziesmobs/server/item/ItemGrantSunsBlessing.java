package com.bobmowzie.mowziesmobs.server.item;

import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.potion.EffectHandler;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Created by BobMowzie on 10/31/2016.
 */
public class ItemGrantSunsBlessing extends Item {
    public ItemGrantSunsBlessing(Settings properties) {
        super(properties);
    }

    @Override
    public TypedActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand hand) {
        playerIn.addStatusEffect(new StatusEffectInstance(EffectHandler.SUNS_BLESSING, -1, 0, false, false));
        return super.use(worldIn, playerIn, hand);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World worldIn, List<Text> tooltip, TooltipContext flagIn) {
        super.appendTooltip(stack, worldIn, tooltip, flagIn);
        int effectDuration = ConfigHandler.COMMON.TOOLS_AND_ABILITIES.SUNS_BLESSING.effectDuration;
        int solarBeamCost = ConfigHandler.COMMON.TOOLS_AND_ABILITIES.SUNS_BLESSING.solarBeamCost;
        int supernovaCost = ConfigHandler.COMMON.TOOLS_AND_ABILITIES.SUNS_BLESSING.supernovaCost;
        tooltip.add(
                Text.translatable(this.getTranslationKey() + ".text.0")
                        .append(" " + effectDuration + " ")
                        .append(Text.translatable(this.getTranslationKey() + ".text.1")).setStyle(ItemHandler.TOOLTIP_STYLE)
        );
        tooltip.add(Text.translatable(this.getTranslationKey() + ".text.2").setStyle(ItemHandler.TOOLTIP_STYLE));

        tooltip.add(Text.translatable(this.getTranslationKey() + ".text.3").setStyle(ItemHandler.TOOLTIP_STYLE));
        tooltip.add(Text.translatable(this.getTranslationKey() + ".text.4").setStyle(ItemHandler.TOOLTIP_STYLE));
        tooltip.add(
                Text.translatable(this.getTranslationKey() + ".text.5")
                        .append(" " + solarBeamCost + " ")
                        .append(Text.translatable(this.getTranslationKey() + ".text.6")).setStyle(ItemHandler.TOOLTIP_STYLE)
        );
        MutableText supernovaComponent = Text.translatable(this.getTranslationKey() + ".text.7");
        if (supernovaCost >= effectDuration) {
            supernovaComponent.append(Text.translatable(this.getTranslationKey() + ".text.8"));
        } else {
            supernovaComponent.append(" " + supernovaCost + " minutes");
        }
        supernovaComponent.append(Text.translatable(this.getTranslationKey() + ".text.9"));
        supernovaComponent.fillStyle(ItemHandler.TOOLTIP_STYLE);
        tooltip.add(supernovaComponent);
    }
}
