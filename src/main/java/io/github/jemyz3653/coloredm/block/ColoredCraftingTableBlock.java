package io.github.jemyz3653.coloredm.block;

import io.github.jemyz3653.coloredm.Colored;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.CraftingTableBlock;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class ColoredCraftingTableBlock extends CraftingTableBlock implements Colored {
   private final DyeColor color;

   public ColoredCraftingTableBlock(DyeColor var1, Properties var2) {
      super(var2);
      this.color = var1;
   }

   @Override
   public DyeColor color() {
      return this.color;
   }
}
