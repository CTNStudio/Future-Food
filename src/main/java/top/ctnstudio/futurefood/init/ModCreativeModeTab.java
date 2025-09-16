package top.ctnstudio.futurefood.init;

import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.RegisterEvent;
import top.ctnstudio.futurefood.FutureFood;

import java.util.function.Supplier;

public final class ModCreativeModeTab {

  public static void init(final RegisterEvent event) {
    if (event.getRegistry() != BuiltInRegistries.CREATIVE_MODE_TAB) {
      return;
    }

    if (ModItem.data.isEmpty()) {
      return;
    }

    event.register(Registries.CREATIVE_MODE_TAB, FutureFood.modRL("futurefood"), () ->
      CreativeModeTab.builder()
        .icon(() -> ModBlock.QED.get().asItem().getDefaultInstance())
        .title(Component.translatable("itemGroup." + FutureFood.ID))
        .displayItems((par, output) ->
          ModItem.data.values().stream()
            .map(Supplier::get)
            .forEach(output::accept))
        .build());
  }
}
