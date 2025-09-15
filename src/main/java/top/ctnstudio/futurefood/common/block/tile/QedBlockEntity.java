package top.ctnstudio.futurefood.common.block.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.logging.log4j.util.Lazy;
import top.ctnstudio.futurefood.FutureFood;

public class QedBlockEntity extends BlockEntity {
  private static final Lazy<BlockEntityType<?>> lazyType = Lazy.lazy(() ->
    FutureFood.getModObject(BuiltInRegistries.BLOCK_ENTITY_TYPE,
      "quantum_energy_diffuser_block_entity"));

  public QedBlockEntity(BlockPos pos, BlockState blockState) {
    super(lazyType.get(), pos, blockState);}
}
