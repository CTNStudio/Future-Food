package top.ctnstudio.futurefood.core.init;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import top.ctnstudio.futurefood.core.FutureFood;

import java.util.function.Supplier;

public final class ModCreativeModeTab {
  public static final DeferredRegister<CreativeModeTab> TABS =
    DeferredRegister.create(BuiltInRegistries.CREATIVE_MODE_TAB, FutureFood.ID);

  public static final DeferredHolder<CreativeModeTab, CreativeModeTab> TAB =
    TABS.register("futurefood_tab", () -> CreativeModeTab.builder()
      .icon(() -> ModItem.QED.get().getDefaultInstance())
      .title(Component.translatable("itemGroup.futurefood"))
      .displayItems((pr, out) -> ModItem.ITEMS.getEntries().stream()
        .map(Supplier::get)
        .forEach(out::accept))
      .build()
    );
}
