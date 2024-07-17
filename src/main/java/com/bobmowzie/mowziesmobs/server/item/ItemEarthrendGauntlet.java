package com.bobmowzie.mowziesmobs.server.item;

import com.bobmowzie.mowziesmobs.client.render.item.RenderEarthrendGauntlet;
import com.bobmowzie.mowziesmobs.server.ability.AbilityHandler;
import com.bobmowzie.mowziesmobs.server.capability.AbilityCapability;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterials;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by BobMowzie on 6/6/2017.
 */
public class ItemEarthrendGauntlet extends MowzieToolItem implements GeoItem {
    public static final String CONTROLLER_NAME = "controller";
    public static final String CONTROLLER_IDLE_NAME = "controller_idle";
    public static final String IDLE_ANIM_NAME = "idle";
    public static final String OPEN_ANIM_NAME = "open";
    public static final String ATTACK_ANIM_NAME = "attack";
    private static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("idle");
    private static final RawAnimation OPEN_ANIM = RawAnimation.begin().thenLoop("open");
    private static final RawAnimation ATTACK_ANIM = RawAnimation.begin().thenPlay("attack");
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public ItemEarthrendGauntlet(Settings properties) {
        super(-2 + ConfigHandler.COMMON.TOOLS_AND_ABILITIES.EARTHREND_GAUNTLET.toolConfig.attackDamageValue, -4f + ConfigHandler.COMMON.TOOLS_AND_ABILITIES.EARTHREND_GAUNTLET.toolConfig.attackSpeedValue, ToolMaterials.STONE, BlockTags.PICKAXE_MINEABLE, properties);

        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        super.initializeClient(consumer);
        consumer.accept(new IClientItemExtensions() {
            private final BuiltinModelItemRenderer renderer = new RenderEarthrendGauntlet();

            @Override
            public BuiltinModelItemRenderer getCustomRenderer() {
                return this.renderer;
            }
        });
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
            if (stack.getDamage() + 5 < stack.getMaxDamage() || ConfigHandler.COMMON.TOOLS_AND_ABILITIES.EARTHREND_GAUNTLET.breakable.get()) {
                if (!worldIn.isClient())
                    AbilityHandler.INSTANCE.sendAbilityMessage(playerIn, AbilityHandler.TUNNELING_ABILITY);
                playerIn.setCurrentHand(handIn);
                return new TypedActionResult<ItemStack>(ActionResult.SUCCESS, playerIn.getStackInHand(handIn));
            } else {
                abilityCapability.getAbilityMap().get(AbilityHandler.TUNNELING_ABILITY).end();
            }
        }
        return super.use(worldIn, playerIn, handIn);
    }

    public int getMaxUseTime(ItemStack stack) {
        return 72000;
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return ConfigHandler.COMMON.TOOLS_AND_ABILITIES.EARTHREND_GAUNTLET.durability.get();
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World worldIn, List<Text> tooltip, TooltipContext flagIn) {
        super.appendTooltip(stack, worldIn, tooltip, flagIn);
        tooltip.add(Text.translatable(this.getTranslationKey() + ".text.0").setStyle(ItemHandler.TOOLTIP_STYLE));
        tooltip.add(Text.translatable(this.getTranslationKey() + ".text.1").setStyle(ItemHandler.TOOLTIP_STYLE));
        if (!ConfigHandler.COMMON.TOOLS_AND_ABILITIES.EARTHREND_GAUNTLET.breakable.get()) {
            tooltip.add(Text.translatable(this.getTranslationKey() + ".text.2").setStyle(ItemHandler.TOOLTIP_STYLE));
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, CONTROLLER_IDLE_NAME, 3, this::predicateIdle));
        controllers.add(new AnimationController<>(this, CONTROLLER_NAME, 3, state -> PlayState.STOP)
                .triggerableAnim(IDLE_ANIM_NAME, IDLE_ANIM)
                .triggerableAnim(OPEN_ANIM_NAME, OPEN_ANIM)
                .triggerableAnim(ATTACK_ANIM_NAME, ATTACK_ANIM));
    }

    public <P extends Item & GeoItem> PlayState predicateIdle(AnimationState<P> event) {
        event.getController().setAnimation(IDLE_ANIM);
        return PlayState.CONTINUE;
    }

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
        AbilityCapability.IAbilityCapability abilityCapability = AbilityHandler.INSTANCE.getAbilityCapability(entity);
        if (abilityCapability != null && abilityCapability.getActiveAbility() == null) {
            if (entity.getActiveItem() != stack) {
                if (entity.getWorld() instanceof ServerWorld) {
                    this.triggerAnim(entity, GeoItem.getOrAssignId(stack, (ServerWorld) entity.getWorld()), CONTROLLER_NAME, ATTACK_ANIM_NAME);
                }
            }
        }
        return super.onEntitySwing(stack, entity);
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, PlayerEntity player, Entity entity) {
        if (player.getActiveItem() != stack) {
            if (entity.getWorld() instanceof ServerWorld) {
                this.triggerAnim(entity, GeoItem.getOrAssignId(stack, (ServerWorld) entity.getWorld()), CONTROLLER_NAME, ATTACK_ANIM_NAME);
            }
        }
        return super.onLeftClickEntity(stack, player, entity);
    }

    @Override
    public ConfigHandler.ToolConfig getConfig() {
        return ConfigHandler.COMMON.TOOLS_AND_ABILITIES.EARTHREND_GAUNTLET.toolConfig;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}
