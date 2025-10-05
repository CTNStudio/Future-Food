package top.ctnstudio.futurefood.common.block.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;

public abstract class ModBlockEntity extends BlockEntity {
  public ModBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
    super(type, pos, blockState);
  }

  public static <B extends ModBlockEntity> void tick(Level level, BlockPos pos, BlockState bs, B blockEntity) {
    blockEntity.tick(level, pos, bs);
  }

  public void tick(@Nonnull Level level, @Nonnull BlockPos pos, @Nonnull BlockState bs) {
  }
}
