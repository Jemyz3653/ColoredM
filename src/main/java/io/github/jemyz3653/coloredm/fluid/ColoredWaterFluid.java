package io.github.jemyz3653.coloredm.fluid;

import io.github.jemyz3653.coloredm.Colored;
import io.github.jemyz3653.coloredm.registry.CmFluids;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.WaterFluid;

public abstract class ColoredWaterFluid extends WaterFluid implements Colored {
   private final DyeColor color;

   protected ColoredWaterFluid(DyeColor var1) {
      this.color = var1;
   }

   @Override
   public DyeColor color() {
      return this.color;
   }

   @Override
   public Fluid getFlowing() {
      return CmFluids.flowing(this.color);
   }

   @Override
   public Fluid getSource() {
      return CmFluids.source(this.color);
   }

   @Override
   public Item getBucket() {
      return CmFluids.bucket(this.color);
   }

   @Override
   public BlockState createLegacyBlock(FluidState var1) {
      return CmFluids.block(this.color).defaultBlockState().setValue(LiquidBlock.LEVEL, getLegacyLevel(var1));
   }

   @Override
   public boolean isSame(Fluid var1) {
      return var1 == CmFluids.source(this.color) || var1 == CmFluids.flowing(this.color);
   }

   public static class Flowing extends ColoredWaterFluid {
      public Flowing(DyeColor var1) {
         super(var1);
      }

      @Override
      protected void createFluidStateDefinition(Builder<Fluid, FluidState> var1) {
         super.createFluidStateDefinition(var1);
         var1.add(LEVEL);
      }

      @Override
      public int getAmount(FluidState var1) {
         return var1.getValue(LEVEL);
      }

      @Override
      public boolean isSource(FluidState var1) {
         return false;
      }
   }

   public static class Source extends ColoredWaterFluid {
      public Source(DyeColor var1) {
         super(var1);
      }

      @Override
      public int getAmount(FluidState var1) {
         return 8;
      }

      @Override
      public boolean isSource(FluidState var1) {
         return true;
      }
   }
}
