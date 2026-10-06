package com.nazarbatrider.frozenbees.block.entity;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/** Лёгкий реестр загруженных гнёзд/ульев, чтобы пчёлы находили дом без сканирования мира. */
public final class FrozenHiveTracker {
    private static final Map<RegistryKey<World>, Set<BlockPos>> HIVES = new ConcurrentHashMap<>();

    private FrozenHiveTracker() {}

    public static void add(World world, BlockPos pos) {
        HIVES.computeIfAbsent(world.getRegistryKey(), k -> ConcurrentHashMap.newKeySet()).add(pos.toImmutable());
    }

    public static void remove(World world, BlockPos pos) {
        Set<BlockPos> set = HIVES.get(world.getRegistryKey());
        if (set != null) set.remove(pos);
    }

    public static Optional<BlockPos> findNearest(World world, BlockPos from, double radius, Predicate<BlockPos> filter) {
        Set<BlockPos> set = HIVES.get(world.getRegistryKey());
        if (set == null) return Optional.empty();
        BlockPos best = null;
        double bestDist = radius * radius;
        for (BlockPos p : set) {
            double d = p.getSquaredDistance(from);
            if (d <= bestDist && filter.test(p)) {
                bestDist = d;
                best = p;
            }
        }
        return Optional.ofNullable(best);
    }

    public static void clear() {
        HIVES.clear();
    }
}
