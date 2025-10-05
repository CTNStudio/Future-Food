package top.ctnstudio.futurefood.common.menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;
import top.ctnstudio.futurefood.client.gui.widget.energy.EnergyInputSlot;
import top.ctnstudio.futurefood.client.gui.widget.energy.EnergyOutputSlot;
import top.ctnstudio.futurefood.core.init.ModMenu;

public class InputOutputEnergyMenu extends BasicEnergyMenu {
  public InputOutputEnergyMenu(int containerId, Inventory container, FriendlyByteBuf buf) {
    super(ModMenu.ENERGY_MENU.get(), containerId, container, new ItemStackHandler(2), new SimpleContainerData(2), buf);
  }

  public InputOutputEnergyMenu(int containerId, Inventory container, IItemHandler dataInventory, EnergyData energyData) {
    super(ModMenu.ENERGY_MENU.get(), containerId, container, dataInventory, energyData);
  }

  @Override
  protected void addOtherSlot(IItemHandler dataInventory) {
    addSlot(new EnergyOutputSlot(dataInventory, 0, 8, 58));
    addSlot(new EnergyInputSlot(dataInventory, 0, 26, 58));
  }

  @Override
  protected @Nullable ItemStack mobileItemLogic(int index, ItemStack movedItems, Slot slot, ItemStack movedItemsCopy) {
    if (index < 2) {
      if (!this.moveItemStackTo(movedItems, 2, 38, true)) {
        return ItemStack.EMPTY;
      }
      slot.onQuickCraft(movedItems, movedItemsCopy);
    } else if (!this.moveItemStackTo(movedItems, 0, 2, false)) {
      return ItemStack.EMPTY;
    }
    return null;
  }
}
