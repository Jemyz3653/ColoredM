package io.github.jemyz3653.coloredm.client;

import io.github.jemyz3653.coloredm.CmIds;
import io.github.jemyz3653.coloredm.block.ColoredPottedBlock;
import io.github.jemyz3653.coloredm.registry.CmBlocks;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin.Context;
import net.minecraft.client.model.geom.builders.UVPair;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.block.dispatch.SingleVariant;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel.UnbakedRoot;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ResolvableModel.Resolver;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.client.resources.model.geometry.BakedQuad.MaterialInfo;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.client.resources.model.sprite.Material.Baked;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

public final class ModdedPotModels implements ModelLoadingPlugin {
   private static final Identifier VANILLA_POT = Identifier.withDefaultNamespace("block/flower_pot");

   @Override
   public void initialize(Context var1) {
      List<ColoredPottedBlock> var2 = CmBlocks.moddedPotted();
      if (!var2.isEmpty()) {
         ConcurrentHashMap var3 = new ConcurrentHashMap();
         HashSet var4 = new HashSet();

         for (ColoredPottedBlock var6 : var2) {
            var4.add(var6.vanillaPotted().defaultBlockState());
         }

         var1.modifyBlockModelOnLoad().register((var2x, var3x) -> {
            if (var4.contains(var3x.state())) {
               var3.put(var3x.state(), var2x);
            }

            return var2x;
         });

         for (ColoredPottedBlock var9 : var2) {
            BlockState var7 = var9.vanillaPotted().defaultBlockState();
            var1.registerBlockStateResolver(var9, var3x -> {
               for (BlockState var5 : var9.getStateDefinition().getPossibleStates()) {
                  var3x.setModel(var5, new ModdedPotModels.Root(var3, var7, var5.getValue(ColoredPottedBlock.COLOR)));
               }
            });
         }
      }
   }

   static List<BakedQuad> retexture(List<BakedQuad> var0, TextureAtlasSprite var1) {
      ArrayList var2 = null;

      for (int var3 = 0; var3 < var0.size(); var3++) {
         BakedQuad var4 = (BakedQuad)var0.get(var3);
         TextureAtlasSprite var5 = var4.materialInfo().sprite();
         if (!VANILLA_POT.equals(var5.contents().name())) {
            if (var2 != null) {
               var2.add(var4);
            }
         } else {
            if (var2 == null) {
               var2 = new ArrayList(var0.size());
               var2.addAll(var0.subList(0, var3));
            }

            var2.add(moveToSprite(var4, var5, var1));
         }
      }

      return var2 == null ? var0 : List.copyOf(var2);
   }

   private static BakedQuad moveToSprite(BakedQuad var0, TextureAtlasSprite var1, TextureAtlasSprite var2) {
      long[] var3 = new long[4];

      for (int var4 = 0; var4 < 4; var4++) {
         long var5 = var0.packedUV(var4);
         float var7 = (UVPair.unpackU(var5) - var1.getU0()) / (var1.getU1() - var1.getU0());
         float var8 = (UVPair.unpackV(var5) - var1.getV0()) / (var1.getV1() - var1.getV0());
         var3[var4] = UVPair.pack(var2.getU0() + var7 * (var2.getU1() - var2.getU0()), var2.getV0() + var8 * (var2.getV1() - var2.getV0()));
      }

      MaterialInfo var9 = var0.materialInfo();
      MaterialInfo var10 = new MaterialInfo(
         var2,
         var9.layer(),
         var9.itemRenderType(),
         var9.itemGlintRenderType(),
         var9.itemGlintSpecialRenderType(),
         var9.tintIndex(),
         var9.shadeDirectionOverride(),
         var9.lightEmission()
      );
      return new BakedQuad(var0.position0(), var0.position1(), var0.position2(), var0.position3(), var3[0], var3[1], var3[2], var3[3], var0.direction(), var10);
   }

   private static final class Model implements BlockStateModel {
      private final BlockStateModel base;
      private final Baked dyed;
      private final Map<BlockStateModelPart, BlockStateModelPart> parts = new ConcurrentHashMap<>();

      Model(BlockStateModel var1, Baked var2) {
         this.base = var1;
         this.dyed = var2;
      }

      @Override
      public void collectParts(RandomSource var1, List<BlockStateModelPart> var2) {
         int var3 = var2.size();
         this.base.collectParts(var1, var2);

         for (int var4 = var3; var4 < var2.size(); var4++) {
            var2.set(var4, this.parts.computeIfAbsent((BlockStateModelPart)var2.get(var4), var1x -> new ModdedPotModels.Part(var1x, this.dyed)));
         }
      }

      @Override
      public Baked particleMaterial() {
         return this.dyed;
      }

      @Override
      public int materialFlags() {
         return this.base.materialFlags();
      }
   }

   private static final class Part implements BlockStateModelPart {
      private final BlockStateModelPart base;
      private final Baked dyed;
      private final List<?>[] quads = new List[7];

      Part(BlockStateModelPart var1, Baked var2) {
         this.base = var1;
         this.dyed = var2;

         for (Direction var6 : Direction.values()) {
            this.quads[var6.ordinal()] = ModdedPotModels.retexture(var1.getQuads(var6), var2.sprite());
         }

         this.quads[6] = ModdedPotModels.retexture(var1.getQuads(null), var2.sprite());
      }

      @Override
      public List<BakedQuad> getQuads(@Nullable Direction var1) {
         return (List<BakedQuad>)this.quads[var1 == null ? 6 : var1.ordinal()];
      }

      @Override
      public boolean useAmbientOcclusion() {
         return this.base.useAmbientOcclusion();
      }

      @Override
      public Baked particleMaterial() {
         return this.dyed;
      }

      @Override
      public int materialFlags() {
         return this.base.materialFlags();
      }
   }

   private static final class Root implements UnbakedRoot {
      private final Map<BlockState, UnbakedRoot> sources;
      private final BlockState source;
      private final DyeColor color;

      Root(Map<BlockState, UnbakedRoot> var1, BlockState var2, DyeColor var3) {
         this.sources = var1;
         this.source = var2;
         this.color = var3;
      }

      @Override
      public void resolveDependencies(Resolver var1) {
         UnbakedRoot var2 = this.sources.get(this.source);
         if (var2 != null) {
            var2.resolveDependencies(var1);
         }
      }

      @Override
      public Object visualEqualityGroup(BlockState var1) {
         return this;
      }

      @Override
      public BlockStateModel bake(BlockState var1, ModelBaker var2) {
         Baked var3 = var2.materials().get(new Material(CmIds.id("block/" + this.color.getSerializedName() + "_flower_pot")), () -> "coloredm dyed pot");
         UnbakedRoot var4 = this.sources.get(this.source);
         BlockStateModel var5 = var4 != null ? var4.bake(this.source, var2) : new SingleVariant(var2.missingBlockModelPart());
         return new ModdedPotModels.Model(var5, var3);
      }
   }
}
