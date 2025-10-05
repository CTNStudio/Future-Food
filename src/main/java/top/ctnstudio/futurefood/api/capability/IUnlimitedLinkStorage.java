package top.ctnstudio.futurefood.api.capability;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.util.INBTSerializable;
import top.ctnstudio.futurefood.util.EnergyUtil;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;

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

    return EnergyUtil.getEnergyStorageCapabilities(level, pos) != null;
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
