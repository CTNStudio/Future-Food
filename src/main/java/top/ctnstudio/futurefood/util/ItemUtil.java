package top.ctnstudio.futurefood.util;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;

import java.util.ArrayList;
import java.util.List;

public class ItemUtil {
  /**
   * 获取 IItemHandler 中的物品集合
   *
   * @param handler IItemHandler 能力
   * @return 不可修改的限定大小的物品集合
   */
  public static List<ItemStack> getItemStacks(IItemHandler handler) {
    int slots = handler.getSlots();
    List<ItemStack> arrayList = NonNullList.withSize(slots, ItemStack.EMPTY);
    for (int i = 0; i < slots; i++) {
      arrayList.add(handler.getStackInSlot(i));
    }
    return arrayList;
  }

  /**
   * 移除并返回 IItemHandler 中所有的物品
   * <br>
   * 不包含空物品
   * @param handler IItemHandler 能力
   * @return 不可修改的限定大小的被移除的物品集合
   */
  public static List<ItemStack> clearContent(IItemHandler handler) {
    int slots = handler.getSlots();
    List<ItemStack> arrayList = new ArrayList<>();
    for (int i = 0; i < slots; i++) {
      if (handler.extractItem(i, handler.getSlotLimit(i), true).isEmpty()) {
        continue;
      }
      arrayList.add(handler.extractItem(i, handler.getSlotLimit(i), false));
    }
    return NonNullList.copyOf(arrayList);
  }
}
