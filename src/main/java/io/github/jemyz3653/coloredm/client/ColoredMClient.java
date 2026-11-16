package io.github.jemyz3653.coloredm.client;

import io.github.jemyz3653.coloredm.CmIds;
import io.github.jemyz3653.coloredm.client.dye.DyedSpriteSource;
import io.github.jemyz3653.coloredm.registry.CmEntities;
import io.github.jemyz3653.coloredm.registry.CmFluids;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderingRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.SpriteSourceRegistry;
import net.minecraft.client.color.block.BlockTintSources;
import net.minecraft.client.renderer.block.FluidModel.Unbaked;
import net.minecraft.client.renderer.entity.ItemFrameRenderer;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.DyeColor;

public final class ColoredMClient implements ClientModInitializer {
   @Override
   public void onInitializeClient() {
      SpriteSourceRegistry.register(CmIds.id("dyed"), DyedSpriteSource.MAP_CODEC);

      for (DyeColor var4 : DyeColor.values()) {
         EntityRendererRegistry.register(CmEntities.itemFrame(var4), ItemFrameRenderer::new);
         FluidRenderingRegistry.register(
            CmFluids.source(var4),
            CmFluids.flowing(var4),
            new Unbaked(
               new Material(Identifier.withDefaultNamespace("block/water_still")),
               new Material(Identifier.withDefaultNamespace("block/water_flow")),
               new Material(Identifier.withDefaultNamespace("block/water_overlay")),
               BlockTintSources.constant(waterTint(var4))
            )
         );
      }
   }

   static int waterTint(DyeColor var0) {
      int var1 = var0.getTextureDiffuseColor();
      int var2 = var1 >> 16 & 0xFF;
      int var3 = var1 >> 8 & 0xFF;
      int var4 = var1 & 0xFF;
      var2 += (255 - var2) / 8;
      var3 += (255 - var3) / 8;
      var4 += (255 - var4) / 8;
      return 0xFF000000 | var2 << 16 | var3 << 8 | var4;
   }
}
