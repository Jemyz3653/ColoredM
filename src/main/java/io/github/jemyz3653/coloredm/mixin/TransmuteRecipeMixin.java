package io.github.jemyz3653.coloredm.mixin;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.TransmuteRecipe;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(TransmuteRecipe.class)
public abstract class TransmuteRecipeMixin {
   @Shadow
   @Final
   private Ingredient input;

   public NonNullList<ItemStack> getRemainingItems(CraftingInput var1) {
      NonNullList var2 = NonNullList.withSize(var1.size(), ItemStack.EMPTY);
      boolean var3 = false;

      for (int var4 = 0; var4 < var2.size(); var4++) {
         ItemStack var5 = var1.getItem(var4);
         if (!var3 && !var5.isEmpty() && this.input.test(var5)) {
            var3 = true;
         } else {
            ItemStackTemplate var6 = var5.getItem().getCraftingRemainder();
            var2.set(var4, var6 != null ? var6.create() : ItemStack.EMPTY);
         }
      }

      return var2;
   }
}
