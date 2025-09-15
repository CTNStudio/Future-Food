package top.ctnstudio.futurefood.common.block.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import top.ctnstudio.futurefood.init.ModTileEntity;

public class QedBlockEntity extends BlockEntity {
  public QedBlockEntity(BlockPos pos, BlockState blockState) {
    super(ModTileEntity.QED.get(), pos, blockState);
  }
}
