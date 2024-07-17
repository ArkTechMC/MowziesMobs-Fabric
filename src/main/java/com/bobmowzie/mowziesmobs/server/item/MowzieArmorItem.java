package com.bobmowzie.mowziesmobs.server.item;

import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.google.common.collect.ImmutableMultimap;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;

import java.util.UUID;

public abstract class MowzieArmorItem extends ArmorItem {
    private static final UUID[] ARMOR_MODIFIERS = new UUID[]{UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"), UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"), UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"), UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150")};

    public MowzieArmorItem(ArmorMaterial materialIn, Type slot, Settings builderIn) {
        super(materialIn, slot, builderIn);
    }

    public void getAttributesFromConfig() {
        ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
        UUID uuid = ARMOR_MODIFIERS[this.type.getEquipmentSlot().getEntitySlotId()];
        builder.put(EntityAttributes.GENERIC_ARMOR, new EntityAttributeModifier(uuid, "Armor modifier", this.getConfig().damageReduction, EntityAttributeModifier.Operation.ADDITION));
        builder.put(EntityAttributes.GENERIC_ARMOR_TOUGHNESS, new EntityAttributeModifier(uuid, "Armor toughness", this.getConfig().toughness, EntityAttributeModifier.Operation.ADDITION));
        if (this.knockbackResistance > 0) {
            builder.put(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, new EntityAttributeModifier(uuid, "Armor knockback resistance", this.knockbackResistance, EntityAttributeModifier.Operation.ADDITION));
        }
        this.attributeModifiers = builder.build();
    }

    public abstract ConfigHandler.ArmorConfig getConfig();
}
