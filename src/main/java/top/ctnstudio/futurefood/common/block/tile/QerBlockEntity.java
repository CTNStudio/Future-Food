package top.ctnstudio.futurefood.common.block.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.ctnstudio.futurefood.api.adapter.ModEnergyStorage;
import top.ctnstudio.futurefood.api.block.IUnlimitedEntityReceive;
import top.ctnstudio.futurefood.common.menu.EnergyMenu;
import top.ctnstudio.futurefood.core.init.ModTileEntity;

import static top.ctnstudio.futurefood.util.ModUtil.getOppositeDirection;

public class QerBlockEntity extends EnergyStorageBlockEntity<EnergyMenu> implements IUnlimitedEntityReceive {

  public QerBlockEntity(BlockPos pos, BlockState blockState) {
    super(ModTileEntity.QER.get(), pos, blockState, new ModEnergyStorage(20480));
  }

  @Override
  public void tick(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState bs) {
    if (level.isClientSide) {
      return;
    }
    super.tick(level, pos, bs);
    controlItemEnergy(energyStorage, itemHandler, false);
  }

  @Override
  public IEnergyStorage externalGetEnergyStorage(@Nullable Direction direction) {
    if (direction == null) {
      return energyStorage;
    }
    return !getOppositeDirection(this, direction) ? null :
      super.externalGetEnergyStorage(direction);
  }

  /**
   * 能量存储
   */
  @Override
  public IEnergyStorage getEnergyStorage() {
    return energyStorage;
  }
}
