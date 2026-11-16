package io.github.jemyz3653.coloredm.client.dye;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.jemyz3653.coloredm.CmIds;
import io.github.jemyz3653.coloredm.mixin.client.SpriteContentsAccessor;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.client.renderer.texture.atlas.SpriteSource;
import net.minecraft.client.renderer.texture.atlas.SpriteSource.Output;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.DyeColor;

public record DyedSpriteSource(Map<Identifier, Mask> textures, List<DyedSpriteSource.Directory> directories) implements SpriteSource {
   public static final MapCodec<DyedSpriteSource> MAP_CODEC = RecordCodecBuilder.mapCodec(
      var0 -> var0.group(
            Codec.unboundedMap(Identifier.CODEC, Mask.CODEC).optionalFieldOf("textures", Map.of()).forGetter(DyedSpriteSource::textures),
            DyedSpriteSource.Directory.CODEC.listOf().optionalFieldOf("directories", List.of()).forGetter(DyedSpriteSource::directories)
         )
         .apply(var0, DyedSpriteSource::new)
   );

   public static Identifier dyedId(Identifier var0, DyeColor var1) {
      String var2 = var0.getPath();
      int var3 = var2.lastIndexOf(47);
      String var4 = var3 < 0 ? "" : var2.substring(0, var3 + 1);
      String var5 = var3 < 0 ? var2 : var2.substring(var3 + 1);
      return CmIds.id(var4 + var1.getName() + "_" + var5);
   }

   @Override
   public void run(ResourceManager var1, Output var2) {
      LinkedHashMap<Identifier, Mask> var3 = new LinkedHashMap<>(this.textures);

      for (DyedSpriteSource.Directory var5 : this.directories) {
         FileToIdConverter var6 = new FileToIdConverter("textures/" + var5.source(), ".png");
         var6.listMatchingResources(var1)
            .keySet()
            .stream()
            .map(var6::fileToId)
            .filter(var0 -> var0.getNamespace().equals("minecraft"))
            .sorted()
            .forEach(var2x -> var3.putIfAbsent(var2x.withPrefix(var5.source() + "/"), var5.mask()));
      }

      for (Entry<Identifier, Mask> var15 : var3.entrySet()) {
         Identifier var16 = var15.getKey();
         Mask var7 = var15.getValue();
         Optional var8 = var1.getResource(TEXTURE_ID_CONVERTER.idToFile(var16));
         if (!var8.isEmpty()) {
            for (DyeColor var12 : DyeColor.values()) {
               Identifier var13 = dyedId(var16, var12);
               var2.add(var13, var4 -> {
                  SpriteContents var5x = var4.loadSprite(var13, (Resource)var8.get());
                  if (var5x != null) {
                     try {
                        Dyer.dye(((SpriteContentsAccessor)var5x).coloredm$getOriginalImage(), var12, var7);
                     } catch (IllegalArgumentException var7x) {
                     }
                  }

                  return var5x;
               });
            }
         }
      }
   }

   @Override
   public MapCodec<DyedSpriteSource> codec() {
      return MAP_CODEC;
   }

   public record Directory(String source, Mask mask) {
      public static final Codec<DyedSpriteSource.Directory> CODEC = RecordCodecBuilder.create(
         var0 -> var0.group(
               Codec.STRING.fieldOf("source").forGetter(DyedSpriteSource.Directory::source),
               Mask.CODEC.fieldOf("mask").forGetter(DyedSpriteSource.Directory::mask)
            )
            .apply(var0, DyedSpriteSource.Directory::new)
      );
   }
}
