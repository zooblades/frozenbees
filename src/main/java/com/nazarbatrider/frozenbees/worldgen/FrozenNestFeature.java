package com.nazarbatrider.frozenbees.worldgen;

import com.mojang.serialization.Codec;
import com.nazarbatrider.frozenbees.block.FrozenHiveBlock;
import com.nazarbatrider.frozenbees.registry.ModBlockEntities;
import com.nazarbatrider.frozenbees.registry.ModBlocks;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

/** Ставит морозное гнездо с 1-3 пчёлами на ствол ели или на землю. */
public class FrozenNestFeature extends Feature<FrozenNestConfig> {
    public FrozenNestFeature(Codec<FrozenNestConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generate(FeatureContext<FrozenNestConfig> context) {
        StructureWorldAccess world = context.getWorld();
        BlockPos origin = context.getOrigin();
        Random random = context.getRandom();

        BlockPos nestPos = null;
        Direction facing = null;

        if (context.getConfig().onGround()) {
            BlockState below = world.getBlockState(origin.down());
            BlockState at = world.getBlockState(origin);
            boolean soil = below.isOf(Blocks.ICE) || below.isOf(Blocks.PACKED_ICE)
                    || below.isOf(Blocks.BLUE_ICE) || below.isOf(Blocks.SNOW_BLOCK);
            if (soil && (at.isAir() || at.isOf(Blocks.SNOW))) {
                nestPos = origin;
                facing = Direction.Type.HORIZONTAL.random(random);
            }
        } else {
            List<BlockPos> spots = new ArrayList<>();
            List<Direction> dirs = new ArrayList<>();
            for (int dx = -7; dx <= 7; dx++) {
                for (int dz = -7; dz <= 7; dz++) {
                    for (int dy = -10; dy <= 6; dy++) {
                        BlockPos log = origin.add(dx, dy, dz);
                        if (!world.getBlockState(log).isIn(BlockTags.SPRUCE_LOGS)) continue;
                        // только ствол: бревно и сверху, и снизу
                        if (!world.getBlockState(log.up()).isIn(BlockTags.SPRUCE_LOGS)
                                || !world.getBlockState(log.down()).isIn(BlockTags.SPRUCE_LOGS)) continue;
                        for (Direction d : Direction.Type.HORIZONTAL) {
                            BlockPos n = log.offset(d);
                            if (world.getBlockState(n).isAir() && world.getBlockState(n.offset(d)).isAir()) {
                                spots.add(n);
                                dirs.add(d);
                            }
                        }
                    }
                }
            }
            if (!spots.isEmpty()) {
                int i = random.nextInt(spots.size());
                nestPos = spots.get(i);
                facing = dirs.get(i);
            }
        }

        if (nestPos == null) return false;

        world.setBlockState(nestPos, ModBlocks.FROZEN_BEE_NEST.getDefaultState().with(FrozenHiveBlock.FACING, facing), Block.NOTIFY_LISTENERS);
        int bees = 1 + random.nextInt(3);
        world.getBlockEntity(nestPos, ModBlockEntities.FROZEN_HIVE).ifPresent(be -> {
            for (int i = 0; i < bees; i++) {
                NbtCompound data = new NbtCompound();
                data.putString("id", "frozenbees:frozen_bee");
                be.addBee(data, random.nextInt(300), 400);
            }
        });
        return true;
    }
}
