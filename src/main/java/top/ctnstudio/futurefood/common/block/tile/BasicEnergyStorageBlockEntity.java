package top.ctnstudio.futurefood.common.block.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.CapabilityRegistry.CapabilityConstructor;
import net.neoforged.neoforge.energy.IEnergyStorage;
import top.ctnstudio.futurefood.capability.ModEnergyStorage;

import javax.annotation.Nullable;

public abstract class BasicEnergyStorageBlockEntity extends BlockEntity
  implements IEnergyStorage, CapabilityConstructor {
  protected final ModEnergyStorage energyStorage;

  public BasicEnergyStorageBlockEntity(BlockEntityType<?> type, BlockPos pos,
                                       BlockState blockState) {
    this(type, pos, blockState,
      new ModEnergyStorage(10240, 1024, 1024));
  }

  public BasicEnergyStorageBlockEntity(BlockEntityType<?> type, BlockPos pos,
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
  public Packet<ClientGamePacketListener> getUpdatePacket() {
    return ClientboundBlockEntityDataPacket.create(this);
  }

  /**
   * 外部能量存储
   */
  public IEnergyStorage externalGetEnergyStorage(@Nullable Direction direction) {
    return energyStorage;
  }



  /**
   * 接受能量
   *
   * @param value 接收量
   * @param test  模拟输入
   * @return 多余能量
   */
  @Override
  public int receiveEnergy(int value, boolean test) {
    return energyStorage.receiveEnergy(value, test);
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
    return energyStorage.extractEnergy(value, test);
  }

  /**
   * 当前容器能量
   */
  @Override
  public int getEnergyStored() {
    return energyStorage.getEnergyStored();
  }

  /**
   * 当前最大能量
   */
  @Override
  public int getMaxEnergyStored() {
    return energyStorage.getMaxEnergyStored();
  }

  /**
   * 可以释出
   */
  @Override
  public boolean canExtract() {
    return energyStorage.canExtract();
  }

  /**
   * 可以接受
   */
  @Override
  public boolean canReceive() {
    return energyStorage.canReceive();
  }
}
