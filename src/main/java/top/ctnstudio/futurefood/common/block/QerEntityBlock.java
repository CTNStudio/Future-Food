package top.ctnstudio.futurefood.common.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import top.ctnstudio.futurefood.api.block.IEntityStorageBlock;
import top.ctnstudio.futurefood.common.block.tile.QerBlockEntity;
import top.ctnstudio.futurefood.core.init.ModBlock;
import top.ctnstudio.futurefood.core.init.ModTileEntity;
import top.ctnstudio.futurefood.util.BlockEntyUtil;
import top.ctnstudio.futurefood.util.EntityItemUtil;

import javax.annotation.Nullable;

// TODO 完成状态变化
public class QerEntityBlock extends DirectionEntityBlock<QerBlockEntity> implements IEntityStorageBlock, SimpleWaterloggedBlock {
  public static final EnumProperty<Activate> ACTIVATE = EnumProperty.create("activate", Activate.class);
  public static final EnumProperty<QedEntityBlock.Light> LIGHT = QedEntityBlock.LIGHT;
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
    this.registerDefaultState(this.stateDefinition.any()
      .setValue(ACTIVATE, Activate.DEFAULT)
      .setValue(LIGHT, QedEntityBlock.Light.DEFAULT)
    );
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    builder.add(ACTIVATE, LIGHT);
  }

  @Override
  public boolean onDestroyedByPlayer(BlockState state, Level world, BlockPos pos, Player player,
                                     boolean willHarvest, FluidState fluid) {
    if (world.isClientSide()) {
      return true;
    }

    final ServerLevel serverWorld = (ServerLevel) world;
    final var tile = getBlockEntity(serverWorld, pos);

    EntityItemUtil.summonLootItems(serverWorld, pos, tile.getEnergyItemStack().copy());
    tile.clearContent();

    return true;
  }

  public @NotNull QerBlockEntity getBlockEntity(Level level, BlockPos pos) {
    return BlockEntyUtil.getBlockEntity(level, pos, ModTileEntity.QER.get());
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
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state,
                                                                BlockEntityType<T> type) {
    return createTickerHelper(type, ModTileEntity.QER.get(), (l, bp, bs, be) -> be.tick(level, bp, bs));
  }

  @Override
  protected float getShadeBrightness(BlockState blockState, BlockGetter getter,
                                     BlockPos pos) {
    return 1.0F;
  }

  @Override
  protected VoxelShape getVisualShape(BlockState blockState, BlockGetter getter,
                                      BlockPos pos, CollisionContext context) {
    return Shapes.empty();
  }

  @Override
  protected boolean propagatesSkylightDown(BlockState blockState, BlockGetter getter,
                                           BlockPos pos) {
    return true;
  }

  public enum Activate implements StringRepresentable {
    DEFAULT(0, "default"),
    WORK(1, "work"),
    ;

    private final int id;
    private final String name;

    Activate(int id, String name) {
      this.id = id;
      this.name = name;
    }

    @Override
    public @NotNull String getSerializedName() {
      return name;
    }

    public int getId() {
      return id;
    }

    public String getName() {
      return name;
    }
  }
}
