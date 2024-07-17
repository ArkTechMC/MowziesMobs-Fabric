package com.bobmowzie.mowziesmobs.server.world;

import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;

import java.util.HashSet;
import java.util.Set;

public class BiomeChecker {
    private final Set<BiomeCombo> comboList;
    private final Set<Identifier> whitelist;
    private final Set<Identifier> blacklist;

    public BiomeChecker(ConfigHandler.BiomeConfig biomeConfig) {
        this.comboList = new HashSet<>();
        for (String biomeComboString : biomeConfig.biomeTags) {
            BiomeCombo biomeCombo = new BiomeCombo(biomeComboString);
            this.comboList.add(biomeCombo);
        }
        this.whitelist = new HashSet<>();
        for (String biomeString : biomeConfig.biomeWhitelist) {
            this.whitelist.add(new Identifier(biomeString));
        }
        this.blacklist = new HashSet<>();
        for (String biomeString : biomeConfig.biomeBlacklist) {
            this.blacklist.add(new Identifier(biomeString));
        }
    }

    public boolean isBiomeInConfig(RegistryEntry<Biome> biome) {
        for (Identifier biomeName : this.whitelist) {
            TagKey<Biome> tagKey = TagKey.of(RegistryKeys.BIOME, biomeName);
            if (biome.isIn(tagKey)) return true;
            if (biome.matchesId(biomeName)) return true;
        }
        for (Identifier biomeName : this.blacklist) {
            TagKey<Biome> tagKey = TagKey.of(RegistryKeys.BIOME, biomeName);
            if (biome.isIn(tagKey)) return false;
            if (biome.matchesId(biomeName)) return false;
        }

        for (BiomeCombo biomeCombo : this.comboList) {
            if (biomeCombo.acceptsBiome(biome)) return true;
        }
        return false;
    }

    private static class BiomeCombo {
        Identifier[] neededTags;
        boolean[] inverted;

        private BiomeCombo(String biomeComboString) {
            String[] typeStrings = biomeComboString.replace(" ", "").split(",");
            this.neededTags = new Identifier[typeStrings.length];
            this.inverted = new boolean[typeStrings.length];
            for (int i = 0; i < typeStrings.length; i++) {
                if (typeStrings[i].isEmpty()) continue;
                this.inverted[i] = typeStrings[i].charAt(0) == '!';
                String name = typeStrings[i].replace("!", "");
                this.neededTags[i] = new Identifier(name);
            }
        }

        private boolean acceptsBiome(RegistryEntry<Biome> biome) {
            for (int i = 0; i < this.neededTags.length; i++) {
                Identifier neededBiomeName = this.neededTags[i];
                if (neededBiomeName == null) continue;
                TagKey<Biome> neededBiomeTag = TagKey.of(RegistryKeys.BIOME, neededBiomeName);
                boolean failIfMatches = this.inverted[i];
                if (failIfMatches) {
                    if (biome.isIn(neededBiomeTag) || biome.matchesId(neededBiomeName)) {
                        return false;
                    }
                } else {
                    if (!(biome.isIn(neededBiomeTag) || biome.matchesId(neededBiomeName))) {
                        return false;
                    }
                }
            }
            return true;
        }
    }
}
