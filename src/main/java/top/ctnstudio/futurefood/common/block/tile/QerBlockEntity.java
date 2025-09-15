package top.ctnstudio.futurefood.common.block.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import top.ctnstudio.futurefood.init.ModTileEntity;

public class QerBlockEntity extends BlockEntity {
  public QerBlockEntity(BlockPos pos, BlockState blockState) {
    super(ModTileEntity.QER.get(), pos, blockState);
  }
}
