package top.ctnstudio.futurefood.common.menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;
import top.ctnstudio.futurefood.client.gui.widget.energy.EnergyInputSlot;

import static top.ctnstudio.futurefood.core.init.ModMenu.ENERGY_MENU;

// Menu 是一段魔法代理，但绝对不是仅客户端的。它在服务端处理数据，然后以数据结构同步给客户端的 Screen。
// @OnlyIn(Dist.CLIENT)
public class EnergyMenu extends AbstractContainerMenu {
  private final Inventory container;
  protected int maxSlot;
  private ContainerData energyData;

  public EnergyMenu(int containerId, Inventory container, FriendlyByteBuf buf) {
    this(containerId, container, new ItemStackHandler(1), new SimpleContainerData(2), null);
  }

  public EnergyMenu(int containerId, Inventory container, IItemHandler dataInventory, EnergyData energyData) {
    this(containerId, container, dataInventory, energyData, null);
  }

  public EnergyMenu(int containerId, Inventory container, IItemHandler dataInventory, ContainerData energyData, @Nullable ContainerData data) {
    super(ENERGY_MENU.get(), containerId);
    this.container = container;
    addSlot(container, dataInventory, energyData, data);
  }

  protected void addSlot(Inventory container, IItemHandler dataInventory, ContainerData energyData, @Nullable ContainerData data) {
    int slots = dataInventory.getSlots();
    if (slots == 1) {
      addSlot(new EnergyInputSlot(dataInventory, maxSlot++, 8, 58));
    } else if (slots > 1) {
      addOtherSlot(dataInventory, data, slots);
    }
    // 物品栏
    for (int l = 0; l < 3; l++) {
      for (int j1 = 0; j1 < 9; j1++) {
        this.addSlot(new Slot(container, j1 + (l + 1) * 9, 8 + j1 * 18, 84 + l * 18));
      }
    }
    // 快捷栏
    for (int i1 = 0; i1 < 9; i1++) {
      this.addSlot(new Slot(container, i1, 8 + i1 * 18, 142));
    }

    if (energyData.getCount() > 0) {
      addDataSlots(energyData);
      this.energyData = energyData;
    } else if (data != null && data.getCount() > 0) {
      addDataSlots(data);
    }
  }

  protected void addOtherSlot(IItemHandler dataInventory, ContainerData data, int slots) {
    for (int i = 0; i < slots; i++) {
    }
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

  @Override
  public void sendAllDataToRemote() {
    super.sendAllDataToRemote();
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
    // 如果是能量输入槽(索引0)，将物品移动到玩家物品栏(索引1-27)
    if (index == 0) {
      if (!this.moveItemStackTo(movedItems, 1, 28, true)) {
        return ItemStack.EMPTY;
      }
      slot.onQuickCraft(movedItems, movedItemsCopy);
      // 如果是玩家物品栏中的物品槽(索引>=1)，将物品移动到能量输入槽(索引0)
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

  public int getMaxSlot() {
    return maxSlot;
  }

  public int getEnergy() {
    return energyData.get(0);
  }

  public int getMaxEnergy() {
    return energyData.get(1);
  }

  public void setEnergyData(EnergyData energyData) {
    this.energyData = energyData;
  }

  public ContainerData getEnergyData() {
    return energyData;
  }

  public static final class EnergyData implements ContainerData {
    private int energy;
    private int maxEnergy;

    public EnergyData(int energy, int maxEnergy) {
      this.energy = energy;
      this.maxEnergy = maxEnergy;
    }

    @Override
    public int get(int index) {
      return switch (index) {
        case 0 -> energy;
        case 1 -> maxEnergy;
        default -> 0;
      };
    }

    @Override
    public void set(int index, int value) {
      switch (index) {
        case 0 -> energy = value;
        case 1 -> maxEnergy = value;
      }
    }

    @Override
    public int getCount() {
      return 2;
    }
  }
}
