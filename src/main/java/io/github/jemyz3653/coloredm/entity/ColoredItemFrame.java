package io.github.jemyz3653.coloredm.entity;

import io.github.jemyz3653.coloredm.Colored;
import io.github.jemyz3653.coloredm.registry.CmEntities;
import io.github.jemyz3653.coloredm.registry.CmItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ColoredItemFrame extends ItemFrame implements Colored {
   private final DyeColor color;

   public ColoredItemFrame(EntityType<? extends ItemFrame> var1, Level var2) {
      super(var1, var2);
      this.color = CmEntities.colorOf(var1);
   }

   public ColoredItemFrame(EntityType<? extends ItemFrame> var1, Level var2, BlockPos var3, Direction var4) {
      super(var1, var2, var3, var4);
      this.color = CmEntities.colorOf(var1);
   }

   @Override
   public DyeColor color() {
      return this.color;
   }

   @Override
   protected ItemStack getFrameItemStack() {
      return new ItemStack(CmItems.itemFrame(this.color));
   }
}
