package io.github.jemyz3653.coloredm.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import io.github.jemyz3653.coloredm.client.ColoredFrameModels;
import java.util.function.Function;
import net.minecraft.client.resources.model.BlockStateDefinitions;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BlockStateDefinitions.class)
public abstract class BlockStateDefinitionsMixin {
   @ModifyReturnValue(method = "definitionLocationToBlockStateMapper", at = @At("RETURN"))
   private static Function<Identifier, StateDefinition<Block, BlockState>> coloredm$frames(Function<Identifier, StateDefinition<Block, BlockState>> var0) {
      return var1 -> {
         StateDefinition var2 = ColoredFrameModels.byLocation().get(var1);
         return var2 != null ? var2 : (StateDefinition)var0.apply(var1);
      };
   }
}
