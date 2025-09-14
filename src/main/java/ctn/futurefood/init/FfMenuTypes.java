package ctn.futurefood.init;

import ctn.futurefood.FutureFood;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * 菜单类型
 */
public class FfMenuTypes {
	public static final DeferredRegister<MenuType<?>> MENU_TYPE_REGISTER = DeferredRegister.create(BuiltInRegistries.MENU, FutureFood.ID);
	
	private static <T extends AbstractContainerMenu> Supplier<MenuType<T>> register(String key, MenuType.MenuSupplier<T> factory) {
		return MENU_TYPE_REGISTER.register(key, () -> new MenuType<>(factory, FeatureFlags.VANILLA_SET));
	}
}
