package top.ctnstudio.futurefood.core.init;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.apache.logging.log4j.util.Lazy;
import org.jetbrains.annotations.NotNull;
import top.ctnstudio.futurefood.common.block.ParticleColliderEntityBlock;
import top.ctnstudio.futurefood.common.block.QedEntityBlock;
import top.ctnstudio.futurefood.common.block.QerEntityBlock;

import java.util.function.Supplier;

public final class ModBlock extends AbstractObjectRegister<Block> {
  public static final ModBlock INSTANCE = new ModBlock();

  public static final Supplier<Block> QED =
    INSTANCE.register("quantum_energy_diffuser", QedEntityBlock::new);
  public static final Supplier<Block> QER =
    INSTANCE.register("quantum_energy_receiver", QerEntityBlock::new);
  public static final Supplier<Block> PARTICLE_COLLIDER =
    INSTANCE.register("particle_collider", ParticleColliderEntityBlock::new);

  private ModBlock() {
    super(BuiltInRegistries.BLOCK, Registries.BLOCK);
  }

  public static @NotNull BlockBehaviour.StatePredicate never() {
    return (blockState, blockGetter, blockPos) -> false;
  }

  public static @NotNull BlockBehaviour.StateArgumentPredicate<EntityType<?>> argumentNever() {
    return (state, level, pos, value) -> false;
  }

  @Override
  public void afterRegister(ResourceLocation registerName, Lazy<Block> lazy) {
    ModItem.INSTANCE.register(registerName, () ->
      new BlockItem(lazy.get(), new Item.Properties()));
  }
}
