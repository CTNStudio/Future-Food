package top.ctnstudio.futurefood.common.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.ctnstudio.futurefood.api.block.IEntityStorageBlock;
import top.ctnstudio.futurefood.common.block.tile.ParticleColliderBlockEntity;
import top.ctnstudio.futurefood.core.init.ModBlock;
import top.ctnstudio.futurefood.core.init.ModTileEntity;
import top.ctnstudio.futurefood.util.BlockEntyUtil;
import top.ctnstudio.futurefood.util.EntityItemUtil;
import top.ctnstudio.futurefood.util.ModUtil;

// TODO 完成状态变化
public class ParticleColliderEntityBlock extends HorizontalDirectionalEntityBlock
  implements IEntityStorageBlock, SimpleWaterloggedBlock {
  private static final MapCodec<ParticleColliderEntityBlock> CODEC =
    simpleCodec(ParticleColliderEntityBlock::new);

  public ParticleColliderEntityBlock() {
    this(Properties.of());
  }

  public ParticleColliderEntityBlock(Properties properties) {
    super(properties
      .noOcclusion()
      .isValidSpawn(ModBlock.argumentNever())
      .isRedstoneConductor(ModBlock.never())
      .isSuffocating(ModBlock.never())
      .isViewBlocking(ModBlock.never()));
  }

  @Override
  protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
    if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
      serverPlayer.openMenu(state.getMenuProvider(level, pos));
    }
    return InteractionResult.sidedSuccess(level.isClientSide);
  }

  @Override
  public boolean onDestroyedByPlayer(BlockState state, Level world, BlockPos pos, Player player,
                                     boolean willHarvest, FluidState fluid) {
    if (!world.isClientSide()) {
      ServerLevel serverWorld = (ServerLevel) world;
      var blockEntity = getBlockEntity(serverWorld, pos);
      EntityItemUtil.summonLootItemStacks(serverWorld, pos, ModUtil.getItemStacks(blockEntity.externalGetItemHandler(null)));
      blockEntity.clearContent();
    }
    return super.onDestroyedByPlayer(state, world, pos, player, willHarvest, fluid);
  }

  public @NotNull ParticleColliderBlockEntity getBlockEntity(Level level, BlockPos pos) {
    return BlockEntyUtil.getBlockEntity(level, pos, ModTileEntity.PARTICLE_COLLIDER.get());
  }

  @Override
  protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
    return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
  }

  @Override
  protected MapCodec<? extends HorizontalDirectionalEntityBlock> codec() {
    return CODEC;
  }

  @Override
  public RenderShape getRenderShape(BlockState state) {
    return RenderShape.ENTITYBLOCK_ANIMATED;
  }

  @Override
  public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new ParticleColliderBlockEntity(pos, state);
  }

  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state,
                                                                BlockEntityType<T> type) {
    return createTickerHelper(type, ModTileEntity.PARTICLE_COLLIDER.get(), (l, bp, bs, be) -> be.tick(level, bp, bs));
  }
}
