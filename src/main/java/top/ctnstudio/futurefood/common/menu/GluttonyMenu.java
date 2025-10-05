package top.ctnstudio.futurefood.common.menu;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.Nullable;
import top.ctnstudio.futurefood.core.init.ModMenu;

public class GluttonyMenu extends BasicEnergyMenu {
  protected final ContainerData workProgress;

  public GluttonyMenu(int containerId, Inventory container, FriendlyByteBuf buf) {
    super(ModMenu.GLUTTONY_MENU.get(), containerId, container,
      new ItemStackHandler(4), new SimpleContainerData(2), buf);
    this.workProgress = new SimpleContainerData(2);
    addDataSlots(workProgress);
  }

  public GluttonyMenu(int containerId, Inventory container, IItemHandler dataInventory,
                      EnergyData energyData, ContainerData data) {
    super(ModMenu.GLUTTONY_MENU.get(), containerId, container, dataInventory, energyData);
    this.workProgress = data;
    addDataSlots(workProgress);
  }

  @Override
  protected void addOtherSlot(IItemHandler dataInventory) {
    super.addOtherSlot(dataInventory);
    addSlot(new SlotItemHandler(dataInventory, 1, 51, 22) {
      // 仅允许带有食物组件的物品输入
      @Override
      public boolean mayPlace(ItemStack stack) {
        return stack.getComponents().has(DataComponents.FOOD);
      }
    });
    addSlot(new SlotItemHandler(dataInventory, 2, 109, 58) {
      // 仅允许输出
      @Override
      public boolean mayPlace(ItemStack stack) {
        return false;
      }
    });
  }

  @Override
  protected @Nullable ItemStack mobileItemLogic(int index, ItemStack movedItems, Slot slot, ItemStack movedItemsCopy) {
    if (index >= 0 && index < 4) {
      if (!this.moveItemStackTo(movedItems, 3, 39, true)) {
        return ItemStack.EMPTY;
      }
      slot.onQuickCraft(movedItems, movedItemsCopy);
    } else if (!this.moveItemStackTo(movedItems, 0, 3, false)) {
      return ItemStack.EMPTY;
    }
    return null;
  }

  public int getRemainingTick() {
    return workProgress.get(0);
  }

  public void setRemainingTick(int remainingTick) {
    this.workProgress.set(0, remainingTick);
  }

  public int getMaxWorkTick() {
    return workProgress.get(1);
  }

  public void setMaxWorkTick(int maxWorkTick) {
    this.workProgress.set(1, maxWorkTick);
  }
}
