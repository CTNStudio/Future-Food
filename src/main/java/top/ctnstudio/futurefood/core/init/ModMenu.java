package top.ctnstudio.futurefood.core.init;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.network.IContainerFactory;
import net.neoforged.neoforge.registries.DeferredRegister;
import top.ctnstudio.futurefood.common.menu.EnergyMenu;
import top.ctnstudio.futurefood.core.FutureFood;

import java.util.function.Supplier;

public final class ModMenu {
  public static final DeferredRegister<MenuType<?>> MENU =
    DeferredRegister.create(BuiltInRegistries.MENU, FutureFood.ID);

  public static final Supplier<MenuType<EnergyMenu>> ENERGY_MENU =
    MENU.register("energy_menu", () -> IMenuTypeExtension.create(EnergyMenu::new));

  private static <T extends AbstractContainerMenu>
  Supplier<MenuType<T>> register(String key, IContainerFactory<T> factory) {
    return MENU.register(key, () -> IMenuTypeExtension.create(factory));
  }
}
