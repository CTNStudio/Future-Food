package top.ctnstudio.futurefood.util;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import top.ctnstudio.futurefood.core.FutureFood;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Summon the entity of item into the server world.
 *
 * @author 尽
 */
public class EntityItemUtil {
  public static void summonLootItems(ServerLevel serverLevel, BlockPos pos, Item... items) {
    ItemStack[] stacks = Arrays.stream(items)
      .map(ItemStack::new)
      .filter(stack -> !stack.isEmpty())
      .toArray(ItemStack[]::new);
    summonLootItemStacks(serverLevel, pos, stacks);
  }

  public static void summonLootItems(ServerLevel serverLevel, float x, float y, float z, Item... items) {
    ItemStack[] stacks = Arrays.stream(items)
      .map(ItemStack::new)
      .filter(stack -> !stack.isEmpty())
      .toArray(ItemStack[]::new);
    summonLootItemStacks(serverLevel, x, y, z, stacks);
  }

  public static void summonLootItems(ServerLevel serverLevel, BlockPos pos, List<Item> items) {
    summonLootItems(serverLevel, pos, items.toArray(Item[]::new));
  }

  public static void summonLootItems(ServerLevel serverLevel, float x, float y, float z, List<Item> items) {
    summonLootItems(serverLevel, x, y, z, items.toArray(Item[]::new));
  }

  public static void summonLootItemStacks(ServerLevel serverLevel, BlockPos pos, ItemStack... itemStack) {
    float x = pos.getX() + 0.5f;
    float y = pos.getY() + 0.5f;
    float z = pos.getZ() + 0.5f;
    summonLootItemStacks(serverLevel, x, y, z, itemStack);
  }

  public static void summonLootItemStacks(ServerLevel serverLevel, float x, float y, float z, ItemStack... itemStack) {
    if (itemStack.length == 0) return;

    try {
      // 过滤掉空的 ItemStack 并创建副本以防外部变更
      List<ItemStack> validStacks = Arrays.stream(itemStack)
        .filter(stack -> stack != null && !stack.isEmpty())
        .map(ItemStack::copy) // 防御性拷贝
        .collect(Collectors.toList());

      if (validStacks.isEmpty()) return;

      // 缓存名字字符串避免多次调用 getName
      Map<ItemStack, String> nameCache = new HashMap<>();
      for (ItemStack stack : validStacks) {
        nameCache.putIfAbsent(stack, stack.getItem().getName(stack).getString());
      }

      // 排序：优先按物品类型再按名称排序
      validStacks.sort((a, b) -> {
        if (ItemStack.isSameItemSameComponents(a, b)) return 0;
        String nameA = nameCache.get(a);
        String nameB = nameCache.get(b);
        return nameA.compareTo(nameB);
      });

      // 合并相同物品（基于 isSameItemSameComponents）
      Map<String, Integer> itemCountMap = new LinkedHashMap<>();
      Map<String, ItemStack> templateMap = new HashMap<>();

      for (ItemStack current : validStacks) {
        String key = getItemKey(current); // 构造唯一标识符
        templateMap.putIfAbsent(key, current);
        itemCountMap.merge(key, current.getCount(), Integer::sum);
      }

      // 构造最终的 ItemStack 列表并拆分成最大堆叠大小限制内的多个项
      List<ItemStack> mergedStacks = new ArrayList<>();
      for (Map.Entry<String, Integer> entry : itemCountMap.entrySet()) {
        ItemStack template = templateMap.get(entry.getKey());
        int totalCount = entry.getValue();
        int maxStackSize = template.getMaxStackSize();

        while (totalCount > 0) {
          int countToTake = Math.min(totalCount, maxStackSize);
          ItemStack newItem = template.copy();
          newItem.setCount(countToTake);
          mergedStacks.add(newItem);
          totalCount -= countToTake;
        }
      }

      // 创建并添加实体
      mergedStacks.stream()
        .map(stack -> new ItemEntity(serverLevel, x, y, z, stack))
        .forEach(serverLevel::addFreshEntity);

    } catch (Exception e) {
      FutureFood.LOGGER.error("Error occurred in summonLootItemStacks: {}", e.getMessage());
    }
  }

  public static void summonLootItemStacks(ServerLevel serverLevel, BlockPos pos, List<ItemStack> itemStacks) {
    summonLootItemStacks(serverLevel, pos, itemStacks.toArray(ItemStack[]::new));
  }

  public static void summonLootItemStacks(ServerLevel serverLevel, float x, float y, float z, List<ItemStack> itemStacks) {
    summonLootItemStacks(serverLevel, x, y, z, itemStacks.toArray(ItemStack[]::new));
  }

  private static String getItemKey(ItemStack stack) {
    return stack.getItem() + "|" + stack.getComponents();
  }
}
