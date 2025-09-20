package top.ctnstudio.futurefood.common.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import top.ctnstudio.futurefood.api.IModEnergyStorage;
import top.ctnstudio.futurefood.common.block.tile.QedBlockEntity;
import top.ctnstudio.futurefood.core.init.ModBlock;
import top.ctnstudio.futurefood.core.init.ModTileEntity;
import top.ctnstudio.futurefood.datagen.tag.FfBlockTags;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static top.ctnstudio.futurefood.util.BlockEntyUtil.getBlockEntityFromLevel;

public class QedEntityBlock extends DirectionalEntityBlock<QedBlockEntity> implements IModEnergyStorage {
  private static final MapCodec<QedEntityBlock> CODEC = simpleCodec(QedEntityBlock::new);

  public QedEntityBlock() {
    super(Properties.of()
      .noOcclusion()
      .isValidSpawn(ModBlock.argumentNever())
      .isRedstoneConductor(ModBlock.never())
      .isSuffocating(ModBlock.never())
      .isViewBlocking(ModBlock.never()));
  }

  @Override
  public void setPlacedBy(Level level, BlockPos pos, BlockState state,
                          @Nullable LivingEntity placer, ItemStack stack) {
    buildUnlimitedLinks(level, pos, state);
  }

  /**
   * 建立无限链接
   */
  public void buildUnlimitedLinks(Level level, BlockPos pos, BlockState state) {
    AABB aabb = AABB.of(BoundingBox.fromCorners(pos.offset(5, 5, 5), pos.offset(-5, -5, -5)));
    Map<BlockPos, BlockState> blockStateMap = new HashMap<>();
    BlockPos.betweenClosedStream(aabb)
      .forEach(pos1 -> blockStateMap.put(pos1, level.getBlockState(pos1)));
    QedBlockEntity blockEntity = getBlockEntity(level, pos);
    blockStateMap.entrySet().stream()
      .filter(entry -> entry.getValue().is(FfBlockTags.UNLIMITED_RECEPTION))
      .forEach(entry -> blockEntity.linkBlock(entry.getKey()));
  }

  public @NotNull QedBlockEntity getBlockEntity(Level level, BlockPos pos) {
    Optional<QedBlockEntity> blockEntity = getBlockEntityFromLevel(level, pos,
        ModTileEntity.QED.get());
    if (blockEntity.isEmpty()) {
      throw new IllegalStateException("QedBlockEntity not found at " + pos);
    }
    return blockEntity.get();
  }

  private QedEntityBlock(Properties properties) {
    super(properties);
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
    return type == ModTileEntity.QED.get() ? QedBlockEntity::tick : null;
  }

  @Override
  protected boolean skipRendering(BlockState state, BlockState adjacentBlockState, Direction side) {
    return adjacentBlockState.is(this) || super.skipRendering(state, adjacentBlockState, side);
  }

  @Override
  protected VoxelShape getVisualShape(BlockState p_309057_, BlockGetter p_308936_, BlockPos p_308956_, CollisionContext p_309006_) {
    return Shapes.empty();
  }

  @Override
  protected float getShadeBrightness(BlockState p_308911_, BlockGetter p_308952_, BlockPos p_308918_) {
    return 1.0F;
  }

  @Override
  protected boolean propagatesSkylightDown(BlockState p_309084_, BlockGetter p_309133_, BlockPos p_309097_) {
    return true;
  }

}
