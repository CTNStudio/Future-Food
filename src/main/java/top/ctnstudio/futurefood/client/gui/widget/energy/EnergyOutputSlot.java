package top.ctnstudio.futurefood.client.gui.widget.energy;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities.EnergyStorage;
import net.neoforged.neoforge.energy.IEnergyStorage;

/**
 * 能量输出槽
 */
public class EnergyOutputSlot extends EnergySlot {
  public EnergyOutputSlot(Container container, int slot, int x, int y) {
    super(container, slot, x, y);
  }

  @Override
  public boolean mayPlace(ItemStack stack) {
    IEnergyStorage capability = stack.getCapability(EnergyStorage.ITEM);
    if (capability == null) {
      return false;
    }
    return capability.canReceive();
  }
}
