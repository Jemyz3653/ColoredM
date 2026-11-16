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
   SAND("sand");

   private final String name;

   Family(String nullxx) {
      this.name = nullxx;
   }

   public String id() {
      return this.name;
   }
}
