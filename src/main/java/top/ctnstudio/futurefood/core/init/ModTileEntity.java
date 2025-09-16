package top.ctnstudio.futurefood.core.init;

import club.someoneice.json.Pair;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BlockEntityType.BlockEntitySupplier;
import net.neoforged.neoforge.registries.RegisterEvent;
import top.ctnstudio.futurefood.common.block.tile.QedBlockEntity;
import top.ctnstudio.futurefood.common.block.tile.QerBlockEntity;
import top.ctnstudio.futurefood.core.FutureFood;

import javax.annotation.Nonnull;
import java.util.Stack;
import java.util.function.Supplier;

// TODO - 迁移到新的注册器。
public final class ModTileEntity {
  private static final Stack<Pair<ResourceLocation, Supplier<BlockEntityType<?>>>> data =
    new Stack<>();

  public static final Supplier<BlockEntityType<?>> QED = register("quantum_energy_diffuser",
    QedBlockEntity::new);
  public static final Supplier<BlockEntityType<?>> QER = register("quantum_energy_receiver",
    QerBlockEntity::new);

  private ModTileEntity() {
  }

  @Nonnull
  private static Supplier<BlockEntityType<?>> register(final String name,
                                                       final BlockEntitySupplier<?> blockEntity) {
    final Supplier<BlockEntityType<?>> dat = () -> {
      final var block = BuiltInRegistries.BLOCK.get(FutureFood.modRL(name));
      return BlockEntityType.Builder.of(blockEntity, block).build(null);
    };
    return data.push(new Pair<>(FutureFood.modRL(name), dat)).getValue();
  }

  public static void init(final RegisterEvent event) {
    if (event.getRegistry() != BuiltInRegistries.BLOCK_ENTITY_TYPE) {
      return;
    }

    while (!data.isEmpty()) {
      final var pair = data.pop();
      event.register(Registries.BLOCK_ENTITY_TYPE, pair.getKey(),
        pair.getValue());
    }
  }
}
