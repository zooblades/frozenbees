package com.nazarbatrider.frozenbees;

import com.nazarbatrider.frozenbees.registry.ModBlocks;
import com.nazarbatrider.frozenbees.registry.ModItemGroups;
import com.nazarbatrider.frozenbees.registry.ModItems;
import net.fabricmc.api.ModInitializer;
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
        ModItems.register();
        ModItemGroups.register();
        LOGGER.info("Frozen Bees loaded");
    }
}
