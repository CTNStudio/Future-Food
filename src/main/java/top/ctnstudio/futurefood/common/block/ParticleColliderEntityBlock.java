package top.ctnstudio.futurefood.common.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.ctnstudio.futurefood.common.block.tile.ParticleColliderBlockEntity;

public class ParticleColliderEntityBlock extends BaseEntityBlock {
  private static final MapCodec<ParticleColliderEntityBlock> CODEC = simpleCodec(ParticleColliderEntityBlock::new);

  public ParticleColliderEntityBlock() {
    this(Properties.of()
      .noOcclusion()
      .isValidSpawn(argumentNever())
      .isRedstoneConductor(never())
      .isSuffocating(never())
      .isViewBlocking(never()));
  }

  public static @NotNull BlockBehaviour.StatePredicate never() {
    return (blockState, blockGetter, blockPos) -> false;
  }

  public static @NotNull BlockBehaviour.StateArgumentPredicate<EntityType<?>> argumentNever() {
    return (state, level, pos, value) -> false;
  }

  private ParticleColliderEntityBlock(Properties properties) {
    super(properties);
  }

  @Override
  protected MapCodec<? extends BaseEntityBlock> codec() {
    return CODEC;
  }

  @Override
  public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new ParticleColliderBlockEntity(pos, state);
  }

  @Override
  public RenderShape getRenderShape(BlockState state) {
    return RenderShape.ENTITYBLOCK_ANIMATED;
  }
}
