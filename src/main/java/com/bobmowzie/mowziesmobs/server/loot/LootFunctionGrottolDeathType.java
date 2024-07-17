package com.bobmowzie.mowziesmobs.server.loot;

import com.bobmowzie.mowziesmobs.server.entity.grottol.EntityGrottol;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.function.ConditionalLootFunction;
import net.minecraft.loot.function.LootFunctionType;

public class LootFunctionGrottolDeathType extends ConditionalLootFunction {
    public LootFunctionGrottolDeathType(LootCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Override
    protected ItemStack process(ItemStack stack, LootContext context) {
        Entity entity = context.get(LootContextParameters.THIS_ENTITY);
        if (entity instanceof EntityGrottol grottol) {
            EntityGrottol.EnumDeathType deathType = grottol.getDeathType();
            if (deathType == EntityGrottol.EnumDeathType.NORMAL) {
                stack.setCount(0);
            } else if (deathType == EntityGrottol.EnumDeathType.FORTUNE_PICKAXE) {
                stack.setCount(stack.getCount() + 1);
            }
        }
        return stack;
    }

    @Override
    public LootFunctionType getType() {
        return LootTableHandler.GROTTOL_DEATH_TYPE;
    }

    public static class FunctionSerializer extends Serializer<LootFunctionGrottolDeathType> {
        @Override
        public void toJson(JsonObject object, LootFunctionGrottolDeathType functionClazz, JsonSerializationContext serializationContext) {
        }

        @Override
        public LootFunctionGrottolDeathType fromJson(JsonObject object, JsonDeserializationContext deserializationContext, LootCondition[] conditionsIn) {
            return new LootFunctionGrottolDeathType(conditionsIn);
        }
    }
}
