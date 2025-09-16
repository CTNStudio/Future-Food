package top.ctnstudio.futurefood.core.init;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BlockEntityType.BlockEntitySupplier;
import top.ctnstudio.futurefood.common.block.tile.QedBlockEntity;
import top.ctnstudio.futurefood.common.block.tile.QerBlockEntity;
import top.ctnstudio.futurefood.core.FutureFood;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public final class ModTileEntity extends AbstractObjectRegister<BlockEntityType<?>> {
  public static final ModTileEntity INSTANCE = new ModTileEntity();

  public static final Supplier<BlockEntityType<?>> QED = register("quantum_energy_diffuser",
    QedBlockEntity::new);
  public static final Supplier<BlockEntityType<?>> QER = register("quantum_energy_receiver",
    QerBlockEntity::new);

  private ModTileEntity() {
    super(BuiltInRegistries.BLOCK_ENTITY_TYPE, Registries.BLOCK_ENTITY_TYPE);
  }

  @Nonnull
  private static Supplier<BlockEntityType<?>> register(final String name,
                                                       final BlockEntitySupplier<?> blockEntity) {
    final Supplier<BlockEntityType<?>> dat = () -> {
      final var block = BuiltInRegistries.BLOCK.get(FutureFood.modRL(name));
      return BlockEntityType.Builder.of(blockEntity, block).build(null);
    };
    return INSTANCE.register(FutureFood.modRL(name), dat);
  }
}
