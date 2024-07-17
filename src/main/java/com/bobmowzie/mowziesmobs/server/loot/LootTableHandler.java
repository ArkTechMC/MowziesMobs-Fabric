package com.bobmowzie.mowziesmobs.server.loot;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.condition.LootConditionType;
import net.minecraft.loot.function.LootFunction;
import net.minecraft.loot.function.LootFunctionType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonSerializer;

public class LootTableHandler {
    // Mob drops
    public static final Identifier FERROUS_WROUGHTNAUT = register("entities/ferrous_wroughtnaut");
    public static final Identifier LANTERN = register("entities/lantern");
    public static final Identifier NAGA = register("entities/naga");
    public static final Identifier FOLIAATH = register("entities/foliaath");
    public static final Identifier GROTTOL = register("entities/grottol");
    public static final Identifier FROSTMAW = register("entities/frostmaw");
    public static final Identifier UMVUTHANA_FURY = register("entities/umvuthana_fury");
    public static final Identifier UMVUTHANA_MISERY = register("entities/umvuthana_misery");
    public static final Identifier UMVUTHANA_BLISS = register("entities/umvuthana_bliss");
    public static final Identifier UMVUTHANA_RAGE = register("entities/umvuthana_rage");
    public static final Identifier UMVUTHANA_FEAR = register("entities/umvuthana_fear");
    public static final Identifier UMVUTHANA_FAITH = register("entities/umvuthana_faith");
    public static final Identifier UMVUTHI = register("entities/umvuthi");
    public static final Identifier UMVUTHANA_GROVE_CHEST = register("chests/umvuthana_grove_chest");

    public static LootFunctionType GROTTOL_DEATH_TYPE = registerFunction("grottol_death_type", new LootFunctionGrottolDeathType.FunctionSerializer());

    public static LootConditionType FROSTMAW_HAS_CRYSTAL = registerCondition("has_crystal", new LootConditionFrostmawHasCrystal.ConditionSerializer());

    private static Identifier register(String id) {
        return new Identifier(MowziesMobs.MODID, id);
    }

    private static LootFunctionType registerFunction(String name, JsonSerializer<? extends LootFunction> serializer) {
        return Registry.register(Registries.LOOT_FUNCTION_TYPE, new Identifier(MowziesMobs.MODID, name), new LootFunctionType(serializer));
    }

    private static LootConditionType registerCondition(String registryName, JsonSerializer<? extends LootCondition> serializer) {
        return Registry.register(Registries.LOOT_CONDITION_TYPE, new Identifier(MowziesMobs.MODID, registryName), new LootConditionType(serializer));
    }

    public static void init() {
    }
}
