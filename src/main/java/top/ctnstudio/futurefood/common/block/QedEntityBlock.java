package top.ctnstudio.futurefood.common.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.ctnstudio.futurefood.api.block.IEntityStorageBlock;
import top.ctnstudio.futurefood.api.block.IUnlimitedEntityReceive;
import top.ctnstudio.futurefood.api.capability.IUnlimitedLinkStorage;
import top.ctnstudio.futurefood.common.block.tile.QedBlockEntity;
import top.ctnstudio.futurefood.core.init.ModBlock;
import top.ctnstudio.futurefood.core.init.ModTileEntity;
import top.ctnstudio.futurefood.datagen.tag.FfBlockTags;
import top.ctnstudio.futurefood.util.EntityItemUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static top.ctnstudio.futurefood.util.BlockEntyUtil.getBlockEntityFromLevel;

// TODO 完成状态变化
// TODO 破坏不掉落库存物品
public class QedEntityBlock extends DirectionEntityBlock<QedBlockEntity> implements IEntityStorageBlock, SimpleWaterloggedBlock {
  private static final MapCodec<QedEntityBlock> CODEC = simpleCodec(QedEntityBlock::new);
  public static final EnumProperty<Activate> ACTIVATE = EnumProperty.create("activate", Activate.class);
  public static final EnumProperty<Light> LIGHT = EnumProperty.create("light", Light.class);
  public QedEntityBlock() {
    this(Properties.of());
  }

  public QedEntityBlock(Properties properties) {
    super(properties.noOcclusion()
      .isValidSpawn(ModBlock.argumentNever())
      .isRedstoneConductor(ModBlock.never())
      .isSuffocating(ModBlock.never())
      .isViewBlocking(ModBlock.never()));
    this.registerDefaultState(this.stateDefinition.any()
      .setValue(ACTIVATE, Activate.DEFAULT)
      .setValue(LIGHT, Light.DEFAULT)
    );
  }

  @Override
  protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    builder.add(ACTIVATE, LIGHT);
  }

  /**
   * 与周围方块建立无限链接
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
      .forEach(entry -> linkBlock(level, entry.getKey(), blockEntity.getUnlimitedStorage()));
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

  /**
   * 链接方块
   */
  // TODO 添加配置功能 进行链接条件判断
  public boolean linkBlock(Level level, BlockPos pos, IUnlimitedLinkStorage linkStorage) {
    return linkStorage.linkBlock(level, pos);
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
  public QedBlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new QedBlockEntity(pos, state);
  }

  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state,
                                                                BlockEntityType<T> type) {
    return createTickerHelper(type, ModTileEntity.QED.get(), (l, bp, bs, be) -> be.tick(level, bp, bs));
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
  protected float getShadeBrightness(BlockState blockState, BlockGetter getter,
    BlockPos pos) {
    return 1.0F;
  }

  @Override
  protected VoxelShape getVisualShape(BlockState state, BlockGetter getter,
                                      BlockPos pos, CollisionContext context) {
    return Shapes.empty();
  }

  @Override
  protected void spawnAfterBreak(BlockState state, ServerLevel level, BlockPos pos, ItemStack stack,
                                 boolean dropExperience) {
    final var tile = getBlockEntity(level, pos);
    EntityItemUtil.summonLootItems(level, pos, tile.getEnergyItemStack().copy());
    tile.clearContent();
  }

  @Override
  protected boolean propagatesSkylightDown(BlockState state, BlockGetter getter,
    BlockPos pos) {
    return true;
  }

  public enum Activate implements StringRepresentable {
    DEFAULT(0, "default"),
    WORK(1, "work"),
    FLASH(2, "flash"),
    FLASH1(3, "flash1"),
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

  public enum Light implements StringRepresentable {
    DEFAULT(0, "default"),
    WORK(1, "work"),
    ABNORMAL(2, "abnormal"),
    ;

    private final int id;
    private final String name;

    Light(int id, String name) {
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
