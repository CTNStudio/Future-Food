package top.ctnstudio.futurefood.init;


import club.someoneice.json.Pair;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.RegisterEvent;
import top.ctnstudio.futurefood.FutureFood;

import java.util.Stack;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public final class ModItem {
  private static final Stack<Pair<ResourceLocation, Supplier<Item>>> data = new Stack<>();

  public static final Supplier<BlockItem> QED = registerBlockItem("quantum_energy_diffuser", ModBlock.QED);
  public static final Supplier<BlockItem> QER = registerBlockItem("quantum_energy_receiver", ModBlock.QER);

  private static Supplier<BlockItem> registerBlockItem(String name, Supplier<Block> block) {
    return registerBlockItem(name, block, new Item.Properties());
  }

  private static Supplier<BlockItem> registerBlockItem(String name, Supplier<Block> block, Item.Properties properties) {
    return (Supplier<BlockItem>) registerItem(name, () -> new BlockItem(block.get(), properties));
  }

  private static Supplier<? extends Item> registerItem(String name, Supplier<Item> block) {
    return data.push(new Pair<>(FutureFood.modRL(name), block)).getValue();
  }

  public static void init(final RegisterEvent event) {
    if (event.getRegistry() != BuiltInRegistries.ITEM) {
      return;
    }

    while (!data.isEmpty()) {
      final var pair = data.pop();
      event.register(Registries.ITEM, pair.getKey(), pair.getValue());
    }
  }
}
