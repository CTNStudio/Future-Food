package top.ctnstudio.futurefood.init;

import club.someoneice.json.Pair;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.ImmutableSortedSet.Builder;
import com.google.common.collect.Maps;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.util.Map;
import java.util.Stack;
import java.util.function.Supplier;

public final class ModItem {
  static final Map<ResourceLocation, Supplier<Item>> data = Maps.newConcurrentMap();


  public static void init(final RegisterEvent event) {
    if (event.getRegistry() != BuiltInRegistries.ITEM) {
      return;
    }

    data.forEach((key, value) -> {
      event.register(Registries.ITEM, key, value);
    });
  }
}
