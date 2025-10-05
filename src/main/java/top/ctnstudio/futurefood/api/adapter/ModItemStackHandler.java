package top.ctnstudio.futurefood.api.adapter;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;
import top.ctnstudio.futurefood.api.capability.IModStackModify;

public class ModItemStackHandler extends ItemStackHandler {
  private IModStackModify onContentsChanged;

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
    onContentsChanged.onStackLoad();
  }

  @Override
  protected void onContentsChanged(int slot) {
    super.onContentsChanged(slot);
    onContentsChanged.onStackContentsChanged(slot);
  }

  public void setOn(IModStackModify onContentsChanged) {
    this.onContentsChanged = onContentsChanged;
  }
}
