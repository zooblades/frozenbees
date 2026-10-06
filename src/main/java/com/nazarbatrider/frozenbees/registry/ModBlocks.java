package com.nazarbatrider.frozenbees.registry;

import com.nazarbatrider.frozenbees.FrozenBees;
import com.nazarbatrider.frozenbees.block.ColdFlowerBlock;
import com.nazarbatrider.frozenbees.block.ColdHoneyBlock;
import com.nazarbatrider.frozenbees.block.CoolerBlock;
import com.nazarbatrider.frozenbees.block.FrozenHiveBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.sound.BlockSoundGroup;

public class ModBlocks {
    public static final Block SNOWDROP = registerWithItem("snowdrop", new ColdFlowerBlock(
            StatusEffects.FIRE_RESISTANCE, 6f, flowerSettings(MapColor.WHITE),
            s -> s.isIn(BlockTags.DIRT) || s.isOf(Blocks.SNOW_BLOCK)));

    public static final Block FROSTBLOOM = registerWithItem("frostbloom", new ColdFlowerBlock(
            StatusEffects.RESISTANCE, 6f, flowerSettings(MapColor.LIGHT_BLUE),
            s -> s.isOf(Blocks.ICE) || s.isOf(Blocks.PACKED_ICE) || s.isOf(Blocks.BLUE_ICE) || s.isOf(Blocks.SNOW_BLOCK)));

    public static final Block FROZEN_HONEYCOMB_BLOCK = registerWithItem("frozen_honeycomb_block", new Block(
            AbstractBlock.Settings.create().mapColor(MapColor.LIGHT_BLUE).strength(0.6f).sounds(BlockSoundGroup.CORAL)));

    public static final Block COLD_HONEY_BLOCK = registerWithItem("cold_honey_block", new ColdHoneyBlock(
            AbstractBlock.Settings.create().mapColor(MapColor.LIGHT_BLUE).velocityMultiplier(0.4f)
                    .jumpVelocityMultiplier(0.5f).slipperiness(0.98f).ticksRandomly()
                    .nonOpaque().sounds(BlockSoundGroup.HONEY)));

    public static final Block FROZEN_BEE_NEST = registerWithItem("frozen_bee_nest", new FrozenHiveBlock(
            AbstractBlock.Settings.create().mapColor(MapColor.LIGHT_BLUE).strength(0.3f).sounds(BlockSoundGroup.WOOD)));

    public static final Block FROZEN_BEEHIVE = registerWithItem("frozen_beehive", new FrozenHiveBlock(
            AbstractBlock.Settings.create().mapColor(MapColor.LIGHT_BLUE).strength(0.6f).sounds(BlockSoundGroup.WOOD)));

    public static final Block COOLER = registerWithItem("cooler", new CoolerBlock(
            AbstractBlock.Settings.create().mapColor(MapColor.LIGHT_BLUE).strength(1.0f).sounds(BlockSoundGroup.GLASS)));

    private static AbstractBlock.Settings flowerSettings(MapColor color) {
        return AbstractBlock.Settings.create().mapColor(color).noCollision().breakInstantly()
                .sounds(BlockSoundGroup.GRASS).offset(AbstractBlock.OffsetType.XZ)
                .pistonBehavior(PistonBehavior.DESTROY);
    }

    private static Block registerWithItem(String name, Block block) {
        Registry.register(Registries.ITEM, FrozenBees.id(name), new BlockItem(block, new Item.Settings()));
        return Registry.register(Registries.BLOCK, FrozenBees.id(name), block);
    }

    public static void register() {}
}
