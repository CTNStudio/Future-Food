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
import top.ctnstudio.futurefood.capability.ModEnergyStorage;

public abstract class BasicEnergyStorageBlockEntity<T extends BlockEntity> extends BlockEntity {
  protected final ModEnergyStorage energyStorage;

  public BasicEnergyStorageBlockEntity(BlockEntityType<? extends T> type,BlockPos pos, BlockState blockState) {
    this(type, pos, blockState, new ModEnergyStorage(10240, 1024, 1024));
  }

  public BasicEnergyStorageBlockEntity(BlockEntityType<? extends T> type, BlockPos pos, BlockState blockState, ModEnergyStorage energyStorage) {
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
  public void onDataPacket(Connection connection, ClientboundBlockEntityDataPacket packet, HolderLookup.Provider registries) {
    super.onDataPacket(connection, packet, registries);
  }

  public ModEnergyStorage getEnergyStorage() {
    return energyStorage;
  }
}
