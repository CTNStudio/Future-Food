package top.ctnstudio.futurefood.common.block.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.energy.IEnergyStorage;
import top.ctnstudio.futurefood.api.adapter.ModEnergyStorage;
import top.ctnstudio.futurefood.api.adapter.TileEntityUnlimitedLinkStorage;
import top.ctnstudio.futurefood.api.adapter.UnlimitedLinkStorage;
import top.ctnstudio.futurefood.api.capability.IUnlimitedLinkStorage;
import top.ctnstudio.futurefood.common.menu.EnergyMenu;
import top.ctnstudio.futurefood.core.init.ModTileEntity;
import top.ctnstudio.futurefood.util.BlockUtil;
import top.ctnstudio.futurefood.util.EnergyUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Queue;

// TODO 添加配置功能
public class QedBlockEntity extends EnergyStorageBlockEntity<EnergyMenu> {
  public static final int DEFAULT_MAX_REMAINING_TIME = 5;
  protected final UnlimitedLinkStorage linkStorage; // 无限链接存储
  public float sphereTick;

  /**
   * 剩余传递计时
   */
  private int remainingTime;
  /**
   * 最大传递计时
   */
  private int maxRemainingTime; // 使用变量以方便后续做升级

  public QedBlockEntity(BlockPos pos, BlockState blockState) {
    this(ModTileEntity.QED.get(), pos, blockState,
      new ModEnergyStorage(20480),
      DEFAULT_MAX_REMAINING_TIME);
  }

  public QedBlockEntity(BlockEntityType<? extends QedBlockEntity> type, BlockPos pos,
                        BlockState blockState, ModEnergyStorage energyStorage,
                        int maxRemainingTime) {
    super(type, pos, blockState, energyStorage);
    this.maxRemainingTime = maxRemainingTime;
    linkStorage = new TileEntityUnlimitedLinkStorage(this);
  }

  @Override
  protected void loadAdditional(CompoundTag nbt, Provider provider) {
    super.loadAdditional(nbt, provider);
    if (nbt.contains("linkStorage"))
      linkStorage.deserializeNBT(provider, nbt.getCompound("linkStorage"));
    if (nbt.contains("remainingTime")) remainingTime = nbt.getInt("remainingTime");
    maxRemainingTime = !nbt.contains("maxRemainingTime") ? DEFAULT_MAX_REMAINING_TIME : nbt.getInt("maxRemainingTime");
  }

  @Override
  protected void saveAdditional(CompoundTag nbt, Provider provider) {
    super.saveAdditional(nbt, provider);
    nbt.put("linkStorage", linkStorage.serializeNBT(provider));
    nbt.putInt("remainingTime", remainingTime);
    nbt.putInt("maxRemainingTime", maxRemainingTime);
  }

  @Override
  public IEnergyStorage externalGetEnergyStorage(@Nullable Direction direction) {
    if (direction == null) {
      return energyStorage;
    }
    return BlockUtil.getOppositeDirection(this, direction) ? super.externalGetEnergyStorage(direction) : null;
  }

  @Override
  public void tick(@Nonnull Level level, @Nonnull BlockPos pos, @Nonnull BlockState bs) {
    if (level.isClientSide) {
      return;
    }
    // 使用方法方便重写逻辑
    int time = getRemainingTime();

    // 提取物品方块的能量
    controlItemEnergy(itemHandler, false);

    Queue<BlockPos> cacheData = linkStorage.getCacheData();
    if (!cacheData.isEmpty()) {
      for (BlockPos cache : cacheData) {
        if (level.getBlockEntity(cache) instanceof IEnergyStorage) {
          linkStorage.linkBlock(level, cache);
        } else {
          linkStorage.removeLink(pos);
        }
      }
    }

    if (time <= 0) {
      executeEnergyTransmission(level, bs);
      resetRemainingTime();
    }
    remainingTime--;
  }

  public int getRemainingTime() {
    return remainingTime;
  }

  /**
   * 执行能量传递
   *
   * @param qedLevel      qed世界
   * @param qedBlockState qed方块状态
   */
  public void executeEnergyTransmission(Level qedLevel, BlockState qedBlockState) {
    if (!energyStorage.canExtract() || energyStorage.getEnergyStored() <= 0) {
      return;
    }

    linkStorage.getLinkSet().forEach(bp -> {
      IEnergyStorage capability = EnergyUtil.getEnergyStorageCapabilities(qedLevel, bp);
      if (capability == null) {
        return;
      }
      EnergyUtil.controlEnergy(energyStorage, capability);
    });
  }

  /**
   * 重置传递时间
   */
  public void resetRemainingTime() {
    remainingTime = maxRemainingTime;
  }

  public int getMaxRemainingTime() {
    return maxRemainingTime;
  }

  public IUnlimitedLinkStorage getUnlimitedStorage() {
    return linkStorage;
  }
}
