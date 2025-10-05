package top.ctnstudio.futurefood.util;

import com.google.common.collect.LinkedListMultimap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.ctnstudio.futurefood.api.block.IUnlimitedEntityReceive;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class EnergyUtil {

  /**
   * 获取方块方向的对应能接收能量的能量接口
   *
   * @param level     世界
   * @param pos       中心坐标
   * @param direction 方向
   * @return 能接收能量的能量接口
   */
  @Nullable
  public static IEnergyStorage getSurroundingEnergyDirectionStorage(@NotNull Level level, @NotNull BlockPos pos, @NotNull Direction direction) {
    return level.getCapability(Capabilities.EnergyStorage.BLOCK, pos.relative(direction.getOpposite(), 1), direction);
  }

  /**
   * 控制两个能源槽之间的能量传递
   *
   * @param extract 被提取
   * @param receive 接收的
   */
  public static void controlEnergy(@NotNull IEnergyStorage extract, @NotNull IEnergyStorage receive) {
    int energyStored = extract.getEnergyStored();
    if (energyStored <= 0 ||
      receive.getEnergyStored() >= receive.getMaxEnergyStored() ||
      !extract.canExtract() || !receive.canReceive()) {
      return;
    }
    int extractValue = extract.extractEnergy(energyStored, true);
    if (extractValue <= 0) {
      return;
    }
    int receiveValue = receive.receiveEnergy(extractValue, true);
    if (receiveValue <= 0) {
      return;
    }
    extract.extractEnergy(receiveValue, false);
    receive.receiveEnergy(receiveValue, false);
  }

  /**
   * 获取方块的能接收能量的能量接口
   *
   * @param level 世界
   * @param pos   方块位置
   * @return 能接收能量的能量接口
   */
  @Nullable
  public static IEnergyStorage getEnergyStorageCapabilities(Level level, BlockPos pos) {
    if (level.getBlockEntity(pos) instanceof IUnlimitedEntityReceive i) {
      return i.getEnergyStorage();
    }
    final var capabilities = getEnergyStorageAllCapabilities(level, pos);
    return capabilities.values().stream().filter(Objects::nonNull).findAny().orElse(null);
  }

  /**
   * 获取方块所有可以接收能量的能量接口
   *
   * @param level 世界
   * @param pos   方块位置
   * @return 能量接口
   */
  @Nonnull
  public static LinkedListMultimap<Direction, IEnergyStorage> getEnergyStorageAllCapabilities(Level level, BlockPos pos) {
    var capabilities = LinkedListMultimap.<Direction, IEnergyStorage>create(7);
    IEnergyStorage capability = level.getCapability(Capabilities.EnergyStorage.BLOCK, pos, null);
    if (capability != null && capability.canReceive()) {
      capabilities.put(null, capability);
    }
    for (Direction direction : Direction.values()) {
      IEnergyStorage capability1 = level.getCapability(Capabilities.EnergyStorage.BLOCK, pos,
        direction);
      if (capability1 != null && capability1.canReceive()) {
        capabilities.put(direction, capability1);
      }
    }
    return capabilities;
  }

  /**
   * 获取周围方块的对应方向能接收能量的能量接口
   *
   * @param level 中心世界
   * @param pos   坐标
   * @return 能接收能量的能量接口
   */
  public static Map<Direction, Map.Entry<BlockPos, Optional<IEnergyStorage>>> getSurroundingEnergyStorage(Level level, BlockPos pos) {
    var capabilities = new HashMap<Direction, Map.Entry<BlockPos, Optional<IEnergyStorage>>>();
    for (Direction direction : Direction.values()) {
      var capability = getSurroundingEnergyDirectionStorage(level, pos, direction);
      capabilities.put(direction, Map.entry(pos.relative(direction.getOpposite(), 1), Optional.ofNullable(capability)));
    }
    return capabilities;
  }
}
