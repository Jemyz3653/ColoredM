package io.github.jemyz3653.coloredm.registry;

import io.github.jemyz3653.coloredm.CmIds;
import io.github.jemyz3653.coloredm.item.ColoredItemFrameItem;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.DoubleHighBlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.item.equipment.EquipmentAssets;
import net.minecraft.world.item.equipment.Equippable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.PotDecorations;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders;

public final class CmItems {
   private static final Map<Family, Map<DyeColor, Item>> BLOCK_ITEMS = new EnumMap<>(Family.class);
   private static final Map<DyeColor, Item> ITEM_FRAMES = new EnumMap<>(DyeColor.class);
   private static final Map<DyeColor, Item> SADDLES = new EnumMap<>(DyeColor.class);
   private static final Map<DyeColor, Item> WATER_BUCKETS = new EnumMap<>(DyeColor.class);
   private static final List<Item> ALL = new ArrayList<>();

   private CmItems() {
   }

   private static Equippable saddleEquippable(DyeColor var0) {
      ResourceKey var1 = ResourceKey.create(EquipmentAssets.ROOT_ID, CmIds.id(CmIds.colored(var0, "saddle")));
      return Equippable.builder(EquipmentSlot.SADDLE)
         .setEquipSound(SoundEvents.HORSE_SADDLE)
         .setAsset(var1)
         .setAllowedEntities(BuiltInRegistries.acquireBootstrapRegistrationLookup(BuiltInRegistries.ENTITY_TYPE).getOrThrow(EntityTypeTags.CAN_EQUIP_SADDLE))
         .setEquipOnInteract(true)
         .setCanBeSheared(true)
         .setShearingSound(SoundEvents.SADDLE_UNEQUIP)
         .build();
   }

   private static void blockItems(Family var0, UnaryOperator<Properties> var1) {
      EnumMap<DyeColor, Item> var2 = new EnumMap<>(DyeColor.class);
      BiFunction<Block, Properties, Item> factory = var0 == Family.DOOR || var0.id().endsWith("_door")
            ? DoubleHighBlockItem::new : BlockItem::new;

      for (DyeColor var6 : DyeColor.values()) {
         Block var7 = CmBlocks.get(var0, var6);
         var2.put(var6, blockItem(var7, factory, var1.apply(new Properties())));
      }

      BLOCK_ITEMS.put(var0, Collections.unmodifiableMap(var2));
   }

   private static Item blockItem(Block var0, BiFunction<Block, Properties, Item> var1, Properties var2) {
      ResourceKey var3 = ResourceKey.create(Registries.ITEM, BuiltInRegistries.BLOCK.getKey(var0));
      Item var4 = (Item)var1.apply(var0, var2.setId(var3).useBlockDescriptionPrefix());
      if (var4 instanceof BlockItem var5) {
         var5.registerBlocks(Item.BY_BLOCK, var4);
      }

      ALL.add(var4);
      return Registry.register(BuiltInRegistries.ITEM, var3, var4);
   }

   private static Item item(String var0, Function<Properties, Item> var1, Properties var2) {
      ResourceKey var3 = ResourceKey.create(Registries.ITEM, CmIds.id(var0));
      Item var4 = (Item)var1.apply(var2.setId(var3));
      ALL.add(var4);
      return Registry.register(BuiltInRegistries.ITEM, var3, var4);
   }

   public static Item get(Family var0, DyeColor var1) {
      return BLOCK_ITEMS.get(var0).get(var1);
   }

   public static Item itemFrame(DyeColor var0) {
      return ITEM_FRAMES.get(var0);
   }

   public static Item saddle(DyeColor var0) {
      return SADDLES.get(var0);
   }

   public static Map<DyeColor, Item> saddles() {
      return Collections.unmodifiableMap(SADDLES);
   }

   public static Map<DyeColor, Item> itemFrames() {
      return Collections.unmodifiableMap(ITEM_FRAMES);
   }

   public static Map<DyeColor, Item> waterBuckets() {
      return Collections.unmodifiableMap(WATER_BUCKETS);
   }

   public static List<Item> all() {
      return Collections.unmodifiableList(ALL);
   }

   public static void init() {
   }

   static {
      blockItems(Family.FLOWER_POT, var0 -> var0);
      blockItems(
         Family.DECORATED_POT,
         var0 -> var0.delayedComponent(DataComponents.POT_DECORATIONS, var0x -> PotDecorations.allBrick(var0x.lookupOrThrow(Registries.ITEM)))
            .component(DataComponents.CONTAINER, ItemContainerContents.EMPTY)
      );
      blockItems(
         Family.CHEST, var0 -> var0.component(DataComponents.CONTAINER, ItemContainerContents.EMPTY).cookingFuel(ContextIntProviders.COOKING_TIME_WOOD_BLOCKS)
      );
      blockItems(
         Family.BARREL, var0 -> var0.component(DataComponents.CONTAINER, ItemContainerContents.EMPTY).cookingFuel(ContextIntProviders.COOKING_TIME_WOOD_BLOCKS)
      );
      blockItems(Family.CRAFTING_TABLE, var0 -> var0.cookingFuel(ContextIntProviders.COOKING_TIME_WOOD_BLOCKS));
      blockItems(Family.LADDER, var0 -> var0.cookingFuel(ContextIntProviders.COOKING_TIME_WOOD_BLOCKS));
      blockItems(Family.LANTERN, var0 -> var0);
      blockItems(Family.SEA_PICKLE, var0 -> var0.compostable(ContextIntProviders.COMPOSTABLE_MEDIUM));
      blockItems(Family.DOOR, properties -> properties.cookingFuel(ContextIntProviders.COOKING_TIME_WOOD_BLOCKS));
      blockItems(Family.TRAPDOOR, properties -> properties.cookingFuel(ContextIntProviders.COOKING_TIME_WOOD_BLOCKS));
      blockItems(Family.SAND, UnaryOperator.identity());
      for (Family family : Family.values()) {
         if (!BLOCK_ITEMS.containsKey(family)) blockItems(family, UnaryOperator.identity());
      }

      for (DyeColor var3 : DyeColor.values()) {
         ITEM_FRAMES.put(
            var3, item(CmIds.colored(var3, "item_frame"), var1 -> new ColoredItemFrameItem(var3, CmEntities.itemFrame(var3), var1), new Properties())
         );
      }

      for (DyeColor var11 : DyeColor.values()) {
         SADDLES.put(
            var11, item(CmIds.colored(var11, "saddle"), Item::new, new Properties().stacksTo(1).component(DataComponents.EQUIPPABLE, saddleEquippable(var11)))
         );
      }

      for (DyeColor var12 : DyeColor.values()) {
         Item var4 = item(
            CmIds.colored(var12, "water_bucket"),
            var1 -> new BucketItem(CmFluids.source(var12), var1),
            new Properties().craftRemainder(Items.BUCKET).stacksTo(1)
         );
         WATER_BUCKETS.put(var12, var4);
         CmFluids.putBucket(var12, var4);
      }
   }
}
