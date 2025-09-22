package top.ctnstudio.futurefood.client.gui.widget.energy;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities.EnergyStorage;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.items.IItemHandler;

/**
 * 能量输入槽
 */
public class EnergyInputSlot extends EnergySlot {
  public EnergyInputSlot(IItemHandler itemHandler, int slot, int x, int y) {
    super(itemHandler, slot, x, y);
  }

  @Override
  public boolean mayPlace(ItemStack stack) {
    IEnergyStorage capability = stack.getCapability(EnergyStorage.ITEM);
    if (capability == null) {
      return false;
    }
    return capability.canExtract();
  }
}
