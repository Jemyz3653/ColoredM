package io.github.jemyz3653.coloredm.dye;

import io.github.jemyz3653.coloredm.block.ColoredPottedBlock;
import io.github.jemyz3653.coloredm.entity.ColoredItemFrame;
import io.github.jemyz3653.coloredm.registry.CmBlocks;
import io.github.jemyz3653.coloredm.registry.CmEntities;
import io.github.jemyz3653.coloredm.registry.Family;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.decoration.GlowItemFrame;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import org.jspecify.annotations.Nullable;

public final class Dyeing {
   private Dyeing() {
   }

   public static void register() {
      UseBlockCallback.EVENT.register(Dyeing::onUseBlock);
      UseEntityCallback.EVENT.register(Dyeing::onUseEntity);
   }

   private static @Nullable DyeColor dyeOf(ItemStack var0) {
      return var0.isEmpty() ? null : var0.get(DataComponents.DYE);
   }

   private static InteractionResult onUseBlock(Player var0, Level var1, InteractionHand var2, BlockHitResult var3) {
      if (var0.isSpectator()) {
         return InteractionResult.PASS;
      }

      ItemStack var4 = var0.getItemInHand(var2);
      DyeColor var5 = dyeOf(var4);
      if (var5 == null) {
         return InteractionResult.PASS;
      }

      BlockPos var6 = var3.getBlockPos();
      BlockState var7 = var1.getBlockState(var6);
      BlockState var8 = recolored(var7, var5);
      if (var8 != null && var0.mayUseItemAt(var6, var3.getDirection(), var4)) {
         if (!var1.isClientSide()) {
            apply(var1, var6, var7, var8);
            var1.playSound(null, var6, SoundEvents.DYE_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
            var1.gameEvent(var0, GameEvent.BLOCK_CHANGE, var6);
            var4.consume(1, var0);
         }

         return InteractionResult.SUCCESS;
      } else {
         return InteractionResult.PASS;
      }
   }

   public static @Nullable BlockState recolored(BlockState var0, DyeColor var1) {
      Block var2 = var0.getBlock();
      if (var2 instanceof ColoredPottedBlock) {
         return var0.getValue(ColoredPottedBlock.COLOR) == var1 ? null : var0.setValue(ColoredPottedBlock.COLOR, var1);
      } else if (var2 instanceof FlowerPotBlock var3 && var3.getPotted() != Blocks.AIR) {
         ColoredPottedBlock var4 = CmBlocks.pottedForVanilla(var2);
         return var4 == null ? null : var4.withColor(var1);
      } else {
         Family var5 = CmBlocks.familyOf(var2);
         return var5 != null && CmBlocks.colorOf(var2) != var1 ? copyProperties(var0, CmBlocks.get(var5, var1)) : null;
      }
   }

   public static BlockState copyProperties(BlockState var0, Block var1) {
      BlockState var2 = var1.defaultBlockState();

      for (Property var4 : var0.getProperties()) {
         if (var2.hasProperty(var4)) {
            var2 = copy(var0, var2, var4);
         }
      }

      return var2;
   }

   private static <T extends Comparable<T>> BlockState copy(BlockState var0, BlockState var1, Property<T> var2) {
      return var1.setValue(var2, var0.getValue(var2));
   }

   public static void apply(Level var0, BlockPos var1, BlockState var2, BlockState var3) {
      BlockPos var4 = null;
      BlockState var5 = null;
      if (var2.getBlock() instanceof ChestBlock && var2.hasProperty(ChestBlock.TYPE) && var2.getValue(ChestBlock.TYPE) != ChestType.SINGLE) {
         BlockPos var6 = var1.relative(ChestBlock.getConnectedDirection(var2));
         BlockState var7 = var0.getBlockState(var6);
         if (var7.is(var2.getBlock())) {
            var4 = var6;
            var5 = var7;
         }
      }

      replace(var0, var1, var2, var3);
      if (var4 != null) {
         replace(var0, var4, var5, copyProperties(var5, var3.getBlock()));
         var0.updateNeighborsAt(var4, var3.getBlock());
      }

      var0.updateNeighborsAt(var1, var3.getBlock());
   }

   private static void replace(Level var0, BlockPos var1, BlockState var2, BlockState var3) {
      BlockEntity var4 = var0.getBlockEntity(var1);
      CompoundTag var5 = null;
      if (var4 != null) {
         var5 = var4.saveWithoutMetadata(var0.registryAccess());
         if (var4 instanceof Container var6) {
            var6.clearContent();
         }
      }

      var0.setBlock(var1, var3, 274);
      if (var5 != null) {
         BlockEntity var7 = var0.getBlockEntity(var1);
         if (var7 != null) {
            var7.loadWithComponents(TagValueInput.create(ProblemReporter.DISCARDING, var0.registryAccess(), var5));
            var7.setChanged();
         }
      }

      var0.sendBlockUpdated(var1, var2, var3, 2);
   }

   private static InteractionResult onUseEntity(Player var0, Level var1, InteractionHand var2, Entity var3, @Nullable EntityHitResult var4) {
      if (!var0.isSpectator() && var0.isSecondaryUseActive()) {
         ItemStack var5 = var0.getItemInHand(var2);
         DyeColor var6 = dyeOf(var5);
         if (!(var6 != null && var3 instanceof ItemFrame var7) || var3 instanceof GlowItemFrame || !var0.mayBuild()) {
            return InteractionResult.PASS;
         } else if (var7 instanceof ColoredItemFrame var8 && var8.color() == var6) {
            return InteractionResult.PASS;
         } else {
            if (!var1.isClientSide()) {
               TagValueOutput var11 = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, var1.registryAccess());
               var7.saveWithoutId(var11);
               CompoundTag var9 = var11.buildResult();
               var9.remove("UUID");
               ColoredItemFrame var10 = CmEntities.itemFrame(var6).create(var1, EntitySpawnReason.CONVERSION);
               if (var10 == null) {
                  return InteractionResult.PASS;
               }

               var10.load(TagValueInput.create(ProblemReporter.DISCARDING, var1.registryAccess(), var9));
               var7.setItem(ItemStack.EMPTY, false);
               var7.discard();
               var1.addFreshEntity(var10);
               var1.playSound(null, var10.blockPosition(), SoundEvents.DYE_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
               var1.gameEvent(var0, GameEvent.ENTITY_INTERACT, var10.position());
               var5.consume(1, var0);
            }

            return InteractionResult.SUCCESS;
         }
      } else {
         return InteractionResult.PASS;
      }
   }

   static boolean isVanilla(Block var0) {
      return BuiltInRegistries.BLOCK.getKey(var0).getNamespace().equals("minecraft");
   }
}
