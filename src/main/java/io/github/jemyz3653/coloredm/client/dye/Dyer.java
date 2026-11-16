package io.github.jemyz3653.coloredm.client.dye;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.world.item.DyeColor;

public final class Dyer {
   private Dyer() {
   }

   private static float luminance(float var0, float var1, float var2) {
      return 0.299F * var0 + 0.587F * var1 + 0.114F * var2;
   }

   public static void dye(NativeImage var0, DyeColor var1, Mask var2) {
      dye(var0, var1.getTextureDiffuseColor(), var2);
   }

   public static void dye(NativeImage var0, int var1, Mask var2) {
      int var3 = var0.getWidth();
      int var4 = var0.getHeight();
      boolean[] var5 = new boolean[var3 * var4];
      double var6 = 0.0;
      int var8 = 0;

      for (int var9 = 0; var9 < var4; var9++) {
         for (int var10 = 0; var10 < var3; var10++) {
            int var11 = var0.getPixel(var10, var9);
            if (var11 >>> 24 != 0) {
               float var12 = (var11 >> 16 & 0xFF) / 255.0F;
               float var13 = (var11 >> 8 & 0xFF) / 255.0F;
               float var14 = (var11 & 0xFF) / 255.0F;
               if (var2.test(var12, var13, var14)) {
                  var5[var9 * var3 + var10] = true;
                  var6 += luminance(var12, var13, var14);
                  var8++;
               }
            }
         }
      }

      if (var8 != 0) {
         float var24 = (float)Math.max(var6 / var8, 1.0E-4);
         float var25 = (var1 >> 16 & 0xFF) / 255.0F;
         float var26 = (var1 >> 8 & 0xFF) / 255.0F;
         float var27 = (var1 & 0xFF) / 255.0F;

         for (int var28 = 0; var28 < var4; var28++) {
            for (int var29 = 0; var29 < var3; var29++) {
               if (var5[var28 * var3 + var29]) {
                  int var15 = var0.getPixel(var29, var28);
                  float var16 = (var15 >> 16 & 0xFF) / 255.0F;
                  float var17 = (var15 >> 8 & 0xFF) / 255.0F;
                  float var18 = (var15 & 0xFF) / 255.0F;
                  float var19 = luminance(var16, var17, var18) / var24;
                  float var20;
                  float var21;
                  float var22;
                  if (var19 <= 1.0F) {
                     var20 = var25 * var19;
                     var21 = var26 * var19;
                     var22 = var27 * var19;
                  } else {
                     float var23 = Math.min(1.0F, (var19 - 1.0F) * 1.1F) * 0.75F;
                     var20 = var25 + (1.0F - var25) * var23;
                     var21 = var26 + (1.0F - var26) * var23;
                     var22 = var27 + (1.0F - var27) * var23;
                  }

                  var0.setPixel(var29, var28, var15 & 0xFF000000 | channel(var20) << 16 | channel(var21) << 8 | channel(var22));
               }
            }
         }
      }
   }

   private static int channel(float var0) {
      return Math.round(Math.max(0.0F, Math.min(1.0F, var0)) * 255.0F);
   }
}
