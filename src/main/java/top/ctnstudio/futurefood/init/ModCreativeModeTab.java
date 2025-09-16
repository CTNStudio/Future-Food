package top.ctnstudio.futurefood.init;

import club.someoneice.json.Pair;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.RegisterEvent;
import top.ctnstudio.futurefood.FutureFood;

import java.util.Stack;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public final class ModCreativeModeTab {
  private static final Stack<Pair<ResourceLocation, Supplier<CreativeModeTab>>> data = new Stack<>();

  public static final Supplier<CreativeModeTab> FUTURE_FOOD = register("futurefood",
    () -> ModItem.QED.get().getDefaultInstance(),
    (name, icon) -> registerCreativeModeTab(
      name, (parameters, output) -> {
        output.accept(ModItem.QED.get());
        output.accept(ModItem.QER.get());
      }, icon));

  private static Supplier<CreativeModeTab> register(String name, Supplier<ItemStack> icon,
    BiFunction<String, Supplier<ItemStack>, CreativeModeTab.Builder> builder) {
    return data.push(new Pair<>(FutureFood.modRL(name), builder.apply(name, icon)::build)).getValue();
  }

  private static CreativeModeTab.Builder registerCreativeModeTab(
    String name,
    CreativeModeTab.DisplayItemsGenerator displayItemsGenerator,
    Supplier<ItemStack> icon,
    ResourceKey<CreativeModeTab> withTabsBefore) {
    return registerCreativeModeTab(name, displayItemsGenerator, icon).withTabsBefore(withTabsBefore);
  }

  private static CreativeModeTab.Builder registerCreativeModeTab(
    String name,
    CreativeModeTab.DisplayItemsGenerator displayItemsGenerator,
    Supplier<ItemStack> icon) {
    return registerCreativeModeTab(name, displayItemsGenerator).icon(icon);
  }

  private static CreativeModeTab.Builder registerCreativeModeTab(String name, CreativeModeTab.DisplayItemsGenerator displayItemsGenerator) {
    return CreativeModeTab.builder().title(getComponent(name)).displayItems(displayItemsGenerator);
  }

  private static MutableComponent getComponent(String imagePath) {
    return Component.translatable("itemGroup." + FutureFood.ID + "." + imagePath);
  }

  public static void init(final RegisterEvent event) {
    if (event.getRegistry() != BuiltInRegistries.CREATIVE_MODE_TAB) {
      return;
    }

    final var pair = data.pop();
    event.register(Registries.CREATIVE_MODE_TAB, pair.getKey(), pair.getValue());
  }
}
