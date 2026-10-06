package com.nazarbatrider.frozenbees.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.gen.feature.FeatureConfig;

/** onGround=false: гнездо на стволе ели; onGround=true: на земле (снег, лёд). */
public record FrozenNestConfig(boolean onGround) implements FeatureConfig {
    public static final Codec<FrozenNestConfig> CODEC = RecordCodecBuilder.create(i -> i.group(
            Codec.BOOL.fieldOf("on_ground").forGetter(FrozenNestConfig::onGround)
    ).apply(i, FrozenNestConfig::new));
}
