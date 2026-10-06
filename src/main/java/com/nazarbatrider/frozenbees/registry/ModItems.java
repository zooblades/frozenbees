package com.nazarbatrider.frozenbees.registry;

import com.nazarbatrider.frozenbees.FrozenBees;
import com.nazarbatrider.frozenbees.item.ColdHoneyBottleItem;
import java.util.List;
import java.util.Optional;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModItems {
    public static final Item FROZEN_HONEYCOMB = register("frozen_honeycomb", new Item(new Item.Settings()));

    public static final Item COLD_HONEY_BOTTLE = register("cold_honey_bottle", new ColdHoneyBottleItem(
            new Item.Settings().maxCount(16).recipeRemainder(Items.GLASS_BOTTLE)
                    .food(new FoodComponent.Builder().nutrition(3).saturationModifier(0.1f).alwaysEdible()
                            .statusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 600), 1.0f)
                            .build())));

    public static final Item FROZEN_BEE_SPAWN_EGG = register("frozen_bee_spawn_egg",
            new SpawnEggItem(ModEntities.FROZEN_BEE, 0xA8DCF5, 0xFFFFFF, new Item.Settings()));

    public static final Item FROZEN_ARROW = register("frozen_arrow", new ArrowItem(new Item.Settings()
            .component(DataComponentTypes.POTION_CONTENTS, new PotionContentsComponent(Optional.empty(), Optional.of(0xA8DCF5),
                    List.of(new StatusEffectInstance(StatusEffects.SLOWNESS, 160, 1))))));

    private static Item register(String name, Item item) {
        return Registry.register(Registries.ITEM, FrozenBees.id(name), item);
    }

    public static void register() {}
}
