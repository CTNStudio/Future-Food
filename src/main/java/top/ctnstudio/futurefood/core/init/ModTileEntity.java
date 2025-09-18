package top.ctnstudio.futurefood.core.init;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import top.ctnstudio.futurefood.common.block.tile.ParticleColliderBlockEntity;
import top.ctnstudio.futurefood.common.block.tile.QedBlockEntity;
import top.ctnstudio.futurefood.common.block.tile.QerBlockEntity;
import top.ctnstudio.futurefood.core.FutureFood;

import java.util.Arrays;
import java.util.function.Supplier;

public final class ModTileEntity {
  public static final DeferredRegister<BlockEntityType<?>> TILES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, FutureFood.ID);

  public static final Supplier<BlockEntityType<QedBlockEntity>> QED = register("quantum_energy_diffuser", QedBlockEntity::new, ModBlock.QED);
  public static final Supplier<BlockEntityType<QerBlockEntity>> QER = register("quantum_energy_receiver", QerBlockEntity::new, ModBlock.QER);
  public static final Supplier<BlockEntityType<ParticleColliderBlockEntity>> PARTICLE_COLLIDER = register("particle_collider", ParticleColliderBlockEntity::new, ModBlock.PARTICLE_COLLIDER);

  @SafeVarargs
  private static <T extends BlockEntity> DeferredHolder<BlockEntityType<?>, BlockEntityType<T>> register(String name,
    BlockEntityType.BlockEntitySupplier<T> factory, Supplier<Block>... validBlocks) {
    return TILES.register(name, () -> BlockEntityType.Builder.of(factory, Arrays.stream(validBlocks).map(Supplier::get).toList().toArray(new Block[0])).build(null));
  }
}
