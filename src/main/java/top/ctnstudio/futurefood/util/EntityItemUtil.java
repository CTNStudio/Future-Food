package top.ctnstudio.futurefood.util;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Arrays;

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
    summonLootItems(serverLevel, pos, stacks);
  }

  public static void summonLootItems(ServerLevel serverLevel, float x, float y, float z, Item... items) {
    ItemStack[] stacks = Arrays.stream(items)
      .map(ItemStack::new)
      .filter(stack -> !stack.isEmpty())
      .toArray(ItemStack[]::new);
    summonLootItems(serverLevel, x, y, z, stacks);
  }

  public static void summonLootItems(ServerLevel serverLevel, BlockPos pos, ItemStack... itemStack) {
    float x = pos.getX() + 0.5f;
    float y = pos.getY() + 0.5f;
    float z = pos.getZ() + 0.5f;
    summonLootItems(serverLevel, x, y, z, itemStack);
  }

  public static void summonLootItems(ServerLevel serverLevel, float x, float y, float z, ItemStack... itemStack) {
    Arrays.stream(itemStack)
      .filter(stack -> !stack.isEmpty())
      .map(it -> new ItemEntity(serverLevel, x, y, z, it))
      .forEach(serverLevel::addFreshEntity);
  }
}
