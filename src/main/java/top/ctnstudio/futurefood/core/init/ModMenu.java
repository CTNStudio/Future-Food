package top.ctnstudio.futurefood.core.init;

import com.mojang.datafixers.util.Function3;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.network.IContainerFactory;
import net.neoforged.neoforge.registries.DeferredRegister;
import top.ctnstudio.futurefood.client.gui.screen.EnergyScreen;
import top.ctnstudio.futurefood.client.gui.screen.ParticleColliderScreen;
import top.ctnstudio.futurefood.common.menu.EnergyMenu;
import top.ctnstudio.futurefood.common.menu.ParticleColliderMenu;
import top.ctnstudio.futurefood.core.FutureFood;

import java.util.function.Supplier;

@EventBusSubscriber(modid = FutureFood.ID)
public final class ModMenu {
  public static final DeferredRegister<MenuType<?>> MENU = DeferredRegister.create(BuiltInRegistries.MENU, FutureFood.ID);

  public static final Supplier<MenuType<EnergyMenu>> ENERGY_MENU =
    MENU.register("energy_menu", () -> IMenuTypeExtension.create(EnergyMenu::new));
  public static final Supplier<MenuType<ParticleColliderMenu>> PARTICLE_COLLIDER_MENU =
    MENU.register("particle_collider_menu", () -> IMenuTypeExtension.create(ParticleColliderMenu::new));

  @SubscribeEvent
  public static void registerMenuScreens(RegisterMenuScreensEvent event) {
    FutureFood.LOGGER.info("Registering Menu Screens");

    registerScreen(event, ModMenu.PARTICLE_COLLIDER_MENU.get(), ParticleColliderScreen::new);
    registerScreen(event, ModMenu.ENERGY_MENU.get(), EnergyScreen::new);

    FutureFood.LOGGER.info("Registering Menu Screens Completed");
  }

  private static <T extends AbstractContainerMenu> Supplier<MenuType<T>> register(String key, IContainerFactory<T> factory) {
    return MENU.register(key, () -> IMenuTypeExtension.create(factory));
  }

  private static <M extends AbstractContainerMenu, E extends Screen & MenuAccess<M>> void registerScreen(
    RegisterMenuScreensEvent event,
    MenuType<M> menuType, Function3<M, Inventory, Component, E> screenConstructor) {
    event.register(menuType, screenConstructor::apply);
  }
}
