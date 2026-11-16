package io.github.jemyz3653.coloredm.client;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.jemyz3653.coloredm.CmIds;
import io.github.jemyz3653.coloredm.mixin.client.DecoratedPotRendererAccessor;
import java.util.Optional;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.level.block.entity.DecoratedPotPattern;
import net.minecraft.world.level.block.entity.PotDecorations;

public final class ColoredPots {
   private ColoredPots() {
   }

   public static SpriteId base(DyeColor var0) {
      return Sheets.DECORATED_POT_MAPPER.apply(CmIds.id(var0.getName() + "_decorated_pot_base"));
   }

   public static SpriteId side(DyeColor var0, Optional<? extends ItemInstance> var1) {
      if (var1.isPresent()) {
         Holder var2 = ((ItemInstance)var1.get()).get(DataComponents.PROVIDES_POTTERY_PATTERN);
         if (var2 != null) {
            Identifier var3 = ((DecoratedPotPattern)var2.value()).assetId();
            return Sheets.DECORATED_POT_MAPPER.apply(var3.getNamespace().equals("minecraft") ? CmIds.id(var0.getName() + "_" + var3.getPath()) : var3);
         }
      }

      return Sheets.DECORATED_POT_MAPPER.apply(CmIds.id(var0.getName() + "_decorated_pot_side"));
   }

   public static void submit(
      DecoratedPotRendererAccessor var0, DyeColor var1, PoseStack var2, SubmitNodeCollector var3, int var4, int var5, PotDecorations var6, int var7
   ) {
      SpriteId var8 = base(var1);
      RenderType var9 = var8.renderType(RenderTypes::entitySolid);
      TextureAtlasSprite var10 = var0.coloredm$sprites().get(var8);
      var3.submitModelPart(var0.coloredm$neck(), var2, var9, var4, var5, var10, -1, var7);
      var3.submitModelPart(var0.coloredm$top(), var2, var9, var4, var5, var10, -1, var7);
      var3.submitModelPart(var0.coloredm$bottom(), var2, var9, var4, var5, var10, -1, var7);
      side(var0, var0.coloredm$front(), side(var1, var6.front()), var2, var3, var4, var5, var7);
      side(var0, var0.coloredm$back(), side(var1, var6.back()), var2, var3, var4, var5, var7);
      side(var0, var0.coloredm$left(), side(var1, var6.left()), var2, var3, var4, var5, var7);
      side(var0, var0.coloredm$right(), side(var1, var6.right()), var2, var3, var4, var5, var7);
   }

   private static void side(
      DecoratedPotRendererAccessor var0, ModelPart var1, SpriteId var2, PoseStack var3, SubmitNodeCollector var4, int var5, int var6, int var7
   ) {
      var4.submitModelPart(var1, var3, var2.renderType(RenderTypes::entitySolid), var5, var6, var0.coloredm$sprites().get(var2), -1, var7);
   }
}
