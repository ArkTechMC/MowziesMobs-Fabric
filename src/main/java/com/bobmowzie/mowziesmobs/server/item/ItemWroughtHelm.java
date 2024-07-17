package com.bobmowzie.mowziesmobs.server.item;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.LayerHandler;
import com.bobmowzie.mowziesmobs.client.model.armor.WroughtHelmModel;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.iafenvoy.uranus.client.render.armor.IArmorTextureProvider;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ArmorMaterials;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.minecraft.world.item.*;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

public class ItemWroughtHelm extends MowzieArmorItem implements IArmorTextureProvider {
    private static final WroughtHelmMaterial ARMOR_WROUGHT_HELM = new WroughtHelmMaterial();

    public ItemWroughtHelm(Settings properties) {
        super(ARMOR_WROUGHT_HELM, Type.HELMET, properties);
    }

    @Override
    public boolean canRepair(ItemStack toRepair, ItemStack repair) {
        if (ConfigHandler.COMMON.TOOLS_AND_ABILITIES.WROUGHT_HELM.breakable)
            return super.canRepair(toRepair, repair);
        return false;
    }

    @Override
    public boolean isEnchantable(ItemStack p_77616_1_) {
        return true;
    }

    @Override
    public boolean isDamageable() {
        return ConfigHandler.COMMON.TOOLS_AND_ABILITIES.WROUGHT_HELM.breakable;
    }

    @Override
    public int getDamage(ItemStack stack) {
        return ConfigHandler.COMMON.TOOLS_AND_ABILITIES.WROUGHT_HELM.breakable ? super.getDamage(stack) : 0;
    }

    @Override
    public int getMaxDamage() {
        return ConfigHandler.COMMON.TOOLS_AND_ABILITIES.WROUGHT_HELM.breakable ? super.getMaxDamage() : 0;
    }

    @Override
    public void setDamage(ItemStack stack, int damage) {
        if (ConfigHandler.COMMON.TOOLS_AND_ABILITIES.WROUGHT_HELM.breakable) super.setDamage(stack, damage);
    }

    @Nullable
    @Override
    public Identifier getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return new Identifier(MowziesMobs.MODID, "textures/item/wrought_helmet.png");
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World worldIn, List<Text> tooltip, TooltipContext flagIn) {
        super.appendTooltip(stack, worldIn, tooltip, flagIn);
        tooltip.add(Text.translatable(this.getTranslationKey() + ".text.0").setStyle(ItemHandler.TOOLTIP_STYLE));
    }

    @Override
    public ConfigHandler.ArmorConfig getConfig() {
        return ConfigHandler.COMMON.TOOLS_AND_ABILITIES.WROUGHT_HELM.armorConfig;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(ArmorRender.INSTANCE);
    }

    private static class WroughtHelmMaterial implements ArmorMaterial {

        @Override
        public int getDurability(Type equipmentSlotType) {
            return ArmorMaterials.IRON.getDurability(equipmentSlotType);
        }

        @Override
        public int getProtection(Type equipmentSlotType) {
            return ConfigHandler.COMMON.TOOLS_AND_ABILITIES.WROUGHT_HELM.armorConfig.damageReductionValue;
        }

        @Override
        public int getEnchantability() {
            return ArmorMaterials.IRON.getEnchantability();
        }

        @Override
        public SoundEvent getEquipSound() {
            return ArmorMaterials.IRON.getEquipSound();
        }

        @Override
        public Ingredient getRepairIngredient() {
            return ArmorMaterials.IRON.getRepairIngredient();
        }

        @Override
        public String getName() {
            return "wrought_helm";
        }

        @Override
        public float getToughness() {
            return ConfigHandler.COMMON.TOOLS_AND_ABILITIES.WROUGHT_HELM.armorConfig.toughnessValue;
        }

        @Override
        public float getKnockbackResistance() {
            return 0.1f;
        }
    }

    private static final class ArmorRender implements IClientItemExtensions {
        private static final ArmorRender INSTANCE = new ArmorRender();
        private static BipedEntityModel<?> MODEL;

        @Override
        public BipedEntityModel<?> getHumanoidArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlot armorSlot, BipedEntityModel<?> _default) {
            if (MODEL == null) {
                EntityModelLoader models = MinecraftClient.getInstance().getEntityModelLoader();
                ModelPart root = models.getModelPart(LayerHandler.WROUGHT_HELM_LAYER);
                MODEL = new WroughtHelmModel<>(root);
            }
            return MODEL;
        }
    }
}
