package top.ctnstudio.futurefood.common.block.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.ctnstudio.futurefood.api.adapter.ModEnergyStorage;
import top.ctnstudio.futurefood.api.block.IUnlimitedEntityReceive;
import top.ctnstudio.futurefood.common.block.DirectionEntityBlock;
import top.ctnstudio.futurefood.common.menu.EnergyMenu;
import top.ctnstudio.futurefood.core.init.ModTileEntity;
import top.ctnstudio.futurefood.util.ModUtil;

import java.util.Optional;

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

    // TODO 输入到通用机械的能源转换有误差
    controlItemEnergy(itemHandler, true);

    IEnergyStorage energyStorage = getSurroundingEnergyStorage(level, pos, bs);
    if (energyStorage != null) {
      ModUtil.controlEnergy(this.energyStorage, energyStorage);
    }
  }

  @Nullable
  public static BlockState getSurroundingBlock(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState bs) {
    Direction direction = getDirection(bs).orElse(null);
    if (direction == null) return null;
    return getSurroundingDirectionBlock(level, pos, direction);
  }

  @NotNull
  private static BlockState getSurroundingDirectionBlock(@NotNull Level level, @NotNull BlockPos pos, @NotNull Direction direction) {
    return level.getBlockState(pos.relative(direction.getOpposite(), 1));
  }

  @Nullable
  public static IEnergyStorage getSurroundingEnergyStorage(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState bs) {
    Direction direction = getDirection(bs).orElse(null);
    if (direction == null) return null;
    return getSurroundingEnergyDirectionStorage(level, pos, direction);
  }

  @Nullable
  private static IEnergyStorage getSurroundingEnergyDirectionStorage(@NotNull Level level, @NotNull BlockPos pos, @NotNull Direction direction) {
    return level.getCapability(Capabilities.EnergyStorage.BLOCK, pos.relative(direction.getOpposite(), 1), direction);
  }

  public static Optional<Direction> getDirection(@NotNull BlockState bs) {
    return bs.getOptionalValue(DirectionEntityBlock.FACING);
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
