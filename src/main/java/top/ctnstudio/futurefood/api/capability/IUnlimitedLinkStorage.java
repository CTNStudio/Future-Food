package top.ctnstudio.futurefood.api.capability;

import com.google.common.collect.LinkedListMultimap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.energy.IEnergyStorage;
import top.ctnstudio.futurefood.api.block.IUnlimitedEntityReceive;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * 无限连接API
 */
public interface IUnlimitedLinkStorage extends INBTSerializable<CompoundTag> {
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
  static LinkedListMultimap<Direction, IEnergyStorage> getEnergyStorageAllCapabilities(Level level, BlockPos pos) {
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
   * 链接失败
   *
   * @param pos 链接失败的方块位置
   */
  void linkFailure(BlockPos pos);

  /**
   * 移除一个链接
   *
   * @param pos 要移除的链接方块位置
   */
  boolean removeLink(BlockPos pos);

  /**
   * 获取链接的列表
   *
   * @return 链接列表
   */
  List<BlockPos> getLinkPosList();

  /**
   * 获取链接数量
   *
   * @return 链接数量
   */
  int getSize();

  /**
   * 链接一个方块
   *
   * @param level
   * @param pos   要链接的方块位置
   * @return 是否成功
   */
  boolean linkBlock(Level level, BlockPos pos);

  /**
   * 是否可以链接
   *
   * @param pos 要链接的方块位置
   * @return 是否可以链接
   */
  boolean isLink(BlockPos pos);

  /**
   * 是否包含链接
   *
   * @param pos 要链接的方块位置
   * @return 是否包含链接
   */
  boolean isContainLink(BlockPos pos);

  /**
   * 保存链接列表
   */
  CompoundTag serializeNBT(HolderLookup.Provider provider);

  /**
   * 获取一个链接的方块
   *
   * @param pos 要获取的链接方块位置
   * @return 链接的方块
   */
  BlockState getLinkBlock(BlockPos pos);

  /**
   * 加载链接列表
   */
  void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt);

  /**
   * 添加一个链接缓存
   */
  void addLinkCache(BlockPos pos);

  /**
   * 清空链接列表
   */
  void clear();

  /**
   * 修改链接
   *
   * @param level
   * @param oldPos 旧链接方块位置
   * @param newPos 新链接方块位置
   * @return 是否成功
   */
  boolean setLink(Level level, BlockPos oldPos, BlockPos newPos);

  /**
   * 设置链接列表
   *
   * @param linkList 链接列表
   */
  void setLinkList(Collection<BlockPos> linkList);

  /**
   * 获取世界
   */
  @Nullable
  Level getLevel();
}
