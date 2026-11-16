package io.github.jemyz3653.coloredm.mixin.client;

import io.github.jemyz3653.coloredm.client.ColoredFrameModels;
import io.github.jemyz3653.coloredm.entity.ColoredItemFrame;
import net.minecraft.client.renderer.block.BlockModelResolver;
import net.minecraft.client.renderer.entity.ItemFrameRenderer;
import net.minecraft.client.renderer.entity.state.ItemFrameRenderState;
import net.minecraft.world.entity.decoration.ItemFrame;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemFrameRenderer.class)
public abstract class ItemFrameRendererMixin {
   @Shadow
   @Final
   private BlockModelResolver blockModelResolver;

   @Inject(
      method = "extractRenderState(Lnet/minecraft/world/entity/decoration/ItemFrame;Lnet/minecraft/client/renderer/entity/state/ItemFrameRenderState;F)V",
      at = @At("TAIL")
   )
   private void coloredm$dyedFrame(ItemFrame var1, ItemFrameRenderState var2, float var3, CallbackInfo var4) {
      if (var1 instanceof ColoredItemFrame var5 && !var2.isInvisible) {
         this.blockModelResolver.update(var2.frameModel, ColoredFrameModels.state(var5.color(), var2.mapId != null), ItemFrameRenderer.BLOCK_DISPLAY_CONTEXT);
      }
   }
}
