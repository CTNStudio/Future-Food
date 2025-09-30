package top.ctnstudio.futurefood.common.menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

public class EnergyMenu extends BasicEnergyMenu {
  public EnergyMenu(int containerId, Inventory container, FriendlyByteBuf buf) {
    super(containerId, container, buf);
  }

  public EnergyMenu(int containerId, Inventory container, IItemHandler dataInventory, ContainerData energyData, @Nullable ContainerData data) {
    super(containerId, container, dataInventory, energyData, data);
  }

  public EnergyMenu(int containerId, Inventory container, IItemHandler dataInventory, EnergyData energyData) {
    super(containerId, container, dataInventory, energyData);
  }
}
