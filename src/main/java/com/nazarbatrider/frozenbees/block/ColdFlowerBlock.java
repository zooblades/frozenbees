package com.nazarbatrider.frozenbees.block;

import java.util.function.Predicate;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowerBlock;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

/** Цветок, который можно посадить только на заданные блоки (снег, лёд и т.д.). */
public class ColdFlowerBlock extends FlowerBlock {
    private final Predicate<BlockState> soil;

    public ColdFlowerBlock(RegistryEntry<StatusEffect> stewEffect, float seconds, Settings settings, Predicate<BlockState> soil) {
        super(stewEffect, seconds, settings);
        this.soil = soil;
    }

    @Override
    protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
        return soil.test(floor);
    }
}
