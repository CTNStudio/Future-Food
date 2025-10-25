package top.ctnstudio.futurefood.common.block.tile;

import com.google.common.collect.Sets;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.energy.IEnergyStorage;
import top.ctnstudio.futurefood.api.adapter.ModEnergyStorage;
import top.ctnstudio.futurefood.api.adapter.TileEntityUnlimitedLinkStorage;
import top.ctnstudio.futurefood.api.adapter.UnlimitedLinkStorage;
import top.ctnstudio.futurefood.api.capability.IUnlimitedLinkModify;
import top.ctnstudio.futurefood.api.capability.IUnlimitedLinkStorage;
import top.ctnstudio.futurefood.common.block.QedEntityBlock;
import top.ctnstudio.futurefood.common.menu.OutputEnergyMenu;
import top.ctnstudio.futurefood.common.payloads.UnlimitedLinkStorageData;
import top.ctnstudio.futurefood.core.init.ModTileEntity;
import top.ctnstudio.futurefood.util.BlockUtil;
import top.ctnstudio.futurefood.util.EnergyUtil;
import top.ctnstudio.futurefood.util.ModPayloadUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

// TODO 添加配置功能
// TODO 让外部无法提取能源
public class QedBlockEntity extends BaseEnergyStorageBlockEntity<OutputEnergyMenu>
  implements IUnlimitedLinkModify {
  public static final int DEFAULT_MAX_REMAINING_TIME = 5;
  protected final UnlimitedLinkStorage linkStorage; // 无限链接存储

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
    linkStorage.setOn(this);
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

    // 提取物品方块的能量
    controlItemEnergy(itemHandler, false);
    controlBlockEnergy(level, pos, bs);

    // 使用方法方便重写逻辑
    if (getRemainingTime() <= 0) {
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
  // TODO - 或许一部分应该放到客户端去处理。
  public void executeEnergyTransmission(Level qedLevel, BlockState qedBlockState) {
    if (!energyStorage.canExtract() || energyStorage.getEnergyStored() <= 0) {
      return;
    }

    final var removeSet = Sets.<BlockPos>newHashSet();
    linkStorage.getLinkSet().forEach(bp -> {
      IEnergyStorage capability = EnergyUtil.getEnergyStorageCapabilities(qedLevel, bp);
      if (capability == null) {
        removeSet.add(bp);
        return;
      }
      EnergyUtil.controlEnergy(energyStorage, capability);
    });

    removeSet.forEach(linkStorage::removeLink);
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

  @Override
  public @Nullable OutputEnergyMenu createMenu(int containerId, Inventory playerInventory, Player player) {
    if (!player.isAlive()) {
      return null;
    }
    return new OutputEnergyMenu(containerId, playerInventory, itemHandler, energyData);
  }

  @Override
  public void onLinkChanged() {
    if (level == null) {
      return;
    }
    synchronousLink();
    BlockPos pos = getBlockPos();
    BlockState blockState = level.getBlockState(pos);
    BlockState newBlockState = level.getBlockState(pos);
    IEnergyStorage iEnergyStorage = externalGetEnergyStorage(null);
    if (getUnlimitedStorage().getSize() > 0) {
      if (iEnergyStorage.getEnergyStored() > 0) {
        newBlockState = newBlockState.setValue(QedEntityBlock.ACTIVATE, QedEntityBlock.Activate.WORK);
      }
      newBlockState = newBlockState.setValue(QedEntityBlock.LIGHT, QedEntityBlock.Light.WORK);
    } else {
      if (iEnergyStorage.getEnergyStored() <= 0) {
        newBlockState = newBlockState.setValue(QedEntityBlock.LIGHT, QedEntityBlock.Light.ABNORMAL);
        newBlockState = newBlockState.setValue(QedEntityBlock.ACTIVATE, QedEntityBlock.Activate.DEFAULT);
      } else {
        newBlockState = newBlockState.setValue(QedEntityBlock.LIGHT, QedEntityBlock.Light.DEFAULT);
      }
    }
    if (!blockState.equals(newBlockState)) {
      level.setBlockAndUpdate(pos, newBlockState);
    }
  }

  @Override
  public void onEnergyChanged() {
    super.onEnergyChanged();
    setBlockState();
  }

  private void setBlockState() {
    if (!(level instanceof ServerLevel)) {
      return;
    }
    BlockPos pos = getBlockPos();
    BlockState blockState = level.getBlockState(pos);
    BlockState newBlockState = level.getBlockState(pos);
    IEnergyStorage iEnergyStorage = externalGetEnergyStorage(null);
    newBlockState = newBlockState.setValue(QedEntityBlock.ACTIVATE, iEnergyStorage.getEnergyStored() > 0 ?
      QedEntityBlock.Activate.WORK : QedEntityBlock.Activate.DEFAULT);
    if (!blockState.equals(newBlockState)) {
      level.setBlockAndUpdate(pos, newBlockState);
    }
  }

  @Override
  public void onLinkLoad() {
    synchronousLink();
  }

  public void synchronousLink() {
    if (!(level instanceof ServerLevel serverLevel)) {
      return;
    }
    serverLevel.players().stream()
      .filter(Objects::nonNull)
      .forEach(p -> ModPayloadUtil.sendToClient(p, new UnlimitedLinkStorageData(getUnlimitedStorage().getLinkPosList(), getBlockPos())));
  }
}
