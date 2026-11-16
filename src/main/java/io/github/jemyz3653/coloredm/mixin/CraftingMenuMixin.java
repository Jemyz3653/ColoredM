package io.github.jemyz3653.coloredm.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.jemyz3653.coloredm.block.ColoredCraftingTableBlock;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CraftingMenu.class)
public abstract class CraftingMenuMixin {
   @WrapOperation(
      method = "stillValid",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/world/inventory/CraftingMenu;stillValid(Lnet/minecraft/world/inventory/ContainerLevelAccess;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/level/block/Block;)Z"
      )
   )
   private boolean coloredm$dyedTables(ContainerLevelAccess var1, Player var2, Block var3, Operation<Boolean> var4) {
      return (Boolean)var4.call(new Object[]{var1, var2, var3})
         || var1.evaluate(
            (var1x, var2x) -> var1x.getBlockState(var2x).getBlock() instanceof ColoredCraftingTableBlock && var2.isWithinBlockInteractionRange(var2x, 4.0),
            true
         );
   }
}
