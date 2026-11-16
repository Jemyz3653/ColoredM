package io.github.jemyz3653.coloredm.block;

import io.github.jemyz3653.coloredm.Colored;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.SeaPickleBlock;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class ColoredSeaPickleBlock extends SeaPickleBlock implements Colored {
   private final DyeColor color;

   public ColoredSeaPickleBlock(DyeColor var1, Properties var2) {
      super(var2);
      this.color = var1;
   }

   @Override
   public DyeColor color() {
      return this.color;
   }
}
