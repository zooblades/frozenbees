package com.nazarbatrider.frozenbees.registry;

import com.nazarbatrider.frozenbees.FrozenBees;
import com.nazarbatrider.frozenbees.block.entity.FrozenHiveBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModBlockEntities {
    public static final BlockEntityType<FrozenHiveBlockEntity> FROZEN_HIVE = Registry.register(
            Registries.BLOCK_ENTITY_TYPE, FrozenBees.id("frozen_hive"),
            BlockEntityType.Builder.create(FrozenHiveBlockEntity::new, ModBlocks.FROZEN_BEE_NEST, ModBlocks.FROZEN_BEEHIVE).build(null));

    public static void register() {}
}
