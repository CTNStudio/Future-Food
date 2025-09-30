package top.ctnstudio.futurefood.common.menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.items.IItemHandler;
import top.ctnstudio.futurefood.core.init.ModMenu;

public class ParticleColliderMenu extends BasicEnergyMenu {
  public ParticleColliderMenu(int containerId, Inventory container, FriendlyByteBuf buf) {
    super(ModMenu.PARTICLE_COLLIDER_MENU.get(), containerId, container, buf);
  }

  public ParticleColliderMenu(int containerId, Inventory container, IItemHandler dataInventory, EnergyData energyData) {
    super(ModMenu.PARTICLE_COLLIDER_MENU.get(), containerId, container, dataInventory, energyData);
  }
}
