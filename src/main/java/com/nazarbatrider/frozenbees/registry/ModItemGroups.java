package com.nazarbatrider.frozenbees.registry;

import com.nazarbatrider.frozenbees.FrozenBees;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;

public class ModItemGroups {
    public static final ItemGroup MAIN = Registry.register(Registries.ITEM_GROUP, FrozenBees.id("main"),
            FabricItemGroup.builder()
                    .icon(() -> new ItemStack(ModItems.COLD_HONEY_BOTTLE))
                    .displayName(Text.translatable("itemGroup.frozenbees.main"))
                    .entries((ctx, entries) -> {
                        entries.add(ModBlocks.SNOWDROP);
                        entries.add(ModBlocks.FROSTBLOOM);
                        entries.add(ModBlocks.FROZEN_BEE_NEST);
                        entries.add(ModBlocks.FROZEN_BEEHIVE);
                        entries.add(ModItems.FROZEN_BEE_SPAWN_EGG);
                        entries.add(ModItems.FROZEN_HONEYCOMB);
                        entries.add(ModBlocks.FROZEN_HONEYCOMB_BLOCK);
                        entries.add(ModItems.COLD_HONEY_BOTTLE);
                        entries.add(ModBlocks.COLD_HONEY_BLOCK);
                        entries.add(ModBlocks.COOLER);
                        entries.add(ModItems.FROZEN_ARROW);
                    }).build());

    public static void register() {}
}
