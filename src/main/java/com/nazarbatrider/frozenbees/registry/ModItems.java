package com.nazarbatrider.frozenbees.registry;

import com.nazarbatrider.frozenbees.FrozenBees;
import com.nazarbatrider.frozenbees.item.ColdHoneyBottleItem;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModItems {
    public static final Item FROZEN_HONEYCOMB = register("frozen_honeycomb", new Item(new Item.Settings()));

    public static final Item COLD_HONEY_BOTTLE = register("cold_honey_bottle", new ColdHoneyBottleItem(
            new Item.Settings().maxCount(16).recipeRemainder(Items.GLASS_BOTTLE)
                    .food(new FoodComponent.Builder().nutrition(4).saturationModifier(0.1f).alwaysEdible()
                            .statusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 1200), 1.0f)
                            .build())));

    private static Item register(String name, Item item) {
        return Registry.register(Registries.ITEM, FrozenBees.id(name), item);
    }

    public static void register() {}
}
