package io.github.jemyz3653.coloredm.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.jemyz3653.coloredm.client.dye.DyedTexture;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(TextureManager.class)
public abstract class TextureManagerMixin {
   @WrapOperation(
      method = "getTexture",
      at = @At(value = "NEW", target = "(Lnet/minecraft/resources/Identifier;)Lnet/minecraft/client/renderer/texture/SimpleTexture;")
   )
   private SimpleTexture coloredm$dyed(Identifier var1, Operation<SimpleTexture> var2) {
      DyedTexture var3 = DyedTexture.forLocation(var1);
      return var3 != null ? var3 : (SimpleTexture)var2.call(new Object[]{var1});
   }
}
