package io.github.jemyz3653.coloredm.client;

import net.fabricmc.fabric.api.client.rendering.v1.RenderStateDataKey;
import net.minecraft.world.item.DyeColor;

public final class ColorKeys {
   public static final RenderStateDataKey<DyeColor> COLOR = RenderStateDataKey.create(() -> "coloredm:color");

   private ColorKeys() {
   }
}
