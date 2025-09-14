package ctn.futurefood.init;


import ctn.ctntemplate.CtnTemplate;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;
import java.util.function.Supplier;

import static ctn.ctntemplate.builder.FoodPropertiesBuilder.foodBuilder;

public class Items {
	public static final DeferredRegister.Items ITEM_REGISTER = DeferredRegister.createItems(CtnTemplate.ID);
//	public static final DeferredItem<Item>     EGO_CURIOS_ICON    = registerSimpleIconItem("ego_curios_icon");
	
	public static final DeferredItem<Item> STONE_APPLE       = registerFood("stone_apple", foodBuilder().nutrition(6).saturation(5).eatSeconds(2.1f).build());
	public static final DeferredItem<Item> STONE_BREAD       = registerFood("stone_bread", foodBuilder().nutrition(7).saturation(7).eatSeconds(2.1f).build());
	public static final DeferredItem<Item> STONE_CARROT      = registerFood("stone_carrot", foodBuilder().nutrition(5).saturation(4).eatSeconds(2.1f).build());
	public static final DeferredItem<Item> STONE_KELP        = registerFood("stone_kelp", foodBuilder().nutrition(3).saturation(1.6f).eatSeconds(1.9f).build());
	public static final DeferredItem<Item> STONE_MELON_SLICE = registerFood("stone_melon_slice", foodBuilder().nutrition(4).saturation(2.2f).eatSeconds(2.1f).build());
	public static final DeferredItem<Item> STONE_POTATO      = registerFood("stone_potato", foodBuilder().nutrition(3).saturation(1.6f).eatSeconds(2.1f).build());
	public static final DeferredItem<Item> STONE_HODGEPODGE  = registerFood("stone_hodgepodge", foodBuilder()
			.nutrition(20).saturation(10).eatSeconds(3f)
			.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 100, 1))
			.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 100, 1))
			.addEffect(new MobEffectInstance(MobEffects.SATURATION, 200, 1))
			.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 200, 0))
			.usingConvertsTo(net.minecraft.world.item.Items.BOWL)
			.build()
	);
	
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
