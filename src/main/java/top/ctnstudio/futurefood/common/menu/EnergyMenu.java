package top.ctnstudio.futurefood.common.menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.items.IItemHandler;
import top.ctnstudio.futurefood.core.init.ModMenu;

public class EnergyMenu extends BasicEnergyMenu {
  public EnergyMenu(int containerId, Inventory container, FriendlyByteBuf buf) {
    super(ModMenu.ENERGY_MENU.get(), containerId, container, buf);
  }

  public EnergyMenu(int containerId, Inventory container, IItemHandler dataInventory, EnergyData energyData) {
    super(ModMenu.ENERGY_MENU.get(), containerId, container, dataInventory, energyData);
  }
}
