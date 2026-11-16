package io.github.jemyz3653.coloredm.block;

import io.github.jemyz3653.coloredm.Colored;
import io.github.jemyz3653.coloredm.fluid.ColoredWaterFluid;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class ColoredLiquidBlock extends LiquidBlock implements Colored {
   private final DyeColor color;

   public ColoredLiquidBlock(DyeColor var1, ColoredWaterFluid var2, Properties var3) {
      super(var2, var3);
      this.color = var1;
   }

   @Override
   public DyeColor color() {
      return this.color;
   }
}
