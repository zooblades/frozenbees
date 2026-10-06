package com.nazarbatrider.frozenbees.client;

import com.nazarbatrider.frozenbees.registry.ModBlocks;
import com.nazarbatrider.frozenbees.registry.ModEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.RenderLayer;

public class FrozenBeesClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(ModEntities.FROZEN_BEE, FrozenBeeRenderer::new);

        // Прозрачные пиксели цветов вырезаются, мёд полупрозрачный
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.SNOWDROP, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.FROSTBLOOM, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.COLD_HONEY_BLOCK, RenderLayer.getTranslucent());
    }
}
