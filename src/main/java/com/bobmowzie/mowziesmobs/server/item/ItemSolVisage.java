package com.bobmowzie.mowziesmobs.server.item;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.iafenvoy.uranus.client.render.armor.IArmorTextureProvider;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ArmorMaterials;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Created by BobMowzie on 8/15/2016.
 */
public class ItemSolVisage extends MowzieArmorItem implements UmvuthanaMask, GeoItem, IArmorTextureProvider {
    private static final SolVisageMaterial SOL_VISAGE_MATERIAL = new SolVisageMaterial();
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public String controllerName = "controller";

    public ItemSolVisage(Settings properties) {
        super(SOL_VISAGE_MATERIAL, Type.HELMET, properties);
    }

    @Override
    public boolean canRepair(ItemStack toRepair, ItemStack repair) {
        if (ConfigHandler.COMMON.TOOLS_AND_ABILITIES.SOL_VISAGE.breakable)
            return super.canRepair(toRepair, repair);
        return false;
    }

    @Override
    public boolean isEnchantable(ItemStack p_77616_1_) {
        return true;
    }

    //FIXME: No API found
//    @Override
//    public boolean makesPiglinsNeutral(ItemStack stack, LivingEntity wearer) {
//        return true;
//    }

    @Override
    public boolean isDamageable() {
        return ConfigHandler.COMMON.TOOLS_AND_ABILITIES.SOL_VISAGE.breakable;
    }

    @Override
    public int getMaxDamage() {
        return ConfigHandler.COMMON.TOOLS_AND_ABILITIES.SOL_VISAGE.breakable ? super.getMaxDamage() : 0;
    }

    @Nullable
    @Override
    public Identifier getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return new Identifier(MowziesMobs.MODID, "textures/entity/umvuthi.png");
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World worldIn, List<Text> tooltip, TooltipContext flagIn) {
        super.appendTooltip(stack, worldIn, tooltip, flagIn);
        tooltip.add(Text.translatable(this.getTranslationKey() + ".text.0").setStyle(ItemHandler.TOOLTIP_STYLE));
        tooltip.add(Text.translatable(this.getTranslationKey() + ".text.1").setStyle(ItemHandler.TOOLTIP_STYLE));
        tooltip.add(Text.translatable(this.getTranslationKey() + ".text.2").setStyle(ItemHandler.TOOLTIP_STYLE));
    }

    @Override
    public ConfigHandler.ArmorConfig getConfig() {
        return ConfigHandler.COMMON.TOOLS_AND_ABILITIES.SOL_VISAGE.armorConfig;
    }

    private PlayState predicate(AnimationState<ItemSolVisage> state) {
        return PlayState.STOP;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, this.controllerName, 0, this::predicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public void createRenderer(Consumer<Object> consumer) {
        //FIXME:wtf is this???
    }

    @Override
    public Supplier<Object> getRenderProvider() {
        return null;
    }

    private static class SolVisageMaterial implements ArmorMaterial {

        @Override
        public int getDurability(Type equipmentSlotType) {
            return ArmorMaterials.GOLD.getDurability(equipmentSlotType);
        }

        @Override
        public int getProtection(Type equipmentSlotType) {
            return ConfigHandler.COMMON.TOOLS_AND_ABILITIES.SOL_VISAGE.armorConfig.damageReductionValue;
        }

        @Override
        public int getEnchantability() {
            return ArmorMaterials.GOLD.getEnchantability();
        }

        @Override
        public SoundEvent getEquipSound() {
            return ArmorMaterials.GOLD.getEquipSound();
        }

        @Override
        public Ingredient getRepairIngredient() {
            return ArmorMaterials.GOLD.getRepairIngredient();
        }

        @Override
        public String getName() {
            return "sol_visage";
        }

        @Override
        public float getToughness() {
            return ConfigHandler.COMMON.TOOLS_AND_ABILITIES.SOL_VISAGE.armorConfig.toughnessValue;
        }

        @Override
        public float getKnockbackResistance() {
            return ArmorMaterials.GOLD.getKnockbackResistance();
        }
    }
}
