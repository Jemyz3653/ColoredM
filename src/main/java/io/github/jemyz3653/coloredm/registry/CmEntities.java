package io.github.jemyz3653.coloredm.registry;

import io.github.jemyz3653.coloredm.CmIds;
import io.github.jemyz3653.coloredm.entity.ColoredItemFrame;
import java.util.EnumMap;
import java.util.IdentityHashMap;
import java.util.Map;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.EntityType.Builder;
import net.minecraft.world.item.DyeColor;

public final class CmEntities {
   private static final Map<DyeColor, EntityType<ColoredItemFrame>> ITEM_FRAMES = new EnumMap<>(DyeColor.class);
   private static final Map<EntityType<?>, DyeColor> COLOR_BY_TYPE = new IdentityHashMap<>();

   private CmEntities() {
   }

   public static EntityType<ColoredItemFrame> itemFrame(DyeColor var0) {
      return ITEM_FRAMES.get(var0);
   }

   public static Map<DyeColor, EntityType<ColoredItemFrame>> itemFrames() {
      return ITEM_FRAMES;
   }

   public static DyeColor colorOf(EntityType<?> var0) {
      DyeColor var1 = COLOR_BY_TYPE.get(var0);
      return var1 == null ? DyeColor.WHITE : var1;
   }

   public static void init() {
   }

   static {
      for (DyeColor var3 : DyeColor.values()) {
         ResourceKey var4 = ResourceKey.create(Registries.ENTITY_TYPE, CmIds.id(CmIds.colored(var3, "item_frame")));
         EntityType var5 = Builder.<ColoredItemFrame>of(ColoredItemFrame::new, MobCategory.MISC)
            .noLootTable()
            .sized(0.5F, 0.5F)
            .eyeHeight(0.0F)
            .clientTrackingRange(10)
            .noUpdateInterval()
            .dontTrackDeltas()
            .build(var4);
         COLOR_BY_TYPE.put(var5, var3);
         ITEM_FRAMES.put(var3, Registry.register(BuiltInRegistries.ENTITY_TYPE, var4, var5));
      }
   }
}
