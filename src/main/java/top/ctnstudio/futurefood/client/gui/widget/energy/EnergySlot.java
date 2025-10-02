package top.ctnstudio.futurefood.client.gui.widget.energy;

import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities.EnergyStorage;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.Nullable;
import top.ctnstudio.futurefood.client.core.ModMaterialAtlases;
import top.ctnstudio.futurefood.core.FutureFood;

public abstract class EnergySlot extends SlotItemHandler {
  public static final ResourceLocation ENERGY_ICON = FutureFood.modRL("container/energy_bar/energy_icon");
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

  @Override
  public @Nullable Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
    return Pair.of(ModMaterialAtlases.SPRITES, ENERGY_ICON);
  }
}
