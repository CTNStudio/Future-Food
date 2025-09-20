package top.ctnstudio.futurefood.api.tile;

import com.google.common.collect.HashBiMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * 无限连接API
 */
public interface IUnlimitedLink {
  /**
   * 验证方块是否包含能获取能量的 capability
   *
   * @return 是否包含能获取能量的 capability
   */
  static boolean verifyEnergyStorage(Level level, BlockPos pos) {
    if (level == null || pos == null) {
      return false;
    }

    return getEnergyStorageCapabilities(level, pos) != null;
  }

  /**
   * 链接失败
   * @param pos 链接失败的方块位置
   */
  void linkFailure(BlockPos pos);

  /**
   * 移除一个链接
   * @param pos 要移除的链接方块位置
   */
  void removeLink(BlockPos pos);

  /**
   * 获取一个链接的方块
   * @param pos 要获取的链接方块位置
   * @return 链接的方块
   */
  BlockState getLinkedBlock(BlockPos pos);

  /**
   * 获取方块的能接收能量的能量接口
   *
   * @param level 世界
   * @param pos   方块位置
   * @return 能接收能量的能量接口
   */
  static IEnergyStorage getEnergyStorageCapabilities(Level level, BlockPos pos) {
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
  static HashBiMap<Direction, IEnergyStorage> getEnergyStorageAllCapabilities(Level level, BlockPos pos) {
    HashBiMap<Direction, IEnergyStorage> capabilities = HashBiMap.create(7);
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
   * 链接一个方块
   *
   * @param level
   * @param pos   要链接的方块位置
   * @return 是否成功
   */
  boolean linkBlock(Level level, BlockPos pos);

  /**
   * 序列化链接列表
   * @param provider 注册表
   * @param nbt NBT
   */
  void serializeLinkedListNBT(HolderLookup.Provider provider, CompoundTag nbt);

  /**
   * 反序列化链接列表
   * @param provider 注册表
   * @param nbt NBT
   */
  void deserializeLinkedListNBT(HolderLookup.Provider provider, CompoundTag nbt);
}
