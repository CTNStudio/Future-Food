package ctn.futurefood.init;

import ctn.futurefood.FutureFood;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 创造模式物品栏
 */
public class FfCreativeModeTab extends CreativeModeTabs {
	public static final DeferredRegister<net.minecraft.world.item.CreativeModeTab>                                         PROJECT_MOON_TAB_REGISTER = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, FutureFood.ID);
	public static final DeferredHolder<net.minecraft.world.item.CreativeModeTab, net.minecraft.world.item.CreativeModeTab> FUTURE_FOOD =
			register("futurefood", (name) -> registerCreativeModeTab(
					name, (parameters, output) -> {
						output.accept(FfItems.QED.get());
						output.accept(FfItems.QER.get());
					}, () -> FfItems.QED.get().getDefaultInstance()));
	
	private static DeferredHolder<net.minecraft.world.item.CreativeModeTab, net.minecraft.world.item.CreativeModeTab> register(String name, Function<String, net.minecraft.world.item.CreativeModeTab.Builder> builder) {
		return PROJECT_MOON_TAB_REGISTER.register(name, builder.apply(name)::build);
	}
	
	private static net.minecraft.world.item.CreativeModeTab.Builder registerCreativeModeTab(
			String name,
			net.minecraft.world.item.CreativeModeTab.DisplayItemsGenerator displayItemsGenerator,
			Supplier<ItemStack> icon,
			ResourceKey<net.minecraft.world.item.CreativeModeTab> withTabsBefore) {
		return registerCreativeModeTab(name, displayItemsGenerator, icon).withTabsBefore(withTabsBefore);
	}
	
	private static net.minecraft.world.item.CreativeModeTab.Builder registerCreativeModeTab(
			String name,
			net.minecraft.world.item.CreativeModeTab.DisplayItemsGenerator displayItemsGenerator,
			Supplier<ItemStack> icon) {
		return registerCreativeModeTab(name, displayItemsGenerator).icon(icon);
	}
	
	private static net.minecraft.world.item.CreativeModeTab.Builder registerCreativeModeTab(String name, net.minecraft.world.item.CreativeModeTab.DisplayItemsGenerator displayItemsGenerator) {
		return net.minecraft.world.item.CreativeModeTab.builder().title(getComponent(name)).displayItems(displayItemsGenerator);
	}
	
	private static MutableComponent getComponent(String imagePath) {
		return Component.translatable("itemGroup." + FutureFood.ID + "." + imagePath);
	}
}
