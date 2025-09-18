package top.ctnstudio.futurefood.common.block.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.energy.IEnergyStorage;
import top.ctnstudio.futurefood.capability.ModEnergyStorage;

public abstract class BasicEnergyStorageBlockEntity<T extends BlockEntity> extends BlockEntity
  implements IEnergyStorage {
  protected final ModEnergyStorage energyStorage;

  public BasicEnergyStorageBlockEntity(BlockEntityType<? extends T> type, BlockPos pos,
                                       BlockState blockState) {
    this(type, pos, blockState, new ModEnergyStorage(10240, 1024, 1024));
  }

  public BasicEnergyStorageBlockEntity(BlockEntityType<? extends T> type, BlockPos pos,
                                       BlockState blockState, ModEnergyStorage energyStorage) {
    super(type, pos, blockState);
    this.energyStorage = energyStorage;
  }

  @Override
  protected void loadAdditional(CompoundTag nbt, HolderLookup.Provider registries) {
    super.loadAdditional(nbt, registries);
    ModEnergyStorage.serializeNBT(registries, nbt, energyStorage);
  }

  @Override
  protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider registries) {
    super.saveAdditional(nbt, registries);
    ModEnergyStorage.deserializeNBT(registries, nbt, energyStorage);
  }

  @Override
  public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
    CompoundTag tag = new CompoundTag();
    saveAdditional(tag, registries);
    return tag;
  }

  @Override
  public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider registries) {
    super.handleUpdateTag(tag, registries);
  }

  @Override
  public Packet<ClientGamePacketListener> getUpdatePacket() {
    return ClientboundBlockEntityDataPacket.create(this);
  }

  @Override
  public void onDataPacket(Connection connection, ClientboundBlockEntityDataPacket packet,
                           HolderLookup.Provider registries) {
    super.onDataPacket(connection, packet, registries);
  }

  public ModEnergyStorage getEnergyStorage() {
    return energyStorage;
  }

  // TODO 下面的方法补全一下

  /**
   * 接受能量
   *
   * @param value 接收量
   * @param test  模拟输入
   * @return 多余能量
   */
  @Override
  public int receiveEnergy(int value, boolean test) {
    return 0;
  }

  /**
   * 释出能量
   *
   * @param value 释出量
   * @param test  模拟输出
   * @return 实际输出
   */
  @Override
  public int extractEnergy(int value, boolean test) {
    return 0;
  }

  /**
   * 当前容器能量
   */
  @Override
  public int getEnergyStored() {
    return 0;
  }

  /**
   * 当前最大能量
   */
  @Override
  public int getMaxEnergyStored() {
    return 0;
  }

  /**
   * 可以释出
   */
  @Override
  public boolean canExtract() {
    return this.getEnergyStored() > 0;
  }

  /**
   * 可以接受
   */
  @Override
  public boolean canReceive() {
    return this.getEnergyStored() < this.getMaxEnergyStored();
  }
}
