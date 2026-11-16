package io.github.jemyz3653.coloredm.block;

import io.github.jemyz3653.coloredm.registry.CmBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Prediction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EyeblossomBlock.Type;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ColoredPottedBlock extends Block {
   public static final EnumProperty<DyeColor> COLOR = EnumProperty.create("color", DyeColor.class);
   private static final VoxelShape SHAPE = Block.column(6.0, 0.0, 6.0);
   private final Block plant;
   private final Block vanillaPotted;

   public ColoredPottedBlock(Block var1, Block var2, Properties var3) {
      super(var3);
      this.plant = var1;
      this.vanillaPotted = var2;
      this.registerDefaultState(this.stateDefinition.any().setValue(COLOR, DyeColor.WHITE));
   }

   public Block plant() {
      return this.plant;
   }

   public Block vanillaPotted() {
      return this.vanillaPotted;
   }

   public BlockState withColor(DyeColor var1) {
      return this.defaultBlockState().setValue(COLOR, var1);
   }

   @Override
   protected void createBlockStateDefinition(Builder<Block, BlockState> var1) {
      var1.add(COLOR);
   }

   @Override
   protected VoxelShape getShape(BlockState var1, BlockGetter var2, BlockPos var3, CollisionContext var4) {
      return SHAPE;
   }

   @Override
   protected InteractionResult useItemOn(ItemStack var1, BlockState var2, Level var3, BlockPos var4, Player var5, InteractionHand var6, BlockHitResult var7) {
      return var1.getItem() instanceof BlockItem var8 && CmBlocks.pottedFor(var8.getBlock()) != null
         ? InteractionResult.CONSUME
         : InteractionResult.TRY_WITH_EMPTY_HAND;
   }

   @Override
   protected InteractionResult useWithoutItem(BlockState var1, Level var2, BlockPos var3, Player var4, BlockHitResult var5) {
      ItemStack var6 = new ItemStack(this.plant);
      if (!var4.addItem(var6)) {
         var4.drop(var6, false, Prediction.PREDICTED);
      }

      var2.setBlockAndUpdate(var3, CmBlocks.flowerPot(var1.getValue(COLOR)).defaultBlockState());
      var2.gameEvent(var4, GameEvent.BLOCK_CHANGE, var3);
      return InteractionResult.SUCCESS;
   }

   @Override
   protected ItemStack getCloneItemStack(LevelReader var1, BlockPos var2, BlockState var3, boolean var4) {
      return new ItemStack(this.plant);
   }

   @Override
   protected BlockState updateShape(
      BlockState var1, LevelReader var2, ScheduledTickAccess var3, BlockPos var4, Direction var5, BlockPos var6, BlockState var7, RandomSource var8
   ) {
      return var5 == Direction.DOWN && !var1.canSurvive(var2, var4)
         ? Blocks.AIR.defaultBlockState()
         : super.updateShape(var1, var2, var3, var4, var5, var6, var7, var8);
   }

   @Override
   protected boolean isPathfindable(BlockState var1, PathComputationType var2) {
      return false;
   }

   private boolean isEyeblossom() {
      return this.plant == Blocks.OPEN_EYEBLOSSOM || this.plant == Blocks.CLOSED_EYEBLOSSOM;
   }

   @Override
   protected boolean isRandomlyTicking(BlockState var1) {
      return this.isEyeblossom();
   }

   @Override
   protected void randomTick(BlockState var1, ServerLevel var2, BlockPos var3, RandomSource var4) {
      if (this.isEyeblossom()) {
         boolean var5 = this.plant == Blocks.OPEN_EYEBLOSSOM;
         boolean var6 = var2.environmentAttributes().getValue(EnvironmentAttributes.EYEBLOSSOM_OPEN, var3).toBoolean(var5);
         if (var5 != var6) {
            ColoredPottedBlock var7 = CmBlocks.pottedFor(var5 ? Blocks.CLOSED_EYEBLOSSOM : Blocks.OPEN_EYEBLOSSOM);
            if (var7 != null) {
               var2.setBlockAndUpdate(var3, var7.withColor(var1.getValue(COLOR)));
               Type var8 = Type.fromBoolean(var5).transform();
               var8.spawnTransformParticle(var2, var3, var4);
               var2.playSound(null, var3, var8.longSwitchSound(), SoundSource.BLOCKS, 1.0F, 1.0F);
            }
         }
      }

      super.randomTick(var1, var2, var3, var4);
   }
}
