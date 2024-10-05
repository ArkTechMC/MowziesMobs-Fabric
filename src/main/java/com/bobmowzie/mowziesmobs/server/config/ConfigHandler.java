package com.bobmowzie.mowziesmobs.server.config;

import com.google.gson.Gson;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterials;

import java.util.ArrayList;
import java.util.List;

public final class ConfigHandler {
    private static final Gson GSON = new Gson();
    public static final Common COMMON;
    public static final Client CLIENT;

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
        public int spawnRate = 10;
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
        public boolean glowEffect = true;
        public boolean umvuthanaFootprints = true;
        public boolean doCameraShakes = true;
        public boolean playBossMusic = true;
        public boolean customBossBars = true;
        public boolean customPlayerAnims = true;
        public boolean doUmvuthanaCraneHealSound = true;
    }

    public static class Common {
        public ToolsAndAbilities TOOLS_AND_ABILITIES = new ToolsAndAbilities();
        public Mobs MOBS = new Mobs();
    }

    public static final String defaultString = """
            {
              "TOOLS_AND_ABILITIES": {
                "SUNS_BLESSING": {
                  "sunsBlessingAttackMultiplier": 1.0,
                  "effectDuration": 60,
                  "solarBeamCost": 5,
                  "supernovaCost": 0
                },
                "WROUGHT_HELM": {
                  "armorConfig": {
                    "damageReduction": 2,
                    "damageReductionValue": 2,
                    "toughnessValue": 0.0,
                    "toughness": 0.0
                  },
                  "breakable": false
                },
                "AXE_OF_A_THOUSAND_METALS": {
                  "toolConfig": {
                    "attackDamage": 9.0,
                    "attackDamageValue": 9.0,
                    "attackSpeedValue": 0.8999999761581421,
                    "attackSpeed": 0.8999999761581421
                  },
                  "breakable": false
                },
                "SOL_VISAGE": {
                  "armorConfig": {
                    "damageReduction": 2,
                    "damageReductionValue": 2,
                    "toughnessValue": 0.0,
                    "toughness": 0.0
                  },
                  "breakable": false,
                  "maxFollowers": 10
                },
                "ICE_CRYSTAL": {
                  "attackMultiplier": 1.0,
                  "breakable": false,
                  "durability": 600,
                  "durabilityValue": 600
                },
                "UMVUTHANA_MASK": {
                  "armorConfig": {
                    "damageReduction": 1,
                    "damageReductionValue": 1,
                    "toughnessValue": 0.0,
                    "toughness": 0.0
                  }
                },
                "SPEAR": {
                  "toolConfig": {
                    "attackDamage": 5.0,
                    "attackDamageValue": 5.0,
                    "attackSpeedValue": 1.600000023841858,
                    "attackSpeed": 1.600000023841858
                  }
                },
                "NAGA_FANG_DAGGER": {
                  "toolConfig": {
                    "attackDamage": 3.0,
                    "attackDamageValue": 3.0,
                    "attackSpeedValue": 2.0,
                    "attackSpeed": 2.0
                  },
                  "poisonDuration": 40,
                  "backstabDamageMultiplier": 2.0
                },
                "BLOW_GUN": {
                  "attackDamage": 1.0,
                  "poisonDuration": 40
                },
                "EARTHREND_GAUNTLET": {
                  "attackMultiplier": 1.0,
                  "breakable": false,
                  "durability": 400,
                  "durabilityValue": 400,
                  "toolConfig": {
                    "attackDamage": 6.0,
                    "attackDamageValue": 6.0,
                    "attackSpeedValue": 1.2000000476837158,
                    "attackSpeed": 1.2000000476837158
                  },
                  "enableTunneling": false
                }
              },
              "MOBS": {
                "FROSTMAW": {
                  "generationConfig": {
                    "generationDistance": 25,
                    "generationSeparation": 8,
                    "biomeConfig": {
                      "biomeTags": ["!minecraft:is_ocean,!minecraft:is_river,!minecraft:is_beach,!minecraft:is_forest,!minecraft:is_taiga"],
                      "biomeWhitelist": [],
                      "biomeBlacklist": []
                    },
                    "heightMin": 25,
                    "heightMax": 100,
                    "avoidStructures": ["minecraft:villages", "minecraft:pillager_outposts"]
                  },
                  "combatConfig": {
                    "healthMultiplier": 1.0,
                    "attackMultiplier": 1.0
                  },
                  "stealableIceCrystal": true,
                  "hasBossBar": true,
                  "healsOutOfBattle": true,
                  "resetHealthWhenRespawn": true
                },
                "UMVUTHI": {
                  "generationConfig": {
                    "generationDistance": 25,
                    "generationSeparation": 8,
                    "biomeConfig": {
                      "biomeTags": ["minecraft:is_savanna"],
                      "biomeWhitelist": [],
                      "biomeBlacklist": []
                    },
                    "heightMin": 50,
                    "heightMax": 100,
                    "avoidStructures": ["minecraft:villages", "minecraft:pillager_outposts"]
                  },
                  "combatConfig": {
                    "healthMultiplier": 1.0,
                    "attackMultiplier": 1.0
                  },
                  "hasBossBar": true,
                  "healsOutOfBattle": true,
                  "whichItem": "minecraft:gold_block",
                  "howMany": 7,
                  "resetHealthWhenRespawn": true
                },
                "FERROUS_WROUGHTNAUT": {
                  "generationConfig": {
                    "generationDistance": 15,
                    "generationSeparation": 5,
                    "biomeConfig": {
                      "biomeTags": ["!minecraft:is_ocean"],
                      "biomeWhitelist": [],
                      "biomeBlacklist": []
                    },
                    "heightMin": 20,
                    "heightMax": 50,
                    "avoidStructures": []
                  },
                  "combatConfig": {
                    "healthMultiplier": 1.0,
                    "attackMultiplier": 1.0
                  },
                  "hasBossBar": true,
                  "healsOutOfBattle": true,
                  "resetHealthWhenRespawn": true
                },
                "SCULPTOR": {
                  "generationConfig": {
                    "generationDistance": 25,
                    "generationSeparation": 8,
                    "biomeConfig": {
                      "biomeTags": ["minecraft:is_mountain"],
                      "biomeWhitelist": [],
                      "biomeBlacklist": []
                    },
                    "heightMin": 120,
                    "heightMax": 200,
                    "avoidStructures": []
                  },
                  "combatConfig": {
                    "healthMultiplier": 1.0,
                    "attackMultiplier": 1.0
                  },
                  "healsOutOfBattle": true
                },
                "GROTTOL": {
                  "spawnConfig": {
                    "spawnRate": 2,
                    "minGroupSize": 1,
                    "maxGroupSize": 1,
                    "extraRarity": 1.0,
                    "biomeConfig": {
                      "biomeTags": [""],
                      "biomeWhitelist": [],
                      "biomeBlacklist": []
                    },
                    "dimensions": ["minecraft:overworld"],
                    "heightMin": -65,
                    "heightMax": 16,
                    "needsDarkness": true,
                    "needsSeeSky": false,
                    "needsCantSeeSky": true,
                    "allowedBlocks": [],
                    "allowedBlockTags": ["minecraft:base_stone_overworld"],
                    "avoidStructures": []
                  },
                  "combatConfig": {
                    "healthMultiplier": 1.0,
                    "attackMultiplier": 1.0
                  }
                },
                "LANTERN": {
                  "spawnConfig": {
                    "spawnRate": 5,
                    "minGroupSize": 2,
                    "maxGroupSize": 4,
                    "extraRarity": 1.0,
                    "biomeConfig": {
                      "biomeTags": ["minecraft:is_forest,mowziesmobs:is_magical,!forge:is_snowy"],
                      "biomeWhitelist": [],
                      "biomeBlacklist": []
                    },
                    "dimensions": ["minecraft:overworld"],
                    "heightMin": 60,
                    "heightMax": -65,
                    "needsDarkness": true,
                    "needsSeeSky": false,
                    "needsCantSeeSky": false,
                    "allowedBlocks": [],
                    "allowedBlockTags": ["minecraft:valid_spawn", "minecraft:leaves", "minecraft:logs"],
                    "avoidStructures": []
                  },
                  "combatConfig": {
                    "healthMultiplier": 1.0,
                    "attackMultiplier": 1.0
                  }
                },
                "UMVUTHANA": {
                  "spawnConfig": {
                    "spawnRate": 5,
                    "minGroupSize": 1,
                    "maxGroupSize": 1,
                    "extraRarity": 1.0,
                    "biomeConfig": {
                      "biomeTags": ["minecraft:is_savanna"],
                      "biomeWhitelist": [],
                      "biomeBlacklist": []
                    },
                    "dimensions": ["minecraft:overworld"],
                    "heightMin": -65,
                    "heightMax": 60,
                    "needsDarkness": false,
                    "needsSeeSky": false,
                    "needsCantSeeSky": false,
                    "allowedBlocks": [],
                    "allowedBlockTags": ["minecraft:valid_spawn", "minecraft:sand"],
                    "avoidStructures": ["minecraft:villages", "minecraft:pillager_outposts", "mowziesmobs:umvuthana_groves"]
                  },
                  "combatConfig": {
                    "healthMultiplier": 1.0,
                    "attackMultiplier": 1.0
                  }
                },
                "NAGA": {
                  "spawnConfig": {
                    "spawnRate": 13,
                    "minGroupSize": 2,
                    "maxGroupSize": 3,
                    "extraRarity": 1.0,
                    "biomeConfig": {
                      "biomeTags": ["minecraft:is_beach,minecraft:is_mountain", "minecraft:is_beach,minecraft:is_hill"],
                      "biomeWhitelist": ["minecraft:stony_shore"],
                      "biomeBlacklist": []
                    },
                    "dimensions": [],
                    "heightMin": -65,
                    "heightMax": 70,
                    "needsDarkness": false,
                    "needsSeeSky": true,
                    "needsCantSeeSky": false,
                    "allowedBlocks": [],
                    "allowedBlockTags": [],
                    "avoidStructures": ["minecraft:villages", "minecraft:pillager_outposts"]
                  },
                  "combatConfig": {
                    "healthMultiplier": 1.0,
                    "attackMultiplier": 1.0
                  }
                },
                "FOLIAATH": {
                  "spawnConfig": {
                    "spawnRate": 70,
                    "minGroupSize": 1,
                    "maxGroupSize": 4,
                    "extraRarity": 1.0,
                    "biomeConfig": {
                      "biomeTags": ["minecraft:is_jungle"],
                      "biomeWhitelist": [],
                      "biomeBlacklist": []
                    },
                    "dimensions": ["minecraft:overworld"],
                    "heightMin": -65,
                    "heightMax": 60,
                    "needsDarkness": true,
                    "needsSeeSky": false,
                    "needsCantSeeSky": false,
                    "allowedBlocks": [],
                    "allowedBlockTags": ["minecraft:valid_spawn", "minecraft:leaves", "minecraft:logs"],
                    "avoidStructures": ["minecraft:villages", "minecraft:pillager_outposts"]
                  },
                  "combatConfig": {
                    "healthMultiplier": 1.0,
                    "attackMultiplier": 1.0
                  }
                }
              }
            }""";

    static {
        COMMON = ConfigLoader.load(Common.class, "./config/mmobs/common.json", GSON.fromJson(defaultString, Common.class));
        CLIENT = ConfigLoader.load(Client.class, "./config/mmobs/client.json", new Client());
        //FIXME
        initConfig();
    }
}
