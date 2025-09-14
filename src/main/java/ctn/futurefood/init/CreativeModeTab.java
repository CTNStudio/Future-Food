package ctn.futurefood.init;

import ctn.ctntemplate.CtnTemplate;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 创造模式物品栏
 */
public class CreativeModeTab extends CreativeModeTabs {
	public static final DeferredRegister<net.minecraft.world.item.CreativeModeTab> PROJECT_MOON_TAB_REGISTER = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CtnTemplate.ID);
//	public static final DeferredHolder<CreativeModeTab, CreativeModeTab> EGO_WEAPON                =
//			register(
//					"ego_weapon", (name) -> registerCreativeModeTab(
//							name,
//							(parameters, output) -> {
//								output.accept(PmItems.DETONATING_BATON.get());
//								output.accept(PmItems.WRIST_CUTTER.get());
//								output.accept(PmItems.BEAR_PAWS.get());
//								output.accept(PmItems.LOVE_HATE.get());
//								output.accept(PmItems.PARADISE_LOST.get());
//								output.accept(PmItems.MAGIC_BULLET.get());
//							}, () -> PmItems.EGO_WEAPON_ICON.get().getDefaultInstance()));
	
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
	
	private static @NotNull MutableComponent getComponent(String imagePath) {
		return Component.translatable("itemGroup." + CtnTemplate.ID + "." + imagePath);
	}
}
