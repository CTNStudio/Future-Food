package top.ctnstudio.futurefood.client.gui.screen;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import top.ctnstudio.futurefood.common.menu.EnergyMenu;
import top.ctnstudio.futurefood.core.FutureFood;

public class EnergyScreen extends BasicEnergyScreen<EnergyMenu> {
  public static final ResourceLocation BG = FutureFood.modRL("textures/gui/container/energy.png");

  public EnergyScreen(EnergyMenu menu, Inventory playerInventory, Component title) {
    super(menu, playerInventory, title, BG);
  }
}
