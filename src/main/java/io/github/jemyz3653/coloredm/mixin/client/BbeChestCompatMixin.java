package io.github.jemyz3653.coloredm.mixin.client;

import io.github.jemyz3653.coloredm.client.BbeCompat;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ChestBlockEntity.class, priority = 1500)
public abstract class BbeChestCompatMixin {
   @Inject(
      method = "<init>(Lnet/minecraft/world/level/block/entity/BlockEntityType;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)V",
      at = @At("TAIL")
   )
   private void coloredm$excludeFromBbe(CallbackInfo var1) {
      BbeCompat.exclude((BlockEntity)(Object)this);
   }
}
