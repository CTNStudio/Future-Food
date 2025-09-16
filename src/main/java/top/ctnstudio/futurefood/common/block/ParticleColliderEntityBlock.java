package top.ctnstudio.futurefood.common.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class ParticleColliderEntityBlock extends BaseEntityBlock {
  private static final MapCodec<ParticleColliderEntityBlock> CODEC = simpleCodec(ParticleColliderEntityBlock::new);

  public ParticleColliderEntityBlock() {
    this(Properties.of());
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
    return null;
  }
}
