package top.ctnstudio.futurefood.core.init;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.neoforge.registries.RegisterEvent;
import top.ctnstudio.futurefood.core.FutureFood;

import java.util.function.Supplier;

// TODO - 迁移到新的注册器。
public final class ModCreativeModeTab {

  public static void init(final RegisterEvent event) {
    if (event.getRegistry() != BuiltInRegistries.CREATIVE_MODE_TAB) {
      return;
    }

    final var objects = ModItem.INSTANCE.copyObjects();

    if (objects.isEmpty()) {
      return;
    }

    event.register(Registries.CREATIVE_MODE_TAB, FutureFood.modRL("futurefood"), () ->
      CreativeModeTab.builder()
        .icon(() -> ModBlock.QED.get().asItem().getDefaultInstance())
        .title(Component.translatable("itemGroup." + FutureFood.ID))
        .displayItems((par, output) ->
          objects.values().stream()
            .map(Supplier::get)
            .forEach(output::accept))
        .build());
  }
}
