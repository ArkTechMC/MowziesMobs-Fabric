package com.bobmowzie.mowziesmobs.server.config;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterials;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.*;

import java.util.List;
import java.util.function.Predicate;

public final class ConfigHandler {

    public static final Common COMMON;
    public static final Client CLIENT;
    private static final String LANG_PREFIX = "config." + MowziesMobs.MODID + ".";
    private static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();
    private static final ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();
    private static final Predicate<Object> STRING_PREDICATE = s -> s instanceof String;
    private static final Predicate<Object> RESOURCE_LOCATION_PREDICATE = STRING_PREDICATE.and(s -> Identifier.isValid((String) s));
    private static final Predicate<Object> BIOME_COMBO_PREDICATE = STRING_PREDICATE.and(s -> {
        String bigString = (String) s;
        String[] typeStrings = bigString.replace(" ", "").split("[,!]");
        for (String string : typeStrings) {
            if (!RESOURCE_LOCATION_PREDICATE.test(string)) {
                return false;
            }
        }
        return true;
    });
    private static final Predicate<Object> ITEM_NAME_PREDICATE = RESOURCE_LOCATION_PREDICATE.and(s -> Registries.ITEM.containsId(new Identifier((String) s)));
    public static ForgeConfigSpec COMMON_CONFIG;
    public static ForgeConfigSpec CLIENT_CONFIG;

    static {
        COMMON = new Common(COMMON_BUILDER);
        CLIENT = new Client(CLIENT_BUILDER);

        COMMON_CONFIG = COMMON_BUILDER.build();
        CLIENT_CONFIG = CLIENT_BUILDER.build();
    }

    // Config templates
    public static class BiomeConfig {
        public List<? extends String> biomeTags;

        public List<? extends String> biomeWhitelist;

        public List<? extends String> biomeBlacklist;
    }

    public static class SpawnConfig {
        public int spawnRate;
        public int minGroupSize;
        public int maxGroupSize;
        public double extraRarity;
        public BiomeConfig biomeConfig;
        public List<? extends String> dimensions;
        public int heightMin;
        public int heightMax;
        public boolean needsDarkness;
        public boolean needsSeeSky;
        public boolean needsCantSeeSky;
        public List<? extends String> allowedBlocks;
        public List<? extends String> allowedBlockTags;
        public List<? extends String> avoidStructures;
    }

    public static class GenerationConfig {
        public int generationDistance;
        public int generationSeparation;
        public BiomeConfig biomeConfig;
        public int heightMin;
        public int heightMax;
        public List<? extends String> avoidStructures;
    }

    public static class CombatConfig {
        public double healthMultiplier;
        public double attackMultiplier;
    }

    public static class ToolConfig {
        public double attackDamage;
        public float attackDamageValue = 9;
        public float attackSpeedValue = 0.9F;
        public double attackSpeed;
    }

    public static class ArmorConfig {
        public int damageReduction;
        public int damageReductionValue = ArmorMaterials.IRON.getProtection(ArmorItem.Type.HELMET);
        public float toughnessValue = ArmorMaterials.IRON.getToughness();
        public double toughness;
    }

    // Mob configuration
    public static class Foliaath {
        public SpawnConfig spawnConfig;
        public CombatConfig combatConfig;
    }

    public static class Umvuthana {
        public SpawnConfig spawnConfig;
        public CombatConfig combatConfig;
    }

    public static class Naga {
        public SpawnConfig spawnConfig;
        public CombatConfig combatConfig;
    }

    public static class Lantern {
        public SpawnConfig spawnConfig;
        public CombatConfig combatConfig;
    }

    public static class Grottol {
        public SpawnConfig spawnConfig;
        public CombatConfig combatConfig;
    }

    public static class FerrousWroughtnaut {
        public GenerationConfig generationConfig;
        public CombatConfig combatConfig;
        public boolean hasBossBar;
        public boolean healsOutOfBattle;
        public boolean resetHealthWhenRespawn;
    }

    public static class Umvuthi {
        public GenerationConfig generationConfig;
        public CombatConfig combatConfig;
        public boolean hasBossBar;
        public boolean healsOutOfBattle;
        public String whichItem;
        public int howMany;
        public boolean resetHealthWhenRespawn;
    }

    public static class Frostmaw {
        public GenerationConfig generationConfig;
        public CombatConfig combatConfig;
        public boolean stealableIceCrystal;
        public boolean hasBossBar;
        public boolean healsOutOfBattle;
        public boolean resetHealthWhenRespawn;
    }

    public static class Sculptor {
        public GenerationConfig generationConfig;
        public CombatConfig combatConfig;
        public boolean healsOutOfBattle;
    }

    public static class WroughtHelm {
        public ArmorConfig armorConfig;
        public boolean breakable;
    }

    public static class AxeOfAThousandMetals {
        public ToolConfig toolConfig;
        public boolean breakable;
    }

    public static class SolVisage {
        public ArmorConfig armorConfig;
        public boolean breakable;
        public int maxFollowers;
    }

    public static class UmvuthanaMask {
        public ArmorConfig armorConfig;
    }

    public static class IceCrystal {
        public double attackMultiplier;
        public boolean breakable;
        public int durability;
        public int durabilityValue;
    }

    public static class EarthrendGauntlet {
        public double attackMultiplier;
        public boolean breakable;
        public int durability;
        public int durabilityValue;
        public ToolConfig toolConfig;
        public boolean enableTunneling;
    }

    public static class Spear {
        public ToolConfig toolConfig;
    }

    public static class NagaFangDagger {
        public ToolConfig toolConfig;
        public int poisonDuration;
        public double backstabDamageMultiplier;
    }

    public static class Blowgun {
        public double attackDamage;
        public int poisonDuration;
    }

    public static class SunsBlessing {
        public double sunsBlessingAttackMultiplier;
        public int effectDuration;
        public int solarBeamCost;
        public int supernovaCost;
    }

    public static class Mobs {
        public Frostmaw FROSTMAW;
        public Umvuthi UMVUTHI;
        public FerrousWroughtnaut FERROUS_WROUGHTNAUT;
        public Sculptor SCULPTOR;
        public Grottol GROTTOL;
        public Lantern LANTERN;
        public Umvuthana UMVUTHANA;
        public Naga NAGA;
        public Foliaath FOLIAATH;
    }

    public static class ToolsAndAbilities {
        public SunsBlessing SUNS_BLESSING;
        public WroughtHelm WROUGHT_HELM;
        public AxeOfAThousandMetals AXE_OF_A_THOUSAND_METALS;
        public SolVisage SOL_VISAGE;
        public IceCrystal ICE_CRYSTAL;
        public UmvuthanaMask UMVUTHANA_MASK;
        public Spear SPEAR;
        public NagaFangDagger NAGA_FANG_DAGGER;
        public Blowgun BLOW_GUN;
        public EarthrendGauntlet EARTHREND_GAUNTLET;
    }

    public static class Client {
        public boolean glowEffect;
        public boolean umvuthanaFootprints;
        public boolean doCameraShakes;
        public boolean playBossMusic;
        public boolean customBossBars;
        public boolean customPlayerAnims;
        public boolean doUmvuthanaCraneHealSound;
    }

    public static class Common {
        public ToolsAndAbilities TOOLS_AND_ABILITIES;
        public Mobs MOBS;
    }
}
