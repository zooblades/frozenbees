package com.nazarbatrider.frozenbees.worldgen;

import com.nazarbatrider.frozenbees.FrozenBees;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.PlacedFeature;

/** Подключает placed features (JSON в data/frozenbees/worldgen) к холодным биомам. */
public class ModWorldGen {
    private static RegistryKey<PlacedFeature> key(String name) {
        return RegistryKey.of(RegistryKeys.PLACED_FEATURE, FrozenBees.id(name));
    }

    public static void register() {
        ModFeatures.register();
        GenerationStep.Feature step = GenerationStep.Feature.VEGETAL_DECORATION;

        // Цветы во всех пяти биомах
        var all = BiomeSelectors.includeByKey(BiomeKeys.ICE_SPIKES, BiomeKeys.SNOWY_PLAINS,
                BiomeKeys.GROVE, BiomeKeys.SNOWY_SLOPES, BiomeKeys.FROZEN_PEAKS);
        BiomeModifications.addFeature(all, step, key("snowdrop_patch"));
        BiomeModifications.addFeature(all, step, key("frostbloom_patch"));

        // Гнёзда: на елях (равнины, роща, склоны) и на земле (ледяные шипы, пики)
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(BiomeKeys.SNOWY_PLAINS), step, key("nest_tree_plains"));
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(BiomeKeys.GROVE), step, key("nest_tree_grove"));
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(BiomeKeys.SNOWY_SLOPES), step, key("nest_tree_slopes"));
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(BiomeKeys.ICE_SPIKES), step, key("nest_ground_spikes"));
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(BiomeKeys.FROZEN_PEAKS), step, key("nest_ground_peaks"));
    }
}
