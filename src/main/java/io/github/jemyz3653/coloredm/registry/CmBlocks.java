package io.github.jemyz3653.coloredm.registry;

import io.github.jemyz3653.coloredm.CmIds;
import io.github.jemyz3653.coloredm.Colored;
import io.github.jemyz3653.coloredm.block.ColoredBarrelBlock;
import io.github.jemyz3653.coloredm.block.ColoredChestBlock;
import io.github.jemyz3653.coloredm.block.ColoredCraftingTableBlock;
import io.github.jemyz3653.coloredm.block.ColoredDecoratedPotBlock;
import io.github.jemyz3653.coloredm.block.ColoredFlowerPotBlock;
import io.github.jemyz3653.coloredm.block.ColoredLadderBlock;
import io.github.jemyz3653.coloredm.block.ColoredLanternBlock;
import io.github.jemyz3653.coloredm.block.ColoredPottedBlock;
import io.github.jemyz3653.coloredm.block.ColoredSeaPickleBlock;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import org.jspecify.annotations.Nullable;

public final class CmBlocks {
   private static final Map<Family, Map<DyeColor, Block>> FAMILIES = new EnumMap<>(Family.class);
   private static final Map<Block, Family> FAMILY_OF = new IdentityHashMap<>();
   private static final Map<Block, ColoredPottedBlock> POTTED_BY_VANILLA = new LinkedHashMap<>();
   private static final Map<Block, ColoredPottedBlock> POTTED_BY_PLANT = new IdentityHashMap<>();

   private CmBlocks() {
   }

   private static void family(Family var0, Block var1, boolean var2, BiFunction<DyeColor, Properties, Block> var3) {
      EnumMap var4 = new EnumMap<>(DyeColor.class);

      for (DyeColor var8 : DyeColor.values()) {
         Properties var9 = Properties.ofFullCopy(var1);
         if (var2) {
            var9.mapColor(var8.getMapColor());
         }

         Block var10 = register(CmIds.colored(var8, var0.id()), var2x -> (Block)var3.apply(var8, var2x), var9);
         var4.put(var8, var10);
         FAMILY_OF.put(var10, var0);
      }

      FAMILY_OF.put(var1, var0);
      FAMILIES.put(var0, Collections.unmodifiableMap(var4));
   }

   static Block register(String var0, Function<Properties, Block> var1, Properties var2) {
      ResourceKey var3 = ResourceKey.create(Registries.BLOCK, CmIds.id(var0));
      Block var4 = (Block)var1.apply(var2.setId(var3));
      return Registry.register(BuiltInRegistries.BLOCK, var3, var4);
   }

   public static Block get(Family var0, DyeColor var1) {
      return FAMILIES.get(var0).get(var1);
   }

   public static Map<DyeColor, Block> family(Family var0) {
      return FAMILIES.get(var0);
   }

   public static @Nullable Family familyOf(Block var0) {
      return FAMILY_OF.get(var0);
   }

   public static @Nullable DyeColor colorOf(Block var0) {
      return var0 instanceof Colored var1 ? var1.color() : null;
   }

   public static Block flowerPot(DyeColor var0) {
      return get(Family.FLOWER_POT, var0);
   }

   public static @Nullable ColoredPottedBlock pottedFor(Block var0) {
      return POTTED_BY_PLANT.get(var0);
   }

   public static @Nullable ColoredPottedBlock pottedForVanilla(Block var0) {
      return POTTED_BY_VANILLA.get(var0);
   }

   public static Map<Block, ColoredPottedBlock> potted() {
      return Collections.unmodifiableMap(POTTED_BY_VANILLA);
   }

   public static void init() {
   }

   static {
      family(Family.FLOWER_POT, Blocks.FLOWER_POT, true, ColoredFlowerPotBlock::new);
      family(Family.DECORATED_POT, Blocks.DECORATED_POT, true, ColoredDecoratedPotBlock::new);
      family(Family.CHEST, Blocks.CHEST, true, ColoredChestBlock::new);
      family(Family.BARREL, Blocks.BARREL, true, ColoredBarrelBlock::new);
      family(Family.CRAFTING_TABLE, Blocks.CRAFTING_TABLE, true, ColoredCraftingTableBlock::new);
      family(Family.LADDER, Blocks.LADDER, true, ColoredLadderBlock::new);
      family(Family.LANTERN, Blocks.LANTERN, false, ColoredLanternBlock::new);
      family(Family.SEA_PICKLE, Blocks.SEA_PICKLE, true, ColoredSeaPickleBlock::new);
      ArrayList<FlowerPotBlock> var0 = new ArrayList<>();

      for (Block var2 : BuiltInRegistries.BLOCK) {
         if (var2 instanceof FlowerPotBlock var3 && var3.getPotted() != Blocks.AIR && BuiltInRegistries.BLOCK.getKey(var2).getNamespace().equals("minecraft")) {
            var0.add(var3);
         }
      }

      for (FlowerPotBlock var6 : var0) {
         String var7 = BuiltInRegistries.BLOCK.getKey(var6).getPath();
         ColoredPottedBlock var4 = (ColoredPottedBlock)register(var7, var1 -> new ColoredPottedBlock(var6.getPotted(), var6, var1), Properties.ofFullCopy(var6));
         POTTED_BY_VANILLA.put(var6, var4);
         POTTED_BY_PLANT.put(var6.getPotted(), var4);
      }
   }
}
