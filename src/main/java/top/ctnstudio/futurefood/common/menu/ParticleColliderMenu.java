package top.ctnstudio.futurefood.common.menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

public class ParticleColliderMenu extends BasicEnergyMenu {
  public ParticleColliderMenu(int containerId, Inventory container, FriendlyByteBuf buf) {
    super(containerId, container, buf);
  }

  public ParticleColliderMenu(int containerId, Inventory container, IItemHandler dataInventory, ContainerData energyData, @Nullable ContainerData data) {
    super(containerId, container, dataInventory, energyData, data);
  }

  public ParticleColliderMenu(int containerId, Inventory container, IItemHandler dataInventory, EnergyData energyData) {
    super(containerId, container, dataInventory, energyData);
  }
}
