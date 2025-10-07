package top.ctnstudio.futurefood.api.adapter;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;
import top.ctnstudio.futurefood.api.capability.IItemStackModify;

import javax.annotation.Nullable;

public class ModItemStackHandler extends ItemStackHandler {
  @Nullable
  protected IItemStackModify onContentsChanged;

  public ModItemStackHandler() {
  }

  public ModItemStackHandler(int size) {
    super(size);
  }

  public ModItemStackHandler(NonNullList<ItemStack> stacks) {
    super(stacks);
  }

  @Override
  protected void onLoad() {
    super.onLoad();
    if (onContentsChanged != null) onContentsChanged.onItemLoad();
  }

  @Override
  protected void onContentsChanged(int slot) {
    super.onContentsChanged(slot);
    if (onContentsChanged != null) onContentsChanged.onItemChanged(slot);
  }

  public void setOn(IItemStackModify onContentsChanged) {
    this.onContentsChanged = onContentsChanged;
  }
}
