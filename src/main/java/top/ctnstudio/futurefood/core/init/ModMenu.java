package top.ctnstudio.futurefood.core.init;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.registries.DeferredRegister;
import top.ctnstudio.futurefood.client.gui.menu.BasicEnergyMenu;
import top.ctnstudio.futurefood.core.FutureFood;

import java.util.function.Supplier;

public final class ModMenu {
  public static final DeferredRegister<MenuType<?>> MENU_TYPE_REGISTER = DeferredRegister.create(BuiltInRegistries.MENU, FutureFood.ID);

  public static final Supplier<MenuType<BasicEnergyMenu>> Basic_Energy_Menu =
    register("basic_energy_menu", BasicEnergyMenu::new);

  private static <T extends AbstractContainerMenu> Supplier<MenuType<T>> register(String key, MenuType.MenuSupplier<T> factory) {
    return MENU_TYPE_REGISTER.register(key, () -> new MenuType<>(factory, FeatureFlags.VANILLA_SET));
  }
}
