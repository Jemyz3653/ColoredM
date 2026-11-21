package io.github.jemyz3653.coloredm.registry;

public enum Family {
   FLOWER_POT("flower_pot"),
   DECORATED_POT("decorated_pot"),
   CHEST("chest"),
   BARREL("barrel"),
   CRAFTING_TABLE("crafting_table"),
   LADDER("ladder"),
   LANTERN("lantern"),
   SEA_PICKLE("sea_pickle"),
   DOOR("door"),
   TRAPDOOR("trapdoor"),
   SAND("sand"),
   ACACIA_DOOR("acacia_door"),
   ACACIA_TRAPDOOR("acacia_trapdoor"),
   BAMBOO_DOOR("bamboo_door"),
   BAMBOO_TRAPDOOR("bamboo_trapdoor"),
   BIRCH_DOOR("birch_door"),
   BIRCH_TRAPDOOR("birch_trapdoor"),
   CHERRY_DOOR("cherry_door"),
   CHERRY_TRAPDOOR("cherry_trapdoor"),
   COPPER_DOOR("copper_door"),
   COPPER_TRAPDOOR("copper_trapdoor"),
   CRIMSON_DOOR("crimson_door"),
   CRIMSON_TRAPDOOR("crimson_trapdoor"),
   DARK_OAK_DOOR("dark_oak_door"),
   DARK_OAK_TRAPDOOR("dark_oak_trapdoor"),
   EXPOSED_COPPER_DOOR("exposed_copper_door"),
   EXPOSED_COPPER_TRAPDOOR("exposed_copper_trapdoor"),
   IRON_DOOR("iron_door"),
   IRON_TRAPDOOR("iron_trapdoor"),
   JUNGLE_DOOR("jungle_door"),
   JUNGLE_TRAPDOOR("jungle_trapdoor"),
   MANGROVE_DOOR("mangrove_door"),
   MANGROVE_TRAPDOOR("mangrove_trapdoor"),
   OXIDIZED_COPPER_DOOR("oxidized_copper_door"),
   OXIDIZED_COPPER_TRAPDOOR("oxidized_copper_trapdoor"),
   PALE_OAK_DOOR("pale_oak_door"),
   PALE_OAK_TRAPDOOR("pale_oak_trapdoor"),
   POPLAR_DOOR("poplar_door"),
   POPLAR_TRAPDOOR("poplar_trapdoor"),
   SPRUCE_DOOR("spruce_door"),
   SPRUCE_TRAPDOOR("spruce_trapdoor"),
   WARPED_DOOR("warped_door"),
   WARPED_TRAPDOOR("warped_trapdoor"),
   WAXED_COPPER_DOOR("waxed_copper_door"),
   WAXED_COPPER_TRAPDOOR("waxed_copper_trapdoor"),
   WAXED_EXPOSED_COPPER_DOOR("waxed_exposed_copper_door"),
   WAXED_EXPOSED_COPPER_TRAPDOOR("waxed_exposed_copper_trapdoor"),
   WAXED_OXIDIZED_COPPER_DOOR("waxed_oxidized_copper_door"),
   WAXED_OXIDIZED_COPPER_TRAPDOOR("waxed_oxidized_copper_trapdoor"),
   WAXED_WEATHERED_COPPER_DOOR("waxed_weathered_copper_door"),
   WAXED_WEATHERED_COPPER_TRAPDOOR("waxed_weathered_copper_trapdoor"),
   WEATHERED_COPPER_DOOR("weathered_copper_door"),
   WEATHERED_COPPER_TRAPDOOR("weathered_copper_trapdoor");

   private final String name;

   Family(String nullxx) {
      this.name = nullxx;
   }

   public String id() {
      return this.name;
   }
}
