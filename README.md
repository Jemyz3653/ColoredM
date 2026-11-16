# ColoredM

ColoredM adds 16 dye colors to blocks and items in Minecraft 26.3 on Fabric. The colors follow the textures in your active resource pack.

## ColoredM 1.2.0

- Right-click an oak door, oak trapdoor, or ordinary sand with dye. The same dyeing works at a crafting table and on already colored variants.
- Both halves of a door change color together. Doors and trapdoors retain their orientation, open state, and redstone behavior. Trapdoors can be waterlogged.
- Colored sand falls like vanilla sand and can be smelted into glass.

The mod also supports flower pots, decorated pots, chests, barrels, crafting tables, ladders, lanterns, sea pickles, item frames, saddles, and water.

## Installing and building

Install Fabric API and the same ColoredM JAR on the client and server. For singleplayer, install them in the client's `mods` folder.

Build with JDK 25 and `./gradlew build` (Windows PowerShell: `.\gradlew.bat build`). The mod JAR is `build/libs/coloredm-1.2.0+26.3.jar`. To regenerate the 1.2.0 resource files, run `python tools/generate_colored_blocks.py` from the project directory.

The source tree was originally reconstructed from the released 1.0.0 and 1.0.1 JARs. Author: Jemyz3653. MIT License.
