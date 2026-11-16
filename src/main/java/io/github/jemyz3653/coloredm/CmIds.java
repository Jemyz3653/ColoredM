package io.github.jemyz3653.coloredm;

import net.minecraft.resources.Identifier;
import net.minecraft.world.item.DyeColor;

public final class CmIds {
   public static final String MOD_ID = "coloredm";

   private CmIds() {
   }

   public static Identifier id(String var0) {
      return Identifier.fromNamespaceAndPath("coloredm", var0);
   }

   public static String colored(DyeColor var0, String var1) {
      return var0.getName() + "_" + var1;
   }
}
