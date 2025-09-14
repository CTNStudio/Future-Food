package ctn.futurefood.init;


import ctn.futurefood.FutureFood;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;
import java.util.function.Supplier;

public class FfItems {
	public static final DeferredRegister.Items ITEM_REGISTER = DeferredRegister.createItems(FutureFood.ID);
	
	public static final DeferredItem<BlockItem> QED = registerSimpleBlockItem("quantum_energy_diffuser", FfBlocks.QED);
	public static final DeferredItem<BlockItem> QER = registerSimpleBlockItem("quantum_energy_receiver", FfBlocks.QER);
	
	private static DeferredItem<Item> registerSimpleItem(String name, Item.Properties props) {
		return ITEM_REGISTER.registerSimpleItem(name, props);
	}
	
	private static DeferredItem<BlockItem> registerSimpleBlockItem(String name, Supplier<? extends Block> block) {
		return ITEM_REGISTER.registerSimpleBlockItem(name, block);
	}
	
	private static DeferredItem<Item> registerSimpleIconItem(String name) {
		return ITEM_REGISTER.registerSimpleItem(name, new Item.Properties().stacksTo(1));
	}
	
	private static DeferredItem<Item> registerToolItem(String name) {
		return ITEM_REGISTER.registerSimpleItem(name, new Item.Properties().stacksTo(1));
	}
	
	private static DeferredItem<Item> registerToolItem(String name, Function<Item.Properties, ? extends Item> item) {
		return ITEM_REGISTER.registerItem(name, item, new Item.Properties().stacksTo(1));
	}
	
	private static DeferredItem<Item> registerItem(String name, Function<Item.Properties, ? extends Item> item, Item.Properties properties) {
		return ITEM_REGISTER.registerItem(name, item, properties.stacksTo(1));
	}
	
	private static DeferredItem<Item> registerItem(String name, Function<Item.Properties, ? extends Item> item) {
		return ITEM_REGISTER.registerItem(name, item, new Item.Properties().stacksTo(1));
	}
	
	private static DeferredItem<Item> registerFood(String name, FoodProperties foodProperties) {
		return ITEM_REGISTER.registerItem(name, Item::new, new Item.Properties().food(foodProperties));
	}
}
