package io.github.jemyz3653.coloredm.block;

import io.github.jemyz3653.coloredm.Colored;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntityTypes;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class ColoredChestBlock extends ChestBlock implements Colored {
   private final DyeColor color;

   public ColoredChestBlock(DyeColor var1, Properties var2) {
      super(() -> BlockEntityTypes.CHEST, SoundEvents.CHEST_OPEN, SoundEvents.CHEST_CLOSE, var2);
      this.color = var1;
   }

   @Override
   public DyeColor color() {
      return this.color;
   }
}
