package top.ctnstudio.futurefood.client.gui.widget.energy;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities.EnergyStorage;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

public abstract class EnergySlot extends SlotItemHandler {
  public EnergySlot(IItemHandler itemHandler, int slot, int x, int y) {
    super(itemHandler, slot, x, y);
  }

  @Override
  public boolean mayPlace(ItemStack stack) {
    return stack.getCapability(EnergyStorage.ITEM) != null;
  }

  @Override
  public int getMaxStackSize() {
    return 1;
  }

  @Override
  public int getMaxStackSize(ItemStack stack) {
    return 1;
  }
}
