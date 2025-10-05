package top.ctnstudio.futurefood.common.menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.SimpleContainerData;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import top.ctnstudio.futurefood.client.gui.widget.energy.EnergyOutputSlot;
import top.ctnstudio.futurefood.core.init.ModMenu;

public class OutputEnergyMenu extends BasicEnergyMenu {
  public OutputEnergyMenu(int containerId, Inventory container, FriendlyByteBuf buf) {
    super(ModMenu.OUTPUT_ENERGY_MENU.get(), containerId, container, new ItemStackHandler(1), new SimpleContainerData(2), buf);
  }

  public OutputEnergyMenu(int containerId, Inventory container, IItemHandler dataInventory, EnergyData energyData) {
    super(ModMenu.OUTPUT_ENERGY_MENU.get(), containerId, container, dataInventory, energyData);
  }

  @Override
  protected void addOtherSlot(IItemHandler dataInventory) {
    addSlot(new EnergyOutputSlot(dataInventory, 0, 8, 58));
  }
}
