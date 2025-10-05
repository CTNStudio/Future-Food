package top.ctnstudio.futurefood.common.menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;
import top.ctnstudio.futurefood.api.adapter.ModEnergyStorage;
import top.ctnstudio.futurefood.client.gui.widget.energy.EnergySlot;

public abstract class BasicEnergyMenu extends AbstractContainerMenu {
  private final Inventory container;
  private final ContainerData energyData;

  public BasicEnergyMenu(MenuType<?> menuType, int containerId, Inventory container,
                         IItemHandler dataInventory, ContainerData energyData, FriendlyByteBuf buf) {
    this(menuType, containerId, container, dataInventory, energyData);
  }

  public BasicEnergyMenu(MenuType<?> menuType, int containerId, Inventory container,
                         IItemHandler dataInventory, ContainerData energyData) {
    super(menuType, containerId);
    this.container = container;
    this.energyData = energyData;
    addOtherSlot(dataInventory);
    addSlot(container);
    addDataSlots(energyData);
  }

  protected void addSlot(Inventory container) {
    // 物品栏
    for (int l = 0; l < 3; l++) {
      for (int j1 = 0; j1 < 9; j1++) {
        addSlot(new Slot(container, j1 + (l + 1) * 9, 8 + j1 * 18, 84 + l * 18));
      }
    }
    // 快捷栏
    for (int i1 = 0; i1 < 9; i1++) {
      addSlot(new Slot(container, i1, 8 + i1 * 18, 142));
    }
  }

  protected void addOtherSlot(IItemHandler dataInventory) {
    addSlot(new EnergySlot(dataInventory, 0, 8, 58));
  }

  @Override
  public void sendAllDataToRemote() {
    super.sendAllDataToRemote();
  }

  /**
   * 处理当槽位中的物品被快速移动时的逻辑（Shift点击）
   *
   * @param player 操作的玩家
   * @param index  被操作的槽位索引
   * @return 移动后的物品栈
   */
  @Override
  public ItemStack quickMoveStack(Player player, int index) {
    ItemStack itemstack = ItemStack.EMPTY;
    Slot slot = this.slots.get(index);
    // 如果槽位没有物品，直接返回空物品
    if (!slot.hasItem()) {
      return itemstack;
    }

    ItemStack itemstack1 = slot.getItem();
    itemstack = itemstack1.copy();

    // 调用物品移动逻辑处理具体移动
    ItemStack mobileResults = mobileItemLogic(index, itemstack1, slot, itemstack);
    if (mobileResults != null) {
      return mobileResults;
    }

    // 如果原物品栈为空，设置槽位为空物品，否则标记槽位已更改
    if (itemstack1.isEmpty()) {
      slot.setByPlayer(ItemStack.EMPTY, itemstack);
    } else {
      slot.setChanged();
    }

    // 如果物品数量没有变化，返回空物品
    if (itemstack1.getCount() == itemstack.getCount()) {
      return ItemStack.EMPTY;
    }

    // 触发物品取出事件
    slot.onTake(player, itemstack1);
    // 如果是第一个槽位，将物品掉落出来
    if (index == 0) {
      player.drop(itemstack1, false);
    }

    return itemstack;
  }

  /**
   * 物品移动逻辑
   *
   * @param index          移动的物品索引
   * @param movedItems     被移动物品
   * @param slot           被移动物品所在的物品槽
   * @param movedItemsCopy 被移动物品的拷贝
   * @return 移动后的物品结果，如果移动失败则返回ItemStack.EMPTY，否则返回null表示移动成功
   */
  protected @Nullable ItemStack mobileItemLogic(int index, ItemStack movedItems, Slot slot, ItemStack movedItemsCopy) {
    if (index == 0) {
      if (!this.moveItemStackTo(movedItems, 1, 37, true)) {
        return ItemStack.EMPTY;
      }
      slot.onQuickCraft(movedItems, movedItemsCopy);
    } else if (!this.moveItemStackTo(movedItems, 0, 1, false)) {
      return ItemStack.EMPTY;
    }
    return null;
  }

  @Override
  public boolean stillValid(Player player) {
    return this.container.stillValid(player);
  }

  public Inventory getContainer() {
    return container;
  }

  public int getEnergy() {
    return energyData.get(0);
  }

  public int getMaxEnergy() {
    return energyData.get(1);
  }

  public ContainerData getEnergyData() {
    return energyData;
  }

  public record EnergyData(ModEnergyStorage energyStorage) implements ContainerData {
    @Override
    public int get(int index) {
      return switch (index) {
        case 0 -> this.energyStorage.getEnergyStored();
        case 1 -> this.energyStorage.getMaxEnergyStored();
        default -> 0;
      };
    }

    @Override
    public void set(int index, int value) {
      switch (index) {
        case 0 -> energyStorage.setEnergy(value);
        case 1 -> energyStorage.setMaxEnergyStored(value);
      }
    }

    @Override
    public int getCount() {
      return 2;
    }
  }
}
