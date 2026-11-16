package io.github.jemyz3653.coloredm.mixin.client;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.blockentity.DecoratedPotRenderer;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(DecoratedPotRenderer.class)
public interface DecoratedPotRendererAccessor {
   @Accessor("sprites")
   SpriteGetter coloredm$sprites();

   @Accessor("neck")
   ModelPart coloredm$neck();

   @Accessor("top")
   ModelPart coloredm$top();

   @Accessor("bottom")
   ModelPart coloredm$bottom();

   @Accessor("frontSide")
   ModelPart coloredm$front();

   @Accessor("backSide")
   ModelPart coloredm$back();

   @Accessor("leftSide")
   ModelPart coloredm$left();

   @Accessor("rightSide")
   ModelPart coloredm$right();
}
