package io.github.jemyz3653.coloredm.client.dye;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;

public enum Mask implements StringRepresentable {
   WOOD("wood", 0.0F, 60.0F, 0.2F, 0.06F),
   BIRCH("birch", 15.0F, 70.0F, 0.12F, 0.1F),
   TERRACOTTA("terracotta", 0.0F, 45.0F, 0.2F, 0.06F),
   PICKLE("pickle", 40.0F, 170.0F, 0.1F, 0.1F),
   FLAME("flame", 0.0F, 75.0F, 0.2F, 0.45F),
   WATER("water", 170.0F, 260.0F, 0.25F, 0.1F),
   LEATHER("leather", 0.0F, 50.0F, 0.25F, 0.06F),
   ALL("all", 0.0F, 360.0F, 0.0F, 0.0F);

   public static final Codec<Mask> CODEC = StringRepresentable.fromEnum(Mask::values);
   private final String name;
   final float hueMin;
   final float hueMax;
   final float satMin;
   final float valMin;

   Mask(String nullxx, float nullxxx, float nullxxxx, float nullxxxxx, float nullxxxxxx) {
      this.name = nullxx;
      this.hueMin = nullxxx;
      this.hueMax = nullxxxx;
      this.satMin = nullxxxxx;
      this.valMin = nullxxxxxx;
   }

   @Override
   public String getSerializedName() {
      return this.name;
   }

   boolean test(float var1, float var2, float var3) {
      float var4 = Math.max(var1, Math.max(var2, var3));
      float var5 = Math.min(var1, Math.min(var2, var3));
      float var6 = var4;
      float var7 = var4 <= 0.0F ? 0.0F : (var4 - var5) / var4;
      if (!(var7 < this.satMin) && !(var6 < this.valMin)) {
         float var9 = var4 - var5;
         float var8;
         if (var9 <= 0.0F) {
            var8 = 0.0F;
         } else if (var4 == var1) {
            var8 = 60.0F * ((var2 - var3) / var9 % 6.0F);
         } else if (var4 == var2) {
            var8 = 60.0F * ((var3 - var1) / var9 + 2.0F);
         } else {
            var8 = 60.0F * ((var1 - var2) / var9 + 4.0F);
         }

         if (var8 < 0.0F) {
            var8 += 360.0F;
         }

         return var8 >= this.hueMin && var8 <= this.hueMax;
      } else {
         return false;
      }
   }
}
