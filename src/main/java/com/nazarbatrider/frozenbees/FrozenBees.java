package com.nazarbatrider.frozenbees;

import com.nazarbatrider.frozenbees.block.entity.FrozenHiveTracker;
import com.nazarbatrider.frozenbees.registry.ModBlockEntities;
import com.nazarbatrider.frozenbees.registry.ModBlocks;
import com.nazarbatrider.frozenbees.registry.ModEntities;
import com.nazarbatrider.frozenbees.registry.ModItemGroups;
import com.nazarbatrider.frozenbees.registry.ModItems;
import com.nazarbatrider.frozenbees.worldgen.ModWorldGen;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FrozenBees implements ModInitializer {
    public static final String MOD_ID = "frozenbees";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    /** Единственное место создания Identifier: при порте на 1.20.1 меняется одна строка (new Identifier). */
    public static Identifier id(String path) {
        return Identifier.of(MOD_ID, path);
    }

    @Override
    public void onInitialize() {
        ModBlocks.register();
        ModBlockEntities.register();
        ModEntities.register();
        ModItems.register();
        ModItemGroups.register();
        ModWorldGen.register();
        ServerLifecycleEvents.SERVER_STOPPED.register(server -> FrozenHiveTracker.clear());
        LOGGER.info("Frozen Bees loaded");
    }
}
