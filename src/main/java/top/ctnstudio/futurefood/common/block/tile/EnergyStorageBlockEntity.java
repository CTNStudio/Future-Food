package top.ctnstudio.futurefood.common.block.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;
import top.ctnstudio.futurefood.api.adapter.ModEnergyStorage;
import top.ctnstudio.futurefood.common.menu.EnergyMenu;
import top.ctnstudio.futurefood.common.menu.EnergyMenu.EnergyData;
import top.ctnstudio.futurefood.util.EntityItemUtil;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public abstract class EnergyStorageBlockEntity extends BlockEntity
  implements Container, MenuProvider {
  public static final Supplier<ModEnergyStorage> DEFAULT_ENERGY_STORAGE
    = () -> new ModEnergyStorage(10240, 1024, 1024);
  protected final ModEnergyStorage energyStorage;
  protected final ItemStackHandler itemHandler;
  protected final EnergyData energyData;

  public EnergyStorageBlockEntity(BlockEntityType<?> type, BlockPos pos,
    BlockState blockState) {
    this(type, pos, blockState, new ItemStackHandler(1),
      DEFAULT_ENERGY_STORAGE.get());
  }

  public EnergyStorageBlockEntity(BlockEntityType<?> type, BlockPos pos,
    BlockState blockState, ItemStackHandler itemHandler, ModEnergyStorage energyStorage) {
    super(type, pos, blockState);
    this.energyStorage = energyStorage;
    this.itemHandler = itemHandler;
    energyData = new EnergyData(this.energyStorage);
  }

  public EnergyStorageBlockEntity(BlockEntityType<?> type, BlockPos pos,
                                  BlockState blockState, ModEnergyStorage energyStorage) {
    this(type, pos, blockState, new ItemStackHandler(1), energyStorage);
  }

  /**
   * 处理接收的数据
   */
  @Override
  public void handleUpdateTag(CompoundTag tag, Provider provider) {
    super.handleUpdateTag(tag, provider);
    loadAdditional(tag, provider);
  }

  /**
   * 加载数据
   */
  @Override
  protected void loadAdditional(CompoundTag nbt, HolderLookup.Provider provider) {
    super.loadAdditional(nbt, provider);
    if (nbt.contains("energyStorage")) {
      energyStorage.deserializeNBT(provider, nbt.getCompound("energyStorage"));
    } else {
      energyStorage.setEnergy(0);
    }
    if (nbt.contains("items")) {
      itemHandler.deserializeNBT(provider, nbt.getCompound("items"));
    } else {
      itemHandler.setSize(1);
    }
  }

  /**
   * 保存数据
   */
  @Override
  protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider provider) {
    super.saveAdditional(nbt, provider);
    nbt.put("energyStorage", energyStorage.serializeNBT(provider));
    nbt.put("items", itemHandler.serializeNBT(provider));
  }

  /**
   * 获取更新数据包
   */
  @Override
  public Packet<ClientGamePacketListener> getUpdatePacket() {
    return ClientboundBlockEntityDataPacket.create(this);
  }

  /**
   * 获取更新nbt
   */
  @Override
  public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
    CompoundTag tag = super.getUpdateTag(provider);
    saveAdditional(tag, provider);
    return tag;
  }

  @Override
  public @Nullable EnergyMenu createMenu(int containerId, Inventory playerInventory, Player player) {
    if (!player.isAlive()) {
      return null;
    }
    return new EnergyMenu(containerId, playerInventory, itemHandler, energyData);
  }

  @Override
  public void writeClientSideData(AbstractContainerMenu menu, RegistryFriendlyByteBuf buffer) {
    if (!(menu instanceof EnergyMenu energyMenu)) {
      return;
    }

    ContainerData energyData = energyMenu.getEnergyData();
    int energy = energyData.get(0);
    int maxEnergy = energyData.get(1);
    buffer.writeInt(energy).writeInt(maxEnergy);
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
  public IItemHandler externalGetItemHandler(@Nullable Direction direction) {
    return itemHandler;
  }

  /**
   * 获取能量槽物品
   */
  public ItemStack getEnergyItemStack() {
    return itemHandler.getStackInSlot(0);
  }

  @Override
  public int getContainerSize() {
    return itemHandler.getSlots();
  }

  @Override
  public boolean isEmpty() {
    for (ItemStack stack : getItems()) {
      if (stack.isEmpty()) {
        return false;
      }
    }
    return true;
  }

  @Override
  public ItemStack getItem(int slot) {
    return itemHandler.getStackInSlot(slot);
  }

  @Override
  public ItemStack removeItem(int slot, int amount) {
    return itemHandler.extractItem(slot, amount, false);
  }

  @Override
  public ItemStack removeItemNoUpdate(int slot) {
    return itemHandler.extractItem(slot, getItem(slot).getCount(), false);
  }

  @Override
  public void setItem(int slot, ItemStack stack) {
    itemHandler.setStackInSlot(slot, stack);
  }

  @Override
  public boolean stillValid(Player player) {
    return !this.isRemoved() && player.canInteractWithEntity(new AABB(getBlockPos()), 4.0);
  }

  protected List<ItemStack> getItems() {
    List<ItemStack> list = new ArrayList<>();
    int slots = itemHandler.getSlots();
    for (int i = 0; i < slots; i++) {
      itemHandler.getStackInSlot(i);
    }
    return list;
  }

  @Override
  public void clearContent() {
    if (getLevel() != null && getLevel().isClientSide) {
      return;
    }
    ServerLevel serverLevel = (ServerLevel) getLevel();
    for (ItemStack stack : getItems()) {
      EntityItemUtil.summonLootItems(serverLevel, getBlockPos(), stack);
    }
  }

  @Override
  public Component getDisplayName() {
    return getBlockState().getBlock().getName();
  }

  public void tick(@Nonnull Level level, @Nonnull BlockPos pos, @Nonnull BlockState bs) {
  }
}
