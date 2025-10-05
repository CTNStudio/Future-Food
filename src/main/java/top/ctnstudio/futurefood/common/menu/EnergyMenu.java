package top.ctnstudio.futurefood.common.menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.SimpleContainerData;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import top.ctnstudio.futurefood.core.init.ModMenu;

public class EnergyMenu extends BasicEnergyMenu {
  public EnergyMenu(int containerId, Inventory container, FriendlyByteBuf buf) {
    super(ModMenu.ENERGY_MENU.get(), containerId, container, new ItemStackHandler(1), new SimpleContainerData(2), buf);
  }

  public EnergyMenu(int containerId, Inventory container, IItemHandler dataInventory, EnergyData energyData) {
    super(ModMenu.ENERGY_MENU.get(), containerId, container, dataInventory, energyData);
  }
}
