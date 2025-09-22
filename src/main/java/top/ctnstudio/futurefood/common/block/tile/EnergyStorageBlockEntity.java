package top.ctnstudio.futurefood.common.block.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;
import top.ctnstudio.futurefood.capability.ModEnergyStorage;
import top.ctnstudio.futurefood.client.gui.menu.EnergyMenu;

public abstract class EnergyStorageBlockEntity extends BlockEntity
  implements MenuProvider {
  protected final ModEnergyStorage energyStorage;
  protected final ItemStackHandler itemHandler;

  public EnergyStorageBlockEntity(BlockEntityType<?> type, BlockPos pos,
    BlockState blockState) {
    this(type, pos, blockState, new ItemStackHandler(1),
      new ModEnergyStorage(10240, 1024, 1024));
  }

  public EnergyStorageBlockEntity(BlockEntityType<?> type, BlockPos pos,
    BlockState blockState, ModEnergyStorage energyStorage) {
    this(type, pos, blockState, new ItemStackHandler(1), energyStorage);
  }

  public EnergyStorageBlockEntity(BlockEntityType<?> type, BlockPos pos,
    BlockState blockState, ItemStackHandler itemHandler, ModEnergyStorage energyStorage) {
    super(type, pos, blockState);
    this.energyStorage = energyStorage;
    this.itemHandler = itemHandler;
  }

  @Override
  protected void loadAdditional(CompoundTag nbt, HolderLookup.Provider provider) {
    super.loadAdditional(nbt, provider);
    ModEnergyStorage.serializeNBT(provider, nbt, energyStorage);
    nbt.put("items", itemHandler.serializeNBT(provider));
  }

  @Override
  protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider provider) {
    super.saveAdditional(nbt, provider);
    ModEnergyStorage.deserializeNBT(provider, nbt, energyStorage);
    if (nbt.contains("items")) {
      itemHandler.deserializeNBT(provider, nbt.getCompound("items"));
    } else {
      itemHandler.setSize(1);
    }
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

  @Override
  public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
    if (!player.isHurt()) {
      return null;
    }
    return new EnergyMenu(containerId, playerInventory, itemHandler,
      energyStorage.getEnergyStored(), energyStorage.getMaxEnergyStored());
  }

  /**
   * 外部获取能量存储
   */
  public IEnergyStorage externalGetEnergyStorage(@Nullable Direction direction) {
    return energyStorage;
  }

  /**
   * 外部获取物品处理器
   */
  public IItemHandler getExternalItemHandler() {
    return itemHandler;
  }

  /**
   * 获取能量槽物品
   */
  public ItemStack getEnergyItemStack() {
    return itemHandler.getStackInSlot(0);
  }
}
