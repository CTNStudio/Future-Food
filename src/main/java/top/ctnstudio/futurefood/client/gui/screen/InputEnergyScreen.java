package top.ctnstudio.futurefood.client.gui.screen;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import top.ctnstudio.futurefood.common.menu.InputEnergyMenu;
import top.ctnstudio.futurefood.core.FutureFood;

public class InputEnergyScreen extends BasicEnergyScreen<InputEnergyMenu> {
  public static final ResourceLocation BG = FutureFood.modRL("textures/gui/container/input_energy.png");

  public InputEnergyScreen(InputEnergyMenu menu, Inventory playerInventory, Component title) {
    super(menu, playerInventory, title, BG);
  }
}
