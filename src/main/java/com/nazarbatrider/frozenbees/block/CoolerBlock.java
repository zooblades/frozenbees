package com.nazarbatrider.frozenbees.block;

import com.mojang.serialization.MapCodec;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CampfireBlock;
import net.minecraft.block.FluidBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;

/** Охладитель: тушит огонь, костры и горящих существ рядом, застывает лаву вплотную. */
public class CoolerBlock extends Block {
    public static final MapCodec<CoolerBlock> CODEC = createCodec(CoolerBlock::new);
    private static final int RANGE = 4;
    private static final int INTERVAL = 10;

    public CoolerBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends Block> getCodec() {
        return CODEC;
    }

    @Override
    protected void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        if (!world.isClient) {
            world.scheduleBlockTick(pos, this, INTERVAL);
        }
    }

    @Override
    protected void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        boolean acted = false;

        for (BlockPos p : BlockPos.iterate(pos.add(-RANGE, -2, -RANGE), pos.add(RANGE, 3, RANGE))) {
            BlockState s = world.getBlockState(p);
            if (s.isIn(BlockTags.FIRE)) {
                world.removeBlock(p, false);
                world.syncWorldEvent(WorldEvents.FIRE_EXTINGUISHED, p, 0);
                acted = true;
            } else if (s.getBlock() instanceof CampfireBlock && s.get(CampfireBlock.LIT)) {
                world.setBlockState(p, s.with(CampfireBlock.LIT, false));
                world.syncWorldEvent(WorldEvents.FIRE_EXTINGUISHED, p, 0);
                acted = true;
            }
        }

        for (Direction d : Direction.values()) {
            BlockPos p = pos.offset(d);
            BlockState s = world.getBlockState(p);
            if (s.isOf(Blocks.LAVA)) {
                boolean source = s.get(FluidBlock.LEVEL) == 0;
                world.setBlockState(p, source ? Blocks.OBSIDIAN.getDefaultState() : Blocks.COBBLESTONE.getDefaultState());
                world.syncWorldEvent(WorldEvents.LAVA_EXTINGUISHED, p, 0);
                acted = true;
            }
        }

        List<LivingEntity> burning = world.getEntitiesByClass(LivingEntity.class, new Box(pos).expand(RANGE), LivingEntity::isOnFire);
        for (LivingEntity e : burning) {
            e.extinguish();
            acted = true;
        }

        if (acted) {
            world.spawnParticles(ParticleTypes.SNOWFLAKE, pos.getX() + 0.5, pos.getY() + 1.1, pos.getZ() + 0.5, 6, 0.35, 0.1, 0.35, 0.02);
        }
        world.scheduleBlockTick(pos, this, INTERVAL);
    }
}
