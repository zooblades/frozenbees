package com.nazarbatrider.frozenbees.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.Fluids;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;

/** Скользкий и липкий блок. Случайно замораживает воду-источник рядом. */
public class ColdHoneyBlock extends Block {
    public static final MapCodec<ColdHoneyBlock> CODEC = createCodec(ColdHoneyBlock::new);

    public ColdHoneyBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends Block> getCodec() {
        return CODEC;
    }

    @Override
    protected void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        for (Direction dir : Direction.values()) {
            BlockPos p = pos.offset(dir);
            if (world.getBlockState(p).isOf(Blocks.WATER) && world.getFluidState(p).getFluid() == Fluids.WATER) {
                world.setBlockState(p, Blocks.ICE.getDefaultState());
            }
        }
    }
}
