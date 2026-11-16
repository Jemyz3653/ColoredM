package io.github.jemyz3653.coloredm.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.jemyz3653.coloredm.mixin.client.DecoratedPotRendererAccessor;
import java.util.Objects;
import java.util.function.Consumer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.DecoratedPotRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderer.BakingContext;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.PotDecorations;
import org.joml.Vector3fc;
import org.jspecify.annotations.Nullable;

public class ColoredDecoratedPotSpecialRenderer implements SpecialModelRenderer<PotDecorations> {
   private final DecoratedPotRenderer renderer;
   private final DyeColor color;

   public ColoredDecoratedPotSpecialRenderer(DecoratedPotRenderer var1, DyeColor var2) {
      this.renderer = var1;
      this.color = var2;
   }

   public @Nullable PotDecorations extractArgument(ItemStack var1) {
      return var1.get(DataComponents.POT_DECORATIONS);
   }

   public void submit(@Nullable PotDecorations var1, PoseStack var2, SubmitNodeCollector var3, int var4, int var5, boolean var6, int var7) {
      ColoredPots.submit(
         (DecoratedPotRendererAccessor)this.renderer, this.color, var2, var3, var4, var5, Objects.requireNonNullElse(var1, PotDecorations.EMPTY), var7
      );
   }

   @Override
   public void getExtents(Consumer<Vector3fc> var1) {
      this.renderer.getExtents(var1);
   }

   public record Unbaked(DyeColor color) implements net.minecraft.client.renderer.special.SpecialModelRenderer.Unbaked<PotDecorations> {
      public static final MapCodec<ColoredDecoratedPotSpecialRenderer.Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec(
         var0 -> var0.group(DyeColor.CODEC.fieldOf("color").forGetter(ColoredDecoratedPotSpecialRenderer.Unbaked::color))
            .apply(var0, ColoredDecoratedPotSpecialRenderer.Unbaked::new)
      );

      @Override
      public MapCodec<ColoredDecoratedPotSpecialRenderer.Unbaked> type() {
         return MAP_CODEC;
      }

      @Override
      public SpecialModelRenderer<PotDecorations> bake(BakingContext var1) {
         return new ColoredDecoratedPotSpecialRenderer(new DecoratedPotRenderer(var1), this.color);
      }
   }
}
