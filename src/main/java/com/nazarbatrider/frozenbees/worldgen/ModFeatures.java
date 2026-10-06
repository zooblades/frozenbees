package com.nazarbatrider.frozenbees.worldgen;

import com.nazarbatrider.frozenbees.FrozenBees;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.world.gen.feature.Feature;

public class ModFeatures {
    public static final Feature<FrozenNestConfig> FROZEN_NEST = Registry.register(
            Registries.FEATURE, FrozenBees.id("frozen_nest"), new FrozenNestFeature(FrozenNestConfig.CODEC));

    public static void register() {}
}
