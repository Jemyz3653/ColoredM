package io.github.jemyz3653.coloredm.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.jemyz3653.coloredm.CmIds;
import io.github.jemyz3653.coloredm.Colored;
import io.github.jemyz3653.coloredm.client.ColorKeys;
import net.fabricmc.fabric.api.client.rendering.v1.FabricRenderState;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.ChestRenderer;
import net.minecraft.client.renderer.blockentity.state.ChestRenderState;
import net.minecraft.client.renderer.blockentity.state.ChestRenderState.ChestMaterialType;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer.CrumblingOverlay;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChestRenderer.class)
public abstract class ChestRendererMixin {
   @Inject(
      method = "extractRenderState(Lnet/minecraft/world/level/block/entity/BlockEntity;Lnet/minecraft/client/renderer/blockentity/state/ChestRenderState;FLnet/minecraft/world/phys/Vec3;Lnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;)V",
      at = @At("TAIL")
   )
   private void coloredm$color(BlockEntity var1, ChestRenderState var2, float var3, Vec3 var4, CrumblingOverlay var5, CallbackInfo var6) {
      DyeColor var7 = var1.getLevel() != null && var1.getBlockState().getBlock() instanceof Colored var8 ? var8.color() : null;
      ((FabricRenderState)var2).setData(ColorKeys.COLOR, var7);
   }

   @WrapOperation(
      method = "submit(Lnet/minecraft/client/renderer/blockentity/state/ChestRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/Sheets;chooseSprite(Lnet/minecraft/client/renderer/blockentity/state/ChestRenderState$ChestMaterialType;Lnet/minecraft/world/level/block/state/properties/ChestType;)Lnet/minecraft/client/resources/model/sprite/SpriteId;"
      )
   )
   private SpriteId coloredm$dyedSprite(ChestMaterialType var1, ChestType var2, Operation<SpriteId> var3, @Local(argsOnly = true) ChestRenderState var4) {
      DyeColor var5 = ((FabricRenderState)var4).getData(ColorKeys.COLOR);
      if (var5 == null) {
         return (SpriteId)var3.call(new Object[]{var1, var2});
      }

      String var6 = switch (var2) {
         case SINGLE -> "";
         case LEFT -> "_left";
         case RIGHT -> "_right";
      };
      return Sheets.CHEST_MAPPER.apply(CmIds.id(var5.getName() + "_normal" + var6));
   }
}
