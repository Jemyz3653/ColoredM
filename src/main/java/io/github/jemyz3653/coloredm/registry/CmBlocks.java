package io.github.jemyz3653.coloredm.registry;

import io.github.jemyz3653.coloredm.CmIds;
import io.github.jemyz3653.coloredm.Colored;
import io.github.jemyz3653.coloredm.block.ColoredBarrelBlock;
import io.github.jemyz3653.coloredm.block.ColoredChestBlock;
import io.github.jemyz3653.coloredm.block.ColoredCraftingTableBlock;
import io.github.jemyz3653.coloredm.block.ColoredDecoratedPotBlock;
import io.github.jemyz3653.coloredm.block.ColoredDoorBlock;
import io.github.jemyz3653.coloredm.block.ColoredFlowerPotBlock;
import io.github.jemyz3653.coloredm.block.ColoredLadderBlock;
import io.github.jemyz3653.coloredm.block.ColoredLanternBlock;
import io.github.jemyz3653.coloredm.block.ColoredPottedBlock;
import io.github.jemyz3653.coloredm.block.ColoredSandBlock;
import io.github.jemyz3653.coloredm.block.ColoredSeaPickleBlock;
import io.github.jemyz3653.coloredm.block.ColoredTrapDoorBlock;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
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
   private static final List<ColoredPottedBlock> MODDED_POTTED = new ArrayList<>();

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

   public static List<ColoredPottedBlock> moddedPotted() {
      synchronized (MODDED_POTTED) {
         return List.copyOf(MODDED_POTTED);
      }
   }

   public static void registerModdedPots() {
      ArrayList<FlowerPotBlock> var0 = new ArrayList<>();

      for (Block var2 : BuiltInRegistries.BLOCK) {
         if (isModdedPot(BuiltInRegistries.BLOCK.getKey(var2), var2)) {
            var0.add((FlowerPotBlock)var2);
         }
      }

      for (FlowerPotBlock var4 : var0) {
         registerModdedPot(BuiltInRegistries.BLOCK.getKey(var4), var4);
      }

      RegistryEntryAddedCallback.event(BuiltInRegistries.BLOCK).register((var0x, var1, var2x) -> {
         if (isModdedPot(var1, var2x)) {
            registerModdedPot(var1, (FlowerPotBlock)var2x);
         }
      });
   }

   private static boolean isModdedPot(Identifier var0, Block var1) {
      return var1 instanceof FlowerPotBlock var2
         && var2.getPotted() != Blocks.AIR
         && !var0.getNamespace().equals("minecraft")
         && !var0.getNamespace().equals("coloredm")
         && !POTTED_BY_VANILLA.containsKey(var1);
   }

   private static void registerModdedPot(Identifier var0, FlowerPotBlock var1) {
      ColoredPottedBlock var2 = (ColoredPottedBlock)register(
         var0.getNamespace() + "/" + var0.getPath(),
         var1x -> new ColoredPottedBlock(var1.getPotted(), var1, true, var1x),
         Properties.ofFullCopy(var1).noLootTable()
      );
      POTTED_BY_VANILLA.put(var1, var2);
      POTTED_BY_PLANT.putIfAbsent(var1.getPotted(), var2);
      synchronized (MODDED_POTTED) {
         MODDED_POTTED.add(var2);
      }
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
      family(Family.DOOR, Blocks.OAK_DOOR, true, ColoredDoorBlock::new);
      family(Family.TRAPDOOR, Blocks.OAK_TRAPDOOR, true, ColoredTrapDoorBlock::new);
      family(Family.SAND, Blocks.SAND, true, ColoredSandBlock::new);
      family(Family.ACACIA_DOOR, Blocks.ACACIA_DOOR, true, (color, properties) -> new ColoredDoorBlock(color, ((net.minecraft.world.level.block.DoorBlock)Blocks.ACACIA_DOOR).type(), properties));
      family(Family.ACACIA_TRAPDOOR, Blocks.ACACIA_TRAPDOOR, true, (color, properties) -> new ColoredTrapDoorBlock(color, net.minecraft.world.level.block.state.properties.BlockSetType.ACACIA, properties));
      family(Family.BAMBOO_DOOR, Blocks.BAMBOO_DOOR, true, (color, properties) -> new ColoredDoorBlock(color, ((net.minecraft.world.level.block.DoorBlock)Blocks.BAMBOO_DOOR).type(), properties));
      family(Family.BAMBOO_TRAPDOOR, Blocks.BAMBOO_TRAPDOOR, true, (color, properties) -> new ColoredTrapDoorBlock(color, net.minecraft.world.level.block.state.properties.BlockSetType.BAMBOO, properties));
      family(Family.BIRCH_DOOR, Blocks.BIRCH_DOOR, true, (color, properties) -> new ColoredDoorBlock(color, ((net.minecraft.world.level.block.DoorBlock)Blocks.BIRCH_DOOR).type(), properties));
      family(Family.BIRCH_TRAPDOOR, Blocks.BIRCH_TRAPDOOR, true, (color, properties) -> new ColoredTrapDoorBlock(color, net.minecraft.world.level.block.state.properties.BlockSetType.BIRCH, properties));
      family(Family.CHERRY_DOOR, Blocks.CHERRY_DOOR, true, (color, properties) -> new ColoredDoorBlock(color, ((net.minecraft.world.level.block.DoorBlock)Blocks.CHERRY_DOOR).type(), properties));
      family(Family.CHERRY_TRAPDOOR, Blocks.CHERRY_TRAPDOOR, true, (color, properties) -> new ColoredTrapDoorBlock(color, net.minecraft.world.level.block.state.properties.BlockSetType.CHERRY, properties));
      family(Family.COPPER_DOOR, BuiltInRegistries.BLOCK.getValue(Identifier.fromNamespaceAndPath("minecraft", "copper_door")), true, (color, properties) -> new ColoredDoorBlock(color, ((net.minecraft.world.level.block.DoorBlock)BuiltInRegistries.BLOCK.getValue(Identifier.fromNamespaceAndPath("minecraft", "copper_door"))).type(), properties));
      family(Family.COPPER_TRAPDOOR, BuiltInRegistries.BLOCK.getValue(Identifier.fromNamespaceAndPath("minecraft", "copper_trapdoor")), true, (color, properties) -> new ColoredTrapDoorBlock(color, net.minecraft.world.level.block.state.properties.BlockSetType.COPPER, properties));
      family(Family.CRIMSON_DOOR, Blocks.CRIMSON_DOOR, true, (color, properties) -> new ColoredDoorBlock(color, ((net.minecraft.world.level.block.DoorBlock)Blocks.CRIMSON_DOOR).type(), properties));
      family(Family.CRIMSON_TRAPDOOR, Blocks.CRIMSON_TRAPDOOR, true, (color, properties) -> new ColoredTrapDoorBlock(color, net.minecraft.world.level.block.state.properties.BlockSetType.CRIMSON, properties));
      family(Family.DARK_OAK_DOOR, Blocks.DARK_OAK_DOOR, true, (color, properties) -> new ColoredDoorBlock(color, ((net.minecraft.world.level.block.DoorBlock)Blocks.DARK_OAK_DOOR).type(), properties));
      family(Family.DARK_OAK_TRAPDOOR, Blocks.DARK_OAK_TRAPDOOR, true, (color, properties) -> new ColoredTrapDoorBlock(color, net.minecraft.world.level.block.state.properties.BlockSetType.DARK_OAK, properties));
      family(Family.EXPOSED_COPPER_DOOR, BuiltInRegistries.BLOCK.getValue(Identifier.fromNamespaceAndPath("minecraft", "exposed_copper_door")), true, (color, properties) -> new ColoredDoorBlock(color, ((net.minecraft.world.level.block.DoorBlock)BuiltInRegistries.BLOCK.getValue(Identifier.fromNamespaceAndPath("minecraft", "exposed_copper_door"))).type(), properties));
      family(Family.EXPOSED_COPPER_TRAPDOOR, BuiltInRegistries.BLOCK.getValue(Identifier.fromNamespaceAndPath("minecraft", "exposed_copper_trapdoor")), true, (color, properties) -> new ColoredTrapDoorBlock(color, net.minecraft.world.level.block.state.properties.BlockSetType.COPPER, properties));
      family(Family.IRON_DOOR, Blocks.IRON_DOOR, true, (color, properties) -> new ColoredDoorBlock(color, ((net.minecraft.world.level.block.DoorBlock)Blocks.IRON_DOOR).type(), properties));
      family(Family.IRON_TRAPDOOR, Blocks.IRON_TRAPDOOR, true, (color, properties) -> new ColoredTrapDoorBlock(color, net.minecraft.world.level.block.state.properties.BlockSetType.IRON, properties));
      family(Family.JUNGLE_DOOR, Blocks.JUNGLE_DOOR, true, (color, properties) -> new ColoredDoorBlock(color, ((net.minecraft.world.level.block.DoorBlock)Blocks.JUNGLE_DOOR).type(), properties));
      family(Family.JUNGLE_TRAPDOOR, Blocks.JUNGLE_TRAPDOOR, true, (color, properties) -> new ColoredTrapDoorBlock(color, net.minecraft.world.level.block.state.properties.BlockSetType.JUNGLE, properties));
      family(Family.MANGROVE_DOOR, Blocks.MANGROVE_DOOR, true, (color, properties) -> new ColoredDoorBlock(color, ((net.minecraft.world.level.block.DoorBlock)Blocks.MANGROVE_DOOR).type(), properties));
      family(Family.MANGROVE_TRAPDOOR, Blocks.MANGROVE_TRAPDOOR, true, (color, properties) -> new ColoredTrapDoorBlock(color, net.minecraft.world.level.block.state.properties.BlockSetType.MANGROVE, properties));
      family(Family.OXIDIZED_COPPER_DOOR, BuiltInRegistries.BLOCK.getValue(Identifier.fromNamespaceAndPath("minecraft", "oxidized_copper_door")), true, (color, properties) -> new ColoredDoorBlock(color, ((net.minecraft.world.level.block.DoorBlock)BuiltInRegistries.BLOCK.getValue(Identifier.fromNamespaceAndPath("minecraft", "oxidized_copper_door"))).type(), properties));
      family(Family.OXIDIZED_COPPER_TRAPDOOR, BuiltInRegistries.BLOCK.getValue(Identifier.fromNamespaceAndPath("minecraft", "oxidized_copper_trapdoor")), true, (color, properties) -> new ColoredTrapDoorBlock(color, net.minecraft.world.level.block.state.properties.BlockSetType.COPPER, properties));
      family(Family.PALE_OAK_DOOR, Blocks.PALE_OAK_DOOR, true, (color, properties) -> new ColoredDoorBlock(color, ((net.minecraft.world.level.block.DoorBlock)Blocks.PALE_OAK_DOOR).type(), properties));
      family(Family.PALE_OAK_TRAPDOOR, Blocks.PALE_OAK_TRAPDOOR, true, (color, properties) -> new ColoredTrapDoorBlock(color, net.minecraft.world.level.block.state.properties.BlockSetType.PALE_OAK, properties));
      family(Family.POPLAR_DOOR, Blocks.POPLAR_DOOR, true, (color, properties) -> new ColoredDoorBlock(color, ((net.minecraft.world.level.block.DoorBlock)Blocks.POPLAR_DOOR).type(), properties));
      family(Family.POPLAR_TRAPDOOR, Blocks.POPLAR_TRAPDOOR, true, (color, properties) -> new ColoredTrapDoorBlock(color, net.minecraft.world.level.block.state.properties.BlockSetType.POPLAR, properties));
      family(Family.SPRUCE_DOOR, Blocks.SPRUCE_DOOR, true, (color, properties) -> new ColoredDoorBlock(color, ((net.minecraft.world.level.block.DoorBlock)Blocks.SPRUCE_DOOR).type(), properties));
      family(Family.SPRUCE_TRAPDOOR, Blocks.SPRUCE_TRAPDOOR, true, (color, properties) -> new ColoredTrapDoorBlock(color, net.minecraft.world.level.block.state.properties.BlockSetType.SPRUCE, properties));
      family(Family.WARPED_DOOR, Blocks.WARPED_DOOR, true, (color, properties) -> new ColoredDoorBlock(color, ((net.minecraft.world.level.block.DoorBlock)Blocks.WARPED_DOOR).type(), properties));
      family(Family.WARPED_TRAPDOOR, Blocks.WARPED_TRAPDOOR, true, (color, properties) -> new ColoredTrapDoorBlock(color, net.minecraft.world.level.block.state.properties.BlockSetType.WARPED, properties));
      family(Family.WAXED_COPPER_DOOR, BuiltInRegistries.BLOCK.getValue(Identifier.fromNamespaceAndPath("minecraft", "waxed_copper_door")), true, (color, properties) -> new ColoredDoorBlock(color, ((net.minecraft.world.level.block.DoorBlock)BuiltInRegistries.BLOCK.getValue(Identifier.fromNamespaceAndPath("minecraft", "waxed_copper_door"))).type(), properties));
      family(Family.WAXED_COPPER_TRAPDOOR, BuiltInRegistries.BLOCK.getValue(Identifier.fromNamespaceAndPath("minecraft", "waxed_copper_trapdoor")), true, (color, properties) -> new ColoredTrapDoorBlock(color, net.minecraft.world.level.block.state.properties.BlockSetType.COPPER, properties));
      family(Family.WAXED_EXPOSED_COPPER_DOOR, BuiltInRegistries.BLOCK.getValue(Identifier.fromNamespaceAndPath("minecraft", "waxed_exposed_copper_door")), true, (color, properties) -> new ColoredDoorBlock(color, ((net.minecraft.world.level.block.DoorBlock)BuiltInRegistries.BLOCK.getValue(Identifier.fromNamespaceAndPath("minecraft", "waxed_exposed_copper_door"))).type(), properties));
      family(Family.WAXED_EXPOSED_COPPER_TRAPDOOR, BuiltInRegistries.BLOCK.getValue(Identifier.fromNamespaceAndPath("minecraft", "waxed_exposed_copper_trapdoor")), true, (color, properties) -> new ColoredTrapDoorBlock(color, net.minecraft.world.level.block.state.properties.BlockSetType.COPPER, properties));
      family(Family.WAXED_OXIDIZED_COPPER_DOOR, BuiltInRegistries.BLOCK.getValue(Identifier.fromNamespaceAndPath("minecraft", "waxed_oxidized_copper_door")), true, (color, properties) -> new ColoredDoorBlock(color, ((net.minecraft.world.level.block.DoorBlock)BuiltInRegistries.BLOCK.getValue(Identifier.fromNamespaceAndPath("minecraft", "waxed_oxidized_copper_door"))).type(), properties));
      family(Family.WAXED_OXIDIZED_COPPER_TRAPDOOR, BuiltInRegistries.BLOCK.getValue(Identifier.fromNamespaceAndPath("minecraft", "waxed_oxidized_copper_trapdoor")), true, (color, properties) -> new ColoredTrapDoorBlock(color, net.minecraft.world.level.block.state.properties.BlockSetType.COPPER, properties));
      family(Family.WAXED_WEATHERED_COPPER_DOOR, BuiltInRegistries.BLOCK.getValue(Identifier.fromNamespaceAndPath("minecraft", "waxed_weathered_copper_door")), true, (color, properties) -> new ColoredDoorBlock(color, ((net.minecraft.world.level.block.DoorBlock)BuiltInRegistries.BLOCK.getValue(Identifier.fromNamespaceAndPath("minecraft", "waxed_weathered_copper_door"))).type(), properties));
      family(Family.WAXED_WEATHERED_COPPER_TRAPDOOR, BuiltInRegistries.BLOCK.getValue(Identifier.fromNamespaceAndPath("minecraft", "waxed_weathered_copper_trapdoor")), true, (color, properties) -> new ColoredTrapDoorBlock(color, net.minecraft.world.level.block.state.properties.BlockSetType.COPPER, properties));
      family(Family.WEATHERED_COPPER_DOOR, BuiltInRegistries.BLOCK.getValue(Identifier.fromNamespaceAndPath("minecraft", "weathered_copper_door")), true, (color, properties) -> new ColoredDoorBlock(color, ((net.minecraft.world.level.block.DoorBlock)BuiltInRegistries.BLOCK.getValue(Identifier.fromNamespaceAndPath("minecraft", "weathered_copper_door"))).type(), properties));
      family(Family.WEATHERED_COPPER_TRAPDOOR, BuiltInRegistries.BLOCK.getValue(Identifier.fromNamespaceAndPath("minecraft", "weathered_copper_trapdoor")), true, (color, properties) -> new ColoredTrapDoorBlock(color, net.minecraft.world.level.block.state.properties.BlockSetType.COPPER, properties));
      ArrayList<FlowerPotBlock> var0 = new ArrayList<>();

      for (Block var2 : BuiltInRegistries.BLOCK) {
         if (var2 instanceof FlowerPotBlock var3 && var3.getPotted() != Blocks.AIR && BuiltInRegistries.BLOCK.getKey(var2).getNamespace().equals("minecraft")) {
            var0.add(var3);
         }
      }

      for (FlowerPotBlock var6 : var0) {
         String var7 = BuiltInRegistries.BLOCK.getKey(var6).getPath();
         ColoredPottedBlock var4 = (ColoredPottedBlock)register(
            var7, var1 -> new ColoredPottedBlock(var6.getPotted(), var6, false, var1), Properties.ofFullCopy(var6)
         );
         POTTED_BY_VANILLA.put(var6, var4);
         POTTED_BY_PLANT.put(var6.getPotted(), var4);
      }
   }
}
