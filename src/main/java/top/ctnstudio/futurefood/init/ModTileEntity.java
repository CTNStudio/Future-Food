package top.ctnstudio.futurefood.init;

import club.someoneice.json.Pair;
import com.google.common.base.Suppliers;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.RegisterEvent;
import top.ctnstudio.futurefood.FutureFood;
import top.ctnstudio.futurefood.common.block.tile.QedBlockEntity;
import top.ctnstudio.futurefood.common.block.tile.QerBlockEntity;

import java.util.Objects;
import java.util.Stack;
import java.util.function.Supplier;

import static top.ctnstudio.futurefood.init.ModBlock.QED;
import static top.ctnstudio.futurefood.init.ModBlock.QER;

public final class ModTileEntity {
  private static final Stack<Pair<ResourceLocation, BlockEntityType<?>>> data = new Stack<>();

  static {
    register("quantum_energy_diffuser_block_entity", QedBlockEntity::new, QED);
    register("quantum_energy_receiver_block_entity", QerBlockEntity::new, QER);
  }

  private ModTileEntity() {
  }

  public static void init(final RegisterEvent event) {
    if (event.getRegistry() != BuiltInRegistries.BLOCK_ENTITY_TYPE) {
      return;
    }

    while (!data.isEmpty()) {
      final Pair<ResourceLocation, BlockEntityType<?>> pair = data.pop();
      event.register(Registries.BLOCK_ENTITY_TYPE, pair.getKey(),
        Suppliers.ofInstance(pair.getValue()));
    }
  }

  private static void register(String name,
                               BlockEntityType.BlockEntitySupplier<?> blockEntity,
                               Supplier<Block> blocks) {
    final var pair = BlockEntityType.Builder.of(blockEntity, blocks.get()).build(null);
    Objects.requireNonNull(pair);
    data.push(new Pair<>(FutureFood.modRL(name), pair));
  }
}
