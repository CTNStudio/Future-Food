package top.ctnstudio.futurefood.core.init;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;
import top.ctnstudio.futurefood.common.block.tile.ParticleColliderBlockEntity;
import top.ctnstudio.futurefood.common.block.tile.QedBlockEntity;
import top.ctnstudio.futurefood.common.block.tile.QerBlockEntity;
import top.ctnstudio.futurefood.core.FutureFood;

import java.util.function.Supplier;

public final class ModTileEntity {
  public static final DeferredRegister<BlockEntityType<?>> TILES =
    DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, FutureFood.ID);

  public static final Supplier<BlockEntityType<?>> QED =
    TILES.register("quantum_energy_diffuser", () ->
      BlockEntityType.Builder.of(QedBlockEntity::new, ModBlock.QED.get()).build(null));
  public static final Supplier<BlockEntityType<?>> QER =
    TILES.register("quantum_energy_receiver", () ->
      BlockEntityType.Builder.of(QerBlockEntity::new, ModBlock.QER.get()).build(null));

  public static final Supplier<BlockEntityType<?>> PARTICLE_COLLIDER =
    TILES.register("particle_collider", () -> BlockEntityType.Builder.of(
      ParticleColliderBlockEntity::new, ModBlock.PARTICLE_COLLIDER.get()).build(null));

  private ModTileEntity() {}
}
