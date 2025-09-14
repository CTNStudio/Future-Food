package ctn.futurefood.api.tool;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/**
 * 世界工具类
 *
 * @author 尽
 */
public class LevelTool {
	public static void summonLootItems(ServerLevel serverLevel, BlockPos pos, Item... items) {
		if (items == null) return;
		ItemStack[] stacks = new ItemStack[items.length];
		for (int i = 0; i < items.length; i++) {
			stacks[i] = items[i].getDefaultInstance();
		}
		summonLootItems(serverLevel, pos, stacks);
	}
	
	public static void summonLootItems(ServerLevel serverLevel, BlockPos pos, ItemStack... itemStack) {
		if (itemStack == null) return;
		float x = pos.getX() + 0.5f;
		float y = pos.getY() + 0.5f;
		float z = pos.getZ() + 0.5f;
		summonLootItems(serverLevel, x, y, z, itemStack);
	}
	
	public static void summonLootItems(ServerLevel serverLevel, float x, float y, float z, ItemStack... itemStack) {
		if (itemStack == null) return;
		for (ItemStack stack : itemStack) {
			if (stack == null || stack.isEmpty()) continue;
			ItemEntity itemEntity = new ItemEntity(serverLevel, x, y, z, stack);
			serverLevel.addFreshEntity(itemEntity);
		}
	}
	
	public static void summonLootItems(ServerLevel serverLevel, float x, float y, float z, Item... items) {
		if (items == null) return;
		ItemStack[] stacks = new ItemStack[items.length];
		for (int i = 0; i < items.length; i++) {
			stacks[i] = items[i].getDefaultInstance();
		}
		summonLootItems(serverLevel, x, y, z, stacks);
	}
	
}
