package com.nazarbatrider.frozenbees.client;

import com.nazarbatrider.frozenbees.FrozenBees;
import net.minecraft.client.render.entity.BeeEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.util.Identifier;

/** Ванильная модель пчелы с нашими текстурами. */
public class FrozenBeeRenderer extends BeeEntityRenderer {
    private static final Identifier NORMAL = FrozenBees.id("textures/entity/frozen_bee/frozen_bee.png");
    private static final Identifier ANGRY = FrozenBees.id("textures/entity/frozen_bee/frozen_bee_angry.png");
    private static final Identifier NECTAR = FrozenBees.id("textures/entity/frozen_bee/frozen_bee_nectar.png");
    private static final Identifier ANGRY_NECTAR = FrozenBees.id("textures/entity/frozen_bee/frozen_bee_angry_nectar.png");

    public FrozenBeeRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public Identifier getTexture(BeeEntity bee) {
        boolean angry = bee.hasAngerTime();
        boolean nectar = bee.hasNectar();
        if (angry) return nectar ? ANGRY_NECTAR : ANGRY;
        return nectar ? NECTAR : NORMAL;
    }
}
