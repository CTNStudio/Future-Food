package top.ctnstudio.futurefood.core.init;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;
import top.ctnstudio.futurefood.common.block.ParticleColliderEntityBlock;
import top.ctnstudio.futurefood.common.block.QedEntityBlock;
import top.ctnstudio.futurefood.common.block.QerEntityBlock;
import top.ctnstudio.futurefood.core.FutureFood;

public final class ModBlock {
  public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(FutureFood.ID);

  public static final DeferredBlock<Block> QED =
    BLOCKS.register("quantum_energy_diffuser", QedEntityBlock::new);

  public static final DeferredBlock<Block> QER =
    BLOCKS.register("quantum_energy_receiver", QerEntityBlock::new);

  public static final DeferredBlock<Block> PARTICLE_COLLIDER =
    BLOCKS.register("particle_collider", ParticleColliderEntityBlock::new);

  public static @NotNull BlockBehaviour.StatePredicate never() {
    return (blockState, blockGetter, blockPos) -> false;
  }

  public static @NotNull BlockBehaviour.StateArgumentPredicate<EntityType<?>> argumentNever() {
    return (state, level, pos, value) -> false;
  }
}
