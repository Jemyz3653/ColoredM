package io.github.jemyz3653.coloredm.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SeaPickleBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SeaPickleBlock.class)
public abstract class SeaPickleBlockMixin {
   @ModifyExpressionValue(
      method = "performBonemeal",
      at = @At(value = "FIELD", target = "Lnet/minecraft/world/level/block/Blocks;SEA_PICKLE:Lnet/minecraft/world/level/block/Block;")
   )
   private Block coloredm$sameKind(Block var1) {
      return (Block)(Object)this;
   }
}
