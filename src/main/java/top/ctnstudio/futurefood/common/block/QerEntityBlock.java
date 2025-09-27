package top.ctnstudio.futurefood.common.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import top.ctnstudio.futurefood.api.block.IEntityStorageBlock;
import top.ctnstudio.futurefood.common.block.tile.QerBlockEntity;
import top.ctnstudio.futurefood.core.init.ModBlock;

import javax.annotation.Nullable;

public class QerEntityBlock extends DirectionalEntityBlock<QerBlockEntity> implements IEntityStorageBlock, SimpleWaterloggedBlock {
  private static final MapCodec<QerEntityBlock> CODEC = simpleCodec(QerEntityBlock::new);

  public QerEntityBlock() {
    this(Properties.of());
  }

  public QerEntityBlock(Properties properties) {
    super(properties
      .noOcclusion()
      .isValidSpawn(ModBlock.argumentNever())
      .isRedstoneConductor(ModBlock.never())
      .isSuffocating(ModBlock.never())
      .isViewBlocking(ModBlock.never()));
  }

  @Override
  protected MapCodec<? extends BaseEntityBlock> codec() {
    return CODEC;
  }

  @Override
  protected RenderShape getRenderShape(BlockState state) {
    return RenderShape.MODEL;
  }

  @Override
  public BlockState getStateForPlacement(BlockPlaceContext context) {
    return this.defaultBlockState().setValue(FACING, context.getClickedFace());
  }

  @Nullable
  @Override
  public QerBlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new QerBlockEntity(pos, state);
  }

  @Override
  protected boolean skipRendering(BlockState state, BlockState adjacentBlockState, Direction side) {
    return adjacentBlockState.is(this) || super.skipRendering(state, adjacentBlockState, side);
  }

  @Override
  protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
    if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
      serverPlayer.openMenu(state.getMenuProvider(level, pos));
    }
    return InteractionResult.sidedSuccess(level.isClientSide);
  }

  @Override
  protected float getShadeBrightness(BlockState p_308911_, BlockGetter p_308952_,
                                     BlockPos p_308918_) {
    return 1.0F;
  }

  @Override
  protected VoxelShape getVisualShape(BlockState p_309057_, BlockGetter p_308936_,
                                      BlockPos p_308956_, CollisionContext p_309006_) {
    return Shapes.empty();
  }

  @Override
  protected boolean propagatesSkylightDown(BlockState p_309084_, BlockGetter p_309133_,
                                           BlockPos p_309097_) {
    return true;
  }
}
