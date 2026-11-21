package io.github.jemyz3653.coloredm.block;

import io.github.jemyz3653.coloredm.Colored;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.properties.BlockSetType;

public final class ColoredTrapDoorBlock extends TrapDoorBlock implements Colored {
   private final DyeColor color;

   public ColoredTrapDoorBlock(DyeColor color, Properties properties) {
      this(color, BlockSetType.OAK, properties);
   }

   public ColoredTrapDoorBlock(DyeColor color, BlockSetType type, Properties properties) {
      super(type, properties);
      this.color = color;
   }

   @Override
   public DyeColor color() {
      return this.color;
   }
}
