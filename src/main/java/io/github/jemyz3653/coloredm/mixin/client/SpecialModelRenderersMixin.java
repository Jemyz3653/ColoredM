package io.github.jemyz3653.coloredm.mixin.client;

import com.mojang.serialization.MapCodec;
import io.github.jemyz3653.coloredm.CmIds;
import io.github.jemyz3653.coloredm.client.ColoredDecoratedPotSpecialRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderers;
import net.minecraft.client.renderer.special.SpecialModelRenderer.Unbaked;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs.LateBoundIdMapper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SpecialModelRenderers.class)
public abstract class SpecialModelRenderersMixin {
   @Shadow
   @Final
   private static LateBoundIdMapper<Identifier, MapCodec<? extends Unbaked<?>>> ID_MAPPER;

   @Inject(method = "bootstrap", at = @At("TAIL"))
   private static void coloredm$register(CallbackInfo var0) {
      ID_MAPPER.put(CmIds.id("decorated_pot"), ColoredDecoratedPotSpecialRenderer.Unbaked.MAP_CODEC);
   }
}
