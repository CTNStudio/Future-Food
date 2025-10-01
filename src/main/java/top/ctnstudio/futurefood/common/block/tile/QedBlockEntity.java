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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Queue;

import static top.ctnstudio.futurefood.util.ModUtil.getOppositeDirection;

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
    linkStorage.deserializeNBT(provider, nbt);
    remainingTime = nbt.getInt("remainingTime");
    if (!nbt.contains("maxRemainingTime")) {
      maxRemainingTime = DEFAULT_MAX_REMAINING_TIME;
    } else {
      maxRemainingTime = nbt.getInt("maxRemainingTime");
    }

    final var pos = this.getBlockPos();
  }

  @Override
  protected void saveAdditional(CompoundTag nbt, Provider provider) {
    super.saveAdditional(nbt, provider);
    linkStorage.serializeNBT(provider);
    nbt.putInt("remainingTime", remainingTime);
    nbt.putInt("maxRemainingTime", maxRemainingTime);
  }

  @Override
  public IEnergyStorage externalGetEnergyStorage(@Nullable Direction direction) {
    if (direction == null) {
      return energyStorage;
    }
    return !getOppositeDirection(this, direction) ? null :
      super.externalGetEnergyStorage(direction);
  }

  @Override
  public void tick(@Nonnull Level level, @Nonnull BlockPos pos, @Nonnull BlockState bs) {
    if (level.isClientSide) {
      return;
    }
    // 使用方法方便重写逻辑
    int time = getRemainingTime();

    super.tick(level, pos, bs);

    // 提取物品方块的能量
    controlItemEnergy(energyStorage, itemHandler, true);

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
      executeEnergyTransmission(level, pos, bs);
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
   * @param blockLevel 方块的世界
   * @param pos        当前方块的位置
   * @param bs         当方块的方块状态
   */
  public void executeEnergyTransmission(Level blockLevel, BlockPos pos, BlockState bs) {
    linkStorage.getLinkSet().forEach((bp) -> {
      IEnergyStorage capability = IUnlimitedLinkStorage.getEnergyStorageCapabilities(blockLevel, pos);
      if (capability == null) {
        return;
      }
      int maxReceive = energyStorage.getMaxReceive();
      if (capability.receiveEnergy(maxReceive, true) > 0) {
        capability.receiveEnergy(maxReceive, false);
      }
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

  @Override
  public void onChunkUnloaded() {
    final var pos = this.getBlockPos();
  }

  @Override
  public void onLoad() {
    final var pos = this.getBlockPos();
  }

  public IUnlimitedLinkStorage getUnlimitedStorage() {
    return linkStorage;
  }
}
