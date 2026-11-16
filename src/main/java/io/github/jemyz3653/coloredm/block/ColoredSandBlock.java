package io.github.jemyz3653.coloredm.block;

import io.github.jemyz3653.coloredm.Colored;
import net.minecraft.util.ColorRGBA;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.SandBlock;

public final class ColoredSandBlock extends SandBlock implements Colored {
   private final DyeColor color;

   public ColoredSandBlock(DyeColor color, Properties properties) {
      super(new ColorRGBA(color.getTextureDiffuseColor()), properties);
      this.color = color;
   }

   @Override
   public DyeColor color() {
      return this.color;
   }
}
