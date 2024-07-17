package com.bobmowzie.mowziesmobs.server.item;

import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.google.common.collect.ImmutableMultimap;
import net.minecraft.block.Block;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.MiningToolItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.tag.TagKey;

public abstract class MowzieToolItem extends MiningToolItem {
    public MowzieToolItem(float attackDamageIn, float attackSpeedIn, ToolMaterial tier, TagKey<Block> effectiveBlocksIn, Settings builderIn) {
        super(attackDamageIn, attackSpeedIn, tier, effectiveBlocksIn, builderIn);
    }

    public void getAttributesFromConfig() {
        ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(ATTACK_DAMAGE_MODIFIER_ID, "Tool modifier", this.getConfig().attackDamage - 1.0, EntityAttributeModifier.Operation.ADDITION));
        builder.put(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(ATTACK_SPEED_MODIFIER_ID, "Tool modifier", this.getConfig().attackSpeed - 4.0, EntityAttributeModifier.Operation.ADDITION));
        this.attributeModifiers = builder.build();
    }

    @Override
    public float getAttackDamage() {
        return (float) this.getConfig().attackDamage;
    }

    public abstract ConfigHandler.ToolConfig getConfig();
}
