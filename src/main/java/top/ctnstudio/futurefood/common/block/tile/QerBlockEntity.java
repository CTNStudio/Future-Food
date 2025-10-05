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
import top.ctnstudio.futurefood.util.BlockUtil;
import top.ctnstudio.futurefood.util.EnergyUtil;

public class QerBlockEntity extends EnergyStorageBlockEntity<EnergyMenu> implements IUnlimitedEntityReceive {

  public QerBlockEntity(BlockPos pos, BlockState blockState) {
    super(ModTileEntity.QER.get(), pos, blockState, new ModEnergyStorage(20480));
  }

  /**
   * 获取一个坐标的周围方块
   *
   * @param level 世界
   * @param pos   坐标
   * @param bs    方块状态
   * @return 周围方块
   */
  @javax.annotation.Nullable
  private static BlockState getSurroundingBlock(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState bs) {
    Direction direction = BlockUtil.getFacingDirection(bs).orElse(null);
    if (direction == null) return null;
    return getSurroundingDirectionBlock(level, pos, direction);
  }

  @NotNull
  private static BlockState getSurroundingDirectionBlock(@NotNull Level level, @NotNull BlockPos pos, @NotNull Direction direction) {
    return BlockUtil.getSurroundingDirectionBlock(level, pos, direction.getOpposite());
  }

  @Override
  public void tick(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState bs) {
    if (level.isClientSide) {
      return;
    }

    controlItemEnergy(itemHandler, true);

    // TODO 注意：未做优化 理论要根据方块更新进行缓存再传输，以避免过多的获取
    IEnergyStorage energyStorage = getSurroundingEnergyStorage(level, pos, bs);
    if (energyStorage != null) {
      EnergyUtil.controlEnergy(this.energyStorage, energyStorage);
    }
  }

  @Override
  public IEnergyStorage externalGetEnergyStorage(@Nullable Direction direction) {
    if (direction == null) {
      return energyStorage;
    }
    return BlockUtil.getOppositeDirection(this, direction) ? super.externalGetEnergyStorage(direction) : null;
  }

  /**
   * 能量存储
   */
  @Override
  public IEnergyStorage getEnergyStorage() {
    return energyStorage;
  }
}
