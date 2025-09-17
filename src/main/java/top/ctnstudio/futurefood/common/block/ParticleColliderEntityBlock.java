package top.ctnstudio.futurefood.common.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import top.ctnstudio.futurefood.common.block.tile.ParticleColliderBlockEntity;
import top.ctnstudio.futurefood.core.init.ModBlock;

// TODO 这
public class ParticleColliderEntityBlock extends HorizontalDirectionalEntityBlock {
  private static final MapCodec<ParticleColliderEntityBlock> CODEC =
    simpleCodec(ParticleColliderEntityBlock::new);

  public ParticleColliderEntityBlock() {
    this(Properties.of()
      .noOcclusion()
      .isValidSpawn(ModBlock.argumentNever())
      .isRedstoneConductor(ModBlock.never())
      .isSuffocating(ModBlock.never())
      .isViewBlocking(ModBlock.never()));
  }

  private ParticleColliderEntityBlock(Properties properties) {
    super(properties);
  }

  @Override
  protected MapCodec<? extends HorizontalDirectionalEntityBlock> codec() {
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
