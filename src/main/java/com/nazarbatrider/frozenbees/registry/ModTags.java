package com.nazarbatrider.frozenbees.registry;

import com.nazarbatrider.frozenbees.FrozenBees;
import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public class ModTags {
    /** Цветы, которые опыляют замороженные пчёлы. */
    public static final TagKey<Block> FROZEN_FLOWERS = TagKey.of(RegistryKeys.BLOCK, FrozenBees.id("frozen_flowers"));
}
