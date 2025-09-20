package top.ctnstudio.futurefood.api;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * 无限连接API
 */
public interface IUnlimitedLink {
  /**
   * 链接一个方块
   *
   * @param pos 要链接的方块位置
   * @return 是否成功
   */
  boolean linkBlock(BlockPos pos);

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
   * 获取方块的可以接收能量的能量接口
   *
   * @param level 世界
   * @param pos   方块位置
   * @return 能量接口
   */
  @NotNull
  static IEnergyStorage[] getEnergyStorageCapabilities(Level level, BlockPos pos) {
    List<IEnergyStorage> capabilities = new ArrayList<>();
    IEnergyStorage capability = level.getCapability(Capabilities.EnergyStorage.BLOCK, pos, null);
    if (capability != null && capability.canReceive()) {
      capabilities.add(capability);
    }
    for (Direction direction : Direction.values()) {
      IEnergyStorage capability1 = level.getCapability(Capabilities.EnergyStorage.BLOCK, pos,
        direction);
      if (capability1 != null && capability1.canReceive()) {
        capabilities.add(capability1);
      }
    }
    return capabilities.toArray(new IEnergyStorage[0]);
  }

  /**
   * 验证方块是否包含能获取能量的 capability
   *
   * @return 是否包含能获取能量的 capability
   */
  static boolean verifyEnergyStorage(Level level, BlockPos pos) {
    if (level == null || pos == null) {
      return false;
    }
    return getEnergyStorageCapabilities(level, pos).length != 0;
  }

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
