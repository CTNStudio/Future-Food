package top.ctnstudio.futurefood.client.gui.widget.energy;

import net.minecraft.resources.ResourceLocation;
import top.ctnstudio.futurefood.client.gui.widget.ImageProgressBar;
import top.ctnstudio.futurefood.core.FutureFood;

public class EnergyBar extends ImageProgressBar.Vertical {
  public static final String TOOLTIP = FutureFood.ID + ".gui.energy.tooltip";
  public static final ResourceLocation TEXTURE = FutureFood.modRL("container/energy_bar/energy_bar");

  public EnergyBar(int x, int y, int energy, int maxEnergy) {
    super(x, y, 10, 37, energy, maxEnergy, TEXTURE, TOOLTIP, true);
  }
}
