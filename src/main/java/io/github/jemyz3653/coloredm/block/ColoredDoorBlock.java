package io.github.jemyz3653.coloredm.block;

import io.github.jemyz3653.coloredm.Colored;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.properties.BlockSetType;

public final class ColoredDoorBlock extends DoorBlock implements Colored {
   private final DyeColor color;

   public ColoredDoorBlock(DyeColor color, Properties properties) {
      super(BlockSetType.OAK, properties);
      this.color = color;
   }

   @Override
   public DyeColor color() {
      return this.color;
   }
}
