package io.github.jemyz3653.coloredm.client.dye;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.texture.TextureContents;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.DyeColor;
import org.jspecify.annotations.Nullable;

public class DyedTexture extends SimpleTexture {
   private static final String PREFIX = "textures/entity/equipment/";
   private static final DyeColor[] LONGEST_NAME_FIRST = Arrays.stream(DyeColor.values())
      .sorted(Comparator.<DyeColor>comparingInt(var0 -> var0.getName().length()).reversed())
      .toArray(DyeColor[]::new);
   private final Identifier base;
   private final DyeColor color;
   private final Mask mask;

   private DyedTexture(Identifier var1, Identifier var2, DyeColor var3, Mask var4) {
      super(var1);
      this.base = var2;
      this.color = var3;
      this.mask = var4;
   }

   public static @Nullable DyedTexture forLocation(Identifier var0) {
      String var1 = var0.getPath();
      if (var0.getNamespace().equals("coloredm") && var1.startsWith("textures/entity/equipment/") && var1.endsWith(".png")) {
         int var2 = var1.lastIndexOf(47);
         String var3 = var1.substring(0, var2 + 1);
         String var4 = var1.substring(var2 + 1);

         for (DyeColor var8 : LONGEST_NAME_FIRST) {
            String var9 = var8.getName() + "_";
            if (var4.startsWith(var9)) {
               String var10 = var4.substring(var9.length());
               Mask var11 = var10.startsWith("saddle") ? Mask.LEATHER : Mask.ALL;
               return new DyedTexture(var0, Identifier.withDefaultNamespace(var3 + var10), var8, var11);
            }
         }

         return null;
      } else {
         return null;
      }
   }

   @Override
   public TextureContents loadContents(ResourceManager var1) throws IOException {
      TextureContents var2 = TextureContents.load(var1, this.base);

      try {
         Dyer.dye(var2.image(), this.color, this.mask);
      } catch (IllegalArgumentException var4) {
      }

      return var2;
   }
}
