package io.github.jemyz3653.coloredm.item;

import io.github.jemyz3653.coloredm.Colored;
import io.github.jemyz3653.coloredm.entity.ColoredItemFrame;
import io.github.jemyz3653.coloredm.registry.CmEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemFrameItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;

public class ColoredItemFrameItem extends ItemFrameItem implements Colored {
   private final DyeColor color;

   public ColoredItemFrameItem(DyeColor var1, EntityType<? extends HangingEntity> var2, Properties var3) {
      super(var2, var3);
      this.color = var1;
   }

   @Override
   public DyeColor color() {
      return this.color;
   }

   @Override
   public InteractionResult useOn(UseOnContext var1) {
      BlockPos var2 = var1.getClickedPos();
      Direction var3 = var1.getClickedFace();
      BlockPos var4 = var2.relative(var3);
      Player var5 = var1.getPlayer();
      ItemStack var6 = var1.getItemInHand();
      if (var5 != null && !this.mayPlace(var5, var3, var6, var4)) {
         return InteractionResult.FAIL;
      }

      Level var7 = var1.getLevel();
      ColoredItemFrame var8 = new ColoredItemFrame(CmEntities.itemFrame(this.color), var7, var4, var3);
      EntityType.<ColoredItemFrame>createDefaultStackConfig(var7, var6, var5).apply(var8);
      if (!var8.survives()) {
         return InteractionResult.CONSUME;
      }

      if (!var7.isClientSide()) {
         var8.playPlacementSound();
         var7.gameEvent(var5, GameEvent.ENTITY_PLACE, var8.position());
         var7.addFreshEntity(var8);
      }

      var6.shrink(1);
      return InteractionResult.SUCCESS;
   }
}
