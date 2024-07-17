package com.bobmowzie.mowziesmobs.server.potion;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.BrewingRecipeRegistry;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

/**
 * Created by BobMowzie on 1/10/2019.
 */
public final class PotionTypeHandler {
    public static final Potion POISON_RESIST = register("poison_resist", new Potion("poison_resist", new StatusEffectInstance(EffectHandler.POISON_RESIST, 3600)));
    public static final Potion LONG_POISON_RESIST = register("long_poison_resist", new Potion("long_poison_resist", new StatusEffectInstance(EffectHandler.POISON_RESIST, 9600)));

    public static Potion register(String name, Potion potion) {
        return Registry.register(Registries.POTION, new Identifier(MowziesMobs.MODID, name), potion);
    }

    public static void init() {
        BrewingRecipeRegistry.registerPotionRecipe(Potions.AWKWARD, ItemHandler.NAGA_FANG, POISON_RESIST);
        BrewingRecipeRegistry.registerPotionRecipe(POISON_RESIST, Items.REDSTONE, LONG_POISON_RESIST);
    }
}
