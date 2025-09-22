package top.ctnstudio.futurefood.common.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.ctnstudio.futurefood.api.block.IEntityStorageBlock;
import top.ctnstudio.futurefood.api.tile.IUnlimitedEntityReceive;
import top.ctnstudio.futurefood.api.tile.IUnlimitedLink;
import top.ctnstudio.futurefood.common.block.tile.QedBlockEntity;
import top.ctnstudio.futurefood.core.init.ModBlock;
import top.ctnstudio.futurefood.core.init.ModTileEntity;
import top.ctnstudio.futurefood.datagen.tag.FfBlockTags;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static top.ctnstudio.futurefood.util.BlockEntyUtil.getBlockEntityFromLevel;

public class QedEntityBlock extends DirectionalEntityBlock<QedBlockEntity> implements IEntityStorageBlock, SimpleWaterloggedBlock {
  private static final MapCodec<QedEntityBlock> CODEC = simpleCodec(QedEntityBlock::new);

  public QedEntityBlock() {
    super(Properties.of()
      .noOcclusion()
      .isValidSpawn(ModBlock.argumentNever())
      .isRedstoneConductor(ModBlock.never())
      .isSuffocating(ModBlock.never())
      .isViewBlocking(ModBlock.never()));
  }

  public QedEntityBlock(Properties properties) {
    super(properties);
  }

//  @Override
//  protected @org.jetbrains.annotations.Nullable MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
//    return super.getMenuProvider(state, level, pos);
//  }

  @Override
  protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
    if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
      serverPlayer.openMenu(state.getMenuProvider(level, pos));
    }
    return InteractionResult.sidedSuccess(level.isClientSide);
  }

  @Override
  public void setPlacedBy(Level level, BlockPos pos, BlockState state,
                          @Nullable LivingEntity placer, ItemStack stack) {
//    if (level.isClientSide) {
//      return;
//    }
//    buildUnlimitedLinks(level, pos, state);
  }

  /**
   * 建立无限链接
   */
  public void buildUnlimitedLinks(Level level, BlockPos pos, BlockState state) {
    BoundingBox mutableBox = BoundingBox.fromCorners(pos.offset(5, 5, 5), pos.offset(-5, -5, -5));
    AABB aabb = AABB.of(mutableBox);
    Map<BlockPos, BlockState> blockStateMap = new HashMap<>();
    BlockPos.betweenClosedStream(aabb)
      .forEach(pos1 -> blockStateMap.put(pos1, level.getBlockState(pos1)));
    QedBlockEntity blockEntity = getBlockEntity(level, pos);
    blockStateMap.entrySet().stream()
      .filter(entry -> isLinkable(level, entry.getKey(), entry.getValue()))
      .forEach(entry -> linkBlock(level, entry.getKey(), blockEntity));
  }

  @Override
  protected void spawnAfterBreak(BlockState state, ServerLevel level, BlockPos pos, ItemStack stack, boolean dropExperience) {
    // TODO 将物品丢弃异常
    getBlockEntity(level, pos).clearContent();
    super.spawnAfterBreak(state, level, pos, stack, dropExperience);
  }

  public @NotNull QedBlockEntity getBlockEntity(Level level, BlockPos pos) {
    Optional<QedBlockEntity> blockEntity = getBlockEntityFromLevel(level, pos,
      ModTileEntity.QED.get());
    if (blockEntity.isEmpty()) {
      throw new IllegalStateException("QedBlockEntity not found at " + pos);
    }
    return blockEntity.get();
  }

  public boolean isLinkable(Level level, BlockPos pos, BlockState state) {
    return state.is(FfBlockTags.UNLIMITED_LAUNCH) || level.getBlockEntity(pos) instanceof IUnlimitedEntityReceive;
  }

  public boolean linkBlock(Level level, BlockPos pos, IUnlimitedLink i) {
    return i.linkBlock(level, pos);
  }

  @Override
  protected MapCodec<? extends BaseEntityBlock> codec() {
    return CODEC;
  }

  @Override
  protected RenderShape getRenderShape(BlockState state) {
    return RenderShape.MODEL;
  }

  @Nullable
  @Override
  public QedBlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new QedBlockEntity(pos, state);
  }

  @Override
  public BlockState getStateForPlacement(BlockPlaceContext context) {
    return this.defaultBlockState().setValue(FACING, context.getClickedFace());
  }

  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state,
                                                                BlockEntityType<T> type) {
    return createTickerHelper(type, ModTileEntity.QED.get(), QedBlockEntity::tick);
  }

  @Override
  protected boolean skipRendering(BlockState state, BlockState adjacentBlockState, Direction side) {
    return adjacentBlockState.is(this) || super.skipRendering(state, adjacentBlockState, side);
  }

  @Override
  protected VoxelShape getVisualShape(BlockState p_309057_, BlockGetter p_308936_,
                                      BlockPos p_308956_, CollisionContext p_309006_) {
    return Shapes.empty();
  }

  @Override
  protected float getShadeBrightness(BlockState p_308911_, BlockGetter p_308952_,
                                     BlockPos p_308918_) {
    return 1.0F;
  }

  @Override
  protected boolean propagatesSkylightDown(BlockState p_309084_, BlockGetter p_309133_,
                                           BlockPos p_309097_) {
    return true;
  }

}
