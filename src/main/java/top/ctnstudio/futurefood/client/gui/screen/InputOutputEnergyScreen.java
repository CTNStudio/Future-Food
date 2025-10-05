package top.ctnstudio.futurefood.client.gui.screen;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import top.ctnstudio.futurefood.common.menu.InputOutputEnergyMenu;
import top.ctnstudio.futurefood.core.FutureFood;

public class InputOutputEnergyScreen extends BasicEnergyScreen<InputOutputEnergyMenu> {
  public static final ResourceLocation BG = FutureFood.modRL("textures/gui/container/input_output_energy.png");

  public InputOutputEnergyScreen(InputOutputEnergyMenu menu, Inventory playerInventory, Component title) {
    super(menu, playerInventory, title, BG);
  }
}
