package com.bobmowzie.mowziesmobs.server.item;

import com.bobmowzie.mowziesmobs.client.render.item.RenderSculptorStaff;
import com.bobmowzie.mowziesmobs.server.ability.AbilityHandler;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterials;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by BobMowzie on 6/6/2017.
 */
public class ItemSculptorStaff extends MowzieToolItem implements GeoItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public String controllerName = "controller";

    public ItemSculptorStaff(Settings properties) {
        super(1f, 2f, ToolMaterials.STONE, BlockTags.HOE_MINEABLE, properties);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        super.initializeClient(consumer);
        consumer.accept(new IClientItemExtensions() {
            private final BuiltinModelItemRenderer renderer = new RenderSculptorStaff();

            @Override
            public BuiltinModelItemRenderer getCustomRenderer() {
                return this.renderer;
            }
        });
    }

    @Override
    public TypedActionResult<ItemStack> use(World level, PlayerEntity player, Hand hand) {
        AbilityHandler.INSTANCE.sendAbilityMessage(player, AbilityHandler.ROCK_SLING);
        player.setCurrentHand(hand);
        return new TypedActionResult<ItemStack>(ActionResult.SUCCESS, player.getStackInHand(hand));
    }

    @Override
    public boolean isDamageable() {
        return true;
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
    }

    @Override
    public ConfigHandler.ToolConfig getConfig() {
        return ConfigHandler.COMMON.TOOLS_AND_ABILITIES.EARTHREND_GAUNTLET.toolConfig;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}
