package top.ctnstudio.futurefood.common.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import top.ctnstudio.futurefood.api.block.IEntityStorageBlock;
import top.ctnstudio.futurefood.common.block.tile.GluttonyBlockEntity;
import top.ctnstudio.futurefood.common.block.tile.ModBlockEntity;
import top.ctnstudio.futurefood.core.init.ModBlock;
import top.ctnstudio.futurefood.core.init.ModTileEntity;

public class GluttonyEntityBlock extends HorizontalDirectionalEntityBlock<GluttonyBlockEntity> implements IEntityStorageBlock {
  public static final BooleanProperty ACTIVATE = BooleanProperty.create("activate");
  private static final MapCodec<GluttonyEntityBlock> CODEC = simpleCodec(GluttonyEntityBlock::new);

  public GluttonyEntityBlock() {
    this(Properties.of());
  }

  public GluttonyEntityBlock(Properties properties) {
    super(properties
      .noOcclusion()
      .isValidSpawn(ModBlock.argumentNever())
      .isRedstoneConductor(ModBlock.never())
      .isSuffocating(ModBlock.never())
      .isViewBlocking(ModBlock.never()), ModTileEntity.GLUTTONY);
    this.registerDefaultState(this.stateDefinition.any().setValue(ACTIVATE, false));
  }

  @Override
  protected RenderShape getRenderShape(BlockState state) {
    return RenderShape.MODEL;
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    builder.add(ACTIVATE);
  }

  @Override
  protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
    if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
      serverPlayer.openMenu(state.getMenuProvider(level, pos));
    }
    return InteractionResult.sidedSuccess(level.isClientSide);
  }

  @Override
  public @Nullable GluttonyBlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new GluttonyBlockEntity(pos, state);
  }

  @Override
  protected MapCodec<? extends BaseEntityBlock> codec() {
    return CODEC;
  }

  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
    return createTickerHelper(type, ModTileEntity.GLUTTONY.get(), ModBlockEntity::tick);
  }
}
