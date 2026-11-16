package io.github.jemyz3653.coloredm.client;

import io.github.jemyz3653.coloredm.CmIds;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public final class ColoredFrameModels {
   private static final Map<DyeColor, StateDefinition<Block, BlockState>> DEFINITIONS = new EnumMap<>(DyeColor.class);
   private static final Map<Identifier, StateDefinition<Block, BlockState>> BY_LOCATION = new HashMap<>();

   private ColoredFrameModels() {
   }

   public static BlockState state(DyeColor var0, boolean var1) {
      return DEFINITIONS.get(var0).any().setValue(BlockStateProperties.MAP, var1);
   }

   public static Map<Identifier, StateDefinition<Block, BlockState>> byLocation() {
      return BY_LOCATION;
   }

   static {
      for (DyeColor var3 : DyeColor.values()) {
         StateDefinition var4 = new Builder<Block, BlockState>(Blocks.AIR).add(BlockStateProperties.MAP).create(Block::defaultBlockState, BlockState::new);
         DEFINITIONS.put(var3, var4);
         BY_LOCATION.put(CmIds.id(CmIds.colored(var3, "item_frame")), var4);
      }
   }
}
