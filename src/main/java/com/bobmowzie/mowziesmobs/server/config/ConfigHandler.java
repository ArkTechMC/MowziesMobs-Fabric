package com.bobmowzie.mowziesmobs.server.config;

import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterials;

import java.util.ArrayList;
import java.util.List;

public final class ConfigHandler {
    public static final Common COMMON;
    public static final Client CLIENT;

    static {
        COMMON = ConfigLoader.load(Common.class, "./config/mmobs/common.json", new Common());
        CLIENT = ConfigLoader.load(Client.class, "./config/mmobs/client.json", new Client());
        //FIXME
//        initConfig();
    }

    public static void initConfig() {
        ConfigHandler.COMMON.TOOLS_AND_ABILITIES.AXE_OF_A_THOUSAND_METALS.toolConfig.attackDamageValue = (float) ConfigHandler.COMMON.TOOLS_AND_ABILITIES.AXE_OF_A_THOUSAND_METALS.toolConfig.attackDamage;
        ConfigHandler.COMMON.TOOLS_AND_ABILITIES.AXE_OF_A_THOUSAND_METALS.toolConfig.attackSpeedValue = (float) ConfigHandler.COMMON.TOOLS_AND_ABILITIES.AXE_OF_A_THOUSAND_METALS.toolConfig.attackSpeed;
        ConfigHandler.COMMON.TOOLS_AND_ABILITIES.SPEAR.toolConfig.attackDamageValue = (float) ConfigHandler.COMMON.TOOLS_AND_ABILITIES.SPEAR.toolConfig.attackDamage;
        ConfigHandler.COMMON.TOOLS_AND_ABILITIES.SPEAR.toolConfig.attackSpeedValue = (float) ConfigHandler.COMMON.TOOLS_AND_ABILITIES.SPEAR.toolConfig.attackSpeed;
        ConfigHandler.COMMON.TOOLS_AND_ABILITIES.NAGA_FANG_DAGGER.toolConfig.attackDamageValue = (float) ConfigHandler.COMMON.TOOLS_AND_ABILITIES.NAGA_FANG_DAGGER.toolConfig.attackDamage;
        ConfigHandler.COMMON.TOOLS_AND_ABILITIES.NAGA_FANG_DAGGER.toolConfig.attackSpeedValue = (float) ConfigHandler.COMMON.TOOLS_AND_ABILITIES.NAGA_FANG_DAGGER.toolConfig.attackSpeed;
        ConfigHandler.COMMON.TOOLS_AND_ABILITIES.EARTHREND_GAUNTLET.toolConfig.attackDamageValue = (float) ConfigHandler.COMMON.TOOLS_AND_ABILITIES.EARTHREND_GAUNTLET.toolConfig.attackDamage;
        ConfigHandler.COMMON.TOOLS_AND_ABILITIES.EARTHREND_GAUNTLET.toolConfig.attackSpeedValue = (float) ConfigHandler.COMMON.TOOLS_AND_ABILITIES.EARTHREND_GAUNTLET.toolConfig.attackSpeed;
        ConfigHandler.COMMON.TOOLS_AND_ABILITIES.ICE_CRYSTAL.durabilityValue = ConfigHandler.COMMON.TOOLS_AND_ABILITIES.ICE_CRYSTAL.durability;
        ConfigHandler.COMMON.TOOLS_AND_ABILITIES.EARTHREND_GAUNTLET.durabilityValue = ConfigHandler.COMMON.TOOLS_AND_ABILITIES.EARTHREND_GAUNTLET.durability;
        ConfigHandler.COMMON.TOOLS_AND_ABILITIES.WROUGHT_HELM.armorConfig.damageReductionValue = ConfigHandler.COMMON.TOOLS_AND_ABILITIES.WROUGHT_HELM.armorConfig.damageReduction;
        ConfigHandler.COMMON.TOOLS_AND_ABILITIES.WROUGHT_HELM.armorConfig.toughnessValue = (float) ConfigHandler.COMMON.TOOLS_AND_ABILITIES.WROUGHT_HELM.armorConfig.toughness;
        ConfigHandler.COMMON.TOOLS_AND_ABILITIES.UMVUTHANA_MASK.armorConfig.damageReductionValue = ConfigHandler.COMMON.TOOLS_AND_ABILITIES.UMVUTHANA_MASK.armorConfig.damageReduction;
        ConfigHandler.COMMON.TOOLS_AND_ABILITIES.UMVUTHANA_MASK.armorConfig.toughnessValue = (float) ConfigHandler.COMMON.TOOLS_AND_ABILITIES.UMVUTHANA_MASK.armorConfig.toughness;
        ConfigHandler.COMMON.TOOLS_AND_ABILITIES.SOL_VISAGE.armorConfig.damageReductionValue = ConfigHandler.COMMON.TOOLS_AND_ABILITIES.SOL_VISAGE.armorConfig.damageReduction;
        ConfigHandler.COMMON.TOOLS_AND_ABILITIES.SOL_VISAGE.armorConfig.toughnessValue = (float) ConfigHandler.COMMON.TOOLS_AND_ABILITIES.SOL_VISAGE.armorConfig.toughness;
    }

    // Config templates
    public static class BiomeConfig {
        public List<? extends String> biomeTags = new ArrayList<>();
        public List<? extends String> biomeWhitelist = new ArrayList<>();
        public List<? extends String> biomeBlacklist = new ArrayList<>();
    }

    public static class SpawnConfig {
        public int spawnRate;
        public int minGroupSize;
        public int maxGroupSize;
        public double extraRarity;
        public BiomeConfig biomeConfig = new BiomeConfig();
        public List<? extends String> dimensions = new ArrayList<>();
        public int heightMin;
        public int heightMax;
        public boolean needsDarkness;
        public boolean needsSeeSky;
        public boolean needsCantSeeSky;
        public List<? extends String> allowedBlocks = new ArrayList<>();
        public List<? extends String> allowedBlockTags = new ArrayList<>();
        public List<? extends String> avoidStructures = new ArrayList<>();
    }

    public static class GenerationConfig {
        public int generationDistance;
        public int generationSeparation;
        public BiomeConfig biomeConfig = new BiomeConfig();
        public int heightMin;
        public int heightMax;
        public List<? extends String> avoidStructures = new ArrayList<>();
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
        public SpawnConfig spawnConfig = new SpawnConfig();
        public CombatConfig combatConfig = new CombatConfig();
    }

    public static class Umvuthana {
        public SpawnConfig spawnConfig = new SpawnConfig();
        public CombatConfig combatConfig = new CombatConfig();
    }

    public static class Naga {
        public SpawnConfig spawnConfig = new SpawnConfig();
        public CombatConfig combatConfig = new CombatConfig();
    }

    public static class Lantern {
        public SpawnConfig spawnConfig = new SpawnConfig();
        public CombatConfig combatConfig = new CombatConfig();
    }

    public static class Grottol {
        public SpawnConfig spawnConfig = new SpawnConfig();
        public CombatConfig combatConfig = new CombatConfig();
    }

    public static class FerrousWroughtnaut {
        public GenerationConfig generationConfig = new GenerationConfig();
        public CombatConfig combatConfig = new CombatConfig();
        public boolean hasBossBar;
        public boolean healsOutOfBattle;
        public boolean resetHealthWhenRespawn;
    }

    public static class Umvuthi {
        public GenerationConfig generationConfig = new GenerationConfig();
        public CombatConfig combatConfig = new CombatConfig();
        public boolean hasBossBar;
        public boolean healsOutOfBattle;
        public String whichItem = "";
        public int howMany;
        public boolean resetHealthWhenRespawn;
    }

    public static class Frostmaw {
        public GenerationConfig generationConfig = new GenerationConfig();
        public CombatConfig combatConfig = new CombatConfig();
        public boolean stealableIceCrystal;
        public boolean hasBossBar;
        public boolean healsOutOfBattle;
        public boolean resetHealthWhenRespawn;
    }

    public static class Sculptor {
        public GenerationConfig generationConfig = new GenerationConfig();
        public CombatConfig combatConfig = new CombatConfig();
        public boolean healsOutOfBattle;
    }

    public static class WroughtHelm {
        public ArmorConfig armorConfig = new ArmorConfig();
        public boolean breakable;
    }

    public static class AxeOfAThousandMetals {
        public ToolConfig toolConfig = new ToolConfig();
        public boolean breakable;
    }

    public static class SolVisage {
        public ArmorConfig armorConfig = new ArmorConfig();
        public boolean breakable;
        public int maxFollowers;
    }

    public static class UmvuthanaMask {
        public ArmorConfig armorConfig = new ArmorConfig();
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
        public ToolConfig toolConfig = new ToolConfig();
        public boolean enableTunneling;
    }

    public static class Spear {
        public ToolConfig toolConfig = new ToolConfig();
    }

    public static class NagaFangDagger {
        public ToolConfig toolConfig = new ToolConfig();
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
        public Frostmaw FROSTMAW = new Frostmaw();
        public Umvuthi UMVUTHI = new Umvuthi();
        public FerrousWroughtnaut FERROUS_WROUGHTNAUT = new FerrousWroughtnaut();
        public Sculptor SCULPTOR = new Sculptor();
        public Grottol GROTTOL = new Grottol();
        public Lantern LANTERN = new Lantern();
        public Umvuthana UMVUTHANA = new Umvuthana();
        public Naga NAGA = new Naga();
        public Foliaath FOLIAATH = new Foliaath();
    }

    public static class ToolsAndAbilities {
        public SunsBlessing SUNS_BLESSING = new SunsBlessing();
        public WroughtHelm WROUGHT_HELM = new WroughtHelm();
        public AxeOfAThousandMetals AXE_OF_A_THOUSAND_METALS = new AxeOfAThousandMetals();
        public SolVisage SOL_VISAGE = new SolVisage();
        public IceCrystal ICE_CRYSTAL = new IceCrystal();
        public UmvuthanaMask UMVUTHANA_MASK = new UmvuthanaMask();
        public Spear SPEAR = new Spear();
        public NagaFangDagger NAGA_FANG_DAGGER = new NagaFangDagger();
        public Blowgun BLOW_GUN = new Blowgun();
        public EarthrendGauntlet EARTHREND_GAUNTLET = new EarthrendGauntlet();
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
        public ToolsAndAbilities TOOLS_AND_ABILITIES = new ToolsAndAbilities();
        public Mobs MOBS = new Mobs();
    }
}
