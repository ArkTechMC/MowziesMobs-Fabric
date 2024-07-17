package com.bobmowzie.mowziesmobs.server.loot;

import com.bobmowzie.mowziesmobs.server.entity.frostmaw.EntityFrostmaw;
import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.entity.Entity;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.condition.LootConditionType;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameter;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.util.JsonSerializer;

import java.util.Set;

public class LootConditionFrostmawHasCrystal implements LootCondition {

    private static final LootConditionFrostmawHasCrystal INSTANCE = new LootConditionFrostmawHasCrystal();

    private LootConditionFrostmawHasCrystal() {
    }

    public static Builder builder() {
        return () -> INSTANCE;
    }

    @Override
    public LootConditionType getType() {
        return LootTableHandler.FROSTMAW_HAS_CRYSTAL;
    }

    public Set<LootContextParameter<?>> getRequiredParameters() {
        return ImmutableSet.of(LootContextParameters.LAST_DAMAGE_PLAYER);
    }

    public boolean test(LootContext context) {
        Entity entity = context.get(LootContextParameters.THIS_ENTITY);
        if (entity instanceof EntityFrostmaw frostmaw) {
            return frostmaw.getHasCrystal();
        }
        return false;
    }

    public static class ConditionSerializer implements JsonSerializer<LootConditionFrostmawHasCrystal> {
        @Override
        public void toJson(JsonObject json, LootConditionFrostmawHasCrystal value, JsonSerializationContext context) {
        }

        @Override
        public LootConditionFrostmawHasCrystal fromJson(JsonObject json, JsonDeserializationContext context) {
            return LootConditionFrostmawHasCrystal.INSTANCE;
        }
    }
}
