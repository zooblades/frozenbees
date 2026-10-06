package com.nazarbatrider.frozenbees.registry;

import com.nazarbatrider.frozenbees.FrozenBees;
import com.nazarbatrider.frozenbees.entity.FrozenBeeEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModEntities {
    public static final EntityType<FrozenBeeEntity> FROZEN_BEE = Registry.register(
            Registries.ENTITY_TYPE, FrozenBees.id("frozen_bee"),
            EntityType.Builder.create(FrozenBeeEntity::new, SpawnGroup.CREATURE)
                    .dimensions(0.7f, 0.6f).maxTrackingRange(8).build("frozen_bee"));

    public static void register() {
        FabricDefaultAttributeRegistry.register(FROZEN_BEE, BeeEntity.createBeeAttributes());
    }
}
