package top.ctnstudio.futurefood.client.gui.widget.energy;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities.EnergyStorage;
import top.ctnstudio.futurefood.core.FutureFood;

public abstract class EnergySlot extends Slot {
  public static final ResourceLocation GUI_SPRITES_ATLAS = ResourceLocation.withDefaultNamespace("textures/atlas/gui");
  public static final ResourceLocation ENERGY_ICON = FutureFood.modRL("energy_icon");

  public EnergySlot(Container container, int slot, int x, int y) {
    super(container, slot, x, y);
    setBackground(GUI_SPRITES_ATLAS, ENERGY_ICON);
  }

  @Override
  public boolean mayPlace(ItemStack stack) {
    return stack.getCapability(EnergyStorage.ITEM) != null;
  }
}
