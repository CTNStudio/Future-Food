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
import top.ctnstudio.futurefood.client.gui.screen.*;
import top.ctnstudio.futurefood.common.menu.*;
import top.ctnstudio.futurefood.core.FutureFood;

import java.util.function.Supplier;

@EventBusSubscriber(modid = FutureFood.ID)
public final class ModMenu {
  public static final DeferredRegister<MenuType<?>> MENU = DeferredRegister.create(BuiltInRegistries.MENU, FutureFood.ID);
  public static final Supplier<MenuType<EnergyMenu>> ENERGY_MENU = register(
    "energy_menu", EnergyMenu::new);
  public static final Supplier<MenuType<InputEnergyMenu>> INPUT_ENERGY_MENU = register(
    "input_energy_menu", InputEnergyMenu::new);
  public static final Supplier<MenuType<OutputEnergyMenu>> OUTPUT_ENERGY_MENU = register(
    "output_energy_menu", OutputEnergyMenu::new);
  public static final Supplier<MenuType<InputOutputEnergyMenu>> INPUT_OUTPUT_ENERGY_MENU = register(
    "input_output_energy_menu", InputOutputEnergyMenu::new);
  public static final Supplier<MenuType<ParticleColliderMenu>> PARTICLE_COLLIDER_MENU = register(
    "particle_collider_menu", ParticleColliderMenu::new);
  public static final Supplier<MenuType<GluttonyMenu>> GLUTTONY_MENU = register(
    "gluttony_menu", GluttonyMenu::new);

  @SubscribeEvent
  public static void registerMenuScreens(RegisterMenuScreensEvent event) {
    FutureFood.LOGGER.info("Registering Menu Screens");

    registerScreen(event, ModMenu.PARTICLE_COLLIDER_MENU.get(), ParticleColliderScreen::new);
    registerScreen(event, ModMenu.GLUTTONY_MENU.get(), GluttonyScreen::new);
    registerScreen(event, ModMenu.ENERGY_MENU.get(), EnergyScreen::new);
    registerScreen(event, ModMenu.INPUT_ENERGY_MENU.get(), InputEnergyScreen::new);
    registerScreen(event, ModMenu.OUTPUT_ENERGY_MENU.get(), OutputEnergyScreen::new);
    registerScreen(event, ModMenu.INPUT_OUTPUT_ENERGY_MENU.get(), InputOutputEnergyScreen::new);

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
