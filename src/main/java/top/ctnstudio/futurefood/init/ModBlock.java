package top.ctnstudio.futurefood.init;

import club.someoneice.json.Pair;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.RegisterEvent;
import top.ctnstudio.futurefood.FutureFood;
import top.ctnstudio.futurefood.common.block.QedEntityBlock;
import top.ctnstudio.futurefood.common.block.QerEntityBlock;
import top.ctnstudio.futurefood.common.item.ItemBlockBase;

import javax.annotation.Nonnull;
import java.util.Stack;
import java.util.function.Supplier;

public final class ModBlock {
  private static final Stack<Pair<ResourceLocation, Supplier<Block>>> data = new Stack<>();
  public static final Supplier<Block> QED =
    registerBlock("quantum_energy_diffuser", QedEntityBlock::new);
  public static final Supplier<Block> QER =
    registerBlock("quantum_energy_receiver", QerEntityBlock::new);

  private ModBlock() {
  }

  @Nonnull
  private static Supplier<Block> registerBlock(String name, Supplier<Block> block) {
    return data.push(new Pair<>(FutureFood.modRL(name), block)).getValue();
  }

  public static void init(final RegisterEvent event) {
    if (event.getRegistry() != BuiltInRegistries.BLOCK) {
      return;
    }

    while (!data.isEmpty()) {
      final var pair = data.pop();
      event.register(Registries.BLOCK, pair.getKey(), pair.getValue());

      ModItem.data.put(pair.getKey(), () -> new ItemBlockBase(pair.getKey()));
    }
  }
}
