package io.github.jemyz3653.coloredm.mixin.client;

import io.github.jemyz3653.coloredm.client.BbeCompat;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = DecoratedPotBlockEntity.class, priority = 1500)
public abstract class BbePotCompatMixin {
   @Inject(method = "<init>", at = @At("TAIL"))
   private void coloredm$excludeFromBbe(CallbackInfo var1) {
      BbeCompat.exclude((BlockEntity)(Object)this);
   }
}
