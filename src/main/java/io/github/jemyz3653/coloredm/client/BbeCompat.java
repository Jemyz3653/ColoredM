package io.github.jemyz3653.coloredm.client;

import io.github.jemyz3653.coloredm.ColoredM;
import io.github.jemyz3653.coloredm.block.ColoredChestBlock;
import io.github.jemyz3653.coloredm.block.ColoredDecoratedPotBlock;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;

public final class BbeCompat {
   private static final boolean PRESENT = FabricLoader.getInstance().isModLoaded("betterblockentities");
   private static final MethodHandle SET_SUPPORTED = PRESENT ? find() : null;

   private BbeCompat() {
   }

   private static MethodHandle find() {
      try {
         return MethodHandles.publicLookup().findVirtual(BlockEntity.class, "bbe$setSupportedBlockEntity", MethodType.methodType(void.class, boolean.class));
      } catch (ReflectiveOperationException var1) {
         ColoredM.LOGGER.warn("Better Block Entities is installed, but its block entity hook was not found; dyed chests and pots may look undyed", var1);
         return null;
      }
   }

   public static void exclude(BlockEntity var0) {
      if (SET_SUPPORTED != null) {
         Block var1 = var0.getBlockState().getBlock();
         if (var1 instanceof ColoredChestBlock || var1 instanceof ColoredDecoratedPotBlock) {
            try {
               SET_SUPPORTED.invoke((BlockEntity)var0, (boolean)false);
            } catch (Throwable var3) {
               ColoredM.LOGGER.warn("Could not exclude {} from Better Block Entities", var0, var3);
            }
         }
      }
   }
}
