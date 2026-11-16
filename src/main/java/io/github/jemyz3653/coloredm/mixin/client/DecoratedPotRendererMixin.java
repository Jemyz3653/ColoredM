package io.github.jemyz3653.coloredm.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.jemyz3653.coloredm.Colored;
import io.github.jemyz3653.coloredm.client.ColorKeys;
import io.github.jemyz3653.coloredm.client.ColoredPots;
import net.fabricmc.fabric.api.client.rendering.v1.FabricRenderState;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.DecoratedPotRenderer;
import net.minecraft.client.renderer.blockentity.state.DecoratedPotRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer.CrumblingOverlay;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity;
import net.minecraft.world.level.block.entity.PotDecorations;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DecoratedPotRenderer.class)
public abstract class DecoratedPotRendererMixin {
   @Inject(
      method = "extractRenderState(Lnet/minecraft/world/level/block/entity/DecoratedPotBlockEntity;Lnet/minecraft/client/renderer/blockentity/state/DecoratedPotRenderState;FLnet/minecraft/world/phys/Vec3;Lnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;)V",
      at = @At("TAIL")
   )
   private void coloredm$color(DecoratedPotBlockEntity var1, DecoratedPotRenderState var2, float var3, Vec3 var4, CrumblingOverlay var5, CallbackInfo var6) {
      DyeColor var7 = var1.getBlockState().getBlock() instanceof Colored var8 ? var8.color() : null;
      ((FabricRenderState)var2).setData(ColorKeys.COLOR, var7);
   }

   @WrapOperation(
      method = "submit(Lnet/minecraft/client/renderer/blockentity/state/DecoratedPotRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/blockentity/DecoratedPotRenderer;submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;IILnet/minecraft/world/level/block/entity/PotDecorations;I)V"
      )
   )
   private void coloredm$dyed(
      DecoratedPotRenderer var1,
      PoseStack var2,
      SubmitNodeCollector var3,
      int var4,
      int var5,
      PotDecorations var6,
      int var7,
      Operation<Void> var8,
      @Local(argsOnly = true) DecoratedPotRenderState var9
   ) {
      DyeColor var10 = ((FabricRenderState)var9).getData(ColorKeys.COLOR);
      if (var10 == null) {
         var8.call(new Object[]{var1, var2, var3, var4, var5, var6, var7});
      } else {
         ColoredPots.submit((DecoratedPotRendererAccessor)var1, var10, var2, var3, var4, var5, var6, var7);
      }
   }
}
