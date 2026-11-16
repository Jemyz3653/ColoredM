package io.github.jemyz3653.coloredm;

import io.github.jemyz3653.coloredm.dye.Dyeing;
import io.github.jemyz3653.coloredm.registry.CmBlocks;
import io.github.jemyz3653.coloredm.registry.CmEntities;
import io.github.jemyz3653.coloredm.registry.CmFluids;
import io.github.jemyz3653.coloredm.registry.CmItems;
import io.github.jemyz3653.coloredm.registry.Family;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityType;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry.Entry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BlockEntityTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ColoredM implements ModInitializer {
   public static final Logger LOGGER = LoggerFactory.getLogger("ColoredM");

   @Override
   public void onInitialize() {
      CmBlocks.init();
      CmFluids.init();
      CmEntities.init();
      CmItems.init();
      addValidBlocks(BlockEntityTypes.CHEST, Family.CHEST);
      addValidBlocks(BlockEntityTypes.BARREL, Family.BARREL);
      addValidBlocks(BlockEntityTypes.DECORATED_POT, Family.DECORATED_POT);

      for (Family var4 : Family.values()) {
         Block var5 = vanilla(var4);
         Entry var6 = FlammableBlockRegistry.getDefaultInstance().get(var5);
         if (var6 != null && var6.getBurnOdds() > 0) {
            for (Block var8 : CmBlocks.family(var4).values()) {
               FlammableBlockRegistry.getDefaultInstance().add(var8, var6.getIgniteOdds(), var6.getBurnOdds());
            }
         }
      }

      ResourceKey<CreativeModeTab> var9 = ResourceKey.create(Registries.CREATIVE_MODE_TAB, CmIds.id("coloredm"));
      Registry.register(
         BuiltInRegistries.CREATIVE_MODE_TAB,
         var9,
         FabricCreativeModeTab.builder()
            .title(Component.translatable("itemGroup.coloredm"))
            .icon(() -> new ItemStack(CmItems.get(Family.LANTERN, DyeColor.MAGENTA)))
            .build()
      );
      CreativeModeTabEvents.modifyOutputEvent(var9).register(var0 -> {
         for (Item var2 : CmItems.all()) {
            var0.accept(var2);
         }
      });
      Dyeing.register();
      LOGGER.info("ColoredM: {} items, {} plants for dyed flower pots", CmItems.all().size(), CmBlocks.potted().size());
   }

   private static void addValidBlocks(BlockEntityType<?> var0, Family var1) {
      for (Block var3 : CmBlocks.family(var1).values()) {
         ((FabricBlockEntityType)var0).addValidBlock(var3);
      }
   }

   public static Block vanilla(Family var0) {
      return switch (var0) {
         case FLOWER_POT -> Blocks.FLOWER_POT;
         case DECORATED_POT -> Blocks.DECORATED_POT;
         case CHEST -> Blocks.CHEST;
         case BARREL -> Blocks.BARREL;
         case CRAFTING_TABLE -> Blocks.CRAFTING_TABLE;
         case LADDER -> Blocks.LADDER;
         case LANTERN -> Blocks.LANTERN;
         case SEA_PICKLE -> Blocks.SEA_PICKLE;
      };
   }
}
