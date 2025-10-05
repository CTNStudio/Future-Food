package top.ctnstudio.futurefood.client.gui.screen;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import top.ctnstudio.futurefood.common.menu.OutputEnergyMenu;
import top.ctnstudio.futurefood.core.FutureFood;

public class OutputEnergyScreen extends BasicEnergyScreen<OutputEnergyMenu> {
  public static final ResourceLocation BG = FutureFood.modRL("textures/gui/container/output_energy.png");

  public OutputEnergyScreen(OutputEnergyMenu menu, Inventory playerInventory, Component title) {
    super(menu, playerInventory, title, BG);
  }
}
