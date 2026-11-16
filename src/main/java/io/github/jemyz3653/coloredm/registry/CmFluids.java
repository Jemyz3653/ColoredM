package io.github.jemyz3653.coloredm.registry;

import io.github.jemyz3653.coloredm.CmIds;
import io.github.jemyz3653.coloredm.block.ColoredLiquidBlock;
import io.github.jemyz3653.coloredm.fluid.ColoredWaterFluid;
import java.util.EnumMap;
import java.util.Map;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public final class CmFluids {
   private static final Map<DyeColor, ColoredWaterFluid> SOURCES = new EnumMap<>(DyeColor.class);
   private static final Map<DyeColor, ColoredWaterFluid> FLOWING = new EnumMap<>(DyeColor.class);
   private static final Map<DyeColor, LiquidBlock> BLOCKS = new EnumMap<>(DyeColor.class);
   private static final Map<DyeColor, Item> BUCKETS = new EnumMap<>(DyeColor.class);

   private CmFluids() {
   }

   public static ColoredWaterFluid source(DyeColor var0) {
      return SOURCES.get(var0);
   }

   public static ColoredWaterFluid flowing(DyeColor var0) {
      return FLOWING.get(var0);
   }

   public static LiquidBlock block(DyeColor var0) {
      return BLOCKS.get(var0);
   }

   public static Item bucket(DyeColor var0) {
      return BUCKETS.get(var0);
   }

   static void putBucket(DyeColor var0, Item var1) {
      BUCKETS.put(var0, var1);
   }

   public static void init() {
   }

   static {
      for (DyeColor var3 : DyeColor.values()) {
         SOURCES.put(var3, Registry.register(BuiltInRegistries.FLUID, CmIds.id(CmIds.colored(var3, "water")), new ColoredWaterFluid.Source(var3)));
         FLOWING.put(var3, Registry.register(BuiltInRegistries.FLUID, CmIds.id("flowing_" + CmIds.colored(var3, "water")), new ColoredWaterFluid.Flowing(var3)));
      }

      for (DyeColor var7 : DyeColor.values()) {
         BLOCKS.put(
            var7,
            (LiquidBlock)CmBlocks.register(
               CmIds.colored(var7, "water"),
               var1 -> new ColoredLiquidBlock(var7, SOURCES.get(var7), var1),
               Properties.ofFullCopy(Blocks.WATER).mapColor(var7.getMapColor())
            )
         );
      }
   }
}
