package io.github.jemyz3653.coloredm.block;

import io.github.jemyz3653.coloredm.Colored;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.DecoratedPotBlock;
import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class ColoredDecoratedPotBlock extends DecoratedPotBlock implements Colored {
   private final DyeColor color;

   public ColoredDecoratedPotBlock(DyeColor var1, Properties var2) {
      super(var2);
      this.color = var1;
   }

   @Override
   public DyeColor color() {
      return this.color;
   }

   @Override
   protected ItemStack getCloneItemStack(LevelReader var1, BlockPos var2, BlockState var3, boolean var4) {
      ItemStack var5 = new ItemStack(this);
      if (var1.getBlockEntity(var2) instanceof DecoratedPotBlockEntity var6) {
         var5.set(DataComponents.POT_DECORATIONS, var6.getDecorations());
      }

      return var5;
   }
}
