package com.nazarbatrider.frozenbees.client;

import com.nazarbatrider.frozenbees.registry.ModEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class FrozenBeesClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(ModEntities.FROZEN_BEE, FrozenBeeRenderer::new);
    }
}
