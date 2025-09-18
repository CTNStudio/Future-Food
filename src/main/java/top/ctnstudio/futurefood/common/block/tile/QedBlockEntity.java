package top.ctnstudio.futurefood.common.block.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import top.ctnstudio.futurefood.capability.ModEnergyStorage;
import top.ctnstudio.futurefood.core.init.ModTileEntity;

public class QedBlockEntity extends BasicEnergyStorageBlockEntity<QedBlockEntity> {
  public QedBlockEntity(BlockPos pos, BlockState blockState) {
    super(ModTileEntity.QED.get(), pos, blockState, new ModEnergyStorage(20480, 4096, 4096));
  }
}
